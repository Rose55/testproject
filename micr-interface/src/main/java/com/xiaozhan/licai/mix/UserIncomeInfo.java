package com.xiaozhan.licai.mix;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UserIncomeInfo implements Serializable {

    private static final long serialVersionUID = -6492042392375946559L;
    private String productName;
    private BigDecimal imoney;
    private Date itime;

    @Override
    public String toString() {
        return "UserIncomeInfo{" +
                "productName='" + productName + '\'' +
                ", imoney=" + imoney +
                ", itime=" + itime +
                '}';
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getImoney() {
        return imoney;
    }

    public void setImoney(BigDecimal imoney) {
        this.imoney = imoney;
    }

    public Date getItime() {
        return itime;
    }

    public void setItime(Date itime) {
        this.itime = itime;
    }

    public UserIncomeInfo(String productName, BigDecimal imoney, Date itime) {
        this.productName = productName;
        this.imoney = imoney;
        this.itime = itime;
    }

    public UserIncomeInfo() {
    }
}
