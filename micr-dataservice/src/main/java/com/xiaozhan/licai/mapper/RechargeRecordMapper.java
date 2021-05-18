package com.xiaozhan.licai.mapper;

import com.xiaozhan.licai.mix.UserRechargeInfo;
import com.xiaozhan.licai.model.RechargeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RechargeRecordMapper {
    //获取用户最近的充值记录
    List<UserRechargeInfo> queryRecentlyUserRechargeInfo(@Param("uid") Integer uid, @Param("offset") Integer offset,
                                                         @Param("pageSize") Integer pageSize);

    //根据充值的订单号查询记录
    RechargeRecord selectByRechargeNo(@Param("outTradeNo") String outTradeNo);
   //更新充值记录的状态
    int updateRechargeStatus(@Param("id") Integer id, @Param("status") Integer status);

    int deleteByPrimaryKey(Integer id);

    int insert(RechargeRecord record);

    int insertSelective(RechargeRecord record);

    RechargeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RechargeRecord record);

    int updateByPrimaryKey(RechargeRecord record);
}