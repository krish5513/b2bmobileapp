package com.rightclickit.b2bsaleon.interfaces;

import com.rightclickit.b2bsaleon.beanclass.DeliverysBean;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;

import java.util.Map;

/**
 * Created by Sekhar Kuppa
 */

public interface AgentStockListener {
    void updateSelectedProductsList(Map<String, String> selectedProductsList);

    void updateDeliveryProductsList(Map<String, ProductsBean> deliveryProductsList);

}
