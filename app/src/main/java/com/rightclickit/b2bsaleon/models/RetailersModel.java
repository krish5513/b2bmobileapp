package com.rightclickit.b2bsaleon.models;

import android.content.Context;

import com.rightclickit.b2bsaleon.activities.Products_Activity;
import com.rightclickit.b2bsaleon.activities.RetailersActivity;
import com.rightclickit.b2bsaleon.beanclass.TDCCustomer;
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
 * Created by PPS on 10/13/2017.
 */

public class RetailersModel implements OnAsyncRequestCompleteListener {
    public static final String TAG = Products_Activity.class.getSimpleName();

    private Context context;
    private RetailersActivity activity;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
    private String type = "";
    private ArrayList<String> regionIdsList = new ArrayList<String>();
    private JSONArray routesArray;
    private ArrayList<TDCCustomer> mTDCCustomerList = new ArrayList<TDCCustomer>();
    private String mStock = "", mAgentPrice = "", mRetailerPrice = "", mConsumerPrice = "";
    private String currentDate = "", fromDate = "";


    private ArrayList<String> billnoList = new ArrayList<String>();
    private ArrayList<String> userIdsList = new ArrayList<String>();
    private ArrayList<String> routeIdsList = new ArrayList<String>();
    private HashMap<String, JSONArray> productIdsList = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> taxpercentList = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> quantitysList = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> fromDatesList = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> toDatesList = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> unitPriceArray = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> userDataArray = new HashMap<String, JSONArray>();

    public RetailersModel(Context context, RetailersActivity activity) {
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

    public void getRetailersList(String s) {
        try {
            if (mTDCCustomerList.size() > 0) {
                mTDCCustomerList.clear();
            }

            if (billnoList.size() > 0) {
                billnoList.clear();
            }
            if (userIdsList.size() > 0) {
                userIdsList.clear();
            }
            if (routeIdsList.size() > 0) {
                routeIdsList.clear();
            }

            if (productIdsList.size() > 0) {
                productIdsList.clear();
            }
            if (taxpercentList.size() > 0) {
                taxpercentList.clear();
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
            if (unitPriceArray.size() > 0) {
                unitPriceArray.clear();
            }

            if (userDataArray.size() > 0) {
                userDataArray.clear();
            }
            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String ordersURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.PORT_AGENTS_LIST, Constants.RETAILERS_TDC_SALESLIST);
                JSONArray customerArray = new JSONArray();
                customerArray.put(s);
                JSONObject params = new JSONObject();
                params.put("customer", customerArray);
                params.put("filter_by_filed", "user_id");//created_by
                params.put("from_date", fromDate);
                params.put("to_date", currentDate);

                System.out.println("URL:::: " + ordersURL);
                System.out.println("INPUT:::: " + params.toString());
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

            System.out.println("RETAILERS RESPONSE=====::: " + response);

            JSONArray respArray = new JSONArray(response);
            int resLength = respArray.length();
//            if (resLength > 0) {
//                for (int j = 0; j < resLength; j++) {
//                    JSONObject resObj = respArray.getJSONObject(j);
//
//
//                    // Bill no
//                    if (resObj.has("bill_no")) {
//                        billnoList.add(resObj.getString("bill_no"));
//                    }
//                    // User Id
//                    if (resObj.has("user_id")) {
//                        userIdsList.add(resObj.getString("user_id"));
//                    }
//
//                    // Route Id
//                    if (resObj.has("route_id")) {
//                        routeIdsList.add(resObj.getString("route_id"));
//                    }
//
//                    // Product Ids
//                    if (resObj.has("product_ids")) {
//                        JSONArray pIdsArray = resObj.getJSONArray("product_ids");
//                        productIdsList.put(String.valueOf(j), pIdsArray);
//                    }
//                    //Tax Percent
//                    if (resObj.has("tax_percent")) {
//                        JSONArray taxArray = resObj.getJSONArray("tax_percent");
//                        taxpercentList.put(String.valueOf(j), taxArray);
//                    }
//
//                    // Unit price
//                    if (resObj.has("unit_price")) {
//                        JSONArray upArray = resObj.getJSONArray("unit_price");
//                        unitPriceArray.put(String.valueOf(j), upArray);
//                    }
//                    // Quantitys
//                    if (resObj.has("quantity")) {
//                        JSONArray quArray = resObj.getJSONArray("quantity");
//                        quantitysList.put(String.valueOf(j), quArray);
//                    }
//                    // From date
//                    if (resObj.has("from_date")) {
//                        JSONArray fDatesArray = resObj.getJSONArray("from_date");
//                        fromDatesList.put(String.valueOf(j), fDatesArray);
//                    }
//                    // To date
//                    if (resObj.has("to_date")) {
//                        JSONArray tDatesArray = resObj.getJSONArray("to_date");
//                        toDatesList.put(String.valueOf(j), tDatesArray);
//                    }
//
//
//                    // Products Array
//                    if (resObj.has("user_data")) {
//                        JSONArray userdataArray = resObj.getJSONArray("user_data");
//                        userDataArray.put(String.valueOf(j), userdataArray);
//                    }
//
//                }
//
//
//                for (int d = 0; d < userDataArray.size(); d++) {
//
//                    // Unit prices
//                    ArrayList<String> upPriceList = new ArrayList<String>();
//                    JSONArray uPriceAr = unitPriceArray.get(String.valueOf(d));
//                    for (int z = 0; z < uPriceAr.length(); z++) {
//                        upPriceList.add(uPriceAr.get(z).toString());
//                    }
//                    // From Dates
//                    ArrayList<String> fDaList = new ArrayList<String>();
//                    JSONArray fDAr = fromDatesList.get(String.valueOf(d));
//                    for (int z = 0; z < fDAr.length(); z++) {
//                        fDaList.add(fDAr.get(z).toString());
//                    }
//                    // TO Dates
//                    ArrayList<String> tDaList = new ArrayList<String>();
//                    JSONArray tDAr = toDatesList.get(String.valueOf(d));
//                    for (int z1 = 0; z1 < tDAr.length(); z1++) {
//                        tDaList.add(tDAr.get(z1).toString());
//                    }
//                    // Quantity
//                    ArrayList<String> qList = new ArrayList<String>();
//                    JSONArray qAr = quantitysList.get(String.valueOf(d));
//                    for (int z2 = 0; z2 < qAr.length(); z2++) {
//                        qList.add(qAr.get(z2).toString());
//                    }
//                    JSONArray aaa = userDataArray.get(String.valueOf(d));
//                    for (int s = 0; s < aaa.length(); s++) {
//                        TDCCustomer tdcCustomer = new TDCCustomer();
//                        JSONObject jj = aaa.getJSONObject(s);
//                        tdcCustomer.setName(jj.getString("first_name"));
//                        tdcCustomer.setId(Long.parseLong(jj.getString("_id")));
//                        tdcCustomer.setBusinessName(jj.getString("last_name"));
//                        tdcCustomer.setAddress(jj.getString("address"));
//                        tdcCustomer.setMobileNo(jj.getString("phone"));
//                        tdcCustomer.setLatitude(jj.getString("latitude"));
//                        tdcCustomer.setLongitude(jj.getString("longitude"));
//
//
//                        mTDCCustomerList.add(tdcCustomer);
//                    }
//                }
//
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


