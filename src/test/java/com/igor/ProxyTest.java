package com.igor;

import com.browserup.bup.BrowserUpProxy;
import com.browserup.bup.proxy.CaptureType;
import com.browserup.harreader.model.HarEntry;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.codeborne.selenide.Selenide.open;

class ProxyTest {
  @Test
  void test() {
    Configuration.timeout = 15000;
    Configuration.browserSize = "1920x1080";
    Configuration.holdBrowserOpen = true;
    Configuration.proxyEnabled = true;

    WebDriverRunner.getAndCheckWebDriver(); // otherwise WebDriverRunner.getSelenideProxy() returns null

    BrowserUpProxy proxy = WebDriverRunner.getSelenideProxy().getProxy();
    proxy.setHarCaptureTypes(CaptureType.getAllContentCaptureTypes());
    proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
    proxy.newHar("pofig");
    open("https://google.com");

    List<HarEntry> requests = proxy.getHar().getLog().getEntries();
    for (HarEntry request : requests) {
      System.out.println("URL: " + request.getUrl());
      System.out.println("BODY: " + request.getResponse().getStatus());
      System.out.println("BODY: " + request.getResponse().getStatusText());
      System.out.println("BODY: " + request.getResponse().getContent().getText());
      System.out.println("*****************************************");
    }

  }
}
