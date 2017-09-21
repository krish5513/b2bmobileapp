package com.rightclickit.b2bsaleon.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TDCSalesListAdapter;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.customviews.CustomProgressDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TDCSalesListActivity extends AppCompatActivity {
    private Context applicationContext, activityContext;
    private MMSharedPreferences mmSharedPreferences;

    private SearchView search;
    private ListView tdcSalesListView;
    private TextView no_sales_found_message, tdc_sales_count, tdc_sales_total_amount, tdc_sales_items_count;
    private TextView tdc_sales_today, tdc_sales_weekly, tdc_sales_monthly;

    private TDCSalesListAdapter tdcSalesListAdapter;
    private DBHelper mDBHelper;
    private List<TDCSaleOrder> allTDCSaleOrders;

    private int colorAccent, colorPrimary;
    private Drawable border_accent, border_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_list);

        try {
            applicationContext = getApplicationContext();
            activityContext = TDCSalesListActivity.this;
            mmSharedPreferences = new MMSharedPreferences(activityContext);
            mDBHelper = new DBHelper(activityContext);

            this.getSupportActionBar().setTitle("SALES LIST");
            this.getSupportActionBar().setSubtitle(null);
            this.getSupportActionBar().setLogo(R.drawable.customers_white_24);
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

            tdcSalesListView = (ListView) findViewById(R.id.tdc_sales_list_view);
            tdcSalesListAdapter = new TDCSalesListAdapter(activityContext, this);
            tdcSalesListView.setAdapter(tdcSalesListAdapter);

            loadSalesList(0);

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
                    tdcSalesListAdapter.filter(query);
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
        menu.findItem(R.id.autorenew).setVisible(true);
        menu.findItem(R.id.sort).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, TDCSalesActivity.class);
        startActivity(intent);
        finish();
    }

    public void loadTDCSalesList(View view) {
        int tag = Integer.parseInt(view.getTag().toString());

        CustomProgressDialog.showProgressDialog(activityContext, Constants.LOADING_MESSAGE);

        if (tag == 0) {
            tdc_sales_today.setBackground(border_accent);
            tdc_sales_today.setTextColor(colorAccent);

            tdc_sales_weekly.setBackground(border_btn);
            tdc_sales_weekly.setTextColor(colorPrimary);

            tdc_sales_monthly.setBackground(border_btn);
            tdc_sales_monthly.setTextColor(colorPrimary);
        } else if (tag == 1) {
            tdc_sales_today.setBackground(border_btn);
            tdc_sales_today.setTextColor(colorPrimary);

            tdc_sales_weekly.setBackground(border_accent);
            tdc_sales_weekly.setTextColor(colorAccent);

            tdc_sales_monthly.setBackground(border_btn);
            tdc_sales_monthly.setTextColor(colorPrimary);
        } else if (tag == 2) {
            tdc_sales_today.setBackground(border_btn);
            tdc_sales_today.setTextColor(colorPrimary);

            tdc_sales_weekly.setBackground(border_btn);
            tdc_sales_weekly.setTextColor(colorPrimary);

            tdc_sales_monthly.setBackground(border_accent);
            tdc_sales_monthly.setTextColor(colorAccent);
        }

        loadSalesList(tag);
    }

    /**
     * @param duration :: 0 for Today, 1 for Weekly & 2 for Monthly
     */
    public void loadSalesList(int duration) {
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
                tdcSalesListView.setVisibility(View.GONE);
                no_sales_found_message.setVisibility(View.VISIBLE);

                tdc_sales_count.setText("0");
                tdc_sales_total_amount.setText(Utility.getFormattedCurrency(0));
                tdc_sales_items_count.setText("0");
            } else {
                no_sales_found_message.setVisibility(View.GONE);
                tdcSalesListView.setVisibility(View.VISIBLE);

                tdcSalesListAdapter.setAllTDCSalesOrders(allTDCSaleOrders);
                tdcSalesListAdapter.notifyDataSetChanged();

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

        CustomProgressDialog.hideProgressDialog();
    }
}
