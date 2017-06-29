package com.rightclickit.b2bsaleon.beanclass;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by venkat on 6/29/17.
 */

public class TDCSaleOrder implements Serializable {
    private String selectedCustomerName;
    private int selectedCustomerId;
    private ArrayList<ProductsBean> productsList;
    private double orderTotalAmount;
    private double orderTotalTaxAmount;
    private double orderSubTotal;
    private int isUploaded;

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

    public ArrayList<ProductsBean> getProductsList() {
        return productsList;
    }

    public void setProductsList(ArrayList<ProductsBean> productsList) {
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
