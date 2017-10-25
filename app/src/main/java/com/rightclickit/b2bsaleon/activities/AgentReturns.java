package com.rightclickit.b2bsaleon.activities;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.AgentReturnsAdapter;
import com.rightclickit.b2bsaleon.beanclass.AgentReturnsBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.AgentReturnsModel;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import java.util.ArrayList;

public class AgentReturns extends AppCompatActivity {
    LinearLayout sales;
    LinearLayout returns;
    LinearLayout payments;
    LinearLayout deliveries;
    LinearLayout orders;

    private TextView mNoDataText;

    Button view;
    private DBHelper mDBHelper;
    private MMSharedPreferences mPreferences;
    String tripsheetId,agentId="";
    AgentReturnsAdapter returnsAdapter;
    AgentReturnsModel mAgentReturnsModel;
    ListView deliveriesList;
    private SearchView search;
    TextView rDelivered,r_returned,r_pending;
    ArrayList<String> deliveriess,rdelivery;
    double str_pending,str_delivery,str_returns;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_returns);


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



        // rDelivered=(TextView)findViewById((R.id.tv_rDeliverd));
       // r_returned=(TextView)findViewById(R.id.tv_returned);
       // r_pending=(TextView)findViewById(R.id.tv_rpending);

        deliveriesList=(ListView)findViewById(R.id.ordered_products_list_view) ;
        mNoDataText = (TextView) findViewById(R.id.NoDataText);
        deliveriesList.setEmptyView(mNoDataText);


        mDBHelper = new DBHelper(AgentReturns.this);
        mPreferences = new MMSharedPreferences(AgentReturns.this);

        mAgentReturnsModel=new AgentReturnsModel(this,AgentReturns.this);
        //Bundle bundle = getIntent().getExtras();
        //if (bundle != null) {
        agentId = mPreferences.getString("agentId");

/*

        deliveriess = mDBHelper.getReturnnumber(agentId, "tripsheet_returns_return_number");
        if(deliveriess.size()>0) {
            r_returned.setText(Integer.toString(deliveriess.size()));
        }

        rdelivery=mDBHelper.getDeliverynumber(agentId,"tripsheet_delivery_number");
        if(rdelivery.size()>0) {
            rDelivered.setText(Integer.toString(rdelivery.size()));
        }
        str_delivery= Double.parseDouble(String.valueOf((rdelivery.size())));
        str_returns= Double.parseDouble(String.valueOf(deliveriess.size()));
        str_pending=str_delivery-str_returns;
*/

        //r_pending.setText(Utility.getFormattedCurrency(str_pending));

        ArrayList<AgentReturnsBean> unUploadedDeliveries = mDBHelper.getreturnsDetails(agentId);

        if(unUploadedDeliveries.size()>0){
            loadReturns(unUploadedDeliveries);
        }else {
            mNoDataText.setText("No Returns found.");
        }
        this.getSupportActionBar().setTitle("RETURNS");
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

        mDBHelper = new DBHelper(AgentReturns.this);
        mPreferences = new MMSharedPreferences(AgentReturns.this);

        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(Agents_ReturnsActivity.this, "Clicked on TPC Orders", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                sales.startAnimation(animation1);

                Intent i =new Intent(AgentReturns.this,AgentTDC_Order.class);
                startActivity(i);
                finish();
            }
        });
        deliveries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(Agents_ReturnsActivity.this, "Clicked on Deliveries", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                deliveries.startAnimation(animation1);

                Intent i =new Intent(AgentReturns.this,AgentDeliveries.class);
                startActivity(i);
                finish();

            }
        });
        payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(Agents_ReturnsActivity.this, "Clicked on payments", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                payments.startAnimation(animation1);

                Intent i =new Intent(AgentReturns.this,AgentPayments.class);
                startActivity(i);
                finish();
            }
        });
        returns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(Agents_ReturnsActivity.this, "Clicked on returns", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                returns.startAnimation(animation1);


            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Agents_ReturnsActivity.this, "Clicked on orders", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                orders.startAnimation(animation1);

                Intent i =new Intent(AgentReturns.this,AgentTakeOrderScreen.class);
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

    public void loadReturns(ArrayList<AgentReturnsBean> unUploadedDeliveries) {
        if (returnsAdapter != null) {
            returnsAdapter = null;
        }
        returnsAdapter = new AgentReturnsAdapter(this, AgentReturns.this, unUploadedDeliveries);
        deliveriesList.setAdapter(returnsAdapter);

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
            if (new NetworkConnectionDetector(AgentReturns.this).isNetworkConnected()) {
                mAgentReturnsModel.getReturnsList(agentId);
            } else {
                new NetworkConnectionDetector(AgentReturns.this).displayNoNetworkError(AgentReturns.this);
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
        menu.findItem( R.id.Add).setVisible(false);
        menu.findItem( R.id.autorenew).setVisible(true);
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
