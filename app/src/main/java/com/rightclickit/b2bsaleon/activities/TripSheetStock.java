package com.rightclickit.b2bsaleon.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.rightclickit.b2bsaleon.adapters.TripsheetsStockListAdapter;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.customviews.CustomAlertDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.TripSheetStockListener;
import com.rightclickit.b2bsaleon.models.TripsheetsModel;
import com.rightclickit.b2bsaleon.services.SyncTripSheetsStockService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TripSheetStock extends AppCompatActivity implements TripSheetStockListener {
    private Context applicationContext, activityContext;
    private MMSharedPreferences mmSharedPreferences;
    private DBHelper mDBHelper;

    private SearchView search;
    private TextView dispatchTitle, verifyTitle, ts_dispatch_save, ts_stock_verify, ts_stock_preview;
    private ListView mTripsheetsStockListView;
    private LinearLayout tps_stock_save_layout, tps_stock_verify_layout, tps_stock_preview_layout;

    private ArrayList<String> privilegeActionsData;
    private TripsheetsModel mTripsheetsModel;
    private TripsheetsStockListAdapter mTripsheetsStockAdapter;
    private String mTripSheetId, mTripSheetCode, mTripSheetDate;
    private Map<String, TripsheetsStockList> productsDispatchListHashMap, productsVerifyListHashMap; // Hash Map Key = Product Id
    private String loggedInUserId;
    private boolean isStockDispatched = false, isStockVerified = false, isTripSheetClosed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_sheet_stock);

        try {
            applicationContext = getApplicationContext();
            activityContext = TripSheetStock.this;
            mmSharedPreferences = new MMSharedPreferences(activityContext);
            mDBHelper = new DBHelper(activityContext);

            loggedInUserId = mmSharedPreferences.getString("userId");

            //String activityTitle = String.format("TRIP #%s, %s", "980915", Utility.formatDate(new Date(), Constants.TDC_SALES_LIST_DATE_DISPLAY_FORMAT));
            String activityTitle = String.format("TRIP %s", Utility.formatDate(new Date(), Constants.TDC_SALES_LIST_DATE_DISPLAY_FORMAT));

            this.getSupportActionBar().setTitle(activityTitle);
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

            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                mTripSheetId = bundle.getString("tripsheetId");
                mTripSheetCode = bundle.getString("tripsheetCode");
                mTripSheetDate = bundle.getString("tripsheetDate");
            }

            isTripSheetClosed = mDBHelper.isTripSheetClosed(mTripSheetId);

            dispatchTitle = (TextView) findViewById(R.id.dispatchTitle);
            verifyTitle = (TextView) findViewById(R.id.verifyTitle);
            mTripsheetsStockListView = (ListView) findViewById(R.id.tripsheetStockListView);
            ts_dispatch_save = (TextView) findViewById(R.id.ts_dispatch_save);
            ts_stock_verify = (TextView) findViewById(R.id.ts_stock_verify);
            ts_stock_preview = (TextView) findViewById(R.id.ts_stock_preview);
            tps_stock_save_layout = (LinearLayout) findViewById(R.id.tps_stock_save_layout);
            tps_stock_verify_layout = (LinearLayout) findViewById(R.id.tps_stock_verify_layout);
            tps_stock_preview_layout = (LinearLayout) findViewById(R.id.tps_stock_preview_layout);

            privilegeActionsData = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mmSharedPreferences.getString("TripSheets"));
            //System.out.println("F 11111 ***COUNT === " + privilegeActionsData.size());
            if (privilegeActionsData.contains("Stock_Dispatch")) {
                dispatchTitle.setVisibility(View.VISIBLE);
            }

            if (privilegeActionsData.contains("Stock_Verify")) {
                verifyTitle.setVisibility(View.VISIBLE);
                tps_stock_verify_layout.setVisibility(View.VISIBLE);
            }

            if (privilegeActionsData.contains("Stock_Save")) {
                tps_stock_save_layout.setVisibility(View.VISIBLE);
            }

            if (privilegeActionsData.contains("stock_Preview_Print")) {
                tps_stock_preview_layout.setVisibility(View.VISIBLE);
            }

            if (privilegeActionsData.contains("Stock_Verify") && !privilegeActionsData.contains("Stock_Save")) {
                isStockDispatched = true;
            }

            if (isTripSheetClosed) {
                tps_stock_save_layout.setVisibility(View.GONE);
                tps_stock_verify_layout.setVisibility(View.GONE);
            }

            mTripsheetsModel = new TripsheetsModel(this, TripSheetStock.this);
            ArrayList<TripsheetsStockList> tripsheetsStockLists = mDBHelper.fetchAllTripsheetsStockList(mTripSheetId);

            productsDispatchListHashMap = new HashMap<>();
            productsVerifyListHashMap = new HashMap<>();

            if (new NetworkConnectionDetector(TripSheetStock.this).isNetworkConnected()) {
                mTripsheetsModel.getTripsheetsStockList(mTripSheetId);
            } else if (tripsheetsStockLists.size() > 0) {
                loadTripsData(tripsheetsStockLists);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadTripsData(ArrayList<TripsheetsStockList> tripsStockList) {
        if (mTripsheetsStockAdapter != null) {
            mTripsheetsStockAdapter = null;
        }

        mTripsheetsStockAdapter = new TripsheetsStockListAdapter(this, TripSheetStock.this, this, tripsStockList, privilegeActionsData);
        mTripsheetsStockListView.setAdapter(mTripsheetsStockAdapter);

        // Checking weather this stock already verified or not.
        if (tripsStockList.size() > 0) {
            TripsheetsStockList stockList = tripsStockList.get(0);

            if (stockList.getIsStockDispatched() == 1) {
                isStockDispatched = true;
            }

            if (stockList.getIsStockVerified() == 1) {
                isStockVerified = true;
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
                    mTripsheetsStockAdapter.filter(query);
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

        switch (id) {
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
        menu.findItem(R.id.autorenew).setVisible(true);
        menu.findItem(R.id.sort).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, TripSheetsActivity.class);
        startActivity(intent);
        finish();
    }

    public void showTripSheetStockPreview(View view) {
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        ts_stock_preview.startAnimation(animation1);
        if (!isStockVerified)
           // Toast.makeText(activityContext, "This Preview is unavailable untill the tripsheet stock is verified", Toast.LENGTH_LONG).show();
            CustomAlertDialog.showAlertDialog(activityContext, "Failed", getResources().getString(R.string.preview));
        else {
            Intent i = new Intent(TripSheetStock.this, TripsheetStockPreview.class);
            i.putExtra("tripSheetId", mTripSheetId);
            i.putExtra("tripsheetCode", mTripSheetCode);
            i.putExtra("tripsheetDate", mTripSheetDate);

            startActivity(i);
            finish();
        }
    }

    public void saveTripSheetStock(View view) {
        //if (!isStockDispatched)
        //showAlertDialogWithCancelButton(activityContext, "User Action!", "Are you sure want to save?\n\nOnce you saved you can't edit.", "Save");
        showAlertDialogWithCancelButton(activityContext, "User Action!", "Are you sure want to save?", "Save");
    }

    public void verifyTripSheetStock(View view) {
        if (!isStockDispatched)
            //Toast.makeText(activityContext, "This Trip Sheet Stock is not yet dispatched.", Toast.LENGTH_LONG).show();
        CustomAlertDialog.showAlertDialog(activityContext, "Failed", getResources().getString(R.string.stock));
        else {
            //if (!isStockVerified)
            //showAlertDialogWithCancelButton(activityContext, "User Action!", "Are you sure want to verify?\n\nOnce you verified you can't edit.", "Verify");
            showAlertDialogWithCancelButton(activityContext, "User Action!", "Are you sure want to verify?", "Verify");
        }
    }

    private void showAlertDialogWithCancelButton(Context context, String title, String message, final String actionType) {
        try {
            AlertDialog alertDialog = null;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    if (actionType.equals("Save"))
                        saveProductsDispatchList();
                    else if (actionType.equals("Verify"))
                        saveProductsVerifyList();
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
                cancelButton.setTextColor(ContextCompat.getColor(context, R.color.alert_dialog_color_accent));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProductsDispatchList(Map<String, TripsheetsStockList> productsList) {
        this.productsDispatchListHashMap = productsList;

    }

    @Override
    public void updateProductsVerifyList(Map<String, TripsheetsStockList> productsList) {
        this.productsVerifyListHashMap = productsList;

    }

    public void saveProductsDispatchList() {
        long currentTimeStamp = System.currentTimeMillis();

        for (Map.Entry<String, TripsheetsStockList> stockList : productsDispatchListHashMap.entrySet()) {
            TripsheetsStockList currentStock = stockList.getValue();
            currentStock.setmTripsheetStockDispatchBy(loggedInUserId);
            currentStock.setmTripsheetStockDispatchDate(String.valueOf(currentTimeStamp));

            mDBHelper.updateTripSheetStockDispatchList(currentStock);
        }

        isStockDispatched = true;
        //Toast.makeText(activityContext, "Stock Dispatched Successfully.", Toast.LENGTH_LONG).show();
        CustomAlertDialog.showAlertDialog(activityContext, "Success", getResources().getString(R.string.stocksuccess));

        if (new NetworkConnectionDetector(activityContext).isNetworkConnected()) {
            Intent syncTripSheetsStockServiceIntent = new Intent(activityContext, SyncTripSheetsStockService.class);
            syncTripSheetsStockServiceIntent.putExtra("actionType", "dispatch");
            startService(syncTripSheetsStockServiceIntent);
        }
    }

    public void saveProductsVerifyList() {
        long currentTimeStamp = System.currentTimeMillis();
        String tripSheetId = "";

        for (Map.Entry<String, TripsheetsStockList> stockList : productsVerifyListHashMap.entrySet()) {
            TripsheetsStockList currentStock = stockList.getValue();
            currentStock.setmTripsheetStockVerifyBy(loggedInUserId);
            currentStock.setmTripsheetStockVerifiedDate(String.valueOf(currentTimeStamp));
            double extraQuantity = Double.parseDouble(currentStock.getmTripsheetStockVerifiedQuantity()) - Double.parseDouble(currentStock.getmTripsheetStockProductOrderQuantity());
            if (extraQuantity > 0) {
                currentStock.setInStockQuantity(currentStock.getmTripsheetStockProductOrderQuantity());
                currentStock.setExtraQuantity(String.valueOf(extraQuantity));
            } else {
                currentStock.setInStockQuantity(currentStock.getmTripsheetStockVerifiedQuantity());
                currentStock.setExtraQuantity(String.valueOf(0));
            }

            // to update status as verified in trip sheet table, we are initializing trip sheet id.
            tripSheetId = currentStock.getmTripsheetStockTripsheetId();

            mDBHelper.updateTripSheetStockVerifyList(currentStock);
        }

        // Updating trip sheet verify status in trip sheets table
        mDBHelper.updateTripSheetStockVerifyStatus(tripSheetId);

        isStockVerified = true;

        //Toast.makeText(activityContext, "Stock Verified Successfully.", Toast.LENGTH_LONG).show();
        CustomAlertDialog.showAlertDialog(activityContext, "Success", getResources().getString(R.string.stockverify));
        if (new NetworkConnectionDetector(activityContext).isNetworkConnected()) {
            Intent syncTripSheetsStockServiceIntent = new Intent(activityContext, SyncTripSheetsStockService.class);
            syncTripSheetsStockServiceIntent.putExtra("actionType", "verify");
            startService(syncTripSheetsStockServiceIntent);
        }
    }
}
