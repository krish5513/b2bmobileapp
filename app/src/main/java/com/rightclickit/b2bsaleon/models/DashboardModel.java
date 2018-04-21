package com.rightclickit.b2bsaleon.models;

import android.content.Context;

import com.rightclickit.b2bsaleon.activities.AgentTDC_Order;
import com.rightclickit.b2bsaleon.activities.Products_Activity;
import com.rightclickit.b2bsaleon.beanclass.DashboardPendingIndentBean;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderBean;
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
 * Created by PPS on 10/10/2017.
 */

public class DashboardModel implements OnAsyncRequestCompleteListener {
    public static final String TAG = Products_Activity.class.getSimpleName();

    private Context context;
    private AgentTDC_Order activity;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
    private JSONArray routesArray;
    private String type = "";
    private ArrayList<String> regionIdsList = new ArrayList<String>();

    private ArrayList<DashboardPendingIndentBean> pendingordersBeanList = new ArrayList<DashboardPendingIndentBean>();
    private String mStock = "", mAgentPrice = "", mRetailerPrice = "", mConsumerPrice = "";
    private String selecteddate = "", fromDate = "";
    private ArrayList<String> pendingCountList = new ArrayList<String>();
    private ArrayList<String> approvedCountList = new ArrayList<String>();
    private HashMap<String, JSONArray> categeryList = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> indentList = new HashMap<String, JSONArray>();


    public DashboardModel(Context context, AgentTDC_Order activity) {
        this.context = context;
        this.activity = activity;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);


        selecteddate = mPreferences.getString("selectedDate");


    }

    public void getOrdersList(String s) {
        try {
            if (pendingordersBeanList.size() > 0) {
                pendingordersBeanList.clear();
            }
            if (categeryList.size() > 0) {
                categeryList.clear();
            }
            if (indentList.size() > 0) {
                indentList.clear();
            }
            if (pendingCountList.size() > 0) {
                pendingCountList.clear();
            }
            if (approvedCountList.size() > 0) {
                approvedCountList.clear();
            }


            if (new NetworkConnectionDetector(context).isNetworkConnected()) {

                HashMap<String, String> userMapData = mDBHelper.getUsersData();

                JSONObject routesJob = new JSONObject(userMapData.get("route_ids").toString());
                routesArray = routesJob.getJSONArray("routeArray");
               // String ordersURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.SYNC_TAKE_ORDERS_PORT, Constants.AGENTS_APPROVED_ORDERS);
                JSONObject params = new JSONObject();
                params.put("route_ids", routesArray);
                params.put("date", selecteddate);


               // AsyncRequest routeidRequest = new AsyncRequest(context, this, ordersURL, AsyncRequest.MethodType.POST, params);
               // routeidRequest.execute();
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



                    // categery
                    if (resObj.has("category")) {
                        JSONArray categeryArray = resObj.getJSONArray("category");
                        categeryList.put(String.valueOf(j), categeryArray);
                    }
                    // Litres
                    if (resObj.has("ltrs")) {
                        JSONArray ltrsArray = resObj.getJSONArray("ltrs");
                        indentList.put(String.valueOf(j), ltrsArray);
                    }

                    if (resObj.has("pending_count")) {
                        pendingCountList.add(resObj.getString("pending_count"));
                    }

                    if (resObj.has("approved_count")) {
                        approvedCountList.add(resObj.getString("approved_count"));
                    }

                }

//                System.out.println("ENQ :: " + enqIdsList.size());
//                System.out.println("UID :: " + userIdsList.size());
//                System.out.println("UCODE :: " + userCodesList.size());
//                System.out.println("RID :: " + routeIdsList.size());
//                System.out.println("RCODE :: " + routeCodesList.size());
//                System.out.println("PID :: " + productIdsList.size());
//                System.out.println("PCODE :: " + productCodesList.size());
//                System.out.println("QQQ :: " + quantitysList.size());
//                System.out.println("FDL :: " + fromDatesList.size());
//                System.out.println("TDL :: " + toDatesList.size());
//                System.out.println("PARR :: " + productsArray.size());

                for (int d = 0; d < categeryList.size(); d++) {

                    // TO Dates
                    ArrayList<String> categoryArrayList = new ArrayList<String>();
                    JSONArray cList = categeryList.get(String.valueOf(d));
                    for (int z1 = 0; z1 < cList.length(); z1++) {
                        categoryArrayList.add(cList.get(z1).toString());
                    }
                    // Quantity
                    ArrayList<String> lList = new ArrayList<String>();
                    JSONArray ltrsAr = indentList.get(String.valueOf(d));
                    for (int z2 = 0; z2 < ltrsAr.length(); z2++) {
                        lList.add(ltrsAr.get(z2).toString());
                    }
                    JSONArray aaa = categeryList.get(String.valueOf(d));
                    for (int s = 0; s < aaa.length(); s++) {
                        DashboardPendingIndentBean dashboardBean = new DashboardPendingIndentBean();
                        JSONObject jj = aaa.getJSONObject(s);
                        dashboardBean.setmPendingCount(jj.getString("pending_count"));
                        dashboardBean.setmApprovedCount(jj.getString("approved_count"));

                        dashboardBean.setmCategery(categeryList.get(d).toString());
                        dashboardBean.setmLiters(indentList.get(s).toString());


                        pendingordersBeanList.add(dashboardBean);
                    }
                }
                synchronized (this) {
                    if (pendingordersBeanList.size() > 0) {
                        mDBHelper.insertDashboardPendingIndentDetails(pendingordersBeanList);
                    }
                }

                synchronized (this) {
                    if (pendingordersBeanList.size() > 0) {
                        activity.loadOrders();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

