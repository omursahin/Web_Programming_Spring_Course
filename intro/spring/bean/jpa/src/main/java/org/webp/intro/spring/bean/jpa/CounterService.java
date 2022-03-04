package org.webp.intro.spring.bean.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;


@Service
public class CounterService {

    /*
        Inject edilmiş EntityManager thread için güvenlidir bu yüzden bu singleton
        nesnede kullanmak bir problem yaratmaz.

        Ayrıca burada JPA için bir ayar dosyası olmadığına da dikkat edin.
        örneğin "META-INF/persistence.xml" dosyası yok.
        Peki veritabanı nasıl ayarlanıyor?
        Eğer bir ayar dosyası vermezseniz SpringBoot classpath'i analiz eder (3. parti uygulamalar gibi)
        ve onlar için otomatik ayarlar oluşturur. pom.xml'de olduğu gibi H2 veritabanını import
        ettik ve SpringBoot otomatik olarak hangi veritabanını kullanmak istediğimizi algıladı.
     */
    @Autowired
    private EntityManager em;


    public Long getValueForCounter(long id){

        CounterEntity entity = em.find(CounterEntity.class, id);
        if(entity != null){
            return entity.getValue();
        }

        return null;
    }

    //yalnızca transactionın mevcut davranışını göstermek için gerekli
    public void clearCache(){
        em.clear();
    }

    @Transactional
    public long createNewCounter(){

        CounterEntity entity = new CounterEntity();
        entity.setValue(0L);

        em.persist(entity);

        return entity.getId();
    }

    public void incrementNotInTransaction(long id){

        CounterEntity entity = em.find(CounterEntity.class, id);
        if(entity == null){
            throw new IllegalArgumentException("No counter exists with id " + id);
        }

        entity.setValue(1L + entity.getValue());
    }


    @Transactional
    public void increment(long id){
        incrementNotInTransaction(id);
    }
}
