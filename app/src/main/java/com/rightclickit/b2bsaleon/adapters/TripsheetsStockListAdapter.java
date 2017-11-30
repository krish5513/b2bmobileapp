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
import android.widget.ImageButton;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.TripSheetStock;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
import com.rightclickit.b2bsaleon.database.DBHelper;
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
    private Map<String, String> dispatchProductsListHashMapTemp, verifyProductsListHashMapTemp;

    private final String zero_cost = "0.000";

    String myList ;
    DBHelper mDBHelper;


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
        this.dispatchProductsListHashMapTemp = new HashMap<>();
        this.verifyProductsListHashMap = new HashMap<>();
        this.verifyProductsListHashMapTemp = new HashMap<>();

        mDBHelper=new DBHelper(activity);
        if (dispatchProductsListHashMapTemp.size() > 0) {
            dispatchProductsListHashMapTemp.clear();
        }
        if (verifyProductsListHashMapTemp.size() > 0) {
            verifyProductsListHashMapTemp.clear();
        }
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
            // tripSheetStockViewHolder.mProductCode = (TextView) view.findViewById(R.id.ts_stock_product_code);
            // tripSheetStockViewHolder.mProductUom = (TextView) view.findViewById(R.id.ts_stock_product_uom);


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
        myList= mDBHelper.getProductUnitByProductCode(currentStockList.getmTripsheetStockProductCode());
        tripSheetStockViewHolder.mProductName.setText(currentStockList.getmTripsheetStockProductName() + ","+ currentStockList.getmTripsheetStockProductCode()+","+myList);
        // tripSheetStockViewHolder.mProductCode.setText(","+ currentStockList.getmTripsheetStockProductCode());



        //tripSheetStockViewHolder.mProductUom.setText(","+myList);

        tripSheetStockViewHolder.mOrder.setText(String.format("%.3f", orderQuantity));

        tripSheetStockViewHolder.mDispatchQuantity.setVisibility(View.VISIBLE);
        tripSheetStockViewHolder.mDispatchDecrement.setVisibility(View.GONE);
        tripSheetStockViewHolder.mDispatchIncrement.setVisibility(View.GONE);
        tripSheetStockViewHolder.mDispatchQuantity.setEnabled(false);

        tripSheetStockViewHolder.mVerifyQuantity.setVisibility(View.VISIBLE);
        tripSheetStockViewHolder.mVerifyDecrement.setVisibility(View.GONE);
        tripSheetStockViewHolder.mVerifyIncrement.setVisibility(View.GONE);
        tripSheetStockViewHolder.mVerifyQuantity.setEnabled(false);

        if (currentStockList.getIsStockDispatched() == 0) {
            if (dispatchProductsListHashMapTemp.get(currentStockList.getmTripsheetStockProductId()) != null) {
                Double oq = Double.parseDouble(dispatchProductsListHashMapTemp.get(currentStockList.getmTripsheetStockProductId()));
                tripSheetStockViewHolder.mDispatchQuantity.setText(String.format("%.3f", oq));
                currentStockList.setmTripsheetStockDispatchQuantity(String.valueOf(oq));
            } else if (!currentStockList.getmTripsheetStockDispatchQuantity().equals("")) {
                Double dispatchQuantity = Double.parseDouble(currentStockList.getmTripsheetStockDispatchQuantity());
                tripSheetStockViewHolder.mDispatchQuantity.setText(String.format("%.3f", dispatchQuantity));
                currentStockList.setmTripsheetStockDispatchQuantity(String.valueOf(dispatchQuantity));
            } else {
                tripSheetStockViewHolder.mDispatchQuantity.setText(String.format("%.3f", orderQuantity));
                currentStockList.setmTripsheetStockDispatchQuantity(String.valueOf(orderQuantity));
            }
        } else {
            Double dispatchedQuantity = 0.0;
            if (currentStockList.getmTripsheetStockDispatchQuantity().equals("")) {
                dispatchedQuantity = 0.0;
            } else {
                dispatchedQuantity = Double.parseDouble(currentStockList.getmTripsheetStockDispatchQuantity());
            }
            tripSheetStockViewHolder.mDispatchQuantity.setText(String.format("%.3f", dispatchedQuantity));
            currentStockList.setmTripsheetStockDispatchQuantity(String.valueOf(dispatchedQuantity));
        }

        // Updating current stock in dispatch list
        updateProductsDispatchList(currentStockList);

        if (currentStockList.getIsStockVerified() == 0) {
            if (verifyProductsListHashMapTemp.get(currentStockList.getmTripsheetStockProductId()) != null) {
                Double vq = Double.parseDouble(verifyProductsListHashMapTemp.get(currentStockList.getmTripsheetStockProductId()));
                tripSheetStockViewHolder.mVerifyQuantity.setText(String.format("%.3f", vq));
                currentStockList.setmTripsheetStockVerifiedQuantity(String.valueOf(vq));
            } else if (!currentStockList.getmTripsheetStockVerifiedQuantity().equals("")) {
                Double verifyQuantity = Double.parseDouble(currentStockList.getmTripsheetStockVerifiedQuantity());
                tripSheetStockViewHolder.mVerifyQuantity.setText(String.format("%.3f", verifyQuantity));
                currentStockList.setmTripsheetStockVerifiedQuantity(String.valueOf(verifyQuantity));
            } else {
                tripSheetStockViewHolder.mVerifyQuantity.setText(String.format("%.3f", orderQuantity));
                currentStockList.setmTripsheetStockVerifiedQuantity(String.valueOf(orderQuantity));
            }
        } else {
            Double verifiedQuantity = 0.0;
            if (currentStockList.getmTripsheetStockVerifiedQuantity().equals("")) {
                verifiedQuantity = 0.0;
            } else {
                verifiedQuantity = Double.parseDouble(currentStockList.getmTripsheetStockVerifiedQuantity());
            }
            tripSheetStockViewHolder.mVerifyQuantity.setText(String.format("%.3f", verifiedQuantity));
            currentStockList.setmTripsheetStockVerifiedQuantity(String.valueOf(verifiedQuantity));
        }

        updateProductsVerifyList(currentStockList);

        if (privilegeActionsData.contains("Stock_Dispatch")) {
            tripSheetStockViewHolder.mDispatchQuantity.setVisibility(View.VISIBLE);
            tripSheetStockViewHolder.mDispatchDecrement.setVisibility(View.VISIBLE);
            tripSheetStockViewHolder.mDispatchIncrement.setVisibility(View.VISIBLE);
            tripSheetStockViewHolder.mDispatchQuantity.setEnabled(true);
//
//            if (currentStockList.getIsStockDispatched() == 0) {
//                if (dispatchProductsListHashMapTemp.get(currentStockList.getmTripsheetStockProductId()) != null) {
//                    Double oq = Double.parseDouble(dispatchProductsListHashMapTemp.get(currentStockList.getmTripsheetStockProductId()));
//                    tripSheetStockViewHolder.mDispatchQuantity.setText(String.format("%.3f", oq));
//                    currentStockList.setmTripsheetStockDispatchQuantity(String.valueOf(oq));
//                }else if (!currentStockList.getmTripsheetStockDispatchQuantity().equals("")) {
//                    Double dispatchQuantity = Double.parseDouble(currentStockList.getmTripsheetStockDispatchQuantity());
//                    tripSheetStockViewHolder.mDispatchQuantity.setText(String.format("%.3f", dispatchQuantity));
//                    currentStockList.setmTripsheetStockDispatchQuantity(String.valueOf(dispatchQuantity));
//                } else {
//                    tripSheetStockViewHolder.mDispatchQuantity.setText(String.format("%.3f", orderQuantity));
//                    currentStockList.setmTripsheetStockDispatchQuantity(String.valueOf(orderQuantity));
//                }
//            } else {
//                Double dispatchedQuantity = Double.parseDouble(currentStockList.getmTripsheetStockDispatchQuantity());
//                tripSheetStockViewHolder.mDispatchQuantity.setText(String.format("%.3f", dispatchedQuantity));
//                currentStockList.setmTripsheetStockDispatchQuantity(String.valueOf(dispatchedQuantity));
//            }
//
//            // Updating current stock in dispatch list
//            updateProductsDispatchList(currentStockList);

            tripSheetStockViewHolder.mDispatchDecrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Double presentQuantity = Double.parseDouble(tripSheetStockViewHolder.mDispatchQuantity.getText().toString());

                        if (presentQuantity > 0) {
                            presentQuantity--;

                            currentStockList.setmTripsheetStockDispatchQuantity(String.valueOf(presentQuantity));
                            dispatchProductsListHashMapTemp.put(currentStockList.getmTripsheetStockProductId(), currentStockList.getmTripsheetStockDispatchQuantity());
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
                        dispatchProductsListHashMapTemp.put(currentStockList.getmTripsheetStockProductId(), currentStockList.getmTripsheetStockDispatchQuantity());
                        tripSheetStockViewHolder.mDispatchQuantity.setText(String.format("%.3f", presentQuantity));
                        updateProductsDispatchList(currentStockList);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            tripSheetStockViewHolder.mDispatchQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    try {
                        if(!hasFocus){
                            EditText quantityEditText = (EditText) view;
                            Double enteredQuantity = Double.parseDouble(quantityEditText.getText().toString());

                            currentStockList.setmTripsheetStockDispatchQuantity(String.valueOf(enteredQuantity));
                            dispatchProductsListHashMapTemp.put(currentStockList.getmTripsheetStockProductId(), currentStockList.getmTripsheetStockDispatchQuantity());
                            updateProductsDispatchList(currentStockList);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }

        if (privilegeActionsData.contains("Stock_Verify")) {
            tripSheetStockViewHolder.mVerifyDecrement.setVisibility(View.VISIBLE);
            tripSheetStockViewHolder.mVerifyQuantity.setVisibility(View.VISIBLE);
            tripSheetStockViewHolder.mVerifyIncrement.setVisibility(View.VISIBLE);
            tripSheetStockViewHolder.mVerifyQuantity.setEnabled(true);

//            if (currentStockList.getIsStockVerified() == 0) {
//                if (verifyProductsListHashMapTemp.get(currentStockList.getmTripsheetStockProductId()) != null) {
//                    Double vq = Double.parseDouble(verifyProductsListHashMapTemp.get(currentStockList.getmTripsheetStockProductId()));
//                    tripSheetStockViewHolder.mVerifyQuantity.setText(String.format("%.3f", vq));
//                    currentStockList.setmTripsheetStockVerifiedQuantity(String.valueOf(vq));
//                } else if (!currentStockList.getmTripsheetStockVerifiedQuantity().equals("")) {
//                    Double verifyQuantity = Double.parseDouble(currentStockList.getmTripsheetStockVerifiedQuantity());
//                    tripSheetStockViewHolder.mVerifyQuantity.setText(String.format("%.3f", verifyQuantity));
//                    currentStockList.setmTripsheetStockVerifiedQuantity(String.valueOf(verifyQuantity));
//                } else {
//                    tripSheetStockViewHolder.mVerifyQuantity.setText(String.format("%.3f", orderQuantity));
//                    currentStockList.setmTripsheetStockVerifiedQuantity(String.valueOf(orderQuantity));
//                }
//            } else {
//                Double verifiedQuantity = Double.parseDouble(currentStockList.getmTripsheetStockVerifiedQuantity());
//                tripSheetStockViewHolder.mVerifyQuantity.setText(String.format("%.3f", verifiedQuantity));
//                currentStockList.setmTripsheetStockVerifiedQuantity(String.valueOf(verifiedQuantity));
//            }

            // Updating current stock in verify list
//            updateProductsVerifyList(currentStockList);

            tripSheetStockViewHolder.mVerifyDecrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Double presentQuantity = Double.parseDouble(tripSheetStockViewHolder.mVerifyQuantity.getText().toString());

                        if (presentQuantity > 0) {
                            presentQuantity--;

                            currentStockList.setmTripsheetStockVerifiedQuantity(String.valueOf(presentQuantity));
                            verifyProductsListHashMapTemp.put(currentStockList.getmTripsheetStockProductId(), currentStockList.getmTripsheetStockVerifiedQuantity());
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
                        Double dispatchQuantity = Double.parseDouble(tripSheetStockViewHolder.mDispatchQuantity.getText().toString().trim());
                        if (presentQuantity >= dispatchQuantity) {
                            //tripSheetStockViewHolder.mVerifyQuantity.setText(String.format("%.3f", 0.0));
                            //currentStockList.setmTripsheetStockVerifiedQuantity(String.valueOf(0.0));
                            new AlertDialog.Builder(activity)
                                    .setTitle("Alert..!")
                                    .setMessage("Verify Quantity should not be greater than Dispatch quantity.")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else {
//                            currentStockList.setmTripsheetStockVerifiedQuantity(String.valueOf(enteredQuantity));
//                            verifyProductsListHashMapTemp.put(currentStockList.getmTripsheetStockProductId(), currentStockList.getmTripsheetStockVerifiedQuantity());
//
//                            updateProductsVerifyList(currentStockList);
                            presentQuantity++;
                            currentStockList.setmTripsheetStockVerifiedQuantity(String.valueOf(presentQuantity));
                            verifyProductsListHashMapTemp.put(currentStockList.getmTripsheetStockProductId(), currentStockList.getmTripsheetStockVerifiedQuantity());
                            tripSheetStockViewHolder.mVerifyQuantity.setText(String.format("%.3f", presentQuantity));

                            updateProductsVerifyList(currentStockList);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            tripSheetStockViewHolder.mVerifyQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    try {
                        if (!hasFocus) {
                            EditText quantityEditText = (EditText) view;
                            Double enteredQuantity = Double.parseDouble(quantityEditText.getText().toString());
                            Double dispatchQuantity = Double.parseDouble(tripSheetStockViewHolder.mDispatchQuantity.getText().toString().trim());
                            if (enteredQuantity > dispatchQuantity) {
                                quantityEditText.setText(zero_cost);
                                currentStockList.setmTripsheetStockVerifiedQuantity(String.valueOf(0.0));
                                new AlertDialog.Builder(activity)
                                        .setTitle("Alert..!")
                                        .setMessage("Verify Quantity should not be greater than Dispatch quantity.")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            } else {
                                currentStockList.setmTripsheetStockVerifiedQuantity(String.valueOf(enteredQuantity));
                                verifyProductsListHashMapTemp.put(currentStockList.getmTripsheetStockProductId(), currentStockList.getmTripsheetStockVerifiedQuantity());

                                updateProductsVerifyList(currentStockList);
                            }
                        }
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
        TextView mProductCode;
        TextView mProductUom;


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

                if (stock.getmTripsheetStockProductCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                    filteredTripSheetStockList.add(stock);
                }

                if (myList.toLowerCase(Locale.getDefault()).contains(charText)) {
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