package com.rightclickit.b2bsaleon.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;

public class SalesListActivity extends AppCompatActivity {
    Button view1;
    Button view2;
    Button view3;
    Button view4;
    Button view5;
    Button view6;
    Button view7;
    Button view8;
    Button view9;
    Button view10;
    TextView today;
    TextView monthly;
    TextView weekly;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_list);

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





        today=(TextView) findViewById(R.id.tv_today);
        monthly=(TextView) findViewById(R.id.tv_monthly);
        weekly=(TextView) findViewById(R.id.tv_weekly);
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


        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(SalesListActivity.this,TDCSales_TodayActivity.class);
                startActivity(i);
                finish();
            }
        });

        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(SalesListActivity.this,TDCSales_Month.class);
                startActivity(i);
                finish();
            }
        });

        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(SalesListActivity.this,TDCSales_Weekly.class);
                startActivity(i);
                finish();
            }
        });




        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(SalesListActivity.this,Saleslist_ViewActivity.class);
                startActivity(i);
                finish();
            }
        });
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(SalesListActivity.this,Saleslist_ViewActivity.class);
                startActivity(i);
                finish();
            }
        });
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(SalesListActivity.this,Saleslist_ViewActivity.class);
                startActivity(i);
                finish();
            }
        });
        view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(SalesListActivity.this,Saleslist_ViewActivity.class);
                startActivity(i);
                finish();
            }
        });
        view5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(SalesListActivity.this,Saleslist_ViewActivity.class);
                startActivity(i);
                finish();
            }
        });
        view6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(SalesListActivity.this,Saleslist_ViewActivity.class);
                startActivity(i);
                finish();
            }
        });
        view7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(SalesListActivity.this,Saleslist_ViewActivity.class);
                startActivity(i);
                finish();
            }
        });
        view8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(SalesListActivity.this,Saleslist_ViewActivity.class);
                startActivity(i);
                finish();
            }
        });
        view9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(SalesListActivity.this,Saleslist_ViewActivity.class);
                startActivity(i);
                finish();
            }
        });
        view10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(SalesListActivity.this,Saleslist_ViewActivity.class);
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
            Intent i =new Intent(SalesListActivity.this,SalesActivity.class);
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
        menu.findItem( R.id.action_search).setVisible(true);
        menu.findItem( R.id.Add).setVisible(false);

        menu.findItem( R.id.autorenew).setVisible(true);

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
