package org.webp.intro.spring.security.manual.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name = "user_table")
public class User {

    @Id
    private String userId;

    /**
     * Veritabanı veya herhangi bir yerde şifreyi asla basit text olarak tutmayın
     * Bunun yerine salt ile birleştirilmiş hash olarak tutun.
     */
    @NotBlank
    private String password;


    public User(){}


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
