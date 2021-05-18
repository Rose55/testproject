package com.xiaozhan.vo;

import java.io.Serializable;

public class BidTopBean implements Serializable {
    private static final long serialVersionUID = -5516247676846960169L;
    private String phone;
    private Double bidMoney;

    @Override
    public String toString() {
        return "BidTopBean{" +
                "phone='" + phone + '\'' +
                ", bidMoney=" + bidMoney +
                '}';
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getBidMoney() {
        return bidMoney;
    }

    public void setBidMoney(Double bidMoney) {
        this.bidMoney = bidMoney;
    }

    public BidTopBean(String phone, Double bidMoney) {
        this.phone = phone;
        this.bidMoney = bidMoney;
    }

    public BidTopBean() {
    }
}
