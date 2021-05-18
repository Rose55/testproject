package com.xiaozhan.service.impl;

import com.xiaozhan.common.DecimalUtil;
import com.xiaozhan.common.PageUtils;
import com.xiaozhan.contants.CommonContants;
import com.xiaozhan.licai.mapper.FinanceAccountMapper;
import com.xiaozhan.licai.mapper.RechargeRecordMapper;
import com.xiaozhan.licai.mix.UserRechargeInfo;
import com.xiaozhan.licai.model.FinanceAccount;
import com.xiaozhan.licai.model.RechargeRecord;
import com.xiaozhan.licai.service.RechargeService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@DubboService(interfaceClass = RechargeService.class,version = "1.0")
public class RechargeServiceImpl implements RechargeService{
    @Resource
    private RechargeRecordMapper rechargeRecordMapper;
    @Resource
    private FinanceAccountMapper financeAccountMapper;

    //获取用户最近的充值记录
    @Override
    public List<UserRechargeInfo> queryRecentlyUserReChargeInfo(Integer uid,
                                                                Integer pageNo,
                                                                Integer pageSize) {
        List<UserRechargeInfo> list=new ArrayList<>();
        if(uid!=null&&uid>0){
            pageNo= PageUtils.defaultPageNo(pageNo);
            pageSize=PageUtils.defaultPageSize(pageSize);
            int offset=(pageNo-1)*pageSize;
            list=rechargeRecordMapper.queryRecentlyUserRechargeInfo(uid,offset,pageSize);
        }
        return list;
    }

    //创建充值记录
    @Override
    public int addRecharge(RechargeRecord record) {

        int rows=rechargeRecordMapper.insertSelective(record);
        return rows;

    }

    /*outTradeNo:商家订单号
    * tradeStatus:支付宝的通知的充值结果
    * totalAmount:支付宝的通知的支付金额
    * return:1:充值成功   2：充值失败 3：金额不一致*/
    @Override
    public int doAlipayNotify(String outTradeNo, String tradeStatus, String totalAmount) {
            int result=0;
            int rows=0;
            //1.查询订单是否存在
            RechargeRecord record=rechargeRecordMapper.selectByRechargeNo(outTradeNo);
            //2.订单存在并且是充值中的状态，才需要处理
        if(record!=null&&record.getRechargeStatus()== CommonContants.INCOME_STATUS_PROCESSING){

            //检查金额和你的库中记录是否一致
            if(DecimalUtil.eq(record.getRechargeMoney(),new BigDecimal(totalAmount))){
                //判断充值的结果
                if("TRADE_SUCCESS".equals(tradeStatus)){
                    //充值成功：1 更新资金余额  2：修改充值的状态为1（成功）
                    FinanceAccount account=financeAccountMapper.selectUidForUpdate(record.getUid());
                    if(account!=null){
                        rows=financeAccountMapper.updateAvaiableMoneyRecharge(record.getUid(),record.getRechargeMoney());
                        if(rows<1){
                            throw  new RuntimeException("充值处理支付一步通知：更新资金余额失败");

                        }
                        //修改充值的状态为1（成功）
                        rows=rechargeRecordMapper.updateRechargeStatus(record.getId(),CommonContants.INCOME_STATUS_SUCCESS);
                        if(rows<1){
                            throw  new RuntimeException("充值处理支付一步通知：更新充值记录状态成功时失败");

                        }
                        result=1;
                    }else{
                        //资金账号不存在
                        result=4;
                    }
                }else{
                    //充值失败
                    rows=rechargeRecordMapper.updateRechargeStatus(record.getId(),CommonContants.INCOME_STATUS_FAILED);
                    if(rows<1){
                        throw  new RuntimeException("充值处理支付一步通知：更新充值记录状态成功时失败");
                    }
                    result=2;
                }
            }else{
                //金额不一样
                result=3;
            }
        }else{
            //记录不存在或者已经处理、
            result=5;
        }
            return result;


    }
}
