package com.xiaozhan.licai.service;

import com.xiaozhan.licai.mix.UserInfo;
import com.xiaozhan.licai.model.User;

public interface UserService {
    /*平台注册用户的数量*/
    int queryRegisterUserCount();
    //按手机号查询
    User queryByPhone(String phone);
    //用户注册
    User userRegister(String phone, String pwd);

    //更新用户
    int modifyUser(User user);

    //登录
    User login(String phone, String password);
}
