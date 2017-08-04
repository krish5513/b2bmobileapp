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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.TDCSalesListActivity;
import com.rightclickit.b2bsaleon.activities.TDCSales_Preview_PrintActivity;
import com.rightclickit.b2bsaleon.activities.TripSheetStock;
import com.rightclickit.b2bsaleon.activities.TripsheetStockPreview;
import com.rightclickit.b2bsaleon.beanclass.TDCSaleOrder;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.interfaces.TripSheetStockListener;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by PPS on 8/3/2017.
 */

public class TripsheetStockPreviewAdapter extends BaseAdapter {
    private Context ctxt;
    private Activity activity;
    private TripSheetStockListener listener;
    private LayoutInflater mInflater;
    private ArrayList<TripsheetsStockList> allTripSheetStockList, filteredTripSheetStockList;
    private ArrayList<String> privilegeActionsData;
    private Map<String, TripsheetsStockList> dispatchProductsListHashMap, verifyProductsListHashMap;

    private final String zero_cost = "0.000";

    public TripsheetStockPreviewAdapter(Context ctxt, TripsheetStockPreview agentsActivity, TripsheetStockPreview tripSheetStockListener, ArrayList<TripsheetsStockList> tripSheetStockList) {
        this.ctxt = ctxt;
        this.activity = agentsActivity;

        this.mInflater = LayoutInflater.from(activity);
        this.allTripSheetStockList = tripSheetStockList;
        this.filteredTripSheetStockList = new ArrayList<>();
        this.filteredTripSheetStockList.addAll(allTripSheetStockList);

        this.dispatchProductsListHashMap = new HashMap<>();
        this.verifyProductsListHashMap = new HashMap<>();
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
        final TripsheetStockPreviewAdapter.TripSheetStockViewHolder tripSheetStockViewHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.tripsheetstock_preview_custom, null);

            tripSheetStockViewHolder = new TripsheetStockPreviewAdapter.TripSheetStockViewHolder();
            tripSheetStockViewHolder.mProductName = (TextView) view.findViewById(R.id.productName);
            tripSheetStockViewHolder.mProductCode = (TextView) view.findViewById(R.id.productCode);
            tripSheetStockViewHolder.mProductUom = (TextView) view.findViewById(R.id.UOM);
            tripSheetStockViewHolder.mOrder = (TextView) view.findViewById(R.id.order);

            tripSheetStockViewHolder.mDispatchQuantity = (TextView) view.findViewById(R.id.dispatchProduct);

            tripSheetStockViewHolder.mVerifyQuantity = (TextView) view.findViewById(R.id.verifyProduct);


            view.setTag(tripSheetStockViewHolder);
        } else {
            tripSheetStockViewHolder = (TripsheetStockPreviewAdapter.TripSheetStockViewHolder) view.getTag();
        }

        final TripsheetsStockList currentStockList = getItem(position);

        Double orderQuantity = Double.parseDouble(currentStockList.getmTripsheetStockProductOrderQuantity());

        tripSheetStockViewHolder.mProductName.setText(currentStockList.getmTripsheetStockProductName());
        tripSheetStockViewHolder.mProductCode.setText(currentStockList.getmTripsheetStockProductCode());
        tripSheetStockViewHolder.mOrder.setText(String.format("%.3f", orderQuantity));



            if (currentStockList.getIsStockDispatched() == 0) {
                tripSheetStockViewHolder.mDispatchQuantity.setText(String.format("%.3f", orderQuantity));

                // Updating current stock in dispatch list
                currentStockList.setmTripsheetStockDispatchQuantity(String.valueOf(orderQuantity));
                updateProductsDispatchList(currentStockList);




            } else {
                Double dispatchedQuantity = Double.parseDouble(currentStockList.getmTripsheetStockDispatchQuantity());
                tripSheetStockViewHolder.mDispatchQuantity.setText(String.format("%.3f", dispatchedQuantity));
            }




            if (currentStockList.getIsStockVerified() == 0) {
                tripSheetStockViewHolder.mVerifyQuantity.setText(String.format("%.3f", orderQuantity));

                // Updating current stock in verify list
                currentStockList.setmTripsheetStockVerifiedQuantity(String.valueOf(orderQuantity));
                updateProductsVerifyList(currentStockList);



            } else {
                Double verifiedQuantity = Double.parseDouble(currentStockList.getmTripsheetStockVerifiedQuantity());
                tripSheetStockViewHolder.mVerifyQuantity.setText(String.format("%.3f", verifiedQuantity));
            }


        return view;
    }

    public class TripSheetStockViewHolder {
        TextView mProductName;
        TextView mProductCode;
        TextView mProductUom;
        TextView mOrder;
        TextView mDispatchQuantity;
        TextView mVerifyQuantity;

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

    public void updateProductsDispatchList(TripsheetsStockList tripsheetsStockList) {
        try {
            // In HashMap if you are trying to put same key multiple times then the value of key will be override by latest value which you have pass.
            dispatchProductsListHashMap.put(tripsheetsStockList.getmTripsheetStockProductId(), tripsheetsStockList);

            if (listener != null)
                listener.updateProductsDispatchList(dispatchProductsListHashMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProductsVerifyList(TripsheetsStockList tripsheetsStockList) {
        try {
            verifyProductsListHashMap.put(tripsheetsStockList.getmTripsheetStockProductId(), tripsheetsStockList);

            if (listener != null)
                listener.updateProductsVerifyList(verifyProductsListHashMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void removeProductFromDispatchList(TripsheetsStockList tripsheetsStockList) {
        try {
            if (dispatchProductsListHashMap.containsKey(tripsheetsStockList.getmTripsheetStockProductId()))
                dispatchProductsListHashMap.remove(tripsheetsStockList.getmTripsheetStockProductId());

            if (listener != null)
                listener.updateProductsDispatchList(dispatchProductsListHashMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeProductFromVerifyList(TripsheetsStockList tripsheetsStockList) {
        try {
            if (dispatchProductsListHashMap.containsKey(tripsheetsStockList.getmTripsheetStockProductId()))
                dispatchProductsListHashMap.remove(tripsheetsStockList.getmTripsheetStockProductId());
            System.out.println("==== 1 ===== verifyProductsListHashMap = " + verifyProductsListHashMap.size() + " && Pro. Id - " + tripsheetsStockList.getmTripsheetStockProductId());
            if (listener != null)
                listener.updateProductsVerifyList(verifyProductsListHashMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}


