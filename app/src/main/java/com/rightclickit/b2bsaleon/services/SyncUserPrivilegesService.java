package com.rightclickit.b2bsaleon.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.rightclickit.b2bsaleon.activities.LoginActivity;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.NetworkManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sekhar Kuppa on 19-05-2017.
 */

public class SyncUserPrivilegesService extends Service {
    private Runnable runnable;
    private Handler handler;
    private DBHelper mDBHelper;
    private String mJsonObj;
    private MMSharedPreferences mSessionManagement;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        mDBHelper = new DBHelper(this);
        mSessionManagement = new MMSharedPreferences(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        fetchAndSyncUserPrivilegesData();

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

    private void fetchAndSyncUserPrivilegesData() {
        try {
            new FetchAndSyncUserPrivilegesDataAsyncTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Class to perform background operations.
     */
    private class FetchAndSyncUserPrivilegesDataAsyncTask extends AsyncTask<Void, Void, Void> {
        private String userId = "", userPrivilegeName = "", userPrivilegeId = "", userPrivilegeStatus = "";
        private ArrayList<String> IDSLIST = new ArrayList<String>();
        private ArrayList<String> NAMESLIST = new ArrayList<String>();
        private HashMap<String, Object> ACTIONSLIST = new HashMap<String, Object>();

        @Override
        protected Void doInBackground(Void... params) {
            try {
//                if (mDBHelper.getRouteId().length()>0){
//                    // Clear the db first and then insert..
//                    mDBHelper.deleteValuesFromRoutesTable();
//                }
                userId = mSessionManagement.getString("userId");
                String URL = String.format("%s%s%s%s", Constants.MAIN_URL, Constants.GET_USER_PREVILEGES_SERVICE, mSessionManagement.getString("stakeId"), Constants.PRIVILEGE_CODE);

                mJsonObj = new NetworkManager().makeHttpGetConnection(URL);

                JSONArray resultArray = new JSONArray(mJsonObj);
                System.out.println("The LENGTH IS:: " + resultArray.length());
                if (resultArray != null) {
                    if (resultArray.length() > 0) {
                        for (int i = 0; i < resultArray.length(); i++) {
                            JSONObject obj = resultArray.getJSONObject(i);
                            if (obj.has("id")) {
                                if (obj.has("name") && !obj.getString("name").equals("")) {
                                    // Start Of User Activity
                                    if (obj.getString("name").equals("UserActivity")) {
                                        mSessionManagement.putString("UserActivity", obj.getString("id"));
                                        IDSLIST.add(obj.getString("id"));
                                        NAMESLIST.add(obj.getString("name"));
                                        if (obj.has("actions")) {
                                            JSONArray userActionsArray = obj.getJSONArray("actions");
                                            ACTIONSLIST.put(obj.getString("id"), userActionsArray);
                                            if (userActionsArray.length() > 0) {
                                                for (int j = 0; j < userActionsArray.length(); j++) {
                                                    JSONObject actObj = userActionsArray.getJSONObject(j);
                                                    if (actObj.has("tag")) {
                                                        if (actObj.getString("tag").equals("setup")) {
                                                            mSessionManagement.putString("isSetup", "visible");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } // End of User Activity

                                    // Start Of User Dashboard
                                    if (obj.getString("name").equals("Dashboard")) {
                                        mSessionManagement.putString("isDashboard", "visible");
                                        mSessionManagement.putString("Dashboard", obj.getString("id"));
                                        IDSLIST.add(obj.getString("id"));
                                        NAMESLIST.add(obj.getString("name"));
                                        if (obj.has("actions")) {
                                            JSONArray userActionsArray = obj.getJSONArray("actions");
                                            ACTIONSLIST.put(obj.getString("id"), userActionsArray);
                                        }
                                    } // End of User Dashboard

                                    // Start Of User Tripsheets
                                    if (obj.getString("name").equals("TripSheets")) {
                                        mSessionManagement.putString("isTripsheets", "visible");
                                        mSessionManagement.putString("TripSheets", obj.getString("id"));
                                        IDSLIST.add(obj.getString("id"));
                                        NAMESLIST.add(obj.getString("name"));
                                        if (obj.has("actions")) {
                                            JSONArray userActionsArray = obj.getJSONArray("actions");
                                            ACTIONSLIST.put(obj.getString("id"), userActionsArray);
                                        }
                                    } // End of User Tripsheets

                                    // Start Of User Customers
                                    if (obj.getString("name").equals("Customers")) {
                                        mSessionManagement.putString("isCustomers", "visible");
                                        mSessionManagement.putString("Customers", obj.getString("id"));
                                        IDSLIST.add(obj.getString("id"));
                                        NAMESLIST.add(obj.getString("name"));
                                        if (obj.has("actions")) {
                                            JSONArray userActionsArray = obj.getJSONArray("actions");
                                            ACTIONSLIST.put(obj.getString("id"), userActionsArray);
                                        }
                                    } // End of User Customers

                                    // Start Of User Products
                                    if (obj.getString("name").equals("Products")) {
                                        mSessionManagement.putString("isProducts", "visible");
                                        mSessionManagement.putString("Products", obj.getString("id"));
                                        IDSLIST.add(obj.getString("id"));
                                        NAMESLIST.add(obj.getString("name"));
                                        if (obj.has("actions")) {
                                            JSONArray userActionsArray = obj.getJSONArray("actions");
                                            ACTIONSLIST.put(obj.getString("id"), userActionsArray);
                                        }
                                    } // End of User Products

                                    // Start Of User TDC
                                    if (obj.getString("name").equals("TDC")) {
                                        mSessionManagement.putString("isTdc", "visible");
                                        mSessionManagement.putString("TDC", obj.getString("id"));
                                        IDSLIST.add(obj.getString("id"));
                                        NAMESLIST.add(obj.getString("name"));
                                        if (obj.has("actions")) {
                                            JSONArray userActionsArray = obj.getJSONArray("actions");
                                            ACTIONSLIST.put(obj.getString("id"), userActionsArray);
                                        }
                                    } // End of User TDC

                                    // Start Of User Retailers
                                    if (obj.getString("name").equals("Retailers")) {
                                        mSessionManagement.putString("isRetailers", "visible");
                                        mSessionManagement.putString("Retailers", obj.getString("id"));
                                        IDSLIST.add(obj.getString("id"));
                                        NAMESLIST.add(obj.getString("name"));
                                        if (obj.has("actions")) {
                                            JSONArray userActionsArray = obj.getJSONArray("actions");
                                            ACTIONSLIST.put(obj.getString("id"), userActionsArray);
                                        }
                                    } // End of User Retailers


                                }
                            }
                        }
                        synchronized (this) {
                            if (mDBHelper.getUserPrivilegesTableCount() > 0) {
                                mDBHelper.deleteValuesFromUserActivityTable();
                            }
                            mDBHelper.insertUserActivityDetails(userId, "A", IDSLIST, NAMESLIST);
                        }
                        synchronized (this) {
                            if (mDBHelper.getUserPrivilegesActionsTableCount() > 0) {
                                mDBHelper.deleteValuesFromUserActivityActionsTable();
                            }
                            mDBHelper.insertUserActivityActionsDetails(IDSLIST, ACTIONSLIST);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            stopSelf();
            if (new NetworkConnectionDetector(getApplicationContext()).isNetworkConnected()) {
                startService(new Intent(getApplicationContext(), SyncRoutesMasterDetailsService.class));
            }
            System.out.println("Service Stopped Automatically....");
        }
    }
}
