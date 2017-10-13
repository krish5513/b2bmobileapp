package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.TDCSales_Preview_PrintActivity;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Venkat on 06/20/2017.
 */

public class TDCSalesPreviewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity activity;
    private Context ctxt;
    private ArrayList<ProductsBean> selectedProductsList;
    double taxes;
    String totalRate = "";
    double rate, ratetax, taxvalue;

    public TDCSalesPreviewAdapter(Context ctxt, TDCSales_Preview_PrintActivity TDCSales_preview_printActivity, List<ProductsBean> productsList) {
        this.ctxt = ctxt;
        this.activity = TDCSales_preview_printActivity;
        this.mInflater = LayoutInflater.from(activity);
        this.selectedProductsList = (ArrayList<ProductsBean>) productsList;
    }

    private class TDCSalesPreviewViewHolder {
        TextView order_preview_product_name, order_preview_quantity, order_preview_tax, order_preview_mrp, order_preview_amount, hssn_number, cgst, sgst;
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
            salesPreviewViewHolder.order_preview_mrp = (TextView) convertView.findViewById(R.id.order_preview_mrp);
            salesPreviewViewHolder.order_preview_amount = (TextView) convertView.findViewById(R.id.order_preview_amount);
            salesPreviewViewHolder.order_preview_tax = (TextView) convertView.findViewById(R.id.order_preview_tax);
            salesPreviewViewHolder.hssn_number = (TextView) convertView.findViewById(R.id.hssn_number);
            salesPreviewViewHolder.cgst = (TextView) convertView.findViewById(R.id.cgst);
            salesPreviewViewHolder.sgst = (TextView) convertView.findViewById(R.id.sgst);

            convertView.setTag(salesPreviewViewHolder);
        } else {
            salesPreviewViewHolder = (TDCSalesPreviewViewHolder) convertView.getTag();
        }

        final ProductsBean productBean = getItem(position);

        salesPreviewViewHolder.order_preview_product_name.setText(productBean.getProductTitle());
        salesPreviewViewHolder.order_preview_quantity.setText(String.format("%.3f", productBean.getSelectedQuantity()));


        if (productBean.getControlCode() != null) {
            salesPreviewViewHolder.hssn_number.setText(productBean.getControlCode());
        } else {
            salesPreviewViewHolder.hssn_number.setText("-");
        }
        if (productBean.getProductgst() != null) {
            salesPreviewViewHolder.cgst.setText(productBean.getProductgst() + "%");
        } else {
            salesPreviewViewHolder.cgst.setText("0.00%");
        }
        if (productBean.getProductvat() != null) {
            salesPreviewViewHolder.sgst.setText(productBean.getProductvat() + "%");
        } else {
            salesPreviewViewHolder.sgst.setText("0.00%");
        }

        try {
            Double gst = 0.0, vat = 0.0;
            if (productBean.getProductgst() != null) {
                gst = Double.parseDouble(productBean.getProductgst());
            }
            if (productBean.getProductvat() != null) {
                vat = Double.parseDouble(productBean.getProductvat());
            }
            taxes = gst + vat;
        } catch (Exception e) {
            e.printStackTrace();
        }

//        rate = productBean.getProductRatePerUnit();
//        double per1 = 100 + taxes;
//        double per = taxes / per1;
//        try {
//            ratetax = rate * per;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        totalRate = String.valueOf((rate - ratetax));
//        salesPreviewViewHolder.order_preview_mrp.setText((Utility.getFormattedCurrency(Double.parseDouble(totalRate))));
//        double qty = Double.parseDouble(String.format("%.3f", productBean.getSelectedQuantity()));
//        double salevalue = qty * Double.parseDouble(totalRate);
//        try {
//            taxvalue = salevalue * per;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        double unitPrice = productBean.getProductRatePerUnit();
        System.out.println("ADAPTER Unite Price IS::: " + unitPrice);

        System.out.println("ADAPTER TAX SUM IS::: " + taxes);

        double prodQuantity = productBean.getSelectedQuantity();
        System.out.println("ADAPTER PROD SELCTED QUA IS::: " + prodQuantity);

        double sumRate1 = 100 + taxes;

        double sumRate = (unitPrice / sumRate1) * 100; // ((unitPrice/100+taxSumAmount))*100
        System.out.println("ADAPTER SUM RATE IS::: " + sumRate);

        double salevalue = prodQuantity * sumRate; // prodQuantity * sumRate
        System.out.println("ADAPTER VALUE IS::: " + salevalue);

        double taxValue1 = taxes / 100;
        taxvalue = prodQuantity * sumRate * taxValue1; // prodQuantity * sumRate*(taxSumAmount/100)
        System.out.println("ADAPTER TAX VALUE IS::: " + taxvalue);

        salesPreviewViewHolder.order_preview_mrp.setText((Utility.getFormattedCurrency(sumRate)));

        salesPreviewViewHolder.order_preview_amount.setText(Utility.getFormattedCurrency(salevalue));
        salesPreviewViewHolder.order_preview_tax.setText(Utility.getFormattedCurrency(taxvalue));
        return convertView;
    }
}