package com.rightclickit.b2bsaleon.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.RetailersListAdapter;
import com.rightclickit.b2bsaleon.beanclass.TDCCustomer;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.services.SyncNotificationsListService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RetailersActivity extends AppCompatActivity {
    private Context applicationContext, activityContext;
    private MMSharedPreferences mPreferences;

    private SearchView search;
    private LinearLayout mDashBoardLayout, mTripsheetsLayout, mCustomersLayout, mProductsLayout, mTDCLayout, mRetailersLayout;
    private ListView mRetailerslistview;
    private TextView no_retailers_found_message;
    private FloatingActionButton fab;
    private Button info, payments;

    private DBHelper mDBHelper;
    private RetailersListAdapter retailersListAdapter;
    private List<TDCCustomer> retailersList;
    private String mNotifications = "", mTdcHomeScreen = "", mTripsHomeScreen = " ", mAgentsHomeScreen = "", mRetailersHomeScreen = "", mDashboardHomeScreen = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailers);

        try {
            applicationContext = getApplicationContext();
            activityContext = RetailersActivity.this;

            this.getSupportActionBar().setTitle("RETAILERS");
            this.getSupportActionBar().setSubtitle(null);
            this.getSupportActionBar().setLogo(R.drawable.ic_store_white);
            // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            this.getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            this.getSupportActionBar().setDisplayShowHomeEnabled(true);

            mPreferences = new MMSharedPreferences(RetailersActivity.this);
            mDBHelper = new DBHelper(RetailersActivity.this);

            final ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

            fab = (FloatingActionButton) findViewById(R.id.retailerfab);
            fab.setVisibility(View.GONE);
            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.plus_white));

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(RetailersActivity.this, Retailers_AddActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            mRetailerslistview = (ListView) findViewById(R.id.retailers_list_view);
            mRetailerslistview.setVisibility(View.GONE);

            no_retailers_found_message = (TextView) findViewById(R.id.no_retailers_found);

            mDashBoardLayout = (LinearLayout) findViewById(R.id.DashboardLayout);
            mDashBoardLayout.setVisibility(View.GONE);
            mDashBoardLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                    mDashBoardLayout.startAnimation(animation1);
                    Intent i = new Intent(RetailersActivity.this, DashboardActivity.class);
                    startActivity(i);
                    finish();

                }
            });

            mTripsheetsLayout = (LinearLayout) findViewById(R.id.TripSheetsLayout);
            mTripsheetsLayout.setVisibility(View.GONE);
            mTripsheetsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                    mTripsheetsLayout.startAnimation(animation1);
                    Intent i = new Intent(RetailersActivity.this, TripSheetsActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            mCustomersLayout = (LinearLayout) findViewById(R.id.CustomersLayout);
            mCustomersLayout.setVisibility(View.GONE);
            mCustomersLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                    mCustomersLayout.startAnimation(animation1);
                    Intent i = new Intent(RetailersActivity.this, AgentsActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            mRetailersLayout = (LinearLayout) findViewById(R.id.Retailers);
            mRetailersLayout.setVisibility(View.GONE);
            mRetailersLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                    mRetailersLayout.startAnimation(animation1);

                }
            });

            mProductsLayout = (LinearLayout) findViewById(R.id.ProductsLayout);
            mProductsLayout.setVisibility(View.GONE);
            mProductsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                    mProductsLayout.startAnimation(animation1);
                    Intent i = new Intent(RetailersActivity.this, Products_Activity.class);
                    startActivity(i);
                    finish();
                }
            });

            mTDCLayout = (LinearLayout) findViewById(R.id.TDCLayout);
            mTDCLayout.setVisibility(View.GONE);
            mTDCLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                    mTDCLayout.startAnimation(animation1);
                    Intent i = new Intent(RetailersActivity.this, TDCSalesActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            HashMap<String, String> userMapData = mDBHelper.getUsersData();
            ArrayList<String> privilegesData = mDBHelper.getUserActivityDetailsByUserId(userMapData.get("user_id"));
            System.out.println("F 11111 ***COUNT === " + privilegesData.size());
            if (privilegesData.contains("Dashboard")) {
                mDashBoardLayout.setVisibility(View.VISIBLE);
            }
            if (privilegesData.contains("TripSheets")) {
                mTripsheetsLayout.setVisibility(View.VISIBLE);
            }
            if (privilegesData.contains("Customers")) {
                mCustomersLayout.setVisibility(View.VISIBLE);
            }
            if (privilegesData.contains("Products")) {
                mProductsLayout.setVisibility(View.VISIBLE);
            }
            if (privilegesData.contains("TDC")) {
                mTDCLayout.setVisibility(View.VISIBLE);
            }
            if (privilegesData.contains("Retailers")) {
                mRetailersLayout.setVisibility(View.VISIBLE);
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


            ArrayList<String> privilegeActionsData1 = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("Retailers"));
            System.out.println("F 11111 ***COUNT === " + privilegeActionsData1.size());

            boolean canWeShowRetailersListView = false;

            if (privilegeActionsData1.contains("List_View")) {
                mRetailerslistview.setVisibility(View.VISIBLE);
                canWeShowRetailersListView = true;
            }

            if (privilegeActionsData1.contains("Add")) {
                fab.setVisibility(View.VISIBLE);
            }

            if (canWeShowRetailersListView) {
                retailersList = mDBHelper.fetchRecordsFromTDCCustomers(1);

                if (retailersList.size() <= 0) {
                    mRetailerslistview.setVisibility(View.GONE);
                    no_retailers_found_message.setVisibility(View.VISIBLE);
                }

                retailersListAdapter = new RetailersListAdapter(activityContext, this, retailersList, privilegeActionsData1);
                mRetailerslistview.setAdapter(retailersListAdapter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        startService(new Intent(RetailersActivity.this, SyncNotificationsListService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);

        try {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            search = (SearchView) menu.findItem(R.id.action_search).getActionView();
            search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    retailersListAdapter.filter(query);
                    return true;
                }
            });

            // Get the search close button image view
            ImageView closeButton = (ImageView) search.findViewById(R.id.search_close_btn);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    search.setQuery("", false);
                    search.clearFocus();
                    search.onActionViewCollapsed();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

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
                if (search.isIconified()) {
                    onBackPressed();
                } else {
                    search.setQuery("", false);
                    search.clearFocus();
                    search.onActionViewCollapsed();
                }
                return true;
            default:
                return true;
        }
    }

    private void loadNotifications() {
        Intent navigationIntent = new Intent(RetailersActivity.this, NotificationsActivity.class);
        // mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // if you keep these flags white screen is coming on Intent navigation
        startActivity(navigationIntent);
        finish();
    }

    private void loadSettings() {
        Intent settingsIntent = new Intent(RetailersActivity.this, SettingsActivity.class);
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