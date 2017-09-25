package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.AgentStockDeliveryActivity;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.interfaces.AgentStockListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sekhar Kuppa.
 */

public class AgentStockDeliveryAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    private Activity activity;
    private Context ctxt;
    private ArrayList<ProductsBean> allProductsListSuper, filteredProductsList;
    private Map<String, String> selectedProductsStockListHashMap;
    private Map<String, String> selectedProductsStockListHashMapNameCode;
    private Map<String, String> selectedProductsStockListHashMapIdUOM;
    private AgentStockListener listener;

    public AgentStockDeliveryAdapter(AgentStockDeliveryActivity agentStockDeliveryActivity, AgentStockDeliveryActivity stockDeliveryActivity,
                                     AgentStockListener deliverListener, ArrayList<ProductsBean> allProductsList) {
        this.ctxt = ctxt;
        this.activity = agentStockDeliveryActivity;
        this.mInflater = LayoutInflater.from(activity);
        this.allProductsListSuper = allProductsList;
        this.filteredProductsList = new ArrayList<>();
        this.filteredProductsList.addAll(allProductsListSuper);
        this.selectedProductsStockListHashMap = new HashMap<>();
        this.selectedProductsStockListHashMapNameCode = new HashMap<>();
        this.selectedProductsStockListHashMapIdUOM = new HashMap<>();
        this.listener = deliverListener;
    }

    @Override
    public int getCount() {
        return filteredProductsList.size();
    }

    @Override
    public ProductsBean getItem(int position) {
        ProductsBean productBean = filteredProductsList.get(position);

        return productBean;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        AgentStockSalesViewHolder agentStockSalesViewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.agent_stock_delivery_adapter, null);

            agentStockSalesViewHolder = new AgentStockSalesViewHolder();
            agentStockSalesViewHolder.arrow_icon = (ImageView) convertView.findViewById(R.id.arrow_icon);
            agentStockSalesViewHolder.product_name = (TextView) convertView.findViewById(R.id.product_name);
            agentStockSalesViewHolder.product_code = (TextView) convertView.findViewById(R.id.code);
            agentStockSalesViewHolder.product_uom = (TextView) convertView.findViewById(R.id.uom);
            agentStockSalesViewHolder.product_quantity_decrement = (ImageView) convertView.findViewById(R.id.product_quantity_dec);
            agentStockSalesViewHolder.product_quantity = (EditText) convertView.findViewById(R.id.product_quantity);
            agentStockSalesViewHolder.product_quantity_increment = (ImageView) convertView.findViewById(R.id.product_quantity_inc);

            convertView.setTag(agentStockSalesViewHolder);
        } else {
            agentStockSalesViewHolder = (AgentStockSalesViewHolder) convertView.getTag();
        }

        try {
            final AgentStockSalesViewHolder currentAgentStockSalesViewHolder = agentStockSalesViewHolder;
            final ProductsBean currentProductsBean = getItem(position);

            agentStockSalesViewHolder.product_name.setText(currentProductsBean.getProductTitle());
            agentStockSalesViewHolder.product_code.setText(currentProductsBean.getProductCode());
            agentStockSalesViewHolder.product_uom.setText(currentProductsBean.getProductUOM());

            if (selectedProductsStockListHashMap.get(currentProductsBean.getProductId()) != null) {
                Double selectedQunatity = Double.parseDouble(selectedProductsStockListHashMap.get(currentProductsBean.getProductId()));
                currentAgentStockSalesViewHolder.product_quantity.setText(String.format("%.3f", selectedQunatity));
            } else {
                currentAgentStockSalesViewHolder.product_quantity.setText(String.format("%.3f", 0.0));
            }

            agentStockSalesViewHolder.product_quantity_decrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Double presentQuantity = Double.parseDouble(currentAgentStockSalesViewHolder.product_quantity.getText().toString());
                    if (presentQuantity > 0) {
                        presentQuantity--;
                        currentAgentStockSalesViewHolder.product_quantity.setText(String.format("%.3f", presentQuantity));
                        selectedProductsStockListHashMap.put(currentProductsBean.getProductId(), String.valueOf(presentQuantity));
                        // selectedProductsStockListHashMapNameCode.put(currentProductsBean.getProductTitle(), currentProductsBean.getProductCode());
                        //selectedProductsStockListHashMapIdUOM.put(currentProductsBean.getProductId(), currentProductsBean.getProductUOM());
                        if (listener != null)
                            listener.updateSelectedProductsList(selectedProductsStockListHashMap, selectedProductsStockListHashMapNameCode
                                    , selectedProductsStockListHashMapIdUOM);
                    } else {
                        removeFromSelectedList(currentProductsBean);
                    }
                }
            });

            agentStockSalesViewHolder.product_quantity_increment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Double presentQuantity = Double.parseDouble(currentAgentStockSalesViewHolder.product_quantity.getText().toString());
                    presentQuantity++;
                    currentAgentStockSalesViewHolder.product_quantity.setText(String.format("%.3f", presentQuantity));
                    if (presentQuantity > 0) {
                        selectedProductsStockListHashMap.put(currentProductsBean.getProductId(), String.valueOf(presentQuantity));
//                        selectedProductsStockListHashMapNameCode.put(currentProductsBean.getProductTitle(), currentProductsBean.getProductCode());
                        //                      selectedProductsStockListHashMapIdUOM.put(currentProductsBean.getProductId(), currentProductsBean.getProductUOM());
                        if (listener != null)
                            listener.updateSelectedProductsList(selectedProductsStockListHashMap, selectedProductsStockListHashMapNameCode
                                    , selectedProductsStockListHashMapIdUOM);
                    }
                }
            });

            agentStockSalesViewHolder.product_quantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    try {
                        if (!hasFocus) {
                            EditText quantityEditText = (EditText) view;
                            Double enteredQuantity = Double.parseDouble(quantityEditText.getText().toString());
                            if (enteredQuantity > 0) {
                                selectedProductsStockListHashMap.put(currentProductsBean.getProductId(), String.valueOf(enteredQuantity));
                                // selectedProductsStockListHashMapNameCode.put(currentProductsBean.getProductTitle(), currentProductsBean.getProductCode());
                                //selectedProductsStockListHashMapIdUOM.put(currentProductsBean.getProductId(), currentProductsBean.getProductUOM());
                                if (listener != null)
                                    listener.updateSelectedProductsList(selectedProductsStockListHashMap, selectedProductsStockListHashMapNameCode
                                            , selectedProductsStockListHashMapIdUOM);
                            } else {
                                removeFromSelectedList(currentProductsBean);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private void removeFromSelectedList(ProductsBean productsBean) {
        try {
            if (selectedProductsStockListHashMap.containsKey(productsBean.getProductId()))
                selectedProductsStockListHashMap.remove(productsBean.getProductId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class AgentStockSalesViewHolder {
        ImageView arrow_icon, product_quantity_decrement, product_quantity_increment;
        TextView product_name, product_code, product_uom;
        EditText product_quantity;
    }
}
