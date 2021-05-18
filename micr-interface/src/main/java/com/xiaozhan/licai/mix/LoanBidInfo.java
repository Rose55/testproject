package com.xiaozhan.licai.mix;

import com.xiaozhan.licai.model.BidInfo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
//产品的信息和投资信息的组合
public class LoanBidInfo extends BidInfo implements Serializable {
    private static final long serialVersionUID = -4966265700073223055L;
    private String phone;

    @Override
    public String toString() {
        return "LoanBidInfo{" +
                "phone='" + phone + '\'' +
                '}';
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LoanBidInfo(String phone) {
        this.phone = phone;
    }

    public LoanBidInfo() {

    }
}
