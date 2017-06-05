package com.rightclickit.b2bsaleon.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.rightclickit.b2bsaleon.activities.LoginActivity;
import com.rightclickit.b2bsaleon.activities.Products_Activity;
import com.rightclickit.b2bsaleon.activities.TakeOrderScreen;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.imageloading.ImageLoader;
import com.rightclickit.b2bsaleon.services.SyncTakeOrdersService;
import com.rightclickit.b2bsaleon.services.SyncUserPrivilegesService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    public TakeOrdersAdapter(TakeOrderScreen productsActivity, ArrayList<TakeOrderBean> mTakeOrderBeansList, ListView mTakeOrderListView) {
        this.activity = productsActivity;
        this.mTakeOrderBeansList1 = mTakeOrderBeansList;
        this.mInflater = LayoutInflater.from(activity);
        this.mDBHelper = new DBHelper(activity);
        this.mPreferences = new MMSharedPreferences(activity);
        this.mList = mTakeOrderListView;
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            currentDate = df.format(cal.getTime());
            fromDStr = currentDate;
        }catch (Exception e){
            e.printStackTrace();
        }
        list1.add("One Time");
        list1.add("Daily");
        list1.add("Weekly");
        list1.add("Monthly");
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
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }

        holder.productName.setText(mTakeOrderBeansList1.get(position).getmProductTitle());

        if(mTakeOrderBeansList1.get(position).getmProductFromDate()!=null){
            if(mTakeOrderBeansList1.get(position).getmProductFromDate().length()>0){
                holder.fromDate.setText(mTakeOrderBeansList1.get(position).getmProductFromDate());
            }else {
                holder.fromDate.setText(currentDate);
            }
        }else {
            holder.fromDate.setText(currentDate);
        }

        if(mTakeOrderBeansList1.get(position).getmProductToDate()!=null){
            if(mTakeOrderBeansList1.get(position).getmProductToDate().length()>0){
                holder.toDate.setText(mTakeOrderBeansList1.get(position).getmProductToDate());
            }else {
                holder.toDate.setText(currentDate);
            }
        }else {
            holder.toDate.setText(currentDate);
        }

        if(mTakeOrderBeansList1.get(position).getmProductQuantity()!=null){
            if(mTakeOrderBeansList1.get(position).getmProductQuantity().length()>0){
                holder.productQuantity.setText(mTakeOrderBeansList1.get(position).getmProductQuantity());
            }else {
                holder.productQuantity.setText("0000.000");
            }
        }else {
            holder.productQuantity.setText("0000.000");
        }

        ArrayAdapter<String> dataAdapter =new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item,list1);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.orderTypeSpinner.setAdapter(dataAdapter);
        holder.orderTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String materialOrderType = holder.orderTypeSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        if(mTakeOrderBeansList1.get(position).getmProductOrderType()!=null){
            if(mTakeOrderBeansList1.get(position).getmProductOrderType().length()>0){
                if(mTakeOrderBeansList1.get(position).getmProductOrderType().equals("1")){
                    holder.orderTypeSpinner.setSelection(0);
                }else if(mTakeOrderBeansList1.get(position).getmProductOrderType().equals("2")){
                    holder.orderTypeSpinner.setSelection(1);
                }else if(mTakeOrderBeansList1.get(position).getmProductOrderType().equals("3")){
                    holder.orderTypeSpinner.setSelection(2);
                }else if(mTakeOrderBeansList1.get(position).getmProductOrderType().equals("4")){
                    holder.orderTypeSpinner.setSelection(3);
                }else {
                    holder.orderTypeSpinner.setSelection(0);
                }
            }else {
                holder.orderTypeSpinner.setSelection(0);
            }
        }else {
            holder.orderTypeSpinner.setSelection(0);
        }

        holder.fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View childView = mList.getChildAt(position);
                EditText fromDate = (EditText) childView.findViewById(R.id.from_date);
                et = fromDate;
                fromStr = "from";
                datePickerMethod();
            }
        });

        holder.toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View childView = mList.getChildAt(position);
                EditText toDate = (EditText) childView.findViewById(R.id.to_date);
                et = toDate;
                fromStr = "to";
                datePickerMethod();
            }
        });

        holder.productQuantityIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    View childView = mList.getChildAt(position);
                    EditText quanity11 = (EditText) childView.findViewById(R.id.productQt);
                    String presentValStr = quanity11.getText().toString();
                    Double presentIntVal = Double.parseDouble(presentValStr);
                    presentIntVal++;
                    quanity11.setText(String.format("%.3f",presentIntVal));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.productQuantityDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    View childView = mList.getChildAt(position);
                    EditText quanity11 = (EditText) childView.findViewById(R.id.productQt);
                    String presentValStr = quanity11.getText().toString();
                    Double presentIntVal = Double.parseDouble(presentValStr);
                    if(presentIntVal>0){
                        presentIntVal--;
                        quanity11.setText(String.format("%.3f",presentIntVal));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        TakeOrderScreen.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(temptoList.size()>0){
                    temptoList.clear();
                }
                System.out.println("LIST COUNT::: "+ mList.getCount());
                System.out.println("LIST COUNT::: "+ mList.getLastVisiblePosition());
                for (int d = 0; d< mList.getLastVisiblePosition();d++){
                    try {
                    View childView = mList.getChildAt(d);
                    TextView tv = (TextView) childView.findViewById(R.id.productName);
                    EditText fromDate = (EditText) childView.findViewById(R.id.from_date);
                    EditText toDate = (EditText) childView.findViewById(R.id.to_date);
                    Spinner sp = (Spinner) childView.findViewById(R.id.spinner1);
                    EditText quanity = (EditText) childView.findViewById(R.id.productQt);
                    System.out.println("TITLE IS ::: "+ tv.getText().toString().trim());
                    System.out.println("FROM IS ::: "+ fromDate.getText().toString().trim());
                    System.out.println("TO IS ::: "+ toDate.getText().toString().trim());
                    System.out.println("SPINNER IS ::: "+ sp.getSelectedItem().toString());
                    System.out.println("QUANTITY IS ::: "+ quanity.getText().toString().trim());
                    System.out.println("PROD ID IS ::: "+ mTakeOrderBeansList1.get(d).getmProductId());

                    TakeOrderBean tb = new TakeOrderBean();

                    tb.setmRouteId(mPreferences.getString("routeId"));
                    tb.setmProductId(mTakeOrderBeansList1.get(d).getmProductId());
                    tb.setmProductTitle(tv.getText().toString().trim());
                    tb.setmProductFromDate(fromDate.getText().toString().trim());
                    tb.setmProductToDate(toDate.getText().toString().trim());
                    if(sp.getSelectedItem().toString().equals("One Time")){
                        tb.setmProductOrderType("1");
                    }else if(sp.getSelectedItem().toString().equals("Daily")){
                        tb.setmProductOrderType("2");
                    }else if(sp.getSelectedItem().toString().equals("Weekly")){
                        tb.setmProductOrderType("3");
                    }else if(sp.getSelectedItem().toString().equals("Monthly")){
                        tb.setmProductOrderType("4");
                    }else {
                        tb.setmProductOrderType("1");
                    }
                    tb.setmProductQuantity(quanity.getText().toString().trim());

                    temptoList.add(tb);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                synchronized (this) {
                    if (temptoList.size() > 0) {
                        System.out.println("DB called");
                        mDBHelper.updateTakeOrderDetails(temptoList);
                    }
                }

                // Temporary call api from here....
                synchronized (this) {
                    System.out.println("SERVICE called");
                    if (new NetworkConnectionDetector(activity).isNetworkConnected()) {
                        activity.startService(new Intent(activity, SyncTakeOrdersService.class));
                    }
                }
            }
        });

        return convertView;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = changeDateToString(year, month + 1, dayOfMonth);
        //   textview.setError(null);
        Log.e("date dfd",date);
        if(fromStr.equals("from")){
            fromDStr = date;
            et.setText(date);
        }
        else if(fromStr.equals("to")){
            et.setText(date);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date pickerdate = null;
            Date systemdate  = null;
            try{
                pickerdate = formatter.parse(et.getText().toString().trim());
                Log.e("pick",pickerdate+"");
                systemdate = formatter.parse(fromDStr);
            }catch (ParseException e){
                e.printStackTrace();
            }
            if(pickerdate.after(systemdate)){  //greater 0

            }else if(pickerdate.before(systemdate)){
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
            }else if(systemdate.equals(pickerdate)){ //equal

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

    private void datePickerMethod(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(
                activity,this,
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
}