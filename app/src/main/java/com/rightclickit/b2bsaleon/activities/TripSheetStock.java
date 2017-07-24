package com.rightclickit.b2bsaleon.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TripsheetsStockListAdapter;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.TripsheetsModel;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.Date;

public class TripSheetStock extends AppCompatActivity {
    private Context applicationContext, activityContext;
    private MMSharedPreferences mmSharedPreferences;
    private DBHelper mDBHelper;

    private SearchView search;
    private TextView dispatchTitle, verifyTitle, ts_dispatch_save, ts_stock_verify, ts_stock_preview;
    private ListView mTripsheetsStockListView;

    private ArrayList<String> privilegeActionsData;
    private TripsheetsModel mTripsheetsModel;
    private TripsheetsStockListAdapter mTripsheetsStockAdapter;
    private String mTripSheetId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_sheet_stock);

        try {
            applicationContext = getApplicationContext();
            activityContext = TripSheetStock.this;
            mmSharedPreferences = new MMSharedPreferences(activityContext);
            mDBHelper = new DBHelper(activityContext);

            String activityTitle = String.format("TRIP #%s, %s", "980915", Utility.formatDate(new Date(), Constants.TDC_SALES_LIST_DATE_DISPLAY_FORMAT));

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

            dispatchTitle = (TextView) findViewById(R.id.dispatchTitle);
            verifyTitle = (TextView) findViewById(R.id.verifyTitle);
            mTripsheetsStockListView = (ListView) findViewById(R.id.tripsheetStockListView);
            ts_dispatch_save = (TextView) findViewById(R.id.ts_dispatch_save);
            ts_stock_verify = (TextView) findViewById(R.id.ts_stock_verify);
            ts_stock_preview = (TextView) findViewById(R.id.ts_stock_preview);

            privilegeActionsData = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mmSharedPreferences.getString("TripSheets"));
            //System.out.println("F 11111 ***COUNT === " + privilegeActionsData.size());
            for (int z = 0; z < privilegeActionsData.size(); z++) {
                //System.out.println("Name::: " + privilegeActionsData.get(z).toString());
                if (privilegeActionsData.get(z).toString().equals("Stock_Dispatch")) {
                    dispatchTitle.setVisibility(View.VISIBLE);
                } else if (privilegeActionsData.get(z).toString().equals("Stock_Verify")) {
                    verifyTitle.setVisibility(View.VISIBLE);
                    ts_stock_verify.setVisibility(View.VISIBLE);
                } else if (privilegeActionsData.get(z).toString().equals("Stock_Save")) {
                    ts_dispatch_save.setVisibility(View.VISIBLE);
                } else if (privilegeActionsData.get(z).toString().equals("stock_Preview_Print")) {
                    ts_stock_preview.setVisibility(View.VISIBLE);
                }
            }

            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                mTripSheetId = bundle.getString("tripsheetId");
            }

            mTripsheetsModel = new TripsheetsModel(this, TripSheetStock.this);

            if (new NetworkConnectionDetector(TripSheetStock.this).isNetworkConnected()) {
                mTripsheetsModel.getTripsheetsStockList(mTripSheetId);
            } else {
                ArrayList<TripsheetsStockList> tripsList = mDBHelper.fetchAllTripsheetsStockList(mTripSheetId);
                if (tripsList.size() > 0) {
                    loadTripsData(tripsList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadTripsData(ArrayList<TripsheetsStockList> tripsStockList) {
        if (mTripsheetsStockAdapter != null) {
            mTripsheetsStockAdapter = null;
        }

        mTripsheetsStockAdapter = new TripsheetsStockListAdapter(this, TripSheetStock.this, tripsStockList, privilegeActionsData);
        mTripsheetsStockListView.setAdapter(mTripsheetsStockAdapter);
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
        Intent i = new Intent(TripSheetStock.this, TripsheetStockPreview.class);
        startActivity(i);
        finish();
    }
}
