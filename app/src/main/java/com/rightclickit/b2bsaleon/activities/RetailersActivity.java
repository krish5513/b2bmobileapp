package com.rightclickit.b2bsaleon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;

public class RetailersActivity extends AppCompatActivity {

    FloatingActionButton fab;

    Button info, payments;
    private DBHelper mDBHelper;
    private LinearLayout mDashBoardLayout;
    private LinearLayout mTripsheetsLayout;
    private LinearLayout mCustomersLayout;
    private LinearLayout mProductsLayout;
    private LinearLayout mTDCLayout;
    private LinearLayout mRetailersLayout;
    private LinearLayout mRetailerslistview;
    private MMSharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailers);

        this.getSupportActionBar().setTitle("RETAILERS");
        this.getSupportActionBar().setSubtitle(null);
        this.getSupportActionBar().setLogo(R.drawable.ic_store_white);
        // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPreferences = new MMSharedPreferences(RetailersActivity.this);
        mDBHelper = new DBHelper(RetailersActivity.this);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        fab = (FloatingActionButton) findViewById(R.id.retailerfab);
        fab.setVisibility(View.GONE);
        fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.plus_white));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getApplicationContext(),"Clicked Retailers Add",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(RetailersActivity.this, Retailers_AddActivity.class);
                startActivity(i);
                finish();
            }
        });


        mRetailerslistview = (LinearLayout) findViewById(R.id.retailerslistview);
        mRetailerslistview.setVisibility(View.GONE);


        mDashBoardLayout = (LinearLayout) findViewById(R.id.DashboardLayout);
        mDashBoardLayout.setVisibility(View.GONE);
        mDashBoardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mDashBoardLayout.startAnimation(animation1);
                Intent i = new Intent(RetailersActivity.this, DashboardActivity.class);
                startActivity(i);
                finish();

            }
        });
        mTripsheetsLayout = (LinearLayout) findViewById(R.id.TripSheetsLayout);
        mTripsheetsLayout.setVisibility(View.GONE);
        mTripsheetsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mTripsheetsLayout.startAnimation(animation1);
                Intent i = new Intent(RetailersActivity.this, TripSheetsActivity.class);
                startActivity(i);
                finish();
            }
        });
        mCustomersLayout = (LinearLayout) findViewById(R.id.CustomersLayout);
        mCustomersLayout.setVisibility(View.GONE);
        mCustomersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mCustomersLayout.startAnimation(animation1);
                Intent i = new Intent(RetailersActivity.this, AgentsActivity.class);
                startActivity(i);
                finish();
            }
        });
        mRetailersLayout = (LinearLayout) findViewById(R.id.Retailers);
        mRetailersLayout.setVisibility(View.GONE);
        mRetailersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mRetailersLayout.startAnimation(animation1);

            }
        });
        mProductsLayout = (LinearLayout) findViewById(R.id.ProductsLayout);
        mProductsLayout.setVisibility(View.GONE);
        mProductsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mProductsLayout.startAnimation(animation1);
                Intent i = new Intent(RetailersActivity.this, Products_Activity.class);
                startActivity(i);
                finish();
            }
        });
        mTDCLayout = (LinearLayout) findViewById(R.id.TDCLayout);
        mTDCLayout.setVisibility(View.GONE);
        mTDCLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mTDCLayout.startAnimation(animation1);
                Intent i = new Intent(RetailersActivity.this, SalesActivity.class);
                startActivity(i);
                finish();
            }
        });

        info = (Button) findViewById( R.id.btn_info );
        info.setVisibility(View.GONE);
        payments = (Button) findViewById( R.id.btn_payments );
        payments.setVisibility(View.GONE);



        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RetailersActivity.this, RetailersInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RetailersActivity.this, Retailers_PaymentsActivity.class);
                startActivity(intent);
                finish();
            }

        } );





        HashMap<String, String> userMapData = mDBHelper.getUsersData();
        ArrayList<String> privilegesData = mDBHelper.getUserActivityDetailsByUserId(userMapData.get("user_id"));
        System.out.println("F 11111 ***COUNT === " + privilegesData.size());
        for (int k = 0; k < privilegesData.size(); k++) {
            System.out.println("F 11111 ***COUNT 4444 === " + privilegesData.get(k).toString());
            if (privilegesData.get(k).toString().equals("Dashboard")) {
                mDashBoardLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("TripSheets")) {
                mTripsheetsLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("Customers")) {
                mCustomersLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("Products")) {
                mProductsLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("TDC")) {
                mTDCLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("Retailers")) {
                mRetailersLayout.setVisibility(View.VISIBLE);
            }
        }

        ArrayList<String> privilegeActionsData = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("Retailers"));
        System.out.println("F 11111 ***COUNT === " + privilegeActionsData.size());
        for (int z = 0; z < privilegeActionsData.size(); z++) {
            System.out.println("Name::: " + privilegeActionsData.get(z).toString());

            if (privilegeActionsData.get(z).toString().equals("List_View")) {
                mRetailerslistview.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData.get(z).toString().equals("List_Info")) {
                info.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData.get(z).toString().equals("Payment_List")) {
                payments.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData.get(z).toString().equals("Add")) {
                fab.setVisibility(View.VISIBLE);
            }
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
        if (id == R.id.action_search) {

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
        menu.findItem( R.id.autorenew).setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
