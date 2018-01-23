package com.rightclickit.b2bsaleon.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.models.AgentsModel;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AgentsInfoActivity extends AppCompatActivity implements OnMapReadyCallback {
    EditText firstname, lastname, mobile, address;
    public static ImageView avatar, poi, poa;
    private MMSharedPreferences mPreference;
    ArrayList<AgentsBean> mAgentsBeansList1;
    private ImageLoader mImageLoader;
    private GoogleMap mMap;
    private String mLatitude = "", mLongitude = "";
    DBHelper dbHelper;
    TextView agent_update;
    private ArrayList<AgentsBean> mAgentsBeansList = new ArrayList<AgentsBean>();
    AgentsModel agentsmodel;
    String str_BusinessName, str_PersonName, str_Mobileno, str_address;

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

        firstname.setText(bundle.getString("FIRSTNAME"));
        lastname.setText(bundle.getString("LASTNAME"));
        mobile.setText(bundle.getString("MOBILE"));
        address.setText(bundle.getString("ADDRESS"));
        Bundle extras = getIntent().getExtras();
        Bitmap avatarbmp = (Bitmap) extras.getParcelable("avatar");
        Bitmap poibmp = (Bitmap) extras.getParcelable("poi");
        Bitmap poabmp = (Bitmap) extras.getParcelable("poa");

        agentsmodel = new AgentsModel(AgentsInfoActivity.this, this);
        dbHelper = new DBHelper(getApplicationContext());


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

        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFrag);
        supportMapFragment.getMapAsync(this);
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
             //   agentsBean.setmFirstname(str_BusinessName);
             //   agentsBean.setmLastname(str_PersonName);
             //   agentsBean.setMphoneNO(str_Mobileno);
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
              //  agentsBean.setMaddress(str_address);
               // agentsBean.setmLatitude(String.valueOf(latitude));
               // agentsBean.setmLongitude(String.valueOf(longitude));
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
                // agentsBean.setmAgentRouteId(routesArray.toString());
               // agentsBean.setmAgentRouteId(selected_val);
                // agentsBean.setmSelectedRouteName(selected_val);
                mAgentsBeansList.add(agentsBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // db.insertAgentDetails(mAgentsBeansList);
            synchronized (this) {
                if (new NetworkConnectionDetector(AgentsInfoActivity.this).isNetworkConnected()) {
                    //agentsmodel.customerAdd(str_BusinessName, str_PersonName, str_Mobileno, stakeholderid, userid, "", "123456789", "", "", "", "IA", "N", str_address, String.valueOf(latitude), String.valueOf(longitude), timeStamp, "", "", "", "", "");
                    agentsmodel.customerAdd(mAgentsBeansList, dbHelper.getStakeTypeIdByStakeType("2"));
                }
                dbHelper.updateAgentDetails(mAgentsBeansList, userid);
                Toast.makeText(getApplicationContext(), "Details saved successfully", Toast.LENGTH_SHORT).show();
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

    }
}


