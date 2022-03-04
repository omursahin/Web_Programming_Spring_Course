package org.webp.intro.jee.ejb.multithreading.jse;

import org.webp.intro.jee.ejb.multithreading.Counter;
import org.webp.intro.jee.ejb.multithreading.CounterTestBase;

public class Example03Test extends CounterTestBase {

    @Override
    protected Counter getCounter() {
        return new Example03();
    }
}