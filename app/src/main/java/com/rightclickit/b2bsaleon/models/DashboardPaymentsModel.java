package com.rightclickit.b2bsaleon.models;

import android.content.Context;
import android.content.Intent;

import com.rightclickit.b2bsaleon.activities.AgentTDC_Order;
import com.rightclickit.b2bsaleon.activities.DashboardActivity;
import com.rightclickit.b2bsaleon.activities.Products_Activity;

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
import java.util.Date;
import java.util.HashMap;

/**
 * Created by sys-2 on 4/23/2018.
 */

public class DashboardPaymentsModel implements OnAsyncRequestCompleteListener {

    public static final String TAG = Products_Activity.class.getSimpleName();

    private Context context;
    private DashboardActivity activity;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
    private JSONArray routesArray;
    private String type = "";
    private ArrayList<String> regionIdsList = new ArrayList<String>();


    //private String mStock = "", mAgentPrice = "", mRetailerPrice = "", mConsumerPrice = "";
    private String selecteddate = "", fromDate = "";




    private ArrayList<String> paymentdeliveryList = new ArrayList<String>();
    private ArrayList<String> paymentreceivedlist = new ArrayList<String>();
    private ArrayList<String> paymentsduelist = new ArrayList<String>();

    public DashboardPaymentsModel(Context context, DashboardActivity activity) {
        this.context = context;
        this.activity = activity;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);


      //  selecteddate = mPreferences.getString("selectedDate");


    }

    public void getPaymentsList(String s) {

        selecteddate = s;
        try {

            if (paymentdeliveryList.size() > 0) {
                paymentdeliveryList.clear();
            }
            if (paymentreceivedlist.size() > 0) {
                paymentreceivedlist.clear();
            }
            if (paymentsduelist.size() > 0) {
                paymentsduelist.clear();
            }


            if (new NetworkConnectionDetector(context).isNetworkConnected()) {

                HashMap<String, String> userMapData = mDBHelper.getUsersData();

                JSONObject routesJob = new JSONObject(userMapData.get("route_ids").toString());
                routesArray = routesJob.getJSONArray("routeArray");
                String paymentsURL = String.format("%s%s", Constants.MAIN_URL, Constants.GET_DASHBOARD_PAYMENTS_URL);
                JSONObject params = new JSONObject();
                params.put("route_ids", routesArray);
                params.put("date", selecteddate);
                System.out.println("PAYMENTS URL::: " + paymentsURL);
                System.out.println("PAYMENTS DATA::: " + params.toString());

                 AsyncRequest routeidRequest = new AsyncRequest(context, this, paymentsURL, AsyncRequest.MethodType.POST, params);
                 routeidRequest.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void asyncResponse(String response, Constants.RequestCode requestCode) {

        try {
            String deliveryValue = "", receivedvalue = "",due= "";

            CustomProgressDialog.hideProgressDialog();

            JSONArray responseObj = new JSONArray(response);
            int lengthh = responseObj.length();
            for (int j = 0; j < lengthh; j++) {
                JSONObject job = responseObj.getJSONObject(j);


                if (job.has("delivery")) {
                    deliveryValue = job.getString("delivery");
                }

                if (job.has("received")) {
                    receivedvalue = job.getString("received");
                }

                if (job.has("due")) {
                    due = job.getString("due");
                }
            }

            synchronized (this) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateandTime = sdf.format(new Date());
                String currentDate = sdf1.format(new Date());
                mDBHelper.insertDashboardPaymentsDetails( deliveryValue, receivedvalue,due, selecteddate, currentDateandTime,currentDate);
            }

            synchronized (this) {
                activity.paymentsDashboard();
            }

            Intent i = new Intent("android.intent.action.MAIN").putExtra("receiver_key", "completed");
            context.sendBroadcast(i);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}