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
            //???????????????????????????????????????????????????????????????

            pageNo= PageUtils.defaultPageNo(pageNo);
            pageSize=PageUtils.defaultPageSize(pageSize);
            loanInfoList=loanInfoService.queryPageByType(productType,pageNo,pageSize);
            //????????????
            int totalRecords=loanInfoService.queryRecordNumsByType(productType);
            //??????pageInfo
            PageInfo pageInfo=new PageInfo(pageNo,pageSize,totalRecords);
            // @TODO ??????????????? ???ZSet ????????????
            Set<ZSetOperations.TypedTuple<String>> set= stringRedisTemplate.opsForZSet().reverseRangeWithScores(RedisKey.BID_INVEST_TOP,0,5);
            List<BidTopBean> bidTopBeanList=new ArrayList<>();
            //??????set
            set.stream().forEach(s->{
                BidTopBean bidTopBean=new BidTopBean(s.getValue(),s.getScore());
                bidTopBeanList.add(bidTopBean);
            });

            model.addAttribute("productType",productType);
            model.addAttribute("page",pageInfo);
            model.addAttribute("loanInfoList",loanInfoList);
            model.addAttribute("bidTopBeanList",bidTopBeanList);
        }else{
            //??????:????????????
            viewName="error";

        }
        System.out.println("ptype="+productType);
        return "loan";
    }
    /*?????????????????????*/
    @GetMapping("/loan/loanInfo")
    public String loanInfo(@RequestParam("loanId")Integer loanId, Model model, HttpSession session){
        if(loanId!=null&&loanId.intValue()>0){
            //????????????id?????????????????????
            LoanInfo loanInfo=loanInfoService.queryByLoanId(loanId);

            //?????????????????? ????????????
            List<LoanBidInfo> loanBidInfoList=loanInfoService.queryBidInfoLoanId(loanId);
            User user=(User)session.getAttribute(CommonContants.SESSION_USER_KEY);
            if(user!=null){
                //?????????????????????
                FinanceAccount account=financeAcountService.queryAccount(user.getId());
                if(account!=null){
                    model.addAttribute("accountMoney",account.getAvailableMoney());
                }
            }

            //????????????model
            model.addAttribute("loanInfo",loanInfo);
            model.addAttribute("loanBidInfoList",loanBidInfoList);
            return "loanInfo";

        }else{
            return "error";

        }
    }
    //??????
    @PostMapping("/loan/invest")
    @ResponseBody
    public ResultObject invest(@RequestParam("loanId") Integer loanId, @RequestParam("bidMoney")BigDecimal bidMoney,HttpSession session){
        ResultObject ro=ResultObject.error("??????????????????????????????");
        if(loanId==null||loanId<0){
            ro.setMessage("??????????????????");
        }else if(bidMoney==null&&bidMoney.intValue()<100){
            ro.setMessage("?????????????????????");
        }else{
            //???????????????
            User user=(User)session.getAttribute(CommonContants.SESSION_USER_KEY);
            //??????
            boolean result=bidInfoService.invest(user.getId(),loanId,bidMoney);
            if(result){
                //?????????????????????????????????ZSet??????
                //increaseScore??????????????????Value????????????score?????????
                stringRedisTemplate.opsForZSet().incrementScore(RedisKey.BID_INVEST_TOP,user.getPhone(),bidMoney.doubleValue());
                ro=ResultObject.success("????????????");
            }
        }
        return ro;
    }


}
