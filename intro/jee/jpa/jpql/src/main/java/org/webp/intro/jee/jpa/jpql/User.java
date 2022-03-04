package org.webp.intro.jee.jpa.jpql;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/*
    Eger Intellij kullaniyorsaniz ve query string'ler hata veriyorsa "File -> Project Structure -> Module"
    kismindan "Default JPA Provider" diyerek JPA'yi Hibernate olarak ayarlayin.
    Slaytlara bakabilirsiniz.
 */
@NamedQueries({
        @NamedQuery(name = User.GET_ALL, query = "select u from User u"),
        @NamedQuery(name = User.GET_ALL_IN_TURKEY, query = "select u from User u where u.address.country = 'Turkey' ")
})
@Entity
public class User {

    //Sorgu adlarinin static final fieldlar olmasi iyidir boyle kullanicilar tarafindan daha rahat erisim saglanabilir
    public static final String GET_ALL = "GET_ALL";
    public static final String GET_ALL_IN_TURKEY = "GET_ALL_IN_TURKEY";

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @ManyToMany
    private List<User> friends;


    public User(){}

    //Bu getter'in nasil degistirildigine dikkat edin
    public List<User> getFriends() {
        if(friends == null){
            friends = new ArrayList<>();
        }
        return friends;
    }

    public Address getAddress() {
        if(address == null){
            address = new Address();
        }
        return address;
    }

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

    public void setAddress(Address address) {
        this.address = address;
    }



    public void setFriends(List<User> friends) {
        this.friends = friends;
    }
}
