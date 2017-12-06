package com.rightclickit.b2bsaleon.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
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
import com.rightclickit.b2bsaleon.beanclass.SaleOrderReturnedProducts;
import com.rightclickit.b2bsaleon.beanclass.TripsheetSOList;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class TripsheetReturnsPreview extends AppCompatActivity {
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
    TextView deliveryNo;

    TextView deliveryDate;
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

    private String openingBalance ="";
    private String Deliverdquantity="";
    private String returnQuantity="";
    public String closingBalance="";

    String myList,str_ProductCode,str_Uom ;
    private ArrayList<SaleOrderReturnedProducts> returnedProductsList;

    private String mTripSheetId = "", mAgentId = "",mTripSheetDate="",mTripSheetCode="" ,mAgentName = "", mAgentCode = "", mAgentRouteId = "", mAgentRouteCode = "", mAgentSoId = "", mAgentSoCode,mAgentSoDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripsheet_returns_preview);

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


        mDBHelper = new DBHelper(TripsheetReturnsPreview.this);
        sharedPreferences = new MMSharedPreferences(TripsheetReturnsPreview.this);
        mTripSheetDate = sharedPreferences.getString("tripsheetDate");
        mTripSheetCode = sharedPreferences.getString("tripsheetCode");
        mTripSheetId = this.getIntent().getStringExtra("tripsheetId");
        mAgentId = this.getIntent().getStringExtra("agentId");
        mAgentCode = this.getIntent().getStringExtra("agentCode");
        mAgentName = this.getIntent().getStringExtra("agentName");
        mAgentRouteId = this.getIntent().getStringExtra("agentRouteId");
        mAgentRouteCode = this.getIntent().getStringExtra("agentRouteCode");
        mAgentSoId = this.getIntent().getStringExtra("agentSoId");
        mAgentSoCode = this.getIntent().getStringExtra("agentSoCode");
        totalAmount= this.getIntent().getStringExtra("totalAmount");
        totalTaxAmount=this.getIntent().getStringExtra("totalTaxAmount");
        subTotal= this.getIntent().getStringExtra("subTotal");


        ArrayList<TripsheetsStockList> tripsheetsStockLists = mDBHelper.fetchAllTripsheetsStockList(mTripSheetId);
        System.out.println("STOCK SIZE::: "+tripsheetsStockLists.size());


        Map<String, DeliverysBean> mData=(Map<String, DeliverysBean>)this.getIntent().getSerializableExtra("data");
        System.out.println("STOCK SIZE1111::: "+mData.size());

        final ArrayList<String[]> arList = new ArrayList<String[]>();

        SortedSet<String> keys = new TreeSet<String>(mData.keySet());
        for (String key : keys) {
            //String value = mData.get(key);
            // do something
            DeliverysBean d = mData.get(key);
            System.out.println("SELCTED QTY::: "+ d.getCansDueQuantity());
            String[] temp = new String[8];

            for( int i=0;i<tripsheetsStockLists.size();i++){
                str_ProductCode=tripsheetsStockLists.get(i).getmTripsheetStockProductCode();

            }
            myList=mDBHelper.getProductUnitByProductCode(str_ProductCode);
            str_Uom=myList;

            if (d.getProductCode().equals("2600005")) {
                temp[6] =(String.valueOf(d.getCratesDueQuantity()));

            } else if (d.getProductCode().equals("2600006")) {
                temp[6] =(String.valueOf(d.getCansDueQuantity()));
            } else {
                temp[6] =("0.0");
            }
            temp[0] = d.getProductTitle();
            temp[1] = str_Uom;
            temp[2]= String.valueOf(d.getSelectedQuantity());

            temp[4]=str_ProductCode;
            temp[5]= String.valueOf(d.getDeliveredQuantity());
            temp[7]= String.valueOf(Double.parseDouble(temp[6])+d.getDeliveredQuantity()-d.getSelectedQuantity());


            arList.add(temp);
        }

        // mAgentSoDate=this.getIntent().getStringExtra("agentSoDate");

        for (int i=0;i<arList.size();i++)
        {
            String[] temp=arList.get(i);
        }
       /* for (Map.Entry<String, DeliverysBean> entry : mData.entrySet())
        {
            DeliverysBean  d=entry.getValue();
            String[] temp=new String[5];
            temp[0]=d.getProductTitle();
            temp[1]= String.valueOf(d.getProductOrderedQuantity());
            temp[2]=d.getProductAgentPrice();
            temp[4]= String.valueOf(d.getTaxAmount());
            temp[3]= String.valueOf(d.getProductAmount());
            arList.add(temp);
        }*/
        mAgentsList = (ListView) findViewById(R.id.AgentsList);

        TripsheetReturnsPreview.CustomListView adapter = new TripsheetReturnsPreview.CustomListView(arList,this);
        mAgentsList.setAdapter(adapter);


        deliveryNo = (TextView) findViewById(R.id.return_no);
        deliveryDate = (TextView) findViewById(R.id.return_date);


        returnedProductsList = mDBHelper.getReturnsProductsListForSaleOrder(mTripSheetId, mAgentSoId, mAgentId);

        for (int j = 0; j < returnedProductsList.size(); j++) {


            if (returnedProductsList != null) {

                //str_deliveryNo=returnedProductsList.get(j).getReturnno();
                str_deliveryNo=returnedProductsList.get(j).getReturnNumber();
                //deliveryNo.setText("Return #"+str_deliveryNo);
                deliveryNo.setText(str_deliveryNo);

                if (returnedProductsList.get(j).getReturndate().isEmpty())
                    deliveryDate.setText("-");
                else
                    try {
                        str_deliveryDate=(getDate(returnedProductsList.get(j).getReturndate(),"dd-MM-yyyy"));
                        deliveryDate.setText(str_deliveryDate);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

            }


        }


        ArrayList<TripsheetSOList> tripSheetSOList = mDBHelper.getTripSheetSaleOrderDetails(mTripSheetId);
        for (int i = 0; i < tripSheetSOList.size(); i++) {


            if (tripSheetSOList != null) {
                if (tripSheetSOList.get(i).getmTripshetSOCode().isEmpty())
                    mAgentSoCode="Sale # -";

                else
                    mAgentSoCode=String.format("Sale # %s", tripSheetSOList.get(i).getmTripshetSOCode());

                if (tripSheetSOList.get(i).getmTripshetSODate().isEmpty())
                    mAgentSoDate="-";
                else
                    mAgentSoDate=tripSheetSOList.get(i).getmTripshetSODate();

            }


        }
        tv_companyName = (TextView) findViewById(R.id.tv_companyName);
        tv_companyName.setText(sharedPreferences.getString("companyname"));

        user_Name = (TextView) findViewById(R.id.tv_user_Name);
        user_Name.setText("by " + sharedPreferences.getString("loginusername"));

     /*   Route_Name = (TextView) findViewById(R.id.route_name);
        Route_Name.setText(sharedPreferences.getString("routename"));

        RouteCode = (TextView) findViewById(R.id.tv_routecode);
        str_routecode = (sharedPreferences.getString("routecode") + ",");
        RouteCode.setText(str_routecode);

*/





        // print = (TextView) findViewById(R.id.tv_print);


/*
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pageheight = 570 + arList.size() * 150;
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
                canvas.drawText(sharedPreferences.getString("companyname"), 5, 20, paint);
                paint.setTextSize(20);
                canvas.drawText(sharedPreferences.getString("loginusername"), 5, 50, paint);

                paint.setTextSize(20);
                canvas.drawText("-------------------------------------------", 5, 80, paint);

                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                paint.setTextSize(20);
                canvas.drawText("RETURNS", 100, 110, paint);

                paint.setTextSize(20);
                canvas.drawText("TRIP # ", 5, 140, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + mTripSheetCode, 150, 140, paint);
                paint.setTextSize(20);
                canvas.drawText("DATE ", 5, 170, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + mTripSheetDate, 150, 170, paint);

                paint.setTextSize(20);
                canvas.drawText("SO No ", 5, 200, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + mAgentSoCode, 150, 200, paint);
                paint.setTextSize(20);
                canvas.drawText("DATE ", 5, 230, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + mAgentSoDate, 150, 230, paint);


                paint.setTextSize(20);
                canvas.drawText("CUSTOMER ", 5, 260, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + mAgentName, 150, 260, paint);
                paint.setTextSize(20);
                canvas.drawText("CODE ", 5, 290, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + mAgentCode, 150, 290, paint);

                paint.setTextSize(20);
                canvas.drawText("RETURN # ", 5, 320, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + str_deliveryNo, 150, 320, paint);
                paint.setTextSize(20);
                canvas.drawText("DATE ", 5, 350, paint);
                paint.setTextSize(20);
                canvas.drawText(": " +str_deliveryDate, 150, 350, paint);



                paint.setTextSize(20);
                canvas.drawText("-------------------------------------------", 5, 380, paint);

                int st = 410;
                paint.setTextSize(17);
                // for (Map.Entry<String, String[]> entry : selectedList.entrySet()) {

                for (int i = 0; i < arList.size(); i++) {
                    String[] temp = arList.get(i);
                    paint.setTextSize(20);
                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    // canvas.drawText(temps[0] + "," + temps[1] + "( " + temps[2] + " )", 5, st, paint);
                    canvas.drawText(temp[0] + "," + temp[4] +  "( " + temp[1] +" )", 5, st, paint);

                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("OB QTY ", 5, st, paint);
                    canvas.drawText(": " + temp[6] , 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("DELIVERY QTY ", 5, st, paint);
                    canvas.drawText(": " + temp[5], 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("RETURN QTY ", 5, st, paint);
                    canvas.drawText(": " +  temp[2], 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("CB QTY ", 5, st, paint);
                    canvas.drawText(": " +  temp[7], 150, st, paint);




                    st = st + 40;
                }

                st = st + 30;
                canvas.drawText("--------X---------", 100,st , paint);
                com.szxb.api.jni_interface.api_interface.printBitmap(bmOverlay, 5, 5);
            }
        });
*/


    }
    public String getDate(String time, String format) {
        try {
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(Long.parseLong(time));
            return DateFormat.format(format, cal).toString();
        } catch (Exception e) {
            return "XX-XX-XXXX";
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
        if (id == R.id.Add) {
            Intent i = new Intent(TripsheetReturnsPreview.this, TDCSalesListActivity.class);
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
        Intent intent = new Intent(this, TripsheetReturns.class);
        intent.putExtra("tripsheetId", mTripSheetId);
        intent.putExtra("agentId", mAgentId);
        intent.putExtra("agentCode", mAgentCode);
        intent.putExtra("agentName", mAgentName);
        intent.putExtra("agentRouteId", mAgentRouteId);
        intent.putExtra("agentRouteCode", mAgentRouteCode);
        intent.putExtra("agentSoId", mAgentSoId);
        intent.putExtra("agentSoCode", mAgentSoCode);
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
            TextView ob = (TextView) view.findViewById(R.id.productOB);
            TextView dqty = (TextView) view.findViewById(R.id.productDQ);

            return_preview_product_name.setText(temp[0]);
            return_uom.setText(temp[1]);
            return_qty.setText(temp[2]);
            ob.setText(temp[6]);
            dqty.setText(temp[5]);



            return view;
        }
    }
}