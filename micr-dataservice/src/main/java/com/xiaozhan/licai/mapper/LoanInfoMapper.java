package com.xiaozhan.licai.mapper;

import com.xiaozhan.licai.mix.LoanBidInfo;
import com.xiaozhan.licai.model.LoanInfo;
import org.apache.ibatis.annotations.Param;


import java.math.BigDecimal;
import java.util.List;

public interface LoanInfoMapper {
    /*某产品的投资记录*/
    List<LoanBidInfo> selectBidInfoByLoanId(@Param("loanId") Integer loanId);
    /*收益率的平均值rate*/
    BigDecimal selectAvgRate();

    /*按类型分页查询产品*/
    List<LoanInfo> selectPageByType(@Param("productType") Integer productType, @Param("offSet") Integer offSet, @Param("rows") Integer rows);
    /*获取分页查询产品总记录数*/
    int selectCountRecordByType(@Param("productType") Integer productType);
    //按产品的剩余可投资金额
    List<LoanInfo> selectByStatus(@Param("status") Integer status);

    //扣除产品的投资金额
    int updarteLeftMoneyInvest(@Param("loanId") Integer loanId, @Param("bidMoney") BigDecimal bidMoney);
    int deleteByPrimaryKey(Integer id);

    int insert(LoanInfo record);

    int insertSelective(LoanInfo record);

    LoanInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoanInfo record);

    int updateByPrimaryKey(LoanInfo record);
}