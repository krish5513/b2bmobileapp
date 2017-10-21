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
    // Added by Sekhar for close trip functionality
    private String mDeliveryQuantity;
    private String mReturnQuantity;
    private String mCBQuantity;
    private String mLeakQuantity;
    private String mOtherQuantity;

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
                ", mTripsheetStockProductCode='" + mTripsheetStockProductCode + '\'' +
                ", mTripsheetStockProductName='" + mTripsheetStockProductName + '\'' +
                ", mTripsheetStockProductOrderQuantity='" + mTripsheetStockProductOrderQuantity + '\'' +
                ", inStockQuantity='" + inStockQuantity + '\'' +
                ", extraQuantity='" + extraQuantity + '\'' +
                ", mDeliveryQuantity='" + mDeliveryQuantity + '\'' +
                ", mReturnQuantity='" + mReturnQuantity + '\'' +
                ", mCBQuantity='" + mCBQuantity + '\'' +
                ", mLeakQuantity='" + mLeakQuantity + '\'' +
                ", mOtherQuantity='" + mOtherQuantity + '\'' +
                '}';
    }

    public String getmDeliveryQuantity() {
        return mDeliveryQuantity;
    }

    public void setmDeliveryQuantity(String mDeliveryQuantity) {
        this.mDeliveryQuantity = mDeliveryQuantity;
    }

    public String getmReturnQuantity() {
        return mReturnQuantity;
    }

    public void setmReturnQuantity(String mReturnQuantity) {
        this.mReturnQuantity = mReturnQuantity;
    }

    public String getmCBQuantity() {
        return mCBQuantity;
    }

    public void setmCBQuantity(String mCBQuantity) {
        this.mCBQuantity = mCBQuantity;
    }

    public String getmLeakQuantity() {
        return mLeakQuantity;
    }

    public void setmLeakQuantity(String mLeakQuantity) {
        this.mLeakQuantity = mLeakQuantity;
    }

    public String getmOtherQuantity() {
        return mOtherQuantity;
    }

    public void setmOtherQuantity(String mOtherQuantity) {
        this.mOtherQuantity = mOtherQuantity;
    }
}
