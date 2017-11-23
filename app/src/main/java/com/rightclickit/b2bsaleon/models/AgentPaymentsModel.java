package com.rightclickit.b2bsaleon.models;

import android.content.Context;

import com.rightclickit.b2bsaleon.activities.AgentPayments;
import com.rightclickit.b2bsaleon.activities.Products_Activity;
import com.rightclickit.b2bsaleon.beanclass.PaymentsBean;
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
 * Created by PPS on 10/24/2017.
 */

public class AgentPaymentsModel implements OnAsyncRequestCompleteListener {
    public static final String TAG = Products_Activity.class.getSimpleName();

    private Context context;
    private AgentPayments activity;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
    private String type = "";

    private ArrayList<String> regionIdsList = new ArrayList<String>();
    private JSONArray routesArray;

    private ArrayList<PaymentsBean> mPaymentsBeansList = new ArrayList<PaymentsBean>();

    private String currentDate = "", fromDate = "";
    private ArrayList<String> paymentNosList = new ArrayList<String>();
    private ArrayList<String> paymentsDatesList = new ArrayList<String>();
    private ArrayList<String> paymentsStatusList = new ArrayList<String>();
    private ArrayList<String> paymentType = new ArrayList<String>();
    private ArrayList<String> receivedAmount = new ArrayList<String>();

    private static HashMap<String, JSONArray> productsArray = new HashMap<String, JSONArray>();


    public AgentPaymentsModel(Context context, AgentPayments activity) {

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


    public void getPaymentsList(String s) {

        try {
            if (mPaymentsBeansList.size() > 0) {
                mPaymentsBeansList.clear();
            }
            if (paymentNosList.size() > 0) {
                paymentNosList.clear();
            }
            if (paymentsDatesList.size() > 0) {
                paymentsDatesList.clear();
            }
            if (paymentsStatusList.size() > 0) {
                paymentsStatusList.clear();
            }

            if (paymentType.size() > 0) {
                paymentType.clear();
            }
            if (receivedAmount.size() > 0) {
                receivedAmount.clear();
            }
            if (productsArray.size() > 0) {
                productsArray.clear();
            }

            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String ordersURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.SYNC_TAKE_ORDERS_PORT, Constants.AGENT_PAYMENTS);
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
                    PaymentsBean paymentsBean = new PaymentsBean();
                    // Returns No
                    if (resObj.has("payment_no")) {
                        paymentsBean.setPayments_paymentsNumber(resObj.getString("payment_no"));
                    }
                    // Returns Date
                    if (resObj.has("payment_date")) {
                       // paymentsDatesList.add(resObj.getString("payment_date"));
                        paymentsBean.setPayment_date(resObj.getString("payment_date"));

                    }
                    // Return Status
                    if (resObj.has("status")) {
                       // paymentsStatusList.add(resObj.getString("status"));
                        paymentsBean.setPayments_status(resObj.getString("status"));
                    }
                    // Returned By
                    if (resObj.has("type")) {
                       // paymentType.add(resObj.getString("type"));
                        paymentsBean.setPayments_type(resObj.getString("type"));

                    }
                    if (resObj.has("user_id")) {
                       // paymentType.add(resObj.getString("type"));
                        paymentsBean.setPayments_userId(resObj.getString("user_id"));

                    }


                    // Returned By
                    if (resObj.has("recieved_amt")) {
                       // receivedAmount.add(resObj.getString("recieved_amt"));
                        paymentsBean.setPayments_receivedAmount(Double.parseDouble(resObj.getString("recieved_amt")));
                    }
                    // Products Array
                    if (resObj.has("productdata")) {
                        JSONArray subPArray = resObj.getJSONArray("productdata");
                        productsArray.put(String.valueOf(j), subPArray);
                    }
                    mPaymentsBeansList.add(paymentsBean);

                   /* for (int d = 0; d < productsArray.size(); d++) {


                        JSONArray aaa = productsArray.get(String.valueOf(d));
                        for (int s = 0; s < aaa.length(); s++) {

                            //    JSONObject jj = aaa.getJSONObject(s);







                        }
                    }*/


                    synchronized (this) {
                        if (mPaymentsBeansList.size() > 0) {
                            mDBHelper.updateTripsheetsPaymentsListData(mPaymentsBeansList);
                        }
                    }
                }

                synchronized (this) {
                    if (mPaymentsBeansList.size() > 0) {
                        activity.loadPayments1();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

















