package com.rightclickit.b2bsaleon.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.customviews.CustomAlertDialog;
import com.rightclickit.b2bsaleon.customviews.CustomProgressDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.LogInModel;
import com.rightclickit.b2bsaleon.models.PrevilegesModel;
import com.rightclickit.b2bsaleon.services.SyncNotificationsListService;
import com.rightclickit.b2bsaleon.services.SyncRoutesMasterDetailsService;
import com.rightclickit.b2bsaleon.services.SyncUserPrivilegesService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author venkat
 *         <p/>
 *         A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements EasyPermissions.PermissionCallbacks {
    public static final String TAG = LoginActivity.class.getSimpleName();

    private MMSharedPreferences sharedPreferences;
    private Context applicationContext, activityContext;

    private EditText mEmailView, mPasswordView;
    private Button logInButton;
    private String emailId, password;
    private boolean isAutoLogIn = false;

    private DBHelper mDBHelper;

    LogInModel logInModel;
    PrevilegesModel previlegesModel;

    private Runnable ru;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            mDBHelper = new DBHelper(LoginActivity.this);
            applicationContext = getApplicationContext();
            activityContext = LoginActivity.this;

            sharedPreferences = new MMSharedPreferences(applicationContext);

            logInModel = new LogInModel(activityContext, this);
            previlegesModel = new PrevilegesModel(activityContext, this);

            mEmailView = (EditText) findViewById(R.id.user_name);
            mEmailView.requestFocus();
            mEmailView.setCursorVisible(true);
            mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == R.id.login || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });

            mPasswordView = (EditText) findViewById(R.id.password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == R.id.login || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });


            logInButton = (Button) findViewById(R.id.login_btn);
            logInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });

            if (sharedPreferences.getString("isLogin").equals("true")) {
                loadDashboard();
            }

            requestPermissions();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (CustomAlertDialog.isAlertDialogShown && CustomAlertDialog.alertDialog != null) {
            CustomAlertDialog.alertDialog.dismiss();
        }

        if (CustomProgressDialog.isProgressDialogShown) {
            CustomProgressDialog.hideProgressDialog();
        }
    }

    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        emailId = mEmailView.getText().toString().trim();
        password = mPasswordView.getText().toString();
        sharedPreferences.putString("enterEmail", emailId);
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(emailId)) {
            mEmailView.setError(getString(R.string.email_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!Utility.isValidEmail(emailId)) { // Checking for a valid email address.
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.password_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first form field with an error.
            focusView.requestFocus();
        } else {
            if (mDBHelper.getUserDetailsTableCount() > 0) {
                // Data is there and do actions..
                String userId = mDBHelper.getUserId(emailId, Utility.getMd5String(password.trim()));
                if (!userId.equals("")) {
                    // User exists and do actions..
                    loadDashboard();
                } else if (new NetworkConnectionDetector(LoginActivity.this).isNetworkConnected()) {
                    authenticateUser(emailId, password);
                } else {
                    // The entered user is different...
                    LogInModel.displayNoNetworkError(LoginActivity.this);
                }
            } else if (new NetworkConnectionDetector(LoginActivity.this).isNetworkConnected()) {
                authenticateUser(emailId, password);
            } else {
                LogInModel.displayNoNetworkError(LoginActivity.this);
            }
        }
    }

    public void authenticateUser(String email, String pwd) {
        // CustomProgressDialog.showProgressDialog(activityContext, Constants.LOADING_MESSAGE);
        logInModel.validateUserLogin(email, pwd);
    }

    public void logInError() {
        CustomProgressDialog.hideProgressDialog();
        CustomAlertDialog.showAlertDialog(activityContext, "Invalid Login", getResources().getString(R.string.login_response_invalid_login));
    }

    public void logInSuccess() {
        try {
            CustomProgressDialog.hideProgressDialog();
            //Saving username & password in shared preferences if it's first time log in
            if (!isAutoLogIn) {
                sharedPreferences.putString("emailId", emailId);
                sharedPreferences.putString("password", password);
            }
            sharedPreferences.putString("isloginClick", "true");
            sharedPreferences.putString("isLogin", "true");

            // Call Previleges API
            // previlegesModel.getUserPrevileges();
            synchronized (this) {
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                if (new NetworkConnectionDetector(LoginActivity.this).isNetworkConnected()) {
                    startService(new Intent(LoginActivity.this, SyncUserPrivilegesService.class));
                }
            }

//            ru = new Runnable() {
//                @Override
//                public void run() {
//                    synchronized (this){
//                        System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
//                        mHandler.removeCallbacks(ru);
//                        loadDashboard();
//                    }
//                }
//            }; mHandler.postDelayed(ru,3000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDashboard() {

        if (mDBHelper.getUserDeviceId(sharedPreferences.getString("enterEmail")).equals("")) {
//            synchronized (this){
//                System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC"+mDBHelper.getRouteId());
//                //if (mDBHelper.getRouteId().length()==0) {
//                    if (new NetworkConnectionDetector(LoginActivity.this).isNetworkConnected()) {
//                        startService(new Intent(LoginActivity.this, SyncRoutesMasterDetailsService.class));
//                    }
//               // }
//            }
//            synchronized (this) {
//                System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDD");
            loadSettings();
            //           }
        } else {
            HashMap<String, String> userMapData = mDBHelper.getUsersData();
            //System.out.println("PRIVILEGE======= " + mDBHelper.getUserPrivilegeByUserIdAndPrivilegeName(userMapData.get("user_id"), "TripSheets"));
            Intent mainActivityIntent = null;
            if (mDBHelper.getUserPrivilegeByUserIdAndPrivilegeName(userMapData.get("user_id"), "Dashboard") > 0) {
                // Dashboard
                mainActivityIntent = new Intent(LoginActivity.this, DashboardActivity.class);
            } else if (mDBHelper.getUserPrivilegeByUserIdAndPrivilegeName(userMapData.get("user_id"), "TripSheets") > 0) {
                // Tripsheets
                mainActivityIntent = new Intent(LoginActivity.this, TripSheetsActivity.class);
            } else if (mDBHelper.getUserPrivilegeByUserIdAndPrivilegeName(userMapData.get("user_id"), "Customers") > 0) {
                // Customers
                mainActivityIntent = new Intent(LoginActivity.this, AgentsActivity.class);
            } else if (mDBHelper.getUserPrivilegeByUserIdAndPrivilegeName(userMapData.get("user_id"), "Products") > 0) {
                // Products
                mainActivityIntent = new Intent(LoginActivity.this, Products_Activity.class);
            } else if (mDBHelper.getUserPrivilegeByUserIdAndPrivilegeName(userMapData.get("user_id"), "TDC") > 0) {
                // TDC
                mainActivityIntent = new Intent(LoginActivity.this, TDCSalesActivity.class);
            } else if (mDBHelper.getUserPrivilegeByUserIdAndPrivilegeName(userMapData.get("user_id"), "Retailers") > 0) {
                // Retailers
                mainActivityIntent = new Intent(LoginActivity.this, RetailersActivity.class);
            }
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainActivityIntent);
            finish();
        }
    }

    public void loadSettings() {
        Intent mainActivityIntent = new Intent(LoginActivity.this, SettingsActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainActivityIntent);
        finish();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void requestPermissions() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA
                , Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INSTALL_LOCATION_PROVIDER};
        if (EasyPermissions.hasPermissions(LoginActivity.this, perms)) {
        } else {
            EasyPermissions.requestPermissions(this, "This app needs the following permission to access the app.", 0, perms);
        }
    }
}

