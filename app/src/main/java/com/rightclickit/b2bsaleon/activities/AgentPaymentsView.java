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
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TripSheetsPaymentPreviewDeliveredProductsAdapter;
import com.rightclickit.b2bsaleon.adapters.TripSheetsPaymentPreviewReturnedProductsAdapter;
import com.rightclickit.b2bsaleon.beanclass.AgentPaymentsBean;
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

public class AgentPaymentsView extends AppCompatActivity {
    private Context activityContext;
    private MMSharedPreferences mmSharedPreferences;
    private DBHelper mDBHelper;

    TextView tv_companyName, agentName, aCode, tv_delivered_user_Name, tv_sale_order_no, tv_sale_order_date, tv_delivery_no, tv_delivery_date, price_total, tax_total_amount, sub_total,
            mode_of_payment, cheque_number, cheque_date, bank_name, opening_balance, sale_order_amount, received_amount, closing_balance;
    ListView delivered_products_list_view, returned_products_list_view;

    private String mTripSheetId = "", mAgentId = "",mAgentSoDates="", mAgentName = "", mAgentCode = "", mAgentRouteId = "", mAgentRouteCode = "", mAgentSoId = "", mAgentSoCode = "";
    private String currentDate, receivedAmt, companyName, paymentno, paymentdate, tripid;
    private TripsheetSOList saleOrdersDetails = null;
    private ArrayList<SaleOrderDeliveredProducts> deliveredProductsList;
    private ArrayList<SaleOrderReturnedProducts> returnedProductsList;
    private PaymentsBean paymentsDetails = null;
    private double totalAmount = 0.0;
    private TripSheetsPaymentPreviewDeliveredProductsAdapter tripSheetsPaymentPreviewDeliveredProductsAdapter;
    private TripSheetsPaymentPreviewReturnedProductsAdapter tripSheetsPaymentPreviewReturnedProductsAdapter;
    TextView print;
    String agentId="",firstname;
            LinearLayout checklayout;
    ArrayList<String[]> selectedList, cratesList;
    SaleOrderDeliveredProducts deliveredProduct;
    String ObAmount="",Ordervalue="",receivedAmount="",Due="";
    ArrayList<AgentPaymentsBean> unUploadedPayments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_payments_view);

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

            activityContext = AgentPaymentsView.this;
            mDBHelper = new DBHelper(activityContext);
            mmSharedPreferences = new MMSharedPreferences(activityContext);

            print = (TextView) findViewById(R.id.tv_print_print);

            if (mAgentName != null) {
                mAgentName = mmSharedPreferences.getString("agentName");
            } else {
                mAgentName = "-";
            }

            mAgentCode = mmSharedPreferences.getString("agentCode");


            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {

                paymentno = bundle.getString("PaymentNo");
                paymentdate = bundle.getString("Paymentdate");
                tripid = bundle.getString("tripID");
                receivedAmt = bundle.getString("ReceivedAmount");
                firstname=bundle.getString("firstname");
            }

            tv_companyName = (TextView) findViewById(R.id.tv_companyName);

            tv_sale_order_no = (TextView) findViewById(R.id.tv_sale_order_no);
            tv_sale_order_date = (TextView) findViewById(R.id.tv_sale_order_date);
            tv_delivery_no = (TextView) findViewById(R.id.payment_no);
            tv_delivery_no.setText(paymentno);
            tv_delivery_date = (TextView) findViewById(R.id.payment_date);
            tv_delivery_date.setText(paymentdate);

            agentName = (TextView) findViewById(R.id.tv_customer_name);
            agentName.setText(mAgentName);
            aCode = (TextView) findViewById(R.id.tv_customer_code);
            aCode.setText(mAgentCode);


            mode_of_payment = (TextView) findViewById(R.id.mode_of_payment);
            cheque_number = (TextView) findViewById(R.id.cheque_number);
            cheque_date = (TextView) findViewById(R.id.cheque_date);
            bank_name = (TextView) findViewById(R.id.bank_name);

            received_amount = (TextView) findViewById(R.id.tv_amount);

            checklayout = (LinearLayout) findViewById(R.id.checklayout);
            received_amount.setText(receivedAmt);
            // loggedInUserId = mmSharedPreferences.getString("userId");
            // loggedInUserName = mmSharedPreferences.getString("loginusername");
            companyName = mmSharedPreferences.getString("companyname");

            currentDate = Utility.formatTime(System.currentTimeMillis(), Constants.TDC_SALE_INFO_DATE_DISPLAY_FORMAT);


            // Updating UI with fetched values.
            tv_companyName.setText(firstname);

            ArrayList<TripsheetSOList> tripSheetSOList = mDBHelper.getTripSheetSaleOrderDetails(tripid);
            for (int i = 0; i < tripSheetSOList.size(); i++) {


                if (tripSheetSOList != null) {
                    if (tripSheetSOList.get(i).getmTripshetSOCode().isEmpty())
                        mAgentSoCode = "-";


                    else
                        mAgentSoCode = String.format("Sale # %s", tripSheetSOList.get(i).getmTripshetSOCode());


                    if (tripSheetSOList.get(i).getmTripshetSODate().isEmpty())
                        mAgentSoDates = ("-");

                    else
                        mAgentSoDates = tripSheetSOList.get(i).getmTripshetSODate();


                }


            }
            tv_sale_order_no.setText(mAgentSoCode);
            tv_sale_order_date.setText(mAgentSoDates);


            agentId = mmSharedPreferences.getString("agentId");


            unUploadedPayments = mDBHelper.getpaymentDetails(agentId);

            for (int i = 0; i < unUploadedPayments.size(); i++) {
                mode_of_payment.setText(unUploadedPayments.get(i).getPayment_mop().equals("0") ? "Cash" : "Cheque");

                if (unUploadedPayments.get(i).getPayment_mop().equals("1")) {
                    cheque_number.setText(unUploadedPayments.get(i).getPayment_checkno());
                    cheque_date.setText(unUploadedPayments.get(i).getPayment_checkDate());
                    bank_name.setText(unUploadedPayments.get(i).getPayment_bankName());
                } else {
                    checklayout.setVisibility(View.GONE);
                }
            }

         /*   cratesList = new ArrayList<>(returnedProductsList.size());
            if (returnedProductsList.size() > 0) {

                for (SaleOrderReturnedProducts crates : returnedProductsList) {

                    String[] temp = new String[6];
                    temp[0] = crates.getName();
                    temp[1] = Utility.getFormattedCurrency(Double.parseDouble(String.valueOf(crates.getOpeningBalance())));
                    temp[2] = Utility.getFormattedCurrency(Double.parseDouble(crates.getDelivered()));
                    temp[4] = Utility.getFormattedCurrency(Double.parseDouble(String.valueOf(crates.getReturned())));
                    temp[3] = Utility.getFormattedCurrency(Double.parseDouble(String.valueOf(crates.getOpeningBalance())));
                    temp[5] = crates.getCode();
                    cratesList.add(temp);
                }
                tripSheetsPaymentPreviewReturnedProductsAdapter = new TripSheetsPaymentPreviewReturnedProductsAdapter(activityContext, this, returnedProductsList);
                returned_products_list_view.setAdapter(tripSheetsPaymentPreviewReturnedProductsAdapter);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "print", Toast.LENGTH_LONG).show();
                int pageheight = 400;
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
                canvas.drawText(firstname, 5, 20, paint);
                paint.setTextSize(20);
                // canvas.drawText(routeCode, 5, 80, paint);
                // canvas.drawText(routeName, 200, 80, paint);
                canvas.drawText("PAYMENT INFO", 100, 50, paint);
                //  canvas.drawText("by " + loggedInUserName, 200, 120, paint);
                //canvas.drawText(String.format("Sale # %s", saleOrdersDetails.getmTripshetSOCode()), 5, 80, paint);
                //  canvas.drawText(saleOrdersDetails.getmTripshetSODate(), 200, 80, paint);

                paint.setTextSize(20);
                canvas.drawText("PMT NO,DT.", 5, 80, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + paymentno + ", " + paymentdate, 130, 80, paint);

                paint.setTextSize(20);
                canvas.drawText("SO NO,DT.", 5, 110, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + mAgentSoCode + ", " + mAgentSoDates, 130, 110, paint);

                paint.setTextSize(20);
                canvas.drawText("CUSTOMER", 5, 140, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + mAgentName, 130, 140, paint);

                paint.setTextSize(20);
                canvas.drawText("CODE", 5, 170, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + mAgentCode, 130, 170, paint);


                int st = 200;

                for (int j = 0; j < unUploadedPayments.size(); j++) {
                    if (unUploadedPayments.get(j).getPayment_mop().equals("0")) {

                        paint.setTextSize(20);
                        canvas.drawText("MOP", 5, st, paint);
                        paint.setTextSize(20);
                        canvas.drawText(": " + "CASH", 130, st, paint);

                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("AMOUNT", 5, st, paint);
                        paint.setTextSize(20);
                        canvas.drawText(": " + receivedAmt, 130, st, paint);

                        st = st + 30;
                    } else {

                        paint.setTextSize(20);
                        canvas.drawText("MOP", 5, st, paint);
                        paint.setTextSize(20);
                        canvas.drawText(": " + "CHEQUE", 130, st, paint);

                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("AMOUNT", 5, st, paint);
                        paint.setTextSize(20);
                        canvas.drawText(": " + receivedAmt, 130, st, paint);

                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("BANK", 5, st, paint);
                        paint.setTextSize(20);
                        canvas.drawText(": " + unUploadedPayments.get(j).getPayment_bankName(), 130, st, paint);

                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("CHQ NUM,DT", 5, st, paint);
                        paint.setTextSize(20);
                        canvas.drawText(": " + unUploadedPayments.get(j).getPayment_checkno() + ", " + unUploadedPayments.get(j).getPayment_checkDate(), 130, st, paint);

                    }
                }


                st = st + 30;
                paint.setTextSize(20);
                canvas.drawText("* Please take photocopy of the Bill *", 17, st, paint);
                st = st + 30;
                canvas.drawText("--------X---------", 100, st, paint);
                com.szxb.api.jni_interface.api_interface.printBitmap(bmOverlay, 5, 5);
                //  saveBitmap(bmOverlay);
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
            Intent i = new Intent(AgentPaymentsView.this, TDCSalesListActivity.class);
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
        Intent i = new Intent(this, AgentPayments.class);
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
