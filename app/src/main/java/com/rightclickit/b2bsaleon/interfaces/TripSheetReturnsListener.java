package com.rightclickit.b2bsaleon.interfaces;

import com.rightclickit.b2bsaleon.beanclass.DeliverysBean;

import java.util.Map;

/**
 * Created by venkat on 8/2/17.
 */

public interface TripSheetReturnsListener {
    void updateSelectedProductsList(Map<String, DeliverysBean> productsList);
}
