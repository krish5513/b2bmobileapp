package com.rightclickit.b2bsaleon.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.AgentStockAdapter;
import com.rightclickit.b2bsaleon.adapters.AgentsAdapter;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.beanclass.AgentsStockBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.AgentsStockModel;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import java.util.ArrayList;

public class AgentStockActivity extends AppCompatActivity {
    private DBHelper mDBHelper;
    private MMSharedPreferences sharedPreferences;
    private AgentsStockModel mAgentStockModel;
    private ListView mStockList;
    private AgentStockAdapter mStockAdapter;
    private Context activityContext;
    private String mAgentId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAgentId = this.getIntent().getStringExtra("agentId");
        setContentView(R.layout.activity_agent_stock);

        this.getSupportActionBar().setTitle("AS ON STOCK");
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

        this.activityContext = AgentStockActivity.this;

        mStockList = (ListView) findViewById(R.id.agent_stock_preview);

        this.mAgentStockModel = new AgentsStockModel(activityContext, AgentStockActivity.this);

        mDBHelper = new DBHelper(AgentStockActivity.this);
        sharedPreferences = new MMSharedPreferences(AgentStockActivity.this);

        if (new NetworkConnectionDetector(AgentStockActivity.this).isNetworkConnected()) {
            mAgentStockModel.getAgentsStock(mAgentId);
        } else {
            if (mDBHelper.getAgentsStockTableCount() > 0) {
                loadAgentsStockList();
            } else {
                // No stock available
            }
        }
    }

    public void loadAgentsStockList() {
        ArrayList<AgentsStockBean> stockBeanArrayList = mDBHelper.fetchAllStockByAgentId(mAgentId);
        if (mStockAdapter != null) {
            mStockAdapter = null;
        }
        if (stockBeanArrayList.size() > 0) {
            mStockAdapter = new AgentStockAdapter(this, AgentStockActivity.this, stockBeanArrayList);
            mStockList.setAdapter(mStockAdapter);
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

            if (new NetworkConnectionDetector(AgentStockActivity.this).isNetworkConnected()) {
                mAgentStockModel.getAgentsStock(mAgentId);
            } else {
                new NetworkConnectionDetector(AgentStockActivity.this).displayNoNetworkError(AgentStockActivity.this);
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
        menu.findItem(R.id.Add).setVisible(false);

        menu.findItem(R.id.autorenew).setVisible(true);
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
