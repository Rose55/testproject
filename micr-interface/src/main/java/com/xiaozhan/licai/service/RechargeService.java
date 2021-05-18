package com.xiaozhan.licai.service;

import com.xiaozhan.licai.mix.UserRechargeInfo;
import com.xiaozhan.licai.model.RechargeRecord;

import java.util.List;

//充值的服务
public interface RechargeService {

    //获取用户最近的充值记录
    List<UserRechargeInfo> queryRecentlyUserReChargeInfo(Integer uid, Integer pageNo,
                                                         Integer pageSize);
    //创建充值记录
    int addRecharge(RechargeRecord record);
    int doAlipayNotify(String outTradeNo,String tradeStatus,String totalAmount);
}
