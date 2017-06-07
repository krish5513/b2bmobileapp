package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.AgentsActivity;
import com.rightclickit.b2bsaleon.activities.AgentsInfoActivity;
import com.rightclickit.b2bsaleon.activities.ViewAgent;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Sekhar Kuppa
 */

public class AgentsAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    private Activity activity;
    Context ctxt;
    ArrayList<AgentsBean> mAgentsBeansList1;
    private ImageLoader mImageLoader;
    private MMSharedPreferences mPreferences;
    private ArrayList<AgentsBean> arraylist;

    public AgentsAdapter(Context ctxt,AgentsActivity agentsActivity, ArrayList<AgentsBean> mAgentsBeansList) {
        this.ctxt=ctxt;
        this.activity = agentsActivity;
        this.mAgentsBeansList1 = mAgentsBeansList;
        this.mImageLoader = new ImageLoader(agentsActivity);
        this.mInflater = LayoutInflater.from(activity);
        this.mPreferences = new MMSharedPreferences(activity);
        this.arraylist = new ArrayList<AgentsBean>();
        this.arraylist.addAll(mAgentsBeansList1);
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
    public View getView(final int position, View view, ViewGroup viewGroup) {
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
            mHolder.viewbtn = (Button) view.findViewById(R.id.btn_view);
            mHolder.infobtn = (Button) view.findViewById(R.id.btn_info);

            view.setTag(mHolder);
        }else {
            mHolder = (ViewHolder) view.getTag();
        }

        System.out.println("URL===== "+mAgentsBeansList1.get(position).getmAgentPic());
        if (!mAgentsBeansList1.get(position).getmAgentPic().equals("")){
            mImageLoader.DisplayImage(mAgentsBeansList1.get(position).getmAgentPic(),mHolder.mPicImage,null,"");
        }
        mHolder.mTitle.setText(mAgentsBeansList1.get(position).getmAgentName());
        if (mAgentsBeansList1.get(position).getmStatus().equals("A")){
            mHolder.mStatus.setText("Active");
        }else {
            mHolder.mStatus.setText("InActive");
        }


        mHolder.viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreferences.putString("agentName",mAgentsBeansList1.get(position).getmAgentName());
                activity.startActivity(new Intent(activity,ViewAgent.class));
                activity.finish();
            }
        });

        mHolder.infobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent=new Intent(ctxt,AgentsInfoActivity.class);
               // Bundle bundle = new Bundle();
                //Add your data from getFactualResults method to bundle
                mPreferences.putString("FIRSTNAME", mAgentsBeansList1.get(position).getmAgentName());

                Log.i("firstnamebhagya",mAgentsBeansList1.get(position).getmAgentCode()+"");
                mPreferences.putString("LASTNAME", mAgentsBeansList1.get(position).getmLastname());
                mPreferences.putString("MOBILE", mAgentsBeansList1.get(position).getMphoneNO());
                Log.i("mobilebhagya",mAgentsBeansList1.get(position).getMphoneNO()+"");
                mPreferences.putString("ADDRESS", mAgentsBeansList1.get(position).getMaddress());
                //Add the bundle to the intent
                //intent.putExtras(bundle);

                activity.startActivity(new Intent(activity,AgentsInfoActivity.class));
                activity.finish();
            }
        });


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
        public Button viewbtn;
        public Button infobtn;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mAgentsBeansList1.clear();
        if (charText.length() == 0) {
            mAgentsBeansList1.addAll(arraylist);
        } else {
            for (AgentsBean wp : arraylist) {
                if (wp.getmAgentName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mAgentsBeansList1.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
