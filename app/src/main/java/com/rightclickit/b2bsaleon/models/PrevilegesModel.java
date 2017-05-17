package com.rightclickit.b2bsaleon.models;

import android.content.Context;

import com.rightclickit.b2bsaleon.activities.DashboardActivity;
import com.rightclickit.b2bsaleon.activities.SettingsActivity;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.OnAsyncRequestCompleteListener;
import com.rightclickit.b2bsaleon.util.AsyncRequest;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import org.json.JSONObject;

/**
 * Created by Sekhar Kuppa on 5/17/2017.
 */

public class PrevilegesModel implements OnAsyncRequestCompleteListener {

    public static final String TAG = DashboardActivity.class.getSimpleName();

    private Context context;
    private DashboardActivity activity;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;

    public PrevilegesModel(Context context, DashboardActivity activity) {
        this.context = context;
        this.activity = activity;
        this.mPreferences = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(context);
    }

    public void getUserPrevileges() {
        try {
            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String settingsURL = String.format("%s%s", Constants.MAIN_URL, Constants.GET_USER_PREVILEGES_SERVICE);
                JSONObject params = new JSONObject();
                params.put("type[0]", "m");


                AsyncRequest routeidRequest = new AsyncRequest(context, this, settingsURL, AsyncRequest.MethodType.POST, params);
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
