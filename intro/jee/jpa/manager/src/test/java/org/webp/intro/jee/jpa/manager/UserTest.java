package org.webp.intro.jee.jpa.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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
    public void testPersistAndFind(){

        String name = "foo";

        User user = new User();
        user.setName(name);

        persistInATransaction(user);

        //Cache'in temiz oldugundan emin ol
        em.clear();

        //Bunun gibi okuma islemlerinde transaction'a ihtiyac yoktur
        User found = em.find(User.class, user.getId());

        assertEquals(name, found.getName());
    }

    @Test
    public void testPersistAndModify() {

        String before = "before";
        String during = "during";
        String after = "after";

        User user = new User();
        user.setName(before);

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(user);
            /*
                transaction sonlanmadi bu yuzden herhangi bir degisiklik veritabanina
                commit etmek uzere objeye attach edilir.
             */
            user.setName(during);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            fail();
        }

        //transaction sonlandiktan sonra burasi gerceklestirilecek
        // bu yuzden veritabanina bir etkisi yok
        user.setName(after);

        em.clear();

        //Bunun gibi okuma islemlerinde transaction'a ihtiyac yoktur
        User found = em.find(User.class, user.getId());

        assertEquals(during, found.getName());
        assertEquals(after, user.getName());
    }



    @Test
    public void testDoublePersist(){

        String first = "first";
        String second = "second";

        User user = new User();
        user.setName(first);

        assertTrue(persistInATransaction(user));

        Long id = user.getId();
        assertNotNull(id);

        assertTrue(em.contains(user));

        user.setName(second);
        assertTrue(persistInATransaction(user));

        em.clear();


        User found = em.find(User.class, user.getId());
        assertEquals(second, found.getName());
        assertEquals(id, found.getId());

        /*
            Bu entity'i EntityManager'dan ayirirsak ne olur?
         */

        em.clear();
        assertFalse(em.contains(user));

        /*
            Bu hata verecektir cunku:
            1) Veritabaninda ayni @Id'ye sahip bir User satiri zaten bulunmaktadir
            2) Entity, EntityManager'da bulunmamaktadir or: detach edilmis olabilir
         */

        assertFalse(persistInATransaction(user));
    }



    @Test
    public void testMerge(){

        String before = "before";
        String after = "after";

        User user = new User();
        user.setName(before);

        assertTrue(persistInATransaction(user));

        //bu transaction'dan sonra tamamlanir bu yuzden veritabanina etki gostermez
        user.setName(after);

        //Cache'in temiz oldugundan emin ol. bunun objeleri detach et
        em.clear();

        //Bunun gibi okuma islemlerinde transaction'a ihtiyac yoktur
        User found = em.find(User.class, user.getId());

        assertEquals(before, found.getName());
        assertEquals(after, user.getName());

        //simdi detach edilmis User nesnesindeki degisiklikle veritabanindakini birlestiriyoruz
        EntityTransaction tx = em.getTransaction();
        assertFalse(em.contains(user));
        tx.begin();
        em.merge(user);
        tx.commit();

        em.clear();

        //birlestirmeden sonra, "name" alani guncellenmis olmalidir
        found = em.find(User.class, user.getId());
        assertEquals(after, found.getName());
    }


    @Test
    public void testRefresh(){

        String before = "before";
        String after = "after";

        User user = new User();
        user.setName(before);

        assertTrue(persistInATransaction(user));

        user.setName(after);

        assertEquals(after, user.getName());

        //Yalnizca detach edilmemis entity'ler refresh edilir

        //Transaction'da gerceklesen degisiklikten sonra refresh ediyoruz
        EntityTransaction tx = em.getTransaction();
        em.contains(user);
        tx.begin();
        em.refresh(user); //Veritabanindaki degerler ile entity guncellenir
        tx.commit();

        em.clear();

        assertEquals(before, user.getName());
    }


    @Test
    public void testDetach(){

        String name = "foo";

        User user = new User();
        user.setName(name);

        assertTrue(persistInATransaction(user));

        assertTrue(em.contains(user));

        em.detach(user);

        assertFalse(em.contains(user));

        //Entity detach edildigi icin refresh hata verir
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.refresh(user);
            tx.commit();
            fail();
        } catch (Exception e){
            tx.rollback();
        }

        //EntityManager'de detach edilmis olmasi veritabanindan silindigi anlamina gelmez
        User found = em.find(User.class, user.getId());
        assertEquals(name, found.getName());
    }


    @Test
    public void testRemove(){
        String name = "foo";

        User user = new User();
        user.setName(name);

        assertTrue(persistInATransaction(user));
        assertTrue(em.contains(user));

        User found = em.find(User.class, user.getId());
        assertEquals(name, found.getName());


        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.remove(user);
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            fail();
        }

        assertFalse(em.contains(user));

        //veritabaninda artik bulunmamaktadir
        found = em.find(User.class, user.getId());
        assertNull(found);
    }


    @Test
    public void testOrphanRemovalOwner(){

        Address address = new Address();
        User user = new User();
        user.setAddress(address);

        //User kaydedilmemis Address'e sahiptir bu yuzden hata verir
        assertFalse(persistInATransaction(user));

        user.setId(null);

        assertTrue(persistInATransaction(user,address));

        Address found = em.find(Address.class, address.getId());
        assertNotNull(found);

        //Simdi User'i veritabanidan silelim
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.remove(user);
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            //Eger hata firlatirsa test de basarisiz olsun
            fail();
        }

        //User OneToOne iliskinin sahibidir boylelikle "orphanRemoval" olmasi sebebiyle
        //address de silinecektir
        found = em.find(Address.class, address.getId());
        assertNull(found);
    }


    @Test
    public void testOrphanRemovalFail(){

        Address address = new Address();
        User user = new User();
        user.setAddress(address);

        assertTrue(persistInATransaction(user,address));

        User found = em.find(User.class, user.getId());
        assertNotNull(found.getAddress());

        //Veritabanindan Address'i kaldirmaya calisalim
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        assertThrows(Exception.class, ()->{
            em.remove(address);
            tx.commit();
        });
        tx.rollback();

        //Kaldirma islemi hata vermelidir cunku User hala sahiptir ve veritabaninda bulunmaktadir

        found = em.find(User.class, user.getId());
        assertNotNull(found.getAddress());
    }
}