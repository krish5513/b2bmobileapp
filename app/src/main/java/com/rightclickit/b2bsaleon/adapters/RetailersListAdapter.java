package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.RetailersActivity;
import com.rightclickit.b2bsaleon.beanclass.TDCCustomer;

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

    public RetailersListAdapter(Context ctxt, RetailersActivity retailersActivity, List<TDCCustomer> retailersList) {
        this.ctxt = ctxt;
        this.activity = retailersActivity;
        this.mInflater = LayoutInflater.from(activity);
        this.allRetailersList = (ArrayList<TDCCustomer>) retailersList;
        this.filteredRetailersList = new ArrayList<>();
        this.filteredRetailersList.addAll(allRetailersList);
    }

    private class RetailersViewHolder {
        TextView retailer_id, retailer_name, retailer_mobile_no;
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

            convertView.setTag(retailersViewHolder);
        } else {
            retailersViewHolder = (RetailersViewHolder) convertView.getTag();
        }

        final TDCCustomer currentRetailer = getItem(position);

        retailersViewHolder.retailer_id.setText(String.format("R%05d", currentRetailer.getId()));
        retailersViewHolder.retailer_name.setText(currentRetailer.getName());
        retailersViewHolder.retailer_mobile_no.setText(currentRetailer.getMobileNo());

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
                if (customer.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredRetailersList.add(customer);
                }

                if (customer.getMobileNo().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredRetailersList.add(customer);
                }
            }
        }

        notifyDataSetChanged();
    }
}