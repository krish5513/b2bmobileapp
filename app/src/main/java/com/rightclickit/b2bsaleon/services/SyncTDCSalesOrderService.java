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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
                System.out.println("========== currentOrder = " + currentOrder);
                JSONArray productIdsArray = new JSONArray();
                JSONArray taxPercentArray = new JSONArray();
                JSONArray unitPriceArray = new JSONArray();
                JSONArray quantityArray = new JSONArray();
                JSONArray amountArray = new JSONArray();
                JSONArray taxAmountArray = new JSONArray();

                for (TDCSalesOrderProductBean orderProductsBean : currentOrder.getOrderProductsList()) {
                    productIdsArray.put(orderProductsBean.getProductId());
                    taxPercentArray.put(orderProductsBean.getProductTax());
                    unitPriceArray.put(orderProductsBean.getProductMRP());
                    quantityArray.put(orderProductsBean.getProductQuantity());
                    amountArray.put(orderProductsBean.getProductAmount());
                    taxAmountArray.put(orderProductsBean.getProductTaxAmount());
                }

                JSONObject requestObj = new JSONObject();
                requestObj.put("bill_no", String.format("TDC%05d", currentOrder.getOrderId()));
                requestObj.put("user_id", "" + currentOrder.getSelectedCustomerId());
                requestObj.put("route_id", mSessionManagement.getString("routename"));
                requestObj.put("product_ids", productIdsArray);
                requestObj.put("tax_percent", taxPercentArray);
                requestObj.put("unit_price", unitPriceArray);
                requestObj.put("quantity", quantityArray);
                requestObj.put("amount", amountArray);
                requestObj.put("tax_amount", taxAmountArray);
                requestObj.put("tax_total", currentOrder.getOrderTotalTaxAmount());
                requestObj.put("sale_value", currentOrder.getOrderSubTotal());
                requestObj.put("status", "A");
                requestObj.put("delete", "N");
                requestObj.put("created_by", currentOrder.getCreatedBy());
                requestObj.put("created_on", currentOrder.getCreatedOn());
                requestObj.put("updated_on", currentOrder.getCreatedOn());
                requestObj.put("updated_by", currentOrder.getCreatedBy());
                System.out.println("======= requestObj = " + requestObj);
                String requestURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.PORT_AGENTS_LIST, Constants.TDC_SALES_ORDER_ADD);
                System.out.println("======= requestURL = " + requestURL);
                String responseString = new NetworkManager().makeHttpPostConnection(requestURL, requestObj);
                System.out.println("======= responseString = " + responseString);
                if (responseString != null) {
                    JSONObject resultObj = new JSONObject(responseString);
                    System.out.println("====== resultObj = " + resultObj);
                    // if success, need to update order status as uploaded in local db.
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            stopSelf();
            System.out.println("Sync TDC Sales Order Service Stopped Automatically....");
        }
    }
}