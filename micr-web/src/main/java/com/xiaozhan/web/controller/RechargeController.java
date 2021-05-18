package com.xiaozhan.web.controller;

import com.xiaozhan.common.HttpClientUtils;
import com.xiaozhan.contants.CommonContants;
import com.xiaozhan.licai.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
@Controller
public class RechargeController {

    @Value("${pay.alipay.url}")
    private String micrPayAlipayUrl;
    //显示支付页面
    @GetMapping("/loan/page/toRecharge")
    public String pageRecharge(){
        return "toRecharge";
    }
    //支付宝的入口
    @RequestMapping("/loan/toRecharge/alipay")
    public void alipayEntry(@RequestParam("rechargerMoney")BigDecimal money, HttpSession session, HttpServletResponse response){



        System.out.println("alipayEntry");
        User user=(User)session.getAttribute(CommonContants.SESSION_USER_KEY);
        //向micr-pay发请求
        String result="";
        String url=micrPayAlipayUrl;
        Map<String ,Object> params=new HashMap<>();

        params.put("uid",user.getId());
        params.put("money",money);
        params.put("channel","alipay");

        try {

            result=HttpClientUtils.doPost(url,params);

            //result是一个form
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out=response.getWriter();
            out.println("<html>"+result+"</html>");
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //
}
