package com.rightclickit.b2bsaleon.models;

import android.content.Context;

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

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;

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
    private String type = "", UserCode = "";
    private ArrayList<String> stakeIdsList = new ArrayList<String>();
    private JSONArray routesArray;
    private ArrayList<AgentsBean> mAgentsBeansList = new ArrayList<AgentsBean>();
    private ArrayList<AgentsBean> mAgentsBeansList1 = new ArrayList<AgentsBean>();
    private ArrayList<AgentsBean> mAgentsBeansList_MyPrivilege = new ArrayList<AgentsBean>();
    private String firstname = "", lastname = "", mobileno = "", stakeid = "", userid = "", email = "", password = "123456789", code = "", reportingto = "", verigycode = "", status = "IA", delete = "N", address = "", latitude = "", longitude = "", timestamp = "", ob = "", ordervalue = "", totalamount = "", dueamount = "", pic = "";
    private boolean isSaveDeviceDetails, isMyProfilePrivilege;

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

  /*  public void getStakeHoldersList(String s) {
        try {
            type = s;
            HashMap<String, String> userMapData = mDBHelper.getUsersData();
            JSONObject routesJob = new JSONObject(userMapData.get("route_ids").toString());
            routesArray = routesJob.getJSONArray("routeArray");
            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String logInURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.PORT_USER_PREVILEGES, Constants.GET_STAKE_HOLDERS_LIST);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("type[0]", "2");

                //System.out.println("THE STAKE URL IS::: " + logInURL);
                //System.out.println("THE STAKE DATA IS::: " + params.toString());

                AsyncRequest loginRequest = new AsyncRequest(context, this, logInURL, AsyncRequest.MethodType.POST, params);
                loginRequest.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void getAgentsList(String s) {
        try {
            if (mAgentsBeansList.size() > 0) {
                mAgentsBeansList.clear();
            }
            if (mAgentsBeansList_MyPrivilege.size() > 0) {
                mAgentsBeansList_MyPrivilege.clear();
            }
            HashMap<String, String> userMapData = mDBHelper.getUsersData();
            isMyProfilePrivilege = false;
            UserCode = userMapData.get("user_id");
            ArrayList<String> privilegeActionsData1 = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("Customers"));
            for (int z = 0; z < privilegeActionsData1.size(); z++) {
                if (privilegeActionsData1.get(z).toString().equals("my_profile")) {
                    isMyProfilePrivilege = true;
                }
            }
            type = s;
            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                //HashMap<String, String> userMapData = mDBHelper.getUsersData();
                JSONObject routesJob = new JSONObject(userMapData.get("route_ids").toString());
                routesArray = routesJob.getJSONArray("routeArray");
                String logInURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.PORT_AGENTS_LIST, Constants.GET_CUSTOMERS_LIST);
                JSONObject job = new JSONObject();
                ArrayList<String> stakeHolderId = mDBHelper.getStakeTypeIdByStakeTypeForAgents("2");
                System.out.println("STAKES::::: " + stakeHolderId);
                JSONArray stakesArray = new JSONArray();
                for (int k = 0; k < stakeHolderId.size(); k++) {
                    stakesArray.put(stakeHolderId.get(k));
                }
                job.put("route_ids", routesArray);
                job.put("_ids", stakesArray);

                //System.out.println("THE AGENTS URL IS::: " + logInURL);
                //System.out.println("THE AGENTS DATA IS::: " + job.toString());
                AsyncRequest loginRequest = new AsyncRequest(context, this, logInURL, AsyncRequest.MethodType.POST, job);
                loginRequest.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void customerAdd(ArrayList<AgentsBean> list, String stakeTypeIdByStakeType) {
        try {

            isSaveDeviceDetails = true;
            this.mAgentsBeansList1 = list;

            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                System.out.println("STAKE HOLDER ID IS:: " + stakeTypeIdByStakeType);
                String customerAdd = String.format("%s%s%s", Constants.MAIN_URL, Constants.PORT_AGENTS_LIST, Constants.GET_CUSTOMERS_ADD);
                // HashMap<String,String> params = new HashMap<String,String>();

                JSONObject paramsc = new JSONObject();

                String rId = mAgentsBeansList1.get(0).getmAgentRouteId();
                System.out.println("AGENT SEL R ID:: "+ rId);
                JSONArray agentRouteArray = new JSONArray();
                agentRouteArray.put(rId);
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
                paramsc.put("stakeholder_id", stakeTypeIdByStakeType);
                paramsc.put("device_sync", mAgentsBeansList1.get(0).getmAgentDeviceSync());
                paramsc.put("access_device", mAgentsBeansList1.get(0).getmAgentAccessDevice());
                paramsc.put("back_up", mAgentsBeansList1.get(0).getmAgentBackUp());


               // System.out.println("THE ADD URL IS::: " + customerAdd);
               // System.out.println("THE ADD DATA IS::: " + paramsc.toString());

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
                        // Check for my profile privilege
                        if (isMyProfilePrivilege) {
                            // Privilege exists, display only login user profile
                            if (jo.has("_id")) {
                                if (UserCode.equals(jo.getString("_id"))) {
                                    System.out.println("CODE MATCHED::: " + jo.getString("_id"));
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

                                    if (jo.has("so_details_data")) {
                                        if (jo.get("so_details_data") instanceof JSONObject) {
                                            JSONObject soObj = jo.getJSONObject("so_details_data");
                                            //int length = sodetailsJsonArray.length();
                                            //if (length > 0) {
                                            // for (int n = 0; n < length; n++) {
                                            //JSONObject soObj = sodetailsJsonArray.getJSONObject(n);
                                            if (soObj.has("sale_order_value")) {
                                                // Agent price
                                                agentsBean.setmOrderValue(soObj.getString("sale_order_value"));

                                            }
                                            if (soObj.has("op_amt")) {

                                                //String URL = Constants.MAIN_URL + "/b2b/" + priceObj.getString("poi");
                                                agentsBean.setmObAmount(soObj.getString("op_amt"));

                                                // agentsBean.setmPoiImage(Constants.MAIN_URL+"/b2b/"+priceObj.getString("poi"));
                                            }
                                            if (soObj.has("received_amt")) {
                                                // agentsBean.setmPoaImage(Constants.MAIN_URL + "/b2b/" + priceObj.getString("poa"));
                                                agentsBean.setmTotalAmount(soObj.getString("received_amt"));
                                            }

                                            if (soObj.has("cb_amt")) {
                                                // agentsBean.setmPoaImage(Constants.MAIN_URL + "/b2b/" + priceObj.getString("poa"));
                                                agentsBean.setmDueAmount(soObj.getString("cb_amt"));
                                            }
                                        }
                                        //}
                                        //}
                                    } else {
                                        agentsBean.setmObAmount("");
                                        agentsBean.setmOrderValue("");
                                        agentsBean.setmTotalAmount("");
                                        agentsBean.setmDueAmount("");
                                    }
                                    if (jo.has("route_id")) {
                                        if (jo.get("route_id") instanceof JSONArray) {
                                            JSONArray agentRouteArray = jo.getJSONArray("route_id");
                                            if (agentRouteArray != null) {
                                                agentsBean.setmAgentRouteId(agentRouteArray.toString());
                                            }
                                        } else {
                                            agentsBean.setmAgentRouteId(jo.getString("route_id"));
                                        }
                                    }

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

                                    mAgentsBeansList_MyPrivilege.add(agentsBean);

                                    break;
                                }
                            }
                        } else {
                            // Privilege not exists, display all users profile
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
                            if (jo.has("so_details_data")) {
                                if (jo.get("so_details_data") instanceof JSONObject) {

                                    JSONObject soObj = jo.getJSONObject("so_details_data");
                                    //int length = sodetailsJsonArray.length();
                                    //if (length > 0) {
                                    // for (int n = 0; n < length; n++) {
                                    //JSONObject soObj = sodetailsJsonArray.getJSONObject(n);
                                    if (soObj.has("sale_order_value")) {
                                        // Agent price
                                        agentsBean.setmOrderValue(soObj.getString("sale_order_value"));

                                    }
                                    if (soObj.has("op_amt")) {

                                        //String URL = Constants.MAIN_URL + "/b2b/" + priceObj.getString("poi");
                                        agentsBean.setmObAmount(soObj.getString("op_amt"));

                                        // agentsBean.setmPoiImage(Constants.MAIN_URL+"/b2b/"+priceObj.getString("poi"));
                                    }
                                    if (soObj.has("received_amt")) {
                                        // agentsBean.setmPoaImage(Constants.MAIN_URL + "/b2b/" + priceObj.getString("poa"));
                                        agentsBean.setmTotalAmount(soObj.getString("received_amt"));
                                    }

                                    if (soObj.has("cb_amt")) {
                                        // agentsBean.setmPoaImage(Constants.MAIN_URL + "/b2b/" + priceObj.getString("poa"));
                                        agentsBean.setmDueAmount(soObj.getString("cb_amt"));
                                    }
                                    //}
                                    //}
                                }
                            } else {
                                agentsBean.setmObAmount("");
                                agentsBean.setmOrderValue("");
                                agentsBean.setmTotalAmount("");
                                agentsBean.setmDueAmount("");
                            }
                            if (jo.has("route_id")) {
                                if (jo.get("route_id") instanceof JSONArray) {
                                    JSONArray agentRouteArray = jo.getJSONArray("route_id");
                                    if (agentRouteArray != null) {
                                        agentsBean.setmAgentRouteId(agentRouteArray.toString());
                                    }
                                } else {
                                    agentsBean.setmAgentRouteId(jo.getString("route_id"));
                                }
                            }

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

                    }
//                    synchronized (this) {
//                        if (mDBHelper.getAgentsTableCount() > 0) {
//                            mDBHelper.deleteValuesFromAgentsTable();
//                        }
//                    }
                    synchronized (this) {
                        if (mDBHelper.getAgentsTableCount() > 0) {
                            mDBHelper.deleteValuesFromAgentsTable();
                        }
                        if (isMyProfilePrivilege) {
                            mDBHelper.insertAgentDetails(mAgentsBeansList_MyPrivilege, UserCode);
                        } else {
                            mDBHelper.insertAgentDetails(mAgentsBeansList, UserCode);
                        }
                    }
                    synchronized (this) {
                        if (isMyProfilePrivilege) {
                            activity.loadAgentsList(mAgentsBeansList_MyPrivilege);
                        } else {
                            activity.loadAgentsList(mAgentsBeansList);
                        }
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
