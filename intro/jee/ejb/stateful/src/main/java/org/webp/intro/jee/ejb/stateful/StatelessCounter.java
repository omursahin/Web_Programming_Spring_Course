package org.webp.intro.jee.ejb.stateful;

import javax.ejb.Stateless;

/*
    Bu senaryo hatalıdır: @Stateless bir EJB'de bir state oluşturmaya çalışmak yanlıştır
 */
@Stateless
public class StatelessCounter extends Counter{
}
