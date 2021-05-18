package com.xiaozhan.licai.mapper;

import com.xiaozhan.licai.model.FinanceAccount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface FinanceAccountMapper {

    //根据uid查询账号的金额
    FinanceAccount selectByUid(@Param("uid") Integer uid);
    //投资
    //查询的数据需要上锁
    FinanceAccount selectUidForUpdate(@Param("uid") Integer uid);

    //投资 ，扣除金额
    int updateAvaliableMoneyInvest(@Param("uid") Integer uid, @Param("bidMoney") BigDecimal bidMoney);
    //收益  增加金额
    int updateAvaiableMoneyInCome(@Param("uid") Integer uid, @Param("bidMoney") BigDecimal bidMoney, @Param("incomeMomney") BigDecimal incomeMomney);



    //收益  增加金额
    int updateAvaiableMoneyRecharge(@Param("uid") Integer uid, @Param("rechargeMoney") BigDecimal rechargeMoney);



    int deleteByPrimaryKey(Integer id);

    int insert(FinanceAccount record);

    int insertSelective(FinanceAccount record);

    FinanceAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinanceAccount record);

    int updateByPrimaryKey(FinanceAccount record);
}