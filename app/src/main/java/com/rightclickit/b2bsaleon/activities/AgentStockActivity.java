package com.rightclickit.b2bsaleon.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.AgentStockAdapter;
import com.rightclickit.b2bsaleon.adapters.AgentsAdapter;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.beanclass.AgentsStockBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.AgentsStockModel;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import java.util.ArrayList;

public class AgentStockActivity extends AppCompatActivity {
    private DBHelper mDBHelper;
    private MMSharedPreferences sharedPreferences;
    private AgentsStockModel mAgentStockModel;
    private ListView mStockList;
    private AgentStockAdapter mStockAdapter;
    private Context activityContext;
    private String mAgentId = "", mAgentCode = "", mAgentName = "";
    private SearchView search;
    TextView stock_print, stock_delivery;
    ArrayList<AgentsStockBean> stockBeanArrayList;
    ArrayList<String[]> selectedList;
    String str_ProductName, str_ProductCode, str_Uom, str_Received, str_Sale, str_CB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAgentId = this.getIntent().getStringExtra("agentId");
        mAgentCode = this.getIntent().getStringExtra("agentCode");
        mAgentName = this.getIntent().getStringExtra("agentName");
        setContentView(R.layout.activity_agent_stock);

        this.getSupportActionBar().setTitle("AS ON STOCK");
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

        this.activityContext = AgentStockActivity.this;

        mStockList = (ListView) findViewById(R.id.agent_stock_preview);

        this.mAgentStockModel = new AgentsStockModel(activityContext, AgentStockActivity.this);

        mDBHelper = new DBHelper(AgentStockActivity.this);
        sharedPreferences = new MMSharedPreferences(AgentStockActivity.this);
        stock_print = (TextView) findViewById(R.id.stock_print);
        stock_delivery = (TextView) findViewById(R.id.stock_delivery);


        if (new NetworkConnectionDetector(AgentStockActivity.this).isNetworkConnected()) {
            mAgentStockModel.getAgentsStock(mAgentId);
        } else {
            if (mDBHelper.getAgentsStockTableCount() > 0) {
                loadAgentsStockList();
            } else {
                // No stock available
            }
        }
        stockBeanArrayList = mDBHelper.fetchAllStockByAgentId(mAgentId);
        Log.i("stock", stockBeanArrayList.size() + "");
        selectedList = new ArrayList<>(stockBeanArrayList.size());

        for (int i = 0; i < stockBeanArrayList.size(); i++) {

            str_ProductName = stockBeanArrayList.get(i).getmProductName();
            str_ProductCode = stockBeanArrayList.get(i).getmProductCode();


            str_Uom = stockBeanArrayList.get(i).getmProductUOM();
            ;
            str_Received = stockBeanArrayList.get(i).getmProductStockQunatity();
            str_Sale = stockBeanArrayList.get(i).getmProductDeliveryQunatity();
            str_CB = stockBeanArrayList.get(i).getmProductCBQuantity();
            // str_Uom=mmSharedPreferences.getString("UOM");
            String[] temp = new String[6];
            temp[0] = str_ProductName;
            temp[1] = str_Uom;
            temp[2] = str_Received;
            temp[3] = str_Sale;
            temp[4] = str_CB;
            temp[5] = str_ProductCode;

            selectedList.add(temp);
        }

        stock_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pageheight = 300 + selectedList.size() * 60;
                Bitmap bmOverlay = Bitmap.createBitmap(400, pageheight, Bitmap.Config.ARGB_4444);
                Canvas canvas = new Canvas(bmOverlay);
                canvas.drawColor(Color.WHITE);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setAntiAlias(true);
                paint.setFilterBitmap(true);
                paint.setDither(true);
                paint.setColor(Color.parseColor("#000000"));
                paint.setTextSize(26);

                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                canvas.drawText(sharedPreferences.getString("companyname"), 5, 50, paint);
                canvas.drawText(mAgentName, 5, 80, paint);
                canvas.drawText(mAgentCode, 140, 80, paint);
                canvas.drawText("AS ON STOCK,", 5, 120, paint);
                canvas.drawText("----------------------------------------------------", 5, 180, paint);
                canvas.drawText("Product", 5, 210, paint);
                paint.setTextSize(20);
                canvas.drawText("UOM", 110, 210, paint);
                paint.setTextSize(20);
                canvas.drawText("RECD", 160, 210, paint);
                paint.setTextSize(20);
                canvas.drawText("Sale", 230, 210, paint);
                paint.setTextSize(20);
                canvas.drawText("CB", 330, 210, paint);
                paint.setTextSize(20);
                canvas.drawText("----------------------------------------------------", 5, 230, paint);

                int st = 250;
                paint.setTextSize(17);
                // for (Map.Entry<String, String[]> entry : selectedList.entrySet()) {

                for (int i = 0; i < selectedList.size(); i++) {
                    String[] temps = selectedList.get(i);
                    canvas.drawText(temps[0], 5, st, paint);
                    canvas.drawText(temps[1], 115, st, paint);
                    canvas.drawText(temps[2], 175, st, paint);
                    canvas.drawText(temps[3], 245, st, paint);
                    canvas.drawText(temps[4], 315, st, paint);

                    st = st + 30;
                    canvas.drawText(temps[5], 5, st, paint);
                    st = st + 30;
                    // canvas.drawText("FROM:" + temps[7], 100, st, paint);
                    //canvas.drawText("TO:" + temps[8], 250, st, paint);


                    //  canvas.drawText("----------------------------------------------------", 5, st, paint);
                }


                st = st + 20;
                canvas.drawText("--------X---------", 100, st, paint);
                com.szxb.api.jni_interface.api_interface.printBitmap(bmOverlay, 5, 5);
            }
        });

        stock_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AgentStockActivity.this, AgentStockDeliveryActivity.class);
                intent.putExtra("agentId", mAgentId);
                intent.putExtra("agentCode", mAgentCode);
                intent.putExtra("agentName", mAgentName);
                startActivity(intent);
                finish();
            }
        });
    }

    public void loadAgentsStockList() {

        if (mStockAdapter != null) {
            mStockAdapter = null;
        }
        if (stockBeanArrayList.size() > 0) {
            stockBeanArrayList.clear();
        }
        stockBeanArrayList = mDBHelper.fetchAllStockByAgentId(mAgentId);
        System.out.println("AGENT STOCK QUA==== " + stockBeanArrayList.size());
        if (stockBeanArrayList.size() > 0) {
            mStockAdapter = new AgentStockAdapter(this, AgentStockActivity.this, stockBeanArrayList);
            mStockList.setAdapter(mStockAdapter);
        }
    }


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

                mStockAdapter.filter(query);

                return true;

            }

        });

        // Get the search close button image view
        ImageView closeButton = (ImageView) search.findViewById(R.id.search_close_btn);
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
        if (id == R.id.action_search) {

            return true;
        }

        if (id == R.id.autorenew) {

            if (new NetworkConnectionDetector(AgentStockActivity.this).isNetworkConnected()) {
                mAgentStockModel.getAgentsStock(mAgentId);
            } else {
                new NetworkConnectionDetector(AgentStockActivity.this).displayNoNetworkError(AgentStockActivity.this);
            }
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                if (search.isIconified()) {
                    onBackPressed();
                } else {
                    search.setQuery("", false);
                    search.clearFocus();
                    search.onActionViewCollapsed();
                }
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
        menu.findItem(R.id.Add).setVisible(false);

        menu.findItem(R.id.autorenew).setVisible(true);
        //menu.findItem(R.id.sort).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, AgentsActivity.class);
        startActivity(intent);
        finish();
    }
}
