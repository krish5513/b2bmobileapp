package com.rightclickit.b2bsaleon.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TripSheetReturnsAdapter;
import com.rightclickit.b2bsaleon.beanclass.DeliverysBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetReturnsBean;
import com.rightclickit.b2bsaleon.customviews.CustomAlertDialog;
import com.rightclickit.b2bsaleon.customviews.CustomProgressDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.TripSheetReturnsListener;
import com.rightclickit.b2bsaleon.services.SyncTripsheetReturnsService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TripsheetReturns extends AppCompatActivity implements TripSheetReturnsListener {
    private Context activityContext;
    private MMSharedPreferences mmSharedPreferences;
    private DBHelper mDBHelper;

    private SearchView search;
    private TextView companyName, agentcode;
    private ListView tripSheetReturnProductsList;
    private LinearLayout trip_sheet_returns_save, trip_sheet_returns_preview;
    private TripSheetReturnsAdapter mTripSheetReturnsAdapter;
    private String mTripSheetId = "", mAgentId = "", mAgentName = "", mAgentCode = "", mAgentRouteId = "", mAgentRouteCode = "", mAgentSoId = "", mAgentSoCode = "", loggedInUserId;
    private boolean isReturnsDataSaved = false, isReturnsInEditingMode = false;
    private ArrayList<DeliverysBean> allProductsListFromStock, allReturnablesListFromStock = new ArrayList<>();

    private Map<String, DeliverysBean> selectedProductsHashMap;
    private Map<String, String> previouslyReturnedProductsHashMap;
    private boolean isTripSheetClosed = false;

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
            trip_sheet_returns_save = (LinearLayout) findViewById(R.id.trip_sheet_returns_save);
            trip_sheet_returns_preview = (LinearLayout) findViewById(R.id.trip_sheet_returns_preview);

            activityContext = TripsheetReturns.this;
            mmSharedPreferences = new MMSharedPreferences(activityContext);
            mDBHelper = new DBHelper(activityContext);

            agentcode = (TextView) findViewById(R.id.companyId);

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

            agentcode.setText("(" + mAgentCode + ")");

            isTripSheetClosed = mDBHelper.isTripSheetClosed(mTripSheetId);

            if (isTripSheetClosed) {
                trip_sheet_returns_save.setVisibility(View.GONE);
            }

            selectedProductsHashMap = new HashMap<>();
            previouslyReturnedProductsHashMap = new HashMap<>();

            // In order to pre populate when you came back to this screen.
            previouslyReturnedProductsHashMap = mDBHelper.getAgentPreviouslyReturnsProductsList(mTripSheetId, mAgentSoId, mAgentId);
            if (previouslyReturnedProductsHashMap.size() > 0)
                isReturnsInEditingMode = true;

            Map<String, String> deliveredProductsHashMap = mDBHelper.fetchDeliveriesListByTripSheetId(mTripSheetId, mAgentSoId, mAgentId);

            allProductsListFromStock = mDBHelper.fetchAllRecordsFromProductsAndStockTableForDeliverys(mTripSheetId);
            if (allReturnablesListFromStock.size() > 0) {
                allReturnablesListFromStock.clear();
            }
            for (int i = 0; i < allProductsListFromStock.size(); i++) {

                if (allProductsListFromStock.get(i).getProductReturnableUnit().trim() != null) {
                    if (allProductsListFromStock.get(i).getProductReturnableUnit().trim().equals("Y")) {
                        DeliverysBean productsBean = new DeliverysBean();

                        productsBean.setProductId(allProductsListFromStock.get(i).getProductId());
                        productsBean.setProductCode(allProductsListFromStock.get(i).getProductCode());
                        productsBean.setProductTitle(allProductsListFromStock.get(i).getProductTitle());
                        productsBean.setProductAgentPrice(allProductsListFromStock.get(i).getProductAgentPrice());
                        productsBean.setProductConsumerPrice(allProductsListFromStock.get(i).getProductConsumerPrice());
                        productsBean.setProductRetailerPrice(allProductsListFromStock.get(i).getProductRetailerPrice());
                        productsBean.setProductgst(allProductsListFromStock.get(i).getProductgst());
                        productsBean.setProductvat(allProductsListFromStock.get(i).getProductvat());
                        productsBean.setProductOrderedQuantity(allProductsListFromStock.get(i).getProductOrderedQuantity());
                        productsBean.setProductStock(allProductsListFromStock.get(i).getProductStock());
                        productsBean.setProductExtraQuantity(allProductsListFromStock.get(i).getProductExtraQuantity());
                        productsBean.setProductReturnableUnit(allProductsListFromStock.get(i).getProductReturnableUnit());

                        if (allProductsListFromStock.get(i).getProductCode().equals("2600005")) {
                            String due = mDBHelper.fetchCansorCratesDueByIds(mTripSheetId, mAgentSoId, mAgentId, allProductsListFromStock.get(i).getProductCode(), "crates");
                            // CRATES DUE
                            productsBean.setCansDueQuantity(Double.parseDouble("0.0"));
                            productsBean.setCratesDueQuantity(Double.parseDouble(due));
                        } else if (allProductsListFromStock.get(i).getProductCode().equals("2600006")) {
                            String due = mDBHelper.fetchCansorCratesDueByIds(mTripSheetId, mAgentSoId, mAgentId, allProductsListFromStock.get(i).getProductCode(), "cans");
                            // CANS DUE
                            productsBean.setCansDueQuantity(Double.parseDouble(due));
                            productsBean.setCratesDueQuantity(Double.parseDouble("0.0"));
                        } else {
                            productsBean.setCansDueQuantity(Double.parseDouble("0.0"));
                            productsBean.setCratesDueQuantity(Double.parseDouble("0.0"));
                        }

                        allReturnablesListFromStock.add(productsBean);
                    }
                }
            }
            mTripSheetReturnsAdapter = new TripSheetReturnsAdapter(activityContext, this, this, allReturnablesListFromStock, previouslyReturnedProductsHashMap, deliveredProductsHashMap);
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
        menu.findItem(R.id.sort).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // Intent intent=new Intent();
       // intent.putExtra("tripsheetId",mTripSheetId);
        //setResult(101,intent);
       // finish();//finishing activity

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
            Intent i = new Intent(activityContext, TripsheetReturnsPreview.class);
            i.putExtra("tripsheetId", mTripSheetId);
            i.putExtra("agentId", mAgentId);
            i.putExtra("agentCode", mAgentCode);
            i.putExtra("agentName", mAgentName);
            i.putExtra("agentRouteId", mAgentRouteId);
            i.putExtra("agentRouteCode", mAgentRouteCode);
            i.putExtra("agentSoId", mAgentSoId);
            i.putExtra("agentSoCode", mAgentSoCode);
            i.putExtra("data", (Serializable) mTripSheetReturnsAdapter.getData());
            startActivity(i);
            finish();
        } else {
            // Toast.makeText(activityContext, "This Preview is unavailable untill you saved the tripsheet returns data.", Toast.LENGTH_LONG).show();
            CustomAlertDialog.showAlertDialog(activityContext, "Failed", getResources().getString(R.string.returnsfail));
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
        //i.putExtra("From","Delivery");
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

        for (Map.Entry<String, DeliverysBean> deliverysBeanEntry : productsList.entrySet()) {
                   DeliverysBean deliverysBean = deliverysBeanEntry.getValue();
        android.util.Log.i("getSelectedQuantity",""+String.valueOf(deliverysBean.getSelectedQuantity()));
                }
        selectedProductsHashMap = productsList;
        isReturnsDataSaved = false;
    }

    public void saveTripSheetReturnsProductsData() {
        try {
            String returnNumber = "";
            if (selectedProductsHashMap.size() > 0) {
                long currentTimeStamp = System.currentTimeMillis();

                returnNumber = mDBHelper.getTripsheetReturnsMaxOrderNumber(mTripSheetId, mAgentSoId, "first");
                System.out.println("RETURN NUMBER::: " + returnNumber);
                if (returnNumber.length() == 0) {
                    System.out.println("IFFFFFFFFFFFFF");
                    returnNumber = mDBHelper.getTripsheetReturnsMaxOrderNumber(mTripSheetId, mAgentSoId, "second");
                    System.out.println("ELSEEEEEEEEEEEEE11111" + returnNumber);
                    if (returnNumber.length() == 0) {
                        returnNumber = "RR1-" + mAgentCode;
                        System.out.println("ELSEEEEEEEEEEEEE 000000" + returnNumber);
                    } else {
                        String[] ss = returnNumber.split("-");
                        String ss1 = ss[0];
                        System.out.println("ELSEEEEEEEEEE 2222" + ss1);
                        String ss2 = ss1.substring(2, ss1.length());
                        System.out.println("ELSEEEEEEEEEE 333" + ss2);
                        int newCount = Integer.parseInt(ss2) + 1;
                        returnNumber = "RR" + String.valueOf(newCount) + "-" + mAgentCode;
                        System.out.println("ELSEEEEEEEEEE 4444" + returnNumber);
                    }
                } else {
                    System.out.println("ELSEEEEEEEEEEEEE");
                }

                ArrayList<TripSheetReturnsBean> mTripsheetsReturnsList = new ArrayList<>();
                DeliverysBean deliverysBean=null ;

                    TripSheetReturnsBean tripSheetReturnsBean=null;
                for (Map.Entry<String, DeliverysBean> deliverysBeanEntry : selectedProductsHashMap.entrySet()) {
                     deliverysBean = deliverysBeanEntry.getValue();

                     tripSheetReturnsBean = new TripSheetReturnsBean();
                    tripSheetReturnsBean.setmTripshhetReturnsReturn_no("");
                    tripSheetReturnsBean.setmTripshhetReturnsReturn_number(returnNumber);
                    tripSheetReturnsBean.setmTripshhetReturnsTrip_id(mTripSheetId);
                    tripSheetReturnsBean.setmTripshhetReturns_so_id(mAgentSoId);
                    tripSheetReturnsBean.setmTripshhetReturns_so_code(mAgentSoCode);
                    tripSheetReturnsBean.setmTripshhetReturnsUser_id(mAgentId);
                    tripSheetReturnsBean.setmTripshhetReturnsUser_codes(mAgentCode);
                    tripSheetReturnsBean.setmTripshhetReturnsRoute_id(mAgentRouteId);
                    tripSheetReturnsBean.setmTripshhetReturnsRoute_codes(mAgentRouteCode);
                    tripSheetReturnsBean.setmTripshhetReturnsProduct_ids(deliverysBean.getProductId());
                    tripSheetReturnsBean.setmTripshhetReturnsProduct_codes(deliverysBean.getProductCode());
                   android.util.Log.i("bhagya",String.valueOf(deliverysBean.getSelectedQuantity()));
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
              for(int i=0; i<mTripsheetsReturnsList.size(); i++){
                   android.util.Log.i(i+"...","bhagya"+mTripsheetsReturnsList.get(i).getmTripshhetReturnsQuantity());

}

                mDBHelper.insertTripsheetsReturnsListData(mTripsheetsReturnsList);
                isReturnsDataSaved = true;
                //Toast.makeText(activityContext, "Return Products Data Saved Successfully.", Toast.LENGTH_LONG).show();
                showAlertDialog(activityContext, "Success", getResources().getString(R.string.database_details));
                if (new NetworkConnectionDetector(activityContext).isNetworkConnected()) {
                    Intent syncTripSheetDeliveriesServiceIntent = new Intent(activityContext, SyncTripsheetReturnsService.class);
                    startService(syncTripSheetDeliveriesServiceIntent);
                }
            } else {
                //Toast.makeText(activityContext, "Please select at least one product to save.", Toast.LENGTH_LONG).show();
                CustomAlertDialog.showAlertDialog(activityContext, "Failed", getResources().getString(R.string.deliverylimit));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to display alert after success delivery.
     *
     * @param context
     * @param title
     * @param message
     */
    private void showAlertDialog(Context context, String title, String message) {
        try {
            // AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

            android.support.v7.app.AlertDialog alertDialog = null;
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    CustomProgressDialog.hideProgressDialog();

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
            });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
