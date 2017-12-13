package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.AgentTakeOrderScreen;
import com.rightclickit.b2bsaleon.activities.TripSheetView;
import com.rightclickit.b2bsaleon.activities.TripsheetDelivery;
import com.rightclickit.b2bsaleon.beanclass.TripsheetSOList;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Sekhar Kuppa
 */

public class TripsheetsSOListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity activity;
    private ArrayList<TripsheetSOList> allSaleOrdersList, filteredSaleOrdersList;
    private String mTakeOrderPrivilege = "", mStockVerifyPrivilege = "",mhidePrevilige="";
    private boolean isTripSheetClosed;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
      Double orderTotal=0.0;
    public TripsheetsSOListAdapter(TripSheetView tripSheetView, TripSheetView tripSheetView1, ArrayList<TripsheetSOList> tripsSOList, String mTakeOrderPrivilege, boolean isTripSheetClosed, String mhidePrevilige) {
        this.activity = tripSheetView;
        this.mInflater = LayoutInflater.from(activity);
        this.mTakeOrderPrivilege = mTakeOrderPrivilege;
        this.isTripSheetClosed = isTripSheetClosed;
        this.mhidePrevilige = mhidePrevilige;

        this.allSaleOrdersList = tripsSOList;
        this.filteredSaleOrdersList = new ArrayList<>();
        this.filteredSaleOrdersList.addAll(allSaleOrdersList);
        this.mPreferences = new MMSharedPreferences(activity);
        this.mDBHelper = new DBHelper(activity);
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

    public ArrayList<TripsheetSOList> getData() {
        return allSaleOrdersList;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder mHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.tripsheets_so_list_custom, null);

            mHolder = new ViewHolder();
            mHolder.mAgentCode = (TextView) view.findViewById(R.id.tv_agent_code);
            mHolder.mSOCode = (TextView) view.findViewById(R.id.a_code);
            //mHolder.mSOItemsCount = (TextView) view.findViewById(R.id.tv_itemsQty);
            mHolder.mSOAgentName = (TextView) view.findViewById(R.id.companyName);
            mHolder.mSOOBamtValue = (TextView) view.findViewById(R.id.tv_ObamtValue);
            mHolder.mSOOrderedValue = (TextView) view.findViewById(R.id.tv_orderValue);
            mHolder.mSOReceivedValue = (TextView) view.findViewById(R.id.tv_receivedValue);
            mHolder.mSODueValue = (TextView) view.findViewById(R.id.tv_dueValue);
            mHolder.mSOTakeOrder = (Button) view.findViewById(R.id.btn_sale_ord1);
            mHolder.mSOTakeOrder.setVisibility(View.GONE);
            mHolder.mSOMapIconParent = (LinearLayout) view.findViewById(R.id.gotoCustomer);
            mHolder.mSOMapIconParent.setVisibility(View.GONE);

            mHolder.hideParent = (LinearLayout) view.findViewById(R.id.hide);
            mHolder.mSOAgentDistance = (TextView) view.findViewById(R.id.tv_km);
            mHolder.mEmptyLayout = (LinearLayout) view.findViewById(R.id.EmptyView);
            mHolder.so_status = (TextView) view.findViewById(R.id.tv_status);
            mHolder.mItemBgLayout = (LinearLayout) view.findViewById(R.id.dashboard_takeorder);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }

        if (position == allSaleOrdersList.size() - 1) {
            mHolder.mEmptyLayout.setVisibility(View.VISIBLE);
        } else {
            mHolder.mEmptyLayout.setVisibility(View.GONE);
        }

        if (mTakeOrderPrivilege.equals("list_view_takeorder")) {
            mHolder.mSOTakeOrder.setVisibility(View.VISIBLE);
        }


        if (mhidePrevilige.equals("list_view_delivery")) {
            mHolder.mSOMapIconParent.setVisibility(View.VISIBLE);
        }
        else {
            mHolder.hideParent.setVisibility(View.VISIBLE);
        }

        final TripsheetSOList currentSaleOrder = getItem(position);

        mHolder.mAgentCode.setText(currentSaleOrder.getmTripshetSOCode());


        if (currentSaleOrder.getmTripshetSOCode() != null) {
            if (currentSaleOrder.getmTripshetSOCode().length() == 0) {
                mHolder.mAgentCode.setText("-");
            } else {
                mHolder.mAgentCode.setText(currentSaleOrder.getmTripshetSOCode());
            }
        } else {
            mHolder.mAgentCode.setText("-");
        }
        mHolder.mSOCode.setText("(" + currentSaleOrder.getmTripshetSOAgentCode() + ")");
        //mHolder.mSOItemsCount.setText(currentSaleOrder.getmTripshetSOProductsCount());
        mHolder.mSOAgentName.setText(currentSaleOrder.getmTripshetSOAgentFirstName());
        mHolder.mSOOBamtValue.setText(Utility.getFormattedCurrency(Double.parseDouble(currentSaleOrder.getmTripshetSOOpAmount())));


        mHolder.mSOOrderedValue.setText(Utility.getFormattedCurrency(Double.parseDouble(currentSaleOrder.getmTripshetSOValue())));


        if (Double.parseDouble(currentSaleOrder.getmTripshetSOReceivedAmount()) == -0.00000001) {
            mHolder.mSOReceivedValue.setText(Utility.getFormattedCurrency(Double.parseDouble("0.0")));
        }else {
            mHolder.mSOReceivedValue.setText(Utility.getFormattedCurrency(Double.parseDouble(currentSaleOrder.getmTripshetSOReceivedAmount())));
        }
        mHolder.mSODueValue.setText(Utility.getFormattedCurrency(Double.parseDouble(currentSaleOrder.getmTripshetSODueAmount())));
        mHolder.mSOAgentDistance.setText(currentSaleOrder.getDistance() + " KM");

        if (isTripSheetClosed) {
            mHolder.so_status.setText("Closed");
            mHolder.mItemBgLayout.setBackgroundColor(Color.WHITE);
        } else if (Double.parseDouble(currentSaleOrder.getmTripshetSOReceivedAmount()) >= 0) {
            mHolder.so_status.setText("Delivered");
            mHolder.mItemBgLayout.setBackgroundColor(Color.parseColor("#d3d3d3"));
        } else {
            mHolder.so_status.setText("In Process");
            mHolder.mItemBgLayout.setBackgroundColor(Color.WHITE);
        }

        mHolder.mSOMapIconParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ArrayList<TripSheetDeliveriesBean> alltripsheetsDeliveries = mDBHelper.fetchAllTripsheetsDeliveriesListCombo(currentSaleOrder.getmTripshetSOTripId(),
//                        currentSaleOrder.getmTripshetSOAgentId(), currentSaleOrder.getmTripshetSOId());
//                if (alltripsheetsDeliveries.size() == 0) {
//                    Utility.isDeliveryFirstTime = false;
//                } else {
//                    Utility.isDeliveryFirstTime = true;
//                }
                ArrayList<String> productCodes = new ArrayList<String>();
                String s = currentSaleOrder.getmTripshetSOProductCode();
                try {
                    JSONArray prodJsonArray = new JSONArray(s);
                    for (int j = 0; j < prodJsonArray.length(); j++) {
                        if (!prodJsonArray.get(j).toString().equals("null")) {
                            productCodes.add(prodJsonArray.get(j).toString());
                        }
                    }

                    Intent i = new Intent(activity, TripsheetDelivery.class);
                    i.putExtra("tripsheetId", currentSaleOrder.getmTripshetSOTripId());
                    i.putExtra("agentId", currentSaleOrder.getmTripshetSOAgentId());
                    i.putExtra("agentCode", currentSaleOrder.getmTripshetSOAgentCode());
                    i.putExtra("agentSoId", currentSaleOrder.getmTripshetSOId());
                    i.putExtra("agentSoCode", currentSaleOrder.getmTripshetSOCode());
                    i.putExtra("agentSoDate", currentSaleOrder.getmTripshetSODate());
                    i.putExtra("agentName", currentSaleOrder.getmTripshetSOAgentFirstName());
                    //i.putExtra("agentName", currentSaleOrder.getmTripshetSOAgentFirstName() + currentSaleOrder.getmTripshetSOAgentLastName());
                    activity.startActivity(i);
                    activity.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        mHolder.mSOAgentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> productCodes = new ArrayList<String>();
                String s = currentSaleOrder.getmTripshetSOProductCode();
                try {
                    JSONArray prodJsonArray = new JSONArray(s);
                    for (int j = 0; j < prodJsonArray.length(); j++) {
                        if (!prodJsonArray.get(j).toString().equals("null")) {
                            productCodes.add(prodJsonArray.get(j).toString());
                        }
                    }

                    Intent i = new Intent(activity, TripsheetDelivery.class);
                    i.putExtra("tripsheetId", currentSaleOrder.getmTripshetSOTripId());
                    i.putExtra("agentId", currentSaleOrder.getmTripshetSOAgentId());
                    i.putExtra("agentCode", currentSaleOrder.getmTripshetSOAgentCode());
                    i.putExtra("agentSoId", currentSaleOrder.getmTripshetSOId());
                    i.putExtra("agentSoCode", currentSaleOrder.getmTripshetSOCode());
                    i.putExtra("agentSoDate", currentSaleOrder.getmTripshetSODate());
                    i.putExtra("agentName", currentSaleOrder.getmTripshetSOAgentFirstName());
                    //i.putExtra("agentName", currentSaleOrder.getmTripshetSOAgentFirstName() + currentSaleOrder.getmTripshetSOAgentLastName());
                    activity.startActivity(i);
                    activity.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        mHolder.mSOTakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreferences.putString("agentName", currentSaleOrder.getmTripshetSOAgentFirstName());
                mPreferences.putString("agentId", currentSaleOrder.getmTripshetSOAgentId());

                mPreferences.putString("agentCode", currentSaleOrder.getmTripshetSOAgentCode());
               // mPreferences.putString("enquiryid", currentSaleOrder.getmtr());
                Intent i = new Intent(activity, AgentTakeOrderScreen.class);
                i.putExtra("tripsheetId", currentSaleOrder.getmTripshetSOTripId());
                i.putExtra("From", "Tripsheet");
                activity.startActivity(i);
                activity.finish();
            }
        });

        return view;
    }

    public class ViewHolder {
        TextView mAgentCode;
        TextView mSOCode;
        TextView mSOItemsCount;
        TextView mSOAgentName;
        TextView mSOOBamtValue;
        TextView mSOOrderedValue;
        TextView mSOReceivedValue;
        TextView mSODueValue;
        Button mSOTakeOrder;
        LinearLayout mSOMapIconParent,hideParent;
        TextView mSOAgentDistance;
        LinearLayout mEmptyLayout;
        TextView so_status;
        LinearLayout mItemBgLayout;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        filteredSaleOrdersList.clear();

        if (charText.length() == 0) {
            filteredSaleOrdersList.addAll(allSaleOrdersList);
        } else {
            for (TripsheetSOList wp : allSaleOrdersList) {
                if (wp.getmTripshetSOAgentCode().toLowerCase(Locale.getDefault()).contains(charText)
                        || wp.getmTripshetSOAgentFirstName().toLowerCase(Locale.getDefault()).contains(charText)
                        || wp.getmTripshetSOCode().toLowerCase(Locale.getDefault()).contains(charText)) {
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
