package org.webp.intro.jee.ejb.lazy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class A {

    @Id @GeneratedValue
    private Long id;

    //hatırlayın, varsayılan fetch türü LAZY'dir
    @OneToMany(cascade = CascadeType.ALL)
    private List<B> list = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<B> getList() {
        return list;
    }

    public void setList(List<B> list) {
        this.list = list;
    }
}
