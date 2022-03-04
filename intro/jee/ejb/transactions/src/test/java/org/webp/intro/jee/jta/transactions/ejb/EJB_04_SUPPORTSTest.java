package org.webp.intro.jee.jta.transactions.ejb;


import org.junit.Test;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import static org.junit.Assert.*;


public class EJB_04_SUPPORTSTest extends TestBase{

    @EJB
    private EJB_04_SUPPORTS ejb;

    @Test
    public void testRead(){

        String name = "foo";

        assertFalse(queriesEJB.isInDB(name));

        //REQUIRED, o yüzden beklenildiği gibi çalışacak
        ejb.createFooWithRequiredTransaction(name);

        assertTrue(queriesEJB.isInDB(name));

        /*
            Transaction içerisinde değiliz ve SUPPORT olduğu için de
            yenisi oluşturulmayacak. Ancak yalnızca okuma işlemi yapıldığından
            problem olmayacak ve çalışacaktır.
         */
        boolean present = ejb.isPresentWithSupports(name);
        assertTrue(present);
    }


    @Test
    public void testFailWrite(){

        String name = "foo";

        assertFalse(queriesEJB.isInDB(name));

        /*
            Burada hata meydana gelecektir çünkü transaction içerisinde EntityManager persist ile
            yazma operasyonu gerçekleştirecek.
         */
        try {
            ejb.createFooWithSupports(name);
            fail();
        } catch (EJBException e){
            //beklenildiği gibi
        }
    }


    @Test
    public void testWrite(){

        String first = "first";
        String second = "second";

        assertFalse(queriesEJB.isInDB(first));
        assertFalse(queriesEJB.isInDB(second));

        ejb.createTwo(first,second);

        //burası çalışacaktır çünkü annotation yalnızca proxy çağrılarda gerçekleşir.
        assertTrue(queriesEJB.isInDB(first));
        assertTrue(queriesEJB.isInDB(second));
    }
}