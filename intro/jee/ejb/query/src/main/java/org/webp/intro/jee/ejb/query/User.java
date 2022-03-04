package org.webp.intro.jee.ejb.query;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "query_User")
@Entity
public class User {

    @Id @GeneratedValue
    private Long id;


    private String name;

    @OneToMany
    private List<Comment> comments;

    @OneToMany
    private List<Post> posts;

    /*
        Kısıtlama:  counter == comments.size() + posts.size()

        Toplam post/comment sayısı için bir sayaç tutmanız bazı dezavantajları bulunmaktadır:
        - veritabanı şemasının değiştirilmesi gerektiğinden ölçeklenebilir değildir (yeni bir istatistik bilgisi gerekirse?)
        - ekstra alan kaplar
        - her seferinde sayacın güncellendiğinden emin olmak gerekir

        Ancak bazı iyi yönleri de vardır:
        - Eğer sıklıkla kullanılan bir alansa tekrar hesaplamaya zaman harcanmaz
     */
    private int counter;


    public User() {
        comments = new ArrayList<>();
        posts = new ArrayList<>();
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

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
