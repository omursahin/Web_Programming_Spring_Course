package org.webp.intro.spring.testing.selenium.jsftests.service;


import org.junit.jupiter.api.Test;
import org.webp.intro.spring.jsf.ex01.CounterBean;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CounterBeanTest {

    /*
        CounterBean içindeki mantık herhangi JEE/Spring özellikleri ile bağlantılı değildir bu yüzden
        basit birim testlerle test edilebilir
     */

    @Test
    public void testIncr(){
        CounterBean cb = new CounterBean();
        int x = cb.getCounter();
        cb.increaseCounter();
        int res = cb.getCounter();
        assertEquals(x+1, res);
    }

    @Test
    public void testFailedDecr(){
        CounterBean cb = new CounterBean();
        cb.reset();
        int res = cb.getCounter();
        assertEquals(0, res);
        cb.decreaseCounter();
        res = cb.getCounter();
        assertEquals(0, res);
    }

    @Test
    public void testDecr(){
        CounterBean cb = new CounterBean();
        int x = cb.getCounter();
        cb.increaseCounter();
        assertEquals(x+1, cb.getCounter());
        cb.decreaseCounter();
        assertEquals(x, cb.getCounter());
    }

    @Test
    public void testIncrAndDecr(){
        CounterBean cb = new CounterBean();
        cb.reset(); //0
        cb.decreaseCounter(); //etki olmamalı
        cb.increaseCounter();  // 1
        cb.increaseCounter();  // 2
        cb.decreaseCounter(); // 1
        cb.increaseCounter();  // 2
        assertEquals(2, cb.getCounter());
    }
}