package com.rightclickit.b2bsaleon.beanclass;

import java.io.Serializable;

/**
 * Created by murali on 11/2/17.
 */

public class TDCSalesOrderProductBean implements Serializable {
    private long id;
    private long orderId;
    private String productId;
    private String productName;
    private double productMRP;
    private double productQuantity;
    private double productAmount;
    private double productTax;
    private double productTaxAmount;
    private int isUploaded;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductMRP() {
        return productMRP;
    }

    public void setProductMRP(double productMRP) {
        this.productMRP = productMRP;
    }

    public double getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(double productQuantity) {
        this.productQuantity = productQuantity;
    }

    public double getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(double productAmount) {
        this.productAmount = productAmount;
    }

    public double getProductTax() {
        return productTax;
    }

    public void setProductTax(double productTax) {
        this.productTax = productTax;
    }

    public double getProductTaxAmount() {
        return productTaxAmount;
    }

    public void setProductTaxAmount(double productTaxAmount) {
        this.productTaxAmount = productTaxAmount;
    }

    public int getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(int isUploaded) {
        this.isUploaded = isUploaded;
    }

    @Override
    public String toString() {
        return "TDCSalesOrderProductBean{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", productId='" + productId + '\'' +
                ", productMRP=" + productMRP +
                ", productQuantity=" + productQuantity +
                ", productAmount=" + productAmount +
                ", productTax=" + productTax +
                ", productTaxAmount=" + productTaxAmount +
                ", isUploaded=" + isUploaded +
                '}';
    }
}
