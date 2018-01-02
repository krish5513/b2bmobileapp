package com.rightclickit.b2bsaleon.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.rightclickit.b2bsaleon.beanclass.DeviceDetails;
import com.rightclickit.b2bsaleon.beanclass.TripSheetDeliveriesBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetDeliveriesBeanWithProducts;
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
 * Created by Sekhar Kuppa
 */

public class SyncDeviceLocationDetailsService extends Service {
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
        syncDeviceDetailsWithServer();

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

    private void syncDeviceDetailsWithServer() {
        try {
            ArrayList<DeviceDetails> dList = mDBHelper.fetchUnUploadedDeviceDetails1();
            unUploadedDeliveryTripSheetIdsCount = dList.size();

            for (int i = 0; i < unUploadedDeliveryTripSheetIdsCount; i++) {
                synchronized (this) {
                    DeviceDetails dbean = new DeviceDetails();

                    dbean.setmDeviceId(dList.get(i).getmDeviceId());
                    dbean.setmDeviceUserId(dList.get(i).getmDeviceUserId());
                    dbean.setmDeviceDate(dList.get(i).getmDeviceDate());
                    dbean.setmDeviceTime(dList.get(i).getmDeviceTime());
                    dbean.setmDeviceLatitude(dList.get(i).getmDeviceLatitude());
                    dbean.setmDeviceLongitude(dList.get(i).getmDeviceLongitude());
                    dbean.setmDeviceSpeed(dList.get(i).getmDeviceSpeed());
                    dbean.setmDeviceuploadFlag(dList.get(i).getmDeviceId());

                    if (connectionDetector.isNetworkConnected())
                        new SyncDeviceDetailsAsyncTask().execute(dbean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SyncDeviceDetailsAsyncTask extends AsyncTask<DeviceDetails, Void, Void> {
        private DeviceDetails deviceDetails;

        @Override
        protected Void doInBackground(DeviceDetails... deviceDetailses) {
            try {
                deviceDetails = deviceDetailses[0];

                JSONObject requestObj = new JSONObject();
                requestObj.put("device_id", deviceDetails.getmDeviceId());
                requestObj.put("user_id", deviceDetails.getmDeviceUserId());
                requestObj.put("date", deviceDetails.getmDeviceDate());
                requestObj.put("time", deviceDetails.getmDeviceTime());
                requestObj.put("longitude", deviceDetails.getmDeviceLongitude());
                requestObj.put("latitude", deviceDetails.getmDeviceLatitude());
                requestObj.put("speed", deviceDetails.getmDeviceSpeed());

                String requestURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.SYNC_TAKE_ORDERS_PORT, Constants.DEVICE_LOCATION_DETAILS_URL);
                System.out.println("requestObj = " + requestObj);
                System.out.println("requestURL = " + requestURL);

                String responseString = new NetworkManager().makeHttpPostConnection(requestURL, requestObj);

                System.out.println("responseString = " + responseString);

                if (responseString != null && !(responseString == "error" || responseString == "failure")) {
                    JSONObject resultObj = new JSONObject(responseString);

                    if (resultObj.getInt("result_status") == 1) {
                        // if success, we are updating stock status as uploaded in local db.
                        mDBHelper.updateDeviceLocationUploadFlag(deviceDetails.getmDeviceUserId(),
                                deviceDetails.getmDeviceDate(), deviceDetails.getmDeviceTime());
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
