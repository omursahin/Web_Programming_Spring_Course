package org.webp.intro.jee.ejb.stateless;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

/*
    Bir JEE container'ı binlerce örnek oluşturup her seferinde potansiyel olarak
    farklı bir örnek sunacağından bir stateless EJB herhangi dahili bir state tutmamalıdır
 */
@Stateless
public class UserBean {

    //Dependency injection: container bunu ekleyecektir
    @PersistenceContext
    private EntityManager em;

    public UserBean(){}

    //bütün EJB nesneleri transaction ile sarılacak ve eğer exception
    // meydana gelecek olursa işlemler geri alınacaktır
    public void registerNewUser(@NotNull String userId, @NotNull String name, @NotNull String surname){
        if(isRegistered(userId)){
            return;
        }

        User user = new User();
        user.setUserId(userId);
        user.setName(name);
        user.setSurname(surname);

        em.persist(user);
    }

    public boolean isRegistered(@NotNull String userId){
        User user = em.find(User.class, userId);
        return user != null;
    }

    public long getNumberOfUsers(){
        TypedQuery<Long> query = em.createQuery("select count(u) from User u", Long.class);
        long n = query.getSingleResult();
        return n;
    }
}
