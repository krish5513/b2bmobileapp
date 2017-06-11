package com.rightclickit.b2bsaleon.models;

import android.content.Context;
import android.util.Log;

import com.rightclickit.b2bsaleon.activities.AgentsActivity;
import com.rightclickit.b2bsaleon.activities.Agents_AddActivity;
import com.rightclickit.b2bsaleon.activities.LoginActivity;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.customviews.CustomAlertDialog;
import com.rightclickit.b2bsaleon.customviews.CustomProgressDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.OnAsyncRequestCompleteListener;
import com.rightclickit.b2bsaleon.util.AsyncRequest;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Sekhar Kuppa
 */
public class AgentsModel implements OnAsyncRequestCompleteListener {
    public static final String TAG = LoginActivity.class.getSimpleName();

    private Context context;
    private AgentsActivity activity;
    private Agents_AddActivity activity1;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
    private String type="";
    private ArrayList<String> stakeIdsList = new ArrayList<String>();
    private JSONArray routesArray;
    private ArrayList<AgentsBean> mAgentsBeansList = new ArrayList<AgentsBean>();
    private String firstname="",lastname="",mobileno="";
    private boolean isSaveDeviceDetails;

    public AgentsModel(Context context, AgentsActivity activity) {
        this.context = context;
        this.activity = activity;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);
    }
    public AgentsModel(Context context, Agents_AddActivity activity) {
        this.context = context;
        this.activity1 = activity;
       // this.mPreferences = new MMSharedPreferences(context);
      //  this.mDBHelper = new DBHelper(context);
    }
    public void getStakeHoldersList(String s) {
        try {
            type = s;
            HashMap<String,String> userMapData = mDBHelper.getUsersData();
            JSONObject routesJob = new JSONObject(userMapData.get("route_ids").toString());
            routesArray = routesJob.getJSONArray("routeArray");
            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String logInURL = String.format("%s%s%s", Constants.MAIN_URL,Constants.PORT_USER_PREVILEGES, Constants.GET_STAKE_HOLDERS_LIST);
                HashMap<String,String> params = new HashMap<String,String>();
                params.put("type[0]","2");

                System.out.println("THE STAKE URL IS::: "+ logInURL);
                System.out.println("THE STAKE DATA IS::: "+ params.toString());

                AsyncRequest loginRequest = new AsyncRequest(context, this, logInURL, AsyncRequest.MethodType.POST, params);
                loginRequest.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAgentsList(String s) {
        try {
            type = s;
//            ArrayList<String> routeId = new ArrayList<String>();
//            routeId.add("59158cb42c432907e4771bad");
//
//            ArrayList<String> stakeId = new ArrayList<String>();
//            stakeId.add("5");
//            stakeId.add("10");
            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String logInURL = String.format("%s%s%s", Constants.MAIN_URL,Constants.PORT_AGENTS_LIST, Constants.GET_CUSTOMERS_ADD);
                JSONObject job = new JSONObject();
//                JSONArray routesArray = new JSONArray();
//                routesArray.put("59158cb42c432907e4771bad");
                JSONArray stakesArray = new JSONArray();
                for (int j = 0; j<stakeIdsList.size();j++){
                    stakesArray.put(stakeIdsList.get(j).toString());
                }
//                stakesArray.put("10");
                job.put("route_ids",routesArray);
                job.put("_ids",stakesArray);

//                HashMap<String,String> params = new HashMap<String,String>();
//                params.put("route_ids",routeId.toString());
//                params.put("_ids",stakeId.toString());
                System.out.println("THE AGENTS URL IS::: "+ logInURL);
                System.out.println("THE AGENTS DATA IS::: "+ job.toString());
                AsyncRequest loginRequest = new AsyncRequest(context, this, logInURL, AsyncRequest.MethodType.POST, job);
                loginRequest.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void customerAdd( String firstname,String lastname, String mobile) {
        try {

            isSaveDeviceDetails = true;
             this.firstname=firstname;
            this.lastname=lastname;
            this.mobileno=mobile;

            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String customerAdd = String.format("%s%s%s", Constants.MAIN_URL,Constants.PORT_AGENTS_LIST, Constants.GET_CUSTOMERS_LIST);
               // HashMap<String,String> params = new HashMap<String,String>();

                JSONObject paramsc = new JSONObject();
                JSONArray agentRouteArray = paramsc.getJSONArray("route_id");
                paramsc.put("route_id",agentRouteArray);

                paramsc.put("first_name",firstname);
                paramsc.put("last_name",lastname);
                paramsc.put("phone",mobile);




                AsyncRequest loginRequest = new AsyncRequest(context, this, customerAdd, AsyncRequest.MethodType.POST, paramsc);
                loginRequest.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void asyncResponse(String response, Constants.RequestCode requestCode) {
        try {
            CustomProgressDialog.hideProgressDialog();
            if(type.equals("stakesList")){
                System.out.println("========= STAKES response = " + response);
                JSONArray respArray = new JSONArray(response);
                int length = respArray.length();
                for (int i = 0; i<length;i++){
                    JSONObject jsonObject = respArray.getJSONObject(i);
                    if(jsonObject.has("_id")) {
                        stakeIdsList.add(jsonObject.getString("_id"));
                    }


                }
                //JSONObject logInResponse = new JSONObject(response);
                getAgentsList("agents");
//                if (logInResponse.getInt("result_status") == 1) {
//
//                } else {
//                    displayNoNetworkError(context);
//                    //  activity.logInError();
//                }
            }else {
                System.out.println("========= AGENTS response = " + response);
                 JSONArray respArray = new JSONArray(response);
                int len = respArray.length();
                for (int k = 0;k<len;k++){
                    JSONObject jo = respArray.getJSONObject(k);

                    AgentsBean agentsBean = new AgentsBean();
                    if (jo.has("_id")) {
                        agentsBean.setmAgentId(jo.getString("_id"));
                    }
                    if (jo.has("latitude")){
                        agentsBean.setmLatitude(jo.getString("latitude"));
                    }
                    if (jo.has("longitude")){
                        agentsBean.setmLongitude(jo.getString("longitude"));
                    }
                    if (jo.has("code")){
                        agentsBean.setmAgentCode(jo.getString("code"));
                    }

                    if (jo.has("first_name")){
                        agentsBean.setmFirstname(jo.getString("first_name"));
                        Log.i("dgferkgferf",jo.getString("first_name") );
                    }
                    if (jo.has("last_name")){
                        agentsBean.setmLastname( jo.getString("last_name"));
                        System.out.println("dgferkgferf IS::: "+ jo.getString("last_name"));
                    }
                    if (jo.has("phone")){
                        agentsBean.setMphoneNO( jo.getString("phone"));
                    }

                    if(jo.has("shopdata")){
                        JSONArray priceJsonArray = jo.getJSONArray("shopdata");
                        int length= priceJsonArray.length();
                        if (length>0){
                            for (int s = 0;s<length;s++){
                                JSONObject priceObj = priceJsonArray.getJSONObject(s);
                                if(priceObj.has("shop_address")){
                                    // Agent price
                                   agentsBean.setMaddress(priceObj.getString("shop_address"));

                                }
                                if (priceObj.has("poi")){

                                    String URL  = Constants.MAIN_URL+"/b2b/"+priceObj.getString("poi");
                                    agentsBean.setmPoiImage(URL);
                         Log.i("THE POI URL IS::: ",URL);

                                    // agentsBean.setmPoiImage(Constants.MAIN_URL+"/b2b/"+priceObj.getString("poi"));
                                }
                                if (priceObj.has("poa")){
                                    agentsBean.setmPoaImage(Constants.MAIN_URL+"/b2b/"+priceObj.getString("poa"));
                                }
                            }
                        }
                    }
                    if(jo.has("route_id")){
                        if(jo.get("route_id") instanceof JSONArray) {
                            JSONArray agentRouteArray = jo.getJSONArray("route_id");
                            if(agentRouteArray!=null){
                                agentsBean.setmAgentRouteId(agentRouteArray.toString());
                            }
                        }
                    }

                    agentsBean.setmObAmount("");
                    agentsBean.setmOrderValue("");
                    agentsBean.setmTotalAmount("");
                    agentsBean.setmDueAmount("");
                    if (jo.has("code")){
                        agentsBean.setmAgentCode(jo.getString("code"));
                    }
                    if (jo.has("avatar")){
                        agentsBean.setmAgentPic(Constants.MAIN_URL+"/b2b/"+jo.getString("avatar"));
                    }
                    if (jo.has("status")){
                        agentsBean.setmStatus(jo.getString("status"));
                    }

                    mAgentsBeansList.add(agentsBean);
                }
                if(mDBHelper.getAgentsTableCount()>0){
                    mDBHelper.deleteValuesFromAgentsTable();
                }
                synchronized (this){
                    if(mDBHelper.getAgentsTableCount()>0){
                        mDBHelper.deleteValuesFromAgentsTable();
                    }
                    mDBHelper.insertAgentDetails(mAgentsBeansList);
                }
                synchronized (this) {
                    activity.loadAgentsList(mAgentsBeansList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayNoNetworkError(Context context) {
        CustomProgressDialog.hideProgressDialog();
        CustomAlertDialog.showAlertDialog(context, "Access Denied", "Please Contact Administrater.");
    }
}
