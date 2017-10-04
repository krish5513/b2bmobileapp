package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.TripSheetStock;
import com.rightclickit.b2bsaleon.activities.TripSheetView;
import com.rightclickit.b2bsaleon.activities.TripSheetsActivity;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsList;
import com.rightclickit.b2bsaleon.customviews.CustomAlertDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by PPS on 10/3/2017.
 */

public class RouteStockAdapter extends BaseAdapter{

    LayoutInflater mInflater;
    private Activity activity;
    Context ctxt;
    ArrayList<TripsheetsList> mTripSheetsList;
    private ImageLoader mImageLoader;
    private MMSharedPreferences mPreferences;
    private ArrayList<TripsheetsList> arraylist;
    private DBHelper mDBHelper;
    private String mViewPrivilege = "", mStockPrivilege = "";

    public RouteStockAdapter(Context ctxt, TripSheetsActivity agentsActivity, ArrayList<TripsheetsList> mAgentsBeansList, String mTripSheetViewPrivilege, String mTripSheetStockPrivilege) {
        this.ctxt = ctxt;
        this.activity = agentsActivity;
        this.mTripSheetsList = mAgentsBeansList;
        this.mImageLoader = new ImageLoader(agentsActivity);
        this.mInflater = LayoutInflater.from(activity);
        this.mPreferences = new MMSharedPreferences(activity);
        this.arraylist = new ArrayList<TripsheetsList>();
        this.mDBHelper = new DBHelper(activity);
        this.arraylist.addAll(mTripSheetsList);
        this.mViewPrivilege = mTripSheetViewPrivilege;
        this.mStockPrivilege = mTripSheetStockPrivilege;
    }

    @Override
    public int getCount() {
        return mTripSheetsList.size();
    }

    @Override
    public TripsheetsList getItem(int i) {
        return mTripSheetsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final RouteStockAdapter.ViewHolder mHolder;
        if (view == null) {
            mHolder = new RouteStockAdapter.ViewHolder();
            view = mInflater.inflate(R.layout.routestock_custom, null);

            mHolder.mRouteCode = (TextView) view.findViewById(R.id.route_code);
            mHolder.mTruckQty = (TextView) view.findViewById(R.id.TruckQty);
            mHolder.mClosingBal = (TextView) view.findViewById(R.id.cb);
            mHolder.mProductName = (TextView) view.findViewById(R.id.p_name);
            mHolder.mProductUom = (TextView) view.findViewById(R.id.p_uom);
            mHolder.mDeliveryQty = (TextView) view.findViewById(R.id.delivery_qty);
            mHolder.mReturnQty = (TextView) view.findViewById(R.id.return_quantity);
            mHolder.mReturnQtyInc = (ImageButton) view.findViewById(R.id.return_inc);
            mHolder.mReturnQtyDec = (ImageButton) view.findViewById(R.id.return_dec);

            mHolder.mLeakQty = (TextView) view.findViewById(R.id.leak_quantity);
            mHolder.mLeakQtyInc = (ImageButton) view.findViewById(R.id.leak_inc);
            mHolder.mLeakQtyDec = (ImageButton) view.findViewById(R.id.leak_dec);

            mHolder.mOthersQty = (TextView) view.findViewById(R.id.others_quantity);
            mHolder.mOthersQtyInc = (ImageButton) view.findViewById(R.id.others_inc);
            mHolder.mOthersQtyDec = (ImageButton) view.findViewById(R.id.others_dec);



            view.setTag(mHolder);
        } else {
            mHolder = (RouteStockAdapter.ViewHolder) view.getTag();
        }






        return view;
    }

    public class ViewHolder {
        TextView mRouteCode;
        TextView mTruckQty;
        TextView mClosingBal;
        TextView mProductName;
        TextView mProductUom;
        TextView mDeliveryQty;
        TextView mReturnQty;
        public ImageButton mReturnQtyInc;
        public ImageButton mReturnQtyDec;
        TextView mLeakQty;
        public ImageButton mLeakQtyInc;
        public ImageButton mLeakQtyDec;
        TextView mOthersQty;
        public ImageButton mOthersQtyInc;
        public ImageButton mOthersQtyDec;
    }


}


