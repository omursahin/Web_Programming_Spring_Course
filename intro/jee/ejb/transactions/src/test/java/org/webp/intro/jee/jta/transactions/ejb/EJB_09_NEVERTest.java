package org.webp.intro.jee.jta.transactions.ejb;


import org.junit.Test;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class EJB_09_NEVERTest extends TestBase{

    @EJB
    private EJB_09_NEVER ejb;

    @Test
    public void testGetTrue() throws Exception {

        assertTrue(ejb.getTrue()); //transaction içind edeğiliz, sorun yok
    }

    @Test
    public void testGetFromRequired() throws Exception {

        //REQUIRED sebebiyle transaction oluşacaktır ancak NEVER
        // sebebiyle hataya düşecektir
        try {
            ejb.getFromRequired();
            fail();
        }catch (EJBException e){
            //beklenilen
        }
    }

    @Test
    public void testGetFromNotSupported() throws Exception {

        // transaction yok bu yüzden NEVER çağrısı hataya düşmez
        assertTrue(ejb.getFromNotSupported());
    }


    @Test
    public void testGetFromRequiredBySuspendingFirst() throws Exception {

        /*
            Sorun yok bir transaction oluşsa da NEVER çağırılmadan önce
            askıya alacak.
            Yani:
            - REQUIRED yeni transaction oluşturur (çünkü testler transaction içinde değil)
            - NOT_SUPPORTED transaction'ı beklemeye alır
            - NEVER sorun olmayacaktır çünkü çağrı sırasında aktif transaction yok
         */
        assertTrue(ejb.getFromRequiredBySuspendingFirst());
    }

}