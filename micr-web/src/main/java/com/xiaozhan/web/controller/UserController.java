package com.xiaozhan.web.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.xiaozhan.common.CommonUtils;
import com.xiaozhan.contants.CommonContants;
import com.xiaozhan.licai.mix.UserBidInfo;
import com.xiaozhan.licai.mix.UserIncomeInfo;
import com.xiaozhan.licai.mix.UserRechargeInfo;
import com.xiaozhan.licai.model.FinanceAccount;
import com.xiaozhan.licai.model.User;
import com.xiaozhan.licai.service.*;
import com.xiaozhan.vo.ResultObject;
import com.xiaozhan.web.service.RealNameService;
import com.xiaozhan.web.service.SmsService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private SmsService smsService;
    @Resource
    private RealNameService realNameService;
    @DubboReference(interfaceClass = BidInfoService.class,version = "1.0")
    private BidInfoService bidInfoService;

    @DubboReference(interfaceClass = RechargeService.class,version = "1.0")
    private RechargeService rechargeService;

    @DubboReference(interfaceClass = FinanceAcountService.class,version = "1.0")
    private FinanceAcountService financeAcountService;

    @DubboReference(interfaceClass = IncomeService.class,version = "1.0")
    private IncomeService incomeService;

    //进入注册页面
    @GetMapping("/loan/page/register")
    public String registerPage(Model model){

        return "register";
    }
    //注册用户
    @PostMapping("/loan/register")
    @ResponseBody
    public ResultObject userRegister(@RequestParam("phone")String phone,
                                     @RequestParam("pwd")String password,
                                     @RequestParam("code")String code,
                                     HttpSession session){
        ResultObject ro=ResultObject.error("注册失败");
        if(CommonUtils.checkPhone(phone)&&code!=null){

            //判断code是否有效
            if(smsService.matchCode(phone,code)){
                User user=userService.userRegister(phone,password);
                //注册是否成功
                if(user!=null){
                    //注册成功
                    ro=ResultObject.success("注册成功");
                    session.setAttribute(CommonContants.SESSION_USER_KEY,user);
                }
            }else{
                ro.setMessage("验证码无效");
            }
        }else{
            ro.setMessage("手机号或者验证码无效");
        }



        return ro;
    }

    //实名认证页面
    @GetMapping("/loan/realName")
    public String pageRealName(Model model,HttpSession session){

        User user = (User) session.getAttribute(CommonContants.SESSION_USER_KEY);
        model.addAttribute("phone",user.getPhone());

        return "realName";
    }
    //实名认证
    @PostMapping("/loan/realName")
    @ResponseBody
    public ResultObject realName(@RequestParam("name") String  name,
                                 @RequestParam("card")String card,
                                 @RequestParam("phone")String phone,
                                 HttpSession session){
        ResultObject ro=ResultObject.error("认证失败，稍后重试");
        if(name==null||name.length()<2){
            ro.setMessage("姓名不正确");
        }else if(card==null||(card.length()<15||card.length()>18)){
            ro.setMessage("身份证号不正确");
        }else  if(!CommonUtils.checkPhone(phone)){
            ro.setMessage("手机号不正确");
        }else{
            //进行实名认证
          boolean result=  realNameService.realName(phone,name,card);
          if(result){
              //更新用户的数据
              User user=(User)session.getAttribute(CommonContants.SESSION_USER_KEY);
              user.setIdCard(card);
              user.setName(name);
                int rows=userService.modifyUser(user);

              //创建 success 的 ro 对象
              if(rows>0){
                  ro=ResultObject.success("实名认证成功");
              }
          }
        }
        return ro;
    }


    //登录的页面
    @GetMapping("/loan/page/login")
    public String pageLogin(@RequestParam(value = "returnUrl",required = false)String returnUrl,Model model){
        model.addAttribute("returnUrl",returnUrl);
        int registerUser=userService.queryRegisterUserCount();
        model.addAttribute("registerUsers",registerUser);

        //获取累计投资金额
        BigDecimal sumBidMoney=bidInfoService.querySumBidMoney();
        model.addAttribute("sumBidMoney",sumBidMoney);


        return "login";
    }

    //登录
    @PostMapping("/loan/login")
    @ResponseBody
    public ResultObject login(@RequestParam("phone")String phone,
                              @RequestParam("pwd")String password,
                              HttpSession session){
        ResultObject ro=ResultObject.error("登录失败");
        if(CommonUtils.checkPhone(phone)&&password!=null && password.length()==32){
            User user=userService.login(phone,password);
            //登录成功，user放到session中
           if(user!=null){
               ro=ResultObject.success("登录成功");
               session.setAttribute(CommonContants.SESSION_USER_KEY,user);
           }else{
               ro.setMessage("登录手机号或者密码无效");
           }
        }else{
            ro.setMessage("登录手机号或者密码无效");
        }
        return ro;

    }


    //用户中心
    @GetMapping("/loan/myCenter")
    public String pageMyCenter(HttpSession session,Model model){

        BigDecimal accountMoney=new BigDecimal("0");
        //用户金额
        User user= (User) session.getAttribute(CommonContants.SESSION_USER_KEY);
        FinanceAccount account=financeAcountService.queryAccount(user.getId());
        if(account!=null){
            accountMoney=account.getAvailableMoney();
        }
        //最近投资记录
        List<UserBidInfo> userBidInfos=bidInfoService.queryRecentlyUserBidInfo(user.getId(),0,5);

        //最近的充值记录
        List<UserRechargeInfo> userRechargeInfos=rechargeService.
                queryRecentlyUserReChargeInfo(user.getId(),0,5);


        //最近的收益记录
        List<UserIncomeInfo> userIncomeInfoList=incomeService.queryRecentlyIncomeInfo(user.getId(),0,5);

        model.addAttribute("userBidInfos",userBidInfos);
        model.addAttribute("accountMoney",accountMoney);
        model.addAttribute("userRechargeInfos",userRechargeInfos);
        model.addAttribute("userIncomeInfoList",userIncomeInfoList);
        return "myCenter";
    }
    //退出
    @GetMapping("/loan/logout")
    public String logout(HttpSession session){

        //session无效
        session.removeAttribute(CommonContants.SESSION_USER_KEY);
        session.invalidate();

        return "redirect:/index";
    }

}
