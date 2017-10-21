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
import android.widget.ImageView;
import android.widget.ListView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.AgentStockAdapter;
import com.rightclickit.b2bsaleon.adapters.RouteStockAdapter;
import com.rightclickit.b2bsaleon.adapters.TripsheetsStockListAdapter;
import com.rightclickit.b2bsaleon.beanclass.AgentsStockBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetDeliveriesBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetReturnsBean;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.TripsheetsModel;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import java.util.ArrayList;
import java.util.HashMap;

public class RouteStock extends AppCompatActivity {
    private Context applicationContext, activityContext;
    private SearchView search;
    private String tripSheetId;
    RouteStockAdapter routestockadapter;
    private DBHelper mDBHelper;
    private TripsheetsModel mTripsheetsModel;
    ListView Routestocklist;
    private HashMap<String, String> deliveryQuantityListMap = new HashMap<String, String>();
    private HashMap<String, String> returnQuantityListMap = new HashMap<String, String>();

    // ArrayList<AgentsStockBean> stockBeanArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_stock);
        activityContext = RouteStock.this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tripSheetId = bundle.getString("tripSheetId");
            //str_Tripcode=bundle.getString("tripsheetCode");
            //str_Tripdate=bundle.getString("tripsheetDate");
        }
        mDBHelper = new DBHelper(activityContext);

        this.getSupportActionBar().setTitle("ROUTE STOCK");
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
        Routestocklist = (ListView) findViewById(R.id.RouteStockList);
        mTripsheetsModel = new TripsheetsModel(this, RouteStock.this);
        ArrayList<TripsheetsStockList> tripsheetsStockLists = mDBHelper.fetchAllTripsheetsStockList(tripSheetId);


        if (new NetworkConnectionDetector(RouteStock.this).isNetworkConnected()) {
            mTripsheetsModel.getTripsheetsStockList(tripSheetId);
        } else if (tripsheetsStockLists.size() > 0) {
            loadTripsData(tripsheetsStockLists);
        }

    }


    public void loadTripsData(ArrayList<TripsheetsStockList> tripsStockList) {
        // ALL DELIVIRES
        ArrayList<TripSheetDeliveriesBean> deliveriesBeenList = mDBHelper.fetchAllTripsheetsDeliveriesListByTripAndProductId(tripSheetId, "");
        System.out.println("ALL DELIVERIES:::: " + deliveriesBeenList.size());
        if (deliveriesBeenList.size() > 0) {
            for (int g = 0; g < deliveriesBeenList.size(); g++) {
                if (deliveryQuantityListMap.get(deliveriesBeenList.get(g).getmTripsheetDelivery_productId()) != null) {
                    double quantity = Double.parseDouble(deliveryQuantityListMap.get(deliveriesBeenList.get(g).getmTripsheetDelivery_productId()));

                    double sumQuantity = quantity + Double.parseDouble(deliveriesBeenList.get(g).getmTripsheetDelivery_Quantity());

                    deliveryQuantityListMap.put(deliveriesBeenList.get(g).getmTripsheetDelivery_productId(),
                            String.valueOf(sumQuantity));
                } else {
                    deliveryQuantityListMap.put(deliveriesBeenList.get(g).getmTripsheetDelivery_productId(),
                            deliveriesBeenList.get(g).getmTripsheetDelivery_Quantity());
                }
            }
        }

        // ALL RETURNS
        ArrayList<TripSheetReturnsBean> returnsBeenList = mDBHelper.fetchAllTripsheetsReturnsListByTripId(tripSheetId);
        System.out.println("ALL RETURNS:::: " + returnsBeenList.size());
        if (returnsBeenList.size() > 0) {
            for (int g = 0; g < returnsBeenList.size(); g++) {
                if (returnQuantityListMap.get(returnsBeenList.get(g).getmTripshhetReturnsProduct_ids()) != null) {
                    double quantity = Double.parseDouble(returnQuantityListMap.get(returnsBeenList.get(g).getmTripshhetReturnsProduct_ids()));

                    double sumQuantity = quantity + Double.parseDouble(returnsBeenList.get(g).getmTripshhetReturnsQuantity());

                    returnQuantityListMap.put(returnsBeenList.get(g).getmTripshhetReturnsProduct_ids(),
                            String.valueOf(sumQuantity));
                } else {
                    returnQuantityListMap.put(returnsBeenList.get(g).getmTripshhetReturnsProduct_ids(),
                            returnsBeenList.get(g).getmTripshhetReturnsQuantity());
                }
            }
        }
        if (routestockadapter != null) {
            routestockadapter = null;
        }

        routestockadapter = new RouteStockAdapter(this, RouteStock.this, this, tripsStockList, deliveryQuantityListMap, returnQuantityListMap);
        Routestocklist.setAdapter(routestockadapter);


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
                    //  deliveriesAdapter.filter(query);
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

        if (id == R.id.autorenew) {
            if (new NetworkConnectionDetector(RouteStock.this).isNetworkConnected()) {
                mTripsheetsModel.getTripsheetsStockList(tripSheetId);
            } else {
                new NetworkConnectionDetector(RouteStock.this).displayNoNetworkError(RouteStock.this);
            }
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
        Intent intent = new Intent(this, TripSheetViewPreview.class);
        intent.putExtra("tripSheetId", tripSheetId);
        startActivity(intent);

    }


}
