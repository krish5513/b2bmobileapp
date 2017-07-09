package com.rightclickit.b2bsaleon.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.rightclickit.b2bsaleon.adapters.AgentsAdapter;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.AgentsModel;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Sekhar Kuppa
 */
public class AgentsActivity extends AppCompatActivity {
    private Context applicationContext, activityContext;
    private AgentsModel agentsModel;
    private DBHelper mDBHelper;
    private MMSharedPreferences mPreferences;
    private LinearLayout mDashBoardLayout;
    private LinearLayout mTripsheetsLayout;
    private LinearLayout mCustomersLayout;
    private LinearLayout mProductsLayout;
    private LinearLayout mTDCLayout;
    private LinearLayout mRetailersLayout;
    FloatingActionButton fab;
    private ListView mAgentsList;
    private AgentsAdapter mAgentsAdapter;
    private SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agents);

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

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.customer60));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Clicked Customers Add",Toast.LENGTH_SHORT).show();
                Intent i =new Intent(AgentsActivity.this,Agents_AddActivity.class);
                startActivity(i);
                finish();
            }
        });


        applicationContext = getApplicationContext();
        activityContext = AgentsActivity.this;
        mDBHelper = new DBHelper(AgentsActivity.this);
        mPreferences = new MMSharedPreferences(AgentsActivity.this);
        agentsModel = new AgentsModel(activityContext,this);

        mAgentsList = (ListView) findViewById(R.id.AgentsList);
        mAgentsList.setVisibility(View.GONE);
       // ArrayList<AgentsBean> a = mDBHelper.fetchAllRecordsFromAgentsTable();
        //System.out.println("ELSE::: "+a.size());
        if (new NetworkConnectionDetector(AgentsActivity.this).isNetworkConnected()) {
            if(mDBHelper.getAgentsTableCount()>0){
                ArrayList<AgentsBean> agentsBeanArrayList = mDBHelper.fetchAllRecordsFromAgentsTable();


                /*Collections.sort(agentsBeanArrayList, new Comparator<AgentsBean>() {
                    @Override
                    public int compare(AgentsBean o1, AgentsBean o2) {
                        return o1.getmAgentName().compareToIgnoreCase(o2.getmAgentName());
                    }


                });
*/
                loadAgentsList(agentsBeanArrayList);
            }else {
                agentsModel.getAgentsList("agents");
            }
        }else {
           // System.out.println("ELSE::: ");
            ArrayList<AgentsBean> agentsBeanArrayList = mDBHelper.fetchAllRecordsFromAgentsTable();

            loadAgentsList(agentsBeanArrayList);
        }

        mDashBoardLayout = (LinearLayout) findViewById(R.id.DashboardLayout);
        mDashBoardLayout.setVisibility(View.GONE);
        mDashBoardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(AgentsActivity.this, "Clicked on Dashboard", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mDashBoardLayout.startAnimation(animation1);
                Intent i =new Intent(AgentsActivity.this,DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });
        mTripsheetsLayout = (LinearLayout) findViewById(R.id.TripSheetsLayout);
        mTripsheetsLayout.setVisibility(View.GONE);
        mTripsheetsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(AgentsActivity.this, "Clicked on Tripsheets", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mTripsheetsLayout.startAnimation(animation1);
                Intent i =new Intent(AgentsActivity.this,TripSheetsActivity.class);
                startActivity(i);
                finish();
            }
        });
        mCustomersLayout = (LinearLayout) findViewById(R.id.CustomersLayout);
        mCustomersLayout.setVisibility(View.GONE);
        mCustomersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Toast.makeText(AgentsActivity.this, "Clicked on Customers", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mCustomersLayout.startAnimation(animation1);
                Intent i =new Intent(AgentsActivity.this,AgentsActivity.class);
                startActivity(i);
                finish();
            }
        });
        mRetailersLayout = (LinearLayout) findViewById(R.id.RetailersLayout);
        mRetailersLayout.setVisibility(View.GONE);
        mRetailersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mRetailersLayout.startAnimation(animation1);
                Intent i = new Intent(AgentsActivity.this, RetailersActivity.class);
                startActivity(i);
                finish();
            }
        });
        mProductsLayout = (LinearLayout) findViewById(R.id.ProductsLayout);
        mProductsLayout.setVisibility(View.GONE);
        mProductsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(AgentsActivity.this, "Clicked on Products", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mProductsLayout.startAnimation(animation1);
                Intent i =new Intent(AgentsActivity.this,Products_Activity.class);
                startActivity(i);
                finish();
            }
        });
        mTDCLayout = (LinearLayout) findViewById(R.id.TDCLayout);
        mTDCLayout.setVisibility(View.GONE);
        mTDCLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(AgentsActivity.this, "Clicked on TDC", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mTDCLayout.startAnimation(animation1);
                Intent i =new Intent(AgentsActivity.this,SalesActivity.class);
                startActivity(i);
                finish();
            }
        });
        HashMap<String,String> userMapData = mDBHelper.getUsersData();
        ArrayList<String> privilegesData = mDBHelper.getUserActivityDetailsByUserId(userMapData.get("user_id"));
       // System.out.println("F 11111 ***COUNT === "+ privilegesData.size());
        for (int k = 0; k<privilegesData.size();k++){
            if (privilegesData.get(k).toString().equals("Dashboard")){
                mDashBoardLayout.setVisibility(View.VISIBLE);
            }else if (privilegesData.get(k).toString().equals("TripSheets")){
                mTripsheetsLayout.setVisibility(View.VISIBLE);
            }else if (privilegesData.get(k).toString().equals("Customers")){
                mCustomersLayout.setVisibility(View.VISIBLE);
            }else if (privilegesData.get(k).toString().equals("Products")){
                mProductsLayout.setVisibility(View.VISIBLE);
            }else if (privilegesData.get(k).toString().equals("TDC")){
                mTDCLayout.setVisibility(View.VISIBLE);
            }else if (privilegesData.get(k).toString().equals("Retailers")){
                mRetailersLayout.setVisibility(View.VISIBLE);
            }
        }

        ArrayList<String> privilegeActionsData = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("Customers"));
        //System.out.println("F 11111 ***COUNT === "+ privilegeActionsData.size());
        for (int z = 0;z<privilegeActionsData.size();z++){
            //System.out.println("Name::: "+ privilegeActionsData.get(z).toString());

            if (privilegeActionsData.get(z).toString().equals("List_View")){
                mAgentsList.setVisibility(View.VISIBLE);
            }
             else if (privilegeActionsData.get(z).toString().equals("Add")){
                fab.setVisibility(View.VISIBLE);
            }
        }
    }

    public void loadAgentsList(ArrayList<AgentsBean> mAgentsBeansList){
        if(mAgentsAdapter!=null){
            mAgentsAdapter = null;
        }
        mAgentsAdapter = new AgentsAdapter(this,AgentsActivity.this,mAgentsBeansList);
        mAgentsList.setAdapter(mAgentsAdapter);
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

                mAgentsAdapter.filter(query);

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
        menu.findItem( R.id.autorenew).setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AgentsActivity.this,DashboardActivity.class);
        startActivity(i);
        finish();
    }
}
