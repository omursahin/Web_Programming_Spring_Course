package org.webp.intro.spring.testing.mocking;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


//Not: burada özel bir runner yok
public class ServiceAMockitoTest {

    // service injection yok


    @Test
    public void testOK(){

        long okId = 0;
        long notOkId = 1;

        ServiceB serviceB = mock(ServiceB.class);
        when(serviceB.isOK(okId)).thenReturn(true);
        when(serviceB.isOK(notOkId)).thenReturn(false);

        ServiceA serviceA = new ServiceA(serviceB);

        String res = serviceA.check(notOkId);
        assertEquals("NOT OK", res);

        res = serviceA.check(okId);
        assertEquals("OK", res);
    }
}