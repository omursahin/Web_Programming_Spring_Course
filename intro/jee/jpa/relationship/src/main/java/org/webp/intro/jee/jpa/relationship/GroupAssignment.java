package org.webp.intro.jee.jpa.relationship;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class GroupAssignment {

    @Id //Not, otomatik olarak uretilmez
    private Long id;

    private String text;

    //bu durumda, User bu iliskinin sahibidir (owner)
    @ManyToMany(mappedBy = "assignments")
    private List<User> authors;


    public GroupAssignment(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<User> getAuthors() {
        return authors;
    }

    public void setAuthors(List<User> authors) {
        this.authors = authors;
    }
}
