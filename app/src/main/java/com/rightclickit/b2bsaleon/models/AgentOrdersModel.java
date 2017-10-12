package com.rightclickit.b2bsaleon.models;

import android.content.Context;

import com.rightclickit.b2bsaleon.activities.AgentTDC_Order;
import com.rightclickit.b2bsaleon.activities.Products_Activity;
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

public class AgentOrdersModel implements OnAsyncRequestCompleteListener {
    public static final String TAG = Products_Activity.class.getSimpleName();

    private Context context;
    private AgentTDC_Order activity;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
    private String type = "";
    private ArrayList<String> regionIdsList = new ArrayList<String>();
    private JSONArray routesArray;
    private ArrayList<TakeOrderBean> mTakeOrderProductsBeansList = new ArrayList<TakeOrderBean>();
    private String mStock = "", mAgentPrice = "", mRetailerPrice = "", mConsumerPrice = "";
    private String currentDate = "", fromDate = "";
    private ArrayList<String> enqIdsList = new ArrayList<String>();
    private ArrayList<String> userIdsList = new ArrayList<String>();
    private ArrayList<String> userCodesList = new ArrayList<String>();
    private ArrayList<String> routeIdsList = new ArrayList<String>();
    private ArrayList<String> routeCodesList = new ArrayList<String>();
    private ArrayList<String> orderDateList = new ArrayList<String>();
    private HashMap<String, JSONArray> productIdsList = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> productCodesList = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> quantitysList = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> fromDatesList = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> toDatesList = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> productsArray = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> unitPriceArray = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> spPriceArray = new HashMap<String, JSONArray>();

    public AgentOrdersModel(Context context, AgentTDC_Order activity) {
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

    public void getOrdersList(String s) {
        try {
            if (mTakeOrderProductsBeansList.size() > 0) {
                mTakeOrderProductsBeansList.clear();
            }
            if (enqIdsList.size() > 0) {
                enqIdsList.clear();
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
            if (orderDateList.size() > 0) {
                orderDateList.clear();
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
            if (fromDatesList.size() > 0) {
                fromDatesList.clear();
            }
            if (toDatesList.size() > 0) {
                toDatesList.clear();
            }
            if (productsArray.size() > 0) {
                productsArray.clear();
            }
            if (unitPriceArray.size() > 0) {
                unitPriceArray.clear();
            }
            if (spPriceArray.size() > 0) {
                spPriceArray.clear();
            }
            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String ordersURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.SYNC_TAKE_ORDERS_PORT, Constants.AGENTS_APPROVED_ORDERS);
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
                    if (resObj.has("enquiry_id")) {
                        enqIdsList.add(resObj.getString("enquiry_id"));
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
                    if (resObj.has("approved_on")) {
                        orderDateList.add(resObj.getString("approved_on"));
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
                    // From date
                    if (resObj.has("from_date")) {
                        JSONArray fDatesArray = resObj.getJSONArray("from_date");
                        fromDatesList.put(String.valueOf(j), fDatesArray);
                    }
                    // To date
                    if (resObj.has("to_date")) {
                        JSONArray tDatesArray = resObj.getJSONArray("to_date");
                        toDatesList.put(String.valueOf(j), tDatesArray);
                    }
                    // Unit price
                    if (resObj.has("unit_price")) {
                        JSONArray upArray = resObj.getJSONArray("unit_price");
                        unitPriceArray.put(String.valueOf(j), upArray);
                    }
                    // Special price
                    if (resObj.has("sp_price")) {
                        JSONArray spArray = resObj.getJSONArray("sp_price");
                        spPriceArray.put(String.valueOf(j), spArray);
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
                    // Special prices
                    ArrayList<String> spPriceList = new ArrayList<String>();
                    JSONArray spArray = spPriceArray.get(String.valueOf(d));
                    for (int z = 0; z < spArray.length(); z++) {
                        spPriceList.add(spArray.get(z).toString());
                    }
                    // Unit prices
                    ArrayList<String> upPriceList = new ArrayList<String>();
                    JSONArray uPriceAr = unitPriceArray.get(String.valueOf(d));
                    for (int z = 0; z < uPriceAr.length(); z++) {
                        upPriceList.add(uPriceAr.get(z).toString());
                    }
                    // From Dates
                    ArrayList<String> fDaList = new ArrayList<String>();
                    JSONArray fDAr = fromDatesList.get(String.valueOf(d));
                    for (int z = 0; z < fDAr.length(); z++) {
                        fDaList.add(fDAr.get(z).toString());
                    }
                    // TO Dates
                    ArrayList<String> tDaList = new ArrayList<String>();
                    JSONArray tDAr = toDatesList.get(String.valueOf(d));
                    for (int z1 = 0; z1 < tDAr.length(); z1++) {
                        tDaList.add(tDAr.get(z1).toString());
                    }
                    // Quantity
                    ArrayList<String> qList = new ArrayList<String>();
                    JSONArray qAr = quantitysList.get(String.valueOf(d));
                    for (int z2 = 0; z2 < qAr.length(); z2++) {
                        qList.add(qAr.get(z2).toString());
                    }
                    JSONArray aaa = productsArray.get(String.valueOf(d));
                    for (int s = 0; s < aaa.length(); s++) {
                        TakeOrderBean takeOrderBean = new TakeOrderBean();
                        JSONObject jj = aaa.getJSONObject(s);
                        takeOrderBean.setmProductTitle(jj.getString("name"));
                        takeOrderBean.setmProductId(jj.getString("_id"));
                        takeOrderBean.setMtakeorderProductCode(jj.getString("code"));
                        takeOrderBean.setmRouteId(routeIdsList.get(d).toString());
                        takeOrderBean.setmProductFromDate(fDaList.get(s).toString());
                        takeOrderBean.setmProductToDate(tDaList.get(s).toString());
                        takeOrderBean.setmProductQuantity(qList.get(s).toString());
                        takeOrderBean.setmAgentId(userIdsList.get(d).toString());
                        takeOrderBean.setmTakeorderAgentCode(userCodesList.get(d).toString());
                        takeOrderBean.setmEnquiryId(enqIdsList.get(d).toString());
                        takeOrderBean.setmProductStatus("0");
                        takeOrderBean.setmProductOrderType("");
                        takeOrderBean.setmAgentTakeOrderDate(orderDateList.get(d).toString());
                        if (!spPriceList.get(s).toString().equals("0")) {
                            takeOrderBean.setmAgentPrice(spPriceList.get(s).toString());
                        } else {
                            takeOrderBean.setmAgentPrice(upPriceList.get(s).toString());
                        }

                        mTakeOrderProductsBeansList.add(takeOrderBean);
                    }
                }
                synchronized (this) {
                    if (mTakeOrderProductsBeansList.size() > 0) {
                        mDBHelper.updateTakeOrderDetails(mTakeOrderProductsBeansList);
                    }
                }

                synchronized (this) {
                    if (mTakeOrderProductsBeansList.size() > 0) {
                        activity.loadOrders();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

