package com.rightclickit.b2bsaleon.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TripSheetsPaymentPreviewDeliveredProductsAdapter;
import com.rightclickit.b2bsaleon.adapters.TripSheetsPaymentPreviewReturnedProductsAdapter;
import com.rightclickit.b2bsaleon.beanclass.PaymentsBean;
import com.rightclickit.b2bsaleon.beanclass.SaleOrderDeliveredProducts;
import com.rightclickit.b2bsaleon.beanclass.SaleOrderReturnedProducts;
import com.rightclickit.b2bsaleon.beanclass.TripsheetSOList;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class TripsheetPaymentsPreview extends AppCompatActivity {
    private Context activityContext;
    private MMSharedPreferences mmSharedPreferences;
    private DBHelper mDBHelper;

    TextView tv_companyName, tv_routecode, tv_route_name, tv_delivered_user_Name, tv_sale_order_no, tv_sale_order_date, tv_delivery_no, tv_delivery_date, price_total, tax_total_amount, sub_total,
            mode_of_payment, cheque_number, cheque_date, bank_name, opening_balance, sale_order_amount, received_amount, closing_balance;
    ListView delivered_products_list_view, returned_products_list_view;

    private String mTripSheetId = "", mAgentId = "", mAgentName = "", mTripSheetDate = "", mTripSheetCode = "", mAgentCode = "", mAgentRouteId = "", mAgentRouteCode = "", mAgentSoId = "", mAgentSoCode = "";
    private String loggedInUserId, loggedInUserName, companyName, routeCode, routeName, currentDate;
    private TripsheetSOList saleOrdersDetails = null;
    private ArrayList<SaleOrderDeliveredProducts> deliveredProductsList;
    private ArrayList<SaleOrderReturnedProducts> returnedProductsList;
    private PaymentsBean paymentsDetails = null;
    private double totalAmount = 0.0;
    private TripSheetsPaymentPreviewDeliveredProductsAdapter tripSheetsPaymentPreviewDeliveredProductsAdapter;
    private TripSheetsPaymentPreviewReturnedProductsAdapter tripSheetsPaymentPreviewReturnedProductsAdapter;
    TextView print;
    String name, hssnnumber, cgst, sgst, uom;
    double taxes;
    ArrayList<String[]> selectedList, cratesList;
    SaleOrderDeliveredProducts deliveredProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripsheet_payments_preview);

        try {
            this.getSupportActionBar().setTitle("PAYMENT SUMMARY");
            this.getSupportActionBar().setSubtitle(null);
            this.getSupportActionBar().setLogo(R.drawable.route_white);
            this.getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            this.getSupportActionBar().setDisplayShowHomeEnabled(true);

            final ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

            activityContext = TripsheetPaymentsPreview.this;
            mDBHelper = new DBHelper(activityContext);
            mmSharedPreferences = new MMSharedPreferences(activityContext);

            print = (TextView) findViewById(R.id.tv_print_print);

            tv_companyName = (TextView) findViewById(R.id.tv_companyName);
            //  tv_routecode = (TextView) findViewById(R.id.tv_routecode);
            //  tv_route_name = (TextView) findViewById(R.id.tv_route_name);
            tv_delivered_user_Name = (TextView) findViewById(R.id.tv_delivered_user_Name);
            tv_sale_order_no = (TextView) findViewById(R.id.tv_sale_order_no);
            tv_sale_order_date = (TextView) findViewById(R.id.tv_sale_order_date);
            tv_delivery_no = (TextView) findViewById(R.id.tv_delivery_no);
            tv_delivery_date = (TextView) findViewById(R.id.tv_delivery_date);
            price_total = (TextView) findViewById(R.id.price_total);
            tax_total_amount = (TextView) findViewById(R.id.tax_total_amount);
            sub_total = (TextView) findViewById(R.id.sub_total);
            //mode_of_payment = (TextView) findViewById(R.id.mode_of_payment);
            //cheque_number = (TextView) findViewById(R.id.cheque_number);
            //cheque_date = (TextView) findViewById(R.id.cheque_date);
            // bank_name = (TextView) findViewById(R.id.bank_name);
            opening_balance = (TextView) findViewById(R.id.opening_balance);
            sale_order_amount = (TextView) findViewById(R.id.sale_order_amount);
            received_amount = (TextView) findViewById(R.id.received_amount);
            closing_balance = (TextView) findViewById(R.id.closing_balance);
            delivered_products_list_view = (ListView) findViewById(R.id.delivered_products_list_view);
            returned_products_list_view = (ListView) findViewById(R.id.returned_products_list_view);
            mTripSheetDate = mmSharedPreferences.getString("tripsheetDate");
            mTripSheetCode = mmSharedPreferences.getString("tripsheetCode");
            mTripSheetId = this.getIntent().getStringExtra("tripsheetId");
            mAgentId = this.getIntent().getStringExtra("agentId");
            mAgentCode = this.getIntent().getStringExtra("agentCode");
            mAgentName = this.getIntent().getStringExtra("agentName");
            mAgentRouteId = this.getIntent().getStringExtra("agentRouteId");
            mAgentRouteCode = this.getIntent().getStringExtra("agentRouteCode");
            mAgentSoId = this.getIntent().getStringExtra("agentSoId");
            mAgentSoCode = this.getIntent().getStringExtra("agentSoCode");

            loggedInUserId = mmSharedPreferences.getString("userId");
            loggedInUserName = mmSharedPreferences.getString("loginusername");
            companyName = mmSharedPreferences.getString("companyname");
            routeCode = mmSharedPreferences.getString("routecode") + ",";
            routeName = mmSharedPreferences.getString("routename");
            currentDate = Utility.formatTime(System.currentTimeMillis(), Constants.TDC_SALE_INFO_DATE_DISPLAY_FORMAT);

            saleOrdersDetails = mDBHelper.fetchTripSheetSaleOrderDetails(mTripSheetId, mAgentSoId, mAgentId);
            deliveredProductsList = mDBHelper.getDeliveredProductsListForSaleOrder(mTripSheetId, mAgentSoId, mAgentId);
            returnedProductsList = mDBHelper.getReturnsProductsListForSaleOrder(mTripSheetId, mAgentSoId, mAgentId);
            paymentsDetails = mDBHelper.getSaleOrderPaymentDetails(mTripSheetId, mAgentSoId);

            // Updating UI with fetched values.
            tv_companyName.setText(companyName);
            // tv_routecode.setText(routeCode);
            // tv_route_name.setText(routeName);
            tv_delivered_user_Name.setText("by " + loggedInUserName);

            if (saleOrdersDetails != null) {
                if (saleOrdersDetails.getmTripshetSOCode().isEmpty())
                    tv_sale_order_no.setText("Sale # -");
                else
                    tv_sale_order_no.setText(String.format("Sale # %s", saleOrdersDetails.getmTripshetSOCode()));

                if (saleOrdersDetails.getmTripshetSODate().isEmpty())
                    tv_sale_order_date.setText("-");
                else
                    tv_sale_order_date.setText(saleOrdersDetails.getmTripshetSODate());

                opening_balance.setText(Utility.getFormattedCurrency(Double.parseDouble(saleOrdersDetails.getmTripshetSOOpAmount())));
                sale_order_amount.setText(Utility.getFormattedCurrency(Double.parseDouble(saleOrdersDetails.getmTripshetSOValue())));
                received_amount.setText(Utility.getFormattedCurrency(Double.parseDouble(saleOrdersDetails.getmTripshetSOReceivedAmount())));
                closing_balance.setText(Utility.getFormattedCurrency(Double.parseDouble(saleOrdersDetails.getmTripshetSOCBAmount())));
            }

            selectedList = new ArrayList<>(deliveredProductsList.size());

            if (deliveredProductsList.size() > 0) {

                deliveredProduct = deliveredProductsList.get(0);
                tv_delivery_no.setText(String.format("Delivery # RD%03d", deliveredProduct.getDeliveryNo()));
                tv_delivery_date.setText(Utility.formatTime(Long.parseLong(deliveredProduct.getCreatedTime()), Constants.TDC_SALE_INFO_DATE_DISPLAY_FORMAT));

                tax_total_amount.setText(Utility.getFormattedCurrency(Double.parseDouble(deliveredProduct.getTotalTax())));
                price_total.setText(Utility.getFormattedCurrency(totalAmount));
                sub_total.setText(Utility.getFormattedCurrency(Double.parseDouble(deliveredProduct.getSubTotal())));
                ArrayList<SaleOrderDeliveredProducts> deliveredProductsList1 = new ArrayList<>();
                synchronized (this) {
                    if (deliveredProductsList1.size() > 0) {
                        deliveredProductsList1.clear();
                    }
                    for (int b = 0; b < deliveredProductsList.size(); b++) {
                        SaleOrderDeliveredProducts deliveredProduct = new SaleOrderDeliveredProducts();
                        if (!deliveredProductsList.get(b).getProductReturnable().equals("Y")) {
                            deliveredProduct.setDeliveryNo(deliveredProductsList.get(b).getDeliveryNo());
                            deliveredProduct.setId(deliveredProductsList.get(b).getId());
                            deliveredProduct.setName(deliveredProductsList.get(b).getName());
                            deliveredProduct.setCode(deliveredProductsList.get(b).getCode());
                            deliveredProduct.setQuantity(deliveredProductsList.get(b).getQuantity());
                            deliveredProduct.setUnitRate(deliveredProductsList.get(b).getUnitRate());
                            deliveredProduct.setProductTax(deliveredProductsList.get(b).getProductTax());
                            deliveredProduct.setProductAmount(deliveredProductsList.get(b).getProductAmount());
                            deliveredProduct.setTotalTax(deliveredProductsList.get(b).getTotalTax());
                            deliveredProduct.setSubTotal(deliveredProductsList.get(b).getSubTotal());
                            deliveredProduct.setCreatedTime(deliveredProductsList.get(b).getCreatedTime());
                            deliveredProduct.setProductReturnable(deliveredProductsList.get(b).getProductReturnable());

                            deliveredProductsList1.add(deliveredProduct);
                        }
                    }

                    if (deliveredProductsList1.size() > 0) {
                        for (SaleOrderDeliveredProducts products : deliveredProductsList1) {
                            totalAmount = totalAmount + Double.parseDouble(products.getProductAmount());
                            String[] temp = new String[11];
                            temp[0] = products.getName();

                            if (mDBHelper.getHSSNNUMBERByProductId(products.getId()) != null && mDBHelper.getHSSNNUMBERByProductId(products.getId()).length() > 0) {

                                hssnnumber = mDBHelper.getHSSNNUMBERByProductId(products.getId());
                            } else {
                                hssnnumber = "-";
                            }

                            if (mDBHelper.getGSTByProductId(products.getId()) > 0) {
                                cgst = String.valueOf(mDBHelper.getGSTByProductId(products.getId()) + "%");
                            } else {
                                cgst = "0.00%";
                            }

                            if (mDBHelper.getVATByProductId(products.getId()) > 0) {
                                sgst = String.valueOf(mDBHelper.getVATByProductId(products.getId()) + "%");
                            } else {
                                sgst = "0.00%";
                            }


                            Double gst = 0.0, vat = 0.0;
                            if (gst != null) {
                                gst = mDBHelper.getGSTByProductId(products.getId());
                            } else {
                                gst = 0.00;
                            }


                            if (vat != null) {
                                vat = mDBHelper.getVATByProductId(products.getId());
                            }


                            taxes = gst + vat;

                            temp[1] = products.getQuantity();
                            temp[2] = Utility.getFormattedCurrency(Double.parseDouble(products.getUnitRate()));
                            temp[3] = Utility.getFormattedCurrency(Double.parseDouble(String.valueOf(products.getProductAmount())));
                            temp[4] = Utility.getFormattedCurrency(Double.parseDouble(String.valueOf(products.getProductTax())));
                            temp[5] = products.getCode();
                            temp[6] = hssnnumber;
                            temp[7] = cgst;
                            temp[8] = sgst;
                            temp[9] = String.valueOf(taxes);

                            uom = mDBHelper.getProductUnitByProductCode(products.getCode());
                            temp[10] = uom;
                            selectedList.add(temp);
                        }
                    }
                }
                synchronized (this) {
                    tripSheetsPaymentPreviewDeliveredProductsAdapter = new TripSheetsPaymentPreviewDeliveredProductsAdapter(activityContext, this, deliveredProductsList1);
                    delivered_products_list_view.setAdapter(tripSheetsPaymentPreviewDeliveredProductsAdapter);
                    Utility.setListViewHeightBasedOnChildren(delivered_products_list_view);
                }
            }

           /* if (paymentsDetails != null) {
                mode_of_payment.setText(paymentsDetails.getPayments_type().equals("0") ? "Cash" : "Cheque");

                if (paymentsDetails.getPayments_type().equals("1")) {
                    cheque_number.setText("Cheque #" + paymentsDetails.getPayments_chequeNumber());
                    cheque_date.setText("Date : " + paymentsDetails.getPayments_chequeDate());
                    bank_name.setText(paymentsDetails.getPayments_bankName() + " Bank");
                } else {
                    cheque_number.setVisibility(View.GONE);
                    cheque_date.setVisibility(View.GONE);
                    bank_name.setVisibility(View.GONE);
                }
            }
*/
            cratesList = new ArrayList<>(returnedProductsList.size());


            if (returnedProductsList.size() > 0) {

                for (SaleOrderReturnedProducts crates : returnedProductsList) {

                    String[] temp = new String[7];
                    temp[0] = crates.getName();
                    temp[1] = Utility.getFormattedCurrency(Double.parseDouble(String.valueOf(crates.getOpeningBalance())));
                    temp[2] = Utility.getFormattedCurrency(Double.parseDouble(crates.getDelivered()));
                    temp[4] = Utility.getFormattedCurrency(Double.parseDouble(String.valueOf(crates.getClosingBalance())));
                    temp[3] = Utility.getFormattedCurrency(Double.parseDouble(String.valueOf(crates.getReturned())));
                    temp[5] = crates.getCode();
                    uom = mDBHelper.getProductUnitByProductCode(crates.getCode());
                    temp[6] = uom;
                    cratesList.add(temp);
                }

                tripSheetsPaymentPreviewReturnedProductsAdapter = new TripSheetsPaymentPreviewReturnedProductsAdapter(activityContext, this, returnedProductsList);
                returned_products_list_view.setAdapter(tripSheetsPaymentPreviewReturnedProductsAdapter);
                Utility.setListViewHeightBasedOnChildren(returned_products_list_view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                synchronized (this) {
                    //   Toast.makeText(getApplicationContext(), "print", Toast.LENGTH_LONG).show();
                    int pageheight = 850 + selectedList.size() * 120 + cratesList.size() * 200;
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
                    canvas.drawText(companyName, 5, 20, paint);
                    //paint.setTextSize(20);
                    // canvas.drawText( loggedInUserName, 5, 50, paint);
                    paint.setTextSize(20);
                    canvas.drawText("GST NO. 33AABCT7907M1Z2", 5, 50, paint);

                    paint.setTextSize(20);
                    canvas.drawText("------------------------------------", 5, 80, paint);
                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    paint.setTextSize(23);
                    canvas.drawText("DELIVERY CUM PROFORMA", 5, 110, paint);

                    paint.setTextSize(20);
                    canvas.drawText("TRIP #,DT.", 5, 140, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + mTripSheetCode + ", " + mTripSheetDate, 130, 140, paint);

                    //paint.setTextSize(20);
                    // canvas.drawText("DATE", 5, 200, paint);
                    //paint.setTextSize(20);
                    //canvas.drawText(", " + mTripSheetDate, 230, 170, paint);

                    paint.setTextSize(20);
                    canvas.drawText("SO NO,DT.", 5, 170, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + String.format(saleOrdersDetails.getmTripshetSOCode()) + ", " + saleOrdersDetails.getmTripshetSODate(), 130, 170, paint);

                    //paint.setTextSize(20);
                    // canvas.drawText("DATE", 5, 260, paint);
                    // paint.setTextSize(20);
                    //canvas.drawText(": " + saleOrdersDetails.getmTripshetSODate(), 240, 260, paint);

                    paint.setTextSize(20);
                    canvas.drawText("CUSTOMER", 5, 200, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + mAgentName, 130, 200, paint);

                    paint.setTextSize(20);
                    canvas.drawText("CODE", 5, 230, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + mAgentCode, 130, 230, paint);

                    paint.setTextSize(20);
                    canvas.drawText("-------------------------------------", 5, 260, paint);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    paint.setTextSize(20);
                    canvas.drawText(String.format("Delivery # RD%03d", deliveredProduct.getDeliveryNo()), 5, 290, paint);
                    paint.setTextSize(20);
                    canvas.drawText(", " + Utility.formatTime(Long.parseLong(deliveredProduct.getCreatedTime()), Constants.TDC_SALE_INFO_DATE_DISPLAY_FORMAT), 170, 290, paint);

                    int st = 320;
                    paint.setTextSize(17);
                    // for (Map.Entry<String, String[]> entry : selectedList.entrySet()) {

                    for (int i = 0; i < selectedList.size(); i++) {
                        String[] temp = selectedList.get(i);
                        paint.setTextSize(23);
                        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        // canvas.drawText(temps[0] + "," + temps[1] + "( " + temps[2] + " )", 5, st, paint);
                        canvas.drawText(temp[0] + "," + temp[5] + "( " + temp[10] + " ) ", 5, st, paint);


                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("HSSN: " + temp[6] + "," + "GST: " + temp[9] + "%", 5, st, paint);

                        st = st + 30;
                        paint.setTextSize(20);
                        //canvas.drawText("QTY ", 5, st, paint);
                        canvas.drawText("QTY: " + temp[1], 5, st, paint);
                        paint.setTextSize(20);
                        canvas.drawText("RATE: " + temp[2], 200, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("TOT: " + temp[3], 5, st, paint);
                        paint.setTextSize(20);
                        canvas.drawText("GST: " + temp[4], 200, st, paint);









/*

                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("HSSN CODE ", 5, st, paint);
                    canvas.drawText(": " +temp[6], 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                     canvas.drawText("CGST,SGST " + ": " + temp[7] + " + " + temp[8] + " = " + temp[9], 5, st, paint);
                    //canvas.drawText("CGST,SGST " + ": " + " 2.50 % + 2.50 % = 5.00 % ", 5, st, paint);

                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("QUANTITY ", 5, st, paint);
                    canvas.drawText(": " + temp[1] , 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("RATE ", 5, st, paint);
                    canvas.drawText(": " + temp[2], 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("VALUE ", 5, st, paint);
                    canvas.drawText(": " + temp[3], 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("TAX VALUE ", 5, st, paint);
                    canvas.drawText(": " + temp[4], 150, st, paint);
*/


                        st = st + 40;
                    }

                    canvas.drawText("----------------------------------------------------", 5, st, paint);


                    st = st + 30;
                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    paint.setTextSize(23);
                    canvas.drawText(" DELIVERY  ", 5, st, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + Utility.getFormattedCurrency(Double.parseDouble(deliveredProduct.getSubTotal())), 150, st, paint);
                    st = st + 30;
                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    paint.setTextSize(23);
                    canvas.drawText(" Value + GST  ", 5, st, paint);
                    paint.setTextSize(20);
                    canvas.drawText("(" + Utility.getFormattedCurrency(totalAmount) + " + " + Utility.getFormattedCurrency(Double.parseDouble(deliveredProduct.getTotalTax())) + ")", 150, st, paint);
                    st = st + 30;
                    canvas.drawText("----------------------------------------------------", 5, st, paint);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    st = st + 30;
                    paint.setTextSize(23);
                    canvas.drawText("PAYMENT INFO", 5, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("OB", 5, st, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + Utility.getFormattedCurrency(Double.parseDouble(saleOrdersDetails.getmTripshetSOOpAmount())), 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("Order", 5, st, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + Utility.getFormattedCurrency(Double.parseDouble(saleOrdersDetails.getmTripshetSOValue())), 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("Received", 5, st, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + Utility.getFormattedCurrency(Double.parseDouble(saleOrdersDetails.getmTripshetSOReceivedAmount())), 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("CB", 5, st, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + Utility.getFormattedCurrency(Double.parseDouble(saleOrdersDetails.getmTripshetSOCBAmount())), 150, st, paint);



             /*   st = st + 40;
                if (paymentsDetails.getPayments_type().equals("0")) {
                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    paint.setTextSize(20);
                    canvas.drawText( String.format("RD%03d", deliveredProduct.getDeliveryNo()), 5, st, paint);
                    paint.setTextSize(20);
                    canvas.drawText(", " + Utility.formatTime(Long.parseLong(deliveredProduct.getCreatedTime()), Constants.TDC_SALE_INFO_DATE_DISPLAY_FORMAT), 100, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("AMOUNT ", 5, st, paint);
                    canvas.drawText(": " + Utility.getFormattedCurrency(Double.parseDouble(deliveredProduct.getSubTotal())) , 150, st, paint);
                    st = st + 30;
                    canvas.drawText("MOP", 5, st, paint);
                    canvas.drawText(": " + "CASH PAID", 150, st, paint);
                    st = st + 20;
                } else {
                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    paint.setTextSize(20);
                    canvas.drawText( String.format("RD%03d", deliveredProduct.getDeliveryNo()), 5, st, paint);
                    paint.setTextSize(20);
                    canvas.drawText(", " + Utility.formatTime(Long.parseLong(deliveredProduct.getCreatedTime()), Constants.TDC_SALE_INFO_DATE_DISPLAY_FORMAT), 100, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("AMOUNT ", 5, st, paint);
                    canvas.drawText(": " + Utility.getFormattedCurrency(Double.parseDouble(deliveredProduct.getSubTotal())) , 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("MOP", 5, st, paint);
                    canvas.drawText(": " +  "CHEQUE", 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("CHEQUE NO", 5, st, paint);
                    canvas.drawText(": " + paymentsDetails.getPayments_chequeNumber() , 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("CHEQUE DT", 5, st, paint);
                    canvas.drawText(": " + paymentsDetails.getPayments_chequeDate(), 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("BANK NAME", 5, st, paint);
                    canvas.drawText(": " + paymentsDetails.getPayments_bankName() , 150, st, paint);


                }*/

                    st = st + 40;
                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    paint.setTextSize(23);
                    canvas.drawText("CRATES INFO", 5, st, paint);

                    st = st + 30;
                    paint.setTextSize(17);
                    for (int i = 0; i < cratesList.size(); i++) {
                        String[] temp = cratesList.get(i);
                        paint.setTextSize(20);
                        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        // canvas.drawText(temps[0] + "," + temps[1] + "( " + temps[2] + " )", 5, st, paint);
                        canvas.drawText(temp[0] + "," + temp[5] + "( " + temp[6] + " )", 5, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("OB QTY ", 5, st, paint);
                        canvas.drawText(": " + temp[1], 150, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("DELIVER QTY ", 5, st, paint);
                        canvas.drawText(": " + temp[2], 150, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("RETURN QTY ", 5, st, paint);
                        canvas.drawText(": " + temp[3], 150, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("CB QTY ", 5, st, paint);
                        canvas.drawText(": " + temp[4], 150, st, paint);


                        st = st + 40;
                    }

                    paint.setTextSize(20);
                    canvas.drawText("* Please take photocopy of the Bill *", 17, st, paint);
                    st = st + 30;

                    // paint.setTextSize(20);
                    //canvas.drawText( loggedInUserName, 100, st, paint);
                    //st = st + 30;

                    canvas.drawText("--------X---------", 100, st, paint);
                    com.szxb.api.jni_interface.api_interface.printBitmap(bmOverlay, 5, 5);
                    saveBitmap(bmOverlay);
                }
                synchronized (this) {
//                    Intent intent = new Intent(TripsheetPaymentsPreview.this, TripSheetView.class);
//                    intent.putExtra("tripsheetId", mTripSheetId);
//                    startActivity(intent);
//                    finish();

                    mmSharedPreferences.putString("agentName", mAgentName);
                    mmSharedPreferences.putString("agentId", mAgentId);
                    Intent i = new Intent(TripsheetPaymentsPreview.this, AgentTakeOrderScreen.class);
                    i.putExtra("tripsheetId", mTripSheetId);
                    i.putExtra("From", "Tripsheet");
                    startActivity(i);
                    finish();
                }
            }
        });
    }


    public void saveBitmap(Bitmap bm) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/b2bprintimages");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 100000;
        n = generator.nextInt(n);
//        String fname = "Image-" + n + ".jpg";
        String fname = "print.jpg";
        File file = new File(myDir, fname);
        Log.i("saving bitmap", "" + file);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent mediaScanIntent = new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(file);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
            } else {
                sendBroadcast(new Intent(
                        Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            }
//            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));

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
        if (id == R.id.Add) {
            Intent i = new Intent(TripsheetPaymentsPreview.this, TDCSalesListActivity.class);
            startActivity(i);
            finish();
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
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.Add).setVisible(false);

        menu.findItem(R.id.autorenew).setVisible(true);
        menu.findItem(R.id.sort).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, TripsheetPayments.class);
        i.putExtra("tripsheetId", mTripSheetId);
        i.putExtra("agentId", mAgentId);
        i.putExtra("agentCode", mAgentCode);
        i.putExtra("agentName", mAgentName);
        i.putExtra("agentRouteId", mAgentRouteId);
        i.putExtra("agentRouteCode", mAgentRouteCode);
        i.putExtra("agentSoId", mAgentSoId);
        i.putExtra("agentSoCode", mAgentSoCode);
        startActivity(i);
        finish();
    }
}