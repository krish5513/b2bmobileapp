package com.rightclickit.b2bsaleon.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rightclickit.b2bsaleon.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;

public class Sales_PreviewActivity extends AppCompatActivity {
    TextView Previewprint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales__preview);

        this.getSupportActionBar().setTitle("Select Retailer");
        this.getSupportActionBar().setSubtitle(null);
        //this.getSupportActionBar().setLogo(R.drawable.sales_white);
        // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);


        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator( R.drawable.ic_arrow_back_black_24dp);

        Previewprint=(TextView) findViewById( R.id.tv_preview_print );


        Previewprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Sales_PreviewActivity.this,Sales_Preview_PrintActivity.class);
                startActivity(i);
                finish();
            }
        });






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
            Intent i =new Intent(Sales_PreviewActivity.this,Sales_Retailer_AddActivity.class);
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
        menu.findItem( R.id.Add).setVisible(true);



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