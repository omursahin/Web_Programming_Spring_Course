package org.webp.intro.jee.jpa.lock;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.function.Consumer;

/**
 * DB'de kod calistirmak icin farklı threadler kullanan kod
 */
public class TransactionExecutor {

    private final  EntityManagerFactory factory;

    public TransactionExecutor(EntityManagerFactory factory) {
        this.factory = factory;
    }

    /**
     * Verilen lambda expression'ı farklı bir thread'te calistir.
     * Mevcut thread lambda tamamen calisana kadar bekler
     */
    public void syncExe(Consumer<EntityManager> command) {
        Thread t = createThread(command);
        startAndWait(t);
    }

    /**
     * Verilen lambda ifadesini async olarak farkli threadlerde calistir.
     *
     * @return Lambda ifadesini paralel olarak calistirmak icin yeni uretilen
     * Thread'in referansini doner
     */
    public Thread asyncExe(Consumer<EntityManager> command){
        Thread t = createThread(command);
        t.start();
        return t;
    }

    private Thread createThread(Consumer<EntityManager> command) {

        return new Thread(() ->{
            EntityManager em = factory.createEntityManager();
            EntityTransaction tx = em.getTransaction();

            tx.begin();
            try{
                command.accept(em);
                tx.commit();
            } catch (Exception e){
                tx.rollback();
                System.out.println("\n\nFailed transaction on separated thread: "+e.getCause().toString()+"\n\n");
            }
            em.close();
        });
    }

    private void startAndWait(Thread t){
        //thread başlat
        t.start();

        //ardından bitmesini bekle
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
