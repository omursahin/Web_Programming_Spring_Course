package org.webp.intro.spring.testing.selenium.jsftests.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/*
    Veritabanını temizlemek için kullanılan yardımcı servis.
    Not: bu türd bir servisi src/test/java'de yazmak isteseniz de asla src/main/java
    altında yazmayın.
    Ayrıca bu türde testi production veritabanında canlı sistemde çalıştırmadığınızdan EMİN OLUN.
 */
@Service
@Transactional
public class DeleterService {

    @Autowired
    private EntityManager em;

    public void deleteEntities(Class<?> entity){

        if(entity == null || entity.getAnnotation(Entity.class) == null){
            throw new IllegalArgumentException("Invalid non-entity class");
        }

        String name = entity.getSimpleName();

        /*
            Not: String yerine Class<?>'ı girdi olarak göndererek SQL injection'dan
            korunmalıyız. Ancak burası test kodu olduğu için bir problem yaratmayacaktır.
            Yine de iyi bir alışkanlık değildir. Bütün veritabanını silecek kodunuz
            olduğunda her zaman güvenlik konusunda paranoyak olun
        */

        Query query = em.createQuery("delete from " + name);
        query.executeUpdate();
    }

}
