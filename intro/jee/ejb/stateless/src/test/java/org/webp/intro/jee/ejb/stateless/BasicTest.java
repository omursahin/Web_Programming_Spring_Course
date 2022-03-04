package org.webp.intro.jee.ejb.stateless;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BasicTest {

    @Test
    public void testItWrong(){

        Basic basic = new Basic();
        assertNotNull(basic.getValue());

        /*
            Eğer bir bean'i doğrudan başlatırsanız yalnızca bir Java sınıfı olur
            EJB olmazlar. EJB'lere bütün fonksiyonellik container tarafından eklenir.
            Bu örnekte container'ın ekleyeceği herhangi bir ek fonksiyona ihtiyaç duymadığımız için çalışacaktır.
         */
    }
}