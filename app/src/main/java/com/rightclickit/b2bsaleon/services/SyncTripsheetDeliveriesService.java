package com.rightclickit.b2bsaleon.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.rightclickit.b2bsaleon.beanclass.TripSheetDeliveriesBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetDeliveriesBeanWithProducts;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
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

public class SyncTripsheetDeliveriesService extends Service {
    private DBHelper mDBHelper;
    private NetworkConnectionDetector connectionDetector;
    private int unUploadedDeliveryTripSheetIdsCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        mDBHelper = new DBHelper(this);
        connectionDetector = new NetworkConnectionDetector(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        syncTripSheetDeliveriesListDataWithServer();

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

    private void syncTripSheetDeliveriesListDataWithServer() {
        try {
            ArrayList<String> unUploadedDeliveriesTripSheetIds = mDBHelper.fetchUnUploadedUniqueDeliveryTripSheetIds();
            unUploadedDeliveryTripSheetIdsCount = unUploadedDeliveriesTripSheetIds.size();

            for (String tripSheetId : unUploadedDeliveriesTripSheetIds) {
                List<TripSheetDeliveriesBean> unUploadedDeliveries = mDBHelper.fetchAllTripsheetsDeliveriesList(tripSheetId);

                TripSheetDeliveriesBeanWithProducts tripSheetDeliveriesBeanWithProducts = null;
                JSONArray productIdsArray = new JSONArray();
                JSONArray productCodesArray = new JSONArray();
                JSONArray taxPercentArray = new JSONArray();
                JSONArray unitPriceArray = new JSONArray();
                JSONArray quantityArray = new JSONArray();
                JSONArray amountArray = new JSONArray();
                JSONArray taxAmountArray = new JSONArray();

                for (int i = 0; i < unUploadedDeliveries.size(); i++) {
                    TripSheetDeliveriesBean currentDelivery = unUploadedDeliveries.get(i);

                    if (i == 0) {
                        tripSheetDeliveriesBeanWithProducts = new TripSheetDeliveriesBeanWithProducts();
                        tripSheetDeliveriesBeanWithProducts.setmTripsheetDeliveryNo(currentDelivery.getmTripsheetDeliveryNo());
                        tripSheetDeliveriesBeanWithProducts.setmTripsheetDelivery_tripId(currentDelivery.getmTripsheetDelivery_tripId());
                        tripSheetDeliveriesBeanWithProducts.setmTripsheetDelivery_userId(currentDelivery.getmTripsheetDelivery_userId());
                        tripSheetDeliveriesBeanWithProducts.setmTripsheetDelivery_userCodes(currentDelivery.getmTripsheetDelivery_userCodes());
                        tripSheetDeliveriesBeanWithProducts.setmTripsheetDelivery_so_id(currentDelivery.getmTripsheetDelivery_so_id());
                        tripSheetDeliveriesBeanWithProducts.setmTripsheetDelivery_so_code(currentDelivery.getmTripsheetDelivery_so_code());
                        tripSheetDeliveriesBeanWithProducts.setmTripsheetDelivery_routeId(currentDelivery.getmTripsheetDelivery_routeId());
                        tripSheetDeliveriesBeanWithProducts.setmTripsheetDelivery_routeCodes(currentDelivery.getmTripsheetDelivery_routeCodes());
                        tripSheetDeliveriesBeanWithProducts.setmTripsheetDelivery_TaxTotal(currentDelivery.getmTripsheetDelivery_TaxTotal());
                        tripSheetDeliveriesBeanWithProducts.setmTripsheetDelivery_SaleValue(currentDelivery.getmTripsheetDelivery_SaleValue());
                        tripSheetDeliveriesBeanWithProducts.setmTripsheetDelivery_Status(currentDelivery.getmTripsheetDelivery_Status());
                        tripSheetDeliveriesBeanWithProducts.setmTripsheetDelivery_Delete(currentDelivery.getmTripsheetDelivery_Delete());
                        tripSheetDeliveriesBeanWithProducts.setmTripsheetDelivery_CreatedBy(currentDelivery.getmTripsheetDelivery_CreatedBy());
                        tripSheetDeliveriesBeanWithProducts.setmTripsheetDelivery_CreatedOn(currentDelivery.getmTripsheetDelivery_CreatedOn());
                        tripSheetDeliveriesBeanWithProducts.setmTripsheetDelivery_UpdatedBy(currentDelivery.getmTripsheetDelivery_UpdatedBy());
                        tripSheetDeliveriesBeanWithProducts.setmTripsheetDelivery_UpdatedOn(currentDelivery.getmTripsheetDelivery_UpdatedOn());
                    }

                    productIdsArray.put(currentDelivery.getmTripsheetDelivery_productId());
                    productCodesArray.put(currentDelivery.getmTripsheetDelivery_productCodes());
                    taxPercentArray.put(currentDelivery.getmTripsheetDelivery_TaxPercent());
                    unitPriceArray.put(currentDelivery.getmTripsheetDelivery_UnitPrice());
                    quantityArray.put(currentDelivery.getmTripsheetDelivery_Quantity());
                    amountArray.put(currentDelivery.getmTripsheetDelivery_Amount());
                    taxAmountArray.put(currentDelivery.getmTripsheetDelivery_TaxAmount());
                }

                tripSheetDeliveriesBeanWithProducts.setProductIdsArray(productIdsArray);
                tripSheetDeliveriesBeanWithProducts.setProductCodesArray(productCodesArray);
                tripSheetDeliveriesBeanWithProducts.setTaxPercentArray(taxPercentArray);
                tripSheetDeliveriesBeanWithProducts.setUnitPriceArray(unitPriceArray);
                tripSheetDeliveriesBeanWithProducts.setQuantityArray(quantityArray);
                tripSheetDeliveriesBeanWithProducts.setAmountArray(amountArray);
                tripSheetDeliveriesBeanWithProducts.setTaxAmountArray(taxAmountArray);

                if (connectionDetector.isNetworkConnected())
                    new SyncTripSheetDeliveriesAsyncTask().execute(tripSheetDeliveriesBeanWithProducts);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SyncTripSheetDeliveriesAsyncTask extends AsyncTask<TripSheetDeliveriesBeanWithProducts, Void, Void> {
        private TripSheetDeliveriesBeanWithProducts currentDeliveryBean;

        @Override
        protected Void doInBackground(TripSheetDeliveriesBeanWithProducts... stockList) {
            try {
                currentDeliveryBean = stockList[0];

                JSONObject requestObj = new JSONObject();
                requestObj.put("delivery_no", "");
                requestObj.put("trip_id", currentDeliveryBean.getmTripsheetDelivery_tripId());
                requestObj.put("user_id", currentDeliveryBean.getmTripsheetDelivery_userId());
                requestObj.put("user_codes", currentDeliveryBean.getmTripsheetDelivery_userCodes());
                requestObj.put("sale_order_id", currentDeliveryBean.getmTripsheetDelivery_so_id());
                requestObj.put("sale_order_code", currentDeliveryBean.getmTripsheetDelivery_so_code());
                requestObj.put("route_id", currentDeliveryBean.getmTripsheetDelivery_routeId());
                requestObj.put("route_codes", currentDeliveryBean.getmTripsheetDelivery_routeCodes());
                requestObj.put("product_ids", currentDeliveryBean.getProductIdsArray());
                requestObj.put("product_codes", currentDeliveryBean.getProductCodesArray());
                requestObj.put("tax_percent", currentDeliveryBean.getTaxPercentArray());
                requestObj.put("unit_price", currentDeliveryBean.getUnitPriceArray());
                requestObj.put("quantity", currentDeliveryBean.getQuantityArray());
                requestObj.put("amount", currentDeliveryBean.getAmountArray());
                requestObj.put("tax_amount", currentDeliveryBean.getTaxAmountArray());
                requestObj.put("tax_total", currentDeliveryBean.getmTripsheetDelivery_TaxTotal());
                requestObj.put("sale_value", currentDeliveryBean.getmTripsheetDelivery_SaleValue());
                requestObj.put("status", "A");
                requestObj.put("delete", "N");
                requestObj.put("created_by", currentDeliveryBean.getmTripsheetDelivery_CreatedBy());
                requestObj.put("created_on", Utility.formatTime(Long.parseLong(currentDeliveryBean.getmTripsheetDelivery_CreatedOn()), Constants.TRIP_SHEETS_DELIVERY_ADD_DATE_FORMAT));
                requestObj.put("updated_on", Utility.formatTime(Long.parseLong(currentDeliveryBean.getmTripsheetDelivery_UpdatedOn()), Constants.TRIP_SHEETS_DELIVERY_ADD_DATE_FORMAT));
                requestObj.put("updated_by", currentDeliveryBean.getmTripsheetDelivery_UpdatedBy());

                String requestURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.SYNC_TAKE_ORDERS_PORT, Constants.TRIPSHEETS_DELIVERIES_URL);

                String responseString = new NetworkManager().makeHttpPostConnection(requestURL, requestObj);

                if (responseString != null && !(responseString == "error" || responseString == "failure")) {
                    JSONObject resultObj = new JSONObject(responseString);

                    if (resultObj.getInt("result_status") == 1) {
                        // if success, we are updating stock status as uploaded in local db.
                        mDBHelper.updateTripSheetDeliveriesTable(currentDeliveryBean.getmTripsheetDelivery_tripId());
                    }

                    unUploadedDeliveryTripSheetIdsCount--;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (unUploadedDeliveryTripSheetIdsCount == 0) {
                stopSelf();
            }
        }
    }
}
