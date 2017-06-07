package com.rightclickit.b2bsaleon.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.AgentsAdapter;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

public class AgentsInfoActivity extends AppCompatActivity {
    EditText firstname,lastname,mobile,address;
    private MMSharedPreferences mPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agents_info);

        mPreference = new MMSharedPreferences(this);

        this.getSupportActionBar().setTitle("customerName");
        this.getSupportActionBar().setSubtitle(null);
        this.getSupportActionBar().setLogo(R.drawable.customers_white_24);
        this.getSupportActionBar().setTitle(mPreference.getString("FIRSTNAME"));
        // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);


        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        firstname=(EditText)findViewById(R.id.first_name);
        lastname=(EditText)findViewById(R.id.last_name) ;
        mobile=(EditText)findViewById(R.id.phoneNo);
        address=(EditText)findViewById(R.id.address);
      //  Bundle bundle = getIntent().getExtras();

        //Extract the data…
        /*String str_fName = bundle.getString("FIRSTNAME");
        String str_lName = bundle.getString("LASTNAME");
        String str_mobile = bundle.getString("MOBILE");
        String str_address = bundle.getString("ADDRESS");
*/

        firstname.setText(mPreference.getString("FIRSTNAME"));
        lastname.setText(mPreference.getString("LASTNAME"));
        mobile.setText(mPreference.getString("MOBILE"));
        address.setText(mPreference.getString("ADDRESS"));
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
        menu.findItem(R.id.action_search).setVisible(true);


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


