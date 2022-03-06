package org.webp.intro.exercises.quizgame.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.function.Supplier;


@Service
public class DefaultDataInitializerService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuizService quizService;

    @PostConstruct
    public void initialize(){

        attempt(() -> userService.createUser("foo", "123"));

        Long ctgSE = attempt(() -> categoryService.createCategory("Yazılım Mühendisliği"));
        Long ctgH = attempt(() -> categoryService.createCategory("Tarih"));

        Long sWP = attempt(() -> categoryService.createSubCategory(ctgSE, "Web Programlama"));
        Long sIS = attempt(() -> categoryService.createSubCategory(ctgSE, "Bilgi Güvenliği"));
        Long sJ = attempt(() -> categoryService.createSubCategory(ctgSE, "Java"));
        Long sA = attempt(() -> categoryService.createSubCategory(ctgSE, "Veri Yapıları ve Algoritmalar"));

        Long sRE = attempt(() -> categoryService.createSubCategory(ctgH, "Roma İmparatorluğu"));


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
                "SQL injection ile ilgili hangisi doğrudur?",
                "Parametreli veriler hiçbir şekilde değiştirilemez",
                "SQL ifadelerinden oluşan prosedürler SQL injection açısından incelenmelidir",
                "SQL injection günümüzde çok sık rastlanan bir atak türü değildir",
                "Hiçbiri",
                1);

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
                "Son Roma imparatoru kimdir?",
                "Caligula",
                "Tiberius",
                "Augustus",
                "Caesar",
                2);
    }



    private void createQuiz(
            Long subCategoryId,
            String question,
            String firstAnswer,
            String secondAnswer,
            String thirdAnswer,
            String fourthAnswer,
            int indexOfCorrectAnswer){
        attempt(() -> quizService.createQuiz(
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
