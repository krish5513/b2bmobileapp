package com.rightclickit.b2bsaleon.interfaces;

import com.rightclickit.b2bsaleon.beanclass.ProductsBean;

import java.util.Map;

/**
 * Created by Sekhar Kuppa
 */

public interface AgentTakeOrderListener {
    void updateSelectedTakeOrderQuantity(Map<String, String> productsList, Map<String, String> fromDatesList, Map<String, String> toDatesList);
}
