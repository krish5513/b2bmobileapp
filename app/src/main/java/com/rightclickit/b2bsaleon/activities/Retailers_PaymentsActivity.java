package com.rightclickit.b2bsaleon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.rightclickit.b2bsaleon.R;

public class Retailers_PaymentsActivity extends AppCompatActivity {


    Button view1,view2,view3,view4,view5,view6,view7,view8,view9,view10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_retailers__payments );

        this.getSupportActionBar().setTitle("RETAILER NAME");
        this.getSupportActionBar().setSubtitle(null);
        this.getSupportActionBar().setLogo(R.drawable.ic_store_white);
        // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);


        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);



        view1 = (Button) findViewById(R.id.btn_view1);
        view2 = (Button) findViewById(R.id.btn_view2);
        view3 = (Button) findViewById(R.id.btn_view3);
        view4 = (Button) findViewById(R.id.btn_view4);
        view5 = (Button) findViewById(R.id.btn_view5);
        view6 = (Button) findViewById(R.id.btn_view6);
        view7 = (Button) findViewById(R.id.btn_view7);
        view8 = (Button) findViewById(R.id.btn_view8);
        view9 = (Button) findViewById(R.id.btn_view9);
        view10 = (Button) findViewById(R.id.btn_view10);

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Retailers_PaymentsActivity.this, Retailers_Payments_ViewActivity.class);
                startActivity(intent);
                finish();
            }
        });
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Retailers_PaymentsActivity.this, Retailers_Payments_ViewActivity.class);
                startActivity(intent);
                finish();
            }
        });
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Retailers_PaymentsActivity.this, Retailers_Payments_ViewActivity.class);
                startActivity(intent);
                finish();
            }
        });
        view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Retailers_PaymentsActivity.this, Retailers_Payments_ViewActivity.class);
                startActivity(intent);
                finish();
            }
        });
        view5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Retailers_PaymentsActivity.this, Retailers_Payments_ViewActivity.class);
                startActivity(intent);
                finish();
            }
        });
        view6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Retailers_PaymentsActivity.this, Retailers_Payments_ViewActivity.class);
                startActivity(intent);
                finish();
            }
        });
        view7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Retailers_PaymentsActivity.this, Retailers_Payments_ViewActivity.class);
                startActivity(intent);
                finish();
            }
        });
        view8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Retailers_PaymentsActivity.this, Retailers_Payments_ViewActivity.class);
                startActivity(intent);
                finish();
            }
        });
        view9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Retailers_PaymentsActivity.this, Retailers_Payments_ViewActivity.class);
                startActivity(intent);
                finish();
            }
        });
        view10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Retailers_PaymentsActivity.this, Retailers_Payments_ViewActivity.class);
                startActivity(intent);
                finish();
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
        menu.findItem( R.id.Add).setVisible(false);


        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, RetailersActivity.class);
        startActivity(intent);
        finish();
    }
}
