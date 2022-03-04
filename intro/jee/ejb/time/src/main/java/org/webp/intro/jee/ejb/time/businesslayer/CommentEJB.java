package org.webp.intro.jee.ejb.time.businesslayer;

import org.webp.intro.jee.ejb.time.datalayer.Comment;
import org.webp.intro.jee.ejb.time.datalayer.News;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;


@Stateless
public class CommentEJB {

    @PersistenceContext
    private EntityManager em;


    /**
     {@code newsId} tarafından tanımlanmış News için comment oluştur
     */
    public void save(@NotNull Long newsId, @NotNull String author, @NotNull String draft){

        News news = em.find(News.class, newsId);
        if(news == null){
            return;
        }

        Comment comment = new Comment(null, draft, author);
        em.persist(comment);

        news.getComments().add(comment);
    }
}
