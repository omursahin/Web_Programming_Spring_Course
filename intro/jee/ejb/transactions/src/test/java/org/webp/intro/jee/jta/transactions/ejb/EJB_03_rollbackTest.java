package org.webp.intro.jee.jta.transactions.ejb;


import org.junit.Test;

import javax.ejb.EJB;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EJB_03_rollbackTest extends TestBase{

    @EJB
    private EJB_03_rollback ejb;

    @Test
    public void test(){

        String first = "first";
        String second = "second";

        /*
            2 elemanın da veritabanında bulunmadığından emin ol (veritabanı her bir test senaryosu çalışmasında
            silinecektir, detaylar için üst sınıfa (TestBase) bakınız)
         */
        assertFalse(queriesEJB.isInDB(first));
        assertFalse(queriesEJB.isInDB(second));

        /*
            Burada veritabanına yzmaya çalışıyoruz ancak commit etmeden önce
            geri alınacaktır bu yüzden veritabanını etkilemeyecektir
         */
        ejb.createTwo(true,first,second); //geri almalı

        //hala veritabanında değil
        assertFalse(queriesEJB.isInDB(first));
        assertFalse(queriesEJB.isInDB(second));

        //tekrar oluşturmayı deniyoruz ancak rollback false gönderiyoruz
        ejb.createTwo(false,first,second);

        //şimdi veritabanında bulunuyor olmalı
        assertTrue(queriesEJB.isInDB(first));
        assertTrue(queriesEJB.isInDB(second));
    }

}