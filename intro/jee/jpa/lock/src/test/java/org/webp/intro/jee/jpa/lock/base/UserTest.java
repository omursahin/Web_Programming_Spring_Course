package org.webp.intro.jee.jpa.lock.base;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.webp.intro.jee.jpa.lock.TransactionExecutor;

import javax.persistence.*;

import static org.junit.jupiter.api.Assertions.*;


public class UserTest {

    private EntityManagerFactory factory;

    @BeforeEach
    public void init() {
        factory = Persistence.createEntityManagerFactory("DB");
    }

    @AfterEach
    public void tearDown() {
        factory.close();
    }


    private Long createUser(String name) {
        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        User user = new User();
        user.setName(name);

        tx.begin();
        em.persist(user);
        tx.commit();

        return user.getId();
    }

    private String getName(Long id) {
        EntityManager em = factory.createEntityManager();
        User user = em.find(User.class, id);
        return user.getName();
    }

    @Test
    public void testVersionIncrement() {

        String name = "name";

        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        User user = new User();
        user.setName(name);

        tx.begin();
        em.persist(user);
        tx.commit();
        int x = user.getVersion();

        tx.begin();
        user.setName("foo");
        em.merge(user);
        tx.commit();
        int y = user.getVersion();

        assertEquals(x + 1, y);


        tx.begin();
        //user burada modifiye edilmedi
        em.merge(user);
        tx.commit();
        int z = user.getVersion();

        assertEquals(y, z); // versiyon degismemis olmalidir
    }


    @Test
    public void testOptimistic() {

        String name = "optimistic";

        long id = createUser(name);

        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        User u1 = em.find(User.class, id,
                LockModeType.OPTIMISTIC); //Eger @Version varsa OPTIMISTIC varsayilandir
        assertNotNull(u1);

        //veritabaninda name degerine sahip user var mi kontrol et
        TypedQuery<Long> query = em.createQuery("select count(u) from User u where u.name = ?1", Long.class);
        query.setParameter(1, name);
        long res = query.getSingleResult();
        assertEquals(1, res);

        //yeni bir thread ile farkli bir update yap
        TransactionExecutor executor = new TransactionExecutor(factory);
        executor.syncExe(s -> {
            User user = s.find(User.class, id);
            user.setName("foo");
        });

        //name degistirildi
        res = query.getSingleResult();
        assertEquals(0, res);

        /*
                Beklenildigi gibi: u1 kilitlenmis durumdadir ve bir baska thread icerisinde
                degisitirilmeye calisildiginda izin vermez. Buradaki veriye stale (bayat) veri denir.
                Commit yapmaya calistigimizda, JPA optimistic lock sebebiyle gerceklesen
                bu durumu fark eder ve bir exception firlatir
        */
        assertThrows(RollbackException.class, () -> {
            tx.commit();
        });

        em.close();
    }


    @Test
    public void testReadWhilePessimisticLock() {

        String name = "read-pessimistic";
        String changedName = "changed-pessimistic";

        long id = createUser(name);

        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        /*
            UYARI: farklı PESSIMISTIC lock cesitleri bulunmaktadir.
            Nasil ele alindigi ise mevcut veritabanina (H2, Postgres vs.) baglidir. Daha da kotusu
            bazi modlar desteklenmiyorsa hibernate bu durumu gormezden gelir.
         */
        User u1 = em.find(User.class, id, LockModeType.PESSIMISTIC_WRITE);
        assertNotNull(u1);
        TransactionExecutor executor = new TransactionExecutor(factory);

        executor.syncExe(s -> {
            User user = s.find(User.class, id);
            String readName = user.getName();
        });

        //Yukaridaki butun islemler read islemleriydi o yuzden izin verdi. Degisiklik durumunda exception firlatacakti
        //pessimistic lock deadlock'lara sebep olabilir

        tx.commit();
        em.close();
    }

    @Test
    public void testWriteWhilePessimisticLock() throws Exception {

        String name = "write-pessimistic";
        String other = "foo";

        long id = createUser(name);

        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        User u1 = em.find(User.class, id, LockModeType.PESSIMISTIC_WRITE);
        assertNotNull(u1);

        TransactionExecutor executor = new TransactionExecutor(factory);
        Thread t = executor.asyncExe(s -> {
            User user = s.find(User.class, id);
            user.setName(other); //WRITE operasyonu oldugu icin bu bloklanacaktir
        });
        //Not: Eger executor.syncExe kulanilmasaydi, buraya asla erisilemeyecekti. Buna Deadlock denir.

        //Not: persistence.xml dosyasına bakın, bunun calismasi icin lock timeout 10s'e yukseltildi
        Thread.sleep(5_000); // uzun suren bir islemi simule ediyor

        assertTrue(t.isAlive()); //harici transaction hala lock'i bekliyor

        assertEquals(name, getName(id));

        tx.commit();

        //simdi lock acildi, harici transaction'in tamamlanmasini bekleyebiliriz

        t.join();
        assertFalse(t.isAlive());

        assertEquals(other, getName(id));

        em.close();
    }

}
