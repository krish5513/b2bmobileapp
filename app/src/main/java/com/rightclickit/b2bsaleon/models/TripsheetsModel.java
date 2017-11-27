package com.rightclickit.b2bsaleon.models;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.activities.RouteStock;
import com.rightclickit.b2bsaleon.activities.SettingsActivity;
import com.rightclickit.b2bsaleon.activities.TripSheetStock;
import com.rightclickit.b2bsaleon.activities.TripSheetView;
import com.rightclickit.b2bsaleon.activities.TripSheetViewPreview;
import com.rightclickit.b2bsaleon.activities.TripSheetsActivity;
import com.rightclickit.b2bsaleon.activities.TripsheetStockPreview;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TripsheetSOList;
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
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Sekhar Kuppa.
 */

public class TripsheetsModel implements OnAsyncRequestCompleteListener {

    public static final String TAG = SettingsActivity.class.getSimpleName();

    private Context context;
    private TripSheetsActivity activity;
    private TripSheetStock activity1;
    private TripSheetView activity2;
    private TripSheetViewPreview activity4;
    private TripsheetStockPreview activity3;
    private RouteStock activity5;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
    private TextView mNotripsText;
    private ArrayList<TripsheetsList> mTripsheetsList = new ArrayList<TripsheetsList>();
    private ArrayList<TripsheetsStockList> mTripsheetsStockList = new ArrayList<TripsheetsStockList>();
    private ArrayList<TripsheetSOList> mTripsheetsSOList = new ArrayList<TripsheetSOList>();
    private ArrayList<String> mRouteCodesList = new ArrayList<String>();

    private String currentDate;
    private int calledApi = 0;
    private boolean isRouteStockCalled;

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
        this.isRouteStockCalled = false;
        // Calendar cal = Calendar.getInstance();
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = Utility.formatDate(new Date(), "yyyy-MM-dd");
    }

    public TripsheetsModel(TripsheetStockPreview context, TripsheetStockPreview tripSheetStock) {
        this.context = context;
        this.activity3 = tripSheetStock;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);

        // Calendar cal = Calendar.getInstance();
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = Utility.formatDate(new Date(), "yyyy-MM-dd");
    }

    public TripsheetsModel(RouteStock context, RouteStock routeStock) {
        this.context = context;
        this.activity5 = routeStock;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);
        this.isRouteStockCalled = true;
        // Calendar cal = Calendar.getInstance();
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = Utility.formatDate(new Date(), "yyyy-MM-dd");
    }

    public TripsheetsModel(TripSheetView tripSheetView, TripSheetView tripSheetView1) {
        this.context = tripSheetView;
        this.activity2 = tripSheetView1;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);

        // Calendar cal = Calendar.getInstance();
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = Utility.formatDate(new Date(), "yyyy-MM-dd");
    }
  /*  public TripsheetsModel(TripSheetViewPreview context, TripSheetViewPreview tripSheetStock) {
        this.context = context;
        this.activity4 = tripSheetStock;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);

        // Calendar cal = Calendar.getInstance();
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = Utility.formatDate(new Date(), "yyyy-MM-dd");
    }*/

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
                // System.out.println("ROUTE CODES LIST:: " + mRouteCodesList.size());
                String URL = String.format("%s%s%s", Constants.MAIN_URL, Constants.SYNC_TRIPSHEETS_PORT, Constants.GET_TRIPSHEETS_LIST);
                JSONArray jar = new JSONArray(mRouteCodesList);

                JSONObject params1 = new JSONObject();
                params1.put("route_codes", jar);
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

    /**
     * Method to get tripsheets so list
     */
    public void getTripsheetsSoList(String mTripSheetId) {
        try {
            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                if (mTripsheetsSOList.size() > 0) {
                    mTripsheetsSOList.clear();
                }
                calledApi = 2;
                String URL = String.format("%s%s%s", Constants.MAIN_URL, Constants.SYNC_TRIPSHEETS_PORT, Constants.GET_TRIPSHEETS_SO_LIST);

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
                    //  JSONObject responseObj = new JSONObject(response);
                    // if (responseObj.getInt("result_status") == 0) {
                    //   mNotripsText.setText("No Trip Sheets Found.");
                    // } else {
                    JSONArray resArray = new JSONArray(response);
                    int len = resArray.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject jb = resArray.getJSONObject(i);

                        TripsheetsList tripsheetsListBean = new TripsheetsList();

                        tripsheetsListBean.setmTripshhetId(jb.getString("_id"));
                        tripsheetsListBean.setmTripshhetCode(jb.getString("code"));
                        tripsheetsListBean.setMy_Id(jb.getString("my_id"));
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

                        Double dueAmt = Double.parseDouble(tripsheetsListBean.getmTripshhetOrderedAmount().replace(",", "")) - Double.parseDouble(tripsheetsListBean.getmTripshhetReceivedAmount().replace(",", ""));
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
                        activity.loadTripSheetsData();
                    }

                    break;

                case 1:
                    HashMap<String, String> delMapList = new HashMap<String, String>();
                    HashMap<String, String> retMapList = new HashMap<String, String>();
                    JSONArray stockArray = new JSONArray(response);
                    int stockLen = stockArray.length();

                    JSONArray productCodesArray = null, orderQuantityArray = null, productsInfoArray = null, leakQuantityArray = null,
                            verifyQuantityArray = null, dispatchQuantity = null, otherQuantity = null, routeReturnQuantity = null;

                    for (int i = 0; i < stockLen; i++) {

                        JSONObject jb = stockArray.getJSONObject(i);

                        if (jb.get("productdata") instanceof JSONArray) {
                            productsInfoArray = jb.getJSONArray("productdata");
                        }

                        if (jb.get("product_codes") instanceof JSONArray) {
                            productCodesArray = jb.getJSONArray("product_codes");
                        }

                        if (jb.get("order_qty") instanceof JSONArray) {
                            orderQuantityArray = jb.getJSONArray("order_qty");
                        }

                        if (jb.get("route_leakage_qty") instanceof JSONArray) {
                            leakQuantityArray = jb.getJSONArray("route_leakage_qty");
                        }

                        if (jb.get("verify_qty") instanceof JSONArray) {
                            verifyQuantityArray = jb.getJSONArray("verify_qty");
                        }
                        if (jb.get("dispatch_qty") instanceof JSONArray) {
                            dispatchQuantity = jb.getJSONArray("dispatch_qty");
                        }
                        if (jb.get("other_qty") instanceof JSONArray) {
                            otherQuantity = jb.getJSONArray("other_qty");
                        }
                        if (jb.get("free_qty") instanceof JSONArray) {
                            routeReturnQuantity = jb.getJSONArray("free_qty");
                        }
                        if (jb.has("delivery_res")) {
                            if (jb.get("delivery_res") instanceof JSONObject) {
                                JSONObject delResponse = jb.getJSONObject("delivery_res");
                                Iterator x = delResponse.keys();
                                JSONArray jsonArray = new JSONArray();
                                while (x.hasNext()) {
                                    String key = (String) x.next();
                                    delMapList.put(key, delResponse.get(key).toString());
                                }
                            }
                        }
                        if (jb.has("return_res")) {
                            if (jb.get("return_res") instanceof JSONObject) {
                                JSONObject retResponse = jb.getJSONObject("return_res");
                                Iterator x = retResponse.keys();
                                JSONArray jsonArray = new JSONArray();
                                while (x.hasNext()) {
                                    String key = (String) x.next();
                                    retMapList.put(key, retResponse.get(key).toString());
                                }
                            }
                        }

                        int noOfProducts = 0;
                        if (productCodesArray != null) {
                            if (productCodesArray.length() > 0) {
                                // If products info array found.
                                noOfProducts = productCodesArray.length();
                                Log.i("length 1..." + noOfProducts, "length 2..." + productsInfoArray.length());
                                for (int j = 0; j < productsInfoArray.length(); j++) {//productCodesArray
                                    JSONObject jj = productsInfoArray.getJSONObject(j);
                                    // Checking weather product code is null or not
                                    TripsheetsStockList tripStockBean = new TripsheetsStockList();

                                    tripStockBean.setmTripsheetStockTripsheetId(jb.getString("trip_id"));
                                    tripStockBean.setmTripsheetStockId(jb.getString("_id"));
                                    tripStockBean.setmTripsheetStockProductCode(productCodesArray.get(j).toString());
                                    if (jj.has("_id")) {
                                        tripStockBean.setmTripsheetStockProductId(jj.getString("_id"));
                                    }
                                    if (jj.has("name")) {
                                        tripStockBean.setmTripsheetStockProductName(jj.getString("name"));
                                    }
                                    if (orderQuantityArray != null) {
                                        tripStockBean.setmTripsheetStockProductOrderQuantity(orderQuantityArray.get(j).toString());
                                    } else {
                                        tripStockBean.setmTripsheetStockProductOrderQuantity("");
                                    }
//                                    tripStockBean.setmTripsheetStockDispatchBy("");
//                                    tripStockBean.setmTripsheetStockDispatchDate("");
//                                    tripStockBean.setmTripsheetStockDispatchQuantity("");
//                                    tripStockBean.setmTripsheetStockVerifiedDate("");
//                                    if (verifyQuantityArray != null) {
//                                        if (isRouteStockCalled) {
//                                            tripStockBean.setmTripsheetStockVerifiedQuantity(verifyQuantityArray.get(j).toString());
//                                        } else {
//                                            tripStockBean.setmTripsheetStockVerifiedQuantity("");
//                                        }
//                                    } else {
//                                        tripStockBean.setmTripsheetStockVerifiedQuantity("");
//                                    }
//
//                                    tripStockBean.setmTripsheetStockVerifyBy("");

                                    // DISPATCH QUANTITY STUFF
                                    if (dispatchQuantity instanceof JSONArray) {
                                        //System.out.println("dspatch::: " + dispatchQuantity);
                                        //System.out.println("dspatch length::: " + dispatchQuantity.length());
                                        if (dispatchQuantity.length() > 0) {
                                            tripStockBean.setmTripsheetStockDispatchQuantity(dispatchQuantity.get(j).toString());
                                            tripStockBean.setmTripsheetStockDispatchBy(jb.getString("dispatch_by"));
                                            tripStockBean.setmTripsheetStockDispatchDate(jb.getString("dispatch_date"));
                                            tripStockBean.setIsStockDispatched(1);

                                            mDBHelper.updateTripSheetStockDispatchList(tripStockBean);

                                            mDBHelper.updateTripSheetStockTable(tripStockBean.getmTripsheetStockId(), "dispatch");
                                        } else {
                                            tripStockBean.setmTripsheetStockDispatchBy("");
                                            tripStockBean.setmTripsheetStockDispatchDate("");
                                            tripStockBean.setmTripsheetStockDispatchQuantity("");
                                            tripStockBean.setIsStockDispatched(0);
                                        }
                                    } else {
                                        ///System.out.println("IN ELSEEEEEE");
                                        tripStockBean.setmTripsheetStockDispatchBy("");
                                        tripStockBean.setmTripsheetStockDispatchDate("");
                                        tripStockBean.setmTripsheetStockDispatchQuantity("");
                                        tripStockBean.setIsStockDispatched(0);
                                    }


                                    // VERIFY QUANTITY STUFF
                                    if (verifyQuantityArray instanceof JSONArray) {
                                        if (verifyQuantityArray.length() > 0) {
//                                        if (isRouteStockCalled) {
//                                            tripStockBean.setmTripsheetStockVerifiedQuantity(verifyQuantityArray.get(j).toString());
//                                            tripStockBean.setmTripsheetStockVerifyBy(jb.getString("verify_by"));
//                                            tripStockBean.setmTripsheetStockVerifiedDate(jb.getString("verify_date"));
//                                        } else {
//                                            tripStockBean.setmTripsheetStockVerifiedQuantity("");
//                                            tripStockBean.setmTripsheetStockVerifyBy("");
//                                            tripStockBean.setmTripsheetStockVerifiedDate("");
//                                        }
                                            tripStockBean.setmTripsheetStockVerifiedQuantity(verifyQuantityArray.get(j).toString());
                                            tripStockBean.setmTripsheetStockVerifyBy(jb.getString("verify_by"));
                                            tripStockBean.setmTripsheetStockVerifiedDate(jb.getString("verify_date"));
                                            tripStockBean.setIsStockVerified(1);

                                            double verq = 0, ordq = 0;
                                            if (tripStockBean.getmTripsheetStockVerifiedQuantity().equals("")) {
                                                verq = 0;
                                            } else {
                                                verq = Double.parseDouble(tripStockBean.getmTripsheetStockVerifiedQuantity());
                                            }
                                            if (tripStockBean.getmTripsheetStockProductOrderQuantity().equals("")) {
                                                ordq = 0;
                                            } else {
                                                ordq = Double.parseDouble(tripStockBean.getmTripsheetStockProductOrderQuantity());
                                            }
                                            double extraQuantity = verq - ordq;
                                            if (extraQuantity > 0) {
                                                tripStockBean.setInStockQuantity(tripStockBean.getmTripsheetStockProductOrderQuantity());
                                                tripStockBean.setExtraQuantity(String.valueOf(extraQuantity));
                                            } else {
                                                tripStockBean.setInStockQuantity(tripStockBean.getmTripsheetStockVerifiedQuantity());
                                                tripStockBean.setExtraQuantity(String.valueOf(0));
                                            }

                                            mDBHelper.updateTripSheetStockVerifyList(tripStockBean);

                                            mDBHelper.updateTripSheetStockTable(tripStockBean.getmTripsheetStockId(), "verify");

                                            mDBHelper.updateTripSheetStockVerifyStatus(tripStockBean.getmTripsheetStockTripsheetId());

                                        } else {
                                            tripStockBean.setmTripsheetStockVerifiedQuantity("");
                                            tripStockBean.setmTripsheetStockVerifyBy("");
                                            tripStockBean.setmTripsheetStockVerifiedDate("");
                                            tripStockBean.setIsStockVerified(0);
                                            tripStockBean.setExtraQuantity(String.valueOf(0));
                                            tripStockBean.setInStockQuantity(String.valueOf(0));
                                        }
                                    } else {
                                        tripStockBean.setmTripsheetStockVerifiedQuantity("");
                                        tripStockBean.setmTripsheetStockVerifyBy("");
                                        tripStockBean.setmTripsheetStockVerifiedDate("");
                                        tripStockBean.setIsStockVerified(0);
                                        tripStockBean.setExtraQuantity(String.valueOf(0));
                                        tripStockBean.setInStockQuantity(String.valueOf(0));
                                    }


                                    // Added by Sekhar for close trip functionality
                                    //tripStockBean.setmDeliveryQuantity("");
                                    //tripStockBean.setmReturnQuantity("");
                                    //tripStockBean.setmCBQuantity("");
                                    if (leakQuantityArray instanceof JSONArray) {
                                        if (leakQuantityArray.length() > 0) {
                                            tripStockBean.setmLeakQuantity(leakQuantityArray.get(j).toString());
                                        }
                                    }
                                    //tripStockBean.setmOtherQuantity("");

                                    // OTHER QUANTITY
                                    if (otherQuantity instanceof JSONArray) {
                                        if (otherQuantity.length() > 0) {
                                            tripStockBean.setmOtherQuantity(otherQuantity.get(j).toString());
                                        }
                                    }

                                    // ROUTE RETURN QUANTITY
                                    if (routeReturnQuantity instanceof JSONArray) {
                                        if (routeReturnQuantity.length() > 0) {
                                            tripStockBean.setmRouteReturnQuantity(routeReturnQuantity.get(j).toString());
                                        }
                                    }

                                    // DEL QUANTITY
                                    if (delMapList.get(productCodesArray.get(j).toString()) != null) {
                                        String deq = delMapList.get(productCodesArray.get(j).toString());
                                        if (!deq.equals("null")) {
                                            tripStockBean.setmDeliveryQuantity(deq);
                                        } else {
                                            tripStockBean.setmDeliveryQuantity("0.0");
                                        }
                                    } else {
                                        tripStockBean.setmDeliveryQuantity("0.0");
                                    }

                                    // RETURN QUANTITY
                                    if (retMapList.get(productCodesArray.get(j).toString()) != null) {
                                        String req = retMapList.get(productCodesArray.get(j).toString());
                                        if (!req.equals("null")) {
                                            tripStockBean.setmReturnQuantity(req);
                                        } else {
                                            tripStockBean.setmReturnQuantity("0.0");
                                        }
                                    } else {
                                        tripStockBean.setmReturnQuantity("0.0");
                                    }

                                    mTripsheetsStockList.add(tripStockBean);
                                }
                            } else {
                                // If products info array not found.
                                if (productCodesArray != null) {
                                    noOfProducts = productCodesArray.length();
                                    for (int j = 0; j < noOfProducts; j++) {
                                        // Checking weather product code is null or not
                                        if (productCodesArray.get(j).toString() != "null") {
                                            TripsheetsStockList tripStockBean = new TripsheetsStockList();

                                            tripStockBean.setmTripsheetStockTripsheetId(jb.getString("trip_id"));
                                            tripStockBean.setmTripsheetStockId(jb.getString("_id"));
                                            tripStockBean.setmTripsheetStockProductCode(productCodesArray.get(j).toString());

                                            ProductsBean productsDetails = mDBHelper.fetchProductDetailsByProductCode(productCodesArray.get(j).toString());
                                            if (productsDetails != null) {
                                                tripStockBean.setmTripsheetStockProductId(productsDetails.getProductId());
                                                tripStockBean.setmTripsheetStockProductName(productsDetails.getProductTitle());
                                            }

                                            if (orderQuantityArray != null) {
                                                tripStockBean.setmTripsheetStockProductOrderQuantity(orderQuantityArray.get(j).toString());
                                            } else {
                                                tripStockBean.setmTripsheetStockProductOrderQuantity("");
                                            }
//                                            tripStockBean.setmTripsheetStockDispatchBy("");
//                                            tripStockBean.setmTripsheetStockDispatchDate("");
//                                            tripStockBean.setmTripsheetStockDispatchQuantity("");
//                                            tripStockBean.setmTripsheetStockVerifiedDate("");
//                                            if (verifyQuantityArray != null) {
//                                                if (isRouteStockCalled) {
//                                                    tripStockBean.setmTripsheetStockVerifiedQuantity(verifyQuantityArray.get(j).toString());
//                                                } else {
//                                                    tripStockBean.setmTripsheetStockVerifiedQuantity("");
//                                                }
//                                            } else {
//                                                tripStockBean.setmTripsheetStockVerifiedQuantity("");
//                                            }
//                                            tripStockBean.setmTripsheetStockVerifyBy("");

                                            // DISPATCH QUANTITY STUFF
                                            if (dispatchQuantity.length() > 0) {
                                                tripStockBean.setmTripsheetStockDispatchQuantity(dispatchQuantity.get(j).toString());
                                                tripStockBean.setmTripsheetStockDispatchBy(jb.getString("dispatch_by"));
                                                tripStockBean.setmTripsheetStockDispatchDate(jb.getString("dispatch_date"));
                                                tripStockBean.setIsStockDispatched(1);

                                                mDBHelper.updateTripSheetStockDispatchList(tripStockBean);

                                                mDBHelper.updateTripSheetStockTable(tripStockBean.getmTripsheetStockId(), "dispatch");
                                            } else {
                                                tripStockBean.setmTripsheetStockDispatchBy("");
                                                tripStockBean.setmTripsheetStockDispatchDate("");
                                                tripStockBean.setmTripsheetStockDispatchQuantity("");
                                                tripStockBean.setIsStockDispatched(0);
                                            }

                                            // VERIFY QUANTITY STUFF
                                            if (verifyQuantityArray.length() > 0) {
//                                        if (isRouteStockCalled) {
//                                            tripStockBean.setmTripsheetStockVerifiedQuantity(verifyQuantityArray.get(j).toString());
//                                            tripStockBean.setmTripsheetStockVerifyBy(jb.getString("verify_by"));
//                                            tripStockBean.setmTripsheetStockVerifiedDate(jb.getString("verify_date"));
//                                        } else {
//                                            tripStockBean.setmTripsheetStockVerifiedQuantity("");
//                                            tripStockBean.setmTripsheetStockVerifyBy("");
//                                            tripStockBean.setmTripsheetStockVerifiedDate("");
//                                        }
                                                tripStockBean.setmTripsheetStockVerifiedQuantity(verifyQuantityArray.get(j).toString());
                                                tripStockBean.setmTripsheetStockVerifyBy(jb.getString("verify_by"));
                                                tripStockBean.setmTripsheetStockVerifiedDate(jb.getString("verify_date"));
                                                tripStockBean.setIsStockVerified(1);

                                                double verq = 0, ordq = 0;
                                                if (tripStockBean.getmTripsheetStockVerifiedQuantity().equals("")) {
                                                    verq = 0;
                                                } else {
                                                    verq = Double.parseDouble(tripStockBean.getmTripsheetStockVerifiedQuantity());
                                                }
                                                if (tripStockBean.getmTripsheetStockProductOrderQuantity().equals("")) {
                                                    ordq = 0;
                                                } else {
                                                    ordq = Double.parseDouble(tripStockBean.getmTripsheetStockProductOrderQuantity());
                                                }
                                                double extraQuantity = verq - ordq;
                                                if (extraQuantity > 0) {
                                                    tripStockBean.setInStockQuantity(tripStockBean.getmTripsheetStockProductOrderQuantity());
                                                    tripStockBean.setExtraQuantity(String.valueOf(extraQuantity));
                                                } else {
                                                    tripStockBean.setInStockQuantity(tripStockBean.getmTripsheetStockVerifiedQuantity());
                                                    tripStockBean.setExtraQuantity(String.valueOf(0));
                                                }
                                            } else {
                                                tripStockBean.setmTripsheetStockVerifiedQuantity("");
                                                tripStockBean.setmTripsheetStockVerifyBy("");
                                                tripStockBean.setmTripsheetStockVerifiedDate("");
                                                tripStockBean.setIsStockVerified(0);
                                                tripStockBean.setExtraQuantity(String.valueOf(0));
                                                tripStockBean.setInStockQuantity(String.valueOf(0));
                                            }

                                            // Added by Sekhar for close trip functionality
                                            //tripStockBean.setmDeliveryQuantity("");
                                            //tripStockBean.setmReturnQuantity("");
                                            // tripStockBean.setmCBQuantity("");
                                            if (leakQuantityArray.length() > 0) {
                                                if (leakQuantityArray.length() > 0) {
                                                    tripStockBean.setmLeakQuantity(leakQuantityArray.get(j).toString());
                                                }
                                            }
                                            //tripStockBean.setmOtherQuantity("");

                                            // OTHER QUANTITY
                                            if (otherQuantity instanceof JSONArray) {
                                                if (otherQuantity.length() > 0) {
                                                    tripStockBean.setmOtherQuantity(otherQuantity.get(j).toString());
                                                }
                                            }

                                            // ROUTE RETURN QUANTITY
                                            if (routeReturnQuantity instanceof JSONArray) {
                                                if (routeReturnQuantity.length() > 0) {
                                                    tripStockBean.setmRouteReturnQuantity(routeReturnQuantity.get(j).toString());
                                                }
                                            }

                                            // DEL QUANTITY
                                            if (delMapList.get(productCodesArray.get(j).toString()) != null) {
                                                String deq = delMapList.get(productCodesArray.get(j).toString());
                                                if (!deq.equals("null")) {
                                                    tripStockBean.setmDeliveryQuantity(deq);
                                                } else {
                                                    tripStockBean.setmDeliveryQuantity("0.0");
                                                }
                                            } else {
                                                tripStockBean.setmDeliveryQuantity("0.0");
                                            }

                                            // RETURN QUANTITY
                                            if (retMapList.get(productCodesArray.get(j).toString()) != null) {
                                                String req = retMapList.get(productCodesArray.get(j).toString());
                                                if (!req.equals("null")) {
                                                    tripStockBean.setmReturnQuantity(req);
                                                } else {
                                                    tripStockBean.setmReturnQuantity("0.0");
                                                }
                                            } else {
                                                tripStockBean.setmReturnQuantity("0.0");
                                            }

                                            mTripsheetsStockList.add(tripStockBean);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    synchronized (this) {
                        //System.out.println("TRIPSHEETS STOCK SIZE::: " + mTripsheetsStockList.size());
                        if (mTripsheetsStockList.size() > 0) {
                            mDBHelper.insertTripsheetsStockListData(mTripsheetsStockList);
                        }
                    }

                    synchronized (this) {
                        if (mTripsheetsStockList.size() > 0) {
                            if (isRouteStockCalled) {
                                activity5.loadTripsData(mTripsheetsStockList);
                            } else {
                                activity1.loadTripsData(mTripsheetsStockList);
                            }
                        }
                    }

                    break;
                case 2:
                    JSONArray soArray = new JSONArray(response);
                    int soLen = soArray.length();

                    JSONArray productCodesArray1 = null, orderQuantityArray1 = null, productValuearray = null;
                    JSONObject agentData = null;

                    for (int i = 0; i < soLen; i++) {
                        JSONObject jb = soArray.getJSONObject(i);
                        int productsLen = 0;

                        if (jb.has("product_codes")) {
                            productCodesArray1 = jb.getJSONArray("product_codes");
                            productsLen = productCodesArray1.length();
                        }

                        orderQuantityArray1 = jb.getJSONArray("order_qty");
                        productValuearray = jb.getJSONArray("product_value");

                        if (jb.has("agentdata")) {
                            agentData = jb.getJSONObject("agentdata");
                        }

                        if (soLen > 0) {
                            Map<String, String> latLangDetails = mDBHelper.getLatLangOfAgentByAgentId(agentData.getString("_id"));
                            //System.out.println("AGENT LAT::: " + latLangDetails.get(agentData.getString("_id") + "_Lat"));
                            //System.out.println("AGENT LANG::: " + latLangDetails.get(agentData.getString("_id") + "_Lang"));

                            TripsheetSOList tripStockBean = new TripsheetSOList();

                            tripStockBean.setmTripshetSOId(jb.getString("_id"));
                            tripStockBean.setmTripshetSOTripId(jb.getString("trip_id"));
                            tripStockBean.setmTripshetSOAgentCode(jb.getString("agent_code"));
                            tripStockBean.setmTripshetSOCode(jb.getString("sale_order_code"));
                            tripStockBean.setmTripshetSODate(jb.getString("sale_order_date"));

                            if (jb.getString("sale_order_value").length() > 0)
                                tripStockBean.setmTripshetSOValue(jb.getString("sale_order_value"));
                            else
                                tripStockBean.setmTripshetSOValue("0.00");

                            tripStockBean.setmTripshetSOOpAmount(jb.getString("op_amt"));
                            tripStockBean.setmTripshetSOCBAmount(jb.getString("cb_amt"));
                            tripStockBean.setmTripshetSOAgentId(agentData.getString("_id"));
                            tripStockBean.setmTripshetSOAgentFirstName(agentData.getString("first_name"));

                            tripStockBean.setmTripshetSOProductCode(productCodesArray1.toString());
                            tripStockBean.setmTripshetSOProductOrderQuantity(orderQuantityArray1.toString());
                            tripStockBean.setmTripshetSOProductValue(productValuearray.toString());

                            tripStockBean.setmTripshetSOAgentLastName(agentData.getString("last_name"));
                            tripStockBean.setmTripshetSOApprovedBy(jb.getString("approved_by"));
                            tripStockBean.setmTripshetSOAgentLatitude(latLangDetails.get(agentData.getString("_id") + "_Lat"));
                            tripStockBean.setmTripshetSOAgentLongitude(latLangDetails.get(agentData.getString("_id") + "_Lang"));
                            tripStockBean.setmTripshetSOProductsCount(String.valueOf(productsLen));

                            tripStockBean.setmTripshetSOCansDue(jb.getString("cans_due"));
                            tripStockBean.setmTripshetSOCratesDue(jb.getString("crates_due"));

                            mTripsheetsSOList.add(tripStockBean);
                        }
                    }
                    synchronized (this) {
                        if (mTripsheetsSOList.size() > 0) {
                            mDBHelper.insertTripsheetsSOListData(mTripsheetsSOList);
                        }
                    }
                    synchronized (this) {
                        activity2.loadTripSheetSaleOrderData();
                        //  activity4.loadTripSheetSaleOrderPreviewData();

                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
