package com.rightclickit.b2bsaleon.beanclass;

/**
 * Created by PPS on 7/1/2017.
 */

public class OrdersListBean {

    private String mOrders_enguiryId;
    private String mOrders_date;
    private String mOrdersStatus;

    public String getmOrders_enguiryId() {
        return mOrders_enguiryId;
    }

    public void setmOrders_enguiryId(String mOrders_enguiryId) {
        this.mOrders_enguiryId = mOrders_enguiryId;
    }

    public String getmOrders_date() {
        return mOrders_date;
    }

    public void setmOrders_date(String mOrders_date) {
        this.mOrders_date = mOrders_date;
    }

    public String getmOrdersStatus() {
        return mOrdersStatus;
    }

    public void setmOrdersStatus(String mOrdersStatus) {
        this.mOrdersStatus = mOrdersStatus;
    }

    public String getmOrders_ItemsCount() {
        return mOrders_ItemsCount;
    }

    public void setmOrders_ItemsCount(String mOrders_ItemsCount) {
        this.mOrders_ItemsCount = mOrders_ItemsCount;
    }

    public String getmOrders_TotalValue() {
        return mOrders_TotalValue;
    }

    public void setmOrders_TotalValue(String mOrders_TotalValue) {
        this.mOrders_TotalValue = mOrders_TotalValue;
    }

    private String mOrders_ItemsCount;
    private String mOrders_TotalValue;

}
