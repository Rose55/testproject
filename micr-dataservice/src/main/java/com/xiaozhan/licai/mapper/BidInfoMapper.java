package com.xiaozhan.licai.mapper;

import com.xiaozhan.licai.mix.UserBidInfo;
import com.xiaozhan.licai.model.BidInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BidInfoMapper {

    //查询产品的投资记录
    List<BidInfo> selectByLoanId(@Param("loanId") Integer loanId);
    //用户最近的投资记录 ，分页
    List<UserBidInfo> selectPageUserBidInfo(@Param("uid") Integer uid, @Param("offSet") Integer offSet, @Param("rows") Integer rows);
    /*累计成交投资金额*/
    BigDecimal selectSumBidMoney();
    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);
}