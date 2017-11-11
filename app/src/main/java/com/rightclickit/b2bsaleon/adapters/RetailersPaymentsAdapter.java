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
import com.rightclickit.b2bsaleon.activities.TDCSales_Preview_PrintActivity;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Venkat on 7/10/2017.
 */

public class RetailersPaymentsAdapter extends BaseAdapter {
    private Context context;
    private Activity activity;
    private LayoutInflater mInflater;
    private List<TDCSaleOrder> allTDCSalesOrders, filteredTDCSalesOrders;
    private DBHelper mDBHelper;
    ;

    public RetailersPaymentsAdapter(Context ctxt, Retailers_PaymentsActivity salesListActivity, List<TDCSaleOrder> ordersList) {
        this.context = ctxt;
        this.activity = salesListActivity;
        this.mInflater = LayoutInflater.from(activity);
        this.allTDCSalesOrders = ordersList;
        this.filteredTDCSalesOrders = new ArrayList<>();
        this.filteredTDCSalesOrders.addAll(allTDCSalesOrders);
        this.mDBHelper = new DBHelper(activity);
    }

    private class RetailerPaymentsListViewHolder {
        TextView tdc_sale_bill_no, tdc_sale_order_date, tdc_sale_order_amount, tdc_sale_order_items_count,name;
        Button view_button;
    }

    public void setAllTDCSalesOrders(List<TDCSaleOrder> allTDCSalesOrders) {
        this.allTDCSalesOrders = allTDCSalesOrders;
        this.filteredTDCSalesOrders = new ArrayList<>();
        this.filteredTDCSalesOrders.addAll(allTDCSalesOrders);
    }

    @Override
    public int getCount() {
        return filteredTDCSalesOrders.size();
    }

    @Override
    public TDCSaleOrder getItem(int position) {
        return filteredTDCSalesOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            RetailerPaymentsListViewHolder paymentsListViewHolder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.tdc_sales_list_view, null);

                paymentsListViewHolder = new RetailerPaymentsListViewHolder();
                paymentsListViewHolder.tdc_sale_bill_no = (TextView) convertView.findViewById(R.id.tdc_sale_bill_no);
                paymentsListViewHolder.tdc_sale_order_date = (TextView) convertView.findViewById(R.id.tdc_sale_order_date);
                paymentsListViewHolder.tdc_sale_order_amount = (TextView) convertView.findViewById(R.id.tdc_sale_order_amount);
               // paymentsListViewHolder.tdc_sale_order_items_count = (TextView) convertView.findViewById(R.id.tdc_sale_order_items_count);
                paymentsListViewHolder.view_button = (Button) convertView.findViewById(R.id.tdc_sale_order_btn_view);
                paymentsListViewHolder.name = (TextView) convertView.findViewById(R.id.name);

                convertView.setTag(paymentsListViewHolder);
            } else {
                paymentsListViewHolder = (RetailerPaymentsListViewHolder) convertView.getTag();
            }

            final TDCSaleOrder currentOrder = getItem(position);

            //paymentsListViewHolder.tdc_sale_bill_no.setText(String.format("TDC%05d", currentOrder.getOrderId()));
            paymentsListViewHolder.tdc_sale_bill_no.setText(currentOrder.getOrderBillNumber());
            paymentsListViewHolder.tdc_sale_order_date.setText(Utility.formatTime(currentOrder.getCreatedOn(), Constants.TDC_SALES_LIST_DATE_DISPLAY_FORMAT));
            paymentsListViewHolder.tdc_sale_order_amount.setText(Utility.getFormattedCurrency(currentOrder.getOrderSubTotal()));
            //paymentsListViewHolder.tdc_sale_order_items_count.setText(Utility.getFormattedNumber(currentOrder.getNoOfItems()));
            String name = mDBHelper.getNameById(currentOrder.getSelectedCustomerUserId(),currentOrder.getSelectedCustomerType());
            //paymentsListViewHolder.name.setText(name);
            paymentsListViewHolder.name.setText(currentOrder.getSelectedCustomerName());
            paymentsListViewHolder.view_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, TDCSales_Preview_PrintActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.BUNDLE_TDC_SALE_CURRENT_ORDER_PREVIEW, currentOrder);
                    intent.putExtra(Constants.BUNDLE_REQUEST_FROM, Constants.BUNDLE_REQUEST_FROM_RETAILER_PAYMENTS_LIST);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    // Filter method
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        filteredTDCSalesOrders.clear();

        if (allTDCSalesOrders != null) {
            if (charText.length() == 0) {
                filteredTDCSalesOrders.addAll(allTDCSalesOrders);
            } else {
                for (TDCSaleOrder order : allTDCSalesOrders) {
                    if (String.valueOf(order.getOrderBillNumber()).toLowerCase(Locale.getDefault()).contains(charText)) {
                        filteredTDCSalesOrders.add(order);
                    } else if (Utility.formatTime(order.getCreatedOn(), Constants.TDC_SALES_LIST_DATE_DISPLAY_FORMAT).toLowerCase(Locale.getDefault()).contains(charText)) {
                        filteredTDCSalesOrders.add(order);
                    }
                  else   if (String.valueOf(order.getSelectedCustomerName()).toLowerCase(Locale.getDefault()).contains(charText)) {
                        filteredTDCSalesOrders.add(order);
                    }
                }
            }
        }

        notifyDataSetChanged();
    }
}

