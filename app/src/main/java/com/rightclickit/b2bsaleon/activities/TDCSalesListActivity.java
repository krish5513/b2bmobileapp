package com.rightclickit.b2bsaleon.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TDCSalesListAdapter;
import com.rightclickit.b2bsaleon.beanclass.TDCCustomer;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.customviews.CustomProgressDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TDCSalesListActivity extends AppCompatActivity {
    private Context applicationContext, activityContext;
    private MMSharedPreferences mmSharedPreferences;

    private SearchView search;
    private ListView tdcSalesListView;
    private TextView no_sales_found_message, tdc_sales_count, tdc_sales_total_amount, tdc_sales_items_count,sales_print;
    private TextView tdc_sales_today, tdc_sales_weekly, tdc_sales_monthly;

    private TDCSalesListAdapter tdcSalesListAdapter;
    private DBHelper mDBHelper;
    private List<TDCSaleOrder> allTDCSaleOrders;

    private int colorAccent, colorPrimary;
    private Drawable border_accent, border_btn;
    ArrayList<String[]> selectedList;
    String billno,billdate,billamount,billitems;

    String str_selectedretailername;
    private List<TDCCustomer> customerList;
    String name,code,TroipsTakeorder="";
    String screenType="";
    String custId="";

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
          //  tdc_sales_monthly = (TextView) findViewById(R.id.tdc_sales_monthly);

            sales_print = (TextView) findViewById(R.id.tv_print);

            colorAccent = ContextCompat.getColor(activityContext, R.color.colorAccent);
            colorPrimary = ContextCompat.getColor(activityContext, R.color.colorPrimary);

            border_accent = ContextCompat.getDrawable(activityContext, R.drawable.border_accent);
            border_btn = ContextCompat.getDrawable(activityContext, R.drawable.border_btn);

            tdcSalesListView = (ListView) findViewById(R.id.tdc_sales_list_view);

            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                TroipsTakeorder = bundle.getString("From");
              //custId = bundle.getString("custId");
               // screenType = bundle.getString("screenType"); //AgentOrder
            }


/*
            Bundle bundle1 = this.getIntent().getExtras();
            if (bundle1 != null) {

                custId = bundle.getString("custId");
                screenType = bundle.getString("screenType"); //AgentOrder
            }*/
            tdcSalesListAdapter = new TDCSalesListAdapter(activityContext, this);
            tdcSalesListView.setAdapter(tdcSalesListAdapter);

            loadSalesList(0);

            Date currentDate = new Date(), startingDate = null, endingDate = null;
            Calendar calendar = Calendar.getInstance();
            calendar.setFirstDayOfWeek(Calendar.SUNDAY);

            int duration=0;

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

           // if(screenType.equals("customerDetails")){
               // allTDCSaleOrders = mDBHelper.fetchAllTDCSalesOrdersForSelectedDuration(startDateStr, endDateStr, custId);
           // }else{
                allTDCSaleOrders = mDBHelper.fetchAllTDCSalesOrdersForSelectedDuration(startDateStr, endDateStr, "");
           // }

            customerList = new ArrayList<>();

            customerList = mDBHelper.fetchAllRecordsFromTDCCustomers();
           /* for (int i=0;i<customerList.size();i++){

                if (customerList.get(i).getCustomerType() == 1) {
                    code=(String.format("R%05d", customerList.get(i).getId()));

                } else {
                    code=(String.format("C%05d", customerList.get(i).getId()));

                }
            }

*/
                int noOfSales = allTDCSaleOrders.size();

                int noOfItems = 0;
                double amount = 0;

                for (TDCSaleOrder order : allTDCSaleOrders) {
                    amount = amount + order.getOrderSubTotal();
                    noOfItems = noOfItems + order.getNoOfItems();
                }


                tdc_sales_total_amount.setText(Utility.getFormattedCurrency(amount));



            selectedList = new ArrayList<>(allTDCSaleOrders.size());

            for (int i = 0; i < allTDCSaleOrders.size(); i++) {
                billno = (String.format("TDC%05d", allTDCSaleOrders.get(i).getOrderId()));
                billamount = Utility.getFormattedCurrency(allTDCSaleOrders.get(i).getOrderSubTotal());
                billdate = Utility.formatTime(allTDCSaleOrders.get(i).getCreatedOn(), Constants.TDC_SALES_LIST_DATE_DISPLAY_FORMAT);
                billitems =Utility.getFormattedNumber(allTDCSaleOrders.get(i).getNoOfItems());
                name = mDBHelper.getNameById(allTDCSaleOrders.get(i).getSelectedCustomerUserId(), allTDCSaleOrders.get(i).getSelectedCustomerType());


                if (allTDCSaleOrders.get(i).getSelectedCustomerType() == 1) {
                    code=(String.format("R%05d", allTDCSaleOrders.get(i).getSelectedCustomerId()));

                } else {
                    code=(String.format("C%05d", allTDCSaleOrders.get(i).getSelectedCustomerId()));

                }
              /*  for (int j=0;j<customerList.size();j++){

                    if (customerList.get(j).getCustomerType() == 1) {
                        code=(String.format("R%05d", customerList.get(j).getId()));

                    } else {
                        code=(String.format("C%05d", customerList.get(j).getId()));

                    }
                }
*/

                    String[] temp = new String[6];

                temp[0] =name;
                temp[1] = code;
                temp[2] = billno;
                temp[3] = billdate;
                temp[4] = billamount;
                temp[5] = str_selectedretailername;


                selectedList.add(temp);
            }


            final double finalAmount = amount;
            sales_print.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        int pageheight = 300 + selectedList.size() * 150;
        Bitmap bmOverlay = Bitmap.createBitmap(400, pageheight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#000000"));
        paint.setTextSize(26);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(mmSharedPreferences.getString("companyname"), 5, 20, paint);
        paint.setTextSize(20);
        canvas.drawText(mmSharedPreferences.getString("loginusername"), 5, 50, paint);
        paint.setTextSize(20);
        canvas.drawText("----------------------------------------------------", 5, 80, paint);
        paint.setTextSize(20);
        canvas.drawText("SALES LIST", 100, 110, paint);




        int st = 130;
        paint.setTextSize(20);
        // for (Map.Entry<String, String[]> entry : selectedList.entrySet()) {
        for (int i = 0; i <selectedList.size(); i++) {
            String[] temps = selectedList.get(i);
            canvas.drawText(temps[0], 5, st, paint);
           // canvas.drawText("," + "(" +temps[1] + ")", 150, st, paint);


            st = st + 30;
            canvas.drawText("BILL #", 5, st, paint);
            canvas.drawText( ": " + temps[2], 150, st, paint);

            st = st + 30;
            canvas.drawText("DATE", 5, st, paint);
            canvas.drawText( ": " + temps[3], 150, st, paint);


            st = st + 30;
            canvas.drawText("VALUE", 5, st, paint);
            canvas.drawText( ": " + temps[4], 150, st, paint);

            st = st + 45;


        }

        canvas.drawText("----------------------------------------------------", 5, st, paint);

        st = st + 20;
        canvas.drawText("VALUE:", 5, st, paint);
        //canvas.drawText( ": " + Utility.getFormattedCurrency(mProductsPriceAmountSum), 150, st, paint);
        canvas.drawText( ": " + Utility.getFormattedCurrency(finalAmount), 150, st, paint);


        //  canvas.drawText(Utility.getFormattedCurrency(mTotalProductsTax), 70, st, paint);
        //  canvas.drawText(Utility.getFormattedCurrency(mProductsPriceAmountSum), 170, st, paint);
        // canvas.drawText(Utility.getFormattedCurrency(mTotalProductsPriceAmountSum), 280, st, paint);
        st = st + 20;
        canvas.drawText("--------X---------", 100, st, paint);
        com.szxb.api.jni_interface.api_interface.printBitmap(bmOverlay, 5, 5);
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
        if (TroipsTakeorder.equals("TDC")) {
            Intent intent = new Intent(this, TDCSalesActivity.class);

            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, AgentsActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void loadTDCSalesList(View view) {
        int tag = Integer.parseInt(view.getTag().toString());

        CustomProgressDialog.showProgressDialog(activityContext, Constants.LOADING_MESSAGE);

        if (tag == 0) {
            tdc_sales_today.setBackground(border_accent);
            tdc_sales_today.setTextColor(colorAccent);

            tdc_sales_weekly.setBackground(border_btn);
            tdc_sales_weekly.setTextColor(colorPrimary);

           // tdc_sales_monthly.setBackground(border_btn);
           // tdc_sales_monthly.setTextColor(colorPrimary);
        } else if (tag == 1) {
            tdc_sales_today.setBackground(border_btn);
            tdc_sales_today.setTextColor(colorPrimary);

            tdc_sales_weekly.setBackground(border_accent);
            tdc_sales_weekly.setTextColor(colorAccent);

           // tdc_sales_monthly.setBackground(border_btn);
           // tdc_sales_monthly.setTextColor(colorPrimary);
        } else if (tag == 2) {
            tdc_sales_today.setBackground(border_btn);
            tdc_sales_today.setTextColor(colorPrimary);

            tdc_sales_weekly.setBackground(border_btn);
            tdc_sales_weekly.setTextColor(colorPrimary);

           // tdc_sales_monthly.setBackground(border_accent);
           // tdc_sales_monthly.setTextColor(colorAccent);
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


            if(screenType.equals("customerDetails")){
                allTDCSaleOrders = mDBHelper.fetchAllTDCSalesOrdersForSelectedDuration(startDateStr, endDateStr, custId);
            }else{
                allTDCSaleOrders = mDBHelper.fetchAllTDCSalesOrdersForSelectedDuration(startDateStr, endDateStr, "");
            }
           // allTDCSaleOrders = mDBHelper.fetchAllTDCSalesOrdersForSelectedDuration(startDateStr, endDateStr, "");
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

                int noOfItems = 0;
                double amount = 0;

                for (TDCSaleOrder order : allTDCSaleOrders) {
                    amount =  amount+ order.getOrderSubTotal();
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
