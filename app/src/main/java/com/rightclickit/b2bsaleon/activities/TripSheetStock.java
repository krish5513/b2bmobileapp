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
import android.widget.ImageButton;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TripSheetStock extends AppCompatActivity {
    TextView preview;
    public static TextView Dispatch;
    public static TextView Verify;
    public static TextView tv_save;
    public static TextView tv_verify;
    private DBHelper mDBHelper;
    private MMSharedPreferences mPreferences;

    public static ImageButton dispatchdec, dispatchinc;
    public static ImageButton verifydec, verifyinc;

    private TextView mDispatchText, mVerifyText;


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

        mDispatchText = (TextView) findViewById(R.id.dispatch);
        mDispatchText.setVisibility(View.GONE);
        mVerifyText = (TextView) findViewById(R.id.verify);
        mVerifyText.setVisibility(View.GONE);

//        dispatchdec=(ImageButton)findViewById(R.id.productQtDec1);
//        dispatchdec.setVisibility(View.GONE);
//
//        dispatchinc=(ImageButton)findViewById(R.id.productQtInc1);
//        dispatchinc.setVisibility(View.GONE);
//
//        verifydec=(ImageButton)findViewById(R.id.productQtDec2);
//        verifydec.setVisibility(View.GONE);
//
//        verifyinc=(ImageButton)findViewById(R.id.productQtInc2);
//        verifyinc.setVisibility(View.GONE);


        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_save.setVisibility(View.GONE);

        tv_verify = (TextView) findViewById(R.id.tv_verify);
        tv_verify.setVisibility(View.GONE);

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
                // dispatchdec.setVisibility(View.VISIBLE);
                // dispatchinc.setVisibility(View.VISIBLE);
                mDispatchText.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData.get(z).toString().equals("Stock_Verify")) {
                //verifydec.setVisibility(View.VISIBLE);
                //verifyinc.setVisibility(View.VISIBLE);
                mVerifyText.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData.get(z).toString().equals("Stock_Save")) {
                tv_save.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData.get(z).toString().equals("stock_Preview_Print")) {
                preview.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData.get(z).toString().equals("Stock_Verify")) {
                tv_verify.setVisibility(View.VISIBLE);
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
        menu.findItem(R.id.Add).setVisible(false);
        menu.findItem(R.id.autorenew).setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, TripSheetsActivity.class);
        startActivity(intent);
        finish();
    }

}
