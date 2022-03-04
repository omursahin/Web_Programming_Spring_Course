package org.webp.intro.jee.ejb.framework.proxy.se;

import org.junit.jupiter.api.Test;
import org.webp.intro.jee.ejb.framework.proxy.se.se.ObjectFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ObjectFactoryTest {

    @Test
    public void testProxy() {

        ObjectFactory factory = new ObjectFactory();
        //şu ana kadar çağrı yok
        assertEquals(0, factory.getTotalInvocationCount());

        A a = new AImp();
        assertEquals("X", a.methodX());
        assertEquals("Y", a.methodY());

        //hala 0, çünkü A proxy yapılmadı
        assertEquals(0, factory.getTotalInvocationCount());

        A proxy = factory.createInstance(A.class, AImp.class);
        assertNotNull(proxy);

        //hala aynı davranış
        assertEquals("X", proxy.methodX());
        assertEquals("Y", proxy.methodY());

        //şimdi çağrılar kesildi (intercepted) ve arada işlemler gerçekleşti
        assertEquals(2, factory.getTotalInvocationCount());
    }

}