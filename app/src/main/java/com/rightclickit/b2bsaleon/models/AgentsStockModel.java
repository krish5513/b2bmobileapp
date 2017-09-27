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
    private HashMap<String, String> prodIdsListMap = new HashMap<String, String>();

    private ArrayList<String> prodNamesList = new ArrayList<String>();
    private HashMap<String, String> prodNamesListMap = new HashMap<String, String>();

    private ArrayList<String> prodCodesList = new ArrayList<String>();
    private HashMap<String, String> prodCodesListMap = new HashMap<String, String>();

    private ArrayList<String> prodUOMList = new ArrayList<String>();
    private HashMap<String, String> prodUOMListMap = new HashMap<String, String>();

    // SALE QUNATITY...
    private ArrayList<String> prodSaleIdsList = new ArrayList<String>();

    private ArrayList<String> prodQuantityList = new ArrayList<String>();
    private HashMap<String, String> prodQuantityListMap = new HashMap<String, String>();

    private ArrayList<String> prodDelQuantityList = new ArrayList<String>();
    private HashMap<String, String> prodDelQuantityListMap = new HashMap<String, String>();
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
            if (mAgentsStockList.size() > 0) {
                mAgentsStockList.clear();
            }
            if (prodIdsList.size() > 0) {
                prodIdsList.clear();
            }
            if (prodIdsListMap.size() > 0) {
                prodIdsListMap.clear();
            }

            if (prodCodesList.size() > 0) {
                prodCodesList.clear();
            }
            if (prodCodesListMap.size() > 0) {
                prodCodesListMap.clear();
            }

            if (prodQuantityList.size() > 0) {
                prodQuantityList.clear();
            }
            if (prodQuantityListMap.size() > 0) {
                prodQuantityListMap.clear();
            }

            if (prodNamesList.size() > 0) {
                prodNamesList.clear();
            }
            if (prodNamesListMap.size() > 0) {
                prodNamesListMap.clear();
            }

            if (prodUOMList.size() > 0) {
                prodUOMList.clear();
            }
            if (prodUOMListMap.size() > 0) {
                prodUOMListMap.clear();
            }

            if (prodDelQuantityList.size() > 0) {
                prodDelQuantityList.clear();
            }
            if (prodDelQuantityListMap.size() > 0) {
                prodDelQuantityListMap.clear();
            }
            if (prodSaleIdsList.size() > 0) {
                prodSaleIdsList.clear();
            }
            System.out.println("========= STOCKS response = " + response);
            JSONObject resObj = new JSONObject(response);
            JSONArray stockArray;
            JSONArray saleArray;
            // Stock Response
            if (resObj.has("stock_res")) {
                stockArray = resObj.getJSONArray("stock_res");
                System.out.println("STOCK RESP LENG::: " + stockArray.length());
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

            // Delivery(SALE) Response
            if (resObj.has("sales_res")) {
                stockArray = resObj.getJSONArray("sales_res");
                System.out.println("SALE RESP LENG::: " + stockArray.length());
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
                                    prodSaleIdsList.add(prodIdsArray.get(a).toString());
                                }
                            }
                        }

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
            System.out.println("PS:: " + len);
            if (len > 0) {
                for (int b = 0; b < len; b++) {
                    prodIdsListMap.put(prodIdsList.get(b).toString(), prodIdsList.get(b).toString());
                    prodNamesListMap.put(prodIdsList.get(b).toString(), prodNamesList.get(b).toString());
                    prodCodesListMap.put(prodIdsList.get(b).toString(), prodCodesList.get(b).toString());
                    prodUOMListMap.put(prodIdsList.get(b).toString(), prodUOMList.get(b).toString());
                    prodQuantityListMap.put(prodIdsList.get(b).toString(), prodQuantityList.get(b).toString());
                }
            }

            int saleLen = prodSaleIdsList.size();
            System.out.println("SALE SIZE::: " + saleLen);
            if (saleLen > 0) {
                for (int g = 0; g < saleLen; g++) {
                    prodDelQuantityListMap.put(prodSaleIdsList.get(g).toString(), prodDelQuantityList.get(g).toString());
                }
            }

            System.out.println("FINAL STOCK SIZE::: " + prodQuantityListMap.size());
            System.out.println("FINAL SALE SIZE::: " + prodDelQuantityListMap.size());

            for (int n = 0; n < prodIdsList.size(); n++) {
                System.out.println("StQ:" + prodQuantityListMap.get(prodIdsList.get(n).toString()) + "\n");
                System.out.println("SaleQ:" + prodDelQuantityListMap.get(prodIdsList.get(n).toString()) + "\n");
                double stockQunatity = 0.0f, saleQuantity = 0.0f;
                AgentsStockBean aBean = new AgentsStockBean();

                if (prodIdsListMap.get(prodIdsList.get(n).toString()) != null) {
                    aBean.setmProductId(prodIdsListMap.get(prodIdsList.get(n).toString()));
                } else {
                    aBean.setmProductId("");
                }

                if (prodNamesListMap.get(prodIdsList.get(n).toString()) != null) {
                    aBean.setmProductName(prodNamesListMap.get(prodIdsList.get(n).toString()));
                } else {
                    aBean.setmProductName("");
                }

                if (prodCodesListMap.get(prodIdsList.get(n).toString()) != null) {
                    aBean.setmProductCode(prodCodesListMap.get(prodIdsList.get(n).toString()));
                } else {
                    aBean.setmProductCode("");
                }

                if (prodUOMListMap.get(prodIdsList.get(n).toString()) != null) {
                    aBean.setmProductUOM(prodUOMListMap.get(prodIdsList.get(n).toString()));
                } else {
                    aBean.setmProductUOM("");
                }

                if (prodQuantityListMap.get(prodIdsList.get(n).toString()) != null) {
                    aBean.setmProductStockQunatity(prodQuantityListMap.get(prodIdsList.get(n).toString()));
                    stockQunatity = Double.parseDouble(prodQuantityListMap.get(prodIdsList.get(n).toString()));
                } else {
                    aBean.setmProductStockQunatity("0");
                }

                if (prodDelQuantityListMap.get(prodIdsList.get(n).toString()) != null) {
                    aBean.setmProductDeliveryQunatity(prodDelQuantityListMap.get(prodIdsList.get(n).toString()));
                    saleQuantity = Double.parseDouble(prodDelQuantityListMap.get(prodIdsList.get(n).toString()));
                } else {
                    aBean.setmProductDeliveryQunatity("0");
                }

                double CBQ = stockQunatity - saleQuantity;
                aBean.setmProductCBQuantity(String.valueOf(CBQ));

                mAgentsStockList.add(aBean);
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
            activity.loadAgentsStockList();
        }
    }
}
