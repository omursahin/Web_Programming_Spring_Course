package org.webp.intro.jee.jpa.relationshipsql;

import org.junit.jupiter.api.Test;
import org.webp.intro.jee.jpa.relationshipsql.onetoone.bidirectional.X_1_to_1_bi;
import org.webp.intro.jee.jpa.relationshipsql.onetoone.bidirectional.Y_1_to_1_bi;
import org.webp.intro.jee.jpa.relationshipsql.onetoone.independent.X_1_to_1_ind;
import org.webp.intro.jee.jpa.relationshipsql.onetoone.independent.Y_1_to_1_ind;
import org.webp.intro.jee.jpa.relationshipsql.onetoone.unidirectional.X_1_to_1_uni;
import org.webp.intro.jee.jpa.relationshipsql.onetoone.unidirectional.Y_1_to_1_uni;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class OneToOneTest extends TestBase{

    @Test
    public void testUnidirectional(){

        X_1_to_1_uni x = new X_1_to_1_uni();
        Y_1_to_1_uni y = new Y_1_to_1_uni();

        x.setY(y);

        assertTrue(persistInATransaction(x, y));
    }

    @Test
    public void testBidirectional(){

        X_1_to_1_bi x = new X_1_to_1_bi();
        Y_1_to_1_bi y = new Y_1_to_1_bi();

        x.setY(y);
        y.setX(x);

        assertTrue(persistInATransaction(x, y));
    }

    @Test
    public  void testBidirectionalWrongFKs(){

        X_1_to_1_bi x = new X_1_to_1_bi();
        Y_1_to_1_bi y = new Y_1_to_1_bi();
        x.setY(y);
        y.setX(x);

        X_1_to_1_bi k = new X_1_to_1_bi();
        k.setY(y);

        /*
            Aslinda konsept olarak yanlistir, K nokta Y'i gosterirken Y tekrar ayni
            K'yi gosteremez. Ancak bu foreignkey kisitlamasini ihlal etmemektedir.
         */
        assertTrue(persistInATransaction(x, y, k));
    }

    @Test
    public  void testIndependent(){

        X_1_to_1_ind x = new X_1_to_1_ind();
        Y_1_to_1_ind y = new Y_1_to_1_ind();
        x.setY(y);
        y.setX(x);

        X_1_to_1_ind k = new X_1_to_1_ind();
        k.setY(y);

        /*
            Tam da beklenildigi gibi calismaktadir. Ancak SQL veritabanindaki
            mevcut tablolar cift yonlu durumlarda farklidir. Or: Y icin tabloda ekstra FK
         */
        assertTrue(persistInATransaction(x, y, k));
    }
}
