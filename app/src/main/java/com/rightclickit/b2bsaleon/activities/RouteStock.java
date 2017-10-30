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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.AgentStockAdapter;
import com.rightclickit.b2bsaleon.adapters.RouteStockAdapter;
import com.rightclickit.b2bsaleon.adapters.TripsheetsStockListAdapter;
import com.rightclickit.b2bsaleon.beanclass.AgentsStockBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetDeliveriesBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetReturnsBean;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
import com.rightclickit.b2bsaleon.customviews.CustomAlertDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.RouteStockListener;
import com.rightclickit.b2bsaleon.models.TripsheetsModel;
import com.rightclickit.b2bsaleon.services.SyncCloseTripSheetsStockService;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RouteStock extends AppCompatActivity implements RouteStockListener {
    private Context applicationContext, activityContext;
    private SearchView search;
    private String tripSheetId;
    RouteStockAdapter routestockadapter;
    private DBHelper mDBHelper;
    private TripsheetsModel mTripsheetsModel;
    ListView Routestocklist;
    private LinearLayout mCloseTripsLayout;
    private HashMap<String, String> deliveryQuantityListMap = new HashMap<String, String>();
    private HashMap<String, String> returnQuantityListMap = new HashMap<String, String>();
    private Map<String, String> selectedCBListMap, selectedLeakListMap, selectedOthersListMap,
            selectedPNamesListMap, selectedPDelsListMap, selectedPReturnsListMap;
    private String cbNotZeroStrings = "";
    private ArrayList<TripsheetsStockList> mTripsheetsStockList = new ArrayList<TripsheetsStockList>();

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
        final ArrayList<TripsheetsStockList> tripsheetsStockLists = mDBHelper.fetchAllTripsheetsStockList(tripSheetId);

        mCloseTripsLayout = (LinearLayout) findViewById(R.id.RetailersLayout);
        mCloseTripsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbNotZeroStrings = "";
                synchronized (this) {
                    if (selectedCBListMap.size() > 0) {
                        for (Map.Entry<String, String> productsBeanEntry : selectedCBListMap.entrySet()) {
                            System.out.println("ASDF:::: " + productsBeanEntry.getValue());
                            Double val = Double.parseDouble(productsBeanEntry.getValue());
                            if (val != 0) {
                                if (cbNotZeroStrings.length() == 0) {
                                    cbNotZeroStrings = productsBeanEntry.getKey();
                                } else {
                                    cbNotZeroStrings = cbNotZeroStrings + ", " + productsBeanEntry.getKey();
                                }
                            }
                        }
                    }
                }
                System.out.println("STR:::: " + cbNotZeroStrings);
                System.out.println("TSSSS:::: " + tripsheetsStockLists.size());
                synchronized (this) {
                    if (cbNotZeroStrings.length() > 0) {
                        CustomAlertDialog.showAlertDialog(activityContext, "Failed", "Please make the closing balance as 0 for the following products.\n" + cbNotZeroStrings);
                    } else {
                        if (mTripsheetsStockList.size() > 0) {
                            mTripsheetsStockList.clear();
                        }
                        if (selectedCBListMap.size() > 0) {
                            for (int q = 0; q < tripsheetsStockLists.size(); q++) {
                                TripsheetsStockList tripStockBean = new TripsheetsStockList();

                                if (selectedCBListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductCode()) != null) {
                                    //System.out.println("CB QUANTITY::: " + selectedCBListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductCode()));
                                    tripStockBean.setmCBQuantity(selectedCBListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductCode()));
                                }
                                if (selectedLeakListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()) != null) {
                                    //System.out.println("LEAK QUANTITY::: " + selectedLeakListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                    tripStockBean.setmLeakQuantity(selectedLeakListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                }

                                if (selectedOthersListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()) != null) {
                                    //System.out.println("OTHER QUANTITY::: " + selectedOthersListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                    tripStockBean.setmOtherQuantity(selectedOthersListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                }

                                if (selectedPDelsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()) != null) {
                                    //System.out.println("DEL QUANTITY::: " + selectedPDelsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                    tripStockBean.setmDeliveryQuantity(selectedPDelsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                }

                                if (selectedPReturnsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()) != null) {
                                    //System.out.println("RETU QUANTITY::: " + selectedPReturnsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                    tripStockBean.setmReturnQuantity(selectedPReturnsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                }

                                tripStockBean.setmTripsheetStockProductId(tripsheetsStockLists.get(q).getmTripsheetStockProductId());
                                tripStockBean.setmTripsheetStockTripsheetId(tripsheetsStockLists.get(q).getmTripsheetStockTripsheetId());
                                tripStockBean.setmTripsheetStockId(tripsheetsStockLists.get(q).getmTripsheetStockId());

                                mTripsheetsStockList.add(tripStockBean);
                            }
                        }
                    }
                }
                synchronized (this) {
                    if (mTripsheetsStockList.size() > 0) {
                        System.out.println("HURRYYYY::: " + mTripsheetsStockList.size());
                        // Update the data into tripsheers stock list table and then api
                        mDBHelper.updateTripsheetsStockListDataForCloseTrips(mTripsheetsStockList);
                    } else {
                        System.out.println("BURRRYYYYY::: " + mTripsheetsStockList.size());
                    }
                }

                synchronized (this) {
                    if (mTripsheetsStockList.size() > 0) {
                        System.out.println("HURRYYYY 11111::: " + mTripsheetsStockList.size());
                        // Update the data into tripsheers stock list table and then api
                        if (new NetworkConnectionDetector(activityContext).isNetworkConnected()) {
                            Intent syncTDCOrderServiceIntent = new Intent(activityContext, SyncCloseTripSheetsStockService.class);
                            //syncTDCOrderServiceIntent.putExtra(Constants.BUNDLE_TDC_SALE_ORDER, currentOrder);
                            startService(syncTDCOrderServiceIntent);
                        }
                    } else {
                        System.out.println("BURRRYYYYY 11111::: " + mTripsheetsStockList.size());
                    }
                }

                synchronized (this) {
                    if (mTripsheetsStockList.size() > 0) {
                        int status = mDBHelper.updateTripSheetClosingStatus(tripSheetId);

                        if (status > 0) {
                            Toast.makeText(activityContext, "Trip sheet closed successfully.", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(activityContext, TripSheetsActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(activityContext, "Trip sheet closing failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });


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


    @Override
    public void updateSelectedCBList(Map<String, String> selectedCBList) {
        this.selectedCBListMap = selectedCBList;
        System.out.println("CB LSIT:::: " + selectedCBList.size());
    }

    @Override
    public void updateSelectedLeakageQuantityList(Map<String, String> selectedLeakList) {
        this.selectedLeakListMap = selectedLeakList;
        System.out.println("LEAK LSIT:::: " + selectedLeakListMap.size());
    }

    @Override
    public void updateSelectedOthersQuantityList(Map<String, String> selectedOthersList) {
        this.selectedOthersListMap = selectedOthersList;
        System.out.println("OTHERS LSIT:::: " + selectedOthersListMap.size());
    }

    @Override
    public void updateSelectedPNamesQuantityList(Map<String, String> selectedPNamesList) {
        this.selectedPNamesListMap = selectedPNamesList;
    }

    @Override
    public void updateSelectedPDelsQuantityList(Map<String, String> selectedPDelsList) {
        this.selectedPDelsListMap = selectedPDelsList;
    }

    @Override
    public void updateSelectedPRetunsQuantityList(Map<String, String> selectedPReturnsList) {
        this.selectedPReturnsListMap = selectedPReturnsList;
    }
}
