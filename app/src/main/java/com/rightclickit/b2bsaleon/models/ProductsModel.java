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
                String productsURL = String.format("%s%s%s", Constants.PORT_PRODUCTSLIST, Constants.MAIN_URL, Constants.PRODUCTSLIST_SERVICE);
                JSONObject params = new JSONObject();
                //  params.put("routeid", routeid.trim());


                AsyncRequest routeidRequest = new AsyncRequest(context, this, productsURL, AsyncRequest.MethodType.POST, params);
                routeidRequest.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void asyncResponse(String response, Constants.RequestCode requestCode) {

    }
}
