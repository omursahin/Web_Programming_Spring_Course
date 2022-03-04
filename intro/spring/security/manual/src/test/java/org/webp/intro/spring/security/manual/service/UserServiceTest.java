package org.webp.intro.spring.security.manual.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.webp.intro.spring.security.manual.entity.User;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = NONE)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    private static final AtomicInteger counter = new AtomicInteger(0);

    /*
        Not: burada tekil id kullanarak her seferinde veritabanını temizleme ihtiyacını
        ortadan kaldırıyoruz.
     */
    private String getUniqueId(){
        return "foo_UserServiceTest_" + counter.getAndIncrement();
    }

    @Test
    public void testCanCreateAUser(){

        String user = getUniqueId();
        String password = "password";

        boolean created = userService.createUser(user,password);
        assertTrue(created);
    }


    @Test
    public void testNoTwoUsersWithSameId(){

        String user = getUniqueId();

        boolean created = userService.createUser(user,"a");
        assertTrue(created);

        created = userService.createUser(user,"b");
        assertFalse(created);
    }

    @Test
    public void testSamePasswordLeadToDifferentHashAndSalt(){

        String password = "password";
        String first = getUniqueId();
        String second = getUniqueId();

        userService.createUser(first,password);
        userService.createUser(second,password); //aynı şifre

        User f = userService.getUser(first);
        User s = userService.getUser(second);

        //imkansız olmasa da bunların aynı olma ihtimali oldukça düşüktür
        //muhtemelen kafanıza bir meteor düşme ihtimali daha fazladır
        assertNotEquals(f.getPassword(), s.getPassword());
    }

    @Test
    public void testVerifyPassword(){

        String user = getUniqueId();
        String correct = "correct";
        String wrong = "wrong";

        userService.createUser(user, correct);

        boolean  canLogin = userService.login(user, correct);
        assertTrue(canLogin);

        canLogin = userService.login(user, wrong);
        assertFalse(canLogin);
    }

    @Test
    public void testBeSurePasswordIsNotStoredInPlain(){

        String user = getUniqueId();
        String password = "password";

        userService.createUser(user, password);

        User entity = userService.getUser(user);

        assertFalse(entity.getUserId().contains(password));
        assertFalse(entity.getPassword().contains(password));
    }
}