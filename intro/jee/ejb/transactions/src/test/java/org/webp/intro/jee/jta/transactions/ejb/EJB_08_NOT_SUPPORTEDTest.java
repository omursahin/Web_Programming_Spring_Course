package org.webp.intro.jee.jta.transactions.ejb;



import org.junit.Test;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import static org.junit.Assert.*;


public class EJB_08_NOT_SUPPORTEDTest extends TestBase{

    @EJB
    private EJB_08_NOT_SUPPORTED ejb;

    @Test
    public void testDirectCall(){

        String name = "abc";

        //transaction dışında yazma yaptığı için hata verecek
        try {
            ejb.createFooNotSupported(name);
            fail();
        }catch (EJBException e){
            //beklenilen
        }
    }

    @Test
    public void testIndirectCall(){

        String name = "abc";

        assertFalse(queriesEJB.isInDB(name));

        ejb.createFooIndirectly(name);

        assertTrue(queriesEJB.isInDB(name));
    }


    @Test
    public void testIndirectEJB(){

        String name = "abc";

        //bu metot transaction oluştursa bile dahili çağrı NOT_SUPPORTED sebebiyle dışında kalacak
        try {
            ejb.createFooIndirectlyWithEJBCall(name);
            fail();
        }catch (EJBException e){
            //beklenilen
        }
    }


    @Test
    public void testIndirectSupports(){

        String name = "abc";

        assertFalse(queriesEJB.isInDB(name));

        //çağıran metot transaction'ı tekrar kullandığı için sorun yok
        ejb.createFooIndirectlyWithEJBCallWithSupports(name);

        assertTrue(queriesEJB.isInDB(name));
    }


}