package com.rightclickit.b2bsaleon.models;

import android.content.Context;

import com.rightclickit.b2bsaleon.activities.AgentStockActivity;
import com.rightclickit.b2bsaleon.activities.AgentsActivity;
import com.rightclickit.b2bsaleon.activities.Agents_AddActivity;
import com.rightclickit.b2bsaleon.activities.LoginActivity;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.beanclass.AgentsStockBean;
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
public class AgentsStockModel implements OnAsyncRequestCompleteListener {
    public static final String TAG = LoginActivity.class.getSimpleName();

    private Context context;
    private AgentStockActivity activity;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
    private String agentId = "", UserCode = "";
    private ArrayList<String> stakeIdsList = new ArrayList<String>();
    private JSONArray routesArray;
    private ArrayList<AgentsStockBean> mAgentsStockList = new ArrayList<AgentsStockBean>();
    private ArrayList<String> prodIdsList = new ArrayList<String>();
    private ArrayList<String> prodNamesList = new ArrayList<String>();
    private ArrayList<String> prodCodesList = new ArrayList<String>();
    private ArrayList<String> prodUOMList = new ArrayList<String>();
    private ArrayList<String> prodQuantityList = new ArrayList<String>();
    private ArrayList<String> prodDelQuantityList = new ArrayList<String>();
    private ArrayList<AgentsBean> mAgentsBeansList_MyPrivilege = new ArrayList<AgentsBean>();
    private String firstname = "", lastname = "", mobileno = "", stakeid = "", userid = "", email = "", password = "123456789", code = "", reportingto = "", verigycode = "", status = "IA", delete = "N", address = "", latitude = "", longitude = "", timestamp = "", ob = "", ordervalue = "", totalamount = "", dueamount = "", pic = "";
    private boolean isSaveDeviceDetails, isMyProfilePrivilege;

    public AgentsStockModel(Context context, AgentStockActivity activity) {
        this.context = context;
        this.activity = activity;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);
    }

    public void getAgentsStock(String agentId) {
        try {
            this.agentId = agentId;
            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String logInURL = String.format("%s%s%s", Constants.MAIN_URL_STOCK, Constants.PORT_AGENTS_LIST, Constants.AGENTS_STOCK_URL);
                JSONObject job = new JSONObject();
                job.put("user_id", agentId);

                AsyncRequest loginRequest = new AsyncRequest(context, this, logInURL, AsyncRequest.MethodType.POST, job);
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
            System.out.println("========= STOCKS response = " + response);
            JSONObject resObj = new JSONObject(response);
            JSONArray stockArray = new JSONArray();
            JSONArray saleArray = new JSONArray();
            // Stock Response
            if (resObj.has("stock_res")) {
                int length = stockArray.length();
                if (length > 0) {
                    for (int i = 0; i < length; i++) {
                        JSONObject jsonObject = stockArray.getJSONObject(i);
                        // Product Ids
                        if (jsonObject.has("agent_prod_ids")) {
                            JSONArray prodIdsArray = jsonObject.getJSONArray("agent_prod_ids");
                            int prodIdsLe = prodIdsArray.length();
                            if (prodIdsLe > 0) {
                                for (int a = 0; a < prodIdsLe; a++) {
                                    prodIdsList.add(prodIdsArray.get(a).toString());
                                }
                            }
                        }

                        // Product Codes
                        if (jsonObject.has("agent_prod_codes")) {
                            JSONArray prodCodesArray = jsonObject.getJSONArray("agent_prod_codes");
                            int prodIdsLe = prodCodesArray.length();
                            if (prodIdsLe > 0) {
                                for (int a = 0; a < prodIdsLe; a++) {
                                    prodCodesList.add(prodCodesArray.get(a).toString());
                                }
                            }
                        }

                        // Product Qunatity
                        if (jsonObject.has("agent_prod_quantity")) {
                            JSONArray prodQunatityArray = jsonObject.getJSONArray("agent_prod_quantity");
                            int prodIdsLe = prodQunatityArray.length();
                            if (prodIdsLe > 0) {
                                for (int a = 0; a < prodIdsLe; a++) {
                                    prodQuantityList.add(prodQunatityArray.get(a).toString());
                                }
                            }
                        }

                        // Product Names
                        if (jsonObject.has("agent_prod_names")) {
                            JSONArray prodNamesArray = jsonObject.getJSONArray("agent_prod_names");
                            int prodIdsLe = prodNamesArray.length();
                            if (prodIdsLe > 0) {
                                for (int a = 0; a < prodIdsLe; a++) {
                                    prodNamesList.add(prodNamesArray.get(a).toString());
                                }
                            }
                        }

                        // Product UOM
                        if (jsonObject.has("agent_prod_uom")) {
                            JSONArray prodUOMArray = jsonObject.getJSONArray("agent_prod_uom");
                            int prodIdsLe = prodUOMArray.length();
                            if (prodIdsLe > 0) {
                                for (int a = 0; a < prodIdsLe; a++) {
                                    prodUOMList.add(prodUOMArray.get(a).toString());
                                }
                            }
                        }
                    }
                }
            }

            // Delivery Response
            if (resObj.has("stock_res")) {
                int length = stockArray.length();
                if (length > 0) {
                    for (int i = 0; i < length; i++) {
                        JSONObject jsonObject = stockArray.getJSONObject(i);
                        // Product Qunatity
                        if (jsonObject.has("agent_prod_quantity")) {
                            JSONArray prodQunatityArray = jsonObject.getJSONArray("agent_prod_quantity");
                            int prodIdsLe = prodQunatityArray.length();
                            if (prodIdsLe > 0) {
                                for (int a = 0; a < prodIdsLe; a++) {
                                    prodDelQuantityList.add(prodQunatityArray.get(a).toString());
                                }
                            }
                        }
                    }
                }
            }

            // Add all to bean
            int len = prodIdsList.size();
            if (len > 0) {
                for (int b = 0; b < len; b++) {
                    AgentsStockBean aBean = new AgentsStockBean();

                    aBean.setmProductId(prodIdsList.get(b).toString());
                    aBean.setmProductName(prodNamesList.get(b).toString());
                    aBean.setmProductCode(prodCodesList.get(b).toString());
                    aBean.setmProductUOM(prodUOMList.get(b).toString());
                    aBean.setmProductStockQunatity(prodQuantityList.get(b).toString());
                    aBean.setmProductDeliveryQunatity(prodDelQuantityList.get(b).toString());
                    int CBQ = Integer.parseInt(prodQuantityList.get(b).toString()) - Integer.parseInt(prodDelQuantityList.get(b).toString());
                    aBean.setmProductCBQuantity(String.valueOf(CBQ));

                    mAgentsStockList.add(aBean);
                }
            }

            synchronized (this) {
                if (mAgentsStockList.size() > 0) {
                    mDBHelper.insertAgentsStockListData(mAgentsStockList, agentId);
                }
            }
            synchronized (this) {
                activity.loadAgentsStockList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
