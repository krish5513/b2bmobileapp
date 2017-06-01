package com.rightclickit.b2bsaleon.beanclass;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by murali on 11/2/17.
 */

public class ProductsBean {
    @Expose private String productId;
    @Expose private String productCode;
    @Expose private String  productTitle;
    @Expose private String  productReturnUnit;
    @Expose private String productMOQUnit;
    @Expose private String productAgentunit;

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

    public String getProductReturnUnit() {
        return productReturnUnit;
    }

    public void setProductReturnUnit(String productReturnUnit) {
        this.productReturnUnit = productReturnUnit;
    }

    public String getProductMOQUnit() {
        return productMOQUnit;
    }

    public void setProductMOQUnit(String productMOQUnit) {
        this.productMOQUnit = productMOQUnit;
    }

    public String getProductAgentunit() {
        return productAgentunit;
    }

    public void setProductAgentunit(String productAgentunit) {
        this.productAgentunit = productAgentunit;
    }

    public String getProductRetailerUnit() {
        return productRetailerUnit;
    }

    public void setProductRetailerUnit(String productRetailerUnit) {
        this.productRetailerUnit = productRetailerUnit;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductConsumerUnit() {
        return productConsumerUnit;
    }

    public void setProductConsumerUnit(String productConsumerUnit) {
        this.productConsumerUnit = productConsumerUnit;
    }

    @Expose private String productRetailerUnit;
    @Expose private String   productImage;
    @Expose private String  productConsumerUnit;


}
