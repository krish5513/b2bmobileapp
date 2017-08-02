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
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.TripsheetDelivery;
import com.rightclickit.b2bsaleon.beanclass.DeliverysBean;
import com.rightclickit.b2bsaleon.interfaces.TripSheetDeliveriesListener;
import com.rightclickit.b2bsaleon.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by PPS on 7/17/2017.
 */

public class TripSheetDeliveriesAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context ctxt;
    private Activity activity;
    private TripSheetDeliveriesListener listener;
    private ArrayList<DeliverysBean> allDeliveryProductsList, filteredDeliveryProductsList;
    private Map<String, DeliverysBean> selectedDeliveryProductsHashMap; // Hash Map Key = Product Id
    private final String zero_cost = "0.000";

    public TripSheetDeliveriesAdapter(Context ctxt, TripsheetDelivery deliveryActivity, TripSheetDeliveriesListener deliveriesListener, ArrayList<DeliverysBean> mdeliveriesBeanList) {
        this.ctxt = ctxt;
        this.activity = deliveryActivity;
        this.mInflater = LayoutInflater.from(activity);
        this.listener = deliveriesListener;
        this.allDeliveryProductsList = mdeliveriesBeanList;
        this.filteredDeliveryProductsList = new ArrayList<>();
        this.filteredDeliveryProductsList.addAll(allDeliveryProductsList);
        this.selectedDeliveryProductsHashMap = new HashMap<>();
    }

    public class TripSheetDeliveriesViewHolder {
        TextView product_name, quantity_stock, price, tax, amount;
        EditText product_quantity;
        ImageView product_quantity_decrement, product_quantity_increment;
    }

    @Override
    public int getCount() {
        return filteredDeliveryProductsList.size();
    }

    @Override
    public DeliverysBean getItem(int i) {
        return filteredDeliveryProductsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final TripSheetDeliveriesViewHolder tripSheetDeliveriesViewHolder;
        if (view == null) {
            tripSheetDeliveriesViewHolder = new TripSheetDeliveriesViewHolder();
            view = mInflater.inflate(R.layout.tripsheetdeliveries_custom, null);

            tripSheetDeliveriesViewHolder.product_name = (TextView) view.findViewById(R.id.productName);
            tripSheetDeliveriesViewHolder.quantity_stock = (TextView) view.findViewById(R.id.quantity_in_stock);
            tripSheetDeliveriesViewHolder.price = (TextView) view.findViewById(R.id.productPrice);
            tripSheetDeliveriesViewHolder.tax = (TextView) view.findViewById(R.id.taxAmount);
            tripSheetDeliveriesViewHolder.amount = (TextView) view.findViewById(R.id.amount);
            tripSheetDeliveriesViewHolder.product_quantity = (EditText) view.findViewById(R.id.productQt);
            tripSheetDeliveriesViewHolder.product_quantity_decrement = (ImageView) view.findViewById(R.id.productQtDec);
            tripSheetDeliveriesViewHolder.product_quantity_increment = (ImageView) view.findViewById(R.id.productQtInc);

            view.setTag(tripSheetDeliveriesViewHolder);
        } else {
            tripSheetDeliveriesViewHolder = (TripSheetDeliveriesViewHolder) view.getTag();
        }

        final TripSheetDeliveriesViewHolder currentTripSheetDeliveriesViewHolder = tripSheetDeliveriesViewHolder;

        final DeliverysBean currentDeliveryBean = getItem(position);

        final double productRatePerUnit = Double.parseDouble(currentDeliveryBean.getProductAgentPrice().replace(",", ""));
        float productTax = 0.0f;

        if (currentDeliveryBean.getProductvat() != null)
            productTax = Float.parseFloat(currentDeliveryBean.getProductvat());
        else if (currentDeliveryBean.getProductgst() != null)
            productTax = Float.parseFloat(currentDeliveryBean.getProductgst());

        double orderQuantity = currentDeliveryBean.getProductOrderedQuantity();
        double taxAmountPerUnit = ((productRatePerUnit) * productTax) / 100;
        double taxAmount = taxAmountPerUnit * orderQuantity;
        double amount = productRatePerUnit * orderQuantity;

        final float finalProductTax = productTax;

        currentDeliveryBean.setProductRatePerUnit(productRatePerUnit);
        currentDeliveryBean.setProductTaxPerUnit(productTax);
        currentDeliveryBean.setProductAllAvailableStock(currentDeliveryBean.getProductOrderedQuantity() + currentDeliveryBean.getProductExtraQuantity());

        tripSheetDeliveriesViewHolder.product_name.setText(String.format("%s", currentDeliveryBean.getProductTitle()));
        tripSheetDeliveriesViewHolder.quantity_stock.setText(String.format("%.3f + %.3f", currentDeliveryBean.getProductStock(), currentDeliveryBean.getProductExtraQuantity()));
        tripSheetDeliveriesViewHolder.price.setText(Utility.getFormattedCurrency(Double.parseDouble(currentDeliveryBean.getProductAgentPrice())));
        tripSheetDeliveriesViewHolder.tax.setText(Utility.getFormattedCurrency(taxAmount));
        tripSheetDeliveriesViewHolder.amount.setText(Utility.getFormattedCurrency(amount));
        tripSheetDeliveriesViewHolder.product_quantity.setText(String.format("%.3f", currentDeliveryBean.getProductOrderedQuantity()));

        tripSheetDeliveriesViewHolder.product_quantity_decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Double presentQuantity = Double.parseDouble(currentTripSheetDeliveriesViewHolder.product_quantity.getText().toString());

                    if (presentQuantity > 0) {
                        presentQuantity--;

                        double amount = productRatePerUnit * presentQuantity;
                        double taxAmount = (amount * finalProductTax) / 100;

                        currentDeliveryBean.setSelectedQuantity(presentQuantity);
                        currentDeliveryBean.setProductAmount(amount);
                        currentDeliveryBean.setTaxAmount(taxAmount);

                        currentTripSheetDeliveriesViewHolder.tax.setText(Utility.getFormattedCurrency(currentDeliveryBean.getTaxAmount()));
                        currentTripSheetDeliveriesViewHolder.amount.setText(Utility.getFormattedCurrency(currentDeliveryBean.getProductAmount()));

                        if (presentQuantity == 0) {
                            currentTripSheetDeliveriesViewHolder.product_quantity.setText(zero_cost);
                            removeProductFromSelectedProductsList(currentDeliveryBean);
                        } else {
                            currentTripSheetDeliveriesViewHolder.product_quantity.setText(String.format("%.3f", presentQuantity));
                            updateSelectedProductsList(currentDeliveryBean);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tripSheetDeliveriesViewHolder.product_quantity_increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Double presentQuantity = Double.parseDouble(currentTripSheetDeliveriesViewHolder.product_quantity.getText().toString());

                    if (presentQuantity < currentDeliveryBean.getProductAllAvailableStock()) {
                        presentQuantity++;

                        double amount = productRatePerUnit * presentQuantity;
                        double taxAmount = (amount * finalProductTax) / 100;

                        currentDeliveryBean.setSelectedQuantity(presentQuantity);
                        currentDeliveryBean.setProductAmount(amount);
                        currentDeliveryBean.setTaxAmount(taxAmount);

                        currentTripSheetDeliveriesViewHolder.product_quantity.setText(String.format("%.3f", presentQuantity));
                        currentTripSheetDeliveriesViewHolder.tax.setText(Utility.getFormattedCurrency(currentDeliveryBean.getTaxAmount()));
                        currentTripSheetDeliveriesViewHolder.amount.setText(Utility.getFormattedCurrency(currentDeliveryBean.getProductAmount()));

                        updateSelectedProductsList(currentDeliveryBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tripSheetDeliveriesViewHolder.product_quantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                try {
                    if (!hasFocus) {
                        EditText quantityEditText = (EditText) view;
                        Double enteredQuantity = Double.parseDouble(quantityEditText.getText().toString());

                        if (enteredQuantity > currentDeliveryBean.getProductAllAvailableStock()) {
                            quantityEditText.setText(zero_cost);

                            removeProductFromSelectedProductsList(currentDeliveryBean);

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
                                double amount = productRatePerUnit * enteredQuantity;
                                double taxAmount = (amount * finalProductTax) / 100;

                                currentDeliveryBean.setSelectedQuantity(enteredQuantity);
                                currentDeliveryBean.setProductAmount(amount);
                                currentDeliveryBean.setTaxAmount(taxAmount);

                                currentTripSheetDeliveriesViewHolder.tax.setText(Utility.getFormattedCurrency(currentDeliveryBean.getTaxAmount()));
                                currentTripSheetDeliveriesViewHolder.amount.setText(Utility.getFormattedCurrency(currentDeliveryBean.getProductAmount()));

                                updateSelectedProductsList(currentDeliveryBean);
                            } else if (enteredQuantity == 0) {
                                removeProductFromSelectedProductsList(currentDeliveryBean);
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

    public void updateSelectedProductsList(DeliverysBean deliverysBean) {
        try {
            selectedDeliveryProductsHashMap.put(deliverysBean.getProductId(), deliverysBean);

            if (listener != null)
                listener.updateDeliveryProductsList(selectedDeliveryProductsHashMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeProductFromSelectedProductsList(DeliverysBean deliverysBean) {
        try {
            if (selectedDeliveryProductsHashMap.containsKey(deliverysBean.getProductId()))
                selectedDeliveryProductsHashMap.remove(deliverysBean.getProductId());

            if (listener != null)
                listener.updateDeliveryProductsList(selectedDeliveryProductsHashMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        filteredDeliveryProductsList.clear();

        if (charText.length() == 0) {
            filteredDeliveryProductsList.addAll(allDeliveryProductsList);
        } else {
            for (DeliverysBean deliverysBean : allDeliveryProductsList) {
                if (deliverysBean.getProductTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredDeliveryProductsList.add(deliverysBean);
                } else if (deliverysBean.getProductCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredDeliveryProductsList.add(deliverysBean);
                }
            }
        }

        notifyDataSetChanged();
    }
}
