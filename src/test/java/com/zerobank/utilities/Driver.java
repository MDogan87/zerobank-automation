package com.zerobank.utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class Driver {

    /*
  Creating the private constructor so this class object is not reachable from outside
   */
    private Driver() {}

        /*
        making our driver instance private so that is not reachable from outside of the class
        we make it static because we want it to run before everything else, and also we will use it in a static method
         */

    private static ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();
    /*
    Creating re-usable utility method that will return same driver instance everytime we call it
     */

    public static WebDriver getDriver(){
        if(driverPool.get() == null){

            synchronized (Driver.class){
            /*
            We read our browser type from configuration.properties file using
            .getProperty method we creating in ConfigurationReader class
             */
            String browserType = com.zerobank.utilities.ConfigurationReader.getProperty("browser");

            /*
            Depending on the browser type
             */

            switch(browserType) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    driverPool.set(new ChromeDriver());
                    driverPool.get().manage().window().maximize();
                    driverPool.get().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver();
                    driverPool.set(new FirefoxDriver());
                    driverPool.get().manage().window().maximize();
                    driverPool.get().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                    break;
            }
            }
        }
        /*
        Same driver instance will be returned  every time we call Driver.getDriver(); method
         */
        return driverPool.get();
    }


    public static void closeDriver(){
        if(driverPool.get() != null){
            driverPool.get().quit();
            driverPool.remove();
        }
    }
}
