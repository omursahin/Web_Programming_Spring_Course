package org.webp.intro.jee.jta.transactions.ejb;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;


@Stateless
public class EJB_10_multi_caller {

    @Resource
    private SessionContext ctx;

    /*
        Bu transaction sınırlarının farkında olmamız gerektiğinde tipik bir örnektir.
        Bean inject etmek oldukça yaygın bir senaryodur ancak veritabanı üzerinde çalışan yöntemlerle
        işlem yapmak istediğimizde ne olacak? Tek bir transaction'da mı gerçekleşecek yoksa her biri
        kendi transaction'ınını mı çağıracak? Değişkenlik gösterecektir.
     */
    @EJB
    private EJB_10_multi_base base;



    public void exe(boolean inNewTransaction, String first, String second){

        /*
            Burada 2 eleman eklemek istiyoruz ancak 2. eleman aynı transaction'da mı olmalı
            yoksa farklı mı bunu inceleyeceğiz
         */

        base.add(first);

        if(inNewTransaction){
            base.addInNewTransaction(second);
        } else {
            // eğer bu branch çalışırsa bir sonraki kontrol hataya düşecektir çünkü 2. eleman henüz
            // cache'de bulunur, veritabanına yazılmamıştır (yazma işlemi commit sonrasında yapılır ve
            // genelde bu da metodun sonunda gerçekleşir).
            base.add(second);
        }

        /*
            Eğer herhangi bir sebepten second elemanı veritabanında değilse mevcut işlemi geri alıyoruz.
            Not: bu kontrol yeni bir transaction'da gerçekleşecek çünkü REQUIRES_NEW sebebiyle mevcut
            transaction sıraya alınarak yeni transaction oluşturulacaktır.
            Hatırlatma: "em"'de gerçekleştirilen ekleme işlemi henüz veritabanına yazılmadı, em yalnızca
            cache'den oluşuyor ve bu içerik commit sonrası yazılacak.
         */
        if(! base.isPresentByCheckingOnNewTransaction(second)){
            ctx.setRollbackOnly();
        }
    }

}
