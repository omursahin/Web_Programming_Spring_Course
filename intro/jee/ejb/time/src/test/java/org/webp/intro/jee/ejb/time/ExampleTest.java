package org.webp.intro.jee.ejb.time;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.webp.intro.jee.ejb.time.businesslayer.AuthorBot;
import org.webp.intro.jee.ejb.time.businesslayer.CommentatorBot;
import org.webp.intro.jee.ejb.time.businesslayer.NewsEJB;
import org.webp.intro.jee.ejb.time.datalayer.News;

import javax.ejb.EJB;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/*
        Not: bu Arquillian ile çalıştırılmak zorundadır çünkü zamanlama servisi
        varsayılan JEE container'ı tarafından değil Arquillian tarafından ele alınır
*/
@RunWith(Arquillian.class)
public class ExampleTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "org.webp.intro")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private NewsEJB newsEJB;

    @Test
    public void test() throws Exception{

        Thread.sleep(4_000); //biraz mesaj/yorum oluşturması için zaman tanıyoruz

        List<News> news = newsEJB.getAllNews();

        //en az 1 news olmalı
        assertFalse(news.isEmpty());

        assertTrue(news.stream().anyMatch(n -> n.getAuthor().equals(AuthorBot.POST_CONSTRUCT)));
        assertTrue(news.stream().anyMatch(n -> n.getAuthor().equals(AuthorBot.BAR)));


        /*
            Bazı haberler yalnızca Pazartesi(Monday) oluşturulurken bazıları da yalnızca
            Perşembe (Thursday) oluşturulacaktır. Burası günün hangi gününde olduğumuza
            bağlı olarak kontrol edecektir
         */
        if(LocalDate.now().getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            assertTrue(news.stream().anyMatch(n -> n.getAuthor().equals(AuthorBot.MON)));
            assertFalse(news.stream().anyMatch(n -> n.getAuthor().equals(AuthorBot.THU)));
        } else if(LocalDate.now().getDayOfWeek().equals(DayOfWeek.THURSDAY)) {
            assertTrue(news.stream().anyMatch(n -> n.getAuthor().equals(AuthorBot.THU)));
            assertFalse(news.stream().anyMatch(n -> n.getAuthor().equals(AuthorBot.MON)));
        }


        /*
            Burada "flatMap" kullandık... Bütün haberlere yorumları map'ledik
         */
        long comments = news.stream()
                .flatMap(n -> n.getComments().stream())
                .filter(c -> c.getAuthor().equals(CommentatorBot.COMMENTATOR))
                .count();

        assertTrue(comments>0); //en az 1 yorum
    }
}
