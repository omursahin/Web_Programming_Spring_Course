package org.webp.intro.exercises.quizgame.ejb;

import org.webp.intro.exercises.quizgame.entity.Quiz;
import org.webp.intro.exercises.quizgame.entity.SubCategory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Stateless
public class QuizEjb {

    @PersistenceContext
    private EntityManager em;

    /*
     Burada Long değil long değer döndürüyoruz. "long" temel tiplerden biriyken
     "Long" bir nesnedir. Yani "Long" null olabilirken "long" olamaz. Bizim bu senaryoda
     eğer bir soru oluşturulabildiyse bu quiz var olmalıdır yani null olamaz o yüzden "long"
     türünde gönderiyoruz. "Long" olsaydı soru oluşturumunda hata olabilir demek olacaktı.
    */
    public long createQuiz(
            long subCategoryId,
            String question,
            String firstAnswer,
            String secondAnswer,
            String thirdAnswer,
            String fourthAnswer,
            int indexOfCorrectAnswer
    ){

        SubCategory subCategory = em.find(SubCategory.class, subCategoryId);
        if(subCategory == null){
            throw new IllegalArgumentException("SubCategory "+subCategoryId+" does not exist");
        }

        Quiz quiz = new Quiz();
        quiz.setSubCategory(subCategory);
        quiz.setQuestion(question);
        quiz.setFirstAnswer(firstAnswer);
        quiz.setSecondAnswer(secondAnswer);
        quiz.setThirdAnswer(thirdAnswer);
        quiz.setFourthAnswer(fourthAnswer);
        quiz.setIndexOfCorrectAnswer(indexOfCorrectAnswer);

        em.persist(quiz);

        return quiz.getId();
    }

    public List<Quiz> getQuizzes(){
        TypedQuery<Quiz> query = em.createQuery("select q from Quiz q", Quiz.class);
        return query.getResultList();
    }

    public Quiz getQuiz(long id){
        return em.find(Quiz.class, id);
    }

    public List<Quiz> getRandomQuizzes(int n, long categoryId){

        /*
            Bir tablodan N adet satırı rastgele almanın farklı yolları bulunur. Bu
            yollar arasındaki fark performansı etkiler. Bu yüzden tablo büyüklüğü
            ve ortalama N sayısına göre karar vermek gerekir.
            Buradaki yaklaşım büyük boyutta tablolar ve düşük N sayısı için iyi bir
            yaklaşımdır.

            Buradaki temel fikir önce kaç ader R satırı tabloda bulunuyor onu sorguluyoruz.
            Daha sonra N adet her biri bir satır çeken SQL select atıyoruz. Satırlar rastgele
            seçiliyor. JPQL komutu oluştururken satırların bir alt kümesini K indeksinden başlatıp
            bu K indeksine göre Z eleman seçimi yapabiliriz. Burada K rastgele bir değerken
            Z=1 olacaktır. Bu süreç N sefer tekrarlanır ve 1+N sorgu ile seçimi gerçekleştirebiliriz.
            Tabloda herhangi bir sıralama yapmamıza gerek kalmaz ve O(R*log(R)) karmaşıklığına sahip
            oluruz.

            Burada SELECT'i ORDER BY yapmadığımız aynı sıra ile gelmeyen veri üzerinden tekrarladığımızda aynı verinin
            gelme ihtimali de bulunmaktadır. Ancak buradaki çakışma durumları nadir gerçekleşir (sıralama farklı
            olursa tesadüfen farklı 2 indiste aynı veri olabilir) ve bunu kod tarafında çözebiliriz (sıralama yapmaktan
            daha az maliyetlidir).


         */

        TypedQuery<Long> sizeQuery= em.createQuery(
                "select count(q) from Quiz q where q.subCategory.parent.id=?1", Long.class);
        sizeQuery.setParameter(1, categoryId);
        long size = sizeQuery.getSingleResult();

        if(n > size){
            throw new IllegalArgumentException("Cannot choose " + n + " unique quizzes out of the " + size + " existing");
        }

        Random random = new Random();

        List<Quiz> quizzes = new ArrayList<>();
        Set<Integer> chosen = new HashSet<>();

        while(chosen.size() < n) {

            int k = random.nextInt((int)size);
            if(chosen.contains(k)){
                continue;
            }
            chosen.add(k);

            TypedQuery<Quiz> query = em.createQuery(
                    "select q from Quiz q where q.subCategory.parent.id=?1", Quiz.class);
            query.setParameter(1, categoryId);
            query.setMaxResults(1);
            query.setFirstResult(k);

            quizzes.add(query.getSingleResult());
        }


        return  quizzes;
    }

}
