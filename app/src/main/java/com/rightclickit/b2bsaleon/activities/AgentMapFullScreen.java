package com.rightclickit.b2bsaleon.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            latitude = Double.parseDouble(b.getString("fromLat"));
            longitude = Double.parseDouble(b.getString("fromLong"));
        }

        setContentView(R.layout.map_dialog);

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
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("lat", String.valueOf(latitude));
        i.putExtra("long", String.valueOf(longitude));
        setResult(RESULT_OK, i);
        finish();
    }
}
