package com.rightclickit.b2bsaleon.models;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.activities.SettingsActivity;
import com.rightclickit.b2bsaleon.activities.TripSheetsActivity;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsList;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.customviews.CustomProgressDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.OnAsyncRequestCompleteListener;
import com.rightclickit.b2bsaleon.util.AsyncRequest;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Sekhar Kuppa.
 */

public class TripsheetsModel implements OnAsyncRequestCompleteListener {

    public static final String TAG = SettingsActivity.class.getSimpleName();

    private Context context;
    private TripSheetsActivity activity;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
    private TextView mNotripsText;
    private ArrayList<TripsheetsList> mTripsheetsList = new ArrayList<TripsheetsList>();
    private ArrayList<TripsheetsList> mTripsheetsStockList = new ArrayList<TripsheetsList>();
    private ArrayList<String> mRouteCodesList = new ArrayList<String>();

    private String currentDate;
    private int calledApi = 0;

    public TripsheetsModel(Context context, TripSheetsActivity activity) {
        this.context = context;
        this.activity = activity;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = df.format(cal.getTime());

    }

    /**
     * Method to get tripsheets list
     *
     * @param mNoTripsFoundText
     */
    public void getTripsheetsList(TextView mNoTripsFoundText) {
        try {
            String routeCode = "";
            mNotripsText = mNoTripsFoundText;
            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                if (mTripsheetsList.size() > 0) {
                    mTripsheetsList.clear();
                }
                calledApi = 0;
                HashMap<String, String> userMapData = mDBHelper.getUsersData();
                JSONObject routesJob = new JSONObject(userMapData.get("route_ids").toString());
                JSONArray routesArray = routesJob.getJSONArray("routeArray");
                for (int i = 0; i < routesArray.length(); i++) {
                    routeCode = "";
                    routeCode = mDBHelper.getRouteCodeByRouteId(routesArray.get(i).toString());
                    mRouteCodesList.add(routeCode);
                }
                System.out.println("ROUTE CODES LIST:: " + mRouteCodesList.size());
                String URL = String.format("%s%s%s", Constants.MAIN_URL, Constants.SYNC_TRIPSHEETS_PORT, Constants.GET_TRIPSHEETS_LIST);

                JSONObject params1 = new JSONObject();
                if (mRouteCodesList.size() > 0) {
                    for (int k = 0; k < mRouteCodesList.size(); k++) {
                        params1.put("route_codes[" + k + "]", mRouteCodesList.get(k).toString());
                    }
                }
                params1.put("date", currentDate);

                AsyncRequest getTripsListRequest = new AsyncRequest(context, this, URL, AsyncRequest.MethodType.POST, params1);
                getTripsListRequest.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get tripsheets stock list
     *
     * @param mNoTripsSheetsStockFoundText
     */
    public void getTripsheetsStockList(TextView mNoTripsSheetsStockFoundText, String mTripSheetId) {
        try {
            mNotripsText = mNoTripsSheetsStockFoundText;
            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                if (mTripsheetsStockList.size() > 0) {
                    mTripsheetsStockList.clear();
                }
                calledApi = 1;
                String URL = String.format("%s%s%s", Constants.MAIN_URL, Constants.SYNC_TRIPSHEETS_PORT, Constants.GET_TRIPSHEETS_STOCK_LIST);

                JSONObject params1 = new JSONObject();
                params1.put("trip_id", mTripSheetId);


                AsyncRequest getTripsListRequest = new AsyncRequest(context, this, URL, AsyncRequest.MethodType.POST, params1);
                getTripsListRequest.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void asyncResponse(String response, Constants.RequestCode requestCode) {
        try {
            CustomProgressDialog.hideProgressDialog();
            System.out.println("========= response = " + response);
            switch (calledApi) {
                case 0:
                    JSONArray resArray = new JSONArray(response);
                    int len = resArray.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject jb = resArray.getJSONObject(i);

                        TripsheetsList tripsheetsListBean = new TripsheetsList();

                        tripsheetsListBean.setmTripshhetId(jb.getString("_id"));
                        tripsheetsListBean.setmTripshhetCode(jb.getString("code"));
                        tripsheetsListBean.setmTripshhetDate(jb.getString("date"));
                        tripsheetsListBean.setmTripshhetStatus(jb.getString("status"));
                        tripsheetsListBean.setmTripshhetOBAmount(jb.getString("ob_amt"));
                        tripsheetsListBean.setmTripshhetOrderedAmount(jb.getString("order_amt"));
                        tripsheetsListBean.setmTripshhetReceivedAmount(jb.getString("received_amt"));
                        tripsheetsListBean.setmTripshhetDueAmount("0");
                        tripsheetsListBean.setmTripshhetRouteCode("route_code");
                        tripsheetsListBean.setmTripshhetSalesMenCode("salesman_code");
                        tripsheetsListBean.setmTripshhetVehicleNumber("vehicle_no");
                        tripsheetsListBean.setmTripshhetTrasnsporterName("transporter");

                        mTripsheetsList.add(tripsheetsListBean);
                    }
                    synchronized (this) {
                        if (mTripsheetsList.size() > 0) {
                            mDBHelper.insertTripsheetsListData(mTripsheetsList);
                        }
                    }
                    synchronized (this) {
                        if (mTripsheetsList.size() > 0) {
                            activity.loadTripsData(mTripsheetsList);
                        } else {
                            mNotripsText.setText("No Tripsheets found.");
                        }
                    }
                    break;
                case 1:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
