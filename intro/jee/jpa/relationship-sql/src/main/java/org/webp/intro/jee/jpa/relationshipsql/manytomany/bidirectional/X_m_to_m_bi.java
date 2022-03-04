package org.webp.intro.jee.jpa.relationshipsql.manytomany.bidirectional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;


@Entity
public class X_m_to_m_bi {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany
    private List<Y_m_to_m_bi> ys = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public List<Y_m_to_m_bi> getYs() {
        return ys;
    }

    public void setYs(List<Y_m_to_m_bi> ys) {
        this.ys = ys;
    }
}
