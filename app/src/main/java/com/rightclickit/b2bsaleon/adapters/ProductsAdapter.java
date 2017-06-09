package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.rightclickit.b2bsaleon.activities.ProductViewActivity;
import com.rightclickit.b2bsaleon.activities.Products_Activity;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by PPS on 5/25/2017.
 */

public class ProductsAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    private Activity activity;
    Context ctxt;
    ArrayList<ProductsBean> mProductsBeansList1;
    private ImageLoader mImageLoader;
    private ArrayList<ProductsBean> arraylist;
    private MMSharedPreferences mPreferences;


    public ProductsAdapter(Context ctxt,Products_Activity productsActivity, ArrayList<ProductsBean> mProductsBeansList1) {
        this.ctxt=ctxt;
        this.activity = productsActivity;
        this.mProductsBeansList1 = mProductsBeansList1;
        this.mImageLoader = new ImageLoader(productsActivity);
        this.mInflater = LayoutInflater.from(activity);
        this.arraylist = new ArrayList<ProductsBean>();
        this.arraylist.addAll(mProductsBeansList1);
        this.mPreferences = new MMSharedPreferences(activity);
    }


    @Override
    public int getCount() {
        return mProductsBeansList1.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MyViewHolder holder;

        if (convertView == null) {

            holder = new MyViewHolder();
            convertView = mInflater.inflate(R.layout.products_adapter, null);

            holder.productCode = (TextView)convertView. findViewById(R.id.materialCode);
            holder.product_Unit = (TextView) convertView.findViewById(R.id.material_Unit);
            holder.productTitle = (TextView) convertView.findViewById(R.id.materialTitle);
            holder.materialRetailer = (TextView) convertView.findViewById(R.id.tv_Retailer);
            holder.materialRetailerUnit = (TextView) convertView.findViewById(R.id.materialMRPUnit);
            holder.materialMOQ = (TextView) convertView.findViewById(R.id.tv_moq);
            holder.materialMOQUnit = (TextView) convertView.findViewById(R.id.materialMOQUnit);
            holder.materialConsumer = (TextView) convertView.findViewById(R.id.tv_Consumer);
            holder.materialConsumerUnit= (TextView) convertView.findViewById(R.id.materialSPUnit);
            holder.materialReturnable= (TextView) convertView.findViewById(R.id.material_Returnable);
            holder.productImage = (ImageView) convertView.findViewById(R.id.materialImage);
            holder.downarrowImage=(ImageView) convertView.findViewById(R.id.img);
            holder.viewbtn=(Button) convertView.findViewById(R.id.btnView);
            holder.materialAgent=(TextView)convertView.findViewById(R.id.materialAgent);
            holder.materialAgentUnit=(TextView)convertView.findViewById(R.id.agentUnit);
            holder.stockbtn=(Button) convertView.findViewById(R.id.btnStock);

            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        holder.productCode.setText(mProductsBeansList1.get(position).getProductCode());
        if(mProductsBeansList1.get(position).getProductReturnable().equals("N")){
            holder.product_Unit.setText("NO");
        }else if(mProductsBeansList1.get(position).getProductReturnable().equals("Y")){
            holder.product_Unit.setText("YES");
        }
        holder.productTitle.setText(mProductsBeansList1.get(position).getProductTitle());
        holder.materialMOQUnit.setText(mProductsBeansList1.get(position).getProductMOQ());

        if(mProductsBeansList1.get(position).getProductAgentPrice()!=null) {
            if (mProductsBeansList1.get(position).getProductAgentPrice().length() == 0) {
                holder.materialAgentUnit.setText("-");
            } else {
                holder.materialAgentUnit.setText(mProductsBeansList1.get(position).getProductAgentPrice());
            }
        }else {
            holder.materialAgentUnit.setText("-");
        }

        if(mProductsBeansList1.get(position).getProductRetailerPrice()!=null) {
            if (mProductsBeansList1.get(position).getProductRetailerPrice().length() == 0) {
                holder.materialRetailerUnit.setText("-");
            } else {
                holder.materialRetailerUnit.setText(mProductsBeansList1.get(position).getProductRetailerPrice());
            }
        }else {
            holder.materialRetailerUnit.setText("-");
        }

        if(mProductsBeansList1.get(position).getProductConsumerPrice()!=null) {
            if (mProductsBeansList1.get(position).getProductConsumerPrice().length() == 0) {
                holder.materialConsumerUnit.setText("-");
            } else {
                holder.materialConsumerUnit.setText(mProductsBeansList1.get(position).getProductConsumerPrice());
            }
        }else {
            holder.materialConsumerUnit.setText("-");
        }
        //System.out.println("URL===== "+mProductsBeansList1.get(position).getProductImageUrl());
        if(mProductsBeansList1.get(position).getProductImageUrl()!=null) {
            if (!mProductsBeansList1.get(position).getProductImageUrl().equals("")) {
                mImageLoader.DisplayImage(mProductsBeansList1.get(position).getProductImageUrl(), holder.productImage, null, "");
            }
        }

        holder.viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity,ProductViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("CODE", mProductsBeansList1.get(position).getProductCode());
                Log.i("firstnamebhagya",mProductsBeansList1.get(position).getProductCode()+"");
                bundle.putString("TITLE", mProductsBeansList1.get(position).getProductTitle());
                bundle.putString("RETURNABLE", mProductsBeansList1.get(position).getProductReturnable());
                bundle.putString("MOQ", mProductsBeansList1.get(position).getProductMOQ());
                bundle.putString("AGENT", mProductsBeansList1.get(position).getProductAgentPrice());
                bundle.putString("RETAILER", mProductsBeansList1.get(position).getProductRetailerPrice());
                bundle.putString("CONSUMER", mProductsBeansList1.get(position).getProductConsumerPrice());
                 intent.putExtra("IMAGE", mProductsBeansList1.get(position).getProductImageUrl());
                holder.productImage.buildDrawingCache();
                 Bitmap image= holder.productImage.getDrawingCache();
                Bundle extras = new Bundle();
                extras.putParcelable("imagebitmap", image);
                intent.putExtras(extras);

                intent.putExtras(bundle);

                activity.startActivity(intent);
                activity.finish();
            }
        });

        return convertView;
    }

    private class MyViewHolder {
        public TextView productCode;
        public ImageView productImage;
        public ImageView downarrowImage;
        public TextView product_Unit;
        public TextView productTitle;
        public TextView materialAgent,materialAgentUnit;
        public TextView materialRetailer,materialRetailerUnit;
        public TextView materialMOQ,materialMOQUnit;
        public TextView materialConsumer,materialConsumerUnit;
        public TextView materialReturnable;
        public Button viewbtn;

        public Button stockbtn;


    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mProductsBeansList1.clear();
        if (charText.length() == 0) {
            mProductsBeansList1.addAll(arraylist);
        } else {
            for (ProductsBean wp : arraylist) {
                if (wp.getProductTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mProductsBeansList1.add(wp);
                }

                if (wp.getProductCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mProductsBeansList1.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}