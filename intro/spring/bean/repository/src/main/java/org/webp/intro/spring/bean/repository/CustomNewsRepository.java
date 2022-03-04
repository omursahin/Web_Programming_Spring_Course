package org.webp.intro.spring.bean.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public interface CustomNewsRepository extends CrudRepository<NewsEntity, Long>
        /*
            bir repository bir başka arayüzde tanımlanan özelleşmiş fonksiyonellikle
            genişletilebilir
         */
        , MyCustomMethods {

    List<NewsEntity> findAllByCountry(String country);

    List<NewsEntity> findAllByAuthorId(String authorId);

    List<NewsEntity> findAllByCountryAndAuthorId(String country, String authorId);

    @Query("select n from NewsEntity n where n.country=?1 and n.authorId=?2")
    List<NewsEntity> customQuery(String country, String authorId);
}


/*
    yeni fonksiyonlar bir interface'de tanımlanmalı, sınıfta değil
 */
interface MyCustomMethods{

    @Transactional
    boolean changeText(long newsId, String text);
}


/*
    Ancak, bu interface'in yeni özelliklerinin implementasyonuna ihtiyaç duyulur.
    Bu türde sınıfların adı ise "Impl" ile biten sınıf ile aynı olmalıdır yoksa Spring
    tanımayacaktır.
 */
class MyCustomMethodsImpl implements MyCustomMethods{

    /*
        Bu türde Spring bean'ini istediğimiz herhangi bir şeye autowire ile bağlayabiliriz
     */
    @Autowired
    private EntityManager em;

    @Override
    public boolean changeText(long newsId, String text) {

        NewsEntity news = em.find(NewsEntity.class, newsId);
        if(news == null){
            return false;
        }

        news.setText(text);

        return true;
    }
}

