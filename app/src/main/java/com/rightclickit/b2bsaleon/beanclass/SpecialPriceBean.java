package com.rightclickit.b2bsaleon.beanclass;

/**
 * Created by PPS on 6/30/2017.
 */

public class SpecialPriceBean {
    private String specialUserId;
    private String specialProductId;
    private String specialPrice;

    public String getSpecialUserId() {
        return specialUserId;
    }

    public void setSpecialUserId(String specialUserId) {
        this.specialUserId = specialUserId;
    }

    public String getSpecialProductId() {
        return specialProductId;
    }

    public void setSpecialProductId(String specialProductId) {
        this.specialProductId = specialProductId;
    }

    public String getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(String specialPrice) {
        this.specialPrice = specialPrice;
    }

    @Override
    public String toString() {
        return "SpecialPriceBean{" +
                "specialUserId='" + specialUserId + '\'' +
                ", specialProductId='" + specialProductId + '\'' +
                ", specialPrice='" + specialPrice + '\'' +
                '}';
    }
}
