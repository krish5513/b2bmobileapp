package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.TripsheetDeliveryPreview;
import com.rightclickit.b2bsaleon.beanclass.DeliverysBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.TripSheetDeliveriesListener;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by PPS on 7/17/2017.
 */

public class TripSheetDeleveriesPreviewAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context ctxt;
    private Activity activity;
    private TripSheetDeliveriesListener listener;
    private ArrayList<DeliverysBean> allDeliveryProductsList, filteredDeliveryProductsList;
    private Map<String, DeliverysBean> selectedDeliveryProductsHashMap; // Hash Map Key = Product Id
    private Map<String, String> previouslyDeliveredProductsHashMap;
    private Map<String, String> productOrderQuantitiesHashMap;
    private final String zero_cost = "0.000";
    private boolean isDeliveryInEditingMode = false;
    private DBHelper mDBHelper;

    public TripSheetDeleveriesPreviewAdapter(Context activityContext,  TripSheetDeliveriesListener deliveriesListener, TripsheetDeliveryPreview deliveryActivity, ArrayList<DeliverysBean> mdeliveriesBeanList, Map<String, String> previouslyDeliveredProductsHashMap, Map<String, String> productOrderQuantitiesHashMap) {
        this.ctxt = ctxt;
        this.activity = deliveryActivity;
        this.mInflater = LayoutInflater.from(activity);
        this.listener = deliveriesListener;
        this.allDeliveryProductsList = mdeliveriesBeanList;
        this.filteredDeliveryProductsList = new ArrayList<>();
        this.filteredDeliveryProductsList.addAll(allDeliveryProductsList);
        this.selectedDeliveryProductsHashMap = new HashMap<>();
        this.previouslyDeliveredProductsHashMap = previouslyDeliveredProductsHashMap;
        this.productOrderQuantitiesHashMap = productOrderQuantitiesHashMap;
        this.mDBHelper = new DBHelper(activity);

        // in order to update total amount's at the time of initial loading.



       if (!previouslyDeliveredProductsHashMap.isEmpty()) {
            isDeliveryInEditingMode = true;
        }

        // in order to update total amount's at the time of initial loading.
        for (DeliverysBean deliverysBean : filteredDeliveryProductsList) {
            final double productRatePerUnit = Double.parseDouble(deliverysBean.getProductAgentPrice().replace(",", ""));
            float productTax = 0.0f;

            if (isDeliveryInEditingMode && previouslyDeliveredProductsHashMap.containsKey(deliverysBean.getProductId())) {
                deliverysBean.setProductOrderedQuantity(Double.parseDouble(previouslyDeliveredProductsHashMap.get(deliverysBean.getProductId())));
            } else {
                if (productOrderQuantitiesHashMap.containsKey(deliverysBean.getProductCode()))
                    deliverysBean.setProductOrderedQuantity(Double.parseDouble(productOrderQuantitiesHashMap.get(deliverysBean.getProductCode())));
                else
                    deliverysBean.setProductOrderedQuantity(0);
            }

            if (deliverysBean.getProductgst() != null)
                productTax = Float.parseFloat(deliverysBean.getProductgst());
            else if (deliverysBean.getProductvat() != null)
                productTax = Float.parseFloat(deliverysBean.getProductvat());

            double orderQuantity = deliverysBean.getProductOrderedQuantity();
            double taxAmountPerUnit = ((productRatePerUnit) * productTax) / 100;
            double taxAmount = taxAmountPerUnit * orderQuantity;
            double amount = productRatePerUnit * orderQuantity;

            deliverysBean.setSelectedQuantity(orderQuantity);
            deliverysBean.setProductRatePerUnit(productRatePerUnit);
            deliverysBean.setProductTaxPerUnit(productTax);
            deliverysBean.setProductAmount(amount);
            deliverysBean.setTaxAmount(taxAmount);


            selectedDeliveryProductsHashMap.put(deliverysBean.getProductId(), deliverysBean);

            if (listener != null)
                listener.updateDeliveryProductsList(selectedDeliveryProductsHashMap);
        }

    }

        public class TripSheetDeliveriesViewHolder {
        TextView order_preview_product_name, order_preview_quantity, order_preview_tax, order_preview_mrp, order_preview_amount,cgst,sgst;
    }

    @Override
    public int getCount() {
        return allDeliveryProductsList.size();
    }

    @Override
    public DeliverysBean getItem(int position) {
        return allDeliveryProductsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final TripSheetDeleveriesPreviewAdapter.TripSheetDeliveriesViewHolder tripSheetDeliveriesViewHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.tdc_sales_preview_adapter, null);

            tripSheetDeliveriesViewHolder = new TripSheetDeleveriesPreviewAdapter.TripSheetDeliveriesViewHolder();
            tripSheetDeliveriesViewHolder.order_preview_product_name = (TextView) view.findViewById(R.id.order_preview_product_name);
            tripSheetDeliveriesViewHolder.order_preview_quantity = (TextView) view.findViewById(R.id.order_preview_quantity);
            tripSheetDeliveriesViewHolder.order_preview_mrp = (TextView) view.findViewById(R.id.order_preview_mrp);
            tripSheetDeliveriesViewHolder.order_preview_amount = (TextView) view.findViewById(R.id.order_preview_amount);
            tripSheetDeliveriesViewHolder.order_preview_tax = (TextView) view.findViewById(R.id.order_preview_tax);
            tripSheetDeliveriesViewHolder.cgst = (TextView) view.findViewById(R.id.cgst);
            tripSheetDeliveriesViewHolder.sgst = (TextView) view.findViewById(R.id.sgst);
            view.setTag(tripSheetDeliveriesViewHolder);
        } else {
            tripSheetDeliveriesViewHolder = (TripSheetDeleveriesPreviewAdapter.TripSheetDeliveriesViewHolder) view.getTag();
        }

        final TripSheetDeleveriesPreviewAdapter.TripSheetDeliveriesViewHolder currentTripSheetDeliveriesViewHolder = tripSheetDeliveriesViewHolder;

        final DeliverysBean currentDeliveryBean = getItem(position);



       tripSheetDeliveriesViewHolder.order_preview_product_name.setText(String.format("%s", currentDeliveryBean.getProductTitle()));
     //   tripSheetDeliveriesViewHolder.quantity_stock.setText(String.format("%.3f + %.3f", currentDeliveryBean.getProductStock(), currentDeliveryBean.getProductExtraQuantity()));
        tripSheetDeliveriesViewHolder.order_preview_mrp.setText(Utility.getFormattedCurrency(Double.parseDouble(currentDeliveryBean.getProductAgentPrice())));
        tripSheetDeliveriesViewHolder.order_preview_tax.setText(Utility.getFormattedCurrency(currentDeliveryBean.getTaxAmount()));
       tripSheetDeliveriesViewHolder.order_preview_amount.setText(Utility.getFormattedCurrency(currentDeliveryBean.getProductAmount()));
       tripSheetDeliveriesViewHolder.order_preview_quantity.setText(String.format("%.3f", currentDeliveryBean.getProductOrderedQuantity()));

    //    updateSelectedProductsList(currentDeliveryBean);

        if (currentDeliveryBean.getProductgst() != null) {
            tripSheetDeliveriesViewHolder.cgst.setText(currentDeliveryBean.getProductgst() + "%");
        } else if (mDBHelper.getGSTByProductId(currentDeliveryBean.getProductId()) > 0) {
            String gst = String.valueOf(mDBHelper.getGSTByProductId(currentDeliveryBean.getProductId()));
            tripSheetDeliveriesViewHolder.cgst.setText(gst + "%");
        } else {
            tripSheetDeliveriesViewHolder.cgst.setText("0.00%");
        }
        if (currentDeliveryBean.getProductvat() != null) {
            tripSheetDeliveriesViewHolder.sgst.setText(currentDeliveryBean.getProductvat() + "%");
        } else if (mDBHelper.getVATByProductId(currentDeliveryBean.getProductId()) > 0) {
            String vat = String.valueOf(mDBHelper.getVATByProductId(currentDeliveryBean.getProductId()));
            tripSheetDeliveriesViewHolder.sgst.setText(vat + "%");
        } else {
            tripSheetDeliveriesViewHolder.sgst.setText("0.00%");
        }

        return view;
    }



}
