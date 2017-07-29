package com.rightclickit.b2bsaleon.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.rightclickit.b2bsaleon.beanclass.TDCSalesOrderProductBean;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.NetworkManager;
import com.rightclickit.b2bsaleon.util.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Venkat
 */

public class SyncTripSheetsStockService extends Service {
    private DBHelper mDBHelper;
    private NetworkConnectionDetector connectionDetector;
    private String actionType;
    private int unUploadedTripSheetsStockListsCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        mDBHelper = new DBHelper(this);
        connectionDetector = new NetworkConnectionDetector(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        actionType = intent.getStringExtra("actionType");

        syncTripSheetStockListDataWithServer();

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

    private void syncTripSheetStockListDataWithServer() {
        try {
            List<TripsheetsStockList> unUploadedStockLists = mDBHelper.fetchUnUploadedTripSheetStockList(actionType);
            unUploadedTripSheetsStockListsCount = unUploadedStockLists.size();

            for (TripsheetsStockList stockList : unUploadedStockLists) {
                if (connectionDetector.isNetworkConnected())
                    new SyncTripSheetStockAsyncTask().execute(stockList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SyncTripSheetStockAsyncTask extends AsyncTask<TripsheetsStockList, Void, Void> {
        private TripsheetsStockList currentStock;

        @Override
        protected Void doInBackground(TripsheetsStockList... stockList) {
            try {
                currentStock = stockList[0];

                JSONArray productCodesArray = new JSONArray();
                productCodesArray.put(currentStock.getmTripsheetStockProductCode());

                JSONObject requestObj = new JSONObject();
                requestObj.put("stock_id", currentStock.getmTripsheetStockId());
                requestObj.put("trip_id", currentStock.getmTripsheetStockTripsheetId());
                requestObj.put("product_codes", productCodesArray);
                requestObj.put("action", actionType);

                if (actionType.equals("dispatch")) {
                    JSONArray quantityArray = new JSONArray();
                    quantityArray.put(currentStock.getmTripsheetStockDispatchQuantity());

                    requestObj.put("quantity", quantityArray);
                    requestObj.put("action_by", currentStock.getmTripsheetStockDispatchBy());
                    requestObj.put("date", Utility.formatTime(Long.parseLong(currentStock.getmTripsheetStockDispatchDate()), Constants.TRIP_SHEETS_STOCK_UPDATE_DATE_FORMAT));
                } else {
                    JSONArray quantityArray = new JSONArray();
                    quantityArray.put(currentStock.getmTripsheetStockVerifiedQuantity());

                    requestObj.put("quantity", quantityArray);
                    requestObj.put("action_by", currentStock.getmTripsheetStockVerifyBy());
                    requestObj.put("date", Utility.formatTime(Long.parseLong(currentStock.getmTripsheetStockVerifiedDate()), Constants.TRIP_SHEETS_STOCK_UPDATE_DATE_FORMAT));
                }

                String requestURL = String.format("%s%s%s/update", Constants.MAIN_URL, Constants.SYNC_TRIPSHEETS_PORT, Constants.GET_TRIPSHEETS_STOCK_LIST);

                String responseString = new NetworkManager().makeHttpPostConnection(requestURL, requestObj);

                if (responseString != null) {
                    JSONObject resultObj = new JSONObject(responseString);

                    if (resultObj.getInt("result_status") == 1) {
                        // if success, we are updating stock status as uploaded in local db.
                        mDBHelper.updateTripSheetStockTable(currentStock.getmTripsheetStockId(), actionType);
                    }

                    unUploadedTripSheetsStockListsCount--;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (unUploadedTripSheetsStockListsCount == 0) {
                stopSelf();
                //System.out.println("Sync TDC Sales Order Service Stopped Automatically....");
            }
        }
    }
}
