package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.beanclass.Nextdayindent_moreinfoBeen;

import java.util.List;

public class Nextdayindent_MoreinfoAdapter extends BaseAdapter {

    Context context;
    List<Nextdayindent_moreinfoBeen> nextdayIndentMoreInfo;

    public Nextdayindent_MoreinfoAdapter(Context context, List<Nextdayindent_moreinfoBeen> nextdayIndentMoreInfo) {
        this.context = context;
        this.nextdayIndentMoreInfo = nextdayIndentMoreInfo;
    }

    private class ViewHolder {
        TextView date;
        TextView milk;
        TextView curd;
        TextView other;

    }
    @Override
    public int getCount() {
        return nextdayIndentMoreInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return nextdayIndentMoreInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return nextdayIndentMoreInfo.indexOf(getItem(position));
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.nextdayindent_moreinfo_listcard, null);
            holder = new ViewHolder();
            holder.date = (TextView) convertView.findViewById(R.id.nextdayindent_moreinfo_datevalue);
            holder.milk = (TextView) convertView.findViewById(R.id.nextdayindent_moreinfo_milkvalue);
            holder.curd = (TextView) convertView.findViewById(R.id.nextdayindent_moreinfo_curdvalue);
            holder.other = (TextView) convertView.findViewById(R.id.nextdayindent_moreinfo_othervalue);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Nextdayindent_moreinfoBeen nextdayIndentMoreInfo = (Nextdayindent_moreinfoBeen) getItem(position);

       // holder.date.setText(nextdayIndentMoreInfo.getDate());
       // holder.milk.setText(nextdayIndentMoreInfo.getMilk());
      //  holder.curd.setText(nextdayIndentMoreInfo.getCurd());
      //  holder.other.setText(nextdayIndentMoreInfo.getOther());

        return convertView;


    }


}
