package com.rightclickit.b2bsaleon.beanclass;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by murali on 11/2/17.
 */

public class ProductsBean {
    @Expose private String code;
    @Expose private String materialTitle;
    @Expose private String materialReturnable;
    @Expose private String materialUnit;
    @Expose private String materialMOQ;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMaterialTitle() {
        return materialTitle;
    }

    public void setMaterialTitle(String materialTitle) {
        this.materialTitle = materialTitle;
    }

    public String getMaterialReturnable() {
        return materialReturnable;
    }

    public void setMaterialReturnable(String materialReturnable) {
        this.materialReturnable = materialReturnable;
    }

    public String getMaterialUnit() {
        return materialUnit;
    }

    public void setMaterialUnit(String materialUnit) {
        this.materialUnit = materialUnit;
    }

    public String getMaterialMOQ() {
        return materialMOQ;
    }

    public void setMaterialMOQ(String materialMOQ) {
        this.materialMOQ = materialMOQ;
    }

    public String getMaterialMOQUnit() {
        return materialMOQUnit;
    }

    public void setMaterialMOQUnit(String materialMOQUnit) {
        this.materialMOQUnit = materialMOQUnit;
    }

    public String getMaterialAgent() {
        return materialAgent;
    }

    public void setMaterialAgent(String materialAgent) {
        this.materialAgent = materialAgent;
    }

    public String getMaterialAgentunit() {
        return materialAgentunit;
    }

    public void setMaterialAgentunit(String materialAgentunit) {
        this.materialAgentunit = materialAgentunit;
    }

    public String getMaterialRetailer() {
        return materialRetailer;
    }

    public void setMaterialRetailer(String materialRetailer) {
        this.materialRetailer = materialRetailer;
    }

    public String getMaterialRetailerUnit() {
        return materialRetailerUnit;
    }

    public void setMaterialRetailerUnit(String materialRetailerUnit) {
        this.materialRetailerUnit = materialRetailerUnit;
    }

    public String getMaterialImage() {
        return materialImage;
    }

    public void setMaterialImage(String materialImage) {
        this.materialImage = materialImage;
    }



    public String getMaterialConsumer() {
        return materialConsumer;
    }

    public void setMaterialConsumer(String materialConsumer) {
        this.materialConsumer = materialConsumer;
    }

    public String getMaterialConsumerUnit() {
        return materialConsumerUnit;
    }

    public void setMaterialConsumerUnit(String materialConsumerUnit) {
        this.materialConsumerUnit = materialConsumerUnit;
    }

    public ProductsBean() {
        this.code = code;
        this.materialTitle = materialTitle;
        this.materialReturnable = materialReturnable;
        this.materialUnit = materialUnit;
        this.materialMOQ = materialMOQ;
        this.materialMOQUnit = materialMOQUnit;
        this.materialAgent = materialAgent;
        this.materialAgentunit = materialAgentunit;
        this.materialRetailer = materialRetailer;
        this.materialRetailerUnit = materialRetailerUnit;
        this.materialImage = materialImage;

        this.materialConsumer = materialConsumer;
        this.materialConsumerUnit = materialConsumerUnit;
    }

    @Expose private String materialMOQUnit;
    @Expose private String materialAgent;
    @Expose private String materialAgentunit;
    @Expose private String materialRetailer;
    @Expose private String materialRetailerUnit;
    @Expose private String  materialImage;

    @Expose private String materialConsumer;
    @Expose private String materialConsumerUnit;





}
