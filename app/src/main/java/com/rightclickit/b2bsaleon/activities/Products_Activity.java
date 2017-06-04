package com.rightclickit.b2bsaleon.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.AgentsAdapter;
import com.rightclickit.b2bsaleon.adapters.ProductsAdapter;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.customviews.CustomProgressDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.AgentsModel;
import com.rightclickit.b2bsaleon.models.ProductsModel;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Products_Activity extends AppCompatActivity {


    ListView listView;
    ArrayList<ProductsBean> myList ;
    Context context = Products_Activity.this;
    ProductsBean data;
    ProductsModel productsmodel;
    private MMSharedPreferences sharedPreferences;
    private Context applicationContext, activityContext;
    private  ProductsAdapter pAdapter;

    RecyclerView recyclerView;
    public ScrollView scrollView;
    Products_Activity c;

    private DBHelper dbHelper;
    private LinearLayout mDashboardLayout,mTripSheetsLayout,mCustomersLayout,mProductsLayout,mTDCLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_);


        this.getSupportActionBar().setTitle("PRODUCTS");
        this.getSupportActionBar().setSubtitle(null);
        this.getSupportActionBar().setLogo(R.drawable.ic_shopping_cart_white_24dp);
        // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbHelper = new DBHelper(getApplicationContext());
        myList=new ArrayList<ProductsBean>();


        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        applicationContext = getApplicationContext();
        activityContext = Products_Activity.this;
        dbHelper = new DBHelper(Products_Activity.this);
        sharedPreferences = new MMSharedPreferences(Products_Activity.this);
        productsmodel = new ProductsModel(activityContext,this);

        listView = (ListView) findViewById(R.id.list);
        myList= dbHelper.fetchAllRecordsFromProductsTable();
        System.out.println("ELSE::: "+myList.size());
        if (new NetworkConnectionDetector(Products_Activity.this).isNetworkConnected()) {
            if (dbHelper.getProductsTableCount()>0){
                myList = dbHelper.fetchAllRecordsFromProductsTable();
                loadProductsList(myList);
            }else {
                productsmodel.getProductsList("productsList");
            }
        }else {
            System.out.println("ELSE::: ");
            myList = dbHelper.fetchAllRecordsFromProductsTable();
            loadProductsList(myList);
        }





        myList = new ArrayList<ProductsBean>();


        mDashboardLayout = (LinearLayout) findViewById(R.id.DashboardLayout);
        mDashboardLayout.setVisibility(View.GONE);
        mDashboardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mDashboardLayout.startAnimation(animation1);
                Intent i = new Intent(Products_Activity.this,DashboardActivity.class);
                startActivity(i);
                finish();
            }
        });
        mTripSheetsLayout = (LinearLayout) findViewById(R.id.TripSheetsLayout);
        mTripSheetsLayout.setVisibility(View.GONE);
        mTripSheetsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(applicationContext, "Clicked on TRIPSHEETS", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mTripSheetsLayout.startAnimation(animation1);
            }
        });

        mCustomersLayout = (LinearLayout) findViewById(R.id.CustomersLayout);
        mCustomersLayout.setVisibility(View.GONE);
        mCustomersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mCustomersLayout.startAnimation(animation1);
                Intent i = new Intent(Products_Activity.this,AgentsActivity.class);
                startActivity(i);
                finish();
            }
        });

        mProductsLayout = (LinearLayout) findViewById(R.id.ProductsLayout);
        mProductsLayout.setVisibility(View.GONE);
        mProductsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(applicationContext, "Clicked on Products", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mProductsLayout.startAnimation(animation1);
            }
        });

        mTDCLayout = (LinearLayout) findViewById(R.id.TDCLayout);
        mTDCLayout.setVisibility(View.GONE);
        mTDCLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(applicationContext, "Clicked on TDC", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mTDCLayout.startAnimation(animation1);
            }
        });

        ArrayList<String> privilegesData = dbHelper.getUserActivityDetailsByUserId(sharedPreferences.getString("userId"));
        System.out.println("F 11111 ***COUNT === "+ privilegesData.size());
        for (int k = 0; k<privilegesData.size();k++){
            if (privilegesData.get(k).toString().equals("Dashboard")){
                mDashboardLayout.setVisibility(View.VISIBLE);
            }else if (privilegesData.get(k).toString().equals("TripSheets")){
                mTripSheetsLayout.setVisibility(View.VISIBLE);
            }else if (privilegesData.get(k).toString().equals("Customers")){
                mCustomersLayout.setVisibility(View.VISIBLE);
            }else if (privilegesData.get(k).toString().equals("Products")){
                mProductsLayout.setVisibility(View.VISIBLE);
            }else if (privilegesData.get(k).toString().equals("TDC")){
                mTDCLayout.setVisibility(View.VISIBLE);
            }
        }
        //getDataInList();


    }

    public void loadProductsList(ArrayList<ProductsBean> mProductsBeansList){
        if(pAdapter!=null){
            pAdapter = null;
        }
        pAdapter = new ProductsAdapter(Products_Activity.this,mProductsBeansList);
        listView.setAdapter(pAdapter);
    }

   /* private void getDataInList() {

        for (int i = 0; i < title.length; i++) {
            // Create a new object for each list item
             data = new ProductsBean();
            data.setCode(code[i]);
            data.setMaterialTitle(title[i]);
            data.setMaterialReturnable(returnable[i]);
            data.setMaterialUnit(returnUnit[i]);
            data.setMaterialMOQ(moq[i]);
            data.setMaterialMOQUnit(moqUnit[i]);
            data.setMaterialAgent(agent[i]);
            data.setMaterialAgentunit(agentUnit[i]);
            data.setMaterialRetailer(retailer[i]);
            data.setMaterialRetailerUnit(retailerUnit[i]);
            data.setMaterialImage(images[i]);

            data.setMaterialConsumer(consumer[i]);
            data.setMaterialConsumerUnit(consumerUnit[i]);

            // Add this object into the ArrayList myList
            myList.add(data);
        }

    }*/



   /* public void gotoView(ProductAdapter.ProductInfo pi){
        Map<String, String> product = new HashMap<String,String>();
        product.put("materialCode",pi.materialCode);
        product.put("materialDisc",pi.materialDisc);
        product.put("materialUnit",pi.materialUnit);
        product.put("materialMRP",pi.materialMRP);
        product.put("materialMOQ",pi.materialMOQ);
        product.put("materialSP",pi.materialSP);
        product.put("materialReturnable",pi.materialReturnable);
        product.put("materialImage",pi.materialImage);
        product.put("id",pi.id);
        Bundle extras = new Bundle();
        extras.putSerializable("product", (Serializable) product);
       *//* Products_View fragment = new Products_View();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                bundle.putString("productData",pi);
        fragment.setArguments(extras);
        fragmentTransaction.replace(R.id.flContent, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*//*

    }*/













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
