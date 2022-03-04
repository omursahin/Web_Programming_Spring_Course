package org.webp.intro.jee.jpa.relationshipsql.onetomany.bidirectional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class X_1_to_m_bi {

    @Id
    @GeneratedValue
    private Long id;

    /*
        Cift yonlu iliskide sahip her zaman @ManyToOne'dan biridir
     */
    @OneToMany(mappedBy = "x")
    private List<Y_1_to_m_bi> ys = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public List<Y_1_to_m_bi> getYs() {
        return ys;
    }

    public void setYs(List<Y_1_to_m_bi> ys) {
        this.ys = ys;
    }
}
