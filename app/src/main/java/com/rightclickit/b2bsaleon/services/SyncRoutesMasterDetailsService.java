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

public class SyncRoutesMasterDetailsService extends Service {
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
        fetchAndSyncRoutesMasterData();

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

    private void fetchAndSyncRoutesMasterData(){
        try {
            new FetchAndSyncRoutesMasterDetailsAsyncTask().execute();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * Class to perform background operations.
     */
    private class FetchAndSyncRoutesMasterDetailsAsyncTask extends AsyncTask<Void, Void, Void> {
        private String routeId="",routeName = "",regionId = "",regionName="",officeId="",officeName="";

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (mDBHelper.getRouteId().length()>0){
                    // Clear the db first and then insert..
                    mDBHelper.deleteValuesFromRoutesTable();
                }
                String URL = String.format("%s%s%s", Constants.MAIN_URL,Constants.PORT_ROUTES_MASTER_DATA, Constants.ROUTEID_SERVICE);

                mJsonObj = new NetworkManager().makeHttpGetConnection(URL);

                JSONArray resultArray = new JSONArray(mJsonObj);
                System.out.println("The LENGTH IS:: "+ resultArray.length());
                if (resultArray!=null) {
                    if(resultArray.length()>0){
                        for (int i = 0; i<resultArray.length();i++){
                            JSONObject obj = resultArray.getJSONObject(i);
                            if(obj.has("_id")){
                                System.out.println("The ROUTE ID IS:: "+ obj.getString("_id"));
                                routeId = obj.getString("_id");
                                if(obj.has("region_id")){
                                    regionId = obj.getString("region_id");
                                    if(obj.has("regions")){
                                        JSONArray regionsArray = obj.getJSONArray("regions");
                                        for (int j = 0;j<regionsArray.length();j++){
                                            JSONObject regionObj = regionsArray.getJSONObject(j);
                                            if(regionId.equals(regionObj.getString("_id"))){
                                                regionName = regionObj.getString("name");
                                            }
                                        }
                                    }
                                }
                            }
                            if(obj.has("office_id")){
                                officeId = obj.getString("office_id");
                                if(obj.has("offices")){
                                    JSONArray officesArray = obj.getJSONArray("offices");
                                    for (int k = 0;k<officesArray.length();k++){
                                        JSONObject officeObj = officesArray.getJSONObject(k);
                                        if(officeId.equals(officeObj.getString("_id"))){
                                            officeName = officeObj.getString("name");
                                        }
                                    }
                                }
                            }
                            if(obj.has("name")){
                                System.out.println("The ROUTE NAME IS:: "+ obj.getString("name"));
                                routeName = obj.getString("name");
                            }
                             mDBHelper.insertRoutesDetails(routeId,routeName,regionName,officeName);
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
