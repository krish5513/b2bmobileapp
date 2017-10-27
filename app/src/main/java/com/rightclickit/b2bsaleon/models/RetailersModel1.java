package com.rightclickit.b2bsaleon.models;

import android.content.Context;

import com.rightclickit.b2bsaleon.activities.Agents_AddActivity;
import com.rightclickit.b2bsaleon.activities.LoginActivity;
import com.rightclickit.b2bsaleon.activities.RetailersActivity;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.beanclass.TDCCustomer;
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
import java.util.List;

/**
 * Created by Sekhar Kuppa
 */
public class RetailersModel1 implements OnAsyncRequestCompleteListener {
    public static final String TAG = LoginActivity.class.getSimpleName();

    private Context context;
    private RetailersActivity activity;
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
    private List<TDCCustomer> TDCCustomerList = new ArrayList<TDCCustomer>();

    public RetailersModel1(Context context, RetailersActivity activity) {
        this.context = context;
        this.activity = activity;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);
    }

    public RetailersModel1(Context context, Agents_AddActivity activity) {
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
//            ArrayList<String> privilegeActionsData1 = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("Customers"));
//            for (int z = 0; z < privilegeActionsData1.size(); z++) {
//                if (privilegeActionsData1.get(z).toString().equals("my_profile")) {
//                    isMyProfilePrivilege = true;
//                }
//            }
            type = s;
            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                //HashMap<String, String> userMapData = mDBHelper.getUsersData();
                JSONObject routesJob = new JSONObject(userMapData.get("route_ids").toString());
                routesArray = routesJob.getJSONArray("routeArray");
                String logInURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.PORT_AGENTS_LIST, Constants.GET_CUSTOMERS_LIST);
                JSONObject job = new JSONObject();
                ArrayList<String> stakeHolderId = mDBHelper.getStakeTypeIdByStakeTypeForAgents("3");
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
                System.out.println("AGENT SEL R ID:: " + rId);
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
                getAgentsList("retailers");
//                if (logInResponse.getInt("result_status") == 1) {
//
//                } else {
//                    displayNoNetworkError(context);
//                    //  activity.logInError();
//                }
            } else if (type.equals("retailers")) {
                System.out.println("========= RETAILERS response = " + response);
                Object json = new JSONTokener(response).nextValue();
                if (json instanceof JSONArray) {
                    JSONArray respArray = new JSONArray(response);
                    int len = respArray.length();
                    for (int k = 0; k < len; k++) {
                        JSONObject jo = respArray.getJSONObject(k);
                        // Privilege not exists, display all users profile
                        AgentsBean agentsBean = new AgentsBean();
                        TDCCustomer customer = new TDCCustomer();
                        if (jo.has("_id")) {
                            customer.setUserId(jo.getString("_id"));
                        }
                        if (jo.has("latitude")) {
                            if (!jo.getString("latitude").equals("null")) {
                                customer.setLatitude(jo.getString("latitude"));
                            } else {
                                customer.setLatitude("");
                            }
                        }
                        if (jo.has("longitude")) {
                            if (!jo.getString("longitude").equals("null")) {
                                customer.setLongitude(jo.getString("longitude"));
                            } else {
                                customer.setLongitude("");
                            }
                        }
                        if (jo.has("code")) {
                            if (jo.getString("code") != null) {
                                if (jo.getString("code").length() > 0) {
                                    customer.setCode(jo.getString("code"));
                                } else {
                                    customer.setCode("");
                                }
                            } else {
                                customer.setCode("");
                            }
                        }

                        if (jo.has("address")) {
                            customer.setAddress(jo.getString("address"));
                        }
                        if (jo.has("first_name")) {
                            customer.setBusinessName(jo.getString("first_name"));
                        }
                        if (jo.has("last_name")) {
                            customer.setName(jo.getString("last_name"));
                        }
                        if (jo.has("phone")) {
                            customer.setMobileNo(jo.getString("phone"));
                        }

                        if (jo.has("route_id")) {
                            if (jo.get("route_id") instanceof JSONArray) {
                                JSONArray agentRouteArray = jo.getJSONArray("route_id");
                                if (agentRouteArray != null) {
                                    customer.setRoutecode(agentRouteArray.toString());
                                }
                            } else {
                                customer.setRoutecode(jo.getString("route_id"));
                            }
                        }

                        customer.setCustomerType(1);
                        customer.setShopImage("");
                        customer.setIsShopImageUploaded(0);

                        TDCCustomerList.add(customer);

                        long customerId = mDBHelper.insertIntoTDCCustomers(customer);

                    }
                    synchronized (this) {
                        activity.loadRetailers(TDCCustomerList);
                    }
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
