package org.webp.intro.spring.testing.coverage.instrumentation;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InstrumentationTest {

    @BeforeEach
    public void reset(){
        ExecutionTracer.reset();
    }

    @Test
    public void testNotInstrumented_new(){

        /*
            Instrumentation yalnızca instrumentation classloader'ı kullanıldığında yapılır
         */

        double cov = ExecutionTracer.getCoverage(FooImp.class.getName());

        assertEquals(0, cov, 0.0001);

        Foo foo = new FooImp();
        foo.printString();

        cov = ExecutionTracer.getCoverage(FooImp.class.getName());

        assertEquals(0, cov, 0.0001);
    }

    @Test
    public void testNotInstrumented_classLoader() throws Exception {

        double cov = ExecutionTracer.getCoverage(FooImp.class.getName());

        assertEquals(0, cov, 0.0001);

        /*
            Bir metotta ilk defa bir sınıf gördüğümüzde, bu sınıf metodun kendisi ile
            aynı sınıf yükleyici (classloader) tarafından yüklenecektir.
            Bu nedenle, aşağıdaki kod tam olarak şuna eşdeğerdir:

            Foo foo = new FooImp();

            Daha da ilginci, aşağıdaki "loadClass(...)" aslında önbelleğe alınmış,
            önceden yüklenmiş bir sınıf tanımı alıyor. Peki neden?
            FooImp.class, yürütüldüğünde testin başında zaten yüklendiğinden:

            double cov = ExecutionTracer.getCoverage(FooImp.class.getName());
         */
        Foo foo = (Foo) this.getClass().getClassLoader().loadClass(FooImp.class.getName()).newInstance();
        foo.printString();

        cov = ExecutionTracer.getCoverage(FooImp.class.getName());

        assertEquals(0, cov, 0.0001);
    }

    @Test
    public void testClassloaderMismatch() throws Exception {

        InstrumentingClassLoader cl = new InstrumentingClassLoader(FooImp.class.getName());

        Foo notInst = new FooImp();
        assertTrue(notInst instanceof FooImp);


        //yeni classloader ile sınıfı yükle
        Foo instr = (Foo) cl.loadClass(FooImp.class.getName()).newInstance();
        assertNotNull(instr);

        /*
            Burada aynı isimde 2 sınıf tanımımız var ancak bir sınıf yalnızca
            adıyla tekil olarak tanımlanamaz.
            Örneğin:

            org.webp.testing.instrumentation.FooImp

            sınıf adının yanında onu yükleyen classloader ile birlikte tanımlanır.
            Yani aynı isim ve aynı koda sahip iki farklı (ancak genişletilmiş/instrument edilmiş)
            sınıfa sahibiz. Bu ikisi de aynı interface ile implement edilmiş.
         */
        assertEquals(notInst.getClass().getName(), instr.getClass().getName());
        assertFalse(instr instanceof FooImp);
        assertNotEquals(notInst.getClass(), instr.getClass());
    }

    @Test
    public void testCoverage() throws Exception{

        String name = FooImp.class.getName();
        InstrumentingClassLoader cl = new InstrumentingClassLoader(name);

        double a = ExecutionTracer.getCoverage(name);
        assertEquals(0, a, 0.0001);

        Foo foo = (Foo) cl.loadClass(FooImp.class.getName()).newInstance();

        foo.printString();
        double b = ExecutionTracer.getCoverage(name);
        assertTrue(b > a);

        foo.add(1,2);
        double c = ExecutionTracer.getCoverage(name);
        assertTrue(c > b);

        foo.add(1,2);
        double d = ExecutionTracer.getCoverage(name);
        //aynı metot/parametreleri çağırınca kapsam aynı kalmalı
        assertTrue(d == c);

        foo.isPositive(5);
        double e = ExecutionTracer.getCoverage(name);
        assertTrue(e > d);

        foo.isPositive(-3);
        double f = ExecutionTracer.getCoverage(name);
        assertTrue(f > e);

        assertEquals(1.0, f, 0.0001);
    }

}