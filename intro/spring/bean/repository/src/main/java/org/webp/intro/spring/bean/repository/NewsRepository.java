package org.webp.intro.spring.bean.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Hatırlatma CRUD anlamı: Create, Read, Update ve Delete.
 * <p>
 * @Repository annotation'ı kullanılarak arayüz CrudRepository olarak genişletilebilir. Böylelikle Spring
 * veritabanında işlem yapabileceği save(), exists(), findAll() gibi özellikleri sağlayacaktır. Böylelikle
 * tek satır kod ile çok fazla iş yapabilir oluyoruz.
 * <p>
 * Ayruca JPQL sorgularını manuel oluşturmak yerine EJB'deki metotlar doğrudan çağrıda bulunabilirler.
 * Burada Spring otomatik olarak proxy sınıftaki fonksiyon adlarına bağlı olarak kod oluşturacaktır.
 * Örneğin, eğer findAllByCountry isimli bir metot tanımlarsanız (yalnızca interface'de tanımlanırsa)
 * Spring ismi analiz edecek ve verilen ülke adını vererek bütün girdileri çekebileceğiniz fonksiyonu
 * oluşturacaktır. Eğer bir özelliği yanlış yazarsanız çalışma zamanı hatası alırsınız bu yüzden bunları
 * test etmek oldukça önemlidir. Ancak Intellij otomatik olarak bu fonksiyon ve isimleri kontrol edecek ve
 * hata olması durumunda uyaracaktır.
 */
@Repository
public interface NewsRepository extends CrudRepository<NewsEntity, Long> {

    List<NewsEntity> findAllByCountry(String country);


    List<NewsEntity> findAllByAuthorId(String authorId);

    /*
        "And" gibi bağlantı kelimeleri ile daha karmaşık sorgular yazabilirsiniz.
        Bu yapı basit sorgular için gerçekten çok uygundur ama karmaşıklar için değildir.

        Ayrıntılar için spring dokümantasyonunu inceleyiniz:
        https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#reference
     */
    List<NewsEntity> findAllByCountryAndAuthorId(String country, String authorId);

    /*
     * Metot isimlerine bağımlı kalmak yerine @Query annotation'ı ile özel sorgular oluşturabilirsiniz
     */
    @Query("select n from NewsEntity n where n.country=?1 and n.authorId=?2")
    List<NewsEntity> customQuery(String country, String authorId);

}
