package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.TripsheetReturns;
import com.rightclickit.b2bsaleon.beanclass.DeliverysBean;
import com.rightclickit.b2bsaleon.interfaces.TripSheetReturnsListener;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by PPS on 7/17/2017.
 * <p>
 * Modified by Venkat
 */

public class TripSheetReturnsAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context ctxt;
    private Activity activity;
    private TripSheetReturnsListener listener;
    private ArrayList<DeliverysBean> allProductsList, filteredProductsList;
    private Map<String, DeliverysBean> selectedProductsHashMap; // Hash Map Key = Product Id
    private Map<String, String> previouslyReturnedProductsHashMap;
    private boolean isReturnsInEditingMode = false;
    private final String zero_cost = "0.000";

    public TripSheetReturnsAdapter(Context ctxt, TripsheetReturns returnsActivity, TripSheetReturnsListener tripSheetReturnsListener, ArrayList<DeliverysBean> mdeliveriesBeanList, Map<String, String> previouslyProducts) {
        this.ctxt = ctxt;
        this.activity = returnsActivity;
        this.listener = tripSheetReturnsListener;
        this.mInflater = LayoutInflater.from(activity);
        this.allProductsList = mdeliveriesBeanList;
        this.filteredProductsList = new ArrayList<>();
        this.filteredProductsList.addAll(allProductsList);
        this.selectedProductsHashMap = new HashMap<>();
        this.previouslyReturnedProductsHashMap = previouslyProducts;

        if (!previouslyReturnedProductsHashMap.isEmpty()) {
            isReturnsInEditingMode = true;
        }
    }

    public class TripSheetReturnsViewHolder {
        TextView productName;
        Spinner returnType;
        EditText product_quantity;
        ImageView product_quantity_decrement, product_quantity_increment;
    }

    @Override
    public int getCount() {
        return filteredProductsList.size();
    }

    @Override
    public DeliverysBean getItem(int i) {
        return filteredProductsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final TripSheetReturnsViewHolder tripSheetReturnsViewHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.tripsheetreturns_custom, null);

            tripSheetReturnsViewHolder = new TripSheetReturnsViewHolder();
            tripSheetReturnsViewHolder.productName = (TextView) view.findViewById(R.id.productName);
            tripSheetReturnsViewHolder.returnType = (Spinner) view.findViewById(R.id.returnTypeSpinner);
            tripSheetReturnsViewHolder.product_quantity = (EditText) view.findViewById(R.id.productQt);
            tripSheetReturnsViewHolder.product_quantity_decrement = (ImageView) view.findViewById(R.id.productQtDec);
            tripSheetReturnsViewHolder.product_quantity_increment = (ImageView) view.findViewById(R.id.productQtInc);

            view.setTag(tripSheetReturnsViewHolder);
        } else {
            tripSheetReturnsViewHolder = (TripSheetReturnsViewHolder) view.getTag();
        }

        final TripSheetReturnsViewHolder currentTripSheetReturnsViewHolder = tripSheetReturnsViewHolder;

        final DeliverysBean currentDeliveryBean = getItem(position);

        tripSheetReturnsViewHolder.productName.setText(String.format("%s", currentDeliveryBean.getProductTitle()));

        if (previouslyReturnedProductsHashMap.containsKey(currentDeliveryBean.getProductId()))
            tripSheetReturnsViewHolder.product_quantity.setText(previouslyReturnedProductsHashMap.get(currentDeliveryBean.getProductId()));
        else
            tripSheetReturnsViewHolder.product_quantity.setText(zero_cost);

        tripSheetReturnsViewHolder.product_quantity_decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Double presentQuantity = Double.parseDouble(currentTripSheetReturnsViewHolder.product_quantity.getText().toString());

                    if (presentQuantity > 0) {
                        presentQuantity--;

                        currentDeliveryBean.setSelectedQuantity(presentQuantity);

                        if (presentQuantity == 0) {
                            currentTripSheetReturnsViewHolder.product_quantity.setText(zero_cost);
                            updateSelectedProductsList(currentDeliveryBean);
                        } else {
                            currentTripSheetReturnsViewHolder.product_quantity.setText(String.format("%.3f", presentQuantity));
                            updateSelectedProductsList(currentDeliveryBean);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tripSheetReturnsViewHolder.product_quantity_increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Double presentQuantity = Double.parseDouble(currentTripSheetReturnsViewHolder.product_quantity.getText().toString());
                    presentQuantity++;

                    currentDeliveryBean.setSelectedQuantity(presentQuantity);
                    currentTripSheetReturnsViewHolder.product_quantity.setText(String.format("%.3f", presentQuantity));
                    updateSelectedProductsList(currentDeliveryBean);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tripSheetReturnsViewHolder.product_quantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                try {
                    if (!hasFocus) {
                        EditText quantityEditText = (EditText) view;
                        Double enteredQuantity = Double.parseDouble(quantityEditText.getText().toString());

                        currentDeliveryBean.setSelectedQuantity(enteredQuantity);
                        updateSelectedProductsList(currentDeliveryBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    public void updateSelectedProductsList(DeliverysBean deliverysBean) {
        try {
            if (selectedProductsHashMap.containsKey(deliverysBean.getProductId()))
                selectedProductsHashMap.remove(deliverysBean.getProductId());

            selectedProductsHashMap.put(deliverysBean.getProductId(), deliverysBean);

            if (listener != null)
                listener.updateSelectedProductsList(selectedProductsHashMap);

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
            for (DeliverysBean deliverysBean : allProductsList) {
                if (deliverysBean.getProductTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredProductsList.add(deliverysBean);
                } else if (deliverysBean.getProductCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredProductsList.add(deliverysBean);
                }
            }
        }

        notifyDataSetChanged();
    }
}
