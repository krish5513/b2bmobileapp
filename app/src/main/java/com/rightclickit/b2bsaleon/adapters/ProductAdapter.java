package com.rightclickit.b2bsaleon.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.Products_Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sree on 25-Jan-17.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProcuctViewHolder> {
    public static int from = 0;
    private List<ProductInfo> productInfos,filterList;
    private Products_Activity products_activity;
   // private TripSheet_Delivery_Print tripSheet_delivery_print;
    Context context;

    public ProductAdapter(List<ProductInfo> productInfos, Products_Activity products_activity) {
        from = 1;
        this.productInfos = productInfos;
        this.filterList =  new ArrayList<ProductInfo>();
        this.filterList.addAll(this.productInfos);
        this.products_activity = products_activity;
    }


    @Override
    public int getItemCount() {
        return (null != filterList ? filterList.size() : 0);
    }
    public void filter(final String text) {

        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                filterList.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {
                    filterList.addAll(productInfos);

                }
                else {
                    // Iterate in the original List and add it to filter list...
                    Log.e("productInfos size", String.valueOf(productInfos.size()));
                    for (ProductInfo item : productInfos) {
                        Log.e("p text",item.materialDisc);
                        if (item.materialDisc.contains(text.toLowerCase())||item.materialMRP.contains(text.toLowerCase())
                                ||item.materialSP.contains(text.toLowerCase())||item.materialCode.contains(text.toLowerCase())
                                ||item.materialMOQ.contains(text.toLowerCase())) {
                            filterList.add(item);
                        }
                    }
                }

                // Set on UI Thread
               /* products_activity.getApplicationContext().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...
                        notifyDataSetChanged();
                    }
                });*/

            }
        }).start();

    }

    @Override
    public void onBindViewHolder(ProcuctViewHolder productViewHolder, final int i) {
        Context context = productViewHolder.materialImage.getContext();
        final ProductInfo pi = filterList.get(i);
        productViewHolder.materialCode.setText(pi.materialCode);
        productViewHolder.material_Unit.setText("0 "+pi.materialUnit);
//        productViewHolder.materialImage.setImageBitmap(MainActivity.StringToBitMap(pi.materialImage));
        productViewHolder.materialDisc.setText(pi.materialDisc);
        productViewHolder.materialMRP.setText(" "+pi.materialMRP+" Rs");
        productViewHolder.materialMOQ.setText(pi.materialMOQ);
        productViewHolder.materialSP.setText(pi.materialSP+ " Rs");
        Log.e("metrail return",pi.materialReturnable);
        if(pi.materialReturnable.equals("1"))
        productViewHolder.materialReturnS.setText("RETURNABLE");
        else
            productViewHolder.materialReturnS.setText("NON RETURNABLE");
        if(pi.materialImage.equals("NoImage")){

        }
        else{

            Glide.with(context).load(pi.materialImage).into(productViewHolder.materialImage);
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//            Bitmap bitmap = BitmapFactory.decodeFile(pi.materialImage, options);
//            productViewHolder.materialImage.setImageBitmap(bitmap);
        }
        productViewHolder.viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products_activity.gotoView(pi);
            }
        });
      /*  productViewHolder.editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products_activity.gotoEdit(pi);
            }
        });
        productViewHolder.stockbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products_activity.gotostock(pi.materialDisc);
            }
        });*/


    }


    @Override
    public ProcuctViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.product_card_recylce_view, viewGroup, false);
        context=viewGroup.getContext();
        return new ProcuctViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    } public Button delbtn;

    public static class ProcuctViewHolder extends RecyclerView.ViewHolder {
        public TextView materialCode;
        public ImageView materialImage;
        public TextView material_Unit;
        public TextView materialDisc;
        public TextView materialMRP;
        public TextView materialMOQ;
        public TextView materialSP;
        public TextView materialReturnS;
        public Button viewbtn;
        public Button editbtn;
        public Button stockbtn;


        public ProcuctViewHolder(View v) {
            super(v);
            materialCode = (TextView) v.findViewById(R.id.materialCode);
            material_Unit = (TextView) v.findViewById(R.id.material_Unit);
            materialDisc = (TextView) v.findViewById(R.id.materialDisc);
            materialMRP = (TextView) v.findViewById(R.id.materialMRP);
            materialMOQ = (TextView) v.findViewById(R.id.materialMOQ);
            materialSP = (TextView) v.findViewById(R.id.materialSP);
            materialReturnS = (TextView) v.findViewById(R.id.materialReturnable);
            materialImage = (ImageView) v.findViewById(R.id.materialImage);
            viewbtn=(Button) v.findViewById(R.id.btnView);
            editbtn=(Button) v.findViewById(R.id.btnEdit);
            stockbtn=(Button) v.findViewById(R.id.btnStock);

        }
    }

    public static final class ProductInfo {
        public String id;
        public String materialCode;
        public String materialDisc;
        public String materialImage;
        public String materialUnit;
        public String materialMRP;
        public String materialSP;
        public String materialMOQ;
        public String materialReturnable;
        public String materialTAX;
        public String materialTAXType;
        public String materialValidFrom;
        public String materialValidTo;
        public Button materialView;
        public Button materialStock;
    }

}
