package com.xiaozhan.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
/*@EnableScheduling:启动定时任务*/
@EnableScheduling
@SpringBootApplication
public class MircTaskApplication {

    public static void main(String[] args) {
       ApplicationContext run = SpringApplication.run(MircTaskApplication.class, args);
       TaskMananger tm=(TaskMananger)run.getBean("taskManager");
       //tm.genernateIncomePlan();
       //tm.genernateIncomeBack();
        tm.invokeMicrPayAlipayQuery();
    }

}
