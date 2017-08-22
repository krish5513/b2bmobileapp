package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.AgentDeliveries;
import com.rightclickit.b2bsaleon.activities.AgentDeliveriesView;
import com.rightclickit.b2bsaleon.activities.AgentPayments;
import com.rightclickit.b2bsaleon.activities.AgentPaymentsView;
import com.rightclickit.b2bsaleon.beanclass.AgentDeliveriesBean;
import com.rightclickit.b2bsaleon.beanclass.AgentPaymentsBean;
import com.rightclickit.b2bsaleon.beanclass.DeliverysBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.TripSheetDeliveriesListener;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by PPS on 8/20/2017.
 */

public class AgentPaymentsAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context ctxt;
    private Activity activity;
    private TripSheetDeliveriesListener listener;
    private ArrayList<AgentPaymentsBean> allDeliveryProductsList, filteredDeliveryProductsList;
    private Map<String, DeliverysBean> selectedDeliveryProductsHashMap; // Hash Map Key = Product Id
    private Map<String, String> previouslyDeliveredProductsHashMap;
    private Map<String, String> productOrderQuantitiesHashMap;
    private final String zero_cost = "0.000";
    private boolean isDeliveryInEditingMode = false;
    DBHelper mdbhelper;
    private MMSharedPreferences mPreferences;

    public AgentPaymentsAdapter(Context ctxt, AgentPayments deliveryActivity, ArrayList<AgentPaymentsBean> mdeliveriesBeanList) {
        this.ctxt = ctxt;
        this.activity = deliveryActivity;
        this.mInflater = LayoutInflater.from(activity);
        mdbhelper=new DBHelper(ctxt);
        this.allDeliveryProductsList = mdeliveriesBeanList;
        this.filteredDeliveryProductsList = new ArrayList<>();
        this.filteredDeliveryProductsList.addAll(allDeliveryProductsList);
        this.selectedDeliveryProductsHashMap = new HashMap<>();
        this.mPreferences = new MMSharedPreferences(activity);

    }

    public class TripSheetDeliveriesViewHolder {
        TextView Payment_no, Paymeent_date, Payment_status, payment_obAmount, payment_ordervalue,payment_totalAmount,payment_totalDue;
        Button View;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }


    @Override
    public int getCount() {
        return filteredDeliveryProductsList.size();
    }



    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final AgentPaymentsAdapter.TripSheetDeliveriesViewHolder tripSheetDeliveriesViewHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.agentpayments_custom, null);

            tripSheetDeliveriesViewHolder = new AgentPaymentsAdapter.TripSheetDeliveriesViewHolder();
            tripSheetDeliveriesViewHolder.Payment_no = (TextView) view.findViewById(R.id.tv_prid);
            tripSheetDeliveriesViewHolder.Paymeent_date = (TextView) view.findViewById(R.id.payment_date);
            tripSheetDeliveriesViewHolder.Payment_status = (TextView) view.findViewById(R.id.payment_status);
            tripSheetDeliveriesViewHolder.payment_obAmount = (TextView) view.findViewById(R.id.ob_amount);
            tripSheetDeliveriesViewHolder.payment_ordervalue = (TextView) view.findViewById(R.id.ordervalue);
            tripSheetDeliveriesViewHolder.payment_totalAmount = (TextView) view.findViewById(R.id.totalAmount);
            tripSheetDeliveriesViewHolder.payment_totalDue = (TextView) view.findViewById(R.id.totaldue);
            tripSheetDeliveriesViewHolder.View = (Button) view.findViewById(R.id.btn_view1);


            view.setTag(tripSheetDeliveriesViewHolder);
        } else {
            tripSheetDeliveriesViewHolder = (AgentPaymentsAdapter.TripSheetDeliveriesViewHolder) view.getTag();
        }

        final AgentPaymentsAdapter.TripSheetDeliveriesViewHolder currentTripSheetDeliveriesViewHolder = tripSheetDeliveriesViewHolder;

        final AgentPaymentsBean currentDeliveryBean = allDeliveryProductsList.get(position);

        tripSheetDeliveriesViewHolder.Payment_no.setText(currentDeliveryBean.getPayment_Number());
        tripSheetDeliveriesViewHolder.Paymeent_date.setText(getDate(currentDeliveryBean.getPayment_date(),"dd-MM-yyyy"));
        tripSheetDeliveriesViewHolder.Payment_status.setText(currentDeliveryBean.getPayment_status());
        tripSheetDeliveriesViewHolder.payment_obAmount.setText(currentDeliveryBean.getPayment_OBamount());
        tripSheetDeliveriesViewHolder.payment_ordervalue.setText(currentDeliveryBean.getPayment_ordervalue());
        tripSheetDeliveriesViewHolder.payment_totalAmount.setText(currentDeliveryBean.getPayment_totalamount());
        tripSheetDeliveriesViewHolder.payment_totalDue.setText(currentDeliveryBean.getPayment_totaldue());

        tripSheetDeliveriesViewHolder.View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mPreferences.putString("DeliveryNo",currentDeliveryBean.getTripNo());
                Intent i=new Intent(activity,AgentPaymentsView.class);
                i.putExtra("PaymentNo",currentDeliveryBean.getPayment_Number());
                i.putExtra("Paymentdate",getDate(currentDeliveryBean.getPayment_date(),"dd-MM-yyyy"));
                activity.startActivity(i);
                activity.finish();
            }
        });
        return view;

    }
    public  String getDate(String time, String format) {
        try {
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(Long.parseLong(time));
            return DateFormat.format(format, cal).toString();
        }
        catch (Exception e){
            return "XX-XX-XXXX";
        }
    }


    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        filteredDeliveryProductsList.clear();

        if (charText.length() == 0) {
            filteredDeliveryProductsList.addAll(allDeliveryProductsList);
        } else {
            for (AgentPaymentsBean deliverysBean : allDeliveryProductsList) {
                if (deliverysBean.getPayment_Number().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredDeliveryProductsList.add(deliverysBean);
                } else if (deliverysBean.getPayment_date().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredDeliveryProductsList.add(deliverysBean);
                }
            }
        }

        notifyDataSetChanged();
    }
}




