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
import android.widget.ListView;
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
    private ArrayList<ProductsBean> allProductsList, filteredProductsList, selectedProductsList;
    private ListView products_list_view;
    private MMSharedPreferences mPreferences;
    private Drawable red_circle, green_circle;

    private double availableStock = 5;
    private final String zero_cost = "0000.000";

    public TDCSalesAdapter(Context ctxt, SalesActivity salesActivity, ArrayList<ProductsBean> productsList, ListView products_list_view) {
        this.ctxt = ctxt;
        this.activity = salesActivity;
        this.mImageLoader = new ImageLoader(salesActivity);
        this.mInflater = LayoutInflater.from(activity);
        this.mPreferences = new MMSharedPreferences(activity);
        this.allProductsList = productsList;
        this.filteredProductsList = new ArrayList<>();
        this.filteredProductsList.addAll(allProductsList);
        this.selectedProductsList = new ArrayList<>();
        this.products_list_view = products_list_view;
        this.red_circle = ContextCompat.getDrawable(ctxt, R.drawable.ic_circle_red);
        this.green_circle = ContextCompat.getDrawable(ctxt, R.drawable.ic_circle_green);
    }

    private class TDCSalesViewHolder {
        ImageView arrow_icon, product_quantity_decrement, product_quantity_increment;
        TextView product_name, quantity_stock, price, tax, amount;
        EditText product_quantity;
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
            tdcSalesViewHolder.product_quantity_decrement = (ImageView) convertView.findViewById(R.id.product_quantity_dec);
            tdcSalesViewHolder.product_quantity = (EditText) convertView.findViewById(R.id.product_quantity);
            tdcSalesViewHolder.product_quantity_increment = (ImageView) convertView.findViewById(R.id.product_quantity_inc);

            convertView.setTag(tdcSalesViewHolder);
        } else {
            tdcSalesViewHolder = (TDCSalesViewHolder) convertView.getTag();
        }

        final TDCSalesViewHolder currentTDCSalesViewHolder = tdcSalesViewHolder;

        final ProductsBean currentProductsBean = getItem(position);
        currentProductsBean.setProductStock(availableStock);

        if (currentProductsBean.getProductStock() > 0) {
            tdcSalesViewHolder.arrow_icon.setImageResource(R.drawable.ic_arrow_upward_white_24dp);
            tdcSalesViewHolder.arrow_icon.setBackground(green_circle);
        } else {
            tdcSalesViewHolder.arrow_icon.setImageResource(R.drawable.ic_arrow_downward_white_24dp);
            tdcSalesViewHolder.arrow_icon.setBackground(red_circle);
        }

        final double productRate = Double.parseDouble(currentProductsBean.getProductConsumerPrice().replace(",", ""));
        double taxAmount = 0, amount = 0;
        float productTax = 0.0f;

        if (currentProductsBean.getProductvat() != null)
            productTax = Float.parseFloat(currentProductsBean.getProductvat());
        else if (currentProductsBean.getProductgst() != null)
            productTax = Float.parseFloat(currentProductsBean.getProductgst());

        final float finalProductTax = productTax;

        if (finalProductTax > 0)
            tdcSalesViewHolder.product_name.setText(String.format("%s (%s%%)", currentProductsBean.getProductTitle(), finalProductTax));
        else
            tdcSalesViewHolder.product_name.setText(String.format("%s", currentProductsBean.getProductTitle()));

        tdcSalesViewHolder.quantity_stock.setText(String.format("%.3f", availableStock));
        tdcSalesViewHolder.price.setText(Utility.getFormattedCurrency(productRate));
        tdcSalesViewHolder.tax.setText(Utility.getFormattedCurrency(taxAmount));
        tdcSalesViewHolder.amount.setText(Utility.getFormattedCurrency(amount));

        tdcSalesViewHolder.product_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new NetworkConnectionDetector(activity).isNetworkConnected()) {
                    String productImageURL = currentProductsBean.getProductImageUrl();

                    if (productImageURL != null && productImageURL.length() > 0) {
                        String URL = Constants.MAIN_URL + "/b2b/" + productImageURL;
                        showProductImageFull(URL);
                    } else {
                        Toast.makeText(ctxt, "Product image not available..!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tdcSalesViewHolder.product_quantity_decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Double presentQuantity = Double.parseDouble(currentTDCSalesViewHolder.product_quantity.getText().toString());

                    if (presentQuantity > 0) {
                        presentQuantity--;

                        double amount = productRate * presentQuantity;
                        double taxAmount = (amount * finalProductTax) / 100;

                        currentProductsBean.setSelectedQuantity(presentQuantity);
                        currentProductsBean.setProductAmount(amount);
                        currentProductsBean.setTaxAmount(taxAmount);

                        currentTDCSalesViewHolder.tax.setText(Utility.getFormattedCurrency(currentProductsBean.getTaxAmount()));
                        currentTDCSalesViewHolder.amount.setText(Utility.getFormattedCurrency(currentProductsBean.getProductAmount()));

                        if (presentQuantity == 0) {
                            currentTDCSalesViewHolder.product_quantity.setText(zero_cost);
                            removeProductFromSelectedProductsList(currentProductsBean);
                        } else {
                            currentTDCSalesViewHolder.product_quantity.setText(String.format("%.3f", presentQuantity));
                            updateSelectedProductsList(currentProductsBean, presentQuantity);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tdcSalesViewHolder.product_quantity_increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Double presentQuantity = Double.parseDouble(currentTDCSalesViewHolder.product_quantity.getText().toString());

                    if (presentQuantity < currentProductsBean.getProductStock()) {
                        presentQuantity++;

                        double amount = productRate * presentQuantity;
                        double taxAmount = (amount * finalProductTax) / 100;

                        currentProductsBean.setSelectedQuantity(presentQuantity);
                        currentProductsBean.setProductAmount(amount);
                        currentProductsBean.setTaxAmount(taxAmount);

                        currentTDCSalesViewHolder.product_quantity.setText(String.format("%.3f", presentQuantity));
                        currentTDCSalesViewHolder.tax.setText(Utility.getFormattedCurrency(currentProductsBean.getTaxAmount()));
                        currentTDCSalesViewHolder.amount.setText(Utility.getFormattedCurrency(currentProductsBean.getProductAmount()));

                        updateSelectedProductsList(currentProductsBean, presentQuantity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tdcSalesViewHolder.product_quantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                try {
                    if (!hasFocus) {
                        EditText quantityEditText = (EditText) view;
                        Double enteredQuantity = Double.parseDouble(quantityEditText.getText().toString());

                        if (enteredQuantity > currentProductsBean.getProductStock()) {
                            quantityEditText.setText(zero_cost);

                            new AlertDialog.Builder(activity)
                                    .setTitle("Alert..!")
                                    .setMessage("Quantity should not be greater than available stock.")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else {
                            if (enteredQuantity > 0) {
                                double amount = productRate * enteredQuantity;
                                double taxAmount = (amount * finalProductTax) / 100;

                                currentProductsBean.setSelectedQuantity(enteredQuantity);
                                currentProductsBean.setProductAmount(amount);
                                currentProductsBean.setTaxAmount(taxAmount);

                                currentTDCSalesViewHolder.tax.setText(Utility.getFormattedCurrency(currentProductsBean.getTaxAmount()));
                                currentTDCSalesViewHolder.amount.setText(Utility.getFormattedCurrency(currentProductsBean.getProductAmount()));

                                updateSelectedProductsList(currentProductsBean, enteredQuantity);
                            } else if (enteredQuantity == 0) {
                                if (selectedProductsList.contains(currentProductsBean))
                                    removeProductFromSelectedProductsList(currentProductsBean);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return convertView;
    }

    public void updateSelectedProductsList(ProductsBean productsBean, Double quantity) {
        try {
            if (!selectedProductsList.contains(productsBean)) {
                selectedProductsList.add(productsBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeProductFromSelectedProductsList(ProductsBean productsBean) {
        try {
            if (selectedProductsList.contains(productsBean))
                selectedProductsList.remove(productsBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
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