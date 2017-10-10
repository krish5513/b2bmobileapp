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
import com.rightclickit.b2bsaleon.activities.RouteStock;
import com.rightclickit.b2bsaleon.activities.TripSheetStock;
import com.rightclickit.b2bsaleon.activities.TripSheetView;
import com.rightclickit.b2bsaleon.activities.TripSheetsActivity;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsList;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
import com.rightclickit.b2bsaleon.customviews.CustomAlertDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.interfaces.TripSheetStockListener;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by PPS on 10/3/2017.
 */

public class RouteStockAdapter extends BaseAdapter{
    private Context ctxt;
    private Activity activity;
    private RouteStock listener;
    private LayoutInflater mInflater;
    private ArrayList<TripsheetsStockList> allTripSheetStockList, filteredTripSheetStockList;
    private ArrayList<String> privilegeActionsData;
    private Map<String, TripsheetsStockList> dispatchProductsListHashMap, verifyProductsListHashMap;
    private Map<String, String> dispatchProductsListHashMapTemp, verifyProductsListHashMapTemp;

    private final String zero_cost = "0.000";

    public RouteStockAdapter(Context ctxt, RouteStock agentsActivity, RouteStock tripSheetStockListener, ArrayList<TripsheetsStockList> tripSheetStockList) {
        this.ctxt = ctxt;
        this.activity = agentsActivity;
        this.listener = tripSheetStockListener;
        this.mInflater = LayoutInflater.from(activity);
        this.allTripSheetStockList = tripSheetStockList;
        this.filteredTripSheetStockList = new ArrayList<>();
        this.filteredTripSheetStockList.addAll(allTripSheetStockList);
        this.privilegeActionsData = privilegeActionsData;
        this.dispatchProductsListHashMap = new HashMap<>();
        this.dispatchProductsListHashMapTemp = new HashMap<>();
        this.verifyProductsListHashMap = new HashMap<>();
        this.verifyProductsListHashMapTemp = new HashMap<>();
        if (dispatchProductsListHashMapTemp.size() > 0) {
            dispatchProductsListHashMapTemp.clear();
        }
        if (verifyProductsListHashMapTemp.size() > 0) {
            verifyProductsListHashMapTemp.clear();
        }
    }

    @Override
    public int getCount() {
        return filteredTripSheetStockList.size();
    }

    @Override
    public TripsheetsStockList getItem(int position) {
        return filteredTripSheetStockList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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
          //  mHolder.mReturnQtyInc = (ImageButton) view.findViewById(R.id.return_inc);
          //  mHolder.mReturnQtyDec = (ImageButton) view.findViewById(R.id.return_dec);

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


        final TripsheetsStockList currentStockList = getItem(position);
        mHolder.mProductName.setText(currentStockList.getmTripsheetStockProductName());


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
       // public ImageButton mReturnQtyInc;
       // public ImageButton mReturnQtyDec;
        TextView mLeakQty;
        public ImageButton mLeakQtyInc;
        public ImageButton mLeakQtyDec;
        TextView mOthersQty;
        public ImageButton mOthersQtyInc;
        public ImageButton mOthersQtyDec;
    }


}


