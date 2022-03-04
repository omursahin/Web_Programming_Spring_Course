package org.webp.intro.jee.ejb.stateless;


import javax.ejb.Stateless;

/*
    Üç tipte Enterprise Java Bean (EJB) bulunur:
    - Stateless
    - Stateful
    - Singleton

    Bir sınıfı EJB yapmak için yalnızca ihtiyaç duyduğunuz annotation'ı kullanmanız yeterlidir
 */

@Stateless
public class Basic {

    /*
        EJB container (Wildfly veya GlassFish gibi) tarafından yönetilir.
        Temelde yaptığı eylemler aşağıdaki gibidir:
        - dependency injection
        - özel eylemler (otomatik transaction gibi)

        bu sınıf basit bir sınıftır ve her ikisine de ihtiyaç bulunmamaktadır
     */

    public String getValue(){
        return "something";
    }
}
