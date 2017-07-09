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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.AgentTakeOrder_ViewAdapter;
import com.rightclickit.b2bsaleon.beanclass.OrdersListBean;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderBean;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderPreviewBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;

public class AgentsTDC_View extends AppCompatActivity {
    private ListView mAgentsList;
    private AgentTakeOrder_ViewAdapter mPreviewAdapter;
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
    TextView tv_totalprice;

    DBHelper mDBHelper;
    TextView ordersprint;
    String currentDate, str_routecode, str_enguiryid, str_agentname;
    private MMSharedPreferences sharedPreferences;

    double amount, subtotal;
    double taxAmount;
    String name;
    private double mProductsPriceAmountSum = 0.0, mTotalProductsPriceAmountSum = 0.0, mTotalProductsTax = 0.0;
    float tax;

    double quantity,price;

  //  Map<String, String[]> selectedList = new HashMap<String, String[]>();
       ArrayList<String[]> selectedList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agents_tdc__view);

        this.getSupportActionBar().setTitle("TDC ORDERS VIEW");
        this.getSupportActionBar().setSubtitle(null);
        this.getSupportActionBar().setLogo(R.drawable.ic_shopping_cart_white_24dp);
        // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);


        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);


        mDBHelper = new DBHelper(AgentsTDC_View.this);
        sharedPreferences = new MMSharedPreferences(AgentsTDC_View.this);

        ArrayList<TakeOrderPreviewBean> takeOrderPreviewBeanArrayList = new ArrayList<TakeOrderPreviewBean>();
        ArrayList<TakeOrderBean> mTakeOrderBeansList = new ArrayList<TakeOrderBean>();
        ArrayList<OrdersListBean> ordersList=new ArrayList<>();
        mTakeOrderBeansList = mDBHelper.fetchAllRecordsFromTakeOrderProductsTable("",sharedPreferences.getString("agentId"));




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
       if(mTakeOrderBeansList.size()>0) {
           currentDate=mTakeOrderBeansList.get(0).getmAgentTakeOrderDate();
           orderDate.setText(currentDate);
           //orderDate.setText(currentDate);
       }
       // sharedPreferences.putString("orderdate", currentDate);

        agentName = (TextView) findViewById(R.id.agentname);
        str_agentname = (sharedPreferences.getString("agentName") + ",");
        agentName.setText(str_agentname);

        agentCode = (TextView) findViewById(R.id.tv_AgentCode);
        agentCode.setText(sharedPreferences.getString("agentCode"));




        mAgentsList = (ListView) findViewById(R.id.AgentsList);
        // ArrayList<AgentsBean> a = mDBHelper.fetchAllRecordsFromAgentsTable();
        //System.out.println("ELSE::: "+a.size());

        //   ArrayList<TakeOrderPreviewBean> previewArrayList = new ArrayList<>();
        selectedList=new ArrayList<>(mTakeOrderBeansList.size());
        if (mTakeOrderBeansList.size()>0) {

            for(int i=0;i<mTakeOrderBeansList.size();i++){
                TakeOrderPreviewBean tb=new TakeOrderPreviewBean();
                TakeOrderBean tob=mTakeOrderBeansList.get(i);
                tb.setpName(tob.getmProductTitle());
                tb.setpQuantity(tob.getmProductQuantity());
                tb.setpPrice(tob.getmAgentPrice());
                //tb.setpAmount(tob.ge);
                tb.setmProductTaxVAT(tob.getmAgentVAT());
                tb.setmProductTaxGST(tob.getmAgentGST());
                tb.setmProductFromDate(tob.getmProductFromDate());
                tb.setmProductToDate(tob.getmProductToDate());
                takeOrderPreviewBeanArrayList.add(tb);

                name=mTakeOrderBeansList.get(i).getmProductTitle();
                price= Double.parseDouble(mTakeOrderBeansList.get(i).getmAgentPrice());
                quantity= Double.parseDouble(mTakeOrderBeansList.get(i).getmProductQuantity());
                tax = 0.0f;
                String str_Taxname = "";
                if (mTakeOrderBeansList.get(i).getmAgentVAT() != null) {
                    tax = Float.parseFloat(mTakeOrderBeansList.get(i).getmAgentVAT());
                    str_Taxname = "VAT:";
                } else if (mTakeOrderBeansList.get(i).getmAgentGST() != null) {
                    tax = Float.parseFloat(mTakeOrderBeansList.get(i).getmAgentGST());
                    str_Taxname = "GST:";
                }
                taxAmount = ((quantity * price) * tax) / 100;
                System.out.println("P TAX IS::: " + taxAmount);
                //  amount = price + taxAmount;
                amount = price;

                subtotal = (price * quantity);

                mProductsPriceAmountSum = (mProductsPriceAmountSum + (amount
                        *Double.parseDouble(mTakeOrderBeansList.get(i).getmProductQuantity())));
                System.out.println("P SUBTOTAl IS::: " + mProductsPriceAmountSum);

                mTotalProductsTax = (mTotalProductsTax + taxAmount);

                mTotalProductsPriceAmountSum = (mProductsPriceAmountSum + mTotalProductsTax);
                System.out.println("P FINAL IS::: " + mTotalProductsPriceAmountSum);

                taxprice = (TextView) findViewById(R.id.taxAmount);
                taxprice.setText(Utility.getFormattedCurrency(mTotalProductsTax));

                tv_amount = (TextView) findViewById(R.id.Amount);
                tv_amount.setText(Utility.getFormattedCurrency(mProductsPriceAmountSum));

                tv_totalprice = (TextView) findViewById(R.id.totalAmount);
                tv_totalprice.setText(Utility.getFormattedCurrency(mTotalProductsPriceAmountSum));

                String[] temp = new String[10];
                temp[0] = name;
                temp[1] = String.valueOf(quantity);
                temp[2] = String.valueOf(price);
                temp[3] = String.valueOf(subtotal);
                temp[4] = String.valueOf(taxAmount);
                temp[5] = String.valueOf(str_Taxname);
                temp[6] = String.valueOf("(" + tax + "%)");
                temp[7] = mTakeOrderBeansList.get(i).getmProductFromDate();
                temp[8] = mTakeOrderBeansList.get(i).getmProductToDate();
                selectedList.add(temp);
                Log.i("takeordertemp", temp + "");
            }
            loadAgentsList(takeOrderPreviewBeanArrayList);
        }


        ordersprint=(TextView)findViewById(R.id.tv_ordersprint);
        ordersprint.setOnClickListener(new View.OnClickListener() {
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
                canvas.drawText(sharedPreferences.getString("companyname"), 5, 50, paint);
                paint.setTextSize(20);
                canvas.drawText(str_routecode, 5, 80, paint);
                canvas.drawText(sharedPreferences.getString("routename"), 200, 80, paint);
                canvas.drawText("ORDER,", 5, 120, paint);
                canvas.drawText("by " + sharedPreferences.getString("loginusername"), 200, 120, paint);
                canvas.drawText(str_enguiryid, 5, 150, paint);
                canvas.drawText(currentDate, 200, 150, paint);
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
               // for (Map.Entry<String, String[]> entry : selectedList.entrySet()) {
                for (int i = 0; i <selectedList.size(); i++) {
                    String[] temps = selectedList.get(i);
                    canvas.drawText(temps[0], 5, st, paint);
                    canvas.drawText(temps[1], 115, st, paint);


                    canvas.drawText(temps[2], 175, st, paint);

                    canvas.drawText(temps[3], 245, st, paint);
                    canvas.drawText(temps[4], 315, st, paint);

                    st = st + 30;
                    canvas.drawText(temps[5], 5, st, paint);
                    canvas.drawText(temps[6], 45, st, paint);

                    canvas.drawText(temps[7], 120, st, paint);
                    canvas.drawText("to" + " " + " " + temps[8], 230, st, paint);

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

    private void loadAgentsList(ArrayList<TakeOrderPreviewBean> takeOrderPreviewBeanArrayList) {
        if (mPreviewAdapter != null) {
            mPreviewAdapter = null;
        }
        mPreviewAdapter = new AgentTakeOrder_ViewAdapter(this, AgentsTDC_View.this, takeOrderPreviewBeanArrayList);
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
        if (id == R.id.action_search) {

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
        menu.findItem( R.id.Add).setVisible(false);

        menu.findItem( R.id.autorenew).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, AgentTDC_Order.class);
        startActivity(intent);
        finish();
    }
}


