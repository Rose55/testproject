package com.xiaozhan.licai.mix;

import com.xiaozhan.licai.model.User;

import java.io.Serializable;

public class UserInfo extends User {
    //register： new--注册，old--存在
    private String type;

    @Override
    public String toString() {
        return "UserInfo{" +
                "type='" + type + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserInfo(String type) {
        this.type = type;
    }

    public UserInfo() {
    }
}
