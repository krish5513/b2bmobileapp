package com.rightclickit.b2bsaleon.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.AgentsAdapter;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;

public class AgentsInfoActivity extends AppCompatActivity implements OnMapReadyCallback {
    EditText firstname,lastname,mobile,address;
    public static ImageView avatar,poi,poa;
    private MMSharedPreferences mPreference;
    ArrayList<AgentsBean> mAgentsBeansList1;
    private ImageLoader mImageLoader;
    private GoogleMap mMap;
    private String mLatitude="",mLongitude="";
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agents_info);

        mPreference = new MMSharedPreferences(this);
        Bundle bundle = getIntent().getExtras();
        this.getSupportActionBar().setTitle("customerName");
        this.getSupportActionBar().setSubtitle(null);
        this.getSupportActionBar().setLogo(R.drawable.customers_white_24);
        this.getSupportActionBar().setTitle(bundle.getString("FIRSTNAME"));
        // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        mAgentsBeansList1 = new ArrayList<>();
        mImageLoader=new ImageLoader(this);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        firstname = (EditText) findViewById(R.id.first_name);
        lastname = (EditText) findViewById(R.id.last_name);
        mobile = (EditText) findViewById(R.id.phoneNo);
        address = (EditText) findViewById(R.id.address);
        avatar = (ImageView) findViewById(R.id.shopaddress_image);
        poi = (ImageView) findViewById(R.id.poi_image);
        poa = (ImageView) findViewById(R.id.poa_image);

        firstname.setText(bundle.getString("FIRSTNAME"));
        lastname.setText(bundle.getString("LASTNAME"));
        mobile.setText(bundle.getString("MOBILE"));
        address.setText(bundle.getString("ADDRESS"));
        Bundle extras = getIntent().getExtras();
        Bitmap avatarbmp = (Bitmap) extras.getParcelable("avatar");
        Bitmap poibmp = (Bitmap) extras.getParcelable("poi");
        Bitmap poabmp = (Bitmap) extras.getParcelable("poa");
        if(avatarbmp!=null){
            avatar.setImageBitmap(avatarbmp);
        }
        if(poibmp!=null){
            poi.setImageBitmap(poibmp );
        }
        if(poabmp!=null){
            poa.setImageBitmap(poabmp );
        }
        /*for (int i = 0; i < mAgentsBeansList1.size(); i++) {
            if (!mAgentsBeansList1.get(i).getmPoiImage().equals("")) {
                mImageLoader.DisplayImage(mAgentsBeansList1.get(i).getmPoiImage(),poi, null, "");
                mImageLoader.DisplayImage(mAgentsBeansList1.get(i).getmPoaImage(),poa, null, "");
                mLatitude=mAgentsBeansList1.get(i).getmLatitude();
                mLongitude=mAgentsBeansList1.get(i).getmLatitude();
            }
        }

*/

        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFrag);
        supportMapFragment.getMapAsync(this);
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
    public boolean onPrepareOptionsMenu(Menu menu) {


        menu.findItem(R.id.notifications).setVisible(false);
        menu.findItem(R.id.settings).setVisible(false);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem( R.id.Add).setVisible(false);


        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, AgentsActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
/*
        double latitude = mMap.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(100));*/
        LatLng sydney;
        if(!mLatitude.equals("")&& !mLongitude.equals("")){
            sydney = new LatLng(Double.parseDouble(mLatitude), Double.parseDouble(mLongitude));


        }else {
            // Pass current location lat and long
            sydney = new LatLng(17.3850440, 78.4866710);
        }
        mMap.addMarker(new MarkerOptions().position(sydney).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mMap.setMyLocationEnabled(true);

    }
}


