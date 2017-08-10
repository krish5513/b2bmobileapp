package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.TripSheetView;
import com.rightclickit.b2bsaleon.activities.TripSheetViewPreview;
import com.rightclickit.b2bsaleon.activities.TripsheetDelivery;
import com.rightclickit.b2bsaleon.beanclass.TripsheetSOList;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by PPS on 8/9/2017.
 */

public class TripSheetSoListPreviewAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private Activity activity;
    private ImageLoader mImageLoader;
    private ArrayList<TripsheetSOList> allSaleOrdersList, filteredSaleOrdersList;
    private String mTakeOrderPrivilege = "", mStockVerifyPrivilege = "";

    public TripSheetSoListPreviewAdapter(TripSheetViewPreview tripSheetView, TripSheetViewPreview tripSheetView1, ArrayList<TripsheetSOList> tripsSOList, String mTakeOrderPrivilege) {
        this.activity = tripSheetView;
        this.mInflater = LayoutInflater.from(activity);
        this.mTakeOrderPrivilege = mTakeOrderPrivilege;
        this.allSaleOrdersList = tripsSOList;
        this.filteredSaleOrdersList = new ArrayList<>();
        this.filteredSaleOrdersList.addAll(allSaleOrdersList);
    }

    public void setAllSaleOrdersList(ArrayList<TripsheetSOList> saleOrdersList) {
        this.allSaleOrdersList = saleOrdersList;
        this.filteredSaleOrdersList = new ArrayList<>();
        this.filteredSaleOrdersList.addAll(allSaleOrdersList);
    }

    @Override
    public int getCount() {
        return filteredSaleOrdersList.size();
    }

    @Override
    public TripsheetSOList getItem(int i) {
        return filteredSaleOrdersList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final TripSheetSoListPreviewAdapter.ViewHolder mHolder;
        if (view == null) {
            mHolder = new TripSheetSoListPreviewAdapter.ViewHolder();
            view = mInflater.inflate(R.layout.tripsheetsummary_preview, null);

            mHolder.mAgentCode = (TextView) view.findViewById(R.id.agentCode);
            mHolder.mOBValue = (TextView) view.findViewById(R.id.OB);

            mHolder.mSOAgentName = (TextView) view.findViewById(R.id.AgentName);
            mHolder.mSOOrderedValue = (TextView) view.findViewById(R.id.order);
            mHolder.mSOReceivedValue = (TextView) view.findViewById(R.id.received);
            mHolder.mSODueValue = (TextView) view.findViewById(R.id.due);

            view.setTag(mHolder);
        } else {
            mHolder = (TripSheetSoListPreviewAdapter.ViewHolder) view.getTag();
        }



        final TripsheetSOList currentSaleOrder = getItem(position);

        mHolder.mAgentCode.setText(currentSaleOrder.getmTripshetSOAgentCode());
        mHolder.mOBValue.setText(currentSaleOrder.getmTripshetSOOpAmount());

        mHolder.mSOAgentName.setText(currentSaleOrder.getmTripshetSOAgentFirstName());
        mHolder.mSOOrderedValue.setText(Utility.getFormattedCurrency(Double.parseDouble(currentSaleOrder.getmTripshetSOValue())));
        mHolder.mSOReceivedValue.setText(Utility.getFormattedCurrency(Double.parseDouble(currentSaleOrder.getmTripshetSOReceivedAmount())));
        mHolder.mSODueValue.setText(Utility.getFormattedCurrency(Double.parseDouble(currentSaleOrder.getmTripshetSODueAmount())));




        return view;
    }

    public class ViewHolder {
        TextView mAgentCode;
        TextView mOBValue;

        TextView mSOAgentName;
        TextView mSOOrderedValue;
        TextView mSOReceivedValue;
        TextView mSODueValue;

    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        filteredSaleOrdersList.clear();

        if (charText.length() == 0) {
            filteredSaleOrdersList.addAll(allSaleOrdersList);
        } else {
            for (TripsheetSOList wp : allSaleOrdersList) {
                if (wp.getmTripshetSOAgentCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredSaleOrdersList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    // Method to calculate distance between two locations using HAVERSINE FORMULA
    /*private String getDistanceBetweenLocations(double sourceLat, double sourceLong, double destLat, double destLong) {
        String distanceStr = "";
        double R = 6371000f; // Default Radius of the earth in meters
        double dLat = (sourceLat - destLat) * Math.PI / 180f;
        double dLon = (sourceLong - destLong) * Math.PI / 180f;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(dLat) * Math.cos(dLon) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2f * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        //System.out.println("DISTANCE IN METERS::: " + Math.round(distance));
        //System.out.println("DISTANCE IN KILOMETERS::: " + Math.round(distance / 1000));
        if (Math.round(distance) > 1000) {
            distanceStr = String.valueOf(Math.round(distance / 1000)) + " KM";
        } else {
            distanceStr = String.valueOf(Math.round(distance)) + " M";
        }
        return distanceStr;
    }*/
}
