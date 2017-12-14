package com.rightclickit.b2bsaleon.activities;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
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
import com.rightclickit.b2bsaleon.beanclass.AgentLatLong;
import com.rightclickit.b2bsaleon.beanclass.PaymentsBean;
import com.rightclickit.b2bsaleon.beanclass.TripsheetSOList;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsList;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.TripsheetsModel;
import com.rightclickit.b2bsaleon.services.SyncTakeOrdersService;
import com.rightclickit.b2bsaleon.services.SyncTripSheetsPaymentsService;
import com.rightclickit.b2bsaleon.services.SyncTripSheetsStockService;
import com.rightclickit.b2bsaleon.services.SyncTripsheetDeliveriesService;
import com.rightclickit.b2bsaleon.services.SyncTripsheetReturnsService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.NetworkManager;
import com.rightclickit.b2bsaleon.util.Utility;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TripSheetView extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private Context activityContext;
    private SearchView search;
    FloatingActionButton fab;
    LinearLayout tsDashBoardLayout;
    LinearLayout tsTripsheetsLayout;
    LinearLayout tsCustomersLayout;
    LinearLayout tsProductsLayout;
    LinearLayout tsTDCLayout;
    LinearLayout tsRetailersLayout;
    private TextView ts_ob_amount, ts_order_value, ts_total_received, ts_total_due, ts_code, ts_date;
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
    private String mTripSheetId = "", mTakeOrderPrivilege = "", mTripSheetCode = "", mTripSheetDate = "", mhidePrevilige = "";
    private double mCurrentLocationLat = 0.0, mCurrentLocationLongitude = 0.0;
    private ArrayList<TripsheetSOList> tripSheetSOList = new ArrayList<>();
    private ArrayList<AgentLatLong> agentsLatLongList = new ArrayList<>(); // this list contains all valid agents lat long details
    private NetworkConnectionDetector networkConnectionDetector;
    private boolean isTripSheetClosed = false;
    String startDateStrNewFormat;
    private TextView mNoTripsFoundText;
    Double orderTotal = 0.0;
    private int uploadedCount = 0, uploadedCountReturns = 0, uploadedCountpayments = 0, uploadedTruckQty = 0, uploadedTakeOrderQty = 0;
    private Runnable mRunnable;
    private Handler mHandler = new Handler();
    private android.support.v7.app.AlertDialog alertDialog1 = null;
    private android.support.v7.app.AlertDialog.Builder alertDialogBuilder1;
    private boolean isSyncClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_sheet_view);

        try {
            activityContext = TripSheetView.this;

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

            mDBHelper = new DBHelper(TripSheetView.this);
            mPreferences = new MMSharedPreferences(TripSheetView.this);
            // mPreferences.putString("tripId",mTripSheetId);
            networkConnectionDetector = new NetworkConnectionDetector(activityContext);

            Bundle bundle = this.getIntent().getExtras();
            if (bundle != null) {
                mTripSheetId = bundle.getString("tripsheetId");


            }
            mTripSheetCode = mPreferences.getString("tripsheetCode");
            mTripSheetDate = mPreferences.getString("tripsheetDate");

            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("MMM dd,yyyy");
            mTripSheetDate = mPreferences.getString("tripsheetDate");
            Date date = null;
            try {
                date = inputFormat.parse(mTripSheetDate);
                startDateStrNewFormat = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            ts_code = (TextView) findViewById(R.id.tripsheetCode);
            ts_date = (TextView) findViewById(R.id.tripsheetDate);

            ts_code.setText(mTripSheetCode);
            ts_date.setText(startDateStrNewFormat);

            ts_ob_amount = (TextView) findViewById(R.id.ts_ob_amount);
            ts_order_value = (TextView) findViewById(R.id.ts_order_value);
            ts_total_received = (TextView) findViewById(R.id.ts_total_received);
            ts_total_due = (TextView) findViewById(R.id.ts_total_due);

            mTripsheetsSOListView = (ListView) findViewById(R.id.TripsheetsSOListView);
            mNoTripsFoundText = (TextView) findViewById(R.id.NoTripsFoundTextView);
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

                    loadSaleOrderMapView();
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
                if (privilegeActionsData.get(z).toString().equals("list_view_delivery")) {
                    mhidePrevilige = privilegeActionsData.get(z).toString();
                }
            }

            mLatitude = userMapData.get("latitude");
            mLongitude = userMapData.get("longitude");

            fab = (FloatingActionButton) findViewById(R.id.productfab);
            fab.setVisibility(View.VISIBLE);
            fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_eye_white_24dp));

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getApplicationContext(), "Clicked Tripsheet Preview", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(TripSheetView.this, TripSheetViewPreview.class);
                    i.putExtra("tripSheetId", mTripSheetId);
                    //  i.putExtra("tripsheetCode",mTripSheetCode);
                    //  i.putExtra("tripsheetDate",mTripSheetDate);
                    //   i.putExtra("data", (Serializable) mTripsheetSOAdapter.getData());
                    startActivity(i);
                    finish();
                }
            });

            // Updating Trip Sheet values in header
            TripsheetsList currentTripSheetDetails = mDBHelper.fetchTripSheetDetails(mTripSheetId);
            if (currentTripSheetDetails != null) {
                ts_ob_amount.setText(Utility.getFormattedCurrency(Double.parseDouble(currentTripSheetDetails.getmTripshhetOBAmount().replace(",", ""))));


                //  ts_order_value.setText(Utility.getFormattedCurrency(Double.parseDouble(currentTripSheetDetails.getmTripshhetOrderedAmount().replace(",", ""))));
                ts_total_received.setText(Utility.getFormattedCurrency(Double.parseDouble(currentTripSheetDetails.getmTripshhetReceivedAmount().replace(",", ""))));
                ts_total_due.setText(Utility.getFormattedCurrency(Double.parseDouble(currentTripSheetDetails.getmTripshhetDueAmount().replace(",", ""))));
            }

            tripSheetSOList = mDBHelper.getTripSheetSaleOrderDetails(mTripSheetId);
            for (int i = 0; i < tripSheetSOList.size(); i++) {

                orderTotal += Double.parseDouble(tripSheetSOList.get(i).getmTripshetSOValue());
                ts_order_value.setText(Utility.getFormattedCurrency(orderTotal));
            }


            isTripSheetClosed = mDBHelper.isTripSheetClosed(mTripSheetId);

            mTripsheetSOAdapter = new TripsheetsSOListAdapter(this, TripSheetView.this, tripSheetSOList, mTakeOrderPrivilege, isTripSheetClosed, mhidePrevilige);
            mTripsheetsSOListView.setAdapter(mTripsheetSOAdapter);
            tripSheetSOList = mDBHelper.getTripSheetSaleOrderDetails(mTripSheetId);
            if (networkConnectionDetector.isNetworkConnected()) {
                if (tripSheetSOList.size() > 0) {
                    loadTripSheetSaleOrderData();
                } else {
                    mTripsheetsModel.getTripsheetsSoList(mTripSheetId);
                }
            } else {
                loadTripSheetSaleOrderData();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadTripSheetSaleOrderData() {
        if (isSyncClicked) {
            showAlertDialog1(TripSheetView.this, "Sync Process", "Sales sync completed succssfully.");
        }

        if (tripSheetSOList.size() > 0) {
            tripSheetSOList.clear();
        }

        tripSheetSOList = mDBHelper.getTripSheetSaleOrderDetails(mTripSheetId);
        if (tripSheetSOList.size() > 0) {
            mTripsheetSOAdapter.setAllSaleOrdersList(tripSheetSOList);
            mTripsheetSOAdapter.notifyDataSetChanged();
        } else {
            mNoTripsFoundText.setText("No Sale orders Found." + "\n" + "Please click on sync button to get the sale orders.");
        }
    }

    public void loadSaleOrderMapView() {
        if (networkConnectionDetector.isNetworkConnected()) {
            for (int i = 0; i < agentsLatLongList.size(); i++) {
                AgentLatLong agentLatLong = agentsLatLongList.get(i);
                LatLng destLatLng = new LatLng(agentLatLong.getLatitude(), agentLatLong.getLongitude());

                destinationMarkerOptions = new MarkerOptions();
                destinationMarkerOptions.position(destLatLng).icon(BitmapDescriptorFactory.defaultMarker()).title(agentLatLong.getAgentName());
                destinationMarker = mMap.addMarker(destinationMarkerOptions);
            }

            // Drawing route directions to nearest agent.
            if (agentsLatLongList.size() > 0) {
                AgentLatLong nearestAgent = agentsLatLongList.get(0);

                // Formatting Google Directions API URL
                String destinationDirectionsURL = String.format(Constants.GOOGLE_DIRECTIONS_URL, mCurrentLocationLat, mCurrentLocationLongitude, nearestAgent.getLatitude(), nearestAgent.getLongitude()); // "17.433740", "78.501596"
                new DownloadGoogleDirectionsTask().execute(destinationDirectionsURL);
            }
        } else {
            Toast.makeText(activityContext, getResources().getString(R.string.error_no_internet), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);

        try {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            search = (SearchView) menu.findItem(R.id.action_search).getActionView();
            search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    mTripsheetSOAdapter.filter(query);
                    return true;
                }
            });

            // Get the search close button image view
            ImageView closeButton = (ImageView) search.findViewById(R.id.search_close_btn);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    search.setQuery("", false);
                    search.clearFocus();
                    search.onActionViewCollapsed();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.autorenew) {
            if (new NetworkConnectionDetector(TripSheetView.this).isNetworkConnected()) {
                showAlertDialog(TripSheetView.this, "Sync process", "Are you sure, you want start the sync process?");
            } else {
                new NetworkConnectionDetector(TripSheetView.this).displayNoNetworkError(TripSheetView.this);
            }
            return true;
        }

        switch (id) {
            case android.R.id.home:
                if (search.isIconified()) {
                    onBackPressed();
                } else {
                    search.setQuery("", false);
                    search.clearFocus();
                    search.onActionViewCollapsed();
                }
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
        menu.findItem(R.id.sort).setVisible(false);
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
            //System.out.println("LAT::: " + latitude);
            //System.out.println("LONG::: " + longitude);
            mCurrentLocationLat = latitude;
            mCurrentLocationLongitude = longitude;
            if (markerOptions == null)
                markerOptions = new MarkerOptions();

            // Creating a LatLng object for the current / new location
            LatLng currentLatLng = new LatLng(latitude, longitude);
            markerOptions.position(currentLatLng).icon(BitmapDescriptorFactory.defaultMarker()).title("Current Location");

            if (mapMarker != null)
                mapMarker.remove();

            mapMarker = mMap.addMarker(markerOptions);

//            if (dlBean.getAddress() != null) {
//                if (!dlBean.getAddress().equals("No Location Found") && !dlBean.getAddress().equals("No Address returned") && !dlBean.getAddress().equals("No Network To Get Address"))
//                    mapMarker.setTitle(dlBean.getAddress());
//            }

            // Showing the current location in Google Map by Zooming it
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));

            for (TripsheetSOList saleOrder : tripSheetSOList) {
                AgentLatLong agentLatLong = new AgentLatLong();
                double distance;

                // We are calculating distance b/w current location and agent location if lat long are not empty
                if (saleOrder.getmTripshetSOAgentLatitude() != null && !saleOrder.getmTripshetSOAgentLatitude().equals("") && saleOrder.getmTripshetSOAgentLongitude() != null && !saleOrder.getmTripshetSOAgentLongitude().equals("")) {
                    double agentLatitude = Double.parseDouble(saleOrder.getmTripshetSOAgentLatitude());
                    double agentLongitude = Double.parseDouble(saleOrder.getmTripshetSOAgentLongitude());

                    distance = getDistanceBetweenLocationsInMeters(mCurrentLocationLat, mCurrentLocationLongitude, agentLatitude, agentLongitude);

                    agentLatLong.setAgentName(saleOrder.getmTripshetSOAgentFirstName());
                    agentLatLong.setLatitude(agentLatitude);
                    agentLatLong.setLongitude(agentLongitude);
                    agentLatLong.setDistance(distance);

                    agentsLatLongList.add(agentLatLong);

                } else {
                    distance = 0.0;
                }

                saleOrder.setDistance(Math.round(distance / 1000));
            }

            // Sorting by distance in descending order i.e. nearest destination
            Collections.sort(agentsLatLongList, new Comparator<AgentLatLong>() {
                @Override
                public int compare(AgentLatLong o1, AgentLatLong o2) {
                    return o1.getDistance().compareTo(o2.getDistance());
                }
            });

            Collections.sort(tripSheetSOList, new Comparator<TripsheetSOList>() {
                @Override
                public int compare(TripsheetSOList o1, TripsheetSOList o2) {
                    return o1.getDistance().compareTo(o2.getDistance());
                }
            });

            // to update distance value in list after getting current location details.
            if (mTripsheetSOAdapter != null) {
                mTripsheetSOAdapter.setAllSaleOrdersList(tripSheetSOList);
                mTripsheetSOAdapter.notifyDataSetChanged();
            }
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
            if (lineOptions != null)
                destinationPolyline = mMap.addPolyline(lineOptions);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to calculate distance between two locations using HAVERSINE FORMULA
    private double getDistanceBetweenLocationsInMeters(double sourceLat, double sourceLong, double destLat, double destLong) {
        String distanceStr = "";
        double R = 6371000f; // Default Radius of the earth in meters
        double dLat = (sourceLat - destLat) * Math.PI / 180f;
        double dLon = (sourceLong - destLong) * Math.PI / 180f;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(dLat) * Math.cos(dLon) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2f * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        //System.out.println("DISTANCE IN METERS::: " + Math.round(distance));
        return distance;
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
                    showCustomValidationAlertForSync(TripSheetView.this, "deliverys");
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

    /**
     * Method to display alert without field.
     *
     * @param context
     * @param message
     */
    private void showCustomValidationAlertForSync(Activity context, String message) {
        // custom dialog
        try {
            isSyncClicked = true;
            alertDialogBuilder1 = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder1.setTitle("Sync Process");
            alertDialogBuilder1.setCancelable(false);
            if (message.equals("down")) {
                System.out.println("DOWNLOAD CALLEDDDDDDDD FINALLLLL");
                alertDialogBuilder1.setMessage("Downloading sale orders... Please wait.. ");
                mTripsheetsModel.getTripsheetsSoList(mTripSheetId);
            } else {
                // Stock verify qty
                ArrayList<String> unUploadedStockIds = mDBHelper.fetchUnUploadedTripSheetUniqueStockIds("verify");
                uploadedTruckQty = unUploadedStockIds.size();
                System.out.println("Stock Verify COUNT::: " + uploadedTruckQty);
                // Pending Take Orders
                ArrayList<String> pendingOrderAgents = mDBHelper.fetchAllPendingTakeOrderAgentIds();
                uploadedTakeOrderQty = pendingOrderAgents.size();
                System.out.println("Pending take orders::: " + uploadedTakeOrderQty);
                // Delivires Count
                ArrayList<String> unUploadedDeliveriesTripSheetIds = mDBHelper.fetchUnUploadedUniqueDeliveryTripSheetIds(mTripSheetId);
                uploadedCount = unUploadedDeliveriesTripSheetIds.size();
                System.out.println("D COUNT::: " + uploadedCount);

                // Returns Count
                ArrayList<String> unUploadedReturnsTripSheetIds = mDBHelper.fetchUnUploadedUniqueReturnsTripSheetIds(mTripSheetId);
                uploadedCountReturns = unUploadedReturnsTripSheetIds.size();
                System.out.println("R COUNT::: " + uploadedCountReturns);

                // Payments Count
                ArrayList<PaymentsBean> paymentsBeanArrayList = mDBHelper.fetchAllTripsheetsPaymentsList(mTripSheetId);
                uploadedCountpayments = paymentsBeanArrayList.size();
                System.out.println("P COUNT::: " + uploadedCountpayments);

                if (uploadedTruckQty > 0) {
                    System.out.println("TQ CALLEDDDDDDDD ");
                    alertDialogBuilder1.setMessage("Uploading pending truck quantity... Please wait.. ");
                    fetchCountFromDB(uploadedTruckQty, "tq");
                    if (new NetworkConnectionDetector(activityContext).isNetworkConnected()) {
                        Intent syncTripSheetsStockServiceIntent = new Intent(activityContext, SyncTripSheetsStockService.class);
                        syncTripSheetsStockServiceIntent.putExtra("actionType", "verify");
                        startService(syncTripSheetsStockServiceIntent);
                    }
                } else if (uploadedTakeOrderQty>0) {
                    System.out.println("PENDING TO CALLEDDDDDDDD ");
                    alertDialogBuilder1.setMessage("Uploading pending orders... Please wait.. ");
                    fetchCountFromDB(uploadedTakeOrderQty, "pto");
                    if (new NetworkConnectionDetector(TripSheetView.this).isNetworkConnected()) {
                        startService(new Intent(TripSheetView.this, SyncTakeOrdersService.class));
                    }
                } else if (uploadedCount > 0) {
                    System.out.println("D CALLEDDDDDDDD ");
                    alertDialogBuilder1.setMessage("Uploading pending deliveries... Please wait.. ");
                    fetchCountFromDB(uploadedCount, "deliveries");
                    if (new NetworkConnectionDetector(activityContext).isNetworkConnected()) {
                        Intent syncTripSheetDeliveriesServiceIntent = new Intent(activityContext, SyncTripsheetDeliveriesService.class);
                        startService(syncTripSheetDeliveriesServiceIntent);
                    }
                } else if (uploadedCountReturns > 0) {
                    System.out.println("R CALLEDDDDDDDD ");
                    alertDialogBuilder1.setMessage("Uploading pending returns... Please wait.. ");
                    fetchCountFromDB(uploadedCountReturns, "returns");
                    if (new NetworkConnectionDetector(activityContext).isNetworkConnected()) {
                        Intent syncTripSheetDeliveriesServiceIntent = new Intent(activityContext, SyncTripsheetReturnsService.class);
                        startService(syncTripSheetDeliveriesServiceIntent);
                    }
                } else if (uploadedCountpayments > 0) {
                    System.out.println("P CALLEDDDDDDDD ");
                    alertDialogBuilder1.setMessage("Uploading pending payments... Please wait.. ");
                    fetchCountFromDB(uploadedCountpayments, "payments");
                    if (new NetworkConnectionDetector(TripSheetView.this).isNetworkConnected()) {
                        startService(new Intent(TripSheetView.this, SyncTripSheetsPaymentsService.class));
                    }
                } else {
                    alertDialogBuilder1.setMessage("Downloading sale orders... Please wait.. ");
                    mTripsheetsModel.getTripsheetsSoList(mTripSheetId);
                }
            }

            alertDialog1 = alertDialogBuilder1.create();
            alertDialog1.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchCountFromDB(final int uploadedCount11, String deliveries) {
        if (uploadedCount11 > 0 && deliveries.equals("tq")) {
            System.out.println("TQ CALLEDDDDDDDD IFFFFFFFFFFFFFFF");
            mHandler.removeCallbacks(mRunnable);
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    ArrayList<String> unUploadedStockIds = mDBHelper.fetchUnUploadedTripSheetUniqueStockIds("verify");
                    fetchCountFromDB(unUploadedStockIds.size(), "tq");
                }
            };
            mHandler.postDelayed(mRunnable, 1000);
        } else if (deliveries.equals("tq")) {
            System.out.println("TQ CALLEDDDDDDDD ELSEEEEEEEEEEEEEEEE");
            uploadedCount = 0;
            uploadedCountReturns = 0;
            uploadedCountpayments = 0;
            uploadedTruckQty = 0;
            uploadedTakeOrderQty = 0;
            mHandler.removeCallbacks(mRunnable);
            synchronized (this) {
                if (alertDialogBuilder1 != null) {
                    alertDialog1.dismiss();
                    alertDialogBuilder1 = null;
                }
            }
            synchronized (this) {
                showCustomValidationAlertForSync(TripSheetView.this, "pto");
            }
        }

        if (uploadedCount11 > 0 && deliveries.equals("pto")) {
            System.out.println("PTO CALLEDDDDDDDD IFFFFFFFFFFFFFFF");
            mHandler.removeCallbacks(mRunnable);
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    ArrayList<String> pendingOrderAgents = mDBHelper.fetchAllPendingTakeOrderAgentIds();
                    fetchCountFromDB(pendingOrderAgents.size(), "pto");
                }
            };
            mHandler.postDelayed(mRunnable, 1000);
        } else if (deliveries.equals("pto")) {
            System.out.println("PTO CALLEDDDDDDDD ELSEEEEEEEEEEEEEEEE");
            uploadedCount = 0;
            uploadedCountReturns = 0;
            uploadedCountpayments = 0;
            uploadedTruckQty = 0;
            uploadedTakeOrderQty = 0;
            mHandler.removeCallbacks(mRunnable);
            synchronized (this) {
                if (alertDialogBuilder1 != null) {
                    alertDialog1.dismiss();
                    alertDialogBuilder1 = null;
                }
            }
            synchronized (this) {
                showCustomValidationAlertForSync(TripSheetView.this, "deliveries");
            }
        }

        if (uploadedCount11 > 0 && deliveries.equals("deliveries")) {
            System.out.println("D CALLEDDDDDDDD IFFFFFFFFFFFFFFF");
            mHandler.removeCallbacks(mRunnable);
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    ArrayList<String> unUploadedDeliveriesTripSheetIdsC = mDBHelper.fetchUnUploadedUniqueDeliveryTripSheetIds(mTripSheetId);
                    fetchCountFromDB(unUploadedDeliveriesTripSheetIdsC.size(), "deliveries");
                }
            };
            mHandler.postDelayed(mRunnable, 1000);
        } else if (deliveries.equals("deliveries")) {
            System.out.println("D CALLEDDDDDDDD ELSEEEEEEEEEEEEEEEE");
            uploadedCount = 0;
            uploadedCountReturns = 0;
            uploadedCountpayments = 0;
            uploadedTruckQty = 0;
            uploadedTakeOrderQty = 0;
            mHandler.removeCallbacks(mRunnable);
            synchronized (this) {
                if (alertDialogBuilder1 != null) {
                    alertDialog1.dismiss();
                    alertDialogBuilder1 = null;
                }
            }
            synchronized (this) {
                showCustomValidationAlertForSync(TripSheetView.this, "returns");
            }
        }

        // RETURNS
        if (uploadedCount11 > 0 && deliveries.equals("returns")) {
            System.out.println("R CALLEDDDDDDDD IFFFFFFFFFFFFFFFF");
            mHandler.removeCallbacks(mRunnable);
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    ArrayList<String> unUploadedDeliveriesTripSheetIdsC = mDBHelper.fetchUnUploadedUniqueReturnsTripSheetIds(mTripSheetId);
                    fetchCountFromDB(unUploadedDeliveriesTripSheetIdsC.size(), "returns");
                }
            };
            mHandler.postDelayed(mRunnable, 1000);
        } else if (deliveries.equals("returns")) {
            System.out.println("R CALLEDDDDDDDD ELSEEEEEEEEEEEEEEEEEEE");
            uploadedCount = 0;
            uploadedCountReturns = 0;
            uploadedCountpayments = 0;
            uploadedTruckQty = 0;
            uploadedTakeOrderQty = 0;
            mHandler.removeCallbacks(mRunnable);
            synchronized (this) {
                if (alertDialogBuilder1 != null) {
                    alertDialog1.dismiss();
                    alertDialogBuilder1 = null;
                }
            }
            synchronized (this) {
                showCustomValidationAlertForSync(TripSheetView.this, "payments");
            }
        }

        if (uploadedCount11 > 0 && deliveries.equals("payments")) {
            System.out.println("P CALLEDDDDDDDD IFFFFFFFFFFFFFFFF");
            mHandler.removeCallbacks(mRunnable);
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    ArrayList<PaymentsBean> paymentsBeanArrayList = mDBHelper.fetchAllTripsheetsPaymentsList(mTripSheetId);
                    fetchCountFromDB(paymentsBeanArrayList.size(), "payments");
                }
            };
            mHandler.postDelayed(mRunnable, 1000);
        } else if (deliveries.equals("payments")) {
            System.out.println("P CALLEDDDDDDDD ELSEEEEEEEEEEEEEE");
            uploadedCount = 0;
            uploadedCountReturns = 0;
            uploadedCountpayments = 0;
            uploadedTruckQty = 0;
            uploadedTakeOrderQty = 0;
            mHandler.removeCallbacks(mRunnable);
            synchronized (this) {
                if (alertDialogBuilder1 != null) {
                    alertDialog1.dismiss();
                    alertDialogBuilder1 = null;
                }
            }
            synchronized (this) {
                showCustomValidationAlertForSync(TripSheetView.this, "down");
            }
        }
    }

    public void showAlertDialog1(Context context, String title, String message) {
        try {
            if (alertDialogBuilder1 != null) {
                alertDialog1.dismiss();
                alertDialogBuilder1 = null;
            }
            android.support.v7.app.AlertDialog alertDialog = null;
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    isSyncClicked = false;
                }
            });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
