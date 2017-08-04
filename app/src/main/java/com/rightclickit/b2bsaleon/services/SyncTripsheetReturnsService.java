package com.rightclickit.b2bsaleon.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.rightclickit.b2bsaleon.beanclass.TripSheetReturnsBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetReturnsBeanWithProducts;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.NetworkManager;
import com.rightclickit.b2bsaleon.util.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Venkat
 */

public class SyncTripsheetReturnsService extends Service {
    private DBHelper mDBHelper;
    private NetworkConnectionDetector connectionDetector;
    private int unUploadedReturnsTripSheetIdsCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        mDBHelper = new DBHelper(this);
        connectionDetector = new NetworkConnectionDetector(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        syncTripSheetReturnsDataWithServer();

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

    private void syncTripSheetReturnsDataWithServer() {
        try {
            ArrayList<String> unUploadedReturnsTripSheetIds = mDBHelper.fetchUnUploadedUniqueReturnsTripSheetIds();
            unUploadedReturnsTripSheetIdsCount = unUploadedReturnsTripSheetIds.size();

            for (String tripSheetId : unUploadedReturnsTripSheetIds) {
                List<TripSheetReturnsBean> unUploadedDeliveries = mDBHelper.fetchAllTripsheetsReturnsList(tripSheetId);

                TripSheetReturnsBeanWithProducts tripSheetReturnsBeanWithProducts = null;
                JSONArray productIdsArray = new JSONArray();
                JSONArray productCodesArray = new JSONArray();
                JSONArray quantityArray = new JSONArray();
                JSONArray returnTypeArray = new JSONArray();

                for (int i = 0; i < unUploadedDeliveries.size(); i++) {
                    TripSheetReturnsBean currentDelivery = unUploadedDeliveries.get(i);

                    if (i == 0) {
                        tripSheetReturnsBeanWithProducts = new TripSheetReturnsBeanWithProducts();
                        tripSheetReturnsBeanWithProducts.setmTripshhetReturnsReturn_no(currentDelivery.getmTripshhetReturnsReturn_no());
                        tripSheetReturnsBeanWithProducts.setmTripshhetReturnsTrip_id(currentDelivery.getmTripshhetReturnsTrip_id());
                        tripSheetReturnsBeanWithProducts.setmTripshhetReturnsUser_id(currentDelivery.getmTripshhetReturnsUser_id());
                        tripSheetReturnsBeanWithProducts.setmTripshhetReturnsUser_codes(currentDelivery.getmTripshhetReturnsUser_codes());
                        tripSheetReturnsBeanWithProducts.setmTripshhetReturns_so_id(currentDelivery.getmTripshhetReturns_so_id());
                        tripSheetReturnsBeanWithProducts.setmTripshhetReturns_so_code(currentDelivery.getmTripshhetReturns_so_code());
                        tripSheetReturnsBeanWithProducts.setmTripshhetReturnsRoute_id(currentDelivery.getmTripshhetReturnsRoute_id());
                        tripSheetReturnsBeanWithProducts.setmTripshhetReturnsRoute_codes(currentDelivery.getmTripshhetReturnsRoute_codes());
                        tripSheetReturnsBeanWithProducts.setmTripshhetReturnsStatus(currentDelivery.getmTripshhetReturnsStatus());
                        tripSheetReturnsBeanWithProducts.setmTripshhetReturnsDelete(currentDelivery.getmTripshhetReturnsDelete());
                        tripSheetReturnsBeanWithProducts.setmTripshhetReturnsCreated_by(currentDelivery.getmTripshhetReturnsCreated_by());
                        tripSheetReturnsBeanWithProducts.setmTripshhetReturnsCreated_on(currentDelivery.getmTripshhetReturnsCreated_on());
                        tripSheetReturnsBeanWithProducts.setmTripshhetReturnsUpdated_by(currentDelivery.getmTripshhetReturnsUpdated_by());
                        tripSheetReturnsBeanWithProducts.setmTripshhetReturnsUpdated_on(currentDelivery.getmTripshhetReturnsUpdated_on());
                    }

                    productIdsArray.put(currentDelivery.getmTripshhetReturnsProduct_ids());
                    productCodesArray.put(currentDelivery.getmTripshhetReturnsProduct_codes());
                    quantityArray.put(currentDelivery.getmTripshhetReturnsQuantity());
                    returnTypeArray.put(currentDelivery.getmTripshhetReturnsType());
                }

                tripSheetReturnsBeanWithProducts.setProductIdsArray(productIdsArray);
                tripSheetReturnsBeanWithProducts.setProductCodesArray(productCodesArray);
                tripSheetReturnsBeanWithProducts.setQuantityArray(quantityArray);
                tripSheetReturnsBeanWithProducts.setReturnTypeArray(returnTypeArray);

                if (connectionDetector.isNetworkConnected())
                    new SyncTripSheetReturnsAsyncTask().execute(tripSheetReturnsBeanWithProducts);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SyncTripSheetReturnsAsyncTask extends AsyncTask<TripSheetReturnsBeanWithProducts, Void, Void> {
        private TripSheetReturnsBeanWithProducts currentProduct;

        @Override
        protected Void doInBackground(TripSheetReturnsBeanWithProducts... stockList) {
            try {
                currentProduct = stockList[0];

                JSONObject requestObj = new JSONObject();
                requestObj.put("return_no", "");
                requestObj.put("trip_id", currentProduct.getmTripshhetReturnsTrip_id());
                requestObj.put("user_id", currentProduct.getmTripshhetReturnsUser_id());
                requestObj.put("user_codes", currentProduct.getmTripshhetReturnsUser_codes());
                requestObj.put("sale_order_id", currentProduct.getmTripshhetReturns_so_id());
                requestObj.put("sale_order_code", currentProduct.getmTripshhetReturns_so_code());
                requestObj.put("route_id", currentProduct.getmTripshhetReturnsRoute_id());
                requestObj.put("route_codes", currentProduct.getmTripshhetReturnsRoute_codes());
                requestObj.put("product_ids", currentProduct.getProductIdsArray());
                requestObj.put("product_codes", currentProduct.getProductCodesArray());
                requestObj.put("quantity", currentProduct.getQuantityArray());
                requestObj.put("type", currentProduct.getReturnTypeArray());
                requestObj.put("status", "A");
                requestObj.put("delete", "N");
                requestObj.put("created_by", currentProduct.getmTripshhetReturnsCreated_by());
                requestObj.put("created_on", Utility.formatTime(Long.parseLong(currentProduct.getmTripshhetReturnsCreated_on()), Constants.TRIP_SHEETS_DELIVERY_ADD_DATE_FORMAT));
                requestObj.put("updated_on", Utility.formatTime(Long.parseLong(currentProduct.getmTripshhetReturnsUpdated_on()), Constants.TRIP_SHEETS_DELIVERY_ADD_DATE_FORMAT));
                requestObj.put("updated_by", currentProduct.getmTripshhetReturnsUpdated_by());

                String requestURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.SYNC_TRIPSHEETS_PORT, Constants.TRIPSHEETS_RETURNS_URL);

                String responseString = new NetworkManager().makeHttpPostConnection(requestURL, requestObj);

                System.out.println("************* SyncTripsheetReturnsService *************");
                System.out.println("======= requestObj = " + requestObj);
                System.out.println("======== requestURL = " + requestURL);
                System.out.println("========= responseString = " + responseString);

                if (responseString != null && !(responseString == "error" || responseString == "failure")) {
                    JSONObject resultObj = new JSONObject(responseString);

                    if (resultObj.getInt("result_status") == 1) {
                        // if success, we are updating stock status as uploaded in local db.
                        mDBHelper.updateTripSheetReturnsTable(currentProduct.getmTripshhetReturnsTrip_id());
                    }

                    unUploadedReturnsTripSheetIdsCount--;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (unUploadedReturnsTripSheetIdsCount == 0) {
                stopSelf();
            }
        }
    }
}
