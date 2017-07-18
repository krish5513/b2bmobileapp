package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.TripsheetDelivery;
import com.rightclickit.b2bsaleon.activities.TripsheetDeliveryPreview;
import com.rightclickit.b2bsaleon.activities.TripsheetPayments;
import com.rightclickit.b2bsaleon.activities.TripsheetReturns;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetDeliveriesBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetReturnsBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PPS on 7/17/2017.
 */

public class TripSheetReturnsAdapter extends BaseAdapter{

    LayoutInflater mInflater;
    private Activity activity;
    Context ctxt;
    ArrayList<TripSheetReturnsBean> mTripSheetsReturns;
    private ImageLoader mImageLoader;
    private MMSharedPreferences mPreferences;
    private ArrayList<TripSheetReturnsBean> arraylist;
    private DBHelper mDBHelper;


    public TripSheetReturnsAdapter(Context ctxt, TripsheetReturns returnsActivity, ArrayList<TripSheetReturnsBean> mreturnsBeanList) {
        this.ctxt = ctxt;
        this.activity = returnsActivity;
        this.mTripSheetsReturns = mreturnsBeanList;

        this.mInflater = LayoutInflater.from(activity);
        this.mPreferences = new MMSharedPreferences(activity);
        this.arraylist = new ArrayList<TripSheetReturnsBean>();
        this.mDBHelper = new DBHelper(activity);
        this.arraylist.addAll(mTripSheetsReturns);

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
        final TripSheetReturnsAdapter.ViewHolder mHolder;
        if (view == null) {
            mHolder = new TripSheetReturnsAdapter.ViewHolder();
            view = mInflater.inflate(R.layout.tripsheetreturns_custom, null);

            mHolder.dProductReturnName = (TextView) view.findViewById(R.id.productName);
            mHolder.dProductReturnAmount = (TextView) view.findViewById(R.id.D_QTY);
            mHolder.dProductReturnTypr = (TextView) view.findViewById(R.id.returnable);
            mHolder.dProductIncAmount = (TextView) view.findViewById(R.id.productQt);



            view.setTag(mHolder);
        } else {
            mHolder = (TripSheetReturnsAdapter.ViewHolder) view.getTag();
        }


        mHolder.dProductReturnName.setText(mTripSheetsReturns.get(position).getrProductReturnsName());
        mHolder.dProductReturnAmount.setText(mTripSheetsReturns.get(position).getrProductReturnsQty());
        mHolder.dProductReturnTypr.setText(mTripSheetsReturns.get(position).getrProductReturnsType());
        mHolder.dProductIncAmount.setText(Utility.getFormattedCurrency(Double.parseDouble(mTripSheetsReturns.get(position).getrProductReturnsIncAmount())));





        return view;
    }

    public class ViewHolder {
        TextView dProductReturnName;
        TextView dProductReturnAmount;
        TextView dProductReturnTypr;
        TextView dProductIncAmount;

    }



}
