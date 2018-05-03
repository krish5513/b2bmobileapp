package com.rightclickit.b2bsaleon.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.AgentsModel;
import com.rightclickit.b2bsaleon.models.DashboardDeliveryModel;
import com.rightclickit.b2bsaleon.models.DashboardModel;
import com.rightclickit.b2bsaleon.models.DashboardPaymentsModel;
import com.rightclickit.b2bsaleon.models.ProductsModel;
import com.rightclickit.b2bsaleon.services.SyncNotificationsListService;
import com.rightclickit.b2bsaleon.services.SyncSpecialPriceService;
import com.rightclickit.b2bsaleon.services.SyncUserPrivilegesService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static com.rightclickit.b2bsaleon.customviews.CustomAlertDialog.showAlertDialog;

public class DashboardActivity extends AppCompatActivity implements OnMapReadyCallback {
    private DBHelper mDBHelper;
    private MMSharedPreferences mPreferences;
    private String userActSetupStatus = "";
    private LinearLayout mDashBoardLayout;
    private LinearLayout mTripsheetsLayout;
    private LinearLayout mCustomersLayout;
    private LinearLayout mProductsLayout;
    private LinearLayout mTDCLayout;
    private LinearLayout mRetailersLayout;
    TextView tv_listView;
    TextView tv_mapView;
    LinearLayout listview;
    LinearLayout mapview;
    public static GoogleMap mMap;
    private BroadcastReceiver mReceiver;
    private Marker marker;
    private AlertDialog.Builder alertDialogBuilder1;
    private PolylineOptions mPolylineOptions;

    private boolean isSyncClicked;
    AsyncHttpClient asyncHttpClient;
    Polyline polylineToAdd;
    private String mLatitude = "", mLongitude = "", mDeviceId = "", mProfilePic = "";
    SupportMapFragment mapFragment;
    Button taleorder;
    LinearLayout delivery, pendingIndentsL;
    private boolean isSaveDeviceDetails, isMyProfilePrivilege;
    TextView tvrouts_customerN, Cat1, Cat2, Cat3, Ltr1, Ltr2, Ltr3, pendingInText, approvedInText, lastSyncedText;

    TextView delivery_Cat1, delivery_Cat2, delivery_Cat3, lastSyncedText2;
    TextView delivery_order1, delivery_order2, delivery_order3;
    TextView delivery_deliv1, delivery_deliv2, delivery_deliv3;
    TextView delivery_percent1, delivery_percent2, delivery_percent3;

    TextView payment_deliveryValue, payment_receivedValiue, payment_dueValue, lastSyncedText3;

    public String selectedDate = "", mReportSelectedDateStr = "", mCurrentDate = "";
    private DashboardModel mReportsModel;
    private AlertDialog alertDialog1 = null;
    public static final String TAG = DashboardActivity.class.getSimpleName();
    TextView tv;
    Button moreInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        setContentView(R.layout.activity_dashboard);


        mReportsModel = new DashboardModel(DashboardActivity.this, this);
        mDBHelper = new DBHelper(DashboardActivity.this);
        mPreferences = new MMSharedPreferences(DashboardActivity.this);
//       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //      setSupportActionBar(toolbar);


        this.getSupportActionBar().setLogo(R.drawable.dashboard_icon_white_24);
        this.getSupportActionBar().setTitle("DASHBOARD");
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);


        this.getSupportActionBar().setDisplayShowCustomEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        View v=getLayoutInflater().inflate(R.layout.dashboard_actionbar,null);
        tv = (TextView) v.findViewById(R.id.actitle);
        tv.setText(Html.fromHtml(""));
        this.getSupportActionBar().setCustomView(v);


        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        /*if((month<=10)&&(day<10)) {
            //tv.setText("0" + day + "/0" + (month+1) + "/" + year);

            tv.setText(Html.fromHtml("<u> " + String.format("%02d", day)+"-"+String.format("%02d", (month + 1)) +"-"+year + "</u>"));
        }else if (month<=10){
            //tv.setText("" + day + "/0" + (month+1) + "/" + year);
            tv.setText(Html.fromHtml("<u>  " + day + "-0" + (month+1) + "-" + year+ "</u>"));

        }else if (day<10){
            //tv.setText("0" + day + "/" + (month+1) + "/" + year);
            tv.setText(Html.fromHtml("<u>  0" + day + "-" + (month+1) + "-" + year+ "</u>"));

        }else {
            //tv.setText("" + day + "/" + (month+1) + "/" + year);
            tv.setText(Html.fromHtml("<u>  " + day + "-" + (month+1) + "-" + year+ "</u>"));

        }
*/
        tv.setText(Html.fromHtml("<u> " + String.format("%02d", day)+"-"+String.format("%02d", (month + 1)) +"-"+year + "</u>"));


/*
        if ((month <= 10) && (day < 10)) {
            //tv.setText("0" + day + "/0" + (month+1) + "/" + year);
            selectedDate = ("        0" + year + "-0" + (month + 1) + "-" + day);
            mReportSelectedDateStr =  year + "-" +String.format("%02d", (month + 1))  + "-" + String.format("%02d", day);

        } else if (month <= 10) {
            //tv.setText("" + day + "/0" + (month+1) + "/" + year);

            selectedDate = ("        " + year + "-0" + (month + 1) + "-" + day);
            mReportSelectedDateStr = year + "-0" + (month + 1) + "-" + day;
        } else if (day < 10) {
            //tv.setText("0" + day + "/" + (month+1) + "/" + year);

            selectedDate = ("        0" + year + "-" + (month + 1) + "-" + day);
            mReportSelectedDateStr = "0" + year + "-0" + (month + 1) + "-" + day;
        } else {
            //tv.setText("" + day + "/" + (month+1) + "/" + year);

            selectedDate = ("        " + year + "-" + (month + 1) + "-" + day);
            mReportSelectedDateStr = year + (month + 1) + "-" + day;
        }*/

        selectedDate = year + "-" +String.format("%02d", (month + 1))  + "-" + String.format("%02d", day);
        mReportSelectedDateStr =  year + "-" +String.format("%02d", (month + 1))  + "-" + String.format("%02d", day);



        Log.i("Selecteddate", mReportSelectedDateStr);
        tv_listView = (TextView) findViewById(R.id.tv_listView);
        tv_listView.setVisibility(View.GONE);

        tv_mapView = (TextView) findViewById(R.id.tv_mapView);
        tv_mapView.setVisibility(View.GONE);

        listview = (LinearLayout) findViewById(R.id.ll_listview);
        //  listview.setVisibility(View.GONE);

        mapview = (LinearLayout) findViewById(R.id.ll_mapview);
        //  mapview.setVisibility(View.GONE);

        taleorder = (Button) findViewById(R.id.btn_sale_ord1);
        taleorder.setVisibility(View.GONE);

        delivery = (LinearLayout) findViewById(R.id.gotoCustomer);
        delivery.setVisibility(View.GONE);

        pendingIndentsL = (LinearLayout) findViewById(R.id.PendingIndentsLayout);

        Cat1 = (TextView) findViewById(R.id.Cat1);
        Cat2 = (TextView) findViewById(R.id.Cat2);
        Cat3 = (TextView) findViewById(R.id.Cat3);

        Ltr1 = (TextView) findViewById(R.id.Ltr1);
        Ltr2 = (TextView) findViewById(R.id.Ltr2);
        Ltr3 = (TextView) findViewById(R.id.Ltr3);

        pendingInText = (TextView) findViewById(R.id.PendingIndentText);
        approvedInText = (TextView) findViewById(R.id.ApprovedIndentText);

        lastSyncedText = (TextView) findViewById(R.id.LastSyncedText);


        delivery_Cat1 = (TextView) findViewById(R.id.delivery_cat1);
        delivery_Cat2 = (TextView) findViewById(R.id.delivery_cat2);
        delivery_Cat3 = (TextView) findViewById(R.id.delivery_cat3);

        delivery_order1 = (TextView) findViewById(R.id.delivery_order1);
        delivery_order2 = (TextView) findViewById(R.id.delivery_order2);
        delivery_order3 = (TextView) findViewById(R.id.delivery_order3);

        delivery_deliv1 = (TextView) findViewById(R.id.delivery_delivery1);
        delivery_deliv2 = (TextView) findViewById(R.id.delivery_delivery2);
        delivery_deliv3 = (TextView) findViewById(R.id.delivery_delivery3);

        delivery_percent1 = (TextView) findViewById(R.id.delivery_percentage1);
        delivery_percent2 = (TextView) findViewById(R.id.delivery_percentage2);
        delivery_percent3 = (TextView) findViewById(R.id.delivery_percentage3);

        lastSyncedText2 = (TextView) findViewById(R.id.LastSyncedText2);


        payment_deliveryValue = (TextView) findViewById(R.id.payments_deliveryValue);
        payment_receivedValiue = (TextView) findViewById(R.id.payments_receivedValue);
        payment_dueValue = (TextView) findViewById(R.id.payments_dueValue);

        lastSyncedText3 = (TextView) findViewById(R.id.LastSyncedText3);



        if (getIntent().getExtras() != null) {
            String updatedValue = getIntent().getExtras().getString("key", null);
            if (updatedValue == null) {

            } else {
                isSyncClicked = false;
                if (alertDialogBuilder1 != null) {
                    Log.i("if condition", "not null");
                    if (alertDialog1.isShowing()) {
                        alertDialog1.dismiss();
                    }
                    alertDialogBuilder1 = null;
                    // isSyncClicked = false;
                } else {
                    Log.i("if condition", "null");
                }
                if (mPreferences.getString("isloginClick").equals("false1")) {
                    showAlertDialog1(DashboardActivity.this, "Sync Process", "Dashboard sync completed succssfully.");
                }
            }
        }



        moreInfo = (Button) findViewById(R.id.moreinfo);

        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, NextIndent_Moreinfo.class);
                startActivity(i);
                finish();
            }
        });



        mDashBoardLayout = (LinearLayout) findViewById(R.id.DashboardLayout);
        mDashBoardLayout.setVisibility(View.GONE);
        mDashBoardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mDashBoardLayout.startAnimation(animation1);

            }
        });
        mTripsheetsLayout = (LinearLayout) findViewById(R.id.TripSheetsLayout);
        mTripsheetsLayout.setVisibility(View.GONE);
        mTripsheetsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mTripsheetsLayout.startAnimation(animation1);
                Intent i = new Intent(DashboardActivity.this, TripSheetsActivity.class);
                startActivity(i);
                finish();
            }
        });
        mCustomersLayout = (LinearLayout) findViewById(R.id.CustomersLayout);
        mCustomersLayout.setVisibility(View.GONE);
        mCustomersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mCustomersLayout.startAnimation(animation1);
                Intent i = new Intent(DashboardActivity.this, AgentsActivity.class);
                startActivity(i);
                finish();
            }
        });
        mRetailersLayout = (LinearLayout) findViewById(R.id.RetailersLayout);
        mRetailersLayout.setVisibility(View.GONE);
        mRetailersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mRetailersLayout.startAnimation(animation1);
                Intent i = new Intent(DashboardActivity.this, RetailersActivity.class);
                startActivity(i);
                finish();
            }
        });
        mProductsLayout = (LinearLayout) findViewById(R.id.ProductsLayout);
        mProductsLayout.setVisibility(View.GONE);
        mProductsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mProductsLayout.startAnimation(animation1);
                Intent i = new Intent(DashboardActivity.this, Products_Activity.class);
                startActivity(i);
                finish();
            }
        });
        mTDCLayout = (LinearLayout) findViewById(R.id.TDCLayout);
        mTDCLayout.setVisibility(View.GONE);
        mTDCLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mTDCLayout.startAnimation(animation1);
                Intent i = new Intent(DashboardActivity.this, TDCSalesActivity.class);
                startActivity(i);
                finish();
            }
        });

        tv_listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_listView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.delivery_orange, 0, 0, 0);
                tv_mapView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mapview_grey, 0, 0, 0);
//                line1.setBackgroundColor(Color.parseColor("#e99e3b"));
//                line2.setBackgroundColor(Color.parseColor("#dbd7d7"));
                //               linelist.setBackgroundColor(Color.parseColor("#e99e3b"));
                //               lllist.addView(v);
                listview.setVisibility(View.VISIBLE);
                mapview.setVisibility(View.GONE);
            }
        });
        tv_mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_listView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.delivery_grey, 0, 0, 0);
                tv_mapView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mapview_orange, 0, 0, 0);
//                line2.setBackgroundColor(0xe99e3b);
//                line1.setBackgroundColor(0xdbd7d7);
                listview.setVisibility(View.GONE);
                mapview.setVisibility(View.VISIBLE);

            }
        });
        taleorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, DashboardTakeorder.class);
                startActivity(i);
            }
        });
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, DashboardDelivery.class);
                startActivity(i);
            }
        });
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(DashboardActivity.this);

        // userActSetupStatus = mDBHelper.getUserActivityDetailsByUserId(mPreferences.getString("userId"));
        userActSetupStatus = mPreferences.getString("isSetup");
        System.out.println("F 11111 ***DASHBBBBB === " + userActSetupStatus);

        HashMap<String, String> userMapData = mDBHelper.getUsersData();
        ArrayList<String> privilegesData = mDBHelper.getUserActivityDetailsByUserId(userMapData.get("user_id"));
        System.out.println("F 11111 ***COUNT === " + privilegesData.size());
        for (int k = 0; k < privilegesData.size(); k++) {
            if (privilegesData.get(k).toString().equals("Dashboard")) {
                mDashBoardLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("TripSheets")) {
                mTripsheetsLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("Customers")) {
                mCustomersLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("Products")) {
                mProductsLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("TDC")) {
                mTDCLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("Retailers")) {
                mRetailersLayout.setVisibility(View.VISIBLE);
            }
        }
        mLatitude = userMapData.get("latitude");
        mLongitude = userMapData.get("longitude");

        ArrayList<String> privilegeActionsData = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("Dashboard"));
        System.out.println("F 11111 ***COUNT === " + privilegeActionsData.size());
        for (int z = 0; z < privilegeActionsData.size(); z++) {
            System.out.println("Name::: " + privilegeActionsData.get(z).toString());


            if (privilegeActionsData.get(z).toString().equals("map_view")) {
                tv_mapView.setVisibility(View.VISIBLE);
            }

              /*  if (privilegeActionsData.get(z).toString().equals("map_view_delivery")) {
              mapview.setVisibility(View.VISIBLE);
                 }*/

            else if (privilegeActionsData.get(z).toString().equals("list_view")) {
                tv_listView.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData.get(z).toString().equals("list_view_delivery")) {
                taleorder.setVisibility(View.VISIBLE);

            } else if (privilegeActionsData.get(z).toString().equals("list_view_take_order")) {
                delivery.setVisibility(View.VISIBLE);
            }


        }

        ArrayList<String> privilegeActionsData1 = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("Customers"));
        for (int z = 0; z < privilegeActionsData1.size(); z++) {
            if (privilegeActionsData1.get(z).toString().equals("my_profile")) {
                isMyProfilePrivilege = true;
                tvrouts_customerN = (TextView) findViewById(R.id.tvrouts_customerN);
                tvrouts_customerN.setText("Profile");

            }
        }

//        if (mDBHelper.getRouteId().length()==0) {
//            startService(new Intent(DashboardActivity.this, SyncRoutesMasterDetailsService.class));
//        }


        startService(new Intent(DashboardActivity.this, SyncNotificationsListService.class));


        // Code added by Sekhar Kuppa
        nextDayIndents();
        deliveriesDashboard();
        paymentsDashboard();

    }


    //datepicker
    @SuppressLint("ValidFragment")
    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            return dialog;
        }

        @SuppressLint("SetTextI18n")
        public void onDateSet(DatePicker view, int year, int month, int day) {

          /*  if ((month <= 10) && (day < 10)) {
                //tv.setText("0" + day + "/0" + (month + 1) + "/" + year);
                tv.setText(Html.fromHtml("<u>  0"  + day + "-0" + (month + 1) + "-" + year+ "</u>"));

            } else if (month <= 10) {
                //tv.setText("" + day + "/0" + (month + 1) + "/" + year);
                tv.setText(Html.fromHtml("<u>  " + day + "-0" + (month + 1) + "-" + year+ "</u>"));

            } else if (day < 10) {
                //tv.setText("0" + day + "/" + (month + 1) + "/" + year);
                tv.setText(Html.fromHtml("<u>  0"  + day + "-" + (month + 1) + "-" + year+ "</u>"));

            } else {
                //tv.setText("" + day + "/" + (month + 1) + "/" + year);
                tv.setText(Html.fromHtml("<u>  "  + day + "-" + (month + 1) + "-" + year+ "</u>"));

            }*/
            tv.setText(Html.fromHtml("<u> " + String.format("%02d", day)+"-"+String.format("%02d", (month + 1)) +"-"+year + "</u>"));

            /*if ((month <= 10) && (day < 10)) {
                //tv.setText("0" + day + "/0" + (month + 1) + "/" + year);

                selectedDate = ("        0" + year + "-0" + (month + 1) + "-" + day);
                mReportSelectedDateStr = "0" + year + "-0" + (month + 1) + "-" + day;
            } else if (month <= 10) {
                //tv.setText("" + day + "/0" + (month + 1) + "/" + year);

                selectedDate = ("        " + year + "-0" + (month + 1) + "-" + day);
                mReportSelectedDateStr = year + "-0" + (month + 1) + "-" + day;
            } else if (day < 10) {
                //tv.setText("0" + day + "/" + (month + 1) + "/" + year);

                selectedDate = ("        0" + year + "-" + (month + 1) + "-" + day);
                mReportSelectedDateStr = "0" + year + "-0" + (month + 1) + "-" + day;
            } else {
                //tv.setText("" + day + "/" + (month + 1) + "/" + year);

                selectedDate = ("        " + year + "-" + (month + 1) + "-" + day);
                mReportSelectedDateStr = year + (month + 1) + "-" + day;
            }*/


            selectedDate = year + "-" +String.format("%02d", (month + 1))  + "-" + String.format("%02d", day);
            mReportSelectedDateStr =  year + "-" +String.format("%02d", (month + 1))  + "-" + String.format("%02d", day);

            Log.i("Selecteddate", mReportSelectedDateStr);
            mPreferences.putString("selectedDate", selectedDate);

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        MenuItem settings = menu.findItem(R.id.settings);
        if (userActSetupStatus.equals("visible")) {
            settings.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        if (id == R.id.date) {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getFragmentManager(), "datePicker");
            return true;
        }
        if (id == R.id.notifications) {
            loadNotifications();
            Toast.makeText(this, "Clicked on Notifications...", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.settings) {

            loadSettings();
            Toast.makeText(this, "Clicked on Settings...", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.autorenew) {

            if (new NetworkConnectionDetector(DashboardActivity.this).isNetworkConnected()) {
                showAlertDialog(DashboardActivity.this, "Sync process", "Are you sure, you want start the sync process?");
            } else {
                new NetworkConnectionDetector(DashboardActivity.this).displayNoNetworkError(DashboardActivity.this);
            }
            return true;
           /* if (new NetworkConnectionDetector(DashboardActivity.this).isNetworkConnected()) {
                mReportsModel.getReportsData(mReportSelectedDateStr);
            } else {
                new NetworkConnectionDetector(DashboardActivity.this).displayNoNetworkError(DashboardActivity.this);
            }*/
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadNotifications() {
        Intent navigationIntent = new Intent(DashboardActivity.this, NotificationsActivity.class);
        // mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // if you keep these flags white screen is coming on Intent navigation
        startActivity(navigationIntent);
        finish();
    }

    private void loadSettings() {
        Intent settingsIntent = new Intent(DashboardActivity.this, SettingsActivity.class);
        // mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // if you keep these flags white screen is coming on Intent navigation
        startActivity(settingsIntent);
        finish();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.date).setVisible(true);
        menu.findItem(R.id.notifications).setVisible(true);
        menu.findItem(R.id.settings).setVisible(true);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);

        menu.findItem(R.id.autorenew).setVisible(true);
        menu.findItem(R.id.sort).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }


    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(homeIntent);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney;
        if (!mLatitude.equals("") && !mLongitude.equals("")) {
            sydney = new LatLng(Double.parseDouble(mLatitude), Double.parseDouble(mLongitude));
        } else {
            // Pass current location lat and long
            sydney = new LatLng(17.3850440, 78.4866710);
        }
        mMap.addMarker(new MarkerOptions().position(sydney).title("Hyderabad, Telangana"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mMap.setMyLocationEnabled(true);


    }


    private void showAlertDialog(Context context, String title, String message) {
        try {
            android.support.v7.app.AlertDialog alertDialog = null;
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    isSyncClicked = true;
                    showCustomValidationAlertForSync(DashboardActivity.this, "next");
                }
            });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    isSyncClicked = false;
                    dialogInterface.dismiss();
                }
            });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCustomValidationAlertForSync(Activity context, String key) {
        // custom dialog
        try {
            Log.i("called ", "called");

            alertDialogBuilder1 = new AlertDialog.Builder(DashboardActivity.this);
            alertDialogBuilder1.setTitle("Sync Process");
            alertDialogBuilder1.setCancelable(false);
            if (key.equals("next")) {
                Log.i("showAlertForSync", "next");
                alertDialogBuilder1.setMessage("Downloading Next day Indents...Please wait");
                DashboardModel dashboardModel = new DashboardModel(DashboardActivity.this, this);
                dashboardModel.getReportsData(mReportSelectedDateStr);
            } else if (key.equals("deliveries")) {
                Log.i("showAlertForSync", "deliveries");
                alertDialogBuilder1.setMessage("Downloading Today Deliveries...Please wait");
                DashboardDeliveryModel dashboarddeliveryModel = new DashboardDeliveryModel(DashboardActivity.this, this);
                dashboarddeliveryModel.getDeliveriesList(mReportSelectedDateStr);
            } else if (key.equals("payments")) {
                Log.i("showAlertForSync", "payments");
                alertDialogBuilder1.setMessage("Downloading Today Payments...Please wait");
                DashboardPaymentsModel dashboardPaymentsModel = new DashboardPaymentsModel(DashboardActivity.this, this);
                dashboardPaymentsModel.getPaymentsList(mReportSelectedDateStr);
            }
            alertDialog1 = alertDialogBuilder1.create();
            alertDialog1.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAlertDialog1(Context context, String title, String message) {
        try {
            android.support.v7.app.AlertDialog alertDialog = null;
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to display the next day indents data
     */
    public void nextDayIndents() {
        ArrayList<String> indentDetails = mDBHelper.fetchDashboardPendingIndentDetails(mReportSelectedDateStr);
        try {
            if (indentDetails.size() > 0) {
                JSONArray catArray = new JSONArray(indentDetails.get(0).toString());
                JSONArray ltrArray = new JSONArray(indentDetails.get(1).toString());
                Cat1.setText(catArray.get(0).toString());
                Cat2.setText(catArray.get(1).toString());
                Cat3.setText(catArray.get(2).toString());

                Ltr1.setText(ltrArray.get(0).toString());
                Ltr2.setText(ltrArray.get(1).toString());
                Ltr3.setText(ltrArray.get(2).toString());

                if (indentDetails.get(2).toString().equals("null")) {
                    pendingInText.setText("Indent: 0");
                } else {
                    pendingInText.setText("Indent: " + indentDetails.get(2).toString());
                }

                if (indentDetails.get(3).toString().equals("null")) {
                    approvedInText.setText("Approved: 0");
                } else {
                    approvedInText.setText("Approved: " + indentDetails.get(3).toString());
                }
                // Display time and date from the indentDetails object keys as 4 and 5
                lastSyncedText.setText("Last synced at: " + "\n"
                        + indentDetails.get(4).toString() + " " + indentDetails.get(5).toString());
            } else {
                // Display empty message that to refresh the sync button
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void deliveriesDashboard() {

        ArrayList<String> deliveriesDetails = mDBHelper.fetchDashboardDeliveriesDetails(mReportSelectedDateStr);
        try {
            if (deliveriesDetails.size() > 0) {
                JSONArray catArray = new JSONArray(deliveriesDetails.get(0).toString());
                JSONArray orderedArray = new JSONArray(deliveriesDetails.get(1).toString());
                JSONArray deliveredArray = new JSONArray(deliveriesDetails.get(2).toString());
                JSONArray percentageArray = new JSONArray(deliveriesDetails.get(3).toString());
                delivery_Cat1.setText(catArray.get(0).toString());
                delivery_Cat2.setText(catArray.get(1).toString());
                delivery_Cat3.setText(catArray.get(2).toString());

                delivery_order1.setText(orderedArray.get(0).toString());
                delivery_order2.setText(orderedArray.get(1).toString());
                delivery_order3.setText(orderedArray.get(2).toString());

                delivery_deliv1.setText(deliveredArray.get(0).toString());
                delivery_deliv2.setText(deliveredArray.get(1).toString());
                delivery_deliv3.setText(deliveredArray.get(2).toString());

                delivery_percent1.setText(percentageArray.get(0).toString());
                delivery_percent2.setText(percentageArray.get(1).toString());
                delivery_percent3.setText(percentageArray.get(2).toString());


                // Display time and date from the indentDetails object keys as 4 and 5
                lastSyncedText2.setText("Last synced at: " + "\n"
                        + deliveriesDetails.get(4).toString() + " " + deliveriesDetails.get(5).toString());
            } else {
                // Display empty message that to refresh the sync button
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void paymentsDashboard() {

        ArrayList<String> paymentDetails = mDBHelper.fetchDashboardPaymentsDetails(mReportSelectedDateStr);
        try {
            if (paymentDetails.size() > 0) {


                if (paymentDetails.get(0).toString().equals("null")) {
                    payment_deliveryValue.setText("0");
                } else {
                    payment_deliveryValue.setText(paymentDetails.get(0).toString());
                }

                if (paymentDetails.get(1).toString().equals("null")) {
                    payment_receivedValiue.setText("0");
                } else {
                    payment_receivedValiue.setText(paymentDetails.get(1).toString());
                }

                if (paymentDetails.get(2).toString().equals("null")) {
                    payment_dueValue.setText("0");
                } else {
                    payment_dueValue.setText(paymentDetails.get(2).toString());
                }
                // Display time and date from the indentDetails object keys as 4 and 5
                lastSyncedText3.setText("Last synced at: " + "\n"
                        + paymentDetails.get(3).toString() + " " + paymentDetails.get(4).toString());
            } else {
                // Display empty message that to refresh the sync button
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //extract our message from intent
                String receiver_key = intent.getStringExtra("receiver_key");
                //log our message value
                Log.i(TAG, receiver_key);

                if (alertDialogBuilder1 != null) {
                    Log.i("returnFromService", "not null");
                    if (alertDialog1.isShowing()) {
                        alertDialog1.dismiss();
                    }
                    alertDialogBuilder1 = null;
                } else {
                    Log.i("returnFromService", "null");
                }
                if (receiver_key.equals("completed")) {
                    showAlertDialog1(DashboardActivity.this, "Sync Process", "Dashboard sync completed succssfully.");
                } else {
                   /* if(receiver_key.equals("specialPrice")){
                        boolean bool = intent.getBooleanExtra("whichActivity", false);
                        if(!bool){
                            Intent mainActivityIntent = new Intent(getApplicationContext(), DashboardActivity.class);
                            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(mainActivityIntent);
                        }
                    }*/
                    showCustomValidationAlertForSync(DashboardActivity.this, receiver_key);
                }
            }
        };
        this.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //unregister our receiver
        this.unregisterReceiver(this.mReceiver);
    }
}
