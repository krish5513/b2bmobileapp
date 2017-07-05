package com.rightclickit.b2bsaleon.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.beanclass.TDCCustomer;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Retailers_AddActivity extends AppCompatActivity {
    private Context applicationContext, activityContext;
    private MMSharedPreferences mmSharedPreferences;

    private EditText retailer_name, mobile_no, business_name, address;
    private ImageView shop_image;
    private DBHelper mDBHelper;

    private static final int REQUEST_CODE_TAKE_PHOTO = 1;
    private static final int REQUEST_CODE_IMAGE_SELECTION_FROM_GALLERY = 2;
    private String shop_image_path;

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

            mDBHelper = new DBHelper(activityContext);

            retailer_name = (EditText) findViewById(R.id.retailer_name);
            mobile_no = (EditText) findViewById(R.id.mobile_no);
            business_name = (EditText) findViewById(R.id.business_name);
            address = (EditText) findViewById(R.id.address);
            shop_image = (ImageView) findViewById(R.id.shop_image);

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
        menu.findItem(R.id.autorenew).setVisible(true);

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
            String name = retailer_name.getText().toString().trim();
            String mobileNo = mobile_no.getText().toString().trim();
            String businessName = business_name.getText().toString().trim();
            String retailerAddress = address.getText().toString().trim();

            boolean cancel = false;
            View focusView = null;

            if (name.isEmpty()) {
                retailer_name.setError("Please enter person name.");
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
                business_name.setError("Please enter business name.");
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNewRetailer(String name, String mobileNo, String businessName, String retailerAddress) {
        try {
            TDCCustomer customer = new TDCCustomer();
            customer.setCustomerType(1);
            customer.setName(name);
            customer.setMobileNo(mobileNo);
            customer.setBusinessName(businessName);
            customer.setAddress(retailerAddress);
            customer.setLatLong("");
            customer.setShopImage(shop_image_path);
            customer.setIsShopImageUploaded(0);

            long customerId = mDBHelper.insertIntoTDCCustomers(customer);

            if (customerId == -1)
                Toast.makeText(activityContext, "An error occurred while adding new retailer.", Toast.LENGTH_LONG).show();
            else {
                retailer_name.setText("");
                mobile_no.setText("");
                business_name.setText("");
                address.setText("");

                Toast.makeText(activityContext, "New retailer added successfully.", Toast.LENGTH_LONG).show();

                onBackPressed();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
