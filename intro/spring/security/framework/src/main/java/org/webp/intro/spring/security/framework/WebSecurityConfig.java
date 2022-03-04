package org.webp.intro.spring.security.framework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) {

        /*
            Bu metotta yetkilendirme kurallarını belirteceğiz (Access Control Policy gibi)
         */

        try {
            /*
                Varsayılan olarak Spring Security CSRF Token'ları (CSRF ataklarından koruyan)
                aktif hale getirir. Ancak biz bunu her bir POST form'unda manuel olarak kontrol edeceğiz.
                Bu Spring MVC uygulamalar için oldukça kritiktir ancak JSF'in, JSF-Views tabanlı
                kendi CSRF token mekanizması bulunmaktadır. Bu yüzden Spring Security'i şu anda
                disable edebiliriz.
             */
            http.csrf().disable();

            /*
                Yetkilendirme kuralları 1 sefer uygulama başlatılırken en üst seviyede
                kontrol edilir. Eşleştirme HTTP isteğindeki kaynak yoluna göre yapılır ve
                kullanıcı bunun için regex kullanabilir.

                Burada anasayfa ve login/signup/logout sayfalarına izin verirken diğer
                sayfalara erişim için kimliklendirilmiş olmasını isteyeceğiz.
                Not: login ve logout sayfaları Spring Security tarafından ele alınmaktadır.
             */

            http.authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/index.*").permitAll()
                    .antMatchers("/signup.*").permitAll()
                    .antMatchers("/javax.faces.resource/**").permitAll()
                    //whitelist: yukarıda izin verilmeyen her şey reddedilir
                    .anyRequest().authenticated()
                    .and()
                    /*
                        Burada Spring Security'e login username/password aalnlarını içeren
                        x-www-form-urlencoded kullanan HTML form ile gerçekleştirileceğini
                        söylüyoruz
                     */
                    .formLogin()
                    // Burada login için özel sayfamızın kullanılacağını söylüyoruz
                    .loginPage("/login.jsf")
                    .permitAll()
                    /*
                        login sırasında hata olması durumunda aynı sayfada kalıp URL'e
                        query parametresi "?error=true" eklenmesi gerektiğini söylüyoruz
                     */
                    .failureUrl("/login.jsf?error=true")
                    //eğer login başarılı olursa 302 yönlendirmesi ile anasayfaya gitmesini söylüyoruz
                    .defaultSuccessUrl("/index.jsf?faces-redirect=true")
                    .and()
                    /*
                        Logout için bir sayfamız yok. Spring Security otomatik olarak POST isteklerini
                        ele alan /logout endpointini oluşturacak. JSF'ten veya herhangi bir HTML
                        sayfasından bu endpointi çağırarak doğrudan <form> gönderimi yapabiliriz.
                      */
                    .logout()
                    //logout durumunda anasayfaya 302 yönlendirmesi
                    .logoutSuccessUrl("/index.jsf?faces-redirect=true");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {

        /*
            Burada Spring Security'e SQL veritabanına nasıl bağlanıp username'i çekeceğini ve
            hash'lenmiş şifre ile kimliklendirmenin nasıl yapılacağını belirtmemiz gerekiyor.
         */

        try {
            auth.jdbcAuthentication()
                    .dataSource(dataSource)
                    .usersByUsernameQuery(
                            "SELECT username, password, enabled " +
                                    "FROM users " +
                                    "WHERE username = ?"
                    )
                    .authoritiesByUsernameQuery(
                            "SELECT x.username, y.roles " +
                                    "FROM users x, user_entity_roles y " +
                                    "WHERE x.username = ? and y.user_entity_username = x.username "
                    )
                    /*
                        Not: BCrypt'de "password" alanı aynı zamanda salt değeri de içerir
                     */
                    .passwordEncoder(passwordEncoder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
