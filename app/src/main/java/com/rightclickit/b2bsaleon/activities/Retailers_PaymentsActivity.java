package com.rightclickit.b2bsaleon.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.RetailersPaymentsAdapter;
import com.rightclickit.b2bsaleon.adapters.TDCSalesListAdapter;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Retailers_PaymentsActivity extends AppCompatActivity {

    private Context applicationContext, activityContext;
    private MMSharedPreferences mmSharedPreferences;

    private ListView paymentsListView;
    private TextView no_sales_found_message, tdc_sales_count, tdc_sales_total_amount, tdc_sales_items_count;
    private TextView tdc_sales_today, tdc_sales_weekly, tdc_sales_monthly;

    private RetailersPaymentsAdapter paymentsListAdapter;
    private DBHelper mDBHelper;
    private List<TDCSaleOrder> allTDCSaleOrders;

    private int colorAccent, colorPrimary;
    private Drawable border_accent, border_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_retailers__payments );

       try {
           applicationContext = getApplicationContext();
           activityContext = Retailers_PaymentsActivity.this;
           mmSharedPreferences = new MMSharedPreferences(activityContext);
           mDBHelper = new DBHelper(activityContext);

           this.getSupportActionBar().setTitle("RETAILER NAME");
           this.getSupportActionBar().setSubtitle(null);
           this.getSupportActionBar().setLogo(R.drawable.ic_store_white);
           // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
           this.getSupportActionBar().setDisplayUseLogoEnabled(true);
           getSupportActionBar().setDisplayHomeAsUpEnabled(false);
           this.getSupportActionBar().setDisplayShowHomeEnabled(true);


           final ActionBar actionBar = getSupportActionBar();
           assert actionBar != null;
           actionBar.setDisplayHomeAsUpEnabled(true);
           actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);


           no_sales_found_message = (TextView) findViewById(R.id.no_tdc_sales_found_message);
           tdc_sales_count = (TextView) findViewById(R.id.tdc_sales_count);
           tdc_sales_total_amount = (TextView) findViewById(R.id.tdc_sales_total_amount);
           tdc_sales_items_count = (TextView) findViewById(R.id.tdc_sales_items_count);
           tdc_sales_today = (TextView) findViewById(R.id.tdc_sales_today);
           tdc_sales_weekly = (TextView) findViewById(R.id.tdc_sales_weekly);
           tdc_sales_monthly = (TextView) findViewById(R.id.tdc_sales_monthly);

           colorAccent = ContextCompat.getColor(activityContext, R.color.colorAccent);
           colorPrimary = ContextCompat.getColor(activityContext, R.color.colorPrimary);

           border_accent = ContextCompat.getDrawable(activityContext, R.drawable.border_accent);
           border_btn = ContextCompat.getDrawable(activityContext, R.drawable.border_btn);

           paymentsListView = (ListView) findViewById(R.id.tdc_sales_list_view);
           paymentsListAdapter = new RetailersPaymentsAdapter(activityContext, this, allTDCSaleOrders);
           paymentsListView.setAdapter(paymentsListAdapter);

           loadSalesList(0);


       }catch (Exception e) {
    e.printStackTrace();
}





    }

    private void loadSalesList(int duration) {

        try {
            Date currentDate = new Date(), startingDate = null, endingDate = null;
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.SUNDAY);

            if (duration == 0) {
                startingDate = currentDate;
                endingDate = currentDate;
            } else if (duration == 1) {
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                startingDate = calendar.getTime();
                endingDate = currentDate;
            } else if (duration == 2) {
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                startingDate = calendar.getTime();
                endingDate = currentDate;
            }

            String startDateStr = Utility.formatDate(startingDate, Constants.TDC_SALES_ORDER_DATE_SAVE_FORMAT);
            String endDateStr = Utility.formatDate(endingDate, Constants.TDC_SALES_ORDER_DATE_SAVE_FORMAT);

            allTDCSaleOrders = mDBHelper.fetchAllTDCSalesOrdersForSelectedDuration(startDateStr, endDateStr);

            if (allTDCSaleOrders.size() <= 0) {
                paymentsListView.setVisibility(View.GONE);
                no_sales_found_message.setVisibility(View.VISIBLE);

                tdc_sales_count.setText("0");
                tdc_sales_total_amount.setText(Utility.getFormattedCurrency(0));
                tdc_sales_items_count.setText("0");
            } else {
                no_sales_found_message.setVisibility(View.GONE);
                paymentsListView.setVisibility(View.VISIBLE);

                paymentsListAdapter.setTdcSalesOrders(allTDCSaleOrders);

                int noOfSales = allTDCSaleOrders.size();
                double amount = 0;
                int noOfItems = 0;

                for (TDCSaleOrder order : allTDCSaleOrders) {
                    amount = amount + order.getOrderSubTotal();
                    noOfItems = noOfItems + order.getNoOfItems();
                }

                tdc_sales_count.setText(Utility.getFormattedNumber(noOfSales));
                tdc_sales_total_amount.setText(Utility.getFormattedCurrency(amount));
                tdc_sales_items_count.setText(Utility.getFormattedNumber(noOfItems));
            }
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
        menu.findItem( R.id.Add).setVisible(false);

        menu.findItem( R.id.autorenew).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, RetailersActivity.class);
        startActivity(intent);
        finish();
    }
}
