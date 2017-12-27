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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TripSheetSoListPreviewAdapter;
import com.rightclickit.b2bsaleon.adapters.TripSheetsPaymentPreviewReturnedProductsAdapter;
import com.rightclickit.b2bsaleon.adapters.TripsheetStockPreviewAdapter;
import com.rightclickit.b2bsaleon.beanclass.PaymentsBean;
import com.rightclickit.b2bsaleon.beanclass.SaleOrderReturnedProducts;
import com.rightclickit.b2bsaleon.beanclass.TripsheetSOList;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsList;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.TripsheetsModel;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class TripSheetViewPreview extends AppCompatActivity {
    private String tripSheetId, mTripSheetCode = "", mTripSheetDate = "", mTakeOrderPrivilege = "";

    String myList;

    private Context applicationContext, activityContext;
    private MMSharedPreferences mmSharedPreferences;

    private TextView tripsheet_no_text_view, sale_date_time_text_view, total_tax_amount_text_view, total_amount_text_view, sub_total_amount_text_view;
    private String loggedInUserId, loggedInUserName,routecode;
    private DBHelper mDBHelper;

    String str_routecode, str_Tripcode, str_Tripdate, str_AgentName, str_AgentCode, str_OB, str_Order, str_Received, str_Due, mAgentSoId, mAgentId, uom,status;
    TextView company_name, route_name, route_code, user_name, sales_print,mCratesNameText,mCratesOBText,mCratesDeliverText,mCratesReturnText,mCratesCBText,cashAmount,chequeAmount;
    private ListView tdc_products_list_preview;
    private LinearLayout close_trip_layout;

    ArrayList<String[]> selectedList, cratesList;
    private TextView ts_ob_amount, ts_order_value, ts_total_received, ts_total_due, total_deliverQuantity, total_returnQuantity, crates_deliveredQuantity, crates_returnQuantity;

    private TripsheetsModel mTripsheetsModel;
    private TripsheetStockPreviewAdapter mTripsheetsStockPreviewAdapter;
    private Map<String, TripsheetsStockList> productsDispatchListHashMap, productsVerifyListHashMap;
    ListView listView;
    private ArrayList<TripsheetSOList> tripSheetSOList = new ArrayList<>();

    private TripSheetSoListPreviewAdapter mTripsheetSOAdapter;
    private NetworkConnectionDetector networkConnectionDetector;
    TripsheetsList currentTripSheetDetails;
    private boolean isTripSheetClosed = false;
    private ArrayList<SaleOrderReturnedProducts> returnedProductsList;
    private TripSheetsPaymentPreviewReturnedProductsAdapter tripSheetsPaymentPreviewReturnedProductsAdapter;
    ListView returned_products_list_view;
    double dq = 0.0, fdq=0.0,ob=0.0,fobq=0.0,cb=0.0,fcb=0.0,orderTotal=0.0;
    double rq = 0.0;
    double frq=0.0;
    double cashA;
    double chequeA;
    private PaymentsBean paymentsDetails = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_sheet_view_preview);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tripSheetId = bundle.getString("tripSheetId");
            status=bundle.getString("status");
            //str_Tripcode=bundle.getString("tripsheetCode");
            //str_Tripdate=bundle.getString("tripsheetDate");
        }

        pageLoad();
    }

    public void pageLoad(){
        try {
            applicationContext = getApplicationContext();
            activityContext = TripSheetViewPreview.this;

            mmSharedPreferences = new MMSharedPreferences(applicationContext);
            loggedInUserId = mmSharedPreferences.getString("userId");
            loggedInUserName = mmSharedPreferences.getString("loginusername");
            str_routecode = (mmSharedPreferences.getString("routecode") + ",");
            str_Tripcode = mmSharedPreferences.getString("tripsheetCode");
            str_Tripdate = mmSharedPreferences.getString("tripsheetDate");
            routecode= mmSharedPreferences.getString("tripsheetroutecode");
            mDBHelper = new DBHelper(TripSheetViewPreview.this);
            networkConnectionDetector = new NetworkConnectionDetector(activityContext);
            listView = (ListView) findViewById(R.id.tdc_sales_products_list_preview);
            returned_products_list_view = (ListView) findViewById(R.id.tdc_sales_crates_list);

            this.getSupportActionBar().setTitle("DAILY TRIPSHEET");
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

            cashAmount=(TextView)findViewById(R.id.cashAmt);
            chequeAmount=(TextView)findViewById(R.id.chequeAmt);


            mAgentSoId = mDBHelper.fetchTripSheetSaleorderNo(tripSheetId);
            mAgentId = mDBHelper.fetchTripSheetagentid(tripSheetId);
            sales_print = (TextView) findViewById(R.id.tv_print);
            company_name = (TextView) findViewById(R.id.tdc_sales_company_name);
            user_name = (TextView) findViewById(R.id.tdc_sales_user_name);
            //route_name = (TextView) findViewById(R.id.tdc_sales_route_name);
            // route_code = (TextView) findViewById(R.id.tdc_sales_route_code);
            tripsheet_no_text_view = (TextView) findViewById(R.id.tripsheet_no);
            sale_date_time_text_view = (TextView) findViewById(R.id.tdc_sales_date_time);
            tdc_products_list_preview = (ListView) findViewById(R.id.tdc_sales_products_list_preview);

            //  crates_deliveredQuantity = (TextView) findViewById(R.id.delivery_qty);
            //  crates_returnQuantity = (TextView) findViewById(R.id.return_qty);



            close_trip_layout = (LinearLayout) findViewById(R.id.close_trip_layout);

            company_name.setText(mmSharedPreferences.getString("companyname"));
            user_name.setText("by " + loggedInUserName);
            // route_name.setText(mmSharedPreferences.getString("routename"));
            //  route_code.setText(str_routecode);
            tripsheet_no_text_view.setText(str_Tripcode + ",");
            sale_date_time_text_view.setText(str_Tripdate);

            ts_ob_amount = (TextView) findViewById(R.id.ob_amount);
            ts_order_value = (TextView) findViewById(R.id.order_amount);
            ts_total_received = (TextView) findViewById(R.id.received_amount);
            ts_total_due = (TextView) findViewById(R.id.due_amount);

            // total_deliverQuantity = (TextView) findViewById(R.id.delivery_qty);
            // total_returnQuantity = (TextView) findViewById(R.id.return_qty);

            mCratesNameText = (TextView) findViewById(R.id.CratesNameText);
            mCratesOBText = (TextView) findViewById(R.id.CratesObAmount);
            mCratesDeliverText = (TextView) findViewById(R.id.CratesOrderedAmount);
            mCratesReturnText = (TextView) findViewById(R.id.CratesReceivedAmount);
            mCratesCBText = (TextView) findViewById(R.id.CratesCBAmount);

            isTripSheetClosed = mDBHelper.isTripSheetClosed(tripSheetId);
            returnedProductsList = mDBHelper.getReturnsProductsListForSaleOrder1(tripSheetId);
            //returnedProductsList = mDBHelper.getReturnsProductsListForSaleOrder(tripSheetId, mAgentSoId, mAgentId);
//            if (isTripSheetClosed) {
//                close_trip_layout.setVisibility(View.GONE);
//            }

            currentTripSheetDetails = mDBHelper.fetchTripSheetDetails(tripSheetId);






            //cashAmount.setText(String.valueOf(cashA));
            //chequeAmount.setText(String.valueOf(chequeA));

            tripSheetSOList = mDBHelper.getTripSheetSaleOrderDetails(tripSheetId);
            selectedList = new ArrayList<>(tripSheetSOList.size());

            for (int i = 0; i < tripSheetSOList.size(); i++) {
                str_AgentName = tripSheetSOList.get(i).getmTripshetSOAgentFirstName();
                str_AgentCode = tripSheetSOList.get(i).getmTripshetSOAgentCode();
                str_OB = tripSheetSOList.get(i).getmTripshetSOOpAmount();
                str_Order = tripSheetSOList.get(i).getmTripshetSOValue();
                if(Double.parseDouble(tripSheetSOList.get(i).getmTripshetSOReceivedAmount()) == -0.00000001){
                    str_Received = "0.0";
                }else {
                    str_Received = tripSheetSOList.get(i).getmTripshetSOReceivedAmount();
                }
                str_Due = tripSheetSOList.get(i).getmTripshetSODueAmount();
                orderTotal +=Double.parseDouble(tripSheetSOList.get(i).getmTripshetSOValue());

                String[] temp = new String[6];
                temp[0] = str_AgentName;
                temp[1] = Utility.getFormattedCurrency(Double.parseDouble(str_OB));
                temp[2] = Utility.getFormattedCurrency(Double.parseDouble(str_Order));
                temp[3] = Utility.getFormattedCurrency(Double.parseDouble(str_Received));
                temp[4] = Utility.getFormattedCurrency(Double.parseDouble(str_Due));
                temp[5] = str_AgentCode;

                selectedList.add(temp);
            }


            if (currentTripSheetDetails != null) {
                ts_ob_amount.setText(Utility.getFormattedCurrency(Double.parseDouble(currentTripSheetDetails.getmTripshhetOBAmount())));
                //ts_order_value.setText(Utility.getFormattedCurrency(Double.parseDouble(currentTripSheetDetails.getmTripshhetOrderedAmount())));
                ts_order_value.setText(Utility.getFormattedCurrency(orderTotal));
                ts_total_received.setText(Utility.getFormattedCurrency(Double.parseDouble(currentTripSheetDetails.getmTripshhetReceivedAmount())));
                ts_total_due.setText(Utility.getFormattedCurrency(Double.parseDouble(currentTripSheetDetails.getmTripshhetDueAmount())));
                cashAmount.setText(currentTripSheetDetails.getmCashPayment());
                chequeAmount.setText(currentTripSheetDetails.getmChequePayment());
            }

            TripSheetViewPreview.CustomListView adapter = new TripSheetViewPreview.CustomListView(selectedList, this);
            listView.setAdapter(adapter);
            Utility.setListViewHeightBasedOnChildren(listView);


            cratesList = new ArrayList<>(returnedProductsList.size());


            if (returnedProductsList.size() > 0) {

                for (SaleOrderReturnedProducts crates : returnedProductsList) {

                    String[] temp = new String[9];
                    String aName = mDBHelper.getAgentNameById(crates.getAgentId());
                    String aCode = mDBHelper.getAgentCodeById(crates.getAgentId());
                    temp[0] = aName;
                    temp[1]=aCode;
                    temp[2]=crates.getName();
                    temp[5] = Utility.getFormattedCurrency(Double.parseDouble(String.valueOf(crates.getOpeningBalance())));
                    temp[6] = Utility.getFormattedCurrency(Double.parseDouble(crates.getDelivered()));
                    temp[7] = Utility.getFormattedCurrency(Double.parseDouble(String.valueOf(crates.getClosingBalance())));
                    temp[8] = Utility.getFormattedCurrency(Double.parseDouble(String.valueOf(crates.getReturned())));
                    temp[3] = crates.getCode();
                    uom = mDBHelper.getProductUnitByProductCode(crates.getCode());
                    temp[4] = uom;
                    cratesList.add(temp);

                    // Added by Sekhar
                    dq = dq + Double.parseDouble(String.valueOf(crates.getDelivered()));
                    //fdq = dq + Double.parseDouble(total_deliverQuantity.getText().toString().trim());
                    //total_deliverQuantity.setText(String.valueOf(fdq));

                    rq = rq + Double.parseDouble(String.valueOf(crates.getReturned()));
                    //frq = rq + Double.parseDouble(total_returnQuantity.getText().toString().trim());
                    //total_returnQuantity.setText(String.valueOf(frq));

                    ob = ob + Double.parseDouble(crates.getOpeningBalance());

                    cb = cb + Double.parseDouble(crates.getClosingBalance());

                    mCratesNameText.setText(crates.getName() + "\n"+crates.getCode());
                    mCratesOBText.setText(String.valueOf(ob));
                    mCratesDeliverText.setText(String.valueOf(dq));
                    mCratesReturnText.setText(String.valueOf(rq));
                    mCratesCBText.setText(String.valueOf(cb));
                }

            }

            tripSheetsPaymentPreviewReturnedProductsAdapter = new TripSheetsPaymentPreviewReturnedProductsAdapter(activityContext, this, returnedProductsList);
            returned_products_list_view.setAdapter(tripSheetsPaymentPreviewReturnedProductsAdapter);
            Utility.setListViewHeightBasedOnChildren(returned_products_list_view);
            sales_print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pageheight = 800 + selectedList.size() * 180 + cratesList.size()*180;

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
                    canvas.drawText(mmSharedPreferences.getString("companyname"), 5, 20, paint);
                    paint.setTextSize(20);
                    canvas.drawText(routecode, 5, 50, paint);
                    paint.setTextSize(20);
                    canvas.drawText("-------------------------------------------", 5, 80, paint);
                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    paint.setTextSize(20);
                    canvas.drawText("TRIP SUMMARY", 100, 110, paint);
                    paint.setTextSize(20);
                    canvas.drawText("TRIP #,DATE ", 5, 140, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + str_Tripcode +", " + str_Tripdate, 150, 140, paint);
                   /* paint.setTextSize(20);
                    canvas.drawText("DATE ", 5, 170, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + str_Tripdate, 150, 170, paint);
                   */ paint.setTextSize(20);
                    canvas.drawText("-------------------------------------------", 5, 170, paint);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    paint.setTextSize(20);
                    canvas.drawText("TRIP PAYMENT SUMMARY", 5, 200, paint);

                   /* paint.setTextSize(20);
                    canvas.drawText("OB ", 5, 200, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + Utility.getFormattedCurrency(Double.parseDouble(currentTripSheetDetails.getmTripshhetOBAmount())), 150, 200, paint);
                   */ paint.setTextSize(20);
                    canvas.drawText("ORDER ", 5, 230, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + Utility.getFormattedCurrency(Double.parseDouble(currentTripSheetDetails.getmTripshhetOrderedAmount())), 150, 230, paint);
                    paint.setTextSize(20);
                    canvas.drawText("RECEIVED ", 5, 260, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + Utility.getFormattedCurrency(Double.parseDouble(currentTripSheetDetails.getmTripshhetReceivedAmount())), 150, 260, paint);
                   /* paint.setTextSize(20);
                    canvas.drawText("CB ", 5, 290, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + Utility.getFormattedCurrency(Double.parseDouble(currentTripSheetDetails.getmTripshhetDueAmount())), 150, 290, paint);
*/
                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    paint.setTextSize(20);
                    canvas.drawText("-------------------------------------------", 5, 290, paint);

                    paint.setTextSize(20);
                    canvas.drawText("CUSTOMER WISE PAYMENT SUMMARY", 5, 320, paint);
                    int st = 350;
                    paint.setTextSize(17);
                    // for (Map.Entry<String, String[]> entry : selectedList.entrySet()) {

                    for (int i = 0; i < selectedList.size(); i++) {
                        String[] temp = selectedList.get(i);
                        paint.setTextSize(20);
                        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        // canvas.drawText(temps[0] + "," + temps[1] + "( " + temps[2] + " )", 5, st, paint);
                        canvas.drawText(temp[0] + "," + temp[5], 5, st, paint);

                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("OB ", 5, st, paint);
                        canvas.drawText(": " + temp[1], 150, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("ORDER ", 5, st, paint);
                        canvas.drawText(": " + temp[2], 150, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("RECEIVED ", 5, st, paint);
                        canvas.drawText(": " + temp[3], 150, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("CB ", 5, st, paint);
                        canvas.drawText(": " + temp[4], 150, st, paint);


                        st = st + 40;
                    }
                    paint.setTextSize(20);
                    canvas.drawText("-------------------------------------------", 5, st, paint);


                   /* st = st + 30;
                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    paint.setTextSize(20);
                    canvas.drawText("TRIP CREATE SUMMARY", 5, st, paint);
                    st = st + 30;
                    // paint.setTextSize(20);
                    // canvas.drawText("OB QTY", 5, st, paint);
                    // paint.setTextSize(20);
                    //canvas.drawText(": " + "50.000", 150, st, paint);
                    // st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("DELIVER QTY ", 5, st, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + Utility.getFormattedCurrency(fdq), 150, st, paint);
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("RETURN QTY ", 5, st, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + Utility.getFormattedCurrency(frq), 150, st, paint);
                    //st = st + 30;
                    // paint.setTextSize(20);
                    // canvas.drawText("CB QTY", 5, st, paint);
                    // paint.setTextSize(20);
                    // canvas.drawText(": " + "55.000", 150, st, paint);
                    st = st + 30;
                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    paint.setTextSize(20);
                    canvas.drawText("-------------------------------------------", 5, st, paint);
*/
                    st = st + 30;
                    paint.setTextSize(20);
                    canvas.drawText("CUSTOMER WISE CRATES SUMMARY", 5, st, paint);
                    st = st + 30;
                    for (int i = 0; i < cratesList.size(); i++) {
                        String[] temp = cratesList.get(i);
                        paint.setTextSize(20);
                        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        canvas.drawText(temp[0] + "," +temp[1], 5, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        canvas.drawText(temp[2] + "," + temp[3] + "(" + temp[4] + ")", 5, st, paint);

                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("OB QTY ", 5, st, paint);
                        canvas.drawText(": " + temp[5], 150, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("DELIVER QTY ", 5, st, paint);
                        canvas.drawText(": " + temp[6], 150, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("RETURN QTY ", 5, st, paint);
                        canvas.drawText(": " + temp[7], 150, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("CB QTY", 5, st, paint);
                        canvas.drawText(": " + temp[8], 150, st, paint);


                        st = st + 40;
                    }

                    paint.setTextSize(20);
                    canvas.drawText("* Please take photocopy of the Bill *", 17, st, paint);
                    st = st + 30;

                    canvas.drawText("--------X---------", 100, st, paint);
                    com.szxb.api.jni_interface.api_interface.printBitmap(bmOverlay, 5, 5);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
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
        menu.findItem(R.id.sort).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent();
        intent.putExtra("tripsheetId",tripSheetId);
        setResult(101,intent);
        finish();//finishing activity

        /*Intent intent = new Intent(this, TripSheetView.class);
        intent.putExtra("tripsheetId", tripSheetId);
        intent.putExtra("status",status);
        // intent.putExtra("tripsheetCode", mTripSheetCode);
        //intent.putExtra("tripsheetDate", mTripSheetDate);
        startActivity(intent);
        finish();*/
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
                view = inflater.inflate(R.layout.tripsheetsummary_preview, null);
            }
            String[] temp = list.get(position);
            TextView mAgentCode = (TextView) view.findViewById(R.id.agentCode);
            TextView mOBValue = (TextView) view.findViewById(R.id.OB);
            TextView mSOAgentName = (TextView) view.findViewById(R.id.AgentName);
            TextView mSOOrderedValue = (TextView) view.findViewById(R.id.order);
            TextView mSOReceivedValue = (TextView) view.findViewById(R.id.received);
            TextView mSODueValue = (TextView) view.findViewById(R.id.due);
            mAgentCode.setText(temp[5]);
            mOBValue.setText((temp[1]));
            mSOAgentName.setText(temp[0]);
            mSOOrderedValue.setText((temp[2]));
            mSOReceivedValue.setText((temp[3]));
            mSODueValue.setText((temp[4]));

            return view;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 101) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                tripSheetId = data.getStringExtra("tripSheetId");
                pageLoad();
                // Do something with the contact here (bigger example below)
            }
        }
    }

    public void closeTripSheet(View v) {

        Intent i = new Intent(TripSheetViewPreview.this, RouteStock.class);
        i.putExtra("tripsheetId", tripSheetId);
        i.putExtra("status",status);
        startActivityForResult(i,101);
       // finish();
       /* try {
            AlertDialog alertDialog = null;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activityContext, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle("User Action!");
            alertDialogBuilder.setMessage("Are you sure you want to close this trip sheet?");
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    int status = mDBHelper.updateTripSheetClosingStatus(tripSheetId);

                    if (status > 0) {
                        Toast.makeText(activityContext, "Trip sheet closed successfully.", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(activityContext, TripSheetsActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(activityContext, "Trip sheet closing failed.", Toast.LENGTH_LONG).show();
                    }
                }
            });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            Button cancelButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (cancelButton != null)
                cancelButton.setTextColor(ContextCompat.getColor(activityContext, R.color.alert_dialog_color_accent));

        } catch (Exception e) {
            e.printStackTrace();
        }
*/
    }
}