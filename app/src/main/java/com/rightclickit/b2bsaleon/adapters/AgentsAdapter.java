package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.AgentsActivity;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Sekhar Kuppa
 */

public class AgentsAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    private Activity activity;
    Context ctxt;
    ArrayList<AgentsBean> mAgentsBeansList1;
    private ImageLoader mImageLoader;

    public AgentsAdapter(AgentsActivity agentsActivity, ArrayList<AgentsBean> mAgentsBeansList) {
        this.activity = agentsActivity;
        this.mAgentsBeansList1 = mAgentsBeansList;
        this.mImageLoader = new ImageLoader(agentsActivity);
        this.mInflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return mAgentsBeansList1.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder mHolder = null;
        if(view == null){
            mHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.agents_list_custom,null);

            mHolder.mPicImage = (ImageView) view.findViewById(R.id.personImage);
            mHolder.mStatus = (TextView) view.findViewById(R.id.StatusText);
            mHolder.mTitle = (TextView) view.findViewById(R.id.tv_companyName);
            mHolder.mObAmount = (TextView) view.findViewById(R.id.tv_address1);
            mHolder.mOrderValue = (TextView) view.findViewById(R.id.tv_address4);
            mHolder.mTotalAmount = (TextView) view.findViewById(R.id.tv_address6);
            mHolder.mDueAmount = (TextView) view.findViewById(R.id.tv_address8);

            view.setTag(mHolder);
        }else {
            mHolder = (ViewHolder) view.getTag();
        }

        System.out.println("URL===== "+mAgentsBeansList1.get(i).getmAgentPic());
        if (!mAgentsBeansList1.get(i).getmAgentPic().equals("")){
            mImageLoader.DisplayImage(mAgentsBeansList1.get(i).getmAgentPic(),mHolder.mPicImage,null,"");
        }
        mHolder.mTitle.setText(mAgentsBeansList1.get(i).getmAgentName());
        if (mAgentsBeansList1.get(i).getmStatus().equals("A")){
            mHolder.mStatus.setText("Active");
        }else {
            mHolder.mStatus.setText("InActive");
        }

        return view;
    }

    public class ViewHolder{
        TextView mTitle;
        TextView mObAmount;
        TextView mOrderValue;
        TextView mTotalAmount;
        TextView mDueAmount;
        TextView mStatus;
        ImageView mPicImage;
    }
}
