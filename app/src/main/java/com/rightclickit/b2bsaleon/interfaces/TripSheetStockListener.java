package com.rightclickit.b2bsaleon.interfaces;

import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;

import java.util.Map;

/**
 * Created by venkat on 7/26/17.
 */

public interface TripSheetStockListener {
    void updateProductsDispatchList(Map<String, TripsheetsStockList> productsList);

    void updateProductsVerifyList(Map<String, TripsheetsStockList> productsList);
}
