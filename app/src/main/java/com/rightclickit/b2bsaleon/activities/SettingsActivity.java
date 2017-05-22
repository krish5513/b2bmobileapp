package com.rightclickit.b2bsaleon.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.customviews.CustomAlertDialog;
import com.rightclickit.b2bsaleon.customviews.CustomProgressDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.LogInModel;
import com.rightclickit.b2bsaleon.models.SettingsModel;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    EditText userName;
    EditText mobile;
    EditText region;
    EditText routeNo;
    EditText companyName;
    EditText vehicleNo;
    EditText salesOffice;
    EditText transporterName, deviceSync, accessDevice, backup;


    public static final String TAG = LoginActivity.class.getSimpleName();

    private MMSharedPreferences sharedPreferences;
    private Context applicationContext, activityContext;


    private Button logInButton;

    private boolean isAutoLogIn = false;

    SettingsModel settingsmodel;

    Button saveInfo;
    public static Toolbar toolbar;
    String str_companyName, str_userName, str_mobileNo, str_region, str_salesOffice, str_routeNo, str_vehicleNo, str_transporter, str_devicesync, str_accessdevice, str_backup;

    private DBHelper mDBHelper;

    private String mRouteName = "",mRegionName = "",mOfficeName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        try {
            applicationContext = getApplicationContext();
            activityContext = SettingsActivity.this;

            sharedPreferences = new MMSharedPreferences(applicationContext);

            settingsmodel = new SettingsModel(activityContext, this);

            routeNo = (EditText) findViewById(R.id.tv_routeNo);
            routeNo.requestFocus();
            routeNo.setCursorVisible(true);
            routeNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == R.id.login || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {

                        return true;
                    }
                    return false;
                }
            });


            this.getSupportActionBar().setTitle("SETTINGS");
            this.getSupportActionBar().setSubtitle(null);
            this.getSupportActionBar().setLogo(R.drawable.ic_settings_white_24dp);
            this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            this.getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            this.getSupportActionBar().setDisplayShowHomeEnabled(true);

//            final ActionBar actionBar = getSupportActionBar();
//            assert actionBar != null;
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

            companyName = (EditText) findViewById(R.id.tv_companyName);

            region = (EditText) findViewById(R.id.tv_region);
            userName = (EditText) findViewById(R.id.tv_userName);
            mobile = (EditText) findViewById(R.id.tv_mobileNo);
            salesOffice = (EditText) findViewById(R.id.tv_salesoffice);
            vehicleNo = (EditText) findViewById(R.id.tv_vehicleNo);
            saveInfo = (Button) findViewById(R.id.infoSave);
            transporterName = (EditText) findViewById(R.id.tv_transporterName);
            deviceSync = (EditText) findViewById(R.id.tv_devicesync);
            accessDevice = (EditText) findViewById(R.id.tv_accessdevice);
            backup = (EditText) findViewById(R.id.tv_backup);
            saveInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveValidation();
                }



                private void saveValidation() {

                    str_companyName = companyName.getText().toString();
                    str_userName = userName.getText().toString();
                    str_mobileNo = mobile.getText().toString();
                    str_region = region.getText().toString();
                    str_salesOffice = salesOffice.getText().toString();
                    str_routeNo = routeNo.getText().toString();
                    str_vehicleNo = vehicleNo.getText().toString();
                    str_transporter = transporterName.getText().toString();
                    str_devicesync = deviceSync.getText().toString();
                    str_accessdevice = accessDevice.getText().toString();
                    str_backup = backup.getText().toString();

                    if (str_companyName.length() == 0 || str_companyName.length() == ' ') {
                        companyName.setError("Please enter CompanyName");
                        Toast.makeText(getApplicationContext(), "Please enter CompanyName", Toast.LENGTH_SHORT).show();
                        //
                    } else if (str_userName.length() == 0 || str_userName.length() == ' ') {
                        companyName.setError(null);
                        userName.setError("Please enter Username");
                        Toast.makeText(getApplicationContext(), "Please enter Username", Toast.LENGTH_SHORT).show();

                    } else if (str_mobileNo.length() == 0 || str_mobileNo.length() == ' ' || str_mobileNo.length() != 10) {
                        userName.setError(null);
                        mobile.setError("Please enter 10 digit Mobilenumber");
                        Toast.makeText(getApplicationContext(), "Please enter 10 digit Mobilenumber", Toast.LENGTH_SHORT).show();

                    } else if (str_region.length() == 0 || str_region.length() == ' ') {
                        mobile.setError(null);
                        region.setError("Please enter Region");
                        Toast.makeText(getApplicationContext(), "Please enter Region", Toast.LENGTH_SHORT).show();

                    } else if (str_salesOffice.length() == 0 || str_salesOffice.length() == ' ') {
                        region.setError(null);
                        salesOffice.setError("Please enter Salesoffice");
                        Toast.makeText(getApplicationContext(), "Please enter Salesoffice", Toast.LENGTH_SHORT).show();

                    } else if (str_routeNo.length() == 0 || str_routeNo.length() == ' ') {
                        salesOffice.setError(null);
                        routeNo.setError("Please enter Routeno");
                        Toast.makeText(getApplicationContext(), "Please enter Routeno", Toast.LENGTH_SHORT).show();

                    } else if (str_vehicleNo.length() == 0 || str_vehicleNo.length() == ' ') {
                        routeNo.setError(null);
                        vehicleNo.setError("Please enter Vehicleno");
                        Toast.makeText(getApplicationContext(), "Please enter Vehicleno", Toast.LENGTH_SHORT).show();

                    } else if (str_transporter.length() == 0 || str_transporter.length() == ' ') {
                        vehicleNo.setError(null);
                        transporterName.setError("Please enter Transporter Name");
                        Toast.makeText(getApplicationContext(), "Please enter Transporter Name", Toast.LENGTH_SHORT).show();

                    } else if (str_devicesync.length() == 0 || str_devicesync.length() == ' ') {
                        transporterName.setError(null);
                        deviceSync.setError("Please enter deviceSync time");
                        Toast.makeText(getApplicationContext(), "Please enter deviceSync time", Toast.LENGTH_SHORT).show();

                    } else if (str_accessdevice.length() == 0 || str_accessdevice.length() == ' ') {
                        deviceSync.setError(null);
                        accessDevice.setError("Please enter accessDevice");
                        Toast.makeText(getApplicationContext(), "Please enter accessDevice", Toast.LENGTH_SHORT).show();

                    } else if (str_backup.length() == 0 || str_backup.length() == ' ') {
                        accessDevice.setError(null);
                        backup.setError("Please enter backup days");
                        Toast.makeText(getApplicationContext(), "Please enter backup days", Toast.LENGTH_SHORT).show();

                    } else {
                        companyName.setError(null);
                        userName.setError(null);
                        mobile.setError(null);
                        region.setError(null);
                        salesOffice.setError(null);
                        routeNo.setError(null);
                        vehicleNo.setError(null);
                        transporterName.setError(null);
                        deviceSync.setError(null);
                        accessDevice.setError(null);
                        backup.setError(null);
                        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("routeDetails", MODE_PRIVATE).edit();
                        editor.putString("companyName", companyName.getText().toString());
                        editor.putString("routeName", routeNo.getText().toString());
                        editor.putString("region", region.getText().toString());
                        editor.putString("userName", userName.getText().toString());
                        editor.putString("mobile", mobile.getText().toString());
                        editor.putString("salesOffice", salesOffice.getText().toString());
                        editor.putString("vehicleNo", vehicleNo.getText().toString());
                        editor.putString("transporterName", transporterName.getText().toString());
                        Log.e("save on click", routeNo.getText().toString());
                        editor.commit();

                        companyName.setCursorVisible(false);
                        routeNo.setCursorVisible(false);
                        userName.setCursorVisible(false);
                        mobile.setCursorVisible(false);
                        region.setCursorVisible(false);
                        salesOffice.setCursorVisible(false);
                        vehicleNo.setCursorVisible(false);
                        transporterName.setCursorVisible(false);

                        Toast.makeText(getApplicationContext(), "Details saved successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            mDBHelper = new DBHelper(SettingsActivity.this);

            HashMap<String,String> userMapData = mDBHelper.getUsersData();
            System.out.println("The User Data NAME Is:: "+userMapData.get("name"));
            System.out.println("The User Data PHONE Is:: "+userMapData.get("phone"));
            System.out.println("The User Data DEVICE SYNC Is:: "+userMapData.get("device_sync"));
            System.out.println("The User Data BACKUP Is:: "+userMapData.get("backup"));
            System.out.println("The User Data ACCESS DEVICE Is:: "+userMapData.get("access_device"));
            System.out.println("The User Data ROUTE IDS Is:: "+userMapData.get("route_ids"));

            JSONObject routesJob = new JSONObject(userMapData.get("route_ids").toString());
            JSONArray routesArray = routesJob.getJSONArray("routeArray");
            for (int l = 0;l<routesArray.length();l++){
                System.out.println("The Route Id IS::: "+ routesArray.get(l).toString());
                List<String> routesDataList = mDBHelper.getRouteDataByRouteId(routesArray.get(l).toString());
                for (int k = 0;k<routesDataList.size();k++){
                    System.out.println(" LOOPPPPPPPPPPPPPP "+k);
                    if(routesDataList.get(k)!=null) {
                        switch (k){
                            case 1:
                                mRouteName =  routesDataList.get(1);
                                break;
                            case 2:
                                mRegionName =  routesDataList.get(2);
                                break;
                            case 3:
                                mOfficeName =  routesDataList.get(3);
                                break;
                        }
                    }
                }
            }
            System.out.println("ROUTE NAME IS:::: "+ mRouteName);
            System.out.println("REGION NAME IS:::: "+ mRegionName);
            System.out.println("OFFICE NAME IS:::: "+ mOfficeName);
//            List<String> routesDataList = mDBHelper.getRoutesMasterData();
//            System.out.println("The Data Is SIZE ======= ::: "+ routesDataList.size());
//            for (int i=0;i<routesDataList.size();i++){
//                System.out.println("The Data Is::: "+ routesDataList.get(0).toString());
//            }

            // Append all the db data to lables.
            companyName.setText("");
            if(userMapData.get("name")!=null) {
                userName.setText(userMapData.get("name").toString());
            }
            if(userMapData.get("phone")!=null) {
                mobile.setText(userMapData.get("phone").toString());
            }
            region.setText(mRegionName);
            salesOffice.setText(mOfficeName);
            routeNo.setText(mRouteName);
            vehicleNo.setText("");
            transporterName.setText("");
            if(userMapData.get("access_device")!=null) {
                accessDevice.setText(userMapData.get("access_device").toString());
            }
            if(userMapData.get("device_sync")!=null) {
                deviceSync.setText(userMapData.get("device_sync").toString());
            }
            if(userMapData.get("backup")!=null) {
                backup.setText(userMapData.get("backup").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();

        }



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.notifications) {
            loadNotifications();
            Toast.makeText(this, "Clicked on Notifications...", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.logout) {
            showAlertDialogWithCancelButton(SettingsActivity.this,"User Action!","Are you sure, you want to Log Out?");
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return true;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }



    private void loadLogout() {
        sharedPreferences.putString("isLogin","false");
        Intent loginIntent=new Intent(SettingsActivity.this,LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private void loadNotifications() {
        Intent notificationsIntent=new Intent(SettingsActivity.this,NotificationsActivity.class);
        startActivity(notificationsIntent);
        finish();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        menu.findItem(R.id.notifications).setVisible(true);
        menu.findItem(R.id.settings).setVisible(false);
        menu.findItem(R.id.logout).setVisible(true);
        menu.findItem(R.id.action_search).setVisible(false);


        return super.onPrepareOptionsMenu(menu);
    }

    public void authenticateUser(String routeid) {
        CustomProgressDialog.showProgressDialog(activityContext, Constants.LOADING_MESSAGE);
        settingsmodel.validateSettings(routeid);

    }

    private void showAlertDialogWithCancelButton(Context context, String title, String message) {
        try {
            AlertDialog alertDialog = null;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    loadLogout();
                }
            });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();


            Button cancelButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (cancelButton != null)
                cancelButton.setTextColor(ContextCompat.getColor(context, R.color.gray));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
