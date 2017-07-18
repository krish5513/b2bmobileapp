package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.TripSheetStock;
import com.rightclickit.b2bsaleon.activities.TripSheetView;
import com.rightclickit.b2bsaleon.activities.TripSheetsActivity;
import com.rightclickit.b2bsaleon.activities.TripsheetDelivery;
import com.rightclickit.b2bsaleon.activities.TripsheetDeliveryPreview;
import com.rightclickit.b2bsaleon.beanclass.TripSheetDeliveriesBean;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsList;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by PPS on 7/17/2017.
 */

public class TripSheetDeliveriesAdapter extends BaseAdapter {


    LayoutInflater mInflater;
    private Activity activity;
    Context ctxt;
    ArrayList<TripSheetDeliveriesBean> mTripSheetsDeliveries;
    private ImageLoader mImageLoader;
    private MMSharedPreferences mPreferences;
    private ArrayList<TripSheetDeliveriesBean> arraylist;
    private DBHelper mDBHelper;


    public TripSheetDeliveriesAdapter(Context ctxt, TripsheetDelivery deliveryActivity, ArrayList<TripSheetDeliveriesBean> mdeliveriesBeanList) {
        this.ctxt = ctxt;
        this.activity = deliveryActivity;
        this.mTripSheetsDeliveries = mdeliveriesBeanList;

        this.mInflater = LayoutInflater.from(activity);
        this.mPreferences = new MMSharedPreferences(activity);
        this.arraylist = new ArrayList<TripSheetDeliveriesBean>();
        this.mDBHelper = new DBHelper(activity);
        this.arraylist.addAll(mTripSheetsDeliveries);

    }

    @Override
    public int getCount() {
        return 10;
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final TripSheetDeliveriesAdapter.ViewHolder mHolder;
        if (view == null) {
            mHolder = new TripSheetDeliveriesAdapter.ViewHolder();
            view = mInflater.inflate(R.layout.tripsheetdeliveries_custom, null);

            mHolder.dProductName = (TextView) view.findViewById(R.id.productName);
            mHolder.dProductStatus = (TextView) view.findViewById(R.id.status);
            mHolder.dInStockAmount = (TextView) view.findViewById(R.id.quantity_stock);
            mHolder.dProductPrice = (TextView) view.findViewById(R.id.productSP);
            mHolder.dProductTax = (TextView) view.findViewById(R.id.taxAmount);
            mHolder.dProductAmount = (TextView) view.findViewById(R.id.amount);
            mHolder.dProductQuantity = (TextView) view.findViewById(R.id.productQt);


            view.setTag(mHolder);
        } else {
            mHolder = (TripSheetDeliveriesAdapter.ViewHolder) view.getTag();
        }


        mHolder.dProductName.setText(mTripSheetsDeliveries.get(position).getDproductName());
        mHolder.dProductStatus.setText(mTripSheetsDeliveries.get(position).getDproductStatus());
        mHolder.dInStockAmount.setText(mTripSheetsDeliveries.get(position).getDproductInstockAmount());
        mHolder.dProductPrice.setText(Utility.getFormattedCurrency(Double.parseDouble(mTripSheetsDeliveries.get(position).getDproductPrice())));
        mHolder.dProductTax.setText(Utility.getFormattedCurrency(Double.parseDouble(mTripSheetsDeliveries.get(position).getDproductTax())));
        mHolder.dProductAmount.setText(Utility.getFormattedCurrency(Double.parseDouble(mTripSheetsDeliveries.get(position).getDproductAmount())));
        mHolder.dProductQuantity.setText(Utility.getFormattedCurrency(Double.parseDouble(mTripSheetsDeliveries.get(position).getDproductQuantity())));




        return view;
    }

    public class ViewHolder {
        TextView dProductName;
        TextView dProductStatus;
        TextView dInStockAmount;
        TextView dProductPrice;
        TextView dProductTax;
        TextView dProductAmount;
        TextView dProductQuantity;

    }



}
