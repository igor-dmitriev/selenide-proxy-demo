package com.igor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.logging.Level;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class ProxyTest {
  @Test
  void test() {
    Configuration.timeout = 15000;
    Configuration.browserSize = "1920x1080";

    ChromeOptions options = new ChromeOptions();
    LoggingPreferences logPrefs = new LoggingPreferences();
    logPrefs.enable(LogType.PERFORMANCE, Level.ALL);

    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability("goog:loggingPrefs", logPrefs);
    capabilities.setCapability(ChromeOptions.CAPABILITY, options);

    Configuration.browserCapabilities = capabilities;

    open("https://my.smartcrowd.ae/property-browse");
    $(byText("Live and Coming Soon Properties")).shouldBe(Condition.visible);

    LogEntries logs = WebDriverRunner.getWebDriver().manage().logs().get(LogType.PERFORMANCE);
    String logsBrowser = "";

    for (LogEntry le : logs) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      JsonParser jp = new JsonParser();
      JsonElement je = jp.parse(le.getMessage());

      if (gson.toJson(je).contains("api.my")) {
        logsBrowser = logsBrowser + gson.toJson(je);
      }
    }
    System.out.println(logsBrowser);

  }
}
