package com.rightclickit.b2bsaleon.models;

import android.content.Context;

import com.rightclickit.b2bsaleon.activities.AgentTDC_Order;
import com.rightclickit.b2bsaleon.activities.DashboardActivity;
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
import java.util.Date;
import java.util.HashMap;

/**
 * Created by PPS on 10/10/2017.
 */

public class DashboardModel implements OnAsyncRequestCompleteListener {
    public static final String TAG = Products_Activity.class.getSimpleName();

    private Context context;
    private DashboardActivity activity;
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


    public DashboardModel(Context context, DashboardActivity activity) {
        this.context = context;
        this.activity = activity;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);


        //selecteddate = mPreferences.getString("selectedDate");


    }

    public void getReportsData(String s) {
        try {
            selecteddate = s;
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
                String reportsURL = String.format("%s%s", Constants.MAIN_URL, Constants.GET_DASHBOARD_REPORTS_URL);
                JSONObject params = new JSONObject();
                params.put("route_ids", routesArray);
                params.put("date", selecteddate);
                
                AsyncRequest routeidRequest = new AsyncRequest(context, this, reportsURL, AsyncRequest.MethodType.POST, params);
                routeidRequest.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void asyncResponse(String response, Constants.RequestCode requestCode) {
        try {
            String pendingCount = "", approvedCount = "";
            JSONArray catArray = null, ltrsArray = null;
            CustomProgressDialog.hideProgressDialog();

            JSONArray responseObj = new JSONArray(response);
            int lengthh = responseObj.length();
            for (int j = 0; j < lengthh; j++) {
                JSONObject job = responseObj.getJSONObject(j);
                if (job.has("category")) {
                    catArray = job.getJSONArray("category");
                }
                if (job.has("ltrs")) {
                    ltrsArray = job.getJSONArray("ltrs");
                }

                if (job.has("pending_count")) {
                    pendingCount = job.getString("pending_count");
                }

                if (job.has("approved_count")) {
                    approvedCount = job.getString("approved_count");
                }
            }

            synchronized (this) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateandTime = sdf.format(new Date());
                String currentDate = sdf1.format(new Date());
                mDBHelper.insertDashboardPendingIndentDetails(catArray.toString(), ltrsArray.toString(), pendingCount, approvedCount, selecteddate, currentDateandTime,currentDate);
            }

            synchronized (this) {
                activity.nextDayIndents();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

