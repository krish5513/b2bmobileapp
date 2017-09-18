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
import com.rightclickit.b2bsaleon.beanclass.SpecialPriceBean;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.customviews.CustomAlertDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.services.SyncTDCSalesOrderService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.Utility;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TDCSales_Preview_PrintActivity extends AppCompatActivity {
    private Context applicationContext, activityContext;
    private MMSharedPreferences mmSharedPreferences;

    private TextView sale_no_text_view, sale_date_time_text_view, total_tax_amount_text_view, total_amount_text_view, sub_total_amount_text_view;
    private ListView tdc_products_list_preview;
    private LinearLayout tdc_sales_save_layout;

    private TDCSaleOrder currentOrder;
    private TDCSalesPreviewAdapter tdcSalesPreviewAdapter;
    private DBHelper mDBHelper;
    private String loggedInUserId, loggedInUserName;

    TextView company_name, route_name, route_code, user_name, sales_print;
    String amount, subtotal, taxAmount, name;
    double mrp, quantity;
    String subtaxAmount, subAmount, finalAmount;

    String currentDate, str_routecode, str_enguiryid, str_agentname;
    long currentTimeStamp;

    private long previousOrderId;
    private String currentOrderId;
    private boolean isOrderAlreadySaved = false;
    ArrayList<String[]> selectedList;
    private String requestCameFrom, saleQuantity = "", actualSaleQuantity = "";
    private Map<String, String> saleQuantityListMap = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_preview_print);

        try {
            applicationContext = getApplicationContext();
            activityContext = TDCSales_Preview_PrintActivity.this;

            mmSharedPreferences = new MMSharedPreferences(applicationContext);
            loggedInUserId = mmSharedPreferences.getString("userId");
            loggedInUserName = mmSharedPreferences.getString("loginusername");
            str_routecode = (mmSharedPreferences.getString("routecode") + ",");

            mDBHelper = new DBHelper(TDCSales_Preview_PrintActivity.this);

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
            tdc_sales_save_layout = (LinearLayout) findViewById(R.id.tdc_sales_save_layout);

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                currentOrder = (TDCSaleOrder) bundle.getSerializable(Constants.BUNDLE_TDC_SALE_CURRENT_ORDER_PREVIEW);
                requestCameFrom = bundle.getString(Constants.BUNDLE_REQUEST_FROM);

                if (requestCameFrom.equals(Constants.BUNDLE_REQUEST_FROM_TDC_CUSTOMER_SELECTION)) {
                    saleQuantity = bundle.getString(Constants.BUNDLE_TDC_SALE_QUANTITY);
                    actualSaleQuantity = saleQuantity;
                    // System.out.println("FINAL SQ=====" + actualSaleQuantity);
                    saleQuantity = saleQuantity.substring(1, saleQuantity.length() - 1);           //remove curly brackets
                    String[] keyValuePairs = saleQuantity.split(",");              //split the string to creat key-value pairs

                    for (String pair : keyValuePairs)                        //iterate over the pairs
                    {
                        String[] entry = pair.split("=");                   //split the pairs to get key and value
                        saleQuantityListMap.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
                    }

                    currentTimeStamp = System.currentTimeMillis();
                    currentDate = Utility.formatTime(currentTimeStamp, Constants.TDC_SALE_INFO_DATE_DISPLAY_FORMAT);

                    previousOrderId = mDBHelper.getTDCSalesMaxOrderNumber();
                    currentOrderId = String.format("TDC%05d", previousOrderId + 1);

                    // Checking weather the selected customer is retailer or not, if yes checking weather he has any special prices.
                    if (currentOrder.getSelectedCustomerId() > 0 && currentOrder.getSelectedCustomerType() == 1) {
                        Map<String, String> specialPriceList = mDBHelper.fetchSpecialPricesForUserId(currentOrder.getSelectedCustomerUserId()); // Getting special prices for selected retailer from local db.

                        double totalAmount = 0, totalTaxAmount = 0, subTotal = 0;

                        for (Map.Entry<String, ProductsBean> productsBeanEntry : currentOrder.getProductsList().entrySet()) {
                            ProductsBean productsBean = productsBeanEntry.getValue();

                            // Checking weather product has any special price or not.
                            if (specialPriceList.size() > 0 && specialPriceList.containsKey(productsBean.getProductId())) {
                                Double specialPrice = Double.parseDouble(specialPriceList.get(productsBean.getProductId()));

                                if (specialPrice > 0) {
                                    productsBean.setProductRatePerUnit(specialPrice);
                                }
                            } else {
                                String productRetailerPrice = productsBean.getProductRetailerPrice();

                                if (productRetailerPrice != null) {
                                    Double productPrice = Double.parseDouble(productRetailerPrice);

                                    if (productPrice > 0) {
                                        productsBean.setProductRatePerUnit(productPrice);
                                    }
                                }
                            }

                            double amount = productsBean.getProductRatePerUnit() * productsBean.getSelectedQuantity();
                            double taxAmount = (amount * productsBean.getProductTaxPerUnit()) / 100;

                            productsBean.setProductAmount(amount);
                            productsBean.setTaxAmount(taxAmount);

                            totalAmount = totalAmount + productsBean.getProductAmount();
                            totalTaxAmount = totalTaxAmount + productsBean.getTaxAmount();
                            subTotal = totalAmount + totalTaxAmount;
                        }

                        currentOrder.setOrderTotalAmount(totalAmount);
                        currentOrder.setOrderTotalTaxAmount(totalTaxAmount);
                        currentOrder.setOrderSubTotal(subTotal);
                    }

                } else {
                    currentOrderId = String.format("TDC%05d", currentOrder.getOrderId());
                    currentDate = Utility.formatTime(currentOrder.getCreatedOn(), Constants.TDC_SALE_INFO_DATE_DISPLAY_FORMAT);

                    tdc_sales_save_layout.setVisibility(View.GONE);
                }

                updateUIWithBundleValues(currentOrder);

                subtaxAmount = Utility.getFormattedCurrency(currentOrder.getOrderTotalTaxAmount());
                subAmount = Utility.getFormattedCurrency(currentOrder.getOrderTotalAmount());
                finalAmount = Utility.getFormattedCurrency(currentOrder.getOrderSubTotal());

                Map<String, ProductsBean> productsList = currentOrder.getProductsList();
                selectedList = new ArrayList<>(productsList.size());

                if (productsList != null) {
                    for (Map.Entry<String, ProductsBean> productsBeanEntry : productsList.entrySet()) {
                        ProductsBean productsBean = productsBeanEntry.getValue();
                        name = String.valueOf(productsBean.getProductTitle().replace(",", ""));
                        quantity = productsBean.getSelectedQuantity();
                        mrp = productsBean.getProductRatePerUnit();
                        subtotal = Utility.getFormattedCurrency(productsBean.getProductAmount());
                        taxAmount = Utility.getFormattedCurrency(productsBean.getTaxAmount());

                        String[] temp = new String[5];
                        temp[0] = name;
                        temp[1] = String.valueOf(quantity);
                        temp[2] = String.valueOf(mrp);
                        temp[3] = String.valueOf(subtotal);
                        temp[4] = String.valueOf(taxAmount);
                        selectedList.add(temp);
                    }
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
                paint.setTextSize(20);
                canvas.drawText(mmSharedPreferences.getString("routename"), 200, 80, paint);
                paint.setTextSize(20);
                canvas.drawText("BILL,", 5, 120, paint);
                paint.setTextSize(20);
                canvas.drawText("by " + mmSharedPreferences.getString("loginusername"), 200, 120, paint);
                paint.setTextSize(20);
                canvas.drawText(currentOrderId, 5, 150, paint);
                paint.setTextSize(20);
                canvas.drawText(currentDate, 170, 150, paint);
                paint.setTextSize(20);
                //  canvas.drawText(str_agentname, 5, 180, paint);
                //  canvas.drawText(mmSharedPreferences.getString("agentCode"), 200, 180, paint);

                canvas.drawText("----------------------------------------------------", 5, 180, paint);
                canvas.drawText("Product", 5, 220, paint);
                paint.setTextSize(20);
                canvas.drawText("Qty", 110, 220, paint);
                paint.setTextSize(20);
                canvas.drawText("Price", 160, 220, paint);
                paint.setTextSize(20);
                canvas.drawText("Amount", 230, 220, paint);
                paint.setTextSize(20);
                canvas.drawText("Tax", 330, 220, paint);
                paint.setTextSize(20);
                canvas.drawText("----------------------------------------------------", 5, 235, paint);

                int st = 250;
                paint.setTextSize(17);
                // for (Map.Entry<String, String[]> entry : selectedList.entrySet()) {

                for (int i = 0; i < selectedList.size(); i++) {
                    String[] temps = selectedList.get(i);
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

        if (requestCameFrom.equals(Constants.BUNDLE_REQUEST_FROM_TDC_CUSTOMER_SELECTION)) {
            Intent intent = new Intent(this, TDCSalesCustomerSelectionActivity.class);
            intent.putExtra(Constants.BUNDLE_TDC_SALE_ORDER, currentOrder); // to handle back button
            intent.putExtra(Constants.BUNDLE_TDC_SALE_ORDER_SALE_QUNAT, actualSaleQuantity); // to handle back button
            startActivity(intent);
            finish();
        } else if (requestCameFrom.equals(Constants.BUNDLE_REQUEST_FROM_TDC_SALES_LIST)) {
            finish();
        }
    }

    public void updateUIWithBundleValues(TDCSaleOrder saleOrder) {
        try {
            company_name.setText(mmSharedPreferences.getString("companyname"));
            user_name.setText("by " + loggedInUserName);
            route_name.setText(mmSharedPreferences.getString("routename"));
            route_code.setText(str_routecode);
            sale_no_text_view.setText(currentOrderId + ",");
            sale_date_time_text_view.setText(currentDate);

            if (saleOrder.getProductsList() != null) {
                ArrayList<ProductsBean> selectedProductsList = new ArrayList<>(saleOrder.getProductsList().values());
                tdcSalesPreviewAdapter = new TDCSalesPreviewAdapter(activityContext, this, selectedProductsList);
                tdc_products_list_preview.setAdapter(tdcSalesPreviewAdapter);
            }

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
                currentOrder.setCreatedBy(loggedInUserId);
                currentOrder.setCreatedOn(currentTimeStamp);
                currentOrder.setOrderDate(Utility.formatTime(currentTimeStamp, Constants.TDC_SALES_ORDER_DATE_SAVE_FORMAT));

                long orderId = mDBHelper.insertIntoTDCSalesOrdersTable(currentOrder);

                if (orderId == -1)
                    Toast.makeText(activityContext, "An error occurred while saving order.", Toast.LENGTH_LONG).show();
                else {
                   // Toast.makeText(activityContext, "Order Saved Successfully.", Toast.LENGTH_LONG).show();
                    CustomAlertDialog.showAlertDialog(activityContext, "Success", getResources().getString(R.string.database_details));
                    isOrderAlreadySaved = true;

                    for (Map.Entry<String, String> stockEntry : saleQuantityListMap.entrySet()) {
                        System.out.println("F====KEY::: " + stockEntry.getKey() + "\n");
                        System.out.println("F=====VALUE::: " + stockEntry.getValue() + "\n");
                        String saleQuantity = mDBHelper.getSaleQuantityByAgentAndProductId(loggedInUserId, stockEntry.getKey());
                        System.out.println("SALE QUAN::: " + saleQuantity);
                        double finalSaleVal = Double.parseDouble(saleQuantity) + Double.parseDouble(stockEntry.getValue());
                        mDBHelper.updateAgentStockAfterTDCSales(loggedInUserId, stockEntry.getKey(), String.valueOf(finalSaleVal));
                        String saleQuantity1 = mDBHelper.getSaleQuantityByAgentAndProductId(loggedInUserId, stockEntry.getKey());
                        System.out.println("After Update SALE QUAN::: " + saleQuantity1);
                    }

                    // after order was saved successfully, we are creating a new TDCSaleOrder object to clear previous selected products.
                    currentOrder = new TDCSaleOrder();
                    actualSaleQuantity = "";

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