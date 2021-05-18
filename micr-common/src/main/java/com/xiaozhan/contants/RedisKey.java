package com.xiaozhan.contants;

public class RedisKey {
    //redis key 自己的命名规则，类别：子类别：项目
    public static final String APP_REGISTER_USER="INDEX:USER:REGITER";
    //注册时，短信验证码的key---唯一性REGITER：SMSCODE:13400009999
    public static final String APP_REGISTER_SMS_CODE = "REGITER：SMSCODE:";
    //投资排行榜
    public static final String BID_INVEST_TOP="BID:INVEST:TOP";
    //商户订单号，产生唯一值；
    public static final String ALIPAY_OUT_TRANDE_NO="PAY:ALIPAY:TRADENO";
    //
    public static final String ALIPAY_ALL_TRANDE_NO="ALIPAY:ALL:TRANDE:NO";
}
