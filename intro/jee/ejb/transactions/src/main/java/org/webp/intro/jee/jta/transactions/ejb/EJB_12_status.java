package org.webp.intro.jee.jta.transactions.ejb;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.transaction.TransactionSynchronizationRegistry;

import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

@Singleton
public class EJB_12_status {

    /*
        Mevcut aktif transactionın (varsa) durumunu kontrol etmek için
        bu inject edilen kaynağı (resource) kullanabilir
     */
    @Resource
    private TransactionSynchronizationRegistry tsr;

    /*
        neden -1? yalnızca 0 dışında bir değer (STATUS_ACTIVE anlamına gelir)
        atandığından emin olmak için
     */

    private int transactionStatusInPostConstruct = -1;


    @PostConstruct
    public void init(){
        transactionStatusInPostConstruct = tsr.getTransactionStatus();
    }

    public int getInRequired(){
        return tsr.getTransactionStatus();
    }

    @TransactionAttribute(NOT_SUPPORTED)
    public int getInNotSupported(){
        return tsr.getTransactionStatus();
    }


    public int getTransactionStatusInPostConstruct() {
        return transactionStatusInPostConstruct;
    }

}
