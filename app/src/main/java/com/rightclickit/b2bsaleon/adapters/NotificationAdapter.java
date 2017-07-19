package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.NotificationsActivity;
import com.rightclickit.b2bsaleon.activities.TripsheetReturns;
import com.rightclickit.b2bsaleon.beanclass.NotificationBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetReturnsBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhagya on 5/18/2017.
 */

public class NotificationAdapter extends BaseAdapter {


    LayoutInflater mInflater;
    private Activity activity;
    Context ctxt;
    ArrayList<NotificationBean> mNotifications;
    private ImageLoader mImageLoader;
    private MMSharedPreferences mPreferences;
    private ArrayList<NotificationBean> arraylist;
    private DBHelper mDBHelper;


    public NotificationAdapter(Context ctxt, NotificationsActivity notificationsActivity, ArrayList<NotificationBean> mnotificationsBeanList) {
        this.ctxt = ctxt;
        this.activity = notificationsActivity;
        this.mNotifications = mnotificationsBeanList;

        this.mInflater = LayoutInflater.from(activity);
        this.mPreferences = new MMSharedPreferences(activity);
        this.arraylist = new ArrayList<NotificationBean>();
        this.mDBHelper = new DBHelper(activity);
        this.arraylist.addAll(mNotifications);

    }

    @Override
    public int getCount() {
        return 10;
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
        final NotificationAdapter.ViewHolder mHolder;
        if (view == null) {
            mHolder = new NotificationAdapter.ViewHolder();
            view = mInflater.inflate(R.layout.notigicationadapter, null);
            mHolder.name = (TextView) view.findViewById(R.id.tv_Sales);
            mHolder.description = (TextView) view.findViewById(R.id.tv_Description);
            mHolder.date = (TextView) view.findViewById(R.id.tv_Date);


            view.setTag(mHolder);
        } else {
            mHolder = (NotificationAdapter.ViewHolder) view.getTag();
        }


        mHolder.name.setText(mNotifications.get(position).getName());
        mHolder.description.setText(mNotifications.get(position).getDescription());
        mHolder.date.setText(mNotifications.get(position).getDate());




        return view;
    }

    public class ViewHolder {
        TextView name;
        TextView description;
        TextView date;

    }



}


















