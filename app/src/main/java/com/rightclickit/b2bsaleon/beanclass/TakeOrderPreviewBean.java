package com.rightclickit.b2bsaleon.beanclass;

/**
 * Created by PPS on 6/20/2017.
 */

public class TakeOrderPreviewBean {
    private String pName;
    private String pQuantity;
    private String pPrice;
    private String mProductTaxGST;
    private String mProductTaxVAT;
    private String mProductFromDate;
    private String mProductToDate;

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpQuantity() {
        return pQuantity;
    }

    public void setpQuantity(String pQuantity) {
        this.pQuantity = pQuantity;
    }

    public String getpPrice() {
        return pPrice;
    }

    public void setpPrice(String pPrice) {
        this.pPrice = pPrice;
    }

    public String getpTax() {
        return pTax;
    }

    public void setpTax(String pTax) {
        this.pTax = pTax;
    }

    public String getpAmount() {
        return pAmount;
    }

    public void setpAmount(String pAmount) {
        this.pAmount = pAmount;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(String taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public String getFrompreview() {
        return frompreview;
    }

    public void setFrompreview(String frompreview) {
        this.frompreview = frompreview;
    }

    public String getTopreview() {
        return topreview;
    }

    public void setTopreview(String topreview) {
        this.topreview = topreview;
    }

    private String pTax;
    private String pAmount;
    private String taxName;
    private String taxPercentage;
    private String frompreview;
    private String topreview;

    public String getmProductTaxGST() {
        return mProductTaxGST;
    }

    public void setmProductTaxGST(String mProductTaxGST) {
        this.mProductTaxGST = mProductTaxGST;
    }

    public String getmProductTaxVAT() {
        return mProductTaxVAT;
    }

    public void setmProductTaxVAT(String mProductTaxVAT) {
        this.mProductTaxVAT = mProductTaxVAT;
    }

    public String getmProductToDate() {
        return mProductToDate;
    }

    public void setmProductToDate(String mProductToDate) {
        this.mProductToDate = mProductToDate;
    }

    public String getmProductFromDate() {
        return mProductFromDate;
    }

    public void setmProductFromDate(String mProductFromDate) {
        this.mProductFromDate = mProductFromDate;
    }

}

