package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.activities.AgentTakeOrderPreview;
import com.rightclickit.b2bsaleon.activities.AgentsInfoActivity;
import com.rightclickit.b2bsaleon.activities.DashboardTakeorderPreview;
import com.rightclickit.b2bsaleon.activities.LoginActivity;
import com.rightclickit.b2bsaleon.activities.Products_Activity;
import com.rightclickit.b2bsaleon.activities.AgentTakeOrderScreen;
import com.rightclickit.b2bsaleon.beanclass.AgentsBean;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderBean;
import com.rightclickit.b2bsaleon.customviews.CustomAlertDialog;
import com.rightclickit.b2bsaleon.customviews.CustomProgressDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.services.SyncTakeOrdersService;
import com.rightclickit.b2bsaleon.services.SyncUserPrivilegesService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sekhar Kuppa
 */

public class TakeOrdersAdapter extends BaseAdapter implements DatePickerDialog.OnDateSetListener {

    LayoutInflater mInflater;
    private Activity activity;
    ArrayList<TakeOrderBean> mTakeOrderBeansList1;
    ArrayList<TakeOrderBean> temptoList = new ArrayList<TakeOrderBean>();
    private String currentDate;
    private String fromStr;
    private String fromDStr;
    private EditText et;
    private MyViewHolder holder;
    List<String> list1 = new ArrayList<String>();
    private DBHelper mDBHelper;
    private MMSharedPreferences mPreferences;
    private ListView mList;
    private ArrayList<TakeOrderBean> arraylist;
    private HashMap<String, String> producttitle = new HashMap<String, String>();
    private HashMap<String, String> quantityList = new HashMap<String, String>();
    private HashMap<String, String> fromDatesList = new HashMap<String, String>();
    private HashMap<String, String> toDatesList = new HashMap<String, String>();
    private HashMap<String, String> ordertypesList = new HashMap<String, String>();
    private int clickedPosition;
    private HashMap<String, String> mProductIdsList = new HashMap<String, String>();

    public TakeOrdersAdapter(Activity productsActivity, ArrayList<TakeOrderBean> mTakeOrderBeansList, ListView mTakeOrderListView) {
        this.activity = productsActivity;
        this.mTakeOrderBeansList1 = mTakeOrderBeansList;
        this.mInflater = LayoutInflater.from(activity);
        this.mDBHelper = new DBHelper(activity);
        this.mPreferences = new MMSharedPreferences(activity);
        this.mList = mTakeOrderListView;
        this.arraylist = new ArrayList<TakeOrderBean>();
        this.arraylist.addAll(mTakeOrderBeansList1);
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            currentDate = df.format(cal.getTime());
            fromDStr = currentDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        list1.add("One Time");
        list1.add("Daily");
        list1.add("Weekly");
        list1.add("Monthly");
        if (quantityList.size() > 0) {
            quantityList.clear();
        }
        if (fromDatesList.size() > 0) {
            fromDatesList.clear();
        }
        if (toDatesList.size() > 0) {
            toDatesList.clear();
        }
        if (ordertypesList.size() > 0) {
            ordertypesList.clear();
        }
    }


    @Override
    public int getCount() {
        return mTakeOrderBeansList1.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            holder = new MyViewHolder();
            convertView = mInflater.inflate(R.layout.customer_take_order_customlayout, null);

            holder.productName = (TextView) convertView.findViewById(R.id.productName);
            holder.fromDate = (EditText) convertView.findViewById(R.id.from_date);
            holder.toDate = (EditText) convertView.findViewById(R.id.to_date);
            holder.orderTypeSpinner = (Spinner) convertView.findViewById(R.id.spinner1);
            holder.productQuantity = (EditText) convertView.findViewById(R.id.productQt);
            holder.productQuantityIncrement = (ImageButton) convertView.findViewById(R.id.productQtInc);
            holder.productQuantityDecrement = (ImageButton) convertView.findViewById(R.id.productQtDec);

            convertView.setTag(holder);
            /*Intent intent=new Intent(activity,DashboardTakeorderPreview.class);
            activity.startActivity(intent);
            activity.finish();*/
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        //   holder.productName.setText(mTakeOrderBeansList1.get(position).getmProductTitle());

        if (producttitle.get(mTakeOrderBeansList1.get(position).getmProductTitle()) != null) {
            // Assign changed from date value.
            String fromD = producttitle.get(mTakeOrderBeansList1.get(position).getmProductTitle());
            holder.productName.setText(fromD);
        } else if (mTakeOrderBeansList1.get(position).getmProductTitle() != null) {
            if (mTakeOrderBeansList1.get(position).getmProductTitle().length() > 0) {
                holder.productName.setText(mTakeOrderBeansList1.get(position).getmProductTitle());
            } else {
                holder.productName.setText(mTakeOrderBeansList1.get(position).getmProductTitle());
            }
        } else {
            holder.productName.setText(mTakeOrderBeansList1.get(position).getmProductTitle());
        }

        if (fromDatesList.get(mTakeOrderBeansList1.get(position).getmProductId()) != null) {
            // Assign changed from date value.
            String fromD = fromDatesList.get(mTakeOrderBeansList1.get(position).getmProductId());
            holder.fromDate.setText(fromD);
        } else if (mTakeOrderBeansList1.get(position).getmProductFromDate() != null) {
            if (mTakeOrderBeansList1.get(position).getmProductFromDate().length() > 0) {
                holder.fromDate.setText(mTakeOrderBeansList1.get(position).getmProductFromDate());
            } else {
                holder.fromDate.setText(currentDate);
            }
        } else {
            holder.fromDate.setText(currentDate);
        }

        if (toDatesList.get(mTakeOrderBeansList1.get(position).getmProductId()) != null) {
            // Assign changed to date value.
            String toD = toDatesList.get(mTakeOrderBeansList1.get(position).getmProductId());
            holder.toDate.setText(toD);
        } else if (mTakeOrderBeansList1.get(position).getmProductToDate() != null) {
            if (mTakeOrderBeansList1.get(position).getmProductToDate().length() > 0) {
                holder.toDate.setText(mTakeOrderBeansList1.get(position).getmProductToDate());
            } else {
                holder.toDate.setText(currentDate);
            }
        } else {
            holder.toDate.setText(currentDate);
        }

        if (quantityList.get(mTakeOrderBeansList1.get(position).getmProductId()) != null) {
            // Assign changed quantity value
            String val = quantityList.get(mTakeOrderBeansList1.get(position).getmProductId());
            holder.productQuantity.setText(val);
        } else if (mTakeOrderBeansList1.get(position).getmProductQuantity() != null) {
            if (mTakeOrderBeansList1.get(position).getmProductQuantity().length() > 0) {
                // Assign stored db quantity value
                holder.productQuantity.setText(mTakeOrderBeansList1.get(position).getmProductQuantity());
            } else {
                // Assign default quantity value
                holder.productQuantity.setText("0.000");
            }
        } else {
            // Assign default quantity value
            holder.productQuantity.setText("0.000");
        }

        holder.fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedPosition = position;
                View childView = mList.getChildAt(position - mList.getFirstVisiblePosition());
                EditText fromDate = (EditText) childView.findViewById(R.id.from_date);
                EditText quanity11 = (EditText) childView.findViewById(R.id.productQt);
                TextView prodName = (TextView) childView.findViewById(R.id.productName);
                EditText toDate = (EditText) childView.findViewById(R.id.to_date);
                String presentValStr = quanity11.getText().toString();
                Double presentIntVal = Double.parseDouble(presentValStr);
                quantityList.put(mTakeOrderBeansList1.get(position).getmProductId(), String.format("%.3f", presentIntVal));
                producttitle.put(mTakeOrderBeansList1.get(position).getmProductId(), prodName.getText().toString().trim());
                toDatesList.put(mTakeOrderBeansList1.get(position).getmProductId(), toDate.getText().toString().trim());
                mProductIdsList.put(mTakeOrderBeansList1.get(position).getmProductId().toString(), mTakeOrderBeansList1.get(position).getmProductId().toString());
                et = fromDate;
                fromStr = "from";
                datePickerMethod();
            }
        });

        holder.toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedPosition = position;
                View childView = mList.getChildAt(position - mList.getFirstVisiblePosition());
                EditText toDate = (EditText) childView.findViewById(R.id.to_date);
                EditText quanity11 = (EditText) childView.findViewById(R.id.productQt);
                TextView prodName = (TextView) childView.findViewById(R.id.productName);
                EditText fromDate = (EditText) childView.findViewById(R.id.from_date);
                String presentValStr = quanity11.getText().toString();
                Double presentIntVal = Double.parseDouble(presentValStr);
                quantityList.put(mTakeOrderBeansList1.get(position).getmProductId(), String.format("%.3f", presentIntVal));
                producttitle.put(mTakeOrderBeansList1.get(position).getmProductId(), prodName.getText().toString().trim());
                fromDatesList.put(mTakeOrderBeansList1.get(position).getmProductId(), fromDate.getText().toString().trim());
                mProductIdsList.put(mTakeOrderBeansList1.get(position).getmProductId().toString(), mTakeOrderBeansList1.get(position).getmProductId().toString());
                et = toDate;
                fromStr = "to";
                datePickerMethod();
            }
        });

        holder.productQuantityIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Toast.makeText(activity, "POs Is::: "+ position, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(activity, "POs 1 Is::: "+ mList.getFirstVisiblePosition(), Toast.LENGTH_SHORT).show();
                    View childView = mList.getChildAt(position - mList.getFirstVisiblePosition());
                    //Toast.makeText(activity, "POs Child View Is ::: "+ childView, Toast.LENGTH_SHORT).show();
                    EditText quanity11 = (EditText) childView.findViewById(R.id.productQt);
                    TextView prodName = (TextView) childView.findViewById(R.id.productName);
                    EditText fromDate = (EditText) childView.findViewById(R.id.from_date);
                    EditText toDate = (EditText) childView.findViewById(R.id.to_date);
                    String presentValStr = quanity11.getText().toString();
                    Double presentIntVal = Double.parseDouble(presentValStr);
                    presentIntVal++;
                    quanity11.setText(String.format("%.3f", presentIntVal));
                    quantityList.put(mTakeOrderBeansList1.get(position).getmProductId(), String.format("%.3f", presentIntVal));
                    producttitle.put(mTakeOrderBeansList1.get(position).getmProductId(), prodName.getText().toString().trim());
                    fromDatesList.put(mTakeOrderBeansList1.get(position).getmProductId(), fromDate.getText().toString().trim());
                    toDatesList.put(mTakeOrderBeansList1.get(position).getmProductId(), toDate.getText().toString().trim());
                    mProductIdsList.put(mTakeOrderBeansList1.get(position).getmProductId().toString(), mTakeOrderBeansList1.get(position).getmProductId().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.productQuantityDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    View childView = mList.getChildAt(position - mList.getFirstVisiblePosition());
                    EditText quanity11 = (EditText) childView.findViewById(R.id.productQt);
                    TextView prodName = (TextView) childView.findViewById(R.id.productName);
                    EditText fromDate = (EditText) childView.findViewById(R.id.from_date);
                    EditText toDate = (EditText) childView.findViewById(R.id.to_date);
                    String presentValStr = quanity11.getText().toString();
                    Double presentIntVal = Double.parseDouble(presentValStr);
                    if (presentIntVal > 0) {
                        presentIntVal--;
                        quanity11.setText(String.format("%.3f", presentIntVal));
                    }
                    quantityList.put(mTakeOrderBeansList1.get(position).getmProductId(), String.format("%.3f", presentIntVal));
                    producttitle.put(mTakeOrderBeansList1.get(position).getmProductId(), prodName.getText().toString().trim());
                    fromDatesList.put(mTakeOrderBeansList1.get(position).getmProductId(), fromDate.getText().toString().trim());
                    toDatesList.put(mTakeOrderBeansList1.get(position).getmProductId(), toDate.getText().toString().trim());
                    mProductIdsList.put(mTakeOrderBeansList1.get(position).getmProductId().toString(), mTakeOrderBeansList1.get(position).getmProductId().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        AgentTakeOrderScreen.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (producttitle.size() == 0 &&
                        fromDatesList.size() == 0 &&
                        toDatesList.size() == 0 &&
                        quantityList.size() == 0) {
                    showAlertDialogTakeorder(activity, "Alert!", "No orders placed.");
                } else {
                    String productId = "", productFD = "", productTD = "", productOrType = "", productQua = "", productTitle = "";
                    if (temptoList.size() > 0) {
                        temptoList.clear();
                    }
                    System.out.println("PIDS LIST SIZE IS:: " + mProductIdsList.size());
                    System.out.println("FROM D LIST SIZE IS:: " + fromDatesList.size());
                    System.out.println("TO D LIST SIZE IS:: " + toDatesList.size());
                    System.out.println("QUAN LIST SIZE IS:: " + quantityList.size());
                    for (int k = 0; k < mTakeOrderBeansList1.size(); k++) {
                        TakeOrderBean tb = new TakeOrderBean();
//
//                        productId = mTakeOrderBeansList1.get(k).getmProductId();
//                        productTitle = producttitle.get(mTakeOrderBeansList1.get(k).getmProductId());
//                        System.out.println("If ******  PRODUCT TITLE  **** VAL IS:::: " + mTakeOrderBeansList1.get(k).getmProductId());
//                        tb.setmProductId(productId);
//                        tb.setmRouteId(mTakeOrderBeansList1.get(k).getmRouteId());
//                        tb.setmProductTitle(productTitle);
//                        tb.setmProductStatus("1");
                        if (mProductIdsList.get(mTakeOrderBeansList1.get(k).getmProductId()) != null) {
                            System.out.println("P ID: " + mProductIdsList.get(mTakeOrderBeansList1.get(k).getmProductId()));
                            System.out.println("FR ID: " + fromDatesList.get(mTakeOrderBeansList1.get(k).getmProductId()));
                            System.out.println("TO ID: " + toDatesList.get(mTakeOrderBeansList1.get(k).getmProductId()));
                            System.out.println("QU ID: " + quantityList.get(mTakeOrderBeansList1.get(k).getmProductId()));
                            tb.setmProductId(mTakeOrderBeansList1.get(k).getmProductId());
                            tb.setmRouteId(mTakeOrderBeansList1.get(k).getmRouteId());
                            tb.setmProductTitle(mTakeOrderBeansList1.get(k).getmProductTitle());
                            tb.setmProductToDate(toDatesList.get(mTakeOrderBeansList1.get(k).getmProductId()));
                            tb.setmProductFromDate(fromDatesList.get(mTakeOrderBeansList1.get(k).getmProductId()));
                            tb.setmProductQuantity(quantityList.get(mTakeOrderBeansList1.get(k).getmProductId()));
                            tb.setmProductStatus("1");
                            tb.setmEnquiryId(mPreferences.getString("enqId"));

                            temptoList.add(tb);
                        }
                    }

                    synchronized (this) {
                        if (temptoList.size() > 0) {
                            System.out.println("DB called****" + temptoList.size());
                            long upd = mDBHelper.updateTakeOrderDetails(temptoList);
                            System.out.println("UPDATED VALUES COUNT:: " + upd);
                        }
                    }

                    // Temporary call api from here....
                    synchronized (this) {
                        if (temptoList.size() > 0) {
                            System.out.println("SERVICE called");
                            if (new NetworkConnectionDetector(activity).isNetworkConnected()) {
                                activity.startService(new Intent(activity, SyncTakeOrdersService.class));
                            }
                        }
                    }
                    synchronized (this) {
                        showAlertDialogTakeorder(activity, "Success", activity.getString(R.string.order));
                    }


//                    for (int k = 0; k < mTakeOrderBeansList1.size(); k++) {
//
//                        TakeOrderBean tb = new TakeOrderBean();
//                        if (producttitle.get(mTakeOrderBeansList1.get(k).getmProductId()) != null) {
//                            productId = mTakeOrderBeansList1.get(k).getmProductId();
//                            productTitle = producttitle.get(mTakeOrderBeansList1.get(k).getmProductId());
//                            System.out.println("If ******  PRODUCT TITLE  **** VAL IS:::: " + mTakeOrderBeansList1.get(k).getmProductId());
//                            tb.setmProductId(productId);
//                            tb.setmRouteId(mTakeOrderBeansList1.get(k).getmRouteId());
//                            tb.setmProductTitle(productTitle);
//                            tb.setmProductStatus("1");
//                            mProductIdsList.add(productId);
//                        } else {
//                            tb.setmProductTitle(mTakeOrderBeansList1.get(k).getmProductTitle());
//                        }
//                        if (fromDatesList.get(mTakeOrderBeansList1.get(k).getmProductId()) != null) {
//                            productId = mTakeOrderBeansList1.get(k).getmProductId();
//                            productFD = fromDatesList.get(mTakeOrderBeansList1.get(k).getmProductId());
//                            System.out.println("If ******  FROM DATE  **** VAL IS:::: " + mTakeOrderBeansList1.get(k).getmProductId());
//                            tb.setmProductId(productId);
//                            tb.setmRouteId(mTakeOrderBeansList1.get(k).getmRouteId());
//                            tb.setmProductFromDate(productFD);
//                            tb.setmProductStatus("1");
//                            mProductIdsList.add(productId);
//                        } else {
//                            tb.setmProductFromDate(currentDate);
//                        }
//                        if (toDatesList.get(mTakeOrderBeansList1.get(k).getmProductId()) != null) {
//                            productId = mTakeOrderBeansList1.get(k).getmProductId();
//                            productTD = toDatesList.get(mTakeOrderBeansList1.get(k).getmProductId());
//                            System.out.println("If ******  TO DATE  **** VAL IS:::: " + mTakeOrderBeansList1.get(k).getmProductId());
//                            tb.setmProductId(productId);
//                            tb.setmRouteId(mTakeOrderBeansList1.get(k).getmRouteId());
//                            tb.setmProductToDate(productTD);
//                            tb.setmProductStatus("1");
//                            mProductIdsList.add(productId);
//                        } else {
//                            tb.setmProductToDate(currentDate);
//                        }
//
//                    /*if(ordertypesList.get(mTakeOrderBeansList1.get(k).getmProductId())!= null){
//                        productId = mTakeOrderBeansList1.get(k).getmProductId();
//                        productOrType = ordertypesList.get(mTakeOrderBeansList1.get(k).getmProductId());
//                        System.out.println("If ******  ORDER TYPE  **** VAL IS:::: "+ mTakeOrderBeansList1.get(k).getmProductId());
//                        tb.setmProductId(productId);
//                        tb.setmRouteId(mPreferences.getString("routeId"));
//                        tb.setmProductFromDate(productFD);
//                        tb.setmProductToDate(productTD);
//                        tb.setmProductOrderType("");
//                    }*/
//                        if (quantityList.get(mTakeOrderBeansList1.get(k).getmProductId()) != null) {
//                            productId = mTakeOrderBeansList1.get(k).getmProductId();
//                            productQua = quantityList.get(mTakeOrderBeansList1.get(k).getmProductId());
//                            System.out.println("If ******  QUANTITY **** VAL IS:::: " + mTakeOrderBeansList1.get(k).getmProductId());
//                            tb.setmProductId(productId);
//                            tb.setmRouteId(mTakeOrderBeansList1.get(k).getmRouteId());
//                            tb.setmProductQuantity(productQua);
//                            tb.setmProductStatus("1");
//                            mProductIdsList.add(productId);
//                        } else {
//                            tb.setmProductQuantity("0.000");
//                        }
////                    if(productOrType.equals("One Time")){
////                        tb.setmProductOrderType("1");
////                    }else if(productOrType.equals("Daily")){
////                        tb.setmProductOrderType("2");
////                    }else if(productOrType.equals("Weekly")){
////                        tb.setmProductOrderType("3");
////                    }else if(productOrType.equals("Monthly")){
////                        tb.setmProductOrderType("4");
////                    }else {
////                        tb.setmProductOrderType("1");
////                    }
//
//
//                        temptoList.add(tb);
//                    }

//                    synchronized (this) {
//                        if (temptoList.size() > 0) {
//                            System.out.println("DB called****" + temptoList.size());
//                            long upd = mDBHelper.updateTakeOrderDetails(temptoList);
//                            System.out.println("UPDATED VALUES COUNT:: " + upd);
//                        }
//                    }
//
//                    // Temporary call api from here....
//                    synchronized (this) {
//                        if (temptoList.size() > 0) {
//                            System.out.println("SERVICE called");
//                            if (new NetworkConnectionDetector(activity).isNetworkConnected()) {
//                                activity.startService(new Intent(activity, SyncTakeOrdersService.class));
//                            }
//                        }
//                    }
//                    synchronized (this) {
//                        showAlertDialogTakeorder(activity, "Success", activity.getString(R.string.order));
//                    }
                }
            }


        });

        return convertView;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = changeDateToString(year, month + 1, dayOfMonth);
        //   textview.setError(null);
        Log.e("date dfd", date);
        if (fromStr.equals("from")) {
            fromDStr = date;
            et.setText(date);
            fromDatesList.put(mTakeOrderBeansList1.get(clickedPosition).getmProductId(), date);
        } else if (fromStr.equals("to")) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date pickerdate = null;
            Date systemdate = null;
            try {
                pickerdate = formatter.parse(date);
                Log.e("pick", pickerdate + "");
                systemdate = formatter.parse(fromDStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (pickerdate.after(systemdate)) {  //greater 0
                et.setText(date);
                toDatesList.put(mTakeOrderBeansList1.get(clickedPosition).getmProductId(), date);
            } else if (pickerdate.before(systemdate)) {
                new AlertDialog.Builder(activity)
                        .setTitle("Alert!")
                        .setMessage("To date should be greater than From date")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                dialog.dismiss();
                                //showDialog(DATE_DIALOG_ID);

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();//less0
                //  Toast.makeText(context,"T must be greater than from",Toast.LENGTH_LONG).show();
                //alert less date
            } else if (systemdate.equals(pickerdate)) { //equal
                et.setText(date);
                toDatesList.put(mTakeOrderBeansList1.get(clickedPosition).getmProductId(), date);
            }
        }
    }

    private class MyViewHolder {
        public TextView productName;
        public EditText fromDate;
        public EditText toDate;
        public Spinner orderTypeSpinner;
        public EditText productQuantity;
        public ImageButton productQuantityIncrement;
        public ImageButton productQuantityDecrement;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mTakeOrderBeansList1.clear();
        if (charText.length() == 0) {
            mTakeOrderBeansList1.addAll(arraylist);
        } else {
            for (TakeOrderBean wp : arraylist) {
                if (wp.getmProductTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mTakeOrderBeansList1.add(wp);
                }

            }
        }
        notifyDataSetChanged();
    }

    private void datePickerMethod() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(
                activity, this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.getDatePicker().setMinDate(now.getTimeInMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 365);
//        int yellow = Color.parseColor("#dfba69");
//        dpd.getDatePicker().setAccentColor(yellow);
        dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        dpd.show();
    }

    public String changeDateToString(int year, int month, int day) {
        String fullDate;
        String newMonth;
        String newDay;
        if (day < 10) {
            newDay = "0" + day;
        } else {
            newDay = String.valueOf(day);
        }
        if (month < 10) {
            newMonth = "0" + month;
        } else {
            newMonth = String.valueOf(month);
        }
        fullDate = newDay + "/" + newMonth + "/" + year;

        return fullDate;
    }

    /**
     * Method to show alert dialog.
     *
     * @param context
     * @param title
     * @param message
     */
    public void showAlertDialogTakeorder(final Context context, String title, final String message) {
        try {
            // AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);

            android.support.v7.app.AlertDialog alertDialog = null;
            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    CustomProgressDialog.hideProgressDialog();
                    if (message.equals("Order placed Successfully.")) {
                        Intent ii = new Intent(activity, AgentTakeOrderPreview.class);
                        Bundle args = new Bundle();
                        args.putSerializable("productIdsList", (Serializable) temptoList);
                        ii.putExtra("BUNDLE", args);
                        activity.startActivity(ii);
                        activity.finish();
                    } else {

                    }

                }
            });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}