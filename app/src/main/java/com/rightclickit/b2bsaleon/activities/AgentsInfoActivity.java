package com.rightclickit.b2bsaleon.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.models.AgentsModel;
import com.rightclickit.b2bsaleon.services.SyncAgentsService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class AgentsInfoActivity extends AppCompatActivity  {
    EditText firstname, lastname, mobile, address,routecode;
    public static ImageView avatar, poi, poa;
    private MMSharedPreferences mPreference;
    ArrayList<AgentsBean> mAgentsBeansList1;
    private ImageLoader mImageLoader;
    private GoogleMap mMap;
    private String mLatitude = "", mLongitude = "";
    DBHelper dbHelper;
    TextView agent_update,mapFullView;
    private ArrayList<AgentsBean> mAgentsBeansList = new ArrayList<AgentsBean>();
    AgentsModel agentsmodel;
    String str_BusinessName, str_PersonName, str_Mobileno, str_address;
    double longitude, latitude;
    private static final int ACTION_TAKE_PHOTO_A = 1;
    private static final int ACTION_TAKE_GALLERY_PIC_A = 2;
    private JSONArray routeCodesArray;
    ArrayList<String> idsArray = new ArrayList<String>();
    List<String> routesDataList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agents_info);

        mPreference = new MMSharedPreferences(this);
        Bundle bundle = getIntent().getExtras();
        this.getSupportActionBar().setTitle("CUSTOMER NAME");
        this.getSupportActionBar().setSubtitle(null);
        this.getSupportActionBar().setLogo(R.drawable.customers_white_24);
        this.getSupportActionBar().setTitle(bundle.getString("FIRSTNAME"));
        // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        mAgentsBeansList1 = new ArrayList<>();
        mImageLoader = new ImageLoader(this);

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
        routecode=(EditText) findViewById(R.id.routecode);

        mapFullView = (TextView) findViewById(R.id.MapFullView);


        agentsmodel = new AgentsModel(AgentsInfoActivity.this, this);
        dbHelper = new DBHelper(getApplicationContext());



        firstname.setText(bundle.getString("FIRSTNAME"));
        lastname.setText(bundle.getString("LASTNAME"));
        mobile.setText(bundle.getString("MOBILE"));
        address.setText(bundle.getString("ADDRESS"));
        routecode.setText(dbHelper.getRouteNameByRouteId(bundle.getString("ROUTEID")));


        Bundle extras = getIntent().getExtras();
        Bitmap avatarbmp = (Bitmap) extras.getParcelable("avatar");
        Bitmap poibmp = (Bitmap) extras.getParcelable("poi");
        Bitmap poabmp = (Bitmap) extras.getParcelable("poa");



        agent_update=(TextView)findViewById(R.id.agent_update);




        if (avatarbmp != null) {
            avatar.setImageBitmap(avatarbmp);
        }
        if (poibmp != null) {
            poi.setImageBitmap(poibmp);
        }
        if (poabmp != null) {
            poa.setImageBitmap(poabmp);
        }

        mapFullView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent ii = new Intent(AgentsInfoActivity.this, AgentMapFullScreen.class);
                    ii.putExtra("fromLat", String.valueOf(latitude));
                    ii.putExtra("fromLong", String.valueOf(longitude));
                    ii.putExtra("FromPage", "Agentsinfo");
                    startActivityForResult(ii, 100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });




        agent_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                agent_update.startAnimation(animation1);

                validateCustomerDetails();
            }
        });
        /*for (int i = 0; i < mAgentsBeansList1.size(); i++) {
            if (!mAgentsBeansList1.get(i).getmPoiImage().equals("")) {
                mImageLoader.DisplayImage(mAgentsBeansList1.get(i).getmPoiImage(),poi, null, "");
                mImageLoader.DisplayImage(mAgentsBeansList1.get(i).getmPoaImage(),poa, null, "");
                mLatitude=mAgentsBeansList1.get(i).getmLatitude();
                mLongitude=mAgentsBeansList1.get(i).getmLatitude();
            }
        }

*/

        // LOCATION DETAILS
        try {
            latitude = Double.parseDouble(mPreference.getString("curLat"));
            longitude = Double.parseDouble(mPreference.getString("curLong"));

        }catch (Exception e){
            e.printStackTrace();
        }

        /*SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFrag);
        supportMapFragment.getMapAsync(this);*/


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                replaceMapFragment();
            }
        });
    }

    private void validateCustomerDetails() {
        str_BusinessName = firstname.getText().toString();
        Log.i("bname", str_BusinessName);
        str_PersonName = lastname.getText().toString();
        str_Mobileno = mobile.getText().toString();
        str_address = address.getText().toString();
        if (str_BusinessName.length() == 0 || str_BusinessName.length() == ' ') {
            firstname.setError("Please enter BusinessName");
            Toast.makeText(getApplicationContext(), "Please enter BusinessName", Toast.LENGTH_SHORT).show();
            //
        } else if (str_PersonName.length() == 0 || str_PersonName.length() == ' ') {
            firstname.setError(null);
            lastname.setError("Please enter PersonName");
            Toast.makeText(getApplicationContext(), "Please enter PersonName", Toast.LENGTH_SHORT).show();

        } else if (str_Mobileno.length() == 0 || str_Mobileno.length() == ' ' || str_Mobileno.length() != 10) {
            lastname.setError(null);
            mobile.setError("Please enter  10 digit mobileno");
            Toast.makeText(getApplicationContext(), "Please enter  10 digit mobileno", Toast.LENGTH_SHORT).show();

        } /*else if (str_address.length() == 0 || str_address.length() == ' ') {
                                            mobile.setError(null);
                                            address.setError("Please enter address");
                                            Toast.makeText(getApplicationContext(), "Please enter address", Toast.LENGTH_SHORT).show();

                                        }
*/ else {
            firstname.setError(null);
            lastname.setError(null);
            mobile.setError(null);
            address.setError(null);

            HashMap<String, String> userMapData = dbHelper.getUsersData();
            String stakeholderid = userMapData.get("stakeholder_id");
            //Log.i("STAKEID",stakeholderid);
            String userid = userMapData.get("user_id");
            try {
                JSONObject routesJob = new JSONObject(userMapData.get("route_ids").toString());
                JSONArray routesArray = routesJob.getJSONArray("routeArray");

                AgentsBean agentsBean = new AgentsBean();
               agentsBean.setmFirstname(str_BusinessName);
               agentsBean.setmLastname(str_PersonName);
               agentsBean.setMphoneNO(str_Mobileno);
                agentsBean.setmAgentEmail("");
                agentsBean.setmAgentPassword(Utility.getMd5String("123456789"));
                agentsBean.setmAgentCode("");
                agentsBean.setmAgentReprtingto("");
                agentsBean.setmAgentVerifycode("");
                agentsBean.setmStatus("I");
                agentsBean.setmAgentDelete("N");
                agentsBean.setmAgentStakeid(stakeholderid);
                agentsBean.setmAgentCreatedBy(userid);
                agentsBean.setmAgentUpdatedBy(userid);
                agentsBean.setMaddress(str_address);
                agentsBean.setmLatitude(String.valueOf(latitude));
                agentsBean.setmLongitude(String.valueOf(longitude));
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                agentsBean.setmAgentCreatedOn(timeStamp);
                agentsBean.setmAgentUpdatedOn(timeStamp);
                agentsBean.setmObAmount("");
                agentsBean.setmOrderValue("");
                agentsBean.setmTotalAmount("");
                agentsBean.setmDueAmount("");
                agentsBean.setmAgentPic("");
                agentsBean.setmAgentApprovedOn("");
                agentsBean.setmAgentDeviceSync("0");
                agentsBean.setmAgentAccessDevice("NO");
                agentsBean.setmAgentBackUp("0");
               // agentsBean.setmAgentId();
                // agentsBean.setmAgentRouteId(routesArray.toString());
               // agentsBean.setmAgentRouteId(mPreference.getString("_routename"));
                // agentsBean.setmSelectedRouteName(selected_val);
                agentsBean.setmUploadStatus("0");
                mAgentsBeansList.add(agentsBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // db.insertAgentDetails(mAgentsBeansList);
            synchronized (this) {
                long uniqueId;
                synchronized (this) {
                    uniqueId =  dbHelper.updateAgentDetails(mAgentsBeansList, userid);
                }
                if (new NetworkConnectionDetector(AgentsInfoActivity.this).isNetworkConnected()) {
                    //agentsmodel.customerAdd(str_BusinessName, str_PersonName, str_Mobileno, stakeholderid, userid, "", "123456789", "", "", "", "IA", "N", str_address, String.valueOf(latitude), String.valueOf(longitude), timeStamp, "", "", "", "", "");
                    //agentsmodel.customerAdd(mAgentsBeansList, db.getStakeTypeIdByStakeType("2"), "addC", uniqueId);
                    Intent syncTDCCustomersServiceIntent = new Intent(AgentsInfoActivity.this, SyncAgentsService.class);
                    startService(syncTDCCustomersServiceIntent);
                }
                Toast.makeText(getApplicationContext(), "Details Updated successfully", Toast.LENGTH_SHORT).show();
                synchronized (this) {
                    Intent i = new Intent(AgentsInfoActivity.this, AgentsActivity.class);
                    startActivity(i);
                    finish();
                }
            }
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


    private void replaceMapFragment() {


        // Enable Zoom
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        //set Map TYPE
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

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
        mMap.setMyLocationEnabled(true);

        //set "listener" for changing my location
        //googlemap.setOnMyLocationChangeListener(myLocationChangeListener());

        LatLng loc = new LatLng(latitude, longitude);

        Marker marker;
        mMap.clear();
        marker = mMap.addMarker(new MarkerOptions().position(loc));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                LatLng loc = new LatLng(latLng.latitude, latLng.longitude);
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                Marker marker;
                mMap.clear();
                marker = mMap.addMarker(new MarkerOptions().position(loc));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        });
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
        menu.findItem(R.id.Add).setVisible(false);
        menu.findItem(R.id.autorenew).setVisible(false);
        menu.findItem(R.id.sort).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, AgentsActivity.class);
        startActivity(intent);
        finish();
    }


   /* @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
*//*
        double latitude = mMap.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(100));*//*
        LatLng sydney;
        if (!mLatitude.equals("") && !mLongitude.equals("")) {
            sydney = new LatLng(Double.parseDouble(mLatitude), Double.parseDouble(mLongitude));


        } else {
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

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

         if (requestCode == 100) {
                latitude = Double.parseDouble(data.getStringExtra("lat"));
                longitude = Double.parseDouble(data.getStringExtra("long"));

                LatLng loc = new LatLng(latitude, longitude);

                Marker marker;
                mMap.clear();
                marker = mMap.addMarker(new MarkerOptions().position(loc));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }

    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener() {

        return new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
                Marker marker;
                mMap.clear();
                marker = mMap.addMarker(new MarkerOptions().position(loc));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                // locationText.setText("You are at [" + longitude + " ; " + latitude + " ]");
                // Geocoder geocoder = new Geocoder(Agents_AddActivity.this, Locale.ENGLISH);

                /*try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                    if (addresses != null && addresses.size() > 0) {
                        Address returnedAddress = addresses.get(0);
                        //strReturnedAddress =
                        for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                            strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                        }

                        address.setText(strReturnedAddress.toString());

                    } else {
                        address.setText("");
                        //  address.setText(strReturnedAddress.toString());
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    address.setText("");
                }
*/

                //get current address by invoke an AsyncTask object
                // new GetAddressTask(Agents_AddActivity.this).execute(String.valueOf(latitude), String.valueOf(longitude));
            }
        };
    }

}


