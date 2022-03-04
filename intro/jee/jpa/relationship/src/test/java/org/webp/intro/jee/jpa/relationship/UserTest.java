package org.webp.intro.jee.jpa.relationship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


public class UserTest {

    private EntityManagerFactory factory;
    private EntityManager em;

    @BeforeEach
    public void init() {
        factory = Persistence.createEntityManagerFactory("DB");
        em = factory.createEntityManager();
    }

    @AfterEach
    public void tearDown() {
        em.close();
        factory.close();
    }

    private boolean persistInATransaction(Object... obj) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            for(Object o : obj) {
                em.persist(o);
            }
            tx.commit();
        } catch (Exception e) {
            System.out.println("FAILED TRANSACTION: " + e.toString());
            tx.rollback();
            return false;
        }

        return true;
    }



    @Test
    public void testEmptyUser(){

        User user = new User();
        assertTrue(persistInATransaction(user));
    }


    @Test
    public void testUserWithAddress(){

        Address address = new Address();
        AddressWithUserLink addressWithUserLink = new AddressWithUserLink();

        //Bir entity dogrudan veritabanina kaydedilebilir
        assertTrue(persistInATransaction(address));
        assertTrue(persistInATransaction(addressWithUserLink));

        //henuz user tanimlanmadigi icin null olmalidir
        assertNull(addressWithUserLink.getUser());

        User user = new User();
        user.setAddress(address);
        user.setAddressWithUserLink(addressWithUserLink);
        addressWithUserLink.setUser(user);

        assertTrue(persistInATransaction(user));
    }


    @Test
    public void testMessagesFail(){

        User user = new User();

        //liste baslatilmadi
        assertNull(user.getSentMessages());

        assertTrue(persistInATransaction(user));

        //DB'ye yazildiktan sonra bile liste baslatilmadi
        assertNull(user.getSentMessages());


        User anotherUser = new User();

        Message msg = new Message();
        anotherUser.setSentMessages(new ArrayList<>());
        anotherUser.getSentMessages().add(msg);

       /*
            Bu hata verecektir cunku sentMessages'daki sentMessages objesi kaydedilmektedir.
            persistInATransaction(anotherUser,msg) olsaydi hata vermezdi
       */

        assertFalse(persistInATransaction(anotherUser));
    }


    @Test
    public void testMessagesPassByResettingId(){

        User user = new User();

        Message msg = new Message();
        user.setSentMessages(new ArrayList<>());
        user.getSentMessages().add(msg);

        assertNull(user.getId());
        assertFalse(persistInATransaction(user));

        /*
            Dikkat: Bir onceki transaction hata verse ve geriye alinsa da JVM icerisindeki user objesi'nin degeri
            degistirilmistir (or: artik bir ID'ye sahiptir).

         */
        assertNotNull(user.getId());

        //Hata verecektir cunku "User" entity'sinin @Id != null'dur ancak veritabaninda bu degere sahip bir kayit
        // bulunmamaktadir
        assertFalse(persistInATransaction(msg, user));

        msg.setId(null);
        user.setId(null);


        //Ayni transaction icerisinde olduklari icin sira onemli degildir
        assertTrue(persistInATransaction(msg, user));
    }


    @Test
    public void testMessagesPass(){

        User user = new User();

        Message msg = new Message();
        user.setSentMessages(new ArrayList<>());
        user.getSentMessages().add(msg);

        assertTrue(persistInATransaction(user, msg));
    }


    @Test
    public void testMessagesWithUserLink(){

        User user = new User();

        MessageWithUserLink msg = new MessageWithUserLink();
        msg.setSender(user);
        user.setSentMessagesWithSenderLink(new ArrayList<>());
        user.getSentMessagesWithSenderLink().add(msg);

        /*
            Not: Burada "msg"'i ayni transaction icerisinde kaydetmeye gerek yoktur cunku
             sentMessagesWithSenderLink bu isi yapar
         */
        assertTrue(persistInATransaction(user));
    }


    @Test
    public void testAssignments(){

        User u0 = new User();
        User u1 = new User();

        GroupAssignment g0 = new GroupAssignment();
        g0.setId(0L);
        GroupAssignment g1 = new GroupAssignment();
        g1.setId(1L);

        //Butun kullanicilari butun assignments'lara ata

        u0.setAssignments(new HashMap<>());
        u1.setAssignments(new HashMap<>());

        u0.getAssignments().put(g0.getId(), g0);
        u0.getAssignments().put(g1.getId(), g1);
        u1.getAssignments().put(g0.getId(), g0);
        u1.getAssignments().put(g1.getId(), g1);

        g0.setAuthors(new ArrayList<>());
        g1.setAuthors(new ArrayList<>());

        g0.getAuthors().add(u0);
        g0.getAuthors().add(u1);
        g1.getAuthors().add(u0);
        g1.getAuthors().add(u1);

        // Ic ice entity olusturma (cascade) yok bu yuzden hepsini transaction'a gonder
        assertTrue(persistInATransaction(u0,u1,g0,g1));
    }

    @Test
    public void testElementCollection(){

        User u = new User();
        u.setRoles(new ArrayList<>());

        u.getRoles().add("Admin");

        assertTrue(persistInATransaction(u));
    }
}