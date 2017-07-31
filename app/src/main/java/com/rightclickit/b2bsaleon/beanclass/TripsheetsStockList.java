package com.rightclickit.b2bsaleon.beanclass;

/**
 * Created by Sekhar Kuppa
 */

public class TripsheetsStockList {

    private String mTripsheetStockTripsheetId;
    private String mTripsheetStockId;
    private String mTripsheetStockProductId;
    private String mTripsheetStockProductCode;
    private String mTripsheetStockProductName;
    private String mTripsheetStockProductOrderQuantity;
    private String mTripsheetStockDispatchBy;
    private String mTripsheetStockDispatchDate;
    private String mTripsheetStockDispatchQuantity;
    private String mTripsheetStockVerifyBy;
    private String mTripsheetStockVerifiedDate;
    private String mTripsheetStockVerifiedQuantity;
    private int isStockDispatched; // 0 means not dispatched & 1 means dispatched
    private int isStockVerified; // 0 means not verified & 1 means verified
    private String inStockQuantity;
    private String extraQuantity; // this column contains product extra quantity delivered and also we will update this value after saving of returns if any.

    public String getmTripsheetStockTripsheetId() {
        return mTripsheetStockTripsheetId;
    }

    public void setmTripsheetStockTripsheetId(String mTripsheetStockTripsheetId) {
        this.mTripsheetStockTripsheetId = mTripsheetStockTripsheetId;
    }

    public String getmTripsheetStockId() {
        return mTripsheetStockId;
    }

    public void setmTripsheetStockId(String mTripsheetStockId) {
        this.mTripsheetStockId = mTripsheetStockId;
    }

    public String getmTripsheetStockProductId() {
        return mTripsheetStockProductId;
    }

    public void setmTripsheetStockProductId(String mTripsheetStockProductId) {
        this.mTripsheetStockProductId = mTripsheetStockProductId;
    }

    public String getmTripsheetStockProductCode() {
        return mTripsheetStockProductCode;
    }

    public void setmTripsheetStockProductCode(String mTripsheetStockProductCode) {
        this.mTripsheetStockProductCode = mTripsheetStockProductCode;
    }

    public String getmTripsheetStockProductName() {
        return mTripsheetStockProductName;
    }

    public void setmTripsheetStockProductName(String mTripsheetStockProductName) {
        this.mTripsheetStockProductName = mTripsheetStockProductName;
    }

    public String getmTripsheetStockProductOrderQuantity() {
        return mTripsheetStockProductOrderQuantity;
    }

    public void setmTripsheetStockProductOrderQuantity(String mTripsheetStockProductOrderQuantity) {
        this.mTripsheetStockProductOrderQuantity = mTripsheetStockProductOrderQuantity;
    }

    public String getmTripsheetStockDispatchBy() {
        return mTripsheetStockDispatchBy;
    }

    public void setmTripsheetStockDispatchBy(String mTripsheetStockDispatchBy) {
        this.mTripsheetStockDispatchBy = mTripsheetStockDispatchBy;
    }

    public String getmTripsheetStockDispatchDate() {
        return mTripsheetStockDispatchDate;
    }

    public void setmTripsheetStockDispatchDate(String mTripsheetStockDispatchDate) {
        this.mTripsheetStockDispatchDate = mTripsheetStockDispatchDate;
    }

    public String getmTripsheetStockDispatchQuantity() {
        return mTripsheetStockDispatchQuantity;
    }

    public void setmTripsheetStockDispatchQuantity(String mTripsheetStockDispatchQuantity) {
        this.mTripsheetStockDispatchQuantity = mTripsheetStockDispatchQuantity;
    }

    public String getmTripsheetStockVerifyBy() {
        return mTripsheetStockVerifyBy;
    }

    public void setmTripsheetStockVerifyBy(String mTripsheetStockVerifyBy) {
        this.mTripsheetStockVerifyBy = mTripsheetStockVerifyBy;
    }

    public String getmTripsheetStockVerifiedDate() {
        return mTripsheetStockVerifiedDate;
    }

    public void setmTripsheetStockVerifiedDate(String mTripsheetStockVerifiedDate) {
        this.mTripsheetStockVerifiedDate = mTripsheetStockVerifiedDate;
    }

    public String getmTripsheetStockVerifiedQuantity() {
        return mTripsheetStockVerifiedQuantity;
    }

    public void setmTripsheetStockVerifiedQuantity(String mTripsheetStockVerifiedQuantity) {
        this.mTripsheetStockVerifiedQuantity = mTripsheetStockVerifiedQuantity;
    }

    public int getIsStockDispatched() {
        return isStockDispatched;
    }

    public void setIsStockDispatched(int isStockDispatched) {
        this.isStockDispatched = isStockDispatched;
    }

    public int getIsStockVerified() {
        return isStockVerified;
    }

    public void setIsStockVerified(int isStockVerified) {
        this.isStockVerified = isStockVerified;
    }

    public String getInStockQuantity() {
        return inStockQuantity;
    }

    public void setInStockQuantity(String inStockQuantity) {
        this.inStockQuantity = inStockQuantity;
    }

    public String getExtraQuantity() {
        return extraQuantity;
    }

    public void setExtraQuantity(String extraQuantity) {
        this.extraQuantity = extraQuantity;
    }

    @Override
    public String toString() {
        return "TripsheetsStockList{" +
                "mTripsheetStockTripsheetId='" + mTripsheetStockTripsheetId + '\'' +
                ", mTripsheetStockId='" + mTripsheetStockId + '\'' +
                ", mTripsheetStockProductId='" + mTripsheetStockProductId + '\'' +
                ", mTripsheetStockProductCode='" + mTripsheetStockProductCode + '\'' +
                ", mTripsheetStockProductName='" + mTripsheetStockProductName + '\'' +
                ", mTripsheetStockProductOrderQuantity='" + mTripsheetStockProductOrderQuantity + '\'' +
                ", mTripsheetStockDispatchBy='" + mTripsheetStockDispatchBy + '\'' +
                ", mTripsheetStockDispatchDate='" + mTripsheetStockDispatchDate + '\'' +
                ", mTripsheetStockDispatchQuantity='" + mTripsheetStockDispatchQuantity + '\'' +
                ", mTripsheetStockVerifyBy='" + mTripsheetStockVerifyBy + '\'' +
                ", mTripsheetStockVerifiedDate='" + mTripsheetStockVerifiedDate + '\'' +
                ", mTripsheetStockVerifiedQuantity='" + mTripsheetStockVerifiedQuantity + '\'' +
                ", inStockQuantity='" + inStockQuantity + '\'' +
                ", extraQuantity='" + extraQuantity + '\'' +
                ", isStockDispatched='" + isStockDispatched + '\'' +
                ", isStockVerified='" + isStockVerified + '\'' +
                '}';
    }
}
