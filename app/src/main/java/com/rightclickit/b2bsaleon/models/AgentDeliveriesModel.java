package com.rightclickit.b2bsaleon.models;

import android.content.Context;

import com.rightclickit.b2bsaleon.activities.AgentDeliveries;
import com.rightclickit.b2bsaleon.activities.Products_Activity;
import com.rightclickit.b2bsaleon.beanclass.AgentDeliveriesBean;
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

public class AgentDeliveriesModel implements OnAsyncRequestCompleteListener {
    public static final String TAG = Products_Activity.class.getSimpleName();

    private  Context context;
    private AgentDeliveries activity;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
    private String type = "";
    private ArrayList<String> regionIdsList = new ArrayList<String>();
    private JSONArray routesArray;
    private  ArrayList<AgentDeliveriesBean> mDeliveriesBeansList = new ArrayList<AgentDeliveriesBean>();
    private String mStock = "", mAgentPrice = "", mRetailerPrice = "", mConsumerPrice = "";
    private  String currentDate = "";
    private  String fromDate = "";
    private  ArrayList<String> tripIdsList = new ArrayList<String>();
    private  ArrayList<String> userIdsList = new ArrayList<String>();
    private  ArrayList<String> userCodesList = new ArrayList<String>();
    private  ArrayList<String> routeIdsList = new ArrayList<String>();
    private  ArrayList<String> routeCodesList = new ArrayList<String>();
    private  ArrayList<String> saleorderIdList = new ArrayList<String>();
    private  ArrayList<String> saleorderCodesList = new ArrayList<String>();
    private  ArrayList<String> statusList = new ArrayList<String>();
    private  ArrayList<String> DeliveryDateList = new ArrayList<String>();
    private  ArrayList<String> DeliverybyList = new ArrayList<String>();
    private  ArrayList<String> Size = new ArrayList<String>();
    private  HashMap<String, JSONArray> productIdsList = new HashMap<String, JSONArray>();
    private  HashMap<String, JSONArray> productCodesList = new HashMap<String, JSONArray>();
    private  HashMap<String, JSONArray> quantitysList = new HashMap<String, JSONArray>();

    private static HashMap<String, JSONArray> productsArray = new HashMap<String, JSONArray>();
    private static HashMap<String, JSONArray> unitPriceArray = new HashMap<String, JSONArray>();


    public AgentDeliveriesModel(Context context, AgentDeliveries activity) {
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

    public  void getDeliveriesList(String s) {
        try {
            if (mDeliveriesBeansList.size() > 0) {
                mDeliveriesBeansList.clear();
            }
            if (tripIdsList.size() > 0) {
                tripIdsList.clear();
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
            if (saleorderIdList.size() > 0) {
                saleorderIdList.clear();
            }
            if (saleorderCodesList.size() > 0) {
                saleorderCodesList.clear();
            }
            if (productIdsList.size() > 0) {
                productIdsList.clear();
            }
            if (productCodesList.size() > 0) {
                productCodesList.clear();
            }
            if (quantitysList.size() > 0) {
                quantitysList.clear();
            }
            if (DeliveryDateList.size() > 0) {
                DeliveryDateList.clear();
            }

            if (productsArray.size() > 0) {
                productsArray.clear();
            }
            if (unitPriceArray.size() > 0) {
                unitPriceArray.clear();
            }
            if (DeliverybyList.size() > 0) {
                DeliverybyList.clear();
            }

            if (statusList.size() > 0) {
                statusList.clear();
            }

            if (Size.size() > 0) {
                Size.clear();
            }


            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String ordersURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.SYNC_TAKE_ORDERS_PORT, Constants.AGENT_DELIVERIES_LIST);
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

                    // Enquiry Id
                    if (resObj.has("delivery_no")) {
                        tripIdsList.add(resObj.getString("delivery_no"));
                    }
                    // User Id
                    if (resObj.has("user_id")) {
                        userIdsList.add(resObj.getString("user_id"));
                    }
                    // User code
                    if (resObj.has("user_code")) {
                        userCodesList.add(resObj.getString("user_code"));
                    }
                    // Route Id
                    if (resObj.has("route_id")) {
                        routeIdsList.add(resObj.getString("route_id"));
                    }
                    // Route Code
                    if (resObj.has("route_code")) {
                        routeCodesList.add(resObj.getString("route_code"));
                    }
                    // Order Date
                    if (resObj.has("delivery_date")) {
                        DeliveryDateList.add(resObj.getString("delivery_date"));
                    }

                    if (resObj.has("updated_by")) {
                        DeliverybyList.add(resObj.getString("updated_by"));
                    }

                    if (resObj.has("status")) {
                        statusList.add(resObj.getString("status"));
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
                    // Quantitys
                    if (resObj.has("quantity")) {
                        JSONArray quArray = resObj.getJSONArray("quantity");
                        quantitysList.put(String.valueOf(j), quArray);
                    }

                    // Unit price
                    if (resObj.has("unit_price")) {
                        JSONArray upArray = resObj.getJSONArray("unit_price");
                        unitPriceArray.put(String.valueOf(j), upArray);
                    }

                    // Products Array
                    if (resObj.has("productdata")) {
                        JSONArray subPArray = resObj.getJSONArray("productdata");
                        productsArray.put(String.valueOf(j), subPArray);
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

                for (int d = 0; d < productsArray.size(); d++) {

                    // Unit prices
                    ArrayList<String> upPriceList = new ArrayList<String>();
                    JSONArray uPriceAr = unitPriceArray.get(String.valueOf(d));
                    for (int z = 0; z < uPriceAr.length(); z++) {
                        upPriceList.add(uPriceAr.get(z).toString());
                    }

                    // Quantity
                    ArrayList<String> qList = new ArrayList<String>();
                    JSONArray qAr = quantitysList.get(String.valueOf(d));
                    for (int z2 = 0; z2 < qAr.length(); z2++) {
                        qList.add(qAr.get(z2).toString());
                    }
                    JSONArray aaa = productsArray.get(String.valueOf(d));
                    for (int s = 0; s < aaa.length(); s++) {
                        AgentDeliveriesBean deliveriesBean = new AgentDeliveriesBean();
                        JSONObject jj = aaa.getJSONObject(s);
                        deliveriesBean.setTripNo(tripIdsList.get(d).toString());

                        deliveriesBean.setDeliverdstatus(statusList.get(d).toString());

                        deliveriesBean.setTripDate(DeliveryDateList.get(d).toString());
                        deliveriesBean.setDeliveredBy(DeliverybyList.get(d).toString());
                        deliveriesBean.setDeliveredItems(String.valueOf(mDeliveriesBeansList.size()));

                        mDeliveriesBeansList.add(deliveriesBean);
                    }
                }
                synchronized (this) {
                    if (mDeliveriesBeansList.size() > 0) {
                        mDBHelper.getdeliveryDetails(String.valueOf(mDeliveriesBeansList));
                    }
                }
                synchronized (this) {
                    if (mDeliveriesBeansList.size() > 0) {
                        activity.loadDeliveries(mDeliveriesBeansList);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


