package org.webp.intro.jee.jpa.relationshipsql.onetomany.joincolumn;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class X_1_to_m_join {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    // Bunun anlami Y'de X'e isaret eden bir FK olacagini gosterir
    @JoinColumn(name = "foo_column")
    private List<Y_1_to_m_join> ys = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public List<Y_1_to_m_join> getYs() {
        return ys;
    }

    public void setYs(List<Y_1_to_m_join> ys) {
        this.ys = ys;
    }
}
