package org.webp.intro.spring.testing.selenium.jsftests.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.webp.intro.spring.jsf.Application;
import org.webp.intro.spring.jsf.ex03.Comment;
import org.webp.intro.spring.jsf.ex03.CommentService;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Application.class, DeleterService.class},
        webEnvironment = NONE)
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private DeleterService deleterService;

    @BeforeEach
    @AfterEach
    public void clearDatabase(){
        deleterService.deleteEntities(Comment.class);
    }

    @Test
    public void testCreate(){

        assertEquals(0 , commentService.getMostRecentComments(10).size());

        commentService.createNewComment("Hello");

        List<Comment> comments = commentService.getMostRecentComments(10);
        assertEquals(1 , comments.size());
        assertTrue(comments.stream().map(Comment::getText).anyMatch(s -> "Hello".equals(s)));

        commentService.createNewComment("World");

        comments = commentService.getMostRecentComments(10);
        assertEquals(2 , comments.size());
        assertTrue(comments.stream().map(Comment::getText).anyMatch(s -> "Hello".equals(s)));
        assertTrue(comments.stream().map(Comment::getText).anyMatch(s -> "World".equals(s)));

        /*
            Not: comments.stream() yerine comments.get() tercih edilebilir çünkü değerler
            sıralanmış olacaktır
            Ancak bir test çok fazla şeyi test etmemelidir, yani buradaki amacımız yeni yorum
            oluşturma ve çekmeyi test etmektir. Sıralanmış olup olmaması farklı bir testte
            ele alınabilir.
         */
    }

    @Test
    public void testReturnDatesInOrder() throws Exception {

        commentService.createNewComment("a");
        Thread.sleep(1);
        commentService.createNewComment("b");
        Thread.sleep(1);
        commentService.createNewComment("c");

        List<Comment> comments = commentService.getMostRecentComments(10);
        assertEquals(3 , comments.size());


        assertTrue(comments.get(0).getDate().compareTo(comments.get(1).getDate()) > 0);
        assertTrue(comments.get(1).getDate().compareTo(comments.get(2).getDate()) > 0);

        /*
            Not: burada neyi eklediğimizin (a,b,c gibi) test edilmesine çok da gerek yoktur.
            Bunun için ekstra assertion'lar yazılabilir ancak testi daha uzun ve anlaşılması zor
            hale getirecektir.
            Yani özet olarak testler kısa ve hedef odaklı olmalıdır. Eğer farklı davranışları/özellikleri
            test etmek isterseniz tek bir uzun test yerine kısa yeni testler yazın.
         */
    }

    @Test
    public void testReturnValuesInOrder() throws Exception{

        commentService.createNewComment("a");
        Thread.sleep(10);
        commentService.createNewComment("b");
        Thread.sleep(10);
        commentService.createNewComment("c");
        Thread.sleep(10);
        commentService.createNewComment("d");
        Thread.sleep(10);

        List<Comment> comments = commentService.getMostRecentComments(10);
        assertEquals(4 , comments.size());

        assertEquals("d", comments.get(0).getText());
        assertEquals("c", comments.get(1).getText());
        assertEquals("b", comments.get(2).getText());
        assertEquals("a", comments.get(3).getText());
    }

    @Test
    public void testAll(){

        /*

            Bu önceki testleri bir arada toplayan bir testtir. Diğer testlerden
            daha kısa olsa da anlaşılması ve bakımı oldukça güçtür.

            Bu testte 4 farklı özellik/gereksinimi test eden 12 assertion bulunur.

            Not: bu çok uzun bir test değildir sadece 17 ifade barındırır ancak çok fazla
            şeyi test etmektedir.

            Not 2: bu söylediklerimiz kısa süren testler için geçerlidir. Maliyetli sistem
            testkleri için kısa test ne kadar kısadır desek de bazı değişiklikler gerçekleşebilir.
         */

        assertEquals(0 , commentService.getMostRecentComments(10).size());

        commentService.createNewComment("a");
        assertEquals(1 , commentService.getMostRecentComments(10).size());

        commentService.createNewComment("b");
        assertEquals(2 , commentService.getMostRecentComments(10).size());

        commentService.createNewComment("c");
        assertEquals(3 , commentService.getMostRecentComments(10).size());

        commentService.createNewComment("d");
        assertEquals(4 , commentService.getMostRecentComments(10).size());


        List<Comment> comments = commentService.getMostRecentComments(10);

        assertEquals("d", comments.get(0).getText());
        assertEquals("c", comments.get(1).getText());
        assertEquals("b", comments.get(2).getText());
        assertEquals("a", comments.get(3).getText());

        assertTrue(comments.get(0).getDate().compareTo(comments.get(1).getDate()) > 0);
        assertTrue(comments.get(1).getDate().compareTo(comments.get(2).getDate()) > 0);
        assertTrue(comments.get(2).getDate().compareTo(comments.get(3).getDate()) > 0);
    }


    @Test
    public void testCreateFail(){

        try{
            commentService.createNewComment("");
            fail();
        } catch (Exception e){}

        try{
            commentService.createNewComment(new String(new char[1000]));
            fail();
        } catch (Exception e){}

    }

    @Test
    public void testReturnLimits(){

        commentService.createNewComment("a");
        commentService.createNewComment("b");
        commentService.createNewComment("c");


        try{
            commentService.getMostRecentComments(-1);
            fail();
        } catch (Exception e){}


        assertEquals(0 , commentService.getMostRecentComments(0).size());
        assertEquals(1 , commentService.getMostRecentComments(1).size());
        assertEquals(2 , commentService.getMostRecentComments(2).size());
        assertEquals(3 , commentService.getMostRecentComments(3).size());

        //mevcuttan daha fazlasının kontrolü
        assertEquals(3 , commentService.getMostRecentComments(4).size());
        assertEquals(3 , commentService.getMostRecentComments(100).size());
    }


    @Test
    public void testDelete(){

        commentService.createNewComment("a");
        List<Comment> comments = commentService.getMostRecentComments(10);
        assertEquals(1 , comments.size());

        Long id = comments.get(0).getId();
        commentService.deleteComment(id);

        assertEquals(0 , commentService.getMostRecentComments(10).size());
    }

}