package com.rightclickit.b2bsaleon.models;

import android.content.Context;

import com.rightclickit.b2bsaleon.activities.AgentDeliveries;
import com.rightclickit.b2bsaleon.activities.Products_Activity;
import com.rightclickit.b2bsaleon.beanclass.TripSheetDeliveriesBean;
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
    private  ArrayList<TripSheetDeliveriesBean> mDeliveriesBeansList = new ArrayList<TripSheetDeliveriesBean>();
    private String mStock = "", mAgentPrice = "", mRetailerPrice = "", mConsumerPrice = "";
    private  String currentDate = "";
    private  String fromDate = "";
    private  ArrayList<String> deliveryNoList = new ArrayList<String>();
    private  ArrayList<String> tripIdsList = new ArrayList<String>();
    private  ArrayList<String> saleorderIdList = new ArrayList<String>();
    private  ArrayList<String> saleorderCodesList = new ArrayList<String>();
    private  ArrayList<String> userIdsList = new ArrayList<String>();
    private  ArrayList<String> userCodesList = new ArrayList<String>();
    private  ArrayList<String> routeIdsList = new ArrayList<String>();
    private  ArrayList<String> routeCodesList = new ArrayList<String>();

    private  HashMap<String, JSONArray> productIdsList = new HashMap<String, JSONArray>();
    private  HashMap<String, JSONArray> productCodesList = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> taxPercentArray = new HashMap<String, JSONArray>();
    private  HashMap<String, JSONArray> unitPriceArray = new HashMap<String, JSONArray>();
    private  HashMap<String, JSONArray> quantitysList = new HashMap<String, JSONArray>();
    private  HashMap<String, JSONArray> amount = new HashMap<String, JSONArray>();
    private  HashMap<String, JSONArray> taxAmount = new HashMap<String, JSONArray>();

    private  ArrayList<String> taxTotal = new ArrayList<String>();
    private  ArrayList<String> saleValue = new ArrayList<String>();
    private  ArrayList<String> statusList = new ArrayList<String>();
    private  ArrayList<String> deleteList = new ArrayList<String>();
    private  ArrayList<String> DeliveryDateList = new ArrayList<String>();


    private  ArrayList<String> createdBy = new ArrayList<String>();
    private  ArrayList<String> createdOn = new ArrayList<String>();
    private  ArrayList<String> updatedOn = new ArrayList<String>();
    private  ArrayList<String> updatedBy = new ArrayList<String>();
    private  ArrayList<String> Size = new ArrayList<String>();

    private static HashMap<String, JSONArray> productsArray = new HashMap<String, JSONArray>();


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

            if (deliveryNoList.size() > 0) {
                deliveryNoList.clear();
            }
            if (tripIdsList.size() > 0) {
                tripIdsList.clear();
            }

            if (saleorderIdList.size() > 0) {
                saleorderIdList.clear();
            }
            if (saleorderCodesList.size() > 0) {
                saleorderCodesList.clear();
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

            if (productIdsList.size() > 0) {
                productIdsList.clear();
            }
            if (productCodesList.size() > 0) {
                productCodesList.clear();
            }
            if (taxPercentArray.size() > 0) {
                taxPercentArray.clear();
            }

            if (unitPriceArray.size() > 0) {
                unitPriceArray.clear();
            }
            if (quantitysList.size() > 0) {
                quantitysList.clear();
            }
            if (amount.size() > 0) {
                amount.clear();
            }
            if (taxAmount.size() > 0) {
                taxAmount.clear();
            }
            if (taxTotal.size() > 0) {
                taxTotal.clear();
            }
            if (saleValue.size() > 0) {
                saleValue.clear();
            }
            if (DeliveryDateList.size() > 0) {
                DeliveryDateList.clear();
            }

            if (productsArray.size() > 0) {
                productsArray.clear();
            }
            if (createdOn.size() > 0) {
                createdOn.clear();
            }
            if (createdBy.size() > 0) {
                createdBy.clear();
            }
            if (updatedOn.size() > 0) {
                updatedOn.clear();
            }

            if (updatedBy.size() > 0) {
                updatedBy.clear();
            }

            if (statusList.size() > 0) {
                statusList.clear();
            }
            if (deleteList.size() > 0) {
                deleteList.clear();
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

                    // Delivery No
                    if (resObj.has("delivery_no")) {
                        deliveryNoList.add(resObj.getString("delivery_no"));
                    }
                    // Trid Id
                    if (resObj.has("trip_id")) {
                        tripIdsList.add(resObj.getString("trip_id"));
                    }
                    // Saleorder Id
                    if (resObj.has("sale_order_id")) {
                        saleorderIdList.add(resObj.getString("sale_order_id"));
                    }
                    //  Saleorder Code
                    if (resObj.has("sale_order_code")) {
                        saleorderCodesList.add(resObj.getString("sale_order_code"));
                    }
                    // User Id
                    if (resObj.has("user_id")) {
                        userIdsList.add(resObj.getString("user_id"));
                    }
                    // User code
                    if (resObj.has("user_codes")) {
                        userCodesList.add(resObj.getString("user_codes"));
                    }
                    // Route Id
                    if (resObj.has("route_id")) {
                        routeIdsList.add(resObj.getString("route_id"));
                    }
                    // Route Code
                    if (resObj.has("route_codes")) {
                        routeCodesList.add(resObj.getString("route_codes"));
                    }
                    // Order Date
                    if (resObj.has("delivery_date")) {
                        DeliveryDateList.add(resObj.getString("delivery_date"));
                    }
                    // Created On
                    if (resObj.has("created_on")) {
                        createdOn.add(resObj.getString("created_on"));
                    }
                    // Created By
                    if (resObj.has("created_by")) {
                        createdBy.add(resObj.getString("created_by"));
                    }
                    // Updated On
                    if (resObj.has("updated_on")) {
                        updatedOn.add(resObj.getString("updated_on"));
                    }
                    // Updated By
                    if (resObj.has("updated_by")) {
                        updatedBy.add(resObj.getString("updated_by"));
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

                    // taxPercent
                    if (resObj.has("tax_percent")) {
                        JSONArray taxArray = resObj.getJSONArray("tax_percent");
                        taxPercentArray.put(String.valueOf(j), taxArray);
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

                    // Amount
                    if (resObj.has("amount")) {
                        JSONArray amountArray = resObj.getJSONArray("amount");
                        amount.put(String.valueOf(j), amountArray);
                    }


                    // TaxAmount
                    if (resObj.has("tax_amount")) {
                        JSONArray taxAmountArray = resObj.getJSONArray("tax_amount");
                        taxAmount.put(String.valueOf(j), taxAmountArray);
                    }


                    // TaxTotal
                    if (resObj.has("tax_total")) {
                        taxTotal.add(resObj.getString("tax_total"));
                    }

                    // SaleValue
                    if (resObj.has("sale_value")) {
                        saleValue.add(resObj.getString("sale_value"));
                    }
                    if (resObj.has("status")) {
                        statusList.add(resObj.getString("status"));
                    }
                    if (resObj.has("delete")) {
                        deleteList.add(resObj.getString("delete"));
                    }
                    // Products Array
                    if (resObj.has("productdata")) {
                        JSONArray subPArray = resObj.getJSONArray("productdata");
                        productsArray.put(String.valueOf(j), subPArray);
                    }
                }


                for (int d = 0; d < productsArray.size(); d++) {

                    // Tax Percent
                    ArrayList<String> taxList = new ArrayList<String>();
                    JSONArray taxAr = taxPercentArray.get(String.valueOf(d));
                    for (int z = 0; z < taxAr.length(); z++) {
                        taxList.add(taxAr.get(z).toString());
                    }

                    // Quantity
                    ArrayList<String> qList = new ArrayList<String>();
                    JSONArray qAr = quantitysList.get(String.valueOf(d));
                    for (int z2 = 0; z2 < qAr.length(); z2++) {
                        qList.add(qAr.get(z2).toString());
                    }

                    // Unit prices
                    ArrayList<String> upPriceList = new ArrayList<String>();
                    JSONArray uPriceAr = unitPriceArray.get(String.valueOf(d));
                    for (int z = 0; z < uPriceAr.length(); z++) {
                        upPriceList.add(uPriceAr.get(z).toString());
                    }


                    // Amount
                    ArrayList<String> amountList = new ArrayList<String>();
                    JSONArray amountAr = amount.get(String.valueOf(d));
                    for (int z = 0; z < amountAr.length(); z++) {
                        amountList.add(amountAr.get(z).toString());
                    }


                    // taxAmount
                    ArrayList<String> taxAmountList = new ArrayList<String>();
                    JSONArray taxAmountAr = taxAmount.get(String.valueOf(d));
                    for (int z = 0; z < taxAmountAr.length(); z++) {
                        taxAmountList.add(taxAmountAr.get(z).toString());
                    }

                    // productId
                    ArrayList<String> pIdList = new ArrayList<String>();
                    JSONArray pIdAr = productIdsList.get(String.valueOf(d));
                    for (int z = 0; z < pIdAr.length(); z++) {
                        pIdList.add(pIdAr.get(z).toString());
                    }

                    // productCode
                    ArrayList<String> pCodeList = new ArrayList<String>();
                    JSONArray pCodeAr = productCodesList.get(String.valueOf(d));
                    for (int z = 0; z < pCodeAr.length(); z++) {
                        pCodeList.add(pCodeAr.get(z).toString());
                    }

                    JSONArray aaa = productsArray.get(String.valueOf(d));
                    for (int s = 0; s < aaa.length(); s++) {
                        TripSheetDeliveriesBean deliveriesBean = new TripSheetDeliveriesBean();
                        JSONObject jj = aaa.getJSONObject(s);
                        deliveriesBean.setmTripsheetDeliveryNo(deliveryNoList.get(d).toString());
                        deliveriesBean.setmTripsheetDelivery_tripId(tripIdsList.get(d).toString());
                        deliveriesBean.setmTripsheetDelivery_so_id(saleorderIdList.get(d).toString());
                        deliveriesBean.setmTripsheetDelivery_so_code(saleorderCodesList.get(d).toString());
                        deliveriesBean.setmTripsheetDelivery_userId(userIdsList.get(d).toString());
                        deliveriesBean.setmTripsheetDelivery_userCodes(userCodesList.get(d).toString());
                        deliveriesBean.setmTripsheetDelivery_routeId(routeIdsList.get(d).toString());
                        deliveriesBean.setmTripsheetDelivery_routeCodes(routeCodesList.get(d).toString());
                        deliveriesBean.setmTripsheetDelivery_productId(pIdList.get(s).toString());
                        deliveriesBean.setmTripsheetDelivery_productCodes(pCodeList.get(s).toString());
                        deliveriesBean.setmTripsheetDelivery_TaxPercent(taxList.get(s).toString());
                        deliveriesBean.setmTripsheetDelivery_UnitPrice(upPriceList.get(s).toString());
                        deliveriesBean.setmTripsheetDelivery_Quantity(qList.get(s).toString());
                        deliveriesBean.setmTripsheetDelivery_Amount(amountList.get(s).toString());
                        deliveriesBean.setmTripsheetDelivery_TaxAmount(taxAmountList.get(s).toString());
                        deliveriesBean.setmTripsheetDelivery_TaxTotal(taxTotal.get(d).toString());
                        deliveriesBean.setmTripsheetDelivery_SaleValue(saleValue.get(d).toString());
                        deliveriesBean.setmTripsheetDelivery_Status(statusList.get(d).toString());
                        deliveriesBean.setmTripsheetDelivery_Delete(deleteList.get(d).toString());
                        deliveriesBean.setmTripsheetDelivery_CreatedOn(DeliveryDateList.get(d).toString());
                        deliveriesBean.setmTripsheetDelivery_CreatedBy(createdBy.get(d).toString());
                        deliveriesBean.setmTripsheetDelivery_UpdatedOn(updatedOn.get(d).toString());
                        deliveriesBean.setmTripsheetDelivery_UpdatedBy(updatedBy.get(d).toString());

                        mDeliveriesBeansList.add(deliveriesBean);
                    }
                }
                synchronized (this) {
                    if (mDeliveriesBeansList.size() > 0) {
                        mDBHelper.updateTripsheetsDeliveriesListData(mDeliveriesBeansList);
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


