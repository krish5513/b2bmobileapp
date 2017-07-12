package com.rightclickit.b2bsaleon.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.beanclass.TDCSalesOrderProductBean;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderBean;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.NetworkManager;
import com.rightclickit.b2bsaleon.util.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Venkat
 */

public class SyncTDCSalesOrderService extends Service {
    private Runnable runnable;
    private Handler handler;
    private DBHelper mDBHelper;
    private MMSharedPreferences mSessionManagement;
    private NetworkConnectionDetector connectionDetector;
    private int unUploadedTDCSalesOrdersCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        mDBHelper = new DBHelper(this);
        mSessionManagement = new MMSharedPreferences(this);
        connectionDetector = new NetworkConnectionDetector(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        syncTDCSalesOrdersDataWithServer();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void syncTDCSalesOrdersDataWithServer() {
        try {
            List<TDCSaleOrder> unUploadedTDCSalesOrders = mDBHelper.fetchAllUnUploadedTDCSalesOrders();
            unUploadedTDCSalesOrdersCount = unUploadedTDCSalesOrders.size();

            for (TDCSaleOrder order : unUploadedTDCSalesOrders) {
                if (connectionDetector.isNetworkConnected())
                    new SyncTDCSalesOrderAsyncTask().execute(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SyncTDCSalesOrderAsyncTask extends AsyncTask<TDCSaleOrder, Void, Void> {
        private TDCSaleOrder currentOrder;

        @Override
        protected Void doInBackground(TDCSaleOrder... tdcSaleOrders) {
            try {
                currentOrder = tdcSaleOrders[0];

                String userId = currentOrder.getSelectedCustomerUserId();
                if (userId == null || userId.isEmpty()) {
                    userId = "596312856e6ce831f4279004"; // static value given by service team and we are considering this customer as un known.
                }

                HashMap<String, String> userRouteIds = mDBHelper.getUserRouteIds();
                JSONArray routesArray = new JSONObject(userRouteIds.get("route_ids")).getJSONArray("routeArray");
                String routeId = "";
                if (routesArray.length() > 0)
                    routeId = routesArray.getString(0);

                String orderCreatedTime = Utility.formatTime(currentOrder.getCreatedOn(), Constants.TDC_SALES_ORDER_DATE_SAVE_FORMAT);

                JSONArray productIdsArray = new JSONArray();
                JSONArray taxPercentArray = new JSONArray();
                JSONArray unitPriceArray = new JSONArray();
                JSONArray quantityArray = new JSONArray();
                JSONArray amountArray = new JSONArray();
                JSONArray taxAmountArray = new JSONArray();

                for (TDCSalesOrderProductBean orderProductsBean : currentOrder.getOrderProductsList()) {
                    productIdsArray.put(orderProductsBean.getProductId());
                    taxPercentArray.put(String.valueOf(orderProductsBean.getProductTax()));
                    unitPriceArray.put(String.valueOf(orderProductsBean.getProductMRP()));
                    quantityArray.put(String.valueOf(orderProductsBean.getProductQuantity()));
                    amountArray.put(String.valueOf(orderProductsBean.getProductAmount()));
                    taxAmountArray.put(String.valueOf(orderProductsBean.getProductTaxAmount()));
                }

                JSONObject requestObj = new JSONObject();
                requestObj.put("bill_no", "");
                requestObj.put("user_id", userId);
                requestObj.put("route_id", routeId);
                requestObj.put("product_ids", productIdsArray);
                requestObj.put("tax_percent", taxPercentArray);
                requestObj.put("unit_price", unitPriceArray);
                requestObj.put("quantity", quantityArray);
                requestObj.put("amount", amountArray);
                requestObj.put("tax_amount", taxAmountArray);
                requestObj.put("tax_total", String.valueOf(currentOrder.getOrderTotalTaxAmount()));
                requestObj.put("sale_value", String.valueOf(currentOrder.getOrderSubTotal()));
                requestObj.put("status", "A");
                requestObj.put("delete", "N");
                requestObj.put("created_by", currentOrder.getCreatedBy());
                requestObj.put("created_on", orderCreatedTime);
                requestObj.put("updated_on", orderCreatedTime);
                requestObj.put("updated_by", currentOrder.getCreatedBy());

                String requestURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.PORT_AGENTS_LIST, Constants.TDC_SALES_ORDER_ADD);

                String responseString = new NetworkManager().makeHttpPostConnection(requestURL, requestObj);

                if (responseString != null) {
                    JSONObject resultObj = new JSONObject(responseString);

                    if (resultObj.getInt("result_status") == 1) {
                        // if success, we are updating order status as uploaded in local db.
                        mDBHelper.updateTDCSalesOrdersTable(currentOrder.getOrderId());

                        unUploadedTDCSalesOrdersCount--;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (unUploadedTDCSalesOrdersCount == 0) {
                stopSelf();
                //System.out.println("Sync TDC Sales Order Service Stopped Automatically....");
            }
        }
    }
}
