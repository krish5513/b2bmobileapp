package com.rightclickit.b2bsaleon.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;


import java.util.ArrayList;

public class AgentViewActivity extends AppCompatActivity {

    private LinearLayout mTakeOrdersLayout;
    private LinearLayout mDeliveriesLayout;
    private LinearLayout mReturnsLayout;
    private LinearLayout mPaymentsLayout;
    private LinearLayout mTPCOrdersLayout;
    Button view;

    private DBHelper mDBHelper;
    private MMSharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_agent);


        mDBHelper = new DBHelper(AgentViewActivity.this);
        mPreferences = new MMSharedPreferences(AgentViewActivity.this);
        this.getSupportActionBar().setLogo(R.drawable.customers_white_24);
        this.getSupportActionBar().setTitle(mPreferences.getString("agentName"));
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);



        mTPCOrdersLayout = (LinearLayout) findViewById(R.id.TPCOrdersLayout);
        mTPCOrdersLayout.setVisibility(View.GONE);


                mTakeOrdersLayout = (LinearLayout) findViewById(R.id.TakeOrdersLayout);
        mTakeOrdersLayout.setVisibility(View.GONE);
                mTakeOrdersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mTakeOrdersLayout.startAnimation(animation1);
                Intent i =new Intent(AgentViewActivity.this,AgentTakeOrderScreen.class);
                startActivity(i);
                finish();
            }
        });

        mDeliveriesLayout = (LinearLayout) findViewById(R.id.DeliveriesLayout);
        mDeliveriesLayout.setVisibility(View.GONE);
        mDeliveriesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mDeliveriesLayout.startAnimation(animation1);
                Intent i =new Intent(AgentViewActivity.this,AgentDeliveries.class);
                startActivity(i);
                finish();
            }
        });
        mReturnsLayout = (LinearLayout) findViewById(R.id.ReturnsLayout);
        mReturnsLayout.setVisibility(View.GONE);
        mReturnsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mReturnsLayout.startAnimation(animation1);
                Intent i =new Intent(AgentViewActivity.this,AgentReturns.class);
                startActivity(i);
                finish();
            }
        });
        mPaymentsLayout = (LinearLayout) findViewById(R.id.PaymentsLayout);
        mPaymentsLayout.setVisibility(View.GONE);
        mPaymentsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation  animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mPaymentsLayout.startAnimation(animation1);
                Intent i =new Intent(AgentViewActivity.this,AgentPayments.class);
                startActivity(i);
                finish();
            }
        });
        view = (Button) findViewById(R.id.btn_view1);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =new Intent(AgentViewActivity.this,AgentsTDC_View.class);
                startActivity(i);
                finish();
            }
        });



        ArrayList<String> privilegeActionsData = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("Customers"));
        System.out.println("F 11111 ***COUNT === " + privilegeActionsData.size());
        for (int z = 0; z < privilegeActionsData.size(); z++) {

            System.out.println("Name::: " + privilegeActionsData.get(z).toString());
            if (privilegeActionsData.get(z).toString().equals("Orders_List")) {
                mTPCOrdersLayout.setVisibility(View.VISIBLE);
            }
           else if (privilegeActionsData.get(z).toString().equals("Delivery_List")) {
                mDeliveriesLayout.setVisibility(View.VISIBLE);
            }
           else if (privilegeActionsData.get(z).toString().equals("Return_List")) {
                mReturnsLayout.setVisibility(View.VISIBLE);
            }
           else if (privilegeActionsData.get(z).toString().equals("Payment_List")) {
                mPaymentsLayout.setVisibility(View.VISIBLE);
            }
            else if (privilegeActionsData.get(z).toString().equals("Take_Orders")) {
                mTakeOrdersLayout.setVisibility(View.VISIBLE);
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
        Intent intent = new Intent(this, AgentsActivity.class);
        startActivity(intent);
        finish();
    }






}

