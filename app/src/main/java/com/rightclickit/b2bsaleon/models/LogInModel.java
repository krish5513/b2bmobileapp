package com.rightclickit.b2bsaleon.models;

import android.content.Context;

import com.rightclickit.b2bsaleon.activities.LoginActivity;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.interfaces.OnAsyncRequestCompleteListener;
import com.rightclickit.b2bsaleon.util.AsyncRequest;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import org.json.JSONObject;

/**
 * Created by venkat on 02/07/16.
 */
public class LogInModel implements OnAsyncRequestCompleteListener {
    public static final String TAG = LoginActivity.class.getSimpleName();

    private Context context;
    private LoginActivity activity;

    public LogInModel(Context context, LoginActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void validateUserLogin(final String email, final String pwd) {
        try {
            if (new NetworkConnectionDetector(context).isNetworkConnected()) {
                String logInURL = String.format("%s%s", Constants.MAIN_URL, Constants.LOGIN_SERVICE);
                JSONObject params = new JSONObject();
                params.put("userName", email);
                params.put("password", pwd);

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
            JSONObject logInResponse = new JSONObject(response);
            if (logInResponse.getInt("status") == 200) {
                activity.logInSuccess();
            } else {
                activity.logInError();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
