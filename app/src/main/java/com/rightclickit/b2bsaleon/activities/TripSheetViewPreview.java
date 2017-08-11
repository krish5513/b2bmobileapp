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
import com.rightclickit.b2bsaleon.adapters.TripsheetStockPreviewAdapter;
import com.rightclickit.b2bsaleon.adapters.TripsheetsSOListAdapter;
import com.rightclickit.b2bsaleon.beanclass.DeliverysBean;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
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

public class TripSheetViewPreview extends AppCompatActivity {
    private String tripSheetId, mTripSheetCode = "", mTripSheetDate = "", mTakeOrderPrivilege = "";

    String myList;

    private Context applicationContext, activityContext;
    private MMSharedPreferences mmSharedPreferences;

    private TextView tripsheet_no_text_view, sale_date_time_text_view, total_tax_amount_text_view, total_amount_text_view, sub_total_amount_text_view;
    private String loggedInUserId, loggedInUserName;
    private DBHelper mDBHelper;

    String str_routecode, str_Tripcode, str_Tripdate, str_AgentName, str_AgentCode, str_OB, str_Order, str_Received, str_Due;
    TextView company_name, route_name, route_code, user_name, sales_print;
    private ListView tdc_products_list_preview;
    private LinearLayout tdc_sales_save_layout;

    ArrayList<String[]> selectedList;
    private TextView ts_ob_amount, ts_order_value, ts_total_received, ts_total_due;

    private TripsheetsModel mTripsheetsModel;
    private TripsheetStockPreviewAdapter mTripsheetsStockPreviewAdapter;
    private Map<String, TripsheetsStockList> productsDispatchListHashMap, productsVerifyListHashMap;
    ListView listView;
    private ArrayList<TripsheetSOList> tripSheetSOList = new ArrayList<>();

    private TripSheetSoListPreviewAdapter mTripsheetSOAdapter;
    private NetworkConnectionDetector networkConnectionDetector;
    TripsheetsList currentTripSheetDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_sheet_view_preview);


        applicationContext = getApplicationContext();
        activityContext = TripSheetViewPreview.this;

        mmSharedPreferences = new MMSharedPreferences(applicationContext);
        loggedInUserId = mmSharedPreferences.getString("userId");
        loggedInUserName = mmSharedPreferences.getString("loginusername");
        str_routecode = (mmSharedPreferences.getString("routecode") + ",");
        str_Tripcode = mmSharedPreferences.getString("tripsheetCode");
        str_Tripdate = mmSharedPreferences.getString("tripsheetDate");

        mDBHelper = new DBHelper(TripSheetViewPreview.this);
        networkConnectionDetector = new NetworkConnectionDetector(activityContext);
        listView = (ListView) findViewById(R.id.tdc_sales_products_list_preview);


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


        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            tripSheetId = bundle.getString("tripSheetId");
        //str_Tripcode=bundle.getString("tripsheetCode");
        //str_Tripdate=bundle.getString("tripsheetDate");

        sales_print = (TextView) findViewById(R.id.tv_print);
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
        tripsheet_no_text_view.setText(str_Tripcode + ",");
        sale_date_time_text_view.setText(str_Tripdate);

        ts_ob_amount = (TextView) findViewById(R.id.ob_amount);
        ts_order_value = (TextView) findViewById(R.id.order_amount);
        ts_total_received = (TextView) findViewById(R.id.received_amount);
        ts_total_due = (TextView) findViewById(R.id.due_amount);

        currentTripSheetDetails = mDBHelper.fetchTripSheetDetails(tripSheetId);
        if (currentTripSheetDetails != null) {
            ts_ob_amount.setText(Utility.getFormattedCurrency(Double.parseDouble(currentTripSheetDetails.getmTripshhetOBAmount())));
            ts_order_value.setText(Utility.getFormattedCurrency(Double.parseDouble(currentTripSheetDetails.getmTripshhetOrderedAmount())));
            ts_total_received.setText(Utility.getFormattedCurrency(Double.parseDouble(currentTripSheetDetails.getmTripshhetReceivedAmount())));
            ts_total_due.setText(Utility.getFormattedCurrency(Double.parseDouble(currentTripSheetDetails.getmTripshhetDueAmount())));
        }

        tripSheetSOList = mDBHelper.getTripSheetSaleOrderDetails(tripSheetId);
        selectedList = new ArrayList<>(tripSheetSOList.size());


        for (int i = 0; i < tripSheetSOList.size(); i++) {
            str_AgentName = tripSheetSOList.get(i).getmTripshetSOAgentFirstName();
            str_AgentCode = tripSheetSOList.get(i).getmTripshetSOAgentCode();
            str_OB = tripSheetSOList.get(i).getmTripshetSOOpAmount();
            str_Order = tripSheetSOList.get(i).getmTripshetSOValue();
            str_Received = tripSheetSOList.get(i).getmTripshetSOReceivedAmount();
            str_Due = tripSheetSOList.get(i).getmTripshetSODueAmount();

            String[] temp = new String[6];
            temp[0] = str_AgentName;
            temp[1] = str_OB;
            temp[2] = str_Order;
            temp[3] = str_Received;
            temp[4] = str_Due;
            temp[5] = str_AgentCode;

            selectedList.add(temp);
        }


        TripSheetViewPreview.CustomListView adapter = new TripSheetViewPreview.CustomListView(selectedList, this);
        listView.setAdapter(adapter);


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
                canvas.drawText("TRIPSHEET SUMMARY,", 5, 120, paint);
                paint.setTextSize(20);
                canvas.drawText("by " + mmSharedPreferences.getString("loginusername"), 200, 120, paint);
                paint.setTextSize(20);
                canvas.drawText(str_Tripcode, 5, 150, paint);
                paint.setTextSize(20);
                canvas.drawText(str_Tripdate, 170, 150, paint);
                paint.setTextSize(20);

                canvas.drawText("OB", 5, 180, paint);
                paint.setTextSize(20);
                canvas.drawText("Order", 120, 180, paint);
                paint.setTextSize(20);
                canvas.drawText("Received", 210, 180, paint);
                paint.setTextSize(20);
                canvas.drawText("Due", 300, 180, paint);

                canvas.drawText(Utility.getFormattedCurrency(Double.parseDouble(currentTripSheetDetails.getmTripshhetOBAmount())), 5, 210, paint);
                canvas.drawText(Utility.getFormattedCurrency(Double.parseDouble(currentTripSheetDetails.getmTripshhetOrderedAmount())), 120, 210, paint);
                canvas.drawText(Utility.getFormattedCurrency(Double.parseDouble(currentTripSheetDetails.getmTripshhetReceivedAmount())), 210, 210, paint);
                canvas.drawText(Utility.getFormattedCurrency(Double.parseDouble(currentTripSheetDetails.getmTripshhetDueAmount())), 300, 210, paint);

                paint.setTextSize(22);
                canvas.drawText("Aents/Customers", 5, 240, paint);
                paint.setTextSize(20);

                canvas.drawText("----------------------------------------------------", 5, 270, paint);
                canvas.drawText("Name", 5, 300, paint);
                paint.setTextSize(20);
                canvas.drawText("OB", 110, 300, paint);
                paint.setTextSize(20);
                canvas.drawText("Order", 160, 300, paint);
                paint.setTextSize(20);
                canvas.drawText("Received", 230, 300, paint);
                paint.setTextSize(20);
                canvas.drawText("Due", 330, 300, paint);
                paint.setTextSize(20);
                canvas.drawText("----------------------------------------------------", 5, 330, paint);

                int st = 360;
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
        Intent intent = new Intent(this, TripSheetView.class);
        intent.putExtra("tripsheetId", tripSheetId);
        // intent.putExtra("tripsheetCode", mTripSheetCode);
        //intent.putExtra("tripsheetDate", mTripSheetDate);
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
            mOBValue.setText(temp[1]);
            mSOAgentName.setText(temp[0]);
            mSOOrderedValue.setText(temp[2]);
            mSOReceivedValue.setText(temp[3]);
            mSODueValue.setText(temp[4]);

            return view;
        }
    }

    public void closeTripSheet(View v) {
        mDBHelper.updateTripSheetStatus(tripSheetId);
    }
}
