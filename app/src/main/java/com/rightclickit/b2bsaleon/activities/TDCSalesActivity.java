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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TDCSalesAdapter;
import com.rightclickit.b2bsaleon.beanclass.AgentsStockBean;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.customviews.CustomAlertDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.TDCSalesListener;
import com.rightclickit.b2bsaleon.services.SyncNotificationsListService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class TDCSalesActivity extends AppCompatActivity implements TDCSalesListener {
    private Context applicationContext, activityContext;
    private MMSharedPreferences mmSharedPreferences;

    private SearchView search;
    private ListView tdc_products_list_view;
    private TextView tdc_sales_list, tdc_sales_preview, totalTaxAmountTextView, totalAmountTextView, subTotalAmountTextView;

    private DBHelper mDBHelper;
    private ArrayList<ProductsBean> allProductsList, allProductsListSort;
    private Map<String, ProductsBean> selectedProductsListHashMap, previouslySelectedProductsListHashMap; // Hash Map Key = Product Id
    private TDCSalesAdapter tdcSalesAdapter;
    private boolean showProductsListView = false;
    private double totalAmount = 0, totalTaxAmount = 0, subTotal = 0;
    private TDCSaleOrder currentOrder;
    private String userId = "", mNotifications = "", mTdcHomeScreen = "", mTripsHomeScreen = " ", mAgentsHomeScreen = "",
            mRetailersHomeScreen = "", mDashboardHomeScreen = "", saleQuantity = "";
    ArrayList<AgentsStockBean> stockBeanArrayList;
    //ArrayList<String> availableStockProductsList;
    HashMap<String, String> availableStockProductsList = new HashMap<String, String>();
    HashMap<String, String> availableStockProductsListTemp = new HashMap<String, String>();
    private Map<String, String> selectedProductsStockListHashMap = new HashMap<String, String>();
    private Map<String, String> previousselectedProductsStockListHashMap;

    private boolean isAscendingSort;
    String str_selectedretailername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        try {
            applicationContext = getApplicationContext();
            activityContext = TDCSalesActivity.this;
            mmSharedPreferences = new MMSharedPreferences(activityContext);
            mDBHelper = new DBHelper(activityContext);

            this.getSupportActionBar().setTitle("COUNTER SALES");
            this.getSupportActionBar().setSubtitle(null);
            this.getSupportActionBar().setLogo(R.drawable.sales_white);
            // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            this.getSupportActionBar().setDisplayUseLogoEnabled(true);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            this.getSupportActionBar().setDisplayShowHomeEnabled(true);

            final ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            userId = mmSharedPreferences.getString("userId");


            //Log.e(allProductsList.size())


            ArrayList<String> privilegeActionsData1 = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mmSharedPreferences.getString("UserActivity"));
            //System.out.println("F 11111 ***COUNT === " + privilegeActionsData1.size());
            for (int z = 0; z < privilegeActionsData1.size(); z++) {
                //System.out.println("Name::: " + privilegeActionsData1.get(z).toString());
                if (privilegeActionsData1.get(z).toString().equals("Notification")) {
                    mNotifications = privilegeActionsData1.get(z).toString();
                } else if (privilegeActionsData1.get(z).toString().equals("tdc_home_screen")) {
                    mTdcHomeScreen = privilegeActionsData1.get(z).toString();
                } else if (privilegeActionsData1.get(z).toString().equals("Trips@Home")) {
                    mTripsHomeScreen = privilegeActionsData1.get(z).toString();
                } else if (privilegeActionsData1.get(z).toString().equals("Agents@Home")) {
                    mAgentsHomeScreen = privilegeActionsData1.get(z).toString();
                } else if (privilegeActionsData1.get(z).toString().equals("Retailers@Home")) {
                    mRetailersHomeScreen = privilegeActionsData1.get(z).toString();
                } else if (privilegeActionsData1.get(z).toString().equals("Dashboard@Home")) {
                    mDashboardHomeScreen = privilegeActionsData1.get(z).toString();
                }
            }

            ArrayList<String> privilegeActionsData = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mmSharedPreferences.getString("TDC"));

            tdc_products_list_view = (ListView) findViewById(R.id.tdc_products_list_view);
            tdc_sales_list = (TextView) findViewById(R.id.tdc_sales_list);
            tdc_sales_preview = (TextView) findViewById(R.id.tdc_sales_preview);
            totalTaxAmountTextView = (TextView) findViewById(R.id.totalTaxAmount);
            totalAmountTextView = (TextView) findViewById(R.id.totalAmount);
            subTotalAmountTextView = (TextView) findViewById(R.id.subTotalAmount);

            if (privilegeActionsData.contains("List_View")) {
                tdc_products_list_view.setVisibility(View.VISIBLE);
                showProductsListView = true;
            } else {
                tdc_products_list_view.setVisibility(View.GONE);
                showProductsListView = false;
            }

            if (privilegeActionsData.contains("Sales_List")) {
                tdc_sales_list.setVisibility(View.VISIBLE);
            } else {
                tdc_sales_list.setVisibility(View.GONE);
            }

            allProductsList = new ArrayList<>();
            allProductsListSort = new ArrayList<>();
            selectedProductsListHashMap = new HashMap<>();
            previouslySelectedProductsListHashMap = new HashMap<>();
            previousselectedProductsStockListHashMap = new HashMap<String, String>();
            currentOrder = new TDCSaleOrder();

            // when you came back to this activity when you want to change your order, we are pre populating with previously selected values.
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {


                currentOrder = (TDCSaleOrder) bundle.getSerializable(Constants.BUNDLE_TDC_SALE_CURRENT_ORDER);
                saleQuantity = bundle.getString(Constants.BUNDLE_TDC_SALE_CURRENT_SALEQUNATITY);
                //System.out.println("TDC ACT SALE QUA==:: " + saleQuantity);
                // Getting previously selected products list to update on UI
                previouslySelectedProductsListHashMap = currentOrder.getProductsList();
                if (previouslySelectedProductsListHashMap == null) {
                    previouslySelectedProductsListHashMap = new HashMap<>();
                } else {
                    updateSelectedProductsListAndSubTotal(previouslySelectedProductsListHashMap);
                }

                // Getting previously selected stock products list to update on UI

                if (saleQuantity.length() > 0) {
                    saleQuantity = saleQuantity.substring(1, saleQuantity.length() - 1);           //remove curly brackets
                    String[] keyValuePairs = saleQuantity.split(",");              //split the string to creat key-value pairs
                    if (previousselectedProductsStockListHashMap == null) {
                        previousselectedProductsStockListHashMap = new HashMap<>();
                    } else {
                        for (String pair : keyValuePairs)                        //iterate over the pairs
                        {
                            String[] entry = pair.split("=");                   //split the pairs to get key and value
                            previousselectedProductsStockListHashMap.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
                        }
                        updateAgentStockSaleQuantityAfterTDCSale(previousselectedProductsStockListHashMap);
                    }
                }
            }

            allProductsList = mDBHelper.fetchAllRecordsFromProductsTable();
            System.out.println("ALL PRODs LIST:: " + allProductsList.size());
            stockBeanArrayList = mDBHelper.fetchAllStockByAgentId(userId);
            //availableStockProductsList = new ArrayList<String>();
            //availableStockProductsListTemp = new ArrayList<String>();
            if (availableStockProductsList.size() > 0) {
                availableStockProductsList.clear();
            }
            if (availableStockProductsListTemp.size() > 0) {
                availableStockProductsListTemp.clear();
            }

            if (allProductsList.size() > 0) {
                for (int h = 0; h < allProductsList.size(); h++) {
                    // Check condition for product id match
                    for (int g = 0; g < stockBeanArrayList.size(); g++) {
                        if (allProductsList.get(h).getProductId().equals(stockBeanArrayList.get(g).getmProductId())) {
                            // If match add this cb quantity to temp string array
                            availableStockProductsListTemp.put(
                                    stockBeanArrayList.get(g).getmProductId(), stockBeanArrayList.get(g).getmProductCBQuantity());
                            availableStockProductsList.put(
                                    stockBeanArrayList.get(g).getmProductId(), stockBeanArrayList.get(g).getmProductCBQuantity());
                        }/*else {
                            // If not match add "0" to temp string array
                            availableStockProductsList.add("0");
                        }*/
                    }
                }

                // Make the params array
                //for (int v = 0; v < allProductsList.size(); v++) {
//                if (availableStockProductsListTemp.size() > 0) {
//                    for (int v = 0; v < availableStockProductsListTemp.size(); v++) {
//                        //if (v < availableStockProductsListTemp.size()) {
//                            if (availableStockProductsListTemp.get(allProductsList.get(v).getProductId()) != null) {
//                                availableStockProductsList.put(
//                                        allProductsList.get(v).getProductId(), availableStockProductsListTemp.get(allProductsList.get(v).getProductId()));
//                            } else {
//                                //availableStockProductsList.put(allProductsList.get(v).getProductId(), "0");
//                            }
////                        } else {
////                            //availableStockProductsList.put(allProductsList.get(v).getProductId(), "0");
////                        }
//                    }
//                }
            }
            //Log.i("allpList", allProductsList.size() + "\n");
            //Log.i("available temp", availableStockProductsListTemp.size() + "\n");
            // Log.i("stock", stockBeanArrayList.size() + "\n");
            // Log.i("available actual", availableStockProductsList.size() + "\n");
            if (showProductsListView) {
                allProductsList = mDBHelper.fetchAllRecordsFromProductsTable();

                tdcSalesAdapter = new TDCSalesAdapter(activityContext, this, this, tdc_products_list_view, allProductsList, previouslySelectedProductsListHashMap, availableStockProductsList, userId, previousselectedProductsStockListHashMap);
                tdc_products_list_view.setAdapter(tdcSalesAdapter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        startService(new Intent(TDCSalesActivity.this, SyncNotificationsListService.class));
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
                    Intent i = new Intent(TDCSalesActivity.this, TDCSalesListActivity.class);
                    i.putExtra("CustomerName", str_selectedretailername);
                    startActivity(i);
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
                cancelButton.setTextColor(ContextCompat.getColor(context, R.color.alert_dialog_color_accent));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tdc, menu);

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
                    tdcSalesAdapter.filter(query);
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
            Toast.makeText(this, "Clicked on Notifications...", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.settings) {

            loadSettings();
            Toast.makeText(this, "Clicked on Settings...", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.sort) {
            synchronized (this) {
                if (stockBeanArrayList.size() > 0) {
                    if (allProductsListSort.size() > 0) {
                        allProductsListSort.clear();
                    }
                    synchronized (this) {
                        for (int k = 0; k < allProductsList.size(); k++) {
                            Double selectedQua = 0.0, stockQuantity = 0.0;
                            if (selectedProductsStockListHashMap.get(allProductsList.get(k).getProductId()) != null) {
                                selectedQua = Double.parseDouble(selectedProductsStockListHashMap.get(allProductsList.get(k).getProductId()));
                            }

                            if (availableStockProductsList.get(allProductsList.get(k).getProductId()) != null) {
                                stockQuantity = Double.parseDouble(availableStockProductsList.get(allProductsList.get(k).getProductId()));
                            }

                            // Sorting code
                            ProductsBean productsBean1 = new ProductsBean();

                            productsBean1.setProductId(allProductsList.get(k).getProductId());
                            productsBean1.setProductCode(allProductsList.get(k).getProductCode());
                            productsBean1.setProductTitle(allProductsList.get(k).getProductTitle());
                            productsBean1.setProductDescription(allProductsList.get(k).getProductDescription());
                            productsBean1.setProductImageUrl(allProductsList.get(k).getProductImageUrl());
                            productsBean1.setProductReturnable(allProductsList.get(k).getProductReturnable());
                            productsBean1.setProductMOQ(allProductsList.get(k).getProductMOQ());
                            productsBean1.setProductUOM(allProductsList.get(k).getProductUOM());
                            productsBean1.setProductAgentPrice(allProductsList.get(k).getProductAgentPrice());
                            productsBean1.setProductConsumerPrice(allProductsList.get(k).getProductConsumerPrice());
                            productsBean1.setProductRetailerPrice(allProductsList.get(k).getProductRetailerPrice());
                            productsBean1.setProductgst(allProductsList.get(k).getProductgst());
                            productsBean1.setProductvat(allProductsList.get(k).getProductvat());
                            productsBean1.setControlCode(allProductsList.get(k).getControlCode());
                            productsBean1.setSelectedQuantity(selectedQua);
                            productsBean1.setmStockQuantity(stockQuantity);

                            allProductsListSort.add(productsBean1);
                        }
                    }
                    synchronized (this) {
                        if (!isAscendingSort) {
                            // Ascending order
                            isAscendingSort = true;
                            Collections.sort(allProductsListSort, StringAscComparator);
                        } else {
                            // Descending order
                            isAscendingSort = false;
                            Collections.sort(allProductsListSort, StringDescComparator);
                        }
                    }
                    synchronized (this) {
                        if (showProductsListView) {
                            if (tdcSalesAdapter != null) {
                                tdcSalesAdapter = null;
                            }
                            previousselectedProductsStockListHashMap = selectedProductsStockListHashMap;

                            tdcSalesAdapter = new TDCSalesAdapter(activityContext, this, this, tdc_products_list_view, allProductsListSort, previouslySelectedProductsListHashMap, availableStockProductsList, userId, previousselectedProductsStockListHashMap);
                            tdc_products_list_view.setAdapter(tdcSalesAdapter);
                            tdcSalesAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    CustomAlertDialog.showAlertDialog(activityContext, "Failed", getResources().getString(R.string.deliverylimit_sort));
                }
            }
            return true;
        }


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

    // Comparator for Ascending Order
    public static Comparator<ProductsBean> StringAscComparator = new Comparator<ProductsBean>() {

        public int compare(ProductsBean app1, ProductsBean app2) {
            int g = 0;

            Double stringName1 = app1.getmStockQuantity();
            Double stringName2 = app2.getmStockQuantity();
            if (stringName1 >= stringName2) {
                return -1;
            } else {
                return 1;
            }
        }
    };

    //Comparator for Descending Order
    public static Comparator<ProductsBean> StringDescComparator = new Comparator<ProductsBean>() {

        public int compare(ProductsBean app1, ProductsBean app2) {
            int f = 0;
            Double stringName1 = app1.getmStockQuantity();
            Double stringName2 = app2.getmStockQuantity();
            if (stringName2 >= stringName1) {
                return -1;
            } else {
                return 1;
            }
        }
    };

    private void loadNotifications() {
        Intent navigationIntent = new Intent(TDCSalesActivity.this, NotificationsActivity.class);
        // mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // if you keep these flags white screen is coming on Intent navigation
        startActivity(navigationIntent);
        finish();
    }

    private void loadSettings() {
        Intent settingsIntent = new Intent(TDCSalesActivity.this, SettingsActivity.class);
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
        menu.findItem(R.id.sort).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = null;
        if (mTdcHomeScreen.equals("tdc_home_screen")) {
            // intent = new Intent(this, TDCSalesActivity.class);
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
            intent = new Intent(this, RetailersActivity.class);
            startActivity(intent);
            finish();

        } else if (mDashboardHomeScreen.equals("Dashboard@Home")) {
            intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            intent = new Intent(this, DashboardActivity.class);
        }

    }

    @Override
    public void updateSelectedProductsListAndSubTotal(Map<String, ProductsBean> productsList) {
        this.selectedProductsListHashMap = productsList;
        if (allProductsListSort.size() > 0) {
            allProductsListSort.clear();
        }
        totalAmount = 0;
        totalTaxAmount = 0;
        subTotal = 0;

        for (Map.Entry<String, ProductsBean> productsBeanEntry : selectedProductsListHashMap.entrySet()) {
            ProductsBean productsBean = productsBeanEntry.getValue();
            totalAmount = totalAmount + productsBean.getProductAmount();
            totalTaxAmount = totalTaxAmount + productsBean.getTaxAmount();
            subTotal = totalAmount + totalTaxAmount;

        }

        totalTaxAmountTextView.setText(Utility.getFormattedCurrency(totalTaxAmount));
        totalAmountTextView.setText(Utility.getFormattedCurrency(totalAmount));
        subTotalAmountTextView.setText(Utility.getFormattedCurrency(subTotal));

        //mDBHelper.insertProductDetailsForSort(allProductsListSort);


    }

    @Override
    public void updateAgentStockSaleQuantityAfterTDCSale(Map<String, String> selectedProductsList) {
        // System.out.println("SIZE:::: " + selectedProductsList.size());
        this.selectedProductsStockListHashMap = selectedProductsList;

        //System.out.println("SIZE 1111:::: " + selectedProductsStockListHashMap.size());
    }

    public void showTDCSalesPreview(View view) {
        try {
            if (selectedProductsListHashMap.size() > 0) {
                currentOrder.setProductsList(selectedProductsListHashMap);
                currentOrder.setOrderTotalAmount(totalAmount);
                currentOrder.setOrderTotalTaxAmount(totalTaxAmount);
                currentOrder.setOrderSubTotal(subTotal);

                Intent customerSelectionIntent = new Intent(activityContext, TDCSalesCustomerSelectionActivity.class);
                customerSelectionIntent.putExtra(Constants.BUNDLE_TDC_SALE_ORDER, currentOrder);
                customerSelectionIntent.putExtra(Constants.BUNDLE_TDC_SALE_ORDER_SALE_QUNAT, selectedProductsStockListHashMap.toString());
                startActivity(customerSelectionIntent);
                finish();
            } else {
                //  Toast.makeText(activityContext, "Please select at least one product.", Toast.LENGTH_LONG).show();
                CustomAlertDialog.showAlertDialog(activityContext, "Failed", getResources().getString(R.string.deliverylimit));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTDCSalesList(View view) {
        showAlertDialogWithCancelButton(TDCSalesActivity.this, "User Action!", "Are you sure want to leave sales?");
    }
}
