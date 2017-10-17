package com.rightclickit.b2bsaleon.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.rightclickit.b2bsaleon.adapters.AgentsAdapter;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.AgentsModel;
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

public class Agents_AddActivity extends AppCompatActivity implements OnMapReadyCallback {

    String str_BusinessName, str_PersonName, str_Mobileno, str_address;

    private GoogleMap googlemap;
    private AgentsAdapter mAgentsAdapter;
    double longitude, latitude;
    TextView save;
    EditText bname;
    EditText pname;

    Bitmap bitmapLogo;
    EditText mobile;
    EditText address;
    ImageButton addImage;
    ImageView imageview;
    String imagePath;
    DBHelper db;
    SQLiteDatabase sqlDB;
    private static final int ACTION_TAKE_PHOTO_A = 1;
    private static final int ACTION_TAKE_GALLERY_PIC_A = 2;
    Location presentLocation = null;
    private AgentsActivity activity;
    AgentsModel agentsmodel;
    Spinner paymentTypeSpinner;
    private Context applicationContext, activityContext;
    private ArrayList<AgentsBean> mAgentsBeansList = new ArrayList<AgentsBean>();
    private JSONArray routeCodesArray;
    String selected_val;
    ArrayList<String> idsArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agents__add);
        activityContext = Agents_AddActivity.this;
        this.getSupportActionBar().setTitle("ADD AGENT");
        this.getSupportActionBar().setSubtitle(null);
        this.getSupportActionBar().setLogo(R.drawable.customers_white_24);
        // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);


        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);


        // addImage=(ImageButton) findViewById(R.id.imageview1);
        imageview = (ImageView) findViewById(R.id.shop_image);
        bname = (EditText) findViewById(R.id.business_name);

        pname = (EditText) findViewById(R.id.person_name);
        mobile = (EditText) findViewById(R.id.mobile_no);
        address = (EditText) findViewById(R.id.uid_no);
        save = (TextView) findViewById(R.id.ts_dispatch_save);
        paymentTypeSpinner = (Spinner) findViewById(R.id.paymentTypeSpinner);
        Bundle extras = getIntent().getExtras();
        //  Bitmap avatarbmp = (Bitmap) extras.getParcelable("avatar");

        //  imageview.setImageBitmap(avatarbmp);
        db = new DBHelper(getApplicationContext());
        agentsmodel = new AgentsModel(activityContext, this);


        System.out.println("STAKE ID IS::: " + db.getStakeTypeIdByStakeType("2"));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(this);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


        HashMap<String, String> userRouteIds = db.getUserRouteIds();


        try {
            routeCodesArray = new JSONObject(userRouteIds.get("route_ids")).getJSONArray("routeArray");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<String> stringArray = new ArrayList<String>();

        stringArray.add(0,"Select Card");
        for (int i = 1, count = routeCodesArray.length(); i <= count; i++) {
            List<String> routesDataList = null;
            try {
                idsArray.add(routeCodesArray.get(i-1).toString());
                routesDataList = db.getRouteDataByRouteId(routeCodesArray.get(i-1).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }



            stringArray.add(i, routesDataList.get(1).toString());
        }



        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        stringArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        //paymentTypeSpinner.setPrompt("Select routecode");
        paymentTypeSpinner.setAdapter(spinnerArrayAdapter);

        paymentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i==0){

                }else{
                    selected_val = idsArray.get(i-1).toString();
                    System.out.println("ROUTE JSON OBJ 22:: " + selected_val.toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                save.startAnimation(animation1);

                validateCustomerDetails();
            }
        });
    }


    private void validateCustomerDetails() {
        str_BusinessName = bname.getText().toString();
        Log.i("bname", str_BusinessName);
        str_PersonName = pname.getText().toString();
        str_Mobileno = mobile.getText().toString();
        str_address = address.getText().toString();
        if (str_BusinessName.length() == 0 || str_BusinessName.length() == ' ') {
            bname.setError("Please enter BusinessName");
            Toast.makeText(getApplicationContext(), "Please enter BusinessName", Toast.LENGTH_SHORT).show();
            //
        } else if (str_PersonName.length() == 0 || str_PersonName.length() == ' ') {
            bname.setError(null);
            pname.setError("Please enter PersonName");
            Toast.makeText(getApplicationContext(), "Please enter PersonName", Toast.LENGTH_SHORT).show();

        } else if (str_Mobileno.length() == 0 || str_Mobileno.length() == ' ' || str_Mobileno.length() != 10) {
            pname.setError(null);
            mobile.setError("Please enter  10 digit mobileno");
            Toast.makeText(getApplicationContext(), "Please enter  10 digit mobileno", Toast.LENGTH_SHORT).show();

        } /*else if (str_address.length() == 0 || str_address.length() == ' ') {
                                            mobile.setError(null);
                                            address.setError("Please enter address");
                                            Toast.makeText(getApplicationContext(), "Please enter address", Toast.LENGTH_SHORT).show();

                                        }
*/ else {
            bname.setError(null);
            pname.setError(null);
            mobile.setError(null);
            address.setError(null);

            HashMap<String, String> userMapData = db.getUsersData();
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
               // agentsBean.setmAgentRouteId(routesArray.toString());
                agentsBean.setmAgentRouteId(selected_val);
               // agentsBean.setmSelectedRouteName(selected_val);
                mAgentsBeansList.add(agentsBean);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // db.insertAgentDetails(mAgentsBeansList);
            synchronized (this) {
                if (new NetworkConnectionDetector(Agents_AddActivity.this).isNetworkConnected()) {
                    //agentsmodel.customerAdd(str_BusinessName, str_PersonName, str_Mobileno, stakeholderid, userid, "", "123456789", "", "", "", "IA", "N", str_address, String.valueOf(latitude), String.valueOf(longitude), timeStamp, "", "", "", "", "");
                    agentsmodel.customerAdd(mAgentsBeansList, db.getStakeTypeIdByStakeType("2"));
                }
                db.insertAgentDetails(mAgentsBeansList, userid);
                Toast.makeText(getApplicationContext(), "Details saved successfully", Toast.LENGTH_SHORT).show();
                synchronized (this) {
                    Intent i = new Intent(Agents_AddActivity.this, AgentsActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }
    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Agents_AddActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, ACTION_TAKE_PHOTO_A);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, ACTION_TAKE_GALLERY_PIC_A);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ACTION_TAKE_PHOTO_A) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    BitmapDrawable d = new BitmapDrawable(this.getResources(), bitmap);
                    imageview.setBackgroundDrawable(null);
                    imageview.setBackgroundDrawable(d);

                    //  mPicImage.setImageBitmap(bitmap);

                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
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
            } else if (requestCode == ACTION_TAKE_GALLERY_PIC_A) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                System.out.println("path of image from gallery......******************........." + picturePath + "");
                BitmapDrawable d = new BitmapDrawable(this.getResources(), thumbnail);
                imageview.setBackgroundDrawable(null);
                imageview.setBackgroundDrawable(d);
                //mPicImage.setImageBitmap(thumbnail);
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
        googlemap.setOnMyLocationChangeListener(myLocationChangeListener());


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
                googlemap.clear();
                marker = googlemap.addMarker(new MarkerOptions().position(loc));
                googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
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


    public void callBackDataFromAsyncTask(String laddress) {
        address.setText(laddress);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        menu.findItem(R.id.notifications).setVisible(false);
        menu.findItem(R.id.settings).setVisible(false);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.Add).setVisible(false);
        menu.findItem(R.id.autorenew).setVisible(true);
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
    public void onMapReady(GoogleMap map) {
        googlemap = map;

        replaceMapFragment();
    }
}
