package com.rightclickit.b2bsaleon.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TripsheetsSOListAdapter;
import com.rightclickit.b2bsaleon.adapters.TripsheetsStockListAdapter;
import com.rightclickit.b2bsaleon.beanclass.TripsheetSOList;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.TripsheetsModel;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.NetworkManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TripSheetView extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    FloatingActionButton fab;
    LinearLayout tsDashBoardLayout;
    LinearLayout tsTripsheetsLayout;
    LinearLayout tsCustomersLayout;
    LinearLayout tsProductsLayout;
    LinearLayout tsTDCLayout;
    LinearLayout tsRetailersLayout;
    private Location mLastLocation, startingLocation, endingLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates;

    private LocationRequest mLocationRequest;
    private LocationManager lm = null;
    private boolean gps_enabled;
    private DBHelper mDBHelper;
    private MMSharedPreferences mPreferences;
    private GoogleMap mMap;
    private MarkerOptions markerOptions, sourceMarkerOptions, destinationMarkerOptions;
    private Marker mapMarker, sourceMarker, destinationMarker;
    private DownloadGoogleDirectionsTask downloadGoogleDirectionsTask;
    private List<List<HashMap<String, String>>> routes = null;
    private ArrayList<LatLng> points = null;
    private PolylineOptions lineOptions = null;
    private Polyline destinationPolyline = null;
    private String mLatitude = "", mLongitude = "", mDeviceId = "", mProfilePic = "";
    TextView listView;
    TextView mapView;
    LinearLayout listview;
    LinearLayout mapview;
    Button taleorder;
    LinearLayout delivery;
    private ListView mTripsheetsSOListView;
    private TripsheetsModel mTripsheetsModel;
    private TripsheetsSOListAdapter mTripsheetSOAdapter;
    private String mTripSheetId = "", mTakeOrderPrivilege = "";
    private double mCurrentLocationLat = 0.0, mCurrentLocationLongitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_sheet_view);
        mRequestingLocationUpdates = false;

        if (lm == null)
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        mTripsheetsModel = new TripsheetsModel(this, TripSheetView.this);
        this.getSupportActionBar().setTitle("TRIPSHEET PREVIEW");
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
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            mTripSheetId = bundle.getString("tripsheetId");
        }
        mDBHelper = new DBHelper(TripSheetView.this);
        mPreferences = new MMSharedPreferences(TripSheetView.this);

        mTripsheetsSOListView = (ListView) findViewById(R.id.TripsheetsSOListView);

//        taleorder = (Button) findViewById(R.id.btn_sale_ord1);
//        taleorder.setVisibility(View.GONE);
//        delivery = (LinearLayout) findViewById(R.id.gotoCustomer);
//        delivery.setVisibility(View.GONE);


        listView = (TextView) findViewById(R.id.tv_listView);
        mapView = (TextView) findViewById(R.id.tv_mapView);
        // listview = (LinearLayout) findViewById(R.id.ll_listview);
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
                mTripsheetsSOListView.setVisibility(View.VISIBLE);
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
                mTripsheetsSOListView.setVisibility(View.GONE);
                mapview.setVisibility(View.VISIBLE);

                // Formatting Google Directions API URL
                String destinationDirectionsURL = String.format(Constants.GOOGLE_DIRECTIONS_URL, mCurrentLocationLat, mCurrentLocationLongitude, "17.433740", "78.501596");
                new DownloadGoogleDirectionsTask().execute(destinationDirectionsURL);
                LatLng destLatLng = new LatLng(Double.parseDouble("17.433740"), Double.parseDouble("78.501596"));
                destinationMarkerOptions = new MarkerOptions();
                destinationMarkerOptions.position(destLatLng).icon(BitmapDescriptorFactory.defaultMarker());

                destinationMarker = mMap.addMarker(destinationMarkerOptions);
                destinationMarker.setTitle("Secunderabad RailwayStation");


            }
        });
        tsDashBoardLayout = (LinearLayout) findViewById(R.id.DashboardLayout);
        tsDashBoardLayout.setVisibility(View.GONE);
        tsDashBoardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(TripsheetsActivity.this, "Clicked on Dashboard", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                tsDashBoardLayout.startAnimation(animation1);
                Intent i = new Intent(TripSheetView.this, DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });
        tsTripsheetsLayout = (LinearLayout) findViewById(R.id.TripSheetsLayout);
        tsTripsheetsLayout.setVisibility(View.GONE);
        tsTripsheetsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(TripSheetsActivity.this, "Clicked on Tripsheets", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                tsTripsheetsLayout.startAnimation(animation1);

            }
        });

        tsCustomersLayout = (LinearLayout) findViewById(R.id.CustomersLayout);
        tsCustomersLayout.setVisibility(View.GONE);
        tsCustomersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Tripsheet_Activity.this, "Clicked on Customers", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                tsCustomersLayout.startAnimation(animation1);
                Intent i = new Intent(TripSheetView.this, AgentsActivity.class);
                startActivity(i);
                finish();
            }
        });

        tsRetailersLayout = (LinearLayout) findViewById(R.id.RetailersLayout);
        tsRetailersLayout.setVisibility(View.GONE);
        tsRetailersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                tsRetailersLayout.startAnimation(animation1);
                Intent i = new Intent(TripSheetView.this, RetailersActivity.class);
                startActivity(i);
                finish();
            }
        });
        tsProductsLayout = (LinearLayout) findViewById(R.id.ProductsLayout);
        tsProductsLayout.setVisibility(View.GONE);
        tsProductsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(Tripsheet_Activity.this, "Clicked on Products", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                tsProductsLayout.startAnimation(animation1);
                Intent i = new Intent(TripSheetView.this, Products_Activity.class);
                startActivity(i);
                finish();
            }
        });
        tsTDCLayout = (LinearLayout) findViewById(R.id.TDCLayout);
        tsTDCLayout.setVisibility(View.GONE);
        tsTDCLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(Tripsheet_Activity.this, "Clicked on TDC", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                tsTDCLayout.startAnimation(animation1);
                Intent i = new Intent(TripSheetView.this, TDCSalesActivity.class);
                startActivity(i);
                finish();
            }
        });

//        taleorder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(TripSheetView.this, TripsheetTakeorder.class);
//                startActivity(i);
//                finish();
//            }
//        });
//        delivery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(TripSheetView.this, TripsheetDelivery.class);
//                startActivity(i);
//                finish();
//            }
//        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        HashMap<String, String> userMapData = mDBHelper.getUsersData();
        ArrayList<String> privilegesData = mDBHelper.getUserActivityDetailsByUserId(userMapData.get("user_id"));
        //System.out.println("F 11111 ***COUNT === " + privilegesData.size());
        for (int k = 0; k < privilegesData.size(); k++) {
            if (privilegesData.get(k).toString().equals("Dashboard")) {
                tsDashBoardLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("TripSheets")) {
                tsTripsheetsLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("Customers")) {
                tsCustomersLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("Products")) {
                tsProductsLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("TDC")) {
                tsTDCLayout.setVisibility(View.VISIBLE);
            } else if (privilegesData.get(k).toString().equals("Retailers")) {
                tsRetailersLayout.setVisibility(View.VISIBLE);
            }
        }


        ArrayList<String> privilegeActionsData = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("TripSheets"));
        //System.out.println("F 11111 ***COUNT === " + privilegeActionsData.size());
        for (int z = 0; z < privilegeActionsData.size(); z++) {
            //    System.out.println("Name::: " + privilegeActionsData.get(z).toString());

            if (privilegeActionsData.get(z).toString().equals("list_view_takeorder")) {
                mTakeOrderPrivilege = privilegeActionsData.get(z).toString();
            }


//            if (privilegeActionsData.get(z).toString().equals("list_view_takeorder")) {
//                taleorder.setVisibility(View.VISIBLE);
//            } else if (privilegeActionsData.get(z).toString().equals("list_view_delivery")) {
//                delivery.setVisibility(View.VISIBLE);
//            }


        }


        mLatitude = userMapData.get("latitude");
        mLongitude = userMapData.get("longitude");


        fab = (FloatingActionButton) findViewById(R.id.productfab);
        fab.setVisibility(View.VISIBLE);
        fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_eye_white_24dp));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Clicked Tripsheet Preview", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(TripSheetView.this, TripSheetViewPreview.class);
                startActivity(i);
                finish();
            }
        });

        if (new NetworkConnectionDetector(TripSheetView.this).isNetworkConnected()) {
//            if (mDBHelper.getTripsheetsStockTableCount() > 0) {
//                ArrayList<TripsheetsStockList> tripsList = mDBHelper.fetchAllTripsheetsStockList(mTripSheetId);
//                if (tripsList.size() > 0) {
//                    loadTripsData(tripsList);
//                }
//            } else {
            //startService(new Intent(getApplicationContext(), SyncStakeHolderTypesService.class));
            mTripsheetsModel.getTripsheetsSoList(mTripSheetId);
//            }
        } else {
            // System.out.println("ELSE::: ");
            ArrayList<TripsheetsStockList> tripsList = mDBHelper.fetchAllTripsheetsStockList(mTripSheetId);
            if (tripsList.size() > 0) {
                //loadTripsoData(tripsList);
            }
        }
    }

    public void loadTripsoData(ArrayList<TripsheetSOList> tripsSOList) {
        if (mTripsheetSOAdapter != null) {
            mTripsheetSOAdapter = null;
        }
        mTripsheetSOAdapter = new TripsheetsSOListAdapter(this, TripSheetView.this, tripsSOList, mTakeOrderPrivilege);
        mTripsheetsSOListView.setAdapter(mTripsheetSOAdapter);
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
        menu.findItem(R.id.Add).setVisible(false);
        menu.findItem(R.id.autorenew).setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, TripSheetsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        LatLng sydney;
//        if (!mLatitude.equals("") && !mLongitude.equals("")) {
//            sydney = new LatLng(Double.parseDouble(mLatitude), Double.parseDouble(mLongitude));
//        } else {
//            // Pass current location lat and long
//            sydney = new LatLng(17.3850440, 78.4866710);
//        }
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Hyderabad, Telangana"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            return;
//        }
        mMap.setMyLocationEnabled(true);

        buildGoogleApiClient();
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        try {
            System.out.println("In GOOGLE API CLIENT");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
            mGoogleApiClient.connect();
            createLocationRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creating location reqpuest object
     */
    protected void createLocationRequest() {
        System.out.println("In CREATE LOCATION REQUEST");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(Utils.DISPLACEMENT);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            System.out.println("IN CONNECTED.....");
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (mRequestingLocationUpdates) {
                    startLocationUpdates();
                }

                if (mLastLocation == null) {
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                }

                if (mLastLocation != null) {
                    locationChanged(mLastLocation);
                } else {
                    Toast.makeText(this, "Unable to get location details.", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("TRIPSHEETS", "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            mLastLocation = location;
            locationChanged(mLastLocation);
        }
    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    public void locationChanged(Location location) {
        try {

            // Updating Current Location on Map
            addMarkerOnMap(location.getLatitude(), location.getLongitude());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * function to add marker on map based on current location lat long
     *
     * @param latitude
     * @param longitude
     */
    private void addMarkerOnMap(double latitude, double longitude) {
        try {
            System.out.println("LAT::: " + latitude);
            System.out.println("LONG::: " + longitude);
            mCurrentLocationLat = latitude;
            mCurrentLocationLongitude = longitude;
            if (markerOptions == null)
                markerOptions = new MarkerOptions();

            // Creating a LatLng object for the current / new location
            LatLng currentLatLng = new LatLng(latitude, longitude);
            markerOptions.position(currentLatLng).icon(BitmapDescriptorFactory.defaultMarker());

            if (mapMarker != null)
                mapMarker.remove();

            mapMarker = mMap.addMarker(markerOptions);

//            if (dlBean.getAddress() != null) {
//                if (!dlBean.getAddress().equals("No Location Found") && !dlBean.getAddress().equals("No Address returned") && !dlBean.getAddress().equals("No Network To Get Address"))
//                    mapMarker.setTitle(dlBean.getAddress());
//            }

            // Showing the current location in Google Map by Zooming it
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Fetches data from url passed
    private class DownloadGoogleDirectionsTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // Downloading data in non-ui thread
        @Override
        protected JSONObject doInBackground(String... url) {
            JSONObject data = null;

            try {
                // Fetching the data from web service
                data = new NetworkManager().makeHttpGetConnectionWithJsonOutput(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of doInBackground()
        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<JSONObject, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(JSONObject... jsonData) {

            JSONObject jObject;

            try {
                if (routes != null) {
                    if (routes.size() > 0) {
                        routes.clear();
                    }
                }

                DirectionsJSONParser parser = new DirectionsJSONParser();
                // Starts parsing data
                routes = parser.parse(jsonData[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            generatePolyLinesFromWayPointsAndAddToMap(result);
        }
    }

    public void generatePolyLinesFromWayPointsAndAddToMap(List<List<HashMap<String, String>>> wayPoints) {
        try {
            // Removing previous routes to destination if any
            if (destinationPolyline != null)
                destinationPolyline.remove();

            if (points != null) {
                if (points.size() > 0) {
                    points.clear();
                }
            }

            if (lineOptions != null) {
                lineOptions = null;
            }

            // Traversing through all the routes
            for (int i = 0; i < wayPoints.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = wayPoints.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.parseColor("#1E90FF"));
            }

            // Drawing polyline in the Google Map for the i-th route
            destinationPolyline = mMap.addPolyline(lineOptions);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
