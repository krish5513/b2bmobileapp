package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.Retailers_PaymentsActivity;
import com.rightclickit.b2bsaleon.activities.Sales_Preview_PrintActivity;
import com.rightclickit.b2bsaleon.activities.TDCSalesListActivity;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.List;

/**
 * Created by PPS on 7/10/2017.
 */

public class RetailersPaymentsAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private Context context;
    private Activity activity;
    private List<TDCSaleOrder> tdcSalesOrders;

    public RetailersPaymentsAdapter(Context ctxt, Retailers_PaymentsActivity salesListActivity, List<TDCSaleOrder> ordersList) {
        this.context = ctxt;
        this.activity = salesListActivity;
        this.tdcSalesOrders = ordersList;
        this.mInflater = LayoutInflater.from(activity);
    }

    private class RetailersListViewHolder {
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
            RetailersListViewHolder paymentsListViewHolder;

            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.tdc_sales_list_view, null);

                paymentsListViewHolder = new RetailersListViewHolder();
                paymentsListViewHolder.tdc_sale_bill_no = (TextView) convertView.findViewById(R.id.tdc_sale_bill_no);
                paymentsListViewHolder.tdc_sale_order_date = (TextView) convertView.findViewById(R.id.tdc_sale_order_date);
                paymentsListViewHolder.tdc_sale_order_amount = (TextView) convertView.findViewById(R.id.tdc_sale_order_amount);
                paymentsListViewHolder.tdc_sale_order_items_count = (TextView) convertView.findViewById(R.id.tdc_sale_order_items_count);
                paymentsListViewHolder.view_button = (Button) convertView.findViewById(R.id.tdc_sale_order_btn_view);

                convertView.setTag(paymentsListViewHolder);
            } else {
                paymentsListViewHolder = (RetailersListViewHolder) convertView.getTag();
            }

            final TDCSaleOrder currentOrder = getItem(position);

            paymentsListViewHolder.tdc_sale_bill_no.setText(String.format("TDC%05d", currentOrder.getOrderId()));
            paymentsListViewHolder.tdc_sale_order_date.setText(Utility.formatTime(currentOrder.getCreatedOn(), Constants.TDC_SALES_LIST_DATE_DISPLAY_FORMAT));
            paymentsListViewHolder.tdc_sale_order_amount.setText(Utility.getFormattedCurrency(currentOrder.getOrderSubTotal()));
            paymentsListViewHolder.tdc_sale_order_items_count.setText(Utility.getFormattedNumber(currentOrder.getNoOfItems()));

            paymentsListViewHolder.view_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, Sales_Preview_PrintActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.BUNDLE_TDC_SALE_CURRENT_ORDER_PREVIEW, currentOrder);
                    intent.putExtra(Constants.BUNDLE_REQUEST_FROM, Constants.BUNDLE_REQUEST_FROM_TDC_SALES_LIST);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                    // Need to write code to show order view.
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}

