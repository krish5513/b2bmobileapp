package com.rightclickit.b2bsaleon.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.NotificationAdapter;
import com.rightclickit.b2bsaleon.beanclass.NotificationBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.services.SyncNotificationsListService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationsActivity extends AppCompatActivity {

    ListView listView;
    private SearchView search;

    private TextView mNotificationsFoundText;
    private DBHelper mDBHelper;
    private MMSharedPreferences mPreferences;

    private String mNotifications = "", mTdcHomeScreen = "", mTripsHomeScreen = " ", mAgentsHomeScreen = "", mRetailersHomeScreen = "", mDashboardHomeScreen = "";
    private LinearLayout mDashboardLayout, mTripSheetsLayout, mCustomersLayout, mProductsLayout, mTDCLayout;
    private LinearLayout mRetailersLayout;
    private ArrayList notificationList=new ArrayList();

    private NotificationAdapter mNotificationAdapter;
    private boolean isSaveDeviceDetails,isMyProfilePrivilege;
    TextView tvrouts_customerN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);


        this.getSupportActionBar().setTitle("NOTIFICATIONS");
        this.getSupportActionBar().setSubtitle(null);


        this.getSupportActionBar().setLogo(R.drawable.ic_notifications_black_24dp);

        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);


        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);


        mDBHelper = new DBHelper(NotificationsActivity.this);
        mPreferences = new MMSharedPreferences(NotificationsActivity.this);


        mNotificationsFoundText = (TextView) findViewById(R.id.NoNotificationsFoundTextView);
        listView = (ListView) findViewById(R.id.notifications_listview);



        mDashboardLayout = (LinearLayout) findViewById(R.id.DashboardLayout);
        mDashboardLayout.setVisibility(View.GONE);
        mDashboardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mDashboardLayout.startAnimation(animation1);
                Intent i = new Intent(NotificationsActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });
        mTripSheetsLayout = (LinearLayout) findViewById(R.id.TripSheetsLayout);
        mTripSheetsLayout.setVisibility(View.GONE);
        mTripSheetsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mTripSheetsLayout.startAnimation(animation1);
                Intent i = new Intent(NotificationsActivity.this, TripSheetsActivity.class);
                startActivity(i);
                finish();
            }
        });

        mCustomersLayout = (LinearLayout) findViewById(R.id.CustomersLayout);
        mCustomersLayout.setVisibility(View.GONE);
        mCustomersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mCustomersLayout.startAnimation(animation1);
                Intent i = new Intent(NotificationsActivity.this, AgentsActivity.class);
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
                Intent i = new Intent(NotificationsActivity.this, RetailersActivity.class);
                startActivity(i);
                finish();
            }
        });
        mProductsLayout = (LinearLayout) findViewById(R.id.ProductsLayout);
        mProductsLayout.setVisibility(View.GONE);
        mProductsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(applicationContext, "Clicked on Products", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mProductsLayout.startAnimation(animation1);
                Intent i = new Intent(NotificationsActivity.this, Products_Activity.class);
                startActivity(i);
                finish();
            }
        });

        mTDCLayout = (LinearLayout) findViewById(R.id.TDCLayout);
        mTDCLayout.setVisibility(View.GONE);
        mTDCLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mTDCLayout.startAnimation(animation1);
                Intent i = new Intent(NotificationsActivity.this, TDCSalesActivity.class);
                startActivity(i);
                finish();
            }
        });

        //startService(new Intent(NotificationsActivity.this, SyncNotificationsListService.class));

        HashMap<String, String> userMapData = mDBHelper.getUsersData();
        ArrayList<String> privilegesData = mDBHelper.getUserActivityDetailsByUserId(userMapData.get("user_id"));
        System.out.println("F 11111 ***COUNT === " + privilegesData.size());
        for (int k = 0; k < privilegesData.size(); k++) {
            System.out.println("F 11111 ***COUNT 4444 === " + privilegesData.get(k).toString());
            if (privilegesData.get(k).toString().equals("Dashboard")) {
                mDashboardLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("TripSheets")) {
                mTripSheetsLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("Customers")) {
                mCustomersLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("Products")) {
                mProductsLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("TDC")) {
                mTDCLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("Retailers")) {
                mRetailersLayout.setVisibility(View.VISIBLE);
            }
        }

        ArrayList<String> privilegeActionsData = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("UserActivity"));
        System.out.println("F 11111 ***COUNT === " + privilegeActionsData.size());
        for (int z = 0; z < privilegeActionsData.size(); z++) {
            System.out.println("Name::: " + privilegeActionsData.get(z).toString());
            if (privilegeActionsData.get(z).toString().equals("Notification")) {
                mNotifications = privilegeActionsData.get(z).toString();
            } else if (privilegeActionsData.get(z).toString().equals("tdc_home_screen")) {
                mTdcHomeScreen = privilegeActionsData.get(z).toString();
            } else if (privilegeActionsData.get(z).toString().equals("Trips@Home")) {
                mTripsHomeScreen = privilegeActionsData.get(z).toString();
            } else if (privilegeActionsData.get(z).toString().equals("Agents@Home")) {
                mAgentsHomeScreen = privilegeActionsData.get(z).toString();
            } else if (privilegeActionsData.get(z).toString().equals("Retailers@Home")) {
                mRetailersHomeScreen = privilegeActionsData.get(z).toString();
            } else if (privilegeActionsData.get(z).toString().equals("Dashboard@Home")) {
                mDashboardHomeScreen = privilegeActionsData.get(z).toString();
            }
        }


        ArrayList<String> privilegeActionsData1 = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("Customers"));
        for (int z = 0; z < privilegeActionsData1.size(); z++) {
            if (privilegeActionsData1.get(z).toString().equals("my_profile")) {
                isMyProfilePrivilege = true;
                tvrouts_customerN=(TextView)findViewById(R.id.tvrouts_customerN);
                tvrouts_customerN.setText("Profile");

            }
        }

        if (new NetworkConnectionDetector(NotificationsActivity.this).isNetworkConnected()) {
            if (mDBHelper.getNotificationsTableCount()>0){
                ArrayList<NotificationBean> notificationsList = mDBHelper.fetchAllNotificationsList();
                Log.e("received", notificationsList.size() + "");
                if (notificationsList.size() > 0) {
                    loadNotificationsData(notificationsList);
                } else {
                    mNotificationsFoundText.setText("No Notifications found.");
                }
            }else {
                startService(new Intent(NotificationsActivity.this, SyncNotificationsListService.class));
            }
        }else {
            System.out.println("ELSE::: ");
            ArrayList<NotificationBean> notificationsList = mDBHelper.fetchAllNotificationsList();
            Log.e("received", notificationsList.size() + "");
            if (notificationsList.size() > 0) {
                loadNotificationsData(notificationsList);
            } else {
                mNotificationsFoundText.setText("No Notifications found.");
            }
        }




    }




    public void loadNotificationsData(ArrayList<NotificationBean> notificationsList) {
        if (mNotificationAdapter != null) {
            mNotificationAdapter = null;
        }
        mNotificationAdapter = new NotificationAdapter(NotificationsActivity.this, NotificationsActivity.this,
                notificationsList );
        listView.setAdapter(mNotificationAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        search = (SearchView) menu.findItem(R.id.action_search).getActionView();

        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
            try{

               mNotificationAdapter.filter(query);
              } catch (Exception e){

             }


                return true;

            }

        });

        // Get the search close button image view
        ImageView closeButton = (ImageView)search.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setQuery("", false);
                search.clearFocus();
                search.onActionViewCollapsed();
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.autorenew) {

            if (new NetworkConnectionDetector(NotificationsActivity.this).isNetworkConnected()) {
                startService(new Intent(NotificationsActivity.this, SyncNotificationsListService.class));
            }else {
                new NetworkConnectionDetector(NotificationsActivity.this).displayNoNetworkError(NotificationsActivity.this);
            }
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                if(search.isIconified()) {
                    onBackPressed();
                }else {
                    search.setQuery("", false);
                    search.clearFocus();
                    search.onActionViewCollapsed();
                }
                return true;
            default:
                return true;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        menu.findItem(R.id.notifications).setVisible(false);
        menu.findItem(R.id.settings).setVisible(false);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(true);
        menu.findItem(R.id.Add).setVisible(false);

        menu.findItem(R.id.autorenew).setVisible(true);
        menu.findItem(R.id.sort).setVisible(false);
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


