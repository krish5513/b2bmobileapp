package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.TripSheetStock;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
import com.rightclickit.b2bsaleon.interfaces.TripSheetStockListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Sekhar Kuppa
 * <p>
 * Modified by Venkat
 */

public class TripsheetsStockListAdapter extends BaseAdapter {
    private Context ctxt;
    private Activity activity;
    private TripSheetStockListener listener;
    private LayoutInflater mInflater;
    private ArrayList<TripsheetsStockList> allTripSheetStockList, filteredTripSheetStockList;
    private ArrayList<String> privilegeActionsData;
    private Map<String, TripsheetsStockList> dispatchProductsListHashMap, verifyProductsListHashMap;

    private final String zero_cost = "0.000";

    public TripsheetsStockListAdapter(Context ctxt, TripSheetStock agentsActivity, TripSheetStockListener tripSheetStockListener, ArrayList<TripsheetsStockList> tripSheetStockList, ArrayList<String> privilegeActionsData) {
        this.ctxt = ctxt;
        this.activity = agentsActivity;
        this.listener = tripSheetStockListener;
        this.mInflater = LayoutInflater.from(activity);
        this.allTripSheetStockList = tripSheetStockList;
        this.filteredTripSheetStockList = new ArrayList<>();
        this.filteredTripSheetStockList.addAll(allTripSheetStockList);
        this.privilegeActionsData = privilegeActionsData;
        this.dispatchProductsListHashMap = new HashMap<>();
        this.verifyProductsListHashMap = new HashMap<>();
    }

    @Override
    public int getCount() {
        return filteredTripSheetStockList.size();
    }

    @Override
    public TripsheetsStockList getItem(int position) {
        return filteredTripSheetStockList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final TripSheetStockViewHolder tripSheetStockViewHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.tripsheets_stock_list_custom, null);

            tripSheetStockViewHolder = new TripSheetStockViewHolder();
            tripSheetStockViewHolder.mProductName = (TextView) view.findViewById(R.id.ts_stock_product_name);
            tripSheetStockViewHolder.mOrder = (TextView) view.findViewById(R.id.ts_stock_order_quantity);
            tripSheetStockViewHolder.mDispatchDecrement = (ImageButton) view.findViewById(R.id.ts_stock_dispatch_decrement);
            tripSheetStockViewHolder.mDispatchQuantity = (EditText) view.findViewById(R.id.ts_stock_dispatch_quantity);
            tripSheetStockViewHolder.mDispatchIncrement = (ImageButton) view.findViewById(R.id.ts_stock_dispatch_increment);
            tripSheetStockViewHolder.mVerifyDecrement = (ImageButton) view.findViewById(R.id.ts_stock_verify_decrement);
            tripSheetStockViewHolder.mVerifyQuantity = (EditText) view.findViewById(R.id.ts_stock_verify_quantity);
            tripSheetStockViewHolder.mVerifyIncrement = (ImageButton) view.findViewById(R.id.ts_stock_verify_increment);

            view.setTag(tripSheetStockViewHolder);
        } else {
            tripSheetStockViewHolder = (TripSheetStockViewHolder) view.getTag();
        }

        final TripsheetsStockList currentStockList = getItem(position);

        Double orderQuantity = Double.parseDouble(currentStockList.getmTripsheetStockProductOrderQuantity());

        tripSheetStockViewHolder.mProductName.setText(currentStockList.getmTripsheetStockProductName());
        tripSheetStockViewHolder.mOrder.setText(String.format("%.3f", orderQuantity));

        if (privilegeActionsData.contains("Stock_Dispatch")) {
            tripSheetStockViewHolder.mDispatchQuantity.setVisibility(View.VISIBLE);
            tripSheetStockViewHolder.mDispatchDecrement.setVisibility(View.VISIBLE);
            tripSheetStockViewHolder.mDispatchIncrement.setVisibility(View.VISIBLE);

            if (currentStockList.getIsStockDispatched() == 0) {
                tripSheetStockViewHolder.mDispatchQuantity.setText(String.format("%.3f", orderQuantity));
                currentStockList.setmTripsheetStockDispatchQuantity(String.valueOf(orderQuantity));
            } else {
                Double dispatchedQuantity = Double.parseDouble(currentStockList.getmTripsheetStockDispatchQuantity());
                tripSheetStockViewHolder.mDispatchQuantity.setText(String.format("%.3f", dispatchedQuantity));
                currentStockList.setmTripsheetStockDispatchQuantity(String.valueOf(dispatchedQuantity));
            }

            // Updating current stock in dispatch list
            updateProductsDispatchList(currentStockList);

            tripSheetStockViewHolder.mDispatchDecrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Double presentQuantity = Double.parseDouble(tripSheetStockViewHolder.mDispatchQuantity.getText().toString());

                        if (presentQuantity > 0) {
                            presentQuantity--;

                            currentStockList.setmTripsheetStockDispatchQuantity(String.valueOf(presentQuantity));
                            updateProductsDispatchList(currentStockList);

                            if (presentQuantity == 0) {
                                tripSheetStockViewHolder.mDispatchQuantity.setText(zero_cost);
                                //removeProductFromDispatchList(currentStockList);
                            } else {
                                tripSheetStockViewHolder.mDispatchQuantity.setText(String.format("%.3f", presentQuantity));

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            tripSheetStockViewHolder.mDispatchIncrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Double presentQuantity = Double.parseDouble(tripSheetStockViewHolder.mDispatchQuantity.getText().toString());
                        presentQuantity++;

                        currentStockList.setmTripsheetStockDispatchQuantity(String.valueOf(presentQuantity));
                        tripSheetStockViewHolder.mDispatchQuantity.setText(String.format("%.3f", presentQuantity));
                        updateProductsDispatchList(currentStockList);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        if (privilegeActionsData.contains("Stock_Verify")) {
            tripSheetStockViewHolder.mVerifyDecrement.setVisibility(View.VISIBLE);
            tripSheetStockViewHolder.mVerifyQuantity.setVisibility(View.VISIBLE);
            tripSheetStockViewHolder.mVerifyIncrement.setVisibility(View.VISIBLE);

            if (currentStockList.getIsStockVerified() == 0) {
                tripSheetStockViewHolder.mVerifyQuantity.setText(String.format("%.3f", orderQuantity));
                currentStockList.setmTripsheetStockVerifiedQuantity(String.valueOf(orderQuantity));
            } else {
                Double verifiedQuantity = Double.parseDouble(currentStockList.getmTripsheetStockVerifiedQuantity());
                tripSheetStockViewHolder.mVerifyQuantity.setText(String.format("%.3f", verifiedQuantity));
                currentStockList.setmTripsheetStockVerifiedQuantity(String.valueOf(verifiedQuantity));
            }

            // Updating current stock in verify list
            updateProductsVerifyList(currentStockList);

            tripSheetStockViewHolder.mVerifyDecrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Double presentQuantity = Double.parseDouble(tripSheetStockViewHolder.mVerifyQuantity.getText().toString());

                        if (presentQuantity > 0) {
                            presentQuantity--;

                            currentStockList.setmTripsheetStockVerifiedQuantity(String.valueOf(presentQuantity));
                            updateProductsVerifyList(currentStockList);

                            if (presentQuantity == 0) {
                                tripSheetStockViewHolder.mVerifyQuantity.setText(zero_cost);
                                //removeProductFromVerifyList(currentStockList);
                            } else {
                                tripSheetStockViewHolder.mVerifyQuantity.setText(String.format("%.3f", presentQuantity));

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            tripSheetStockViewHolder.mVerifyIncrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Double presentQuantity = Double.parseDouble(tripSheetStockViewHolder.mVerifyQuantity.getText().toString());
                        presentQuantity++;

                        currentStockList.setmTripsheetStockVerifiedQuantity(String.valueOf(presentQuantity));
                        tripSheetStockViewHolder.mVerifyQuantity.setText(String.format("%.3f", presentQuantity));
                        updateProductsVerifyList(currentStockList);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        return view;
    }

    public class TripSheetStockViewHolder {
        TextView mProductName;
        TextView mOrder;
        EditText mDispatchQuantity;
        EditText mVerifyQuantity;
        ImageButton mDispatchDecrement;
        ImageButton mDispatchIncrement;
        ImageButton mVerifyDecrement;
        ImageButton mVerifyIncrement;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        filteredTripSheetStockList.clear();

        if (charText.length() == 0) {
            filteredTripSheetStockList.addAll(allTripSheetStockList);
        } else {
            for (TripsheetsStockList stock : allTripSheetStockList) {
                if (stock.getmTripsheetStockProductName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredTripSheetStockList.add(stock);
                }
            }
        }

        notifyDataSetChanged();
    }

    public void updateProductsDispatchList(TripsheetsStockList tripsheetsStockList) {
        try {
            // In HashMap if you are trying to put same key multiple times then the value of key will be override by latest value which you have pass.
            dispatchProductsListHashMap.put(tripsheetsStockList.getmTripsheetStockProductId(), tripsheetsStockList);

            if (listener != null)
                listener.updateProductsDispatchList(dispatchProductsListHashMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProductsVerifyList(TripsheetsStockList tripsheetsStockList) {
        try {
            verifyProductsListHashMap.put(tripsheetsStockList.getmTripsheetStockProductId(), tripsheetsStockList);

            if (listener != null)
                listener.updateProductsVerifyList(verifyProductsListHashMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void removeProductFromDispatchList(TripsheetsStockList tripsheetsStockList) {
        try {
            if (dispatchProductsListHashMap.containsKey(tripsheetsStockList.getmTripsheetStockProductId()))
                dispatchProductsListHashMap.remove(tripsheetsStockList.getmTripsheetStockProductId());

            if (listener != null)
                listener.updateProductsDispatchList(dispatchProductsListHashMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeProductFromVerifyList(TripsheetsStockList tripsheetsStockList) {
        try {
            if (dispatchProductsListHashMap.containsKey(tripsheetsStockList.getmTripsheetStockProductId()))
                dispatchProductsListHashMap.remove(tripsheetsStockList.getmTripsheetStockProductId());
            System.out.println("==== 1 ===== verifyProductsListHashMap = " + verifyProductsListHashMap.size() + " && Pro. Id - " + tripsheetsStockList.getmTripsheetStockProductId());
            if (listener != null)
                listener.updateProductsVerifyList(verifyProductsListHashMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
