package com.rightclickit.b2bsaleon.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TripSheetDeleveriesPreviewAdapter;
import com.rightclickit.b2bsaleon.adapters.TripSheetDeliveriesAdapter;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetDeliveriesBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;

public class TripsheetDeliveryPreview extends AppCompatActivity {
    private ListView mAgentsList;
    private TripSheetDeliveriesAdapter mTripSheetDeliveriesAdapter;
    ArrayList customArraylist = new ArrayList();

    TextView tv_companyName;
    TextView Route_Name;
    TextView RouteCode;
    TextView orderNo;
    TextView orderDate;
    TextView user_Name;
    TextView agentName;

    TextView agentCode;
    TextView taxprice;
    TextView tv_amount;
    TextView totalprice;
    TextView print;

    double amount, subtotal;
    double taxAmount;
    String name;

    private MMSharedPreferences sharedPreferences;
    DBHelper mDBHelper;
    private double mProductsPriceAmountSum = 0.0, mTotalProductsPriceAmountSum = 0.0, mTotalProductsTax = 0.0;
    String currentDate, str_routecode, str_enguiryid, str_agentname;

    ArrayList<String[]> selectedList;

    private String mTripSheetId = "", mAgentId = "", mAgentName = "", mAgentCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripsheet_delivery_preview);


        this.getSupportActionBar().setTitle("PREVIEW");
        this.getSupportActionBar().setSubtitle(null);
        this.getSupportActionBar().setLogo(R.drawable.route_white);
        // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);


        mDBHelper = new DBHelper(TripsheetDeliveryPreview.this);
        sharedPreferences = new MMSharedPreferences(TripsheetDeliveryPreview.this);

        mTripSheetId = this.getIntent().getStringExtra("tripsheetId");
        mAgentId = this.getIntent().getStringExtra("agentId");
        mAgentCode = this.getIntent().getStringExtra("agentCode");
        mAgentName = this.getIntent().getStringExtra("agentName");

        mAgentsList = (ListView) findViewById(R.id.AgentsList);
        selectedList = new ArrayList<>(customArraylist.size());
        for (int i = 0; i < 10; i++) {
            ProductsBean dBean = new ProductsBean();
            dBean.setProductTitle("FCM 500ML");
            dBean.setSelectedQuantity(0.00);
            dBean.setProductAgentPrice("00.000");
            dBean.setProductAmount(50.00);
            dBean.setProductTaxPerUnit(00.00);


            String[] temp = new String[10];
            temp[0] = "FCM 500ML";
            temp[1] = String.valueOf("00.00");
            temp[2] = String.valueOf("00.00");
            temp[3] = String.valueOf("00.00");
            temp[4] = String.valueOf("00.00");

            selectedList.add(temp);
            Log.i("takeordertemp", temp + "");
            customArraylist.add(dBean);
        }
        mAgentsList.setAdapter(new TripSheetDeleveriesPreviewAdapter(TripsheetDeliveryPreview.this, TripsheetDeliveryPreview.this, customArraylist));


        tv_companyName = (TextView) findViewById(R.id.tv_companyName);
        tv_companyName.setText(sharedPreferences.getString("companyname"));

        user_Name = (TextView) findViewById(R.id.tv_user_Name);
        user_Name.setText("by " + sharedPreferences.getString("loginusername"));

        Route_Name = (TextView) findViewById(R.id.route_name);
        Route_Name.setText(sharedPreferences.getString("routename"));

        RouteCode = (TextView) findViewById(R.id.tv_routecode);
        str_routecode = (sharedPreferences.getString("routecode") + ",");
        RouteCode.setText(str_routecode);


        orderNo = (TextView) findViewById(R.id.order_no);
        str_enguiryid = (sharedPreferences.getString("enquiryid") + ",");
        orderNo.setText(str_enguiryid);

        orderDate = (TextView) findViewById(R.id.tv_date);
       /* Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = df.format(cal.getTime());*/
        orderDate.setText(currentDate);
        sharedPreferences.putString("orderdate", currentDate);

        agentName = (TextView) findViewById(R.id.agentname);
        str_agentname = (sharedPreferences.getString("agentName") + ",");
        agentName.setText(str_agentname);

        agentCode = (TextView) findViewById(R.id.tv_AgentCode);
        agentCode.setText(sharedPreferences.getString("agentCode"));


        taxprice = (TextView) findViewById(R.id.taxAmount);
        taxprice.setText(Utility.getFormattedCurrency(mTotalProductsTax));


        tv_amount = (TextView) findViewById(R.id.Amount);
        tv_amount.setText(Utility.getFormattedCurrency(mProductsPriceAmountSum));

        totalprice = (TextView) findViewById(R.id.totalAmount);
        totalprice.setText(Utility.getFormattedCurrency(mTotalProductsPriceAmountSum));

        sharedPreferences.putString("totalprice", Utility.getFormattedCurrency(mTotalProductsPriceAmountSum));


        print = (TextView) findViewById(R.id.tv_print);


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                canvas.drawText(sharedPreferences.getString("companyname"), 5, 50, paint);
                paint.setTextSize(20);
                canvas.drawText(str_routecode, 5, 80, paint);
                canvas.drawText(sharedPreferences.getString("routename"), 200, 80, paint);
                canvas.drawText("ORDER,", 5, 120, paint);
                canvas.drawText("by " + sharedPreferences.getString("loginusername"), 200, 120, paint);
                canvas.drawText(str_enguiryid, 5, 150, paint);
                canvas.drawText("2017-07-17", 200, 150, paint);
                canvas.drawText(str_agentname, 5, 180, paint);
                canvas.drawText(sharedPreferences.getString("agentCode"), 200, 180, paint);

                canvas.drawText("----------------------------------------------------", 5, 200, paint);
                canvas.drawText("Product", 5, 220, paint);
                canvas.drawText("Qty", 100, 220, paint);
                canvas.drawText("Price", 160, 220, paint);
                canvas.drawText("Amount", 230, 220, paint);
                canvas.drawText("Tax", 320, 220, paint);
                canvas.drawText("----------------------------------------------------", 5, 235, paint);


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


                    // canvas.drawText("FROM:" + temps[7], 100, st, paint);
                    //canvas.drawText("TO:" + temps[8], 250, st, paint);

                    st = st + 30;
                    //  canvas.drawText("----------------------------------------------------", 5, st, paint);


                }
                canvas.drawText("----------------------------------------------------", 5, st, paint);

                st = st + 20;
                canvas.drawText("Total:", 5, st, paint);
                canvas.drawText(Utility.getFormattedCurrency(mTotalProductsTax), 70, st, paint);
                canvas.drawText(Utility.getFormattedCurrency(mProductsPriceAmountSum), 170, st, paint);
                canvas.drawText(Utility.getFormattedCurrency(mTotalProductsPriceAmountSum), 280, st, paint);
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
        if (id == R.id.Add) {
            Intent i = new Intent(TripsheetDeliveryPreview.this, TDCSalesListActivity.class);
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
        menu.findItem(R.id.action_search).setVisible(true);
        menu.findItem(R.id.Add).setVisible(false);

        menu.findItem(R.id.autorenew).setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, TripsheetDelivery.class);
        intent.putExtra("tripsheetId", mTripSheetId);
        intent.putExtra("agentId", mAgentId);
        intent.putExtra("agentCode", mAgentCode);
        intent.putExtra("agentName", mAgentName);
        startActivity(intent);
        finish();
    }

}
