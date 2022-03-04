package org.webp.intro.spring.bean.service.root.internal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@ExtendWith(SpringExtension.class)
//Not: @SpringBootApplication super-package içinde olduğundan, Application.class belirtmeye
// gerek yok
@SpringBootTest
public class BasePrototypeTest {

    /*
        Bu ikisi farklı bean örnekleri olacak
     */

    @Autowired
    private BasePrototype first;

    @Autowired
    private BasePrototype second;


    @Test
    public void testPrototype(){

        /*
            first/second'ın farklı türde bean olduğunun kontrolü
         */
        int a = first.getCounter();
        int b = second.getCounter();

        assertEquals(a, b);

        first.increment();

        int c = first.getCounter();
        assertEquals(a + 1, c);

        int d = second.getCounter();
        assertNotEquals(c, d);
        assertEquals(b, d);

        assertNotEquals(first, second);
    }
}