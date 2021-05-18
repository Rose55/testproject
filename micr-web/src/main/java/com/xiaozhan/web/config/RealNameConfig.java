package com.xiaozhan.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jdwx.realname")
public class RealNameConfig {
    private String url;
    private String appKey;

    @Override
    public String toString() {
        return "RealNameConfig{" +
                "url='" + url + '\'' +
                ", appKey='" + appKey + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public RealNameConfig(String url, String appKey) {
        this.url = url;
        this.appKey = appKey;
    }

    public RealNameConfig() {
    }
}
