package com.rightclickit.b2bsaleon.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.AgentTakeOrder_ViewAdapter;
import com.rightclickit.b2bsaleon.adapters.TripSheetDeliveriesAdapter;
import com.rightclickit.b2bsaleon.beanclass.TripSheetDeliveriesBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.ArrayList;

public class TripsheetDelivery extends AppCompatActivity {
    LinearLayout ret;
    LinearLayout payments;
    LinearLayout save;
    LinearLayout print;

    private DBHelper mDBHelper;
    private MMSharedPreferences mPreferences;

    private ListView mAgentsList;
    private TripSheetDeliveriesAdapter mTripSheetDeliveriesAdapter;
    ArrayList deliveriesArraylist=new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripsheet_delivery);

        this.getSupportActionBar().setTitle("DELIVERIES");
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


        ret = (LinearLayout) findViewById(R.id.linearreturn);
        ret.setVisibility(View.GONE);
        payments = (LinearLayout) findViewById(R.id.linearpayments);
        payments.setVisibility(View.GONE);

        save = (LinearLayout) findViewById(R.id.linearsave);
        print = (LinearLayout) findViewById(R.id.linearpreview);


        mDBHelper = new DBHelper(TripsheetDelivery.this);
        mPreferences = new MMSharedPreferences(TripsheetDelivery.this);


        mAgentsList = (ListView) findViewById(R.id.AgentsList);

        for (int i=0;i<10;i++){
            TripSheetDeliveriesBean dBean=new TripSheetDeliveriesBean();
            dBean.setmTripsheetDeleveryName("FCM 500ML");
            dBean.setmTripsheetDelivery_Status("In Stock");
            dBean.setmTripsheetDeleveryInstockAmount("00.000");
            dBean.setmTripsheetDelivery_UnitPrice("50.00");
            dBean.setmTripsheetDelivery_TaxPercent("00.00");
            dBean.setmTripsheetDelivery_Amount("00.00");
            dBean.setmTripsheetDelivery_Quantity("00.000");
            deliveriesArraylist.add(dBean);
        }
        mAgentsList.setAdapter(new TripSheetDeliveriesAdapter(TripsheetDelivery.this,TripsheetDelivery.this,deliveriesArraylist));

        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(TripsheetDelivery.this,TripsheetReturns.class);
                startActivity(i);
                finish();


            }
        });
        payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(TripsheetDelivery.this,TripsheetPayments.class);
                startActivity(i);
                finish();


            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialogWithCancelButton(TripsheetDelivery.this,"User Action!","Do you want to save data?");
            }
        });
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(TripsheetDelivery.this,TripsheetDeliveryPreview.class);
                startActivity(i);
                finish();

            }
        });


        ArrayList<String> privilegeActionsData = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("TripSheets"));
        System.out.println("F 11111 ***COUNT === "+ privilegeActionsData.size());
        for (int z = 0;z<privilegeActionsData.size();z++) {
            System.out.println("Name::: " + privilegeActionsData.get(z).toString());

            if (privilegeActionsData.get(z).toString().equals("list_view_return")) {
                ret.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData.get(z).toString().equals("list_view_payment")) {
                payments.setVisibility(View.VISIBLE);
            }


        }





    }
    private void showAlertDialogWithCancelButton(Context context, String title, String message) {
        try {
            AlertDialog alertDialog = null;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false);



            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent i =new Intent(TripsheetDelivery.this,TripsheetDelivery.class);
                    startActivity(i);
                    finish();
                }
            });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();


            Button cancelButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (cancelButton != null)
                cancelButton.setTextColor(ContextCompat.getColor(context, R.color.alert_dialog_color_accent));


        }


        catch (Exception e) {
            e.printStackTrace();
        }
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
            Intent i =new Intent(TripsheetDelivery.this,TDCSalesListActivity.class);
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
        Intent intent = new Intent(this, TripSheetView.class);
        startActivity(intent);
        finish();
    }

}
