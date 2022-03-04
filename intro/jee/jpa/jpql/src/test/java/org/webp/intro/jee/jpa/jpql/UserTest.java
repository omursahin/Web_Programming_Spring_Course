package org.webp.intro.jee.jpa.jpql;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class UserTest {

    private EntityManagerFactory factory;
    private EntityManager em;


    @BeforeEach
    public void init() {
        factory = Persistence.createEntityManagerFactory("DB");
        em = factory.createEntityManager();
        addDefaultData();
    }

    @AfterEach
    public void tearDown() {
        em.close();
        factory.close();
    }

    private void addDefaultData() {

        User a = new User();
        a.setName("A");
        a.getAddress().setCity("Kayseri");
        a.getAddress().setCountry("Turkey");

        User b = new User();
        b.setName("B");
        b.getAddress().setCity("Yozgat");
        b.getAddress().setCountry("Turkey");

        User b2 = new User();
        b2.setName("B");
        b2.getAddress().setCity("Kayseri");
        b2.getAddress().setCountry("Turkey");

        User c = new User();
        c.setName("C");
        c.getAddress().setCity("Lisbon");
        c.getAddress().setCountry("Portugal");

        makeFriends(a, b);
        makeFriends(a, b2);
        makeFriends(a, c);

        makeFriends(b, b2);

        assertTrue(persistInATransaction(a, b, b2, c));
    }

    private void makeFriends(User x, User y) {
        x.getFriends().add(y);
        y.getFriends().add(x);
    }

    private boolean persistInATransaction(Object... obj) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            for (Object o : obj) {
                em.persist(o);
            }
            tx.commit();
        } catch (Exception e) {
            System.out.println("FAILED TRANSACTION: " + e.toString());
            tx.rollback();
            return false;
        }

        return true;
    }

    @Test
    public void testGetAll() {

        TypedQuery<User> query = em.createNamedQuery(User.GET_ALL, User.class);

        //READ operasyonu oldugu icin transaction'a gerek yok
        List<User> users = query.getResultList();

        assertEquals(4, users.size());
    }

    @Test
    public void testGetAllInTurkey() {

        TypedQuery<User> query = em.createNamedQuery(User.GET_ALL_IN_TURKEY, User.class);
        List<User> users = query.getResultList();

        assertEquals(3, users.size());
    }

    @Test
    public void testGetAllWithOnTheFlyQuery() {

        //Kendi sorgularinizi olusturabilirsiniz ancak bir sorgu birden fazla yerde
        // kullaniliyorsa isimlendirmek oldukca dogru olacaktir.
        TypedQuery<User> query = em.createQuery("select u from User u", User.class);
        List<User> users = query.getResultList();

        assertEquals(4, users.size());
    }

    @Test
    public void testCaseWhen() {

        // Bazi kontroller sonrasi farkli degerler dondurecekseniz
        // CASE WHEN THEN ELSE kullanabilirsiniz
        TypedQuery<String> query = em.createQuery(
                "select CASE u.address.city WHEN 'Kayseri' THEN 'yes' ELSE 'no' END " +
                        "from User u", String.class);

        List<String> kayseriCounters = query.getResultList();
        assertEquals(4, kayseriCounters.size());

        assertEquals(2, kayseriCounters.stream().filter(s -> s.equals("yes")).count());
        assertEquals(2, kayseriCounters.stream().filter(s -> s.equals("no")).count());
    }

    @Test
    public void testNew() {

        /*
            Veritabanindaki veriyi temel alarak yeni bir obje olusturabilirsiniz.
            Not: Bu ozellik verimlilik icin kullanislidir.
            Oldukca kucuk bir setini kullanacaginiz buyuk bir veri setini dusunun. Bu turde
            bir nesne olusturarak yalnizca ihtiyaciniz olan veriyi veritabanindan okuyabilirsiniz.
         */
        TypedQuery<Message> query = em.createQuery(
                "select NEW " + Message.class.getName() + "(u.name) from User u", Message.class);

        List<Message> messages = query.getResultList();

        assertEquals(4, messages.size());
        assertTrue(messages.stream().map(Message::getText).anyMatch(t -> t.equals("A")));
        assertTrue(messages.stream().map(Message::getText).anyMatch(t -> t.equals("B")));
        assertTrue(messages.stream().map(Message::getText).anyMatch(t -> t.equals("C")));
    }


    @Test
    public void testDistinct() {

        TypedQuery<String> query = em.createQuery(
                "select distinct u.name from User u", String.class);

        List<String> messages = query.getResultList();

        //Hatirlayin: 2 kullanicinin ayni adi bulunmaktaydi
        assertEquals(3, messages.size());
    }


    @Test
    public void testAvg() {

        //Ortalama kac arkadas var? 3+2+2+1 / 4 = 2
        TypedQuery<Double> queryAvg = em.createQuery(
                "select avg(u.friends.size) from User u", Double.class);

        double avg = queryAvg.getSingleResult();
        assertEquals(2.0, avg, 0.001);

        //diger operatorler ornek olsun diye var: count, max, min, sum

        TypedQuery<Long> querySum = em.createQuery(
                "select sum(u.friends.size) from User u", Long.class);

        long sum = querySum.getSingleResult();
        assertEquals(8L, sum);
    }

    @Test
    public void testBindingParameters() {

        assertEquals(3, findUsers("Turkey").size());
        assertEquals(2, findUsers("Turkey", "Kayseri").size());
    }

    private List<User> findUsers(String country) {

        TypedQuery<User> query = em.createQuery(
                "select u from User u where u.address.country = :country", User.class); //note the ":"
        query.setParameter("country", country);

        return query.getResultList();
    }

    private List<User> findUsers(String country, String city) {

        TypedQuery<User> query = em.createQuery(
                "select u from User u where u.address.country = ?1 and u.address.city = ?2",
                User.class);
        query.setParameter(1, country); //yep, it starts from 1, and not 0...
        query.setParameter(2, city);

        return query.getResultList();
    }


    @Test
    public void testInjection() {

        String param = "Turkey' or '1' = '1"; // this results in a tautology

        /*
            Burada aslinda veritabanindaki butun veriyi okuyoruz cunku:

            where u.address.country = '" + country + "'
            ->
            where u.address.country = 'Turkey' or '1' = '1'
            ->
            her zaman true doner
         */
        assertEquals(4, findUsers_IN_A_VERY_WRONG_WAY(param).size());

        // Bu hata verecektir istedigimiz gibi
        assertEquals(0, findUsers(param).size());
    }


    private List<User> findUsers_IN_A_VERY_WRONG_WAY(String country) {

        /*
            ASLA AMA ASLA boyle bir sey yazmayin. JPQL/SQL sorgularini asla + ile birlestirmeyin.
            Yoksa SQL injection'a yol acabilir
         */

        TypedQuery<User> query = em.createQuery(
                "select u from User u where u.address.country = '" + country + "'",
                User.class);
        return query.getResultList();
    }


    @Test
    public void testSubquery() {

        // farkli ulkeden en az bir arkadasa sahip butun kullanicilari bul
        TypedQuery<User> query = em.createQuery(
                "select u from User u where " +
                        "(select count(f) from User f where (u member of f.friends) and (u.address.country != f.address.country)) " +
                        "> 0",
                User.class);


        List<User> users = query.getResultList();
        assertEquals(2, users.size());
        assertTrue(users.stream().map(User::getName).anyMatch(n -> "A".equals(n))); //note the "Yoda-style" to prevent NPE
        assertTrue(users.stream().map(User::getName).anyMatch(n -> "C".equals(n)));
    }

    @Test
    public void testCriteriaBuilder() {

        /*
            String yazmaktan kacinmak icin CriteriaBuilder kullanabilirsiniz. Daha type-safe'dir.
            Ancak cok daha okumasi ve anlasilmasi zordur. Intellij'in JPQL syntax gosterimi
            dusunuldugunde, CriteriaBuilder pek de tavsiye edilmemektedir.
         */

        //Query query = em.createQuery("select u from User u where u.address.country = 'Turkey'");
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User> q = builder.createQuery(User.class);
        Root<User> u = q.from(User.class);
        q.select(u).where(builder.equal(u.get("address").get("country"), "Turkey"));

        TypedQuery<User> query = em.createQuery(q);
        assertEquals(3, query.getResultList().size());
    }


    @Test
    public void testFindUsersWith_SQL_insteadOf_JPQL() {

        //Query query = em.createQuery("select u from User u where u.address.country = 'Turkey'");
        Query query = em.createNativeQuery("SELECT * FROM User WHERE country = 'Turkey'", User.class);

        assertEquals(3, query.getResultList().size());

        /*
            Bazi durumlarda JPQL yerine dogrudan SQL kullanmak isteyebilirsiniz:
            - JPA/Hibernate tarafindan desteklenmeyen bazi ozel DB ozelliklerini kullanmak icin
            - JPA/Hibernate tarafindan uretilen sorgularin efektif olmadigi durumlarda
            - JPA/Hibernate hatali sonuc verdiginde (or: beklediginiz sonucu almadiginizda)

            Hatirlatma:
            - SQL dogrudan mevcut tabloda calisir
            - JPQL @Entity seviyesinde calisir (Kullanici entity'si country'e sahip degildir)
         */
    }


    @Test
    public void testFindUsersWithJOOQ() {

        //Query query = em.createQuery("select u from User u where u.address.country = 'Turkey'");
        //Query query = em.createNativeQuery("select * from User where country = 'Turkey'");

        DSLContext create = DSL.using(SQLDialect.H2);
        String sql = create
                .select()
                .from(table("User"))
                .where(field("country").eq("Turkey"))
                .getSQL(ParamType.INLINED);

        Query query = em.createNativeQuery(sql, User.class);

        List<User> results = query.getResultList();

        assertEquals(3, results.size());

        /*
           JOOQ SQL yazmak icin kullanilan oldukca populer ve kolay DSL (domain specific language)'dir.
           Type-safety yaninda buyuk faydalarindan biri de hedef DB icin SQL'i belli bir yapida calistirmaktir.
         */
    }

    @Test
    public void testBulkDeleteAll() {
        TypedQuery<User> all = em.createNamedQuery(User.GET_ALL, User.class);
        assertEquals(4, all.getResultList().size());

        //ayni anda birden fazla girdiyi silmek/guncellemek icin faydalidir
        Query delete = em.createQuery("delete from User u");

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            //veritabanini degistirdigi icin transaction icerisinde calistirilmalidir
            delete.executeUpdate();
        } catch (Exception e) {
            tx.rollback();
            fail();
        }

        assertEquals(0, all.getResultList().size()); //her sey silinmis olmalidir
    }

    @Test
    public void testBulkDelete() {

        TypedQuery<User> all = em.createNamedQuery(User.GET_ALL, User.class);
        assertEquals(4, all.getResultList().size());

        String country = "Turkey";

        //ayni anda birden fazla girdiyi silmek/guncellemek icin faydalidir
        Query delete = em.createQuery("delete from User u where u.address.country = :country ");
        delete.setParameter("country", country);

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            //veritabanini degistirdigi icin transaction icerisinde calistirilmalidir
            delete.executeUpdate();
            fail();
        } catch (Exception e) {
            tx.rollback();
            /*
                hata olmasi beklenmektedir cunku veritabanini tutarsiz bir bicimde birakmaktadir.
                Cunku ulkesi Turkey olmayan kullanicilarin en az bir Turkey olan arkadasi olmaktadir ve
                bu bag kirilir.
             */
        }

        assertEquals(4, all.getResultList().size()); //geri al, hicbir sey silinmemis gibi olacak

        /*
            "friends" listesindeki nesneleri topluca silmek icin JPQL kullanamazsiniz ancak native SQL ile yapilabilir.


            Not: Iliski tablosundaki manyToMany iliskisi X_Y olarak adlandirilir. Bizim durumumuzda User_User
            iliskisidir.

            Not 2: Bu sorgunun daha efektif olarak calistirilabilecegi farkli yollar vardir. Bu mevcut veritabaninin
            yapisina baglidir.

            Not 3: "select 1" (or yalnizca her bir satir icin '1' degerini dondur) bir nesnenin var olup olmadigini kontrol etmek icin
             yalnizca verimlilik icin kullanilir
         */
        Query deletedRelation = em.createNativeQuery(
                "DELETE FROM User_User K WHERE EXISTS(SELECT 1 FROM USER w WHERE K.friends_id=w.id AND w.country=?1) ");
        deletedRelation.setParameter(1, country);


        tx = em.getTransaction();
        tx.begin();
        try {

            deletedRelation.executeUpdate();
            delete.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            fail();
        }

        assertEquals(1, all.getResultList().size()); // 3 Turk kullanici silinmis olmalidir
    }

    @Test
    public void testOderBy() {

        /*
            Hatirla:
            A: 3
            B: 2
            B: 2
            C: 1
         */

        TypedQuery<User> query = em.createQuery("select u from User u order by u.friends.size ASC", User.class);
        query.setMaxResults(2); // en az 2 deger don

        List<User> users = query.getResultList();
        assertEquals(2, users.size());

        //bu durumda, "users" siralanmistir
        assertEquals("C", users.get(0).getName());
        assertEquals("B", users.get(1).getName());
    }
}