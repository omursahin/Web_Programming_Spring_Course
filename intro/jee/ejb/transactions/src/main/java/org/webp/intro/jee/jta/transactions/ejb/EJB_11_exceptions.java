package org.webp.intro.jee.jta.transactions.ejb;


import org.webp.intro.jee.jta.transactions.data.Foo;

import javax.ejb.ApplicationException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EJB_11_exceptions {

    /*
        Java exceptionlarındaki CHECKED ve UNCHECKED ayrımı geçmişte kaldı. CHECKED exceptionlar
        her zaman ele alınmak (try/catch veya metotta throws ile) zorundadır. Ancak bunlar yalnızca
        derleme zamanında bulunurlar JVM içerisinde yanı çalışma zamanında değil. Kotlin
        gibi JVM'de çalışan modern dillerin CHECKED exceptionlarla ilgilenmemesinin
        (Java API'larını kullanırken bile) sebebi budur.
     */

    @PersistenceContext
    private EntityManager em;


    public void addAndThrowRuntimeException(String name){
        Foo foo = new Foo(name);
        em.persist(foo);
        /*
            RuntimeException (ve buna bağlı alt exceptionlar) UNCHECKED'dir.
         */
        throw new RuntimeException("Transaction should roll back");
    }


    public void addAndThrowException(String name)
            throws Exception //NOT: buna ihtiyaç vardır yoksa derlenmez
    {
        Foo foo = new Foo(name);
        em.persist(foo);
        //bu CHECKED exception'dır
        throw new Exception("Checked exceptions will not lead to rollback");
        /*
            Ne yazık ki JPA bu türde check exception gerçekleşirse transaction'ı geri almaz. Bunun
            herhangi mantıklı açıklaması yoktur. Bu checked exceptionlardan nefret etmek için
            bir diğer sebep.
         */
    }

    public void addAndThrowRuntimeExceptionNoRollback(String name){
        Foo foo = new Foo(name);
        em.persist(foo);
        throw new NoRollbackRuntimeException("Should not roll back");
    }


    public void addAndThrowExceptionWithRollback(String name)
            throws Exception //NOT: buna ihtiyaç vardır yoksa derlenmez
    {
        Foo foo = new Foo(name);
        em.persist(foo);
        throw new WithRollbackException("Rollback");
    }

    /*
        JPA, CHECKED ve UNCHECKED exceptionları,
        geri alma söz konusu olduğunda farklı şekilde ele alır.
        Ancak bu davranış ek annotation'lar ile değiştirilebilir.
     */

    @ApplicationException(rollback = false)
    private static class NoRollbackRuntimeException extends RuntimeException{
        public NoRollbackRuntimeException(String message) {
            super(message);
        }
    }

    @ApplicationException(rollback = true)
    private static class WithRollbackException extends Exception{
        public WithRollbackException(String message) {
            super(message);
        }
    }
}
