package com.xiaozhan.contants;

import org.omg.PortableInterceptor.INACTIVE;

public class CommonContants {
    //定义产品的类型
    //新手宝0
    public static final Integer LAON_PRODUCT_TYPE_XINSHOUBAO_0=0;
    //优选1
    public static final Integer LAON_PRODUCT_TYPE_XINSHOUBAO_1=1;
    //散标2
    public static final Integer LAON_PRODUCT_TYPE_XINSHOUBAO_2=2;
    //session中的user的key
    public static final String SESSION_USER_KEY="LICAI_USER";
    //产品的状态
    //未满标
    public static final Integer LOAN_STATUS_NOTMANBIAO=0;
    //满标
    public static final Integer LOAN_STATUS_MANBIAO=1;
    //满标已生成收益计划
    public static final Integer LOAN_STATUS_MANBIAO_INCOME=2;
    //收益的状态 income_status
    //生成收益计划
    public static final Integer INCOME_STATUS_PLAN=0;
    //返还收益
    public static final Integer INCOME_STATUS_BACK=1;
    //充值状态
    //充值中
    public static final Integer INCOME_STATUS_PROCESSING=0;
    //充值成功
    public static final Integer INCOME_STATUS_SUCCESS=1;
    //充值失败
    public static final Integer INCOME_STATUS_FAILED=2;

}
