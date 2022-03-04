package org.webp.intro.jee.ejb.lazy;

import org.hibernate.Hibernate;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class Ejb {

    @PersistenceContext
    private EntityManager em;


    public Long create(int n){

        A a = new A();
        for(int i=0; i<n; i++){
            a.getList().add(new B());
        }

        em.persist(a);

        return a.getId();
    }

    public A getLazy(long id){

        return em.find(A.class, id);
    }


    public A getInitialized(long id){

        A a = em.find(A.class, id);
        if(a == null){
            return null;
        }

        /*
            yöntemlerden birini çağırarak listenin çekilmesi zorlanır

            Ancak bu çok efektif bir yöntem değildir çünkü bir veya daha fazla SQL sorgusu oluşturulması
            gerekebilir.

            Bir diğer yaklaşım da em.find yerine özel JPQL query kullanılmasıdır. Böylelikle "jee/jpa/fetch"
            içerisinde tek bir SQL sorgusu ile yapılır
            Ancak bu SQL çevrimi JPA provider'a (Hibernate, EclipseLink gibi) bağlıdır. Performans sizin için
            önemliyse oluşturulan SQL sorgularını kontrol edin.
          */
        a.getList().size();

        return a;
    }

    public A getWrongInitialized(long id){

        A a = em.find(A.class, id);
        if(a == null){
            return null;
        }

        //bu yeterli değil
        a.getList();

        return a;
    }

    public A getInitializedWithHibernate(long id){

        A a = em.find(A.class, id);
        if(a == null){
            return null;
        }

        //Eğer Hibernate kullanıyorsanız aşağıdakini de kullanabilirsiniz
        Hibernate.initialize(a.getList());

        return a;
    }
}
