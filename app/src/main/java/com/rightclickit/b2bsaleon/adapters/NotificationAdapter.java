package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.beanclass.NotificationBean;

import java.util.List;

/**
 * Created by bhagya on 5/18/2017.
 */

public class NotificationAdapter extends ArrayAdapter<NotificationBean> {

    Context context;

    public NotificationAdapter(Context context, int resourceId,
                                 List<NotificationBean> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView name;
        TextView description;
        TextView date;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        NotificationBean rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.notigicationadapter, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_Sales);
            holder.description = (TextView) convertView.findViewById(R.id.tv_Description);
            holder.date = (TextView) convertView.findViewById(R.id.tv_Date);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.name.setText(rowItem.getName());
        holder.description.setText(rowItem.getDescription());
        holder.date.setText(rowItem.getDate());

        return convertView;
    }
}