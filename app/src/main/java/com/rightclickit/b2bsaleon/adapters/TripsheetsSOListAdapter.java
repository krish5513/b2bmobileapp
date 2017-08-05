package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.TripSheetStock;
import com.rightclickit.b2bsaleon.activities.TripSheetView;
import com.rightclickit.b2bsaleon.activities.TripsheetDelivery;
import com.rightclickit.b2bsaleon.beanclass.TripsheetSOList;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Sekhar Kuppa
 */

public class TripsheetsSOListAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    private Activity activity;
    ArrayList<TripsheetSOList> mTripSheetsList;
    private ImageLoader mImageLoader;
    private MMSharedPreferences mPreferences;
    private ArrayList<TripsheetSOList> arraylist;
    private DBHelper mDBHelper;
    private String mTakeOrderPrivilege = "", mStockVerifyPrivilege = "";
    private double currentLat = 0.0, currentLong = 0.0;
    String distance;

    public TripsheetsSOListAdapter(TripSheetView tripSheetView, TripSheetView tripSheetView1, ArrayList<TripsheetSOList> tripsSOList, String mTakeOrderPrivilege
            , double mCurrentLocationLat, double mCurrentLocationLongitude) {
        this.activity = tripSheetView;
        this.mTripSheetsList = tripsSOList;
        this.mInflater = LayoutInflater.from(activity);
        this.mPreferences = new MMSharedPreferences(activity);
        this.arraylist = new ArrayList<TripsheetSOList>();
        this.mDBHelper = new DBHelper(activity);
        this.arraylist.addAll(mTripSheetsList);
        this.mTakeOrderPrivilege = mTakeOrderPrivilege;
        this.currentLat = mCurrentLocationLat;
        this.currentLong = mCurrentLocationLongitude;
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
            view = mInflater.inflate(R.layout.tripsheets_so_list_custom, null);

            mHolder.mAgentCode = (TextView) view.findViewById(R.id.tv_agent_code);
            mHolder.mSODate = (TextView) view.findViewById(R.id.tv_so_date);
            mHolder.mSOItemsCount = (TextView) view.findViewById(R.id.tv_itemsQty);
            mHolder.mSOAgentName = (TextView) view.findViewById(R.id.companyName);
            mHolder.mSOOrderedValue = (TextView) view.findViewById(R.id.tv_orderValue);
            mHolder.mSOReceivedValue = (TextView) view.findViewById(R.id.tv_receivedValue);
            mHolder.mSODueValue = (TextView) view.findViewById(R.id.tv_dueValue);
            mHolder.mSOTakeOrder = (Button) view.findViewById(R.id.btn_sale_ord1);
            mHolder.mSOTakeOrder.setVisibility(View.GONE);
            mHolder.mSOMapIconParent = (LinearLayout) view.findViewById(R.id.gotoCustomer);
            mHolder.mSOAgentDistance = (TextView) view.findViewById(R.id.tv_km);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }

        if (mTakeOrderPrivilege.equals("list_view_takeorder")) {
            mHolder.mSOTakeOrder.setVisibility(View.VISIBLE);
        }
        if(mTripSheetsList.get(position).getmTripshetSOAgentLatitude()!=null && !mTripSheetsList.get(position).getmTripshetSOAgentLatitude().equals("") && mTripSheetsList.get(position).getmTripshetSOAgentLongitude()!=null &&  !mTripSheetsList.get(position).getmTripshetSOAgentLongitude().equals(""))
        {
             distance = getDistanceBetweenLocations(currentLat, currentLong,
                    Double.parseDouble(mTripSheetsList.get(position).getmTripshetSOAgentLatitude())
                    , Double.parseDouble(mTripSheetsList.get(position).getmTripshetSOAgentLongitude()));
        }

        mHolder.mAgentCode.setText(mTripSheetsList.get(position).getmTripshetSOAgentCode());
        mHolder.mSODate.setText(mTripSheetsList.get(position).getmTripshetSODate());
        mHolder.mSOItemsCount.setText(mTripSheetsList.get(position).getmTripshetSOProductsCount());
        mHolder.mSOAgentName.setText(mTripSheetsList.get(position).getmTripshetSOAgentFirstName());
        mHolder.mSOOrderedValue.setText(mTripSheetsList.get(position).getmTripshetSOValue());
        mHolder.mSOAgentDistance.setText(distance);
        mHolder.mSOMapIconParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> productCodes = new ArrayList<String>();
                String s = mTripSheetsList.get(position).getmTripshetSOProductCode();
                try {
                    JSONArray prodJsonArray = new JSONArray(s);
                    for (int j = 0; j < prodJsonArray.length(); j++) {
                        if (!prodJsonArray.get(j).toString().equals("null")) {
                            productCodes.add(prodJsonArray.get(j).toString());
                        }
                    }
                    Intent i = new Intent(activity, TripsheetDelivery.class);
                    i.putExtra("tripsheetId", mTripSheetsList.get(position).getmTripshetSOTripId());
                    i.putExtra("agentId", mTripSheetsList.get(position).getmTripshetSOAgentId());
                    i.putExtra("agentCode", mTripSheetsList.get(position).getmTripshetSOAgentCode());
                    i.putExtra("agentSoId", mTripSheetsList.get(position).getmTripshetSOId());
                    i.putExtra("agentSoCode", mTripSheetsList.get(position).getmTripshetSOCode());
                    i.putExtra("agentName", mTripSheetsList.get(position).getmTripshetSOAgentFirstName() +
                            mTripSheetsList.get(position).getmTripshetSOAgentLastName());
                    activity.startActivity(i);
                    activity.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    public class ViewHolder {
        TextView mAgentCode;
        TextView mSODate;
        TextView mSOItemsCount;
        TextView mSOAgentName;
        TextView mSOOrderedValue;
        TextView mSOReceivedValue;
        TextView mSODueValue;
        Button mSOTakeOrder;
        LinearLayout mSOMapIconParent;
        TextView mSOAgentDistance;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mTripSheetsList.clear();
        if (charText.length() == 0) {
            mTripSheetsList.addAll(arraylist);
        } else {
            for (TripsheetSOList wp : arraylist) {
                if (wp.getmTripshetSOAgentCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mTripSheetsList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    // Method to calculate distance between two locations using HAVERSINE FORMULA
    private String getDistanceBetweenLocations(double sourceLat, double sourceLong, double destLat, double destLong) {
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
    }
}
