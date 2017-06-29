package com.rightclickit.b2bsaleon.interfaces;

import com.rightclickit.b2bsaleon.beanclass.ProductsBean;

import java.util.ArrayList;

/**
 * Created by venkat on 6/28/17.
 */

public interface TDCSalesListener {
    void updateSelectedProductsListAndSubTotal(ArrayList<ProductsBean> selectedProductsList);
}
