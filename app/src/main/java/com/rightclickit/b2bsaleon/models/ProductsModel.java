package com.rightclickit.b2bsaleon.models;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rightclickit.b2bsaleon.activities.Products_Activity;
import com.rightclickit.b2bsaleon.activities.SettingsActivity;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderBean;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.customviews.CustomProgressDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.OnAsyncRequestCompleteListener;
import com.rightclickit.b2bsaleon.util.AsyncRequest;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by PPS on 5/29/2017.
 */

public class ProductsModel implements OnAsyncRequestCompleteListener {

    public static final String TAG = Products_Activity.class.getSimpleName();

    private Context context;
    private Products_Activity activity;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
    private String type = "";
    private ArrayList<String> regionIdsList = new ArrayList<String>();
    private JSONArray routesArray;
    private ArrayList<ProductsBean> mProductsBeansList = new ArrayList<ProductsBean>();
    private ArrayList<TakeOrderBean> mTakeOrderProductsBeansList = new ArrayList<TakeOrderBean>();
    private String mStock = "", mAgentPrice = "", mRetailerPrice = "", mConsumerPrice = "";
    private Gson gson;
    private ArrayList<String> actIdsList = new ArrayList<String>();
    SettingsActivity a;

    public ProductsModel(Context context, Products_Activity activity) {
        this.context = context;
        this.activity = activity;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);
        this.gson = new Gson();
    }

    public ProductsModel(Context context, SettingsActivity activity) {
        this.context = context;
        this.a = activity;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);
        this.gson = new Gson();
    }

    public void getProductsList(String s) {
        try {
            if (mProductsBeansList.size() > 0) {
                mProductsBeansList.clear();
            }
            if (mTakeOrderProductsBeansList.size() > 0) {
                mTakeOrderProductsBeansList.clear();
            }
            HashMap<String, String> userMapData = mDBHelper.getUsersData();
            JSONObject routesJob = new JSONObject(userMapData.get("route_ids").toString());
            JSONArray routesArray = routesJob.getJSONArray("routeArray");
            String storedHashMapString = mPreferences.getString("regionIds");
            java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
            }.getType();

            HashMap<String, String> testHashMap2 = gson.fromJson(storedHashMapString, type);
            // System.out.println("MAP ::: " + testHashMap2.size());
            for (int h = 0; h < routesArray.length(); h++) {
                if (testHashMap2.get(routesArray.get(h).toString()) != null) {
                    // System.out.println("ACT RID:: " + testHashMap2.get(routesArray.get(h).toString()));
                    actIdsList.add(testHashMap2.get(routesArray.get(h).toString()));
                }
            }


            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String productsURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.PORT_PRODUCTSLIST, Constants.PRODUCTSLIST_SERVICE);
                JSONObject params = new JSONObject();
                JSONArray array = new JSONArray();
                for (int y = 0; y < actIdsList.size(); y++) {
                    array.put(actIdsList.get(y).toString());
                }
                // array.put(mPreferences.getString("RegionId"));
                // array.put("591d7844dd3960135dbf02cd"); // Temoparary hard coded this region id...
                params.put("region_ids", array);
                System.out.println("PRO URL::: " + productsURL);
                System.out.println("PRO DATA::: " + params.toString());
                AsyncRequest routeidRequest = new AsyncRequest(context, this, productsURL, AsyncRequest.MethodType.POST, params);
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
            System.out.println("PRO RESPONE:: " + response);
            JSONArray respArray = new JSONArray(response);
            int resLength = respArray.length();
            if (resLength > 0) {
                for (int j = 0; j < resLength; j++) {
                    JSONObject resObj = respArray.getJSONObject(j);

                    ProductsBean productsBean = new ProductsBean();
                    TakeOrderBean takeOrderBean = new TakeOrderBean();
                    takeOrderBean.setmRouteId(mPreferences.getString("routeId"));
                    if (resObj.has("_id")) {
                        productsBean.setProductId(resObj.getString("_id"));
                        takeOrderBean.setmProductId(resObj.getString("_id"));
                    }
                    if (resObj.has("name")) {
                        productsBean.setProductTitle(resObj.getString("name"));
                        takeOrderBean.setmProductTitle(resObj.getString("name"));
                    }
                    if (resObj.has("code")) {
                        productsBean.setProductCode(resObj.getString("code"));
                    }
                    if (resObj.has("description")) {
                        productsBean.setProductDescription(resObj.getString("description"));
                    }
                    if (resObj.has("moq")) {
                        productsBean.setProductMOQ(resObj.getString("moq"));
                    }
                    if (resObj.has("returnable_prod")) {
                        productsBean.setProductReturnable(resObj.getString("returnable_prod"));
                    }
                    if (resObj.has("price_data")) {
                        JSONArray priceJsonArray = resObj.getJSONArray("price_data");
                        int len = priceJsonArray.length();
                        if (len > 0) {
                            for (int k = 0; k < len; k++) {
                                JSONObject priceObj = priceJsonArray.getJSONObject(k);
                                if (priceObj.has("price_type")) {
                                    // Agent price
                                    if (priceObj.getString("price_type").equals("2")) {
                                        productsBean.setProductAgentPrice(priceObj.getString("price"));
                                    }
                                    // Retailer price
                                    if (priceObj.getString("price_type").equals("3")) {
                                        productsBean.setProductRetailerPrice(priceObj.getString("price"));
                                    }
                                    // Consumer price
                                    if (priceObj.getString("price_type").equals("4")) {
                                        productsBean.setProductConsumerPrice(priceObj.getString("price"));
                                    }
                                }
                            }
                        }
                    }
                    if (resObj.has("product_units")) {
                        JSONArray productUnitJsonArray = resObj.getJSONArray("product_units");
                        int len = productUnitJsonArray.length();
                        if (len > 0) {
                            for (int k = 0; k < len; k++) {
                                JSONObject priceUnitObj = productUnitJsonArray.getJSONObject(k);
                                if (priceUnitObj.has("display")) {
                                    // Agent price

                                    productsBean.setProductUOM(priceUnitObj.getString("display"));

                                }
                            }
                        }
                    }

                    if (resObj.has("tax_data")) {
                        JSONArray taxJsonArray = resObj.getJSONArray("tax_data");
                        int len = taxJsonArray.length();
                        if (len > 0) {
                            for (int t = 0; t < len; t++) {
                                JSONObject taxObj = taxJsonArray.getJSONObject(t);
                                if (taxObj.has("control_code")) {
                                    productsBean.setControlCode(taxObj.getString("control_code"));
                                }
                                if (taxObj.has("tax_type")) {
                                    // Agent price
                                    if (taxObj.getString("tax_type").equals("3")) {
                                        productsBean.setProductgst(taxObj.getString("tax"));
                                    }
                                    // Retailer price
                                    else if (taxObj.getString("tax_type").equals("4")) {
                                        productsBean.setProductvat(taxObj.getString("tax"));
                                    }

                                }
                            }
                        }
                    }
                    if (resObj.has("image_data")) {
                        JSONArray imageJsonArray = resObj.getJSONArray("image_data");
                        int len1 = imageJsonArray.length();
                        if (len1 > 0) {
                            for (int m = 0; m < len1; m++) {
                                JSONObject imageObj = imageJsonArray.getJSONObject(m);
                                if (imageObj.has("url")) {
                                    //String URL  = Constants.MAIN_URL+"/b2b/"+imageObj.getString("url");
                                    productsBean.setProductImageUrl(imageObj.getString("url"));
                                }
                            }
                        }
                    }
                    takeOrderBean.setmProductStatus("0");
                    takeOrderBean.setmEnquiryId("");
                    takeOrderBean.setmAgentId("");
                    mProductsBeansList.add(productsBean);
                    mTakeOrderProductsBeansList.add(takeOrderBean);
                }
            }
            synchronized (this) {
                mDBHelper.insertProductDetails(mProductsBeansList);
            }
//            synchronized (this){
//                mDBHelper.insertTakeOrderProductDetails(mTakeOrderProductsBeansList);
//            }
            synchronized (this) {
                if (activity!=null)
                    activity.loadProductsList(mProductsBeansList);
            }

            Intent i = new Intent("android.intent.action.MAIN").putExtra("receiver_key", "completed");
            context.sendBroadcast(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
