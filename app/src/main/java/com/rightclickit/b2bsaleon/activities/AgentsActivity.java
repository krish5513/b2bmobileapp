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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.AgentsAdapter;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.AgentsModel;
import com.rightclickit.b2bsaleon.services.SyncNotificationsListService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sekhar Kuppa
 */
public class AgentsActivity extends AppCompatActivity {
    private Context applicationContext, activityContext;
    private AgentsModel agentsModel;
    private DBHelper mDBHelper;
    private MMSharedPreferences mPreferences;
    private LinearLayout mDashBoardLayout;
    private LinearLayout mTripsheetsLayout;
    private LinearLayout mCustomersLayout;
    private LinearLayout mProductsLayout;
    private LinearLayout mTDCLayout;
    private LinearLayout mRetailersLayout;
    FloatingActionButton fab;
    private ListView mAgentsList;
    private AgentsAdapter mAgentsAdapter;
    private SearchView search;

    private TextView mNoDataText;

    private String mNotifications = "", mTdcHomeScreen = "", mTripsHomeScreen = " ",
            mAgentsHomeScreen = "", mRetailersHomeScreen = "", mDashboardHomeScreen = "",mUserId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agents);

        this.getSupportActionBar().setTitle("AGENTS");
        this.getSupportActionBar().setSubtitle(null);
        this.getSupportActionBar().setLogo(R.drawable.customers_white_24);
        // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.customer60));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Clicked Customers Add",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AgentsActivity.this, Agents_AddActivity.class);
                startActivity(i);
                finish();
            }
        });


        applicationContext = getApplicationContext();
        activityContext = AgentsActivity.this;
        mDBHelper = new DBHelper(AgentsActivity.this);
        mPreferences = new MMSharedPreferences(AgentsActivity.this);
        agentsModel = new AgentsModel(activityContext, this);

        mNoDataText = (TextView) findViewById(R.id.NoDataText);

        mAgentsList = (ListView) findViewById(R.id.AgentsList);
        mAgentsList.setVisibility(View.GONE);
        HashMap<String, String> userMapData = mDBHelper.getUsersData();
        mUserId = userMapData.get("user_id");
        if (new NetworkConnectionDetector(AgentsActivity.this).isNetworkConnected()) {
            if (mDBHelper.getAgentsTableCount() > 0) {
                ArrayList<AgentsBean> agentsBeanArrayList = mDBHelper.fetchAllRecordsFromAgentsTable(mUserId);
                System.out.println("F:::: "+ agentsBeanArrayList.size());
                if (agentsBeanArrayList.size() > 0) {
                    mNoDataText.setText("");
                    loadAgentsList(agentsBeanArrayList);
                } else {
                    agentsModel.getAgentsList("agents");
                }

            } else {
                agentsModel.getAgentsList("agents");
            }
        } else {
            // System.out.println("ELSE::: ");
            ArrayList<AgentsBean> agentsBeanArrayList = mDBHelper.fetchAllRecordsFromAgentsTable(mUserId);
            System.out.println("F12333222222222 :::: "+ agentsBeanArrayList.size());
            if (agentsBeanArrayList.size() > 0) {
                mNoDataText.setText("");
                loadAgentsList(agentsBeanArrayList);
            } else {
                mNoDataText.setText("No Agents found.");
            }

        }

        mDashBoardLayout = (LinearLayout) findViewById(R.id.DashboardLayout);
        mDashBoardLayout.setVisibility(View.GONE);
        mDashBoardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(AgentsActivity.this, "Clicked on Dashboard", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mDashBoardLayout.startAnimation(animation1);
                Intent i = new Intent(AgentsActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });
        mTripsheetsLayout = (LinearLayout) findViewById(R.id.TripSheetsLayout);
        mTripsheetsLayout.setVisibility(View.GONE);
        mTripsheetsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(AgentsActivity.this, "Clicked on Tripsheets", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mTripsheetsLayout.startAnimation(animation1);
                Intent i = new Intent(AgentsActivity.this, TripSheetsActivity.class);
                startActivity(i);
                finish();
            }
        });
        mCustomersLayout = (LinearLayout) findViewById(R.id.CustomersLayout);
        mCustomersLayout.setVisibility(View.GONE);
        mCustomersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(AgentsActivity.this, "Clicked on Customers", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mCustomersLayout.startAnimation(animation1);
                Intent i = new Intent(AgentsActivity.this, AgentsActivity.class);
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
                Intent i = new Intent(AgentsActivity.this, RetailersActivity.class);
                startActivity(i);
                finish();
            }
        });
        mProductsLayout = (LinearLayout) findViewById(R.id.ProductsLayout);
        mProductsLayout.setVisibility(View.GONE);
        mProductsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(AgentsActivity.this, "Clicked on Products", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mProductsLayout.startAnimation(animation1);
                Intent i = new Intent(AgentsActivity.this, Products_Activity.class);
                startActivity(i);
                finish();
            }
        });
        mTDCLayout = (LinearLayout) findViewById(R.id.TDCLayout);
        mTDCLayout.setVisibility(View.GONE);
        mTDCLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(AgentsActivity.this, "Clicked on TDC", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mTDCLayout.startAnimation(animation1);
                Intent i = new Intent(AgentsActivity.this, TDCSalesActivity.class);
                startActivity(i);
                finish();
            }
        });

        ArrayList<String> privilegesData = mDBHelper.getUserActivityDetailsByUserId(userMapData.get("user_id"));
        for (int k = 0; k < privilegesData.size(); k++) {
            if (privilegesData.get(k).toString().equals("Dashboard")) {
                mDashBoardLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("TripSheets")) {
                mTripsheetsLayout.setVisibility(View.VISIBLE);
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

        ArrayList<String> privilegeActionsData1 = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("Customers"));
        for (int z = 0; z < privilegeActionsData1.size(); z++) {
            if (privilegeActionsData1.get(z).toString().equals("List_View")) {
                mAgentsList.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData1.get(z).toString().equals("Add")) {
                fab.setVisibility(View.VISIBLE);
            }
        }


        ArrayList<String> privilegeActionsData = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("UserActivity"));
        for (int z = 0; z < privilegeActionsData.size(); z++) {
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

        startService(new Intent(AgentsActivity.this, SyncNotificationsListService.class));

    }

    public void loadAgentsList(ArrayList<AgentsBean> mAgentsBeansList) {
        if (mAgentsAdapter != null) {
            mAgentsAdapter = null;
        }
        mAgentsAdapter = new AgentsAdapter(this, AgentsActivity.this, mAgentsBeansList);
        mAgentsList.setAdapter(mAgentsAdapter);
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

                mAgentsAdapter.filter(query);

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


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_search) {
//
//            return true;
//        }
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
        Intent navigationIntent = new Intent(AgentsActivity.this, NotificationsActivity.class);
        // mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // if you keep these flags white screen is coming on Intent navigation
        startActivity(navigationIntent);
        finish();
    }

    private void loadSettings() {
        Intent settingsIntent = new Intent(AgentsActivity.this, SettingsActivity.class);
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
            startActivity(intent);
            finish();
        } else if (mTripsHomeScreen.equals("Trips@Home")) {
            intent = new Intent(this, TripSheetsActivity.class);
            startActivity(intent);
            finish();
        } else if (mAgentsHomeScreen.equals("Agents@Home")) {
            //intent = new Intent(this, AgentsActivity.class);
            finish();
        } else if (mRetailersHomeScreen.equals("Retailers@Home")) {
            intent = new Intent(this, RetailersActivity.class);
            startActivity(intent);
            finish();
        } else if (mDashboardHomeScreen.equals("Dashboard@Home")) {
            intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            intent = new Intent(this, DashboardActivity.class);
        }
    }
}
