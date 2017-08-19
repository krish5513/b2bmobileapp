package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.AgentDeliveries;
import com.rightclickit.b2bsaleon.activities.AgentDeliveriesView;
import com.rightclickit.b2bsaleon.activities.AgentTDC_Order;
import com.rightclickit.b2bsaleon.activities.TripsheetDelivery;
import com.rightclickit.b2bsaleon.beanclass.AgentDeliveriesBean;
import com.rightclickit.b2bsaleon.beanclass.DeliverysBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetDeliveriesBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.TripSheetDeliveriesListener;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by PPS on 8/17/2017.
 */

public class AgentDeliveriesAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private Context ctxt;
    private Activity activity;
    private TripSheetDeliveriesListener listener;
    private ArrayList<AgentDeliveriesBean> allDeliveryProductsList, filteredDeliveryProductsList;
    private Map<String, DeliverysBean> selectedDeliveryProductsHashMap; // Hash Map Key = Product Id
    private Map<String, String> previouslyDeliveredProductsHashMap;
    private Map<String, String> productOrderQuantitiesHashMap;
    private final String zero_cost = "0.000";
    private boolean isDeliveryInEditingMode = false;
    DBHelper mdbhelper;

    public AgentDeliveriesAdapter(Context ctxt, AgentDeliveries deliveryActivity, ArrayList<AgentDeliveriesBean> mdeliveriesBeanList) {
        this.ctxt = ctxt;
        this.activity = deliveryActivity;
        this.mInflater = LayoutInflater.from(activity);
        mdbhelper=new DBHelper(ctxt);
        this.allDeliveryProductsList = mdeliveriesBeanList;
        this.filteredDeliveryProductsList = new ArrayList<>();
        this.filteredDeliveryProductsList.addAll(allDeliveryProductsList);
        this.selectedDeliveryProductsHashMap = new HashMap<>();


    }

    public class TripSheetDeliveriesViewHolder {
        TextView Delivery_no, Delivery_date, delivery_status, items_count, deliverd_by;
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
        final AgentDeliveriesAdapter.TripSheetDeliveriesViewHolder tripSheetDeliveriesViewHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.agentdeliveries_custom, null);

            tripSheetDeliveriesViewHolder = new AgentDeliveriesAdapter.TripSheetDeliveriesViewHolder();
            tripSheetDeliveriesViewHolder.Delivery_no = (TextView) view.findViewById(R.id.tv_prid);
            tripSheetDeliveriesViewHolder.Delivery_date = (TextView) view.findViewById(R.id.tv_date);
            tripSheetDeliveriesViewHolder.delivery_status = (TextView) view.findViewById(R.id.tv_status);
            tripSheetDeliveriesViewHolder.items_count = (TextView) view.findViewById(R.id.tv_items);
            tripSheetDeliveriesViewHolder.deliverd_by = (TextView) view.findViewById(R.id.tv_deliveredby);
            tripSheetDeliveriesViewHolder.View = (Button) view.findViewById(R.id.btn_view1);


            view.setTag(tripSheetDeliveriesViewHolder);
        } else {
            tripSheetDeliveriesViewHolder = (AgentDeliveriesAdapter.TripSheetDeliveriesViewHolder) view.getTag();
        }

        final AgentDeliveriesAdapter.TripSheetDeliveriesViewHolder currentTripSheetDeliveriesViewHolder = tripSheetDeliveriesViewHolder;

        final AgentDeliveriesBean currentDeliveryBean = allDeliveryProductsList.get(position);

        tripSheetDeliveriesViewHolder.Delivery_no.setText(currentDeliveryBean.getTripNo());
        tripSheetDeliveriesViewHolder.Delivery_date.setText(getDate(currentDeliveryBean.getTripDate(),"dd-MM-yyyy"));
        tripSheetDeliveriesViewHolder.delivery_status.setText(currentDeliveryBean.getDeliverdstatus());
        tripSheetDeliveriesViewHolder.items_count.setText(currentDeliveryBean.getDeliveredItems());
        tripSheetDeliveriesViewHolder.deliverd_by.setText(mdbhelper.getDeliveryName(currentDeliveryBean.getDeliveredBy()));

        tripSheetDeliveriesViewHolder.View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity,AgentDeliveriesView.class));
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
    public void updateSelectedProductsList(DeliverysBean deliverysBean) {
        try {
            if (selectedDeliveryProductsHashMap.containsKey(deliverysBean.getProductId()))
                selectedDeliveryProductsHashMap.remove(deliverysBean.getProductId());

            selectedDeliveryProductsHashMap.put(deliverysBean.getProductId(), deliverysBean);

            if (listener != null)
                listener.updateDeliveryProductsList(selectedDeliveryProductsHashMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        filteredDeliveryProductsList.clear();

        if (charText.length() == 0) {
            filteredDeliveryProductsList.addAll(allDeliveryProductsList);
        } else {
            for (AgentDeliveriesBean deliverysBean : allDeliveryProductsList) {
                if (deliverysBean.getTripNo().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredDeliveryProductsList.add(deliverysBean);
                } else if (deliverysBean.getTripDate().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredDeliveryProductsList.add(deliverysBean);
                }
            }
        }

        notifyDataSetChanged();
    }
}


