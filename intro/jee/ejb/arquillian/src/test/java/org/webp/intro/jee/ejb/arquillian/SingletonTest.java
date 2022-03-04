package org.webp.intro.jee.ejb.arquillian;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

@RunWith(Arquillian.class)
public class SingletonTest {

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(A.class, Data.class)
                //sınıfların yanında, kaynak da eklenmeli
                .addAsResource("META-INF/persistence.xml");
    }


    @EJB
    private A singleton;

    /*
        Varsayılan olarak Arquillian her bir sınıf testi için tek bir deployment yapar.
        Yani testler aynı state'i paylaşırlar. Bu yüzden yan etkiler konusunda (DB veya EJB'ler)
        dikkatli olmak gerekir. Bütün test senaryoları birbirinden izole olmalıdır ve hangi sıra ile
        çalıştırıldığı önemli olmamalıdır.

        Bu özel durumda 2 test çalıştırıldığında "ignored" olarak işaretlenecektir çünkü "assume" kontrolü
        sonucunda state bir önceki test tarafından değiştirildiğinden hata verecektir. Hangi testin "ignored"
        durumunda düşeceği ise testin sonucuna göre farklılık göstermektedir.

        Bir test "pass", "fail" ve "ignored" durumlarına düşebilir
     */


    @Test
    public void testOnceSingleton(){

        assumeTrue(singleton.getInternalX() == 0);

        checkAndModifyInternalState();
    }

    @Test
    public void testASecondTimeSingleton(){

        assumeTrue(singleton.getInternalX() == 0);

        checkAndModifyInternalState();
    }

    @Test
    public void testOnceDB(){

        assumeTrue(singleton.readDB() == 0);

        checkAndModifyDB();
    }

    @Test
    public void testASecondTimeDB(){

        assumeTrue(singleton.readDB() == 0);

        checkAndModifyDB();
    }


    private void checkAndModifyInternalState(){
        assertEquals(0, singleton.getInternalX());
        singleton.incrementInternalX();
        assertEquals(1, singleton.getInternalX());
    }

    private void checkAndModifyDB(){
        assertEquals(0, singleton.readDB());
        singleton.updateDB(1);
        assertEquals(1, singleton.readDB());
    }
}