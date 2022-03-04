package org.webp.intro.jee.jta.transactions.ejb;



import org.junit.Test;

import javax.ejb.EJB;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class EJB_07_MANDATORYTest extends TestBase{

    @EJB
    private EJB_07_MANDATORY ejb;

    @Test
    public void testWrite(){

        String name = "bar";

        try{
            ejb.createFooMandatory(name);
            fail();
        } catch (Exception e){
            //Beklenildiği gibi çünkü transaction olmayan bir metottan zorunlu transaction içeren
            // bir metot çağırılmaya çalışılıyor
        }

        try{
            ejb.createFooSupports(name);
            fail();
        } catch (Exception e){
            //Transaction dışında yazmaya çalıştığı için SUPPORTS bile hataya düşer
        }
    }


    @Test
    public void testRead(){

        String name = "bar";

        ejb.createFooRequired(name);

        try{
            ejb.isPresentMandatory(name);
            fail();
        } catch (Exception e){
            //Beklenildiği gibi çünkü transaction olmayan bir metottan zorunlu transaction içeren
            // bir metot çağırılmaya çalışılıyor
        }

        //okuma işlemi transaction'a ihtiyaç duymaz bu yüzden transaction dışında da çalışacaktır
        boolean found = ejb.isPresentSupports(name);
        assertTrue(found);
    }
}