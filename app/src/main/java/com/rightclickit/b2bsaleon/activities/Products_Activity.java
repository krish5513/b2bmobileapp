package com.rightclickit.b2bsaleon.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.ProductsAdapter;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.ProductsModel;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import java.util.ArrayList;
import java.util.HashMap;

public class Products_Activity extends AppCompatActivity {


    ListView listView;
    ArrayList<ProductsBean> myList ;
    Context context = Products_Activity.this;
    ProductsBean data;
    ProductsModel productsmodel;
    private Context applicationContext, activityContext;
    private  ProductsAdapter pAdapter;

    RecyclerView recyclerView;
    public ScrollView scrollView;
    Products_Activity c;
    private SearchView search;

    private DBHelper mDBHelper;
    private MMSharedPreferences mPreferences;
    private LinearLayout mDashboardLayout,mTripSheetsLayout,mCustomersLayout,mProductsLayout,mTDCLayout;
    private LinearLayout mRetailersLayout;

    private TextView mNoDataText;

    private String mStock = "",mAgentPrice="",mRetailerPrice="",mConsumerPrice="";

    private String mNotifications = "", mTdcHomeScreen = "", mTripsHomeScreen = " ", mAgentsHomeScreen = "", mRetailersHomeScreen = "", mDashboardHomeScreen = "";

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

        mDBHelper = new DBHelper(Products_Activity.this);
        mPreferences = new MMSharedPreferences(Products_Activity.this);
        myList=new ArrayList<ProductsBean>();


        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        applicationContext = getApplicationContext();
        activityContext = Products_Activity.this;
        mDBHelper = new DBHelper(Products_Activity.this);
        mPreferences = new MMSharedPreferences(Products_Activity.this);
        productsmodel = new ProductsModel(activityContext,this);

        listView = (ListView) findViewById(R.id.list);
        listView.setVisibility(View.GONE);
        mNoDataText = (TextView) findViewById(R.id.NoDataText);
        listView.setEmptyView(mNoDataText);

        myList= mDBHelper.fetchAllRecordsFromProductsTable();

        ArrayList<String> privilegeActionsData = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("Products"));
        System.out.println("F 11111 ***COUNT === "+ privilegeActionsData.size());
        for (int z = 0;z<privilegeActionsData.size();z++){
            System.out.println("Name::: "+ privilegeActionsData.get(z).toString());
            if (privilegeActionsData.get(z).toString().equals("List_View")) {
                listView.setVisibility(View.VISIBLE);
            }else if(privilegeActionsData.get(z).toString().equals("Stock")){
                mStock = privilegeActionsData.get(z).toString();
            }else if(privilegeActionsData.get(z).toString().equals("Agent_Price")){
                mAgentPrice = privilegeActionsData.get(z).toString();
            }else if(privilegeActionsData.get(z).toString().equals("Retailer_Price")){
                mRetailerPrice = privilegeActionsData.get(z).toString();
            }else if(privilegeActionsData.get(z).toString().equals("Consumer_Price")){
                mConsumerPrice = privilegeActionsData.get(z).toString();
            }
        }

        ArrayList<String> privilegeActionsData1= mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreferences.getString("UserActivity"));
        System.out.println("F 11111 ***COUNT === " + privilegeActionsData1.size());
        for (int z = 0; z < privilegeActionsData1.size(); z++) {
            System.out.println("Name::: " + privilegeActionsData1.get(z).toString());
            if (privilegeActionsData1.get(z).toString().equals("Notification")) {
                mNotifications = privilegeActionsData1.get(z).toString();
            } else if (privilegeActionsData1.get(z).toString().equals("tdc_home_screen")) {
                mTdcHomeScreen = privilegeActionsData1.get(z).toString();
            } else if (privilegeActionsData1.get(z).toString().equals("Trips@Home")) {
                mTripsHomeScreen = privilegeActionsData1.get(z).toString();
            } else if (privilegeActionsData1.get(z).toString().equals("Agents@Home")) {
                mAgentsHomeScreen = privilegeActionsData1.get(z).toString();
            } else if (privilegeActionsData1.get(z).toString().equals("Retailers@Home")) {
                mRetailersHomeScreen = privilegeActionsData1.get(z).toString();
            } else if (privilegeActionsData1.get(z).toString().equals("Dashboard@Home")) {
                mDashboardHomeScreen = privilegeActionsData1.get(z).toString();
            }
        }
        System.out.println("ELSE::: "+myList.size());
        if (new NetworkConnectionDetector(Products_Activity.this).isNetworkConnected()) {
            if (mDBHelper.getProductsTableCount()>0){
                myList = mDBHelper.fetchAllRecordsFromProductsTable();
                if(myList.size()>0){
                    mNoDataText.setText("");
                    loadProductsList(myList);
                }else {
                    mNoDataText.setText("No products found.");
                }
            }else {
                productsmodel.getProductsList("productsList");
            }
        }else {
            System.out.println("ELSE::: ");
            myList = mDBHelper.fetchAllRecordsFromProductsTable();
            if(myList.size()>0) {
                mNoDataText.setText("");
                loadProductsList(myList);
            }else {
                mNoDataText.setText("No products found.");
            }
        }

        //myList = new ArrayList<ProductsBean>();


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
               // Toast.makeText(applicationContext, "Clicked on TRIPSHEETS", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mTripSheetsLayout.startAnimation(animation1);
                Intent i =new Intent(Products_Activity.this,TripSheetsActivity.class);
                startActivity(i);
                finish();
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
        mRetailersLayout = (LinearLayout) findViewById(R.id.RetailersLayout);
        mRetailersLayout.setVisibility(View.GONE);
        mRetailersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mRetailersLayout.startAnimation(animation1);
                Intent i = new Intent(Products_Activity.this, RetailersActivity.class);
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
                //Toast.makeText(applicationContext, "Clicked on TDC", Toast.LENGTH_SHORT).show();
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
                mTDCLayout.startAnimation(animation1);
                Intent i =new Intent(Products_Activity.this,TDCSalesActivity.class);
                startActivity(i);
                finish();
            }
        });
        HashMap<String,String> userMapData = mDBHelper.getUsersData();
        ArrayList<String> privilegesData = mDBHelper.getUserActivityDetailsByUserId(userMapData.get("user_id"));
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
            }else if (privilegesData.get(k).toString().equals("Retailers")){
                mRetailersLayout.setVisibility(View.VISIBLE);
            }
        }
        //getDataInList();


    }

    public void loadProductsList(ArrayList<ProductsBean> mProductsBeansList){
        if(pAdapter!=null){
            pAdapter = null;
        }
        if(mProductsBeansList.size()>0){
            mNoDataText.setText("");
            pAdapter = new ProductsAdapter(this,Products_Activity.this,mProductsBeansList,mStock,mAgentPrice,mRetailerPrice,mConsumerPrice);
            listView.setAdapter(pAdapter);
        }else {
            mNoDataText.setText("No products found.");
        }

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

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        search = (SearchView) menu.findItem(R.id.action_search).getActionView();

        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                pAdapter.filter(query);

                return true;

            }

        });

        // Get the search close button image view
        ImageView closeButton = (ImageView)search.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setQuery("", false);
                search.clearFocus();
                search.onActionViewCollapsed();
            }
        });


        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_search) {
//
//            return true;
//        }
        if (id == R.id.notifications) {
            loadNotifications();
            Toast.makeText(this, "Clicked on Notifications...", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.settings) {

            loadSettings();
            Toast.makeText(this, "Clicked on Settings...", Toast.LENGTH_SHORT).show();
            return true;
        }


        switch (item.getItemId()) {
            case android.R.id.home:
                if(search.isIconified()) {
                    onBackPressed();
                }else {
                    search.setQuery("", false);
                    search.clearFocus();
                    search.onActionViewCollapsed();
                }
                return true;
            default:
                return true;
        }
    }
    private void loadNotifications() {
        Intent navigationIntent = new Intent(Products_Activity.this, NotificationsActivity.class);
        // mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // if you keep these flags white screen is coming on Intent navigation
        startActivity(navigationIntent);
        finish();
    }

    private void loadSettings() {
        Intent settingsIntent = new Intent(Products_Activity.this, SettingsActivity.class);
        // mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // if you keep these flags white screen is coming on Intent navigation
        startActivity(settingsIntent);
        finish();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.action_search).setVisible(true);

        menu.findItem(R.id.settings).setVisible(true);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.notifications).setVisible(true);

        menu.findItem( R.id.autorenew).setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = null;
        if (mTdcHomeScreen.equals("tdc_home_screen")) {
            intent = new Intent(this, TDCSalesActivity.class);
        } else if (mTripsHomeScreen.equals("Trips@Home")) {
            intent = new Intent(this, TripSheetsActivity.class);
        } else if (mAgentsHomeScreen.equals("Agents@Home")) {
            intent = new Intent(this, AgentsActivity.class);
        } else if (mRetailersHomeScreen.equals("Retailers@Home")) {
            intent = new Intent(this, RetailersActivity.class);

        } else if (mDashboardHomeScreen.equals("Dashboard@Home")) {
            intent = new Intent(this, DashboardActivity.class);
        } else {
            intent = new Intent(this, DashboardActivity.class);
        }
        startActivity(intent);
        finish();
    }
}


