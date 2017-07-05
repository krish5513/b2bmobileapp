package com.rightclickit.b2bsaleon.beanclass;

import java.io.Serializable;

/**
 * Created by venkat on 6/27/17.
 */

public class TDCCustomer implements Serializable {
    private int id;
    private int customerType; // 0 for Consumer & 1 for Retailer
    private String name;
    private String mobileNo;
    private String businessName;
    private String address;
    private String latLong;
    private String shopImage;
    private int isActive;
    private int isUploaded;
    private int isShopImageUploaded;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerType() {
        return customerType;
    }

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(int isUploaded) {
        this.isUploaded = isUploaded;
    }

    public int getIsShopImageUploaded() {
        return isShopImageUploaded;
    }

    public void setIsShopImageUploaded(int isShopImageUploaded) {
        this.isShopImageUploaded = isShopImageUploaded;
    }
}
