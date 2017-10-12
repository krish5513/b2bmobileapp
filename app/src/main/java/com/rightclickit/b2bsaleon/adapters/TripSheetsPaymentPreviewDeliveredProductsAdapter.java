package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import com.rightclickit.b2bsaleon.activities.TripsheetPaymentsPreview;
import com.rightclickit.b2bsaleon.beanclass.SaleOrderDeliveredProducts;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;

/**
 * Created by Venkat on 11/08/2017.
 */

public class TripSheetsPaymentPreviewDeliveredProductsAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity activity;
    private Context ctxt;
    private ArrayList<SaleOrderDeliveredProducts> deliveredProductsList;

    public TripSheetsPaymentPreviewDeliveredProductsAdapter(Context ctxt, TripsheetPaymentsPreview activity, ArrayList<SaleOrderDeliveredProducts> productsList) {
        this.ctxt = ctxt;
        this.activity = activity;
        this.mInflater = LayoutInflater.from(activity);
        this.deliveredProductsList = productsList;
    }
   /* public TripSheetsPaymentPreviewDeliveredProductsAdapter(Context ctxt, AgentPaymentsView activity, ArrayList<SaleOrderDeliveredProducts> productsList) {
        this.ctxt = ctxt;
        this.activity = activity;
        this.mInflater = LayoutInflater.from(activity);
        this.deliveredProductsList = productsList;
    }*/

    private class TDCSalesPreviewViewHolder {
        TextView product_name, quantity, price, amount, tax;
    }

    @Override
    public int getCount() {
        return deliveredProductsList.size();
    }

    @Override
    public SaleOrderDeliveredProducts getItem(int position) {
        return deliveredProductsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TDCSalesPreviewViewHolder salesPreviewViewHolder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tripsheets_payment_preview_delivered_products_adapter, null);

            salesPreviewViewHolder = new TDCSalesPreviewViewHolder();
            salesPreviewViewHolder.product_name = (TextView) convertView.findViewById(R.id.product_name);
            salesPreviewViewHolder.quantity = (TextView) convertView.findViewById(R.id.quantity);
            salesPreviewViewHolder.amount = (TextView) convertView.findViewById(R.id.amount);
            salesPreviewViewHolder.tax = (TextView) convertView.findViewById(R.id.tax);
            salesPreviewViewHolder.price = (TextView) convertView.findViewById(R.id.price);

            convertView.setTag(salesPreviewViewHolder);
        } else {
            salesPreviewViewHolder = (TDCSalesPreviewViewHolder) convertView.getTag();
        }

        final SaleOrderDeliveredProducts productBean = getItem(position);

        salesPreviewViewHolder.product_name.setText(productBean.getName() + " \n " + productBean.getCode());
        salesPreviewViewHolder.quantity.setText(productBean.getQuantity());
        salesPreviewViewHolder.price.setText(Utility.getFormattedCurrency(Double.parseDouble(productBean.getUnitRate())));
        salesPreviewViewHolder.amount.setText(Utility.getFormattedCurrency(Double.parseDouble(productBean.getProductAmount())));
        salesPreviewViewHolder.tax.setText(Utility.getFormattedCurrency(Double.parseDouble(productBean.getProductTax())));

        return convertView;
    }
}