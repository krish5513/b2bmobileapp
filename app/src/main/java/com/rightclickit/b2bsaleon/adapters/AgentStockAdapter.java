package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.AgentStockActivity;
import com.rightclickit.b2bsaleon.activities.TripsheetStockPreview;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.beanclass.AgentsStockBean;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.TripSheetStockListener;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by PPS on 8/26/2017.
 */

public class AgentStockAdapter extends BaseAdapter {

    private Context ctxt;
    private Activity activity;
    private TripSheetStockListener listener;
    private MMSharedPreferences mPreferences;
    private LayoutInflater mInflater;
    String myList;
    DBHelper mDBHelper;
    private ArrayList<AgentsStockBean> stockList;
    private ArrayList<AgentsStockBean> arraylist;

    public AgentStockAdapter(Activity agentStockActivity, Context ctxt, ArrayList<AgentsStockBean> mAgentsBeansList) {
        this.ctxt = ctxt;
        this.activity = agentStockActivity;

        this.mInflater = LayoutInflater.from(activity);

        this.mPreferences = new MMSharedPreferences(activity);

        mDBHelper = new DBHelper(activity);
        this.arraylist = new ArrayList<AgentsStockBean>();
        this.stockList = mAgentsBeansList;
        this.arraylist.addAll(stockList);
    }


    @Override
    public int getCount() {
        return stockList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final AgentStockViewHolder agentStockViewHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.tripsheetstock_preview_custom, null);

            agentStockViewHolder = new AgentStockViewHolder();
            agentStockViewHolder.mProductName = (TextView) view.findViewById(R.id.productName);
            agentStockViewHolder.mProductSIPCode = (TextView) view.findViewById(R.id.productCode);
            agentStockViewHolder.mProductUom = (TextView) view.findViewById(R.id.UOM);
            agentStockViewHolder.mRecd = (TextView) view.findViewById(R.id.order);

            agentStockViewHolder.mSales = (TextView) view.findViewById(R.id.dispatchProduct);

            agentStockViewHolder.mCB = (TextView) view.findViewById(R.id.verifyProduct);


            view.setTag(agentStockViewHolder);
        } else {
            agentStockViewHolder = (AgentStockViewHolder) view.getTag();
        }


        agentStockViewHolder.mProductName.setText(stockList.get(position).getmProductName());
        agentStockViewHolder.mProductSIPCode.setText(stockList.get(position).getmProductCode());
        agentStockViewHolder.mProductUom.setText(stockList.get(position).getmProductUOM());
        agentStockViewHolder.mRecd.setText(stockList.get(position).getmProductStockQunatity());
        agentStockViewHolder.mSales.setText(stockList.get(position).getmProductDeliveryQunatity());
        agentStockViewHolder.mCB.setText(stockList.get(position).getmProductCBQuantity());

        return view;
    }

    public class AgentStockViewHolder {
        TextView mProductName;
        TextView mProductSIPCode;
        TextView mProductUom;
        TextView mRecd;
        TextView mSales;
        TextView mCB;

    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        stockList.clear();
        if (charText.length() == 0) {
            stockList.addAll(arraylist);
        } else {
            for (AgentsStockBean wp : arraylist) {
                if (wp.getmProductName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    stockList.add(wp);
                }
                if (wp.getmProductCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                    stockList.add(wp);
                }
                if (wp.getmProductUOM().toLowerCase(Locale.getDefault()).contains(charText)) {
                    stockList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}




