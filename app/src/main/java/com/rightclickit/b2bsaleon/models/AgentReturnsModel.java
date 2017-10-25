package com.rightclickit.b2bsaleon.models;

import android.content.Context;

import com.rightclickit.b2bsaleon.activities.AgentReturns;
import com.rightclickit.b2bsaleon.activities.Products_Activity;
import com.rightclickit.b2bsaleon.beanclass.AgentReturnsBean;
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

    private Context context;
    private AgentReturns activity;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
    private String type = "";
    private ArrayList<String> regionIdsList = new ArrayList<String>();
    private JSONArray routesArray;

    private ArrayList<AgentReturnsBean> mReturnsBeansList = new ArrayList<AgentReturnsBean>();

    private String currentDate = "", fromDate = "";
    private ArrayList<String> returnNosList = new ArrayList<String>();
    private ArrayList<String> returnDatesList = new ArrayList<String>();
    private ArrayList<String> returnStatusList = new ArrayList<String>();
    private ArrayList<String> returnedByList = new ArrayList<String>();

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


    public void getReturnsList(String s) {

        try {
            if (mReturnsBeansList.size() > 0) {
                mReturnsBeansList.clear();
            }
            if (returnNosList.size() > 0) {
                returnNosList.clear();
            }
            if (returnDatesList.size() > 0) {
                returnDatesList.clear();
            }
            if (returnStatusList.size() > 0) {
                returnStatusList.clear();
            }

            if (returnedByList.size() > 0) {
                returnedByList.clear();
            }

            if (productsArray.size() > 0) {
                productsArray.clear();
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

                    // Returns No
                    if (resObj.has("return_no")) {
                        returnNosList.add(resObj.getString("return_no"));
                    }
                    // Returns Date
                    if (resObj.has("return_date")) {
                        returnDatesList.add(resObj.getString("return_date"));
                    }
                    // Return Status
                    if (resObj.has("status")) {
                        returnStatusList.add(resObj.getString("status"));
                    }
                    // Returned By
                    if (resObj.has("updated_by")) {
                        returnedByList.add(resObj.getString("updated_by"));
                    }

                    // Products Array
                    if (resObj.has("productdata")) {
                        JSONArray subPArray = resObj.getJSONArray("productdata");
                        productsArray.put(String.valueOf(j), subPArray);
                    }



                }


                for (int d = 0; d < productsArray.size(); d++) {




                    JSONArray aaa = productsArray.get(String.valueOf(d));
                    for (int s = 0; s < aaa.length(); s++) {
                        AgentReturnsBean returnsBean = new AgentReturnsBean();
                        JSONObject jj = aaa.getJSONObject(s);
                        returnsBean.setReturnNo(returnNosList.get(d).toString());

                        returnsBean.setReturnStatus(returnStatusList.get(d).toString());

                        returnsBean.setReturnDate(returnDatesList.get(d).toString());
                        returnsBean.setReturnedBy(returnedByList.get(d).toString());


                        mReturnsBeansList.add(returnsBean);
                    }
                }


                synchronized (this) {
                    if (mReturnsBeansList.size() > 0) {
                        mDBHelper.getreturnsDetails(String.valueOf(mReturnsBeansList));
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















