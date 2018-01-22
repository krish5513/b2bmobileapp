package com.rightclickit.b2bsaleon.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.beanclass.TDCCustomer;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.services.SyncTDCCustomersService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Retailers_AddActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Context applicationContext, activityContext;
    private MMSharedPreferences mmSharedPreferences;

    private ScrollView retailer_add_scrollview;
    private EditText retailer_name, mobile_no, business_name, address;
    private GoogleMap googleMap;
    private ImageView shop_image;
    private LinearLayout retailer_add_footer;

    private static final int REQUEST_CODE_TAKE_PHOTO = 1;
    private static final int REQUEST_CODE_IMAGE_SELECTION_FROM_GALLERY = 2;

    private String name, mobileNo, businessName, retailerAddress, shop_image_path = "";
    private double longitude, latitude;

    private DBHelper mDBHelper;
    private TDCCustomer customer;
    private boolean isCameFromRetailersList = false;
    Spinner paymentTypeSpinner;
    private String mUserId = "", mRegionName = "", mOfficeName = "", mRouteCode = "";
    private JSONArray routeCodesArray;
    String selected_val;
    ArrayList<String> idsArray = new ArrayList<String>();
    TextView update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailers_add);

        try {
            applicationContext = getApplicationContext();
            activityContext = Retailers_AddActivity.this;

            final ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;

            actionBar.setTitle("ADD RETAILER");
            actionBar.setSubtitle(null);
            actionBar.setLogo(R.drawable.ic_store_white);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

            mmSharedPreferences = new MMSharedPreferences(this);

            mDBHelper = new DBHelper(activityContext);

            retailer_add_scrollview = (ScrollView) findViewById(R.id.retailer_add_scrollview);
            retailer_name = (EditText) findViewById(R.id.retailer_business_name); // Business Name
            mobile_no = (EditText) findViewById(R.id.retailer_mobile_no);
            business_name = (EditText) findViewById(R.id.retailer_name); // Person name
            address = (EditText) findViewById(R.id.retailer_address);
            paymentTypeSpinner = (Spinner) findViewById(R.id.paymentTypeSpinner);
            shop_image = (ImageView) findViewById(R.id.retailer_shop_image);
            retailer_add_footer = (LinearLayout) findViewById(R.id.retailer_add_footer);
            update=(TextView) findViewById(R.id.ts_dispatch_save);

            // LOCATION DETAILS
            try {


                latitude = Double.parseDouble(mmSharedPreferences.getString("curLat"));
                longitude = Double.parseDouble(mmSharedPreferences.getString("curLong"));
            }catch (Exception e){
                e.printStackTrace();
            }

            System.out.println("LAT::: " + latitude);
            System.out.println("LONG::: " + longitude);


            HashMap<String, String> userRouteIds = mDBHelper.getUserRouteIds();
            HashMap<String, String> userMapData = mDBHelper.getUsersData();
            mUserId = userMapData.get("user_id");
            try {
                routeCodesArray = new JSONObject(userRouteIds.get("route_ids")).getJSONArray("routeArray");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("ROUTE CODE ARRAY:: " + routeCodesArray + "...length..." + routeCodesArray.length());

            final ArrayList<String> stringArray = new ArrayList<String>();
            final HashMap<Integer, String> map = new HashMap<>();
            final HashMap<Integer, String> idMap = new HashMap<>();
            stringArray.add("Select Routecode");
            map.put(0, "Select Routecode");
            for (int i = 1; i <= routeCodesArray.length(); i++) {
                List<String> routesDataList = null;
                try {
                    //System.out.println("idsArray :: "+i+"..." + routeCodesArray.get(i - 1).toString());
                    idsArray.add(routeCodesArray.get(i - 1).toString());
                    idMap.put(i - 1, routeCodesArray.get(i - 1).toString());
                    routesDataList = mDBHelper.getRouteDataByRouteId(routeCodesArray.get(i - 1).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //System.out.println("idMap :: " + idMap.toString());
                //System.out.println("routesDataList :: " + routesDataList.toString());

                if (routesDataList.size() > 0) {
                    //System.out.println("routesDataList.get(1).toString() :: " + routesDataList.get(1).toString());
                    stringArray.add(routesDataList.get(1).toString());
                    map.put(i, routesDataList.get(1).toString());
                }
            }
            System.out.println("stringArray :: " + map.toString());
            System.out.println("stringArray :: " + idMap.toString());

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringArray); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //paymentTypeSpinner.setPrompt("Select routecode");
            paymentTypeSpinner.setAdapter(spinnerArrayAdapter);

            paymentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //Log.i("postion",i+"");
                    if (i == 0) {

                    } else {
                        String value = paymentTypeSpinner.getSelectedItem().toString();
                        System.out.println("ROUTE value:: " + value);
                        int key = 0;
                        for (Map.Entry entry : map.entrySet()) {
                            if (value.equals(entry.getValue())) {
                                key = (int) entry.getKey();
                                break; //breaking because its one to one map
                            }
                        }

                        System.out.println("ROUTE JSON key:: " + key);
                        selected_val = idMap.get(key - 1).toString();
                        System.out.println("ROUTE JSON OBJ 22:: " + selected_val.toString());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.retailer_map_fragment);
            mapFragment.getMapAsync(this);

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                customer = (TDCCustomer) bundle.getSerializable(Constants.BUNDLE_TDC_CUSTOMER);

                isCameFromRetailersList = true;

                actionBar.setTitle("RETAILER INFO");
                update.setText("Update");

                ViewGroup.MarginLayoutParams scrollViewLp = (ViewGroup.MarginLayoutParams) retailer_add_scrollview.getLayoutParams();
                //scrollViewLp.bottomMargin = 0;
                retailer_add_scrollview.setLayoutParams(scrollViewLp);

              // retailer_add_footer.setVisibility(View.GONE);

                updateUIWithBundleValues(customer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUIWithBundleValues(TDCCustomer retailerObj) {
        try {
            retailer_name.setText(retailerObj.getBusinessName());
            mobile_no.setText(retailerObj.getMobileNo());
            business_name.setText(retailerObj.getName());
            address.setText(retailerObj.getAddress());

            latitude = retailerObj.getLatitude().isEmpty() ? 0 : Double.parseDouble(retailerObj.getLatitude());
            longitude = retailerObj.getLongitude().isEmpty() ? 0 : Double.parseDouble(retailerObj.getLongitude());

            shop_image_path = retailerObj.getShopImage();

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (shop_image_path != "") {
                        File imgFile = new File(shop_image_path);

                        if (imgFile.exists()) {
                            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);

                            shop_image.setBackground(null);
                            shop_image.setBackground(bitmapDrawable);
                        }
                    }
                }
            });

            retailer_name.setEnabled(false);
            mobile_no.setEnabled(false);
            business_name.setEnabled(false);
            address.setEnabled(false);
            shop_image.setEnabled(false);

        } catch (Exception e) {
            e.printStackTrace();
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
        super.onBackPressed();
        Intent intent = new Intent(this, RetailersActivity.class);
        startActivity(intent);
        finish();
    }

    /***
     * Method to select the image from camera or gallery..
     */
    public void selectShopImage(View view) {
        if (!isCameFromRetailersList) {
            final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Take Photo")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
                        }
                    } else if (options[item].equals("Choose from Gallery")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, REQUEST_CODE_IMAGE_SELECTION_FROM_GALLERY);

                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
                    try {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        BitmapDrawable d = new BitmapDrawable(this.getResources(), bitmap);
                        shop_image.setBackground(null);
                        shop_image.setBackground(d);

                        File shopImagesDir = new File(Constants.shopImagesPath);
                        if (!shopImagesDir.exists()) {
                            shopImagesDir.mkdirs();
                        }

                        String fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
                        File outputFile = new File(shopImagesDir, fileName);

                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
                            fileOutputStream.flush();
                            fileOutputStream.close();

                            shop_image_path = outputFile.getAbsolutePath();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (requestCode == REQUEST_CODE_IMAGE_SELECTION_FROM_GALLERY) {
                    Uri selectedImage = data.getData();
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();

                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();

                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    BitmapDrawable d = new BitmapDrawable(this.getResources(), thumbnail);
                    shop_image.setBackground(null);
                    shop_image.setBackground(d);
                    shop_image_path = picturePath;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveNewRetailer(View view) {
        try {
           // if (!isCameFromRetailersList) {
                name = retailer_name.getText().toString().trim();
                mobileNo = mobile_no.getText().toString().trim();
                businessName = business_name.getText().toString().trim();
                retailerAddress = address.getText().toString().trim();

                boolean cancel = false;
                View focusView = null;

                if (name.isEmpty()) {
                    retailer_name.setError("Please enter business name.");
                    focusView = retailer_name;
                    cancel = true;
                } else if (mobileNo.isEmpty()) {
                    mobile_no.setError("Please enter mobile no.");
                    focusView = mobile_no;
                    cancel = true;
                } else if (!mobileNo.isEmpty() && mobileNo.length() < 10) {
                    mobile_no.setError("Please enter valid mobile no.");
                    focusView = mobile_no;
                    cancel = true;
                } else if (businessName.isEmpty()) {
                    business_name.setError("Please enter person name.");
                    focusView = business_name;
                    cancel = true;
                } else if (retailerAddress.isEmpty()) {
                    address.setError("Please enter address.");
                    focusView = address;
                    cancel = true;
                }

                if (cancel) {
                    focusView.requestFocus();
                } else {
                    addNewRetailer(name, mobileNo, businessName, retailerAddress);
                }
            }
        //}
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNewRetailer(String name, String mobileNo, String businessName, String retailerAddress) {
        try {
            int dbCount = mDBHelper.getTDCCustomersTableCount();
            int fdbc = dbCount + 1;
            HashMap<String, String> userMapData = mDBHelper.getUsersData();
            String retailerCode = userMapData.get("user_code") + "-R" + fdbc;

            customer = new TDCCustomer();
            customer.setUserId(""); // later we will update this value by fetching from service.
            customer.setCustomerType(1);
            customer.setName(businessName); // Person Name
            customer.setMobileNo(mobileNo);
            customer.setBusinessName(name); // Business Name
            customer.setAddress(retailerAddress);
            customer.setRoutecode(selected_val);
            customer.setCode(retailerCode);
            customer.setIsUploasStatus("0");


            if (latitude > 0)
                customer.setLatitude(String.valueOf(latitude));
            else
                customer.setLatitude("");

            if (longitude > 0)
                customer.setLongitude(String.valueOf(longitude));
            else
                customer.setLongitude("");

            customer.setShopImage(shop_image_path);
            customer.setIsShopImageUploaded(0);
            long customerId = mDBHelper.insertIntoTDCCustomers(customer, mUserId);


            if (customerId == -1)
                Toast.makeText(activityContext, "An error occurred while adding new retailer.", Toast.LENGTH_LONG).show();
            else {
                retailer_name.setText("");
                mobile_no.setText("");
                business_name.setText("");
                address.setText("");

                Toast.makeText(activityContext, "New retailer added successfully.", Toast.LENGTH_LONG).show();

                if (new NetworkConnectionDetector(activityContext).isNetworkConnected()) {
                    Intent syncTDCCustomersServiceIntent = new Intent(activityContext, SyncTDCCustomersService.class);
                    startService(syncTDCCustomersServiceIntent);
                }

                onBackPressed();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            this.googleMap = googleMap;

            if (!isCameFromRetailersList) {
                replaceMapFragment();
            } else {
                LatLng shopLocation;

                if (latitude > 0 && longitude > 0)
                    shopLocation = new LatLng(latitude, longitude);
                else
                    shopLocation = new LatLng(17.3850440, 78.4866710);

                this.googleMap.addMarker(new MarkerOptions().position(shopLocation).title(""));
                this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(shopLocation));

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                this.googleMap.setMyLocationEnabled(true);
                this.googleMap.getUiSettings().setZoomGesturesEnabled(true);
                this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(shopLocation, 16.0f));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void replaceMapFragment() {
        try {
            // Enable Zoom
            googleMap.getUiSettings().setZoomGesturesEnabled(true);

            //set Map TYPE
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            //enable Current location Button
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                // ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                // public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            googleMap.setMyLocationEnabled(true);

            //set "listener" for changing my location
            //googleMap.setOnMyLocationChangeListener(myLocationChangeListener());
            LatLng loc = new LatLng(latitude, longitude);

            Marker marker;
            googleMap.clear();
            marker = googleMap.addMarker(new MarkerOptions().position(loc));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));

            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    LatLng loc = new LatLng(latLng.latitude, latLng.longitude);
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                    Marker marker;
                    googleMap.clear();
                    marker = googleMap.addMarker(new MarkerOptions().position(loc));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener() {

        return new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(final Location location) {

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();

                        googleMap.clear();
                        Marker marker = googleMap.addMarker(new MarkerOptions().position(loc));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                        //  Geocoder geocoder = new Geocoder(activityContext, Locale.getDefault());

                        /*try {
                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

                            if (addresses != null && addresses.size() > 0) {
                                Address returnedAddress = addresses.get(0);

                                String currentAddress;

                                if (returnedAddress.getMaxAddressLineIndex() > 0) {
                                    StringBuilder strReturnedAddress = new StringBuilder("");

                                    for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                                    }

                                    currentAddress = strReturnedAddress.toString();
                                } else {
                                    // Format the first line of address (if available), city, and country name.
                                    currentAddress = String.format("%s, %s", returnedAddress.getLocality(), returnedAddress.getCountryName());
                                }

                                address.setText(currentAddress);
                            } else {
                                address.setText(" ");
                            }
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            address.setText(" ");
                        }*/
                    }
                });
            }
        };
    }
}
