package org.webp.intro.spring.security.manual.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webp.intro.spring.security.manual.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.security.SecureRandom;


@Service
@Transactional
public class UserService {

    @PersistenceContext
    private EntityManager em;

    private static final String PEPPER = "veritabani disinda saklanan string ifade";

    private static final int ITERATIONS = 10_000;

    private static final char SEPARATOR = '$';

    private static final char SEPARATOR_REPLACEMENT = '!';

    /**
     * @return {@code false} başlangıçta herhangi bir doğrulama hataya düşerse
     *         Not: bu metot eğer transaction hataya düşerse hala exception fırlatabilir
     */
    public boolean createUser(String userId, String password) {
        if (userId == null) {
            return false;
        }

        User userDetails = getUser(userId);
        if (userDetails != null) {
            //aynı id'ye sahip kullanıcı bulunuyor
            return false;
        }

        userDetails = new User();
        userDetails.setUserId(userId);

        //salt için kullanmak üzere en az 128 bitlik "strong" random string üretiliyor
        String salt = getRandomSalt();

        String hash = computeSHA256(password, ITERATIONS, salt, PEPPER);

        /*
            Not: paper veritabanında saklanmaz ancak ITERATIONS ve algoritma saklamaya ihtiyaç duyar.
            Bu yüzden gelecekte burayı değiştirebiliriz (donanımlar daha hızlı hale geldiğinde daha
            maliyetli bir şeyle)
         */
        String hashedPassword = "SHA-256"+SEPARATOR+ITERATIONS+SEPARATOR+salt+SEPARATOR+hash;

        userDetails.setPassword(hashedPassword);

        em.persist(userDetails);

        return true;
    }


    /**
     *
     * @param userId
     * @param password
     * @return  {@code true} eğer verilan şifre ile bir kullanıcı bulunuyorsa
     */
    public boolean login(String userId, String password) {
        if (userId == null) {
            return false;
        }

        User user = getUser(userId);
        if (user == null) {

            /*
                Neden kullanıcı bulunmasa da hash değerini bir yerde saklanmayan rastgele bir
                salt değeri ile hesaplıyoruz?
                Eğer şaşırtıcı geldiyse, güvenlik dünyasının mükemmel dünyasına hoş geldiniz!
                Mesele şu ki, eğer hesaplama yapmazsanız o zaman login metodu verilen userId'ye
                ait kullanıcı olmadığı için oldukça hızlı yanıt dönecektir (hash hesabı oldukça
                maliyetlidir). Bir saldırgan da cevap süresini takip ederek hangi userId'nin veritabanında
                var olup olmadığını takip etmesi mümkün olacaktı.

                Ayrıca bir web sayfasında geçersiz bir giriş yaparsanız kullanıcı adı mı yoksa
                şifre mi hatalı söylememesinin sebebi de budur.

                Not: Bir userId'nin zaten var olduğunu bulabilmek için yalnızca o userId ile kayıt olmayı
                deneyebilirsiniz. Ancak bu durumda da CAPTCHA kullanılarak deneme sayısı sınırlandırılır. Kullanılabilirlik
                açısından bakıldığında üye olurken CAPTCHA kullanmak iyidir (kullanıcı yalnızca bir sefer üye olur),
                ancak her bir giriş denemesinde kullanmak oldukça rahatsız edicidir.
             */
            computeSHA256(password, ITERATIONS, getRandomSalt(), PEPPER);

            return false;
        }

        String storedHashedPassword = user.getPassword();
        String[] tokens = storedHashedPassword.split("\\"+SEPARATOR);

        //NOT: eğer algoritmayı değiştirirsek kontrol etmemiz gerekir

        int iterations = Integer.parseInt(tokens[1]);
        String salt = tokens[2];
        String hash = tokens[3];

        String x = computeSHA256(password, iterations, salt, PEPPER);

        //hesaplanan hash ve veritabanınsa saklanan aynı mı kontrolü
        return  x.equals(hash);
    }


    public User getUser(String userId){
        return em.find(User.class, userId);
    }


    //------------------------------------------------------------------------


    protected String computeSHA256(String password, int iterations, String salt, String pepper){

         /*
            Bir f hash fonksiyonu verildiğinde:

            f(password) = hash

            buradaki temel nokta, hash değerini ve f fonksiyonunun değerlerini bilsek bile
            şifreyi üretmek son derece zor olmalıdır. Örneğin g fonksiyonu bulunamamalı ve şöyle olmalıdır:

            g(hash) = password
         */

        String hash = password + salt + pepper;

        /*
            Şifreyi bir "salt" değeri ile birleştirmek şu durumları engeller:

            1) aynı şifreye sahip kulanıcıların hash değeri aynı olmaz.
               Böylelikle saldırgan şifrelerden birini elde etse de diğer aynı şifreye sahip
               kullanıcıların şifresinin aynı olduğunu anlama şansı olmaz.
            2) N uzunluğuna kadar bütün ifadelerin hash değerini barındıran "rainbow table"
               kullanarak brute force atak gerçekleştirmeyi neredeyse imkansız hale getirir.
               Ayrıca hashlenmiş ifadelerin şifreden bağımsız olarak en az salt kadar (örneğin 26)
               olmasının sebebi budur.

            Not: bir saldırgan veritabanına eriştiğinde "salt" değerini okuyabilir ancak "pepper" değerine
            erişemez.

            Not: commons-codec kütüphanesindeki DigestUtils Java API'ın kendi MessageDigest sınıfının kullanımını
            kolaylaştıran özellikler içeren yardımcı bir programdır.
         */

        for(int i=0; i<iterations; i++) {
            hash = DigestUtils.sha256Hex(hash);
        }

        return hash;
    }

    protected String getRandomSalt(){
        SecureRandom random = new SecureRandom();
        int bitsPerChar = 5;
        int twoPowerOfBits = 32; // 2^5
        int n = 26;
        assert n * bitsPerChar >= 128;

        String salt = new BigInteger(n * bitsPerChar, random).toString(twoPowerOfBits);
        //burada ayırıcının rastgele string'in bir parçası olmasını önlemeliyiz
        salt = salt.replace(SEPARATOR, SEPARATOR_REPLACEMENT);
        return salt;
    }
}
