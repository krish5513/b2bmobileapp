package com.rightclickit.b2bsaleon.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TakeOrdersAdapter;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.ArrayList;

public class TakeOrderScreen extends AppCompatActivity {
    private MMSharedPreferences mPreference;
    private ListView mTakeOrderListView;
    private TakeOrdersAdapter mTakeOrderAdapter;
    private ArrayList<TakeOrderBean> mTakeOrderBeansList = new ArrayList<TakeOrderBean>();
    private DBHelper mDBHelper;

    private LinearLayout mTakeOrdersLayout;
    private LinearLayout mDeliveriesLayout;
    private LinearLayout mReturnsLayout;
    private LinearLayout mTDCorderLayout;

    public static LinearLayout mPaymentsLayout;
    public static FloatingActionButton fab;
    private SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agents_take_order);
        mPreference = new MMSharedPreferences(this);
        mDBHelper = new DBHelper(this);

        this.getSupportActionBar().setLogo(R.drawable.customers_white_24);
        this.getSupportActionBar().setTitle(mPreference.getString("agentName"));
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        mTakeOrderBeansList = mDBHelper.fetchAllRecordsFromTakeOrderProductsTable("no");
        System.out.println("The TO LIST IS::: "+ mTakeOrderBeansList.size());
        for (int k = 0; k<mTakeOrderBeansList.size();k++){
            System.out.println("AAAA:: "+mTakeOrderBeansList.get(k).getmProductFromDate());
            System.out.println("BBBBB:: "+mTakeOrderBeansList.get(k).getmProductQuantity());
            System.out.println("CCCC:: "+mTakeOrderBeansList.get(k).getmProductOrderType());
        }

        mTakeOrderListView = (ListView) findViewById(R.id.TakeOrdersList);
        if(mTakeOrderBeansList.size()>0){
            mTakeOrderAdapter = new TakeOrdersAdapter(this,mTakeOrderBeansList,mTakeOrderListView);
            mTakeOrderListView.setAdapter(mTakeOrderAdapter);
        }

        mPaymentsLayout = (LinearLayout) findViewById(R.id.PaymentsLayout);

        mTDCorderLayout = (LinearLayout) findViewById(R.id.TPCLayout);
        mTDCorderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                mTDCorderLayout.startAnimation(animation1);
                Intent i =new Intent(TakeOrderScreen.this,AgentTDC_Order.class);
                startActivity(i);
                finish();


            }
        });

        mDeliveriesLayout = (LinearLayout) findViewById(R.id.DeliveriesTakeOrder);
        mDeliveriesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                mDeliveriesLayout.startAnimation(animation1);

                Intent i =new Intent(TakeOrderScreen.this,AgentDeliveries.class);
                startActivity(i);
                finish();

            }
        });
        mReturnsLayout = (LinearLayout) findViewById(R.id.ReturnsTakeOrder);
        mReturnsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                mReturnsLayout.startAnimation(animation1);

                Intent i =new Intent(TakeOrderScreen.this,AgentReturns.class);
                startActivity(i);
                finish();

            }
        });
        mPaymentsLayout = (LinearLayout) findViewById(R.id.PaymentsTakeOrder);
        mPaymentsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                mPaymentsLayout.startAnimation(animation1);

                Intent i =new Intent(TakeOrderScreen.this,AgentPayments.class);
                startActivity(i);
                finish();

            }
        });



        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_eye_white_24dp));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        search = (SearchView) menu.findItem(R.id.action_search).getActionView();

        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                mTakeOrderAdapter.filter(query);

                return true;

            }

        });

        // Get the search close button image view
        ImageView closeButton = (ImageView)search.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setQuery("", false);
                search.clearFocus();
                search.onActionViewCollapsed();
            }
        });




        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_search) {
//
//            return true;
//        }

        switch (item.getItemId()) {
            case android.R.id.home:
                if(search.isIconified()) {
                    onBackPressed();
                }else {
                    search.setQuery("", false);
                    search.clearFocus();
                    search.onActionViewCollapsed();
                }
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
        Intent intent = new Intent(this, ViewAgent.class);
        startActivity(intent);
        finish();
    }






}

