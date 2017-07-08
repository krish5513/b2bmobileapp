package com.rightclickit.b2bsaleon.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TDCSalesPreviewAdapter;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.services.SyncTDCSalesOrderService;
import com.rightclickit.b2bsaleon.services.SyncTakeOrdersService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.Utility;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sales_Preview_PrintActivity extends AppCompatActivity {
    private Context applicationContext, activityContext;
    private MMSharedPreferences mmSharedPreferences;

    private TextView sale_no_text_view, sale_date_time_text_view, total_tax_amount_text_view, total_amount_text_view, sub_total_amount_text_view;
    private ListView tdc_products_list_preview;

    private TDCSaleOrder currentOrder;
    private TDCSalesPreviewAdapter tdcSalesPreviewAdapter;
    private DBHelper mDBHelper;
    private String loggedInUserName;

    TextView company_name, route_name, route_code, user_name, sales_print;
    String amount, subtotal, taxAmount, name;
    double mrp, quantity;
    String subtaxAmount, subAmount, finalAmount;

    String currentDate, str_routecode, str_enguiryid, str_agentname;
    Map<String, String[]> selectedList = new HashMap<String, String[]>();
    private long previousOrderId;
    private String currentOrderId;
    private boolean isOrderAlreadySaved = false;
  //  ArrayList<String[]> selectedList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_preview_print);

        try {
            applicationContext = getApplicationContext();
            activityContext = Sales_Preview_PrintActivity.this;

            mmSharedPreferences = new MMSharedPreferences(applicationContext);
            loggedInUserName = mmSharedPreferences.getString("loginusername");

            mDBHelper = new DBHelper(Sales_Preview_PrintActivity.this);

            final ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;

            actionBar.setTitle("SALE INVOICE");
            actionBar.setSubtitle(null);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

            sales_print = (TextView) findViewById(R.id.tdc_sales_print);
            company_name = (TextView) findViewById(R.id.tdc_sales_company_name);
            user_name = (TextView) findViewById(R.id.tdc_sales_user_name);
            route_name = (TextView) findViewById(R.id.tdc_sales_route_name);
            route_code = (TextView) findViewById(R.id.tdc_sales_route_code);
            sale_no_text_view = (TextView) findViewById(R.id.tdc_sales_sale_no);
            sale_date_time_text_view = (TextView) findViewById(R.id.tdc_sales_date_time);
            total_tax_amount_text_view = (TextView) findViewById(R.id.total_tax_amount);
            total_amount_text_view = (TextView) findViewById(R.id.total_amount);
            sub_total_amount_text_view = (TextView) findViewById(R.id.sub_total_amount);
            tdc_products_list_preview = (ListView) findViewById(R.id.tdc_sales_products_list_preview);

            currentDate = Utility.formatDate(new Date(), Constants.DATE_DISPLAY_FORMAT);
            str_routecode = (mmSharedPreferences.getString("routecode") + ",");

            previousOrderId = mDBHelper.getTDCSalesMaxOrderNumber();
            currentOrderId = String.format("TDC%05d", previousOrderId + 1);

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {


                currentOrder = (TDCSaleOrder) bundle.getSerializable(Constants.BUNDLE_TDC_SALE_CURRENT_ORDER_PREVIEW);

                updateUIWithBundleValues(currentOrder);

                subtaxAmount = Utility.getFormattedCurrency(currentOrder.getOrderTotalTaxAmount());
                //Log.i("fdgh", subtaxAmount + "");
                subAmount = Utility.getFormattedCurrency(currentOrder.getOrderTotalAmount());
                finalAmount = Utility.getFormattedCurrency(currentOrder.getOrderSubTotal());
                Map<String, ProductsBean> productsList = currentOrder.getProductsList();
              //  selectedList=productsList.size();
                for (Map.Entry<String, ProductsBean> productsBeanEntry : productsList.entrySet()) {
                    ProductsBean productsBean = productsBeanEntry.getValue();
                    name = String.valueOf(productsBean.getProductTitle().replace(",", ""));
                    Log.i("prod.name", name);
                    quantity = productsBean.getSelectedQuantity();
                    mrp = Double.parseDouble(productsBean.getProductConsumerPrice().replace(",", ""));
                    subtotal = Utility.getFormattedCurrency(productsBean.getProductAmount());
                    taxAmount = Utility.getFormattedCurrency(productsBean.getTaxAmount());
                    String[] temp = new String[5];
                    temp[0] = name;
                    temp[1] = String.valueOf(quantity);
                    temp[2] = String.valueOf(mrp);
                    temp[3] = String.valueOf(subtotal);
                    temp[4] = String.valueOf(taxAmount);
                    selectedList.put(name, temp);
                    //Log.i("pushtemp", temp + "");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        sales_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pageheight = 300 + selectedList.size() * 60;
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
                canvas.drawText(mmSharedPreferences.getString("companyname"), 5, 50, paint);
                paint.setTextSize(20);
                canvas.drawText(str_routecode, 5, 80, paint);
                canvas.drawText(mmSharedPreferences.getString("routename"), 200, 80, paint);
                canvas.drawText("BILL,", 5, 120, paint);
                canvas.drawText("by " + mmSharedPreferences.getString("loginusername"), 200, 120, paint);
                canvas.drawText(currentOrderId, 5, 150, paint);
                canvas.drawText(currentDate, 170, 150, paint);
                //  canvas.drawText(str_agentname, 5, 180, paint);
                //  canvas.drawText(mmSharedPreferences.getString("agentCode"), 200, 180, paint);

                canvas.drawText("----------------------------------------------------", 5, 180, paint);
                canvas.drawText("Product", 5, 220, paint);
                canvas.drawText("Qty", 110, 220, paint);
                canvas.drawText("Price", 160, 220, paint);
                canvas.drawText("Amount", 230, 220, paint);
                canvas.drawText("Tax", 330, 220, paint);
                canvas.drawText("----------------------------------------------------", 5, 235, paint);

                int st = 250;
                paint.setTextSize(17);
                for (Map.Entry<String, String[]> entry : selectedList.entrySet()) {
                    String[] temps = entry.getValue();
                    canvas.drawText(temps[0], 5, st, paint);
                    canvas.drawText(temps[1], 115, st, paint);
                    canvas.drawText(temps[2], 150, st, paint);
                    canvas.drawText(temps[3], 190, st, paint);
                    canvas.drawText(temps[4], 300, st, paint);
                    // canvas.drawText("FROM:" + temps[7], 100, st, paint);
                    //canvas.drawText("TO:" + temps[8], 250, st, paint);

                    st = st + 30;
                    //  canvas.drawText("----------------------------------------------------", 5, st, paint);
                }

                canvas.drawText("----------------------------------------------------", 5, st, paint);

                st = st + 20;
                canvas.drawText("Total:", 5, st, paint);
                canvas.drawText(subtaxAmount, 70, st, paint);
                canvas.drawText(subAmount, 170, st, paint);
                canvas.drawText(finalAmount, 280, st, paint);
                st = st + 20;
                canvas.drawText("--------X---------", 100, st, paint);
                com.szxb.api.jni_interface.api_interface.printBitmap(bmOverlay, 5, 5);
            }
        });
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
            company_name.setText(mmSharedPreferences.getString("companyname"));
            user_name.setText("by " + loggedInUserName);
            route_name.setText(mmSharedPreferences.getString("routename"));
            route_code.setText(str_routecode);
            sale_no_text_view.setText(currentOrderId);
            sale_date_time_text_view.setText(currentDate);

            ArrayList<ProductsBean> selectedProductsList = new ArrayList<>(saleOrder.getProductsList().values());
            tdcSalesPreviewAdapter = new TDCSalesPreviewAdapter(activityContext, this, selectedProductsList);
            tdc_products_list_preview.setAdapter(tdcSalesPreviewAdapter);

            total_tax_amount_text_view.setText(Utility.getFormattedCurrency(saleOrder.getOrderTotalTaxAmount()));
            total_amount_text_view.setText(Utility.getFormattedCurrency(saleOrder.getOrderTotalAmount()));
            sub_total_amount_text_view.setText(Utility.getFormattedCurrency(saleOrder.getOrderSubTotal()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveTDCSaleOrder(View view) {
        try {
            if (currentOrder != null && !isOrderAlreadySaved) {
                currentOrder.setCreatedBy(loggedInUserName);
                currentOrder.setCreatedOn(currentDate);

                long orderId = mDBHelper.insertIntoTDCSalesOrdersTable(currentOrder);

                if (orderId == -1)
                    Toast.makeText(activityContext, "An error occurred while saving order.", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(activityContext, "Order Saved Successfully.", Toast.LENGTH_LONG).show();
                    isOrderAlreadySaved = true;

                    if (new NetworkConnectionDetector(activityContext).isNetworkConnected()) {
                        Intent syncTDCOrderServiceIntent = new Intent(activityContext, SyncTDCSalesOrderService.class);
                        //syncTDCOrderServiceIntent.putExtra(Constants.BUNDLE_TDC_SALE_ORDER, currentOrder);
                        startService(syncTDCOrderServiceIntent);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}