package com.rightclickit.b2bsaleon.activities;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TripsheetStockPreviewAdapter;
import com.rightclickit.b2bsaleon.adapters.TripsheetsStockListAdapter;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.TripsheetsModel;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class TripsheetStockPreview extends AppCompatActivity {
    private String tripSheetId;

    String myList ;

    private Context applicationContext, activityContext;
    private MMSharedPreferences mmSharedPreferences;

    private TextView tripsheet_no_text_view, sale_date_time_text_view, total_tax_amount_text_view, total_amount_text_view, sub_total_amount_text_view;
    private String loggedInUserId, loggedInUserName;
    private DBHelper mDBHelper;

    String  str_routecode, str_Tripcode, str_Tripdate,str_ProductName,str_ProductCode,str_Uom,str_Order,str_Dispatch,str_Verify;
    TextView company_name, route_name, route_code, user_name, sales_print;
    private ListView tdc_products_list_preview;
    private LinearLayout tdc_sales_save_layout;

    ArrayList<String[]> selectedList;

    private TripsheetsModel mTripsheetsModel;
    private TripsheetStockPreviewAdapter mTripsheetsStockPreviewAdapter;
    private Map<String, TripsheetsStockList> productsDispatchListHashMap, productsVerifyListHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripsheet_stock_preview);



            applicationContext = getApplicationContext();
            activityContext = TripsheetStockPreview.this;

            mmSharedPreferences = new MMSharedPreferences(applicationContext);
            loggedInUserId = mmSharedPreferences.getString("userId");
            loggedInUserName = mmSharedPreferences.getString("loginusername");
            str_routecode = (mmSharedPreferences.getString("routecode") + ",");

      //  myList= new ProductsBean();
            mDBHelper = new DBHelper(TripsheetStockPreview.this);

       // myList= mDBHelper.fetchAllRecordsFromProductsTable();


        this.getSupportActionBar().setTitle("ROUTE STOCK VALUE");
            this.getSupportActionBar().setSubtitle(null);
            this.getSupportActionBar().setLogo(R.drawable.route_white);
            // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            this.getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            this.getSupportActionBar().setDisplayShowHomeEnabled(true);


            final ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

            productsDispatchListHashMap = new HashMap<>();
            productsVerifyListHashMap = new HashMap<>();

        ArrayList<ProductsBean> pbean=new ArrayList<ProductsBean>();



            Bundle bundle = getIntent().getExtras();
            if (bundle != null)
                tripSheetId = bundle.getString("tripSheetId");
                 str_Tripcode=bundle.getString("tripsheetCode");
                 str_Tripdate=bundle.getString("tripsheetDate");


            sales_print = (TextView) findViewById(R.id.stock_print);
            company_name = (TextView) findViewById(R.id.tdc_sales_company_name);
            user_name = (TextView) findViewById(R.id.tdc_sales_user_name);
            route_name = (TextView) findViewById(R.id.tdc_sales_route_name);
            route_code = (TextView) findViewById(R.id.tdc_sales_route_code);
            tripsheet_no_text_view = (TextView) findViewById(R.id.tripsheet_no);
            sale_date_time_text_view = (TextView) findViewById(R.id.tdc_sales_date_time);

            tdc_products_list_preview = (ListView) findViewById(R.id.tdc_sales_products_list_preview);

            company_name.setText(mmSharedPreferences.getString("companyname"));
            user_name.setText("by " + loggedInUserName);
            route_name.setText(mmSharedPreferences.getString("routename"));
            route_code.setText(str_routecode);
            tripsheet_no_text_view.setText(str_Tripcode +",");
            sale_date_time_text_view.setText(str_Tripdate);

           // for (int i=0;i<myList.size();i++){
               // Log.i("UOM",myList.get(i)+"");

                //String str_Pro_id=myList.get(i).getProductId();
              //Log.i("UOM",str_Uom);
                //Log.i("PRODUCT_ID::",str_Pro_id);
                //test[str_Pro_id] =str_Uom;



            mTripsheetsModel = new TripsheetsModel(this, TripsheetStockPreview.this);
            ArrayList<TripsheetsStockList> tripsheetsStockLists = mDBHelper.fetchAllTripsheetsStockList(tripSheetId);
            selectedList = new ArrayList<>(tripsheetsStockLists.size());
        //Log.i("tripsheetSize", tripsheetsStockLists.size()+"");
      //  Log.i("mylistSize", myList.size()+"");
            for( int i=0;i<tripsheetsStockLists.size();i++){

                str_ProductName=tripsheetsStockLists.get(i).getmTripsheetStockProductName();
                str_ProductCode=tripsheetsStockLists.get(i).getmTripsheetStockProductCode();

                myList=mDBHelper.getProductUnitByProductCode(str_ProductCode);
                str_Uom=myList;
                str_Order=tripsheetsStockLists.get(i).getmTripsheetStockProductOrderQuantity();
                str_Dispatch=tripsheetsStockLists.get(i).getmTripsheetStockDispatchQuantity();
                str_Verify = tripsheetsStockLists.get(i).getmTripsheetStockVerifiedQuantity();
               // str_Uom=mmSharedPreferences.getString("UOM");
                String[] temp = new String[6];
                temp[0] = str_ProductName;
                temp[1] = str_Uom;
                temp[2] = str_Order;
                temp[3] = str_Dispatch;
                temp[4] = str_Verify;
                temp[5] = str_ProductCode;

                selectedList.add(temp);
            }



            if (tripsheetsStockLists.size() > 0) {


                loadTripsData(tripsheetsStockLists);
            } else {
                if (new NetworkConnectionDetector(TripsheetStockPreview.this).isNetworkConnected()) {
                    mTripsheetsModel.getTripsheetsStockList(tripSheetId);
                }
            }


        sales_print.setOnClickListener(new View.OnClickListener() {
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
                canvas.drawText(mmSharedPreferences.getString("companyname"), 5, 50, paint);
                paint.setTextSize(20);
                canvas.drawText(str_routecode, 5, 80, paint);
                paint.setTextSize(20);
                canvas.drawText(mmSharedPreferences.getString("routename"), 200, 80, paint);
                paint.setTextSize(20);
                canvas.drawText("ROUTE STOCK,", 5, 120, paint);
                paint.setTextSize(20);
                canvas.drawText("by " + mmSharedPreferences.getString("loginusername"), 200, 120, paint);
                paint.setTextSize(20);
                canvas.drawText(str_Tripcode, 5, 150, paint);
                paint.setTextSize(20);
                canvas.drawText(str_Tripdate, 170, 150, paint);
                paint.setTextSize(20);
                //  canvas.drawText(str_agentname, 5, 180, paint);
                //  canvas.drawText(mmSharedPreferences.getString("agentCode"), 200, 180, paint);

                canvas.drawText("----------------------------------------------------", 5, 180, paint);
                canvas.drawText("Product", 5, 220, paint);
                paint.setTextSize(20);
                canvas.drawText("UOM", 110, 220, paint);
                paint.setTextSize(20);
                canvas.drawText("Order", 160, 220, paint);
                paint.setTextSize(20);
                canvas.drawText("Dispatch", 230, 220, paint);
                paint.setTextSize(20);
                canvas.drawText("Verify", 330, 220, paint);
                paint.setTextSize(20);
                canvas.drawText("----------------------------------------------------", 5, 235, paint);

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
    }




    public void loadTripsData(ArrayList<TripsheetsStockList> tripsStockList) {
        if (mTripsheetsStockPreviewAdapter != null) {
            mTripsheetsStockPreviewAdapter = null;
        }

        mTripsheetsStockPreviewAdapter = new TripsheetStockPreviewAdapter(this, TripsheetStockPreview.this, myList, tripsStockList);
        tdc_products_list_preview.setAdapter(mTripsheetsStockPreviewAdapter);
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
        menu.findItem(R.id.Add).setVisible(false);

        menu.findItem(R.id.autorenew).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, TripSheetStock.class);
        intent.putExtra("tripsheetId", tripSheetId);
        startActivity(intent);
        finish();

    }
}
