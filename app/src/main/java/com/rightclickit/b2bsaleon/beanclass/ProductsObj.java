package com.rightclickit.b2bsaleon.beanclass;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by murali on 11/2/17.
 */

public class ProductsObj {
    @Expose private int id;
    @Expose private String materialName;
    @Expose private String materialLiters;
    @Expose private String materialMRPRS;
    @Expose private String materialMRP;
    @Expose private String materialSP;
    @Expose private String materialSPRS;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialLiters() {
        return materialLiters;
    }

    public void setMaterialLiters(String materialLiters) {
        this.materialLiters = materialLiters;
    }

    public String getMaterialMRPRS() {
        return materialMRPRS;
    }

    public void setMaterialMRPRS(String materialMRPRS) {
        this.materialMRPRS = materialMRPRS;
    }

    public String getMaterialMRP() {
        return materialMRP;
    }

    public void setMaterialMRP(String materialMRP) {
        this.materialMRP = materialMRP;
    }

    public String getMaterialSP() {
        return materialSP;
    }

    public void setMaterialSP(String materialSP) {
        this.materialSP = materialSP;
    }

    public String getMaterialSPRS() {
        return materialSPRS;
    }

    public void setMaterialSPRS(String materialSPRS) {
        this.materialSPRS = materialSPRS;
    }

    public String getMaterialidLiteres() {
        return materialidLiteres;
    }

    public void setMaterialidLiteres(String materialidLiteres) {
        this.materialidLiteres = materialidLiteres;
    }

    public int getMaterialImage() {
        return materialImage;
    }

    public void setMaterialImage(int materialImage) {
        this.materialImage = materialImage;
    }

    public String getMaterialReturnable() {
        return materialReturnable;
    }

    public void setMaterialReturnable(String materialReturnable) {
        this.materialReturnable = materialReturnable;
    }

    public String getMaterialMOQ() {
        return materialMOQ;
    }

    public void setMaterialMOQ(String materialMOQ) {
        this.materialMOQ = materialMOQ;
    }

    public String getMaterialStatus() {
        return materialStatus;
    }

    public void setMaterialStatus(String materialStatus) {
        this.materialStatus = materialStatus;
    }

    public ProductsObj(int id, String materialName, String materialLiters, String materialMRPRS, String materialMRP, String materialSP, String materialSPRS, String materialidLiteres, Integer materialImage, String materialReturnable, String materialMOQ, String materialStatus,int downarrowImage) {
        this.id = id;
        this.materialName = materialName;
        this.materialLiters = materialLiters;
        this.materialMRPRS = materialMRPRS;
        this.materialMRP = materialMRP;
        this.materialSP = materialSP;
        this.materialSPRS = materialSPRS;
        this.materialidLiteres = materialidLiteres;
        this.materialImage = materialImage;
        this.materialReturnable = materialReturnable;
        this.materialMOQ = materialMOQ;
        this.materialStatus = materialStatus;
        this.downarrowImage = downarrowImage;
    }

    @Expose private String materialidLiteres;
    @Expose private int  materialImage;



    public int getDownarrowImage() {
        return downarrowImage;
    }

    public void setDownarrowImage(int downarrowImage) {
        this.downarrowImage = downarrowImage;
    }

    @Expose private int  downarrowImage;
    @Expose private String materialReturnable;
    @Expose private String materialMOQ;
    @Expose private String materialStatus;


}
