package com.rightclickit.b2bsaleon.beanclass;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Venkat on 6/29/17.
 */

public class TDCSaleOrder implements Serializable {
    private long orderId;
    private int noOfItems;
    private String selectedCustomerUserId; // this is mongo db user id
    private long selectedCustomerId; // this is local sqlite db id
    private long selectedCustomerType;
    private Map<String, ProductsBean> productsList; // this is the selected products list for the current order
    private double orderTotalAmount;
    private double orderTotalTaxAmount;
    private double orderSubTotal;
    private int isUploaded;
    private String orderDate;
    private long createdOn;
    private String createdBy;
    private String selectedCustomerName;
    private String selectedCustomerCode;
    private List<TDCSalesOrderProductBean> orderProductsList; // this list is used while syncing data with server

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

    public String getSelectedCustomerUserId() {
        return selectedCustomerUserId;
    }

    public void setSelectedCustomerUserId(String selectedCustomerUserId) {
        this.selectedCustomerUserId = selectedCustomerUserId;
    }

    public long getSelectedCustomerId() {
        return selectedCustomerId;
    }

    public void setSelectedCustomerId(long selectedCustomerId) {
        this.selectedCustomerId = selectedCustomerId;
    }

    public long getSelectedCustomerType() {
        return selectedCustomerType;
    }

    public void setSelectedCustomerType(long selectedCustomerType) {
        this.selectedCustomerType = selectedCustomerType;
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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<TDCSalesOrderProductBean> getOrderProductsList() {
        return orderProductsList;
    }

    public void setOrderProductsList(List<TDCSalesOrderProductBean> orderProductsList) {
        this.orderProductsList = orderProductsList;
    }

    @Override
    public String toString() {
        return "TDCSaleOrder{" +
                "orderId=" + orderId +
                ", noOfItems=" + noOfItems +
                ", selectedCustomerUserId='" + selectedCustomerUserId + '\'' +
                ", selectedCustomerId=" + selectedCustomerId +
                ", selectedCustomerType=" + selectedCustomerType +
                ", productsList=" + productsList +
                ", orderTotalAmount=" + orderTotalAmount +
                ", orderTotalTaxAmount=" + orderTotalTaxAmount +
                ", orderSubTotal=" + orderSubTotal +
                ", isUploaded=" + isUploaded +
                ", orderDate='" + orderDate + '\'' +
                ", createdOn=" + createdOn +
                ", createdBy='" + createdBy + '\'' +
                ", orderProductsList=" + orderProductsList +
                ", selectedCustomerName=" + selectedCustomerName +
                ", selectedCustomerCode=" + selectedCustomerCode +
                '}';
    }

    public String getSelectedCustomerName() {
        return selectedCustomerName;
    }

    public void setSelectedCustomerName(String selectedCustomerName) {
        this.selectedCustomerName = selectedCustomerName;
    }

    public String getSelectedCustomerCode() {
        return selectedCustomerCode;
    }

    public void setSelectedCustomerCode(String selectedCustomerCode) {
        this.selectedCustomerCode = selectedCustomerCode;
    }
}
