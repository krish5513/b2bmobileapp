package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.TripsheetStockPreview;
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

public class AgentStockAdapter extends BaseAdapter{

    private Context ctxt;
    private Activity activity;
    private TripSheetStockListener listener;
    private MMSharedPreferences mPreferences;
    private LayoutInflater mInflater;
    String myList ;
    DBHelper mDBHelper;




    public AgentStockAdapter(Context ctxt) {
        this.ctxt = ctxt;

        this.mInflater = LayoutInflater.from(activity);

        this.mPreferences = new MMSharedPreferences(activity);

        mDBHelper=new DBHelper(activity);
    }


    @Override
    public int getCount() {
        return 0;
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
        final AgentStockAdapter.AgentStockViewHolder agentStockViewHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.tripsheetstock_preview_custom, null);

            agentStockViewHolder = new AgentStockAdapter.AgentStockViewHolder();
            agentStockViewHolder.mProductName = (TextView) view.findViewById(R.id.productName);
            agentStockViewHolder.mProductSIPCode = (TextView) view.findViewById(R.id.productCode);
            agentStockViewHolder.mProductUom = (TextView) view.findViewById(R.id.UOM);
            agentStockViewHolder.mRecd = (TextView) view.findViewById(R.id.order);

            agentStockViewHolder.mSales = (TextView) view.findViewById(R.id.dispatchProduct);

            agentStockViewHolder.mCB = (TextView) view.findViewById(R.id.verifyProduct);


            view.setTag(agentStockViewHolder);
        } else {
            agentStockViewHolder = (AgentStockAdapter.AgentStockViewHolder) view.getTag();
        }


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





}



