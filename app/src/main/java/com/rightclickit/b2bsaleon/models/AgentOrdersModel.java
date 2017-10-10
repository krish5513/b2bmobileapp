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

            System.out.println("ORDERS RESPONSE::::::::: " + response);

            JSONArray respArray = new JSONArray(response);
            int resLength = respArray.length();
            if (resLength > 0) {
                for (int j = 0; j < resLength; j++) {
                    JSONObject resObj = respArray.getJSONObject(j);


                    TakeOrderBean takeOrderBean = new TakeOrderBean();

                    if (resObj.has("product_ids")) {

                        takeOrderBean.setmProductId(resObj.getString("product_ids"));
                    }
                    if (resObj.has("route_id")) {

                        takeOrderBean.setmRouteId(resObj.getString("route_id"));
                    }


                    if (resObj.has("productdata")) {
                        JSONArray productUnitJsonArray = resObj.getJSONArray("productdata");
                        int len = productUnitJsonArray.length();
                        if (len > 0) {
                            for (int k = 0; k < len; k++) {
                                JSONObject priceUnitObj = productUnitJsonArray.getJSONObject(k);
                                if (priceUnitObj.has("name")) {
                                    // Agent price

                                    takeOrderBean.setmProductTitle(priceUnitObj.getString("display"));

                                }
                            }
                        }
                    }
                    if (resObj.has("from_date")) {
                        takeOrderBean.setmProductFromDate(resObj.getString("from_date"));
                    }
                    if (resObj.has("to_date")) {
                        takeOrderBean.setmProductToDate(resObj.getString("to_date"));
                    }
                    if (resObj.has("quantity")) {
                        takeOrderBean.setmProductQuantity(resObj.getString("quantity"));
                    }
                    if (resObj.has("status")) {
                        takeOrderBean.setmProductStatus(resObj.getString("status"));
                    }

                    if (resObj.has("productdata")) {
                        JSONArray productUnitJsonArray = resObj.getJSONArray("productdata");
                        int len = productUnitJsonArray.length();
                        if (len > 0) {
                            for (int k = 0; k < len; k++) {
                                JSONObject priceUnitObj = productUnitJsonArray.getJSONObject(k);
                                if (priceUnitObj.has("name")) {
                                    // Agent price

                                    takeOrderBean.setmProductTitle(priceUnitObj.getString("display"));

                                }
                                if (priceUnitObj.has("code")) {
                                    // Agent price

                                    takeOrderBean.setMtakeorderProductCode(priceUnitObj.getString("code"));

                                }
                            }
                        }
                    }
                    if (resObj.has("user_id")) {
                        takeOrderBean.setmAgentId(resObj.getString("user_id"));
                    }
                    if (resObj.has("enquiry_id")) {
                        takeOrderBean.setmEnquiryId(resObj.getString("enquiry_id"));
                    }
                    if (resObj.has("user_code")) {
                        takeOrderBean.setmTakeorderAgentCode(resObj.getString("user_code"));
                    }
                    if (resObj.has("unit_price")) {
                        takeOrderBean.setmAgentPrice(resObj.getString("unit_price"));
                    }


                    mTakeOrderProductsBeansList.add(takeOrderBean);
                }
            }

            synchronized (this) {
                mDBHelper.updateTakeOrderDetails(mTakeOrderProductsBeansList);
            }

            synchronized (this) {
                activity.loadOrders();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

