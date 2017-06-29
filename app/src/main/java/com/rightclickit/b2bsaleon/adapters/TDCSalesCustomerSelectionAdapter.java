package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.SalesCustomerSelectionActivity;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TDCCustomer;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Venkat on 06/20/2017.
 */

public class TDCSalesCustomerSelectionAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity activity;
    private Context ctxt;
    private ArrayList<TDCCustomer> allCustomersList, filteredCustomersList;

    public TDCSalesCustomerSelectionAdapter(Context ctxt, SalesCustomerSelectionActivity customerSelectionActivity, List<TDCCustomer> customersList) {
        this.ctxt = ctxt;
        this.activity = customerSelectionActivity;
        this.mInflater = LayoutInflater.from(activity);
        this.allCustomersList = (ArrayList<TDCCustomer>) customersList;
        this.filteredCustomersList = new ArrayList<>();
        this.filteredCustomersList.addAll(allCustomersList);
    }

    private class TDCSalesCustomerSelectionViewHolder {
        TextView customer_id, customer_name, customer_mobile_no;
        ImageView customer_type_image;
    }

    public void setAllCustomersList(List<TDCCustomer> customersList) {
        this.allCustomersList = (ArrayList<TDCCustomer>) customersList;
        this.filteredCustomersList = new ArrayList<>();
        this.filteredCustomersList.addAll(allCustomersList);
    }

    @Override
    public int getCount() {
        return filteredCustomersList.size();
    }

    @Override
    public TDCCustomer getItem(int position) {
        return filteredCustomersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TDCSalesCustomerSelectionViewHolder tdcSalesCustomerSelectionViewHolder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tdc_sales_customers_adapter, null);

            tdcSalesCustomerSelectionViewHolder = new TDCSalesCustomerSelectionViewHolder();
            tdcSalesCustomerSelectionViewHolder.customer_id = (TextView) convertView.findViewById(R.id.customer_id);
            tdcSalesCustomerSelectionViewHolder.customer_type_image = (ImageView) convertView.findViewById(R.id.customer_type_image);
            tdcSalesCustomerSelectionViewHolder.customer_name = (TextView) convertView.findViewById(R.id.customer_name);
            tdcSalesCustomerSelectionViewHolder.customer_mobile_no = (TextView) convertView.findViewById(R.id.customer_mobile_no);

            convertView.setTag(tdcSalesCustomerSelectionViewHolder);
        } else {
            tdcSalesCustomerSelectionViewHolder = (TDCSalesCustomerSelectionViewHolder) convertView.getTag();
        }

        final TDCCustomer currentCustomer = getItem(position);

        if (currentCustomer.getCustomerType() == 1) {
            tdcSalesCustomerSelectionViewHolder.customer_id.setText(String.format("R%05d", currentCustomer.getId()));
            tdcSalesCustomerSelectionViewHolder.customer_type_image.setImageResource(R.drawable.ic_store_black_24dp);
        } else {
            tdcSalesCustomerSelectionViewHolder.customer_id.setText(String.format("C%05d", currentCustomer.getId()));
            tdcSalesCustomerSelectionViewHolder.customer_type_image.setImageResource(R.drawable.ic_person_pin_black_24dp);
        }

        tdcSalesCustomerSelectionViewHolder.customer_name.setText(currentCustomer.getName());
        tdcSalesCustomerSelectionViewHolder.customer_mobile_no.setText(currentCustomer.getMobileNo());

        return convertView;
    }

    // Filter method
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        filteredCustomersList.clear();

        if (charText.length() == 0) {
            filteredCustomersList.addAll(allCustomersList);
        } else {
            for (TDCCustomer customer : allCustomersList) {
                if (customer.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredCustomersList.add(customer);
                }

                if (customer.getMobileNo().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredCustomersList.add(customer);
                }
            }
        }

        notifyDataSetChanged();
    }
}