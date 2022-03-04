package org.webp.intro.jee.jpa.relationshipsql;

import org.junit.jupiter.api.Test;
import org.webp.intro.jee.jpa.relationshipsql.onetomany.bidirectional.X_1_to_m_bi;
import org.webp.intro.jee.jpa.relationshipsql.onetomany.bidirectional.Y_1_to_m_bi;
import org.webp.intro.jee.jpa.relationshipsql.onetomany.joincolumn.X_1_to_m_join;
import org.webp.intro.jee.jpa.relationshipsql.onetomany.joincolumn.Y_1_to_m_join;
import org.webp.intro.jee.jpa.relationshipsql.onetomany.manytoone.X_1_to_m_mt1;
import org.webp.intro.jee.jpa.relationshipsql.onetomany.manytoone.Y_1_to_m_mt1;
import org.webp.intro.jee.jpa.relationshipsql.onetomany.unidirectional.X_1_to_m_uni;
import org.webp.intro.jee.jpa.relationshipsql.onetomany.unidirectional.Y_1_to_m_uni;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class OneToManyTest extends TestBase {


    @Test
    public void testUnidirectional(){

        X_1_to_m_uni x = new X_1_to_m_uni();
        Y_1_to_m_uni y0 = new Y_1_to_m_uni();
        Y_1_to_m_uni y1 = new Y_1_to_m_uni();

        x.getYs().add(y0);
        x.getYs().add(y1);

        /*
            Burada X'den Y'ye bir bağlantı bulunur. Ancak bunu yapabilmek için
            bir ara tabloya (@Entity ile maplemediğimiz) ihtiyaç vardır
         */
        assertTrue(persistInATransaction(x, y0, y1));
    }


    @Test
    public void testManyToOne(){

        X_1_to_m_mt1 x = new X_1_to_m_mt1();
        Y_1_to_m_mt1 y0 = new Y_1_to_m_mt1();
        Y_1_to_m_mt1 y1 = new Y_1_to_m_mt1();

        y0.setX(x);
        y1.setX(x);

        /*
            Konsept olarak, M-to-1, 1-to-M ile aynidir,
            ancak Y'den X'e baglanti bulunmaktadir.
            Ara bir tabloya ihtiyac yoktur, yalnizca Y'deki FK kullanilabilir.
         */
        assertTrue(persistInATransaction(x, y0, y1));
    }


    @Test
    public void testBidirectional(){

        X_1_to_m_bi x = new X_1_to_m_bi();
        Y_1_to_m_bi y0 = new Y_1_to_m_bi();
        Y_1_to_m_bi y1 = new Y_1_to_m_bi();

        x.getYs().add(y0);
        x.getYs().add(y1);
        y0.setX(x);
        y1.setX(x);

        /*
            Veritabani @ManyToOne ile birebir aynidir. Yalnizca Y'den X'e FK gerektigi icin
             ara bir tabloya ihtiyac yoktur.
         */
        assertTrue(persistInATransaction(x, y0, y1));
    }


    @Test
    public void testJoin(){

        X_1_to_m_join x = new X_1_to_m_join();
        Y_1_to_m_join y0 = new Y_1_to_m_join();
        Y_1_to_m_join y1 = new Y_1_to_m_join();

        x.getYs().add(y0);
        x.getYs().add(y1);

        /*
            Diger durumlarda oldugu gibi, SQL veritabani aynidir. Yalnizca Y'den X'e
            FK bulunmaktadir.
         */
        assertTrue(persistInATransaction(x, y0, y1));
    }
}
