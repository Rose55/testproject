package com.xiaozhan.licai.mapper;

import com.xiaozhan.licai.model.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    //添加u_user记录，返回id
    int insertUserReturnId(User user);
    /*查询平台注册用户的数量*/
    int selectCountUser();
    //使用手机号来查询用户
    User selectByPhone(@Param("phone") String phone);
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);


}