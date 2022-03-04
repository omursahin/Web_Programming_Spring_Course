package org.webp.intro.jee.ejb.lazy;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class EjbTest {


    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(A.class, B.class, Ejb.class)
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private Ejb ejb;


    @Test
    public void testGetLazy() throws Exception {

        int n = 5;
        long id = ejb.create(n);

        A a = ejb.getLazy(id);
        assertNotNull(a);

        try {
            /*
                Burası hata verecektir çünkü EJB'deki transaction süresince liste içeriği
                yüklenmedi
             */
            a.getList().size();
            fail();
        } catch (Exception e) {
            //beklenildiği gibi
            //FIXME, Arquillian JUNIT 5 ile birlikte assetThrows kullanılabilir
        }
    }

    @Test
    public void testGetInitialized() throws Exception {

        int n = 5;
        long id = ejb.create(n);

        A a = ejb.getInitialized(id);
        assertNotNull(a);

        assertEquals(5, a.getList().size());
        for(int i=0; i<a.getList().size(); i++){
            assertNotNull(a.getList().get(i).getId());
        }
    }

    @Test
    public void testGetWrongInitialized() throws Exception {

        int n = 5;
        long id = ejb.create(n);

        A a = ejb.getWrongInitialized(id);
        assertNotNull(a);

        try {
            a.getList().size();
            fail();
        } catch (Exception e) {
            //beklenildiği gibi
            //FIXME, Arquillian JUNIT 5 ile birlikte assetThrows kullanılabilir
        }
    }

    @Test
    public void testGetInitializedWithHibernate() throws Exception {

        int n = 5;
        long id = ejb.create(n);

        A a = ejb.getInitializedWithHibernate(id);
        assertNotNull(a);

        assertEquals(5, a.getList().size());
        for(int i=0; i<a.getList().size(); i++){
            assertNotNull(a.getList().get(i).getId());
        }
    }
}