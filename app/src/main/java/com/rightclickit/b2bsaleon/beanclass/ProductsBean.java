package com.rightclickit.b2bsaleon.beanclass;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by murali on 11/2/17.
 */

public class ProductsBean {
    private String productId;
    private String productCode;
    private String productTitle;
    private String productDescription;
    private String productImageUrl;
    private String productReturnable;
    private String productMOQ;
    private String productAgentPrice;
    private String productConsumerPrice;
    private String productRetailerPrice;
    private String productgst;
    private String productvat;

    public String getProductgst() {
        return productgst;
    }

    public void setProductgst(String productgst) {
        this.productgst = productgst;
    }

    public String getProductvat() {
        return productvat;
    }

    public void setProductvat(String productvat) {
        this.productvat = productvat;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }


    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductReturnable() {
        return productReturnable;
    }

    public void setProductReturnable(String productReturnable) {
        this.productReturnable = productReturnable;
    }

    public String getProductMOQ() {
        return productMOQ;
    }

    public void setProductMOQ(String productMOQ) {
        this.productMOQ = productMOQ;
    }

    public String getProductAgentPrice() {
        return productAgentPrice;
    }

    public void setProductAgentPrice(String productAgentPrice) {
        this.productAgentPrice = productAgentPrice;
    }

    public String getProductConsumerPrice() {
        return productConsumerPrice;
    }

    public void setProductConsumerPrice(String productConsumerPrice) {
        this.productConsumerPrice = productConsumerPrice;
    }

    public String getProductRetailerPrice() {
        return productRetailerPrice;
    }

    public void setProductRetailerPrice(String productRetailerPrice) {
        this.productRetailerPrice = productRetailerPrice;
    }

    @Override
    public String toString() {
        return "ProductsBean{" +
                "productId='" + productId + '\'' +
                ", productCode='" + productCode + '\'' +
                ", productTitle='" + productTitle + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productImageUrl='" + productImageUrl + '\'' +
                ", productReturnable='" + productReturnable + '\'' +
                ", productMOQ='" + productMOQ + '\'' +
                ", productAgentPrice='" + productAgentPrice + '\'' +
                ", productConsumerPrice='" + productConsumerPrice + '\'' +
                ", productRetailerPrice='" + productRetailerPrice + '\'' +
                ", productgst='" + productgst + '\'' +
                ", productvat='" + productvat + '\'' +
                '}';
    }
}
