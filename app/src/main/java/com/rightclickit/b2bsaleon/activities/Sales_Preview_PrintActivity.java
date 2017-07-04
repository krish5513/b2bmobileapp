package com.rightclickit.b2bsaleon.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TDCSalesPreviewAdapter;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;

public class Sales_Preview_PrintActivity extends AppCompatActivity {
    private Context applicationContext, activityContext;
    private MMSharedPreferences mmSharedPreferences;

    private TextView sale_no_text_view, sale_date_time_text_view, user_name_text_view, total_tax_amount_text_view, total_amount_text_view, sub_total_amount_text_view;
    private ListView tdc_products_list_preview;

    private TDCSaleOrder currentOrder;
    private TDCSalesPreviewAdapter tdcSalesPreviewAdapter;
    private DBHelper mDBHelper;
    private String loogedInUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_preview_print);

        try {
            applicationContext = getApplicationContext();
            activityContext = Sales_Preview_PrintActivity.this;

            mmSharedPreferences = new MMSharedPreferences(applicationContext);
            loogedInUserName = mmSharedPreferences.getString("loogedInUserName");

            final ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;

            actionBar.setTitle("SALE INVOICE");
            actionBar.setSubtitle(null);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

            sale_no_text_view = (TextView) findViewById(R.id.sale_no);
            sale_date_time_text_view = (TextView) findViewById(R.id.sale_date_time);
            user_name_text_view = (TextView) findViewById(R.id.user_name);
            total_tax_amount_text_view = (TextView) findViewById(R.id.total_tax_amount);
            total_amount_text_view = (TextView) findViewById(R.id.total_amount);
            sub_total_amount_text_view = (TextView) findViewById(R.id.sub_total_amount);

            tdc_products_list_preview = (ListView) findViewById(R.id.tdc_products_list_preview);

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                currentOrder = (TDCSaleOrder) bundle.getSerializable(Constants.BUNDLE_TDC_SALE_CURRENT_ORDER_PREVIEW);

                updateUIWithBundleValues(currentOrder);
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
        /*if (id == R.id.Add) {
            Intent i = new Intent(Sales_Preview_PrintActivity.this, Sales_Retailer_AddActivity.class);
            startActivity(i);
            finish();
            return true;
        }*/

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

        Intent intent = new Intent(this, SalesCustomerSelectionActivity.class);
        intent.putExtra(Constants.BUNDLE_TDC_SALE_ORDER, currentOrder); // to handle back button
        startActivity(intent);
        finish();
    }

    public void updateUIWithBundleValues(TDCSaleOrder saleOrder) {
        try {
            sale_no_text_view.setText(String.format("TDC%05d", 1));
            sale_date_time_text_view.setText(Utility.formatDate(new Date(), Constants.DATE_DISPLAY_FORMAT));
            user_name_text_view.setText(loogedInUserName);

            tdcSalesPreviewAdapter = new TDCSalesPreviewAdapter(activityContext, this, saleOrder.getProductsList());
            tdc_products_list_preview.setAdapter(tdcSalesPreviewAdapter);

            total_tax_amount_text_view.setText(Utility.getFormattedCurrency(saleOrder.getOrderTotalTaxAmount()));
            total_amount_text_view.setText(Utility.getFormattedCurrency(saleOrder.getOrderTotalAmount()));
            sub_total_amount_text_view.setText(Utility.getFormattedCurrency(saleOrder.getOrderSubTotal()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}