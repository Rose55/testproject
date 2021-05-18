package com.xiaozhan.service.impl;

import com.xiaozhan.common.DecimalUtil;
import com.xiaozhan.common.PageUtils;
import com.xiaozhan.contants.CommonContants;
import com.xiaozhan.licai.mapper.BidInfoMapper;
import com.xiaozhan.licai.mapper.FinanceAccountMapper;
import com.xiaozhan.licai.mapper.LoanInfoMapper;
import com.xiaozhan.licai.mix.UserBidInfo;
import com.xiaozhan.licai.model.BidInfo;
import com.xiaozhan.licai.model.FinanceAccount;
import com.xiaozhan.licai.model.LoanInfo;
import com.xiaozhan.licai.service.BidInfoService;
import com.xiaozhan.licai.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DubboService(interfaceClass = BidInfoService.class,version = "1.0")
public class BidInfoServiceImpl implements BidInfoService {
    @Resource
    private BidInfoMapper bidInfoMapper;
    @Resource
    private FinanceAccountMapper financeAccountMapper;
    @Resource
    private LoanInfoMapper loanInfoMapper;

    /*查询累计金额*/
    @Override
    public BigDecimal querySumBidMoney() {
        return bidInfoMapper.selectSumBidMoney();
    }

   //用户最近的投资记录 ，分页
    @Override
    public List<UserBidInfo> queryRecentlyUserBidInfo(Integer uid, Integer pageNo, Integer pageSize) {
        List<UserBidInfo> list=new ArrayList<>();
        if(uid!=null&& uid>0){
            pageNo= PageUtils.defaultPageNo(pageNo);
            pageSize=PageUtils.defaultPageSize(pageSize);
            int offset=(pageNo-1)*pageSize;
            list=bidInfoMapper.selectPageUserBidInfo(uid,offset,pageSize);
        }

        return list;
    }

    //投资 return true:投资成功
    @Override
    public boolean invest(Integer id, Integer loanId, BigDecimal bidMoney) {

        int rows=0;
        boolean result=false;
        //1、检查用户的金额
        FinanceAccount account=financeAccountMapper.selectUidForUpdate(id);
        if(account!=null){
            if(DecimalUtil.le(bidMoney,account.getAvailableMoney())){
                //资金充足，扣除金额
                rows=financeAccountMapper.updateAvaliableMoneyInvest(id,bidMoney);
                if(rows<1){

                    throw  new RuntimeException("投资-扣除用户资金失败");
                }
                //处理产品
                LoanInfo loanInfo=loanInfoMapper.selectByPrimaryKey(loanId);
                if(loanInfo!=null){
                    //状态 ,必须是0的状态才能购买
                    if(loanInfo.getProductStatus()== CommonContants.LAON_PRODUCT_TYPE_XINSHOUBAO_0){
                        //bidMoney>=min<=max,<leftMoney
                        if(DecimalUtil.le(bidMoney,loanInfo.getLeftProductMoney())
                        && DecimalUtil.ge(bidMoney,loanInfo.getBidMinLimit()) &&
                        DecimalUtil.le(bidMoney,loanInfo.getBidMaxLimit())){
                            //可以投资
                            //扣除产品的 leftMoney
                            rows=loanInfoMapper.updarteLeftMoneyInvest(loanId,bidMoney);
                            if(rows<1){
                                throw  new RuntimeException("投资-扣除产品的剩余可投资金额失败");
                            }
                            //创建投资记录
                            BidInfo bidInfo=new BidInfo();
                            bidInfo.setBidMoney(bidMoney);
                            bidInfo.setBidStatus(1);
                            bidInfo.setBidTime(new Date());
                            bidInfo.setLoanId(loanId);
                            bidInfo.setUid(id);
                            rows=bidInfoMapper.insertSelective(bidInfo);
                            //查看产品是否满标
                            LoanInfo queryLoan=loanInfoMapper.selectByPrimaryKey(loanId);
                            if(queryLoan.getProductStatus().intValue()==0){
                                //满标 更新产品的状态为1
                                queryLoan.setProductStatus(CommonContants.LAON_PRODUCT_TYPE_XINSHOUBAO_1);
                                queryLoan.setProductFullTime(new Date());
                                rows=loanInfoMapper.updateByPrimaryKeySelective(queryLoan);
                                if(rows<1){
                                    throw  new RuntimeException("投资-更新产品是满标状态异常");
                                }
                            }
                            result=true;

                        }
                    }
                }
            }
        }
        return result;

    }
}
