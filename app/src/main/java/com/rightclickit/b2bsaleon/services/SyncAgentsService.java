package com.rightclickit.b2bsaleon.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.beanclass.TDCCustomer;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.NetworkManager;
import com.rightclickit.b2bsaleon.util.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sekhar Kuppa
 */

public class SyncAgentsService extends Service {
    private DBHelper mDBHelper;
    private NetworkConnectionDetector connectionDetector;
    private String retailerStakeTypeId, consumerStakeTypeId;
    private int unUploadedTDCCustomersCount = 0;
    private String createdBy;
    private JSONArray routeCodesArray;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            mDBHelper = new DBHelper(this);
            connectionDetector = new NetworkConnectionDetector(this);

            retailerStakeTypeId = mDBHelper.getStakeTypeIdByStakeType("3");
            consumerStakeTypeId = mDBHelper.getStakeTypeIdByStakeType("4");

            HashMap<String, String> userRouteIds = mDBHelper.getUserRouteIds();
            createdBy = userRouteIds.get("user_id");

            routeCodesArray = new JSONObject(userRouteIds.get("route_ids")).getJSONArray("routeArray");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        syncAgentsDataWithServer();

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

    private void syncAgentsDataWithServer() {
        try {
            ArrayList<AgentsBean> unUploadedTDCCustomers = mDBHelper.fetchAllUnUploadedRecordsFromAgents();
            unUploadedTDCCustomersCount = unUploadedTDCCustomers.size();

            for (AgentsBean customer : unUploadedTDCCustomers) {
                if (connectionDetector.isNetworkConnected())
                    new SyncTDCCustomerAsyncTask().execute(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SyncTDCCustomerAsyncTask extends AsyncTask<AgentsBean, Void, Void> {
        private AgentsBean mAgentsBeansList1;

        @Override
        protected Void doInBackground(AgentsBean... tdcCustomers) {
            try {
                mAgentsBeansList1 = tdcCustomers[0];

                String customerAdd = String.format("%s%s%s", Constants.MAIN_URL, Constants.PORT_AGENTS_LIST, Constants.GET_CUSTOMERS_ADD);
                // HashMap<String,String> params = new HashMap<String,String>();

                JSONObject paramsc = new JSONObject();

                String rId = mAgentsBeansList1.getmAgentRouteId();
                System.out.println("AGENT SEL R ID:: " + rId);
                JSONArray agentRouteArray = new JSONArray();
                agentRouteArray.put(rId);
                paramsc.put("route_id", agentRouteArray);

                paramsc.put("first_name", mAgentsBeansList1.getmFirstname());
                paramsc.put("last_name", mAgentsBeansList1.getmLastname());
                paramsc.put("phone", mAgentsBeansList1.getMphoneNO());
                paramsc.put("email", mAgentsBeansList1.getmAgentEmail());
                paramsc.put("_id",mAgentsBeansList1.getmAgentId());
                paramsc.put("password", mAgentsBeansList1.getmAgentPassword());
                paramsc.put("code", mAgentsBeansList1.getmAgentCode());
                paramsc.put("reporting_to", mAgentsBeansList1.getmAgentReprtingto());
                paramsc.put("verify_code", mAgentsBeansList1.getmAgentVerifycode());
                paramsc.put("status", mAgentsBeansList1.getmStatus());
                paramsc.put("delete", mAgentsBeansList1.getmAgentDelete());
                paramsc.put("address", mAgentsBeansList1.getMaddress());
                paramsc.put("created_by", mAgentsBeansList1.getmAgentCreatedBy());
                paramsc.put("created_on", mAgentsBeansList1.getmAgentCreatedOn());
                paramsc.put("updated_on", mAgentsBeansList1.getmAgentUpdatedOn());
                paramsc.put("updated_by", mAgentsBeansList1.getmAgentUpdatedBy());
                paramsc.put("avatar", mAgentsBeansList1.getmAgentPic());
                paramsc.put("approved_on", mAgentsBeansList1.getmAgentApprovedOn());
                paramsc.put("stakeholder_id", mAgentsBeansList1.getmAgentStakeid());
                paramsc.put("device_sync", mAgentsBeansList1.getmAgentDeviceSync());
                paramsc.put("access_device", mAgentsBeansList1.getmAgentAccessDevice());
                paramsc.put("back_up", mAgentsBeansList1.getmAgentBackUp());
                paramsc.put("latitude", mAgentsBeansList1.getmLatitude());
                paramsc.put("longitude", mAgentsBeansList1.getmLongitude());

                String responseString = new NetworkManager().makeHttpPostConnection(customerAdd, paramsc);

                if (responseString != null) {
                    JSONObject resultObj = new JSONObject(responseString);

                    if (resultObj.getInt("result_status") == 1) {
                        // if success, we are updating customer status as uploaded and customer user id (i.e. mongo db id) in local db.
                        mDBHelper.updateAgentUploadStatus(mAgentsBeansList1.getmAgentUniqueId(), resultObj.getString("insert_id"));

                        unUploadedTDCCustomersCount--;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (unUploadedTDCCustomersCount == 0) {
                stopSelf();
                //System.out.println("Sync TDC Sales Order Service Stopped Automatically....");
            }
        }
    }
}
