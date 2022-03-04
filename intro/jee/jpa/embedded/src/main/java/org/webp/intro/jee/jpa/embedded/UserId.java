package org.webp.intro.jee.jpa.embedded;


import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Embeddable nesneler ID olarak kullanıldığında Serializable olarak implement edilmelidir yoksa
 * runtime exception alırsınız. Sebebi için okuyabilirsiniz::
 * https://stackoverflow.com/questions/9271835/why-composite-id-class-must-implement-serializable
 *
 * "Serializable" nesne bit-string'e çevirilebilir ve diske kaydedilebilir anlamına gelir.
 * Varsayılan olarak, açıkça Serializable olarak belirtilmediği sürece Java nesneleri serializable değildir.
 * Tüm nesne alanları Serializable olmalıdır (not: String Serializable'dır).
 * Eğer bazı alanlar olamayacaksa "transient" anahtar kelimesi kullanılabilir.
 *
 * Not: Sunucular nesneleri yerel olarak (örneğin hard-diskte) tutmaya ihtiyaç duyabilir.
 * Ancak, bu nesneleri network aracılığı ile göndermek güvenlik riski barındırır (saldırıya açık bir konudur).
 * Bu gibi durumlarda doğrudan nesne göndermek yerine JSON/XML gibi formatlara çevirilir
 */
@Embeddable
public class UserId implements Serializable {

    /*
        Bu arada name/surname'i id olarak kullanmak iyi bir fikir değildir çünkü birden fazla insan aynı kombinasoyna sahip olabilir.
     */

    private String name;
    private String surname;

    public UserId(){}

    /*
       Hem "equals" hem de "hashCode" gereklidir. getter/setter gibi otomatik olarak oluşturulabilir.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return Objects.equals(name, userId.name) &&
                Objects.equals(surname, userId.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
