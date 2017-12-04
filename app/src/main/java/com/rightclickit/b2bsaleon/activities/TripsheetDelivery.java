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
import com.rightclickit.b2bsaleon.adapters.TripSheetDeliveriesAdapter;
import com.rightclickit.b2bsaleon.beanclass.DeliverysBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetDeliveriesBean;
import com.rightclickit.b2bsaleon.customviews.CustomAlertDialog;
import com.rightclickit.b2bsaleon.customviews.CustomProgressDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.TripSheetDeliveriesListener;
import com.rightclickit.b2bsaleon.services.SyncTripsheetDeliveriesService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.Utility;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TripsheetDelivery extends AppCompatActivity implements TripSheetDeliveriesListener {
    private Context activityContext;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;

    private SearchView search;
    private ListView ordered_products_list_view;
    private TextView companyName, totalTaxAmountTextView, totalAmountTextView, subTotalAmountTextView, soCode, agentcode;
    private LinearLayout trip_sheet_deliveries_save, trip_sheet_deliveries_preview, trip_sheet_returns, trip_sheet_payments;

    private TripSheetDeliveriesAdapter mTripSheetDeliveriesAdapter;
    private ArrayList<DeliverysBean> allProductsListFromStock = new ArrayList<>();
    private Map<String, DeliverysBean> selectedDeliveryProductsHashMap;
    private Map<String, String> previouslyDeliveredProductsHashMap,selectedDeliveryProductsHashMapTemp; // this hash map contains previously delivered product quantity. key = product id & value = previously delivered quantity
    private Map<String, String> productOrderQuantitiesHashMap; // this hash map contains product codes & it's order quantity fetched from sale oder table.
    private String mTripSheetId = "", loggedInUserId, mAgentId = "", mAgentName = "", mAgentCode = "", mAgentRouteId = "", mAgentRouteCode = "", mAgentSoId = "", mAgentSoCode = "", mAgentSoDate;
    private double totalAmount = 0, totalTaxAmount = 0, subTotal = 0;
    private boolean isDeliveryDataSaved = false, isDeliveryInEditingMode = false, isTripSheetClosed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripsheet_delivery);

        try {
            this.getSupportActionBar().setTitle("DELIVERIES");
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
            soCode = (TextView) findViewById(R.id.soCode);
            agentcode = (TextView) findViewById(R.id.companyId);
            ordered_products_list_view = (ListView) findViewById(R.id.ordered_products_list_view);
            totalTaxAmountTextView = (TextView) findViewById(R.id.delivery_total_tax_amount);
            totalAmountTextView = (TextView) findViewById(R.id.delivery_total_amount);
            subTotalAmountTextView = (TextView) findViewById(R.id.delivery_sub_total_amount);
            trip_sheet_deliveries_save = (LinearLayout) findViewById(R.id.trip_sheet_deliveries_save);
            trip_sheet_deliveries_preview = (LinearLayout) findViewById(R.id.trip_sheet_deliveries_preview);
            trip_sheet_returns = (LinearLayout) findViewById(R.id.trip_sheet_returns);
            trip_sheet_returns.setVisibility(View.GONE);
            trip_sheet_payments = (LinearLayout) findViewById(R.id.trip_sheet_payments);
            trip_sheet_payments.setVisibility(View.GONE);
            activityContext = TripsheetDelivery.this;
            mDBHelper = new DBHelper(activityContext);
            mPreferences = new MMSharedPreferences(activityContext);

            mTripSheetId = this.getIntent().getStringExtra("tripsheetId");
            mAgentId = this.getIntent().getStringExtra("agentId");
            mAgentCode = this.getIntent().getStringExtra("agentCode");
            mAgentName = this.getIntent().getStringExtra("agentName");
            mAgentSoId = this.getIntent().getStringExtra("agentSoId");
            mAgentSoCode = this.getIntent().getStringExtra("agentSoCode");
            mAgentSoDate = this.getIntent().getStringExtra("agentSoDate");
            loggedInUserId = mPreferences.getString("userId");

            soCode.setText(mAgentSoCode);
            agentcode.setText("(" + mAgentCode + ")");

            if (mAgentId != null && mAgentId != "") {
                List<String> agentRouteIds = mDBHelper.getAgentRouteId(mAgentId);
                if (mAgentRouteId != null && agentRouteIds.size() > 0) {
                    mAgentRouteId = agentRouteIds.get(0);
                    mAgentRouteCode = mDBHelper.getRouteCodeByRouteId(mAgentRouteId);
                }
            }
            companyName.setText(mAgentName);

            ArrayList<String> privilegeActionsData = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("TripSheets"));

            for (int z = 0; z < privilegeActionsData.size(); z++) {
                if (privilegeActionsData.get(z).toString().equals("list_view_return")) {
                    trip_sheet_returns.setVisibility(View.VISIBLE);
                } else if (privilegeActionsData.get(z).toString().equals("list_view_payment")) {
                    trip_sheet_payments.setVisibility(View.VISIBLE);
                }
            }

            isTripSheetClosed = mDBHelper.isTripSheetClosed(mTripSheetId);

            if (isTripSheetClosed) {
                trip_sheet_deliveries_save.setVisibility(View.GONE);
            }

            selectedDeliveryProductsHashMap = new HashMap<>();
            previouslyDeliveredProductsHashMap = new HashMap<>();
            productOrderQuantitiesHashMap = new HashMap<>();
            selectedDeliveryProductsHashMapTemp = new HashMap<>();

            allProductsListFromStock = mDBHelper.fetchAllRecordsFromProductsAndStockTableForDeliverys(mTripSheetId);

            // In order to pre populate when you came back to this screen.
            previouslyDeliveredProductsHashMap = mDBHelper.getAgentPreviouslyDeliveredProductsList(mTripSheetId, mAgentSoId, mAgentId);
            if (previouslyDeliveredProductsHashMap.size() > 0)
                isDeliveryInEditingMode = true;

            ArrayList<String> productOrderQuantities = mDBHelper.getAgentOrderedProductsQuantityFromSaleOrderTable(mTripSheetId, mAgentSoId, mAgentId);
            if (productOrderQuantities.size() > 0) {
                JSONArray productCodes = new JSONArray(productOrderQuantities.get(0));
                JSONArray orderQuantities = new JSONArray(productOrderQuantities.get(1));

                for (int i = 0; i < productCodes.length(); i++) {
                    productOrderQuantitiesHashMap.put(productCodes.get(i).toString(), orderQuantities.get(i).toString());
                }
            }

            // fetching & checking weather Agent have any special prices.
            Map<String, String> agentSpecialPricesHashMap = mDBHelper.fetchSpecialPricesForUserId(mAgentId);

            for (DeliverysBean deliverysBean : allProductsListFromStock) {
                if (agentSpecialPricesHashMap.containsKey(deliverysBean.getProductId()))
                    deliverysBean.setProductAgentPrice(agentSpecialPricesHashMap.get(deliverysBean.getProductId()));
            }

            mTripSheetDeliveriesAdapter = new TripSheetDeliveriesAdapter(activityContext, this, this, allProductsListFromStock, previouslyDeliveredProductsHashMap, productOrderQuantitiesHashMap);
            ordered_products_list_view.setAdapter(mTripSheetDeliveriesAdapter);

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
                    mTripSheetDeliveriesAdapter.filter(query);
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
        menu.findItem(R.id.sort).setVisible(true);
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

    public void saveTripSheetDeliveries(View v) {
        showAlertDialogWithCancelButton(activityContext, "User Action!", "Do you want to save data?");
    }

    public void showTripSheetDeliveriesPreview(View v) {
        if (isDeliveryDataSaved || isDeliveryInEditingMode) {
            Intent i = new Intent(activityContext, TripsheetDeliveryPreview.class);
            i.putExtra("tripsheetId", mTripSheetId);
            i.putExtra("agentId", mAgentId);
            i.putExtra("agentCode", mAgentCode);
            i.putExtra("agentName", mAgentName);
            i.putExtra("agentRouteId", mAgentRouteId);
            i.putExtra("agentRouteCode", mAgentRouteCode);
            i.putExtra("agentSoId", mAgentSoId);
            i.putExtra("agentSoCode", mAgentSoCode);
            i.putExtra("agentSoDate", mAgentSoDate);
            i.putExtra("data", (Serializable) mTripSheetDeliveriesAdapter.getData());
            i.putExtra("totalAmount", String.valueOf(totalAmount));
            i.putExtra("totalTaxAmount", String.valueOf(totalTaxAmount));
            i.putExtra("subTotal", String.valueOf(subTotal));
            startActivity(i);
            finish();
        } else {
            //  Toast.makeText(activityContext, "This Preview is unavailable untill the tripsheet delivery is saved.", Toast.LENGTH_LONG).show();
            CustomAlertDialog.showAlertDialog(activityContext, "Failed", getResources().getString(R.string.deliveryfail));
        }
    }

    public void openTripSheetReturns(View v) {
        Intent i = new Intent(activityContext, TripsheetReturns.class);
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
        Intent i = new Intent(TripsheetDelivery.this, TripsheetPayments.class);
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
                    saveTripSheetDeliveryProductsData();
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
    public void updateDeliveryProductsList(Map<String, DeliverysBean> deliveryProductsList) {
        selectedDeliveryProductsHashMap = deliveryProductsList;

        totalAmount = 0;
        totalTaxAmount = 0;
        subTotal = 0;

        for (Map.Entry<String, DeliverysBean> deliverysBeanEntry : selectedDeliveryProductsHashMap.entrySet()) {
            DeliverysBean deliverysBean = deliverysBeanEntry.getValue();

            totalAmount = totalAmount + deliverysBean.getProductAmount();
            totalTaxAmount = totalTaxAmount + deliverysBean.getTaxAmount();
            subTotal = totalAmount + totalTaxAmount;
        }

        totalTaxAmountTextView.setText(Utility.getFormattedCurrency(totalTaxAmount));
        totalAmountTextView.setText(Utility.getFormattedCurrency(totalAmount));
        subTotalAmountTextView.setText(Utility.getFormattedCurrency(subTotal));
        isDeliveryDataSaved = false;
    }

    @Override
    public void updateDeliveryProductsListTemp(Map<String, String> deliveryProductsListTemp) {
        selectedDeliveryProductsHashMapTemp = deliveryProductsListTemp;
    }

    public boolean validateTripSheetDeliveryData() {
        boolean isValid = true;

        for (Map.Entry<String, DeliverysBean> deliverysBeanEntry : selectedDeliveryProductsHashMap.entrySet()) {
            DeliverysBean deliverysBean = deliverysBeanEntry.getValue();

            double productAvailableStock;

            if (isDeliveryInEditingMode)
                productAvailableStock = deliverysBean.getProductStock() + deliverysBean.getProductExtraQuantity() + deliverysBean.getProductOrderedQuantity();
            else
                productAvailableStock = deliverysBean.getProductStock() + deliverysBean.getProductExtraQuantity();

            if (deliverysBean.getSelectedQuantity() > productAvailableStock) {
                isValid = false;
                break;
            }
        }

        return isValid;
    }

    public void saveTripSheetDeliveryProductsData() {
        try {
            if (selectedDeliveryProductsHashMap.size() > 0) {
                if (validateTripSheetDeliveryData()) {
                    long currentTimeStamp = System.currentTimeMillis();

                    ArrayList<TripSheetDeliveriesBean> mTripsheetsDeliveriesList = new ArrayList<>();

                    for (Map.Entry<String, DeliverysBean> deliverysBeanEntry : selectedDeliveryProductsHashMap.entrySet()) {
                        DeliverysBean deliverysBean = deliverysBeanEntry.getValue();

                        double remainingInStock, remainingExtraStock, totalAvailableStock;

                        if (isDeliveryInEditingMode)
                            totalAvailableStock = deliverysBean.getProductStock() + deliverysBean.getProductExtraQuantity() + deliverysBean.getProductOrderedQuantity();
                        else
                            totalAvailableStock = deliverysBean.getProductStock() + deliverysBean.getProductExtraQuantity();

                        if (deliverysBean.getSelectedQuantity() > deliverysBean.getProductStock()) {
                            remainingInStock = 0;
                            remainingExtraStock = totalAvailableStock - deliverysBean.getSelectedQuantity();
                        } else {
                            //System.out.println("PREVIOUS QUAN::: " + previouslyDeliveredProductsHashMap.get(deliverysBean.getProductId()));
                            //System.out.println("SELECTED QUAN::: " + deliverysBean.getSelectedQuantity());
                            //System.out.println("PROD STOCK::: " + deliverysBean.getProductStock());
                            //System.out.println("ACTUAL IN STOCK QUA::: " + deliverysBean.getProductStock());
                            if (previouslyDeliveredProductsHashMap.get(deliverysBean.getProductId()) != null) {
                                Double pres = Double.parseDouble(previouslyDeliveredProductsHashMap.get(deliverysBean.getProductId()));
                                Double selcnew = deliverysBean.getSelectedQuantity();
                                Double finalA = selcnew - pres;
                                remainingInStock = deliverysBean.getProductStock() - finalA;
                            } else {
                                remainingInStock = deliverysBean.getProductStock() - deliverysBean.getSelectedQuantity();
                            }

                            //remainingInStock = deliverysBean.getProductStock() - deliverysBean.getSelectedQuantity();
                            remainingExtraStock = deliverysBean.getProductExtraQuantity();
                        }
                        if (selectedDeliveryProductsHashMapTemp.get(deliverysBean.getProductCode()) != null) {
                            // Quantity is available....
                            TripSheetDeliveriesBean tripSheetDeliveriesBean = new TripSheetDeliveriesBean();
                            tripSheetDeliveriesBean.setmTripsheetDeliveryNo("");
                            tripSheetDeliveriesBean.setmTripsheetDelivery_tripId(mTripSheetId);
                            tripSheetDeliveriesBean.setmTripsheetDelivery_so_id(mAgentSoId);
                            tripSheetDeliveriesBean.setmTripsheetDelivery_so_code(mAgentSoCode);
                            tripSheetDeliveriesBean.setmTripsheetDelivery_userId(mAgentId);
                            tripSheetDeliveriesBean.setmTripsheetDelivery_userCodes(mAgentCode);
                            tripSheetDeliveriesBean.setmTripsheetDelivery_routeId(mAgentRouteId);
                            tripSheetDeliveriesBean.setmTripsheetDelivery_routeCodes(mAgentRouteCode);
                            tripSheetDeliveriesBean.setmTripsheetDelivery_productId(deliverysBean.getProductId());
                            tripSheetDeliveriesBean.setmTripsheetDelivery_productCodes(deliverysBean.getProductCode());
                            tripSheetDeliveriesBean.setmTripsheetDelivery_TaxPercent(String.valueOf(deliverysBean.getProductTaxPerUnit()));
                            tripSheetDeliveriesBean.setmTripsheetDelivery_UnitPrice(String.valueOf(deliverysBean.getProductRatePerUnit()));
                            tripSheetDeliveriesBean.setmTripsheetDelivery_Quantity(String.valueOf(deliverysBean.getSelectedQuantity()));
                            tripSheetDeliveriesBean.setmTripsheetDelivery_Amount(String.valueOf(deliverysBean.getProductAmount()));
                            tripSheetDeliveriesBean.setmTripsheetDelivery_TaxAmount(String.valueOf(deliverysBean.getTaxAmount()));
                            tripSheetDeliveriesBean.setmTripsheetDelivery_TaxTotal(String.valueOf(totalTaxAmount));
                            tripSheetDeliveriesBean.setmTripsheetDelivery_SaleValue(String.valueOf(subTotal));
                            tripSheetDeliveriesBean.setmTripsheetDelivery_Status("A");
                            tripSheetDeliveriesBean.setmTripsheetDelivery_Delete("N");
                            tripSheetDeliveriesBean.setmTripsheetDelivery_CreatedBy(loggedInUserId);
                            tripSheetDeliveriesBean.setmTripsheetDelivery_CreatedOn(String.valueOf(currentTimeStamp));
                            tripSheetDeliveriesBean.setmTripsheetDelivery_UpdatedBy(loggedInUserId);
                            tripSheetDeliveriesBean.setmTripsheetDelivery_UpdatedOn(String.valueOf(currentTimeStamp));
                            tripSheetDeliveriesBean.setProductRemainingInStock(String.valueOf(remainingInStock));
                            tripSheetDeliveriesBean.setProductRemainingExtraStock(String.valueOf(remainingExtraStock));

                            mTripsheetsDeliveriesList.add(tripSheetDeliveriesBean);
                        }
                    }

                    mDBHelper.insertTripsheetsDeliveriesListData(mTripsheetsDeliveriesList);
                    isDeliveryDataSaved = true;
                    // Toast.makeText(activityContext, "Delivery Data Saved Successfully.", Toast.LENGTH_LONG).show();
                    showAlertDialog(activityContext, "Success", getResources().getString(R.string.database_details));
                    Utility.isDeliveryFirstTime = true;
                    if (new NetworkConnectionDetector(activityContext).isNetworkConnected()) {
                        Intent syncTripSheetDeliveriesServiceIntent = new Intent(activityContext, SyncTripsheetDeliveriesService.class);
                        startService(syncTripSheetDeliveriesServiceIntent);
                    }
                } else {
                    // Toast.makeText(activityContext, "Delivery quantity for one of the product exceeds available stock, please check it. ", Toast.LENGTH_LONG).show();
                    CustomAlertDialog.showAlertDialog(activityContext, "Failed", getResources().getString(R.string.deliveryexceed));
                }
            } else {
                // Toast.makeText(activityContext, "Please select at least one product to deliver.", Toast.LENGTH_LONG).show();
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

                    Intent i = new Intent(activityContext, TripsheetReturns.class);
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
