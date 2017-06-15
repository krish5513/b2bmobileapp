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
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;


import java.util.ArrayList;

public class ViewAgent extends AppCompatActivity {
    private MMSharedPreferences mPreference;
    private LinearLayout mTakeOrdersLayout;
    private LinearLayout mDeliveriesLayout;
    private LinearLayout mReturnsLayout;
    private LinearLayout mPaymentsLayout;
Button view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_agent);
        mPreference = new MMSharedPreferences(this);

        this.getSupportActionBar().setLogo(R.drawable.customers_white_24);
        this.getSupportActionBar().setTitle(mPreference.getString("agentName"));
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        mTakeOrdersLayout = (LinearLayout) findViewById(R.id.TakeOrdersLayout);
        mTakeOrdersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mTakeOrdersLayout.startAnimation(animation1);
                Intent i =new Intent(ViewAgent.this,TakeOrderScreen.class);
                startActivity(i);
                finish();
            }
        });

        mDeliveriesLayout = (LinearLayout) findViewById(R.id.DeliveriesLayout);
        mDeliveriesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mDeliveriesLayout.startAnimation(animation1);
                Intent i =new Intent(ViewAgent.this,AgentDeliveries.class);
                startActivity(i);
                finish();
            }
        });
        mReturnsLayout = (LinearLayout) findViewById(R.id.ReturnsLayout);
        mReturnsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mReturnsLayout.startAnimation(animation1);
                Intent i =new Intent(ViewAgent.this,AgentReturns.class);
                startActivity(i);
                finish();
            }
        });
        mPaymentsLayout = (LinearLayout) findViewById(R.id.PaymentsLayout);
        mPaymentsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation  animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mPaymentsLayout.startAnimation(animation1);
                Intent i =new Intent(ViewAgent.this,AgentPayments.class);
                startActivity(i);
                finish();
            }
        });
        view = (Button) findViewById(R.id.btn_view1);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =new Intent(ViewAgent.this,AgentsTDC_View.class);
                startActivity(i);
                finish();
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

