package com.example.DTO;

public class PayDTO {
    private int userid;
    private String outTradeNo;
    private String totalAmount;
    private int effectiveDay;

    // Getters and Setters
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getEffectiveDay() {
        return effectiveDay;
    }

    public void setEffectiveDay(int effectiveDay) {
        this.effectiveDay = effectiveDay;
    }
}
