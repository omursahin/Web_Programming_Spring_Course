package org.webp.intro.jee.jta.transactions.ejb;



import org.junit.Test;

import javax.ejb.EJB;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;


public class EJB_06_proxyTest extends TestBase{

    @EJB
    private EJB_06_proxy ejb;

    @Test
    public void testProxy(){

        String proxiedClass = ejb.getClass().getName();
        String actualClass = ejb.getClassNameFromEJBInstance();

        //mevcut, orijinal sınıf
        assertEquals(EJB_06_proxy.class.getName(), actualClass);


        // bu tam olarak yukarıdaki assertion ile aynıdır.
        // Ancak olası bir isim/package değişikliğinde (IDE'ye de bağlıdır) muhtemelen
        // değiştirilmeyeceğinden bunun yerine yukarıdaki gibi yazmak daha doğrudur
        assertEquals("org.webp.intro.jee.jta.transactions.ejb.EJB_06_proxy", actualClass);

        //proxy sınıf ise bambaşka bir hikayedir...
        assertNotEquals(proxiedClass, actualClass);

        System.out.println("\n\nProxy class name: "+proxiedClass+"\n");

        // NOT: isminin ne olacağını bilemeyiz hatta farklı EJB containerında ve JEE versiyonunda
        // bu isim de değişebilir. Buradaki assertion açıklama için bulunmaktadır böyle bir
        // assertion yazmamlısınız çünkü oldukça kırılgandır.
        assertTrue(proxiedClass.contains("$"));


        //Proxy hala geçerli bir EJB_06_proxy, yani bir alt sınıf
        assertTrue(EJB_06_proxy.class.isAssignableFrom(ejb.getClass()));

        //Ancak ekstra metotları bulunuyor
        Method[] proxiedMethods = ejb.getClass().getDeclaredMethods();
        assertNotEquals(proxiedMethods.length, ejb.getMethodsFromEJBInstance().length);

        Set<String> methodNames = Arrays.asList(ejb.getMethodsFromEJBInstance()).stream()
                .map(Method::getName)
                .collect(Collectors.toSet());

        Set<String> addedMethods = Arrays.asList(proxiedMethods).stream()
                .map(Method::getName)
                .filter(name -> ! methodNames.contains(name))
                .collect(Collectors.toSet());


        for(String name : addedMethods){
            System.out.println("Added method: "+name);
        }

        System.out.println("\n\n");
    }
}
