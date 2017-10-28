package com.rightclickit.b2bsaleon.models;

import android.content.Context;

import com.rightclickit.b2bsaleon.activities.AgentReturns;
import com.rightclickit.b2bsaleon.activities.Products_Activity;
import com.rightclickit.b2bsaleon.beanclass.TripSheetReturnsBean;
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
 * Created by sys-2 on 10/24/2017.
 */

public class AgentReturnsModel implements OnAsyncRequestCompleteListener {
    public static final String TAG = Products_Activity.class.getSimpleName();

    private  Context context;
    private AgentReturns activity;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
    private String type = "";
    private ArrayList<String> regionIdsList = new ArrayList<String>();
    private JSONArray routesArray;
    private  ArrayList<TripSheetReturnsBean> mReturnsBeansList = new ArrayList<TripSheetReturnsBean>();
    private String mStock = "", mAgentPrice = "", mRetailerPrice = "", mConsumerPrice = "";
    private  String currentDate = "";
    private  String fromDate = "";
    private  ArrayList<String> returnsNoList = new ArrayList<String>();
    private  ArrayList<String> tripIdsList = new ArrayList<String>();
    private  ArrayList<String> saleorderIdList = new ArrayList<String>();
    private  ArrayList<String> saleorderCodesList = new ArrayList<String>();
    private  ArrayList<String> userIdsList = new ArrayList<String>();
    private  ArrayList<String> userCodesList = new ArrayList<String>();
    private  ArrayList<String> routeIdsList = new ArrayList<String>();
    private  ArrayList<String> routeCodesList = new ArrayList<String>();

    private  HashMap<String, JSONArray> productIdsList = new HashMap<String, JSONArray>();
    private  HashMap<String, JSONArray> productCodesList = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> returntype = new HashMap<String, JSONArray>();

    private  HashMap<String, JSONArray> quantitysList = new HashMap<String, JSONArray>();


 ;
    private  ArrayList<String> statusList = new ArrayList<String>();
    private  ArrayList<String> deleteList = new ArrayList<String>();
    private  ArrayList<String> returnDateList = new ArrayList<String>();


    private  ArrayList<String> createdBy = new ArrayList<String>();
    private  ArrayList<String> createdOn = new ArrayList<String>();
    private  ArrayList<String> updatedOn = new ArrayList<String>();
    private  ArrayList<String> updatedBy = new ArrayList<String>();

    private static HashMap<String, JSONArray> productsArray = new HashMap<String, JSONArray>();


    public AgentReturnsModel(Context context, AgentReturns activity) {
        this.context = context;
        this.activity = activity;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = df.format(cal.getTime());

        cal.add(Calendar.DATE, -30);
        fromDate = df.format(cal.getTime());

    }

    public  void getReturnsList(String s) {
        try {
            if (mReturnsBeansList.size() > 0) {
                mReturnsBeansList.clear();
            }

            if (returnsNoList.size() > 0) {
                returnsNoList.clear();
            }
            if (tripIdsList.size() > 0) {
                tripIdsList.clear();
            }

            if (saleorderIdList.size() > 0) {
                saleorderIdList.clear();
            }
            if (saleorderCodesList.size() > 0) {
                saleorderCodesList.clear();
            }
            if (userIdsList.size() > 0) {
                userIdsList.clear();
            }
            if (userCodesList.size() > 0) {
                userCodesList.clear();
            }
            if (routeIdsList.size() > 0) {
                routeIdsList.clear();
            }
            if (routeCodesList.size() > 0) {
                routeCodesList.clear();
            }

            if (productIdsList.size() > 0) {
                productIdsList.clear();
            }
            if (productCodesList.size() > 0) {
                productCodesList.clear();
            }
            if (returntype.size() > 0) {
                returntype.clear();
            }


            if (quantitysList.size() > 0) {
                quantitysList.clear();
            }


            if (returnDateList.size() > 0) {
                returnDateList.clear();
            }

            if (productsArray.size() > 0) {
                productsArray.clear();
            }
            if (createdOn.size() > 0) {
                createdOn.clear();
            }
            if (createdBy.size() > 0) {
                createdBy.clear();
            }
            if (updatedOn.size() > 0) {
                updatedOn.clear();
            }

            if (updatedBy.size() > 0) {
                updatedBy.clear();
            }

            if (statusList.size() > 0) {
                statusList.clear();
            }
            if (deleteList.size() > 0) {
                deleteList.clear();
            }




            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String ordersURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.SYNC_TAKE_ORDERS_PORT, Constants.AGENT_RETURNS);
                JSONObject params = new JSONObject();
                params.put("user_id", s);
                params.put("from_date", fromDate);
                params.put("to_date", currentDate);

                AsyncRequest routeidRequest = new AsyncRequest(context, this, ordersURL, AsyncRequest.MethodType.POST, params);
                routeidRequest.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void asyncResponse(String response, Constants.RequestCode requestCode) {
        try {
            CustomProgressDialog.hideProgressDialog();

            //System.out.println("ORDERS RESPONSE::::::::: " + response);

            JSONArray respArray = new JSONArray(response);
            int resLength = respArray.length();
            if (resLength > 0) {
                for (int j = 0; j < resLength; j++) {
                    JSONObject resObj = respArray.getJSONObject(j);

                    // Delivery No
                    if (resObj.has("return_no")) {
                        returnsNoList.add(resObj.getString("return_no"));
                    }
                    // Trid Id
                    if (resObj.has("trip_id")) {
                        tripIdsList.add(resObj.getString("trip_id"));
                    }
                    // Saleorder Id
                    if (resObj.has("sale_order_id")) {
                        saleorderIdList.add(resObj.getString("sale_order_id"));
                    }
                    //  Saleorder Code
                    if (resObj.has("sale_order_code")) {
                        saleorderCodesList.add(resObj.getString("sale_order_code"));
                    }
                    // User Id
                    if (resObj.has("user_id")) {
                        userIdsList.add(resObj.getString("user_id"));
                    }
                    // User code
                    if (resObj.has("user_codes")) {
                        userCodesList.add(resObj.getString("user_codes"));
                    }
                    // Route Id
                    if (resObj.has("route_id")) {
                        routeIdsList.add(resObj.getString("route_id"));
                    }
                    // Route Code
                    if (resObj.has("route_codes")) {
                        routeCodesList.add(resObj.getString("route_codes"));
                    }
                    // Order Date
                    if (resObj.has("return_date")) {
                        returnDateList.add(resObj.getString("return_date"));
                    }
                    // Created On
                    if (resObj.has("created_on")) {
                        createdOn.add(resObj.getString("created_on"));
                    }
                    // Created By
                    if (resObj.has("created_by")) {
                        createdBy.add(resObj.getString("created_by"));
                    }
                    // Updated On
                    if (resObj.has("updated_on")) {
                        updatedOn.add(resObj.getString("updated_on"));
                    }
                    // Updated By
                    if (resObj.has("updated_by")) {
                        updatedBy.add(resObj.getString("updated_by"));
                    }


                    // Product Ids
                    if (resObj.has("product_ids")) {
                        JSONArray pIdsArray = resObj.getJSONArray("product_ids");
                        productIdsList.put(String.valueOf(j), pIdsArray);
                    }
                    // Product Codes
                    if (resObj.has("product_codes")) {
                        JSONArray pCodesArray = resObj.getJSONArray("product_codes");
                        productCodesList.put(String.valueOf(j), pCodesArray);
                    }

                    // type
                    if (resObj.has("type")) {
                        JSONArray typeArray = resObj.getJSONArray("type");
                        returntype.put(String.valueOf(j), typeArray);
                    }
                    // Quantitys
                    if (resObj.has("quantity")) {
                        JSONArray quArray = resObj.getJSONArray("quantity");
                        quantitysList.put(String.valueOf(j), quArray);
                    }




                    if (resObj.has("status")) {
                        statusList.add(resObj.getString("status"));
                    }
                    if (resObj.has("delete")) {
                        deleteList.add(resObj.getString("delete"));
                    }
                    // Products Array
                    if (resObj.has("productdata")) {
                        JSONArray subPArray = resObj.getJSONArray("productdata");
                        productsArray.put(String.valueOf(j), subPArray);
                    }
                }


                for (int d = 0; d < productsArray.size(); d++) {

                    // Type
                    ArrayList<String> typeList = new ArrayList<String>();
                    JSONArray typeAr = returntype.get(String.valueOf(d));
                    for (int z = 0; z < typeAr.length(); z++) {
                        typeList.add(typeAr.get(z).toString());
                    }

                    // Quantity
                    ArrayList<String> qList = new ArrayList<String>();
                    JSONArray qAr = quantitysList.get(String.valueOf(d));
                    for (int z2 = 0; z2 < qAr.length(); z2++) {
                        qList.add(qAr.get(z2).toString());
                    }






                    // productId
                    ArrayList<String> pIdList = new ArrayList<String>();
                    JSONArray pIdAr = productIdsList.get(String.valueOf(d));
                    for (int z = 0; z < pIdAr.length(); z++) {
                        pIdList.add(pIdAr.get(z).toString());
                    }

                    // productCode
                    ArrayList<String> pCodeList = new ArrayList<String>();
                    JSONArray pCodeAr = productCodesList.get(String.valueOf(d));
                    for (int z = 0; z < pCodeAr.length(); z++) {
                        pCodeList.add(pCodeAr.get(z).toString());
                    }

                    JSONArray aaa = productsArray.get(String.valueOf(d));
                    for (int s = 0; s < aaa.length(); s++) {
                        TripSheetReturnsBean returnsBean = new TripSheetReturnsBean();
                        JSONObject jj = aaa.getJSONObject(s);
                        returnsBean.setmTripshhetReturnsReturn_no(returnsNoList.get(d).toString());
                        returnsBean.setmTripshhetReturnsTrip_id(tripIdsList.get(d).toString());
                        returnsBean.setmTripshhetReturns_so_id(saleorderIdList.get(d).toString());
                        returnsBean.setmTripshhetReturns_so_code(saleorderCodesList.get(d).toString());
                        returnsBean.setmTripshhetReturnsUser_id(userIdsList.get(d).toString());
                        returnsBean.setmTripshhetReturnsUser_codes(userCodesList.get(d).toString());
                        returnsBean.setmTripshhetReturnsRoute_id(routeIdsList.get(d).toString());
                        returnsBean.setmTripshhetReturnsRoute_codes(routeCodesList.get(d).toString());
                        returnsBean.setmTripshhetReturnsProduct_ids(pIdList.get(s).toString());
                        returnsBean.setmTripshhetReturnsProduct_codes(pCodeList.get(s).toString());
                        returnsBean.setmTripshhetReturns_productTitle(jj.getString("name"));
                        returnsBean.setmTripshhetReturnsType(typeList.get(s).toString());
                        returnsBean.setmTripshhetReturnsQuantity(qList.get(s).toString());

                        returnsBean.setmTripshhetReturnsStatus(statusList.get(d).toString());
                        returnsBean.setmTripshhetReturnsDelete(deleteList.get(d).toString());
                        returnsBean.setmTripshhetReturnsCreated_on(returnDateList.get(d).toString());
                        returnsBean.setmTripshhetReturnsCreated_by(createdBy.get(d).toString());
                        returnsBean.setmTripshhetReturnsUpdated_on(updatedOn.get(d).toString());
                        returnsBean.setmTripshhetReturnsUpdated_by(updatedBy.get(d).toString());

                        mReturnsBeansList.add(returnsBean);
                    }
                }
                synchronized (this) {
                    if (mReturnsBeansList.size() > 0) {
                        mDBHelper.updateTripsheetsReturnsListData(mReturnsBeansList);
                    }
                }
                synchronized (this) {
                    if (mReturnsBeansList.size() > 0) {
                        activity.loadReturns(mReturnsBeansList);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


