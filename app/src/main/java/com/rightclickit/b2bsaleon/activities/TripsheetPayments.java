package com.rightclickit.b2bsaleon.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.beanclass.PaymentsBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetDeliveriesBean;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.services.SyncTripSheetsPaymentsService;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TripsheetPayments extends AppCompatActivity {

    LinearLayout ret;
    LinearLayout payments;
    LinearLayout save;
    LinearLayout print;
    LinearLayout delivery;
    LinearLayout cheqLinearLayout, captureChequeLayout;
    Spinner paymentTypeSpinner;
    ArrayList paymentsList = new ArrayList();
    EditText mAmountText, mChequeNumber, mBankName, mChequeDate;
    ImageView imageview;
    DBHelper mDBHelper;
    ArrayList<TripSheetDeliveriesBean> deliveryArrayList;
    String mTripSheetId = "", mAgentId = "", mAgentCode = "", mAgentName = "", currentDate = "", mAgentRouteId = "", mAgentRouteCode = "", mAgentSoId = "", mAgentSoCode = "";
    private static final int ACTION_TAKE_PHOTO_A = 1;
    private TripSheetDeliveriesBean currentDeliveriesBean = null;
    private double receivedAmount = 0.0, remainingAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripsheet_payments);
        mDBHelper = new DBHelper(TripsheetPayments.this);

        mTripSheetId = this.getIntent().getStringExtra("tripsheetId");
        mAgentId = this.getIntent().getStringExtra("agentId");
        mAgentCode = this.getIntent().getStringExtra("agentCode");
        mAgentName = this.getIntent().getStringExtra("agentName");
        mAgentRouteId = this.getIntent().getStringExtra("agentRouteId");
        mAgentRouteCode = this.getIntent().getStringExtra("agentRouteCode");
        mAgentSoId = this.getIntent().getStringExtra("agentSoId");
        mAgentSoCode = this.getIntent().getStringExtra("agentSoCode");
        Log.i("fdgjhujgf", mAgentSoCode);

        this.getSupportActionBar().setTitle("PAYMENTS");
        this.getSupportActionBar().setSubtitle(null);
        this.getSupportActionBar().setLogo(R.drawable.route_white);
        // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        TextView agentName = (TextView) findViewById(R.id.companyName);
        agentName.setText(mAgentName);


        ret = (LinearLayout) findViewById(R.id.rlinear);
        payments = (LinearLayout) findViewById(R.id.plinear);
        save = (LinearLayout) findViewById(R.id.slinear);
        print = (LinearLayout) findViewById(R.id.prelinear);
        delivery = (LinearLayout) findViewById(R.id.dlinear);
        mAmountText = (EditText) findViewById(R.id.amount);
        cheqLinearLayout = (LinearLayout) findViewById(R.id.chequeLinearLayout);
        paymentTypeSpinner = (Spinner) findViewById(R.id.paymentTypeSpinner);
        captureChequeLayout = (LinearLayout) findViewById(R.id.CaptureChequeLayout);
        imageview = (ImageView) findViewById(R.id.image);
        mChequeNumber = (EditText) findViewById(R.id.chequeNo);
        mBankName = (EditText) findViewById(R.id.bankName);
        mChequeDate = (EditText) findViewById(R.id.date);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        currentDate = df.format(cal.getTime());
        mChequeDate.setText(currentDate);
//        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
//        currentDate = df1.format(cal.getTime());

        captureChequeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        paymentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (paymentTypeSpinner.getSelectedItem().toString().equals("Cash")) {
                    cheqLinearLayout.setVisibility(View.GONE);
                } else if (paymentTypeSpinner.getSelectedItem().toString().equals("Cheque")) {
                    cheqLinearLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TripsheetPayments.this, TripsheetReturns.class);
                i.putExtra("tripsheetId", mTripSheetId);
                i.putExtra("agentId", mAgentId);
                i.putExtra("agentCode", mAgentCode);
                i.putExtra("agentName", mAgentName);
                i.putExtra("agentRouteId", mAgentRouteId);
                i.putExtra("agentRouteCode", mAgentRouteCode);
                i.putExtra("agentSoId", mAgentSoId);
                i.putExtra("agentSoCode", mAgentSoCode);
                startActivity(i);

            }
        });
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TripsheetPayments.this, TripsheetDelivery.class);
                i.putExtra("tripsheetId", mTripSheetId);
                i.putExtra("agentId", mAgentId);
                i.putExtra("agentCode", mAgentCode);
                i.putExtra("agentName", mAgentName);
                i.putExtra("agentSoId", mAgentSoId);
                i.putExtra("agentSoCode", mAgentSoCode);
                startActivity(i);

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialogWithCancelButton(TripsheetPayments.this, "User Action!", "Do you want to save data?");
            }
        });
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TripsheetPayments.this, TripsheetPaymentsPreview.class);
                i.putExtra("tripsheetId", mTripSheetId);
                i.putExtra("agentId", mAgentId);
                i.putExtra("agentCode", mAgentCode);
                i.putExtra("agentName", mAgentName);
                i.putExtra("agentRouteId", mAgentRouteId);
                i.putExtra("agentRouteCode", mAgentRouteCode);
                i.putExtra("agentSoId", mAgentSoId);
                i.putExtra("agentSoCode", mAgentSoCode);
                startActivity(i);
            }
        });

        deliveryArrayList = mDBHelper.fetchOrderPaymentFromDeliveriesTable(mTripSheetId, mAgentSoId, mAgentId);

        if (deliveryArrayList.size() > 0) {
            currentDeliveriesBean = deliveryArrayList.get(0);
        } else {
            currentDeliveriesBean = new TripSheetDeliveriesBean();
            currentDeliveriesBean.setmTripsheetDelivery_SaleValue("0.0");
            currentDeliveriesBean.setmTripsheetDelivery_TaxTotal("0.0");
        }

        receivedAmount = mDBHelper.fetchTripSheetSaleOrderReceivedAmount(mTripSheetId, mAgentSoId);
        remainingAmount = Double.parseDouble(currentDeliveriesBean.getmTripsheetDelivery_SaleValue()) - receivedAmount;

        if (remainingAmount > 0)
            mAmountText.setText(String.format("%.2f", remainingAmount));
        else
            mAmountText.setText("0.00");
        //mAmountText.setText(currentDeliveriesBean.getmTripsheetDelivery_SaleValue());
    }

    private void showAlertDialogWithCancelButton(Context context, String title, String message) {
        try {
            AlertDialog alertDialog = null;
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(false);


            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    savePaymentDetails();
                }
            });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();


            Button cancelButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (cancelButton != null)
                cancelButton.setTextColor(ContextCompat.getColor(context, R.color.alert_dialog_color_accent));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Add) {
            Intent i = new Intent(TripsheetPayments.this, TDCSalesListActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return true;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.notifications).setVisible(false);
        menu.findItem(R.id.settings).setVisible(false);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(true);
        menu.findItem(R.id.Add).setVisible(false);
        menu.findItem(R.id.autorenew).setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, TripSheetView.class);
        intent.putExtra("tripsheetId", mTripSheetId);
        startActivity(intent);
        finish();
    }

    private void selectImage() {

        final CharSequence[] options = {"Capture Cheque", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(TripsheetPayments.this);
        builder.setTitle("Capture Cheque!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Capture Cheque")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, ACTION_TAKE_PHOTO_A);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ACTION_TAKE_PHOTO_A) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    BitmapDrawable d = new BitmapDrawable(this.getResources(), bitmap);
                    imageview.setBackgroundDrawable(null);
                    imageview.setBackgroundDrawable(d);

                    //  mPicImage.setImageBitmap(bitmap);

                    String path = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Method to form API Data
     * Type: 0 is for Cash
     * 1 is for Cheque
     *
     * @param i
     */
    private PaymentsBean formAPIData(int type) {
        PaymentsBean paymentsBean = new PaymentsBean();
        if (mTripSheetId != null) {
            paymentsBean.setPayments_tripsheetId(mTripSheetId);
        } else {
            paymentsBean.setPayments_tripsheetId("");
        }
        if (mAgentId != null) {
            paymentsBean.setPayments_userId(mAgentId);
        } else {
            paymentsBean.setPayments_userId("");
        }
        if (mAgentCode != null) {
            paymentsBean.setPayments_userCodes(mAgentCode);
        } else {
            paymentsBean.setPayments_userCodes("");
        }
        if (mAgentRouteId != null) {
            paymentsBean.setPayments_routeId(mAgentRouteId);
        } else {
            paymentsBean.setPayments_routeId("");
        }
        if (mAgentRouteCode != null) {
            paymentsBean.setPayments_routeCodes(mAgentRouteCode);
        } else {
            paymentsBean.setPayments_routeCodes("");
        }
        if (type == 0) {
            paymentsBean.setPayments_chequeNumber("");
        } else if (type == 1) {
            paymentsBean.setPayments_chequeNumber(mChequeNumber.getText().toString().trim());
        }
        paymentsBean.setPayments_accountNumber("");
        paymentsBean.setPayments_accountName("");
        if (type == 0) {
            paymentsBean.setPayments_bankName("");
        } else if (type == 1) {
            paymentsBean.setPayments_bankName(mBankName.getText().toString().trim());
        }
        if (type == 0) {
            paymentsBean.setPayments_chequeDate("");
        } else if (type == 1) {
            paymentsBean.setPayments_chequeDate(mChequeDate.getText().toString().trim());
        }
        paymentsBean.setPayments_chequeClearDate("");
        paymentsBean.setPayments_receiverName(""); // Company name
        paymentsBean.setPayments_transActionStatus("A");
        paymentsBean.setPayments_taxTotal(Double.parseDouble(currentDeliveriesBean.getmTripsheetDelivery_TaxTotal()));
        paymentsBean.setPayments_saleValue(Double.parseDouble(currentDeliveriesBean.getmTripsheetDelivery_SaleValue()));
        paymentsBean.setPayments_receivedAmount(Double.parseDouble(mAmountText.getText().toString().trim()));
        paymentsBean.setPayments_type(String.valueOf(type));
        paymentsBean.setPayments_status("A");
        paymentsBean.setPayments_delete("N");
        if (mAgentId != null) {
            paymentsBean.setPayments_saleOrderId(mAgentSoId);
        } else {
            paymentsBean.setPayments_saleOrderId("");
        }
        if (mAgentSoCode != null) {
            paymentsBean.setPayments_saleOrderCode(mAgentSoCode);
        } else {
            paymentsBean.setPayments_saleOrderCode("");
        }
        return paymentsBean;
    }

    public void savePaymentDetails() {
        Double enteredAmount = Double.parseDouble(mAmountText.getText().toString());
        if (enteredAmount > 0) {
            PaymentsBean paymentsBean = null;

            if (paymentTypeSpinner.getSelectedItem().toString().equals("Cash")) {
                synchronized (this) {
                    paymentsBean = formAPIData(0);
                }

            } else if (paymentTypeSpinner.getSelectedItem().toString().equals("Cheque")) {
                cheqLinearLayout.setVisibility(View.VISIBLE);

                if (mChequeNumber.getText().toString().trim().length() == 0) {
                    Toast.makeText(TripsheetPayments.this, "Please enter cheque number.", Toast.LENGTH_SHORT).show();
                } else if (mBankName.getText().toString().trim().length() == 0) {
                    Toast.makeText(TripsheetPayments.this, "Please enter bank name.", Toast.LENGTH_SHORT).show();
                } else if (mChequeDate.getText().toString().trim().length() == 0) {
                    Toast.makeText(TripsheetPayments.this, "Please enter cheque date.", Toast.LENGTH_SHORT).show();
                } else {
                    synchronized (this) {
                        paymentsBean = formAPIData(1);
                    }
                }
            }

            synchronized (this) {
                mDBHelper.insertTripsheetsPaymentsListData(paymentsBean);
            }

            Toast.makeText(TripsheetPayments.this, "Payment Details Saved Successfully.", Toast.LENGTH_SHORT).show();

            synchronized (this) {
                if (new NetworkConnectionDetector(TripsheetPayments.this).isNetworkConnected()) {
                    startService(new Intent(TripsheetPayments.this, SyncTripSheetsPaymentsService.class));
                }
            }

        } else {
            Toast.makeText(TripsheetPayments.this, "Please enter received amount.", Toast.LENGTH_SHORT).show();
        }
    }
}

