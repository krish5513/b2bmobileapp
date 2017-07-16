package com.rightclickit.b2bsaleon.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;

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
    private Marker marker;
    private PolylineOptions mPolylineOptions;
    AsyncHttpClient asyncHttpClient;
    Polyline polylineToAdd;
    private String mLatitude = "", mLongitude = "", mDeviceId = "", mProfilePic = "";
    SupportMapFragment mapFragment;
    Button taleorder;
    LinearLayout delivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        setContentView(R.layout.activity_dashboard);



        mDBHelper = new DBHelper(DashboardActivity.this);
        mPreferences = new MMSharedPreferences(DashboardActivity.this);
//       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //      setSupportActionBar(toolbar);


        this.getSupportActionBar().setLogo(R.drawable.dashboard_icon_white_24);
        this.getSupportActionBar().setTitle("DASHBOARD");
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);


        tv_listView=(TextView) findViewById(R.id.tv_listView);
        tv_listView.setVisibility(View.GONE);

        tv_mapView=(TextView) findViewById(R.id.tv_mapView);
        tv_mapView.setVisibility(View.GONE);

        listview=(LinearLayout) findViewById(R.id.ll_listview);
      //  listview.setVisibility(View.GONE);

        mapview=(LinearLayout) findViewById(R.id.ll_mapview);
      //  mapview.setVisibility(View.GONE);

        taleorder=(Button)findViewById(R.id.btn_sale_ord1) ;
        taleorder.setVisibility(View.GONE);

        delivery=(LinearLayout) findViewById(R.id.gotoCustomer);
        delivery.setVisibility(View.GONE);

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
                tv_listView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.delivery_orange,0,0,0);
                tv_mapView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mapview_grey,0,0,0);
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
                tv_listView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.delivery_grey,0,0,0);
                tv_mapView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mapview_orange,0,0,0);
//                line2.setBackgroundColor(0xe99e3b);
//                line1.setBackgroundColor(0xdbd7d7);
                listview.setVisibility(View.GONE);
                mapview.setVisibility(View.VISIBLE);

            }
        });
        taleorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DashboardActivity.this,DashboardTakeorder.class);
                startActivity(i);
            }
        });
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DashboardActivity.this,DashboardDelivery.class);
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
            }else if (privilegesData.get(k).toString().equals("Retailers")){
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
                }

             else if (privilegeActionsData.get(z).toString().equals("list_view_delivery")) {
                taleorder.setVisibility(View.VISIBLE);

              }


             else  if (privilegeActionsData.get(z).toString().equals("list_view_take_order")) {
                delivery.setVisibility(View.VISIBLE);
              }


         }

//        if (mDBHelper.getRouteId().length()==0) {
//            startService(new Intent(DashboardActivity.this, SyncRoutesMasterDetailsService.class));
//        }

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


        menu.findItem(R.id.notifications).setVisible(true);
        menu.findItem(R.id.settings).setVisible(true);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);

        menu.findItem( R.id.autorenew).setVisible(true);
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
}
