package com.xiaozhan.licai.service;

import com.xiaozhan.licai.model.FinanceAccount;

public interface FinanceAcountService {
    //根据uid查询账号的金额
    FinanceAccount queryAccount(Integer uid);
}
