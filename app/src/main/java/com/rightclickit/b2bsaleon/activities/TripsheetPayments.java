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

import com.rightclickit.b2bsaleon.R;

public class TripsheetPayments extends AppCompatActivity {
    LinearLayout ret;
    LinearLayout payments;
    LinearLayout save;
    LinearLayout print;
    LinearLayout delivery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripsheet_payments);

        this.getSupportActionBar().setTitle("PAYMENTS");
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


        ret = (LinearLayout) findViewById(R.id.rlinear);
        payments = (LinearLayout) findViewById(R.id.plinear);
        save = (LinearLayout) findViewById(R.id.slinear);
        print = (LinearLayout) findViewById(R.id.prelinear);
        delivery=(LinearLayout) findViewById(R.id.dlinear) ;








        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(TripsheetPayments.this,TripsheetReturns.class);
                startActivity(i);

            }
        });
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(TripsheetPayments.this,TripsheetDelivery.class);
                startActivity(i);

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialogWithCancelButton(TripsheetPayments.this,"User Action!","Do you want to save data?");
            }
        });
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(TripsheetPayments.this,TripsheetDeliveryPreview.class);
                startActivity(i);
            }
        });

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
                    Intent i =new Intent(TripsheetPayments.this,DashboardDelivery.class);
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
            Intent i =new Intent(TripsheetPayments.this,SalesListActivity.class);
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
