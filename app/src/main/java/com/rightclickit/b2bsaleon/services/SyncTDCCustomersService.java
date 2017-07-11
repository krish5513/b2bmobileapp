package com.rightclickit.b2bsaleon.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.rightclickit.b2bsaleon.beanclass.TDCCustomer;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.NetworkManager;
import com.rightclickit.b2bsaleon.util.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by venkat on 7/11/17.
 */

public class SyncTDCCustomersService extends Service {
    private DBHelper mDBHelper;
    private MMSharedPreferences mmSharedPreferences;
    private NetworkConnectionDetector connectionDetector;
    private int unUploadedTDCCustomersCount = 0;
    private String loggedInUserId, retailerStakeTypeId, consumerStakeTypeId;

    @Override
    public void onCreate() {
        super.onCreate();
        mDBHelper = new DBHelper(this);
        mmSharedPreferences = new MMSharedPreferences(this);
        connectionDetector = new NetworkConnectionDetector(this);

        loggedInUserId = mmSharedPreferences.getString("userId");
        retailerStakeTypeId = mDBHelper.getStakeTypeIdByStakeType("3");
        consumerStakeTypeId = mDBHelper.getStakeTypeIdByStakeType("4");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        syncTDCCustomersDataWithServer();

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

    private void syncTDCCustomersDataWithServer() {
        try {
            List<TDCCustomer> unUploadedTDCCustomers = mDBHelper.fetchAllUnUploadedRecordsFromTDCCustomers();
            unUploadedTDCCustomersCount = unUploadedTDCCustomers.size();
            System.out.println("========== unUploadedTDCCustomersCount = " + unUploadedTDCCustomersCount);
            for (TDCCustomer customer : unUploadedTDCCustomers) {
                if (connectionDetector.isNetworkConnected())
                    new SyncTDCCustomerAsyncTask().execute(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SyncTDCCustomerAsyncTask extends AsyncTask<TDCCustomer, Void, Void> {
        private TDCCustomer currentTDCCustomer;

        @Override
        protected Void doInBackground(TDCCustomer... tdcCustomers) {
            try {
                currentTDCCustomer = tdcCustomers[0];

                HashMap<String, String> userRouteIds = mDBHelper.getUserRouteIds();
                System.out.println("===== userRouteIds = " + userRouteIds);
                JSONArray routesArray = new JSONObject(userRouteIds.get("route_ids")).getJSONArray("routeArray");
                String routeId = "";
                if (routesArray.length() > 0)
                    routeId = routesArray.getString(0);

                String createdBy = loggedInUserId;
                String createdTime = Utility.formatDate(new Date(), Constants.CUSTOMER_ADD_DATE_FORMAT);

                JSONObject requestObj = new JSONObject();
                requestObj.put("route_id", routeId);
                requestObj.put("stakeholder_id", (currentTDCCustomer.getCustomerType() == 1 ? retailerStakeTypeId : consumerStakeTypeId));
                requestObj.put("first_name", currentTDCCustomer.getName());
                requestObj.put("last_name", currentTDCCustomer.getBusinessName());
                requestObj.put("phone", currentTDCCustomer.getMobileNo());
                requestObj.put("email", "");
                requestObj.put("password", Utility.getMd5String("123456789"));
                requestObj.put("code", "");
                requestObj.put("reporting_to", "");
                requestObj.put("verify_code", "");
                requestObj.put("address", currentTDCCustomer.getAddress());
                requestObj.put("avatar", "");
                requestObj.put("approved_on", "");
                requestObj.put("device_sync", "0");
                requestObj.put("access_device", "NO");
                requestObj.put("back_up", "0");
                requestObj.put("status", "A");
                requestObj.put("delete", "N");
                requestObj.put("created_by", createdBy);
                requestObj.put("created_on", createdTime);
                requestObj.put("updated_on", createdTime);
                requestObj.put("updated_by", createdBy);
                System.out.println("====== requestObj = " + requestObj);
                String addCustomerURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.PORT_AGENTS_LIST, Constants.GET_CUSTOMERS_ADD);
                String responseString = new NetworkManager().makeHttpPostConnection(addCustomerURL, requestObj);
                System.out.println("======= requestURL = " + addCustomerURL);
                System.out.println("responseString = " + responseString);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (unUploadedTDCCustomersCount == 0) {
                stopSelf();
                System.out.println("Sync TDC Sales Order Service Stopped Automatically....");
            }
        }
    }
}
