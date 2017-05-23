package com.rightclickit.b2bsaleon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.services.SyncRoutesMasterDetailsService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

public class DashboardActivity extends AppCompatActivity {
    private DBHelper mDBHelper;
    private MMSharedPreferences mPreferences;
    private String userActSetupStatus = "";
    private LinearLayout mDashBoardLayout;
    private LinearLayout mTripsheetsLayout;
    private LinearLayout mCustomersLayout;
    private LinearLayout mProductsLayout;
    private LinearLayout mTDCLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mDBHelper = new DBHelper(DashboardActivity.this);
        mPreferences = new MMSharedPreferences(DashboardActivity.this);
//       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
  //      setSupportActionBar(toolbar);


        this.getSupportActionBar().setLogo(R.drawable.dashboard_icon_white_24);
        this.getSupportActionBar().setTitle("DASHBOARD");
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);






        mDashBoardLayout = (LinearLayout) findViewById(R.id.DashboardLayout);
        mDashBoardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Clicked on Dashboard", Toast.LENGTH_SHORT).show();
            }
        });
        mTripsheetsLayout = (LinearLayout) findViewById(R.id.TripSheetsLayout);
        mTripsheetsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Clicked on Tripsheets", Toast.LENGTH_SHORT).show();
            }
        });
        mCustomersLayout = (LinearLayout) findViewById(R.id.CustomersLayout);
        mCustomersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Clicked on Customers", Toast.LENGTH_SHORT).show();
            }
        });
        mProductsLayout = (LinearLayout) findViewById(R.id.ProductsLayout);
        mProductsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Clicked on Products", Toast.LENGTH_SHORT).show();
                Intent i =new Intent(DashboardActivity.this,Products_Activity.class);
                startActivity(i);
                finish();
            }
        });
        mTDCLayout = (LinearLayout) findViewById(R.id.TDCLayout);
        mTDCLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Clicked on TDC", Toast.LENGTH_SHORT).show();
            }
        });

       // userActSetupStatus = mDBHelper.getUserActivityDetailsByUserId(mPreferences.getString("userId"));
        userActSetupStatus = mPreferences.getString("isSetup");


         if(mPreferences.getString("isDashboard").equals("visible")){
            mDashBoardLayout.setVisibility(View.VISIBLE);
         }else {
             mDashBoardLayout.setVisibility(View.GONE);
         }

        if(mPreferences.getString("isTripsheets").equals("visible")){
            mTripsheetsLayout.setVisibility(View.VISIBLE);
        }else {
            mTripsheetsLayout.setVisibility(View.GONE);
        }

        if(mPreferences.getString("isCustomers").equals("visible")){
            mCustomersLayout.setVisibility(View.VISIBLE);
        }else {
            mCustomersLayout.setVisibility(View.GONE);
        }

        if(mPreferences.getString("isProducts").equals("visible")){
            mProductsLayout.setVisibility(View.VISIBLE);
        }else {
            mProductsLayout.setVisibility(View.GONE);
        }

        if(mPreferences.getString("isTdc").equals("visible")){
            mTDCLayout.setVisibility(View.VISIBLE);
        }else {
            mTDCLayout.setVisibility(View.GONE);
        }

        if (mDBHelper.getRouteId().length()==0) {
            startService(new Intent(DashboardActivity.this, SyncRoutesMasterDetailsService.class));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        MenuItem settings = menu.findItem(R.id.settings);
        if (userActSetupStatus.equals("visible")){
            settings.setVisible(true);
        }else {
            settings.setVisible(false);
        }
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
        if (id == R.id.settings) {

            loadSettings();
            Toast.makeText(this, "Clicked on Settings...", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadNotifications() {
        Intent navigationIntent = new Intent(DashboardActivity.this, NotificationsActivity.class);
        // mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // if you keep these flags white screen is coming on Intent navigation
        startActivity(navigationIntent);
        finish();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        menu.findItem(R.id.notifications).setVisible(true);
        menu.findItem(R.id.settings).setVisible(true);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);


        return super.onPrepareOptionsMenu(menu);
    }


    private void loadSettings() {
        Intent settingsIntent = new Intent(DashboardActivity.this, SettingsActivity.class);
        // mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // if you keep these flags white screen is coming on Intent navigation
        startActivity(settingsIntent);
        finish();
    }
}
