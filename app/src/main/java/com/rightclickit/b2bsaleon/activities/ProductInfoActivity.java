package com.rightclickit.b2bsaleon.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductInfoActivity extends AppCompatActivity {
    EditText code;
    EditText description;
    EditText returnable;
    EditText moq;
    EditText agent;
    EditText retailer;
    EditText consumer;
    EditText gst;
    EditText vat,uom;
    ImageView image;
    private MMSharedPreferences mPreference;
     DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        mPreference = new MMSharedPreferences(this);
        Bundle bundle = getIntent().getExtras();
        this.getSupportActionBar().setTitle("PRODUCTS VIEW");
        this.getSupportActionBar().setSubtitle(null);
        this.getSupportActionBar().setLogo(R.drawable.ic_shopping_cart_white_24dp);
        this.getSupportActionBar().setTitle(bundle.getString("TITLE"));
        // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);


        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        code=(EditText)findViewById(R.id.material_code) ;
        description=(EditText)findViewById(R.id.material_disc) ;
        returnable=(EditText)findViewById(R.id.Returnable) ;
        moq=(EditText)findViewById(R.id.MOQ) ;
        agent=(EditText)findViewById(R.id.agent) ;
        retailer=(EditText)findViewById(R.id.retailer) ;
        consumer=(EditText)findViewById(R.id.Consumer) ;
        image=(ImageView)findViewById(R.id.productimageview) ;
        gst=(EditText)findViewById(R.id.GST);
        vat=(EditText)findViewById(R.id.VAT);
        uom=(EditText)findViewById(R.id.UOM);

        code.setText(bundle.getString("CODE"));
        description.setText(bundle.getString("TITLE"));
        returnable.setText(bundle.getString("RETURNABLE"));
        moq.setText(bundle.getString("MOQ"));
        agent.setText(bundle.getString("AGENT"));
        retailer.setText(bundle.getString("RETAILER"));
        consumer.setText(bundle.getString("CONSUMER"));
        gst.setText(bundle.getString("GST"));
        vat.setText(bundle.getString("VAT"));
        uom.setText(bundle.getString("UOM"));

        Bundle extras = getIntent().getExtras();
        Bitmap bmp = (Bitmap) extras.getParcelable("imagebitmap");
        image.setImageBitmap(bmp );


       // image.setImageResource(Integer.parseInt(mPreference.getString("IMAGE")));
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
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem( R.id.Add).setVisible(false);

        menu.findItem( R.id.autorenew).setVisible(true);
        menu.findItem(R.id.sort).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Products_Activity.class);
        startActivity(intent);
        finish();
    }
    }

