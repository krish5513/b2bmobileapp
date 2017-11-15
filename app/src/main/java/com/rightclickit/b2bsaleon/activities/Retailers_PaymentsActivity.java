package com.rightclickit.b2bsaleon.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.RetailersPaymentsAdapter;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.List;

public class Retailers_PaymentsActivity extends AppCompatActivity {
    private Context applicationContext, activityContext;
    private MMSharedPreferences mmSharedPreferences;

    private SearchView search;
    private ListView retailers_sales_list_view;
    private TextView no_payments_found_message;

    private long selectedCustomerId;
    private String selectedCustomerId1;
    private DBHelper mDBHelper;
    private RetailersPaymentsAdapter paymentsListAdapter;
    private List<TDCSaleOrder> selectedRetailerAllOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailers_payments);

        try {
            applicationContext = getApplicationContext();
            activityContext = Retailers_PaymentsActivity.this;

            this.getSupportActionBar().setTitle("RETAILER PAYMENTS");
            this.getSupportActionBar().setSubtitle(null);
            this.getSupportActionBar().setLogo(R.drawable.ic_store_white);
            this.getSupportActionBar().setDisplayUseLogoEnabled(true);
            this.getSupportActionBar().setDisplayShowHomeEnabled(true);

            final ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

            mmSharedPreferences = new MMSharedPreferences(activityContext);
            mDBHelper = new DBHelper(activityContext);

            no_payments_found_message = (TextView) findViewById(R.id.no_payments_found_message);
            retailers_sales_list_view = (ListView) findViewById(R.id.retailers_sales_list_view);

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                // selectedCustomerId = bundle.getLong(Constants.BUNDLE_SELECTED_CUSTOMER_ID);
                selectedCustomerId1 = bundle.getString("AgentId");
            }
            //System.out.println("RETAILER ID::: "+ selectedCustomerId1);
            selectedRetailerAllOrders = mDBHelper.fetchTDCSalesOrdersForSelectedCustomer(selectedCustomerId1);
            //System.out.println("RETAILER SALE SIZE::: "+ selectedRetailerAllOrders.size());
            if (selectedRetailerAllOrders.size() <= 0) {
                retailers_sales_list_view.setVisibility(View.GONE);
                no_payments_found_message.setVisibility(View.VISIBLE);
            } else {
                no_payments_found_message.setVisibility(View.GONE);
                retailers_sales_list_view.setVisibility(View.VISIBLE);

                paymentsListAdapter = new RetailersPaymentsAdapter(activityContext, this, selectedRetailerAllOrders);
                retailers_sales_list_view.setAdapter(paymentsListAdapter);
            }

        } catch (Exception e) {
            e.printStackTrace();
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
                    paymentsListAdapter.filter(query);
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.notifications).setVisible(false);
        menu.findItem(R.id.settings).setVisible(false);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(true);
        menu.findItem(R.id.Add).setVisible(false);
        menu.findItem(R.id.autorenew).setVisible(false);
        menu.findItem(R.id.sort).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, RetailersActivity.class);
        startActivity(intent);
        finish();
    }


    /**
     * Method to display alert without field.
     *
     * @param context
     * @param message
     */
//    private void showCustomValidationAlertForSync(Activity context, String message) {
//        // custom dialog
//        try {
//
//            alertDialogBuilder1 = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
//            alertDialogBuilder1.setTitle("Sync Process");
//            alertDialogBuilder1.setCancelable(false);
//            if (message.equals("down")) {
//                alertDialogBuilder1.setMessage("Downloading retailers/consumers... Please wait.. ");
//                synchronized (this) {
//                    mRetailersModel.getAgentsList("retailers");
//                }
//            } else {
//                List<TDCCustomer> unUploadedTDCCustomers = mDBHelper.fetchAllUnUploadedRecordsFromTDCCustomers();
//                uploadedCount = unUploadedTDCCustomers.size();
//                if (uploadedCount > 0) {
//                    alertDialogBuilder1.setMessage("Uploading retailers/consumers... Please wait.. ");
//                    fetchCountFromDB(uploadedCount);
//                    if (new NetworkConnectionDetector(activityContext).isNetworkConnected()) {
//                        Intent syncTDCCustomersServiceIntent = new Intent(activityContext, SyncTDCCustomersService.class);
//                        startService(syncTDCCustomersServiceIntent);
//                    }
//                } else {
//                    alertDialogBuilder1.setMessage("Downloading retailers/consumers... Please wait.. ");
//                    synchronized (this) {
//                        mRetailersModel.getAgentsList("retailers");
//                    }
//                }
//            }
//
//            alertDialogBuilder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    if (uploadedCount == 0 && isDataDisplayed) {
//                        isDataDisplayed = false;
//                        dialog.dismiss();
//                    }
//                }
//            });
//
//            alertDialog1 = alertDialogBuilder1.create();
//            alertDialog1.show();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void fetchCountFromDB(final int uploadedCount11) {
//        if (uploadedCount11 > 0) {
//            //mSyncText.setText("Uploading " + String.valueOf(this.uploadedCount1) + " of " + String.valueOf(uploadedCount));
//            //mSyncText.setText("Uploading retailers/consumers... Please wait.. ");
//            mRunnable = new Runnable() {
//                @Override
//                public void run() {
//                    List<TDCCustomer> unUploadedTDCCustomers = mDBHelper.fetchAllUnUploadedRecordsFromTDCCustomers();
//                    if (unUploadedTDCCustomers.size() < uploadedCount) {
//                        uploadedCount1++;
//                    }
//                    fetchCountFromDB(unUploadedTDCCustomers.size());
//                }
//            };
//            mHandler.postDelayed(mRunnable, 1000);
//        } else {
//            uploadedCount = 0;
//            mHandler.removeCallbacks(mRunnable);
//            //mSyncText.setText("Downloading retailers/consumers... Please wait.. ");
//            synchronized (this) {
//                if (alertDialogBuilder1 != null) {
//                    alertDialog1.dismiss();
//                    alertDialogBuilder1 = null;
//                }
//            }
//            synchronized (this) {
//                showCustomValidationAlertForSync(RetailersActivity.this, "down");
//            }
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
//        }
//    }
}
