package org.webp.intro.jee.ejb.singleton;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;


import javax.ejb.EJB;

import static org.junit.Assert.*;

/*
    Intellij'de testleri çalıştırmadan önce:

    "mvn test"

    komutunu çalıştırmalısınız.

    Bu projenizi derleyecek ve "target" klasörüne WildFly kurarak Maven plugin'ini
    çalıştıracaktır. "mvn clean" (target içindeki her şeyi siler) komutunu çalıştırana
    kadar yalnızca bir sefer çalıştırmanız yeterlidir.

    Intellij'de "Run ..." ile testleri çalıştırmak isterseniz şu ayarlamaları yapmanız gerekir:
    - "Arquillian Containers" altında "Manual container configuration"
    - "Configuration" altındaki "Working directory", "$MODULE_DIR$" olmalıdır.

    Bütün testler için ayrı ayrı yapmamak için bunu bir sefer tamamı için ayarlayabilirsiniz
    "Defaults -> Arquillian JUnit"

 */

/*
    RunWith testleri çalıştırmak için özelleştirilmiş bir yoldur.
    Özellikle Arquillian kullanılırken JEE container'a otomatik deploy eder ve dependency
    testteki injection'ları çözümler
 */
@RunWith(Arquillian.class)
public class CounterTest {

    @Deployment
    public static JavaArchive createDeployment() {

        /*
            Burası verilen sınıflar ile Jar dosyası oluşturur ve WildFly'a deploy eder
         */

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(Counter.class, A.class, B.class);
    }


    /*
        JEE Container'ındaki aynı EJB burada test'e inject edilebilir böylelikle
        doğrudan çağırabiliriz

        Buradaki önemli nokta bu test sınıfındaki kod JEE container'ında da çalışır böylelikle
        dependency injection'ı kullanabiliriz. Bu eylemi Arquillian'daki @RunWith ele almaktadır.
     */
    @EJB
    private A a;
    @EJB
    private B b;
    @EJB
    private Counter counter;


    @Test
    public void testSingleton(){

        /*
            Counter singleton olduğundan inject edilen A ve B'de aynı instance olduğunu kontrol
            ediyoruz.
         */

        a.incrementCounter();
        b.incrementCounter();
        counter.increment();

        assertEquals(3, counter.get());
    }

}