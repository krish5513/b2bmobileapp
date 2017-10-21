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
import com.rightclickit.b2bsaleon.activities.RouteStock;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by PPS on 10/3/2017.
 */

public class RouteStockAdapter extends BaseAdapter {
    private Context ctxt;
    private Activity activity;
    private RouteStock listener;
    private LayoutInflater mInflater;
    private ArrayList<TripsheetsStockList> allTripSheetStockList, filteredTripSheetStockList;
    private ArrayList<String> privilegeActionsData;
    private Map<String, String> selectedLeakageProductsListHashMapTemp, selectedOthersProductsListHashMapTemp, selectedCBListMap;
    private HashMap<String, String> deliveriesQuanListMap1, returnsQuanListMap1;

    private final String zero_cost = "0.000";
    double tq = 0.0, dq = 0.0, rq = 0.0, leq = 0.0, oq = 0.0;

    public RouteStockAdapter(Context ctxt, RouteStock agentsActivity, RouteStock tripSheetStockListener,
                             ArrayList<TripsheetsStockList> tripSheetStockList, HashMap<String, String> deliveriesQuanListMap,
                             HashMap<String, String> returnsQuanListMap) {
        this.ctxt = ctxt;
        this.activity = agentsActivity;
        this.listener = tripSheetStockListener;
        this.mInflater = LayoutInflater.from(activity);
        this.allTripSheetStockList = tripSheetStockList;
        this.filteredTripSheetStockList = new ArrayList<>();
        this.filteredTripSheetStockList.addAll(allTripSheetStockList);
        this.privilegeActionsData = privilegeActionsData;
        this.selectedLeakageProductsListHashMapTemp = new HashMap<>();
        this.selectedOthersProductsListHashMapTemp = new HashMap<>();
        this.selectedCBListMap = new HashMap<>();
        this.deliveriesQuanListMap1 = deliveriesQuanListMap;
        this.returnsQuanListMap1 = returnsQuanListMap;
        if (selectedLeakageProductsListHashMapTemp.size() > 0) {
            selectedLeakageProductsListHashMapTemp.clear();
        }
        if (selectedOthersProductsListHashMapTemp.size() > 0) {
            selectedOthersProductsListHashMapTemp.clear();
        }
        if (selectedCBListMap.size() > 0) {
            selectedCBListMap.clear();
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
        final RouteStockAdapter.ViewHolder mHolder;
        if (view == null) {
            mHolder = new RouteStockAdapter.ViewHolder();
            view = mInflater.inflate(R.layout.routestock_custom, null);

            mHolder.mRouteCode = (TextView) view.findViewById(R.id.route_code);
            mHolder.mTruckQty = (TextView) view.findViewById(R.id.TruckQty);
            mHolder.mClosingBal = (TextView) view.findViewById(R.id.cb);
            mHolder.mProductName = (TextView) view.findViewById(R.id.p_name);
            mHolder.mProductUom = (TextView) view.findViewById(R.id.p_uom);
            mHolder.mProductUom.setVisibility(View.GONE);
            mHolder.mDeliveryQty = (TextView) view.findViewById(R.id.delivery_qty);
            mHolder.mReturnQty = (TextView) view.findViewById(R.id.return_quantity);
            //  mHolder.mReturnQtyInc = (ImageButton) view.findViewById(R.id.return_inc);
            //  mHolder.mReturnQtyDec = (ImageButton) view.findViewById(R.id.return_dec);

            mHolder.mLeakQty = (EditText) view.findViewById(R.id.leak_quantity);
            mHolder.mLeakQtyInc = (ImageButton) view.findViewById(R.id.leak_inc);
            mHolder.mLeakQtyDec = (ImageButton) view.findViewById(R.id.leak_dec);

            mHolder.mOthersQty = (EditText) view.findViewById(R.id.others_quantity);
            mHolder.mOthersQtyInc = (ImageButton) view.findViewById(R.id.others_inc);
            mHolder.mOthersQtyDec = (ImageButton) view.findViewById(R.id.others_dec);


            view.setTag(mHolder);
        } else {
            mHolder = (RouteStockAdapter.ViewHolder) view.getTag();
        }


        final TripsheetsStockList currentStockList = getItem(position);

        mHolder.mProductName.setText(currentStockList.getmTripsheetStockProductName());
        mHolder.mRouteCode.setText(currentStockList.getmTripsheetStockProductCode());

        // TRUCK QUANTITY
        if (currentStockList.getmTripsheetStockVerifiedQuantity().length() > 0) {
            Double verifiedQuantity = Double.parseDouble(currentStockList.getmTripsheetStockVerifiedQuantity());
            mHolder.mTruckQty.setText(String.format("%.3f", verifiedQuantity));
            tq = verifiedQuantity;
        } else {
            mHolder.mTruckQty.setText(String.format("%.3f", 0.0));
            tq = 0.0;
        }

        // DELIVERY QUANTITY
        if (deliveriesQuanListMap1.get(currentStockList.getmTripsheetStockProductId()) != null) {
            Double delQua = Double.parseDouble(deliveriesQuanListMap1.get(currentStockList.getmTripsheetStockProductId()));
            mHolder.mDeliveryQty.setText(String.format("%.3f", delQua));
            dq = delQua;
        } else {
            mHolder.mDeliveryQty.setText(String.format("%.3f", 0.0));
            dq = 0.0;
        }

        // RETUNS QUANTITY
        if (returnsQuanListMap1.get(currentStockList.getmTripsheetStockProductId()) != null) {
            Double delQua = Double.parseDouble(returnsQuanListMap1.get(currentStockList.getmTripsheetStockProductId()));
            mHolder.mReturnQty.setText(String.format("%.3f", delQua));
            rq = delQua;
        } else {
            mHolder.mReturnQty.setText(String.format("%.3f", 0.0));
            rq = 0.0;
        }

        // LEAKAGE
        if (selectedLeakageProductsListHashMapTemp.get(currentStockList.getmTripsheetStockProductId()) != null) {
            Double quantity = Double.parseDouble(selectedLeakageProductsListHashMapTemp.get(currentStockList.getmTripsheetStockProductId()));
            mHolder.mLeakQty.setText(String.format("%.3f", quantity));
            leq = quantity;
        } else if (currentStockList.getmLeakQuantity() != null) {
            String lq = currentStockList.getmLeakQuantity();
            if (lq.length() > 0) {
                Double quantity = Double.parseDouble(lq);
                if (quantity > 0) {
                    mHolder.mLeakQty.setText(String.format("%.3f", quantity));
                    leq = quantity;
                } else {
                    mHolder.mLeakQty.setText(String.format("%.3f", 0.0));
                    leq = 0.0;
                }
            } else {
                mHolder.mLeakQty.setText(String.format("%.3f", 0.0));
                leq = 0.0;
            }
        } else {
            mHolder.mLeakQty.setText(String.format("%.3f", 0.0));
            leq = 0.0;
        }

        // OTHERS
        if (selectedOthersProductsListHashMapTemp.get(currentStockList.getmTripsheetStockProductId()) != null) {
            Double quantity = Double.parseDouble(selectedOthersProductsListHashMapTemp.get(currentStockList.getmTripsheetStockProductId()));
            mHolder.mOthersQty.setText(String.format("%.3f", quantity));
            oq = quantity;
        } else if (currentStockList.getmOtherQuantity() != null) {
            String lq = currentStockList.getmOtherQuantity();
            if (lq.length() > 0) {
                Double quantity = Double.parseDouble(lq);
                if (quantity > 0) {
                    mHolder.mOthersQty.setText(String.format("%.3f", quantity));
                    oq = quantity;
                } else {
                    mHolder.mOthersQty.setText(String.format("%.3f", 0.0));
                }
            } else {
                mHolder.mOthersQty.setText(String.format("%.3f", 0.0));
                oq = 0.0;
            }
        } else {
            mHolder.mOthersQty.setText(String.format("%.3f", 0.0));
            oq = 0.0;
        }

        // CB formula ---- ClosingBalance = (TruckQuantity + ReturnsQuantity) - (DeliveryQunatity+LeakQuantity+OthersQuantity)
        double cb1 = tq + rq;
        System.out.println("CB11:::: " + cb1);
        double cb2 = dq + leq + oq;
        System.out.println("CB22:::: " + cb2);
        double cb = cb1 - cb2;
        System.out.println("CB:::: " + cb);
        mHolder.mClosingBal.setText(String.format("%.3f", cb));
        selectedCBListMap.put(currentStockList.getmTripsheetStockProductId(), String.valueOf(cb));


        // LEAK
        mHolder.mLeakQtyInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Double presentQuantity = Double.parseDouble(mHolder.mLeakQty.getText().toString().trim());
                    presentQuantity++;
                    selectedLeakageProductsListHashMapTemp.put(currentStockList.getmTripsheetStockProductId(), String.valueOf(presentQuantity));
                    mHolder.mLeakQty.setText(String.format("%.3f", presentQuantity));
                    setCbVal(mHolder, currentStockList.getmTripsheetStockProductId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mHolder.mLeakQtyDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Double presentQuantity = Double.parseDouble(mHolder.mLeakQty.getText().toString().trim());
                    presentQuantity--;
                    selectedLeakageProductsListHashMapTemp.put(currentStockList.getmTripsheetStockProductId(), String.valueOf(presentQuantity));
                    mHolder.mLeakQty.setText(String.format("%.3f", presentQuantity));
                    setCbVal(mHolder, currentStockList.getmTripsheetStockProductId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mHolder.mLeakQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                try {
                    if (!hasFocus) {
                        EditText quantityEditText = (EditText) view;
                        Double presentQuantity = Double.parseDouble(quantityEditText.getText().toString().trim());
                        selectedLeakageProductsListHashMapTemp.put(currentStockList.getmTripsheetStockProductId(), String.valueOf(presentQuantity));
                        setCbVal(mHolder, currentStockList.getmTripsheetStockProductId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // OTHERS
        mHolder.mOthersQtyInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Double presentQuantity = Double.parseDouble(mHolder.mOthersQty.getText().toString().trim());
                    presentQuantity++;
                    selectedOthersProductsListHashMapTemp.put(currentStockList.getmTripsheetStockProductId(), String.valueOf(presentQuantity));
                    mHolder.mOthersQty.setText(String.format("%.3f", presentQuantity));
                    setCbVal(mHolder, currentStockList.getmTripsheetStockProductId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mHolder.mOthersQtyDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Double presentQuantity = Double.parseDouble(mHolder.mOthersQty.getText().toString().trim());
                    presentQuantity--;
                    selectedOthersProductsListHashMapTemp.put(currentStockList.getmTripsheetStockProductId(), String.valueOf(presentQuantity));
                    mHolder.mOthersQty.setText(String.format("%.3f", presentQuantity));
                    setCbVal(mHolder, currentStockList.getmTripsheetStockProductId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mHolder.mOthersQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                try {
                    if (!hasFocus) {
                        EditText quantityEditText = (EditText) view;
                        Double presentQuantity = Double.parseDouble(quantityEditText.getText().toString().trim());
                        selectedOthersProductsListHashMapTemp.put(currentStockList.getmTripsheetStockProductId(), String.valueOf(presentQuantity));
                        setCbVal(mHolder, currentStockList.getmTripsheetStockProductId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        return view;
    }

    public class ViewHolder {
        TextView mRouteCode;
        TextView mTruckQty;
        TextView mClosingBal;
        TextView mProductName;
        TextView mProductUom;
        TextView mDeliveryQty;
        TextView mReturnQty;
        // public ImageButton mReturnQtyInc;
        // public ImageButton mReturnQtyDec;
        EditText mLeakQty;
        public ImageButton mLeakQtyInc;
        public ImageButton mLeakQtyDec;
        EditText mOthersQty;
        public ImageButton mOthersQtyInc;
        public ImageButton mOthersQtyDec;
    }

    /**
     * Method to assign the cb val
     *
     * @param mHolder
     * @param pId
     */
    private void setCbVal(ViewHolder mHolder, String pId) {
        double tq = Double.parseDouble(mHolder.mTruckQty.getText().toString().trim());
        double rq = Double.parseDouble(mHolder.mReturnQty.getText().toString().trim());
        double dq = Double.parseDouble(mHolder.mDeliveryQty.getText().toString().trim());
        double leq = Double.parseDouble(mHolder.mLeakQty.getText().toString().trim());
        double oq = Double.parseDouble(mHolder.mOthersQty.getText().toString().trim());
        double cb1 = tq + rq;
        System.out.println("CB11:::: " + cb1);
        double cb2 = dq + leq + oq;
        System.out.println("CB22:::: " + cb2);
        double cb = cb1 - cb2;
        System.out.println("CB:::: " + cb);
        mHolder.mClosingBal.setText(String.format("%.3f", cb));
        selectedCBListMap.put(pId, String.valueOf(cb));
    }

}


