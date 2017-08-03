package com.rightclickit.b2bsaleon.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.rightclickit.b2bsaleon.beanclass.PaymentsBean;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsList;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.NetworkManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SyncTripSheetsPaymentsService extends Service {
    private Runnable runnable;
    private Handler handler;
    private DBHelper mDBHelper;
    private String mJsonObj;
    private MMSharedPreferences mSessionManagement;
    private NetworkConnectionDetector connectionDetector;

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
        fetchAndSyncTripsheetsPaymentsData();

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

    private void fetchAndSyncTripsheetsPaymentsData() {
        try {
            ArrayList<PaymentsBean> paymentsBeanArrayList = mDBHelper.fetchAllTripsheetsPaymentsList();
            System.out.println("Size::: " + paymentsBeanArrayList.size());
            if (paymentsBeanArrayList.size() > 0) {
                for (PaymentsBean paymentsBe : paymentsBeanArrayList) {
                    if (connectionDetector.isNetworkConnected())
                        new FetchAndSyncTripsheetsPaymentsDataAsyncTask().execute(paymentsBe);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Class to perform background operations.
     */
    private class FetchAndSyncTripsheetsPaymentsDataAsyncTask extends AsyncTask<PaymentsBean, Void, Void> {

        private PaymentsBean paymentBean;
        private ArrayList<TripsheetsList> mTripsheetsList = new ArrayList<TripsheetsList>();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


        @Override
        protected Void doInBackground(PaymentsBean... params) {
            try {
                paymentBean = params[0];
                System.out.println("F111::: " + paymentBean.getPayments_saleOrderId());
                System.out.println("F222::: " + paymentBean.getPayments_saleOrderCode());
                String URL = String.format("%s%s%s", Constants.MAIN_URL, Constants.SYNC_TAKE_ORDERS_PORT, Constants.TRIPSHEETS_PAYMENTS_URL);

                JSONObject params1 = new JSONObject();
                params1.put("payment_no", "");
                params1.put("trip_id", paymentBean.getPayments_tripsheetId());
                params1.put("user_id", paymentBean.getPayments_userId());
                params1.put("agent_code", paymentBean.getPayments_userCodes());
                params1.put("sale_order_id", paymentBean.getPayments_saleOrderId());
                params1.put("sale_order_code", paymentBean.getPayments_saleOrderCode());
                params1.put("route_id", paymentBean.getPayments_routeId());
                params1.put("route_code", paymentBean.getPayments_routeCodes());
                params1.put("che_trans_id", paymentBean.getPayments_chequeNumber()); // Cheque Number
                params1.put("ac_ca_no", "");
                params1.put("account_name", "");
                params1.put("bank_name", paymentBean.getPayments_bankName());
                params1.put("trans_date", paymentBean.getPayments_chequeDate()); // Cheque Date
                params1.put("trans_clear_date", "");
                params1.put("receiver_name", ""); // Company name
                params1.put("trans_status", paymentBean.getPayments_transActionStatus());
                params1.put("recieved_amt", String.valueOf(paymentBean.getPayments_receivedAmount()));
                params1.put("type", paymentBean.getPayments_type());
                params1.put("status", paymentBean.getPayments_status());
                params1.put("delete", paymentBean.getPayments_delete());
                params1.put("created_by", mSessionManagement.getString("userId"));
                params1.put("created_on", timeStamp);
                params1.put("updated_on", timeStamp);
                params1.put("updated_by", mSessionManagement.getString("userId"));

                System.out.println("URL::: " + URL);
                System.out.println("INPUT::: " + params1.toString());
                mJsonObj = new NetworkManager().makeHttpPostConnection(URL, params1);
                // System.out.println("Tripsheets payments list Response Is::: " + mJsonObj);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            stopSelf();
            // mDBHelper.insertTripsheetsListData(mTripsheetsList);
        }
    }
}