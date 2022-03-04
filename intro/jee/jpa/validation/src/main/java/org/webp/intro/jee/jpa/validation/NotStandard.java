package org.webp.intro.jee.jpa.validation;


import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * JEE 7'de, @NotBlank ve @Email Hibernate'de bulunurken JPA'da bulunmaz.
 * Bu kısıtlamaları Hibernate dışında kullanamazsınız. Ancak JEE 8 ile birlikte bu standart olmayan
 * kısıtlamalar JPA'ya da eklenmiştir
 *
 * Ancak hala Hibernate'de bulunan ancak JPA'da bulunmayan bazı kısıtlamalar bulunmaktadır
 * Örnekler için:
 * https://docs.jboss.org/hibernate/validator/5.1/api/org/hibernate/validator/constraints/package-summary.html
 *
 */
@Entity
public class NotStandard {

    @Id
    @GeneratedValue
    private Long id;

    /*
       @Length (Hibernate'de) eşdeğeri @Size (JPA'da).
       @Size kullanmak daha iyidir.
     */
    @NotBlank
    @Length(min=2, max = 128)
    private String  name;

    /*
        Burası Hibernate için özeldi ancak JPA 2.2 ile birlikte JPA'ya da eklendi
     */
    @Email
    private String email;

    /*
        Bir URL'in kendine ait yapısı bulunur. Bakınız:
        https://tools.ietf.org/html/rfc3986
     */
    @URL
    private String homePage;

    /**
     * Range'in minimum ve maksimum değerleri bulunur.
     *
     * JPA'da @Min ve @Max annotation'ları da bulunur
     */
    @Range(min = 0, max = 150)
    private Integer ageInYears;


    public NotStandard() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public Integer getAgeInYears() {
        return ageInYears;
    }

    public void setAgeInYears(Integer ageInYears) {
        this.ageInYears = ageInYears;
    }
}
