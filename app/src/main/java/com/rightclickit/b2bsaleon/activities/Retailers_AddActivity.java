package com.rightclickit.b2bsaleon.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.beanclass.TDCCustomer;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

public class Retailers_AddActivity extends AppCompatActivity {
    private Context applicationContext, activityContext;
    private MMSharedPreferences mmSharedPreferences;

    private EditText retailer_name, mobile_no, business_name, address;
    private DBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailers_add);

        try {
            applicationContext = getApplicationContext();
            activityContext = Retailers_AddActivity.this;

            final ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;

            actionBar.setTitle("ADD RETAILER");
            actionBar.setSubtitle(null);
            actionBar.setLogo(R.drawable.ic_store_white);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

            retailer_name = (EditText) findViewById(R.id.retailer_name);
            mobile_no = (EditText) findViewById(R.id.mobile_no);
            business_name = (EditText) findViewById(R.id.business_name);
            address = (EditText) findViewById(R.id.address);

            mDBHelper = new DBHelper(activityContext);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.Add).setVisible(false);
        menu.findItem(R.id.autorenew).setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, RetailersActivity.class);
        startActivity(intent);
        finish();
    }

    public void saveNewRetailer(View view) {
        try {
            String name = retailer_name.getText().toString().trim();
            String mobileNo = mobile_no.getText().toString().trim();
            String businessName = business_name.getText().toString().trim();
            String retailerAddress = address.getText().toString().trim();

            boolean cancel = false;
            View focusView = null;

            if (name.isEmpty()) {
                retailer_name.setError("Please enter person name.");
                focusView = retailer_name;
                cancel = true;
            } else if (mobileNo.isEmpty()) {
                mobile_no.setError("Please enter mobile no.");
                focusView = mobile_no;
                cancel = true;
            } else if (businessName.isEmpty()) {
                business_name.setError("Please enter business name.");
                focusView = business_name;
                cancel = true;
            } else if (retailerAddress.isEmpty()) {
                address.setError("Please enter address.");
                focusView = address;
                cancel = true;
            }

            if (cancel) {
                focusView.requestFocus();
            } else {
                addNewRetailer(name, mobileNo, businessName, retailerAddress);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNewRetailer(String name, String mobileNo, String businessName, String retailerAddress) {
        try {
            TDCCustomer customer = new TDCCustomer();
            customer.setCustomerType(1);
            customer.setName(name);
            customer.setMobileNo(mobileNo);
            customer.setBusinessName(businessName);
            customer.setAddress(retailerAddress);
            customer.setLatLong("");
            customer.setShopImage("");

            long customerId = mDBHelper.insertIntoTDCCustomers(customer);

            if (customerId == -1)
                Toast.makeText(activityContext, "An error occurred while adding new retailer.", Toast.LENGTH_LONG).show();
            else {
                retailer_name.setText("");
                mobile_no.setText("");
                business_name.setText("");
                address.setText("");

                Toast.makeText(activityContext, "New retailer added successfully.", Toast.LENGTH_LONG).show();

                onBackPressed();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
