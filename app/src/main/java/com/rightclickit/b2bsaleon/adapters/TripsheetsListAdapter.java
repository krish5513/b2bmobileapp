package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.AgentTDC_Order;
import com.rightclickit.b2bsaleon.activities.AgentsActivity;
import com.rightclickit.b2bsaleon.activities.AgentsInfoActivity;
import com.rightclickit.b2bsaleon.activities.Agents_AddActivity;
import com.rightclickit.b2bsaleon.activities.TripSheetStock;
import com.rightclickit.b2bsaleon.activities.TripSheetView;
import com.rightclickit.b2bsaleon.activities.TripSheetsActivity;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsList;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Sekhar Kuppa
 */

public class TripsheetsListAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    private Activity activity;
    Context ctxt;
    ArrayList<TripsheetsList> mTripSheetsList;
    private ImageLoader mImageLoader;
    private MMSharedPreferences mPreferences;
    private ArrayList<TripsheetsList> arraylist;
    private DBHelper mDBHelper;
    private String mViewPrivilege = "", mStockPrivilege = "";

    public TripsheetsListAdapter(Context ctxt, TripSheetsActivity agentsActivity, ArrayList<TripsheetsList> mAgentsBeansList,
                                 String mTripSheetViewPrivilege, String mTripSheetStockPrivilege) {
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
            view = mInflater.inflate(R.layout.tripsheets_list_custom, null);

            mHolder.mTripsheetCode = (TextView) view.findViewById(R.id.tv_prid);
            mHolder.mTripsheetDate = (TextView) view.findViewById(R.id.tv_date);
            mHolder.mTripsheetStatus = (TextView) view.findViewById(R.id.tv_status);
            mHolder.mTripsheetOBAmount = (TextView) view.findViewById(R.id.tv_ob_amount);
            mHolder.mTripsheetOrderedAmount = (TextView) view.findViewById(R.id.tv_ordered_amount);
            mHolder.mTripsheetReceivedAmount = (TextView) view.findViewById(R.id.tv_received_amount);
            mHolder.mTripsheetDueAmount = (TextView) view.findViewById(R.id.tv_due_amount);
            mHolder.viewbtn = (Button) view.findViewById(R.id.btn_view1);
            mHolder.viewbtn.setVisibility(View.GONE);
            mHolder.stockbtn = (Button) view.findViewById(R.id.btn_stock1);
            mHolder.stockbtn.setVisibility(View.GONE);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }

        if (mViewPrivilege.equals("tripsheet_summary")) {
            mHolder.viewbtn.setVisibility(View.VISIBLE);
        }

        if (mStockPrivilege.equals("list_stock")) {
            mHolder.stockbtn.setVisibility(View.VISIBLE);
        }

        mHolder.mTripsheetCode.setText(mTripSheetsList.get(position).getmTripshhetCode());
        mHolder.mTripsheetDate.setText(mTripSheetsList.get(position).getmTripshhetDate());
        mHolder.mTripsheetStatus.setText(mTripSheetsList.get(position).getmTripshhetStatus());
        mHolder.mTripsheetOBAmount.setText(Utility.getFormattedCurrency(Double.parseDouble(mTripSheetsList.get(position).getmTripshhetOBAmount())));
        mHolder.mTripsheetOrderedAmount.setText(Utility.getFormattedCurrency(Double.parseDouble(mTripSheetsList.get(position).getmTripshhetDueAmount())));
        mHolder.mTripsheetReceivedAmount.setText(Utility.getFormattedCurrency(Double.parseDouble(mTripSheetsList.get(position).getmTripshhetReceivedAmount())));
        mHolder.mTripsheetDueAmount.setText(Utility.getFormattedCurrency(Double.parseDouble(mTripSheetsList.get(position).getmTripshhetDueAmount())));

        mHolder.stockbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stockIntent = new Intent(activity, TripSheetStock.class);
                stockIntent.putExtra("tripsheetId", mTripSheetsList.get(position).getmTripshhetId());
                activity.startActivity(stockIntent);
                activity.finish();
            }
        });

        mHolder.viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTripSheetsList.get(position).getmTripshhetVerifyStatus().equals("1")) {
                    mPreferences.putString("TripId", mTripSheetsList.get(position).getmTripshhetId());
                    Intent stockIntent = new Intent(activity, TripSheetView.class);
                    stockIntent.putExtra("tripsheetId", mTripSheetsList.get(position).getmTripshhetId());
                    activity.startActivity(stockIntent);
                    activity.finish();
                } else {
                    Toast.makeText(ctxt, "This Trip Sheet is not yet verified.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    public class ViewHolder {
        TextView mTripsheetCode;
        TextView mTripsheetDate;
        TextView mTripsheetStatus;
        TextView mTripsheetOBAmount;
        TextView mTripsheetOrderedAmount;
        TextView mTripsheetReceivedAmount;
        TextView mTripsheetDueAmount;
        public Button viewbtn;
        public Button stockbtn;
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
