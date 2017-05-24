package com.rightclickit.b2bsaleon.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.beanclass.ProductsObj;
import com.rightclickit.b2bsaleon.database.DBHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.rightclickit.b2bsaleon.R.id.mrp;

public class Product_Add_Activity extends AppCompatActivity {

    public  int year,month,day,hour,minute;
    // declare  the variables to Show/Set the date and time when Time and  Date Picker Dialog first appears
    private int mYear, mMonth, mDay,mHour,mMinute;

    double mrp_double,sp_double;
    Bitmap bitmapLogo;

    DBHelper dbHelper;

    TextView save;
    EditText materialCode;
    EditText materialDisc;
    Spinner productReturn;
    Spinner taxType;
    Spinner materialUnit;
    EditText taxTextField;
    EditText materialMRP;
    EditText materialMOQ;
    EditText materialTAX;
    EditText materialSP;
    EditText materialValidFrom;
    EditText materialValidTo;
    ImageButton addImage;
    ImageView imageview;
    private String userChoosenTask;
    private static final int REQUEST_CAMERA = 2;
    private static final int SELECT_FILE = 1;
    final Calendar c = Calendar.getInstance();
    boolean validFrom;
    boolean validTo;
    boolean saveBool = false;
    String produtRetunable="0";
    String imagePath;
    FragmentManager fragmentManager;
    String productId=null;



    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        // the callback received when the user "sets" the Date in the DatePickerDialog
        @SuppressWarnings("deprecation")
        public void onDateSet(DatePicker view, int yearSelected, int monthOfYear, int dayOfMonth) {
            year = yearSelected;
            month = monthOfYear+1;
            day = dayOfMonth;
            String selectedDate = String.format("%02d",day)+"-"+String.format("%02d",month)+"-"+year;
            validation(selectedDate);
            // Toast.makeText(getApplicationContext(), "day "+day+" month  "+month+" year  "+year, Toast.LENGTH_LONG).show();
        }
    };


    private void validation(String userdate) {
        currentDate();
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-hh-mm");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date pickerdate = null;
        Date systemdate  = null;
        String str = "";
        try {
            pickerdate = formatter.parse(userdate);
            if(validFrom)
                str = mDay+"-"+(mMonth+1)+"-"+mYear;
            else if(validTo)
                str = materialValidFrom.getText().toString().trim();
            Log.i("String",str);
            systemdate = formatter.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("valid From : "+validFrom,"Valid To :"+validTo);
        Log.i("pickerdate",pickerdate+"");
        Log.i("systemdate",systemdate+"");

        if(systemdate!=null){
            if(pickerdate.after(systemdate)){
                if (validFrom)
                    materialValidFrom.setText(formatter.format(pickerdate));
                else if (validTo)
                    materialValidTo.setText(formatter.format(pickerdate));
            } else if(pickerdate.before(systemdate)){
                if(validFrom) {
                    showAlert("ValidFrom date should be greater than Current date");
                } else if(validTo){
                    showAlert("ValidTo date should be greater or equal to ValidFrom date");
                }
            } else if(pickerdate.equals(systemdate)){
                if (validFrom)
                    materialValidFrom.setText(formatter.format(pickerdate));
                else if (validTo)
                    materialValidTo.setText(formatter.format(pickerdate));
            }
        }else{
            showAlert("Please select ValidFrom date");
        }

      /*  if(saveBool){
            validateProductDetails();
        }*/

    }

    private void currentDate() {
        // Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
    }

    private void showAlert(String message){
        new AlertDialog.Builder(getApplication())
                .setTitle("Alert!")
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        dialog.dismiss();
                        // showDialog(DATE_DIALOG_ID);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__add_);

        this.getSupportActionBar().setTitle("ADD PRODUCT");
        this.getSupportActionBar().setSubtitle(null);
        this.getSupportActionBar().setLogo(R.drawable.ic_shopping_cart_white_24dp);
        // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);


        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        final Bundle bundle=getIntent().getExtras();

        addImage = (ImageButton) findViewById(R.id.imageview1);
        imageview = (ImageView) findViewById(R.id.image);
        taxTextField = (EditText) findViewById(R.id.taxInput);
        materialCode = (EditText) findViewById(R.id.material_code);
        materialDisc = (EditText) findViewById(R.id.material_disc);
        materialUnit = (Spinner) findViewById(R.id.material_type);
        taxType = (Spinner) findViewById(R.id.tax_type);

        materialMRP = (EditText) findViewById(mrp);
        materialMOQ = (EditText) findViewById(R.id.moq);
        materialTAX = (EditText) findViewById(R.id.taxInput);
        materialSP = (EditText) findViewById(R.id.sp);

        materialValidFrom = (EditText) findViewById(R.id.validFrom);
        materialValidTo = (EditText) findViewById(R.id.validTo);
        save = (TextView) findViewById(R.id.tv_save);

        dbHelper=new DBHelper(getApplicationContext());


        materialValidFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validFrom = true;
                validTo = false;
                saveBool = false;
                new DatePickerDialog(Product_Add_Activity.this, date, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        materialValidTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validFrom = false;
                validTo = true;
                saveBool = false;
                new DatePickerDialog(Product_Add_Activity.this, date, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        List<String> categories = new ArrayList<String>();
        categories.add("Select UOM");
        categories.add("Ltrs");
        categories.add("Each");
        categories.add("Kg");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Product_Add_Activity.this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        materialUnit.setAdapter(dataAdapter);
        List<String> categories1 = new ArrayList<String>();
        categories1.add("Product Returnable");
        categories1.add("YES");
        categories1.add("NO");
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(Product_Add_Activity.this, android.R.layout.simple_spinner_item, categories1);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        List<String> categories3 = new ArrayList<String>();
        categories3.add("Select Tax");
        categories3.add("TAX");
        categories3.add("VAT");
        categories3.add("CST");
        categories3.add("GST");
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(Product_Add_Activity.this, android.R.layout.simple_spinner_item, categories3);

        // Drop down layout style - list view with radio button
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        materialUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Log.e("drop down selected", item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        taxType.setAdapter(dataAdapter3);

        taxType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Log.e("Tax drop down ", item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        productReturn=(Spinner) findViewById(R.id.productReturn);
        productReturn.setAdapter(dataAdapter1);
        productReturn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Log.e("prod seleted", item);
                if(item.equals("YES")){
                    produtRetunable="1";
                }
                else{
                    produtRetunable="0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validFrom=false;
                validTo=true;
                saveBool = true;
                validateProductDetails(bundle);
            }
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


    }


    private void validateProductDetails(final Bundle bundle) {
        if (materialMRP.getText().toString().length() > 0) {
            mrp_double = Double.valueOf((materialMRP.getText().toString().trim()));
            Log.i("mrp", mrp_double + "");
        }
        if (materialSP.getText().toString().length() > 0) {
            sp_double = Double.valueOf((materialSP.getText().toString().trim()));
        }
        /*else if (materialSP.getText().toString().length()>mrp_double) {
           sp_double = Double.parseDouble((materialSP.getText().toString().trim()));
           Log.i("sp",sp_double+"");
       }*/
        String str_materialCode = materialCode.getText().toString();
        String str_materialDes = materialDisc.getText().toString();
        String str_tax = taxTextField.getText().toString();
        String str_moq = materialMOQ.getText().toString();
        String str_mrp = materialMRP.getText().toString();
        String str_sp = materialSP.getText().toString();
        String valid_From = materialValidFrom.getText().toString();
        String valid_To = materialValidTo.getText().toString();

        String product_return = "Product Returnable";
        String select_UOM = "Select UOM";
        String select_Tax = "Select Tax";

        String selectedView_product = productReturn.getSelectedItem().toString();
        String selectedView_UOM = materialUnit.getSelectedItem().toString();
        String selectedView_Tax = taxType.getSelectedItem().toString();

        Log.e("selecter item", selectedView_product + "");
        TextView product = (TextView) productReturn.getSelectedView();
        TextView UOM = (TextView) materialUnit.getSelectedView();
        TextView Tax = (TextView) taxType.getSelectedView();

        if (str_materialCode.length() == 0 || str_materialCode.length() == ' ') {
            materialCode.setError("Please enter material code");
            //
        } else if (str_materialDes.length() == 0 || str_materialDes.length() == ' ') {
            materialCode.setError(null);
            materialDisc.setError("Please enter material description");

        } else if (selectedView_UOM.equals(select_UOM)) {
            materialDisc.setError(null);

            UOM.setError("please select input value");

        } else if (selectedView_product.equals(product_return)) {
            UOM.setError(null);

            product.setError("please select input value");

        } else if (selectedView_Tax.equals(select_Tax)) {
            product.setError(null);
            Tax.setError("please select input value");

        } else if (str_tax.length() == 0 || str_tax.length() == ' ') {
            //  TextView errorText = (TextView)selectedView;
            // errorText.setError(null);
            Tax.setError(null);
            taxTextField.setError("Please enter  Tax Percent");


        } else if (str_moq.length() == 0 || str_moq.length() == ' ') {
            taxTextField.setError(null);
            materialMOQ.setError("Please enter MOQ");

        } else if (str_mrp.length() == 0 || str_mrp.length() == ' ') {
            materialMOQ.setError(null);
            materialMRP.setError("Please enter MRP");

        } else if (str_sp.length() == 0 || str_sp.length() == ' ' || sp_double > mrp_double) {
            materialMRP.setError(null);
            materialSP.setError("Selling price should not be greater than MRP");

        } else if (valid_From.length() == 0 || valid_From.length() == ' ') {
            materialSP.setError(null);
            materialValidFrom.setError("Please Select From Date");

        } else if (valid_To.length() == 0 || valid_To.length() == ' ') {
            materialValidFrom.setError(null);
            materialValidTo.setError("Please select To Date");

        }
        else {
            materialValidTo.setError(null);
            new AlertDialog.Builder(getApplicationContext())
                    .setMessage("Do you want to save the data? Please confirm.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, int which) {
                            final String prodImage;
                            if (bitmapLogo == null)
                                prodImage = "NoImage";
                            else
                                prodImage = imagePath;
                            Log.e("prod return" ,produtRetunable);
                            if(materialTAX.getText().toString().equals("")||materialTAX.getText().toString().equals(null))
                                materialTAX.setText("0");
                            if(bundle ==null &&productId ==null)
                                dbHelper.addProducts(new ProductsObj(materialCode.getText().toString(), materialDisc.getText().toString(),prodImage, materialMRP.getText().toString(),materialSP.getText().toString(),"1", materialUnit.getSelectedItem().toString(), materialValidFrom.getText().toString(), materialValidTo.getText().toString(), produtRetunable,materialMOQ.getText().toString(),materialTAX.getText().toString(),taxType.getSelectedItem().toString()));
                            else
                                dbHelper.updateProduct(new ProductsObj(materialCode.getText().toString(), materialDisc.getText().toString(),prodImage, materialMRP.getText().toString(),materialSP.getText().toString(),"1", materialUnit.getSelectedItem().toString(), materialValidFrom.getText().toString(), materialValidTo.getText().toString(), produtRetunable,materialMOQ.getText().toString(),materialTAX.getText().toString(),taxType.getSelectedItem().toString()),productId);
                            Log.e("completed", "dssdf");
                            dialog.dismiss();
                            /*Products_Fragment fragment = new Products_Fragment();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.flContent, fragment);
                            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentTransaction.commit();*/
                            Intent productsactivity=new Intent(Product_Add_Activity.this,Products_Activity.class);
                            startActivity(productsactivity);
                            finish();

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                            //   checkBox1.setChecked(true);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
               /* View selectedView = productReturn.getSelectedView();
                if (selectedView != null && selectedView instanceof TextView) {
                    TextView selectedTextView = (TextView) selectedView;
                    if (!valid) {
                        String errorString = selectedTextView.getResources().getString(mErrorStringResource);
                        selectedTextView.setError(errorString);
                    }
                    else {
                        selectedTextView.setError(null);
                    }
                }*/

    }




    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if (validFrom)
            materialValidFrom.setText(sdf.format(c.getTime()));
        else if (validTo)
            materialValidTo.setText(sdf.format(c.getTime()));

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


        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getApplicationContext());
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public static class Utility {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static boolean checkPermission(final Context context) {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
//code for deny
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("requestcode", String.valueOf(requestCode));
        Log.e("response", String.valueOf(resultCode));

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                imageview.setVisibility(View.VISIBLE);
                onSelectFromGalleryResult(data);
            }
//
            else if (requestCode == REQUEST_CAMERA) {
                imageview.setVisibility(View.VISIBLE);
                onCaptureImageResult(data);
            }

        }

    }

    /**
     * if choosen select from gallery option
     *
     * @param data
     */
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bitmapLogo = Bitmap.createScaledBitmap(bm, 120, 120, false);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmapLogo.compress(Bitmap.CompressFormat.PNG, 50, bytes);
        File destination = new File(Environment.getExternalStorageDirectory() + "/B2BSaleImages/",
                System.currentTimeMillis() + ".png");
        imagePath=destination.getAbsolutePath();

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageview.setImageBitmap(bitmapLogo);
    }

    /**
     * if choosen open camera option this method is called
     *
     * @param data
     */
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//        File destination = new File(Environment.getExternalStorageDirectory() + "/B2BSaleImages/",
//                System.currentTimeMillis() + ".jpg");
        bitmapLogo = Bitmap.createScaledBitmap(thumbnail, 120, 120, false);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmapLogo.compress(Bitmap.CompressFormat.PNG, 50, bytes);
        File destination = new File(Environment.getExternalStorageDirectory() + "/B2BSaleImages/",
                System.currentTimeMillis() + ".png");
        imagePath=destination.getAbsolutePath();

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        bitmapLogo = thumbnail;
        imageview.setImageBitmap(bitmapLogo);
    }


}
