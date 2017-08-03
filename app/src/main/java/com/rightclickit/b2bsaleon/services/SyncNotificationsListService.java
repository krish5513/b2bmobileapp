package com.rightclickit.b2bsaleon.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.rightclickit.b2bsaleon.beanclass.NotificationBean;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.NetworkManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SyncNotificationsListService extends Service {
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
        fetchAndSyncNotificationsData();

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

    private void fetchAndSyncNotificationsData() {
        try {
            if (new NetworkConnectionDetector(this).isNetworkConnected()) {
                new FetchAndSyncNotificationsDataAsyncTask().execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Class to perform background operations.
     */
    private class FetchAndSyncNotificationsDataAsyncTask extends AsyncTask<Void, Void, Void> {

        private ArrayList<NotificationBean> mNotificationsList = new ArrayList<NotificationBean>();

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (mNotificationsList.size() > 0) {
                    mNotificationsList.clear();
                }

                String URL = String.format("%s%s%s", Constants.MAIN_URL, Constants.SYNC_NOTIFICATIONS_PORT, Constants.GET_NOTIFICATIONS_LIST);

                JSONObject params1 = new JSONObject();

                mJsonObj = new NetworkManager().makeHttpPostConnection(URL, params1);

                //System.out.println("The URL IS:: "+ URL);

                //System.out.println("The LENGTH IS:: "+ mJsonObj.toString());
                //System.out.println("Notifications Response Is::: " + mJsonObj);
                // JSONObject responseObj = new JSONObject(mJsonObj);
                ///if (responseObj.getInt("result_status") != 0) {
                if (mJsonObj != null && !mJsonObj.equals("error")) {
                    JSONArray resArray = new JSONArray(mJsonObj);
                    int len = resArray.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject jb = resArray.getJSONObject(i);

                        NotificationBean notificationsBean = new NotificationBean();
                        notificationsBean.setNotification_id(jb.getString("_id"));
                        notificationsBean.setName(jb.getString("name"));
                        notificationsBean.setDescription(jb.getString("description"));
                        notificationsBean.setDate(jb.getString("created_on"));

                        mNotificationsList.add(notificationsBean);
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
            mDBHelper.insertNotificationsListData(mNotificationsList);
        }
    }
}