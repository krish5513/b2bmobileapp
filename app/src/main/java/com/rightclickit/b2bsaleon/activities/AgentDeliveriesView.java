package com.rightclickit.b2bsaleon.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.Map;

public class AgentDeliveriesView extends AppCompatActivity {
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

    private double totalAmount = 0;
    private double totalTaxAmount = 0;
    private double subTotal = 0;
    private boolean isDeliveryDataSaved = false, isDeliveryInEditingMode = false;

    // double amount, subtotal;
    // double taxAmount;
    String name, hssnnumber, cgst, sgst;

    private MMSharedPreferences sharedPreferences;
    DBHelper mDBHelper;
    private double mProductsPriceAmountSum = 0.0, mTotalProductsPriceAmountSum = 0.0, mTotalProductsTax = 0.0;
    String currentDate, str_routecode, str_deliveryDate, str_deliveryNo;
    double taxes;

    private String mDeliveryNo = "", mDeliverydate = "", productID = "", mTripSheetCode = "", mTripSheetDate = "", mAgentName = "", mAgentCode = "", mAgentTripId = "", mAgentRouteCode = "", mAgentSoId = "", mAgentSoCode, mAgentSoDates, deliveryBy, updatedBy;
    private ArrayList<String> deliveredProdIdsList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_deliveries_view);

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


        mDBHelper = new DBHelper(AgentDeliveriesView.this);
        sharedPreferences = new MMSharedPreferences(AgentDeliveriesView.this);

        mTripSheetDate = sharedPreferences.getString("tripsheetDate");
        mTripSheetCode = sharedPreferences.getString("tripsheetCode");
        //mAgentSoCode= sharedPreferences.getString("SaleOrderId");
        // mAgentSoDates = sharedPreferences.getString("saleOrderDate");
        if (mAgentName != null) {
            mAgentName = sharedPreferences.getString("agentName");
        } else {
            mAgentName = "-";
        }

        mAgentCode = sharedPreferences.getString("agentCode");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mDeliveryNo = bundle.getString("DeliveryNo");
            mDeliverydate = bundle.getString("Deliverydate");
            productID = bundle.getString("productId");
            //  mAgentSoCode = bundle.getString("SaleOId");
            mAgentTripId = bundle.getString("tripsheetId");
            deliveryBy = bundle.getString("deliveryBy");
            updatedBy = bundle.getString("updatedBy");
        }

        mAgentsList = (ListView) findViewById(R.id.AgentsList);
        taxprice = (TextView) findViewById(R.id.taxAmount);


        tv_amount = (TextView) findViewById(R.id.Amount);
        tv_amount.setText(Utility.getFormattedCurrency(totalAmount));

        totalprice = (TextView) findViewById(R.id.totalAmount);


        tv_companyName = (TextView) findViewById(R.id.tv_companyName);

        user_Name = (TextView) findViewById(R.id.tv_user_Name);

        deliveryNo = (TextView) findViewById(R.id.agentname);

        deliveryDate = (TextView) findViewById(R.id.tv_AgentCode);

        print = (TextView) findViewById(R.id.tv_print);


        ArrayList<TripsheetSOList> tripSheetSOList = mDBHelper.getTripSheetSaleOrderDetails(mAgentTripId);
        for (int i = 0; i < tripSheetSOList.size(); i++) {


            if (tripSheetSOList != null) {
                if (tripSheetSOList.get(i).getmTripshetSOCode().isEmpty())
                    mAgentSoCode = "-";


                else
                    mAgentSoCode = String.format("Sale # %s", tripSheetSOList.get(i).getmTripshetSOCode());


                if (tripSheetSOList.get(i).getmTripshetSODate().isEmpty())
                    mAgentSoDates = ("-");

                else
                    mAgentSoDates = tripSheetSOList.get(i).getmTripshetSODate();
            }
        }
        final ArrayList<String[]> arList = mDBHelper.getdeliveryDetailsPreview(mDeliveryNo, mAgentTripId);

        deliveredProdIdsList = mDBHelper.getdeliveryDetailsPreviewProdIdsList(mDeliveryNo);

        AgentDeliveriesView.CustomListView adapter = new AgentDeliveriesView.CustomListView(arList, this);
        mAgentsList.setAdapter(adapter);


        // tv_companyName.setText(sharedPreferences.getString("companyname"));
        tv_companyName.setText(updatedBy);

        //user_Name.setText(sharedPreferences.getString("loginusername"));
        user_Name.setText(deliveryBy);

        /*Route_Name = (TextView) findViewById(R.id.route_name);
        Route_Name.setText(sharedPreferences.getString("routename"));

        RouteCode = (TextView) findViewById(R.id.tv_routecode);
        str_routecode = (sharedPreferences.getString("routecode") + ",");
        RouteCode.setText(str_routecode);

*/
       /* sale_orderNo = (TextView) findViewById(R.id.order_no);

       if (sale_orderNo != null) {
            sale_orderNo.setText(mAgentSoCode);
        } else {
            sale_orderNo.setText("-");
        }
        sale_orderDate = (TextView) findViewById(R.id.tv_date);

        if (sale_orderDate != null) {
            sale_orderDate.setText(mDeliverydate);
        } else {
            sale_orderDate.setText("-");
        }
*/

        deliveryNo.setText(mDeliveryNo);

        deliveryDate.setText(mDeliverydate);


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pageheight = 500 + arList.size() * 210;
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
                canvas.drawText(updatedBy, 5, 20, paint);
              /*  paint.setTextSize(20);
                canvas.drawText(sharedPreferences.getString("loginusername"), 5, 50, paint);
                */
                paint.setTextSize(20);
                canvas.drawText("-------------------------------------------", 5, 50, paint);
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                paint.setTextSize(20);
                canvas.drawText("DELIVERY", 100, 80, paint);

               /* paint.setTextSize(20);
                canvas.drawText("TRIP # ", 5, 140, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + mTripSheetCode, 150, 140, paint);
                paint.setTextSize(20);
                canvas.drawText("DATE ", 5, 170, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + mTripSheetDate, 150, 170, paint);
*/


                paint.setTextSize(20);
                canvas.drawText("TRIP #,DT.", 5, 110, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + mTripSheetCode + ", " + mTripSheetDate, 130, 110, paint);


              /*  paint.setTextSize(20);
                canvas.drawText("SO No ", 5, 140, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + mAgentSoCode, 150, 140, paint);
                paint.setTextSize(20);
                canvas.drawText("DATE ", 5, 170, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + mAgentSoDates, 150, 170, paint);
*/

                paint.setTextSize(20);
                canvas.drawText("SO NO,DT.", 5, 140, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + mAgentSoCode + ", " + mAgentSoDates, 130, 140, paint);


                paint.setTextSize(20);
                canvas.drawText("CUSTOMER ", 5, 170, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + mAgentName, 150, 170, paint);
                paint.setTextSize(20);
                canvas.drawText("CODE ", 5, 200, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + mAgentCode, 150, 200, paint);

                paint.setTextSize(20);
                canvas.drawText("DELIVERY # ", 5, 230, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + mDeliveryNo, 150, 230, paint);
                paint.setTextSize(20);
                canvas.drawText("DATE ", 5, 260, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + mDeliverydate, 150, 260, paint);
                paint.setTextSize(20);
                canvas.drawText("-------------------------------------------", 5, 290, paint);


                int st = 320;
                paint.setTextSize(17);
                // for (Map.Entry<String, String[]> entry : selectedList.entrySet()) {

                for (int i = 0; i < arList.size(); i++) {
                    Double gst = 0.0, vat = 0.0;
                    if (mDBHelper.getHSSNNUMBERByProductId(deliveredProdIdsList.get(i).toString()) != null
                            && mDBHelper.getHSSNNUMBERByProductId(deliveredProdIdsList.get(i).toString()).length() > 0) {

                        hssnnumber = mDBHelper.getHSSNNUMBERByProductId(deliveredProdIdsList.get(i).toString());
                    } else {
                        hssnnumber = "-";
                    }
                    if (mDBHelper.getGSTByProductId(deliveredProdIdsList.get(i).toString()) > 0) {
                        cgst = String.valueOf(mDBHelper.getGSTByProductId(deliveredProdIdsList.get(i).toString())) + "%";
                        gst = mDBHelper.getGSTByProductId(deliveredProdIdsList.get(i).toString());
                    } else {
                        cgst = "0.00%";
                        gst = 0.0;
                    }

                    if (mDBHelper.getVATByProductId(deliveredProdIdsList.get(i).toString()) > 0) {
                        sgst = String.valueOf(mDBHelper.getVATByProductId(deliveredProdIdsList.get(i).toString())) + "%";
                        vat = mDBHelper.getVATByProductId(deliveredProdIdsList.get(i).toString());
                    } else {
                        sgst = "0.00%";
                        vat = 0.0;
                    }

                    taxes = gst + vat;


                    String[] temp = arList.get(i);
                    paint.setTextSize(20);
                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    // canvas.drawText(temps[0] + "," + temps[1] + "( " + temps[2] + " )", 5, st, paint);
                    canvas.drawText(temp[0] + "," + temp[5] + temp[6], 5, st, paint);


                 /*   st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("HSSN CODE ", 5, st, paint);
                    canvas.drawText(": " + hssnnumber, 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    // canvas.drawText("CGST,SGST " + ": " + temps[4] + " + " + temps[5] + " = " + temps[6], 5, st, paint);
                    canvas.drawText("CGST,SGST " + ": " + cgst + sgst + " = " + taxes, 5, st, paint);

*/

                    paint.setTextSize(20);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("HSSN: " + hssnnumber + "," + "GST: " + taxes + "%", 5, st, paint);


                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("QUANTITY ", 5, st, paint);
                    canvas.drawText(": " + temp[1], 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("RATE ", 5, st, paint);
                    canvas.drawText(": " + Utility.getFormattedCurrency(Double.parseDouble(temp[2])), 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("TOT ", 5, st, paint);
                    canvas.drawText(": " + Utility.getFormattedCurrency(Double.parseDouble(temp[3])), 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("GST ", 5, st, paint);
                    canvas.drawText(": " + Utility.getFormattedCurrency(Double.parseDouble(temp[4])), 150, st, paint);


                    st = st + 40;
                }
                paint.setTextSize(20);
                canvas.drawText("-------------------------------------------", 5, st, paint);

                st = st + 30;
                paint.setTextSize(20);
                canvas.drawText(" DELIVERY ", 5, st, paint);
                paint.setTextSize(20);
                canvas.drawText(": " + Utility.getFormattedCurrency(subTotal), 150, st, paint);
                st = st + 30;
                paint.setTextSize(20);
                canvas.drawText("VALUE ", 5, st, paint);
                paint.setTextSize(20);
                canvas.drawText("(" + Utility.getFormattedCurrency((totalAmount)) + "+" + Utility.getFormattedCurrency((totalTaxAmount)) + ")", 150, st, paint);
                st = st + 30;


                paint.setTextSize(20);
                canvas.drawText("* Please take photocopy of the Bill *", 17, st, paint);
                st = st + 30;

                canvas.drawText("--------X---------", 100, st, paint);
                com.szxb.api.jni_interface.api_interface.printBitmap(bmOverlay, 5, 5);
            }
        });

        synchronized (this) {
            for (int g = 0; g < arList.size(); g++) {
                String[] temp = arList.get(g);
                totalAmount = totalAmount + Double.parseDouble(temp[3]);
                totalTaxAmount = totalTaxAmount + Double.parseDouble(temp[4]);
                subTotal = totalAmount + totalTaxAmount;
            }
        }
        synchronized (this) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    totalprice.setText(Utility.getFormattedCurrency(subTotal));
                    sharedPreferences.putString("total", String.valueOf(subTotal));
                    tv_amount.setText(Utility.getFormattedCurrency(totalAmount));
                    taxprice.setText(Utility.getFormattedCurrency(totalTaxAmount));
                }
            }, 100);
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
            Intent i = new Intent(AgentDeliveriesView.this, TDCSalesListActivity.class);
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
        Intent intent = new Intent(this, AgentDeliveries.class);
        intent.putExtra("DeliveryId", mDeliveryNo);
        intent.putExtra("DeliveryDate", mDeliverydate);
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
            TextView hssnNumber = (TextView) view.findViewById(R.id.hssn_number);
            TextView CGST = (TextView) view.findViewById(R.id.cgst);
            TextView SGST = (TextView) view.findViewById(R.id.sgst);

            order_preview_product_name.setText(temp[0]);
            order_preview_quantity.setText(temp[1]);
            order_preview_mrp.setText(Utility.getFormattedCurrency(Double.parseDouble(temp[2])));
            order_preview_amount.setText(Utility.getFormattedCurrency(Double.parseDouble(temp[3])));
            order_preview_tax.setText(Utility.getFormattedCurrency(Double.parseDouble(temp[4])));

            String hssnnumber2331 = mDBHelper.getHSSNNUMBERByProductId(deliveredProdIdsList.get(position).toString());
            if (hssnnumber2331 != null && hssnnumber2331.length() > 0) {
                hssnNumber.setText(hssnnumber2331);
            } else {
                hssnNumber.setText("-");
            }
            String cgst1, sgst1;
            if (mDBHelper.getGSTByProductId(productID) > 0) {
                cgst1 = String.valueOf(mDBHelper.getGSTByProductId(deliveredProdIdsList.get(position).toString())) + "%";
            } else {
                cgst1 = "0.00%";
            }

            if (mDBHelper.getVATByProductId(productID) > 0) {
                sgst1 = String.valueOf(mDBHelper.getVATByProductId(deliveredProdIdsList.get(position).toString())) + "%";
            } else {
                sgst1 = "0.00%";
            }

            CGST.setText(cgst1);

            SGST.setText(sgst1);

//            totalAmount = totalAmount + Double.parseDouble(temp[3]);
//            totalTaxAmount = totalTaxAmount + Double.parseDouble(temp[4]);
//            subTotal = totalAmount + totalTaxAmount;

            return view;
        }
    }
}



