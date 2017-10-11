package com.rightclickit.b2bsaleon.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TDCSalesPreviewAdapter;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.customviews.CustomAlertDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.services.SyncTDCSalesOrderService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.Utility;

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
    String amount, subtotal, taxAmount, name, hssnnumber, cgst, sgst;
    double mrp, quantity;
    String subtaxAmount, subAmount, finalAmount;

    String currentDate, str_routecode, str_enguiryid, str_agentname, id;
    long currentTimeStamp;

    private long previousOrderId;
    private String currentOrderId;
    private boolean isOrderAlreadySaved = false;
    ArrayList<String[]> selectedList;
    private String requestCameFrom, saleQuantity = "", actualSaleQuantity = "";
    private Map<String, String> saleQuantityListMap = new HashMap<String, String>();

    String totalRate = "";
    double taxes;
    double rate = 0.0, ratetax=0.0, taxvalue=0.0;
    Map<String, ProductsBean> productsList;
    ProductsBean productsBean;

    double per;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_preview_print);

        try {
            applicationContext = getApplicationContext();
            activityContext = TDCSales_Preview_PrintActivity.this;

            mmSharedPreferences = new MMSharedPreferences(TDCSales_Preview_PrintActivity.this);
            loggedInUserId = mmSharedPreferences.getString("userId");
            loggedInUserName = (mmSharedPreferences.getString("loginusername") + ",");
            str_routecode = mmSharedPreferences.getString("routecode");
            //str_agentname = mmSharedPreferences.getString("agentNameAdapter");
            str_enguiryid = mmSharedPreferences.getString("agentCodeAdapter");

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
           // user_name = (TextView) findViewById(R.id.tdc_sales_user_name);
            //route_name = (TextView) findViewById(R.id.tdc_sales_route_name);
            //route_code = (TextView) findViewById(R.id.tdc_sales_route_code);
            sale_no_text_view = (TextView) findViewById(R.id.tdc_sales_sale_no);
            sale_date_time_text_view = (TextView) findViewById(R.id.tdc_sales_date_time);
            total_tax_amount_text_view = (TextView) findViewById(R.id.total_tax_amount);
            total_amount_text_view = (TextView) findViewById(R.id.total_amount);
            sub_total_amount_text_view = (TextView) findViewById(R.id.sub_total_amount);
            tdc_products_list_preview = (ListView) findViewById(R.id.tdc_sales_products_list_preview);
            tdc_sales_save_layout = (LinearLayout) findViewById(R.id.tdc_sales_save_layout);

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                str_agentname=bundle.getString("CustomerName");
                currentOrder = (TDCSaleOrder) bundle.getSerializable(Constants.BUNDLE_TDC_SALE_CURRENT_ORDER_PREVIEW);
                requestCameFrom = bundle.getString(Constants.BUNDLE_REQUEST_FROM);
                id = bundle.getString("incid");
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
                    System.out.println("1111111111111 :::"+currentOrder.getSelectedCustomerId());
                    System.out.println("2222222222222 :::"+currentOrder.getSelectedCustomerType());
                   // if (currentOrder.getSelectedCustomerId() > 0 && currentOrder.getSelectedCustomerType() == 1) {

                    //if (currentOrder.getSelectedCustomerId() > 0 && currentOrder.getSelectedCustomerType() == 1) {

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

                            Double gst = 0.0, vat = 0.0;
                            if (productsBean.getProductgst() != null) {
                                gst = Double.parseDouble(productsBean.getProductgst());
                            }
                            if (productsBean.getProductvat() != null) {
                                vat = Double.parseDouble(productsBean.getProductvat());
                            }
                            double taxAmount = gst + vat;

                            double rate = productsBean.getProductRatePerUnit();
                            System.out.println(" RATE:: "+ rate);
                            double per = taxAmount / 100;
                            System.out.println(" PERCENAGE:: "+ per);
                            double ratetax = 0.0;
                            try {
                                ratetax = rate * per;
                                System.out.println(" RATE TAX:: "+ ratetax);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            String totalRate = String.valueOf((rate - ratetax));
                            System.out.println(" TOTAL RATE:: "+ totalRate);
                            double amount = Double.parseDouble(totalRate) * productsBean.getSelectedQuantity();
                            System.out.println("  AMOUNT:: "+ amount);
                            taxAmount = amount * per;
                            System.out.println(" TAX AMOUNT:: "+ taxAmount);

                            productsBean.setProductAmount(amount);
                            productsBean.setTaxAmount(taxAmount);

                            totalAmount = totalAmount + productsBean.getProductAmount();
                            System.out.println(" TOTAL AMOUNT:: "+ totalAmount);
                            totalTaxAmount = totalTaxAmount + productsBean.getTaxAmount();
                            System.out.println("TOTAL TAX AMOUNT:: "+ totalTaxAmount);
                            subTotal = totalAmount + totalTaxAmount;
                            System.out.println("SUB TOTAL:: "+ subTotal);
                        }

                        currentOrder.setOrderTotalAmount(totalAmount);
                        currentOrder.setOrderTotalTaxAmount(totalTaxAmount);
                        currentOrder.setOrderSubTotal(subTotal);

                   // }

                    //}


                } else {
                    currentOrderId = String.format("TDC%05d", currentOrder.getOrderId());
                    currentDate = Utility.formatTime(currentOrder.getCreatedOn(), Constants.TDC_SALE_INFO_DATE_DISPLAY_FORMAT);

                    tdc_sales_save_layout.setVisibility(View.GONE);
                }

                updateUIWithBundleValues(currentOrder);

                subtaxAmount = Utility.getFormattedCurrency(currentOrder.getOrderTotalTaxAmount());
                subAmount = Utility.getFormattedCurrency(currentOrder.getOrderTotalAmount());
                finalAmount = Utility.getFormattedCurrency(currentOrder.getOrderSubTotal());
                productsList = currentOrder.getProductsList();
                selectedList = new ArrayList<>(productsList.size());

                Log.i("selectedlist00",selectedList.size()+"");

                if (productsList != null) {
                    for (Map.Entry<String, ProductsBean> productsBeanEntry : productsList.entrySet()) {
                        productsBean = productsBeanEntry.getValue();
                        name = String.valueOf(productsBean.getProductTitle().replace(",", ""));
                        quantity = productsBean.getSelectedQuantity();
                        // mrp = productsBean.getProductRatePerUnit();
                        // subtotal = Utility.getFormattedCurrency(productsBean.getProductAmount());
                        // taxAmount = Utility.getFormattedCurrency(productsBean.getTaxAmount());
                        if (productsBean.getControlCode() != null) {
                            hssnnumber = productsBean.getControlCode();
                        } else {
                            hssnnumber = "-";
                        }
                        if (productsBean.getProductgst() != null) {
                            cgst = productsBean.getProductgst() + "%";
                        } else {
                            cgst = "0.00%";
                        }
                        if (productsBean.getProductvat() != null) {
                            sgst = productsBean.getProductvat() + "%";
                        } else {
                            sgst = "0.00%";
                        }
/*
                        try {
                            if (productsBean.getProductgst() != null && productsBean.getProductvat() != null) {


                                taxes = String.valueOf(Double.parseDouble(productsBean.getProductgst())
                                        + Double.parseDouble(productsBean.getProductvat()));
                            } else {
                                taxes = "0.00%";
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/

                        rate = productsBean.getProductRatePerUnit();




                        try {
                            Double gst = 0.0,vat = 0.0;
                            if(productsBean.getProductgst()!=null){
                                gst = Double.parseDouble(productsBean.getProductgst());
                            }
                            if(productsBean.getProductvat()!=null){
                                vat = Double.parseDouble(productsBean.getProductvat());
                            }
                            taxes = gst + vat;
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                        try {
                         per = taxes / 100; // Added extra...
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            ratetax = rate * per;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        totalRate = String.valueOf((rate - ratetax));
                        mrp = (Double.parseDouble(totalRate));
                        double qty = Double.parseDouble(String.format("%.3f", productsBean.getSelectedQuantity()));
                        double amount = Double.parseDouble(totalRate) * productsBean.getSelectedQuantity();
                        double salevalue = productsBean.getSelectedQuantity() * rate;
                        try {
                            taxvalue = amount * per;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Double sbT = amount + taxvalue;
                        subtotal = Utility.getFormattedCurrency(sbT); // instead of sbT -- salevalue
                        taxAmount = Utility.getFormattedCurrency(taxvalue);


                        String[] temp = new String[11];
                        temp[0] = name;
                        temp[7] = String.valueOf(quantity);
                        temp[8] = Utility.getFormattedCurrency(mrp);
                        temp[9] = subtotal;
                        temp[10] = taxAmount;
                        temp[3] = hssnnumber;
                        temp[4] = cgst;
                        temp[5] = sgst;
                        temp[1] = productsBean.getProductCode();
                        temp[2] = productsBean.getProductUOM();
                        temp[6] = String.valueOf(taxes);
                        // temp[11]=id;
                        selectedList.add(temp);

                        Log.i("selectedlist11",selectedList.size()+"");
//                        System.out.println("NAME::: "+ name + "\n");
//                        System.out.println("QUAN::: "+ quantity + "\n");
//                        System.out.println("MRP::: "+ mrp + "\n");
//                        System.out.println("SUBTOTAL::: "+ subtotal + "\n");
//                        System.out.println("TAXMAOUNT::: "+ taxAmount + "\n");
//                        System.out.println("HSSNNUM::: "+ hssnnumber + "\n");
//                        System.out.println("CGST::: "+ cgst + "\n");
//                        System.out.println("SGST::: "+ sgst + "\n");
//                        System.out.println("PROCODE::: "+ productsBean.getProductCode() + "\n");
//                        System.out.println("PROUOM::: "+ productsBean.getProductUOM() + "\n");
//                        System.out.println("TAXES::: "+ taxes + "\n");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        sales_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOrderAlreadySaved = true) {


                    int pageheight = 550 + selectedList.size() * 200; // 2000 is old
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
//                canvas.drawText(mmSharedPreferences.getString("routename") + ",", 5, 80, paint);
//                paint.setTextSize(20);
//                canvas.drawText(str_routecode, 150, 80, paint);
//                paint.setTextSize(20);
//                canvas.drawText(mmSharedPreferences.getString("loginusername"), 5, 110, paint);
//                paint.setTextSize(20);
//                canvas.drawText("USER GST NUMBER", 5, 140, paint);
//                paint.setTextSize(20);
                    canvas.drawText("-------------------------------------------", 5, 50, paint);
                    paint.setTextSize(20);
                    canvas.drawText("BILL", 150, 80, paint);

                    paint.setTextSize(20);
                    canvas.drawText("BILL# ", 5, 110, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + "TDC-" + str_enguiryid + "-" + currentOrderId, 150, 110, paint);
                    paint.setTextSize(20);
                    canvas.drawText("DATE ", 5, 140, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + currentDate, 150, 140, paint);
                    paint.setTextSize(20);
                    canvas.drawText("CUSTOMER ", 5, 170, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + str_agentname, 150, 170, paint);
                    paint.setTextSize(20);
//                canvas.drawText("CODE ", 5, 320, paint);
//                paint.setTextSize(20);
//                canvas.drawText(": " + str_enguiryid, 150, 320, paint);
//                paint.setTextSize(20);
                    canvas.drawText("-------------------------------------------", 5, 200, paint);
                    paint.setTextSize(20);

                    int st = 230;
                    paint.setTextSize(17);
                    // for (Map.Entry<String, String[]> entry : selectedList.entrySet()) {

                    for (int i = 0; i < selectedList.size(); i++) {

                        Log.i("selectedlist", selectedList.size() + "");
                        String[] temps = selectedList.get(i);
                    /*paint.setTextSize(20);
                    canvas.drawText("#" + temps[11] , 5, st, paint);*/

                        paint.setTextSize(22);
                        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        canvas.drawText(temps[0] + "," + temps[1] + "( " + temps[2] + " )", 5, st, paint);
//                    paint.setTextSize(22);
//                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//                    canvas.drawText("," + temps[1], 150, st, paint);
//                    paint.setTextSize(22);
//                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//                    canvas.drawText("( " + temps[2] + " )", 240, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("HSSN CODE ", 5, st, paint);
                        canvas.drawText(": " + temps[3], 150, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("CGST,SGST " + ": " + temps[4] + " + " + temps[5] + " = " + temps[6], 5, st, paint);
//                    canvas.drawText(": " + temps[4], 150, st, paint);
//                    canvas.drawText(" + " + temps[5], 225, st, paint);
//                    canvas.drawText(" = " + temps[6], 300, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("QUANTITY ", 5, st, paint);
                        canvas.drawText(": " + temps[7], 150, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("RATE ", 5, st, paint);
                        canvas.drawText(": " + temps[8], 150, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("VALUE ", 5, st, paint);
                        canvas.drawText(": " + temps[9], 150, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("TAX VALUE ", 5, st, paint);
                        canvas.drawText(": " + temps[10], 150, st, paint);


                        st = st + 40;
                        //  canvas.drawText("----------------------------------------------------", 5, st, paint);
                    }
                    st = st + 5;
                    paint.setTextSize(20);
                    canvas.drawText("-------------------------------------------", 5, st, paint);

                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText(" SALE VALUE ", 5, st, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + finalAmount, 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("(VALUE+TAX) :" + "(" + subAmount + " + " + subtaxAmount + ")", 5, st, paint);
                    //paint.setTextSize(20);
                    //canvas.drawText("(" +  subAmount + " + " +  subtaxAmount + ")", 100, st, paint);
                    st = st + 30;
                    canvas.drawText("--------X---------", 100, st, paint);
                    com.szxb.api.jni_interface.api_interface.printBitmap(bmOverlay, 5, 5);
                }else {
                    CustomAlertDialog.showAlertDialog(activityContext, "Failed", getResources().getString(R.string.print));
                }
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
        menu.findItem(R.id.sort).setVisible(false);
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

           // user_name.setText("by " + loggedInUserName);
           // route_name.setText(mmSharedPreferences.getString("routename"));
           // route_code.setText(str_routecode);
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
                        //System.out.println("F====KEY::: " + stockEntry.getKey() + "\n");
                        //System.out.println("F=====VALUE::: " + stockEntry.getValue() + "\n");
                        String saleQuantity = mDBHelper.getSaleQuantityByAgentAndProductId(loggedInUserId, stockEntry.getKey());
                        //System.out.println("SALE QUAN::: " + saleQuantity);
                        double finalSaleVal = Double.parseDouble(saleQuantity) + Double.parseDouble(stockEntry.getValue());
                        mDBHelper.updateAgentStockAfterTDCSales(loggedInUserId, stockEntry.getKey(), String.valueOf(finalSaleVal));
                        String saleQuantity1 = mDBHelper.getSaleQuantityByAgentAndProductId(loggedInUserId, stockEntry.getKey());
                        //System.out.println("After Update SALE QUAN::: " + saleQuantity1);
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