package org.webp.intro.jee.ejb.framework.injection;

/**
 * Bütün inject edilmesi gereken alanların nesnelerini oluşturan servis
 */
public interface Injector {

    /**
     * AnnotatedForInjection ile işaretlenmiş tüm alanların inject edilmesi için gerekli sınıflar oluşturuluyor
     *
     * @return T sınıfının null olmayan örneğini gönderir
     * @throws IllegalArgumentException Eğer girdi geçersizse (null gibi) veya inject edilebilir nesnenin herhangi varsayılan bir değeri yoksa,
     *  0 parametreli constructor
     */
    <T> T createInstance(Class<T> klass) throws IllegalArgumentException;
}
