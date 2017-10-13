package com.rightclickit.b2bsaleon.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TDCSalesCustomerSelectionAdapter;
import com.rightclickit.b2bsaleon.beanclass.TDCCustomer;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.customviews.CustomAlertDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.services.SyncTDCCustomersService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TDCSalesCustomerSelectionActivity extends AppCompatActivity {
    private Context applicationContext, activityContext;
    private MMSharedPreferences mmSharedPreferences;

    private SearchView search;
    private ListView tdc_customers_list_view;
    private TextView tdc_sales_no_consumers_message;
    private FloatingActionButton fab_add_customer;

    private DBHelper mDBHelper;
    private TDCSalesCustomerSelectionAdapter customerSelectionAdapter;
    private List<TDCCustomer> customerList;

    private TDCSaleOrder currentOrder;
    private boolean isCustomerSelected = false;

    private String saleQunatity = "";
    private Map<String, String> saleQuantityListMap = new HashMap<String, String>();
    private String customer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_customer_selection);

        try {
            applicationContext = getApplicationContext();
            activityContext = TDCSalesCustomerSelectionActivity.this;

            final ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;

            actionBar.setTitle("SELECT RETAILER/CONSUMER");
            actionBar.setSubtitle(null);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

            tdc_customers_list_view = (ListView) findViewById(R.id.tdc_customers_list_view);
            tdc_sales_no_consumers_message = (TextView) findViewById(R.id.tdc_sales_no_consumers);

            fab_add_customer = (FloatingActionButton) findViewById(R.id.add_customer_fab);
            fab_add_customer.setVisibility(View.VISIBLE);
            fab_add_customer.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.customer60));
            fab_add_customer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAddCustomerDialog();
                }
            });

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                currentOrder = (TDCSaleOrder) bundle.getSerializable(Constants.BUNDLE_TDC_SALE_ORDER);
                saleQunatity = bundle.getString(Constants.BUNDLE_TDC_SALE_ORDER_SALE_QUNAT);
                //System.out.println("CUSTOMER SALE QUANTITY=== " + saleQunatity);
            }

            mDBHelper = new DBHelper(activityContext);
            customerList = new ArrayList<>();

            customerList = mDBHelper.fetchAllRecordsFromTDCCustomers();

            if (customerList.size() <= 0) {
                tdc_customers_list_view.setVisibility(View.GONE);
                tdc_sales_no_consumers_message.setVisibility(View.VISIBLE);
            }

            customerSelectionAdapter = new TDCSalesCustomerSelectionAdapter(activityContext, this, customerList, currentOrder);
            tdc_customers_list_view.setAdapter(customerSelectionAdapter);

            tdc_customers_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    TDCCustomer selectedCustomer = customerList.get(position);
                    if (currentOrder != null) {
                        currentOrder.setSelectedCustomerId(selectedCustomer.getId());
                        currentOrder.setSelectedCustomerUserId(selectedCustomer.getUserId());
                        currentOrder.setSelectedCustomerType(selectedCustomer.getCustomerType());
                        isCustomerSelected = true;
                        customer = selectedCustomer.getName();

                        showTDCSalesOrderPreview(null);
                    }
                }
            });

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
                    customerSelectionAdapter.filter(query);
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

        if (id == R.id.Add) {
            finish();
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
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, TDCSalesActivity.class);
        intent.putExtra(Constants.BUNDLE_TDC_SALE_CURRENT_ORDER, currentOrder); // to handle back button
        intent.putExtra(Constants.BUNDLE_TDC_SALE_CURRENT_SALEQUNATITY, saleQunatity); // to handle back button
        startActivity(intent);
        finish();
    }

    protected void showAddCustomerDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(TDCSalesCustomerSelectionActivity.this);
        View promptView = layoutInflater.inflate(R.layout.consumer_adddialog, null);
        final EditText name = (EditText) promptView.findViewById(R.id.name);
        final EditText mob = (EditText) promptView.findViewById(R.id.mobileno);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TDCSalesCustomerSelectionActivity.this);
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
                        String cName = name.getText().toString().trim();
                        String cMobileNo = mob.getText().toString().trim();

                        boolean cancel = false;
                        View focusView = null;

                        if (cName.isEmpty()) {
                            name.setError("Please enter name.");
                            focusView = name;
                            cancel = true;
                        } else if (cMobileNo.isEmpty()) {
                            mob.setError("Please enter mobile number.");
                            focusView = mob;
                            cancel = true;
                        }

                        if (cancel) {
                            focusView.requestFocus();
                        } else {
                            alertDialog.dismiss();

                            addNewCustomer(cName, cMobileNo);
                        }
                    }
                });
            }
        });

        alertDialog.show();
    }

    public void addNewCustomer(String name, String mobileNo) {
        try {
            TDCCustomer customer = new TDCCustomer();
            customer.setUserId(""); // later we will update this value by fetching from service.
            customer.setCustomerType(0);
            customer.setName(name);
            customer.setMobileNo(mobileNo);
            customer.setBusinessName("");
            customer.setAddress("");
            customer.setLatitude("");
            customer.setLongitude("");
            customer.setShopImage("");
            customer.setIsShopImageUploaded(1);

            long customerId = mDBHelper.insertIntoTDCCustomers(customer);

            if (customerId == -1)
                Toast.makeText(activityContext, "An error occurred while adding new consumer.", Toast.LENGTH_LONG).show();
            else {
                tdc_customers_list_view.setVisibility(View.VISIBLE);
                tdc_sales_no_consumers_message.setVisibility(View.GONE);

                customerList = mDBHelper.fetchAllRecordsFromTDCCustomers();
                customerSelectionAdapter.setAllCustomersList(customerList);
                customerSelectionAdapter.notifyDataSetChanged();

                if (new NetworkConnectionDetector(activityContext).isNetworkConnected()) {
                    Intent syncTDCCustomersServiceIntent = new Intent(activityContext, SyncTDCCustomersService.class);
                    startService(syncTDCCustomersServiceIntent);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTDCSalesOrderPreview(View view) {
        if (isCustomerSelected) {
            Intent i = new Intent(TDCSalesCustomerSelectionActivity.this, TDCSales_Preview_PrintActivity.class);
            i.putExtra(Constants.BUNDLE_TDC_SALE_CURRENT_ORDER_PREVIEW, currentOrder);
            i.putExtra(Constants.BUNDLE_REQUEST_FROM, Constants.BUNDLE_REQUEST_FROM_TDC_CUSTOMER_SELECTION);
            i.putExtra(Constants.BUNDLE_TDC_SALE_QUANTITY, saleQunatity);
            i.putExtra("CustomerName", customer);
            startActivity(i);
            finish();
        } else {
            CustomAlertDialog.showAlertDialog(activityContext, "Failed", "Please select/add either retailer or consumer.");
        }
    }
}
