package com.xiaozhan.licai.service;

import com.xiaozhan.licai.mix.LoanBidInfo;
import com.xiaozhan.licai.model.LoanInfo;

import java.math.BigDecimal;
import java.util.List;

public interface LoanInfoService {
    //收益率的平均值
    BigDecimal queryHistoryAvgRate();
    /*分页查询产品*/
    List<LoanInfo> queryPageByType(Integer productType,
                                   Integer pageNo,
                                   Integer pageSize);
    /*获取分页查询产品总记录数*/
    int queryRecordNumsByType(Integer productType);
    /*使用主键，查询产品的信息*/
    LoanInfo queryByLoanId(Integer loanId);
    /*某产品的最近的3条投资记录*/
    List<LoanBidInfo> queryBidInfoLoanId(Integer loanId);
}
