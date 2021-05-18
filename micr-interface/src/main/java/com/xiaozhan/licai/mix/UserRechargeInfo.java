package com.xiaozhan.licai.mix;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UserRechargeInfo implements Serializable {

    private static final long serialVersionUID = -2168322838228556786L;
    private BigDecimal money;
    private Date time;
    private String cresult;

    @Override
    public String toString() {
        return "UserRechargeInfo{" +
                "money=" + money +
                ", time=" + time +
                ", cresult='" + cresult + '\'' +
                '}';
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getCresult() {
        return cresult;
    }

    public void setCresult(String cresult) {
        this.cresult = cresult;
    }

    public UserRechargeInfo(BigDecimal money, Date time, String cresult) {
        this.money = money;
        this.time = time;
        this.cresult = cresult;
    }

    public UserRechargeInfo() {
    }
}
