package com.rightclickit.b2bsaleon.activities;

import android.content.Context;
import android.content.Intent;
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
import com.rightclickit.b2bsaleon.beanclass.SaleOrderDeliveredProducts;
import com.rightclickit.b2bsaleon.beanclass.TripsheetSOList;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class TripsheetDeliveryPreview extends AppCompatActivity {
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

    double taxes;
    private ArrayList<DeliverysBean> allProductsListFromStock = new ArrayList<>();
    private Map<String, DeliverysBean> selectedDeliveryProductsHashMap;
    private Map<String, String> previouslyDeliveredProductsHashMap; // this hash map contains previously delivered product quantity. key = product id & value = previously delivered quantity
    private Map<String, String> productOrderQuantitiesHashMap; // this hash map contains product codes & it's order quantity fetched from sale oder table.

    private String totalAmount = "";
    private String totalTaxAmount = "";
    private String subTotal = "";
    private boolean isDeliveryDataSaved = false, isDeliveryInEditingMode = false;

    double amount, subtotal;
    double taxAmount;
    String name,hssnnumber,cgst,sgst;

    private MMSharedPreferences sharedPreferences;
    DBHelper mDBHelper;
    private double mProductsPriceAmountSum = 0.0, mTotalProductsPriceAmountSum = 0.0, mTotalProductsTax = 0.0;
    String currentDate, str_routecode, str_deliveryDate, str_deliveryNo;
    private ArrayList<SaleOrderDeliveredProducts> deliveredProductsList;

    private String mTripSheetId = "",saleorderno="", mAgentId = "",mTripSheetDate="",mTripSheetCode="", mAgentName = "", mAgentCode = "", mAgentRouteId = "", mAgentRouteCode = "", mAgentSoId = "", mAgentSoCode, mAgentSoDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripsheet_delivery_preview);


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


        mDBHelper = new DBHelper(TripsheetDeliveryPreview.this);
        sharedPreferences = new MMSharedPreferences(TripsheetDeliveryPreview.this);

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
        totalAmount = this.getIntent().getStringExtra("totalAmount");
        totalTaxAmount = this.getIntent().getStringExtra("totalTaxAmount");
        subTotal = this.getIntent().getStringExtra("subTotal");


        deliveryNo = (TextView) findViewById(R.id.agentname);



        deliveryDate = (TextView) findViewById(R.id.tv_AgentCode);



        deliveredProductsList = mDBHelper.getDeliveredProductsListForSaleOrder(mTripSheetId, mAgentSoId, mAgentId);

        for (int j = 0; j < deliveredProductsList.size(); j++) {


            if (deliveredProductsList != null) {

                str_deliveryNo=(String.format("Delivery # RD%03d", deliveredProductsList.get(j).getDeliveryNo()));
                deliveryNo.setText(str_deliveryNo);

                if (deliveredProductsList.get(j).getCreatedTime().isEmpty())
                    deliveryDate.setText("-");
                else
                    try {
                        str_deliveryDate=(deliveredProductsList.get(j).getCreatedTime());
                        deliveryDate.setText(str_deliveryDate);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

            }


        }
        taxprice = (TextView) findViewById(R.id.taxAmount);
        if (totalTaxAmount != null)
            taxprice.setText(Utility.getFormattedCurrency(Double.parseDouble(totalTaxAmount)));


        tv_amount = (TextView) findViewById(R.id.Amount);
        tv_amount.setText(Utility.getFormattedCurrency(Double.parseDouble(totalAmount)));

        totalprice = (TextView) findViewById(R.id.totalAmount);
        totalprice.setText(Utility.getFormattedCurrency(Double.parseDouble(subTotal)));


        Map<String, DeliverysBean> mData = (Map<String, DeliverysBean>) this.getIntent().getSerializableExtra("data");
        final ArrayList<String[]> arList = new ArrayList<String[]>();
        SortedSet<String> keys = new TreeSet<String>(mData.keySet());
        for (String key : keys) {
            //String value = mData.get(key);
            // do something

            DeliverysBean d = mData.get(key);
            String[] temp = new String[11];
            temp[0] = d.getProductTitle();
            temp[1] = (String.valueOf(d.getProductOrderedQuantity()));
            if (d.getProductAgentPrice() != null) {
                temp[2] = Utility.getFormattedCurrency(Double.parseDouble(d.getProductAgentPrice()));
            } else {
                temp[2] = "Rs.0.00";
            }
            temp[4] = Utility.getFormattedCurrency(Double.parseDouble(String.valueOf(d.getTaxAmount())));
            temp[3] = Utility.getFormattedCurrency(Double.parseDouble(String.valueOf(d.getProductAmount())));
            temp[5]=d.getProductCode();
            temp[6]= mDBHelper.getProductUnitByProductCode(d.getProductCode());
            if (mDBHelper.getHSSNNUMBERByProductId(d.getProductId()) != null && mDBHelper.getHSSNNUMBERByProductId(d.getProductId()).length()> 0) {

                    hssnnumber = mDBHelper.getHSSNNUMBERByProductId(d.getProductId());
                }
             else {
                hssnnumber = "-";
            }


             if (mDBHelper.getGSTByProductId(d.getProductId()) > 0) {
                cgst = String.valueOf(mDBHelper.getGSTByProductId(d.getProductId())) + "%";
            } else {
                cgst = "0.00%";
            }

           if (mDBHelper.getVATByProductId(d.getProductId()) > 0) {
                sgst = String.valueOf(mDBHelper.getVATByProductId(d.getProductId())) + "%";
            } else {
                sgst = "0.00%";
            }

            try {
                Double gst = 0.0, vat = 0.0;
                if (d.getProductgst() != null) {
                    gst = Double.parseDouble(d.getProductgst());
                } else {
                    gst = mDBHelper.getGSTByProductId(d.getProductId());
                }
                if (d.getProductvat() != null) {
                    vat = Double.parseDouble(d.getProductvat());
                } else {
                    vat = mDBHelper.getVATByProductId(d.getProductId());
                }
                taxes = gst + vat;
            } catch (Exception e) {
                e.printStackTrace();
            }
            temp[7]=hssnnumber;
            temp[8]=cgst;
            temp[9]=sgst;
            temp[10]= String.valueOf(taxes);


            arList.add(temp);

        }
        // mAgentSoDate=this.getIntent().getStringExtra("agentSoDate");

        for (int i = 0; i < arList.size(); i++) {
            String[] temp = arList.get(i);
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

        CustomListView adapter = new CustomListView(arList, this);
        mAgentsList.setAdapter(adapter);

        sale_orderNo = (TextView) findViewById(R.id.order_no);
        sale_orderDate = (TextView) findViewById(R.id.tv_date);

        ArrayList<TripsheetSOList> tripSheetSOList = mDBHelper.getTripSheetSaleOrderDetails(mTripSheetId);
        for (int i = 0; i < tripSheetSOList.size(); i++) {


            if (tripSheetSOList != null) {
                if (tripSheetSOList.get(i).getmTripshetSOCode().isEmpty())
                    saleorderno="Sale # -";


                else
                    saleorderno=String.format("Sale # %s", tripSheetSOList.get(i).getmTripshetSOCode());


                if (tripSheetSOList.get(i).getmTripshetSODate().isEmpty())
                    sale_orderDate.setText("-");

                else
                    mAgentSoDate=tripSheetSOList.get(i).getmTripshetSODate();
                    sale_orderDate.setText(mAgentSoDate);

            }


        }
        sale_orderNo.setText(saleorderno);
        sharedPreferences.putString("SaleOrderId", saleorderno);
        sharedPreferences.putString("saleOrderDate",mAgentSoDate);

        tv_companyName = (TextView) findViewById(R.id.tv_companyName);
        tv_companyName.setText(sharedPreferences.getString("companyname"));

        user_Name = (TextView) findViewById(R.id.tv_user_Name);
        user_Name.setText("by " + sharedPreferences.getString("loginusername"));

        // Route_Name = (TextView) findViewById(R.id.route_name);
        // Route_Name.setText(sharedPreferences.getString("routename"));

        // RouteCode = (TextView) findViewById(R.id.tv_routecode);
        // str_routecode = (sharedPreferences.getString("routecode") + ",");
        // RouteCode.setText(str_routecode);






        //print = (TextView) findViewById(R.id.tv_print);


/*
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pageheight = 700 + arList.size() * 210;
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
                canvas.drawText("DELIVERY", 100, 110, paint);

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
                canvas.drawText("DELIVERY # ", 5, 320, paint);
                paint.setTextSize(20);
                canvas.drawText(": " +  str_deliveryNo, 150, 320, paint);
                paint.setTextSize(20);
                canvas.drawText("DATE ", 5, 350, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + str_deliveryDate, 150, 350, paint);
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
                    canvas.drawText(temp[0] + "," + temp[5]  + "( " +   temp[6]   + " ) ", 5, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("HSSN CODE ", 5, st, paint);
                    canvas.drawText(": " + temp[7], 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    // canvas.drawText("CGST,SGST " + ": " + temps[4] + " + " + temps[5] + " = " + temps[6], 5, st, paint);
                    canvas.drawText("CGST,SGST " + ": " + temp[8]+ " + " + temp[9] + " = "  + temp[10], 5, st, paint);

                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("QUANTITY ", 5, st, paint);
                    canvas.drawText(": " + temp[1], 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("RATE ", 5, st, paint);
                    canvas.drawText(": " + temp[2], 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("VALUE ", 5, st, paint);
                    canvas.drawText(": " + temp[3], 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("TAX VALUE ", 5, st, paint);
                    canvas.drawText(": " + temp[4], 150, st, paint);


                    st = st + 40;
                }
                paint.setTextSize(20);
                canvas.drawText("-------------------------------------------", 5, st, paint);

                st = st + 30;
                paint.setTextSize(20);
                canvas.drawText(" DELIVERY ", 5, st, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + Utility.getFormattedCurrency(Double.parseDouble(subTotal)), 150, st, paint);
                st = st + 30;
                paint.setTextSize(20);
                canvas.drawText("VALUE ", 5, st, paint);
                paint.setTextSize(20);
                canvas.drawText("(" + Utility.getFormattedCurrency(Double.parseDouble(totalAmount)) + "+" + Utility.getFormattedCurrency(Double.parseDouble(totalTaxAmount)) + ")", 150, st, paint);
                st = st + 30;
                canvas.drawText("--------X---------", 100, st, paint);
                com.szxb.api.jni_interface.api_interface.printBitmap(bmOverlay, 5, 5);
            }
        });
*/


    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == R.id.Add) {
            Intent i = new Intent(TripsheetDeliveryPreview.this, TDCSalesListActivity.class);
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
    public boolean onPrepareOptionsMenu (Menu menu){


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
    public void onBackPressed () {
        super.onBackPressed();
        Intent intent = new Intent(this, TripsheetDelivery.class);
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
                view = inflater.inflate(R.layout.tdc_sales_preview_adapter, null);
            }
            String[] temp = list.get(position);
            TextView order_preview_product_name = (TextView) view.findViewById(R.id.order_preview_product_name);
            TextView order_preview_quantity = (TextView) view.findViewById(R.id.order_preview_quantity);
            TextView order_preview_mrp = (TextView) view.findViewById(R.id.order_preview_mrp);
            TextView order_preview_amount = (TextView) view.findViewById(R.id.order_preview_amount);
            TextView order_preview_tax = (TextView) view.findViewById(R.id.order_preview_tax);
            TextView hsssn_number=(TextView)view.findViewById(R.id.hssn_number);
            TextView tv_cgst=(TextView)view.findViewById(R.id.cgst);
            TextView tv_sgst=(TextView)view.findViewById(R.id.sgst);
            order_preview_product_name.setText(temp[0]);
            order_preview_quantity.setText(temp[1]);
            order_preview_mrp.setText(temp[2]);
            order_preview_amount.setText(temp[3]);
            order_preview_tax.setText(temp[4]);
            hsssn_number.setText(temp[7]);
            tv_cgst.setText(temp[8]);
            tv_sgst.setText(temp[9]);
            return view;
        }
    }
}
