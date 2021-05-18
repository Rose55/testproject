package com.xiaozhan.web.controller;

import com.xiaozhan.common.CommonUtils;
import com.xiaozhan.contants.CommonContants;
import com.xiaozhan.contants.RedisKey;
import com.xiaozhan.licai.model.FinanceAccount;
import com.xiaozhan.licai.model.User;
import com.xiaozhan.licai.service.FinanceAcountService;
import com.xiaozhan.licai.service.UserService;
import com.xiaozhan.vo.ResultObject;
import com.xiaozhan.web.service.SmsService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@RestController
public class HelpController {
    @Resource
    private RedisTemplate redisTemplate;

    @DubboReference(interfaceClass = UserService.class,version = "1.0")
    private UserService userService;

    @DubboReference(interfaceClass = FinanceAcountService.class,version = "1.0")
    private FinanceAcountService financeAcountService;
    @Resource
    private SmsService smsService;
    //判断手机号是否注册过
    @GetMapping("/loan/phone/register")
    public ResultObject phoneRegister(@RequestParam("phone")String phone){

        ResultObject resultObject=ResultObject.error("请更换手机号");
        //判断phone的格式
        if(CommonUtils.checkPhone(phone)){
            //手机号是正确的，查询数据库
            User user=userService.queryByPhone(phone);
            if(user==null){
                //可以注册
                resultObject=ResultObject.success("可以注册");
            }
        }
        return resultObject;

    }
    //发送验证码
    @GetMapping("/loan/sendCode")
    public ResultObject sendCode(@RequestParam("phone")String phone){
        ResultObject ro=ResultObject.error("发送失败");
        if(CommonUtils.checkPhone(phone)){

            //可以发送验证码
            StringBuilder builder=new StringBuilder("");
           boolean result= smsService.sendSmsCode(phone,builder);
           if(result){
               //存放到redis，设置3分钟有效
               String k= RedisKey.APP_REGISTER_SMS_CODE+phone;
               redisTemplate.opsForValue().set(k,builder.toString(),3, TimeUnit.MINUTES);
               ro=ResultObject.success("短信下发成功");
           }

        }else{
            ro.setMessage("手机号码格式不正确");
        }
        return ro;
    }
    //查询金额
    @GetMapping("/loan/account")
    public ResultObject queryAccount( HttpSession session){
        ResultObject ro=ResultObject.error("获取失败");
        BigDecimal accountMoney=new BigDecimal("0");
        //session中获取数据
        User user=(User)session.getAttribute(CommonContants.SESSION_USER_KEY);
        if(user!=null){
            FinanceAccount account=financeAcountService.queryAccount(user.getId());
            if(account!=null){
                accountMoney=account.getAvailableMoney();
                ro=ResultObject.success("查询成功");
                ro.setData(accountMoney);
            }
        }


        return ro;
    }

}
