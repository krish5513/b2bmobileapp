package com.rightclickit.b2bsaleon.activities;

import android.app.Activity;
import android.app.Dialog;
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
import android.util.Log;
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

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.RetailersListAdapter;
import com.rightclickit.b2bsaleon.beanclass.TDCCustomer;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.RetailersModel1;
import com.rightclickit.b2bsaleon.services.SyncTDCCustomersService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

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
    private boolean isSaveDeviceDetails, isMyProfilePrivilege, isDataDisplayed;
    TextView tvrouts_customerN;
    private RetailersModel1 mRetailersModel;
    ArrayList<String> privilegeActionsData1;
    public static TextView mSyncText;
    private String upload = "", userId = "";
    private int uploadedCount = 0, uploadedCount1 = 0;
    public static RetailersActivity context;
    private Runnable mRunnable;
    private Handler mHandler = new Handler();
    private Dialog dialog;
    private android.support.v7.app.AlertDialog alertDialog1 = null;
    private android.support.v7.app.AlertDialog.Builder alertDialogBuilder1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = RetailersActivity.this;
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
            mRetailersModel = new RetailersModel1(this, RetailersActivity.this);

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
            userId = userMapData.get("user_id");
            Log.i("Reatailer activity@@@",userId+"");
            ArrayList<String> privilegesData = mDBHelper.getUserActivityDetailsByUserId(userMapData.get("user_id"));
            //System.out.println("F 11111 ***COUNT === " + privilegesData.size());
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
            //System.out.println("F 11111 ***COUNT === " + privilegeActionsData.size());
            for (int z = 0; z < privilegeActionsData.size(); z++) {
                //System.out.println("Name::: " + privilegeActionsData.get(z).toString());
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


            ArrayList<String> privilegeActionsData2 = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("Customers"));
            for (int z = 0; z < privilegeActionsData2.size(); z++) {
                if (privilegeActionsData2.get(z).toString().equals("my_profile")) {
                    isMyProfilePrivilege = true;
                    tvrouts_customerN = (TextView) findViewById(R.id.tvrouts_customerN);
                    tvrouts_customerN.setText("Profile");

                }
            }


            privilegeActionsData1 = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("Retailers"));
            System.out.println(" retailer activity***COUNT === " + privilegeActionsData1.size());

            boolean canWeShowRetailersListView = false;

            if (privilegeActionsData1.contains("List_View")) {
                mRetailerslistview.setVisibility(View.VISIBLE);
                canWeShowRetailersListView = true;
            }

            if (privilegeActionsData1.contains("Add")) {
                fab.setVisibility(View.VISIBLE);
            }

            if (canWeShowRetailersListView) {
                if (new NetworkConnectionDetector(RetailersActivity.this).isNetworkConnected()) {
                    if (retailersList != null) {
                        if (retailersList.size() > 0) {
                            retailersList.clear();
                        }
                    }
                    retailersList = mDBHelper.fetchRecordsFromTDCCustomers(1, userMapData.get("user_id"));
                    if (retailersList.size() > 0) {
                        loadRetailers(retailersList, "");
                    } else {
                        mRetailersModel.getAgentsList("retailers");
                    }
                } else {
                    if (retailersList != null) {
                        if (retailersList.size() > 0) {
                            retailersList.clear();
                        }
                    }
                    retailersList = mDBHelper.fetchRecordsFromTDCCustomers(1, userMapData.get("user_id"));
                    loadRetailers(retailersList, "");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //startService(new Intent(RetailersActivity.this, SyncNotificationsListService.class));
    }

    public void loadRetailers(List<TDCCustomer> retailersList, String s) {
        synchronized (this) {
            List<TDCCustomer> list = null;
            list = mDBHelper.fetchRecordsFromTDCCustomers(1, userId);
            //System.out.println("RETAILERS::: " + list.size());
            if (list.size() <= 0) {
                mRetailerslistview.setVisibility(View.GONE);
                no_retailers_found_message.setVisibility(View.VISIBLE);
            } else {
//                if (s.equals("")) {
//                    retailersListAdapter = new RetailersListAdapter(activityContext, this, list, privilegeActionsData1);
//                    mRetailerslistview.setAdapter(retailersListAdapter);
//                } else {
//                    retailersListAdapter = new RetailersListAdapter(activityContext, this, list, privilegeActionsData1);
//                    mRetailerslistview.setAdapter(retailersListAdapter);
//                }
                retailersListAdapter = new RetailersListAdapter(activityContext, this, list, privilegeActionsData1);
                mRetailerslistview.setAdapter(retailersListAdapter);
            }
        }
        synchronized (this) {
            isDataDisplayed = true;
            if (alertDialog1 != null) {
                synchronized (this) {
                    alertDialog1.dismiss();
                }
                synchronized (this) {
                    showAlertDialog1(RetailersActivity.this, "Sync Process", "Data uploaded/downloaded successfully.");
                }
            }
        }
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

            return true;
        }
        if (id == R.id.settings) {

            loadSettings();

            return true;
        }
//        if (id == R.id.autorenew) {
//
//            if (new NetworkConnectionDetector(RetailersActivity.this).isNetworkConnected()) {
//                // First we need to call the special price service and then call the sync for retailers
//                synchronized (this) {
//
//                }
//                synchronized (this) {
//                    mRetailersModel.getRetailersList(mPreferences.getString("userId"));
//                }
//            } else {
//                new NetworkConnectionDetector(RetailersActivity.this).displayNoNetworkError(RetailersActivity.this);
//            }
//            return true;
//        }

        if (id == R.id.autorenew) {
            if (new NetworkConnectionDetector(RetailersActivity.this).isNetworkConnected()) {
                showAlertDialog(RetailersActivity.this, "Sync process", "Are you sure, you want start the sync process?");
            } else {
                new NetworkConnectionDetector(RetailersActivity.this).displayNoNetworkError(RetailersActivity.this);
            }
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
            intent = new Intent(this, AgentsActivity.class);
            startActivity(intent);
            finish();
        } else if (mRetailersHomeScreen.equals("Retailers@Home")) {
            //intent = new Intent(this, RetailersActivity.class);
            //startActivity(intent);
            finish();

        } else if (mDashboardHomeScreen.equals("Dashboard@Home")) {
            intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            intent = new Intent(this, DashboardActivity.class);
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
                    showCustomValidationAlertForSync(RetailersActivity.this, "upload");
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
                alertDialogBuilder1.setMessage("Downloading retailers/consumers... Please wait.. ");
                synchronized (this) {
                    mRetailersModel.getAgentsList("retailers");
                }
            } else {
                List<TDCCustomer> unUploadedTDCCustomers = mDBHelper.fetchAllUnUploadedRecordsFromTDCCustomers();
                uploadedCount = unUploadedTDCCustomers.size();
                if (uploadedCount > 0) {
                    alertDialogBuilder1.setMessage("Uploading retailers/consumers... Please wait.. ");
                    fetchCountFromDB(uploadedCount);
                    if (new NetworkConnectionDetector(activityContext).isNetworkConnected()) {
                        Intent syncTDCCustomersServiceIntent = new Intent(activityContext, SyncTDCCustomersService.class);
                        startService(syncTDCCustomersServiceIntent);
                    }
                } else {
                    alertDialogBuilder1.setMessage("Downloading retailers/consumers... Please wait.. ");
                    synchronized (this) {
                        mRetailersModel.getAgentsList("retailers");
                    }
                }
            }

//            alertDialogBuilder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    if (uploadedCount == 0 && isDataDisplayed) {
//                        isDataDisplayed = false;
//                        dialog.dismiss();
//                    }
//                }
//            });

            alertDialog1 = alertDialogBuilder1.create();
            alertDialog1.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

//        dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.custom_popup);
//        dialog.setCancelable(false);
//
//        RelativeLayout popUpLayout = (RelativeLayout) dialog.findViewById(R.id.CustomLayout);
//
//        mSyncText = (TextView) popUpLayout.findViewById(R.id.SyncText);
//        List<TDCCustomer> unUploadedTDCCustomers = mDBHelper.fetchAllUnUploadedRecordsFromTDCCustomers();
//        uploadedCount = unUploadedTDCCustomers.size();
//        System.out.println("COUNT:: " + uploadedCount);
//        if (uploadedCount > 0) {
//            fetchCountFromDB(uploadedCount);
//            if (new NetworkConnectionDetector(activityContext).isNetworkConnected()) {
//                Intent syncTDCCustomersServiceIntent = new Intent(activityContext, SyncTDCCustomersService.class);
//                startService(syncTDCCustomersServiceIntent);
//            }
//        } else {
//            System.out.println("COUNT:: " + uploadedCount);
//            System.out.println("ELSEEEEEEEEE:: ");
//            mHandler.removeCallbacks(mRunnable);
//            mSyncText.setText("Downloading retailers/consumers... Please wait.. ");
//            synchronized (this) {
//                mRetailersModel.getAgentsList("retailers");
//            }
//            mRunnable = new Runnable() {
//                @Override
//                public void run() {
//                    mHandler.removeCallbacks(mRunnable);
//                    if (dialog != null) {
//                        dialog.dismiss();
//                    }
//                }
//            };
//            mHandler.postDelayed(mRunnable, 1000);
//        }
//
//        dialog.show();
    }

    private void fetchCountFromDB(final int uploadedCount11) {
        if (uploadedCount11 > 0) {
            //mSyncText.setText("Uploading " + String.valueOf(this.uploadedCount1) + " of " + String.valueOf(uploadedCount));
            //mSyncText.setText("Uploading retailers/consumers... Please wait.. ");
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    List<TDCCustomer> unUploadedTDCCustomers = mDBHelper.fetchAllUnUploadedRecordsFromTDCCustomers();
                    if (unUploadedTDCCustomers.size() < uploadedCount) {
                        uploadedCount1++;
                    }
                    fetchCountFromDB(unUploadedTDCCustomers.size());
                }
            };
            mHandler.postDelayed(mRunnable, 1000);
        } else {
            uploadedCount = 0;
            mHandler.removeCallbacks(mRunnable);
            //mSyncText.setText("Downloading retailers/consumers... Please wait.. ");
            synchronized (this) {
                if (alertDialogBuilder1 != null) {
                    alertDialog1.dismiss();
                    alertDialogBuilder1 = null;
                }
            }
            synchronized (this) {
                showCustomValidationAlertForSync(RetailersActivity.this, "down");
            }
//            synchronized (this) {
//                mRetailersModel.getAgentsList("retailers");
//            }
//            mRunnable = new Runnable() {
//                @Override
//                public void run() {
//                    if (isDataDisplayed) {
//                        //isDataDisplayed = false;
//                        mHandler.removeCallbacks(mRunnable);
//                    }
//                }
//            };
//            mHandler.postDelayed(mRunnable, 1000);
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

}