package org.webp.intro.jee.jpa.validation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.time.LocalDate;


/*
    Kısıtlamalar (constraints) neden kullanılır? En az 3 temel sebebi bulunmaktadır:

    1) Eğer bir şeyler yolunda gitmezse (kodda bug bulunması gibi), olabildiği kadar hızlı bir şekilde hata meydana
    gelmesini istersiniz (debeg sırasında yardımcı olacaktır)

    2) sınırları belli bir dokumantasyon olusturmakta kullanılabilir

    3) bazı zararlı ataklardan korunmak için kullanılabilir (bütün hard diski tüketecek türde bir saldırıyı engellemek gibi)

    Var olan bütün annotation'lara buradan bakabilirsiniz:
    https://javaee.github.io/javaee-spec/javadocs/javax/validation/constraints/package-summary.html
 */

@UserClassConstraints //özel kısıtlamalar
@Entity
public class User {

    @Id @GeneratedValue
    private Long id;

    /*
        Bu kısıtlamada name alanı kesinlikle olmalı ve en az 2 maksimum 128 karakter olması gerektiği
        belirtilmiştir. Üst sınırın özellikle de kullanıcıdan gelen alanlar için her zaman belirtilmiş
        olması oldukça önemlidir. Çünkü 2 milyar karakter uzunluğunda (MAX_INT) bir karakter yaklaşık
        olarak 2 GB alan tutacaktır
     */
    @NotNull
    @Size(min = 2 , max = 128)
    private String name;

    //middleName alanı girilmeyebilir ancak girilirse de çok uzun olmamalıdır (max=128)
    @Size(min = 0 , max = 128)
    private String middleName;

    /*
        NotBlank:
        null olamaz ancak boş bırakılabilir demektir

        Not: Eğer space'e izin vermek istemezseniz regular expression kullanmanız gerekir
      */
    @NotBlank
    @Size(min = 2 , max = 128)
    private String surname;

     /*
        Eğer Java'da ZonedDateTime (Java 8) gibi tarihleri kullanmak isterseniz java.time.* paketini kullanın
        Date (Java<=7) paketini değil. java.util.Date kullanımdan kaldırılmış gibi düşünebilirsiniz.

        Not: Yalnızca tarih kullanacak, zaman kullanmayacaksanız LocalDate kullanabilirsiniz (doğum tarihinde olduğu gibi).
        Eğer zaman dilimi ve zaman kullanmak isterseniz ZonedDateTime kullanabilirsiniz çünkü aşağıdaki değerleri içeriyor olacaktır:

        - tarih: ör 1/1/2017
        - zaman: ör 16:43:23
        - zaman dilimi: ör CET (Central European Time) and UTC (Coordinated Universal Time)

        Not: JPA 2.1 (JEE 7) Java 8 zaman nesnelerini desteklemezken Hibernate destekler.
        JPA 2.2 (JEE 8'deki) doğrudan desteklemektedir.
     */


    //gelecek bir zaman diliminde olamaz...
    @Past  // aynı zamanda @Future kısıtlaması da bulunmaktadır
    @NotNull
    private LocalDate dateOfRegistration;


    @Age(min = 18) //bu özelleşmiş bir kısıtlamadır (Age dosyasına bakınız)
    @NotNull
    private LocalDate dateOfBirth;

    /*
        Regular Expression kullanarak String ifadenin neleri içermesi gerektiğini tanımlayabiliriz.
        Örneğin bir e-mail adresinin içerisinde bir @ bulunması gerektiği söylenebilir

        Ayrıntı için: https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html

        ^  : başlangıç
        $  : son
        [] : karakterler, örneğin [A-Z] büyük harf veya küçük harf için [a-z] veya sayılar için [0-9]
        +  : Bir veya daha fazla, örneğin [A-Z]+ 1 veya daha fazla büyük harf
        *  : 0 veya daha fazla
        {m,M} : en az m sefer en fazla M sefer

        Not: bu yalnızca bir örnektir bunun yerine @Email kullanınız
     */
    @NotNull
    @Column(unique=true)
    @Pattern(regexp =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String email;

    public User(){}

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

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(LocalDate dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
