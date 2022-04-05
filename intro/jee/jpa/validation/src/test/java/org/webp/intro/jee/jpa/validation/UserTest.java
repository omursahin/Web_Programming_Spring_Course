package org.webp.intro.jee.jpa.validation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    private EntityManagerFactory emFactory;
    private EntityManager em;

    private ValidatorFactory valFactory;
    private Validator validator;

    @BeforeEach
    public void init() {
        emFactory = Persistence.createEntityManagerFactory("DB");
        em = emFactory.createEntityManager();

        valFactory = Validation.buildDefaultValidatorFactory();
        validator = valFactory.getValidator();
    }

    @AfterEach
    public void tearDown() {
        em.close();
        emFactory.close();
        valFactory.close();
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

    private User getAValidUser(){
        User user = new User();
        user.setName("Foo");
        user.setMiddleName(null);
        user.setSurname("Bar");
        user.setEmail("foobar@gmail.com");
        user.setDateOfBirth(LocalDate.of(1970, 1, 1));
        user.setDateOfRegistration(LocalDate.of(2015, 1, 1));

        return user;
    }

    private <T> boolean hasViolations(T obj){
        Set<ConstraintViolation<T>> violations = validator.validate(obj);

        for(ConstraintViolation<T> cv : violations){
            System.out.println("VIOLATION: "+cv.toString());
        }

        return violations.size() > 0;
    }

    @Test
    public void testValid(){

        User user = getAValidUser();

        //kısıtlama ihlali yok
        assertFalse(hasViolations(user));

        //veritabanına yazılabilir
        assertTrue(persistInATransaction(user));
    }

    @Test
    public void testNoName(){

        User user = getAValidUser();
        user.setName(null);

        assertTrue(hasViolations(user));

        //veritabanına yazmak hata verecektir
        assertFalse(persistInATransaction(user));
    }


    @Test
    public void testShortName(){

        User user = getAValidUser();
        user.setName("a");

        assertTrue(hasViolations(user));
        assertFalse(persistInATransaction(user));
    }

    @Test
    public void testLongName(){

        User user = getAValidUser();
        user.setName(new String(new char[1_000]));

        assertTrue(hasViolations(user));
        assertFalse(persistInATransaction(user));
    }

    @Test
    public void testBlankName(){

        User user = getAValidUser();
        user.setName("      ");

        /*
            Entity'de name alanı boş olamaz veya sadece space'lerden oluşamaz
            gibi bir kısıt bulunmamaktadır. Eğer bunu yapmak isteseydik regular
            expression ile tanımlanabilirdi veya @NotBlank kullanılabilabilirdi
         */
        assertFalse(hasViolations(user));
        assertTrue(persistInATransaction(user));
    }


    @Test
    public void testMiddleName(){

        User user = getAValidUser();

        //null geçerlidir
        user.setMiddleName(null);
        assertFalse(hasViolations(user));

        //boş bırakılması geçerlidir
        user.setMiddleName("");
        assertFalse(hasViolations(user));

        //ancak çok uzun karakter geçersizdir
        user.setMiddleName(new String(new char[1_000]));
        assertTrue(hasViolations(user));
    }


    @Test
    public void testBlankSurname(){

        User user = getAValidUser();
        // null değil ancak boşluk
        user.setSurname("    ");

        //bu yüzden doğrulamada hata olacaktır
        assertTrue(hasViolations(user));
        assertFalse(persistInATransaction(user));
    }

    @Test
    public void testShortSurname(){

        User user = getAValidUser();
        user.setSurname("   a  ");

        /*
            Bu hata vermeyecektir çünkü boş değildir ("a" karakteri var)
            ve en az 2 karakter (space'ler) bulunmaktadır.
         */
        assertFalse(hasViolations(user));
        assertTrue(persistInATransaction(user));
    }

    @Test
    public void testRegistrationInTheFuture(){
        User user = getAValidUser();
        user.setDateOfRegistration(LocalDate.of(2116, 1, 1));

        assertTrue(hasViolations(user));
        assertFalse(persistInATransaction(user));
    }

    @Test
    public void testTooYoung(){
        User user = getAValidUser();
        user.setDateOfBirth(LocalDate.of(2014, 1, 1));

        assertTrue(hasViolations(user));
        assertFalse(persistInATransaction(user));

        /*
            not: eğer bu dersi 2033'de vermeye devam edersem test hata verecektir
            ancak o yıla kadar JEE veya Java bulunur mu bilemem :)
         */
    }

    @Test
    public void testRegisteredBeforeBeingBorn(){
        User user = getAValidUser();
        user.setDateOfBirth(LocalDate.of(1980, 1, 1));
        user.setDateOfRegistration(LocalDate.of(1970, 1, 1));

        assertTrue(hasViolations(user));
        assertFalse(persistInATransaction(user));
    }


    @Test
    public void testEmail(){
        User user = getAValidUser();
        user.setEmail("anInvalidEmail");

        assertTrue(hasViolations(user));
        assertFalse(persistInATransaction(user));

        user.setEmail("stillThisIs@nInvalidEmail");
        user.setId(null);

        assertTrue(hasViolations(user));
        assertFalse(persistInATransaction(user));
    }

    @Test
    public void testUnique(){

        String email = "unique@foo.com";

        User a = getAValidUser();
        a.setEmail(email);
        assertTrue(persistInATransaction(a));

        User b = getAValidUser();
        b.setEmail(email+".tr");
        assertTrue(persistInATransaction(b));

        User c = getAValidUser();
        c.setEmail(email); //aynı email
        assertFalse(persistInATransaction(c));
    }
}