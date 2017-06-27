package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.AgentTakeOrderPreview;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderPreviewBean;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by PPS on 6/20/2017.
 */

public class TakeOrderPreviewAdapter extends BaseAdapter{


    LayoutInflater mInflater;
    private Activity activity;
    Context ctxt;
    ArrayList<TakeOrderPreviewBean> mpreviewBeansList1;
    private ImageLoader mImageLoader;
    private ArrayList<TakeOrderPreviewBean> arraylist;
    private MMSharedPreferences mPreferences;


    public TakeOrderPreviewAdapter(Context ctxt, AgentTakeOrderPreview previewActivity, ArrayList<TakeOrderPreviewBean> takeOrderPreviewBeanArrayList) {
        this.ctxt = ctxt;
        this.activity = previewActivity;
        this.mpreviewBeansList1 = takeOrderPreviewBeanArrayList;
        this.mInflater = LayoutInflater.from(activity);
        this.arraylist = new ArrayList<TakeOrderPreviewBean>();
        this.mPreferences = new MMSharedPreferences(activity);
    }


    @Override
    public int getCount() {
        return mpreviewBeansList1.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final TakeOrderPreviewAdapter.MyViewHolder holder;

        if (convertView == null) {

            holder = new TakeOrderPreviewAdapter.MyViewHolder();
            convertView = mInflater.inflate(R.layout.takeorderpreview_adapter, null);

            holder.pName = (TextView) convertView.findViewById(R.id.pName);
            holder.pQuantity = (TextView) convertView.findViewById(R.id.productQt);
            holder.pPrice = (TextView) convertView.findViewById(R.id.price);
            holder.pTax = (TextView) convertView.findViewById(R.id.tax);
            holder.pAmount = (TextView) convertView.findViewById(R.id.amount);
            holder.taxName = (TextView) convertView.findViewById(R.id.taxname);
            holder.taxPer = (TextView) convertView.findViewById(R.id.taxper);
            holder.fromPreview = (TextView) convertView.findViewById(R.id.fromDate);
            holder.toPreview = (TextView) convertView.findViewById(R.id.toDate);

            convertView.setTag(holder);
        } else {
            holder = (TakeOrderPreviewAdapter.MyViewHolder) convertView.getTag();
        }


        double price = Double.parseDouble(mpreviewBeansList1.get(position).getpPrice().replace(",", ""));

        float tax = 0.0f;
        if (mpreviewBeansList1.get(position).getmProductTaxVAT() != null)
            tax = Float.parseFloat(mpreviewBeansList1.get(position).getmProductTaxVAT());
        else if (mpreviewBeansList1.get(position).getmProductTaxGST() != null)
            tax = Float.parseFloat(mpreviewBeansList1.get(position).getmProductTaxGST());

        double taxAmount = (price * tax) / 100;
        double amount = price + taxAmount;

        holder.pName.setText(mpreviewBeansList1.get(position).getpName());
        holder.pQuantity.setText(mpreviewBeansList1.get(position).getpQuantity());
        holder.pPrice.setText(Utility.getFormattedCurrency(price));
        holder.pTax.setText(Utility.getFormattedCurrency(taxAmount));
        holder.pAmount.setText(Utility.getFormattedCurrency(amount*Double.parseDouble(mpreviewBeansList1.get(position).getpQuantity())));
        holder.fromPreview.setText(mpreviewBeansList1.get(position).getmProductFromDate());
        holder.toPreview.setText(mpreviewBeansList1.get(position).getmProductToDate());


        return convertView;
    }

    private class MyViewHolder {
        public TextView pName;

        public TextView pQuantity;
        public TextView pPrice;
        public TextView pTax, pAmount;
        public TextView taxName, taxPer;
        public TextView fromPreview, toPreview;






    }



    // Methos to display product image as full image

}
