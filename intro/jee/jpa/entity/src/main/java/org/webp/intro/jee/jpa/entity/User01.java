package org.webp.intro.jee.jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

//By sınıf ayni isimdeki bir veritabani tablosunu maplemektedir.
@Entity
public class User01 {

    /*

        Bir unique id ihtiyaci (or, Primary Key) oldugunda bu satir kullanilir.
        Deger onemli degildir, DB bizim icin uretir.

        not: "long" yerine "Long" kullanma sebebi bu alanin "null" olabilme ihtimalidir.
        Aksi takdirde, "long" kulanilsaydi varsayilan deger "0" olurdu ki bu veritabani icin null
        degerden tamamen farklidir.
        Kural olarak, @Entity objelerde asla primitive alan (or, "int" ve "long") kullanilmamalidir.
        Bunu yerine null olabilir versiyonlari kullanilmalidir (or, "Integer" ve "Long").
     */
    @Id @GeneratedValue
    private Long id;

    private String name;
    private String surname;

    //Her bir entity boş bir default-constructor'a ihtiyaç duyar
    public User01(){}

    /*
        Aşağıdaki getter/setter'lar otomatik olarak üretilmiştir. Hem Intellij hem de
        Eclipse'te üretilebilir.
        Intellij için: sağ tık, "generate" ve ardından "getter and setter" seçilmelidir.
    */

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}

