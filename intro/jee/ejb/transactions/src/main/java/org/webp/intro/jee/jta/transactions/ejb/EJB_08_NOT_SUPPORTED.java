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
public class EJB_08_NOT_SUPPORTED {

    @PersistenceContext
    private EntityManager em;

    @Resource
    private SessionContext ctx;

    /*
        Eğer devam eden bir transaction varsa o transactionı beklemeye al
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void createFooNotSupported(String name){
        Foo foo = new Foo(name);
        em.persist(foo); //hata alacak
        // Not: yalnızca örnek olarak, genellikle NOT_SUPPORTED metotda persist işlemi yapılmaz

    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void createFooSupports(String name){
        Foo foo = new Foo(name);
        em.persist(foo);
    }


    public void createFooIndirectly(String name){
        //NOT_SUPPORTED yok sayıldığı için çalışacak
        createFooNotSupported(name);
    }


    public void createFooIndirectlyWithEJBCall(String name){
        EJB_08_NOT_SUPPORTED ejb = ctx.getBusinessObject(EJB_08_NOT_SUPPORTED.class);
        ejb.createFooNotSupported(name); //hata çünkü transaction askıya alındı
    }

    public void createFooIndirectlyWithEJBCallWithSupports(String name){
        EJB_08_NOT_SUPPORTED ejb = ctx.getBusinessObject(EJB_08_NOT_SUPPORTED.class);
        ejb.createFooSupports(name); //transaction içerisinde olduğumuz için hata almayacağız
    }


}
