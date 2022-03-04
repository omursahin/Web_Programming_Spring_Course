package org.webp.intro.spring.bean.async;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AsyncServiceTest {


    @Autowired
    private AsyncService a;

    @Test
    public void testAsync() throws InterruptedException {

        int x = 0;
        a.setX(x);

        assertEquals(0 , a.getX());

        //Async beklenildiği gibi çalışırsa bu çağrı hemen dönecektir
        a.compute();

        //en az 2 saniye sürdüğünden henüz değer değişmemeli
        assertEquals(0 , a.getX());

        Thread.sleep(4_000); //kesin olarak değişmesi için 4 saniye kadar bekleyelim

        //Artık değer değişti
        assertNotEquals(0 , a.getX());
    }


    @Test
    public void testAsyncResult() throws Exception {

        Future<String> future = a.asyncResult();

        /*
            Burası asyncResult() tamamlanana kadar bloklanır.
            Not: Bu yalnızca bir örnektir. Bir async işlem yapıp
            onu tamamlanana kadar beklemek çok da mantıklı değildir.
            Temel hedef async tamamlanana kadar thread'in başka işlemler
            yapmasıdır.
         */
        String result = future.get();

        assertEquals("foo", result);
    }
}