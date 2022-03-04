package org.webp.misc.testutils.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class SeleniumDriverHandler {


    private static boolean tryToSetGeckoIfExists(String property, Path path) {
        if (Files.exists(path)) {
            System.setProperty(property, path.toAbsolutePath().toString());
            return true;
        }
        return false;
    }

    private static boolean setupDriverExecutable(String executableName, String property) {
        String homeDir = System.getProperty("user.home");

        //ilk olarak Linux/Mac dene
        if (!tryToSetGeckoIfExists(property, Paths.get(homeDir, executableName))) {
            //then check if on Windows
            if (!tryToSetGeckoIfExists(property, Paths.get(homeDir, executableName + ".exe"))) {
                System.out.println("WARNING: Cannot locate the " + executableName + " in your home directory " + homeDir);
                return false;
            }
        }

        return true;
    }

    public static WebDriver getChromeDriver() {

        /*
            Chrome ve Chrome Driver'ı gerekir. Home klasöründe kurulu olmalıdır.

            Bakınız https://sites.google.com/a/chromium.org/chromedriver/getting-started
         */

        boolean isOk = setupDriverExecutable("chromedriver", "webdriver.chrome.driver");
        if(! isOk){
            return null;
        }

        return new ChromeDriver();
    }

    public static WebDriver getFirefoxDriver(){
        /*
            Güncellenmiş Firefox ve home klasöründe geckodriver gerekir.
            Bakınız:

            https://developer.mozilla.org/en-US/docs/Mozilla/QA/Marionette/WebDriver
            https://github.com/mozilla/geckodriver/releases

            Ancak Firefox driverları çok stabil çalışmamaktadır bu yüzden Chrome önerilir.
         */

        setupDriverExecutable("geckodriver", "webdriver.gecko.driver");

        DesiredCapabilities desiredCapabilities = DesiredCapabilities.firefox();
        desiredCapabilities.setCapability("marionette", true);
        desiredCapabilities.setJavascriptEnabled(true);

        return  new FirefoxDriver(desiredCapabilities);
    }

}
