package com.rightclickit.b2bsaleon.beanclass;

public class DashboardPendingIndentBean {

    private String mCategery;
    private String mLiters;

    public String getmCategery() {
        return mCategery;
    }

    public void setmCategery(String mCategery) {
        this.mCategery = mCategery;
    }

    public String getmLiters() {
        return mLiters;
    }

    public void setmLiters(String mLiters) {
        this.mLiters = mLiters;
    }

    public String getmPendingCount() {
        return mPendingCount;
    }

    public void setmPendingCount(String mPendingCount) {
        this.mPendingCount = mPendingCount;
    }

    public String getmApprovedCount() {
        return mApprovedCount;
    }

    public void setmApprovedCount(String mApprovedCount) {
        this.mApprovedCount = mApprovedCount;
    }

    private String mPendingCount;
    private String mApprovedCount;
    private String mID;

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public String getMselectedDate() {
        return mselectedDate;
    }

    public void setMselectedDate(String mselectedDate) {
        this.mselectedDate = mselectedDate;
    }

    private String mselectedDate;
}
