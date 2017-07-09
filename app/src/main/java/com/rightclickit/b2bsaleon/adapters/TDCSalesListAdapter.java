package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.Saleslist_ViewActivity;
import com.rightclickit.b2bsaleon.activities.TDCSalesListActivity;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Venkat on 7/9/2017.
 */

public class TDCSalesListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;
    private Activity activity;
    private List<TDCSaleOrder> tdcSalesOrders;

    public TDCSalesListAdapter(Context ctxt, TDCSalesListActivity salesListActivity, List<TDCSaleOrder> ordersList) {
        this.context = ctxt;
        this.activity = salesListActivity;
        this.tdcSalesOrders = ordersList;
        this.mInflater = LayoutInflater.from(activity);
    }

    private class TDCSalesListViewHolder {
        TextView tdc_sale_bill_no, tdc_sale_order_date, tdc_sale_order_amount, tdc_sale_order_items_count;
        Button view_button;
    }

    public void setTdcSalesOrders(List<TDCSaleOrder> tdcSalesOrders) {
        this.tdcSalesOrders = tdcSalesOrders;

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (tdcSalesOrders != null)
            return tdcSalesOrders.size();
        else
            return 0;
    }

    @Override
    public TDCSaleOrder getItem(int position) {
        return tdcSalesOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            TDCSalesListViewHolder tdcSalesListViewHolder;

            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.tdc_sales_list_view, null);

                tdcSalesListViewHolder = new TDCSalesListViewHolder();
                tdcSalesListViewHolder.tdc_sale_bill_no = (TextView) convertView.findViewById(R.id.tdc_sale_bill_no);
                tdcSalesListViewHolder.tdc_sale_order_date = (TextView) convertView.findViewById(R.id.tdc_sale_order_date);
                tdcSalesListViewHolder.tdc_sale_order_amount = (TextView) convertView.findViewById(R.id.tdc_sale_order_amount);
                tdcSalesListViewHolder.tdc_sale_order_items_count = (TextView) convertView.findViewById(R.id.tdc_sale_order_items_count);
                tdcSalesListViewHolder.view_button = (Button) convertView.findViewById(R.id.tdc_sale_order_btn_view);

                convertView.setTag(tdcSalesListViewHolder);
            } else {
                tdcSalesListViewHolder = (TDCSalesListViewHolder) convertView.getTag();
            }

            final TDCSaleOrder currentOrder = getItem(position);

            tdcSalesListViewHolder.tdc_sale_bill_no.setText(String.format("TDC%05d", currentOrder.getOrderId()));
            tdcSalesListViewHolder.tdc_sale_order_date.setText(Utility.formatTime(currentOrder.getCreatedOn(), Constants.TDC_SALES_LIST_DATE_DISPLAY_FORMAT));
            tdcSalesListViewHolder.tdc_sale_order_amount.setText(Utility.getFormattedCurrency(currentOrder.getOrderSubTotal()));
            tdcSalesListViewHolder.tdc_sale_order_items_count.setText(Utility.getFormattedNumber(currentOrder.getNoOfItems()));

            tdcSalesListViewHolder.view_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Need to write code to show order view.
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}