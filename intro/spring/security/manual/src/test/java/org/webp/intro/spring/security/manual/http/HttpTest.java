package org.webp.intro.spring.security.manual.http;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.webp.misc.testutils.HttpUtils;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class HttpTest {

    @LocalServerPort
    private int port;

    @Test
    public void testSetCookies() throws Exception {

        String httpGet = "GET / HTTP/1.1\r\n" +
                "Host:localhost\r\n" +
                "\r\n";
        String response = HttpUtils.executeHttpCommand("localhost", port, httpGet);

        assertTrue(response.contains("200"));
        //SpringBoot yeni bir session oluşturur ve sonraki HTTP isteklerinde kullanmak üzere
        // bir id değeri gönderir.
        assertTrue(response.contains("Set-Cookie"));

        System.out.println(response);
    }


    @Test
    public void testCookieHandling() throws Exception{

        String httpGet = "GET / HTTP/1.1\r\n" +
                "Host:localhost\r\n" +
                "\r\n";

        //cookie'siz istekte bulunarak sunucudan yeni tane ayarlamasını isteyeceğiz
        String a = HttpUtils.executeHttpCommand("localhost", port, httpGet);
        assertTrue(a.contains("200"));
        assertTrue(a.contains("Set-Cookie"));

        //bir diğer cookie'siz istek: sunucu yeni bir session oluşturacak çünkü bizi farklı bir
        // kullanıcı olarak düşünüyor.
        String b = HttpUtils.executeHttpCommand("localhost", port, httpGet);
        assertTrue(b.contains("200"));
        assertTrue(b.contains("Set-Cookie"));

        String first = HttpUtils.getSessionCookie(a);
        String second = HttpUtils.getSessionCookie(b);
        //iki cookie farklı çünkü doğru cookie ile yapılmamış her bir HTTP isteği için
        // yeni bir session oluşturur.
        assertNotEquals(first, second);

        //şimdi daha önce aldığımız bir cookie ile istekte bulunalım
        String httpWithCookie = "GET / HTTP/1.1\r\n" +
                "Host:localhost\r\n" +
                "Cookie:"+first+"\r\n" +
                "\r\n";

        String c = HttpUtils.executeHttpCommand("localhost", port, httpWithCookie);
        assertTrue(c.contains("200"));
        //JSESSIONID için bir cookie set ettiğimiz için sunucu yeni bir cookie set etmemizi istememeli
        assertFalse(c.contains("Set-Cookie"));

        System.out.println("COOKIE = " + first+"\n\n");
        System.out.println(c);
    }
}
