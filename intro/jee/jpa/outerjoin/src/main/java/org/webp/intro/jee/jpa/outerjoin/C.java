package org.webp.intro.jee.jpa.outerjoin;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class C {

    @Id
    @GeneratedValue
    private Long id;

    public C() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
