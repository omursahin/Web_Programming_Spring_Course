package org.webp.intro.jee.jta.transactions.ejb;

import org.webp.intro.jee.jta.transactions.data.Foo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EJB_02_abort {

    @PersistenceContext
    private EntityManager em;

    public void createTwoCopies(String name){

        Foo foo = new Foo(name);
        Foo copy = new Foo(name);

        em.persist(foo);
        em.persist(copy);//aynı id'ye sahip olduğu için hata vermeli

        /*
                em.persist(copy) çalıştırıldığında exception fırlatacaktır.
                Ancak farklı JPA sağlayıcıları farklı davranışlara sahip olabilir.
                Örneğin metot çalışması tamamlandıktan sonra exception fırlatabilir.
                Bu durumu belirleyen nokta veritabanına işleme eylemini JEE container'ının
                ne zaman yapacağı ile ilgilidir.
         */
    }
}
