package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.AgentsActivity;
import com.rightclickit.b2bsaleon.activities.Products_Activity;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;

import java.util.ArrayList;

/**
 * Created by PPS on 5/25/2017.
 */

public class ProductsAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    private Activity activity;
    Context ctxt;
    ArrayList<ProductsBean> mProductsBeansList1;
    private ImageLoader mImageLoader;

    public ProductsAdapter(Products_Activity productsActivity, ArrayList<ProductsBean> mProductsBeansList1) {
        this.activity = productsActivity;
        this.mProductsBeansList1 = mProductsBeansList1;
        this.mImageLoader = new ImageLoader(productsActivity);
        this.mInflater = LayoutInflater.from(activity);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;

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
        System.out.println("URL===== "+mProductsBeansList1.get(position).getProductImage());
        if (!mProductsBeansList1.get(position).getProductImage().equals("")){
            mImageLoader.DisplayImage(mProductsBeansList1.get(position).getProductImage(),holder.productImage,null,"");
        }
        holder.productTitle.setText(mProductsBeansList1.get(position).getProductTitle());
        if (mProductsBeansList1.get(position).getProductReturnUnit().equals("Y")){
            holder.product_Unit.setText("YES");
        }else {
            holder.product_Unit.setText("NO");
        }


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
}