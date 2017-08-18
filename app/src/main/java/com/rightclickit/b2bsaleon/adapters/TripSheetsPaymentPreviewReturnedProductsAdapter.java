package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.AgentPaymentsView;
import com.rightclickit.b2bsaleon.activities.TripsheetPaymentsPreview;
import com.rightclickit.b2bsaleon.beanclass.SaleOrderDeliveredProducts;
import com.rightclickit.b2bsaleon.beanclass.SaleOrderReturnedProducts;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;

/**
 * Created by Venkat on 11/08/2017.
 */

public class TripSheetsPaymentPreviewReturnedProductsAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity activity;
    private Context ctxt;
    private ArrayList<SaleOrderReturnedProducts> returnedProductsList;

    public TripSheetsPaymentPreviewReturnedProductsAdapter(Context ctxt, TripsheetPaymentsPreview activity, ArrayList<SaleOrderReturnedProducts> productsList) {
        this.ctxt = ctxt;
        this.activity = activity;
        this.mInflater = LayoutInflater.from(activity);
        this.returnedProductsList = productsList;
    }
    public TripSheetsPaymentPreviewReturnedProductsAdapter(Context ctxt, AgentPaymentsView activity, ArrayList<SaleOrderReturnedProducts> productsList) {
        this.ctxt = ctxt;
        this.activity = activity;
        this.mInflater = LayoutInflater.from(activity);
        this.returnedProductsList = productsList;
    }

    private class TDCSalesPreviewViewHolder {
        TextView product_name, ob, delivered, returned, cb;
    }

    @Override
    public int getCount() {
        return returnedProductsList.size();
    }

    @Override
    public SaleOrderReturnedProducts getItem(int position) {
        return returnedProductsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TDCSalesPreviewViewHolder salesPreviewViewHolder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tripsheets_payment_preview_returned_products_adapter, null);

            salesPreviewViewHolder = new TDCSalesPreviewViewHolder();
            salesPreviewViewHolder.product_name = (TextView) convertView.findViewById(R.id.product_name);
            salesPreviewViewHolder.ob = (TextView) convertView.findViewById(R.id.ob);
            salesPreviewViewHolder.delivered = (TextView) convertView.findViewById(R.id.delivered);
            salesPreviewViewHolder.returned = (TextView) convertView.findViewById(R.id.returned);
            salesPreviewViewHolder.cb = (TextView) convertView.findViewById(R.id.cb);

            convertView.setTag(salesPreviewViewHolder);
        } else {
            salesPreviewViewHolder = (TDCSalesPreviewViewHolder) convertView.getTag();
        }

        final SaleOrderReturnedProducts productBean = getItem(position);

        salesPreviewViewHolder.product_name.setText(productBean.getName() + " \n " + productBean.getCode());
        salesPreviewViewHolder.ob.setText(productBean.getOpeningBalance());
        salesPreviewViewHolder.delivered.setText(productBean.getDelivered());
        salesPreviewViewHolder.returned.setText(productBean.getReturned());
        salesPreviewViewHolder.cb.setText(productBean.getClosingBalance());

        return convertView;
    }
}