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
import com.rightclickit.b2bsaleon.database.DBHelper;
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
    private DBHelper mDBHelper;

    public TDCSalesPreviewAdapter(Context ctxt, TDCSales_Preview_PrintActivity TDCSales_preview_printActivity, List<ProductsBean> productsList) {
        this.ctxt = ctxt;
        this.activity = TDCSales_preview_printActivity;
        this.mInflater = LayoutInflater.from(activity);
        this.selectedProductsList = (ArrayList<ProductsBean>) productsList;
        this.mDBHelper = new DBHelper(activity);
    }

    private class TDCSalesPreviewViewHolder {
        TextView order_preview_product_name, order_preview_quantity, order_preview_tax, order_preview_mrp, order_preview_amount, hssn_number, cgst, sgst,code,uom,cstotal;
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
            convertView = mInflater.inflate(R.layout.tdcpreviewcard, null);

            salesPreviewViewHolder = new TDCSalesPreviewViewHolder();
            salesPreviewViewHolder.order_preview_product_name = (TextView) convertView.findViewById(R.id.order_preview_product_name);
            salesPreviewViewHolder.order_preview_quantity = (TextView) convertView.findViewById(R.id.order_preview_quantity);
            salesPreviewViewHolder.order_preview_mrp = (TextView) convertView.findViewById(R.id.order_preview_mrp);
            salesPreviewViewHolder.order_preview_amount = (TextView) convertView.findViewById(R.id.order_preview_amount);
            salesPreviewViewHolder.order_preview_tax = (TextView) convertView.findViewById(R.id.order_preview_tax);
            salesPreviewViewHolder.hssn_number = (TextView) convertView.findViewById(R.id.hssn_number);
            salesPreviewViewHolder.cgst = (TextView) convertView.findViewById(R.id.cgst);
            salesPreviewViewHolder.sgst = (TextView) convertView.findViewById(R.id.sgst);
            salesPreviewViewHolder.code = (TextView) convertView.findViewById(R.id.order_preview_product_code);
            salesPreviewViewHolder.uom = (TextView) convertView.findViewById(R.id.order_preview_product_uom);
            salesPreviewViewHolder.cstotal = (TextView) convertView.findViewById(R.id.cgst_sgst);

            convertView.setTag(salesPreviewViewHolder);
        } else {
            salesPreviewViewHolder = (TDCSalesPreviewViewHolder) convertView.getTag();
        }

        final ProductsBean productBean = getItem(position);

        salesPreviewViewHolder.order_preview_product_name.setText(productBean.getProductTitle());
        salesPreviewViewHolder.code.setText(productBean.getProductCode());
        salesPreviewViewHolder.uom.setText(productBean.getProductUOM());
        salesPreviewViewHolder.order_preview_quantity.setText(String.format("%.3f", productBean.getSelectedQuantity()));

        if (productBean.getControlCode() != null) {
            salesPreviewViewHolder.hssn_number.setText(productBean.getControlCode());
        } else if (mDBHelper.getHSSNNUMBERByProductId(productBean.getProductId()) != null) {
            if (mDBHelper.getHSSNNUMBERByProductId(productBean.getProductId()).length() > 0) {
                salesPreviewViewHolder.hssn_number.setText(mDBHelper.getHSSNNUMBERByProductId(productBean.getProductId()));
            }
        } else {
            salesPreviewViewHolder.hssn_number.setText("-");
        }

        if (productBean.getProductgst() != null) {
            salesPreviewViewHolder.cgst.setText(productBean.getProductgst() + "%");
        } else if (mDBHelper.getGSTByProductId(productBean.getProductId()) > 0) {
            String gst = String.valueOf(mDBHelper.getGSTByProductId(productBean.getProductId()));
            salesPreviewViewHolder.cgst.setText(gst + "%");
        } else {
            salesPreviewViewHolder.cgst.setText("0.00%");
        }
        if (productBean.getProductvat() != null) {
            salesPreviewViewHolder.sgst.setText(productBean.getProductvat() + "%");
        } else if (mDBHelper.getVATByProductId(productBean.getProductId()) > 0) {
            String vat = String.valueOf(mDBHelper.getVATByProductId(productBean.getProductId()));
            salesPreviewViewHolder.sgst.setText(vat + "%");
        } else {
            salesPreviewViewHolder.sgst.setText("0.00%");
        }

        try {
            Double gst = 0.0, vat = 0.0;
            if (productBean.getProductgst() != null) {
                gst = Double.parseDouble(productBean.getProductgst());
            } else {
                gst = mDBHelper.getGSTByProductId(productBean.getProductId());
            }
            if (productBean.getProductvat() != null) {
                vat = Double.parseDouble(productBean.getProductvat());
            } else {
                vat = mDBHelper.getVATByProductId(productBean.getProductId());
            }
            taxes = gst + vat;
        } catch (Exception e) {
            e.printStackTrace();
        }
        salesPreviewViewHolder.cstotal.setText(String.valueOf(taxes));
        double unitPrice = productBean.getProductRatePerUnit();

        double prodQuantity = productBean.getSelectedQuantity();

        double sumRate1 = 100 + taxes;

        double sumRate = (unitPrice / sumRate1) * 100; // ((unitPrice/100+taxSumAmount))*100

        double salevalue = prodQuantity * sumRate; // prodQuantity * sumRate

        double taxValue1 = taxes / 100;
        taxvalue = prodQuantity * sumRate * taxValue1; // prodQuantity * sumRate*(taxSumAmount/100)

        salesPreviewViewHolder.order_preview_mrp.setText((Utility.getFormattedCurrency(sumRate)));

        salesPreviewViewHolder.order_preview_amount.setText(Utility.getFormattedCurrency(salevalue));
        salesPreviewViewHolder.order_preview_tax.setText(Utility.getFormattedCurrency(taxvalue));
        return convertView;
    }
}