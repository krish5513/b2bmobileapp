package com.rightclickit.b2bsaleon.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

public class ProductViewActivity extends AppCompatActivity {
    EditText code;
    EditText description;
    EditText returnable;
    EditText moq;
    EditText agent;
    EditText retailer;
    EditText consumer;
    ImageView image;
    private MMSharedPreferences mPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        mPreference = new MMSharedPreferences(this);

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
        image=(ImageView)findViewById(R.id.productimageview) ;
     /*   Bundle bundle = getIntent().getExtras();

        //Extract the data…
        String str_code= bundle.getString("CODE");
        String str_des = bundle.getString("DESCRIPTION");
        String str_returnable = bundle.getString("RETURNABLE");
        String str_moq = bundle.getString("MOQ");
        String str_agent = bundle.getString("AGENT");
        String str_retailer = bundle.getString("RETAILER");
        String str_consumer = bundle.getString("CONSUMER");
*/
        code.setText(mPreference.getString("CODE"));
        description.setText(mPreference.getString("TITLE"));
        returnable.setText(mPreference.getString("RETURNABLE"));
        moq.setText(mPreference.getString("MOQ"));
        agent.setText(mPreference.getString("AGENT"));
        retailer.setText(mPreference.getString("RETAILER"));
        consumer.setText(mPreference.getString("CONSUMER"));
       // image.setImageResource(Integer.parseInt(mPreference.getString("IMAGE")));
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

