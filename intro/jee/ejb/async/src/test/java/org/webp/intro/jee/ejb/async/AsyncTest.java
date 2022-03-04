package org.webp.intro.jee.ejb.async;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.*;

//@Asynchronous testi için embedded değil gerçek bir JEE container'ına ihtiyacımız var
@RunWith(Arquillian.class)
public class AsyncTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class).addClasses(A.class);
    }

    @EJB
    private A a;

    @Test
    public void testAsync() throws InterruptedException {

        int x = 0;
        a.setX(x);

        assertEquals(0 , a.getX());

        //Eğer Async doğru çalışıyorsa if Async works as expected, bu çağrı hemen dönmelidir
        a.compute();

        //Burası en az 2 saniye süreceğinden henüz x değişmemiş olmalı
        assertEquals(0 , a.getX());

        Thread.sleep(4_000); //emin olmak için 4 saniye kadar bekleyelim

        //Artık değer değişmiş olmalı
        assertNotEquals(0 , a.getX());
    }
}