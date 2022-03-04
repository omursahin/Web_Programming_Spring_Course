package org.webp.intro.jee.jpa.lock.base;

import javax.persistence.*;


@Entity
public class User {

    @Id @GeneratedValue
    private Long id;

    /*
        @Version ile isaretlemenin yaninda optimistic lock icin farkli
        secenekler de bulunmaktadir.

        Var olan alanlarin degisiklikler icin kontrol edilmesi icin
        org.hibernate.annotations.OptimisticLocking de kullanabilirsiniz.

        Ancak, bu Hibernate'in bir ozelligidir JPA 2.1 standartlarinin
        bir parcasi degildir.

     */
    @Version
    private Integer version;

    private String name;

    public User(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
