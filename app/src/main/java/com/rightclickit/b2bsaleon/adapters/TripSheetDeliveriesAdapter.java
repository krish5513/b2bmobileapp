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
    private Map<String, String> previouslyDeliveredProductsHashMap;
    private Map<String, String> productOrderQuantitiesHashMap;
    private final String zero_cost = "0.000";
    private boolean isDeliveryInEditingMode = false;
    private Map<String, DeliverysBean> selectedDeliveryProductsHashMapTemp;

    public TripSheetDeliveriesAdapter(Context ctxt, TripsheetDelivery deliveryActivity, TripSheetDeliveriesListener deliveriesListener, ArrayList<DeliverysBean> mdeliveriesBeanList, Map<String, String> previouslyDeliveredProducts, Map<String, String> productOrderQuantities) {
        this.ctxt = ctxt;
        this.activity = deliveryActivity;
        this.mInflater = LayoutInflater.from(activity);
        this.listener = deliveriesListener;
        this.allDeliveryProductsList = mdeliveriesBeanList;
        this.filteredDeliveryProductsList = new ArrayList<>();
        this.filteredDeliveryProductsList.addAll(allDeliveryProductsList);
        this.selectedDeliveryProductsHashMap = new HashMap<>();
        this.previouslyDeliveredProductsHashMap = previouslyDeliveredProducts;
        this.productOrderQuantitiesHashMap = productOrderQuantities;
        this.selectedDeliveryProductsHashMapTemp = new HashMap<>();
        if (!previouslyDeliveredProductsHashMap.isEmpty()) {
            isDeliveryInEditingMode = true;
        }

        // in order to update total amount's at the time of initial loading.
        for (DeliverysBean deliverysBean : filteredDeliveryProductsList) {
            System.out.println("Agent Price::"+deliverysBean.getProductAgentPrice());
            final double productRatePerUnit;
            if(deliverysBean.getProductAgentPrice()!=null) {
                 productRatePerUnit = Double.parseDouble(deliverysBean.getProductAgentPrice().replace(",", ""));
            }else {
                productRatePerUnit = 0.0f;
            }
            float productTax = 0.0f;

            if (isDeliveryInEditingMode && previouslyDeliveredProductsHashMap.containsKey(deliverysBean.getProductId())) {
                deliverysBean.setProductOrderedQuantity(Double.parseDouble(previouslyDeliveredProductsHashMap.get(deliverysBean.getProductId())));
            } else {
                if (productOrderQuantitiesHashMap.containsKey(deliverysBean.getProductCode()))
                    deliverysBean.setProductOrderedQuantity(Double.parseDouble(productOrderQuantitiesHashMap.get(deliverysBean.getProductCode())));
                else
                    deliverysBean.setProductOrderedQuantity(0);
            }

            if (deliverysBean.getProductgst() != null)
                productTax = Float.parseFloat(deliverysBean.getProductgst());
            else if (deliverysBean.getProductvat() != null)
                productTax = Float.parseFloat(deliverysBean.getProductvat());

            double orderQuantity = deliverysBean.getProductOrderedQuantity();
            double taxAmountPerUnit = ((productRatePerUnit) * productTax) / 100;
            double taxAmount = taxAmountPerUnit * orderQuantity;
            double amount = productRatePerUnit * orderQuantity;

            deliverysBean.setSelectedQuantity(orderQuantity);
            deliverysBean.setProductRatePerUnit(productRatePerUnit);
            deliverysBean.setProductTaxPerUnit(productTax);
            deliverysBean.setProductAmount(amount);
            deliverysBean.setTaxAmount(taxAmount);

            if (isDeliveryInEditingMode)
                deliverysBean.setProductAvailableStockForSpecificAgent(orderQuantity + deliverysBean.getProductStock() + deliverysBean.getProductExtraQuantity());
            else {
                //deliverysBean.setProductAvailableStockForSpecificAgent(deliverysBean.getProductOrderedQuantity() + deliverysBean.getProductExtraQuantity());
                deliverysBean.setProductAvailableStockForSpecificAgent(deliverysBean.getProductStock() + deliverysBean.getProductExtraQuantity());
            }

            selectedDeliveryProductsHashMap.put(deliverysBean.getProductId(), deliverysBean);

           // if (!Utility.isDeliveryFirstTime) {
                if (listener != null)
                    listener.updateDeliveryProductsList(selectedDeliveryProductsHashMap);
           // }
        }
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
        DeliverysBean deliverysBean = filteredDeliveryProductsList.get(i);

        if (selectedDeliveryProductsHashMap.get(deliverysBean.getProductId()) != null)
            deliverysBean = selectedDeliveryProductsHashMap.get(deliverysBean.getProductId());

        return deliverysBean;
    }

    public Map<String, DeliverysBean> getData() {
        return selectedDeliveryProductsHashMap;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final TripSheetDeliveriesViewHolder tripSheetDeliveriesViewHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.tripsheetdeliveries_custom, null);

            tripSheetDeliveriesViewHolder = new TripSheetDeliveriesViewHolder();
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

        /*final double productRatePerUnit = Double.parseDouble(currentDeliveryBean.getProductAgentPrice().replace(",", ""));
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
        currentDeliveryBean.setProductAvailableStockForSpecificAgent(currentDeliveryBean.getProductOrderedQuantity() + currentDeliveryBean.getProductExtraQuantity());
        currentDeliveryBean.setProductAmount(amount);
        currentDeliveryBean.setTaxAmount(taxAmount);*/

        tripSheetDeliveriesViewHolder.product_name.setText(String.format("%s", currentDeliveryBean.getProductTitle()));
        tripSheetDeliveriesViewHolder.quantity_stock.setText(String.format("%.3f + %.3f", currentDeliveryBean.getProductStock(), currentDeliveryBean.getProductExtraQuantity()));
        if(currentDeliveryBean.getProductAgentPrice()!=null) {
            tripSheetDeliveriesViewHolder.price.setText(Utility.getFormattedCurrency(Double.parseDouble(currentDeliveryBean.getProductAgentPrice())));
        }else {
            tripSheetDeliveriesViewHolder.price.setText("RS.0.00");
        }
        tripSheetDeliveriesViewHolder.tax.setText(Utility.getFormattedCurrency(currentDeliveryBean.getTaxAmount()));
        tripSheetDeliveriesViewHolder.amount.setText(Utility.getFormattedCurrency(currentDeliveryBean.getProductAmount()));
        tripSheetDeliveriesViewHolder.product_quantity.setText(String.format("%.3f", currentDeliveryBean.getProductOrderedQuantity()));

        tripSheetDeliveriesViewHolder.product_quantity_decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Double presentQuantity = Double.parseDouble(currentTripSheetDeliveriesViewHolder.product_quantity.getText().toString());

                    if (presentQuantity > 0) {
                        presentQuantity--;

                        double amount = currentDeliveryBean.getProductRatePerUnit() * presentQuantity;
                        double taxAmount = (amount * currentDeliveryBean.getProductTaxPerUnit()) / 100;

                        currentDeliveryBean.setSelectedQuantity(presentQuantity);
                        currentDeliveryBean.setProductAmount(amount);
                        currentDeliveryBean.setTaxAmount(taxAmount);

                        currentTripSheetDeliveriesViewHolder.tax.setText(Utility.getFormattedCurrency(currentDeliveryBean.getTaxAmount()));
                        currentTripSheetDeliveriesViewHolder.amount.setText(Utility.getFormattedCurrency(currentDeliveryBean.getProductAmount()));

                        if (presentQuantity == 0) {
                            currentTripSheetDeliveriesViewHolder.product_quantity.setText(zero_cost);
                            updateSelectedProductsList(currentDeliveryBean);
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

                    if (presentQuantity < currentDeliveryBean.getProductAvailableStockForSpecificAgent()) {
                        presentQuantity++;

                        double amount = currentDeliveryBean.getProductRatePerUnit() * presentQuantity;
                        double taxAmount = (amount * currentDeliveryBean.getProductTaxPerUnit()) / 100;

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

                        if (enteredQuantity > currentDeliveryBean.getProductAvailableStockForSpecificAgent()) {
                            quantityEditText.setText(zero_cost);

                            currentDeliveryBean.setSelectedQuantity(0);
                            currentDeliveryBean.setProductAmount(0);
                            currentDeliveryBean.setTaxAmount(0);

                            currentTripSheetDeliveriesViewHolder.tax.setText(Utility.getFormattedCurrency(currentDeliveryBean.getTaxAmount()));
                            currentTripSheetDeliveriesViewHolder.amount.setText(Utility.getFormattedCurrency(currentDeliveryBean.getProductAmount()));

                            updateSelectedProductsList(currentDeliveryBean);

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
                                double amount = currentDeliveryBean.getProductRatePerUnit() * enteredQuantity;
                                double taxAmount = (amount * currentDeliveryBean.getProductTaxPerUnit()) / 100;

                                currentDeliveryBean.setSelectedQuantity(enteredQuantity);
                                currentDeliveryBean.setProductAmount(amount);
                                currentDeliveryBean.setTaxAmount(taxAmount);

                                currentTripSheetDeliveriesViewHolder.tax.setText(Utility.getFormattedCurrency(currentDeliveryBean.getTaxAmount()));
                                currentTripSheetDeliveriesViewHolder.amount.setText(Utility.getFormattedCurrency(currentDeliveryBean.getProductAmount()));

                                updateSelectedProductsList(currentDeliveryBean);
                            } else if (enteredQuantity == 0) {
                                currentDeliveryBean.setSelectedQuantity(0);
                                currentDeliveryBean.setProductAmount(0);
                                currentDeliveryBean.setTaxAmount(0);

                                currentTripSheetDeliveriesViewHolder.tax.setText(Utility.getFormattedCurrency(0));
                                currentTripSheetDeliveriesViewHolder.amount.setText(Utility.getFormattedCurrency(0));

                                updateSelectedProductsList(currentDeliveryBean);
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
           // if (!Utility.isDeliveryFirstTime) {
                if (selectedDeliveryProductsHashMap.containsKey(deliverysBean.getProductId()))
                    selectedDeliveryProductsHashMap.remove(deliverysBean.getProductId());

                selectedDeliveryProductsHashMap.put(deliverysBean.getProductId(), deliverysBean);

                if (listener != null)
                    listener.updateDeliveryProductsList(selectedDeliveryProductsHashMap);
           // }
//            else {
//                if (selectedDeliveryProductsHashMapTemp.containsKey(deliverysBean.getProductId()))
//                    selectedDeliveryProductsHashMapTemp.remove(deliverysBean.getProductId());
//
//                selectedDeliveryProductsHashMapTemp.put(deliverysBean.getProductId(), deliverysBean);
//
//                if (listener != null)
//                    listener.updateDeliveryProductsList(selectedDeliveryProductsHashMapTemp);
//            }

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
