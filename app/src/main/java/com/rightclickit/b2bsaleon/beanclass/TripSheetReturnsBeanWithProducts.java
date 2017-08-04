package com.rightclickit.b2bsaleon.beanclass;

import org.json.JSONArray;

import java.io.Serializable;

/**
 * Created by venkat on 8/4/17.
 */

public class TripSheetReturnsBeanWithProducts extends TripSheetReturnsBean implements Serializable {
    private JSONArray productIdsArray;
    private JSONArray productCodesArray;
    private JSONArray quantityArray;
    private JSONArray returnTypeArray;

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

    public JSONArray getQuantityArray() {
        return quantityArray;
    }

    public void setQuantityArray(JSONArray quantityArray) {
        this.quantityArray = quantityArray;
    }

    public JSONArray getReturnTypeArray() {
        return returnTypeArray;
    }

    public void setReturnTypeArray(JSONArray returnTypeArray) {
        this.returnTypeArray = returnTypeArray;
    }
}
