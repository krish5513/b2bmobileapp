package com.rightclickit.b2bsaleon.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.RouteStockAdapter;
import com.rightclickit.b2bsaleon.beanclass.TripSheetDeliveriesBean;
import com.rightclickit.b2bsaleon.beanclass.TripSheetReturnsBean;
import com.rightclickit.b2bsaleon.beanclass.TripsheetsStockList;
import com.rightclickit.b2bsaleon.customviews.CustomAlertDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.RouteStockListener;
import com.rightclickit.b2bsaleon.models.TripsheetsModel;
import com.rightclickit.b2bsaleon.services.SyncCloseTripSheetsStockService;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RouteStock extends AppCompatActivity implements RouteStockListener {
    private Context applicationContext, activityContext;
    private SearchView search;
    private String tripSheetId;
    RouteStockAdapter routestockadapter;
    private DBHelper mDBHelper;
    private TripsheetsModel mTripsheetsModel;
    ListView Routestocklist;
    private LinearLayout mCloseTripsLayout, mCloseTripApproveLayout, mCloseTripPrintLayout;
    ;
    private HashMap<String, String> deliveryQuantityListMap = new HashMap<String, String>();
    private HashMap<String, String> returnQuantityListMap = new HashMap<String, String>();
    private Map<String, String> selectedCBListMap, selectedLeakListMap, selectedOthersListMap,
            selectedPNamesListMap, selectedPDelsListMap, selectedPReturnsListMap;
    private String cbNotZeroStrings = "", mCloseTripSave = "", mCloseTripApprove = "", mCloseTripPrint = "";
    private ArrayList<TripsheetsStockList> mTripsheetsStockList = new ArrayList<TripsheetsStockList>();
    ArrayList<String[]> selectedList, cratesList;
    // ArrayList<AgentsStockBean> stockBeanArrayList;
    ArrayList<TripsheetsStockList> tripsheetsStockLists;
    TextView print;
    private Map<String, String> selectedLeakageProductsListHashMapTemp, selectedOthersProductsListHashMapTemp;
    String str_ProductName, str_ProductCode, str_Uom, str_DelQty, str_RetQty, str_LeakQty, str_OtherQty, str_CB;
    double tq = 0.0, dq = 0.0, rq = 0.0, leq = 0.0, oq = 0.0;
    private MMSharedPreferences mmSharedPreferences;
    double cb;
    private boolean isTripSheetClosed = false;
    private MenuItem mItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_stock);
        activityContext = RouteStock.this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tripSheetId = bundle.getString("tripSheetId");
            //str_Tripcode=bundle.getString("tripsheetCode");
            //str_Tripdate=bundle.getString("tripsheetDate");
        }
        mDBHelper = new DBHelper(activityContext);
        mmSharedPreferences = new MMSharedPreferences(this);
        this.getSupportActionBar().setTitle("ROUTE STOCK");
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
        Routestocklist = (ListView) findViewById(R.id.RouteStockList);
        mTripsheetsModel = new TripsheetsModel(this, RouteStock.this);

        ArrayList<String> privilegeActionsData = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mmSharedPreferences.getString("TripSheets"));
        //System.out.println("F 11111 ***COUNT === " + privilegeActionsData.size());
        for (int z = 0; z < privilegeActionsData.size(); z++) {
            //    System.out.println("Name::: " + privilegeActionsData.get(z).toString());

            if (privilegeActionsData.get(z).toString().equals("close_trip_save")) {
                mCloseTripSave = privilegeActionsData.get(z).toString();
            } else if (privilegeActionsData.get(z).toString().equals("close_trip_approve")) {
                mCloseTripApprove = privilegeActionsData.get(z).toString();
            } else if (privilegeActionsData.get(z).toString().equals("preview_print")) {
                mCloseTripPrint = privilegeActionsData.get(z).toString();
            }
        }

        tripsheetsStockLists = mDBHelper.fetchAllTripsheetsStockList(tripSheetId);
        selectedList = new ArrayList<>(tripsheetsStockLists.size());

        this.selectedLeakageProductsListHashMapTemp = new HashMap<>();
        this.selectedOthersProductsListHashMapTemp = new HashMap<>();
        this.selectedPDelsListMap = new HashMap<>();
        this.selectedCBListMap = new HashMap<>();
        print = (TextView) findViewById(R.id.tv_printroute);

        isTripSheetClosed = mDBHelper.isTripSheetClosed(tripSheetId);


        mCloseTripsLayout = (LinearLayout) findViewById(R.id.RetailersLayout);
        if (isTripSheetClosed) {
            mCloseTripsLayout.setVisibility(View.GONE);
        } else {
            if (mCloseTripSave.equals("close_trip_save")) {
                mCloseTripsLayout.setVisibility(View.VISIBLE);
            } else {
                mCloseTripsLayout.setVisibility(View.GONE);
            }
        }

        mCloseTripApproveLayout = (LinearLayout) findViewById(R.id.ProductsLayout);
        if (isTripSheetClosed) {
            mCloseTripApproveLayout.setVisibility(View.GONE);
        } else {
            if (mCloseTripApprove.equals("close_trip_approve")) {
                mCloseTripApproveLayout.setVisibility(View.VISIBLE);
            } else {
                mCloseTripApproveLayout.setVisibility(View.GONE);
            }
        }

        mCloseTripPrintLayout = (LinearLayout) findViewById(R.id.TDCLayout);
        if (mCloseTripPrint.equals("preview_print")) {
            mCloseTripPrintLayout.setVisibility(View.VISIBLE);
        } else {
            mCloseTripPrintLayout.setVisibility(View.GONE);
        }

        mCloseTripsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbNotZeroStrings = "";
                synchronized (this) {
                    if (selectedCBListMap.size() > 0) {
                        for (Map.Entry<String, String> productsBeanEntry : selectedCBListMap.entrySet()) {
                            // System.out.println("ASDF:::: " + productsBeanEntry.getValue());
                            Double val = Double.parseDouble(productsBeanEntry.getValue());
                            if (val != 0) {
                                if (cbNotZeroStrings.length() == 0) {
                                    cbNotZeroStrings = productsBeanEntry.getKey();
                                } else {
                                    cbNotZeroStrings = cbNotZeroStrings + ", " + productsBeanEntry.getKey();
                                }
                            }
                        }
                    }
                }
                //System.out.println("STR:::: " + cbNotZeroStrings);
                //System.out.println("TSSSS:::: " + tripsheetsStockLists.size());
                synchronized (this) {
                    if (cbNotZeroStrings.length() > 0) {
                        CustomAlertDialog.showAlertDialog(activityContext, "Failed", "Please make the closing balance as 0 for the following products.\n" + cbNotZeroStrings);
                    } else {
                        if (mTripsheetsStockList.size() > 0) {
                            mTripsheetsStockList.clear();
                        }
                        if (selectedCBListMap.size() > 0) {
                            for (int q = 0; q < tripsheetsStockLists.size(); q++) {
                                TripsheetsStockList tripStockBean = new TripsheetsStockList();

                                tripStockBean.setmTripsheetStockProductId(tripsheetsStockLists.get(q).getmTripsheetStockProductId());
                                tripStockBean.setmTripsheetStockTripsheetId(tripsheetsStockLists.get(q).getmTripsheetStockTripsheetId());
                                tripStockBean.setmTripsheetStockId(tripsheetsStockLists.get(q).getmTripsheetStockId());

//                                if (selectedCBListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductCode()) != null) {
//                                    // System.out.println("CB QUANTITY::: " + selectedCBListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductCode()));
//                                    tripStockBean.setmCBQuantity(selectedCBListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductCode()));
//                                }
//                                if (selectedLeakListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()) != null) {
//                                    //System.out.println("LEAK QUANTITY::: " + selectedLeakListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
//                                    tripStockBean.setmLeakQuantity(selectedLeakListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
//                                }
//
//                                if (selectedOthersListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()) != null) {
//                                    //System.out.println("OTHER QUANTITY::: " + selectedOthersListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
//                                    tripStockBean.setmOtherQuantity(selectedOthersListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
//                                }
//
//                                if (selectedPDelsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()) != null) {
//                                    //System.out.println("DEL QUANTITY::: " + selectedPDelsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
//                                    tripStockBean.setmDeliveryQuantity(selectedPDelsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
//                                }
//
//                                if (selectedPReturnsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()) != null) {
//                                    //System.out.println("RETU QUANTITY::: " + selectedPReturnsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
//                                    tripStockBean.setmRouteReturnQuantity(selectedPReturnsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
//                                }

                                if (selectedCBListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductCode()) != null) {
                                    // System.out.println("CB QUANTITY::: " + selectedCBListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductCode()));
                                    tripStockBean.setmCBQuantity(selectedCBListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductCode()));
                                    cb = Double.parseDouble(selectedCBListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductCode()));
                                }
                                if (selectedLeakListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()) != null) {
                                    //System.out.println("LEAK QUANTITY::: " + selectedLeakListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                    tripStockBean.setmLeakQuantity(selectedLeakListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                    leq = Double.parseDouble(selectedLeakListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                }

                                if (selectedOthersListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()) != null) {
                                    //System.out.println("OTHER QUANTITY::: " + selectedOthersListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                    tripStockBean.setmOtherQuantity(selectedOthersListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                    oq = Double.parseDouble(selectedOthersListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                }

                                if (selectedPDelsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()) != null) {
                                    //System.out.println("DEL QUANTITY::: " + selectedPDelsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                    tripStockBean.setmDeliveryQuantity(selectedPDelsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                    dq = Double.parseDouble(selectedPDelsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                }

                                if (selectedPReturnsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()) != null) {
                                    //System.out.println("RETU QUANTITY::: " + selectedPReturnsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                    tripStockBean.setmRouteReturnQuantity(selectedPReturnsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                    rq = Double.parseDouble(selectedPReturnsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));

                                }

                                mTripsheetsStockList.add(tripStockBean);
                            }
                        }
                    }
                }
                synchronized (this) {
                    if (mTripsheetsStockList.size() > 0) {
                        //System.out.println("HURRYYYY::: " + mTripsheetsStockList.size());
                        // Update the data into tripsheers stock list table and then api
                        mDBHelper.updateTripsheetsStockListDataForCloseTrips(mTripsheetsStockList);
                    }
                }

                synchronized (this) {
                    if (mTripsheetsStockList.size() > 0) {
                        //System.out.println("HURRYYYY 11111::: " + mTripsheetsStockList.size());
                        // Update the data into tripsheers stock list table and then api
                        if (new NetworkConnectionDetector(activityContext).isNetworkConnected()) {
                            Intent syncTDCOrderServiceIntent = new Intent(activityContext, SyncCloseTripSheetsStockService.class);
                            //syncTDCOrderServiceIntent.putExtra(Constants.BUNDLE_TDC_SALE_ORDER, currentOrder);
                            startService(syncTDCOrderServiceIntent);
                        }
                    }
                }

                synchronized (this) {
                    if (mTripsheetsStockList.size() > 0) {
                        int status = mDBHelper.updateTripSheetClosingStatus(tripSheetId);

                        if (status > 0) {
                            //Toast.makeText(activityContext, "Trip sheet closed successfully.", Toast.LENGTH_LONG).show();
                            showAlertDialogWithCancelButton(RouteStock.this, "Success", "Trip sheet closed successfully.");

                        } else {
                            //Toast.makeText(activityContext, "Trip sheet closing failed.", Toast.LENGTH_LONG).show();
                            CustomAlertDialog.showAlertDialog(activityContext, "Failed", getResources().getString(R.string.tripfail));
                        }
                    }
                }
            }
        });

        mCloseTripApproveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbNotZeroStrings = "";
                synchronized (this) {
                    if (selectedCBListMap.size() > 0) {
                        for (Map.Entry<String, String> productsBeanEntry : selectedCBListMap.entrySet()) {
                            //System.out.println("ASDF:::: " + productsBeanEntry.getValue());
                            Double val = Double.parseDouble(productsBeanEntry.getValue());
                            if (val != 0) {
                                if (cbNotZeroStrings.length() == 0) {
                                    cbNotZeroStrings = productsBeanEntry.getKey();
                                } else {
                                    cbNotZeroStrings = cbNotZeroStrings + ", " + productsBeanEntry.getKey();
                                }
                            }
                        }
                    }
                }
                //System.out.println("STR:::: " + cbNotZeroStrings);
                //System.out.println("TSSSS:::: " + tripsheetsStockLists.size());
                synchronized (this) {
                    if (cbNotZeroStrings.length() > 0) {
                        CustomAlertDialog.showAlertDialog(activityContext, "Failed", "Please make the closing balance as 0 for the following products.\n" + cbNotZeroStrings);
                    } else {
                        if (mTripsheetsStockList.size() > 0) {
                            mTripsheetsStockList.clear();
                        }
                        if (selectedCBListMap.size() > 0) {
                            for (int q = 0; q < tripsheetsStockLists.size(); q++) {
                                TripsheetsStockList tripStockBean = new TripsheetsStockList();

                                tripStockBean.setmTripsheetStockProductId(tripsheetsStockLists.get(q).getmTripsheetStockProductId());
                                tripStockBean.setmTripsheetStockTripsheetId(tripsheetsStockLists.get(q).getmTripsheetStockTripsheetId());
                                tripStockBean.setmTripsheetStockId(tripsheetsStockLists.get(q).getmTripsheetStockId());

                                if (selectedCBListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductCode()) != null) {
                                    // System.out.println("CB QUANTITY::: " + selectedCBListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductCode()));
                                    tripStockBean.setmCBQuantity(selectedCBListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductCode()));
                                    cb = Double.parseDouble(selectedCBListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductCode()));
                                }
                                if (selectedLeakListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()) != null) {
                                    //System.out.println("LEAK QUANTITY::: " + selectedLeakListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                    tripStockBean.setmLeakQuantity(selectedLeakListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                    leq = Double.parseDouble(selectedLeakListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                }

                                if (selectedOthersListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()) != null) {
                                    //System.out.println("OTHER QUANTITY::: " + selectedOthersListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                    tripStockBean.setmOtherQuantity(selectedOthersListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                    oq = Double.parseDouble(selectedOthersListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                }

                                if (selectedPDelsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()) != null) {
                                    //System.out.println("DEL QUANTITY::: " + selectedPDelsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                    tripStockBean.setmDeliveryQuantity(selectedPDelsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                    dq = Double.parseDouble(selectedPDelsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                }

                                if (selectedPReturnsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()) != null) {
                                    //System.out.println("RETU QUANTITY::: " + selectedPReturnsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                    tripStockBean.setmRouteReturnQuantity(selectedPReturnsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                    rq = Double.parseDouble(selectedPReturnsListMap.get(tripsheetsStockLists.get(q).getmTripsheetStockProductId()));
                                }

                                mTripsheetsStockList.add(tripStockBean);
                            }
                        }
                    }
                }
                synchronized (this) {
                    if (mTripsheetsStockList.size() > 0) {
                        //System.out.println("HURRYYYY::: " + mTripsheetsStockList.size());
                        // Update the data into tripsheers stock list table and then api
                        mDBHelper.updateTripsheetsStockListDataForCloseTrips(mTripsheetsStockList);
                    }
                }

                synchronized (this) {
                    if (mTripsheetsStockList.size() > 0) {
                        //System.out.println("HURRYYYY 11111::: " + mTripsheetsStockList.size());
                        // Update the data into tripsheers stock list table and then api
                        if (new NetworkConnectionDetector(activityContext).isNetworkConnected()) {
                            Intent syncTDCOrderServiceIntent = new Intent(activityContext, SyncCloseTripSheetsStockService.class);
                            //syncTDCOrderServiceIntent.putExtra(Constants.BUNDLE_TDC_SALE_ORDER, currentOrder);
                            startService(syncTDCOrderServiceIntent);
                        }
                    }
                }

                synchronized (this) {
                    if (mTripsheetsStockList.size() > 0) {
                        int status = mDBHelper.updateTripSheetClosingStatus(tripSheetId);

                        if (status > 0) {
                          //  Toast.makeText(activityContext, "Trip sheet closed/approved successfully.", Toast.LENGTH_LONG).show();
                            //CustomAlertDialog.showAlertDialog(activityContext, "Success",  getResources().getString(R.string.tripsave));
                            showAlertDialogWithCancelButton(RouteStock.this, "Success", "Trip sheet closed/approved successfully.");

                        } else {
                            //Toast.makeText(activityContext, "Trip sheet closing failed.", Toast.LENGTH_LONG).show();
                            CustomAlertDialog.showAlertDialog(activityContext, "Failed",  getResources().getString(R.string.tripfail));
                        }
                    }
                }
            }
        });


        mCloseTripPrintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                synchronized (this) {
                    for (int i = 0; i < tripsheetsStockLists.size(); i++) {
                        double tq = 0.0, cb = 0.0, leq = 0.0, oq = 0.0, dq = 0.0, rq = 0.0;
                        str_ProductName = tripsheetsStockLists.get(i).getmTripsheetStockProductName();
                        str_ProductCode = tripsheetsStockLists.get(i).getmTripsheetStockProductCode();

                        // TRUCK QUANTITY
                        if (tripsheetsStockLists.get(i).getmTripsheetStockVerifiedQuantity().length() > 0) {
                            Double verifiedQuantity = Double.parseDouble(tripsheetsStockLists.get(i).getmTripsheetStockVerifiedQuantity());
                            tq = verifiedQuantity;
                        }
                        // CB QUANTITY
                        if (selectedCBListMap.get(tripsheetsStockLists.get(i).getmTripsheetStockProductCode()) != null) {
                            cb = Double.parseDouble(selectedCBListMap.get(tripsheetsStockLists.get(i).getmTripsheetStockProductCode()));
                        }
                        // LEAK QUANTITY
                        if (selectedLeakListMap.get(tripsheetsStockLists.get(i).getmTripsheetStockProductId()) != null) {
                            leq = Double.parseDouble(selectedLeakListMap.get(tripsheetsStockLists.get(i).getmTripsheetStockProductId()));
                        }
                        // OTHER QUANTITY
                        if (selectedOthersListMap.get(tripsheetsStockLists.get(i).getmTripsheetStockProductId()) != null) {
                            oq = Double.parseDouble(selectedOthersListMap.get(tripsheetsStockLists.get(i).getmTripsheetStockProductId()));
                        }
                        // DELIVERY QUANTITY
                        if (selectedPDelsListMap.get(tripsheetsStockLists.get(i).getmTripsheetStockProductId()) != null) {
                            dq = Double.parseDouble(selectedPDelsListMap.get(tripsheetsStockLists.get(i).getmTripsheetStockProductId()));
                        }
                        // RETURNS QUANTITY
                        if (selectedPReturnsListMap.get(tripsheetsStockLists.get(i).getmTripsheetStockProductId()) != null) {
                            rq = Double.parseDouble(selectedPReturnsListMap.get(tripsheetsStockLists.get(i).getmTripsheetStockProductId()));
                        }

                        // CB formula
                        try {
                            double cb1 = tq;
                            double cb2 = dq + rq + leq + oq;
                            cb = cb1 - cb2;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        String[] temp = new String[9];
                        temp[0] = str_ProductName;
                        temp[1] = str_ProductCode;
                        str_Uom = mDBHelper.getProductUnitByProductCode(str_ProductCode);
                        temp[2] = str_Uom;
                        temp[3] = String.valueOf(tq);
                        temp[4] = String.valueOf(dq);
                        temp[5] = String.valueOf(rq);
                        temp[6] = String.valueOf(leq);
                        temp[7] = String.valueOf(oq);
                        temp[8] = String.valueOf(cb);

                        selectedList.add(temp);
                    }
                }
                synchronized (this) {
                    int pageheight = 450 + selectedList.size() * 200;
                    Bitmap bmOverlay = Bitmap.createBitmap(400, pageheight, Bitmap.Config.ARGB_4444);
                    Canvas canvas = new Canvas(bmOverlay);
                    canvas.drawColor(Color.WHITE);
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setAntiAlias(true);
                    paint.setFilterBitmap(true);
                    paint.setDither(true);
                    paint.setColor(Color.parseColor("#000000"));
                    paint.setTextSize(26);

                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    canvas.drawText(mmSharedPreferences.getString("companyname"), 5, 20, paint);
                    paint.setTextSize(20);
                    canvas.drawText(mmSharedPreferences.getString("loginusername"), 5, 50, paint);
                    paint.setTextSize(20);
                    canvas.drawText("-------------------------------------------", 5, 80, paint);
                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    paint.setTextSize(20);
                    canvas.drawText("TRUCK STOCK SUMMARY", 50, 110, paint);

                    paint.setTextSize(20);
                    canvas.drawText("TRIP# ", 5, 140, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + "804405", 150, 140, paint);
                    paint.setTextSize(20);
                    canvas.drawText("DATE ", 5, 170, paint);
                    paint.setTextSize(20);
                    canvas.drawText(": " + "17-09-2017", 150, 170, paint);
                    paint.setTextSize(20);
                    canvas.drawText("-------------------------------------------", 5, 200, paint);

                    int st = 230;
                    paint.setTextSize(17);
                    // for (Map.Entry<String, String[]> entry : selectedList.entrySet()) {

                    for (int i = 0; i < selectedList.size(); i++) {
                        String[] temp = selectedList.get(i);
                        paint.setTextSize(20);
                        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        canvas.drawText(temp[0] + "," + temp[1] + " ( " + temp[2] + " )", 5, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("TRUCK QTY ", 5, st, paint);
                        canvas.drawText(": " + temp[3], 150, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("DELIVERY QTY ", 5, st, paint);
                        canvas.drawText(": " + temp[4], 150, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("RETURN QTY ", 5, st, paint);
                        canvas.drawText(": " + temp[5], 150, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("LEAK QTY ", 5, st, paint);
                        canvas.drawText(": " + temp[6], 150, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("OTHER QTY", 5, st, paint);
                        canvas.drawText(": " + temp[7], 150, st, paint);
                        st = st + 30;
                        paint.setTextSize(20);
                        canvas.drawText("CB", 5, st, paint);
                        canvas.drawText(": " + temp[8], 150, st, paint);


                        st = st + 40;
                    }

                    canvas.drawText("--------X---------", 100, st, paint);
                    com.szxb.api.jni_interface.api_interface.printBitmap(bmOverlay, 5, 5);
                }
            }
        });


        if (new NetworkConnectionDetector(RouteStock.this).isNetworkConnected()) {
            mTripsheetsModel.getTripsheetsStockList(tripSheetId);
        } else if (tripsheetsStockLists.size() > 0) {
            loadTripsData(tripsheetsStockLists);
        }

    }


    public void loadTripsData(ArrayList<TripsheetsStockList> tripsStockList) {
        // ALL DELIVIRES
        ArrayList<TripSheetDeliveriesBean> deliveriesBeenList = mDBHelper.fetchAllTripsheetsDeliveriesListByTripAndProductId(tripSheetId, "");
        if (deliveryQuantityListMap.size() > 0) {
            deliveryQuantityListMap.clear();
        }
        if (deliveriesBeenList.size() > 0) {
            // This for Sales man
            for (int g = 0; g < deliveriesBeenList.size(); g++) {
                if (deliveryQuantityListMap.get(deliveriesBeenList.get(g).getmTripsheetDelivery_productId()) != null) {
                    double quantity = Double.parseDouble(deliveryQuantityListMap.get(deliveriesBeenList.get(g).getmTripsheetDelivery_productId()));

                    double sumQuantity = quantity + Double.parseDouble(deliveriesBeenList.get(g).getmTripsheetDelivery_Quantity());

                    deliveryQuantityListMap.put(deliveriesBeenList.get(g).getmTripsheetDelivery_productId(),
                            String.valueOf(sumQuantity));
                } else {
                    deliveryQuantityListMap.put(deliveriesBeenList.get(g).getmTripsheetDelivery_productId(),
                            deliveriesBeenList.get(g).getmTripsheetDelivery_Quantity());
                }
            }
        } else {
            // This for Dispatch Manager
            ArrayList<TripsheetsStockList> tripsheetsStockLists = mDBHelper.fetchAllTripsheetsStockList(tripSheetId);
            if (deliveryQuantityListMap.size() > 0) {
                deliveryQuantityListMap.clear();
            }
            if (tripsheetsStockLists.size() > 0) {
                for (int g1 = 0; g1 < tripsheetsStockLists.size(); g1++) {
                    if (deliveryQuantityListMap.get(tripsheetsStockLists.get(g1).getmTripsheetStockProductId()) != null) {
                        double quantity = Double.parseDouble(deliveryQuantityListMap.get(tripsheetsStockLists.get(g1).getmTripsheetStockProductId()));

                        if (tripsheetsStockLists.get(g1).getmDeliveryQuantity() != null) {
                            double sumQuantity = quantity + Double.parseDouble(tripsheetsStockLists.get(g1).getmDeliveryQuantity());

                            deliveryQuantityListMap.put(tripsheetsStockLists.get(g1).getmTripsheetStockProductId(),
                                    String.valueOf(sumQuantity));
                        }

                    } else {
                        if (tripsheetsStockLists.get(g1).getmDeliveryQuantity() != null) {
                            deliveryQuantityListMap.put(tripsheetsStockLists.get(g1).getmTripsheetStockProductId(),
                                    tripsheetsStockLists.get(g1).getmDeliveryQuantity());
                        } else {
                            deliveryQuantityListMap.put(tripsheetsStockLists.get(g1).getmTripsheetStockProductId(), String.valueOf(0.0));
                        }
                    }
                }
            }
        }

        // ALL RETURNS
        ArrayList<TripSheetReturnsBean> returnsBeenList = mDBHelper.fetchAllTripsheetsReturnsListByTripId(tripSheetId);
        if (returnQuantityListMap.size() > 0) {
            returnQuantityListMap.clear();
        }
        if (returnsBeenList.size() > 0) {
            for (int g = 0; g < returnsBeenList.size(); g++) {
                if (returnQuantityListMap.get(returnsBeenList.get(g).getmTripshhetReturnsProduct_ids()) != null) {
                    double quantity = Double.parseDouble(returnQuantityListMap.get(returnsBeenList.get(g).getmTripshhetReturnsProduct_ids()));

                    double sumQuantity = quantity + Double.parseDouble(returnsBeenList.get(g).getmTripshhetReturnsQuantity());

                    returnQuantityListMap.put(returnsBeenList.get(g).getmTripshhetReturnsProduct_ids(),
                            String.valueOf(sumQuantity));
                } else {
                    returnQuantityListMap.put(returnsBeenList.get(g).getmTripshhetReturnsProduct_ids(),
                            returnsBeenList.get(g).getmTripshhetReturnsQuantity());
                }
            }
        } else {
            if (returnQuantityListMap.size() > 0) {
                returnQuantityListMap.clear();
            }
            // This for Dispatch Manager
            ArrayList<TripsheetsStockList> tripsheetsStockLists1 = mDBHelper.fetchAllTripsheetsStockList(tripSheetId);
            if (tripsheetsStockLists1.size() > 0) {
                for (int g1 = 0; g1 < tripsheetsStockLists1.size(); g1++) {
                    if (returnQuantityListMap.get(tripsheetsStockLists1.get(g1).getmTripsheetStockProductId()) != null) {
                        double quantity = Double.parseDouble(returnQuantityListMap.get(tripsheetsStockLists1.get(g1).getmTripsheetStockProductId()));

                        if (tripsheetsStockLists1.get(g1).getmReturnQuantity() != null) {
                            double sumQuantity = quantity + Double.parseDouble(tripsheetsStockLists1.get(g1).getmReturnQuantity());

                            returnQuantityListMap.put(tripsheetsStockLists1.get(g1).getmTripsheetStockProductId(),
                                    String.valueOf(sumQuantity));
                        }

                    } else {
                        if (tripsheetsStockLists1.get(g1).getmReturnQuantity() != null) {
                            returnQuantityListMap.put(tripsheetsStockLists1.get(g1).getmTripsheetStockProductId(),
                                    tripsheetsStockLists1.get(g1).getmReturnQuantity());
                        } else {
                            returnQuantityListMap.put(tripsheetsStockLists1.get(g1).getmTripsheetStockProductId(), String.valueOf(0.0));
                        }
                    }
                }
            }
        }

        if (routestockadapter != null) {
            routestockadapter = null;
        }

        routestockadapter = new RouteStockAdapter(this, RouteStock.this, this, tripsStockList, deliveryQuantityListMap, returnQuantityListMap);
        Routestocklist.setAdapter(routestockadapter);


//        if (new NetworkConnectionDetector(RouteStock.this).isNetworkConnected()) {
//            mTripsheetsModel.getTripsheetsStockList(tripSheetId);
//        } else if (tripsheetsStockLists.size() > 0) {
//            loadTripsData(tripsheetsStockLists);
//        }
        mItem.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);

        try {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            search = (SearchView) menu.findItem(R.id.action_search).getActionView();
            search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    //  deliveriesAdapter.filter(query);
                    return true;
                }
            });

            // Get the search close button image view
            ImageView closeButton = (ImageView) search.findViewById(R.id.search_close_btn);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    search.setQuery("", false);
                    search.clearFocus();
                    search.onActionViewCollapsed();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.mItem = item;
        int id = item.getItemId();
        if (id == R.id.action_search) {

            return true;
        }

        if (id == R.id.autorenew) {
            if (new NetworkConnectionDetector(RouteStock.this).isNetworkConnected()) {
                mItem.setEnabled(false);
                mTripsheetsModel.getTripsheetsStockList(tripSheetId);
            } else {
                new NetworkConnectionDetector(RouteStock.this).displayNoNetworkError(RouteStock.this);
            }
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
        menu.findItem(R.id.sort).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, TripSheetViewPreview.class);
        intent.putExtra("tripSheetId", tripSheetId);
        startActivity(intent);

    }


    @Override
    public void updateSelectedCBList(Map<String, String> selectedCBList) {
        this.selectedCBListMap = selectedCBList;
        System.out.println("CB LSIT:::: " + selectedCBList.size());
    }

    @Override
    public void updateSelectedLeakageQuantityList(Map<String, String> selectedLeakList) {
        this.selectedLeakListMap = selectedLeakList;
        System.out.println("LEAK LSIT:::: " + selectedLeakListMap.size());
    }

    @Override
    public void updateSelectedOthersQuantityList(Map<String, String> selectedOthersList) {
        this.selectedOthersListMap = selectedOthersList;
        System.out.println("OTHERS LSIT:::: " + selectedOthersListMap.size());
    }

    @Override
    public void updateSelectedPNamesQuantityList(Map<String, String> selectedPNamesList) {
        this.selectedPNamesListMap = selectedPNamesList;
    }

    @Override
    public void updateSelectedPDelsQuantityList(Map<String, String> selectedPDelsList) {
        this.selectedPDelsListMap = selectedPDelsList;
    }

    @Override
    public void updateSelectedPRetunsQuantityList(Map<String, String> selectedPReturnsList) {
        this.selectedPReturnsListMap = selectedPReturnsList;
        System.out.println("RRTN LSIT:::: " + selectedPReturnsListMap.size());
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
                    Intent intent = new Intent(activityContext, TripSheetsActivity.class);
                    startActivity(intent);
                    finish();
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

}
