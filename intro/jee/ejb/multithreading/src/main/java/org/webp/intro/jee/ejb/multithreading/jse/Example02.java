package org.webp.intro.jee.ejb.multithreading.jse;

import org.webp.intro.jee.ejb.multithreading.Counter;

public class Example02 implements Counter {

    /**
     * Buradaki "volatile" keyword'ü yerel thread cache'inden değil
     * RAM'den okuma/yazma yapması gerektiğini belirtmek için
     */
    private volatile int x;

    @Override
    public void incrementCounter() {
        x = x + 1;
    }

    @Override
    public int getCounter() {
        return x;
    }
}

