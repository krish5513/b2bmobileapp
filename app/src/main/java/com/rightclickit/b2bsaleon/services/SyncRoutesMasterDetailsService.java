package com.rightclickit.b2bsaleon.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.rightclickit.b2bsaleon.activities.DashboardActivity;
import com.rightclickit.b2bsaleon.activities.SettingsActivity;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sekhar Kuppa on 19-05-2017.
 */

public class SyncRoutesMasterDetailsService extends Service {
    private Runnable runnable;
    private Handler handler;
    private DBHelper mDBHelper;
    private String mJsonObj;
    private MMSharedPreferences mSessionManagement;
    private String mRouteIdIs;

    ArrayList<String> routeIdsList = new ArrayList<String>();
    ArrayList<String> regionNamesList = new ArrayList<String>();
    ArrayList<String> officeNamesList = new ArrayList<String>();
    ArrayList<String> routeCodelist = new ArrayList<String>();
    ArrayList<String> routeNamesList = new ArrayList<String>();
    HashMap<String, String> regionIdsList = new HashMap<String, String>();

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        mDBHelper = new DBHelper(this);
        mSessionManagement = new MMSharedPreferences(this);
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

    private void fetchAndSyncRoutesMasterData() {
        try {
            new FetchAndSyncRoutesMasterDetailsAsyncTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Class to perform background operations.
     */
    private class FetchAndSyncRoutesMasterDetailsAsyncTask extends AsyncTask<Void, Void, Void> {
        private String routeId = "", routeName = "", regionId = "", regionName = "", officeId = "", officeName = "", mStoredRouteIds = "", routeCode = "";
        private HashMap<String, String> routeIdsMap = new HashMap<String, String>();


        @Override
        protected Void doInBackground(Void... params) {
            try {
                // System.out.println("USER DEF ROUTE IDSSSS::: "+ mSessionManagement.getString("userId"));
                routeIdsMap = mDBHelper.getUserRouteIds();
                mStoredRouteIds = routeIdsMap.get("route_ids").toString();
                // System.out.println("S ROUTE IDSSSS::: "+ routeIdsMap.get("route_ids").toString());
                if (mStoredRouteIds == null) {
                    mStoredRouteIds = "";
                }
           // JSONArray regionidArray=new JSONArray();
             //   regionidArray.put(mStoredRouteIds);
             //  JSONObject parm=new JSONObject();
              //  parm.put("route_ids",regionidArray);

                if (mDBHelper.getRouteId().length() > 0) {
                    // Clear the db first and then insert..
                    mDBHelper.deleteValuesFromRoutesTable();
                }
                String URL = String.format("%s%s%s", Constants.MAIN_URL, Constants.PORT_ROUTES_MASTER_DATA, Constants.ROUTEID_SERVICE);

                mJsonObj = new NetworkManager().makeHttpGetConnection(URL);

                JSONArray resultArray = new JSONArray(mJsonObj);
                //  System.out.println("The LENGTH IS:: " + resultArray.length());
                if (resultArray != null) {
                    if (resultArray.length() > 0) {
//                        if(mStoredRouteIds.length()>0){
//                            JSONObject routesJob = new JSONObject(mStoredRouteIds);
//                            JSONArray routesArray = routesJob.getJSONArray("routeArray");
//                            for (int s = 0;s<routesArray.length();s++) {
                        for (int i = 0; i < resultArray.length(); i++) {
                            JSONObject obj = resultArray.getJSONObject(i);
                            if (obj.has("_id")) {
                                //  System.out.println("The ROUTE ID IS:: " + obj.getString("_id"));
                                //  System.out.println("The STORED ROUTE ID IS:: " + mSessionManagement.getString("routeId"));
                                routeId = obj.getString("_id");
                                routeIdsList.add(routeId);
//                                        if (routesArray.get(s).toString().equals(routeId)) {
                                if (obj.has("region_id")) {
                                    regionId = obj.getString("region_id");
                                    regionIdsList.put(routeId, regionId);
                                    if (obj.has("regions")) {
                                        JSONArray regionsArray = obj.getJSONArray("regions");
                                        for (int j = 0; j < regionsArray.length(); j++) {
                                            JSONObject regionObj = regionsArray.getJSONObject(j);
                                            if (regionId.equals(regionObj.getString("_id"))) {
                                                mSessionManagement.putString("RegionId", regionId);
                                                if (regionName.equals("")) {
                                                    regionName = regionObj.getString("name");
                                                } else {
                                                    if (!regionObj.getString("name").equals(regionName)) {
                                                        regionName = regionName + "," + regionObj.getString("name");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                if (obj.has("office_id")) {
                                    officeId = obj.getString("office_id");
                                    if (obj.has("offices")) {
                                        JSONArray officesArray = obj.getJSONArray("offices");
                                        for (int k = 0; k < officesArray.length(); k++) {
                                            JSONObject officeObj = officesArray.getJSONObject(k);
                                            if (officeId.equals(officeObj.getString("_id"))) {
                                                if (officeName.equals("")) {
                                                    officeName = officeObj.getString("name");
                                                } else {
                                                    if (!officeObj.getString("name").equals(officeName)) {
                                                        officeName = officeName + "," + officeObj.getString("name");
                                                    }
                                                }
                                                officeNamesList.add(officeName);
                                            }
                                        }
                                    }
                                }
                                if (obj.has("name")) {
                                    //System.out.println("The ROUTE NAME IS:: " + obj.getString("name"));
                                    routeName = obj.getString("name");
                                    routeNamesList.add(routeName);
                                }
                                if (obj.has("code")) {
                                    //System.out.println("The ROUTE NAME IS:: " + obj.getString("name"));
                                    routeCode = obj.getString("code");
                                    routeCodelist.add(routeCode);
                                }

                                System.out.println("ROUTE NAME IS:: " + routeName);
                                System.out.println("REGION NAME IS:: " + regionName);
                                System.out.println("OFFICE NAME IS:: " + officeName);
                                System.out.println("ROUTE CODE IS:: " + routeCode);
                                synchronized (this) {
                                    k = mDBHelper.insertRoutesDetails(routeId, routeName, regionName, officeName, routeCode);
                                    routeId = "";
                                    routeName = "";
                                    regionName = "";
                                    officeName = "";
                                    routeCode = "";
                                    Gson gson = new Gson();
                                    String idsL = gson.toJson(regionIdsList);
                                    mSessionManagement.putString("regionIds", idsL);
                                }
//                                        }
//                                    }
//                                }
                            }
                        }
                    }
                }
//                System.out.println("ROUTE NAMES LIST::: "+ routeNamesList.size());
//                System.out.println("ROUTE CODES LIST::: "+ routeCodelist.size());
//                System.out.println("OFFICE NAMES LIST::: "+ officeNamesList.size());
//                System.out.println("REGION NAMES LIST::: "+ regionNamesList.size());
//                System.out.println("ROUTE IDS LIST::: "+ routeIdsList.size());
//                System.out.println("INSERTED COUNT IS::: "+ k);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            stopSelf();
            if (mDBHelper.getUserDeviceId(mSessionManagement.getString("enterEmail")).equals("")) {
                Intent mainActivityIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainActivityIntent);
            } else {
                Intent mainActivityIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainActivityIntent);
            }
        }
    }

    long k = 0;
}
