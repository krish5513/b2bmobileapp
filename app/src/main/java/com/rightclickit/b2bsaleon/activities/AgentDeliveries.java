package com.rightclickit.b2bsaleon.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.AgentDeliveriesAdapter;
import com.rightclickit.b2bsaleon.beanclass.TripSheetDeliveriesBean;
import com.rightclickit.b2bsaleon.beanclass.TripsheetSOList;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.AgentDeliveriesModel;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import java.util.ArrayList;

public class AgentDeliveries extends AppCompatActivity {
    LinearLayout sales;
    LinearLayout returns;
    LinearLayout payments;
    LinearLayout deliveries;
    LinearLayout orders;
    Button view;

    private TextView mNoDataText;

    private DBHelper mDBHelper;
    private MMSharedPreferences mPreferences;
    ArrayList<TripsheetSOList> tripsheetsoList;
    String tripsheetId, agentId = "";
    AgentDeliveriesAdapter deliveriesAdapter;
    ListView deliveriesList;
    private SearchView search;
    TextView tv_deliveries, tv_deliveriesValue, tv_pendingvalue;
    ArrayList<String> deliveriess = new ArrayList<>();
    ArrayList<String> mtripsheetId = new ArrayList<>();
    String d_no;
    private double totalAmount = 0;
    private double totalTaxAmount = 0;
    private double subTotal = 0;
    AgentDeliveriesModel deliveriesmodel;
    private String mDeliveryNo = "", mDeliverydate = "";
    ArrayList<TripSheetDeliveriesBean> unUploadedDeliveries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_deliveries);


        this.getSupportActionBar().setTitle("DELIVERIES");
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

        deliveriesmodel = new AgentDeliveriesModel(this, AgentDeliveries.this);


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


        // tv_deliveries=(TextView)findViewById(R.id.tv_totalDeliveries);
        // tv_deliveriesValue=(TextView)findViewById(R.id.tv_value);
        // tv_pendingvalue=(TextView)findViewById(R.id.tv_pendingvalue);


        deliveriesList = (ListView) findViewById(R.id.ordered_products_list_view);

        mNoDataText = (TextView) findViewById(R.id.NoDataText);
        deliveriesList.setEmptyView(mNoDataText);


        mDBHelper = new DBHelper(AgentDeliveries.this);
        mPreferences = new MMSharedPreferences(AgentDeliveries.this);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mDeliveryNo = bundle.getString("DeliveryId");
            mDeliverydate = bundle.getString("DeliveryDate");


        }
        agentId = mPreferences.getString("agentId");


        deliveriess = mDBHelper.getDeliverynumber(agentId, "tripsheet_delivery_number");
        if (deliveriess.size() > 0) {
            // tv_deliveries.setText(Integer.toString(deliveriess.size()));
        }


        mtripsheetId = mDBHelper.getTripId(agentId, "tripsheet_delivery_trip_id");





        ArrayList<TripSheetDeliveriesBean> unUploadedDeliveries = mDBHelper.fetchAllTripsheetsDeliveriesListForAgents(agentId);
        for (int i = 0; i < unUploadedDeliveries.size(); i++) {
            d_no = unUploadedDeliveries.get(i).getmTripsheetDeliveryNo();
        }
        ArrayList<String[]> arList = mDBHelper.getdeliveryDetailsPreview(d_no);
        for (int i = 0; i < arList.size(); i++) {
            String[] temp = arList.get(i);

            totalAmount = totalAmount + Double.parseDouble(temp[3]);
            totalTaxAmount = totalTaxAmount + Double.parseDouble(temp[4]);
            subTotal = totalAmount + totalTaxAmount;
// tv_deliveriesValue.setText(Utility.getFormattedCurrency((subTotal)));
        }
        if (unUploadedDeliveries.size() > 0) {
            loadDeliveries(unUploadedDeliveries);
        } else {
            mNoDataText.setText("No Deliveries found.");
        }

        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                sales.startAnimation(animation1);

                Intent i = new Intent(AgentDeliveries.this, AgentTDC_Order.class);
                startActivity(i);
                finish();
            }
        });
        deliveries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(Agents_DeliveriesActivity.this, "Clicked on Deliveries", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                deliveries.startAnimation(animation1);
            }
        });
        payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Agents_DeliveriesActivity.this, "Clicked on payments", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                payments.startAnimation(animation1);

                Intent i = new Intent(AgentDeliveries.this, AgentPayments.class);

                startActivity(i);
                finish();
            }
        });
        returns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Agents_DeliveriesActivity.this, "Clicked on returns", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                returns.startAnimation(animation1);

                Intent i = new Intent(AgentDeliveries.this, AgentReturns.class);
                startActivity(i);
                finish();
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(Agents_DeliveriesActivity.this, "Clicked on orders", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                orders.startAnimation(animation1);

                Intent i = new Intent(AgentDeliveries.this, TDCSalesListActivity.class);
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

    public void loadDeliveries(ArrayList<TripSheetDeliveriesBean> unUploadedDeliveries) {

        if (deliveriesAdapter != null) {
            deliveriesAdapter = null;
        }
        deliveriesAdapter = new AgentDeliveriesAdapter(this, AgentDeliveries.this, unUploadedDeliveries);
        deliveriesList.setAdapter(deliveriesAdapter);

    }

    public void loadDeliveries1() {
        ArrayList<TripSheetDeliveriesBean> unUploadedDeliveries1 = mDBHelper.fetchAllTripsheetsDeliveriesList(agentId);
        for (int i = 0; i < unUploadedDeliveries1.size(); i++) {
            d_no = unUploadedDeliveries1.get(i).getmTripsheetDeliveryNo();
        }
        ArrayList<String[]> arList = mDBHelper.getdeliveryDetailsPreview(d_no);
        for (int i = 0; i < arList.size(); i++) {
            String[] temp = arList.get(i);

            totalAmount = totalAmount + Double.parseDouble(temp[3]);
            totalTaxAmount = totalTaxAmount + Double.parseDouble(temp[4]);
            subTotal = totalAmount + totalTaxAmount;
            //   tv_deliveriesValue.setText(Utility.getFormattedCurrency((subTotal)));
        }
        if (unUploadedDeliveries1.size() > 0) {
            loadDeliveries(unUploadedDeliveries1);
        } else {
            mNoDataText.setText("No Deliveries found.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);

        try {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            search = (SearchView) menu.findItem(R.id.action_search).getActionView();
            search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    deliveriesAdapter.filter(query);
                    return true;
                }
            });

            // Get the search close button image view
            ImageView closeButton = (ImageView) search.findViewById(R.id.search_close_btn);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    search.setQuery("", false);
                    search.clearFocus();
                    search.onActionViewCollapsed();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {

            return true;
        }

        if (id == R.id.autorenew) {
            if (new NetworkConnectionDetector(AgentDeliveries.this).isNetworkConnected())
                deliveriesmodel.getDeliveriesList(agentId);
            else {
                new NetworkConnectionDetector(AgentDeliveries.this).displayNoNetworkError(AgentDeliveries.this);
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

}
