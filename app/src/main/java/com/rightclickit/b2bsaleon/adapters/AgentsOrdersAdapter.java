package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.AgentTDC_Order;
import com.rightclickit.b2bsaleon.activities.AgentTakeOrderPreview;
import com.rightclickit.b2bsaleon.activities.AgentsTDC_View;
import com.rightclickit.b2bsaleon.beanclass.OrdersListBean;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderBean;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderPreviewBean;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;

/**
 * Created by PPS on 7/1/2017.
 */

public class AgentsOrdersAdapter extends BaseAdapter{
    LayoutInflater mInflater;
    private Activity activity;
    Context ctxt;
    private ArrayList<TakeOrderBean> mOrdersList;
    private ImageLoader mImageLoader;

    private MMSharedPreferences mPreferences;


    public AgentsOrdersAdapter(Context ctxt, AgentTDC_Order ordersActivity, ArrayList<TakeOrderBean> ordersListBeanArrayList) {
        this.ctxt = ctxt;
        this.activity = ordersActivity;
        this.mOrdersList = ordersListBeanArrayList;
        this.mInflater = LayoutInflater.from(activity);

        this.mPreferences = new MMSharedPreferences(activity);
    }


    @Override
    public int getCount() {
        return mOrdersList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final AgentsOrdersAdapter.MyViewHolder holder;

        if (convertView == null) {

            holder = new AgentsOrdersAdapter.MyViewHolder();
            convertView = mInflater.inflate(R.layout.agent_orders_adapter, null);

            holder.enquiryId = (TextView) convertView.findViewById(R.id.tv_EnquiryId);
            holder.date = (TextView) convertView.findViewById(R.id.tv_ordersdate);
            holder.status= (TextView) convertView.findViewById(R.id.tv_orderstatus);
            holder.itemsCount= (TextView) convertView.findViewById(R.id.tv_ItemsCount);
            holder.valueCount = (TextView) convertView.findViewById(R.id.tv_OrdervalueCount);
            holder.view = (TextView) convertView.findViewById(R.id.btn_ordersview);


            convertView.setTag(holder);
        } else {
            holder = (AgentsOrdersAdapter.MyViewHolder) convertView.getTag();
        }




        holder.enquiryId.setText(mOrdersList.get(position).getmEnquiryId());
        holder.date.setText(mOrdersList.get(position).getmAgentTakeOrderDate());
        holder.status.setText("Paid");
        holder.itemsCount.setText("ITEMS");
        holder.valueCount.setText("TOTALVALUE");

    holder.view.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        activity.startActivity(new Intent(activity,AgentsTDC_View.class));
        activity.finish();
    }
    });

        return convertView;
    }

    private class MyViewHolder {
        public TextView enquiryId;

        public TextView date;
        public TextView status;
        public TextView itemsCount;
        public TextView valueCount;
        public TextView view;






    }



    // Methos to display product image as full image

}
