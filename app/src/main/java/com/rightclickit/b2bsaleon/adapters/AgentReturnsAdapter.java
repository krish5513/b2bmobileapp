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
import com.rightclickit.b2bsaleon.activities.AgentReturns;
import com.rightclickit.b2bsaleon.activities.AgentReturnsView;
import com.rightclickit.b2bsaleon.beanclass.TripSheetReturnsBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.TripSheetDeliveriesListener;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by PPS on 8/19/2017.
 */

public class AgentReturnsAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private Context ctxt;
    private Activity activity;
    private TripSheetDeliveriesListener listener;
    private ArrayList<TripSheetReturnsBean> allDeliveryProductsList, filteredDeliveryProductsList;

    private final String zero_cost = "0.000";
    private boolean isDeliveryInEditingMode = false;
    DBHelper mdbhelper;
    private MMSharedPreferences mPreferences;

    public AgentReturnsAdapter(Context ctxt, AgentReturns returnsActivity, ArrayList<TripSheetReturnsBean> mdeliveriesBeanList) {
        this.ctxt = ctxt;
        this.activity = returnsActivity;
        this.mInflater = LayoutInflater.from(activity);
        mdbhelper=new DBHelper(ctxt);
        this.allDeliveryProductsList = mdeliveriesBeanList;
        this.filteredDeliveryProductsList = new ArrayList<>();
        this.filteredDeliveryProductsList.addAll(allDeliveryProductsList);
        this.mPreferences = new MMSharedPreferences(activity);

    }

    public class TripSheetDeliveriesViewHolder {
        TextView returns_no, returns_date, return_status, items_count, returns_by;
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
        final AgentReturnsAdapter.TripSheetDeliveriesViewHolder tripSheetDeliveriesViewHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.agentreturns_custom, null);

            tripSheetDeliveriesViewHolder = new AgentReturnsAdapter.TripSheetDeliveriesViewHolder();
            tripSheetDeliveriesViewHolder.returns_no = (TextView) view.findViewById(R.id.tv_returnno);
            tripSheetDeliveriesViewHolder.returns_date = (TextView) view.findViewById(R.id.tv_date);
            tripSheetDeliveriesViewHolder.return_status = (TextView) view.findViewById(R.id.tv_returnstatus);
            tripSheetDeliveriesViewHolder.items_count = (TextView) view.findViewById(R.id.tv_itemscount);
            tripSheetDeliveriesViewHolder.returns_by = (TextView) view.findViewById(R.id.tv_returnsby);
            tripSheetDeliveriesViewHolder.View = (Button) view.findViewById(R.id.btn_view1);


            view.setTag(tripSheetDeliveriesViewHolder);
        } else {
            tripSheetDeliveriesViewHolder = (AgentReturnsAdapter.TripSheetDeliveriesViewHolder) view.getTag();
        }

        final AgentReturnsAdapter.TripSheetDeliveriesViewHolder currentTripSheetDeliveriesViewHolder = tripSheetDeliveriesViewHolder;

        final TripSheetReturnsBean currentReturnsBean = allDeliveryProductsList.get(position);

        tripSheetDeliveriesViewHolder.returns_no.setText(currentReturnsBean.getmTripshhetReturnsReturn_number());
        tripSheetDeliveriesViewHolder.returns_date.setText(getDate(currentReturnsBean.getmTripshhetReturnsCreated_on(),"dd-MM-yyyy"));
        tripSheetDeliveriesViewHolder.return_status.setText(currentReturnsBean.getmTripshhetReturnsStatus());
        tripSheetDeliveriesViewHolder.items_count.setText(currentReturnsBean.getReturnsItemsCount());
        tripSheetDeliveriesViewHolder.returns_by.setText(mdbhelper.getDeliveryName(currentReturnsBean.getmTripshhetReturnsCreated_by()));

        tripSheetDeliveriesViewHolder.View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mPreferences.putString("DeliveryNo",currentDeliveryBean.getTripNo());
                Intent i=new Intent(activity,AgentReturnsView.class);
                i.putExtra("ReturnNo",currentReturnsBean.getmTripshhetReturnsReturn_number());
                i.putExtra("Returndate",getDate(currentReturnsBean.getmTripshhetReturnsCreated_on(),"dd-MM-yyyy"));
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
            for (TripSheetReturnsBean deliverysBean : allDeliveryProductsList) {
                if (deliverysBean.getmTripshhetReturnsReturn_no().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredDeliveryProductsList.add(deliverysBean);
                } else if (deliverysBean.getmTripshhetReturnsCreated_on().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredDeliveryProductsList.add(deliverysBean);
                }
            }
        }

        notifyDataSetChanged();
    }
}




