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
import com.rightclickit.b2bsaleon.adapters.TDCSalesAdapter;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.TDCSalesListener;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;

public class SalesActivity extends AppCompatActivity implements TDCSalesListener {
    private Context applicationContext, activityContext;
    private MMSharedPreferences mmSharedPreferences;

    private SearchView search;
    private ListView tdc_products_list_view;
    private TextView tdc_sales_list, tdc_sales_preview, totalTaxAmountTextView, totalAmountTextView, subTotalAmountTextView;

    private DBHelper mDBHelper;
    private ArrayList<ProductsBean> productsList, selectedProductsList;
    private TDCSalesAdapter tdcSalesAdapter;
    private boolean showProductsListView = false;
    private double totalAmount = 0, totalTaxAmount = 0, subTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        try {
            applicationContext = getApplicationContext();
            activityContext = SalesActivity.this;

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

            mDBHelper = new DBHelper(activityContext);
            mmSharedPreferences = new MMSharedPreferences(activityContext);
            productsList = new ArrayList<>();
            selectedProductsList = new ArrayList<>();

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

            if (showProductsListView) {
                productsList = mDBHelper.fetchAllRecordsFromProductsTable();
                tdcSalesAdapter = new TDCSalesAdapter(activityContext, this, this, productsList, tdc_products_list_view);
                tdc_products_list_view.setAdapter(tdcSalesAdapter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    Intent i = new Intent(SalesActivity.this, SalesListActivity.class);
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

        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void updateSelectedProductsListAndSubTotal(ArrayList<ProductsBean> productsList) {
        this.selectedProductsList = productsList;

        totalAmount = 0;
        totalTaxAmount = 0;
        subTotal = 0;

        for (ProductsBean productsBean : selectedProductsList) {
            totalAmount = totalAmount + productsBean.getProductAmount();
            totalTaxAmount = totalTaxAmount + productsBean.getTaxAmount();
            subTotal = totalAmount + totalTaxAmount;
        }

        totalTaxAmountTextView.setText(Utility.getFormattedCurrency(totalAmount));
        totalAmountTextView.setText(Utility.getFormattedCurrency(totalTaxAmount));
        subTotalAmountTextView.setText(Utility.getFormattedCurrency(subTotal));
    }

    public void showTDCSalesPreview(View view) {
        try {
            if (selectedProductsList.size() > 0) {
                TDCSaleOrder saleOrder = new TDCSaleOrder();
                saleOrder.setProductsList(selectedProductsList);
                saleOrder.setOrderTotalAmount(totalAmount);
                saleOrder.setOrderTotalTaxAmount(totalTaxAmount);
                saleOrder.setOrderSubTotal(subTotal);

                Intent customerSelectionIntent = new Intent(activityContext, SalesCustomerSelectionActivity.class);
                customerSelectionIntent.putExtra("TDCSaleOrder", saleOrder);
                startActivity(customerSelectionIntent);
                finish();
            } else {
                Toast.makeText(activityContext, "Please select at least one product.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTDCSalesList(View view) {
        showAlertDialogWithCancelButton(SalesActivity.this, "User Action!", "Are you sure want to leave sales?");
    }
}
