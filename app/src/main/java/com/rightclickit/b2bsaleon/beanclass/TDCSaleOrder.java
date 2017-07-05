package com.rightclickit.b2bsaleon.beanclass;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by venkat on 6/29/17.
 */

public class TDCSaleOrder implements Serializable {
    private long orderId;
    private int noOfItems;
    private String selectedCustomerName;
    private int selectedCustomerId;
    private Map<String, ProductsBean> productsList;
    private double orderTotalAmount;
    private double orderTotalTaxAmount;
    private double orderSubTotal;
    private int isUploaded;
    private String createdOn;
    private String createdBy;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getNoOfItems() {
        return noOfItems;
    }

    public void setNoOfItems(int noOfItems) {
        this.noOfItems = noOfItems;
    }

    public String getSelectedCustomerName() {
        return selectedCustomerName;
    }

    public void setSelectedCustomerName(String selectedCustomerName) {
        this.selectedCustomerName = selectedCustomerName;
    }

    public int getSelectedCustomerId() {
        return selectedCustomerId;
    }

    public void setSelectedCustomerId(int selectedCustomerId) {
        this.selectedCustomerId = selectedCustomerId;
    }

    public Map<String, ProductsBean> getProductsList() {
        return productsList;
    }

    public void setProductsList(Map<String, ProductsBean> productsList) {
        this.productsList = productsList;
    }

    public double getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public void setOrderTotalAmount(double orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }

    public double getOrderTotalTaxAmount() {
        return orderTotalTaxAmount;
    }

    public void setOrderTotalTaxAmount(double orderTotalTaxAmount) {
        this.orderTotalTaxAmount = orderTotalTaxAmount;
    }

    public double getOrderSubTotal() {
        return orderSubTotal;
    }

    public void setOrderSubTotal(double orderSubTotal) {
        this.orderSubTotal = orderSubTotal;
    }

    public int getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(int isUploaded) {
        this.isUploaded = isUploaded;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "TDCSaleOrder{" +
                "selectedCustomerName='" + selectedCustomerName + '\'' +
                ", selectedCustomerId=" + selectedCustomerId +
                ", productsList=" + productsList +
                ", orderTotalAmount=" + orderTotalAmount +
                ", orderTotalTaxAmount=" + orderTotalTaxAmount +
                ", orderSubTotal=" + orderSubTotal +
                ", isUploaded=" + isUploaded +
                '}';
    }
}
