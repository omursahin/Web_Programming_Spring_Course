package org.webp.intro.jee.jpa.table;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/*
    "MovieDetails" isimi bir tablo bulunmamaktadir o yuzden hangi tablo maplenecek
    belirtmek gerekmektedir
 */
@Table(name = "MOVIE")
@Entity
public class MovieDetails {

    @Column(name = "ID") @Id
    private Long id;

    private String title; //aynı sütun adı

    //"directoryName" adında bir sütun yok bu yüzden "DIRECTOR" ile mapliyoruz
    @Column(name = "DIRECTOR")
    private String directorName;


    public MovieDetails(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }
}
