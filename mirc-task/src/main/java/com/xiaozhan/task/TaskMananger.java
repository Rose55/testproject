package com.xiaozhan.task;

import com.xiaozhan.common.HttpClientUtils;
import com.xiaozhan.licai.service.IncomeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("taskManager")
public class TaskMananger {
    @Value("${micrpay.alipay.url}")
    private String micrPayAlipayQueryUrl;
    @DubboReference(interfaceClass = IncomeService.class,version = "1.0")
    private IncomeService incommService;
    /* @Scheduled:指定定时任务
    位置：放在方法的上面，方法定时执行
    属性：cron 时间表达式*/
   // @Scheduled(cron = "*/10 * * * * ?")
    public void testCron(){
        System.out.println("指定定时任务 ："+new Date());
    }

    //调用生成收益计划的方法
    //@Scheduled(cron = "0 */20 * * * ？")
    public void genernateIncomePlan(){
        //调用dubbo服务的提供者
        incommService.generateIncomePlan();
    }

    //调用生成收益返还
   // @Scheduled(cron = "0 */30 * * * ？")
    public void genernateIncomeBack(){
        //调用dubbo服务的提供者
        incommService.genernateIncomeBack();
    }
    //定时任务 ： 调用micr-pay的支付，调用支付宝的查询接口
   // @Scheduled(cron = "0 */10 * * * ？")
    public void invokeMicrPayAlipayQuery(){
        //使用HttpClient调用，controll接口
        try {
            String result=HttpClientUtils.doGet(micrPayAlipayQueryUrl);
            System.out.println("支付微服务返回的结果："+result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
