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
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TakeOrderPreviewAdapter;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderBean;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderPreviewBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AgentTakeOrderPreview extends AppCompatActivity {
    private ListView mAgentsList;
    DBHelper mDBHelper;
    private TakeOrderPreviewAdapter mPreviewAdapter;
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
    private MMSharedPreferences sharedPreferences;
    private ArrayList<TakeOrderBean> mProductIdsList;
    private ArrayList<TakeOrderPreviewBean> takeOrderPreviewBeanArrayList = new ArrayList<TakeOrderPreviewBean>();
    private double mProductsPriceAmountSum = 0.0, mTotalProductsPriceAmountSum = 0.0,mTotalProductsTax = 0.0;
    String currentDate;

    double amount;
    double taxAmount;
    String name;

    Map<String, String[]> selectedList = new HashMap<String, String[]>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_take_order_preview);


        this.getSupportActionBar().setTitle("ORDERS ");
        this.getSupportActionBar().setSubtitle(null);
        this.getSupportActionBar().setLogo(R.drawable.pr_icon_black);
        // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        mDBHelper = new DBHelper(AgentTakeOrderPreview.this);
        sharedPreferences = new MMSharedPreferences(AgentTakeOrderPreview.this);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        mProductIdsList = (ArrayList<TakeOrderBean>) args.getSerializable("productIdsList");
        System.out.println("SIZE::: "+ mProductIdsList.size());
        ArrayList<ProductsBean> productsList = mDBHelper.fetchAllRecordsFromProductsTable();
        System.out.println("SIZE111::: "+ productsList.size());
        for (int k = 0; k < mProductIdsList.size(); k++) {
            for (int i = 0; i < productsList.size(); i++) {
                if (mProductIdsList.get(k).getmProductId().toString().equals(productsList.get(i).getProductId())) {
                    System.out.println("P TITLE IS::: " + productsList.get(i).getProductTitle());




                    TakeOrderPreviewBean topBean = new TakeOrderPreviewBean();
                    topBean.setpName(mProductIdsList.get(k).getmProductTitle());
                    topBean.setpQuantity(mProductIdsList.get(k).getmProductQuantity());
                    topBean.setpPrice(productsList.get(i).getProductAgentPrice());
                    topBean.setmProductTaxGST(productsList.get(i).getProductgst());
                    topBean.setmProductTaxVAT(productsList.get(i).getProductvat());
                    topBean.setmProductFromDate(mProductIdsList.get(k).getmProductFromDate());
                    topBean.setmProductToDate(mProductIdsList.get(k).getmProductToDate());
                    //topBean.setTaxPercentage(productsList.get(i).getProductgst());
                   // topBean.setTaxName(productsList.get(i).getProductvat());
                    topBean.setmProductToDate(mProductIdsList.get(k).getmProductToDate());


                    float tax = 0.0f;
                    if (productsList.get(i).getProductvat() != null)
                        tax = Float.parseFloat(productsList.get(i).getProductvat());
                    else if (productsList.get(i).getProductgst() != null)
                        tax = Float.parseFloat(productsList.get(i).getProductgst());


                     name  = String.valueOf(mProductIdsList.get(k).getmProductTitle().replace(",", ""));

                    double price = Double.parseDouble(productsList.get(i).getProductAgentPrice().replace(",", ""));

                    double quantity = Double.parseDouble(mProductIdsList.get(k).getmProductQuantity().replace(",", ""));




                    taxAmount = ((quantity*price)* tax) / 100;
                   //  amount = price + taxAmount;
                    amount = price;


                    mProductsPriceAmountSum = (mProductsPriceAmountSum + (amount
                            * Double.parseDouble(mProductIdsList.get(k).getmProductQuantity())));
                    System.out.println("P PRICE IS::: " + mProductsPriceAmountSum);

                    mTotalProductsTax=(mTotalProductsTax + taxAmount);

                    mTotalProductsPriceAmountSum = (mProductsPriceAmountSum + mTotalProductsTax);
                    System.out.println("FINAL AMOUNT PRICE IS::: " + mTotalProductsPriceAmountSum);

                    takeOrderPreviewBeanArrayList.add(topBean);
                }
            }
        }

        tv_companyName = (TextView) findViewById(R.id.tv_companyName);
        tv_companyName.setText(sharedPreferences.getString("companyname"));

        user_Name = (TextView) findViewById(R.id.tv_user_Name);
        user_Name.setText("by "+sharedPreferences.getString("loginusername"));

        Route_Name = (TextView) findViewById(R.id.route_name);
        Route_Name.setText(sharedPreferences.getString("routename"));

        RouteCode = (TextView) findViewById(R.id.tv_routecode);
        RouteCode.setText(sharedPreferences.getString("routecode")+",");

        orderNo = (TextView) findViewById(R.id.order_no);
        orderNo.setText(sharedPreferences.getString("enquiryid")+",");

        orderDate = (TextView) findViewById(R.id.tv_date);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = df.format(cal.getTime());
        orderDate.setText(currentDate);

        agentName = (TextView) findViewById(R.id.agentname);
        agentName.setText(sharedPreferences.getString("agentName")+",");
        agentCode = (TextView) findViewById(R.id.tv_AgentCode);
        agentCode.setText(sharedPreferences.getString("agentCode"));


        taxprice=(TextView)findViewById(R.id.taxAmount);
        taxprice.setText(Utility.getFormattedCurrency(mTotalProductsTax));
        taxprice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(20,2)});

        tv_amount=(TextView)findViewById(R.id.Amount);
        tv_amount.setText(Utility.getFormattedCurrency(mProductsPriceAmountSum));
        tv_amount.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(20,2)});

        totalprice=(TextView)findViewById(R.id.totalAmount);
        totalprice.setText(Utility.getFormattedCurrency(mTotalProductsPriceAmountSum));
        totalprice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(20,3)});



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
                canvas.drawText(sharedPreferences.getString("routename"), 150, 80, paint);
                canvas.drawText(sharedPreferences.getString("routecode"), 5, 80, paint);
                canvas.drawText("ORDER", 5, 120, paint);
                canvas.drawText("by "+sharedPreferences.getString("loginusername"), 150, 120, paint);
                canvas.drawText(sharedPreferences.getString("enquiryid"), 5, 150, paint);
                canvas.drawText(currentDate, 150, 150, paint);
                canvas.drawText(sharedPreferences.getString("agentName"), 5, 180, paint);
                canvas.drawText(sharedPreferences.getString("agentCode"), 200, 180, paint);
              //  canvas.drawText(String.valueOf(mTotalProductsPriceAmountSum), 240, 150, paint);
                canvas.drawText("Product", 5, 220, paint);
                canvas.drawText("QTY", 100, 220, paint);
                canvas.drawText("Price", 160, 220, paint);
                canvas.drawText("Amount", 230, 220, paint);
                canvas.drawText("Tax", 320, 220, paint);



                int st = 250;
                double tAmount = 0.00;
                double subtAmount = 0.00;
              //  double taxAmount = 0.00;
                double totaltaxAmount = 0.00;
                double totalqty = 0.000;
                paint.setTextSize(17);
                for (Map.Entry<String, String[]> entry : selectedList.entrySet()) {
                    String[] temps = entry.getValue();
                    canvas.drawText(name, 5, st, paint);
                    canvas.drawText(temps[4], 100, st, paint);

                   // double Amount = Double.parseDouble(temps[3]) * Double.parseDouble(temps[4]);

                  //  subtAmount = subtAmount + Amount;

                   // taxAmount=(Double.parseDouble(temps[3]) * Double.parseDouble(temps[4]))*(Double.parseDouble(temps[9])/100);
                   // totaltaxAmount = totaltaxAmount + taxAmount;
                    canvas.drawText(String.format("%.2f",taxAmount), 160, st, paint);
                    Log.e("tax amount",String.format("%.2f",taxAmount));
                    canvas.drawText(temps[3], 230, st, paint);
                    canvas.drawText(String.format("%.2f", amount), 300, st, paint);
                    tAmount = totaltaxAmount + subtAmount;
                    totalqty=totalqty+Double.parseDouble(temps[4]);
                    st = st + 30;
                    canvas.drawText(temps[8], 5, st, paint);
                    canvas.drawText("FROM:" + temps[5], 100, st, paint);
                    canvas.drawText("TO:" + temps[6], 250, st, paint);
                    st = st + 30;
                }
                canvas.drawText("----------------------------------------------------", 5, st, paint);
               /* st=st+20;
                canvas.drawText("Sub Total ", 5, st, paint);
                canvas.drawText(String.format("%.3f", totalqty), 100, st, paint);
                canvas.drawText(String.format("%.2f", totaltaxAmount), 160, st, paint);
                Log.e("dfsdfds",String.format("%.2f", totaltaxAmount));
                canvas.drawText(String.format("%.2f", subtAmount), 300, st, paint);*/
                st=st+20;
                canvas.drawText("Total:", 5, st, paint);
                canvas.drawText(Utility.getFormattedCurrency(mTotalProductsTax), 70, st, paint);
                canvas.drawText(Utility.getFormattedCurrency(mProductsPriceAmountSum), 170, st, paint);
                canvas.drawText(Utility.getFormattedCurrency(mTotalProductsPriceAmountSum), 280, st, paint);
                st=st+20;
                canvas.drawText("--------X---------", 100, st, paint);
                com.szxb.api.jni_interface.api_interface.printBitmap(bmOverlay, 5, 5);
            }
        });











        mAgentsList = (ListView) findViewById(R.id.AgentsList);
        // ArrayList<AgentsBean> a = mDBHelper.fetchAllRecordsFromAgentsTable();
        //System.out.println("ELSE::: "+a.size());

        //   ArrayList<TakeOrderPreviewBean> previewArrayList = new ArrayList<>();
        if (takeOrderPreviewBeanArrayList.size() > 0) {
            loadAgentsList(takeOrderPreviewBeanArrayList);
        }

    }

    private void loadAgentsList(ArrayList<TakeOrderPreviewBean> takeOrderPreviewBeanArrayList) {
        if (mPreviewAdapter != null) {
            mPreviewAdapter = null;
        }
        mPreviewAdapter = new TakeOrderPreviewAdapter(this, AgentTakeOrderPreview.this, takeOrderPreviewBeanArrayList);
        mAgentsList.setAdapter(mPreviewAdapter);
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
            Intent i = new Intent(AgentTakeOrderPreview.this, SalesListActivity.class);
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


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, AgentTakeOrderScreen.class);
        startActivity(intent);
        finish();
    }
    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
            mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher=mPattern.matcher(dest);
            if(!matcher.matches())
                return "";
            return null;
        }

    }
}