package org.webp.intro.jee.jta.transactions.ejb;


import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
public class EJB_09_NEVER {

    @Resource
    private SessionContext ctx;


    /*
        eğer transaction içerisindeyse exception fırlatır
     */
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public boolean getTrue(){
        return true;
    }


    public boolean getFromRequired(){
        EJB_09_NEVER ejb = ctx.getBusinessObject(EJB_09_NEVER.class);
        return ejb.getTrue(); //eğer bir transaction oluşturursa hataya düşer
    }


    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public boolean getFromNotSupported(){
        EJB_09_NEVER ejb = ctx.getBusinessObject(EJB_09_NEVER.class);
        return ejb.getTrue(); //hataya düşmyecek çünkü transaction (varsa) askıya alındı
    }


    public boolean getFromRequiredBySuspendingFirst(){
        EJB_09_NEVER ejb = ctx.getBusinessObject(EJB_09_NEVER.class);
        return ejb.getFromNotSupported(); //transaction'ı askıya alacak
    }

}
