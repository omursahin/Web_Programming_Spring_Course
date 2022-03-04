package org.webp.intro.jee.jpa.fetch;



import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.*;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;



public class FooTest {

    private EntityManagerFactory factory;

    @BeforeEach
    public void init() {
        factory = Persistence.createEntityManagerFactory("DB");
    }

    @AfterEach
    public void tearDown() {
        factory.close();
    }

    private boolean executeInTransaction(Consumer<EntityManager> lambda) {

        EntityManager manager = factory.createEntityManager();
        EntityTransaction tx = manager.getTransaction();
        tx.begin();

        try {
            lambda.accept(manager);
            tx.commit();
        } catch (Exception e) {
            System.out.println("FAILED TRANSACTION: " + e.toString());
            tx.rollback();
            return false;
        } finally {
            manager.close();
        }

        return true;
    }

    private Foo getById(long id) {

        EntityManager em = factory.createEntityManager();

        try {
            TypedQuery<Foo> query = em.createQuery("select f from Foo f where f.id=?1", Foo.class);
            query.setParameter(1, id);

            List<Foo> list = query.getResultList();
            if (list.isEmpty()) {
                return null;
            } else {
                return list.get(0);
            }
        } finally {
            em.close();
        }
    }

    private Foo getByIdForcingLoading(long id) {

        EntityManager em = factory.createEntityManager();

        try {
            TypedQuery<Foo> query = em.createQuery(
                    //Burada f.lazyBi yukleme icin zorluyoruz
                    "select f from Foo f left join fetch f.lazyBi where f.id=?1", Foo.class);
            query.setParameter(1, id);

            List<Foo> list = query.getResultList();
            if (list.isEmpty()) {
                return null;
            } else {
                return list.get(0);
            }

        } finally {
            em.close();
        }
    }

    @Test
    public void testFetchEager() {

        long id = 1;

        Foo foo = new Foo();
        foo.setId(id);
        Bar bar = new Bar();
        foo.getEagerBi().add(bar);
        bar.setParent(foo);

        boolean executed = executeInTransaction(em -> {
            em.persist(foo);
            em.persist(bar);
        });

        assertTrue(executed);

        /*
            Burada, transaction disarisinda veri okumaya devam etmek problem yaratmamaktadir.
            Bu turde bir veri foo entity icerisinde okunmus ve saklanmistir.
         */

        Foo readBack = getById(id);

        assertEquals(1, readBack.getEagerBi().size());
    }

    @Test
    public void testFetchLazy() {

        long id = 1;

        Foo foo = new Foo();
        foo.setId(id);
        Bar bar = new Bar();
        foo.getLazyBi().add(bar);
        bar.setParent(foo);

        boolean executed = executeInTransaction(em -> {
            em.persist(foo);
            em.persist(bar);
        });

        assertTrue(executed);

        /*
            Bir sonraki neden hata verir?
            Cunku liste lazy olarak yuklenir. Yuklenmesi icin aktif session icerisinde
            (or: bir transaction icerisinde) erismek gerekmektedir

            Not: EJB ile calisirken bu durum hakkinda ayrintilara bakacagiz
         */

        Foo readBack = getById(id);
        assertThrows(Exception.class, () -> readBack.getLazyBi().size());

        //Asagidaki durumda bir sorun olmayacaktir
        Foo readBackForceLoading = getByIdForcingLoading(id);
        assertEquals(1, readBackForceLoading.getLazyBi().size());
    }


    @Test
    public void testDeleteBidirectional() {

        long id = 1;

        Foo foo = new Foo();
        foo.setId(id);
        Bar bar = new Bar();
        foo.getLazyBi().add(bar);
        bar.setParent(foo);

        boolean executed = executeInTransaction(em -> {
            em.persist(foo);
            em.persist(bar);
        });

        assertTrue(executed);

        assertNotNull(getById(id));

        boolean deleted = executeInTransaction(em -> {
            Foo x = em.find(Foo.class, id);
            em.remove(x);
        });

        assertTrue(deleted);
        assertNull(getById(id));
    }
}