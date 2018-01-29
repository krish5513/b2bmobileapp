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
import com.rightclickit.b2bsaleon.activities.RetailersActivity;
import com.rightclickit.b2bsaleon.activities.Retailers_AddActivity;
import com.rightclickit.b2bsaleon.activities.Retailers_PaymentsActivity;
import com.rightclickit.b2bsaleon.beanclass.TDCCustomer;
import com.rightclickit.b2bsaleon.constants.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Venkat on 06/20/2017.
 */

public class RetailersListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity activity;
    private Context ctxt;
    private ArrayList<TDCCustomer> allRetailersList, filteredRetailersList;
    private ArrayList<String> privilegeActionsData;

    public RetailersListAdapter(Context ctxt, RetailersActivity retailersActivity, List<TDCCustomer> retailersList, ArrayList<String> privilegeActions) {
        this.ctxt = ctxt;
        this.activity = retailersActivity;
        this.mInflater = LayoutInflater.from(activity);
        this.allRetailersList = (ArrayList<TDCCustomer>) retailersList;
        this.filteredRetailersList = new ArrayList<>();
        this.filteredRetailersList.addAll(allRetailersList);
        this.privilegeActionsData = privilegeActions;
    }

    private class RetailersViewHolder {
        TextView retailer_id, retailer_name, retailer_mobile_no;
        Button retailer_btn_info, retailer_btn_payments;
    }

    public void setAllRetailersList(List<TDCCustomer> customersList) {
        this.allRetailersList = (ArrayList<TDCCustomer>) customersList;
        this.filteredRetailersList = new ArrayList<>();
        this.filteredRetailersList.addAll(allRetailersList);
    }

    @Override
    public int getCount() {
        return filteredRetailersList.size();
    }

    @Override
    public TDCCustomer getItem(int position) {
        return filteredRetailersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RetailersViewHolder retailersViewHolder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.retailers_list_adapter, null);

            retailersViewHolder = new RetailersViewHolder();
            retailersViewHolder.retailer_id = (TextView) convertView.findViewById(R.id.retailer_id);
            retailersViewHolder.retailer_name = (TextView) convertView.findViewById(R.id.retailer_name);
            retailersViewHolder.retailer_mobile_no = (TextView) convertView.findViewById(R.id.retailer_mobile_no);
            retailersViewHolder.retailer_btn_info = (Button) convertView.findViewById(R.id.retailer_btn_info);
            retailersViewHolder.retailer_btn_payments = (Button) convertView.findViewById(R.id.retailer_btn_payments);

            convertView.setTag(retailersViewHolder);
        } else {
            retailersViewHolder = (RetailersViewHolder) convertView.getTag();
        }

        final TDCCustomer currentRetailer = getItem(position);

        //retailersViewHolder.retailer_id.setText(String.format("R%05d", currentRetailer.getId()));
        if (currentRetailer.getCode().equals("")) {
            retailersViewHolder.retailer_id.setText("Null");
        } else {
            retailersViewHolder.retailer_id.setText(currentRetailer.getCode());
        }
        retailersViewHolder.retailer_name.setText(currentRetailer.getBusinessName());
        retailersViewHolder.retailer_mobile_no.setText(currentRetailer.getMobileNo());

        if (privilegeActionsData.contains("List_Info")) {
            retailersViewHolder.retailer_btn_info.setVisibility(View.VISIBLE);
        } else {
            retailersViewHolder.retailer_btn_info.setVisibility(View.GONE);
        }

        if (privilegeActionsData.contains("Payment_List")) {
            retailersViewHolder.retailer_btn_payments.setVisibility(View.VISIBLE);
        } else {
            retailersViewHolder.retailer_btn_payments.setVisibility(View.GONE);
        }

        retailersViewHolder.retailer_btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, Retailers_AddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.BUNDLE_TDC_CUSTOMER, currentRetailer);

                String rid=currentRetailer.getRoutecode();
                if(rid.contains("["))
                    rid=rid.substring(2,(rid.length()-2));
                bundle.putString("RouteId", rid);

                //bundle.putString("RouteId",currentRetailer.getRoutecode());
                intent.putExtras(bundle);
                activity.startActivity(intent);
                activity.finish();
            }
        });


        retailersViewHolder.retailer_btn_payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, Retailers_PaymentsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(Constants.BUNDLE_SELECTED_CUSTOMER_ID, currentRetailer.getId());
                bundle.putString("AgentId", currentRetailer.getUserId());

                intent.putExtras(bundle);
                activity.startActivity(intent);
                activity.finish();
            }
        });


        return convertView;
    }

    // Filter method
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        filteredRetailersList.clear();

        if (charText.length() == 0) {
            filteredRetailersList.addAll(allRetailersList);
        } else {
            for (TDCCustomer customer : allRetailersList) {
                if (customer.getBusinessName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredRetailersList.add(customer);
                }

                if (customer.getMobileNo().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredRetailersList.add(customer);
                }

                if (String.valueOf(customer.getCode()).toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredRetailersList.add(customer);
                }
            }
        }

        notifyDataSetChanged();
    }
}