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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.ProductInfoActivity;
import com.rightclickit.b2bsaleon.activities.Products_Activity;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by bhagya on 5/25/2017.
 */

public class ProductsAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    private Activity activity;
    Context ctxt;
    ArrayList<ProductsBean> mProductsBeansList1;
    private ImageLoader mImageLoader;
    private ArrayList<ProductsBean> arraylist;
    private MMSharedPreferences mPreferences;
    private DBHelper mDBHelper;
    private String mStock="",mAgentPrice="",mRetailerPrice="",mConsumerPrice="";


    public ProductsAdapter(Context ctxt, Products_Activity productsActivity, ArrayList<ProductsBean> mProductsBeansList1, String mStock, String mAgentPrice, String mRetailerPrice, String mConsumerPrice) {
        this.ctxt = ctxt;
        this.activity = productsActivity;
        this.mProductsBeansList1 = mProductsBeansList1;
        this.mImageLoader = new ImageLoader(productsActivity);
        this.mInflater = LayoutInflater.from(activity);
        this.arraylist = new ArrayList<ProductsBean>();
        this.arraylist.addAll(mProductsBeansList1);
        this.mPreferences = new MMSharedPreferences(activity);
        this.mDBHelper=new DBHelper(activity);
        this.mStock = mStock;
        this.mAgentPrice = mAgentPrice;
        this.mRetailerPrice = mRetailerPrice;
        this.mConsumerPrice = mConsumerPrice;
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

            holder.productCode = (TextView) convertView.findViewById(R.id.materialCode);
            holder.product_Unit = (TextView) convertView.findViewById(R.id.material_Unit);
            holder.productTitle = (TextView) convertView.findViewById(R.id.materialTitle);
            holder.materialRetailer = (TextView) convertView.findViewById(R.id.tv_Retailer);

            holder.materialRetailerUnit = (TextView) convertView.findViewById(R.id.materialMRPUnit);
            holder.materialRetailerUnit.setVisibility(View.GONE);

            holder.materialMOQ = (TextView) convertView.findViewById(R.id.tv_moq);
            holder.materialMOQUnit = (TextView) convertView.findViewById(R.id.materialMOQUnit);
            holder.materialConsumer = (TextView) convertView.findViewById(R.id.tv_Consumer);

            holder.materialConsumerUnit = (TextView) convertView.findViewById(R.id.materialSPUnit);
            holder.materialConsumerUnit.setVisibility(View.GONE);


            holder.productImage = (ImageView) convertView.findViewById(R.id.materialImage);
            holder.downarrowImage = (ImageView) convertView.findViewById(R.id.img);
            holder.viewbtn = (Button) convertView.findViewById(R.id.btnView);
            holder.materialAgent = (TextView) convertView.findViewById(R.id.materialAgent);

            holder.materialAgentUnit = (TextView) convertView.findViewById(R.id.agentUnit);
            holder.materialAgentUnit.setVisibility(View.GONE);

           // holder.stockbtn = (Button) convertView.findViewById(R.id.btnStock);
           // holder.stockbtn.setVisibility(View.GONE);

            holder.gst = (EditText) convertView.findViewById(R.id.GST);
            holder.vat = (EditText) convertView.findViewById(R.id.VAT);
            holder.materialUOM=(EditText)convertView.findViewById(R.id.UOM);
            holder.controlCode = (EditText) convertView.findViewById(R.id.controlCode);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

       /* if(mStock.equals("Stock")){
            holder.stockbtn.setVisibility(View.VISIBLE);
        }*/

        if(mAgentPrice.equals("Agent_Price")){
            holder.materialAgentUnit.setVisibility(View.VISIBLE);
        }

        if(mRetailerPrice.equals("Retailer_Price")){
            holder.materialRetailerUnit.setVisibility(View.VISIBLE);
        }

        if(mConsumerPrice.equals("Consumer_Price")){
            holder.materialConsumerUnit.setVisibility(View.VISIBLE);
        }

        holder.productCode.setText(mProductsBeansList1.get(position).getProductCode());
        if (mProductsBeansList1.get(position).getProductReturnable().equals("N")) {
            holder.product_Unit.setText("NO");
        } else if (mProductsBeansList1.get(position).getProductReturnable().equals("Y")) {
            holder.product_Unit.setText("YES");
        }
        holder.productTitle.setText(mProductsBeansList1.get(position).getProductTitle());
        holder.materialMOQUnit.setText(mProductsBeansList1.get(position).getProductMOQ());

        if (mProductsBeansList1.get(position).getProductAgentPrice() != null) {
            if (mProductsBeansList1.get(position).getProductAgentPrice().length() == 0) {
                holder.materialAgentUnit.setText("-");
            } else {
                holder.materialAgentUnit.setText(mProductsBeansList1.get(position).getProductAgentPrice());
            }
        } else {
            holder.materialAgentUnit.setText("-");
        }

        if (mProductsBeansList1.get(position).getProductRetailerPrice() != null) {
            if (mProductsBeansList1.get(position).getProductRetailerPrice().length() == 0) {
                holder.materialRetailerUnit.setText("-");
            } else {
                holder.materialRetailerUnit.setText(mProductsBeansList1.get(position).getProductRetailerPrice());
            }
        } else {
            holder.materialRetailerUnit.setText("-");
        }

        if (mProductsBeansList1.get(position).getProductConsumerPrice() != null) {
            if (mProductsBeansList1.get(position).getProductConsumerPrice().length() == 0) {
                holder.materialConsumerUnit.setText("-");
            } else {
                holder.materialConsumerUnit.setText(mProductsBeansList1.get(position).getProductConsumerPrice());
            }
        } else {
            holder.materialConsumerUnit.setText("-");
        }


        if (mProductsBeansList1.get(position).getProductgst() != null) {
            if (mProductsBeansList1.get(position).getProductgst().length() == 0) {
                holder.gst.setText("-");
            } else {
                holder.gst.setText(mProductsBeansList1.get(position).getProductgst());
            }
        } else {
            holder.gst.setText("-");
        }

        if (mProductsBeansList1.get(position).getProductvat() != null) {
            if (mProductsBeansList1.get(position).getProductvat().length() == 0) {
                holder.vat.setText("-");
            } else {
                holder.vat.setText(mProductsBeansList1.get(position).getProductvat());
            }
        } else {
            holder.vat.setText("-");
        }


        if (mProductsBeansList1.get(position).getProductUOM() != null) {
            if (mProductsBeansList1.get(position).getProductUOM().length() == 0) {
                holder.materialUOM.setText("-");
            } else {
                holder.materialUOM.setText(mProductsBeansList1.get(position).getProductUOM());
            }
        } else {
            holder.materialUOM.setText("-");
        }


        if (mProductsBeansList1.get(position).getControlCode() != null) {
            if (mProductsBeansList1.get(position).getControlCode().length() == 0) {
                holder.controlCode.setText("-");
            } else {
                holder.controlCode.setText(mProductsBeansList1.get(position).getControlCode());
            }
        } else {
            holder.controlCode.setText("-");
        }


        //System.out.println("URL===== "+mProductsBeansList1.get(position).getProductImageUrl());
        if (mProductsBeansList1.get(position).getProductImageUrl() != null) {
            if (!mProductsBeansList1.get(position).getProductImageUrl().equals("")) {
                String URL = Constants.MAIN_URL + "/b2b/" + mProductsBeansList1.get(position).getProductImageUrl();
                if (new NetworkConnectionDetector(activity).isNetworkConnected()) {
                    mImageLoader.DisplayImage(URL, holder.productImage, null, "");
                } else {
                    holder.productImage.setBackgroundResource(R.drawable.tlogo);
                }
            }
        } else {
            holder.productImage.setBackgroundResource(R.drawable.tlogo);
        }

       /* holder.stockbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ProductStock.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });
*/
        holder.viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ProductInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("CODE", mProductsBeansList1.get(position).getProductCode());
                bundle.putString("TITLE", mProductsBeansList1.get(position).getProductTitle());
                bundle.putString("RETURNABLE", mProductsBeansList1.get(position).getProductReturnable());
                bundle.putString("MOQ", mProductsBeansList1.get(position).getProductMOQ());
                bundle.putString("AGENT", mProductsBeansList1.get(position).getProductAgentPrice());
                bundle.putString("RETAILER", mProductsBeansList1.get(position).getProductRetailerPrice());
                bundle.putString("CONSUMER", mProductsBeansList1.get(position).getProductConsumerPrice());
                bundle.putString("GST", mProductsBeansList1.get(position).getProductgst());
                Log.i("gst",mProductsBeansList1.get(position).getProductgst()+"");
                bundle.putString("VAT", mProductsBeansList1.get(position).getProductvat());
                bundle.putString("UOM", mProductsBeansList1.get(position).getProductUOM());
                bundle.putString("controlCode", mProductsBeansList1.get(position).getControlCode());
                intent.putExtra("IMAGE", mProductsBeansList1.get(position).getProductImageUrl());
                holder.productImage.buildDrawingCache();
                Bitmap image = holder.productImage.getDrawingCache();
                Bundle extras = new Bundle();
                extras.putParcelable("imagebitmap", image);
                intent.putExtras(extras);

                intent.putExtras(bundle);

                activity.startActivity(intent);
                activity.finish();
            }
        });

        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  if (mProductsBeansList1.get(position).getProductImageUrl() != null) {
                     if (!mProductsBeansList1.get(position).getProductImageUrl().equals("")) {
                        String URL = Constants.MAIN_URL + "/b2b/" + mProductsBeansList1.get(position).getProductImageUrl();

                        if (new NetworkConnectionDetector(activity).isNetworkConnected()) {
                            mImageLoader.DisplayImage(URL, holder.productImage, null, "");
                            showProductImageFull(URL);
                        } else {

                            holder.productImage.setBackgroundResource(R.drawable.logo);
                            showProductImageFull(URL);

                        }

                    }

                  }
                   else {
                       Toast.makeText(ctxt, "Product image not available..!Selected is the default image", Toast.LENGTH_SHORT).show();
                   }
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
        public TextView materialAgent, materialAgentUnit;
        public TextView materialRetailer, materialRetailerUnit;
        public TextView materialMOQ, materialMOQUnit;
        public TextView materialConsumer, materialConsumerUnit;
        public TextView materialReturnable;
        public TextView materialUOM;
        public Button viewbtn;

        public Button stockbtn;
        public EditText gst;
        public EditText controlCode;
        public EditText vat;


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

    // Methos to display product image as full image
    private void showProductImageFull(String url){
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