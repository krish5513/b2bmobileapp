package com.rightclickit.b2bsaleon.models;

import android.content.Context;

import com.rightclickit.b2bsaleon.activities.LoginActivity;
import com.rightclickit.b2bsaleon.activities.SettingsActivity;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.OnAsyncRequestCompleteListener;
import com.rightclickit.b2bsaleon.util.AsyncRequest;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.Utility;

import org.json.JSONObject;

/**
 * Created by PPS on 5/17/2017.
 */

public class SettingsModel implements OnAsyncRequestCompleteListener {

    public static final String TAG = SettingsActivity.class.getSimpleName();

    private Context context;
    private SettingsActivity activity;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;

    public SettingsModel(Context context, SettingsActivity activity) {
        this.context = context;
        this.activity = activity;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);
    }

    public void validateSettings(final String routeid) {
        try {
            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String settingsURL = String.format("%s%s%s", Constants.PORT_ROUTES_MASTER_DATA,Constants.MAIN_URL, Constants.ROUTEID_SERVICE);
                JSONObject params = new JSONObject();
              //  params.put("routeid", routeid.trim());


                AsyncRequest routeidRequest = new AsyncRequest(context, this, settingsURL, AsyncRequest.MethodType.GET, params);
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
