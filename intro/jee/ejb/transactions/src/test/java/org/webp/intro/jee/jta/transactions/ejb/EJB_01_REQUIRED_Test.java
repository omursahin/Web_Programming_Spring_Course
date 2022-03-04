package org.webp.intro.jee.jta.transactions.ejb;


import org.junit.Test;

import javax.ejb.EJB;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EJB_01_REQUIRED_Test extends TestBase{

    @EJB
    private EJB_01_REQUIRED ejb;

    @Test
    public void test(){

        /*
            Testler transaction içinde yürütülmez
         */

        String name = "a name";

        /*
            proxy bean çağrısı transaction başlatacak ve çağrı sonunda da commit edecektir
         */
        assertFalse(queriesEJB.isInDB(name));

        /*
            transaction normal olarak tamamlanmalı.
            Metot REQUIRED olarak işaretlendiğinden ve aktif transaction
            bulunmadığından yeni bir transaction başlatılacaktır.
         */
        ejb.createFoo(name);

        assertTrue(queriesEJB.isInDB(name));
    }
}