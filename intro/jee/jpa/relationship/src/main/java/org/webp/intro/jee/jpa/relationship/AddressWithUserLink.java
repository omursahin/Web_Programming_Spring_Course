package org.webp.intro.jee.jpa.relationship;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class AddressWithUserLink extends Address {

    //@Id'de dahil olmak uzere tum alanlar Address sinifinden kalitimla alinmistir

    @OneToOne(mappedBy = "addressWithUserLink")
    //Cift yonlu iliskinin sahibi User'dir bu yuzden,
    // User tablosundaki hangi alanin bu Address'i  mapleyecegi belirtilmelidir.
    private User user;

    public AddressWithUserLink(){}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
