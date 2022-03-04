package org.webp.intro.jee.jta.transactions.ejb;



import org.junit.Test;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;


public class EJB_02_abortTest extends TestBase{

    @EJB
    private EJB_02_abort ejb;

    @Test
    public void test(){

        String name = "a name";

        assertFalse(queriesEJB.isInDB(name));

        try {
            ejb.createTwoCopies(name); //hata vermeli
            fail();
        } catch (EJBException e){
            //beklenildiği gibi
        }

        assertFalse(queriesEJB.isInDB(name));
    }

}