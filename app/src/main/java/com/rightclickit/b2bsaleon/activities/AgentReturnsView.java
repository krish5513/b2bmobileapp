package com.rightclickit.b2bsaleon.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TripSheetDeleveriesPreviewAdapter;
import com.rightclickit.b2bsaleon.beanclass.DeliverysBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.ArrayList;
import java.util.Map;

public class AgentReturnsView extends AppCompatActivity {
    private ListView mAgentsList;
    private TripSheetDeleveriesPreviewAdapter mTripSheetDeliveriesPreviewAdapter;
    ArrayList customArraylist = new ArrayList();
    private Context activityContext;
    TextView tv_companyName;
    TextView Route_Name;
    TextView RouteCode;
    TextView sale_orderNo;
    TextView sale_orderDate;
    TextView user_Name;
    TextView returnNo;

    TextView returnDate;
    TextView taxprice;
    TextView tv_amount;
    TextView totalprice;
    TextView print;


    private ArrayList<DeliverysBean> allProductsListFromStock = new ArrayList<>();
    private Map<String, DeliverysBean> selectedDeliveryProductsHashMap;
    private Map<String, String> previouslyDeliveredProductsHashMap; // this hash map contains previously delivered product quantity. key = product id & value = previously delivered quantity
    private Map<String, String> productOrderQuantitiesHashMap; // this hash map contains product codes & it's order quantity fetched from sale oder table.

    private String totalAmount ="";
    private String totalTaxAmount="";
    private String subTotal="";
    private boolean isDeliveryDataSaved = false, isDeliveryInEditingMode = false;

    double amount, subtotal;
    double taxAmount;
    String name;

    private MMSharedPreferences sharedPreferences;
    DBHelper mDBHelper;
    private double mProductsPriceAmountSum = 0.0, mTotalProductsPriceAmountSum = 0.0, mTotalProductsTax = 0.0;
    String currentDate, str_routecode, str_deliveryDate, str_deliveryNo;



    String myList,str_ProductCode,str_Uom ;


    private String mReturnNo = "", mReturndate = "", mAgentName = "", mAgentCode = "", mAgentRouteId = "", mAgentRouteCode = "", mAgentSoId = "", mAgentSoCode,mAgentSoDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_returns_view);

        this.getSupportActionBar().setTitle("PREVIEW");
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


        mDBHelper = new DBHelper(AgentReturnsView.this);
        sharedPreferences = new MMSharedPreferences(AgentReturnsView.this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mReturnNo= bundle.getString("ReturnNo");
            mReturndate=bundle.getString("Returndate");
        }


        final ArrayList<String[]> arList = mDBHelper.getreturnDetailsPreview(mReturnNo);


        mAgentsList = (ListView) findViewById(R.id.AgentsList);

        AgentReturnsView.CustomListView adapter = new AgentReturnsView.CustomListView(arList,this);
        mAgentsList.setAdapter(adapter);





        tv_companyName = (TextView) findViewById(R.id.tv_companyName);
        tv_companyName.setText(sharedPreferences.getString("companyname"));

        user_Name = (TextView) findViewById(R.id.tv_user_Name);
        user_Name.setText("by " + sharedPreferences.getString("loginusername"));

        Route_Name = (TextView) findViewById(R.id.route_name);
        Route_Name.setText(sharedPreferences.getString("routename"));

        RouteCode = (TextView) findViewById(R.id.tv_routecode);
        str_routecode = (sharedPreferences.getString("routecode") + ",");
        RouteCode.setText(str_routecode);




        returnNo = (TextView) findViewById(R.id.return_no);

        returnNo.setText(mReturnNo);

        returnDate = (TextView) findViewById(R.id.return_date);
        returnDate.setText(mReturndate);





        print = (TextView) findViewById(R.id.tv_print);


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pageheight = 300 + arList.size() * 60;
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
                paint.setTextSize(22);
                canvas.drawText(str_routecode, 5, 80, paint);
                paint.setTextSize(22);
                canvas.drawText(sharedPreferences.getString("routename"), 200, 80, paint);
                paint.setTextSize(22);
                canvas.drawText("ROUTE DELEVERY,", 5, 120, paint);
                paint.setTextSize(22);
                canvas.drawText("by " + sharedPreferences.getString("loginusername"), 200, 120, paint);
                paint.setTextSize(22);
                canvas.drawText(mReturnNo, 5, 180, paint);
                paint.setTextSize(22);
                canvas.drawText(mReturndate, 200, 180, paint);
                paint.setTextSize(22);

                canvas.drawText("----------------------------------------------------", 5, 200, paint);
                canvas.drawText("Product", 5, 220, paint);
                paint.setTextSize(22);
                canvas.drawText("UOM", 100, 220, paint);
                paint.setTextSize(22);
                canvas.drawText("Qty", 160, 220, paint);
                paint.setTextSize(22);
                canvas.drawText("Return Type", 230, 220, paint);


                canvas.drawText("----------------------------------------------------", 5, 235, paint);


                int st = 250;
                paint.setTextSize(17);
//                    for (Map.Entry<String, String[]> entry : selectedList.entrySet()) {
                for (int i = 0; i < arList.size(); i++) {
                    String[] temps = arList.get(i);
                    //String[] temps = selectedList.get(i-1);
                    canvas.drawText(temps[0], 5, st, paint);
                    canvas.drawText(temps[1], 115, st, paint);


                    canvas.drawText(temps[2], 175, st, paint);

                    canvas.drawText(temps[3], 245, st, paint);
                    //  canvas.drawText(temps[4], 315, st, paint);


                    // canvas.drawText("FROM:" + temps[7], 100, st, paint);
                    //canvas.drawText("TO:" + temps[8], 250, st, paint);

                    st = st + 30;
                    //  canvas.drawText("----------------------------------------------------", 5, st, paint);


                }
                canvas.drawText("----------------------------------------------------", 5, st, paint);

                // st = st + 20;
                //canvas.drawText("Total:", 5, st, paint);
                //canvas.drawText(Utility.getFormattedCurrency(Double.parseDouble(totalTaxAmount)), 70, st, paint);
                // canvas.drawText(Utility.getFormattedCurrency(Double.parseDouble(totalAmount)), 170, st, paint);
                // canvas.drawText(Utility.getFormattedCurrency(Double.parseDouble(subTotal)), 280, st, paint);
                st = st + 20;
                canvas.drawText("--------X---------", 100, st, paint);
                com.szxb.api.jni_interface.api_interface.printBitmap(bmOverlay, 5, 5);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Add) {
            Intent i = new Intent(AgentReturnsView.this, TDCSalesListActivity.class);
            startActivity(i);
            finish();
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
        menu.findItem(R.id.sort).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, AgentReturns.class);

        startActivity(intent);
        finish();
    }


    class CustomListView extends BaseAdapter implements ListAdapter {
        private ArrayList<String[]> list = new ArrayList<String[]>();
        private Context context;


        public CustomListView(ArrayList<String[]> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int pos) {
            return list.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return 0;
            //just return 0 if your list items do not have an Id variable.
        }



        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.tripsheetreturnspreview_custom, null);
            }
            String[] temp=list.get(position);
            TextView return_preview_product_name = (TextView) view.findViewById(R.id.productName);
            TextView return_uom = (TextView) view.findViewById(R.id.uom);
            TextView return_qty= (TextView) view.findViewById(R.id.productQt);
          //  TextView returnType = (TextView) view.findViewById(R.id.returnType);

            return_preview_product_name.setText(temp[0]);
            return_uom.setText(temp[1]);
            return_qty.setText(temp[2]);
           // returnType.setText(temp[3]);



            return view;
        }
    }
}
