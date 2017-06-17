package com.rightclickit.b2bsaleon.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;

public class TripSheetView extends AppCompatActivity implements OnMapReadyCallback {
    FloatingActionButton fab;
    LinearLayout tsDashBoardLayout;
    LinearLayout tsTripsheetsLayout;
    LinearLayout tsCustomersLayout;
    LinearLayout tsProductsLayout;
    LinearLayout tsTDCLayout;
    private DBHelper mDBHelper;
    private MMSharedPreferences mPreferences;
    private GoogleMap mMap;
    private String mLatitude = "", mLongitude = "", mDeviceId = "", mProfilePic = "";
    TextView listView;
    TextView mapView;
    LinearLayout listview;
    LinearLayout mapview;
    Button taleorder;
    LinearLayout delivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_sheet_view);


        this.getSupportActionBar().setTitle("Tripsheet Preview");
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

        mDBHelper = new DBHelper(TripSheetView.this);
        mPreferences = new MMSharedPreferences(TripSheetView.this);

        tsDashBoardLayout = (LinearLayout) findViewById(R.id.dashboard);
        tsTripsheetsLayout = (LinearLayout) findViewById(R.id.tripsheet);
        tsCustomersLayout = (LinearLayout) findViewById(R.id.agent);
        tsProductsLayout = (LinearLayout) findViewById(R.id.product);
        tsTDCLayout = (LinearLayout) findViewById(R.id.tdcsales);
        taleorder=(Button)findViewById(R.id.btn_sale_ord1) ;
        delivery=(LinearLayout) findViewById(R.id.gotoCustomer);


        listView = (TextView) findViewById(R.id.tv_listView);
        mapView = (TextView) findViewById(R.id.tv_mapView);
        listview = (LinearLayout) findViewById(R.id.ll_listview);
        mapview = (LinearLayout) findViewById(R.id.ll_mapview);

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.delivery_orange, 0, 0, 0);
                mapView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mapview_grey, 0, 0, 0);
//                line1.setBackgroundColor(Color.parseColor("#e99e3b"));
//                line2.setBackgroundColor(Color.parseColor("#dbd7d7"));
                //               linelist.setBackgroundColor(Color.parseColor("#e99e3b"));
                //               lllist.addView(v);
                listview.setVisibility(View.VISIBLE);
                mapview.setVisibility(View.GONE);

            }
        });
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.delivery_grey, 0, 0, 0);
                mapView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mapview_orange, 0, 0, 0);
//                line2.setBackgroundColor(0xe99e3b);
//                line1.setBackgroundColor(0xdbd7d7);
                listview.setVisibility(View.GONE);
                mapview.setVisibility(View.VISIBLE);


            }
        });

        tsDashBoardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(TripsheetsActivity.this, "Clicked on Dashboard", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                tsDashBoardLayout.startAnimation(animation1);
                Intent i =new Intent(TripSheetView.this,DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });

        tsTripsheetsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(TripSheetsActivity.this, "Clicked on Tripsheets", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                tsTripsheetsLayout.startAnimation(animation1);

            }
        });
        tsCustomersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Tripsheet_Activity.this, "Clicked on Customers", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                tsCustomersLayout.startAnimation(animation1);
                Intent i =new Intent(TripSheetView.this,AgentsActivity.class);
                startActivity(i);
                finish();
            }
        });
        tsProductsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(Tripsheet_Activity.this, "Clicked on Products", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                tsProductsLayout.startAnimation(animation1);
                Intent i =new Intent(TripSheetView.this,Products_Activity.class);
                startActivity(i);
                finish();
            }
        });
        tsTDCLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(Tripsheet_Activity.this, "Clicked on TDC", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                tsTDCLayout.startAnimation(animation1);
                Intent i =new Intent(TripSheetView.this,SalesActivity.class);
                startActivity(i);
                finish();
            }
        });

        taleorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(TripSheetView.this,DashboardTakeorder.class);
                startActivity(i);
                finish();
            }
        });
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(TripSheetView.this,DashboardDelivery.class);
                startActivity(i);
                finish();
            }
        });

       SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        HashMap<String,String> userMapData = mDBHelper.getUsersData();
        ArrayList<String> privilegesData = mDBHelper.getUserActivityDetailsByUserId(userMapData.get("user_id"));
        System.out.println("F 11111 ***COUNT === "+ privilegesData.size());
        for (int k = 0; k<privilegesData.size();k++){
            if (privilegesData.get(k).toString().equals("Dashboard")){
                tsDashBoardLayout.setVisibility(View.VISIBLE);
            }else if (privilegesData.get(k).toString().equals("TripSheets")){
                tsTripsheetsLayout.setVisibility(View.VISIBLE);
            }else if (privilegesData.get(k).toString().equals("Customers")){
                tsCustomersLayout.setVisibility(View.VISIBLE);
            }else if (privilegesData.get(k).toString().equals("Products")){
                tsProductsLayout.setVisibility(View.VISIBLE);
            }else if (privilegesData.get(k).toString().equals("TDC")){
                tsTDCLayout.setVisibility(View.VISIBLE);
            }
        }

        mLatitude = userMapData.get("latitude");
        mLongitude = userMapData.get("longitude");

        ArrayList<String> privilegeActionsData = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("Dashboard"));
        System.out.println("F 11111 ***COUNT === "+ privilegeActionsData.size());
        for (int z = 0;z<privilegeActionsData.size();z++){
            System.out.println("Name::: "+ privilegeActionsData.get(z).toString());
        }

        fab = (FloatingActionButton) findViewById(R.id.productfab);
        fab.setVisibility(View.VISIBLE);
        fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_eye_white_24dp));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Clicked Tripsheet Preview",Toast.LENGTH_SHORT).show();
                Intent i =new Intent(TripSheetView.this,TripSheetViewPreview.class);
                startActivity(i);
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
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
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
