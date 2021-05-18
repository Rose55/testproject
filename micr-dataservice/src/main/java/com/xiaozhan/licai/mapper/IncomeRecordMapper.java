package com.xiaozhan.licai.mapper;

import com.xiaozhan.licai.mix.UserIncomeInfo;
import com.xiaozhan.licai.model.IncomeRecord;
import com.xiaozhan.licai.model.LoanInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IncomeRecordMapper {

    //查询 到期的收益计划
    List<IncomeRecord> selectExpireIncome();

    //某个用户最近的收益记录
    List<UserIncomeInfo> selectRecentlyInfo(@Param("uid") Integer uid, @Param("affest") Integer affest, @Param("rows") Integer rows);
    int deleteByPrimaryKey(Integer id);

    int insert(IncomeRecord record);

    int insertSelective(IncomeRecord record);

    IncomeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncomeRecord record);

    int updateByPrimaryKey(IncomeRecord record);
}