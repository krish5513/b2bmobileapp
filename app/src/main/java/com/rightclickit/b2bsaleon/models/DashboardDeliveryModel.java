package com.rightclickit.b2bsaleon.models;

import android.content.Context;
import android.content.Intent;

import com.rightclickit.b2bsaleon.activities.AgentTDC_Order;
import com.rightclickit.b2bsaleon.activities.DashboardActivity;
import com.rightclickit.b2bsaleon.activities.DashboardDelivery;
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

public class DashboardDeliveryModel implements OnAsyncRequestCompleteListener {

    public static final String TAG = Products_Activity.class.getSimpleName();

    private Context context;
    private DashboardActivity activity;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
    private JSONArray routesArray;
    private String type = "";
    private ArrayList<String> regionIdsList = new ArrayList<String>();


    private String selecteddate = "", fromDate = "";


    private HashMap<String, JSONArray> deliverycategeryList = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> deliveryordervaluelist = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> deliverydeliveryvaluelist = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> deliverypercentagevaluelist = new HashMap<String, JSONArray>();



    public DashboardDeliveryModel(Context context, DashboardActivity activity) {
        this.context = context;
        this.activity = activity;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);


      //  selecteddate = mPreferences.getString("selectedDate");


    }


    public void getDeliveriesList(String s) {
        try {
            selecteddate = s;
            if (deliverycategeryList.size() > 0) {
                deliverycategeryList.clear();
            }
            if (deliveryordervaluelist.size() > 0) {
                deliveryordervaluelist.clear();
            }
            if (deliverydeliveryvaluelist.size() > 0) {
                deliverydeliveryvaluelist.clear();
            }
            if (deliverypercentagevaluelist.size() > 0) {
                deliverypercentagevaluelist.clear();
            }


            if (new NetworkConnectionDetector(context).isNetworkConnected()) {

                HashMap<String, String> userMapData = mDBHelper.getUsersData();

                JSONObject routesJob = new JSONObject(userMapData.get("route_ids").toString());
                routesArray = routesJob.getJSONArray("routeArray");
                String DeliveriesURL = String.format("%s%s", Constants.MAIN_URL, Constants.GET_DASHBOARD_DELIVERIES_URL);
                JSONObject params = new JSONObject();
                params.put("route_ids", routesArray);
                params.put("date", selecteddate);
                System.out.println("DELIVERY URL::: " + DeliveriesURL);
                System.out.println("DELIVERY DATA::: " + params.toString());

                AsyncRequest routeidRequest = new AsyncRequest(context, this, DeliveriesURL, AsyncRequest.MethodType.POST, params);
                 routeidRequest.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void asyncResponse(String response, Constants.RequestCode requestCode) {

        try {

            JSONArray catArray = null, orderedArray = null,deliveredArray=null,perDeliveryArray=null;
            CustomProgressDialog.hideProgressDialog();

            JSONArray responseObj = new JSONArray(response);
            int lengthh = responseObj.length();
            for (int j = 0; j < lengthh; j++) {
                JSONObject job = responseObj.getJSONObject(j);
                if (job.has("category")) {
                    catArray = job.getJSONArray("category");
                }
                if (job.has("ordered")) {
                    orderedArray = job.getJSONArray("ordered");
                }

                if (job.has("delivered")) {
                    deliveredArray = job.getJSONArray("delivered");
                }

                if (job.has("percentage")) {
                    perDeliveryArray = job.getJSONArray("percentage");
                }
            }

            synchronized (this) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateandTime = sdf.format(new Date());
                String currentDate = sdf1.format(new Date());
                mDBHelper.insertDashboardDeliveryDetails(catArray.toString(), orderedArray.toString(), deliveredArray.toString(), perDeliveryArray.toString(), selecteddate, currentDateandTime,currentDate);
            }

            synchronized (this) {
                activity.deliveriesDashboard();
            }


            Intent i = new Intent("android.intent.action.MAIN").putExtra("receiver_key", "payments");
            context.sendBroadcast(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


