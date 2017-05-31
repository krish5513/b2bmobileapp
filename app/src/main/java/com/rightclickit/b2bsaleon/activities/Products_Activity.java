package com.rightclickit.b2bsaleon.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ScrollView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.ProductsAdapter;
import com.rightclickit.b2bsaleon.beanclass.ProductsObj;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.customviews.CustomProgressDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.ProductsModel;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rightclickit.b2bsaleon.R.id.img;

public class Products_Activity extends AppCompatActivity {
    public static final String[] code= new String[] {"M5G"};
    public static final Integer[] images = { R.drawable.milk_converted};
    public static final Integer[] downarrow = { R.drawable.ic_circle_orange};

    public static final String[] title= new String[] {"Milk 500G"};

    public static final String[] moq = new String[] {
            "MOQ:"};

    public static final String[] returnUnit = new String[] {"YES"};
    public static final String[] returnable = new String[] {"Returnable"};
    public static final String[] retailer = new String[] {"Retailer:"};
    public static final String[] retailerUnit = new String[] {"Rs. 000.00"};
    public static final String[] consumer = new String[] {"Consumer:"};
    public static final String[] consumerUnit = new String[] {"46.00"};
    public static final String[] moqUnit = new String[] {"50 Ltrs"};
    public static final String[] agent = new String[] {"Agent:"};
    public static final String[] agentUnit = new String[] {"000.00"};


    ListView listView;
    ArrayList<ProductsObj> myList ;
    Context context = Products_Activity.this;
    ProductsObj data;
    ProductsModel productsmodel;



    private MMSharedPreferences sharedPreferences;
    private Context applicationContext, activityContext;

    // List<ProductsObj> rowItems;

     //FloatingActionButton fab;
    RecyclerView recyclerView;
    public ScrollView scrollView;
    Products_Activity c;
   // ProductAdapter adapter;
    private DBHelper dbHelper;
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


        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        listView = (ListView) findViewById(R.id.list);


        myList = new ArrayList<ProductsObj>();

        getDataInList();
        listView.setAdapter(new ProductsAdapter(context, myList));

        HashMap<String,String> userMapData = dbHelper.getUsersData();
        JSONObject routesJob = null;
        try {
            routesJob = new JSONObject(userMapData.get("route_ids").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray routesArray = null;
        try {
            routesArray = routesJob.getJSONArray("routeArray");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        for (int l = 0;l<routesArray.length();l++){
            try {
                System.out.println("The Route Id IS::: "+ routesArray.get(l).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            List<String> routesDataList = null;
            try {
                routesDataList = dbHelper.getRouteDataByRouteId(routesArray.get(l).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int k = 0;k<routesDataList.size();k++){
                System.out.println(" LOOPPPPPPPPPPPPPP "+k);
                if(routesDataList.get(k)!=null) {
                    switch (k){
                        
                    }
                }
            }
        }

    }


    private void getDataInList() {

        for (int i = 0; i < title.length; i++) {
            // Create a new object for each list item
             data = new ProductsObj();
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
            data.setDownarrowImage(downarrow[i]);
            data.setMaterialConsumer(consumer[i]);
            data.setMaterialConsumerUnit(consumerUnit[i]);

            // Add this object into the ArrayList myList
            myList.add(data);
        }

    }



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

    public void authenticateUser(String routeid) {
        CustomProgressDialog.showProgressDialog(activityContext, Constants.LOADING_MESSAGE);
        productsmodel.validateProducts(routeid);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
