package org.webp.intro.exercises.quizgame.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import java.util.function.Supplier;

import static javax.ejb.TransactionAttributeType.NOT_SUPPORTED;

@Singleton
@Startup
public class DefaultDataInitializerEjb {

    @EJB
    private CategoryEjb categoryEjb;

    @EJB
    private QuizEjb quizEjb;

    /*
        Veritabanındaki varsayılan değerler ve migration işlemleri harici araçlarla (Flyway, Liquibase gibi)
        gerçekleştirilmelidir. Burada kolaylık açısından kod ile yapıyoruz. Ancak production veritabanında
        çalışırken uygulama yeniden başlatılacak olursa (pratikte genelde bu senaryo geçerlidir) ne olacak?
        Aynı soruları tekrar tekrar her sunucu başlatıldığında oluşturmak mantıklı değildir.
        Ayrıca isimler tekil olması gerekirse kısıtlamalar da hataya düşecektir. O yüzden her biri bir transaction
        içerisinde yer almalı ve hata oluşursa yok sayılmalıdır. NOT_SUPPORTED kullanmamızın temel sebebi de budur
        yoksa tek bir transaction olacak (REQUIRED sebebiyle) ve hata olması durumunda bütün uygulamayı etkileyecektir.
     */

    @PostConstruct
    @TransactionAttribute(NOT_SUPPORTED)
    public void initialize(){

        Long ctgSE = attempt(() -> categoryEjb.createCategory("Yazılım Mühendisliği"));
        Long ctgH = attempt(() -> categoryEjb.createCategory("Tarih"));

        Long sWP = attempt(() -> categoryEjb.createSubCategory(ctgSE, "Web Programlama"));
        Long sIS = attempt(() -> categoryEjb.createSubCategory(ctgSE, "Bilgi Güvenliği"));
        Long sJ = attempt(() -> categoryEjb.createSubCategory(ctgSE, "Java"));
        Long sA = attempt(() -> categoryEjb.createSubCategory(ctgSE, "Veri Yapıları ve Algoritmalar"));

        Long sRE = attempt(() -> categoryEjb.createSubCategory(ctgH, "Roma İmparatorluğu"));

        createWebQuestions(sWP);
        createJavaQuestions(sJ);
        createSecurityQuestions(sIS);
        createAlgorithmQuestions(sA);
        createRomeEmpireQuestions(sRE);
    }

    private void createWebQuestions(Long sub) {

        createQuiz(
                sub,
                "JPA ne anlama gelmektedir?",
                "Java Permit Access",
                "Java Persistence API",
                "Java Process Analyzer",
                "Java Persistence Analyzer",
                1);

        createQuiz(
                sub,
                "JEE ne anlama gelmektedir?",
                "Java Embedded Edition",
                "Java Extended Edition",
                "Java Enterprise Edition",
                "Java Excelsior Edition",
                2);
        createQuiz(
                sub,
                "Hangisi JPA implementasyonudur?",
                "Hibernate",
                "Wildfly",
                "Glassfish",
                "Jackson",
                0);
    }

    private void createJavaQuestions(Long sub){

        createQuiz(
                sub,
                "'volatile' değişkenler hakkında hangisi doğrudur?",
                "Java'da volatile değişkenler bulunmamaktadır",
                "Değeri değiştirilemeyen değişkenlerdir",
                "Her okunduğunda değeri değişebilen değişkenlerdir",
                "Asla cache'lenmeyen değişkenlerdir",
                3);
        createQuiz(
                sub,
                "'final' değişkenler hakkında hangisi doğrudur?",
                "Yalnızca JVM kapatılırken kullanılabilen değişkenlerdir",
                "Başlatıldıktan sonra değeri değiştirilemeyen değişkenlerdir",
                "Her okunduğunda değeri değişebilen değişkenlerdir",
                "Java'da final değişkenler bulunmamaktadır",
                1);
    }

    private void createSecurityQuestions(Long sub) {
        createQuiz(
                sub,
                "Neden şifrelerde 'salt' kullanılmalıdır?",
                "Şifrelerde 'salt' değer kullanılamaz",
                "Şifreyi hatırlamayı kolaylaştırır",
                "Hash'lenmiş değerlerin bulunduğu tabloları kullanarak gerçekleştirilen ataklardan korur",
                "Şifrenin daha hızlı hashlenmesini sağlar",
                2);

        createQuiz(
                sub,
                "Which grade will you get if you submit a project that is vulnerable to SQL Injection attacks?",
                "One grade less (e.g., a B turns into a C)",
                "Two grades less (e.g., a B turns into a D)",
                "An A, because so your lecturer can have fun hacking your web site",
                "A straight F, regardless of what done in the rest of the project",
                3);

    }

    private void createAlgorithmQuestions(Long sub) {
        createQuiz(
                sub,
                "Binary aramanın karmaşıklığı nedir?",
                "5n",
                "O(n)",
                "O(log n)",
                "O(n log n)",
                2);

        createQuiz(
                sub,
                "Sıralama algoritmaları hakkında aşağıdakilerden hangisi doğrudur?",
                "Merge Sort kesinlikle Quick Sort'dan daha iyidir",
                "Quick Sort kesinlikle Merge Sort'dan daha iyidir",
                "Bubble Sort daha az alan kullandığı için Merge/Quick Sort'dan daha iyidir",
                "Merge Sort kıyaslama sayısı bakımından optimale yakındır",
                3);

    }

    private void createRomeEmpireQuestions(Long sub) {
        createQuiz(
                sub,
                "İlk Roma İmparatoru kimdir?",
                "Caligula",
                "Tiberius",
                "Augustus",
                "Caesar",
                2);

        createQuiz(
                sub,
                "Efsanelere göre Roma'yı kim kurdu?",
                "Romulus ve Remus",
                "Augustus ve Caesar",
                "Tiberius ve Claudius",
                "Erik ve Olav",
                0);

        createQuiz(
                sub,
                "Tarihteki en uzun süren imparatorluk hangisidir?",
                "Moğol İmparatorluğu",
                "Osmanlı İmparatorluğu",
                "İngiliz İmparatorluğu",
                "Roma İmparatorluğu",
                2);

        createQuiz(
                sub,
                "Praetorian kimlerdir?",
                "Tanrı Pratunus'un Rahipleri",
                "Köleler",
                "Barbarlar",
                "Elit Askerler",
                3);
        createQuiz(
                sub,
                "Praetorian kimlerdir?",
                "Tanrı Pratunus'un Rahipleri",
                "Köleler",
                "Barbarlar",
                "Elit Askerler",
                3);
        createQuiz(
                sub,
                "Son Roma imparatoru kimdir?",
                "Caligula",
                "Tiberius",
                "Augustus",
                "Caesar",
                2);
    }



    private void createQuiz(
            long subCategoryId,
            String question,
            String firstAnswer,
            String secondAnswer,
            String thirdAnswer,
            String fourthAnswer,
            int indexOfCorrectAnswer){
        attempt(() -> quizEjb.createQuiz(
                subCategoryId,
                question,
                firstAnswer,
                secondAnswer,
                thirdAnswer,
                fourthAnswer,
                indexOfCorrectAnswer));
    }

    private  <T> T attempt(Supplier<T> lambda){
        try{
            return lambda.get();
        }catch (Exception e){
            return null;
        }
    }
}
