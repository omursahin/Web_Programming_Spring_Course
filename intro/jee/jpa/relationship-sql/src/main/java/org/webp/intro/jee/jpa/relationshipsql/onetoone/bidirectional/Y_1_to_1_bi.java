package org.webp.intro.jee.jpa.relationshipsql.onetoone.bidirectional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Y_1_to_1_bi {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(mappedBy = "y")
    private X_1_to_1_bi x;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public X_1_to_1_bi getX() {
        return x;
    }

    public void setX(X_1_to_1_bi x) {
        this.x = x;
    }
}
