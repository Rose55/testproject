package com.xiaozhan.web.controller;

import com.xiaozhan.common.CommonUtils;
import com.xiaozhan.common.PageUtils;
import com.xiaozhan.contants.CommonContants;
import com.xiaozhan.contants.RedisKey;
import com.xiaozhan.licai.mix.LoanBidInfo;
import com.xiaozhan.licai.model.BidInfo;
import com.xiaozhan.licai.model.FinanceAccount;
import com.xiaozhan.licai.model.LoanInfo;
import com.xiaozhan.licai.model.User;
import com.xiaozhan.licai.service.BidInfoService;
import com.xiaozhan.licai.service.FinanceAcountService;
import com.xiaozhan.licai.service.LoanInfoService;
import com.xiaozhan.vo.BidTopBean;
import com.xiaozhan.vo.PageInfo;
import com.xiaozhan.vo.ResultObject;
import org.apache.dubbo.config.annotation.DubboReference;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class LoanInfoController {
    @DubboReference(interfaceClass = LoanInfoService.class,version = "1.0")
    private LoanInfoService loanInfoService;
    @DubboReference(interfaceClass = FinanceAcountService.class,version = "1.0")
    private FinanceAcountService financeAcountService;

    @DubboReference(interfaceClass = BidInfoService.class,version = "1.0")
    private BidInfoService bidInfoService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @GetMapping("/loan/loan")
    public String loanPageByType(@RequestParam("ptype")Integer productType,
                                 @RequestParam(value = "pageNo",required = false,defaultValue = "1")Integer pageNo,
                                 @RequestParam(value = "pageSize",required = false,defaultValue = "9")Integer pageSize, Model model){
        String viewName="loan";
        List<LoanInfo> loanInfoList=new ArrayList<>();
        if(CommonUtils.checkProductType(productType)){
            //产品类型是有效地，可以继续处理其他业务逻辑

            pageNo= PageUtils.defaultPageNo(pageNo);
            pageSize=PageUtils.defaultPageSize(pageSize);
            loanInfoList=loanInfoService.queryPageByType(productType,pageNo,pageSize);
            //总记录数
            int totalRecords=loanInfoService.queryRecordNumsByType(productType);
            //创建pageInfo
            PageInfo pageInfo=new PageInfo(pageNo,pageSize,totalRecords);
            // @TODO 投资排行榜 从ZSet 获取数据
            Set<ZSetOperations.TypedTuple<String>> set= stringRedisTemplate.opsForZSet().reverseRangeWithScores(RedisKey.BID_INVEST_TOP,0,5);
            List<BidTopBean> bidTopBeanList=new ArrayList<>();
            //变量set
            set.stream().forEach(s->{
                BidTopBean bidTopBean=new BidTopBean(s.getValue(),s.getScore());
                bidTopBeanList.add(bidTopBean);
            });

            model.addAttribute("productType",productType);
            model.addAttribute("page",pageInfo);
            model.addAttribute("loanInfoList",loanInfoList);
            model.addAttribute("bidTopBeanList",bidTopBeanList);
        }else{
            //提示:错误业务
            viewName="error";

        }
        System.out.println("ptype="+productType);
        return "loan";
    }
    /*产品的详情页面*/
    @GetMapping("/loan/loanInfo")
    public String loanInfo(@RequestParam("loanId")Integer loanId, Model model, HttpSession session){
        if(loanId!=null&&loanId.intValue()>0){
            //根据产品id，获取产品信息
            LoanInfo loanInfo=loanInfoService.queryByLoanId(loanId);

            //查询此产品的 投资记录
            List<LoanBidInfo> loanBidInfoList=loanInfoService.queryBidInfoLoanId(loanId);
            User user=(User)session.getAttribute(CommonContants.SESSION_USER_KEY);
            if(user!=null){
                //获取账户的资金
                FinanceAccount account=financeAcountService.queryAccount(user.getId());
                if(account!=null){
                    model.addAttribute("accountMoney",account.getAvailableMoney());
                }
            }

            //数据放到model
            model.addAttribute("loanInfo",loanInfo);
            model.addAttribute("loanBidInfoList",loanBidInfoList);
            return "loanInfo";

        }else{
            return "error";

        }
    }
    //投资
    @PostMapping("/loan/invest")
    @ResponseBody
    public ResultObject invest(@RequestParam("loanId") Integer loanId, @RequestParam("bidMoney")BigDecimal bidMoney,HttpSession session){
        ResultObject ro=ResultObject.error("投资失败，请稍后重试");
        if(loanId==null||loanId<0){
            ro.setMessage("产品信息不正");
        }else if(bidMoney==null&&bidMoney.intValue()<100){
            ro.setMessage("投资金额不正确");
        }else{
            //投资的处理
            User user=(User)session.getAttribute(CommonContants.SESSION_USER_KEY);
            //投资
            boolean result=bidInfoService.invest(user.getId(),loanId,bidMoney);
            if(result){
                //更新投资的排行榜，使用ZSet类型
                //increaseScore：累加，指定Value，对应的score的值、
                stringRedisTemplate.opsForZSet().incrementScore(RedisKey.BID_INVEST_TOP,user.getPhone(),bidMoney.doubleValue());
                ro=ResultObject.success("投资成功");
            }
        }
        return ro;
    }


}
