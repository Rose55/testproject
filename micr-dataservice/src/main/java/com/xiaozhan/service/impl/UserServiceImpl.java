package com.xiaozhan.service.impl;

import com.xiaozhan.common.CommonUtils;
import com.xiaozhan.contants.RedisKey;
import com.xiaozhan.licai.mapper.FinanceAccountMapper;
import com.xiaozhan.licai.mapper.UserMapper;
import com.xiaozhan.licai.model.FinanceAccount;
import com.xiaozhan.licai.model.User;
import com.xiaozhan.licai.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.omg.CORBA.portable.ValueOutputStream;
import org.omg.DynamicAny.DynValueOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@DubboService(interfaceClass = UserService.class,version = "1.0")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private FinanceAccountMapper financeAccountMapper;
    //创建一个日志门面(slf4)中的对象
    Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);

    /*查询平台注册用户的数量*/
    @Override
    public int queryRegisterUserCount() {

        //使用logger对象的方法，输出信息
        logger.debug("queryRegisterUserCount 调试信息： 开始执行方法"+new Date());
        //1、从redis获取数据
        ValueOperations vo=redisTemplate.opsForValue();
        Integer countUsers = (Integer) vo.get(RedisKey.APP_REGISTER_USER);
       logger.debug("queryRegisterUserCount 调试信息，从redis获取信息： "+countUsers);
        if(countUsers==null){
            synchronized (this){//同步
                //再查redis的数据
                countUsers=(Integer)vo.get(RedisKey.APP_REGISTER_USER);
                if(countUsers==null){
                    //2、从数据库获取数据
                    countUsers=userMapper.selectCountUser();
                    //3、把数据放入到redis
                    vo.set(RedisKey.APP_REGISTER_USER,countUsers,30, TimeUnit.MINUTES);
                }

            }
        }
        logger.info("queryRegisterUserCount 信息 ："+countUsers);
        logger.debug("queryRegisterUserCount 调试信息 ： 方法执行完毕 ："+new Date());
        return countUsers;
    }

    //按手机号查询
    @Override
    public User queryByPhone(String phone) {
        User user=null;
        if(CommonUtils.checkPhone(phone)){
            user=userMapper.selectByPhone(phone);
        }
        return user;
    }
    /*//用户注册
    @Override
    public User userRegister(String phone, String pwd) {
        //1、判断手机号是否注册过
        User user=userMapper.selectByPhone(phone);
        if(user==null){
            user.setPhone(phone);
            user.setLoginPassword(pwd);
            userMapper.insert(user);
            //2、查询phone的对于的User 的 id
            user=userMapper.selectByPhone(phone);
            Integer id=user.getId();
            //添加u_finace_account

        }
        return null;
    }*/
    //用户注册
    @Transactional
    @Override
    public User userRegister(String phone, String pwd) {
        //1、判断手机号是否注册过
        User user=userMapper.selectByPhone(phone);
        if(user==null){
            //2、添加u_user记录，返回id

            user=new User();
            user.setPhone(phone);
            user.setLoginPassword(pwd);
            user.setAddTime(new Date());
            int rows=userMapper.insertUserReturnId(user);
            if(rows<1){
                throw new RuntimeException("添加用户异常");
            }
            //3、添加u_finace_account
            FinanceAccount account=new FinanceAccount();
            account.setUid(user.getId());
            account.setAvailableMoney(new BigDecimal("888"));
            rows=financeAccountMapper.insertSelective(account);
            if(rows<1){
                throw new RuntimeException("添加资金账户异常");
            }

        }else {
            user=null;
        }
        return user;
    }

    //更新用户
    @Override
    public int modifyUser(User user) {
        int rows=userMapper.updateByPrimaryKeySelective(user);
        return rows;
    }

    //登录
    @Override
    public User login(String phone, String password) {
        User user=userMapper.selectByPhone(phone);
        if(user!=null){
            if(user.getLoginPassword().equals(password)){
                //登录成功，更新此用户登录时间
                user.setLastLoginTime(new Date());
                userMapper.updateByPrimaryKeySelective(user);
            }else{
                user=null;
            }
        }


        return user;
    }


}
