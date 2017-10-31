package com.rightclickit.b2bsaleon.interfaces;

import com.rightclickit.b2bsaleon.beanclass.ProductsBean;

import java.util.Map;

/**
 * Created by Sekhar Kuppa
 */

public interface RouteStockListener {
    void updateSelectedCBList(Map<String, String> selectedCBList);

    void updateSelectedLeakageQuantityList(Map<String, String> selectedLeakList);

    void updateSelectedOthersQuantityList(Map<String, String> selectedOthersList);

    void updateSelectedPNamesQuantityList(Map<String, String> selectedPNamesList);

    void updateSelectedPDelsQuantityList(Map<String, String> selectedPDelsList);

    void updateSelectedPRetunsQuantityList(Map<String, String> selectedPReturnsList);

}
