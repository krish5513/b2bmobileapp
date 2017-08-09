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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TripSheetReturnsAdapter;
import com.rightclickit.b2bsaleon.beanclass.DeliverysBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetReturnsBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.TripSheetReturnsListener;
import com.rightclickit.b2bsaleon.services.SyncTripsheetReturnsService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TripsheetReturns extends AppCompatActivity implements TripSheetReturnsListener {
    private Context activityContext;
    private MMSharedPreferences mmSharedPreferences;
    private DBHelper mDBHelper;

    private SearchView search;
    private TextView companyName;
    private ListView tripSheetReturnProductsList;
    private TripSheetReturnsAdapter mTripSheetReturnsAdapter;
    private String mTripSheetId = "", mAgentId = "", mAgentName = "", mAgentCode = "", mAgentRouteId = "", mAgentRouteCode = "", mAgentSoId = "", mAgentSoCode = "", loggedInUserId;
    private boolean isReturnsDataSaved = false, isReturnsInEditingMode = false;
    private ArrayList<DeliverysBean> allProductsListFromStock = new ArrayList<>();
    private Map<String, DeliverysBean> selectedProductsHashMap;
    private Map<String, String> previouslyReturnedProductsHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripsheet_returns);

        try {
            this.getSupportActionBar().setTitle("RETURNS");
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

            companyName = (TextView) findViewById(R.id.companyName);
            tripSheetReturnProductsList = (ListView) findViewById(R.id.trip_sheet_return_products_list_view);

            activityContext = TripsheetReturns.this;
            mmSharedPreferences = new MMSharedPreferences(activityContext);
            mDBHelper = new DBHelper(activityContext);

            mTripSheetId = this.getIntent().getStringExtra("tripsheetId");
            mAgentId = this.getIntent().getStringExtra("agentId");
            mAgentCode = this.getIntent().getStringExtra("agentCode");
            mAgentName = this.getIntent().getStringExtra("agentName");
            loggedInUserId = mmSharedPreferences.getString("userId");
            mAgentRouteId = this.getIntent().getStringExtra("agentRouteId");
            mAgentRouteCode = this.getIntent().getStringExtra("agentRouteCode");
            mAgentSoId = this.getIntent().getStringExtra("agentSoId");
            mAgentSoCode = this.getIntent().getStringExtra("agentSoCode");

            companyName.setText(mAgentName);

            selectedProductsHashMap = new HashMap<>();
            previouslyReturnedProductsHashMap = new HashMap<>();

            // In order to pre populate when you came back to this screen.
            previouslyReturnedProductsHashMap = mDBHelper.getAgentPreviouslyReturnsProductsList(mTripSheetId, mAgentSoId, mAgentId);
            if (previouslyReturnedProductsHashMap.size() > 0)
                isReturnsInEditingMode = true;

            Map<String, String> deliveredProductsHashMap = mDBHelper.fetchDeliveriesListByTripSheetId(mTripSheetId);

            allProductsListFromStock = mDBHelper.fetchAllRecordsFromProductsAndStockTableForDeliverys(mTripSheetId);

            mTripSheetReturnsAdapter = new TripSheetReturnsAdapter(activityContext, this, this, allProductsListFromStock, previouslyReturnedProductsHashMap, deliveredProductsHashMap);
            tripSheetReturnProductsList.setAdapter(mTripSheetReturnsAdapter);

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
                    mTripSheetReturnsAdapter.filter(query);
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

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, TripSheetView.class);
        intent.putExtra("tripsheetId", mTripSheetId);
        startActivity(intent);
        finish();
    }

    public void saveTripSheetReturns(View v) {
        showAlertDialogWithCancelButton(activityContext, "User Action!", "Do you want to save data?");
    }

    public void showTripSheetReturnsPreview(View v) {
        if (isReturnsDataSaved || isReturnsInEditingMode) {
            Intent i = new Intent(activityContext, TripsheetDeliveryPreview.class);
            i.putExtra("tripsheetId", mTripSheetId);
            i.putExtra("agentId", mAgentId);
            i.putExtra("agentCode", mAgentCode);
            i.putExtra("agentName", mAgentName);
            i.putExtra("agentRouteId", mAgentRouteId);
            i.putExtra("agentRouteCode", mAgentRouteCode);
            i.putExtra("agentSoId", mAgentSoId);
            i.putExtra("agentSoCode", mAgentSoCode);
            startActivity(i);
            finish();
        } else {
            Toast.makeText(activityContext, "This Preview is unavailable untill you saved the tripsheet returns data.", Toast.LENGTH_LONG).show();
        }
    }

    public void openTripSheetDeliveries(View v) {
        Intent i = new Intent(activityContext, TripsheetDelivery.class);
        i.putExtra("tripsheetId", mTripSheetId);
        i.putExtra("agentId", mAgentId);
        i.putExtra("agentCode", mAgentCode);
        i.putExtra("agentName", mAgentName);
        i.putExtra("agentRouteId", mAgentRouteId);
        i.putExtra("agentRouteCode", mAgentRouteCode);
        i.putExtra("agentSoId", mAgentSoId);
        i.putExtra("agentSoCode", mAgentSoCode);
        startActivity(i);
        finish();
    }

    public void openTripSheetPayments(View v) {
        Intent i = new Intent(activityContext, TripsheetPayments.class);
        i.putExtra("tripsheetId", mTripSheetId);
        i.putExtra("agentId", mAgentId);
        i.putExtra("agentCode", mAgentCode);
        i.putExtra("agentName", mAgentName);
        i.putExtra("agentRouteId", mAgentRouteId);
        i.putExtra("agentRouteCode", mAgentRouteCode);
        i.putExtra("agentSoId", mAgentSoId);
        i.putExtra("agentSoCode", mAgentSoCode);
        startActivity(i);
        finish();
    }

    private void showAlertDialogWithCancelButton(Context context, String title, String message) {
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
                    saveTripSheetReturnsProductsData();
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
    public void updateSelectedProductsList(Map<String, DeliverysBean> productsList) {
        selectedProductsHashMap = productsList;
        isReturnsDataSaved = false;
    }

    public void saveTripSheetReturnsProductsData() {
        try {
            if (selectedProductsHashMap.size() > 0) {
                long currentTimeStamp = System.currentTimeMillis();

                ArrayList<TripSheetReturnsBean> mTripsheetsReturnsList = new ArrayList<>();

                for (Map.Entry<String, DeliverysBean> deliverysBeanEntry : selectedProductsHashMap.entrySet()) {
                    DeliverysBean deliverysBean = deliverysBeanEntry.getValue();

                    TripSheetReturnsBean tripSheetReturnsBean = new TripSheetReturnsBean();
                    tripSheetReturnsBean.setmTripshhetReturnsReturn_no("");
                    tripSheetReturnsBean.setmTripshhetReturnsTrip_id(mTripSheetId);
                    tripSheetReturnsBean.setmTripshhetReturns_so_id(mAgentSoId);
                    tripSheetReturnsBean.setmTripshhetReturns_so_code(mAgentSoCode);
                    tripSheetReturnsBean.setmTripshhetReturnsUser_id(mAgentId);
                    tripSheetReturnsBean.setmTripshhetReturnsUser_codes(mAgentCode);
                    tripSheetReturnsBean.setmTripshhetReturnsRoute_id(mAgentRouteId);
                    tripSheetReturnsBean.setmTripshhetReturnsRoute_codes(mAgentRouteCode);
                    tripSheetReturnsBean.setmTripshhetReturnsProduct_ids(deliverysBean.getProductId());
                    tripSheetReturnsBean.setmTripshhetReturnsProduct_codes(deliverysBean.getProductCode());
                    tripSheetReturnsBean.setmTripshhetReturnsQuantity(String.valueOf(deliverysBean.getSelectedQuantity()));
                    tripSheetReturnsBean.setmTripshhetReturnsType("R");
                    tripSheetReturnsBean.setmTripshhetReturnsStatus("A");
                    tripSheetReturnsBean.setmTripshhetReturnsDelete("N");
                    tripSheetReturnsBean.setmTripshhetReturnsCreated_by(loggedInUserId);
                    tripSheetReturnsBean.setmTripshhetReturnsCreated_on(String.valueOf(currentTimeStamp));
                    tripSheetReturnsBean.setmTripshhetReturnsUpdated_on(String.valueOf(currentTimeStamp));
                    tripSheetReturnsBean.setmTripshhetReturnsUpdated_by(loggedInUserId);

                    mTripsheetsReturnsList.add(tripSheetReturnsBean);
                }

                mDBHelper.insertTripsheetsReturnsListData(mTripsheetsReturnsList);
                isReturnsDataSaved = true;
                Toast.makeText(activityContext, "Return Products Data Saved Successfully.", Toast.LENGTH_LONG).show();

                if (new NetworkConnectionDetector(activityContext).isNetworkConnected()) {
                    Intent syncTripSheetDeliveriesServiceIntent = new Intent(activityContext, SyncTripsheetReturnsService.class);
                    startService(syncTripSheetDeliveriesServiceIntent);
                }
            } else {
                Toast.makeText(activityContext, "Please select at least one product to save.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
