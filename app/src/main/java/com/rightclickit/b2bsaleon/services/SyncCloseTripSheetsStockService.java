package com.rightclickit.b2bsaleon.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
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
import java.util.List;

/**
 * Created by Sekhar Kuppa
 */

public class SyncCloseTripSheetsStockService extends Service {
    private DBHelper mDBHelper;
    private NetworkConnectionDetector connectionDetector;
    private String currentDate;
    private MMSharedPreferences mPreferences;
    private int unUploadedTripSheetsStockIdsCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        mDBHelper = new DBHelper(this);
        connectionDetector = new NetworkConnectionDetector(this);

        mPreferences = new MMSharedPreferences(this);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = df.format(cal.getTime());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

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
        JSONArray routeReturnsArray;
        JSONArray leakageArray;
        JSONArray othersArray;
        String actionType;
        String action_by;
        String date;

        public JSONArray getProductCodesArray() {
            return productCodesArray;
        }

        public void setProductCodesArray(JSONArray productCodesArray) {
            this.productCodesArray = productCodesArray;
        }

        public JSONArray getRouteReturnsArray() {
            return routeReturnsArray;
        }

        public void setRouteReturnsArray(JSONArray routeReturnsArray) {
            this.routeReturnsArray = routeReturnsArray;
        }

        public JSONArray getLeakageArray() {
            return leakageArray;
        }

        public void setLeakageArray(JSONArray leakageArray) {
            this.leakageArray = leakageArray;
        }

        public JSONArray getOthersArray() {
            return othersArray;
        }

        public void setOthersArray(JSONArray othersArray) {
            this.othersArray = othersArray;
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
            ArrayList<String> unUploadedStockIds = mDBHelper.fetchUnUploadedTripSheetUniqueStockIdsForCloseTrip();
            System.out.println("UnUploaded Stock List::: " + unUploadedStockIds.size());
            unUploadedTripSheetsStockIdsCount = unUploadedStockIds.size();

            for (String stockId : unUploadedStockIds) {
                List<TripsheetsStockList> unUploadedStockLists = mDBHelper.fetchUnUploadedTripSheetStockList(stockId);
                System.out.println("UnUploaded Stock List 22222::: " + unUploadedStockLists.size());

                TripSheetStockListWithProducts stockListWithProducts = null;
                JSONArray productCodesArray = new JSONArray();
                JSONArray routeReturnsArray = new JSONArray();
                JSONArray leakageArray = new JSONArray();
                JSONArray othersArray = new JSONArray();
                for (int i = 0; i < unUploadedStockLists.size(); i++) {
                    TripsheetsStockList currentStockDetails = unUploadedStockLists.get(i);
                    if (i == 0) {
                        stockListWithProducts = new TripSheetStockListWithProducts();
                        stockListWithProducts.setmTripsheetStockId(currentStockDetails.getmTripsheetStockId());
                        stockListWithProducts.setmTripsheetStockTripsheetId(currentStockDetails.getmTripsheetStockTripsheetId());
                    }

                    productCodesArray.put(currentStockDetails.getmTripsheetStockProductCode());

                    if (currentStockDetails.getmRouteReturnQuantity() != null) {
                        routeReturnsArray.put(currentStockDetails.getmRouteReturnQuantity());
                    } else {
                        routeReturnsArray.put("0.0");
                    }

                    if (currentStockDetails.getmLeakQuantity() != null) {
                        leakageArray.put(currentStockDetails.getmLeakQuantity());
                    } else {
                        leakageArray.put("0.0");
                    }
                    if (currentStockDetails.getmOtherQuantity() != null) {
                        othersArray.put(currentStockDetails.getmOtherQuantity());
                    } else {
                        othersArray.put("0.0");
                    }

//                    if (actionType.equals("dispatch")) {
//                        if (i == 0) {
//                            stockListWithProducts.setAction_by(currentStockDetails.getmTripsheetStockDispatchBy());
//                            stockListWithProducts.setDate(Utility.formatTime(Long.parseLong(currentStockDetails.getmTripsheetStockDispatchDate()), Constants.SEND_DATA_TO_SERVICE_DATE_FORMAT));
//                        }
//
//                        quantitiesArray.put(currentStockDetails.getmTripsheetStockDispatchQuantity());
//                    } else {
//                        if (i == 0) {
//                            stockListWithProducts.setAction_by(currentStockDetails.getmTripsheetStockVerifyBy());
//                            stockListWithProducts.setDate(Utility.formatTime(Long.parseLong(currentStockDetails.getmTripsheetStockVerifiedDate()), Constants.SEND_DATA_TO_SERVICE_DATE_FORMAT));
//                        }
//                        quantitiesArray.put(currentStockDetails.getmTripsheetStockVerifiedQuantity());
//                    }
                }

                stockListWithProducts.setProductCodesArray(productCodesArray);
                stockListWithProducts.setRouteReturnsArray(routeReturnsArray);
                stockListWithProducts.setLeakageArray(leakageArray);
                stockListWithProducts.setOthersArray(othersArray);

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
                requestObj.put("leakage_qty", currentStock.getLeakageArray());
                requestObj.put("others_qty", currentStock.getOthersArray());
                requestObj.put("route_return_qty", currentStock.getRouteReturnsArray());
                requestObj.put("action_by", mPreferences.getString("userId").toString());
                requestObj.put("date", currentDate);
                System.out.println("requestObj = " + requestObj.toString());

//                String requestURL = String.format("%s%s%s/update", Constants.MAIN_URL, Constants.SYNC_TRIPSHEETS_PORT, Constants.GET_TRIPSHEETS_STOCK_LIST);
//                System.out.println("requestObj = " + requestObj);
//                System.out.println("requestURL = " + requestURL);
//
//                String responseString = new NetworkManager().makeHttpPostConnection(requestURL, requestObj);
//                System.out.println("responseString = " + responseString);
//                if (responseString != null && !(responseString == "error" || responseString == "failure")) {
//                    JSONObject resultObj = new JSONObject(responseString);
//
//                    if (resultObj.getInt("result_status") == 1) {
//                        // if success, we are updating stock status as uploaded in local db.
//                        mDBHelper.updateTripSheetStockTable(currentStock.getmTripsheetStockId(), actionType);
//                    }
//
//                    unUploadedTripSheetsStockIdsCount--;
//                }
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
