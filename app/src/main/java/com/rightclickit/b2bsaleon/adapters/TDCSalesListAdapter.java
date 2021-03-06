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
import com.rightclickit.b2bsaleon.activities.TDCSalesListActivity;
import com.rightclickit.b2bsaleon.activities.TDCSales_Preview_PrintActivity;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Venkat on 7/9/2017.
 */

public class TDCSalesListAdapter extends BaseAdapter {
    private Context context;
    private Activity activity;
    private LayoutInflater mInflater;
    private MMSharedPreferences sp;
    private List<TDCSaleOrder> allTDCSalesOrders, filteredTDCSalesOrders;
    private DBHelper mDBHelper;
    private String isFrom = "";

    public TDCSalesListAdapter(Context ctxt, TDCSalesListActivity salesListActivity, String troipsTakeorder) {
        this.context = ctxt;
        this.activity = salesListActivity;
        this.mInflater = LayoutInflater.from(activity);
        this.filteredTDCSalesOrders = new ArrayList<>();
        sp = new MMSharedPreferences(context);
        this.mDBHelper = new DBHelper(activity);
        this.isFrom = troipsTakeorder;
    }

    private class TDCSalesListViewHolder {
        TextView tdc_sale_bill_no, tdc_sale_order_date, tdc_sale_order_amount, tdc_sale_order_items_count, customer;
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
            TDCSalesListViewHolder tdcSalesListViewHolder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.tdc_sales_list_view, null);

                tdcSalesListViewHolder = new TDCSalesListViewHolder();
                tdcSalesListViewHolder.tdc_sale_bill_no = (TextView) convertView.findViewById(R.id.tdc_sale_bill_no);
                tdcSalesListViewHolder.tdc_sale_order_date = (TextView) convertView.findViewById(R.id.tdc_sale_order_date);
                tdcSalesListViewHolder.tdc_sale_order_amount = (TextView) convertView.findViewById(R.id.tdc_sale_order_amount);
                // tdcSalesListViewHolder.tdc_sale_order_items_count = (TextView) convertView.findViewById(R.id.tdc_sale_order_items_count);
                tdcSalesListViewHolder.customer = (TextView) convertView.findViewById(R.id.name);
                tdcSalesListViewHolder.view_button = (Button) convertView.findViewById(R.id.tdc_sale_order_btn_view);

                convertView.setTag(tdcSalesListViewHolder);
            } else {
                tdcSalesListViewHolder = (TDCSalesListViewHolder) convertView.getTag();
            }

            final TDCSaleOrder currentOrder = getItem(position);

            tdcSalesListViewHolder.tdc_sale_bill_no.setText(currentOrder.getOrderBillNumber());
            tdcSalesListViewHolder.tdc_sale_order_date.setText(Utility.formatTime(currentOrder.getCreatedOn(), Constants.TDC_SALES_LIST_DATE_DISPLAY_FORMAT));
            tdcSalesListViewHolder.tdc_sale_order_amount.setText(Utility.getFormattedCurrency(currentOrder.getOrderSubTotal()));
            //tdcSalesListViewHolder.tdc_sale_order_items_count.setText(Utility.getFormattedNumber(currentOrder.getNoOfItems()));
            String name = mDBHelper.getNameById(currentOrder.getSelectedCustomerUserId(), currentOrder.getSelectedCustomerType());

            tdcSalesListViewHolder.customer.setText(currentOrder.getSelectedCustomerName());

            tdcSalesListViewHolder.view_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, TDCSales_Preview_PrintActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("incid", String.valueOf(position + 1));
                    bundle.putSerializable(Constants.BUNDLE_TDC_SALE_CURRENT_ORDER_PREVIEW, currentOrder);
                    if (isFrom.equals("TDC")) {
                        intent.putExtra(Constants.BUNDLE_REQUEST_FROM, Constants.BUNDLE_REQUEST_FROM_TDC_SALES_LIST);
                    } else {
                        intent.putExtra(Constants.BUNDLE_REQUEST_FROM, "Agents");
                    }
                    intent.putExtra("CustomerName",currentOrder.getSelectedCustomerName());
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                    activity.finish();
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
                    } else if (String.valueOf(order.getSelectedCustomerName()).toLowerCase(Locale.getDefault()).contains(charText)) {
                        filteredTDCSalesOrders.add(order);
                    }
                }
            }
        }

        notifyDataSetChanged();
    }
}