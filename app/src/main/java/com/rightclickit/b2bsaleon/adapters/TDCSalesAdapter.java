package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.SalesActivity;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Venkat on 06/20/2017.
 */

public class TDCSalesAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    private Activity activity;
    private Context ctxt;
    private ImageLoader mImageLoader;
    private ArrayList<ProductsBean> allProductsList, filteredProductsList;
    private MMSharedPreferences mPreferences;
    private Drawable red_circle, green_circle;

    public TDCSalesAdapter(Context ctxt, SalesActivity salesActivity, ArrayList<ProductsBean> productsList) {
        this.ctxt = ctxt;
        this.activity = salesActivity;
        this.mImageLoader = new ImageLoader(salesActivity);
        this.mInflater = LayoutInflater.from(activity);
        this.mPreferences = new MMSharedPreferences(activity);
        this.allProductsList = productsList;
        this.filteredProductsList = new ArrayList<>();
        this.filteredProductsList.addAll(allProductsList);
        this.red_circle = ContextCompat.getDrawable(ctxt, R.drawable.ic_circle_red);
        this.green_circle = ContextCompat.getDrawable(ctxt, R.drawable.ic_circle_green);
    }

    @Override
    public int getCount() {
        return filteredProductsList.size();
    }

    @Override
    public ProductsBean getItem(int position) {
        return filteredProductsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TDCSalesViewHolder tdcSalesViewHolder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tdc_sales_adapter, null);

            tdcSalesViewHolder = new TDCSalesViewHolder();
            tdcSalesViewHolder.arrow_icon = (ImageView) convertView.findViewById(R.id.arrow_icon);
            tdcSalesViewHolder.product_name = (TextView) convertView.findViewById(R.id.product_name);
            tdcSalesViewHolder.quantity_stock = (TextView) convertView.findViewById(R.id.quantity_stock);
            tdcSalesViewHolder.price = (TextView) convertView.findViewById(R.id.price);
            tdcSalesViewHolder.tax = (TextView) convertView.findViewById(R.id.tax);
            tdcSalesViewHolder.amount = (TextView) convertView.findViewById(R.id.amount);
            tdcSalesViewHolder.product_quantity_dec = (ImageView) convertView.findViewById(R.id.product_quantity_dec);
            tdcSalesViewHolder.product_quantity = (EditText) convertView.findViewById(R.id.product_quantity);
            tdcSalesViewHolder.product_quantity_inc = (ImageView) convertView.findViewById(R.id.product_quantity_inc);

            convertView.setTag(tdcSalesViewHolder);
        } else {
            tdcSalesViewHolder = (TDCSalesViewHolder) convertView.getTag();
        }

        final ProductsBean productsBean = getItem(position);

        float availableStock = 50.0f;

        if (availableStock > 0) {
            tdcSalesViewHolder.arrow_icon.setImageResource(R.drawable.ic_arrow_upward_white_24dp);
            tdcSalesViewHolder.arrow_icon.setBackground(green_circle);
        } else {
            tdcSalesViewHolder.arrow_icon.setImageResource(R.drawable.ic_arrow_downward_white_24dp);
            tdcSalesViewHolder.arrow_icon.setBackground(red_circle);
        }

        double price = Double.parseDouble(productsBean.getProductConsumerPrice().replace(",", ""));

        float tax = 0.0f;
        if (productsBean.getProductvat() != null)
            tax = Float.parseFloat(productsBean.getProductvat());
        else if (productsBean.getProductgst() != null)
            tax = Float.parseFloat(productsBean.getProductgst());

        double taxAmount = (price * tax) / 100;
        double amount = price + taxAmount;

        tdcSalesViewHolder.product_name.setText(String.format("%s @ %s%%", productsBean.getProductTitle(), tax));
        tdcSalesViewHolder.quantity_stock.setText(String.format("%s", availableStock));
        tdcSalesViewHolder.price.setText(Utility.getFormattedCurrency(price));
        tdcSalesViewHolder.tax.setText(Utility.getFormattedCurrency(taxAmount));
        tdcSalesViewHolder.amount.setText(Utility.getFormattedCurrency(amount));

        tdcSalesViewHolder.product_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new NetworkConnectionDetector(activity).isNetworkConnected()) {
                    String productImageURL = productsBean.getProductImageUrl();
                    if (productImageURL != null && productImageURL.length() > 0) {
                        String URL = Constants.MAIN_URL + "/b2b/" + productImageURL;
                        showProductImageFull(URL);
                    } else {
                        Toast.makeText(ctxt, "Product image not available..!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return convertView;
    }

    private class TDCSalesViewHolder {
        ImageView arrow_icon, product_quantity_dec, product_quantity_inc;
        TextView product_name, quantity_stock, price, tax, amount;
        EditText product_quantity;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        filteredProductsList.clear();

        if (charText.length() == 0) {
            filteredProductsList.addAll(allProductsList);
        } else {
            for (ProductsBean wp : allProductsList) {
                if (wp.getProductTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredProductsList.add(wp);
                }

                if (wp.getProductCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredProductsList.add(wp);
                }
            }

            System.out.println("========== filteredProductsList = " + filteredProductsList);
        }

        notifyDataSetChanged();
    }

    // Methos to display product image as full image
    private void showProductImageFull(String url) {
        final AlertDialog.Builder alertadd = new AlertDialog.Builder(activity);
        LayoutInflater factory = LayoutInflater.from(activity);
        final View view = factory.inflate(R.layout.image_full_screen_layout, null);
        alertadd.setView(view);

        ImageView iv = (ImageView) view.findViewById(R.id.dialog_imageview);
        mImageLoader.DisplayImage(url, iv, null, "");

        alertadd.setNeutralButton("Okay!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                dlg.dismiss();
            }
        });

        alertadd.show();
    }
}