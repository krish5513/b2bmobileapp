package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.Sales_Preview_PrintActivity;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TDCCustomer;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Venkat on 06/20/2017.
 */

public class TDCSalesPreviewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity activity;
    private Context ctxt;
    private ArrayList<ProductsBean> selectedProductsList;

    public TDCSalesPreviewAdapter(Context ctxt, Sales_Preview_PrintActivity sales_preview_printActivity, List<ProductsBean> productsList) {
        this.ctxt = ctxt;
        this.activity = sales_preview_printActivity;
        this.mInflater = LayoutInflater.from(activity);
        this.selectedProductsList = (ArrayList<ProductsBean>) productsList;
    }

    private class TDCSalesPreviewViewHolder {
        TextView order_preview_product_name, order_preview_quantity, order_preview_tax, order_preview_mrp, order_preview_amount;
    }

    @Override
    public int getCount() {
        return selectedProductsList.size();
    }

    @Override
    public ProductsBean getItem(int position) {
        return selectedProductsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TDCSalesPreviewViewHolder salesPreviewViewHolder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tdc_sales_preview_adapter, null);

            salesPreviewViewHolder = new TDCSalesPreviewViewHolder();
            salesPreviewViewHolder.order_preview_product_name = (TextView) convertView.findViewById(R.id.order_preview_product_name);
            salesPreviewViewHolder.order_preview_quantity = (TextView) convertView.findViewById(R.id.order_preview_quantity);
            salesPreviewViewHolder.order_preview_tax = (TextView) convertView.findViewById(R.id.order_preview_tax);
            salesPreviewViewHolder.order_preview_mrp = (TextView) convertView.findViewById(R.id.order_preview_mrp);
            salesPreviewViewHolder.order_preview_amount = (TextView) convertView.findViewById(R.id.order_preview_amount);

            convertView.setTag(salesPreviewViewHolder);
        } else {
            salesPreviewViewHolder = (TDCSalesPreviewViewHolder) convertView.getTag();
        }

        final ProductsBean productBean = getItem(position);

        salesPreviewViewHolder.order_preview_product_name.setText(productBean.getProductTitle());
        salesPreviewViewHolder.order_preview_quantity.setText(String.format("%.3f", productBean.getSelectedQuantity()));
        salesPreviewViewHolder.order_preview_tax.setText(Utility.getFormattedCurrency(productBean.getTaxAmount()));
        salesPreviewViewHolder.order_preview_mrp.setText(Utility.getFormattedCurrency(Double.parseDouble(productBean.getProductConsumerPrice().replace(",", ""))));
        salesPreviewViewHolder.order_preview_amount.setText(Utility.getFormattedCurrency(productBean.getProductAmount()));

        return convertView;
    }
}