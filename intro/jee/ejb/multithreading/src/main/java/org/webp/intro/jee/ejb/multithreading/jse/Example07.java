package org.webp.intro.jee.ejb.multithreading.jse;

import org.webp.intro.jee.ejb.multithreading.Counter;

import java.util.concurrent.atomic.AtomicInteger;

public class Example07 implements Counter {

    /*
        java.util.concurrent paketi eşzamanlı çalışmalar için pek çok API sağlar.
        Örneğin, AtomicInteger okuma/yazma işlemlerini atomik olarak yapabileceğiniz
        ve synchronized bloklarına ihtiyaç duymadığınız bir yapıdır. Elde edeceğiniz sonuç da
        aynıdır.
     */
    private final AtomicInteger x = new AtomicInteger(0);

    @Override
    public void incrementCounter() {
        x.incrementAndGet();
    }

    @Override
    public int getCounter() {
        return x.get();
    }
}