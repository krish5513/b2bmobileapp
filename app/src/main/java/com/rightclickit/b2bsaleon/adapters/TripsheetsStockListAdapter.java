package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.TripSheetStock;
import com.rightclickit.b2bsaleon.activities.TripSheetsActivity;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsList;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Sekhar Kuppa
 */

public class TripsheetsStockListAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    private Activity activity;
    Context ctxt;
    ArrayList<TripsheetsList> mTripSheetsList;
    private ImageLoader mImageLoader;
    private MMSharedPreferences mPreferences;
    private ArrayList<TripsheetsList> arraylist;
    private DBHelper mDBHelper;
    private String mViewPrivilege = "", mStockPrivilege = "";

    public TripsheetsStockListAdapter(Context ctxt, TripSheetStock agentsActivity) {
        this.ctxt = ctxt;
        this.activity = agentsActivity;
//        this.mTripSheetsList = mAgentsBeansList;
//        this.mImageLoader = new ImageLoader(agentsActivity);
        this.mInflater = LayoutInflater.from(activity);
//        this.mPreferences = new MMSharedPreferences(activity);
//        this.arraylist = new ArrayList<TripsheetsList>();
//        this.mDBHelper = new DBHelper(activity);
//        this.arraylist.addAll(mTripSheetsList);
//        this.mViewPrivilege = mTripSheetViewPrivilege;
//        this.mStockPrivilege = mTripSheetStockPrivilege;
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
        final ViewHolder mHolder;
        if (view == null) {
            mHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.tripsheets_stock_list_custom, null);

            mHolder.mProductName = (TextView) view.findViewById(R.id.productName);
            mHolder.mOrder = (TextView) view.findViewById(R.id.order);
            mHolder.mDispatchQuantity = (EditText) view.findViewById(R.id.DispatchQuantity);
            mHolder.mVerifyQuantity = (EditText) view.findViewById(R.id.VerifyQuantity);
            mHolder.mDispatchDecrement = (ImageButton) view.findViewById(R.id.DispatchDecrement);
            mHolder.mDispatchIncrement = (ImageButton) view.findViewById(R.id.DispatchIncrement);
            mHolder.mVerifyDecrement = (ImageButton) view.findViewById(R.id.VerifyDecrement);
            mHolder.mVerifyIncrement = (ImageButton) view.findViewById(R.id.VerifyIncrement);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }

        return view;
    }

    public class ViewHolder {
        TextView mProductName;
        TextView mOrder;
        EditText mDispatchQuantity;
        EditText mVerifyQuantity;
        ImageButton mDispatchDecrement;
        ImageButton mDispatchIncrement;
        ImageButton mVerifyDecrement;
        ImageButton mVerifyIncrement;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mTripSheetsList.clear();
        if (charText.length() == 0) {
            mTripSheetsList.addAll(arraylist);
        } else {
            for (TripsheetsList wp : arraylist) {
                if (wp.getmTripshhetTrasnsporterName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mTripSheetsList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
