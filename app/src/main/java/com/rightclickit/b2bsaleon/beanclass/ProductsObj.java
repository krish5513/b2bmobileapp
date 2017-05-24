package com.rightclickit.b2bsaleon.beanclass;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by murali on 11/2/17.
 */

public class ProductsObj implements Serializable{
    @Expose private int id;
    @Expose private String materialCode;
    @Expose private String materialDisc;
    @Expose private String materialUnit;
    @Expose private String materialMRP;
    @Expose private String materialSP;
    @Expose private String materialValidFrom;
    @Expose private String materialValidTo;
    @Expose private String materialImage;
    @Expose private String materialStatus;
    @Expose private String materialMOQ;
    @Expose private String materialTAX;
    @Expose private String materialTAXType;
    @Expose private String fromDate;
    @Expose private String toDate;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public ProductsObj(String materialCode, String materialDisc, String materialImage, String materialMRP, String materialSP, String materialStatus, String materialUnit, String materialValidFrom, String materialValidTo, String isReturnAble, String materialMOQ, String materialTAX, String materialTAXType) {
        this.materialCode = materialCode;
        this.materialDisc = materialDisc;
        this.materialImage = materialImage;
        this.materialMRP = materialMRP;
        this.materialSP = materialSP;
        this.materialStatus = materialStatus;
        this.materialUnit = materialUnit;
        this.materialValidFrom = materialValidFrom;
        this.materialValidTo = materialValidTo;
        this.isReturnAble = isReturnAble;
        this.materialMOQ = materialMOQ;
        this.materialTAX = materialTAX;
        this.materialTAXType = materialTAXType;
    }
    public ProductsObj(String materialCode, String materialDisc, String materialImage, String materialMRP, String materialSP, String materialStatus, String materialUnit, String materialValidFrom, String materialValidTo, String isReturnAble, String materialMOQ, String materialTAX, String materialTAXType, String fromDate, String toDate) {
        this.materialCode = materialCode;
        this.materialDisc = materialDisc;
        this.materialImage = materialImage;
        this.materialMRP = materialMRP;
        this.materialSP = materialSP;
        this.materialStatus = materialStatus;
        this.materialUnit = materialUnit;
        this.materialValidFrom = materialValidFrom;
        this.materialValidTo = materialValidTo;
        this.isReturnAble = isReturnAble;
        this.materialMOQ = materialMOQ;
        this.materialTAX = materialTAX;
        this.materialTAXType = materialTAXType;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String getMaterialDisc() {
        return materialDisc;
    }

    public void setMaterialDisc(String materialDisc) {
        this.materialDisc = materialDisc;
    }

    public String getMaterialImage() {
        return materialImage;
    }

    public void setMaterialImage(String materialImage) {
        this.materialImage = materialImage;
    }

    public String getMaterialSP() {
        return materialSP;
    }

    public void setMaterialSP(String materialSP) {
        this.materialSP = materialSP;
    }

    public String getMaterialMRP() {
        return materialMRP;
    }

    public void setMaterialMRP(String materialMRP) {
        this.materialMRP = materialMRP;
    }

    public String getMaterialStatus() {
        return materialStatus;
    }

    public void setMaterialStatus(String materialStatus) {
        this.materialStatus = materialStatus;
    }

    public String getMaterialUnit() {
        return materialUnit;
    }

    public void setMaterialUnit(String materialUnit) {
        this.materialUnit = materialUnit;
    }

    public String getMaterialValidFrom() {
        return materialValidFrom;
    }

    public void setMaterialValidFrom(String materialValidFrom) {
        this.materialValidFrom = materialValidFrom;
    }

    public String getMaterialValidTo() {
        return materialValidTo;
    }

    public void setMaterialValidTo(String materialValidTo) {
        this.materialValidTo = materialValidTo;
    }

    public String getMaterialTAX() {
        return materialTAX;
    }

    public void setMaterialTAX(String materialTAX) {
        this.materialTAX = materialTAX;
    }

    public String getMaterialTAXType() {
        return materialTAXType;
    }

    public void setMaterialTAXType(String materialTAXType) {
        this.materialTAXType = materialTAXType;
    }


    public String getMaterialMOQ() {
        return materialMOQ;
    }

    public void setMaterialMOQ(String materialMOQ) {
        this.materialMOQ = materialMOQ;
    }


    public String getIsReturnAble() {
        return isReturnAble;
    }

    public void setIsReturnAble(String isReturnAble) {
        this.isReturnAble = isReturnAble;
    }

    String isReturnAble;
    // Empty constructor
    public ProductsObj() {

    }
}
