package com.rightclickit.b2bsaleon.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.AgentPaymentsAdapter;
import com.rightclickit.b2bsaleon.beanclass.PaymentsBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.AgentPaymentsModel;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import java.util.ArrayList;

public class AgentPayments extends AppCompatActivity {
    LinearLayout sales;
    LinearLayout returns;
    LinearLayout payments;
    LinearLayout deliveries;
    LinearLayout orders;
    Button view;
    private DBHelper mDBHelper;
    private MMSharedPreferences mPreferences;
    String agentId="",payment_no,paymentid;
    AgentPaymentsAdapter paymentsAdapter;
    ListView paymentsList;
    FloatingActionButton fab;
    String ObAmount="",Ordervalue="",receivedAmount="",Due="";
    private TextView mNoDataText;

    TextView tv_obAmount,tv_orderValue,tv_receivedAmount,tv_due;
    AgentPaymentsModel  paymentsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_payments);


        this.getSupportActionBar().setTitle("PAYMENTS");
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

        mDBHelper = new DBHelper(AgentPayments.this);
        mPreferences = new MMSharedPreferences(AgentPayments.this);

        paymentsModel=new AgentPaymentsModel(this,AgentPayments.this);

        ObAmount=mPreferences.getString("ObAmount");
        Ordervalue=mPreferences.getString("OrderValue");
        receivedAmount=mPreferences.getString("ReceivedAmount");
        Due=mPreferences.getString("due");

        //tv_obAmount=(TextView)findViewById(R.id.tv_obAmount) ;
        //tv_orderValue=(TextView)findViewById(R.id.tv_orderValue) ;
        // tv_receivedAmount=(TextView)findViewById(R.id.tv_receivedAmount) ;
        // tv_due=(TextView)findViewById(R.id.tv_due) ;
/*

        tv_obAmount.setText(ObAmount);
        tv_orderValue.setText(Ordervalue);
        tv_receivedAmount.setText(receivedAmount);
        tv_due.setText(Due);
*/

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.customer60));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Clicked Customers Add",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AgentPayments.this, AgentAddPayment.class);
                i.putExtra("ObAmount",ObAmount);
                i.putExtra("OrderValue",Ordervalue);
                i.putExtra("ReceivedAmount",receivedAmount);
                i.putExtra("due",Due);
                startActivity(i);
                finish();
            }
        });

        paymentsList=(ListView)findViewById(R.id.ordered_products_list_view) ;
        mNoDataText = (TextView) findViewById(R.id.NoDataText);
        paymentsList.setEmptyView(mNoDataText);

        agentId = mPreferences.getString("agentId");





        ArrayList<PaymentsBean> unUploadedPayments = mDBHelper.getpaymentDetailsForAgents(agentId);
        System.out.println("PAYMENTS SIZE::: "+ unUploadedPayments.size());

      /*  for (int i = 0; i < unUploadedPayments.size(); i++) {
            // return_no = unUploadedReturns.get(i).getmTripshhetReturnsReturn_no();
            payment_no = unUploadedPayments.get(i).getPayments_paymentsNumber();
            paymentid=unUploadedPayments.get(i).getPayments_tripsheetId();
        }

        ArrayList<String[]> arList = mDBHelper.getpaymentDetailsPreview(payment_no,paymentid);
        for (int i = 0; i < arList.size(); i++) {
            String[] temp = arList.get(i);

          *//*  totalAmount = totalAmount + Double.parseDouble(temp[3]);
            totalTaxAmount = totalTaxAmount + Double.parseDouble(temp[4]);
            subTotal = totalAmount + totalTaxAmount;
            //   tv_deliveriesValue.setText(Utility.getFormattedCurrency((subTotal)));*//*
        }*/
        if(unUploadedPayments.size()>0){
            loadPayments(unUploadedPayments);
        }else {
            mNoDataText.setText("No Payments Found."+"\n"+"Please click on sync button to get the payments.");
        }

        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(Agents_PaymentsActivity.this, "Clicked on TPC Orders", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                sales.startAnimation(animation1);

                Intent i =new Intent(AgentPayments.this,AgentTDC_Order.class);
                startActivity(i);
                finish();
            }
        });
        deliveries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Agents_PaymentsActivity.this, "Clicked on Deliveries", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                deliveries.startAnimation(animation1);

                Intent i =new Intent(AgentPayments.this,AgentDeliveries.class);
                startActivity(i);
                finish();

            }
        });
        payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(Agents_PaymentsActivity.this, "Clicked on payments", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                payments.startAnimation(animation1);
                Intent i =new Intent(AgentPayments.this,AgentPayments.class);

                startActivity(i);
                finish();

            }
        });
        returns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Agents_PaymentsActivity.this, "Clicked on returns", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                returns.startAnimation(animation1);

                Intent i =new Intent(AgentPayments.this,AgentReturns.class);
                startActivity(i);
                finish();
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Agents_PaymentsActivity.this, "Clicked on orders", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                orders.startAnimation(animation1);

                Intent i =new Intent(AgentPayments.this,TDCSalesListActivity.class);
                // i.putExtra("custId",mPreferences.getString("agentId"));
                // i.putExtra("screenType","customerDetails");
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

    public void loadPayments(ArrayList<PaymentsBean> unUploadedPayments) {
        if (paymentsAdapter != null) {
            paymentsAdapter = null;
        }
        paymentsAdapter = new AgentPaymentsAdapter(this, AgentPayments.this, unUploadedPayments);
        paymentsList.setAdapter(paymentsAdapter);
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
            if (new NetworkConnectionDetector(AgentPayments.this).isNetworkConnected()) {
                mNoDataText.setVisibility(View.GONE);
                showAlertDialog(AgentPayments.this, "Sync process", "Are you sure, you want start the sync process?");
            }

            else {
                new NetworkConnectionDetector(AgentPayments.this).displayNoNetworkError(AgentPayments.this);
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
        menu.findItem(R.id.action_search).setVisible(true);
        menu.findItem( R.id.Add).setVisible(false);
        menu.findItem( R.id.autorenew).setVisible(true);
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

    public void loadPayments1() {
        ArrayList<PaymentsBean> unUploadedPayments = mDBHelper.getpaymentDetailsForAgents(agentId);
       /* for (int i = 0; i < unUploadedPayments.size(); i++) {
            // return_no = unUploadedReturns.get(i).getmTripshhetReturnsReturn_no();
            payment_no = unUploadedPayments.get(i).getPayments_paymentsNumber();
            paymentid=unUploadedPayments.get(i).getPayments_tripsheetId();
        }

        ArrayList<String[]> arList = mDBHelper.getpaymentDetailsPreview(payment_no,paymentid);
        for (int i = 0; i < arList.size(); i++) {
            String[] temp = arList.get(i);

          *//*  totalAmount = totalAmount + Double.parseDouble(temp[3]);
            totalTaxAmount = totalTaxAmount + Double.parseDouble(temp[4]);
            subTotal = totalAmount + totalTaxAmount;
            //   tv_deliveriesValue.setText(Utility.getFormattedCurrency((subTotal)));*//*
        }

*/
        if(unUploadedPayments.size()>0){
            loadPayments(unUploadedPayments);
        }else {
            mNoDataText.setText("No Payments Found."+"\n"+"Please click on sync button to get the payments.");
        }
    }
    private void showAlertDialog(Context context, String title, String message) {
        try {
            android.support.v7.app.AlertDialog alertDialog = null;
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    paymentsModel.getPaymentsList(agentId);
                }
            });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
