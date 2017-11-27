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
    private Map<String, String> deliveredProductsHashMap;
    private boolean isReturnsInEditingMode = false;
    private final String zero_cost = "0.000";
    DeliverysBean currentDeliveryBean;

    public TripSheetReturnsAdapter(Context ctxt, TripsheetReturns returnsActivity, TripSheetReturnsListener tripSheetReturnsListener, ArrayList<DeliverysBean> mdeliveriesBeanList, Map<String, String> previouslyProducts, Map<String, String> productsDeliveryDetails) {
        this.ctxt = ctxt;
        this.activity = returnsActivity;
        this.listener = tripSheetReturnsListener;
        this.mInflater = LayoutInflater.from(activity);
        this.allProductsList = mdeliveriesBeanList;
        this.filteredProductsList = new ArrayList<>();
        this.filteredProductsList.addAll(allProductsList);
        this.selectedProductsHashMap = new HashMap<>();
        this.previouslyReturnedProductsHashMap = previouslyProducts;
        this.deliveredProductsHashMap = productsDeliveryDetails;

        if (!previouslyReturnedProductsHashMap.isEmpty()) {
            isReturnsInEditingMode = true;
        }
    }

    public class TripSheetReturnsViewHolder {
        TextView productName;
        Spinner returnType;
        EditText product_quantity, deliveredQuantity, obQuantity;
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

    public Map<String, DeliverysBean> getData() {
        return selectedProductsHashMap;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final TripSheetReturnsViewHolder tripSheetReturnsViewHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.tripsheetreturns_custom, null);

            tripSheetReturnsViewHolder = new TripSheetReturnsViewHolder();
            tripSheetReturnsViewHolder.productName = (TextView) view.findViewById(R.id.productName);
            tripSheetReturnsViewHolder.deliveredQuantity = (EditText) view.findViewById(R.id.deliveredQuantity);
            tripSheetReturnsViewHolder.obQuantity = (EditText) view.findViewById(R.id.OBQuantity);
            tripSheetReturnsViewHolder.returnType = (Spinner) view.findViewById(R.id.returnTypeSpinner);
            tripSheetReturnsViewHolder.product_quantity = (EditText) view.findViewById(R.id.productQt);
            tripSheetReturnsViewHolder.product_quantity_decrement = (ImageView) view.findViewById(R.id.productQtDec);
            tripSheetReturnsViewHolder.product_quantity_increment = (ImageView) view.findViewById(R.id.productQtInc);

            view.setTag(tripSheetReturnsViewHolder);
        } else {
            tripSheetReturnsViewHolder = (TripSheetReturnsViewHolder) view.getTag();
        }

        tripSheetReturnsViewHolder.deliveredQuantity.setEnabled(false);
        tripSheetReturnsViewHolder.deliveredQuantity.setFocusable(false);
        tripSheetReturnsViewHolder.deliveredQuantity.setClickable(false);

        tripSheetReturnsViewHolder.obQuantity.setEnabled(false);
        tripSheetReturnsViewHolder.obQuantity.setFocusable(false);
        tripSheetReturnsViewHolder.obQuantity.setClickable(false);

        final TripSheetReturnsViewHolder currentTripSheetReturnsViewHolder = tripSheetReturnsViewHolder;
     /* if(filteredProductsList.get(position).getProductReturnableUnit().equals("Y"))
       {*/
        currentDeliveryBean = getItem(position);

        tripSheetReturnsViewHolder.productName.setText(String.format("%s", currentDeliveryBean.getProductTitle()));

        if (deliveredProductsHashMap.containsKey(currentDeliveryBean.getProductId())) {
            tripSheetReturnsViewHolder.deliveredQuantity.setText(deliveredProductsHashMap.get(currentDeliveryBean.getProductId()));
            currentDeliveryBean.setDeliveredQuantity(Double.parseDouble(deliveredProductsHashMap.get(currentDeliveryBean.getProductId())));
        } else {
            tripSheetReturnsViewHolder.deliveredQuantity.setText(zero_cost);
            currentDeliveryBean.setDeliveredQuantity(0);
        }

        if (currentDeliveryBean.getProductCode().equals("2600005")) {
            tripSheetReturnsViewHolder.obQuantity.setText(String.valueOf(currentDeliveryBean.getCratesDueQuantity()));
        } else if (currentDeliveryBean.getProductCode().equals("2600006")) {
            tripSheetReturnsViewHolder.obQuantity.setText(String.valueOf(currentDeliveryBean.getCansDueQuantity()));
        } else {
            tripSheetReturnsViewHolder.obQuantity.setText("0.0");
        }

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
                            updateSelectedReturnProductsList(currentDeliveryBean);
                        } else {
                            currentTripSheetReturnsViewHolder.product_quantity.setText(String.format("%.3f", presentQuantity));
                            updateSelectedReturnProductsList(currentDeliveryBean);
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
                    Double dueQuan = Double.parseDouble(currentTripSheetReturnsViewHolder.obQuantity.getText().toString().trim());
                    Double deliQuaQuan = Double.parseDouble(currentTripSheetReturnsViewHolder.deliveredQuantity.getText().toString().trim());
                    Double finalQua = dueQuan + deliQuaQuan;

                    if (presentQuantity < finalQua) {
                        presentQuantity++;

                        currentDeliveryBean.setSelectedQuantity(presentQuantity);
                        if (presentQuantity == 0) {
                            currentTripSheetReturnsViewHolder.product_quantity.setText(zero_cost);
                            updateSelectedReturnProductsList(currentDeliveryBean);
                        } else {
                            currentTripSheetReturnsViewHolder.product_quantity.setText(String.format("%.3f", presentQuantity));
                            updateSelectedReturnProductsList(currentDeliveryBean);
                        }
                    } else {

                    }

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

                        if (quantityEditText.getText().length() > 0) {
                            Double enteredQuantity = Double.parseDouble(quantityEditText.getText().toString());
                            Double dueQuan = Double.parseDouble(currentTripSheetReturnsViewHolder.obQuantity.getText().toString().trim());
                            Double deliQuaQuan = Double.parseDouble(currentTripSheetReturnsViewHolder.deliveredQuantity.getText().toString().trim());
                            Double finalQua = dueQuan + deliQuaQuan;

                            if (enteredQuantity > finalQua) {
                                quantityEditText.setText(zero_cost);
                                currentDeliveryBean.setSelectedQuantity(0);

                                updateSelectedReturnProductsList(currentDeliveryBean);

                                new AlertDialog.Builder(activity)
                                        .setTitle("Alert..!")
                                        .setMessage("Quantity should not be greater than sum of ob quantity and delivered quantity.")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();

                            } else {
                                currentDeliveryBean.setSelectedQuantity(enteredQuantity);
                                updateSelectedReturnProductsList(currentDeliveryBean);
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    public void updateSelectedReturnProductsList(DeliverysBean deliverysBean) {
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
