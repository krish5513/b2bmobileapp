package com.rightclickit.b2bsaleon.activities;

import android.content.Intent;
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
import com.rightclickit.b2bsaleon.adapters.AgentTDC_ListAdapter;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;

import java.util.ArrayList;

public class TDCSales_Today extends AppCompatActivity {
    private ListView mAgentsList;
    private AgentTDC_ListAdapter mPreviewAdapter;
    TextView today;
    TextView monthly;
    TextView weekly;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_list);

        ArrayList<TDCSaleOrder> tdcBeanArrayList = new ArrayList<TDCSaleOrder>();

        this.getSupportActionBar().setTitle("AGENTS");
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




        mAgentsList = (ListView) findViewById(R.id.AgentsList);
        today=(TextView) findViewById(R.id.tv_today);
        monthly=(TextView) findViewById(R.id.tv_monthly);
        weekly=(TextView) findViewById(R.id.tv_weekly);



        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(TDCSales_Today.this,TDCSales_Month.class);
                startActivity(i);
                finish();
            }
        });

        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(TDCSales_Today.this,TDCSales_Weekly.class);
                startActivity(i);
                finish();
            }
        });

      if(tdcBeanArrayList.size()>0) {
    loadAgentsList(tdcBeanArrayList);


    }


    }

    private void loadAgentsList(ArrayList<TDCSaleOrder> tdcBeanArrayList) {
        if (mPreviewAdapter != null) {
            mPreviewAdapter = null;
        }
        mPreviewAdapter = new AgentTDC_ListAdapter(this, TDCSales_Today.this, tdcBeanArrayList);
        Log.i("previewadapter",mPreviewAdapter+"");

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
            Intent i =new Intent(TDCSales_Today.this,SalesActivity.class);
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
        menu.findItem( R.id.action_search).setVisible(true);
        menu.findItem( R.id.Add).setVisible(false);

        menu.findItem( R.id.autorenew).setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, SalesActivity.class);
        startActivity(intent);
        finish();
    }
}
