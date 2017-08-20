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
import com.rightclickit.b2bsaleon.activities.AgentReturns;
import com.rightclickit.b2bsaleon.activities.AgentReturnsView;
import com.rightclickit.b2bsaleon.beanclass.AgentDeliveriesBean;
import com.rightclickit.b2bsaleon.beanclass.AgentReturnsBean;
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
 * Created by PPS on 8/19/2017.
 */

public class AgentReturnsAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private Context ctxt;
    private Activity activity;
    private TripSheetDeliveriesListener listener;
    private ArrayList<AgentReturnsBean> allDeliveryProductsList, filteredDeliveryProductsList;

    private final String zero_cost = "0.000";
    private boolean isDeliveryInEditingMode = false;
    DBHelper mdbhelper;
    private MMSharedPreferences mPreferences;

    public AgentReturnsAdapter(Context ctxt, AgentReturns returnsActivity, ArrayList<AgentReturnsBean> mdeliveriesBeanList) {
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

        final AgentReturnsBean currentReturnsBean = allDeliveryProductsList.get(position);

        tripSheetDeliveriesViewHolder.returns_no.setText(currentReturnsBean.getReturnNo());
        tripSheetDeliveriesViewHolder.returns_date.setText(getDate(currentReturnsBean.getReturnDate(),"dd-MM-yyyy"));
        tripSheetDeliveriesViewHolder.return_status.setText(currentReturnsBean.getReturnStatus());
        tripSheetDeliveriesViewHolder.items_count.setText(currentReturnsBean.getReturnedItems());
        tripSheetDeliveriesViewHolder.returns_by.setText(mdbhelper.getDeliveryName(currentReturnsBean.getReturnedBy()));

        tripSheetDeliveriesViewHolder.View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mPreferences.putString("DeliveryNo",currentDeliveryBean.getTripNo());
                Intent i=new Intent(activity,AgentReturnsView.class);
                i.putExtra("ReturnNo",currentReturnsBean.getReturnNo());
                i.putExtra("Returndate",getDate(currentReturnsBean.getReturnDate(),"dd-MM-yyyy"));
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
            for (AgentReturnsBean deliverysBean : allDeliveryProductsList) {
                if (deliverysBean.getReturnNo().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredDeliveryProductsList.add(deliverysBean);
                } else if (deliverysBean.getReturnDate().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredDeliveryProductsList.add(deliverysBean);
                }
            }
        }

        notifyDataSetChanged();
    }
}




