package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.TripSheetStock;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Sekhar Kuppa
 * <p>
 * Modified by Venkat
 */

public class TripsheetsStockListAdapter extends BaseAdapter {
    private Context ctxt;
    private Activity activity;
    private LayoutInflater mInflater;
    private ArrayList<TripsheetsStockList> allTripSheetStockList, filteredTripSheetStockList;
    private ArrayList<String> privilegeActionsData;

    public TripsheetsStockListAdapter(Context ctxt, TripSheetStock agentsActivity, ArrayList<TripsheetsStockList> tripSheetStockList, ArrayList<String> privilegeActionsData) {
        this.ctxt = ctxt;
        this.activity = agentsActivity;
        this.mInflater = LayoutInflater.from(activity);
        this.allTripSheetStockList = tripSheetStockList;
        this.filteredTripSheetStockList = new ArrayList<>();
        this.filteredTripSheetStockList.addAll(allTripSheetStockList);
        this.privilegeActionsData = privilegeActionsData;
    }

    @Override
    public int getCount() {
        return filteredTripSheetStockList.size();
    }

    @Override
    public TripsheetsStockList getItem(int position) {
        return filteredTripSheetStockList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final TripSheetStockViewHolder tripSheetStockViewHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.tripsheets_stock_list_custom, null);

            tripSheetStockViewHolder = new TripSheetStockViewHolder();
            tripSheetStockViewHolder.mProductName = (TextView) view.findViewById(R.id.ts_stock_product_name);
            tripSheetStockViewHolder.mOrder = (TextView) view.findViewById(R.id.ts_stock_order_quantity);
            tripSheetStockViewHolder.mDispatchQuantity = (EditText) view.findViewById(R.id.ts_stock_dispatch_quantity);
            tripSheetStockViewHolder.mDispatchDecrement = (ImageButton) view.findViewById(R.id.ts_stock_dispatch_decrement);
            tripSheetStockViewHolder.mDispatchIncrement = (ImageButton) view.findViewById(R.id.ts_stock_dispatch_increment);
            tripSheetStockViewHolder.mVerifyDecrement = (ImageButton) view.findViewById(R.id.ts_stock_verify_decrement);
            tripSheetStockViewHolder.mVerifyQuantity = (EditText) view.findViewById(R.id.ts_stock_verify_quantity);
            tripSheetStockViewHolder.mVerifyIncrement = (ImageButton) view.findViewById(R.id.ts_stock_verify_increment);

            view.setTag(tripSheetStockViewHolder);
        } else {
            tripSheetStockViewHolder = (TripSheetStockViewHolder) view.getTag();
        }

        if (privilegeActionsData.contains("Stock_Dispatch")) {
            tripSheetStockViewHolder.mDispatchQuantity.setVisibility(View.VISIBLE);
            tripSheetStockViewHolder.mDispatchDecrement.setVisibility(View.VISIBLE);
            tripSheetStockViewHolder.mDispatchIncrement.setVisibility(View.VISIBLE);
        }

        if (privilegeActionsData.contains("Stock_Verify")) {
            tripSheetStockViewHolder.mVerifyDecrement.setVisibility(View.VISIBLE);
            tripSheetStockViewHolder.mVerifyQuantity.setVisibility(View.VISIBLE);
            tripSheetStockViewHolder.mVerifyIncrement.setVisibility(View.VISIBLE);
        }

        TripsheetsStockList currentStockList = getItem(position);

        tripSheetStockViewHolder.mProductName.setText(currentStockList.getmTripsheetStockProductName());
        tripSheetStockViewHolder.mOrder.setText(currentStockList.getmTripsheetStockProductOrderQuantity());
        tripSheetStockViewHolder.mDispatchQuantity.setText(currentStockList.getmTripsheetStockProductOrderQuantity());
        tripSheetStockViewHolder.mVerifyQuantity.setText(currentStockList.getmTripsheetStockProductOrderQuantity());



        return view;
    }

    public class TripSheetStockViewHolder {
        TextView mProductName;
        TextView mOrder;
        EditText mDispatchQuantity;
        EditText mVerifyQuantity;
        ImageButton mDispatchDecrement;
        ImageButton mDispatchIncrement;
        ImageButton mVerifyDecrement;
        ImageButton mVerifyIncrement;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        filteredTripSheetStockList.clear();

        if (charText.length() == 0) {
            filteredTripSheetStockList.addAll(allTripSheetStockList);
        } else {
            for (TripsheetsStockList stock : allTripSheetStockList) {
                if (stock.getmTripsheetStockProductName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredTripSheetStockList.add(stock);
                }
            }
        }

        notifyDataSetChanged();
    }
}
