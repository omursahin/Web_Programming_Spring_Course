package org.webp.intro.jee.ejb.multithreading.jse;

import org.webp.intro.jee.ejb.multithreading.Counter;

public class Example03 implements Counter {


    private volatile int x;

    @Override
    public void incrementCounter() {
        /*
            Bir nesneye LOCK koyabiliriz (bu örnekte "this").
            Bu kod bloğuna LOCK bırakılana kadar diğer thread'ler erişemez. Burayı yürütmeye çalışan
            diğer thread'ler beklemeye alınır.
         */
        synchronized (this) {
            x = x + 1;
        }
    }

    @Override
    public int getCounter() {
        return x;
    }
}
