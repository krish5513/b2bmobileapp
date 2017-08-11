package com.rightclickit.b2bsaleon.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
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
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TripSheetsPaymentPreviewDeliveredProductsAdapter;
import com.rightclickit.b2bsaleon.adapters.TripSheetsPaymentPreviewReturnedProductsAdapter;
import com.rightclickit.b2bsaleon.beanclass.DeliverysBean;
import com.rightclickit.b2bsaleon.beanclass.PaymentsBean;
import com.rightclickit.b2bsaleon.beanclass.SaleOrderDeliveredProducts;
import com.rightclickit.b2bsaleon.beanclass.SaleOrderReturnedProducts;
import com.rightclickit.b2bsaleon.beanclass.TripsheetSOList;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;

public class TripsheetPaymentsPreview extends AppCompatActivity {
    private Context activityContext;
    private MMSharedPreferences mmSharedPreferences;
    private DBHelper mDBHelper;

    TextView tv_companyName, tv_routecode, tv_route_name, tv_delivered_user_Name, tv_sale_order_no, tv_sale_order_date, tv_delivery_no, tv_delivery_date, price_total, tax_total_amount, sub_total,
            mode_of_payment, cheque_number, cheque_date, bank_name, opening_balance, sale_order_amount, received_amount, closing_balance;
    ListView delivered_products_list_view, returned_products_list_view;

    private String mTripSheetId = "", mAgentId = "", mAgentName = "", mAgentCode = "", mAgentRouteId = "", mAgentRouteCode = "", mAgentSoId = "", mAgentSoCode = "", mAgentSoDate;
    private String loggedInUserId, loggedInUserName, companyName, routeCode, routeName, currentDate;
    private TripsheetSOList saleOrdersDetails = null;
    private ArrayList<SaleOrderDeliveredProducts> deliveredProductsList;
    private ArrayList<SaleOrderReturnedProducts> returnedProductsList;
    private PaymentsBean paymentsDetails = null;
    private double totalAmount = 0.0;
    private TripSheetsPaymentPreviewDeliveredProductsAdapter tripSheetsPaymentPreviewDeliveredProductsAdapter;
    private TripSheetsPaymentPreviewReturnedProductsAdapter tripSheetsPaymentPreviewReturnedProductsAdapter;
    TextView print;

    ArrayList<String[]> selectedList;
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

            print=(TextView) findViewById(R.id.tv_print_print);

            tv_companyName = (TextView) findViewById(R.id.tv_companyName);
            tv_routecode = (TextView) findViewById(R.id.tv_routecode);
            tv_route_name = (TextView) findViewById(R.id.tv_route_name);
            tv_delivered_user_Name = (TextView) findViewById(R.id.tv_delivered_user_Name);
            tv_sale_order_no = (TextView) findViewById(R.id.tv_sale_order_no);
            tv_sale_order_date = (TextView) findViewById(R.id.tv_sale_order_date);
            tv_delivery_no = (TextView) findViewById(R.id.tv_delivery_no);
            tv_delivery_date = (TextView) findViewById(R.id.tv_delivery_date);
            price_total = (TextView) findViewById(R.id.price_total);
            tax_total_amount = (TextView) findViewById(R.id.tax_total_amount);
            sub_total = (TextView) findViewById(R.id.sub_total);
            mode_of_payment = (TextView) findViewById(R.id.mode_of_payment);
            cheque_number = (TextView) findViewById(R.id.cheque_number);
            cheque_date = (TextView) findViewById(R.id.cheque_date);
            bank_name = (TextView) findViewById(R.id.bank_name);
            opening_balance = (TextView) findViewById(R.id.opening_balance);
            sale_order_amount = (TextView) findViewById(R.id.sale_order_amount);
            received_amount = (TextView) findViewById(R.id.received_amount);
            closing_balance = (TextView) findViewById(R.id.closing_balance);
            delivered_products_list_view = (ListView) findViewById(R.id.delivered_products_list_view);
            returned_products_list_view = (ListView) findViewById(R.id.returned_products_list_view);

            mTripSheetId = this.getIntent().getStringExtra("tripsheetId");
            mAgentId = this.getIntent().getStringExtra("agentId");
            mAgentCode = this.getIntent().getStringExtra("agentCode");
            mAgentName = this.getIntent().getStringExtra("agentName");
            mAgentSoId = this.getIntent().getStringExtra("agentSoId");
            mAgentSoCode = this.getIntent().getStringExtra("agentSoCode");
            mAgentSoDate = this.getIntent().getStringExtra("agentSoDate");

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
            tv_routecode.setText(routeCode);
            tv_route_name.setText(routeName);
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
            selectedList=new ArrayList<>(deliveredProductsList.size());
            if (deliveredProductsList.size() > 0) {
                for (SaleOrderDeliveredProducts products : deliveredProductsList)
                    totalAmount = totalAmount + Double.parseDouble(products.getProductAmount());

                 deliveredProduct = deliveredProductsList.get(0);
                tv_delivery_no.setText(String.format("Delivery # RD%03d", deliveredProduct.getDeliveryNo()));
                tv_delivery_date.setText(Utility.formatTime(Long.parseLong(deliveredProduct.getCreatedTime()), Constants.TDC_SALE_INFO_DATE_DISPLAY_FORMAT));

                String[] temp = new String[6];
                temp[0] = deliveredProduct.getName();
                temp[1] = Utility.getFormattedCurrency(Double.parseDouble(String.valueOf(deliveredProduct.getQuantity())));
                temp[2] = Utility.getFormattedCurrency(Double.parseDouble(deliveredProduct.getUnitRate()));
                temp[4] = Utility.getFormattedCurrency(Double.parseDouble(String.valueOf(deliveredProduct.getProductTax())));
                temp[3] = Utility.getFormattedCurrency(Double.parseDouble(String.valueOf(deliveredProduct.getProductAmount())));
                temp[5] = deliveredProduct.getCode();
                selectedList.add(temp);
                tax_total_amount.setText(Utility.getFormattedCurrency(Double.parseDouble(deliveredProduct.getTotalTax())));
                price_total.setText(Utility.getFormattedCurrency(totalAmount));
                sub_total.setText(Utility.getFormattedCurrency(Double.parseDouble(deliveredProduct.getSubTotal())));

                tripSheetsPaymentPreviewDeliveredProductsAdapter = new TripSheetsPaymentPreviewDeliveredProductsAdapter(activityContext, this, deliveredProductsList);
                delivered_products_list_view.setAdapter(tripSheetsPaymentPreviewDeliveredProductsAdapter);
            }

            if (paymentsDetails != null) {
                mode_of_payment.setText(paymentsDetails.getPayments_type().equals("0") ? "Cash" : "Cheque");
                cheque_number.setText(paymentsDetails.getPayments_chequeNumber());
                cheque_date.setText(paymentsDetails.getPayments_chequeDate());
                bank_name.setText(paymentsDetails.getPayments_bankName());
            }

            if (returnedProductsList.size() > 0) {
                tripSheetsPaymentPreviewReturnedProductsAdapter = new TripSheetsPaymentPreviewReturnedProductsAdapter(activityContext, this, returnedProductsList);
                returned_products_list_view.setAdapter(tripSheetsPaymentPreviewReturnedProductsAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "print", Toast.LENGTH_LONG).show();
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
                canvas.drawText(companyName, 5, 50, paint);
                paint.setTextSize(20);
                canvas.drawText(routeCode, 5, 80, paint);
                canvas.drawText(routeName, 200, 80, paint);
                canvas.drawText("PAYMENT INFO,", 5, 120, paint);
                canvas.drawText("by " + loggedInUserName, 200, 120, paint);
                canvas.drawText(String.format("Sale # %s", saleOrdersDetails.getmTripshetSOCode()), 5, 150, paint);
                canvas.drawText(saleOrdersDetails.getmTripshetSODate(), 200, 150, paint);
                canvas.drawText(String.format("Delivery # RD%03d", deliveredProduct.getDeliveryNo()), 5, 180, paint);
                canvas.drawText(Utility.formatTime(Long.parseLong(deliveredProduct.getCreatedTime()), Constants.TDC_SALE_INFO_DATE_DISPLAY_FORMAT), 200, 180, paint);
                paint.setTextSize(30);
                canvas.drawText("------------------------------------", 5, 200, paint);
                paint.setTextSize(20);
                canvas.drawText("Product", 5, 220, paint);
                paint.setTextSize(20);
                canvas.drawText("Qty", 100, 220, paint);
                paint.setTextSize(20);
                canvas.drawText("Price", 160, 220, paint);
                paint.setTextSize(20);
                canvas.drawText("Amount", 230, 220, paint);
                paint.setTextSize(20);
                canvas.drawText("Tax", 320, 220, paint);

                canvas.drawText("-------------------------------------", 5, 235, paint);


                int st = 250;
                paint.setTextSize(17);
//                    for (Map.Entry<String, String[]> entry : selectedList.entrySet()) {
                for (int i = 0; i < selectedList.size(); i++) {
                    String[] temps = selectedList.get(i);
                    //String[] temps = selectedList.get(i-1);
                    canvas.drawText(temps[0], 5, st, paint);
                    canvas.drawText(temps[1], 115, st, paint);
                    canvas.drawText(temps[2], 175, st, paint);
                    canvas.drawText(temps[3], 245, st, paint);
                    canvas.drawText(temps[4], 315, st, paint);

                    st = st + 30;
                    canvas.drawText(temps[5], 5, st, paint);


                    st = st + 30;
                    //  canvas.drawText("----------------------------------------------------", 5, st, paint);


                }

                canvas.drawText("----------------------------------------------------", 5, st, paint);

                st = st + 20;
                canvas.drawText("Total:", 5, st, paint);
                canvas.drawText(Utility.getFormattedCurrency(Double.parseDouble(deliveredProduct.getTotalTax())), 70, st, paint);
                canvas.drawText(Utility.getFormattedCurrency(totalAmount), 170, st, paint);
                canvas.drawText(Utility.getFormattedCurrency(Double.parseDouble(deliveredProduct.getSubTotal())), 280, st, paint);
                st = st + 20;
                canvas.drawText("PAYMENT INFO", 5, st, paint);
                st = st + 20;
                if (paymentsDetails.getPayments_type().equals("0")) {
                    canvas.drawText("MOP", 5, st, paint);
                    canvas.drawText("Cash Paid", 60, st, paint);
                    st = st + 20;
                }
                else {
                    canvas.drawText("MOP", 5, st, paint);
                    canvas.drawText("Cheque", 60, st, paint);
                    st = st + 20;
                    canvas.drawText(paymentsDetails.getPayments_chequeNumber(), 5, st, paint);
                    canvas.drawText(paymentsDetails.getPayments_chequeDate(), 120, st, paint);
                    canvas.drawText(paymentsDetails.getPayments_bankName(), 250, st, paint);
                }
                    st=st+30;
                canvas.drawText("OB", 5, st, paint);
                paint.setTextSize(20);
                canvas.drawText("S.Order", 120, st, paint);
                paint.setTextSize(20);
                canvas.drawText("Received", 210, st, paint);
                paint.setTextSize(20);
                canvas.drawText("CB", 300, st, paint);
                  st=st+30;
                canvas.drawText(Utility.getFormattedCurrency(Double.parseDouble(saleOrdersDetails.getmTripshetSOOpAmount())), 5, st, paint);
                canvas.drawText(Utility.getFormattedCurrency(Double.parseDouble(saleOrdersDetails.getmTripshetSOValue())), 120, st, paint);
                canvas.drawText(Utility.getFormattedCurrency(Double.parseDouble(saleOrdersDetails.getmTripshetSOReceivedAmount())), 210, st, paint);
                canvas.drawText(Utility.getFormattedCurrency(Double.parseDouble(saleOrdersDetails.getmTripshetSOCBAmount())), 300, st, paint);
                 st=st+30;
                canvas.drawText("CRATES", 5, st, paint);
                st=st+30;
                canvas.drawText("Product", 5, st, paint);
                paint.setTextSize(20);
                canvas.drawText("OB", 100, st, paint);
                paint.setTextSize(20);
                canvas.drawText("Delivery", 160, st, paint);
                paint.setTextSize(20);
                canvas.drawText("Return", 230, st, paint);
                paint.setTextSize(20);
                canvas.drawText("CB", 320, st, paint);
                st=st+30;
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
