package com.rightclickit.b2bsaleon.models;

import android.content.Context;

import com.rightclickit.b2bsaleon.activities.Products_Activity;
import com.rightclickit.b2bsaleon.activities.SettingsActivity;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
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

/**
 * Created by PPS on 5/29/2017.
 */

public class ProductsModel  implements OnAsyncRequestCompleteListener {

    public static final String TAG = Products_Activity.class.getSimpleName();

    private Context context;
    private Products_Activity activity;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
    private String type="";
    private ArrayList<String> regionIdsList = new ArrayList<String>();
    private JSONArray routesArray;
    private ArrayList<ProductsBean> mProductsBeansList = new ArrayList<ProductsBean>();

    public ProductsModel(Context context, Products_Activity activity) {
        this.context = context;
        this.activity = activity;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);
    }

    public void getProductsList(String s) {
        try {
            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String productsURL = String.format("%s%s%s",  Constants.MAIN_URL, Constants.PORT_PRODUCTSLIST,Constants.PRODUCTSLIST_SERVICE);
                JSONObject params = new JSONObject();
                JSONArray array = new JSONArray();
                array.put(mPreferences.getString("RegionId"));
              //  array.put("591d7844dd3960135dbf02cd"); // Temoparary hard coded this region id...
                params.put("region_ids", array);
                System.out.println("PRO URL::: "+ productsURL);
                System.out.println("PRO DATA::: "+ params.toString());
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
            System.out.println("PRO RESPONE:: "+ response);
            JSONArray respArray = new JSONArray(response);
            int resLength = respArray.length();
            if(resLength>0){
                for (int j = 0;j<resLength;j++){
                    JSONObject resObj = respArray.getJSONObject(j);

                    ProductsBean productsBean = new ProductsBean();
                    if(resObj.has("_id")){
                        productsBean.setProductId(resObj.getString("_id"));
                    }
                    if(resObj.has("name")){
                        productsBean.setProductTitle(resObj.getString("name"));
                    }
                    if(resObj.has("code")){
                        productsBean.setProductCode(resObj.getString("code"));
                    }
                    if(resObj.has("description")){
                        productsBean.setProductDescription(resObj.getString("description"));
                    }
                    if(resObj.has("moq")){
                        productsBean.setProductMOQ(resObj.getString("moq"));
                    }
                    if(resObj.has("returnable_prod")){
                        productsBean.setProductReturnable(resObj.getString("returnable_prod"));
                    }
                    if(resObj.has("price_data")){
                        JSONArray priceJsonArray = resObj.getJSONArray("price_data");
                        int len = priceJsonArray.length();
                        if (len>0){
                            for (int k = 0;k<len;k++){
                                JSONObject priceObj = priceJsonArray.getJSONObject(k);
                                if(priceObj.has("price_type")){
                                    // Agent price
                                    if(priceObj.getString("price_type").equals("2")){
                                        productsBean.setProductAgentPrice(priceObj.getString("price"));
                                    }
                                    // Retailer price
                                    if(priceObj.getString("price_type").equals("3")){
                                        productsBean.setProductRetailerPrice(priceObj.getString("price"));
                                    }
                                    // Consumer price
                                    if(priceObj.getString("price_type").equals("4")){
                                        productsBean.setProductConsumerPrice(priceObj.getString("price"));
                                    }
                                }
                            }
                        }
                    }
                    if (resObj.has("image_data")){
                        JSONArray imageJsonArray = resObj.getJSONArray("image_data");
                        int len1 = imageJsonArray.length();
                        if (len1>0){
                            for (int m = 0;m <len1;m++){
                                JSONObject imageObj = imageJsonArray.getJSONObject(m);
                                if (imageObj.has("url")){
                                    String URL  = Constants.MAIN_URL+"/b2b/"+imageObj.getString("url");
                                    productsBean.setProductImageUrl(URL);
                                }
                            }
                        }
                    }

                    mProductsBeansList.add(productsBean);
                }
            }
            mDBHelper.insertProductDetails(mProductsBeansList);
            activity.loadProductsList(mProductsBeansList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
