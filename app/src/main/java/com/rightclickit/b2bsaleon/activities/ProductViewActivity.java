package com.rightclickit.b2bsaleon.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.rightclickit.b2bsaleon.R;

public class ProductViewActivity extends AppCompatActivity {
    EditText code;
    EditText description;
    EditText returnable;
    EditText moq;
    EditText agent;
    EditText retailer;
    EditText consumer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        this.getSupportActionBar().setTitle("PRODUCTS VIEW");
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

        code=(EditText)findViewById(R.id.material_code) ;
        description=(EditText)findViewById(R.id.material_disc) ;
        returnable=(EditText)findViewById(R.id.Returnable) ;
        moq=(EditText)findViewById(R.id.MOQ) ;
        agent=(EditText)findViewById(R.id.agent) ;
        retailer=(EditText)findViewById(R.id.retailer) ;
        consumer=(EditText)findViewById(R.id.Consumer) ;

        Bundle bundle = getIntent().getExtras();

        //Extract the data…
       /* String str_code= bundle.getString("CODE");
        String str_des = bundle.getString("DESCRIPTION");
        String str_returnable = bundle.getString("RETURNABLE");
        String str_moq = bundle.getString("MOQ");
        String str_agent = bundle.getString("AGENT");
        String str_retailer = bundle.getString("RETAILER");
        String str_consumer = bundle.getString("CONSUMER");


        code.setText(str_code);
        description.setText(str_des);
        returnable.setText(str_returnable);
        moq.setText(str_moq);
        agent.setText(str_agent);
        retailer.setText(str_retailer);
        consumer.setText(str_consumer);*/

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
        menu.findItem(R.id.action_search).setVisible(false);


        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Products_Activity.class);
        startActivity(intent);
        finish();
    }
    }

