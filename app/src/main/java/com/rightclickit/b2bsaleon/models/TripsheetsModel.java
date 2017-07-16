package com.rightclickit.b2bsaleon.models;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.activities.SettingsActivity;
import com.rightclickit.b2bsaleon.activities.TripSheetStock;
import com.rightclickit.b2bsaleon.activities.TripSheetsActivity;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsList;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.customviews.CustomProgressDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.OnAsyncRequestCompleteListener;
import com.rightclickit.b2bsaleon.util.AsyncRequest;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Sekhar Kuppa.
 */

public class TripsheetsModel implements OnAsyncRequestCompleteListener {

    public static final String TAG = SettingsActivity.class.getSimpleName();

    private Context context;
    private TripSheetsActivity activity;
    private TripSheetStock activity1;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
    private TextView mNotripsText;
    private ArrayList<TripsheetsList> mTripsheetsList = new ArrayList<TripsheetsList>();
    private ArrayList<TripsheetsStockList> mTripsheetsStockList = new ArrayList<TripsheetsStockList>();
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

    public TripsheetsModel(TripSheetStock context, TripSheetStock tripSheetStock) {
        this.context = context;
        this.activity1 = tripSheetStock;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);

        // Calendar cal = Calendar.getInstance();
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = Utility.formatDate(new Date(), "yyyy-MM-dd");
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
                JSONArray jar = new JSONArray(mRouteCodesList);

                JSONObject params1 = new JSONObject();
                params1.put("route_codes", jar);
                params1.put("date", "2017-07-12");

                AsyncRequest getTripsListRequest = new AsyncRequest(context, this, URL, AsyncRequest.MethodType.POST, params1);
                getTripsListRequest.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to get tripsheets stock list
     */
    public void getTripsheetsStockList(String mTripSheetId) {
        try {
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
                        if (!jb.getString("order_amt").trim().equals("")) {
                            tripsheetsListBean.setmTripshhetOrderedAmount(jb.getString("order_amt"));
                        } else {
                            tripsheetsListBean.setmTripshhetOrderedAmount("0");
                        }
                        if (!jb.getString("received_amt").trim().equals("")) {
                            tripsheetsListBean.setmTripshhetReceivedAmount(jb.getString("received_amt"));
                        } else {
                            tripsheetsListBean.setmTripshhetReceivedAmount("0");
                        }
                        int dueAmt = Integer.parseInt(tripsheetsListBean.getmTripshhetOrderedAmount()) - Integer.parseInt(tripsheetsListBean.getmTripshhetReceivedAmount());
                        tripsheetsListBean.setmTripshhetDueAmount(String.valueOf(dueAmt));
                        tripsheetsListBean.setmTripshhetRouteCode("route_code");
                        tripsheetsListBean.setmTripshhetSalesMenCode("salesman_code");
                        tripsheetsListBean.setmTripshhetVehicleNumber("vehicle_no");
                        tripsheetsListBean.setmTripshhetTrasnsporterName("transporter");
                        tripsheetsListBean.setmTripshhetVerifyStatus("0");

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
                    JSONArray stockArray = new JSONArray(response);
                    int stockLen = stockArray.length();
                    JSONArray productCodesArray, orderQuantityArray;
                    for (int i = 0; i < stockLen; i++) {
                        JSONObject jb = stockArray.getJSONObject(i);

                        productCodesArray = jb.getJSONArray("product_codes");
                        orderQuantityArray = jb.getJSONArray("order_qty");

                        int productsLen = productCodesArray.length();
                        if (productsLen > 0) {
                            for (int j = 0; j < productsLen; j++) {
                                TripsheetsStockList tripStockBean = new TripsheetsStockList();

                                tripStockBean.setmTripsheetStockTripsheetId(jb.getString("trip_id"));
                                tripStockBean.setmTripsheetStockId(jb.getString("_id"));
                                tripStockBean.setmTripsheetStockProductId("");
                                tripStockBean.setmTripsheetStockProductCode(productCodesArray.get(j).toString());
                                tripStockBean.setmTripsheetStockProductName("");
                                tripStockBean.setmTripsheetStockProductOrderQuantity(orderQuantityArray.get(j).toString());
                                tripStockBean.setmTripsheetStockDispatchBy("");
                                tripStockBean.setmTripsheetStockDispatchDate("");
                                tripStockBean.setmTripsheetStockDispatchQuantity("");
                                tripStockBean.setmTripsheetStockVerifiedDate("");
                                tripStockBean.setmTripsheetStockVerifiedQuantity("");
                                tripStockBean.setmTripsheetStockVerifyBy("");

                                mTripsheetsStockList.add(tripStockBean);
                            }
                        }
                    }
                    synchronized (this) {
                        if (mTripsheetsStockList.size() > 0) {
                            mDBHelper.insertTripsheetsStockListData(mTripsheetsStockList);
                        }
                    }
                    synchronized (this) {
                        if (mTripsheetsStockList.size() > 0) {
                            activity1.loadTripsData(mTripsheetsStockList);
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
