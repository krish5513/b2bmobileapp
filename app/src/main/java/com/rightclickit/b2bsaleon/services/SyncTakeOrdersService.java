package com.rightclickit.b2bsaleon.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.rightclickit.b2bsaleon.beanclass.TakeOrderBean;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sekhar Kuppa
 */

public class SyncTakeOrdersService extends Service {
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

    private void fetchAndSyncUserPrivilegesData(){
        try {
            new FetchAndSyncTakeOrdersDataAsyncTask().execute();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * Class to perform background operations.
     */
    private class FetchAndSyncTakeOrdersDataAsyncTask extends AsyncTask<Void, Void, Void> {
        private String userId="",userPrivilegeName = "",userPrivilegeId = "",userPrivilegeStatus="";
        private ArrayList<String> IDSLIST = new ArrayList<String>();
        private ArrayList<String> NAMESLIST = new ArrayList<String>();
        private ArrayList<TakeOrderBean> mTakeOrderBeansList = new ArrayList<TakeOrderBean>();

        @Override
        protected Void doInBackground(Void... params) {
            try {
//                if (mDBHelper.getRouteId().length()>0){
//                    // Clear the db first and then insert..
//                    mDBHelper.deleteValuesFromRoutesTable();
//                }
                mTakeOrderBeansList = mDBHelper.fetchAllRecordsFromTakeOrderProductsTable();
                userId = mSessionManagement.getString("userId");
                String URL = String.format("%s%s%s%s", Constants.MAIN_URL,Constants.SYNC_TAKE_ORDERS_PORT,Constants.SYNC_TAKE_ORDERS_SERVICE,mSessionManagement.getString("token"));
                JSONObject params1 = new JSONObject();
                params1.put("enquiry_id","EPR2");
                params1.put("user_id",mSessionManagement.getString("userId"));
                params1.put("route_id",mSessionManagement.getString("routeId"));
                JSONArray productArra = new JSONArray();
                JSONArray quantityArra = new JSONArray();
                for (int i = 0; i<mTakeOrderBeansList.size();i++){
                    productArra.put(mTakeOrderBeansList.get(i).getmProductId());
                    quantityArra.put(mTakeOrderBeansList.get(i).getmProductQuantity());
                }
                params1.put("product_ids",productArra);
                params1.put("from_date",mTakeOrderBeansList.get(0).getmProductFromDate());
                params1.put("to_date",mTakeOrderBeansList.get(0).getmProductToDate());
                params1.put("order_type","1");
                params1.put("quantity",quantityArra);
                params1.put("status","I");
                params1.put("delete","N");
                params1.put("created_by",mSessionManagement.getString("createdBy"));
                params1.put("created_on",mSessionManagement.getString("createdOn"));
                params1.put("updated_on",mSessionManagement.getString("updatedOn"));
                params1.put("updated_by",mSessionManagement.getString("updatedBy"));

                mJsonObj = new NetworkManager().makeHttpPostConnection(URL,params1);

                JSONArray resultArray = new JSONArray(mJsonObj);
                System.out.println("The LENGTH IS:: "+ resultArray.length());
                System.out.println("The LENGTH IS:: "+ mJsonObj.toString());
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
