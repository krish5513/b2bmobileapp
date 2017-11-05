package com.rightclickit.b2bsaleon.beanclass;

import java.io.Serializable;

/**
 * Created by venkat on 8/11/17.
 */

public class SaleOrderDeliveredProducts implements Serializable {
    private int deliveryNo;
    private String id;
    private String name;
    private String code;
    private String quantity;
    private String unitRate;
    private String productTax;
    private String productAmount;
    private String totalTax;
    private String totalAmount;
    private String subTotal;
    private String createdTime;
    private String str_hssn;
    private String productReturnable;

    public String getStr_hssn() {
        return str_hssn;
    }

    public void setStr_hssn(String str_hssn) {
        this.str_hssn = str_hssn;
    }

    public String getStr_cgst() {
        return str_cgst;
    }

    public void setStr_cgst(String str_cgst) {
        this.str_cgst = str_cgst;
    }

    public String getStr_sgst() {
        return str_sgst;
    }

    public void setStr_sgst(String str_sgst) {
        this.str_sgst = str_sgst;
    }

    private String str_cgst;
    private String str_sgst;


    public int getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(int deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnitRate() {
        return unitRate;
    }

    public void setUnitRate(String unitRate) {
        this.unitRate = unitRate;
    }

    public String getProductTax() {
        return productTax;
    }

    public void setProductTax(String productTax) {
        this.productTax = productTax;
    }

    public String getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(String productAmount) {
        this.productAmount = productAmount;
    }

    public String getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(String totalTax) {
        this.totalTax = totalTax;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "SaleOrderDeliveredProducts{" +
                "deliveryNo=" + deliveryNo +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", quantity='" + quantity + '\'' +
                ", unitRate='" + unitRate + '\'' +
                ", productTax='" + productTax + '\'' +
                ", productAmount='" + productAmount + '\'' +
                ", totalTax='" + totalTax + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", subTotal='" + subTotal + '\'' +
                ", createdTime='" + createdTime + '\'' +
                '}';
    }

    public String getProductReturnable() {
        return productReturnable;
    }

    public void setProductReturnable(String productReturnable) {
        this.productReturnable = productReturnable;
    }
}
