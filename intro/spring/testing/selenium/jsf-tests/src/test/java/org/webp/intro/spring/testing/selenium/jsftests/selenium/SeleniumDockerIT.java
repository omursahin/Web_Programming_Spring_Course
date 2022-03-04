package org.webp.intro.spring.testing.selenium.jsftests.selenium;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.nio.file.Paths;
//TODO bu test çalışmıyor

public class SeleniumDockerIT extends SeleniumTestBase {

    private static String HOST_ALIAS = "jsf-tests";

    public static Network network = Network.newNetwork();

    /*
        Basit olması için SpringBoot uygulamasını ve tarayıcıyı iki Docker örneği olarak başlatıyoruz.
        Ancak bu iki uygulama da aynı networkte olmalı ki birbiri ile iletişime geçebilsinler.
     */


    public static GenericContainer spring = new GenericContainer(
            new ImageFromDockerfile("jsf-tests")
                    .withFileFromPath("target/spring-jsf-exec.jar",
                            Paths.get("../../../jsf/target/spring-jsf-exec.jar"))
                    .withFileFromPath("Dockerfile", Paths.get("../../../jsf/Dockerfile")))
            .withExposedPorts(8080)
            .withNetwork(network)
            .withNetworkAliases(HOST_ALIAS)
            /*
                Docker imajı ayağa kalktıktan sonra SpringBoot uygulamasının tamamen çalışması için zamana
                ihtiyaç bulunur bu yüzden burada ana sayfayı çekene kadar bekliyoruz.
             */
            .waitingFor(Wait.forHttp("/"))
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("SPRING-APPLICATION")))
            ;


    public static BrowserWebDriverContainer browser = (BrowserWebDriverContainer) new BrowserWebDriverContainer()
            .withCapabilities(new ChromeOptions())
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.SKIP, null)
            .withNetwork(network);


    @BeforeAll
    public static void init(){
        spring.start();
        browser.start();
    }

    @AfterAll
    public static void tearDown(){
        browser.stop();
        spring.stop();
    }

    @Override
    protected WebDriver getDriver() {
        return browser.getWebDriver();
    }

    @Override
    protected String getServerHost() {
        return HOST_ALIAS;
    }

    @Override
    protected int getServerPort() {
        return 8080;
    }
}
