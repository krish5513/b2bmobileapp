package com.rightclickit.b2bsaleon.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.AgentsAdapter;
import com.rightclickit.b2bsaleon.adapters.TakeOrderPreviewAdapter;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderPreviewBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import java.util.ArrayList;

public class DashboardTakeorderPreview extends AppCompatActivity {
    private ListView mAgentsList;
    DBHelper mDBHelper;
    private TakeOrderPreviewAdapter mPreviewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_takeorder_preview);


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

        mDBHelper = new DBHelper(DashboardTakeorderPreview.this);
        mAgentsList = (ListView) findViewById(R.id.AgentsList);
        // ArrayList<AgentsBean> a = mDBHelper.fetchAllRecordsFromAgentsTable();
        //System.out.println("ELSE::: "+a.size());

                ArrayList<TakeOrderPreviewBean> previewArrayList = new ArrayList<>();
                loadAgentsList(previewArrayList);


/*
        mTakeOrderBeansList = mDBHelper.fetchAllRecordsFromTakeOrderProductsTable();
        System.out.println("The TO LIST IS::: "+ mTakeOrderBeansList.size());
        for (int k = 0; k<mTakeOrderBeansList.size();k++){
            System.out.println("AAAA:: "+mTakeOrderBeansList.get(k).getmProductFromDate());
            System.out.println("BBBBB:: "+mTakeOrderBeansList.get(k).getmProductQuantity());
            System.out.println("CCCC:: "+mTakeOrderBeansList.get(k).getmProductOrderType());
        }

        mTakeOrderListView = (ListView) findViewById(R.id.TakeOrdersList);
        if(mTakeOrderBeansList.size()>0){
            mTakeOrderAdapter = new TakeOrdersAdapter(DashboardTakeorder.this,mTakeOrderBeansList,mTakeOrderListView);
            mTakeOrderListView.setAdapter(mTakeOrderAdapter);
        }*/

    }

    private void loadAgentsList(ArrayList<TakeOrderPreviewBean> previewArrayList) {
        if(mPreviewAdapter!=null){
            mPreviewAdapter = null;
        }
        mPreviewAdapter = new TakeOrderPreviewAdapter(this,DashboardTakeorderPreview.this,previewArrayList);
        mAgentsList.setAdapter(mPreviewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Add) {
            Intent i =new Intent(DashboardTakeorderPreview.this,SalesListActivity.class);
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


        menu.findItem( R.id.notifications).setVisible(false);
        menu.findItem( R.id.settings).setVisible(false);
        menu.findItem( R.id.logout).setVisible(false);
        menu.findItem( R.id.action_search).setVisible(false);
        menu.findItem( R.id.Add).setVisible(false);



        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DashboardTakeorder.class);
        startActivity(intent);
        finish();
    }


}
