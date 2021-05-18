package com.xiaozhan.service.impl;

import com.xiaozhan.common.DecimalUtil;
import com.xiaozhan.common.PageUtils;
import com.xiaozhan.contants.CommonContants;
import com.xiaozhan.licai.mapper.BidInfoMapper;
import com.xiaozhan.licai.mapper.FinanceAccountMapper;
import com.xiaozhan.licai.mapper.IncomeRecordMapper;
import com.xiaozhan.licai.mapper.LoanInfoMapper;
import com.xiaozhan.licai.mix.UserIncomeInfo;
import com.xiaozhan.licai.model.BidInfo;
import com.xiaozhan.licai.model.FinanceAccount;
import com.xiaozhan.licai.model.IncomeRecord;
import com.xiaozhan.licai.model.LoanInfo;
import com.xiaozhan.licai.service.IncomeService;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@DubboService(interfaceClass = IncomeService.class,version = "1.0")
public class IncomeServiceImpl implements IncomeService {

    @Resource
    private IncomeRecordMapper incomeRecordMapper;
    @Resource
    private LoanInfoMapper loanInfoMapper;

    @Resource
    private BidInfoMapper bidInfoMapper;
    @Resource private FinanceAccountMapper financeAccountMapper;
    //某个用户最近的收益记录
    @Override
    public List<UserIncomeInfo> queryRecentlyIncomeInfo(Integer uid,Integer pageNo,Integer pageSize) {
       List<UserIncomeInfo> list=new ArrayList<>();
       if(uid!=null&&uid>0){
           pageNo= PageUtils.defaultPageNo(pageNo);
           pageSize=PageUtils.defaultPageSize(pageSize);
           int offset=(pageNo-1)*pageSize;
           list=incomeRecordMapper.selectRecentlyInfo(uid,offset,pageSize);
       }
       return list;
    }

    //生成收益计划
    @Transactional
    @Override
    public synchronized boolean generateIncomePlan() {

        BigDecimal rate =new BigDecimal("0");
        BigDecimal incomeMoney=new BigDecimal("0");
        Double cycle=0.0;
        Date incomeDate=null;
        int rows=0;
        boolean result=false;
        //1. 获取满标的产品
        List<LoanInfo> loanInfoList=loanInfoMapper.selectByStatus(CommonContants.LOAN_STATUS_MANBIAO);
        //2. 获取每个产品的 投资记录
        for(LoanInfo loanInfo:loanInfoList){
            List<BidInfo> bidInfoList=bidInfoMapper.selectByLoanId(loanInfo.getId());
            //3.循环投资记录 计算 收益
            for (BidInfo bidInfo : bidInfoList) {

                rate=DecimalUtil.devide(loanInfo.getRate(),new BigDecimal("100"));
                //收益=投资金额*利率*周期
                if(loanInfo.getProductType()==CommonContants.LAON_PRODUCT_TYPE_XINSHOUBAO_0){
                    //周期是天
                    cycle=(loanInfo.getCycle()*1.0)/365;
                    incomeMoney=DecimalUtil.multiply(bidInfo.getBidMoney(),rate).multiply(new BigDecimal(cycle));
                    //到期时间=满标时间/产品的周期
                    incomeDate=DateUtils.addDays(loanInfo.getProductFullTime(),loanInfo.getCycle());

                }else{
                    //收益=投资金额*利率*周期
                    //周期是月
                    cycle=(loanInfo.getCycle()*30*1.0)/365;
                    incomeMoney=DecimalUtil.multiply(bidInfo.getBidMoney(),rate).multiply(new BigDecimal(cycle));

                    incomeDate=DateUtils.addMonths(loanInfo.getProductFullTime(),loanInfo.getCycle());

                }
                //生成收益计划
                IncomeRecord ir=new IncomeRecord();
                ir.setBidId(bidInfo.getId());
                ir.setBidMoney(bidInfo.getBidMoney());
                ir.setIncomeMoney(incomeMoney);
                ir.setIncomeDate(incomeDate);
                ir.setIncomeStatus(CommonContants.INCOME_STATUS_PLAN);
                ir.setLoanId(loanInfo.getId());
                ir.setUid(bidInfo.getUid());
                rows=incomeRecordMapper.insertSelective(ir);
                if(rows<0){
                    throw  new RuntimeException("收益生成计划-创建收益记录失败");
                }






            }
            //需要去修改产品的状态是： 满标已生成收益计划
            loanInfo.setProductStatus(CommonContants.LOAN_STATUS_MANBIAO_INCOME);
            rows=loanInfoMapper.updateByPrimaryKeySelective(loanInfo);
            if(rows<0){
                throw new RuntimeException("收益计划的生成-更新产品状态失败 ");
            }
            result=true;
        }
        return result;
    }

    //收益的返还
    @Transactional
    @Override
    public synchronized boolean genernateIncomeBack() {
        boolean result=false;
        int rows=0;
        //1.获取到期的收益计划
        List<IncomeRecord> incomeRecordList=incomeRecordMapper.selectExpireIncome();
        //2.遍历数据
        for (IncomeRecord incomeRecord : incomeRecordList) {
            //获取 收益和投资金额
            //获取资金账户 锁定它
            FinanceAccount account=financeAccountMapper.selectUidForUpdate(incomeRecord.getUid());
            if(account!=null){
                rows=financeAccountMapper.updateAvaiableMoneyInCome(incomeRecord.getUid(),incomeRecord.getBidMoney(),incomeRecord.getIncomeMoney());
                if(rows<1){
                    throw  new RuntimeException("收益返还-更新资金账户失败");
                }
                incomeRecord.setIncomeStatus(CommonContants.INCOME_STATUS_BACK);
                rows=incomeRecordMapper.updateByPrimaryKeySelective(incomeRecord);
                if(rows<1){
                    throw new RuntimeException("收益返还-更新收益状态失败");
                }
                result=true;
            }
        }
        return result;

    }
}
