package org.webp.intro.jee.jta.transactions.ejb;


import org.webp.intro.jee.jta.transactions.data.Foo;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EJB_04_SUPPORTS {

    @PersistenceContext
    private EntityManager em;


    //gereksiz annotation, REQUIRED varsayılandır
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createFooWithRequiredTransaction(String name){
        Foo foo = new Foo(name);
        em.persist(foo);
    }


    /*
        Eğer bir transaction'a ihtiyaç yoksa performans için container'a bir
        tane daha oluşturmaması talimatı verilebilir ancak eğer bir transaction
        içerisindeyse ona katılır.
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean isPresentWithSupports(String name){
        return em.find(Foo.class, name) != null;
    }


    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void createFooWithSupports(String name){
        Foo foo = new Foo(name);
        em.persist(foo);
    }



    public void createTwo(String first, String second){
        //burada bir transaction içerisindeyiz

        createFooWithRequiredTransaction(first); //aynı transaction, yenisi oluşturulmaz

        /*
            transaction içerisinde olduğumuzdan burası çalışacaktır ancak
            annotation ile bağımsızdır çünkü burası EJB içerisindeki Java çağrısıdır
            JEE container'ı tarafından oluşturulan proxy çağrısı değildir. Bu noktayı
            ilerleyen kısımda daha detaylı göreceğiz
         */
        createFooWithSupports(second);
    }
}
