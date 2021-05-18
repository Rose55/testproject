package com.xiaozhan.web.controller;

import com.xiaozhan.contants.CommonContants;
import com.xiaozhan.licai.model.LoanInfo;
import com.xiaozhan.licai.service.BidInfoService;
import com.xiaozhan.licai.service.LoanInfoService;
import com.xiaozhan.licai.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class IndexController {
    @DubboReference(interfaceClass = UserService.class,version = "1.0")
    private UserService userService;
    @DubboReference(interfaceClass = BidInfoService.class,version = "1.0")
    private BidInfoService bidInfoService;
    @DubboReference(interfaceClass = LoanInfoService.class,version = "1.0")
    private LoanInfoService loanInfoService;
    @GetMapping({"/index", "/"})
    public String index(Model model){
        //获取平台注册总用户数量
        int countUers=userService.queryRegisterUserCount();
        model.addAttribute("registerUsers",countUers);
        //获取累计投资金额
        BigDecimal sumBidMoney=bidInfoService.querySumBidMoney();
        model.addAttribute("sumBidMoney",sumBidMoney);
        //获取历史的平均收益率
        BigDecimal avgRate=loanInfoService.queryHistoryAvgRate();
        model.addAttribute("avgHistoryRate",avgRate);
        //获取新手宝的产品
        List<LoanInfo> loanInfoList=loanInfoService.queryPageByType(CommonContants.LAON_PRODUCT_TYPE_XINSHOUBAO_0,1,1);
        model.addAttribute("xinshouBaoList",loanInfoList);
        //优选====>因为首页有4个数据
        List<LoanInfo> youloanInfoList=loanInfoService.queryPageByType(CommonContants.LAON_PRODUCT_TYPE_XINSHOUBAO_1,1,4);
        model.addAttribute("youXuanList",youloanInfoList);
       /* loanInfoList=loanInfoService.queryPageByType(CommonContants.LAON_PRODUCT_TYPE_XINSHOUBAO_1,1,4);
        model.addAttribute("youXuanList",loanInfoList);*/
        //散标===>首页有8个散标产品
        List<LoanInfo> sanloanInfoList=loanInfoService.queryPageByType(CommonContants.LAON_PRODUCT_TYPE_XINSHOUBAO_2,1,8);
        model.addAttribute("sanBiaoList",sanloanInfoList);
        return "index";
    }

}
