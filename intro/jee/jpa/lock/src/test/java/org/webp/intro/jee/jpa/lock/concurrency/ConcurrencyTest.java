package org.webp.intro.jee.jpa.lock.concurrency;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ConcurrencyTest {

    private EntityManagerFactory factory;

    @BeforeEach
    public void init() {
        factory = Persistence.createEntityManagerFactory("DB");
    }

    @AfterEach
    public void tearDown() {
        factory.close();
    }


    private Long createNewCounter(Class<? extends Counter> klass) throws Exception {

        Counter counter = klass.getDeclaredConstructor().newInstance();

        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        try {
            em.persist(counter);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }

        em.close();

        return counter.getId();
    }

    private boolean incrementCounter(Counter counter, EntityManager em, LockModeType lockModeType){

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            //Ilk olarak veritabanindaki mevcut durumu okuyun
            em.refresh(counter, lockModeType);

            counter.increment();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            return false;
        }

        return true;
    }


    private int runInParallel(Long id,
                              LockModeType lockModeType,
                              boolean retryIfFails,
                              BiFunction<EntityManager, Long, Counter> provider) {

        final int nThreads = 4;
        final int loops = 10_000;

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < nThreads; i++) {

            Thread t = new Thread() {
                @Override
                public void run() {

                    EntityManager em = factory.createEntityManager();
                    Counter counter = provider.apply(em, id);

                    int executions = 0;

                    while(executions < loops){
                        if(! em.contains(counter)){
                            //bazi durumlarda, basarisiz bir artis entity'nin detach olmasina sebep olabilir
                            counter = provider.apply(em, id);
                        }

                        boolean success = incrementCounter(counter, em, lockModeType);
                        if(success || !retryIfFails){
                            executions++;
                        }
                    }

                    em.close();
                }
            };
            t.start();
            threads.add(t);
        }

        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        });

        EntityManager em = factory.createEntityManager();
        Counter counter = provider.apply(em, id);
        em.close();

        int result = counter.getCounter();
        int expected = nThreads * loops;
        int diff = expected - result ;

        return diff;
    }

    private void printMissingIterations(int delta){
        System.out.println("\nMISSING ITERATIONS: " + delta +"\n\n");
    }

    //-----------------------------------------------------------

    @Test
    public void testNoLockControl() throws Exception{

        Class<BaseCounter> klass = BaseCounter.class;

        Long id = createNewCounter(klass);

        // Bunun bir etkisi bulunmayacaktir cunku transaction bir hata vermez.
        boolean retryIfFails = true;

        int delta = runInParallel(id, LockModeType.NONE, retryIfFails, (em, i) -> em.find(klass, i) );

        printMissingIterations(delta);

        assertTrue(delta > 0);//en az bir işlemin başarısız olmaması mümkün değil
    }

    /*
            Optimistic veya pessimistic lock?

            Optimistic: catismanin (conflict) daha nadir oldugu durumlarda kullanilir.
            Eger bir conflict tespit edilirse exception firlatir ve kullanicinin ele almasini
            saglar. Bu maliyetsiz degildir ancak nadir oldugu durumlarda cok da sorun yaratmamaktadir.

            Pessimistic: daha guvenli/kolay, ancak veritabanini kilitlemek maliyetlidir. Bu yuzden sadece
            yuksek ihtimalle conflict yasanacak durumlarda kullanilmalidir.
     */



    @Test
    public void testPessimisticWriteLock() throws Exception{

        Class<BaseCounter> klass = BaseCounter.class;
        Long id = createNewCounter(klass);

        //tekrar denemeye gerek yok cunku transaction hata vermeyecektir.
        //Not: pessimistic lock cok fazla beklerse bir exception firlatir (or timeout, varsayilan olarak 1 sn)
        boolean retryIfFails = false;

        int delta = runInParallel(id, LockModeType.PESSIMISTIC_WRITE, retryIfFails, (em, i) -> em.find(klass, i) );

        printMissingIterations(delta);

        assertEquals(0, delta);
    }

    @Test
    public void testOptimisticButNoRetry() throws Exception{

        Class<CounterWithVersion> klass = CounterWithVersion.class;
        Long id = createNewCounter(klass);

        boolean retryIfFails = false;

        int delta = runInParallel(id, LockModeType.OPTIMISTIC, retryIfFails, (em, i) -> em.find(klass, i) );

        printMissingIterations(delta);

        assertTrue(delta > 0); //tutarsizlik sebebiyle bazi transactionlar hata verebilir
    }

    @Test
    public void testOptimisticButNoVersion() throws Exception{

        Class<BaseCounter> klass = BaseCounter.class;
        Long id = createNewCounter(klass);

        boolean retryIfFails = true;

        int delta = runInParallel(id, LockModeType.OPTIMISTIC, retryIfFails, (em, i) -> em.find(klass, i) );

        printMissingIterations(delta);

        //yine hata vermis transactionlar bulunacaktir, hata veren transactionlar tekrarlansa bile
        //ilk kisimdaki transaction hata vermeyecektir
        assertTrue(delta > 0);
    }

    @Test
    public void testOptimisticWithRetry() throws Exception{

        Class<CounterWithVersion> klass = CounterWithVersion.class;
        Long id = createNewCounter(klass);

        boolean retryIfFails = true;

        int delta = runInParallel(id, LockModeType.OPTIMISTIC, retryIfFails, (em, i) -> em.find(klass, i) );

        printMissingIterations(delta);

        assertEquals(0, delta); //hata veren transactionlar tekrar calistirilir
    }
}
