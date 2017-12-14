package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.AgentStockActivity;
import com.rightclickit.b2bsaleon.activities.AgentTDC_Order;
import com.rightclickit.b2bsaleon.activities.AgentsActivity;
import com.rightclickit.b2bsaleon.activities.AgentsInfoActivity;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.customviews.CustomAlertDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.Utility;

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
    private String mStock="";

    public AgentsAdapter(Context ctxt, AgentsActivity agentsActivity, ArrayList<AgentsBean> mAgentsBeansList,String mStock) {
        this.ctxt = ctxt;
        this.activity = agentsActivity;
        this.mAgentsBeansList1 = mAgentsBeansList;
        this.mImageLoader = new ImageLoader(agentsActivity);
        this.mInflater = LayoutInflater.from(activity);
        this.mPreferences = new MMSharedPreferences(activity);
        this.arraylist = new ArrayList<AgentsBean>();
        this.mDBHelper = new DBHelper(activity);
        this.arraylist.addAll(mAgentsBeansList1);
        this.mStock=mStock;
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
        if (view == null) {
            mHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.agents_list_custom, null);
            mHolder.id = (TextView) view.findViewById(R.id.tv_uidNo);
            mHolder.mPicImage = (ImageView) view.findViewById(R.id.personImage);
            mHolder.mStatus = (TextView) view.findViewById(R.id.StatusText);
            mHolder.mTitle = (TextView) view.findViewById(R.id.tv_companyName);
            mHolder.mObAmount = (TextView) view.findViewById(R.id.tv_address1);
            mHolder.mOrderValue = (TextView) view.findViewById(R.id.tv_address4);
            mHolder.mTotalAmount = (TextView) view.findViewById(R.id.tv_address6);
            mHolder.mDueAmount = (TextView) view.findViewById(R.id.tv_address8);
            mHolder.viewbtn = (Button) view.findViewById(R.id.btn_view);
            mHolder.infobtn = (Button) view.findViewById(R.id.btn_info);
            mHolder.stockbtn = (Button) view.findViewById(R.id.btnStock);
            mHolder.stockbtn.setVisibility(View.GONE);
            //   AgentsInfoActivity.avatar=(ImageView) view.findViewById(R.id.shopaddress_image);
            mHolder.mPoiImage = (ImageView) view.findViewById(R.id.poiImage);
            mHolder.mPoaImage = (ImageView) view.findViewById(R.id.poaImage);

            mHolder.mEmptyLayout = (LinearLayout) view.findViewById(R.id.EmptyView);

            view.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) view.getTag();
        }





        if (position == mAgentsBeansList1.size() - 1) {
            mHolder.mEmptyLayout.setVisibility(View.VISIBLE);
        } else {
            mHolder.mEmptyLayout.setVisibility(View.GONE);
        }

        if(mStock.equals("ViewStock")){
            mHolder.stockbtn.setVisibility(View.VISIBLE);
        }

        System.out.println("URL===== " + mAgentsBeansList1.get(position).getmAgentPic());
        if (!mAgentsBeansList1.get(position).getmAgentPic().equals("")) {
            String URL = Constants.MAIN_URL + "/b2b/" + mAgentsBeansList1.get(position).getmAgentPic();
            mImageLoader.DisplayImage(URL, mHolder.mPicImage, null, "");
        } else {
            mHolder.mPicImage.setBackgroundResource(R.drawable.logo);
        }
        mHolder.mTitle.setText(mAgentsBeansList1.get(position).getmFirstname());
        mHolder.id.setText(mAgentsBeansList1.get(position).getmAgentCode());
        if (mAgentsBeansList1.get(position).getmStatus().equals("A")) {
            mHolder.mStatus.setText("Active");
        } else {
            mHolder.mStatus.setText("InActive");
        }
        final double obAmount, receivedAmount, ordervalue, due;


        obAmount = mDBHelper.getSoDetails(mAgentsBeansList1.get(position).getmAgentId(), "tripsheet_so_opamount");
        // obAmount =mAgentsBeansList1.get(position).getmObAmount();
        //mHolder.mObAmount.setText(String.valueOf(Utility.getFormattedCurrency(obAmount)));
        if (mAgentsBeansList1.get(position).getmObAmount()!=null && mAgentsBeansList1.get(position).getmObAmount()!="") {
            mHolder.mObAmount.setText(mAgentsBeansList1.get(position).getmObAmount());
        }
        else {
            mHolder.mObAmount.setText("Rs.0.00");
        }
        ordervalue = mDBHelper.getSoDetails(mAgentsBeansList1.get(position).getmAgentId(), "tripsheet_so_value");
        //  mHolder.mOrderValue.setText(String.valueOf(Utility.getFormattedCurrency(ordervalue)));
        if (mAgentsBeansList1.get(position).getmOrderValue()!=null && mAgentsBeansList1.get(position).getmOrderValue()!="") {
            mHolder.mOrderValue.setText(mAgentsBeansList1.get(position).getmOrderValue());
        }else {
            mHolder.mOrderValue.setText("Rs.0.00");
        }
        receivedAmount = mDBHelper.getReceivedAmountDetails(mAgentsBeansList1.get(position).getmAgentId(), "tripsheet_payments_received_amount");

        // mHolder.mTotalAmount.setText(String.valueOf(Utility.getFormattedCurrency(receivedAmount)));
        if (mAgentsBeansList1.get(position).getmTotalAmount()!=null && mAgentsBeansList1.get(position).getmTotalAmount()!="") {
            mHolder.mTotalAmount.setText(mAgentsBeansList1.get(position).getmTotalAmount());
        }else {
            mHolder.mTotalAmount.setText("Rs.0.00");
        }


        due = (obAmount + ordervalue) - receivedAmount;


        //mHolder.mDueAmount.setText(String.valueOf(Utility.getFormattedCurrency(due)));
        if (mAgentsBeansList1.get(position).getmDueAmount()!=null && mAgentsBeansList1.get(position).getmDueAmount()!=""){
            mHolder.mDueAmount.setText(mAgentsBeansList1.get(position).getmDueAmount());
        }
        else {
            mHolder.mDueAmount.setText("Rs.0.00");
        }


        mPreferences.putString("agentNameAdapter", mAgentsBeansList1.get(position).getmFirstname());
        mPreferences.putString("agentCodeAdapter", mAgentsBeansList1.get(position).getmAgentCode());
        mPreferences.putString("incId", String.valueOf(position + 1));



        mHolder.viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAgentsBeansList1.get(position).getmStatus().equals("A") ){

                    mPreferences.putString("agentName", mAgentsBeansList1.get(position).getmFirstname());

                    mPreferences.putString("agentId", mAgentsBeansList1.get(position).getmAgentId());
                    Log.i("agentId", mAgentsBeansList1.get(position).getmAgentId() + "");
                    mPreferences.putString("agentrouteId", mAgentsBeansList1.get(position).getmAgentRouteId());
                    //mPreferences.putString("enqId", String.valueOf(position + 1));
                    // Added by sekhar
                    String enqidd = "ENQ-"+mAgentsBeansList1.get(position).getmAgentCode();
                    mPreferences.putString("enqId", enqidd);
                    mPreferences.putString("agentCode", mAgentsBeansList1.get(position).getmAgentCode());

                    mPreferences.putString("ObAmount", String.valueOf(Utility.getFormattedCurrency(obAmount)));
                    mPreferences.putString("OrderValue", String.valueOf(Utility.getFormattedCurrency(ordervalue)));
                    mPreferences.putString("ReceivedAmount", String.valueOf(Utility.getFormattedCurrency(receivedAmount)));
                    mPreferences.putString("due", String.valueOf(Utility.getFormattedCurrency(due)));
                    Intent i = new Intent(activity, AgentTDC_Order.class);
                    activity.startActivity(i);

                    activity.finish();


                }    else{
                    CustomAlertDialog.showAlertDialog(ctxt, "Failed", activity.getResources().getString(R.string.agent));
                }}


        });

        mHolder.infobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mAgentsBeansList1.get(position).getmStatus().equals("A") ){
                    Intent intent = new Intent(activity, AgentsInfoActivity.class);
                    Bundle bundle = new Bundle();
                    //Add your data from getFactualResults method to bundle

                    bundle.putString("FIRSTNAME", mAgentsBeansList1.get(position).getmFirstname());
                    bundle.putString("LASTNAME", mAgentsBeansList1.get(position).getmLastname());
                    bundle.putString("MOBILE", mAgentsBeansList1.get(position).getMphoneNO());
                    bundle.putString("ADDRESS", mAgentsBeansList1.get(position).getMaddress());
                    bundle.putString("AGENTCODE", mAgentsBeansList1.get(position).getmAgentCode());

                    intent.putExtra("AVATAR", mAgentsBeansList1.get(position).getmAgentPic());

                    intent.putExtra("POI", mAgentsBeansList1.get(position).getmPoiImage());

                    intent.putExtra("POA", mAgentsBeansList1.get(position).getmPoaImage());
                    mHolder.mPicImage.buildDrawingCache();
                    mHolder.mPoiImage.buildDrawingCache();
                    mHolder.mPoaImage.buildDrawingCache();
                    Bitmap avatar = mHolder.mPicImage.getDrawingCache();
                    Log.i("avatar", avatar + "");
                    Bitmap poi = mHolder.mPoiImage.getDrawingCache();
                    Log.i("poi", poi + "");
                    Bitmap poa = mHolder.mPoaImage.getDrawingCache();
                    Bundle extras = new Bundle();
                    extras.putParcelable("avatar", avatar);
                    extras.putParcelable("poi", poi);
                    extras.putParcelable("poa", poa);
                    intent.putExtras(extras);
                    //Add the bundle to the intent
                    intent.putExtras(bundle);

                    activity.startActivity(intent);
                    activity.finish();


                }    else{
                    CustomAlertDialog.showAlertDialog(ctxt, "Failed", activity.getResources().getString(R.string.agent));

                }}
        });

        mHolder.stockbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAgentsBeansList1.get(position).getmStatus().equals("A")) {
                    Intent intent = new Intent(activity, AgentStockActivity.class);
                    intent.putExtra("agentId", mAgentsBeansList1.get(position).getmAgentId());
                    intent.putExtra("agentCode", mAgentsBeansList1.get(position).getmAgentCode());
                    intent.putExtra("agentName", mAgentsBeansList1.get(position).getmFirstname());
                    activity.startActivity(intent);
                    activity.finish();


                } else {

                    CustomAlertDialog.showAlertDialog(ctxt, "Failed", activity.getResources().getString(R.string.agent));
                }
            }
        });
        mHolder.mPicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new NetworkConnectionDetector(activity).isNetworkConnected()) {
                    if (!mAgentsBeansList1.get(position).getmAgentPic().equals("")) {
                        String URL = Constants.MAIN_URL + "/b2b/" + mAgentsBeansList1.get(position).getmAgentPic();


                        showProductImageFull(URL);
                    } else {
                        Toast.makeText(ctxt, "Agent image not available..!Selected is the default image", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    public class ViewHolder {
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
        public Button stockbtn;
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

    private void showProductImageFull(String url) {
        final AlertDialog.Builder alertadd = new AlertDialog.Builder(activity);
        LayoutInflater factory = LayoutInflater.from(activity);
        final View view = factory.inflate(R.layout.image_full_screen_layout, null);
        alertadd.setView(view);

        ImageView iv = (ImageView) view.findViewById(R.id.dialog_imageview);
        mImageLoader.DisplayImage(url, iv, null, "");

        alertadd.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                dlg.dismiss();
            }
        });

        alertadd.show();
    }
}
