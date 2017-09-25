package com.rightclickit.b2bsaleon.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.AgentStockAdapter;
import com.rightclickit.b2bsaleon.adapters.AgentStockDeliveryAdapter;
import com.rightclickit.b2bsaleon.beanclass.AgentsStockBean;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.AgentStockListener;
import com.rightclickit.b2bsaleon.models.AgentsStockModel;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sekhar Kuppa.
 */

public class AgentStockDeliveryActivity extends AppCompatActivity implements AgentStockListener {
    private DBHelper mDBHelper;
    private MMSharedPreferences sharedPreferences;
    private AgentsStockModel mAgentStockModel;
    private ListView mStockList;
    private AgentStockDeliveryAdapter mStockAdapter;
    private Context activityContext;
    private String mAgentId = "", mAgentCode = "", mAgentName = "";
    private SearchView search;
    TextView stock_delivery_print, stock_delivery_save;
    private ArrayList<ProductsBean> allProductsList;
    private Map<String, String> selectedProductsStockListHashMap;
    private Map<String, String> selectedProductsStockListHashMapNameCode;
    private Map<String, String> selectedProductsStockListHashMapIdUOM;
    private ArrayList<String> productIdsList = new ArrayList<String>();
    private ArrayList<String> productValuesList = new ArrayList<String>();
    private ArrayList<String> productCodesList = new ArrayList<String>();
    private ArrayList<String> productNamesList = new ArrayList<String>();
    private ArrayList<String> productUOMsList = new ArrayList<String>();
    private ArrayList<AgentsStockBean> agentsStockBeanList = new ArrayList<AgentsStockBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAgentId = this.getIntent().getStringExtra("agentId");
        mAgentCode = this.getIntent().getStringExtra("agentCode");
        mAgentName = this.getIntent().getStringExtra("agentName");
        System.out.println("AGENT ID ======= " + mAgentId + "\n");
        System.out.println("AGENT CODE ======= " + mAgentCode + "\n");
        System.out.println("AGENT NAME ======= " + mAgentName + "\n");

        setContentView(R.layout.activity_agent_stock_delivery);

        this.getSupportActionBar().setTitle(mAgentName);
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

        mDBHelper = new DBHelper(AgentStockDeliveryActivity.this);
        selectedProductsStockListHashMap = new HashMap<String, String>();
        selectedProductsStockListHashMapNameCode = new HashMap<String, String>();
        selectedProductsStockListHashMapIdUOM = new HashMap<String, String>();
        allProductsList = new ArrayList<>();

        this.activityContext = AgentStockDeliveryActivity.this;

        mStockList = (ListView) findViewById(R.id.agent_stock_delivery_list);

        stock_delivery_save = (TextView) findViewById(R.id.stock_delivery_save);

        allProductsList = mDBHelper.fetchAllRecordsFromProductsTable();

        if (allProductsList.size() > 0) {
            if (mStockAdapter != null) {
                mStockAdapter = null;
            }
            mStockAdapter = new AgentStockDeliveryAdapter(this, AgentStockDeliveryActivity.this, this, allProductsList);
            mStockList.setAdapter(mStockAdapter);
        }

        stock_delivery_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("IN 0000000000::: " + selectedProductsStockListHashMap.size());
                if (selectedProductsStockListHashMap.size() > 0) {
                    synchronized (this) {
                        System.out.println("IN 11111111111111");
                        for (Map.Entry<String, String> productsBeanEntry : selectedProductsStockListHashMap.entrySet()) {
                            productIdsList.add(productsBeanEntry.getKey());
                            productValuesList.add(productsBeanEntry.getValue());
                        }
//                        for (int g = 0; g < allProductsList.size(); g++) {
//                            if (selectedProductsStockListHashMap.get(allProductsList.get(g).getProductId()) != null) {
//                                if (allProductsList.get(g).getProductId().equals(
//                                        selectedProductsStockListHashMap.get(allProductsList.get(g).getProductId()))) {
//
//                                    AgentsStockBean agentsStockBean = new AgentsStockBean();
//
//                                    agentsStockBean.setmProductName(allProductsList.get(g).getProductTitle());
//                                    agentsStockBean.setmProductCode(allProductsList.get(g).getProductCode());
//                                    agentsStockBean.setmProductId(allProductsList.get(g).getProductId());
//                                    agentsStockBean.setmProductStockQunatity(selectedProductsStockListHashMap.get(allProductsList.get(g).getProductId()));
//                                    agentsStockBean.setmProductDeliveryQunatity("0");
//                                    agentsStockBean.setmProductUOM(allProductsList.get(g).getProductUOM());
//                                    agentsStockBean.setmProductCBQuantity("0");
//
//                                    agentsStockBeanList.add(agentsStockBean);
//                                }
//                            }
//                        }
                    }
                    synchronized (this) {
                        System.out.println("IN 22222222222222222:::" + productIdsList.size());
//                        if (agentsStockBeanList.size() > 0) {
//                            mDBHelper.insertAgentsStockListData1(agentsStockBeanList, mAgentId);
//                        }
                        mDBHelper.updateAgentStockAfterAgentDeliverFromStock(mAgentId, productIdsList, productValuesList);
                    }
                }
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
//        if (id == R.id.action_search) {
//
//            return true;
//        }

//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            default:
//                return true;
//        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        menu.findItem(R.id.notifications).setVisible(false);
        menu.findItem(R.id.settings).setVisible(false);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(true);
        menu.findItem(R.id.Add).setVisible(false);
        menu.findItem(R.id.autorenew).setVisible(false);
        //menu.findItem(R.id.sort).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void updateSelectedProductsList(Map<String, String> selectedProductsList, Map<String, String> selectedProductsListNameCode
            , Map<String, String> selectedProductsListIdUOM) {
        this.selectedProductsStockListHashMap = selectedProductsList;
        this.selectedProductsStockListHashMapNameCode = selectedProductsListNameCode;
        this.selectedProductsStockListHashMapIdUOM = selectedProductsListIdUOM;
    }
}
