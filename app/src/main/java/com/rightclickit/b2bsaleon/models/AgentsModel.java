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
import org.json.JSONTokener;

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
    private String type = "";
    private ArrayList<String> stakeIdsList = new ArrayList<String>();
    private JSONArray routesArray;
    private ArrayList<AgentsBean> mAgentsBeansList = new ArrayList<AgentsBean>();
    private ArrayList<AgentsBean> mAgentsBeansList1 = new ArrayList<AgentsBean>();
    private String firstname = "", lastname = "", mobileno = "", stakeid = "", userid = "", email = "", password = "123456789", code = "", reportingto = "", verigycode = "", status = "IA", delete = "N", address = "", latitude = "", longitude = "", timestamp = "", ob = "", ordervalue = "", totalamount = "", dueamount = "", pic = "";
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
            HashMap<String, String> userMapData = mDBHelper.getUsersData();
            JSONObject routesJob = new JSONObject(userMapData.get("route_ids").toString());
            routesArray = routesJob.getJSONArray("routeArray");
            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String logInURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.PORT_USER_PREVILEGES, Constants.GET_STAKE_HOLDERS_LIST);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("type[0]", "2");

                System.out.println("THE STAKE URL IS::: " + logInURL);
                System.out.println("THE STAKE DATA IS::: " + params.toString());

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
                String logInURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.PORT_AGENTS_LIST, Constants.GET_CUSTOMERS_LIST);
                JSONObject job = new JSONObject();
//                JSONArray routesArray = new JSONArray();
//                routesArray.put("59158cb42c432907e4771bad");
                JSONArray stakesArray = new JSONArray();
                for (int j = 0; j < stakeIdsList.size(); j++) {
                    stakesArray.put(stakeIdsList.get(j).toString());
                }
//                stakesArray.put("10");
                job.put("route_ids", routesArray);
                job.put("_ids", stakesArray);

//                HashMap<String,String> params = new HashMap<String,String>();
//                params.put("route_ids",routeId.toString());
//                params.put("_ids",stakeId.toString());
                System.out.println("THE AGENTS URL IS::: " + logInURL);
                System.out.println("THE AGENTS DATA IS::: " + job.toString());
                AsyncRequest loginRequest = new AsyncRequest(context, this, logInURL, AsyncRequest.MethodType.POST, job);
                loginRequest.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void customerAdd(String str_businessName, String str_personName, String str_mobileno, String stakeholderid, String userid, String email, String password1, String code, String reportingto, String verifycode, String status, String delete, String str_address, String latitude, String longitude, String timestamp, String ob, String order, String total, String due, String pic) {
//        try {
//
//            isSaveDeviceDetails = true;
//            this.firstname = str_businessName;
//            this.lastname = str_personName;
//            this.mobileno = str_mobileno;
//            this.stakeid = stakeholderid;
//            this.userid = userid;
//            this.email = email;
//            this.password = password1;
//            this.mobileno = str_mobileno;
//            this.code = code;
//            this.reportingto = reportingto;
//            this.verigycode = verifycode;
//            this.status = status;
//            this.delete = delete;
//            this.address = str_address;
//            this.latitude = latitude;
//            this.longitude = longitude;
//            this.timestamp = timestamp;
//            this.ordervalue = order;
//            this.ob = ob;
//            this.totalamount = total;
//            this.dueamount = due;
//            this.pic = pic;
//
//            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
//                String customerAdd = String.format("%s%s%s", Constants.MAIN_URL, Constants.PORT_AGENTS_LIST, Constants.GET_CUSTOMERS_ADD);
//                // HashMap<String,String> params = new HashMap<String,String>();
//
//                JSONObject paramsc = new JSONObject();
//                JSONArray agentRouteArray = paramsc.getJSONArray("route_id");
//                paramsc.put("route_id", agentRouteArray);
//
//                paramsc.put("first_name", firstname);
//                paramsc.put("last_name", lastname);
//                paramsc.put("phone", mobileno);
//                paramsc.put("email", email);
//                paramsc.put("password", password);
//                paramsc.put("code", code);
//                paramsc.put("reporting_to", reportingto);
//                paramsc.put("verify_code", verifycode);
//                paramsc.put("status", status);
//                paramsc.put("delete", delete);
//                paramsc.put("address", address);
//                paramsc.put("created_by", userid);
//                paramsc.put("created_on", timestamp);
//                paramsc.put("updated_on", timestamp);
//                paramsc.put("updated_by", userid);
//                paramsc.put("avatar", pic);
//
//
//                System.out.println("THE ADD URL IS::: " + customerAdd);
//                System.out.println("THE ADD DATA IS::: " + paramsc.toString());
//
//                AsyncRequest loginRequest = new AsyncRequest(context, this, customerAdd, AsyncRequest.MethodType.POST, paramsc);
//                loginRequest.execute();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void customerAdd(ArrayList<AgentsBean> list) {
        try {

            isSaveDeviceDetails = true;
            this.mAgentsBeansList1 = list;

            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String customerAdd = String.format("%s%s%s", Constants.MAIN_URL, Constants.PORT_AGENTS_LIST, Constants.GET_CUSTOMERS_ADD);
                // HashMap<String,String> params = new HashMap<String,String>();

                JSONObject paramsc = new JSONObject();

                String rId = mAgentsBeansList1.get(0).getmAgentRouteId();
                JSONArray agentRouteArray = new JSONArray(rId);
                paramsc.put("route_id", agentRouteArray);

                paramsc.put("first_name", mAgentsBeansList1.get(0).getmFirstname());
                paramsc.put("last_name", mAgentsBeansList1.get(0).getmLastname());
                paramsc.put("phone", mAgentsBeansList1.get(0).getMphoneNO());
                paramsc.put("email", mAgentsBeansList1.get(0).getmAgentEmail());
                paramsc.put("password", mAgentsBeansList1.get(0).getmAgentPassword());
                paramsc.put("code", mAgentsBeansList1.get(0).getmAgentCode());
                paramsc.put("reporting_to", mAgentsBeansList1.get(0).getmAgentReprtingto());
                paramsc.put("verify_code", mAgentsBeansList1.get(0).getmAgentVerifycode());
                paramsc.put("status", mAgentsBeansList1.get(0).getmStatus());
                paramsc.put("delete", mAgentsBeansList1.get(0).getmAgentDelete());
                paramsc.put("address", mAgentsBeansList1.get(0).getMaddress());
                paramsc.put("created_by", mAgentsBeansList1.get(0).getmAgentCreatedBy());
                paramsc.put("created_on", mAgentsBeansList1.get(0).getmAgentCreatedOn());
                paramsc.put("updated_on", mAgentsBeansList1.get(0).getmAgentUpdatedOn());
                paramsc.put("updated_by", mAgentsBeansList1.get(0).getmAgentUpdatedBy());
                paramsc.put("avatar", mAgentsBeansList1.get(0).getmAgentPic());
                paramsc.put("approved_on", mAgentsBeansList1.get(0).getmAgentApprovedOn());


                System.out.println("THE ADD URL IS::: " + customerAdd);
                System.out.println("THE ADD DATA IS::: " + paramsc.toString());

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
            if (type.equals("stakesList")) {
                System.out.println("========= STAKES response = " + response);
                JSONArray respArray = new JSONArray(response);
                int length = respArray.length();
                for (int i = 0; i < length; i++) {
                    JSONObject jsonObject = respArray.getJSONObject(i);
                    if (jsonObject.has("_id")) {
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
            } else if (type.equals("agents")) {
                System.out.println("========= AGENTS response = " + response);
                Object json = new JSONTokener(response).nextValue();
                if (json instanceof JSONArray) {
                    JSONArray respArray = new JSONArray(response);
                    int len = respArray.length();
                    for (int k = 0; k < len; k++) {
                        JSONObject jo = respArray.getJSONObject(k);

                        AgentsBean agentsBean = new AgentsBean();
                        if (jo.has("_id")) {
                            agentsBean.setmAgentId(jo.getString("_id"));
                        }
                        if (jo.has("latitude")) {
                            agentsBean.setmLatitude(jo.getString("latitude"));
                        }
                        if (jo.has("longitude")) {
                            agentsBean.setmLongitude(jo.getString("longitude"));
                        }
                        if (jo.has("code")) {
                            agentsBean.setmAgentCode(jo.getString("code"));
                        }

                        if (jo.has("email")) {
                            agentsBean.setmAgentEmail(jo.getString("email"));
                        }
                        if (jo.has("password")) {
                            agentsBean.setmAgentPassword(jo.getString("password"));
                        }
                        if (jo.has("reporting_to")) {
                            agentsBean.setmAgentReprtingto(jo.getString("reporting_to"));
                        }
                        if (jo.has("verify_code")) {
                            agentsBean.setmAgentVerifycode(jo.getString("verify_code"));
                        }
                        if (jo.has("status")) {
                            agentsBean.setmStatus(jo.getString("status"));
                        }
                        if (jo.has("delete")) {
                            agentsBean.setmAgentDelete(jo.getString("delete"));
                        }
                        if (jo.has("first_name")) {
                            agentsBean.setmFirstname(jo.getString("first_name"));
                            //Log.i("dgferkgferf", jo.getString("first_name"));
                        }
                        if (jo.has("last_name")) {
                            agentsBean.setmLastname(jo.getString("last_name"));
                            //System.out.println("dgferkgferf IS::: " + jo.getString("last_name"));
                        }
                        if (jo.has("phone")) {
                            agentsBean.setMphoneNO(jo.getString("phone"));
                        }

                        if (jo.has("shopdata")) {
                            JSONArray priceJsonArray = jo.getJSONArray("shopdata");
                            int length = priceJsonArray.length();
                            if (length > 0) {
                                for (int s = 0; s < length; s++) {
                                    JSONObject priceObj = priceJsonArray.getJSONObject(s);
                                    if (priceObj.has("shop_address")) {
                                        // Agent price
                                        agentsBean.setMaddress(priceObj.getString("shop_address"));

                                    }
                                    if (priceObj.has("poi")) {

                                        //String URL = Constants.MAIN_URL + "/b2b/" + priceObj.getString("poi");
                                        agentsBean.setmPoiImage(priceObj.getString("poi"));

                                        // agentsBean.setmPoiImage(Constants.MAIN_URL+"/b2b/"+priceObj.getString("poi"));
                                    }
                                    if (priceObj.has("poa")) {
                                        // agentsBean.setmPoaImage(Constants.MAIN_URL + "/b2b/" + priceObj.getString("poa"));
                                        agentsBean.setmPoaImage(priceObj.getString("poa"));
                                    }
                                }
                            }
                        }
                        if (jo.has("route_id")) {
                            if (jo.get("route_id") instanceof JSONArray) {
                                JSONArray agentRouteArray = jo.getJSONArray("route_id");
                                if (agentRouteArray != null) {
                                    agentsBean.setmAgentRouteId(agentRouteArray.toString());
                                }
                            }else {
                                agentsBean.setmAgentRouteId(jo.getString("route_id"));
                            }
                        }

                        agentsBean.setmObAmount("");
                        agentsBean.setmOrderValue("");
                        agentsBean.setmTotalAmount("");
                        agentsBean.setmDueAmount("");
                        if (jo.has("stakeholder_id")) {
                            agentsBean.setmAgentStakeid(jo.getString("stakeholder_id"));
                        }
                        if (jo.has("avatar")) {
                            agentsBean.setmAgentPic(jo.getString("avatar"));
                        }
//                        if (jo.has("address")) {
//                            agentsBean.setMaddress(jo.getString("address"));
//                        }
                        if (jo.has("created_by")) {
                            agentsBean.setmAgentCreatedBy(jo.getString("created_by"));
                        }
                        if (jo.has("created_on")) {
                            agentsBean.setmAgentCreatedOn(jo.getString("created_on"));
                        }
                        if (jo.has("updated_on")) {
                            agentsBean.setmAgentUpdatedOn(jo.getString("updated_on"));
                        }
                        if (jo.has("updated_by")) {
                            agentsBean.setmAgentUpdatedBy(jo.getString("updated_by"));
                        }

                        mAgentsBeansList.add(agentsBean);
                    }
                    synchronized (this) {
                        if (mDBHelper.getAgentsTableCount() > 0) {
                            mDBHelper.deleteValuesFromAgentsTable();
                        }
                    }
                    synchronized (this) {
                        if (mDBHelper.getAgentsTableCount() > 0) {
                            mDBHelper.deleteValuesFromAgentsTable();
                        }
                        mDBHelper.insertAgentDetails(mAgentsBeansList);
                    }
                    synchronized (this) {
                        activity.loadAgentsList(mAgentsBeansList);
                    }
                }
            } else {
                // Handle Async response of add customer...
                System.out.println("RES:: " + response);
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
