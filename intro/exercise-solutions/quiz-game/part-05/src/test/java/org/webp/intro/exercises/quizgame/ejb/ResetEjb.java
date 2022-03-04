package org.webp.intro.exercises.quizgame.ejb;

import org.webp.intro.exercises.quizgame.entity.Category;
import org.webp.intro.exercises.quizgame.entity.Quiz;
import org.webp.intro.exercises.quizgame.entity.SubCategory;

import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ResetEjb {

    @PersistenceContext
    private EntityManager em;

    public void resetDatabase(){
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
