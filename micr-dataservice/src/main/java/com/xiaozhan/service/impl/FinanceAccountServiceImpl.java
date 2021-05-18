package com.xiaozhan.service.impl;

import com.xiaozhan.licai.mapper.FinanceAccountMapper;
import com.xiaozhan.licai.model.FinanceAccount;
import com.xiaozhan.licai.service.FinanceAcountService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@DubboService(interfaceClass = FinanceAcountService.class,version = "1.0")
public class FinanceAccountServiceImpl implements FinanceAcountService {

    @Resource
    private FinanceAccountMapper financeAccountMapper;
    //根据uid查询账号的金额
    @Override
    public FinanceAccount queryAccount(Integer uid) {
        return financeAccountMapper.selectByUid(uid);
    }
}
