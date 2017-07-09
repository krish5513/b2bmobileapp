package com.rightclickit.b2bsaleon.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.StakeHolderTypes;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderBean;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.AsyncRequest;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.NetworkManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Sekhar Kuppa
 */

public class SyncStakeHolderTypesService extends Service {
    private Runnable runnable;
    private Handler handler;
    private DBHelper mDBHelper;
    private String mJsonObj, str_routecode;
    private MMSharedPreferences mSessionManagement;
    private String mRouteName = "", mRegionName = "", mOfficeName = "", mRouteCode = "";
    private NetworkConnectionDetector connectionDetector;
    private ArrayList<StakeHolderTypes> StakeHolderTypesList = new ArrayList<StakeHolderTypes>();

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        mDBHelper = new DBHelper(this);
        mSessionManagement = new MMSharedPreferences(this);
        connectionDetector = new NetworkConnectionDetector(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        fetchAndSyncStakeTypesData();

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

    private void fetchAndSyncStakeTypesData() {
        try {
            if (connectionDetector.isNetworkConnected())
                new FetchAndSyncStakeTypesDataAsyncTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Class to perform background operations.
     */
    private class FetchAndSyncStakeTypesDataAsyncTask extends AsyncTask<Void, Void, Void> {
        private String userId = "", userPrivilegeName = "", userPrivilegeId = "", userPrivilegeStatus = "";
        private ArrayList<String> IDSLIST = new ArrayList<String>();
        private ArrayList<String> NAMESLIST = new ArrayList<String>();
        private ArrayList<TakeOrderBean> mTakeOrderBeansList = new ArrayList<TakeOrderBean>();
        private ArrayList<AgentsBean> mAgentsBeansList = new ArrayList<AgentsBean>();
        private ArrayList<ProductsBean> mProductsBeansList = new ArrayList<ProductsBean>();

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String requestURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.PORT_USER_PREVILEGES, Constants.GET_STAKE_HOLDERS_LIST);
                HashMap<String, String> requestObj = new HashMap<String, String>();
                requestObj.put("type[0]", "1");
                requestObj.put("type[1]", "2");
                requestObj.put("type[2]", "3");
                requestObj.put("type[3]", "4");

                System.out.println("THE STAKE URL IS::: " + requestURL);
                System.out.println("THE STAKE DATA IS::: " + requestObj.toString());

                String responseString = new NetworkManager().makeHttpPostConnectionWithUrlEncoeContentType(requestURL, requestObj);
                System.out.println("RESPONSE STR===== "+responseString);
                JSONArray stakeTypesArray = new JSONArray(responseString);
                int len = stakeTypesArray.length();
                if (len>0){
                    for (int i = 0;i<len;i++){
                        JSONObject job = stakeTypesArray.getJSONObject(i);

                        StakeHolderTypes stakeHolderTypes = new StakeHolderTypes();
                        if (job.has("_id"))
                            stakeHolderTypes.setmStakeHolderTypeId(job.getString("_id"));

                        if (job.has("stake_name"))
                            stakeHolderTypes.setmStakeHolderTypeName(job.getString("stake_name"));

                        if (job.has("stake_type"))
                            stakeHolderTypes.setmStakeHolderType(job.getString("stake_type"));

                        StakeHolderTypesList.add(stakeHolderTypes);
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
            if (StakeHolderTypesList.size()>0){
                if(mDBHelper.getStakeTypesTableCount()>0){
                    mDBHelper.deleteValuesFromStakeTypesTable();
                }
                mDBHelper.insertStakeTypesDetails(StakeHolderTypesList);
            }
            System.out.println("Service Stopped Automatically....");
        }
    }
}
