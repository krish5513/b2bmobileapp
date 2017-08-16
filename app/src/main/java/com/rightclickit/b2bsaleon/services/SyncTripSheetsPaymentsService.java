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
import com.rightclickit.b2bsaleon.util.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SyncTripSheetsPaymentsService extends Service {
    private Runnable runnable;
    private Handler handler;
    private DBHelper mDBHelper;
    private MMSharedPreferences mSessionManagement;
    private NetworkConnectionDetector connectionDetector;
    private int unUploadedPaymentsTripSheetIdsCount = 0;

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
            unUploadedPaymentsTripSheetIdsCount = paymentsBeanArrayList.size();

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
        String timeStamp = Utility.formatDate(new Date(), Constants.SEND_DATA_TO_SERVICE_DATE_TIME_FORMAT);

        @Override
        protected Void doInBackground(PaymentsBean... params) {
            try {
                paymentBean = params[0];

                JSONObject requestObj = new JSONObject();
                //requestObj.put("payment_no", "");
                requestObj.put("trip_id", paymentBean.getPayments_tripsheetId());
                requestObj.put("user_id", paymentBean.getPayments_userId());
                requestObj.put("agent_code", paymentBean.getPayments_userCodes());
                requestObj.put("sale_order_id", paymentBean.getPayments_saleOrderId());
                requestObj.put("sale_order_code", paymentBean.getPayments_saleOrderCode());
                requestObj.put("route_id", paymentBean.getPayments_routeId());
                requestObj.put("route_code", paymentBean.getPayments_routeCodes());
                requestObj.put("che_trans_id", paymentBean.getPayments_chequeNumber()); // Cheque Number
                requestObj.put("ac_ca_no", "");
                requestObj.put("account_name", "");
                requestObj.put("bank_name", paymentBean.getPayments_bankName());
                requestObj.put("trans_date", paymentBean.getPayments_chequeDate()); // Cheque Date
                requestObj.put("trans_clear_date", "");
                requestObj.put("receiver_name", ""); // Company name
                requestObj.put("trans_status", paymentBean.getPayments_transActionStatus());
                requestObj.put("recieved_amt", String.valueOf(paymentBean.getPayments_receivedAmount()));
                requestObj.put("type", paymentBean.getPayments_type());
                requestObj.put("status", paymentBean.getPayments_status());
                requestObj.put("delete", paymentBean.getPayments_delete());
                requestObj.put("created_by", mSessionManagement.getString("userId"));
                requestObj.put("created_on", timeStamp);
                requestObj.put("updated_on", timeStamp);
                requestObj.put("updated_by", mSessionManagement.getString("userId"));

                String requestURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.SYNC_TAKE_ORDERS_PORT, Constants.TRIPSHEETS_PAYMENTS_URL);

                String responseString = new NetworkManager().makeHttpPostConnection(requestURL, requestObj);

                System.out.println("requestObj = " + requestObj);
                System.out.println("requestURL = " + requestURL);
                System.out.println("responseString = " + responseString);

                if (responseString != null && !(responseString == "error" || responseString == "failure")) {
                    JSONObject resultObj = new JSONObject(responseString);

                    if (resultObj.getInt("result_status") == 1) {
                        mDBHelper.updateTripsheetsPaymentsUploadStatus(resultObj.getString("insert_no"), paymentBean.getPayments_tripsheetId(), paymentBean.getPayments_saleOrderId(), paymentBean.getPayments_paymentsNo());
                    }
                }
                unUploadedPaymentsTripSheetIdsCount--;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (unUploadedPaymentsTripSheetIdsCount == 0) {
                stopSelf();
            }
        }
    }
}