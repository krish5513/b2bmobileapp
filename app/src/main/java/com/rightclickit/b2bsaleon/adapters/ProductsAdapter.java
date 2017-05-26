package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.beanclass.NotificationItem;
import com.rightclickit.b2bsaleon.beanclass.ProductsObj;

import java.util.List;

/**
 * Created by PPS on 5/25/2017.
 */

public class ProductsAdapter extends ArrayAdapter<ProductsObj> {

        Context context;

public ProductsAdapter(Context context, int resourceId,
        List<ProductsObj> items) {
        super(context, resourceId, items);
        this.context = context;
        }

/*private view holder class*/
private class ViewHolder {
    public TextView materialCode;
    public ImageView materialImage;
    public ImageView downarrowImage;
    public TextView material_Unit;
    public TextView materialDisc;
    public TextView materialMRP,materialMRPUnit;
    public TextView materialMOQ,materialMOQUnit;
    public TextView materialSP,materialSPUnit;
    public TextView materialReturnS;
    public Button viewbtn;
    public Button editbtn;
    public Button stockbtn;
}

    public View getView(int position, View convertView, ViewGroup parent) {
        ProductsAdapter.ViewHolder holder = null;
        ProductsObj rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.products_adapter, null);
            holder = new ViewHolder();
            holder.materialCode = (TextView)convertView. findViewById(R.id.materialCode);
            holder.material_Unit = (TextView) convertView.findViewById(R.id.material_Unit);
            holder.materialDisc = (TextView) convertView.findViewById(R.id.materialDisc);
            holder.materialMRP = (TextView) convertView.findViewById(R.id.materialMRP);
            holder.materialMRPUnit = (TextView) convertView.findViewById(R.id.materialMRPUnit);
            holder.materialMOQ = (TextView) convertView.findViewById(R.id.materialMOQ);
            holder.materialMOQUnit = (TextView) convertView.findViewById(R.id.materialMOQUnit);
            holder.materialSP = (TextView) convertView.findViewById(R.id.materialSP);
            holder.materialSPUnit = (TextView) convertView.findViewById(R.id.materialSPUnit);
            holder.materialReturnS = (TextView) convertView.findViewById(R.id.materialReturnable);
            holder.materialImage = (ImageView) convertView.findViewById(R.id.materialImage);
            holder.downarrowImage=(ImageView) convertView.findViewById(R.id.img);
            holder.viewbtn=(Button) convertView.findViewById(R.id.btnView);
            holder.editbtn=(Button) convertView.findViewById(R.id.btnEdit);
            holder.stockbtn=(Button) convertView.findViewById(R.id.btnStock);
            convertView.setTag(holder);
        } else
            holder = (ProductsAdapter.ViewHolder) convertView.getTag();

        holder.materialMOQUnit.setText(rowItem.getMaterialLiters());
        holder.material_Unit.setText(rowItem.getMaterialidLiteres());
        holder.materialDisc.setText(rowItem.getMaterialName());
        holder.materialMRP.setText(rowItem.getMaterialMRP());
        holder.materialMRPUnit.setText(rowItem.getMaterialSPRS());
        holder.materialMOQ.setText(rowItem.getMaterialMOQ());
        holder.materialSP.setText(rowItem.getMaterialSP());
        holder.materialSPUnit.setText(rowItem.getMaterialSPRS());
        holder.materialReturnS.setText(rowItem.getMaterialReturnable());
        holder.materialImage.setImageResource((rowItem.getMaterialImage()));
        holder.downarrowImage.setImageResource(rowItem.getDownarrowImage());

        return convertView;
    }
}