package com.rightclickit.b2bsaleon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.beanclass.OrdersListBean;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderBean;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderPreviewBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.AgentOrdersModel;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;

public class AgentTDC_Order extends AppCompatActivity {
    LinearLayout sales;
    LinearLayout returns;
    LinearLayout payments;
    LinearLayout deliveries;
    LinearLayout orders;
    public LinearLayout agentOrders, noOrders;
    FloatingActionButton fab;

    public TextView tv_enquiryId;

    public TextView date;
    public TextView status;
    public TextView tv_itemsCount;
    public TextView valueCount;
    public Button ordersview;

    private DBHelper mDBHelper;
    private MMSharedPreferences mPreferences;


    private MMSharedPreferences mSessionManagement;
    private ArrayList<TakeOrderPreviewBean> takeOrderPreviewBeanArrayList = new ArrayList<TakeOrderPreviewBean>();
    double amount, subtotal;
    double taxAmount;
    String name, agentId = "", ObAmount = "", Ordervalue = "", receivedAmount = "", Due = "";
    private double mProductsPriceAmountSum = 0.0, mTotalProductsPriceAmountSum = 0.0, mTotalProductsTax = 0.0;

    String enquiryId;
    String orderdate;
    String itemCount;
    String totalprice;
    float tax;

    double quantity, price;
    private AgentOrdersModel mAgentOrdersModel;
    ArrayList<TakeOrderBean> mTakeOrderBeansList = new ArrayList<TakeOrderBean>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_tdc__order);

        mSessionManagement = new MMSharedPreferences(this);
        this.getSupportActionBar().setTitle("ORDERS");
        this.getSupportActionBar().setSubtitle(null);
        this.getSupportActionBar().setLogo(R.drawable.customers_white_24);
        // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);


        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setVisibility(View.GONE);
        fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pr_ic_white));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Clicked Customers Add",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AgentTDC_Order.this, AgentTakeOrderScreen.class);
                startActivity(i);
                finish();
            }
        });


        mAgentOrdersModel = new AgentOrdersModel(this, AgentTDC_Order.this);


        mDBHelper = new DBHelper(AgentTDC_Order.this);
        mPreferences = new MMSharedPreferences(AgentTDC_Order.this);

        tv_enquiryId = (TextView) findViewById(R.id.tv_EnquiryId);
        date = (TextView) findViewById(R.id.tv_ordersdate);
        status = (TextView) findViewById(R.id.tv_orderstatus);
        tv_itemsCount = (TextView) findViewById(R.id.tv_ItemsCount);
        valueCount = (TextView) findViewById(R.id.tv_OrdervalueCount);
        ordersview = (Button) findViewById(R.id.btn_ordersview);


        agentOrders = (LinearLayout) findViewById(R.id.agent_ordersLayout);
        noOrders = (LinearLayout) findViewById(R.id.noOrders);

        ArrayList<TakeOrderPreviewBean> takeOrderPreviewBeanArrayList = new ArrayList<TakeOrderPreviewBean>();

        agentId = mPreferences.getString("agentId");
        ArrayList<OrdersListBean> ordersList = new ArrayList<>();

        loadOrders();

        Log.i("takeorderlist", String.valueOf(mTakeOrderBeansList.size()));

        // Bundle bundle = this.getIntent().getExtras();
        //if (bundle != null) {


        /* ObAmount=this.getIntent().getStringExtra("ObAmount");
        Ordervalue=this.getIntent().getStringExtra("OrderValue");
        receivedAmount=this.getIntent().getStringExtra("ReceivedAmount");
        Due=this.getIntent().getStringExtra("due");

*/


        sales = (LinearLayout) findViewById(R.id.linear_sales);
        sales.setVisibility(View.GONE);
        deliveries = (LinearLayout) findViewById(R.id.linear_deliveries);
        deliveries.setVisibility(View.GONE);
        returns = (LinearLayout) findViewById(R.id.linear_returns);
        returns.setVisibility(View.GONE);
        payments = (LinearLayout) findViewById(R.id.linear_payments);
        payments.setVisibility(View.GONE);
        orders = (LinearLayout) findViewById(R.id.linear_orders);
        orders.setVisibility(View.GONE);


        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(Agents_Tpc_OrdersActivity.this, "Clicked on TPC Orders", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                sales.startAnimation(animation1);

            }
        });
        deliveries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Agents_Tpc_OrdersActivity.this, "Clicked on Deliveries", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                deliveries.startAnimation(animation1);

                Intent i = new Intent(AgentTDC_Order.this, AgentDeliveries.class);
                // i.putExtra("agentID",agentId);
                startActivity(i);
                finish();

            }
        });
        payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Agents_Tpc_OrdersActivity.this, "Clicked on payments", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                payments.startAnimation(animation1);

                Intent i = new Intent(AgentTDC_Order.this, AgentPayments.class);
             /*   i.putExtra("ObAmount",ObAmount);
                i.putExtra("OrderValue",Ordervalue);
                i.putExtra("ReceivedAmount",receivedAmount);
                i.putExtra("due",Due);*/
                startActivity(i);
                finish();
            }
        });
        returns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(Agents_Tpc_OrdersActivity.this, "Clicked on returns", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                returns.startAnimation(animation1);

                Intent i = new Intent(AgentTDC_Order.this, AgentReturns.class);
                startActivity(i);
                finish();
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(Agents_Tpc_OrdersActivity.this, "Clicked on orders", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                orders.startAnimation(animation1);

                Intent i = new Intent(AgentTDC_Order.this, TDCSalesListActivity.class);
              //  i.putExtra("custId",agentId);
                i.putExtra("From", "Agents");
                // i.putExtra("screenType","customerDetails");
                startActivity(i);
                finish();
            }
        });
        ordersview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AgentTDC_Order.this, AgentsTDC_View.class);
                mSessionManagement.putString("endiddd",tv_enquiryId.getText().toString().trim());
                startActivity(i);
                finish();
            }
        });


        ArrayList<String> privilegeActionsData = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("Customers"));
        System.out.println("F 11111 ***COUNT === " + privilegeActionsData.size());
        for (int z = 0; z < privilegeActionsData.size(); z++) {

            System.out.println("Name::: " + privilegeActionsData.get(z).toString());
            if (privilegeActionsData.get(z).toString().equals("Orders_List")) {
                sales.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData.get(z).toString().equals("Delivery_List")) {
                deliveries.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData.get(z).toString().equals("Return_List")) {
                returns.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData.get(z).toString().equals("Payment_List")) {
                payments.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData.get(z).toString().equals("Take_Orders")) {
                orders.setVisibility(View.VISIBLE);
            }


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
        if (id == R.id.action_search) {

            return true;
        }

        if (id == R.id.autorenew) {
            if (new NetworkConnectionDetector(AgentTDC_Order.this).isNetworkConnected()) {
                mAgentOrdersModel.getOrdersList(agentId);
            } else {
                new NetworkConnectionDetector(AgentTDC_Order.this).displayNoNetworkError(AgentTDC_Order.this);
            }
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
        Intent intent = new Intent(this, AgentsActivity.class);
        startActivity(intent);
        finish();
    }

    /***
     * Method to load the orders data
     */
    public void loadOrders() {
        quantity = 0.0;
        price = 0.0;
        amount = 0.0;
        subtotal = 0.0;
        taxAmount = 0.0;
        mProductsPriceAmountSum = 0.0;
        mTotalProductsPriceAmountSum = 0.0;
        mTotalProductsTax = 0.0;
        totalprice = "";
        mTakeOrderBeansList = mDBHelper.fetchAllRecordsFromTakeOrderProductsTable("", mSessionManagement.getString("agentId"));
        System.out.println("UPDATED SIZE::: " + mTakeOrderBeansList.size());

        if (mTakeOrderBeansList.size() > 0) {
            agentOrders.setVisibility(View.VISIBLE);
            noOrders.setVisibility(View.GONE);
//            if (mTakeOrderBeansList.get(0).getmEnquiryId().contains("ENQ")) {
//                tv_enquiryId.setText(mTakeOrderBeansList.get(0).getmEnquiryId());
//            } else {
//                tv_enquiryId.setText("ENQ" + mTakeOrderBeansList.get(0).getmEnquiryId());
//            }
            tv_enquiryId.setText(mTakeOrderBeansList.get(0).getmEnquiryId());
            date.setText(mTakeOrderBeansList.get(0).getmAgentTakeOrderDate());
            mPreferences.putString("ORDERDATE", String.valueOf(date));
            itemCount = String.valueOf(mTakeOrderBeansList.size());
            tv_itemsCount.setText(itemCount);


            try {
                for (int j = 0; j < mTakeOrderBeansList.size(); j++) {
                    Log.i("take order price log", (mTakeOrderBeansList.get(j).getmAgentPrice() + "  count " + j + " size of takeorder" + mTakeOrderBeansList.size()));
                    price = mTakeOrderBeansList.get(j).getmAgentPrice() != null ? Double.parseDouble(mTakeOrderBeansList.get(j).getmAgentPrice()) : 0.00;
                    quantity = mTakeOrderBeansList.get(j).getmProductQuantity() != null ? Double.parseDouble(mTakeOrderBeansList.get(j).getmProductQuantity()) : 0.000;
                    tax = 0.0f;
                    if (mTakeOrderBeansList.get(j).getmAgentVAT() != null) {
                        tax = Float.parseFloat(mTakeOrderBeansList.get(j).getmAgentVAT());

                    } else if (mTakeOrderBeansList.get(j).getmAgentGST() != null) {
                        tax = Float.parseFloat(mTakeOrderBeansList.get(j).getmAgentGST());

                    }
                    taxAmount = ((quantity * price) * tax) / 100;
                    System.out.println("P TAX IS::: " + taxAmount);
                    //  amount = price + taxAmount;
                    amount = price;

                    subtotal = (price * quantity);

                    mProductsPriceAmountSum = (mProductsPriceAmountSum + (amount
                            * Double.parseDouble(mTakeOrderBeansList.get(j).getmProductQuantity())));
                    System.out.println("P SUBTOTAl IS::: " + mProductsPriceAmountSum);

                    mTotalProductsTax = (mTotalProductsTax + taxAmount);

                    mTotalProductsPriceAmountSum = (mProductsPriceAmountSum + mTotalProductsTax);
                    System.out.println("P FINAL IS::: " + mTotalProductsPriceAmountSum);
                    totalprice = String.valueOf(Utility.getFormattedCurrency(mTotalProductsPriceAmountSum));
                    valueCount.setText(totalprice);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (mTakeOrderBeansList.size() == 0) {
            agentOrders.setVisibility(View.GONE);
            noOrders.setVisibility(View.VISIBLE);
        }
    }


}
