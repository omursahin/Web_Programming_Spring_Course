package org.webp.intro.spring.bean.profile;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.webp.intro.spring.bean.jpa.Application;

/**
 * Burada aynı testleri gerçek bir veritabanında çalıştıracağız
 * Ancak, production veritabanına bağlı olduğumuz için aynı ayarları production kodunda
 * kullanamayız.
 * O yüzden burada yalnızca TestContainer kullanarak Docker ile Postgres başlatıyoruz.
 * Ancak, bu veritabanına bağlanmak için ayarları değiştirmeliyiz. Bu yüzden farklı bir ayar
 * dosyası ile farklı bir profili aktif ediyoruz.
 */
@ActiveProfiles("docker") //profil aktifleştirme, application-docker.yml dosyasını kullanır
@ContextConfiguration(initializers = PostgresDocketTest.DockerInitializer.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public class PostgresDocketTest extends DbTestBase {

    /*
        Sıradaki kural Docker içinde Postgres başlatacaktır ve 5432
        portunu açacaktır böylelikle Postgres TCP bağlantılarını dinleyebilir olacaktır.

        Not: testte port çakışmasını engellemek için ephemeral port'lar kullanılır.
        TestContainers docker'da port açtığında biz bu portu işletim sistemi ile maplememiz gerekir.
     */
    public static GenericContainer postgres = new GenericContainer("postgres:10")
            .withExposedPorts(5432)
            .withEnv("POSTGRES_HOST_AUTH_METHOD","trust");


    @BeforeAll
    public static void init(){
        postgres.start();
    }

    @AfterAll
    public static void tearDown(){
        postgres.stop();
    }

    /*
        Gerçek host/port yalnızca runtime'da bilinir (ephemeral port). Bu yüzden application.yml'da
        spring.datasource.url ataması yapamayız. Bu runtime'da atanmak zorundadır. Bu yüzden
        @ContextConfiguration kullanacağız
     */
    public static class DockerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

            //Docker içerisinde Postgres'e bağlanmak için local hosttaki host:port değerlerini al
            String host = postgres.getContainerIpAddress();
            int port = postgres.getMappedPort(5432);

            TestPropertyValues.of("spring.datasource.url=jdbc:postgresql://" + host + ":" + port + "/postgres")
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }


    @Override
    protected String getExpectedDatabaseName() {
        return "postgresql";
    }
}
