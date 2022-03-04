package org.webp.intro.jee.jta.transactions.ejb;


import org.webp.intro.jee.jta.transactions.data.Foo;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
/*
    REQUIRED varsayılandır o yüzden teknik olarak burada belirtmek gereksizdir.
    Buradaki önemli nokta eğer ki bir sınıfa bu annotation uygulanırsa bütün public metotlara da
    uygulanmış olacaktır.
 */
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class EJB_01_REQUIRED {

    @PersistenceContext
    private EntityManager em;

    public void createFoo(String name){
        Foo foo = new Foo(name);
        em.persist(foo);
    }
}
