package org.webp.intro.jee.ejb.stateless;



import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(Arquillian.class)
public class UserBeanInJEEContainerTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(UserBean.class, User.class)
                //sınıfların yanında kaynak da eklenir
                .addAsResource("META-INF/persistence.xml");
    }


    @EJB
    private UserBean bean;

    @Test
    public void testWithEmbeddedContainer(){

        String userId = "foo";

        assertFalse(bean.isRegistered(userId));

        bean.registerNewUser(userId,"a","b");

        assertTrue(bean.isRegistered(userId));
    }

    @Test
    public void testQuery(){

        /*
            Veritabanı resetlenmediğinden kaç kullanıcının bulunduğunu bilemeyiz.
            Bu sayı kaç testin çalıştırıldığına bağlı olarak değişecektir
         */
        long k = bean.getNumberOfUsers();

        bean.registerNewUser("0","a","b");
        bean.registerNewUser("1","a","b");
        bean.registerNewUser("2","a","b");

        long n = bean.getNumberOfUsers();
        assertEquals(k+3, n);
    }

    @Test
    public void testNullValue(){

        //EJB'de, @NotNull kontrolü çalışma zamanında JEE container'ı tarafından gerçekleştirilir
        //assertThrows(EJBException.class, () -> bean.registerNewUser("0", "a", null));

        try{
            bean.registerNewUser("0", "a", null);
            fail();
        }catch (EJBException e){
            //beklenildiği gibi
        }

    }
}
