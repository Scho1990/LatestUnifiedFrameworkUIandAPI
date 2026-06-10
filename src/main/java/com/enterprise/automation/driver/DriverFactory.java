package com.enterprise.automation.driver;

import com.enterprise.automation.config.ConfigManager;
import com.enterprise.automation.exceptions.FrameworkException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.HashMap;

public final class DriverFactory {
    private DriverFactory() {
    }

    public static WebDriver createDriver(String testMethodName) {
        String executionMode =
                ConfigManager.getRequired("execution.mode");

        if ("browserstack".equalsIgnoreCase(executionMode)) {
            return createBrowserStackDriver(testMethodName);
        }

        BrowserType browserType = BrowserType.from(ConfigManager.getRequired("browser"));
        boolean headless = ConfigManager.getBoolean("headless");

        return switch (browserType) {
            case CHROME -> createChromeDriver(headless);
            case FIREFOX -> createFirefoxDriver(headless);
            case EDGE -> createEdgeDriver(headless);
            default -> throw new FrameworkException("Unsupported browser: " + browserType);
        };
    }

    private static WebDriver createChromeDriver(boolean headless) {
        //WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*", "--disable-notifications", "--window-size=1920,1080");
        if (headless) {
            options.addArguments("--headless=new");
        }
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        //WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }

        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver(boolean headless) {
        //WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-notifications", "--window-size=1920,1080");
        if (headless) {
            options.addArguments("--headless=new");
        }
        return new EdgeDriver(options);
    }

    private static WebDriver createBrowserStackDriver(String testMethodName) {

        try {

            String username =
                    ConfigManager.getRequired("browserstack.username");

            String accessKey =
                    ConfigManager.getRequired("browserstack.accesskey");

            MutableCapabilities capabilities =
                    new MutableCapabilities();

            capabilities.setCapability(
                    "browserName",
                    ConfigManager.getRequired("bs.browser"));

            capabilities.setCapability(
                    "browserVersion",
                    ConfigManager.getRequired("bs.browserVersion"));

            HashMap<String, Object> bstackOptions =
                    new HashMap<>();

            bstackOptions.put(
                    "os",
                    ConfigManager.getRequired("bs.os"));

            bstackOptions.put(
                    "osVersion",
                    ConfigManager.getRequired("bs.osVersion"));

            bstackOptions.put(
                    "projectName",
                    ConfigManager.getRequired("bs.projectName"));

            bstackOptions.put(
                    "buildName",
                    ConfigManager.getRequired("bs.buildName"));

            bstackOptions.put(
                    "sessionName",
                    ConfigManager.getRequired("bs.browser")+"_"+testMethodName);

            capabilities.setCapability(
                    "bstack:options",
                    bstackOptions);

            String url = String.format(
                    "https://%s:%s@hub-cloud.browserstack.com/wd/hub",
                    username,
                    accessKey);

            return new RemoteWebDriver(
                    new URL(url),
                    capabilities);

        } catch (Exception e) {
            throw new FrameworkException(
                    "Failed to initialize BrowserStack Driver",
                    e);
        }
    }
}
