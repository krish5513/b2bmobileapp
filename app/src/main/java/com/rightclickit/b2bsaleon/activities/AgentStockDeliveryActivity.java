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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.AgentStockDeliveryAdapter;
import com.rightclickit.b2bsaleon.beanclass.AgentsStockBean;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetDeliveriesBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.AgentStockListener;
import com.rightclickit.b2bsaleon.models.AgentsStockModel;
import com.rightclickit.b2bsaleon.services.SyncAgentStockDeliveriesService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sekhar Kuppa.
 */

public class AgentStockDeliveryActivity extends AppCompatActivity implements AgentStockListener {
    private DBHelper mDBHelper;
    private MMSharedPreferences sharedPreferences;
    private AgentsStockModel mAgentStockModel;
    private ListView mStockList;
    private AgentStockDeliveryAdapter mStockAdapter;
    private Context activityContext;
    private String mAgentId = "", mAgentCode = "", mAgentName = "", mAgentRouteId = "", mAgentRouteCode = "", loggedInUserId = "";
    private SearchView search;
    TextView stock_delivery_print, stock_delivery_save;
    private ArrayList<ProductsBean> allProductsList;
    private Map<String, String> selectedProductsStockListHashMap;
    private Map<String, String> selectedProductsStockListHashMap1;
    private Map<String, ProductsBean> selectedProductsStockListHashMapProducts;
    private ArrayList<String> productIdsList = new ArrayList<String>();
    private ArrayList<String> productValuesList = new ArrayList<String>();
    private ArrayList<String> productCodesList = new ArrayList<String>();
    private ArrayList<String> productNamesList = new ArrayList<String>();
    private ArrayList<String> productUOMsList = new ArrayList<String>();
    private ArrayList<AgentsStockBean> agentsStockBeanList = new ArrayList<AgentsStockBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAgentId = this.getIntent().getStringExtra("agentId");
        mAgentCode = this.getIntent().getStringExtra("agentCode");
        mAgentName = this.getIntent().getStringExtra("agentName");
        System.out.println("AGENT ID ======= " + mAgentId + "\n");
        System.out.println("AGENT CODE ======= " + mAgentCode + "\n");
        System.out.println("AGENT NAME ======= " + mAgentName + "\n");

        setContentView(R.layout.activity_agent_stock_delivery);

        this.getSupportActionBar().setTitle("STOCK UPDATE");
        this.getSupportActionBar().setSubtitle(null);
        this.getSupportActionBar().setLogo(R.drawable.ic_shopping_cart_white_24dp);
        // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);


        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        mDBHelper = new DBHelper(AgentStockDeliveryActivity.this);
        sharedPreferences = new MMSharedPreferences(AgentStockDeliveryActivity.this);
        selectedProductsStockListHashMap = new HashMap<String, String>();
        selectedProductsStockListHashMap1 = new HashMap<String, String>();
        selectedProductsStockListHashMapProducts = new HashMap<String, ProductsBean>();

        allProductsList = new ArrayList<>();

        this.activityContext = AgentStockDeliveryActivity.this;

        mStockList = (ListView) findViewById(R.id.agent_stock_delivery_list);

        stock_delivery_save = (TextView) findViewById(R.id.stock_delivery_save);

        if (mAgentId != null && mAgentId != "") {
            List<String> agentRouteIds = mDBHelper.getAgentRouteId(mAgentId);
            if (mAgentRouteId != null && agentRouteIds.size() > 0) {
                mAgentRouteId = agentRouteIds.get(0);
                mAgentRouteCode = mDBHelper.getRouteCodeByRouteId(mAgentRouteId);
            }
        }
        loggedInUserId = sharedPreferences.getString("userId");

        allProductsList = mDBHelper.fetchAllRecordsFromProductsTable();

        if (allProductsList.size() > 0) {
            if (mStockAdapter != null) {
                mStockAdapter = null;
            }
            mStockAdapter = new AgentStockDeliveryAdapter(this, AgentStockDeliveryActivity.this, this, allProductsList);
            mStockList.setAdapter(mStockAdapter);
        }

        stock_delivery_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("IN 0000000000::: " + selectedProductsStockListHashMap.size());
                if (selectedProductsStockListHashMap.size() > 0) {
                    showAddTripDetailsDialog();
                } else {
                    Toast.makeText(activityContext, "Please select at least one product quantity.", Toast.LENGTH_LONG).show();
                }
            }
        });
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
                if (query.length() > 0) {
                    mStockAdapter.filter(query);
                }
                return true;
            }
        });

        // Get the search close button image view
        ImageView closeButton = (ImageView) search.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setQuery("", false);
                mStockAdapter.filter("");
                search.clearFocus();
                search.onActionViewCollapsed();
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {

            return true;
        }
        if (id == R.id.autorenew) {

            if (new NetworkConnectionDetector(AgentStockDeliveryActivity.this).isNetworkConnected()) {
                Intent syncTripSheetDeliveriesServiceIntent = new Intent(activityContext, SyncAgentStockDeliveriesService.class);
                startService(syncTripSheetDeliveriesServiceIntent);
            } else {
                new NetworkConnectionDetector(AgentStockDeliveryActivity.this).displayNoNetworkError(AgentStockDeliveryActivity.this);
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
    public void updateSelectedProductsList(Map<String, String> selectedProductsList) {
        this.selectedProductsStockListHashMap = selectedProductsList;
    }

    @Override
    public void updateDeliveryProductsList(Map<String, ProductsBean> deliveryProductsList) {
        this.selectedProductsStockListHashMapProducts = deliveryProductsList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, AgentStockActivity.class);
        intent.putExtra("agentId", mAgentId);
        intent.putExtra("agentCode", mAgentCode);
        intent.putExtra("agentName", mAgentName);
        startActivity(intent);
        finish();
    }

    protected void showAddTripDetailsDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(AgentStockDeliveryActivity.this);
        View promptView = layoutInflater.inflate(R.layout.agent_stock_tripso_adddialog, null);
        final EditText tripshhetNumber = (EditText) promptView.findViewById(R.id.tripsheet_number);
        final EditText soId = (EditText) promptView.findViewById(R.id.sale_order_id);
        final EditText soNumber = (EditText) promptView.findViewById(R.id.sale_order_number);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AgentStockDeliveryActivity.this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(android.R.string.ok, null);
        alertDialogBuilder.setNegativeButton(android.R.string.cancel, null);

        final AlertDialog alertDialog = alertDialogBuilder.create();// create an alert dialog
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String tripSheetStr = tripshhetNumber.getText().toString().trim();
                        String soIdStr = soId.getText().toString().trim();
                        String soNumStr = soNumber.getText().toString().trim();
                        saveQuantityToLocalAgentsStockDB(tripSheetStr, soIdStr, soNumStr);
                        alertDialog.dismiss();
//                        boolean cancel = false;
//                        View focusView = null;
//
//                        if (tripSheetStr.isEmpty()) {
//                            tripshhetNumber.setError("Please enter tripsheet number.");
//                            focusView = tripshhetNumber;
//                            cancel = true;
//                        } else if (soIdStr.isEmpty()) {
//                            soId.setError("Please enter sale order id.");
//                            focusView = soId;
//                            cancel = true;
//                        } else if (soNumStr.isEmpty()) {
//                            soNumber.setError("Please enter sale order number.");
//                            focusView = soNumber;
//                            cancel = true;
//                        }
//
//                        if (cancel) {
//                            focusView.requestFocus();
//                        } else {
//                            alertDialog.dismiss();
//
//                            saveQuantityToLocalAgentsStockDB(tripSheetStr, soIdStr, soNumStr);
//                        }
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void saveQuantityToLocalAgentsStockDB(String tripSheetStr, String soIdStr, String soNumStr) {
        long currentTimeStamp = System.currentTimeMillis();
        ArrayList<TripSheetDeliveriesBean> mAgentStockDeliveriesList = new ArrayList<>();
        if (tripSheetStr.length() == 0) {
            tripSheetStr = "";
        }
        if (soIdStr.length() == 0) {
            soIdStr = "";
        }
        if (soNumStr.length() == 0) {
            soNumStr = "";
        }
        if (selectedProductsStockListHashMap.size() > 0) {
            synchronized (this) {
                for (Map.Entry<String, String> productsBeanEntry : selectedProductsStockListHashMap.entrySet()) {
                    productIdsList.add(productsBeanEntry.getKey());
                    productValuesList.add(productsBeanEntry.getValue());
                    selectedProductsStockListHashMap1.put(productsBeanEntry.getKey(), productsBeanEntry.getValue());
                }
            }
            synchronized (this) {
                mDBHelper.updateAgentStockAfterAgentDeliverFromStock(mAgentId, productIdsList, productValuesList);
            }

            synchronized (this) {
                if (selectedProductsStockListHashMapProducts.size() > 0) {
                    for (Map.Entry<String, ProductsBean> productsBeanEntry : selectedProductsStockListHashMapProducts.entrySet()) {
                        String key = productsBeanEntry.getKey();
                        ProductsBean deliverysBean = productsBeanEntry.getValue();
                        if (selectedProductsStockListHashMap1.get(key) != null) {
                            TripSheetDeliveriesBean tripSheetDeliveriesBean = new TripSheetDeliveriesBean();
                            tripSheetDeliveriesBean.setmTripsheetDeliveryNo("");
                            tripSheetDeliveriesBean.setmTripsheetDelivery_tripId(tripSheetStr);
                            tripSheetDeliveriesBean.setmTripsheetDelivery_so_id(soIdStr);
                            tripSheetDeliveriesBean.setmTripsheetDelivery_so_code(soNumStr);
                            tripSheetDeliveriesBean.setmTripsheetDelivery_userId(mAgentId);
                            tripSheetDeliveriesBean.setmTripsheetDelivery_userCodes(mAgentCode);
                            tripSheetDeliveriesBean.setmTripsheetDelivery_routeId(mAgentRouteId);
                            tripSheetDeliveriesBean.setmTripsheetDelivery_routeCodes(mAgentRouteCode);
                            tripSheetDeliveriesBean.setmTripsheetDelivery_productId(deliverysBean.getProductId());
                            tripSheetDeliveriesBean.setmTripsheetDelivery_productCodes(deliverysBean.getProductCode());
                            tripSheetDeliveriesBean.setmTripsheetDelivery_TaxPercent(String.valueOf(deliverysBean.getProductTaxPerUnit()));
                            tripSheetDeliveriesBean.setmTripsheetDelivery_UnitPrice(String.valueOf(deliverysBean.getProductRatePerUnit()));
                            tripSheetDeliveriesBean.setmTripsheetDelivery_Quantity(selectedProductsStockListHashMap1.get(key));
                            tripSheetDeliveriesBean.setmTripsheetDelivery_Amount(String.valueOf(deliverysBean.getProductAmount()));
                            tripSheetDeliveriesBean.setmTripsheetDelivery_TaxAmount(String.valueOf(deliverysBean.getTaxAmount()));
                            tripSheetDeliveriesBean.setmTripsheetDelivery_TaxTotal("0.0");
                            Double unitPrice = deliverysBean.getProductRatePerUnit();
                            Double qunatity = Double.parseDouble(selectedProductsStockListHashMap1.get(key));
                            Double saleVal = qunatity * unitPrice;
                            //System.out.println("SALE VAL::: " + saleVal);
                            tripSheetDeliveriesBean.setmTripsheetDelivery_SaleValue(String.valueOf(saleVal));
                            tripSheetDeliveriesBean.setmTripsheetDelivery_Status("A");
                            tripSheetDeliveriesBean.setmTripsheetDelivery_Delete("N");
                            tripSheetDeliveriesBean.setmTripsheetDelivery_CreatedBy(loggedInUserId);
                            tripSheetDeliveriesBean.setmTripsheetDelivery_CreatedOn(String.valueOf(currentTimeStamp));
                            tripSheetDeliveriesBean.setmTripsheetDelivery_UpdatedBy(loggedInUserId);
                            tripSheetDeliveriesBean.setmTripsheetDelivery_UpdatedOn(String.valueOf(currentTimeStamp));

                            mAgentStockDeliveriesList.add(tripSheetDeliveriesBean);
                        }
                    }
                }
                //System.out.println("Agent Stock API INPUT IS::: " + mAgentStockDeliveriesList.size());
                if (mAgentStockDeliveriesList.size() > 0) {
                    mDBHelper.insertAgentStockDeliveriesListData(mAgentStockDeliveriesList);
                }

                if (new NetworkConnectionDetector(activityContext).isNetworkConnected()) {
                    Intent syncTripSheetDeliveriesServiceIntent = new Intent(activityContext, SyncAgentStockDeliveriesService.class);
                    startService(syncTripSheetDeliveriesServiceIntent);
                }
            }
            showAlertDialogToGoBack();
        }
    }

    protected void showAlertDialogToGoBack() {
        try {
            AlertDialog alertDialog = null;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AgentStockDeliveryActivity.this, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle("User Action!");
            alertDialogBuilder.setMessage("Stock details updated successfully..");
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(AgentStockDeliveryActivity.this, AgentStockActivity.class);
                    intent.putExtra("agentId", mAgentId);
                    intent.putExtra("agentCode", mAgentCode);
                    intent.putExtra("agentName", mAgentName);
                    startActivity(intent);
                    finish();
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
                cancelButton.setTextColor(ContextCompat.getColor(AgentStockDeliveryActivity.this, R.color.alert_dialog_color_accent));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
