package com.rightclickit.b2bsaleon.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.TakeOrdersAdapter;
import com.rightclickit.b2bsaleon.beanclass.ProductsBean;
import com.rightclickit.b2bsaleon.beanclass.TakeOrderBean;
import com.rightclickit.b2bsaleon.customviews.CustomAlertDialog;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.interfaces.AgentTakeOrderListener;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class AgentTakeOrderScreen extends AppCompatActivity implements AgentTakeOrderListener {
    private MMSharedPreferences mPreference;
    private ListView mTakeOrderListView;
    private TakeOrdersAdapter mTakeOrderAdapter;
    private ArrayList<TakeOrderBean> mTakeOrderBeansList = new ArrayList<TakeOrderBean>();
    private DBHelper mDBHelper;

    private LinearLayout mTakeOrdersLayout;
    private LinearLayout mDeliveriesLayout;
    private LinearLayout mReturnsLayout;
    private LinearLayout mTDCorderLayout;


    private LinearLayout tpsBottomOptionsLayout;


    public static LinearLayout mPaymentsLayout;

    public static FloatingActionButton fab;
    public static SearchView search;

    private ArrayList<ProductsBean> productsList, productsListSort;

    public String Agents;
    public String TroipsTakeorder = "", tripSheetId = "";

    public static boolean isCloseClicked;
    Runnable rr;
    Handler h = new Handler();

    private Map<String, String> selectedTakeOrderQuantityListMap, selectedTakeOrderFromDatesListMap, selectedTakeOrderToDatesListMap;
    private boolean isAscendingSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agents_take_order);

        mPreference = new MMSharedPreferences(this);
        mDBHelper = new DBHelper(this);
        selectedTakeOrderQuantityListMap = new HashMap<String, String>();
        selectedTakeOrderFromDatesListMap = new HashMap<String, String>();
        selectedTakeOrderToDatesListMap = new HashMap<String, String>();
        productsListSort = new ArrayList<ProductsBean>();
        this.getSupportActionBar().setLogo(R.drawable.customers_white_24);
        this.getSupportActionBar().setTitle(mPreference.getString("agentName"));
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);


        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            TroipsTakeorder = bundle.getString("From");
            tripSheetId = bundle.getString("tripsheetId");
        }


        mTakeOrderBeansList = mDBHelper.fetchAllRecordsFromTakeOrderProductsTable("no", mPreference.getString("agentId"));
        productsList = mDBHelper.fetchAllRecordsFromProductsTableForTakeOrders(mPreference.getString("agentId"));
        //System.out.println("The TO LIST IS::: " + productsList.size());
        //System.out.println("The TO LIST 111 IS::: " + mTakeOrderBeansList.size());
//        for (int k = 0; k<productsList.size();k++){
//            System.out.println("AAAA FROM:: "+mTakeOrderBeansList.get(k).getmProductFromDate());
//            System.out.println("BBBBB QUA:: "+mTakeOrderBeansList.get(k).getmProductQuantity());
//            System.out.println("CCCC TO:: "+mTakeOrderBeansList.get(k).getmProductToDate());
//            System.out.println("DDDDD PID:: "+mTakeOrderBeansList.get(k).getmProductId());
//            System.out.println("EEEEE PTI:: "+mTakeOrderBeansList.get(k).getmProductTitle());
//            System.out.println("FFFFF PAGE ID:: "+mTakeOrderBeansList.get(k).getmAgentId());
//            System.out.println("GGGGG PENQ ID:: "+mTakeOrderBeansList.get(k).getmEnquiryId());
//        }


        mTakeOrdersLayout = (LinearLayout) findViewById(R.id.takeorder);
        mTakeOrdersLayout.setVisibility(View.GONE);

        mTakeOrderListView = (ListView) findViewById(R.id.TakeOrdersList);
        if (productsList.size() > 0) {
            mTakeOrderAdapter = new TakeOrdersAdapter(this, this, productsList, mTakeOrderListView, mPreference.getString("agentId"), mTakeOrderBeansList,
                    selectedTakeOrderQuantityListMap, selectedTakeOrderFromDatesListMap, selectedTakeOrderToDatesListMap,TroipsTakeorder,tripSheetId);
            mTakeOrderListView.setAdapter(mTakeOrderAdapter);
        }

        mPaymentsLayout = (LinearLayout) findViewById(R.id.PaymentsLayout);

        mTDCorderLayout = (LinearLayout) findViewById(R.id.TPCLayout);
        mTDCorderLayout.setVisibility(View.GONE);
        mTDCorderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                mTDCorderLayout.startAnimation(animation1);
                Intent i = new Intent(AgentTakeOrderScreen.this, AgentTDC_Order.class);
                startActivity(i);
                finish();


            }
        });

        mDeliveriesLayout = (LinearLayout) findViewById(R.id.DeliveriesTakeOrder);
        mDeliveriesLayout.setVisibility(View.GONE);
        mDeliveriesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                mDeliveriesLayout.startAnimation(animation1);

                Intent i = new Intent(AgentTakeOrderScreen.this, AgentDeliveries.class);
                startActivity(i);
                finish();

            }
        });
        mReturnsLayout = (LinearLayout) findViewById(R.id.ReturnsTakeOrder);
        mReturnsLayout.setVisibility(View.GONE);
        mReturnsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                mReturnsLayout.startAnimation(animation1);

                Intent i = new Intent(AgentTakeOrderScreen.this, AgentReturns.class);
                startActivity(i);
                finish();

            }
        });
        mPaymentsLayout = (LinearLayout) findViewById(R.id.PaymentsTakeOrder);
        mPaymentsLayout.setVisibility(View.GONE);
        mPaymentsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                mPaymentsLayout.startAnimation(animation1);

                Intent i = new Intent(AgentTakeOrderScreen.this, AgentPayments.class);
                startActivity(i);
                finish();

            }
        });

        mTakeOrdersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                mPaymentsLayout.startAnimation(animation1);

                Intent i = new Intent(AgentTakeOrderScreen.this, AgentTakeOrderScreen.class);
                i.putExtra("From", "Agents");

                startActivity(i);
                finish();

            }
        });


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.save_icon_white));

        ArrayList<String> privilegeActionsData = mDBHelper.getUserActivityActionsDetailsByPrivilegeId(mPreference.getString("Customers"));
        // System.out.println("F 11111 ***COUNT === " + privilegeActionsData.size());
        for (int z = 0; z < privilegeActionsData.size(); z++) {

            //System.out.println("Name::: " + privilegeActionsData.get(z).toString());
            if (privilegeActionsData.get(z).toString().equals("Orders_List")) {
                mTDCorderLayout.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData.get(z).toString().equals("Delivery_List")) {
                mDeliveriesLayout.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData.get(z).toString().equals("Return_List")) {
                mReturnsLayout.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData.get(z).toString().equals("Payment_List")) {
                mPaymentsLayout.setVisibility(View.VISIBLE);
            } else if (privilegeActionsData.get(z).toString().equals("Take_Orders")) {
                mTakeOrdersLayout.setVisibility(View.VISIBLE);
            }


        }


        tpsBottomOptionsLayout = (LinearLayout) findViewById(R.id.tpsBottomOptionsLayout);
        if (TroipsTakeorder.equals("Tripsheet")) {
            tpsBottomOptionsLayout.setVisibility(View.GONE);

        } else {
            tpsBottomOptionsLayout.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_tdc, menu);
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        search = (SearchView) menu.findItem(R.id.action_search).getActionView();

        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mTakeOrderAdapter.filter(query);
                rr = new Runnable() {
                    @Override
                    public void run() {
                        h.removeCallbacks(rr);
                        isCloseClicked = false;
                    }
                };
                h.postDelayed(rr, 2000);
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


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_search) {
//
//            return true;
//        }

        if (id == R.id.sort) {
            System.out.println("Selected TO::: " + selectedTakeOrderQuantityListMap.size());
            System.out.println("Products List Size::: " + productsList.size());
            if (selectedTakeOrderQuantityListMap.size() > 0) {
                synchronized (this) {
                    if (productsList.size() > 0) {
                        if (productsListSort.size() > 0) {
                            productsListSort.clear();
                        }
                        for (int y = 0; y < productsList.size(); y++) {
                            Double toq = 0.000;
                            ProductsBean pB = new ProductsBean();

                            pB.setProductId(productsList.get(y).getProductId());
                            pB.setProductCode(productsList.get(y).getProductCode());
                            pB.setProductTitle(productsList.get(y).getProductTitle());
                            pB.setProductDescription(productsList.get(y).getProductDescription());
                            pB.setProductImageUrl(productsList.get(y).getProductImageUrl());
                            pB.setProductReturnable(productsList.get(y).getProductReturnable());
                            pB.setProductMOQ(productsList.get(y).getProductMOQ());
                            pB.setProductUOM(productsList.get(y).getProductUOM());
                            pB.setProductAgentPrice(productsList.get(y).getProductAgentPrice());
                            pB.setProductConsumerPrice(productsList.get(y).getProductConsumerPrice());
                            pB.setProductRetailerPrice(productsList.get(y).getProductRetailerPrice());
                            pB.setProductgst(productsList.get(y).getProductgst());
                            pB.setProductvat(productsList.get(y).getProductvat());

                            if (selectedTakeOrderQuantityListMap.get(productsList.get(y).getProductId()) != null) {
                                toq = Double.parseDouble(selectedTakeOrderQuantityListMap.get(productsList.get(y).getProductId()));
                                pB.setmTakeOrderQuantity(String.valueOf(toq));
                            } else {
                                if (productsList.get(y).getmTakeOrderQuantity() != null) {
                                    pB.setmTakeOrderQuantity(productsList.get(y).getmTakeOrderQuantity());
                                } else {
                                    pB.setmTakeOrderQuantity(String.valueOf(toq));
                                }
                            }
                            if (selectedTakeOrderFromDatesListMap.get(productsList.get(y).getProductId()) != null) {
                                pB.setmTakeOrderFromDate(selectedTakeOrderFromDatesListMap.get(productsList.get(y).getProductId()));
                            } else {
                                pB.setmTakeOrderFromDate(productsList.get(y).getmTakeOrderFromDate());
                            }
                            if (selectedTakeOrderToDatesListMap.get(productsList.get(y).getProductId()) != null) {
                                pB.setmTakeOrderToDate(selectedTakeOrderToDatesListMap.get(productsList.get(y).getProductId()));
                            } else {
                                pB.setmTakeOrderToDate(productsList.get(y).getmTakeOrderToDate());
                            }

                            productsListSort.add(pB);
                        }

                        System.out.println("Products SORT List Size::: " + productsListSort.size());
                    }
                }

                synchronized (this) {
                    if (!isAscendingSort) {
                        // Ascending order
                        isAscendingSort = true;
                        Collections.sort(productsListSort, StringAscComparator);
                    } else {
                        // Descending order
                        isAscendingSort = false;
                        Collections.sort(productsListSort, StringDescComparator);
                    }
                }

                synchronized (this) {
                    if (mTakeOrderAdapter != null) {
                        mTakeOrderAdapter = null;
                    }
                    mTakeOrderAdapter = new TakeOrdersAdapter(this, this, productsListSort, mTakeOrderListView, mPreference.getString("agentId"), mTakeOrderBeansList,
                            selectedTakeOrderQuantityListMap, selectedTakeOrderFromDatesListMap, selectedTakeOrderToDatesListMap,TroipsTakeorder,tripSheetId);
                    mTakeOrderListView.setAdapter(mTakeOrderAdapter);
                    mTakeOrderAdapter.notifyDataSetChanged();
                }
            } else {
                CustomAlertDialog.showAlertDialog(AgentTakeOrderScreen.this, "Failed", getResources().getString(R.string.take_order_sort));
            }
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                if (search.isIconified()) {
                    onBackPressed();
                } else {
                    search.setQuery("", false);
                    search.clearFocus();
                    search.onActionViewCollapsed();
                }
                return true;
            default:
                return true;
        }
    }

    // Comparator for Ascending Order
    public static Comparator<ProductsBean> StringAscComparator = new Comparator<ProductsBean>() {

        public int compare(ProductsBean app1, ProductsBean app2) {
            int g = 0;

            Double stringName1 = Double.parseDouble(app1.getmTakeOrderQuantity());
            Double stringName2 = Double.parseDouble(app2.getmTakeOrderQuantity());
            if (stringName1 >= stringName2) {
                return -1;
            } else {
                return 1;
            }
        }
    };

    //Comparator for Descending Order
    public static Comparator<ProductsBean> StringDescComparator = new Comparator<ProductsBean>() {

        public int compare(ProductsBean app1, ProductsBean app2) {
            int f = 0;
            Double stringName1 = Double.parseDouble(app1.getmTakeOrderQuantity());
            Double stringName2 = Double.parseDouble(app2.getmTakeOrderQuantity());
            if (stringName2 >= stringName1) {
                return -1;
            } else {
                return 1;
            }
        }
    };

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        menu.findItem(R.id.notifications).setVisible(false);
        menu.findItem(R.id.settings).setVisible(false);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(true);
        menu.findItem(R.id.Add).setVisible(false);

        menu.findItem(R.id.autorenew).setVisible(false);
        menu.findItem(R.id.sort).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (TroipsTakeorder.equals("Tripsheet")) {
            Intent intent = new Intent(this, TripSheetView.class);
            intent.putExtra("tripsheetId", tripSheetId);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, AgentTDC_Order.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void updateSelectedTakeOrderQuantity(Map<String, String> productsList, Map<String, String> fromDatesList, Map<String, String> toDatesList) {
        this.selectedTakeOrderQuantityListMap = productsList;
        this.selectedTakeOrderFromDatesListMap = fromDatesList;
        this.selectedTakeOrderToDatesListMap = toDatesList;
    }
}

