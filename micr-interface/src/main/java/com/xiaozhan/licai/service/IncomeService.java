package com.xiaozhan.licai.service;

import com.xiaozhan.licai.mix.UserIncomeInfo;

import java.util.List;

//收益的service
public interface IncomeService {
    //某个用户最近的收益记录
    List<UserIncomeInfo> queryRecentlyIncomeInfo(Integer uid,Integer pageNo,Integer pageSize);

    //生成收益计划
    boolean generateIncomePlan();
    //收益的返还
    boolean genernateIncomeBack();

}
