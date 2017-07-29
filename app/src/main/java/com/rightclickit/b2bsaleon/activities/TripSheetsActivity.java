package com.rightclickit.b2bsaleon.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TripsheetsListAdapter;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsList;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.TripsheetsModel;
import com.rightclickit.b2bsaleon.services.SyncNotificationsListService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import java.util.ArrayList;
import java.util.HashMap;

public class TripSheetsActivity extends AppCompatActivity {
    private LinearLayout tsDashBoardLayout;
    private LinearLayout tsTripsheetsLayout;
    private LinearLayout tsCustomersLayout;
    private LinearLayout tsProductsLayout;
    private LinearLayout tsTDCLayout;
    private LinearLayout mRetailersLayout;

    private TextView mNoTripsFoundText;

    private ListView mTripsListview;
    private DBHelper mDBHelper;
    private MMSharedPreferences mPreferences;
    private TripsheetsListAdapter mTripsListAdapter;
    private TripsheetsModel mTripsheetsModel;

    private String mTripSheetViewPrivilege = "", mTripSheetStockPrivilege = "";
    private String mNotifications = "", mTdcHomeScreen = "", mTripsHomeScreen = " ", mAgentsHomeScreen = "", mRetailersHomeScreen = "", mDashboardHomeScreen = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_sheets);


        this.getSupportActionBar().setTitle("TRIPSHEET");
        this.getSupportActionBar().setSubtitle(null);
        this.getSupportActionBar().setLogo(R.drawable.route_white);
        // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);


        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        mTripsheetsModel = new TripsheetsModel(this, TripSheetsActivity.this);

        mNoTripsFoundText = (TextView) findViewById(R.id.NoTripsFoundTextView);

        mTripsListview = (ListView) findViewById(R.id.TripsheetsListview);
        mTripsListview.setVisibility(View.GONE);


        mDBHelper = new DBHelper(TripSheetsActivity.this);
        mPreferences = new MMSharedPreferences(TripSheetsActivity.this);

        tsDashBoardLayout = (LinearLayout) findViewById(R.id.DashboardLayout);
        tsDashBoardLayout.setVisibility(View.GONE);
        tsDashBoardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(TripsheetsActivity.this, "Clicked on Dashboard", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                tsDashBoardLayout.startAnimation(animation1);
                Intent i = new Intent(TripSheetsActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });
        tsTripsheetsLayout = (LinearLayout) findViewById(R.id.TripSheetsLayout);
        tsTripsheetsLayout.setVisibility(View.GONE);
        tsTripsheetsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(TripSheetsActivity.this, "Clicked on Tripsheets", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                tsTripsheetsLayout.startAnimation(animation1);

            }
        });
        tsCustomersLayout = (LinearLayout) findViewById(R.id.CustomersLayout);
        tsCustomersLayout.setVisibility(View.GONE);
        tsCustomersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(Tripsheet_Activity.this, "Clicked on Customers", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                tsCustomersLayout.startAnimation(animation1);
                Intent i = new Intent(TripSheetsActivity.this, AgentsActivity.class);
                startActivity(i);
                finish();
            }
        });
        mRetailersLayout = (LinearLayout) findViewById(R.id.RetailersLayout);
        mRetailersLayout.setVisibility(View.GONE);
        mRetailersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mRetailersLayout.startAnimation(animation1);
                Intent i = new Intent(TripSheetsActivity.this, RetailersActivity.class);
                startActivity(i);
                finish();
            }
        });
        tsProductsLayout = (LinearLayout) findViewById(R.id.ProductsLayout);
        tsProductsLayout.setVisibility(View.GONE);
        tsProductsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Toast.makeText(Tripsheet_Activity.this, "Clicked on Products", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                tsProductsLayout.startAnimation(animation1);
                Intent i = new Intent(TripSheetsActivity.this, Products_Activity.class);
                startActivity(i);
                finish();
            }
        });
        tsTDCLayout = (LinearLayout) findViewById(R.id.TDCLayout);
        tsTDCLayout.setVisibility(View.GONE);

        tsTDCLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Toast.makeText(Tripsheet_Activity.this, "Clicked on TDC", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                tsTDCLayout.startAnimation(animation1);
                Intent i = new Intent(TripSheetsActivity.this, TDCSalesActivity.class);
                startActivity(i);
                finish();
            }
        });
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
//                        R.anim.blink);
//                tsTDCLayout.startAnimation(animation1);
//                Intent i =new Intent(TripSheetsActivity.this,TripSheetView.class);
//                startActivity(i);
//                finish();
//
//           }
//           });
//        stock.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
//                        R.anim.blink);
//                tsTDCLayout.startAnimation(animation1);
//                Intent i =new Intent(TripSheetsActivity.this,TripSheetStock.class);
//                startActivity(i);
//                finish();
//
//            }
//        });
        HashMap<String, String> userMapData = mDBHelper.getUsersData();
        ArrayList<String> privilegesData = mDBHelper.getUserActivityDetailsByUserId(userMapData.get("user_id"));
        for (int k = 0; k < privilegesData.size(); k++) {
            if (privilegesData.get(k).toString().equals("Dashboard")) {
                tsDashBoardLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("TripSheets")) {
                tsTripsheetsLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("Customers")) {
                tsCustomersLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("Products")) {
                tsProductsLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("TDC")) {
                tsTDCLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("Retailers")) {
                mRetailersLayout.setVisibility(View.VISIBLE);
            }
        }
        ArrayList<String> privilegeActionsData = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("TripSheets"));
        for (int z = 0; z < privilegeActionsData.size(); z++) {
            if (privilegeActionsData.get(z).toString().equals("list_view")) {
                mTripsListview.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData.get(z).toString().equals("tripsheet_summary")) {
                mTripSheetViewPrivilege = privilegeActionsData.get(z).toString();
            } else if (privilegeActionsData.get(z).toString().equals("list_stock")) {
                mTripSheetStockPrivilege = privilegeActionsData.get(z).toString();
            }
        }

        ArrayList<String> privilegeActionsData1 = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("UserActivity"));
        System.out.println("F 11111 ***COUNT === " + privilegeActionsData1.size());
        for (int z = 0; z < privilegeActionsData1.size(); z++) {
            System.out.println("Name::: " + privilegeActionsData1.get(z).toString());
            if (privilegeActionsData1.get(z).toString().equals("Notification")) {
                mNotifications = privilegeActionsData1.get(z).toString();
            } else if (privilegeActionsData1.get(z).toString().equals("tdc_home_screen")) {
                mTdcHomeScreen = privilegeActionsData1.get(z).toString();
            } else if (privilegeActionsData1.get(z).toString().equals("Trips@Home")) {
                mTripsHomeScreen = privilegeActionsData1.get(z).toString();
            } else if (privilegeActionsData1.get(z).toString().equals("Agents@Home")) {
                mAgentsHomeScreen = privilegeActionsData1.get(z).toString();
            } else if (privilegeActionsData1.get(z).toString().equals("Retailers@Home")) {
                mRetailersHomeScreen = privilegeActionsData1.get(z).toString();
            } else if (privilegeActionsData1.get(z).toString().equals("Dashboard@Home")) {
                mDashboardHomeScreen = privilegeActionsData1.get(z).toString();
            }
        }
        startService(new Intent(TripSheetsActivity.this, SyncNotificationsListService.class));

        if (new NetworkConnectionDetector(TripSheetsActivity.this).isNetworkConnected()) {
//            if (mDBHelper.getTripsheetsTableCount() > 0) {
//                ArrayList<TripsheetsList> tripsList = mDBHelper.fetchTripsheetsList();
//                if (tripsList.size() > 0) {
//                    loadTripsData(tripsList);
//                } else {
//                    mNoTripsFoundText.setText("No Trips found.");
//                }
//            } else {
//                //startService(new Intent(getApplicationContext(), SyncStakeHolderTypesService.class));
//                mTripsheetsModel.getTripsheetsList(mNoTripsFoundText);
//            }
            mTripsheetsModel.getTripsheetsList(mNoTripsFoundText);
        } else {
            // System.out.println("ELSE::: ");
            ArrayList<TripsheetsList> tripsList = mDBHelper.fetchTripsheetsList();
            if (tripsList.size() > 0) {
                loadTripsData(tripsList);
            } else {
                mNoTripsFoundText.setText("No Trips found.");
            }
        }
    }

    public void loadTripsData(ArrayList<TripsheetsList> tripsList) {
        tripsList = mDBHelper.fetchTripsheetsList();
        if (mTripsListAdapter != null) {
            mTripsListAdapter = null;
        }
        mTripsListAdapter = new TripsheetsListAdapter(TripSheetsActivity.this, TripSheetsActivity.this,
                tripsList, mTripSheetViewPrivilege, mTripSheetStockPrivilege);
        mTripsListview.setAdapter(mTripsListAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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

        if (id == R.id.action_search) {

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

    private void loadNotifications() {
        Intent navigationIntent = new Intent(TripSheetsActivity.this, NotificationsActivity.class);
        // mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // if you keep these flags white screen is coming on Intent navigation
        startActivity(navigationIntent);
        finish();
    }

    private void loadSettings() {
        Intent settingsIntent = new Intent(TripSheetsActivity.this, SettingsActivity.class);
        // mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // if you keep these flags white screen is coming on Intent navigation
        startActivity(settingsIntent);
        finish();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.action_search).setVisible(true);
        menu.findItem(R.id.settings).setVisible(true);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.notifications).setVisible(true);

        menu.findItem(R.id.autorenew).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = null;
        if (mTdcHomeScreen.equals("tdc_home_screen")) {
            intent = new Intent(this, TDCSalesActivity.class);
        } else if (mTripsHomeScreen.equals("Trips@Home")) {
            intent = new Intent(this, TripSheetsActivity.class);
        } else if (mAgentsHomeScreen.equals("Agents@Home")) {
            intent = new Intent(this, AgentsActivity.class);
        } else if (mRetailersHomeScreen.equals("Retailers@Home")) {
            intent = new Intent(this, RetailersActivity.class);

        } else if (mDashboardHomeScreen.equals("Dashboard@Home")) {
            intent = new Intent(this, DashboardActivity.class);
        } else {
            intent = new Intent(this, DashboardActivity.class);
        }
        startActivity(intent);
        finish();
    }
}





