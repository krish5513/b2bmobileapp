package com.rightclickit.b2bsaleon.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TripSheetReturnsAdapter;
import com.rightclickit.b2bsaleon.beanclass.TripSheetReturnsBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.ArrayList;

public class TripsheetReturns extends AppCompatActivity {
    private Context activityContext;
    private MMSharedPreferences mmSharedPreferences;
    private DBHelper mDBHelper;

    private SearchView search;
    private ListView tripSheetReturnProductsList;
    private TripSheetReturnsAdapter mTripSheetReturnsAdapter;
    ArrayList customArraylist = new ArrayList();
    private String loggedInUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripsheet_returns);

        try {
            activityContext = TripsheetReturns.this;
            mmSharedPreferences = new MMSharedPreferences(activityContext);
            mDBHelper = new DBHelper(activityContext);

            loggedInUserId = mmSharedPreferences.getString("userId");

            this.getSupportActionBar().setTitle("RETURNS");
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

            tripSheetReturnProductsList = (ListView) findViewById(R.id.trip_sheet_return_products_list_view);

            for (int i = 0; i < 2; i++) {
                TripSheetReturnsBean dBean = new TripSheetReturnsBean();
                dBean.setmTripsheetReturnsName("FCM 500ML");
                dBean.setmTripshhetReturnsQuantity("000.00");
                dBean.setmTripshhetReturnsType("  ");
                dBean.setmTripsheetReturnsIncAmount("000.00");

                customArraylist.add(dBean);
            }

            mTripSheetReturnsAdapter = new TripSheetReturnsAdapter(TripsheetReturns.this, TripsheetReturns.this, customArraylist);
            tripSheetReturnProductsList.setAdapter(mTripSheetReturnsAdapter);

        } catch (Exception e) {
            e.printStackTrace();
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
        if (id == R.id.Add) {
            Intent i = new Intent(TripsheetReturns.this, TDCSalesListActivity.class);
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
        Intent intent = new Intent(this, TripSheetView.class);
        startActivity(intent);
        finish();
    }

    public void saveTripSheetReturns(View v) {
        showAlertDialogWithCancelButton(TripsheetReturns.this, "User Action!", "Do you want to save data?");
    }

    public void showTripSheetReturnsPreview(View v) {
        Intent i = new Intent(TripsheetReturns.this, TripsheetDeliveryPreview.class);
        startActivity(i);
        finish();
    }

    public void openTripSheetDeliveries(View v) {
        Intent i = new Intent(TripsheetReturns.this, TripsheetDelivery.class);
        startActivity(i);
        finish();
    }

    public void openTripSheetPayments(View v) {
        Intent i = new Intent(TripsheetReturns.this, TripsheetPayments.class);
        startActivity(i);
        finish();
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
                    Intent i = new Intent(TripsheetReturns.this, DashboardDelivery.class);
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


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
