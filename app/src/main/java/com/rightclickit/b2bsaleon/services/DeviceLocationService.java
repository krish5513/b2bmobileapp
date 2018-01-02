package com.rightclickit.b2bsaleon.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.rightclickit.b2bsaleon.activities.SettingsActivity;
import com.rightclickit.b2bsaleon.beanclass.DeviceDetails;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sekhar Kuppa on 10/30/2017.
 */

public class DeviceLocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    protected LocationManager locationManager;
    Location location;
    Context ctx;
    private MMSharedPreferences mSessionManagement;

    GoogleApiClient mGoogleApiClient;
    Location mLocation;
    private DBHelper mDBHelper;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 1 * 60 * 1000;  /* 5 Mins */
    private long FASTEST_INTERVAL = 1 * 60 * 1000; /* 5 Mins */

    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 10;

    private double mLatitude = 0.0, mLongitude = 0.0;

    private final int AVAILABLE = 1;

    private boolean isAvailable;

    private JSONObject json;

    public DeviceLocationService() {
    }

    public DeviceLocationService(Context context) {
        this.ctx = context;
        locationManager = (LocationManager) context
                .getSystemService(LOCATION_SERVICE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("22222222222222222222");
        mSessionManagement = new MMSharedPreferences(this);
        mDBHelper = new DBHelper(this);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("YES DESTROYED CALLEDD.");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        mGoogleApiClient = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        System.out.println("11111111111111111111");
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        return START_STICKY;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            System.out.println("LOCATOIN CHANGEDDDDD");
            mSessionManagement.putString("curLat", String.valueOf(location.getLatitude()));
            mSessionManagement.putString("curLong", String.valueOf(location.getLongitude()));
            //System.out.println("LOCA LAT:::" + location.getLatitude());
            //System.out.println("LOCA LANG:::" + location.getLongitude());
            saveDetailsInDB(location.getLatitude(), location.getLongitude());
        }
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        System.out.println("LOCATOIN CONNECTED");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (mLocation != null) {
            mSessionManagement.putString("curLat", String.valueOf(mLocation.getLatitude()));
            mSessionManagement.putString("curLong", String.valueOf(mLocation.getLongitude()));
            //System.out.println("LOCA LAT:::" + mLocation.getLatitude());
            //System.out.println("LOCA LANG:::" + mLocation.getLongitude());
            //saveDetailsInDB(mLocation.getLatitude(), mLocation.getLongitude());
        }

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void startLocationUpdates() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    private String getDeviceId() {
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }

    private void saveDetailsInDB(double latitude1, double longitude1) {
        synchronized (this) {
            try {
                Date curDate = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");

                String date = format.format(curDate);
                String time = format1.format(curDate);
                String deviceId = getDeviceId();
                String userId = mSessionManagement.getString("userId");
                String latitude = String.valueOf(latitude1);
                String longitude = String.valueOf(longitude1);
                String speed = "";
                mDBHelper.insertDeviceOrUserLocationDetails(deviceId, userId, date, time, latitude, longitude, speed, "0");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        synchronized (this) {
            if (new NetworkConnectionDetector(this).isNetworkConnected()) {
                this.startService(new Intent(this, SyncDeviceLocationDetailsService.class));
            }
        }
    }
}
