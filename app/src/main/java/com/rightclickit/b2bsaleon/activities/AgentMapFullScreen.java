package com.rightclickit.b2bsaleon.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rightclickit.b2bsaleon.R;

/**
 * Created by Sekhar Kuppa
 */

public class AgentMapFullScreen extends AppCompatActivity {

    double latitude, longitude;
    private GoogleMap googlemap = null;
    private String ONBACK = "";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            latitude = Double.parseDouble(b.getString("fromLat"));
            longitude = Double.parseDouble(b.getString("fromLong"));
            ONBACK = b.getString("FromPage");

        }

        setContentView(R.layout.map_dialog);


        this.getSupportActionBar().setTitle("MAP FULLVIEW");
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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragFullView);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googlemap = googleMap;

                replaceMapFragment();
            }
        });
    }

    private void replaceMapFragment() {


        // Enable Zoom
        googlemap.getUiSettings().setZoomGesturesEnabled(true);

        //set Map TYPE
        googlemap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //enable Current location Button
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googlemap.setMyLocationEnabled(true);

        //set "listener" for changing my location
        //googlemap.setOnMyLocationChangeListener(myLocationChangeListener());

        LatLng loc = new LatLng(latitude, longitude);

        Marker marker;
        googlemap.clear();
        marker = googlemap.addMarker(new MarkerOptions().position(loc));
        googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));

        googlemap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                LatLng loc = new LatLng(latLng.latitude, latLng.longitude);
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                Marker marker;
                googlemap.clear();
                marker = googlemap.addMarker(new MarkerOptions().position(loc));
                googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
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
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.Add).setVisible(false);
        menu.findItem(R.id.autorenew).setVisible(false);
        menu.findItem(R.id.sort).setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        if (ONBACK.equals("Retailersadd")) {
            Intent i = new Intent(AgentMapFullScreen.this, Retailers_AddActivity.class);
            i.putExtra("lat", String.valueOf(latitude));
            i.putExtra("long", String.valueOf(longitude));
            setResult(RESULT_OK, i);
            finish();
        } else if (ONBACK.equals("Agentsadd")) {
            Intent i = new Intent(AgentMapFullScreen.this, Agents_AddActivity.class);
            i.putExtra("lat", String.valueOf(latitude));
            i.putExtra("long", String.valueOf(longitude));
            setResult(RESULT_OK, i);
            finish();
        }   else if (ONBACK.equals("Agentsinfo")) {
            Intent i = new Intent(AgentMapFullScreen.this, AgentsInfoActivity.class);
            i.putExtra("lat", String.valueOf(latitude));
            i.putExtra("long", String.valueOf(longitude));
            setResult(RESULT_OK, i);
            finish();
        }

    }
}
