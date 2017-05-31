package com.rightclickit.b2bsaleon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;

import java.util.ArrayList;

/**
 * Created by PPS on 5/25/2017.
 */

public class ProductsAdapter extends BaseAdapter {

    ArrayList<ProductsBean> myList = new ArrayList<ProductsBean>();
    LayoutInflater inflater;
    Context context;


    public ProductsAdapter(Context context, ArrayList<ProductsBean> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {

        return myList.size();
    }

    @Override
    public ProductsBean getItem(int position)
    {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.products_adapter, parent, false);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        ProductsBean rowItem = getItem(position);

        holder.materialCode.setText(rowItem.getCode());
        holder.materialMOQUnit.setText(rowItem.getMaterialMOQUnit());
        holder.material_Unit.setText(rowItem.getMaterialUnit());
        holder.materialTitle.setText(rowItem.getMaterialTitle());
        holder.materialRetailer.setText(rowItem.getMaterialRetailer());
        holder.materialRetailerUnit.setText(rowItem.getMaterialRetailerUnit());
        holder.materialMOQ.setText(rowItem.getMaterialMOQ());
        holder.materialConsumer.setText(rowItem.getMaterialConsumer());
        holder.materialConsumerUnit.setText(rowItem.getMaterialConsumerUnit());
        holder.materialReturnable.setText(rowItem.getMaterialReturnable());
        holder.materialImage.setImageResource((rowItem.getMaterialImage()));
        holder.downarrowImage.setImageResource(rowItem.getDownarrowImage());
        holder.materialAgent.setText(rowItem.getMaterialAgent());
        holder.materialAgentUnit.setText(rowItem.getMaterialAgentunit());

        return convertView;
    }

    private class MyViewHolder {
        public TextView materialCode;
        public ImageView materialImage;
        public ImageView downarrowImage;
        public TextView material_Unit;
        public TextView materialTitle;
        public TextView materialAgent,materialAgentUnit;
        public TextView materialRetailer,materialRetailerUnit;
        public TextView materialMOQ,materialMOQUnit;
        public TextView materialConsumer,materialConsumerUnit;
        public TextView materialReturnable;
        public Button viewbtn;

        public Button stockbtn;

        public MyViewHolder(View convertView) {
            materialCode = (TextView)convertView. findViewById(R.id.materialCode);
            material_Unit = (TextView) convertView.findViewById(R.id.material_Unit);
            materialTitle = (TextView) convertView.findViewById(R.id.materialTitle);
            materialRetailer = (TextView) convertView.findViewById(R.id.tv_Retailer);
            materialRetailerUnit = (TextView) convertView.findViewById(R.id.materialMRPUnit);
            materialMOQ = (TextView) convertView.findViewById(R.id.tv_moq);
            materialMOQUnit = (TextView) convertView.findViewById(R.id.materialMOQUnit);
            materialConsumer = (TextView) convertView.findViewById(R.id.tv_Consumer);
            materialConsumerUnit= (TextView) convertView.findViewById(R.id.materialSPUnit);
            materialReturnable= (TextView) convertView.findViewById(R.id.material_Returnable);
            materialImage = (ImageView) convertView.findViewById(R.id.materialImage);
            downarrowImage=(ImageView) convertView.findViewById(R.id.img);
            viewbtn=(Button) convertView.findViewById(R.id.btnView);
            materialAgent=(TextView)convertView.findViewById(R.id.materialAgent);
            materialAgentUnit=(TextView)convertView.findViewById(R.id.agentUnit);
            stockbtn=(Button) convertView.findViewById(R.id.btnStock);
        }
    }
}