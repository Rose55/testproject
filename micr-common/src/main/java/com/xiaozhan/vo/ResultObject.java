package com.xiaozhan.vo;

import com.xiaozhan.contants.Code;

//ajax请求的返回结果
public class ResultObject {
    /*返回状态码*/
    private Integer code;
    /*返回状态码的解读*/
    private String message;
//   数据
    private Object data;
    /*创建一个 默认错误的 ro对象*/
    public static ResultObject error(String msg){
        ResultObject ro=new ResultObject(Code.UNKNOWN_ERROR,msg,"");
        return ro;
    }
    /*创建一个 成功的 ro对象*/
    public static ResultObject success(String msg){
        ResultObject ro=new ResultObject(Code.SUCCESS_OK,msg,"");
        return ro;
    }



    @Override
    public String toString() {
        return "ResultObject{}";
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResultObject(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResultObject() {
    }
}
