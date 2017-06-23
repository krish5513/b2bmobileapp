package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.AgentViewActivity;
import com.rightclickit.b2bsaleon.activities.AgentsActivity;
import com.rightclickit.b2bsaleon.activities.AgentsInfoActivity;
import com.rightclickit.b2bsaleon.activities.Agents_AddActivity;
import com.rightclickit.b2bsaleon.activities.AgentViewActivity;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
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
    private DBHelper mDBHelper;

    public AgentsAdapter(Context ctxt,AgentsActivity agentsActivity, ArrayList<AgentsBean> mAgentsBeansList) {
        this.ctxt=ctxt;
        this.activity = agentsActivity;
        this.mAgentsBeansList1 = mAgentsBeansList;
        this.mImageLoader = new ImageLoader(agentsActivity);
        this.mInflater = LayoutInflater.from(activity);
        this.mPreferences = new MMSharedPreferences(activity);
        this.arraylist = new ArrayList<AgentsBean>();
        this.mDBHelper=new DBHelper(activity);
        this.arraylist.addAll(mAgentsBeansList1);
    }
    public AgentsAdapter(Context ctxt, Agents_AddActivity agentsActivity, ArrayList<AgentsBean> mAgentsBeansList) {
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
        final ViewHolder mHolder;
        if(view == null){
            mHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.agents_list_custom,null);
            mHolder.id=(TextView)view.findViewById(R.id.tv_uidNo) ;
            mHolder.mPicImage = (ImageView) view.findViewById(R.id.personImage);
            mHolder.mStatus = (TextView) view.findViewById(R.id.StatusText);
            mHolder.mTitle = (TextView) view.findViewById(R.id.tv_companyName);
            mHolder.mObAmount = (TextView) view.findViewById(R.id.tv_address1);
            mHolder.mOrderValue = (TextView) view.findViewById(R.id.tv_address4);
            mHolder.mTotalAmount = (TextView) view.findViewById(R.id.tv_address6);
            mHolder.mDueAmount = (TextView) view.findViewById(R.id.tv_address8);
            mHolder.viewbtn = (Button) view.findViewById(R.id.btn_view);
            mHolder.infobtn = (Button) view.findViewById(R.id.btn_info);
         //   AgentsInfoActivity.avatar=(ImageView) view.findViewById(R.id.shopaddress_image);
            mHolder.mPoiImage=(ImageView) view.findViewById(R.id.poiImage);
            mHolder.mPoaImage=(ImageView) view.findViewById(R.id.poaImage);

            mHolder.mEmptyLayout = (LinearLayout) view.findViewById(R.id.EmptyView);

            view.setTag(mHolder);
        }else {
            mHolder = (ViewHolder) view.getTag();
        }

        if(position == mAgentsBeansList1.size()-1){
            mHolder.mEmptyLayout.setVisibility(View.VISIBLE);
        }else {
            mHolder.mEmptyLayout.setVisibility(View.GONE);
        }


        System.out.println("URL===== "+mAgentsBeansList1.get(position).getmAgentPic());
        if (!mAgentsBeansList1.get(position).getmAgentPic().equals("")){
            String URL = Constants.MAIN_URL + "/b2b/" + mAgentsBeansList1.get(position).getmAgentPic();
            mImageLoader.DisplayImage(URL,mHolder.mPicImage,null,"");
        }else {
            mHolder.mPicImage.setBackgroundResource(R.drawable.logo);
        }
        mHolder.mTitle.setText(mAgentsBeansList1.get(position).getmFirstname());
        mHolder.id.setText(mAgentsBeansList1.get(position).getmAgentCode());
        if (mAgentsBeansList1.get(position).getmStatus().equals("A")){
            mHolder.mStatus.setText("Active");
        }else {
            mHolder.mStatus.setText("InActive");
        }


        mHolder.viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreferences.putString("agentName",mAgentsBeansList1.get(position).getmFirstname());
                activity.startActivity(new Intent(activity,AgentViewActivity.class));
                activity.finish();
            }
        });

        mHolder.infobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity,AgentsInfoActivity.class);
                Bundle bundle = new Bundle();
                //Add your data from getFactualResults method to bundle
             //   bundle.putString("AGENTNAME", mAgentsBeansList1.get(position).getmFirstname());
                bundle.putString("FIRSTNAME", mAgentsBeansList1.get(position).getmFirstname());
                bundle.putString("LASTNAME", mAgentsBeansList1.get(position).getmLastname());
                bundle.putString("MOBILE", mAgentsBeansList1.get(position).getMphoneNO());
                bundle.putString("ADDRESS", mAgentsBeansList1.get(position).getMaddress());
                bundle.putString("AGENTCODE", mAgentsBeansList1.get(position).getmAgentCode());

                intent.putExtra("AVATAR", mAgentsBeansList1.get(position).getmAgentPic());
               // Log.i("avatarimage", mAgentsBeansList1.get(position).getmAgentPic());
                intent.putExtra("POI", mAgentsBeansList1.get(position).getmPoiImage());
                //Log.i("poi_image", mAgentsBeansList1.get(position).getmPoiImage());
                intent.putExtra("POA", mAgentsBeansList1.get(position).getmPoaImage());
                mHolder.mPicImage.buildDrawingCache();
                mHolder.mPoiImage.buildDrawingCache();
                mHolder.mPoaImage.buildDrawingCache();
                Bitmap avatar=   mHolder.mPicImage.getDrawingCache();
                Log.i("avatar",avatar+"");
                Bitmap poi= mHolder.mPoiImage.getDrawingCache();
                Log.i("poi",poi+"");
                Bitmap poa= mHolder.mPoaImage.getDrawingCache();
                Bundle extras = new Bundle();
                extras.putParcelable("avatar", avatar);
                extras.putParcelable("poi", poi);
                extras.putParcelable("poa", poa);
                intent.putExtras(extras);
                //Add the bundle to the intent
                intent.putExtras(bundle);

                activity.startActivity(intent);
                activity.finish();
            }
        });


        return view;
    }

    public class ViewHolder{
        TextView id;
        TextView mTitle;
        TextView mObAmount;
        TextView mOrderValue;
        TextView mTotalAmount;
        TextView mDueAmount;
        TextView mStatus;
        ImageView mPicImage;
        ImageView mPoiImage;
        ImageView mPoaImage;
        public Button viewbtn;
        public Button infobtn;
        LinearLayout mEmptyLayout;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mAgentsBeansList1.clear();
        if (charText.length() == 0) {
            mAgentsBeansList1.addAll(arraylist);
        } else {
            for (AgentsBean wp : arraylist) {
                if (wp.getmFirstname().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mAgentsBeansList1.add(wp);
                }
                if (wp.getmAgentCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mAgentsBeansList1.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
