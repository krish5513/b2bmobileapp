package com.rightclickit.b2bsaleon.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.NetworkManager;
import com.rightclickit.b2bsaleon.util.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Venkat
 */

public class SyncTripSheetsStockService extends Service {
    private DBHelper mDBHelper;
    private NetworkConnectionDetector connectionDetector;
    private String actionType;
    private int unUploadedTripSheetsStockIdsCount = 0;

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

    private class TripSheetStockListWithProducts extends TripsheetsStockList {
        JSONArray productCodesArray;
        JSONArray quantityArray;
        String actionType;
        String action_by;
        String date;

        public JSONArray getProductCodesArray() {
            return productCodesArray;
        }

        public void setProductCodesArray(JSONArray productCodesArray) {
            this.productCodesArray = productCodesArray;
        }

        public JSONArray getQuantityArray() {
            return quantityArray;
        }

        public void setQuantityArray(JSONArray quantityArray) {
            this.quantityArray = quantityArray;
        }

        public String getActionType() {
            return actionType;
        }

        public void setActionType(String actionType) {
            this.actionType = actionType;
        }

        public String getAction_by() {
            return action_by;
        }

        public void setAction_by(String action_by) {
            this.action_by = action_by;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

    private void syncTripSheetStockListDataWithServer() {
        try {
            ArrayList<String> unUploadedStockIds = mDBHelper.fetchUnUploadedTripSheetUniqueStockIds(actionType);
            unUploadedTripSheetsStockIdsCount = unUploadedStockIds.size();

            for (String stockId : unUploadedStockIds) {
                List<TripsheetsStockList> unUploadedStockLists = mDBHelper.fetchUnUploadedTripSheetStockList(stockId);

                TripSheetStockListWithProducts stockListWithProducts = null;
                JSONArray productCodesArray = new JSONArray();
                JSONArray quantitiesArray = new JSONArray();
                System.out.println("STOCK QUANTITY" + unUploadedStockLists.size());
                for (int i = 0; i < unUploadedStockLists.size(); i++) {
                    TripsheetsStockList currentStockDetails = unUploadedStockLists.get(i);
                    System.out.println("P CODE ===== " + currentStockDetails.getmTripsheetStockProductCode());
                    System.out.println("P QAUN ===== " + currentStockDetails.getmTripsheetStockDispatchQuantity());
                    if (i == 0) {
                        stockListWithProducts = new TripSheetStockListWithProducts();
                        stockListWithProducts.setmTripsheetStockId(currentStockDetails.getmTripsheetStockId());
                        stockListWithProducts.setmTripsheetStockTripsheetId(currentStockDetails.getmTripsheetStockTripsheetId());
                        stockListWithProducts.setActionType(actionType);
                    }

                    productCodesArray.put(currentStockDetails.getmTripsheetStockProductCode());

                    if (actionType.equals("dispatch")) {
                        if (i == 0) {
                            stockListWithProducts.setAction_by(currentStockDetails.getmTripsheetStockDispatchBy());
                            //stockListWithProducts.setDate(Utility.formatTime(Long.parseLong(currentStockDetails.getmTripsheetStockDispatchDate()), Constants.SEND_DATA_TO_SERVICE_DATE_FORMAT));
                            String verifiedDate = currentStockDetails.getmTripsheetStockDispatchDate();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                Date formatedDate = format.parse(verifiedDate);
                                stockListWithProducts.setDate(Utility.formatTime(formatedDate.getTime(), Constants.SEND_DATA_TO_SERVICE_DATE_FORMAT));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        quantitiesArray.put(currentStockDetails.getmTripsheetStockDispatchQuantity());
                    } else {
                        if (i == 0) {
                            stockListWithProducts.setAction_by(currentStockDetails.getmTripsheetStockVerifyBy());
                            String verifiedDate = currentStockDetails.getmTripsheetStockVerifiedDate();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                Date formatedDate = format.parse(verifiedDate);
                                stockListWithProducts.setDate(Utility.formatTime(formatedDate.getTime(), Constants.SEND_DATA_TO_SERVICE_DATE_FORMAT));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            //stockListWithProducts.setDate(Utility.formatTime(Long.parseLong(currentStockDetails.getmTripsheetStockVerifiedDate()), Constants.SEND_DATA_TO_SERVICE_DATE_FORMAT));
                        }
                        quantitiesArray.put(currentStockDetails.getmTripsheetStockVerifiedQuantity());
                    }
                }

                stockListWithProducts.setProductCodesArray(productCodesArray);
                stockListWithProducts.setQuantityArray(quantitiesArray);

                if (connectionDetector.isNetworkConnected())
                    new SyncTripSheetStockAsyncTask().execute(stockListWithProducts);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SyncTripSheetStockAsyncTask extends AsyncTask<TripSheetStockListWithProducts, Void, Void> {
        private TripSheetStockListWithProducts currentStock;

        @Override
        protected Void doInBackground(TripSheetStockListWithProducts... stockList) {
            try {
                currentStock = stockList[0];

                JSONArray productCodesArray = new JSONArray();
                productCodesArray.put(currentStock.getmTripsheetStockProductCode());

                JSONObject requestObj = new JSONObject();
                requestObj.put("stock_id", currentStock.getmTripsheetStockId());
                requestObj.put("trip_id", currentStock.getmTripsheetStockTripsheetId());
                requestObj.put("product_codes", currentStock.getProductCodesArray());
                requestObj.put("quantity", currentStock.getQuantityArray());
                requestObj.put("action", currentStock.getActionType());
                requestObj.put("action_by", currentStock.getAction_by());
                requestObj.put("date", currentStock.getDate());

                String requestURL = String.format("%s%s%s/update", Constants.MAIN_URL, Constants.SYNC_TRIPSHEETS_PORT, Constants.GET_TRIPSHEETS_STOCK_LIST);
                System.out.println("requestObj = " + requestObj);
                System.out.println("requestURL = " + requestURL);

                String responseString = new NetworkManager().makeHttpPostConnection(requestURL, requestObj);
                System.out.println("responseString = " + responseString);
                if (responseString != null && !(responseString == "error" || responseString == "failure")) {
                    JSONObject resultObj = new JSONObject(responseString);

                    if (resultObj.getInt("result_status") == 1) {
                        // if success, we are updating stock status as uploaded in local db.
                        mDBHelper.updateTripSheetStockTable(currentStock.getmTripsheetStockId(), actionType);
                    }

                    unUploadedTripSheetsStockIdsCount--;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (unUploadedTripSheetsStockIdsCount == 0) {
                stopSelf();
                //System.out.println("Sync TDC Sales Order Service Stopped Automatically....");
            }
        }
    }
}
