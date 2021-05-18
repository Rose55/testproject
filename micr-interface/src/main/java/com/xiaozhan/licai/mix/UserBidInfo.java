package com.xiaozhan.licai.mix;

import com.xiaozhan.licai.model.BidInfo;

import java.io.Serializable;

public class UserBidInfo extends BidInfo implements Serializable {
    private static final long serialVersionUID = 2599395046155782842L;
    private String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
