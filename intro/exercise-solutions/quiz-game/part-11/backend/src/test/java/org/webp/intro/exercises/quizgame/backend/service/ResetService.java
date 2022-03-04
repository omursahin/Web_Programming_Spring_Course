package org.webp.intro.exercises.quizgame.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webp.intro.exercises.quizgame.backend.entity.*;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Service
@Transactional
public class ResetService {

    @PersistenceContext
    private EntityManager em;

    public void resetDatabase(){
        //ElementCollection için native SQL kullanılmalı
        Query query = em.createNativeQuery("delete from user_roles");
        query.executeUpdate();

        deleteEntities(MatchStats.class);
        deleteEntities(User.class);
        deleteEntities(Quiz.class);
        deleteEntities(SubCategory.class);
        deleteEntities(Category.class);
    }

    private void deleteEntities(Class<?> entity){

        if(entity == null || entity.getAnnotation(Entity.class) == null){
            throw new IllegalArgumentException("Invalid non-entity class");
        }

        String name = entity.getSimpleName();

        /*
            Not: String yerine Class<?>'ı girdi olarak göndererek SQL injection'dan
            korunmalıyız. Ancak burası test kodu olduğu için bir problem yaratmayacaktır.
            Yine de iyi bir alışkanlık değildir. Bütün veritabanını silecek kodunuz
            olduğunda her zaman güvenlik konusunda paranoyak olun
         */

        Query query = em.createQuery("delete from " + name);
        query.executeUpdate();
    }

}
