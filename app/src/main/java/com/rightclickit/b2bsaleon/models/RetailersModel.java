package com.rightclickit.b2bsaleon.models;

import android.content.Context;

import com.rightclickit.b2bsaleon.activities.Products_Activity;
import com.rightclickit.b2bsaleon.activities.TDCSalesActivity;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TDCCustomer;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.customviews.CustomProgressDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.OnAsyncRequestCompleteListener;
import com.rightclickit.b2bsaleon.util.AsyncRequest;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by PPS on 10/13/2017.
 */

public class RetailersModel implements OnAsyncRequestCompleteListener {
    public static final String TAG = Products_Activity.class.getSimpleName();

    private Context context;
    private TDCSalesActivity activity;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
    private String type = "", mAgentId = "";
    private ArrayList<String> regionIdsList = new ArrayList<String>();
    private JSONArray routesArray;
    private ArrayList<TDCCustomer> mTDCCustomerList = new ArrayList<TDCCustomer>();
    private String mStock = "", mAgentPrice = "", mRetailerPrice = "", mConsumerPrice = "";
    private String currentDate = "", fromDate = "";


    private ArrayList<String> billnoList = new ArrayList<String>();
    private ArrayList<String> userIdsList = new ArrayList<String>();
    private ArrayList<String> routeIdsList = new ArrayList<String>();
    private ArrayList<String> proIdsList = new ArrayList<String>();
    private ArrayList<String> taxPercentsList = new ArrayList<String>();
    private ArrayList<String> unitPricesList = new ArrayList<String>();
    private ArrayList<String> quantitesList = new ArrayList<String>();
    private ArrayList<String> taxAmountsList = new ArrayList<String>();
    private ArrayList<String> amountsList = new ArrayList<String>();
    private HashMap<String, JSONArray> productIdsList = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> taxpercentList = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> quantitysList = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> fromDatesList = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> toDatesList = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> unitPriceArray = new HashMap<String, JSONArray>();
    private HashMap<String, JSONArray> userDataArray = new HashMap<String, JSONArray>();
    private Map<String, ProductsBean> pBeanMapList = new HashMap<String, ProductsBean>();

    public RetailersModel(Context context, TDCSalesActivity activity) {
        this.context = context;
        this.activity = activity;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = df.format(cal.getTime());

        cal.add(Calendar.DATE, -30);
        fromDate = df.format(cal.getTime());

    }

    public void getRetailersListSales(String s) {
        try {
            mAgentId = s;
            if (mTDCCustomerList.size() > 0) {
                mTDCCustomerList.clear();
            }

            if (billnoList.size() > 0) {
                billnoList.clear();
            }
            if (userIdsList.size() > 0) {
                userIdsList.clear();
            }
            if (routeIdsList.size() > 0) {
                routeIdsList.clear();
            }

            if (productIdsList.size() > 0) {
                productIdsList.clear();
            }
            if (taxpercentList.size() > 0) {
                taxpercentList.clear();
            }
            if (quantitysList.size() > 0) {
                quantitysList.clear();
            }
            if (fromDatesList.size() > 0) {
                fromDatesList.clear();
            }
            if (toDatesList.size() > 0) {
                toDatesList.clear();
            }
            if (unitPriceArray.size() > 0) {
                unitPriceArray.clear();
            }

            if (userDataArray.size() > 0) {
                userDataArray.clear();
            }
            if (proIdsList.size() > 0) {
                proIdsList.clear();
            }
            if (taxPercentsList.size() > 0) {
                taxPercentsList.clear();
            }
            if (unitPricesList.size() > 0) {
                unitPricesList.clear();
            }
            if (quantitesList.size() > 0) {
                quantitesList.clear();
            }
            if (amountsList.size() > 0) {
                amountsList.clear();
            }
            if (taxAmountsList.size() > 0) {
                taxAmountsList.clear();
            }

            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String ordersURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.PORT_AGENTS_LIST, Constants.RETAILERS_TDC_SALESLIST);
                JSONArray customerArray = new JSONArray();
                customerArray.put(s);
                JSONObject params = new JSONObject();
                params.put("customer", customerArray);
                params.put("filter_by_filed", "created_by");//created_by for all sales
                params.put("from_date", fromDate);
                params.put("to_date", currentDate);

                System.out.println("URL:::: " + ordersURL);
                System.out.println("INPUT:::: " + params.toString());
                AsyncRequest routeidRequest = new AsyncRequest(context, this, ordersURL, AsyncRequest.MethodType.POST, params);
                routeidRequest.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void asyncResponse(String response, Constants.RequestCode requestCode) {
        try {
            CustomProgressDialog.hideProgressDialog();

            // System.out.println("RETAILERS RESPONSE=====::: " + response);

            JSONArray respArray = new JSONArray(response);
            int resLength = respArray.length();
            if (resLength > 0) {
                synchronized (this) {
                    for (int j = 0; j < resLength; j++) {
                        TDCSaleOrder saleOrder = new TDCSaleOrder();
                        JSONObject resObj = respArray.getJSONObject(j);

                        // Product Ids
                        if (resObj.has("product_ids")) {
                            proIdsList.add(resObj.getJSONArray("product_ids").toString());
                        }

                        // TAX Percent
                        if (resObj.has("tax_percent")) {
                            taxPercentsList.add(resObj.getJSONArray("tax_percent").toString());
                        }

                        // UnitPrice
                        if (resObj.has("unit_price")) {
                            unitPricesList.add(resObj.getJSONArray("unit_price").toString());
                        }

                        // Quantity
                        if (resObj.has("quantity")) {
                            quantitesList.add(resObj.getJSONArray("quantity").toString());
                        }

                        // Amount
                        if (resObj.has("amount")) {
                            amountsList.add(resObj.getJSONArray("amount").toString());
                        }

                        // TAX Amount
                        if (resObj.has("tax_amount")) {
                            taxAmountsList.add(resObj.getJSONArray("tax_amount").toString());
                        }

                        // Sale Value
                        if (resObj.has("sale_value")) {
                            saleOrder.setOrderSubTotal(Double.parseDouble(resObj.getString("sale_value")));
                        }

                        // Bill no
                        if (resObj.has("bill_no")) {
                            saleOrder.setOrderBillNumber(resObj.getString("bill_no"));
                        }

                        // Order Date
                        if (resObj.has("created_on")) {
                            String dattt = resObj.getString("created_on");
                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                            Date date = format.parse(dattt);
                            saleOrder.setCreatedOn(date.getTime());
                            saleOrder.setOrderDate(Utility.formatTime(date.getTime(), Constants.TDC_SALES_ORDER_DATE_SAVE_FORMAT));
                        }

                        // Products Array
                        if (resObj.has("user_data")) {
                            JSONObject userObj = resObj.getJSONObject("user_data");

                            saleOrder.setSelectedCustomerUserId(userObj.getString("_id"));

                            saleOrder.setCreatedBy(userObj.getString("created_by"));

                            if (userObj.has("stakeholder_id")) {
                                String stakeId = userObj.getString("stakeholder_id");
                                if (stakeId.equals("7")) {
                                    // Retailer
                                    saleOrder.setSelectedCustomerName(userObj.getString("first_name"));
                                    saleOrder.setSelectedCustomerType(Long.parseLong("1"));

                                    if (userObj.has("code")) {
                                        String c = userObj.getString("code");
                                        saleOrder.setSelectedCustomerCode(c);
                                        String[] cArray = c.split("-");
                                        String s1 = cArray[1];
                                        String s2 = s1.substring(1, s1.length());
                                        //System.out.println("S@@@@@@@@@@@@@@@@@@::: " + s2);
                                        saleOrder.setSelectedCustomerId(Long.parseLong(s2));
                                    }
                                } else {
                                    // Consumer
                                    saleOrder.setSelectedCustomerName(userObj.getString("last_name"));
                                    saleOrder.setSelectedCustomerType(Long.parseLong("0"));

                                    if (userObj.has("code")) {
                                        String c = userObj.getString("code");
                                        saleOrder.setSelectedCustomerCode(c);
                                        if (!c.equals("0")) {
                                            String[] cArray = c.split("-");
                                            String s1 = cArray[1];
                                            String s2 = s1.substring(1, s1.length());
                                            //System.out.println("S@@@@@@@@@@@@@@@@@@::: " + s2);
                                            saleOrder.setSelectedCustomerId(Long.parseLong(s2));
                                        } else {
                                            saleOrder.setSelectedCustomerId(0); // Worst case
                                        }
                                    }
                                }
                            }
                        }

                        String prodIdsArray = proIdsList.get(j).toString();
                        String taxpercentArray = taxPercentsList.get(j).toString();
                        String unitPriceArray = unitPricesList.get(j).toString();
                        String quantityArray = quantitesList.get(j).toString();
                        String amountArray = amountsList.get(j).toString();
                        String taxAmountArray = taxAmountsList.get(j).toString();

                        JSONArray prA = new JSONArray(prodIdsArray);
                        JSONArray taxPercentA = new JSONArray(taxpercentArray);
                        JSONArray unitPriceA = new JSONArray(unitPriceArray);
                        JSONArray quantityA = new JSONArray(quantityArray);
                        JSONArray amountA = new JSONArray(amountArray);
                        JSONArray taxAmountA = new JSONArray(taxAmountArray);

                        if (prA.length() > 0) {
                            for (int r = 0; r < prA.length(); r++) {
                                String title = mDBHelper.getProductName(prA.get(r).toString(), "product_title");
                                //System.out.println("PR TITLE::: " + title);
                                ProductsBean pBean = new ProductsBean();

                                pBean.setProductId(prA.get(r).toString());
                                pBean.setProductTitle(title);
                                pBean.setProductRatePerUnit(Double.parseDouble(unitPriceA.get(r).toString()));
                                pBean.setSelectedQuantity(Double.parseDouble(quantityA.get(r).toString()));
                                pBean.setProductAmount(Double.parseDouble(amountA.get(r).toString()));
                                pBean.setProductTaxPerUnit(Double.parseDouble(taxPercentA.get(r).toString()));
                                pBean.setTaxAmount(Double.parseDouble(taxAmountA.get(r).toString()));

                                pBeanMapList.put(prA.get(r).toString(), pBean);
                            }
                        }
                        saleOrder.setProductsList(pBeanMapList);

                        mDBHelper.insertIntoTDCSalesOrdersTable(saleOrder);

//                    System.out.println("BI::" + saleOrder.getOrderBillNumber());
//                    System.out.println("NAME::" + saleOrder.getSelectedCustomerName());
//                    System.out.println("MAP LSIT::" + saleOrder.getProductsList().size());
                    }
                }
                synchronized (this) {
                    activity.showAlertDialog1(activity, "Sync Process", "Sales sync completed succssfully.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


