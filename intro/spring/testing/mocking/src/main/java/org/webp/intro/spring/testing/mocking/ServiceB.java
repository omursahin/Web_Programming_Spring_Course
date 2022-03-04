package org.webp.intro.spring.testing.mocking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
public class ServiceB {

    //burada annotation yok
    private final EntityManager em;

    /*
        burada "field" injection yerine "constructor" injection yapıyoruz
     */

    public ServiceB(@Autowired EntityManager em) {
        this.em = em;
    }

    public boolean isOK(long id){

        FooEntity entity = em.find(FooEntity.class, id);
        if(entity == null){
            return false;
        }

        return entity.getOK();
    }

    @Transactional
    public long createEntry(boolean ok){

        FooEntity entity = new FooEntity();
        entity.setOK(ok);

        em.persist(entity);

        return entity.getId();
    }
}
