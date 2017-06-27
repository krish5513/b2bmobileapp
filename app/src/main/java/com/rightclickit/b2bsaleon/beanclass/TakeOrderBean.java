package com.rightclickit.b2bsaleon.beanclass;

import java.io.Serializable;

/**
 * Created by Sekhar Kuppa
 */

public class TakeOrderBean implements Serializable {

    private String mProductId;
    private String mRouteId;
    private String mProductTitle;
    private String mProductFromDate;
    private String mProductToDate;
    private String mProductOrderType;
    private String mProductQuantity;
    private String mProductStatus;
    private String mEnquiryId;

    public String getmProductId() {
        return mProductId;
    }

    public void setmProductId(String mProductId) {
        this.mProductId = mProductId;
    }

    public String getmRouteId() {
        return mRouteId;
    }

    public void setmRouteId(String mRouteId) {
        this.mRouteId = mRouteId;
    }

    public String getmProductTitle() {
        return mProductTitle;
    }

    public void setmProductTitle(String mProductTitle) {
        this.mProductTitle = mProductTitle;
    }

    public String getmProductFromDate() {
        return mProductFromDate;
    }

    public void setmProductFromDate(String mProductFromDate) {
        this.mProductFromDate = mProductFromDate;
    }

    public String getmProductToDate() {
        return mProductToDate;
    }

    public void setmProductToDate(String mProductToDate) {
        this.mProductToDate = mProductToDate;
    }

    public String getmProductOrderType() {
        return mProductOrderType;
    }

    public void setmProductOrderType(String mProductOrderType) {
        this.mProductOrderType = mProductOrderType;
    }

    public String getmProductQuantity() {
        return mProductQuantity;
    }

    public void setmProductQuantity(String mProductQuantity) {
        this.mProductQuantity = mProductQuantity;
    }

    public String getmProductStatus() {
        return mProductStatus;
    }

    public void setmProductStatus(String mProductStatus) {
        this.mProductStatus = mProductStatus;
    }

    public String getmEnquiryId() {
        return mEnquiryId;
    }

    public void setmEnquiryId(String mEnquiryId) {
        this.mEnquiryId = mEnquiryId;
    }
}
