package com.rightclickit.b2bsaleon.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Agents_AddActivity extends AppCompatActivity {

    String str_BusinessName,str_PersonName,str_Mobileno,str_UID;

    TextView save;
    EditText bname;
    EditText pname;
    Bitmap bitmapLogo;
    EditText mobile;
    EditText uid_no;
    ImageButton addImage;
    ImageView imageview;
    String imagePath;

    private static final int ACTION_TAKE_PHOTO_A = 1;
    private static final int ACTION_TAKE_GALLERY_PIC_A = 2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agents__add);

        this.getSupportActionBar().setTitle("ADD CUSTOMER");
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



        addImage=(ImageButton) findViewById(R.id.imageview1);
        imageview=(ImageView) findViewById(R.id.image);
        bname=(EditText) findViewById(R.id.business_name);

        pname=(EditText) findViewById(R.id.person_name);
        mobile=(EditText) findViewById(R.id.mobile_no);
        // uid_no=(EditText) v.findViewById(R.id.uid_no);
        save=(TextView) findViewById(R.id.tv_save);




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                save.startAnimation(animation1);

                validateCustomerDetails();
                SharedPreferences.Editor editor = Agents_AddActivity.this.getSharedPreferences("customerDetails", MODE_PRIVATE).edit();
                editor.putString("customerName", bname.getText().toString());
                editor.putString("personName", pname.getText().toString());
                //  editor.putString("uid", uid_no.getText().toString());
                editor.putString("mobile", mobile.getText().toString());
                editor.commit();


            }

            private void validateCustomerDetails() {
                str_BusinessName=bname.getText().toString();
                str_PersonName=pname.getText().toString();
                str_Mobileno=mobile.getText().toString();
                //  str_UID=uid_no.getText().toString();
                if (str_BusinessName.length() == 0 || str_BusinessName.length() == ' ') {
                    bname.setError("Please enter BusinessName");
                    Toast.makeText(getApplicationContext(),"Please enter BusinessName",Toast.LENGTH_SHORT).show();
                    //
                }
                else if (str_PersonName.length() == 0 || str_PersonName.length() == ' ') {
                    bname.setError(null);
                    pname.setError("Please enter PersonName");
                    Toast.makeText(getApplicationContext(),"Please enter PersonName",Toast.LENGTH_SHORT).show();

                } else if (str_Mobileno.length() == 0 || str_Mobileno.length() == ' ' || str_Mobileno.length()!=10) {
                    pname.setError(null);
                    mobile.setError("Please enter  10 digit mobileno");
                    Toast.makeText(getApplicationContext(),"Please enter  10 digit mobileno",Toast.LENGTH_SHORT).show();

                }


                else {
/*                    bname.setError(null);
                    pname.setError(null);
                    mobile.setError(null);
                    // uid_no.setError(null);
                    new AlertDialog.Builder(getApplicationContext())
                            .setMessage("Do you want to save the data? Please confirm.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    // checkBox1.setChecked(false);
                                    sqlDB = db.getWritableDatabase();
                                    //      ContentValues values = new ContentValues();
                                    String CustImage;
                                    if(bitmapLogo==null)
                                        CustImage="NoImage";
                                    else
                                        CustImage=imagePath;
                                    String location="";
                                    if(presentLocation!= null)
                                        location=presentLocation.getLatitude()+","+presentLocation.getLongitude();
                                    String time= String.valueOf(System.currentTimeMillis());
                                    db.addCutomers(new CustomerObj(bname.getText().toString(),pname.getText().toString(),mobile.getText().toString(),CustImage,0,location,time));
                                    Log.e("completed","dssdf");
                                    List<CustomerObj> customers = db.getAllCustomers();

                                    for (CustomerObj en : customers) {
                                        String log = "Id: "+en.getID()+" ,BName: " + en.getBName() + " ,PName: " + en.getPName() + ", Mobile:" + en.getMobile();
                                        // Writing Contacts to log
                                        Log.d("Name: ", log);
                                    }
                                    isActive = true;
                                    Customers_Fragment fragment = new Customers_Fragment();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.flContent, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
//                                    values.put("bname",bname.getText().toString() );
//                                    values.put("pname", pname.getText().toString());
//                                    values.put("mobile", mobile.getText().toString());

                                    //    sqlDB.execSQL("delete from customers");
                                    //    sqlDB.insert("customers", null, values);


                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    //   checkBox1.setChecked(true);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();*/

                }
            }
        });


        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });




    }


    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Agents_AddActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, ACTION_TAKE_PHOTO_A);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, ACTION_TAKE_GALLERY_PIC_A);

                }
                else if (options[item].equals("Cancel")) {
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
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                System.out.println("path of image from gallery......******************........."+ picturePath+"");
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
}
