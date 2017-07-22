package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.TripsheetDelivery;
import com.rightclickit.b2bsaleon.activities.TripsheetDeliveryPreview;
import com.rightclickit.b2bsaleon.activities.TripsheetPayments;
import com.rightclickit.b2bsaleon.beanclass.TripSheetDeliveriesBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetPaymentsBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;

/**
 * Created by PPS on 7/17/2017.
 */

public class TripSheetPaymentsAdapter extends BaseAdapter{

    LayoutInflater mInflater;
    private Activity activity;
    Context ctxt;
    ArrayList<TripSheetPaymentsBean> mTripSheetsPayments;
    private ImageLoader mImageLoader;
    private MMSharedPreferences mPreferences;
    private ArrayList<TripSheetPaymentsBean> arraylist;
    private DBHelper mDBHelper;


    public TripSheetPaymentsAdapter(Context ctxt, TripsheetPayments paymentsActivity, ArrayList<TripSheetPaymentsBean> mpaymentsBeanList) {
        this.ctxt = ctxt;
        this.activity = paymentsActivity;
        this.mTripSheetsPayments = mpaymentsBeanList;

        this.mInflater = LayoutInflater.from(activity);
        this.mPreferences = new MMSharedPreferences(activity);
        this.arraylist = new ArrayList<TripSheetPaymentsBean>();
        this.mDBHelper = new DBHelper(activity);
        this.arraylist.addAll(mTripSheetsPayments);

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
        final TripSheetPaymentsAdapter.ViewHolder mHolder;
        if (view == null) {
            mHolder = new TripSheetPaymentsAdapter.ViewHolder();
            view = mInflater.inflate(R.layout.tripsheetpayments_custom, null);

            mHolder.paymentsAmount = (TextView) view.findViewById(R.id.amount);
            mHolder.paymentsMOP = (Spinner) view.findViewById(R.id.paymentTypeSpinner);



            view.setTag(mHolder);
        } else {
            mHolder = (TripSheetPaymentsAdapter.ViewHolder) view.getTag();
        }


        mHolder.paymentsAmount.setText(mTripSheetsPayments.get(position).getmTripshhetPaymentsAmount());
       // mHolder.paymentsMOP.setText(mTripSheetsPayments.get(position).getPaymentMOP());





        return view;
    }

    public class ViewHolder {
        TextView paymentsAmount;
        Spinner paymentsMOP;


    }



}


