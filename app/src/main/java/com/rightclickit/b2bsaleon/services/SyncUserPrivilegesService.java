package com.rightclickit.b2bsaleon.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkManager;

import org.json.JSONArray;
import org.json.JSONObject;

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

    private void fetchAndSyncUserPrivilegesData(){
        try {
            new FetchAndSyncUserPrivilegesDataAsyncTask().execute();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * Class to perform background operations.
     */
    private class FetchAndSyncUserPrivilegesDataAsyncTask extends AsyncTask<Void, Void, Void> {
        private String routeId="",routeName = "",regionId = "",regionName="",officeId="",officeName="";

        @Override
        protected Void doInBackground(Void... params) {
            try {
//                if (mDBHelper.getRouteId().length()>0){
//                    // Clear the db first and then insert..
//                    mDBHelper.deleteValuesFromRoutesTable();
//                }
                String URL = String.format("%s%s%s%s", Constants.MAIN_URL,Constants.GET_USER_PREVILEGES_SERVICE,mSessionManagement.getString("stakeId"), Constants.PRIVILEGE_CODE);

                mJsonObj = new NetworkManager().makeHttpGetConnection(URL);

                JSONArray resultArray = new JSONArray(mJsonObj);
                System.out.println("The LENGTH IS:: "+ resultArray.length());
                if (resultArray!=null) {
                    if(resultArray.length()>0){
                        for (int i = 0; i<resultArray.length();i++){
                            JSONObject obj = resultArray.getJSONObject(i);
                            if(obj.has("id")){
                                if(obj.has("name") && !obj.getString("name").equals("")){
                                    // Start Of User Activity
                                    if(obj.getString("name").equals("UserActivity")){
                                        if (obj.has("actions")) {
                                            JSONArray userActionsArray = obj.getJSONArray("actions");
                                            if (userActionsArray.length()>0){
                                                for (int j = 0; j<userActionsArray.length();j++){
                                                    JSONObject actObj = userActionsArray.getJSONObject(j);
                                                    if (actObj.has("tag")){
                                                        if (actObj.getString("tag").equals("setup")){
                                                                mSessionManagement.putString("isSetup","visible");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } // End of User Activity

                                    // Start Of User Dashboard
                                    if(obj.getString("name").equals("Dashboard")){
                                        mSessionManagement.putString("isDashboard","visible");
//                                        if (obj.has("actions")) {
//                                            JSONArray userActionsArray = obj.getJSONArray("actions");
//                                            if (userActionsArray.length()>0){
//                                                for (int j = 0; j<userActionsArray.length();j++){
//                                                    JSONObject actObj = userActionsArray.getJSONObject(j);
//                                                    if (actObj.has("tag")){
//                                                        if (actObj.getString("tag").equals("setup")){
//                                                            mSessionManagement.putString("isSetup","visible");
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
                                    } // End of User Dashboard

                                    // Start Of User Dashboard
                                    if(obj.getString("name").equals("TripSheets")){
                                        mSessionManagement.putString("isTripsheets","visible");
//                                        if (obj.has("actions")) {
//                                            JSONArray userActionsArray = obj.getJSONArray("actions");
//                                            if (userActionsArray.length()>0){
//                                                for (int j = 0; j<userActionsArray.length();j++){
//                                                    JSONObject actObj = userActionsArray.getJSONObject(j);
//                                                    if (actObj.has("tag")){
//                                                        if (actObj.getString("tag").equals("setup")){
//                                                            mSessionManagement.putString("isSetup","visible");
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
                                    } // End of User Dashboard

                                    // Start Of User Dashboard
                                    if(obj.getString("name").equals("Customers")){
                                        mSessionManagement.putString("isCustomers","visible");
//                                        if (obj.has("actions")) {
//                                            JSONArray userActionsArray = obj.getJSONArray("actions");
//                                            if (userActionsArray.length()>0){
//                                                for (int j = 0; j<userActionsArray.length();j++){
//                                                    JSONObject actObj = userActionsArray.getJSONObject(j);
//                                                    if (actObj.has("tag")){
//                                                        if (actObj.getString("tag").equals("setup")){
//                                                            mSessionManagement.putString("isSetup","visible");
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
                                    } // End of User Dashboard

                                    // Start Of User Dashboard
                                    if(obj.getString("name").equals("Products")){
                                        mSessionManagement.putString("isProducts","visible");
//                                        if (obj.has("actions")) {
//                                            JSONArray userActionsArray = obj.getJSONArray("actions");
//                                            if (userActionsArray.length()>0){
//                                                for (int j = 0; j<userActionsArray.length();j++){
//                                                    JSONObject actObj = userActionsArray.getJSONObject(j);
//                                                    if (actObj.has("tag")){
//                                                        if (actObj.getString("tag").equals("setup")){
//                                                            mSessionManagement.putString("isSetup","visible");
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
                                    } // End of User Dashboard

                                    // Start Of User Dashboard
                                    if(obj.getString("name").equals("TDC")){
                                        mSessionManagement.putString("isTdc","visible");
//                                        if (obj.has("actions")) {
//                                            JSONArray userActionsArray = obj.getJSONArray("actions");
//                                            if (userActionsArray.length()>0){
//                                                for (int j = 0; j<userActionsArray.length();j++){
//                                                    JSONObject actObj = userActionsArray.getJSONObject(j);
//                                                    if (actObj.has("tag")){
//                                                        if (actObj.getString("tag").equals("setup")){
//                                                            mSessionManagement.putString("isSetup","visible");
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
                                    } // End of User Dashboard


                                }
                            }
                        }
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            stopSelf();
            System.out.println("Service Stopped Automatically....");
        }
    }
}
