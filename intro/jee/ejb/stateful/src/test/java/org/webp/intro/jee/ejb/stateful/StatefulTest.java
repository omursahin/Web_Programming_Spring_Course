package org.webp.intro.jee.ejb.stateful;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.ejb.EJB;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class StatefulTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(A.class, Counter.class, StatefulCounter.class, StatelessCounter.class);
    }


    @EJB
    private A a;

    @Test
    public void testStateful(){

        a.increment();
        a.increment();
        a.increment();
        int expected = 3;

        /*
            Burada StatefulCounter sayacın değerinin her zaman aynı olduğunu biliyoruz.
            Stateful JEE kullanırken dikkatli olmak gerekir. Eğer her bir kullanıcı için
            bir stateful nesne varsa ve web sayfanız 1m kullanıcıya sahip ise 1m EJB nesnesi
            memory'de tutulur. Bunu engellemek için JEE container'ı diske kaydederek aynı anda
            küçük bir miktarda örneği hafızada tutabilir.
         */
        assertEquals(expected, a.getStateful());

        /*
            Stateless için herhangi bir şey söylenemez... 3, 0, veya herhangi bir değere
            sahip olabilir... hepsi de JEE container'ının proxyde ne kullanmaya karar verdiğine
            bağlıdır...
         */
        System.out.println("Stateless: " + a.getStateless());
    }
}