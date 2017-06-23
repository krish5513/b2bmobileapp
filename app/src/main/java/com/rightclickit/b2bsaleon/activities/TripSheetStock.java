package com.rightclickit.b2bsaleon.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.ArrayList;

public class TripSheetStock extends AppCompatActivity {
    TextView preview;
    public static TextView Dispatch;
    public static TextView Verify;
    public static TextView tv_save;
    private DBHelper mDBHelper;
    private MMSharedPreferences mPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_sheet_stock);

        this.getSupportActionBar().setTitle("TRIP #908915,27/02/17");
        this.getSupportActionBar().setSubtitle(null);
        this.getSupportActionBar().setLogo(R.drawable.route_white);
        // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);



        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        mDBHelper = new DBHelper(TripSheetStock.this);
        mPreferences = new MMSharedPreferences(TripSheetStock.this);


        Dispatch=(TextView)findViewById(R.id.dispatch);
      Dispatch.setVisibility(View.GONE);

        Verify=(TextView)findViewById(R.id.verify);
          Verify.setVisibility(View.GONE);

        tv_save=(TextView)findViewById(R.id.tv_save);
       tv_save.setVisibility(View.GONE);

        preview = (TextView) findViewById(R.id.tv_print_print);
        preview.setVisibility(View.GONE);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                preview.startAnimation(animation1);
                Intent i = new Intent(TripSheetStock.this, TripsheetStockPreview.class);
                startActivity(i);
                finish();
            }
        });


        ArrayList<String> privilegeActionsData = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("TripSheets"));
        System.out.println("F 11111 ***COUNT === " + privilegeActionsData.size());
        for (int z = 0; z < privilegeActionsData.size(); z++) {
            System.out.println("Name::: " + privilegeActionsData.get(z).toString());


            if (privilegeActionsData.get(z).toString().equals("Stock_Dispatch")) {
                Dispatch.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData.get(z).toString().equals("Stock_Verify")) {
                Verify.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData.get(z).toString().equals("Stock_Save")) {
                tv_save.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData.get(z).toString().equals("stock_Preview_Print")) {
                preview.setVisibility(View.VISIBLE);
            }
        }

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
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
    
}
