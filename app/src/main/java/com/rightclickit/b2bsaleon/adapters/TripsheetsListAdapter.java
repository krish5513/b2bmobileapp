package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.rightclickit.b2bsaleon.beanclass.TripsheetsList;
import com.rightclickit.b2bsaleon.customviews.CustomAlertDialog;
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
    private String mViewPrivilege = "", mStockPrivilege = "",tripCode="";

    public TripsheetsListAdapter(Context ctxt, TripSheetsActivity agentsActivity, ArrayList<TripsheetsList> mAgentsBeansList, String mTripSheetViewPrivilege, String mTripSheetStockPrivilege) {
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
        final ViewHolder mHolder;
        if (view == null) {
            mHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.tripsheets_list_custom, null);

            mHolder.mTripsheetCode = (TextView) view.findViewById(R.id.tv_prid);
            mHolder.routecode = (TextView) view.findViewById(R.id.routecode);
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

        final TripsheetsList currentTripSheet = getItem(position);


        if (currentTripSheet.getmTripshhetCode() != null) {
            if (currentTripSheet.getmTripshhetCode().length() == 0) {

                tripCode=("TR-"+currentTripSheet.getMy_Id());
                mHolder.mTripsheetCode.setText(tripCode);
            } else {

                tripCode=currentTripSheet.getmTripshhetCode();
                mHolder.mTripsheetCode.setText(tripCode);
            }
        } else {
            tripCode=("TR-"+currentTripSheet.getMy_Id());
            mHolder.mTripsheetCode.setText(tripCode);
        }


        mHolder.mTripsheetDate.setText(currentTripSheet.getmTripshhetDate());
/*
        if (currentTripSheet.getIsTripshhetClosed() == 0)
            mHolder.mTripsheetStatus.setText("In Transit");
        else{
            mHolder.mTripsheetStatus.setText("Closed");
        }
          */
        if (currentTripSheet.getApproved_by() != null) {
            Log.i("hytdfktfy",currentTripSheet.getApproved_by());
            if (currentTripSheet.getApproved_by().equals("1")) {


                mHolder.mTripsheetStatus.setText("Closed");
            } else {


                mHolder.mTripsheetStatus.setText("In Transit");
            }
        } else {

            mHolder.mTripsheetStatus.setText("null");
        }




        mHolder.routecode.setText(currentTripSheet.getmTripshhetRouteCode());

        mHolder.mTripsheetOBAmount.setText(Utility.getFormattedCurrency(Double.parseDouble(currentTripSheet.getmTripshhetOBAmount().replace(",", ""))));
        mHolder.mTripsheetOrderedAmount.setText(Utility.getFormattedCurrency(Double.parseDouble(currentTripSheet.getmTripshhetOrderedAmount().replace(",", ""))));
        //   mHolder.mTripsheetReceivedAmount.setText(Utility.getFormattedCurrency(Double.parseDouble(currentTripSheet.getmTripshhetReceivedAmount().replace(",", ""))));
        mHolder.mTripsheetDueAmount.setText(Utility.getFormattedCurrency(Double.parseDouble(currentTripSheet.getmTripshhetDueAmount().replace(",", ""))));
        mHolder.mTripsheetReceivedAmount.setText(mDBHelper.getRouteNameByRouteCode(currentTripSheet.getmTripshhetRouteCode()));
        mHolder.stockbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tripCode111 = "";
                if (currentTripSheet.getmTripshhetCode() != null) {
                    if (currentTripSheet.getmTripshhetCode().length() == 0) {
                        tripCode111=("TR-"+currentTripSheet.getMy_Id());
                    } else {
                        tripCode111=currentTripSheet.getmTripshhetCode();
                    }
                } else {
                    tripCode111=("TR-"+currentTripSheet.getMy_Id());
                }
                Intent stockIntent = new Intent(activity, TripSheetStock.class);
                stockIntent.putExtra("tripsheetId", currentTripSheet.getmTripshhetId());
                stockIntent.putExtra("tripsheetCode", tripCode111);
                stockIntent.putExtra("tripsheetDate", currentTripSheet.getmTripshhetDate());
                activity.startActivity(stockIntent);
                activity.finish();
            }
        });

        mHolder.viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentTripSheet.getmTripshhetVerifyStatus().equals("1")) {
                    String tripCode111 = "";
                    if (currentTripSheet.getmTripshhetCode() != null) {
                        if (currentTripSheet.getmTripshhetCode().length() == 0) {
                            tripCode111=("TR-"+currentTripSheet.getMy_Id());
                        } else {
                            tripCode111=currentTripSheet.getmTripshhetCode();
                        }
                    } else {
                        tripCode111=("TR-"+currentTripSheet.getMy_Id());
                    }
                    mPreferences.putString("TripId", currentTripSheet.getmTripshhetId());
                    mPreferences.putString("tripsheetCode",tripCode111);
                    mPreferences.putString("tripsheetDate", currentTripSheet.getmTripshhetDate());
                    mPreferences.putString("tripsheetroutecode", currentTripSheet.getmTripshhetRouteCode());
                    Intent stockIntent = new Intent(activity, TripSheetView.class);
                    stockIntent.putExtra("tripsheetId", currentTripSheet.getmTripshhetId());
                    stockIntent.putExtra("tripsheetCode",tripCode111);
                    stockIntent.putExtra("tripsheetDate", currentTripSheet.getmTripshhetDate());
                    activity.startActivity(stockIntent);
                    activity.finish();
                } else {
                    //  Toast.makeText(ctxt, "This Trip Sheet is not yet verified.", Toast.LENGTH_LONG).show();
                    CustomAlertDialog.showAlertDialog(ctxt, "Failed", activity.getResources().getString(R.string.stockv));
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
        TextView mTripsheetDueAmount,routecode;
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
                if (wp.getmTripshhetCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mTripSheetsList.add(wp);
                }
                if (wp.getmTripshhetRouteCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mTripSheetsList.add(wp);
                }
                if (wp.getmTripshhetDate().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mTripSheetsList.add(wp);
                }
                if (wp.getmTripshhetStatus().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mTripSheetsList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
