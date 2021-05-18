package com.xiaozhan.licai.service;

import com.xiaozhan.licai.mix.UserBidInfo;

import java.math.BigDecimal;
import java.util.List;

public interface BidInfoService {
    //累计投资金额
    BigDecimal querySumBidMoney();
//   用户最近的投资记录 ，分页
    List<UserBidInfo> queryRecentlyUserBidInfo(Integer uid,Integer pageNo,Integer pageSize);

    //投资
    boolean invest(Integer id, Integer loanId, BigDecimal bidMoney);
}
