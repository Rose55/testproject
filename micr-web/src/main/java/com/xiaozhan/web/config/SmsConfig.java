package com.xiaozhan.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jdwx.sms")
public class SmsConfig {
    private String url;
    private String content;
    private String appKey;

    @Override
    public String toString() {
        return "SmsCongig{" +
                "url='" + url + '\'' +
                ", content='" + content + '\'' +
                ", appKey='" + appKey + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public SmsConfig(String url, String content, String appKey) {
        this.url = url;
        this.content = content;
        this.appKey = appKey;
    }

    public SmsConfig() {
    }
}
