package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.TripSheetStock;
import com.rightclickit.b2bsaleon.activities.TripSheetView;
import com.rightclickit.b2bsaleon.beanclass.TripsheetSOList;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Sekhar Kuppa
 */

public class TripsheetsSOListAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    private Activity activity;
    ArrayList<TripsheetSOList> mTripSheetsList;
    private ImageLoader mImageLoader;
    private MMSharedPreferences mPreferences;
    private ArrayList<TripsheetSOList> arraylist;
    private DBHelper mDBHelper;
    private String mTakeOrderPrivilege = "", mStockVerifyPrivilege = "";

    public TripsheetsSOListAdapter(TripSheetView tripSheetView, TripSheetView tripSheetView1, ArrayList<TripsheetSOList> tripsSOList, String mTakeOrderPrivilege) {
        this.activity = tripSheetView;
        this.mTripSheetsList = tripsSOList;
        this.mInflater = LayoutInflater.from(activity);
        this.mPreferences = new MMSharedPreferences(activity);
        this.arraylist = new ArrayList<TripsheetSOList>();
        this.mDBHelper = new DBHelper(activity);
        this.arraylist.addAll(mTripSheetsList);
        this.mTakeOrderPrivilege = mTakeOrderPrivilege;
        System.out.println("TO PRIVILEGE=" + mTakeOrderPrivilege);
    }


    @Override
    public int getCount() {
        return mTripSheetsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder mHolder;
        if (view == null) {
            mHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.tripsheets_so_list_custom, null);

            mHolder.mAgentCode = (TextView) view.findViewById(R.id.tv_agent_code);
            mHolder.mSODate = (TextView) view.findViewById(R.id.tv_so_date);
            mHolder.mSOItemsCount = (TextView) view.findViewById(R.id.tv_itemsQty);
            mHolder.mSOAgentName = (TextView) view.findViewById(R.id.companyName);
            mHolder.mSOOrderedValue = (TextView) view.findViewById(R.id.tv_orderValue);
            mHolder.mSOReceivedValue = (TextView) view.findViewById(R.id.tv_receivedValue);
            mHolder.mSODueValue = (TextView) view.findViewById(R.id.tv_dueValue);
            mHolder.mSOTakeOrder = (Button) view.findViewById(R.id.btn_sale_ord1);
            mHolder.mSOTakeOrder.setVisibility(View.GONE);
            mHolder.mSOMapIconParent = (LinearLayout) view.findViewById(R.id.gotoCustomer);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }

        if (mTakeOrderPrivilege.equals("list_view_takeorder")) {
            mHolder.mSOTakeOrder.setVisibility(View.VISIBLE);
        }

        mHolder.mAgentCode.setText(mTripSheetsList.get(position).getmTripshetSOAgentCode());
        mHolder.mSODate.setText(mTripSheetsList.get(position).getmTripshetSODate());
        mHolder.mSOItemsCount.setText("");
        mHolder.mSOAgentName.setText(mTripSheetsList.get(position).getmTripshetSOAgentFirstName());
        mHolder.mSOOrderedValue.setText(mTripSheetsList.get(position).getmTripshetSOValue());

        return view;
    }

    public class ViewHolder {
        TextView mAgentCode;
        TextView mSODate;
        TextView mSOItemsCount;
        TextView mSOAgentName;
        TextView mSOOrderedValue;
        TextView mSOReceivedValue;
        TextView mSODueValue;
        Button mSOTakeOrder;
        LinearLayout mSOMapIconParent;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mTripSheetsList.clear();
        if (charText.length() == 0) {
            mTripSheetsList.addAll(arraylist);
        } else {
            for (TripsheetSOList wp : arraylist) {
                if (wp.getmTripshetSOAgentCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mTripSheetsList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
