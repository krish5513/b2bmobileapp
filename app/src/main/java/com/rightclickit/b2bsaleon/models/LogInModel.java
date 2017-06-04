package com.rightclickit.b2bsaleon.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.rightclickit.b2bsaleon.activities.LoginActivity;
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

import static com.rightclickit.b2bsaleon.util.NetworkConnectionDetector.displayNoNetworkError;

/**
 * Created by venkat on 02/07/16.
 */
public class LogInModel implements OnAsyncRequestCompleteListener {
    public static final String TAG = LoginActivity.class.getSimpleName();

    private Context context;
    private LoginActivity activity;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;

    public LogInModel(Context context, LoginActivity activity) {
        this.context = context;
        this.activity = activity;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);
    }

    public void validateUserLogin(final String email, final String pwd) {
        try {
            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String logInURL = String.format("%s%s%s", Constants.MAIN_URL,Constants.PORT_LOGIN, Constants.LOGIN_SERVICE);
                JSONObject params = new JSONObject();
                params.put("email", email.trim());
                params.put("password", Utility.getMd5String(pwd.trim()));

                AsyncRequest loginRequest = new AsyncRequest(context, this, logInURL, AsyncRequest.MethodType.POST, params);
                loginRequest.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void asyncResponse(String response, Constants.RequestCode requestCode) {
        try {
            System.out.println("========= response = " + response);
            String id = "",userCode="",userName="",email="",phone="",
                    profilePic="",stakeHolderId="",address="",deviceSync="",accessDevice="",backUp="",routeArrayListString=""
                    , latitude="",longitude="",password="";
            JSONObject logInResponse = new JSONObject(response);
            if (logInResponse.getInt("result_status") == 1) {
                if(logInResponse.has("token")){
                    mPreferences.putString("token",logInResponse.getString("token"));
                }
                if(logInResponse.has("_id")){
                    mPreferences.putString("userId",logInResponse.getString("_id"));
                    id = logInResponse.getString("_id");
                }
                if(logInResponse.has("code")){
                    userCode = logInResponse.getString("code");
                }
                if(logInResponse.has("first_name") || logInResponse.has("last_name")){
                    userName = logInResponse.getString("first_name") + logInResponse.getString("last_name");
                }
                if(logInResponse.has("email")){
                    email = logInResponse.getString("email");
                }
                if(logInResponse.has("password")){
                    password = logInResponse.getString("password");
                }
                if(logInResponse.has("phone")){
                    phone = logInResponse.getString("phone");
                }
                if(logInResponse.has("avatar")){
                    profilePic = logInResponse.getString("avatar");
                }
                if(logInResponse.has("stakeholder_id")){
                    mPreferences.putString("stakeId",logInResponse.getString("stakeholder_id"));
                    stakeHolderId = logInResponse.getString("stakeholder_id");
                }
                if(logInResponse.has("address")){
                    address = logInResponse.getString("address");
                }
                if(logInResponse.has("device_sync")){
                    deviceSync = logInResponse.getString("device_sync");
                }
                if(logInResponse.has("access_device")){
                    accessDevice = logInResponse.getString("access_device");
                }
                if(logInResponse.has("back_up")){
                    backUp = logInResponse.getString("back_up");
                }
                if (logInResponse.has("route_id")){
                    JSONArray routesArray = logInResponse.getJSONArray("route_id");
                    JSONObject json = new JSONObject();
                    json.put("routeArray", routesArray);
                    routeArrayListString = json.toString();
                    // System.out.println("Routes Array List Is:: "+ routeArrayList);
                }
                if(logInResponse.has("latitude")){
                    latitude = logInResponse.getString("latitude");
                }
                if(logInResponse.has("longitude")){
                    longitude = logInResponse.getString("longitude");
                }
                if (logInResponse.has("created_by")){
                    mPreferences.putString("createdBy",logInResponse.getString("created_by"));
                }
                if (logInResponse.has("created_on")){
                    mPreferences.putString("createdOn",logInResponse.getString("created_on"));
                }
                if (logInResponse.has("updated_on")){
                    mPreferences.putString("updatedOn",logInResponse.getString("updated_on"));
                }
                if (logInResponse.has("updated_by")){
                    mPreferences.putString("updatedBy",logInResponse.getString("updated_by"));
                }

                if(accessDevice.equals("YES")) {
                    if(mDBHelper.getUserDetailsTableCount()>0) {
                        mDBHelper.deleteValuesFromUserDetailsTable();
                    }
                    synchronized (this) {
                        mDBHelper.insertUserDetails(id, userCode, userName, email, phone, profilePic, stakeHolderId, address, deviceSync, accessDevice, backUp, routeArrayListString,
                                "", "", "", latitude, longitude, password);
                    }
                    synchronized (this) {
                        activity.logInSuccess();
                    }
                }else {
                    displayNoNetworkError(context);
                }
            } else {
                displayNoNetworkError(context);
                //  activity.logInError();
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
