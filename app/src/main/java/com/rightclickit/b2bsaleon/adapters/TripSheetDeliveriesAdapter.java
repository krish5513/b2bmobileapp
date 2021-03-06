package com.rightclickit.b2bsaleon.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.TripsheetDelivery;
import com.rightclickit.b2bsaleon.beanclass.DeliverysBean;
import com.rightclickit.b2bsaleon.beanclass.TripsheetSOList;
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
    private Map<String, DeliverysBean> selectedDeliveryProductsHashMap, selectedDeliveryProductsHashMapForPreview; // Hash Map Key = Product Id
    private Map<String, String> previouslyDeliveredProductsHashMap;
    private Map<String, String> productOrderQuantitiesHashMap, productTypehashMap, productUomHashMap,productUnitRateHashmap;
    private final String zero_cost = "0.000";
    private boolean isDeliveryInEditingMode = false;
    private Map<String, DeliverysBean> selectedDeliveryProductsHashMapTemp;
    private Map<String, String> selectedDeliveryProductsHashMapTemp1, selectedDeliveryProductsHashMapTemp2;
    ArrayList<TripsheetSOList> SoListArray;

    public TripSheetDeliveriesAdapter(Context ctxt, TripsheetDelivery deliveryActivity, TripSheetDeliveriesListener deliveriesListener, ArrayList<DeliverysBean> mdeliveriesBeanList, Map<String, String> previouslyDeliveredProducts, Map<String, String> productOrderQuantities, Map<String, String> productTypehashMap1, Map<String, String> productUomHashMap1,Map<String, String> productUnitRateHashmap1) {
        this.ctxt = ctxt;
        this.activity = deliveryActivity;
        this.mInflater = LayoutInflater.from(activity);
        this.listener = deliveriesListener;
        this.allDeliveryProductsList = mdeliveriesBeanList;
        this.filteredDeliveryProductsList = new ArrayList<>();
        this.filteredDeliveryProductsList.addAll(allDeliveryProductsList);
        this.selectedDeliveryProductsHashMap = new HashMap<>();
        this.selectedDeliveryProductsHashMapForPreview = new HashMap<>();
        this.previouslyDeliveredProductsHashMap = previouslyDeliveredProducts;
        this.productOrderQuantitiesHashMap = productOrderQuantities;
        this.productTypehashMap = productTypehashMap1;
        this.productUomHashMap = productUomHashMap1;
        this.productUnitRateHashmap=productUnitRateHashmap1;

        this.selectedDeliveryProductsHashMapTemp = new HashMap<>();
        this.selectedDeliveryProductsHashMapTemp1 = new HashMap<>();
        this.selectedDeliveryProductsHashMapTemp2 = new HashMap<>();

        if (!previouslyDeliveredProductsHashMap.isEmpty()) {
            isDeliveryInEditingMode = true;
        }
        if (selectedDeliveryProductsHashMapTemp1.size() > 0) {
            selectedDeliveryProductsHashMapTemp1.clear();
        }
        if (selectedDeliveryProductsHashMapTemp2.size() > 0) {
            selectedDeliveryProductsHashMapTemp2.clear();
        }

        // in order to update total amount's at the time of initial loading.
        for (DeliverysBean deliverysBean : filteredDeliveryProductsList) {
            System.out.println("Agent Price::" + deliverysBean.getProductAgentPrice());
             double productRatePerUnit=0.0;
           /* if (deliverysBean.getProductAgentPrice() != null) {
            productRatePerUnit= Double.parseDouble(productUnitRateHashmap.get(deliverysBean.getProductCode()));
            } else {
                productRatePerUnit = 0.0f;
            }*/

          /* if (deliverysBean.getProductAgentPrice() != null) {
                productRatePerUnit = Double.parseDouble(deliverysBean.getProductAgentPrice().replace(",", ""));
            } else {
                productRatePerUnit = 0.0f;
            }
*/
          //  deliverysBean.setProductAgentPrice(productUnitRateHashmap.get(deliverysBean.getProductCode()));
            if (productUnitRateHashmap.containsKey(deliverysBean.getProductCode())) {
                productRatePerUnit = Double.parseDouble(productUnitRateHashmap.get(deliverysBean.getProductCode()));
            }else if(deliverysBean.getProductAgentPrice() != null){
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
            System.out.println("PrdoCode:::: " + deliverysBean.getProductCode());
            System.out.println("PrdoCodeTYPEEE:::: " + productTypehashMap.get(deliverysBean.getProductCode()));

            //if(productTypehashMap.containsKey(deliverysBean.getProductCode())) {


            if(productTypehashMap.get(deliverysBean.getProductCode())!=null) {
                Log.i("","");
                deliverysBean.setProductType(productTypehashMap.get(deliverysBean.getProductCode()));
            }else {
                deliverysBean.setProductType("S");
            }
           // }

            if (productUomHashMap.containsKey(deliverysBean.getProductCode())) {
                deliverysBean.setProductUom(productUomHashMap.get(deliverysBean.getProductCode()));
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

            if (deliverysBean.getSelectedQuantity() > 0) {
                selectedDeliveryProductsHashMapTemp2.put(deliverysBean.getProductCode(), deliverysBean.getProductCode());
                if (listener != null)
                    listener.updateDeliveryProductsListTemp(selectedDeliveryProductsHashMapTemp2);
            }
        }
    }

    public class TripSheetDeliveriesViewHolder {
        TextView product_name, quantity_stock, price, tax, amount, product_code;
        EditText product_quantity;
        ImageView product_quantity_decrement, product_quantity_increment;
        LinearLayout total;
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
        for (Map.Entry<String, DeliverysBean> deliverysBeanEntry : selectedDeliveryProductsHashMap.entrySet()) {
            DeliverysBean deliverysBean = deliverysBeanEntry.getValue();
            if (deliverysBean.getSelectedQuantity() > 0) {
                selectedDeliveryProductsHashMapForPreview.put(deliverysBean.getProductId(), deliverysBean);
            }
        }
        return selectedDeliveryProductsHashMapForPreview;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final TripSheetDeliveriesViewHolder tripSheetDeliveriesViewHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.tripsheetdeliveries_custom, null);

            tripSheetDeliveriesViewHolder = new TripSheetDeliveriesViewHolder();
            tripSheetDeliveriesViewHolder.product_name = (TextView) view.findViewById(R.id.productName);
            tripSheetDeliveriesViewHolder.product_code = (TextView) view.findViewById(R.id.productCode);
            tripSheetDeliveriesViewHolder.quantity_stock = (TextView) view.findViewById(R.id.quantity_in_stock);
            tripSheetDeliveriesViewHolder.price = (TextView) view.findViewById(R.id.productPrice);
            tripSheetDeliveriesViewHolder.tax = (TextView) view.findViewById(R.id.taxAmount);
            tripSheetDeliveriesViewHolder.amount = (TextView) view.findViewById(R.id.amount);
            tripSheetDeliveriesViewHolder.product_quantity = (EditText) view.findViewById(R.id.productQt);
            tripSheetDeliveriesViewHolder.product_quantity_decrement = (ImageView) view.findViewById(R.id.productQtDec);
            tripSheetDeliveriesViewHolder.product_quantity_increment = (ImageView) view.findViewById(R.id.productQtInc);
            tripSheetDeliveriesViewHolder.total = (LinearLayout) view.findViewById(R.id.totallayout);


            view.setTag(tripSheetDeliveriesViewHolder);
        } else {
            tripSheetDeliveriesViewHolder = (TripSheetDeliveriesViewHolder) view.getTag();
        }

        final TripSheetDeliveriesViewHolder currentTripSheetDeliveriesViewHolder = tripSheetDeliveriesViewHolder;

        final DeliverysBean currentDeliveryBean = getItem(position);

        ;
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
        Log.i("currentDeliveryBean...",currentDeliveryBean.getProductCode().endsWith("_F")+"");
        if (currentDeliveryBean.getProductCode().endsWith("_F")) {
            tripSheetDeliveriesViewHolder.total.setBackgroundColor(Color.parseColor("#d3d3d3"));
            String[] codeParts = currentDeliveryBean.getProductCode().split("_");
            tripSheetDeliveriesViewHolder.product_name.setText(String.format("%s", currentDeliveryBean.getProductTitle())
                    + ", " + codeParts[0]
                    + ", " + currentDeliveryBean.getProductType() +
                    ", " + currentDeliveryBean.getProductUom());
        } else {
            tripSheetDeliveriesViewHolder.total.setBackgroundColor(Color.parseColor("#f3f3f3"));
            tripSheetDeliveriesViewHolder.product_name.setText(String.format("%s", currentDeliveryBean.getProductTitle())
                    + ", " + currentDeliveryBean.getProductCode()
                    + ", " + currentDeliveryBean.getProductType() +
                    ", " + currentDeliveryBean.getProductUom());
        }

        //       tripSheetDeliveriesViewHolder.product_name.setText(String.format("%s", currentDeliveryBean.getProductTitle()) + "," + String.format("%s", currentDeliveryBean.getProductType()));
     //   tripSheetDeliveriesViewHolder.product_code.setText("," + currentDeliveryBean.getProductCode());
        tripSheetDeliveriesViewHolder.quantity_stock.setText(String.format("%.3f + %.3f", currentDeliveryBean.getProductStock(), currentDeliveryBean.getProductExtraQuantity()));
        if (currentDeliveryBean.getProductAgentPrice() != null) {
            tripSheetDeliveriesViewHolder.price.setText(Utility.getFormattedCurrency((currentDeliveryBean.getProductRatePerUnit())));
        } else {
            tripSheetDeliveriesViewHolder.price.setText("RS.0.00");
        }
        tripSheetDeliveriesViewHolder.tax.setText(Utility.getFormattedCurrency(currentDeliveryBean.getTaxAmount()));
        tripSheetDeliveriesViewHolder.amount.setText(Utility.getFormattedCurrency(currentDeliveryBean.getProductAmount()));


        if (selectedDeliveryProductsHashMapTemp1.get(currentDeliveryBean.getProductId()) != null) {
            Double dq = Double.parseDouble(selectedDeliveryProductsHashMapTemp1.get(currentDeliveryBean.getProductId()));
            tripSheetDeliveriesViewHolder.product_quantity.setText(String.format("%.3f", dq));
        } else {
            tripSheetDeliveriesViewHolder.product_quantity.setText(String.format("%.3f", currentDeliveryBean.getProductOrderedQuantity()));
        }


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
                        selectedDeliveryProductsHashMapTemp1.put(currentDeliveryBean.getProductId(), String.valueOf(currentDeliveryBean.getSelectedQuantity()));
                        currentTripSheetDeliveriesViewHolder.tax.setText(Utility.getFormattedCurrency(currentDeliveryBean.getTaxAmount()));
                        currentTripSheetDeliveriesViewHolder.amount.setText(Utility.getFormattedCurrency(currentDeliveryBean.getProductAmount()));

                        selectedDeliveryProductsHashMapTemp2.put(currentDeliveryBean.getProductCode(), currentDeliveryBean.getProductCode());

                        if (presentQuantity == 0) {
                            currentTripSheetDeliveriesViewHolder.product_quantity.setText(zero_cost);
                            updateSelectedProductsList(currentDeliveryBean);
                        } else {
                            currentTripSheetDeliveriesViewHolder.product_quantity.setText(String.format("%.3f", presentQuantity));
                            updateSelectedProductsList(currentDeliveryBean);
                        }

                        if (listener != null)
                            listener.updateDeliveryProductsListTemp(selectedDeliveryProductsHashMapTemp2);
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

                    if(currentDeliveryBean.getProductType()!=null){
                        if (currentDeliveryBean.getProductType().equals("F")) {
                            presentQuantity++;

                            double amount = currentDeliveryBean.getProductRatePerUnit() * presentQuantity;
                            double taxAmount = (amount * currentDeliveryBean.getProductTaxPerUnit()) / 100;

                            currentDeliveryBean.setSelectedQuantity(presentQuantity);
                            currentDeliveryBean.setProductAmount(amount);
                            currentDeliveryBean.setTaxAmount(taxAmount);
                            selectedDeliveryProductsHashMapTemp1.put(currentDeliveryBean.getProductId(), String.valueOf(currentDeliveryBean.getSelectedQuantity()));
                            currentTripSheetDeliveriesViewHolder.product_quantity.setText(String.format("%.3f", presentQuantity));
                            currentTripSheetDeliveriesViewHolder.tax.setText(Utility.getFormattedCurrency(currentDeliveryBean.getTaxAmount()));
                            currentTripSheetDeliveriesViewHolder.amount.setText(Utility.getFormattedCurrency(currentDeliveryBean.getProductAmount()));

                            selectedDeliveryProductsHashMapTemp2.put(currentDeliveryBean.getProductCode(), currentDeliveryBean.getProductCode());

                            updateSelectedProductsList(currentDeliveryBean);

                            if (listener != null)
                                listener.updateDeliveryProductsListTemp(selectedDeliveryProductsHashMapTemp2);
                        } else {
                            if (presentQuantity < currentDeliveryBean.getProductAvailableStockForSpecificAgent()) {
                                presentQuantity++;

                                double amount = currentDeliveryBean.getProductRatePerUnit() * presentQuantity;
                                double taxAmount = (amount * currentDeliveryBean.getProductTaxPerUnit()) / 100;

                                currentDeliveryBean.setSelectedQuantity(presentQuantity);
                                currentDeliveryBean.setProductAmount(amount);
                                currentDeliveryBean.setTaxAmount(taxAmount);
                                selectedDeliveryProductsHashMapTemp1.put(currentDeliveryBean.getProductId(), String.valueOf(currentDeliveryBean.getSelectedQuantity()));
                                currentTripSheetDeliveriesViewHolder.product_quantity.setText(String.format("%.3f", presentQuantity));
                                currentTripSheetDeliveriesViewHolder.tax.setText(Utility.getFormattedCurrency(currentDeliveryBean.getTaxAmount()));
                                currentTripSheetDeliveriesViewHolder.amount.setText(Utility.getFormattedCurrency(currentDeliveryBean.getProductAmount()));

                                selectedDeliveryProductsHashMapTemp2.put(currentDeliveryBean.getProductCode(), currentDeliveryBean.getProductCode());

                                updateSelectedProductsList(currentDeliveryBean);

                                if (listener != null)
                                    listener.updateDeliveryProductsListTemp(selectedDeliveryProductsHashMapTemp2);
                            }
                        }
                    }else {
                        if (presentQuantity < currentDeliveryBean.getProductAvailableStockForSpecificAgent()) {
                            presentQuantity++;

                            double amount = currentDeliveryBean.getProductRatePerUnit() * presentQuantity;
                            double taxAmount = (amount * currentDeliveryBean.getProductTaxPerUnit()) / 100;

                            currentDeliveryBean.setSelectedQuantity(presentQuantity);
                            currentDeliveryBean.setProductAmount(amount);
                            currentDeliveryBean.setTaxAmount(taxAmount);
                            selectedDeliveryProductsHashMapTemp1.put(currentDeliveryBean.getProductId(), String.valueOf(currentDeliveryBean.getSelectedQuantity()));
                            currentTripSheetDeliveriesViewHolder.product_quantity.setText(String.format("%.3f", presentQuantity));
                            currentTripSheetDeliveriesViewHolder.tax.setText(Utility.getFormattedCurrency(currentDeliveryBean.getTaxAmount()));
                            currentTripSheetDeliveriesViewHolder.amount.setText(Utility.getFormattedCurrency(currentDeliveryBean.getProductAmount()));

                            selectedDeliveryProductsHashMapTemp2.put(currentDeliveryBean.getProductCode(), currentDeliveryBean.getProductCode());

                            updateSelectedProductsList(currentDeliveryBean);

                            if (listener != null)
                                listener.updateDeliveryProductsListTemp(selectedDeliveryProductsHashMapTemp2);
                        }
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
                    if (currentDeliveryBean.getProductType()!=null) {
                        if (currentDeliveryBean.getProductType().equals("F")) {
                            EditText quantityEditText = (EditText) view;
                            Double enteredQuantity = Double.parseDouble(quantityEditText.getText().toString());
                            if (enteredQuantity >= 0) {
                                double amount = currentDeliveryBean.getProductRatePerUnit() * enteredQuantity;
                                double taxAmount = (amount * currentDeliveryBean.getProductTaxPerUnit()) / 100;

                                currentDeliveryBean.setSelectedQuantity(enteredQuantity);
                                currentDeliveryBean.setProductAmount(amount);
                                currentDeliveryBean.setTaxAmount(taxAmount);
                                selectedDeliveryProductsHashMapTemp1.put(currentDeliveryBean.getProductId(), String.valueOf(currentDeliveryBean.getSelectedQuantity()));
                                currentTripSheetDeliveriesViewHolder.tax.setText(Utility.getFormattedCurrency(currentDeliveryBean.getTaxAmount()));
                                currentTripSheetDeliveriesViewHolder.amount.setText(Utility.getFormattedCurrency(currentDeliveryBean.getProductAmount()));

                                selectedDeliveryProductsHashMapTemp2.put(currentDeliveryBean.getProductCode(), currentDeliveryBean.getProductCode());

                                updateSelectedProductsList(currentDeliveryBean);

                                if (listener != null)
                                    listener.updateDeliveryProductsListTemp(selectedDeliveryProductsHashMapTemp2);
                            } else if (enteredQuantity == 0) {
                                currentDeliveryBean.setSelectedQuantity(0);
                                currentDeliveryBean.setProductAmount(0);
                                currentDeliveryBean.setTaxAmount(0);

                                currentTripSheetDeliveriesViewHolder.tax.setText(Utility.getFormattedCurrency(0));
                                currentTripSheetDeliveriesViewHolder.amount.setText(Utility.getFormattedCurrency(0));

                                selectedDeliveryProductsHashMapTemp2.put(currentDeliveryBean.getProductCode(), currentDeliveryBean.getProductCode());

                                updateSelectedProductsList(currentDeliveryBean);

                                if (listener != null)
                                    listener.updateDeliveryProductsListTemp(selectedDeliveryProductsHashMapTemp2);
                            }
                        } else {
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

                                    selectedDeliveryProductsHashMapTemp2.put(currentDeliveryBean.getProductCode(), currentDeliveryBean.getProductCode());

                                    updateSelectedProductsList(currentDeliveryBean);

                                    if (listener != null)
                                        listener.updateDeliveryProductsListTemp(selectedDeliveryProductsHashMapTemp2);

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
                                    if (enteredQuantity >= 0) {
                                        double amount = currentDeliveryBean.getProductRatePerUnit() * enteredQuantity;
                                        double taxAmount = (amount * currentDeliveryBean.getProductTaxPerUnit()) / 100;

                                        currentDeliveryBean.setSelectedQuantity(enteredQuantity);
                                        currentDeliveryBean.setProductAmount(amount);
                                        currentDeliveryBean.setTaxAmount(taxAmount);
                                        selectedDeliveryProductsHashMapTemp1.put(currentDeliveryBean.getProductId(), String.valueOf(currentDeliveryBean.getSelectedQuantity()));
                                        currentTripSheetDeliveriesViewHolder.tax.setText(Utility.getFormattedCurrency(currentDeliveryBean.getTaxAmount()));
                                        currentTripSheetDeliveriesViewHolder.amount.setText(Utility.getFormattedCurrency(currentDeliveryBean.getProductAmount()));

                                        selectedDeliveryProductsHashMapTemp2.put(currentDeliveryBean.getProductCode(), currentDeliveryBean.getProductCode());

                                        updateSelectedProductsList(currentDeliveryBean);

                                        if (listener != null)
                                            listener.updateDeliveryProductsListTemp(selectedDeliveryProductsHashMapTemp2);
                                    } else if (enteredQuantity == 0) {
                                        currentDeliveryBean.setSelectedQuantity(0);
                                        currentDeliveryBean.setProductAmount(0);
                                        currentDeliveryBean.setTaxAmount(0);

                                        currentTripSheetDeliveriesViewHolder.tax.setText(Utility.getFormattedCurrency(0));
                                        currentTripSheetDeliveriesViewHolder.amount.setText(Utility.getFormattedCurrency(0));

                                        selectedDeliveryProductsHashMapTemp2.put(currentDeliveryBean.getProductCode(), currentDeliveryBean.getProductCode());

                                        updateSelectedProductsList(currentDeliveryBean);

                                        if (listener != null)
                                            listener.updateDeliveryProductsListTemp(selectedDeliveryProductsHashMapTemp2);
                                    }
                                }
                            }
                        }
                    }else {
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

                                selectedDeliveryProductsHashMapTemp2.put(currentDeliveryBean.getProductCode(), currentDeliveryBean.getProductCode());

                                updateSelectedProductsList(currentDeliveryBean);

                                if (listener != null)
                                    listener.updateDeliveryProductsListTemp(selectedDeliveryProductsHashMapTemp2);

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
                                if (enteredQuantity >= 0) {
                                    double amount = currentDeliveryBean.getProductRatePerUnit() * enteredQuantity;
                                    double taxAmount = (amount * currentDeliveryBean.getProductTaxPerUnit()) / 100;

                                    currentDeliveryBean.setSelectedQuantity(enteredQuantity);
                                    currentDeliveryBean.setProductAmount(amount);
                                    currentDeliveryBean.setTaxAmount(taxAmount);
                                    selectedDeliveryProductsHashMapTemp1.put(currentDeliveryBean.getProductId(), String.valueOf(currentDeliveryBean.getSelectedQuantity()));
                                    currentTripSheetDeliveriesViewHolder.tax.setText(Utility.getFormattedCurrency(currentDeliveryBean.getTaxAmount()));
                                    currentTripSheetDeliveriesViewHolder.amount.setText(Utility.getFormattedCurrency(currentDeliveryBean.getProductAmount()));

                                    selectedDeliveryProductsHashMapTemp2.put(currentDeliveryBean.getProductCode(), currentDeliveryBean.getProductCode());

                                    updateSelectedProductsList(currentDeliveryBean);

                                    if (listener != null)
                                        listener.updateDeliveryProductsListTemp(selectedDeliveryProductsHashMapTemp2);
                                } else if (enteredQuantity == 0) {
                                    currentDeliveryBean.setSelectedQuantity(0);
                                    currentDeliveryBean.setProductAmount(0);
                                    currentDeliveryBean.setTaxAmount(0);

                                    currentTripSheetDeliveriesViewHolder.tax.setText(Utility.getFormattedCurrency(0));
                                    currentTripSheetDeliveriesViewHolder.amount.setText(Utility.getFormattedCurrency(0));

                                    selectedDeliveryProductsHashMapTemp2.put(currentDeliveryBean.getProductCode(), currentDeliveryBean.getProductCode());

                                    updateSelectedProductsList(currentDeliveryBean);

                                    if (listener != null)
                                        listener.updateDeliveryProductsListTemp(selectedDeliveryProductsHashMapTemp2);
                                }
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
