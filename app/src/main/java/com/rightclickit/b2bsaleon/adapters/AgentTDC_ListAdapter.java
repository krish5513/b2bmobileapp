package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.AgentTakeOrderPreview;
import com.rightclickit.b2bsaleon.activities.AgentsInfoActivity;
import com.rightclickit.b2bsaleon.activities.Sales_Preview_PrintActivity;
import com.rightclickit.b2bsaleon.activities.Saleslist_ViewActivity;
import com.rightclickit.b2bsaleon.activities.TDCSales_Today;
import com.rightclickit.b2bsaleon.beanclass.OrdersListBean;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderPreviewBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PPS on 7/8/2017.
 */

public class AgentTDC_ListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    private Activity activity;
    Context ctxt;
    ArrayList<TDCSaleOrder> ordersListBeen;
    private ImageLoader mImageLoader;

    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
    String currentOrderId;
    private long previousOrderId;

    public AgentTDC_ListAdapter(Context ctxt, TDCSales_Today previewActivity, ArrayList<TDCSaleOrder> ordersListBeen) {
        this.ctxt = ctxt;
        this.activity = previewActivity;
        this.ordersListBeen = ordersListBeen;
        this.mInflater = LayoutInflater.from(activity);
        mDBHelper=new DBHelper(activity);
        this.mPreferences = new MMSharedPreferences(activity);
    }


    @Override
    public int getCount() {
        return ordersListBeen.size();
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
        final AgentTDC_ListAdapter.MyViewHolder holder;

        if (convertView == null) {

            holder = new AgentTDC_ListAdapter.MyViewHolder();
            convertView = mInflater.inflate(R.layout.tdcorderslist_card, null);

            holder.tv_billno = (TextView) convertView.findViewById(R.id.tv_billno);
            holder.tv_billdate = (TextView) convertView.findViewById(R.id.tv_tdcdate);
            holder.tv_totalAmount = (TextView) convertView.findViewById(R.id.tv_AmountValue);
            holder.tv_totalValue = (TextView) convertView.findViewById(R.id.tv_ItemsCount);
            holder.ViewButton= (TextView) convertView.findViewById(R.id.btn_weeklyview1);


            convertView.setTag(holder);
        } else {
            holder = (AgentTDC_ListAdapter.MyViewHolder) convertView.getTag();
        }

        previousOrderId = mDBHelper.getTDCSalesMaxOrderNumber();
        currentOrderId = String.format("TDC%05d", previousOrderId + 1);

        holder.tv_billno.setText(currentOrderId);
        Log.i("cid",currentOrderId);
        holder.tv_billdate.setText("Date");
        holder.tv_totalAmount.setText(Utility.getFormattedCurrency(Double.parseDouble(String.valueOf(ordersListBeen.get(position).getOrderSubTotal()))));//  holder.tv_totalValue.setText(Utility.getFormattedCurrency(taxAmount));
        holder.tv_totalValue.setText(ordersListBeen.size());

        holder.ViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity,Saleslist_ViewActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
        return convertView;
    }

    private class MyViewHolder {
        public TextView tv_billno;

        public TextView tv_billdate;
        public TextView tv_totalAmount;
        public TextView tv_totalValue;
        public TextView ViewButton;






    }



    // Methos to display product image as full image

}