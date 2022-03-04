package org.webp.intro.exercises.quizgame.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.webp.intro.exercises.quizgame.backend.StubApplication;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = StubApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
/*
    Aynı SpringBoot uygulama context'ini paylaştıklarından
    (yani SpringBoot tüm testler için yalnızca bir kez başlatılır),
    burası önemlidir. Buradaki seçimin ServiceTestBase'den extend
    edilmediğine dikkat edin. Yani reset işlemlerini Spring yapıyor.

    Varsayılan'ı AFTER_METHOD'dur. Context'in ne zaman resetleneceğini belirler.
    BEFORE_CLASS: Mevcut test sınıfından önce
    BEFORE_EACH_TEST_METHOD: Mevcut test sınıfında, test metodundan önce
    AFTER_EACH_TEST_METHOD: Mevcut test sınıfında her bir test metodundan sonra
    AFTER_CLASS: mevcut test sınıfından sonra
 */
@DirtiesContext(classMode = BEFORE_CLASS)
public class DefaultDataInitializerServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private QuizService quizService;

    @Test
    public void testInit() {

        assertTrue(categoryService.getAllCategories(false).size() > 0);

        assertTrue(categoryService.getAllCategories(true).stream()
                .mapToLong(c -> c.getSubCategories().size())
                .sum() > 0);

        assertTrue(quizService.getQuizzes().size() > 0);
    }
}