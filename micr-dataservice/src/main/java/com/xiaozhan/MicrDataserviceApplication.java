package com.xiaozhan;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan(basePackages = "com.xiaozhan.licai.mapper")
@EnableDubbo
@SpringBootApplication
public class MicrDataserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicrDataserviceApplication.class, args);
    }

}
