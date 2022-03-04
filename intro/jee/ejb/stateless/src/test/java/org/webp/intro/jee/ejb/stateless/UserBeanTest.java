package org.webp.intro.jee.ejb.stateless;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class UserBeanTest {


    @Test
    public void testBeanAsRegularJava(){

        UserBean bean = new UserBean();

        /*
           Burası Null Pointer Exception hatası verecektir çünkü bean container tarafından
           yönetilmemektedir.
           Herhangi bir şekilde entity manager alsak bile (örneğin setter aracılığı ile) işlem yürütülemediği
           için hata oluşacaktır
         */
        assertThrows(NullPointerException.class, () -> bean.registerNewUser("a","b","c"));
    }

}