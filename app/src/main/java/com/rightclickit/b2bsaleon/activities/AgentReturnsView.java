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
import com.rightclickit.b2bsaleon.beanclass.TripsheetSOList;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
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


    private String mReturnNo = "", mReturndate = "", mTripSheetDate="",mAgentName = "",mTripSheetCode="", mAgentCode = "", mAgenttripId = "", mAgentRouteCode = "", mAgentSoId = "", mAgentSoCode,mAgentSoDate;

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
            mAgenttripId=bundle.getString("tripId");
        }
        mTripSheetDate = sharedPreferences.getString("tripsheetDate");
        mTripSheetCode = sharedPreferences.getString("tripsheetCode");

        final ArrayList<String[]> arList = mDBHelper.getreturnDetailsPreview(mReturnNo);


        mAgentsList = (ListView) findViewById(R.id.AgentsList);

        AgentReturnsView.CustomListView adapter = new AgentReturnsView.CustomListView(arList,this);
        mAgentsList.setAdapter(adapter);





        tv_companyName = (TextView) findViewById(R.id.tv_companyName);
        tv_companyName.setText(sharedPreferences.getString("companyname"));

        user_Name = (TextView) findViewById(R.id.tv_user_Name);
        user_Name.setText(sharedPreferences.getString("loginusername"));

       /* Route_Name = (TextView) findViewById(R.id.route_name);
        Route_Name.setText(sharedPreferences.getString("routename"));

        RouteCode = (TextView) findViewById(R.id.tv_routecode);
        str_routecode = (sharedPreferences.getString("routecode") + ",");
        RouteCode.setText(str_routecode);*/
        ArrayList<TripsheetSOList> tripSheetSOList = mDBHelper.getTripSheetSaleOrderDetails(mAgenttripId);
        for (int i = 0; i < tripSheetSOList.size(); i++) {


            if (tripSheetSOList != null) {
                if (tripSheetSOList.get(i).getmTripshetSOCode().isEmpty())
                    mAgentSoCode="-";


                else
                    mAgentSoCode=String.format("Sale # %s", tripSheetSOList.get(i).getmTripshetSOCode());


                if (tripSheetSOList.get(i).getmTripshetSODate().isEmpty())
                    mAgentSoDate=("-");

                else
                    mAgentSoDate=tripSheetSOList.get(i).getmTripshetSODate();


            }


        }
        ArrayList<TripsheetsStockList> tripsheetsStockLists = mDBHelper.fetchAllTripsheetsStockList(mAgenttripId);

        for( int i=0;i<tripsheetsStockLists.size();i++){
            str_ProductCode=tripsheetsStockLists.get(i).getmTripsheetStockProductCode();

        }
        returnNo = (TextView) findViewById(R.id.return_no);

        returnNo.setText(mReturnNo);

        returnDate = (TextView) findViewById(R.id.return_date);
        returnDate.setText(mReturndate);



        if (mAgentName!=null){
            mAgentName = sharedPreferences.getString("agentName");
        }
        else {
            mAgentName="-";
        }

        mAgentCode = sharedPreferences.getString("agentCode");

        print = (TextView) findViewById(R.id.tv_print);


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   int pageheight = 300 + arList.size() * 60;
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
        });*/

        int pageheight = 500 + arList.size() * 100;
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
       /* paint.setTextSize(20);
        canvas.drawText(sharedPreferences.getString("loginusername"), 5, 50, paint);
*/
        paint.setTextSize(20);
        canvas.drawText("-------------------------------------------", 5, 50, paint);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(20);
        canvas.drawText("RETURNS", 100, 80, paint);

      /*  paint.setTextSize(20);
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
        canvas.drawText(": " + mAgentSoDate, 150, 230, paint);*/


                paint.setTextSize(20);
                canvas.drawText("PMT NO,DT.", 5, 110, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + mTripSheetCode + ", " + mTripSheetDate, 150, 110, paint);

                paint.setTextSize(20);
                canvas.drawText("SO NO,DT.", 5, 140, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + mAgentSoCode + ", " + mAgentSoDate, 150, 140, paint);



                paint.setTextSize(20);
        canvas.drawText("CUSTOMER ", 5, 170, paint);
        paint.setTextSize(20);
        canvas.drawText(": " + mAgentName, 150, 170, paint);
        paint.setTextSize(20);
        canvas.drawText("CODE ", 5, 200, paint);
        paint.setTextSize(20);
        canvas.drawText(": " + mAgentCode, 150, 200, paint);

        paint.setTextSize(20);
        canvas.drawText("RETURN # ", 5, 230, paint);
        paint.setTextSize(20);
        canvas.drawText(": " + mReturnNo, 150, 230, paint);
        paint.setTextSize(20);
        canvas.drawText("DATE ", 5, 260, paint);
        paint.setTextSize(20);
        canvas.drawText(": " +mReturndate, 150, 260, paint);



        paint.setTextSize(20);
        canvas.drawText("-------------------------------------------", 5, 290, paint);

        int st = 320;
        paint.setTextSize(17);
        // for (Map.Entry<String, String[]> entry : selectedList.entrySet()) {

        for (int i = 0; i < arList.size(); i++) {
            String[] temp = arList.get(i);
            paint.setTextSize(20);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            // canvas.drawText(temps[0] + "," + temps[1] + "( " + temps[2] + " )", 5, st, paint);
            canvas.drawText(temp[0] + "," + str_ProductCode +  "( " + temp[1] +" )", 5, st, paint);

           /* st = st + 30;
            paint.setTextSize(20);
            canvas.drawText("OB QTY ", 5, st, paint);
            canvas.drawText(": " + temp[6] , 150, st, paint);
            st = st + 30;
            paint.setTextSize(20);
            canvas.drawText("DELIVERY QTY ", 5, st, paint);
            canvas.drawText(": " + temp[5], 150, st, paint);*/
            st = st + 30;
            paint.setTextSize(20);
            canvas.drawText("RETURN QTY ", 5, st, paint);
            canvas.drawText(": " +  temp[2], 150, st, paint);
           /* st = st + 30;
            paint.setTextSize(20);
            canvas.drawText("CB QTY ", 5, st, paint);
            canvas.drawText(": " +  temp[7], 150, st, paint);*/




            st = st + 40;
        }
                st = st + 30;
                paint.setTextSize(20);
                canvas.drawText("* Please take photocopy of the Bill *", 17, st, paint);
        st = st + 30;
        canvas.drawText("--------X---------", 100,st , paint);
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
                view = inflater.inflate(R.layout.agentreturnspreview_custom, null);
            }
            String[] temp=list.get(position);
            TextView return_preview_product_name = (TextView) view.findViewById(R.id.productName);
            TextView return_uom = (TextView) view.findViewById(R.id.uom);
            TextView return_qty= (TextView) view.findViewById(R.id.productQt);
            //TextView ob = (TextView) view.findViewById(R.id.productOB);
            //TextView dqty = (TextView) view.findViewById(R.id.productDQ);

            //  TextView returnType = (TextView) view.findViewById(R.id.returnType);

            return_preview_product_name.setText(temp[0]);
            return_uom.setText(temp[1]);
            return_qty.setText(temp[2]);
           // returnType.setText(temp[3]);

         //   ob.setText(temp[5]);
           // dqty.setText(temp[6]);

            return view;
        }
    }
}
