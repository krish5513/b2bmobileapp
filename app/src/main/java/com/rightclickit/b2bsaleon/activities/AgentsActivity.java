package com.rightclickit.b2bsaleon.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.AgentsAdapter;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.beanclass.TDCCustomer;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.AgentsModel;
import com.rightclickit.b2bsaleon.services.SyncAgentsService;
import com.rightclickit.b2bsaleon.services.SyncTDCCustomersService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private Runnable mRunnable;
    private Handler mHandler = new Handler();

    private TextView mNoDataText;
    private String mStock = "";

    private String mNotifications = "", mTdcHomeScreen = "", mTripsHomeScreen = " ",
            mAgentsHomeScreen = "", mRetailersHomeScreen = "", mDashboardHomeScreen = "", mUserId = "";
    private boolean isSaveDeviceDetails, isMyProfilePrivilege;
    TextView tvrouts_customerN;
    private int uploadedCount = 0;
    private android.support.v7.app.AlertDialog alertDialog1 = null;
    private android.support.v7.app.AlertDialog.Builder alertDialogBuilder1;

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

        init();


    }

    public void init() {
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


        ArrayList<String> privilegeActionsData2 = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("Customers"));
        for (int z = 0; z < privilegeActionsData2.size(); z++) {
            if (privilegeActionsData2.get(z).toString().equals("my_profile")) {
                isMyProfilePrivilege = true;
                tvrouts_customerN = (TextView) findViewById(R.id.tvrouts_customerN);
                tvrouts_customerN.setText("Profile");

            }
        }

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
            } else if (privilegeActionsData1.get(z).toString().equals("my_profile")) {
                this.getSupportActionBar().setTitle("PROFILE");
            } else if (privilegeActionsData1.get(z).toString().equals("ViewStock")) {
                mStock = privilegeActionsData1.get(z).toString();
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
        if (new NetworkConnectionDetector(AgentsActivity.this).isNetworkConnected()) {
            if (mDBHelper.getAgentsTableCount() > 0) {
                ArrayList<AgentsBean> agentsBeanArrayList = mDBHelper.fetchAllRecordsFromAgentsTable(mUserId);
                if (agentsBeanArrayList.size() > 0) {
                    mNoDataText.setText("");
                    loadAgentsList(agentsBeanArrayList);
                } else {
                    getAgents();
                    //agentsModel.getAgentsList("agents");
                }

            } else {
                getAgents();
                //agentsModel.getAgentsList("agents");
            }
        } else {
            // System.out.println("ELSE::: ");
            ArrayList<AgentsBean> agentsBeanArrayList = mDBHelper.fetchAllRecordsFromAgentsTable(mUserId);
            if (agentsBeanArrayList.size() > 0) {
                mNoDataText.setText("");
                loadAgentsList(agentsBeanArrayList);
            } else {
                mNoDataText.setText("No Agents found.");
            }

        }

    }

    public void getAgents() {
        agentsModel.getAgentsList("agents");
    }

    public void loadAgentsList(ArrayList<AgentsBean> mAgentsBeansList1) {
        ArrayList<AgentsBean> agentsBeanArrayList;
        synchronized (this){
            agentsBeanArrayList = mDBHelper.fetchAllRecordsFromAgentsTable(mUserId);
        }

        synchronized (this) {
            if (mAgentsAdapter != null) {
                mAgentsAdapter = null;
            }
            mAgentsAdapter = new AgentsAdapter(this, AgentsActivity.this, agentsBeanArrayList, mStock);
            mAgentsList.setAdapter(mAgentsAdapter);
        }

        synchronized (this) {
            if (alertDialogBuilder1 != null) {
                synchronized (this) {
                    alertDialog1.dismiss();
                    alertDialogBuilder1 = null;
                }
                synchronized (this) {
                    showAlertDialog1(AgentsActivity.this, "Sync process", "Agents sync has been completed successfully.");
                }
            }
        }
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

            // if (new NetworkConnectionDetector(AgentsActivity.this).isNetworkConnected()) {
            //  startService(new Intent(AgentsActivity.this, SyncNotificationsListService.class));
            loadNotifications();
            // }else {
            //     new NetworkConnectionDetector(AgentsActivity.this).displayNoNetworkError(AgentsActivity.this);
            // }
            return true;

        }
        if (id == R.id.settings) {

            loadSettings();

            return true;
        }
        if (id == R.id.autorenew) {

//            if (new NetworkConnectionDetector(AgentsActivity.this).isNetworkConnected()) {
//                getAgents();
//                //agentsModel.getAgentsList("agents");
//            }else {
//                new NetworkConnectionDetector(AgentsActivity.this).displayNoNetworkError(AgentsActivity.this);
//            }

            if (id == R.id.autorenew) {
                if (new NetworkConnectionDetector(AgentsActivity.this).isNetworkConnected()) {
                    showAlertDialog(AgentsActivity.this, "Sync process", "Are you sure, you want start the sync process?");
                } else {
                    new NetworkConnectionDetector(AgentsActivity.this).displayNoNetworkError(AgentsActivity.this);
                }
                return true;
            }
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

    private void showAlertDialog(Context context, String title, String message) {
        try {
            android.support.v7.app.AlertDialog alertDialog = null;
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    showCustomValidationAlertForSync(AgentsActivity.this, "upload");
                }
            });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to display alert without field.
     *
     * @param context
     * @param message
     */
    private void showCustomValidationAlertForSync(Activity context, String message) {
        // custom dialog
        try {

            alertDialogBuilder1 = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder1.setTitle("Sync Process");
            alertDialogBuilder1.setCancelable(false);
            if (message.equals("down")) {
                alertDialogBuilder1.setMessage("Downloading agents... Please wait.. ");
                getAgents();
            } else {
                ArrayList<AgentsBean> unUploadedTDCCustomers = mDBHelper.fetchAllUnUploadedRecordsFromAgents();
                uploadedCount = unUploadedTDCCustomers.size();
                if (uploadedCount > 0) {
                    alertDialogBuilder1.setMessage("Uploading agents... Please wait.. ");
                    fetchCountFromDB(uploadedCount);
                    if (new NetworkConnectionDetector(activityContext).isNetworkConnected()) {
                        Intent syncTDCCustomersServiceIntent = new Intent(activityContext, SyncAgentsService.class);
                        startService(syncTDCCustomersServiceIntent);
                    }
                } else {
                    alertDialogBuilder1.setMessage("Downloading agents... Please wait.. ");
                    getAgents();
                }
            }

            alertDialog1 = alertDialogBuilder1.create();
            alertDialog1.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchCountFromDB(final int uploadedCount11) {
        if (uploadedCount11 > 0) {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    ArrayList<AgentsBean> unUploadedTDCCustomers = mDBHelper.fetchAllUnUploadedRecordsFromAgents();
                    fetchCountFromDB(unUploadedTDCCustomers.size());
                }
            };
            mHandler.postDelayed(mRunnable, 1000);
        } else {
            uploadedCount = 0;
            mHandler.removeCallbacks(mRunnable);
            synchronized (this) {
                if (alertDialogBuilder1 != null) {
                    alertDialog1.dismiss();
                    alertDialogBuilder1 = null;
                }
            }
            synchronized (this) {
                showCustomValidationAlertForSync(AgentsActivity.this, "down");
            }
        }
    }

    private void showAlertDialog1(Context context, String title, String message) {
        try {
            android.support.v7.app.AlertDialog alertDialog = null;
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
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
        menu.findItem(R.id.sort).setVisible(false);
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
