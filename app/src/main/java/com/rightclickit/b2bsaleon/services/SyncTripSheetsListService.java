package com.rightclickit.b2bsaleon.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.rightclickit.b2bsaleon.beanclass.SpecialPriceBean;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsList;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SyncTripSheetsListService extends Service {
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
        fetchAndSyncTripsheetsListData();

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

    private void fetchAndSyncTripsheetsListData(){
        try {
            new FetchAndSyncTripsheetsListDataAsyncTask().execute();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * Class to perform background operations.
     */
    private class FetchAndSyncTripsheetsListDataAsyncTask extends AsyncTask<Void, Void, Void> {

        private ArrayList<TripsheetsList> mTripsheetsList = new ArrayList<TripsheetsList>();

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if(mTripsheetsList.size()>0){
                    mTripsheetsList.clear();
                }
                String URL = String.format("%s%s%s", Constants.MAIN_URL,Constants.SYNC_TRIPSHEETS_PORT,Constants.GET_TRIPSHEETS_LIST);

                JSONObject params1 = new JSONObject();

                mJsonObj = new NetworkManager().makeHttpPostConnection(URL,params1);
                System.out.println("Tripsheets list Response Is::: "+ mJsonObj);
                JSONArray resArray = new JSONArray(mJsonObj);
                int len = resArray.length();
                for (int i = 0;i<len;i++){
                    JSONObject jb = resArray.getJSONObject(i);

                    TripsheetsList tripsheetsListBean = new TripsheetsList();

                    tripsheetsListBean.setmTripshhetId(jb.getString("_id"));
                    tripsheetsListBean.setmTripshhetCode(jb.getString("code"));
                    tripsheetsListBean.setMy_Id(jb.getString("my_id"));
                    tripsheetsListBean.setmTripshhetDate(jb.getString("date"));
                    tripsheetsListBean.setmTripshhetStatus(jb.getString("status"));
                    tripsheetsListBean.setmTripshhetOBAmount(jb.getString("ob_amt"));
                    tripsheetsListBean.setmTripshhetOrderedAmount(jb.getString("order_amt"));
                    tripsheetsListBean.setmTripshhetReceivedAmount(jb.getString("received_amt"));
                    tripsheetsListBean.setmTripshhetDueAmount("");
                    tripsheetsListBean.setmTripshhetRouteCode("route_code");
                    tripsheetsListBean.setmTripshhetSalesMenCode("salesman_code");
                    tripsheetsListBean.setmTripshhetVehicleNumber("vehicle_no");
                    tripsheetsListBean.setmTripshhetTrasnsporterName("transporter");
                    tripsheetsListBean.setApproved_by(jb.getString("approved_by"));

                    mTripsheetsList.add(tripsheetsListBean);
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            stopSelf();
            mDBHelper.insertTripsheetsListData(mTripsheetsList);
     }
}
}