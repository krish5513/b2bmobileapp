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
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
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
    ArrayList<TripsheetsStockList> mTripSheetsList;
    private ImageLoader mImageLoader;
    private MMSharedPreferences mPreferences;
    private ArrayList<TripsheetsStockList> arraylist;
    private DBHelper mDBHelper;
    private String mStcokDispatchPrivilege = "", mStockVerifyPrivilege = "";

    public TripsheetsStockListAdapter(Context ctxt, TripSheetStock agentsActivity, ArrayList<TripsheetsStockList> tripsList, String mStockDispatchPrivilege, String mStockVerifyPrivilege) {
        this.ctxt = ctxt;
        this.activity = agentsActivity;
        this.mTripSheetsList = tripsList;
        this.mInflater = LayoutInflater.from(activity);
        this.mPreferences = new MMSharedPreferences(activity);
        this.arraylist = new ArrayList<TripsheetsStockList>();
        this.mDBHelper = new DBHelper(activity);
        this.arraylist.addAll(mTripSheetsList);
        this.mStcokDispatchPrivilege = mStockDispatchPrivilege;
        this.mStockVerifyPrivilege = mStockVerifyPrivilege;
    }


    @Override
    public int getCount() {
        return mTripSheetsList.size();
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
            mHolder.mDispatchQuantity.setVisibility(View.GONE);
            mHolder.mVerifyQuantity = (EditText) view.findViewById(R.id.VerifyQuantity);
            mHolder.mVerifyQuantity.setVisibility(View.GONE);
            mHolder.mDispatchDecrement = (ImageButton) view.findViewById(R.id.DispatchDecrement);
            mHolder.mDispatchDecrement.setVisibility(View.GONE);
            mHolder.mDispatchIncrement = (ImageButton) view.findViewById(R.id.DispatchIncrement);
            mHolder.mDispatchIncrement.setVisibility(View.GONE);
            mHolder.mVerifyDecrement = (ImageButton) view.findViewById(R.id.VerifyDecrement);
            mHolder.mVerifyDecrement.setVisibility(View.GONE);
            mHolder.mVerifyIncrement = (ImageButton) view.findViewById(R.id.VerifyIncrement);
            mHolder.mVerifyIncrement.setVisibility(View.GONE);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }

        if (mStcokDispatchPrivilege.equals("Stock_Dispatch")) {
            mHolder.mDispatchQuantity.setVisibility(View.VISIBLE);
            mHolder.mDispatchDecrement.setVisibility(View.VISIBLE);
            mHolder.mDispatchIncrement.setVisibility(View.VISIBLE);
        }

        if (mStockVerifyPrivilege.equals("Stock_Verify")) {
            mHolder.mDispatchQuantity.setVisibility(View.VISIBLE);
            mHolder.mDispatchQuantity.setVisibility(View.VISIBLE);
            mHolder.mDispatchQuantity.setVisibility(View.VISIBLE);
        }

        mHolder.mProductName.setText(mTripSheetsList.get(position).getmTripsheetStockProductCode());
        mHolder.mOrder.setText(mTripSheetsList.get(position).getmTripsheetStockProductOrderQuantity());
        mHolder.mDispatchQuantity.setText(mTripSheetsList.get(position).getmTripsheetStockProductOrderQuantity());
        mHolder.mVerifyQuantity.setText(mTripSheetsList.get(position).getmTripsheetStockProductOrderQuantity());

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
            for (TripsheetsStockList wp : arraylist) {
                if (wp.getmTripsheetStockProductName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mTripSheetsList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
