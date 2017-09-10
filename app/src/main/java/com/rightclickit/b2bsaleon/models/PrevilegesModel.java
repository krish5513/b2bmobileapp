package com.rightclickit.b2bsaleon.models;

import android.content.Context;

import com.rightclickit.b2bsaleon.activities.DashboardActivity;
import com.rightclickit.b2bsaleon.activities.LoginActivity;
import com.rightclickit.b2bsaleon.activities.SettingsActivity;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.OnAsyncRequestCompleteListener;
import com.rightclickit.b2bsaleon.util.AsyncRequest;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Sekhar Kuppa on 5/17/2017.
 */

public class PrevilegesModel implements OnAsyncRequestCompleteListener {

    public static final String TAG = DashboardActivity.class.getSimpleName();

    private Context context;
    private LoginActivity activity;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;

    public PrevilegesModel(Context context, LoginActivity activity) {
        this.context = context;
        this.activity = activity;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);
    }

  /*  public void getUserPrevileges() {
        try {
            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String token = "token="+mPreferences.getString("token");
                String settingsURL = String.format("%s%s%s%s", Constants.MAIN_URL,Constants.PORT_USER_PREVILEGES, Constants.GET_USER_PREVILEGES_SERVICE,token);
                HashMap<String,String> params = new HashMap<String, String>();
                params.put("type[0]", "m");

                AsyncRequest routeidRequest = new AsyncRequest(context, this, settingsURL, AsyncRequest.MethodType.POST, params);
                routeidRequest.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    @Override
    public void asyncResponse(String response, Constants.RequestCode requestCode) {
        try{
            String userActId="",userActTag="",userActStatus="";
            System.out.println("PREVILEGES RESPONSE ===== "+response);
            JSONArray previlegesArray = new JSONArray(response);
            if(previlegesArray.length()>0){
                for (int i = 0;i<previlegesArray.length();i++){
                    JSONObject previlegeObj = previlegesArray.getJSONObject(i);
                    if(previlegeObj.has("tag")){
                        userActTag = previlegeObj.getString("tag");
                        // User activity
                        if (userActTag.equals("user_activity")){
                            userActId = previlegeObj.getString("_id");
                            if (previlegeObj.has("status")){
                                if(previlegeObj.getString("status").equals("A")){
                                    if (previlegeObj.has("action_data")) {
                                        JSONArray userActionsArray = previlegeObj.getJSONArray("action_data");
                                        if (userActionsArray.length()>0){
                                            for (int j = 0; j<userActionsArray.length();j++){
                                                JSONObject actObj = userActionsArray.getJSONObject(j);
                                                if (actObj.has("tag")){
                                                    if (actObj.getString("tag").equals("setup")){
                                                        if (actObj.has("status")){
                                                            userActStatus = actObj.getString("status");
                                                            mPreferences.putString("isSetup","visible");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }else {
                                    mPreferences.putString("isSetup","gone");
                                }
                            }
                           // mDBHelper.insertUserActivityDetails(userActId,mPreferences.getString("userId"),userActTag,userActStatus);
                        }
                        // ==============End of user activity===============

                        // Dashboard
                        if(userActTag.equals("dashboard")){
                            if (previlegeObj.has("status")){
                                if(previlegeObj.getString("status").equals("A")){
                                    mPreferences.putString("isDashboard","visible");
                                }else {
                                    mPreferences.putString("isDashboard","gone");
                                }
                            }
                        }
                        // =========== End Of Dashboard =========

                        // Trip sheets
                        if(userActTag.equals("tripsheets")){
                            if (previlegeObj.has("status")){
                                if(previlegeObj.getString("status").equals("A")){
                                    mPreferences.putString("isTripsheets","visible");
                                }else {
                                    mPreferences.putString("isTripsheets","gone");
                                }
                            }
                        }
                        // =========== End Of Tripsheets =========

                        // Customers
                        if(userActTag.equals("customers")){
                            if (previlegeObj.has("status")){
                                if(previlegeObj.getString("status").equals("A")){
                                    mPreferences.putString("isCustomers","visible");
                                }else {
                                    mPreferences.putString("isCustomers","gone");
                                }
                            }
                        }
                        // =========== End Of Customers =========

                        // Products
                        if(userActTag.equals("products")){
                            if (previlegeObj.has("status")){
                                if(previlegeObj.getString("status").equals("A")){
                                    mPreferences.putString("isProducts","visible");
                                }else {
                                    mPreferences.putString("isProducts","gone");
                                }
                            }
                        }
                        // =========== End Of Products =========

                        // Tdc
                        if(userActTag.equals("tdc")){
                            if (previlegeObj.has("status")){
                                if(previlegeObj.getString("status").equals("A")){
                                    mPreferences.putString("isTdc","visible");
                                }else {
                                    mPreferences.putString("isTdc","gone");
                                }
                            }
                        }
                        // =========== End Of Tdc =========
                    }
                }
            }
            activity.loadDashboard();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
