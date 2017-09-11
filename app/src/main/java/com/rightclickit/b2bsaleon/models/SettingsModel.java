package com.rightclickit.b2bsaleon.models;

import android.content.Context;

import com.rightclickit.b2bsaleon.activities.LoginActivity;
import com.rightclickit.b2bsaleon.activities.SettingsActivity;
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

import java.util.HashMap;

/**
 * Created by PPS on 5/17/2017.
 */

public class SettingsModel implements OnAsyncRequestCompleteListener {

    public static final String TAG = SettingsActivity.class.getSimpleName();

    private Context context;
    private SettingsActivity activity;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
    private boolean isSaveDeviceDetails;
    private String did = "", transporterName = "", vehicleNumber = "", companyname = "", routeCode = "";

    public SettingsModel(Context context, SettingsActivity activity) {
        this.context = context;
        this.activity = activity;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);
    }

    public void validateSettings(final String routeid) {
        try {
            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String settingsURL = String.format("%s%s%s", Constants.PORT_ROUTES_MASTER_DATA, Constants.MAIN_URL, Constants.ROUTEID_SERVICE);
                JSONObject params = new JSONObject();
                //  params.put("routeid", routeid.trim());


                AsyncRequest routeidRequest = new AsyncRequest(context, this, settingsURL, AsyncRequest.MethodType.GET, params);
                routeidRequest.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changePassword(String userId, String password) {
        try {
            isSaveDeviceDetails = false;
            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String settingsURL = String.format("%s%s%s%s", Constants.MAIN_URL, Constants.PORT_USER_PREVILEGES, Constants.CHANGE_PASSWORD_SERVICE, userId);
                System.out.println("The URL IS==== " + settingsURL);
                JSONObject params = new JSONObject();
                params.put("password", password.trim());
                params.put("action", "reset_password");
                System.out.println("The PARAMS ARE==== " + params.toString());


                AsyncRequest routeidRequest = new AsyncRequest(context, this, settingsURL, AsyncRequest.MethodType.POST, params);
                routeidRequest.execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveDeviceDetails(String deviceId, String vechicleNumber, String transporterName, String companyname, String routeCode, JSONArray mRouteCode) {
        try {
            isSaveDeviceDetails = true;
            this.did = deviceId;
            this.vehicleNumber = vechicleNumber;
            this.transporterName = transporterName;
            this.companyname = companyname;
            this.routeCode = routeCode;
            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String deviceName = mPreferences.getString("name") + deviceId.substring(deviceId.length() - 3);
                String settingsURL = String.format("%s%s%s", Constants.MAIN_URL, Constants.PORT_ADD, Constants.SAVE_DEVICE_DETAILS);
                System.out.println("The URL IS==== " + settingsURL);
                //HashMap<String, Object> params = new HashMap<String, Object>();
                JSONObject params = new JSONObject();
                params.put("device_id", deviceId);
                params.put("user_id", mPreferences.getString("userId"));
                params.put("device_name", deviceName);
                params.put("vehicle_no", vechicleNumber);
                params.put("transporter_name", transporterName);
                // params.put("companyname", companyname);
                params.put("route_code", mRouteCode);
                System.out.println("The PARAMS ARE==== " + params.toString());

                AsyncRequest routeidRequest = new AsyncRequest(context, this, settingsURL, AsyncRequest.MethodType.POST, params);
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
            System.out.println("========= response = " + response);
            JSONObject logInResponse = new JSONObject(response);
            if (isSaveDeviceDetails) {
                mPreferences.putString("deviceId", did);
                mPreferences.putString("transporterName", transporterName);
                mPreferences.putString("vehicleNumber", vehicleNumber);
                // mPreferences.putString("companyname",companyname);
                long f = mDBHelper.updateUserDetails(mPreferences.getString("userId"), companyname, "", "",
                        "", "", "", "", "", "", "", "", "", did, transporterName,
                        vehicleNumber, "", "");
            } else {
                if (logInResponse.getInt("result_status") == 1) {
                    activity.goBackToDashboard();
                } else {

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
