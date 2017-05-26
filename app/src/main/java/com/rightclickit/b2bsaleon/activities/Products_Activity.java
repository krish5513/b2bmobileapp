package com.rightclickit.b2bsaleon.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.NotificationAdapter;
import com.rightclickit.b2bsaleon.adapters.ProductAdapter;
import com.rightclickit.b2bsaleon.adapters.ProductsAdapter;
import com.rightclickit.b2bsaleon.beanclass.NotificationItem;
import com.rightclickit.b2bsaleon.beanclass.ProductsObj;
import com.rightclickit.b2bsaleon.database.DBHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.rightclickit.b2bsaleon.database.DBHelper.KEY_ID;
import static com.rightclickit.b2bsaleon.database.DBHelper.KEY_MATERIAL_CODE;
import static com.rightclickit.b2bsaleon.database.DBHelper.KEY_MATERIAL_DISC;
import static com.rightclickit.b2bsaleon.database.DBHelper.KEY_MATERIAL_IMAGE;
import static com.rightclickit.b2bsaleon.database.DBHelper.KEY_MATERIAL_MOQ;
import static com.rightclickit.b2bsaleon.database.DBHelper.KEY_MATERIAL_MRP;
import static com.rightclickit.b2bsaleon.database.DBHelper.KEY_MATERIAL_RETURNABLE;
import static com.rightclickit.b2bsaleon.database.DBHelper.KEY_MATERIAL_SP;
import static com.rightclickit.b2bsaleon.database.DBHelper.KEY_MATERIAL_TAX;
import static com.rightclickit.b2bsaleon.database.DBHelper.KEY_MATERIAL_TAXType;
import static com.rightclickit.b2bsaleon.database.DBHelper.KEY_MATERIAL_UNIT_DISC;
import static com.rightclickit.b2bsaleon.database.DBHelper.KEY_MATERIAL_VALIDFROM;
import static com.rightclickit.b2bsaleon.database.DBHelper.KEY_MATERIAL_VALIDTO;
import static com.rightclickit.b2bsaleon.database.DBHelper.TABLE_PRODUCTS;

public class Products_Activity extends AppCompatActivity {
    public static final Integer[] id= new Integer[] {10052};
    public static final Integer[] images = { R.drawable.milk_converted};
    public static final Integer[] downarrow = { R.drawable.ic_circle_orange};

    public static final String[] name= new String[] {"Toned milk Thirumala special 500ml"};

    public static final String[] moq = new String[] {
            "MOQ"};

    public static final String[] liters = new String[] {"20 Ltrs"};
    public static final String[] returnable = new String[] {"Returnable"};
    public static final String[] mrp = new String[] {"MRP"};
    public static final String[] mrprs = new String[] {"Rs. 50.00"};
    public static final String[] sp = new String[] {"SP"};
    public static final String[] sprs = new String[] {"46.00"};
    public static final String[] idliters = new String[] {"50 Ltrs"};
    public static final String[] status = new String[] {"Instock"};


    ListView listView;
    List<ProductsObj> rowItems;

     //FloatingActionButton fab;
    RecyclerView recyclerView;
    public ScrollView scrollView;
    Products_Activity c;
    ProductAdapter adapter;
    DBHelper dbHelper;
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

        dbHelper=new DBHelper(getApplicationContext());


        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);




        rowItems = new ArrayList<ProductsObj>();
        for (int i = 0; i < name.length; i++) {
            ProductsObj item = new ProductsObj( id[i],name[i],liters[i],mrprs[i],mrp[i],sp[i],sprs[i],idliters[i],images[i],returnable[i], moq[i],status[i],downarrow[i]);
            rowItems.add(item);
        }

        listView = (ListView) findViewById(R.id.list);
        ProductsAdapter adapter = new ProductsAdapter(this,
                R.layout.products_adapter, rowItems);
        listView.setAdapter(adapter);

       // fab = (FloatingActionButton) findViewById(R.id.fab);
       /* fab.setVisibility(View.GONE);
        fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_shopping_cart_white_24dp));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Clicked Product Add",Toast.LENGTH_SHORT).show();
                Intent i =new Intent(Products_Activity.this,Product_Add_Activity.class);
                startActivity(i);
                finish();
            }
        });
*/

/*
        recyclerView = (RecyclerView) findViewById(R.id.cardList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(c);
        recyclerView.setLayoutManager(layoutManager);
        c = this;
        scrollView = (ScrollView) findViewById(R.id.scrollViewSearch);
        scrollView.setSmoothScrollingEnabled(true);
//        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//
//            @Override
//            public void onScrollChanged() {
//                int scrollY = scrollView.getScrollY();
//                if (scrollY == 0) mSwipeRefreshLayout.setEnabled(true);
//                else mSwipeRefreshLayout.setEnabled(false);
//
//            }
//        });
//        mSwipeRefreshLayout.setEnabled(false);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                //   MainActivity.showProgres(getContext(),true,fm);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new ProductAdapter(createList(), c);
                        c.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setAdapter(adapter);
                                //   MainActivity.stopProgres(getContext());
                            }
                        });
                    }
                }).start();
            }
        });*/


    }

    public void gotoView(ProductAdapter.ProductInfo pi){
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
       /* Products_View fragment = new Products_View();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                bundle.putString("productData",pi);
        fragment.setArguments(extras);
        fragmentTransaction.replace(R.id.flContent, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/

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
