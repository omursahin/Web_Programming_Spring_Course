package org.webp.intro.jee.jpa.embedded;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class User {

    /*
        Bu sınıf tek bir tabloya map edilir ve her bir alanı bir sütun olur. Örneğin:

        User
          - name
          - surname
          - city
          - country
          - postcode
     */


    /*
        Bu, bir satırda anahtar olarak birden fazla sütunun birleşiminin kullanılması gerektiğinde
        gereklidir.
     */
    @EmbeddedId
    private UserId id;

    @Embedded
    private Address address;


    public UserId getId() {
        return id;
    }

    public void setId(UserId id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
