package org.webp.intro.jee.ejb.stateless;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
    Not: Glassfish'de gömülü olarak gelen veritabanı Derby'dir ve Derby USER gibi tablo isimlerinden
    hoşlanmaz.
 */
@Table(name = "user_data")
@Entity
public class User {

    @Id //not, burası otomatik oluşturulmaz
    private String userId;

    private String name;

    private String surname;


    public User(){}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
