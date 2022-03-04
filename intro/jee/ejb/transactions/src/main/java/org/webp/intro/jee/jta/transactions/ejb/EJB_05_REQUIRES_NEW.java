package org.webp.intro.jee.jta.transactions.ejb;


import org.webp.intro.jee.jta.transactions.data.Foo;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EJB_05_REQUIRES_NEW {

    @PersistenceContext
    private EntityManager em;

    @Resource
    private SessionContext ctx;


    public void createFooRequired(String name){
        Foo foo = new Foo(name);
        em.persist(foo);
    }

    /*
        Burası yeni bir transaction başlatır. Eğer mevcutta aktif bir transaction var ise
        önce beklemeye alır, diğerine katılmaz
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void createFooRequiresNew(String name){
        Foo foo = new Foo(name);
        em.persist(foo);
    }


    public void createTwoWithRollback(String first, String second){

        //örneğin içerisinden çağrı, proxy'den değil bu yüzden annotation yok sayılır
        createFooRequired(first); //mevcut transaction'ı kullanmış oldu
        createFooRequiresNew(second); //yeni bir transaction yaratmış oldu

        /*
            İkincisinin hiçbir etkisinin olmaması beklense de etkileyecektir çünkü
            bunlar EJB çağrısı değil doğrudan Java çağrılarıdır ve proxy'ler
            transaction'ı ele ele alamayacaktır
         */
        ctx.setRollbackOnly();
    }

    public void createTwoWithRollbackInEJBCall(String first, String second){

        createFooRequired(first); //mevcut transactionı kullanır

        //yeni proxy'dir bu yüzden uygulanan annotation'ı çağırır
        EJB_05_REQUIRES_NEW ejb = ctx.getBusinessObject(EJB_05_REQUIRES_NEW.class);
        ejb.createFooRequiresNew(second); //yeni transaction oluşturur
        //metot tamamlandıktan (ve transaction tamamalanınca) önceki transaction devam eder


        ctx.setRollbackOnly(); //ikincisinin bir etkisi beklenildiği gibi olmayacaktır
    }

    public void createTwoWithRollbackInEJBCallOnSameTransaction(String first, String second){

        createFooRequired(first); //uses current transaction

        EJB_05_REQUIRES_NEW ejb = ctx.getBusinessObject(EJB_05_REQUIRES_NEW.class);
        //aynı transaction, devam eden bir EJB çağrısı olsa bile REQUIRED olarak yeni transaction
        // oluşturmaz mevcut olana katılacaktır
        ejb.createFooRequired(second);

        ctx.setRollbackOnly(); //her ikisini de etkileyecek
    }



}
