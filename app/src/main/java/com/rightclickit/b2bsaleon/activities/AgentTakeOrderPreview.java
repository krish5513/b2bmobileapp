package com.rightclickit.b2bsaleon.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TakeOrderPreviewAdapter;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderBean;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderPreviewBean;
import com.rightclickit.b2bsaleon.database.DBHelper;

import java.util.ArrayList;

public class AgentTakeOrderPreview extends AppCompatActivity {
    private ListView mAgentsList;
    DBHelper mDBHelper;
    private TakeOrderPreviewAdapter mPreviewAdapter;
    TextView tv_companyName;
    TextView Route_Name;
    TextView RouteCode;
    TextView orderNo;
    TextView orderDate;
    TextView user_Name;
    TextView agentName;

    TextView agentCode;

    TextView print;

    private ArrayList<TakeOrderBean> mProductIdsList;
    private ArrayList<TakeOrderPreviewBean> takeOrderPreviewBeanArrayList = new ArrayList<TakeOrderPreviewBean>();
    private double mProductsPriceAmountSum = 0.0, mTotalProductsPriceAmountSum = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_take_order_preview);

        Bundle bundle = getIntent().getExtras();
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
        mDBHelper = new DBHelper(AgentTakeOrderPreview.this);

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        mProductIdsList = (ArrayList<TakeOrderBean>) args.getSerializable("productIdsList");
        System.out.println("SIZE::: "+ mProductIdsList.size());
        ArrayList<ProductsBean> productsList = mDBHelper.fetchAllRecordsFromProductsTable();
        System.out.println("SIZE111::: "+ productsList.size());
        for (int k = 0; k < mProductIdsList.size(); k++) {
            for (int i = 0; i < productsList.size(); i++) {
                if (mProductIdsList.get(k).getmProductId().toString().equals(productsList.get(i).getProductId())) {
                    System.out.println("P TITLE IS::: " + productsList.get(i).getProductTitle());

                    mProductsPriceAmountSum = (mProductsPriceAmountSum + (Double.parseDouble(productsList.get(i).getProductAgentPrice())
                            * Double.parseDouble(mProductIdsList.get(k).getmProductQuantity())));
                    System.out.println("P PRICE IS::: " + mProductsPriceAmountSum);


                    TakeOrderPreviewBean topBean = new TakeOrderPreviewBean();
                    topBean.setpName(mProductIdsList.get(k).getmProductTitle());
                    topBean.setpQuantity(mProductIdsList.get(k).getmProductQuantity());
                    topBean.setpPrice(productsList.get(i).getProductAgentPrice());
                    topBean.setmProductTaxGST(productsList.get(i).getProductgst());
                    topBean.setmProductTaxVAT(productsList.get(i).getProductvat());
                    topBean.setmProductFromDate(mProductIdsList.get(k).getmProductFromDate());
                    topBean.setmProductToDate(mProductIdsList.get(k).getmProductToDate());

                    double price = Double.parseDouble(productsList.get(i).getProductAgentPrice().replace(",", ""));

                    float tax = 0.0f;
                    if (productsList.get(i).getProductvat() != null)
                        tax = Float.parseFloat(productsList.get(i).getProductvat());
                    else if (productsList.get(i).getProductgst() != null)
                        tax = Float.parseFloat(productsList.get(i).getProductgst());

                    double taxAmount = (price * tax) / 100;
                    double amount = price + taxAmount;
                    mTotalProductsPriceAmountSum = (mTotalProductsPriceAmountSum + (amount
                            *Double.parseDouble(mProductIdsList.get(k).getmProductQuantity())));
                    System.out.println("FINAL AMOUNT PRICE IS::: " + mTotalProductsPriceAmountSum);

                    takeOrderPreviewBeanArrayList.add(topBean);
                }
            }
        }

        tv_companyName = (TextView) findViewById(R.id.tv_companyName);
        user_Name = (TextView) findViewById(R.id.user_Name);
        Route_Name = (TextView) findViewById(R.id.user_Name);
        RouteCode = (TextView) findViewById(R.id.user_Name);
        orderNo = (TextView) findViewById(R.id.user_Name);
        orderDate = (TextView) findViewById(R.id.user_Name);
        agentName = (TextView) findViewById(R.id.user_Name);
        agentCode = (TextView) findViewById(R.id.user_Name);


        print = (TextView) findViewById(R.id.tv_print);

        // user_Name.setText(bundle.getString("USERNAME"));


        // SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // String imgSett = prefs.getString("companyName", "");
        //    String ma=bundle.getString("COMPANYNAME");
        //   Log.i("kjhdfcioseahdf",ma);
        //  tv_companyName.setText(imgSett);


        mAgentsList = (ListView) findViewById(R.id.AgentsList);
        // ArrayList<AgentsBean> a = mDBHelper.fetchAllRecordsFromAgentsTable();
        //System.out.println("ELSE::: "+a.size());

        //   ArrayList<TakeOrderPreviewBean> previewArrayList = new ArrayList<>();
        if (takeOrderPreviewBeanArrayList.size() > 0) {
            loadAgentsList(takeOrderPreviewBeanArrayList);
        }

    }

    private void loadAgentsList(ArrayList<TakeOrderPreviewBean> takeOrderPreviewBeanArrayList) {
        if (mPreviewAdapter != null) {
            mPreviewAdapter = null;
        }
        mPreviewAdapter = new TakeOrderPreviewAdapter(this, AgentTakeOrderPreview.this, takeOrderPreviewBeanArrayList);
        mAgentsList.setAdapter(mPreviewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Add) {
            Intent i = new Intent(AgentTakeOrderPreview.this, SalesListActivity.class);
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


        menu.findItem(R.id.notifications).setVisible(false);
        menu.findItem(R.id.settings).setVisible(false);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.Add).setVisible(false);


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, AgentTakeOrderScreen.class);
        startActivity(intent);
        finish();
    }

}