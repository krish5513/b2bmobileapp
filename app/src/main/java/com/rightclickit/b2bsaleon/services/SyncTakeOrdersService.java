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

/**
 * Created by Sekhar Kuppa
 */

public class SyncTakeOrdersService extends Service {
    private Runnable runnable;
    private Handler handler;
    private DBHelper mDBHelper;
    private String mJsonObj, str_routecode;
    private MMSharedPreferences mSessionManagement;
    private String mRouteName = "", mRegionName = "", mOfficeName = "", mRouteCode = "";
    private String userId = "", userPrivilegeName = "", userPrivilegeId = "", userPrivilegeStatus = "";
    private ArrayList<String> IDSLIST = new ArrayList<String>();
    private ArrayList<String> NAMESLIST = new ArrayList<String>();
    private ArrayList<TakeOrderBean> mTakeOrderBeansList = new ArrayList<TakeOrderBean>();
    private ArrayList<AgentsBean> mAgentsBeansList = new ArrayList<AgentsBean>();
    private ArrayList<ProductsBean> mProductsBeansList = new ArrayList<ProductsBean>();
    private ArrayList<TakeOrderBean> temptoList = new ArrayList<TakeOrderBean>();
    private int unUploadedDeliveryTripSheetIdsCount = 0;

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

    private void fetchAndSyncUserPrivilegesData() {
        try {
            ArrayList<String> pendingOrderAgents = mDBHelper.fetchAllPendingTakeOrderAgentIds();
            System.out.println("Pending Ids::: " + pendingOrderAgents.size());
            unUploadedDeliveryTripSheetIdsCount = pendingOrderAgents.size();
            for (int g = 0; g < pendingOrderAgents.size(); g++) {
                System.out.println("AGENT ID::: " + pendingOrderAgents.get(g).toString());
                mTakeOrderBeansList = mDBHelper.fetchAllRecordsFromTakeOrderProductsTable("yes", pendingOrderAgents.get(g).toString());
                System.out.println("TO DATA::: " + mTakeOrderBeansList.size());
                new FetchAndSyncTakeOrdersDataAsyncTask().execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Class to perform background operations.
     */
    private class FetchAndSyncTakeOrdersDataAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
//                if (mDBHelper.getRouteId().length()>0){
//                    // Clear the db first and then insert..
//                    mDBHelper.deleteValuesFromRoutesTable();
//                }
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String currentDate = df.format(cal.getTime());
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                //mTakeOrderBeansList = mDBHelper.fetchAllRecordsFromTakeOrderProductsTable("yes", mSessionManagement.getString("agentId"));
                //mAgentsBeansList = mDBHelper.fetchAllRecordsFromAgentsTable(mSessionManagement.getString("userId"));
                //mProductsBeansList = mDBHelper.fetchAllRecordsFromProductsTable();
                //System.out.println("BEFORE SERVICE:: " + mTakeOrderBeansList.size());
                // System.out.println("BEFORE SERVICE PRICE:: "+ mTakeOrderBeansList.get(0).getmAgentPrice());
                userId = mSessionManagement.getString("userId");
                String URL = String.format("%s%s%s%s", Constants.MAIN_URL, Constants.SYNC_TAKE_ORDERS_PORT, Constants.SYNC_TAKE_ORDERS_SERVICE, mSessionManagement.getString("token"));


                JSONObject params1 = new JSONObject();
                String enqId = mTakeOrderBeansList.get(0).getmEnquiryId();
                params1.put("enquiry_id", enqId);
                mSessionManagement.putString("enquiryid", enqId);
                //params1.put("route_id",mSessionManagement.getString("agentrouteId"));
                Log.i("lhdighr", mTakeOrderBeansList.get(0).getmRouteId());

                JSONArray rAr = new JSONArray(mTakeOrderBeansList.get(0).getmRouteId());
                String rCode = mDBHelper.getRouteCodeByRouteId(rAr.get(0).toString());
                params1.put("route_id", rAr.get(0).toString());
                //params1.put("route_id",mAgentsBeansList.get(0).getmAgentRouteId());
                params1.put("user_id", mTakeOrderBeansList.get(0).getmAgentId());
                params1.put("user_code", mTakeOrderBeansList.get(0).getmTakeorderAgentCode());
                params1.put("route_code", rCode);
                JSONArray productArra = new JSONArray();
                JSONArray quantityArra = new JSONArray();
                JSONArray fromDateArra = new JSONArray();
                JSONArray toDateArra = new JSONArray();
                JSONArray productCode = new JSONArray();


                for (int i = 0; i < mTakeOrderBeansList.size(); i++) {
                    productCode.put(mTakeOrderBeansList.get(i).getMtakeorderProductCode());
                    //Log.i("pCode", mTakeOrderBeansList.get(i).getMtakeorderProductCode());
                    productArra.put(mTakeOrderBeansList.get(i).getmProductId());
                    quantityArra.put(mTakeOrderBeansList.get(i).getmProductQuantity());
                    fromDateArra.put(mTakeOrderBeansList.get(i).getmProductFromDate());
                    toDateArra.put(mTakeOrderBeansList.get(i).getmProductToDate());
                }
                params1.put("product_ids", productArra);
                //Log.i("pID", productArra.toString());
                params1.put("product_codes", productCode);
                //Log.i("pCode", productCode.toString());
                params1.put("from_date", fromDateArra);
                params1.put("to_date", toDateArra);
                params1.put("order_type", "1");
                params1.put("quantity", quantityArra);
                params1.put("status", "I");
                params1.put("delete", "N");
                params1.put("approved_on", "");
                params1.put("created_by", mSessionManagement.getString("userId"));
                params1.put("created_on", timeStamp);
                params1.put("updated_on", timeStamp);
                params1.put("updated_by", mSessionManagement.getString("userId"));

                System.out.println("******::: " + params1.toString());
                System.out.println("The URL IS:: " + URL);

                mJsonObj = new NetworkManager().makeHttpPostConnection(URL, params1);
                JSONObject resultObj = new JSONObject(mJsonObj);
                //System.out.println("The LENGTH IS:: " + resultObj.toString());
                if (resultObj.has("result_status")) {
                    if (resultObj.getString("result_status").equals("1")) {
                        if (mTakeOrderBeansList.size() > 0) {
                            if (temptoList.size() > 0) {
                                temptoList.clear();
                            }
                            for (int v = 0; v < mTakeOrderBeansList.size(); v++) {
                                TakeOrderBean t = new TakeOrderBean();
                                t.setmProductId(mTakeOrderBeansList.get(v).getmProductId());
                                t.setmRouteId(mTakeOrderBeansList.get(v).getmRouteId());
                                t.setmProductQuantity(mTakeOrderBeansList.get(v).getmProductQuantity());
                                t.setmProductToDate(mTakeOrderBeansList.get(v).getmProductToDate());
                                t.setmProductStatus("0");
                                t.setmProductFromDate(mTakeOrderBeansList.get(v).getmProductFromDate());
                                t.setmProductOrderType("");
                                t.setmProductTitle(mTakeOrderBeansList.get(v).getmProductTitle());
                                t.setmEnquiryId(mTakeOrderBeansList.get(v).getmEnquiryId());
                                t.setmAgentId(mTakeOrderBeansList.get(v).getmAgentId());
                                t.setMtakeorderProductCode(mTakeOrderBeansList.get(v).getMtakeorderProductCode());
                                t.setmAgentTakeOrderDate(mTakeOrderBeansList.get(v).getmAgentTakeOrderDate());
                                t.setmAgentPrice(mTakeOrderBeansList.get(v).getmAgentPrice());
                                t.setmAgentVAT(mTakeOrderBeansList.get(v).getmAgentVAT());
                                t.setmAgentGST(mTakeOrderBeansList.get(v).getmAgentGST());
                                t.setUom(mTakeOrderBeansList.get(v).getUom());
                                t.setMuploadStatus(1);

                                temptoList.add(t);
                            }
                            //System.out.println("DB called****" + temptoList.size());
                            long upd = mDBHelper.updateTakeOrderDetails(temptoList);
                            //System.out.println("UPDATED VALUES COUNT:: " + upd);
                        }
                        unUploadedDeliveryTripSheetIdsCount--;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (unUploadedDeliveryTripSheetIdsCount == 0) {
                stopSelf();
            }
            if (temptoList.size() > 0) {
                temptoList.clear();
            }
            if (mTakeOrderBeansList.size() > 0) {
                mTakeOrderBeansList.clear();
            }
            System.out.println("Service Stopped Automatically....");
        }
    }
}
