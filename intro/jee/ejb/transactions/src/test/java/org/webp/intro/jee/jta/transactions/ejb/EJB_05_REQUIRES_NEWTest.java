package org.webp.intro.jee.jta.transactions.ejb;



import org.junit.Test;

import javax.ejb.EJB;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class EJB_05_REQUIRES_NEWTest extends TestBase{

    @EJB
    private EJB_05_REQUIRES_NEW ejb;

    @Test
    public void testCreateFooRequired() throws Exception {
        String name = "foo";

        assertFalse(queriesEJB.isInDB(name));

        //transaction yoksa REQUIRED yenisini oluşturur
        ejb.createFooRequired(name);

        assertTrue(queriesEJB.isInDB(name));
    }

    @Test
    public void testCreateFooRequiresNew() throws Exception {

        String name = "foo";

        assertFalse(queriesEJB.isInDB(name));

        /*
            REQUIRES_NEW her zaman yeni bir transaction oluşturur. Ancak burada
            devam eden bir transaction olmadığı için davranışı REQUIRED gibidir.
         */
        ejb.createFooRequiresNew(name);

        assertTrue(queriesEJB.isInDB(name));
    }


    @Test
    public void testCreateTwoWithRollback() throws Exception {

        String first = "first";
        String second = "second";

        assertFalse(queriesEJB.isInDB(first));
        assertFalse(queriesEJB.isInDB(second));

        ejb.createTwoWithRollback(first, second);

        assertFalse(queriesEJB.isInDB(first)); //hata olacak çünkü geri alınacak (rollback)
        assertFalse(queriesEJB.isInDB(second));//hala hata alacak çünkü REQUIRES_NEW EJB proxy çağrısı içinde değil
    }

    @Test
    public void testCreateTwoWithRollbackInEJBCall() throws Exception {

        String first = "first";
        String second = "second";

        assertFalse(queriesEJB.isInDB(first));
        assertFalse(queriesEJB.isInDB(second));

        ejb.createTwoWithRollbackInEJBCall(first, second);

        assertFalse(queriesEJB.isInDB(first)); //hata olacak çünkü geri alınacak (rollback)
        assertTrue(queriesEJB.isInDB(second)); //rollback burayı etkilemeyecek
    }


    @Test
    public void testCreateTwoWithRollbackInEJBCallOnSameTransaction() throws Exception {

        String first = "first";
        String second = "second";

        assertFalse(queriesEJB.isInDB(first));
        assertFalse(queriesEJB.isInDB(second));

        ejb.createTwoWithRollbackInEJBCallOnSameTransaction(first, second);

        //rollback sebebiyle ikisi de hata alacak
        assertFalse(queriesEJB.isInDB(first));
        assertFalse(queriesEJB.isInDB(second));
    }
}