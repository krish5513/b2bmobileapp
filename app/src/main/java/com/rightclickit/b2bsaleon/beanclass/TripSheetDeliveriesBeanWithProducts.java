package com.rightclickit.b2bsaleon.beanclass;

import org.json.JSONArray;

import java.io.Serializable;

/**
 * Created by venkat on 8/4/17.
 */

public class TripSheetDeliveriesBeanWithProducts extends TripSheetDeliveriesBean implements Serializable {
    private JSONArray productIdsArray;
    private JSONArray productCodesArray;
    private JSONArray taxPercentArray;
    private JSONArray unitPriceArray;
    private JSONArray quantityArray;
    private JSONArray amountArray;
    private JSONArray taxAmountArray;

    public JSONArray getProductIdsArray() {
        return productIdsArray;
    }

    public void setProductIdsArray(JSONArray productIdsArray) {
        this.productIdsArray = productIdsArray;
    }

    public JSONArray getProductCodesArray() {
        return productCodesArray;
    }

    public void setProductCodesArray(JSONArray productCodesArray) {
        this.productCodesArray = productCodesArray;
    }

    public JSONArray getTaxPercentArray() {
        return taxPercentArray;
    }

    public void setTaxPercentArray(JSONArray taxPercentArray) {
        this.taxPercentArray = taxPercentArray;
    }

    public JSONArray getUnitPriceArray() {
        return unitPriceArray;
    }

    public void setUnitPriceArray(JSONArray unitPriceArray) {
        this.unitPriceArray = unitPriceArray;
    }

    public JSONArray getQuantityArray() {
        return quantityArray;
    }

    public void setQuantityArray(JSONArray quantityArray) {
        this.quantityArray = quantityArray;
    }

    public JSONArray getAmountArray() {
        return amountArray;
    }

    public void setAmountArray(JSONArray amountArray) {
        this.amountArray = amountArray;
    }

    public JSONArray getTaxAmountArray() {
        return taxAmountArray;
    }

    public void setTaxAmountArray(JSONArray taxAmountArray) {
        this.taxAmountArray = taxAmountArray;
    }
}
