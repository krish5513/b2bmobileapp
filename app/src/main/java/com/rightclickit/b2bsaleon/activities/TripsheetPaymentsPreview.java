package com.rightclickit.b2bsaleon.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.beanclass.PaymentsBean;
import com.rightclickit.b2bsaleon.beanclass.SaleOrderDeliveredProducts;
import com.rightclickit.b2bsaleon.beanclass.SaleOrderReturnedProducts;
import com.rightclickit.b2bsaleon.beanclass.TripsheetSOList;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;

public class TripsheetPaymentsPreview extends AppCompatActivity {
    private Context activityContext;
    private MMSharedPreferences mmSharedPreferences;
    private DBHelper mDBHelper;

    TextView tv_companyName, tv_routecode, tv_route_name, tv_delivered_user_Name, tv_sale_order_no, tv_sale_order_date, tv_delivery_no, tv_delivery_date, price_total, tax_total_amount, sub_total,
            mode_of_payment, cheque_number, cheque_date, bank_name, opening_balance, sale_order_amount, received_amount, closing_balance;
    ListView delivered_products_list_view, returned_products_list_view;

    private String mTripSheetId = "", mAgentId = "", mAgentName = "", mAgentCode = "", mAgentRouteId = "", mAgentRouteCode = "", mAgentSoId = "", mAgentSoCode = "", mAgentSoDate;
    private String loggedInUserId, loggedInUserName, companyName, routeCode, routeName, currentDate;
    private TripsheetSOList saleOrdersDetails = null;
    private ArrayList<SaleOrderDeliveredProducts> deliveredProductsList;
    private ArrayList<SaleOrderReturnedProducts> returnedProductsList;
    private PaymentsBean paymentsDetails = null;
    private double totalAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripsheet_payments_preview);

        try {
            this.getSupportActionBar().setTitle("PAYMENT SUMMARY");
            this.getSupportActionBar().setSubtitle(null);
            this.getSupportActionBar().setLogo(R.drawable.route_white);
            this.getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            this.getSupportActionBar().setDisplayShowHomeEnabled(true);

            final ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

            activityContext = TripsheetPaymentsPreview.this;
            mDBHelper = new DBHelper(activityContext);
            mmSharedPreferences = new MMSharedPreferences(activityContext);

            tv_companyName = (TextView) findViewById(R.id.tv_companyName);
            tv_routecode = (TextView) findViewById(R.id.tv_routecode);
            tv_route_name = (TextView) findViewById(R.id.tv_route_name);
            tv_delivered_user_Name = (TextView) findViewById(R.id.tv_delivered_user_Name);
            tv_sale_order_no = (TextView) findViewById(R.id.tv_sale_order_no);
            tv_sale_order_date = (TextView) findViewById(R.id.tv_sale_order_date);
            tv_delivery_no = (TextView) findViewById(R.id.tv_delivery_no);
            tv_delivery_date = (TextView) findViewById(R.id.tv_delivery_date);
            price_total = (TextView) findViewById(R.id.price_total);
            tax_total_amount = (TextView) findViewById(R.id.tax_total_amount);
            sub_total = (TextView) findViewById(R.id.sub_total);
            mode_of_payment = (TextView) findViewById(R.id.mode_of_payment);
            cheque_number = (TextView) findViewById(R.id.cheque_number);
            cheque_date = (TextView) findViewById(R.id.cheque_date);
            bank_name = (TextView) findViewById(R.id.bank_name);
            opening_balance = (TextView) findViewById(R.id.opening_balance);
            sale_order_amount = (TextView) findViewById(R.id.sale_order_amount);
            received_amount = (TextView) findViewById(R.id.received_amount);
            closing_balance = (TextView) findViewById(R.id.closing_balance);
            delivered_products_list_view = (ListView) findViewById(R.id.delivered_products_list_view);
            returned_products_list_view = (ListView) findViewById(R.id.returned_products_list_view);

            mTripSheetId = this.getIntent().getStringExtra("tripsheetId");
            mAgentId = this.getIntent().getStringExtra("agentId");
            mAgentCode = this.getIntent().getStringExtra("agentCode");
            mAgentName = this.getIntent().getStringExtra("agentName");
            mAgentSoId = this.getIntent().getStringExtra("agentSoId");
            mAgentSoCode = this.getIntent().getStringExtra("agentSoCode");
            mAgentSoDate = this.getIntent().getStringExtra("agentSoDate");

            loggedInUserId = mmSharedPreferences.getString("userId");
            loggedInUserName = mmSharedPreferences.getString("loginusername");
            companyName = mmSharedPreferences.getString("companyname");
            routeCode = mmSharedPreferences.getString("routecode") + ",";
            routeName = mmSharedPreferences.getString("routename");
            currentDate = Utility.formatTime(System.currentTimeMillis(), Constants.TDC_SALE_INFO_DATE_DISPLAY_FORMAT);

            saleOrdersDetails = mDBHelper.fetchTripSheetSaleOrderDetails(mTripSheetId, mAgentSoId, mAgentId);
            deliveredProductsList = mDBHelper.getDeliveredProductsListForSaleOrder(mTripSheetId, mAgentSoId, mAgentId);
            returnedProductsList = mDBHelper.getReturnsProductsListForSaleOrder(mTripSheetId, mAgentSoId, mAgentId);
            paymentsDetails = mDBHelper.getSaleOrderPaymentDetails(mTripSheetId, mAgentSoId);

            // Updating UI with fetched values.
            tv_companyName.setText(companyName);
            tv_routecode.setText(routeCode);
            tv_route_name.setText(routeName);
            tv_delivered_user_Name.setText("by " + loggedInUserName);

            if (saleOrdersDetails != null) {
                if (saleOrdersDetails.getmTripshetSOCode().isEmpty())
                    tv_sale_order_no.setText("-");
                else
                    tv_sale_order_no.setText(String.format("Sale # %s", saleOrdersDetails.getmTripshetSOCode()));

                if (saleOrdersDetails.getmTripshetSODate().isEmpty())
                    tv_sale_order_date.setText("Sale # -");
                else
                    tv_sale_order_date.setText(saleOrdersDetails.getmTripshetSODate());

                opening_balance.setText(Utility.getFormattedCurrency(Double.parseDouble(saleOrdersDetails.getmTripshetSOOpAmount())));
                sale_order_amount.setText(Utility.getFormattedCurrency(Double.parseDouble(saleOrdersDetails.getmTripshetSOValue())));
                received_amount.setText(Utility.getFormattedCurrency(Double.parseDouble(saleOrdersDetails.getmTripshetSOReceivedAmount())));
                closing_balance.setText(Utility.getFormattedCurrency(Double.parseDouble(saleOrdersDetails.getmTripshetSOCBAmount())));
            }

            if (deliveredProductsList.size() > 0) {
                for (SaleOrderDeliveredProducts products : deliveredProductsList)
                    totalAmount = totalAmount + Double.parseDouble(products.getProductAmount());

                SaleOrderDeliveredProducts deliveredProduct = deliveredProductsList.get(0);
                tv_delivery_no.setText(String.format("Delivery # RD%03d", deliveredProduct.getDeliveryNo()));
                tv_delivery_date.setText(Utility.formatTime(Long.parseLong(deliveredProduct.getCreatedTime()), Constants.TDC_SALE_INFO_DATE_DISPLAY_FORMAT));

                tax_total_amount.setText(Utility.getFormattedCurrency(Double.parseDouble(deliveredProduct.getTotalTax())));
                price_total.setText(Utility.getFormattedCurrency(totalAmount));
                sub_total.setText(Utility.getFormattedCurrency(Double.parseDouble(deliveredProduct.getSubTotal())));
            }

            if (paymentsDetails != null) {
                mode_of_payment.setText(paymentsDetails.getPayments_type().equals("0") ? "Cash" : "Cheque");
                cheque_number.setText(paymentsDetails.getPayments_chequeNumber());
                cheque_date.setText(paymentsDetails.getPayments_chequeDate());
                bank_name.setText(paymentsDetails.getPayments_bankName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, TripsheetPayments.class);
        i.putExtra("tripsheetId", mTripSheetId);
        i.putExtra("agentId", mAgentId);
        i.putExtra("agentCode", mAgentCode);
        i.putExtra("agentName", mAgentName);
        i.putExtra("agentRouteId", mAgentRouteId);
        i.putExtra("agentRouteCode", mAgentRouteCode);
        i.putExtra("agentSoId", mAgentSoId);
        i.putExtra("agentSoCode", mAgentSoCode);
        startActivity(i);
        finish();
    }
}
