package com.rightclickit.b2bsaleon.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.beanclass.SpecialPriceBean;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderBean;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SyncSpecialPriceService extends Service {
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
        fetchAndSyncSpecialPriceData();

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

    private void fetchAndSyncSpecialPriceData() {
        try {
            new SyncSpecialPriceService.FetchAndSyncSpecialPriceDataAsyncTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Class to perform background operations.
     */
    private class FetchAndSyncSpecialPriceDataAsyncTask extends AsyncTask<Void, Void, Void> {

        private ArrayList<SpecialPriceBean> mSpecialPriceList = new ArrayList<SpecialPriceBean>();

        private ArrayList<SpecialPriceBean> temptoList = new ArrayList<SpecialPriceBean>();


        @Override
        protected Void doInBackground(Void... params) {
            try {
//                if (mDBHelper.getRouteId().length()>0){
//                    // Clear the db first and then insert..
//                    mDBHelper.deleteValuesFromRoutesTable();
//

//                mSpecialPriceList=mDBHelper.fetchAllRecordsFromSpecialPriceTable();
//                System.out.println("BEFORE SERVICE:: "+ mSpecialPriceList.size());
                if (mSpecialPriceList.size() > 0) {
                    mSpecialPriceList.clear();
                }
                String URL = String.format("%s%s%s", Constants.MAIN_URL, Constants.PORT_USER_PREVILEGES, Constants.SPECIAL_PRICE_SERVICE);

                JSONObject params1 = new JSONObject();

                mJsonObj = new NetworkManager().makeHttpPostConnection(URL, params1);
                System.out.println("Special Price Response Is::: " + mJsonObj);
                JSONArray resArray = new JSONArray(mJsonObj);
                int len = resArray.length();
                for (int i = 0; i < len; i++) {
                    if (!resArray.get(i).toString().equals("null")) {
                        if (resArray.getJSONObject(i).toString() != null) {
                            JSONObject jb = resArray.getJSONObject(i);

                            SpecialPriceBean specialPriceBean = new SpecialPriceBean();
                            specialPriceBean.setSpecialUserId(jb.getString("user_id"));
                            specialPriceBean.setSpecialProductId(jb.getString("prod_id"));
                            specialPriceBean.setSpecialPrice(jb.getString("price"));

                            mSpecialPriceList.add(specialPriceBean);
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
//            Intent i = new Intent("android.intent.action.MAIN").putExtra("receiver_key", "agents");
//            getBaseContext().sendBroadcast(i);
            mDBHelper.insertSpecialPriceDetails(mSpecialPriceList);
            stopSelf();
        }
    }
}