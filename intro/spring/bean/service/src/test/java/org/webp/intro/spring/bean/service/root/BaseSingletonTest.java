package org.webp.intro.spring.bean.service.root;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//Bu annotation JUnit'de testin nasıl çalıştırılacağını değiştir özellikle de Spring context başlatır.
//Bir dakikanızı Maven aracılığı ile Wildfly indirmeniz gerekmediği ve Arquillian için manuel ayarlamalar
// yapmamanız gerektiğini anlamız için ayırmanızı rica ediciim :)
@ExtendWith(SpringExtension.class)
//SpringBoot kullandığımızı söylüyoruz, yalnızca Spring değil. Bu oldukça önemlidir çünkü
// @SpringBootApplication sınıfına bağlı olarak bean keşfi başlatılır
@SpringBootTest
public class BaseSingletonTest {

    /*
        Spring'de injection için annotation @Autowired'dır.
        Not: Ayrıca JEE CDI'de @Inject kullanılabilir bu da gayet çalışır (ancak JEE specs'i kütüphane olarak
        eklemek gerekir).

        Varsayılan olarak bir @Service "Singleton"'dır bu yüzden aşağıdaki iki değişken
        aynı referansa sahip olacaktır
     */

    @Autowired
    private BaseSingleton first;

    @Autowired
    private BaseSingleton second;


    @Test
    public void testSingleton(){

        /*
            "first"/"second"'ın aynı bean olduğunu doğrula
         */

        int a = first.getCounter();
        int b = second.getCounter();

        assertEquals(a, b);

        first.increment();

        int c = first.getCounter();
        assertEquals(a + 1, c);

        int d = second.getCounter();
        assertEquals(c, d);

        assertEquals(first, second);
    }

    @Test
    public void testConcurrency(){

        /*
            EJB @Singleton'un aksine @Service singleton otomatik olarak senkron olmaz.
            Bunu ayrıca belirtmeniz gerekir.
         */

        first.setCounter(0);

        final int nThreads = 4;
        final int loops = 100_000_000;

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < nThreads; i++) {

            Thread t = new Thread( () -> {
                for(int j=0; j<loops; j++){
                    first.increment();
                }
            });
            t.start();
            threads.add(t);
        }

        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        });

        int expected = nThreads * loops;
        int result = first.getCounter();

        System.out.println("Result: " + result);
        assertNotEquals(expected, result);
        assertTrue(result > loops);
    }
}