package com.rightclickit.b2bsaleon.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rightclickit.b2bsaleon.R;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.Nextdayindent_MoreinfoAdapter;
import com.rightclickit.b2bsaleon.beanclass.Nextdayindent_moreinfoBeen;
import com.rightclickit.b2bsaleon.database.DBHelper;
import com.rightclickit.b2bsaleon.models.AgentsModel;
import com.rightclickit.b2bsaleon.util.MMSharedPreferences;
import com.rightclickit.b2bsaleon.util.NetworkConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NextIndent_Moreinfo extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    Spinner moreinfo_menubarSpinner,multispinner;
    DBHelper db;
    AgentsModel agentsmodel;
    private MMSharedPreferences mmSharedPreferences;
    private JSONArray routeCodesArray;
    String selected_val, selectedroute;
    ArrayList<String> idsArray = new ArrayList<String>();
    List<String> routesDataList = null;
    private Context applicationContext, activityContext;


    public static final String[] dates = new String[] { "02/05/2018",
            "01/05/2018", "30/04/2018", "29/04/2018" };
    public static final String[] milk = new String[] { "500.000",
            "400.000", "500.000", "300.000" };
    public static final String[] curd = new String[] { "500",
            "600", "200", "400" };
    public static final String[] other = new String[] { "600",
            "700", "400", "600" };
    ListView moreinfo_listView;
    List<Nextdayindent_moreinfoBeen> nextdayIndentMoreInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_indent__moreinfo);
        activityContext = NextIndent_Moreinfo.this;

        this.getSupportActionBar().setTitle("MORE INFO");
        this.getSupportActionBar().setSubtitle(null);
        //this.getSupportActionBar().setLogo(R.drawable.customers_white_24);
        // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);


        this.getSupportActionBar().setDisplayShowCustomEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        View v=getLayoutInflater().inflate(R.layout.moreinfo_menubar,null);
        moreinfo_menubarSpinner = (Spinner) v.findViewById(R.id.moreinfo_menubarSpinner);
        this.getSupportActionBar().setCustomView(v);


        multispinner = (Spinner) findViewById(R.id.multiSpinner);


        db = new DBHelper(getApplicationContext());
        agentsmodel = new AgentsModel(activityContext, this);
        mmSharedPreferences = new MMSharedPreferences(NextIndent_Moreinfo.this);
        nextdayIndentMoreInfo = new ArrayList<Nextdayindent_moreinfoBeen>();


        System.out.println("STAKE ID IS::: " + db.getStakeTypeIdByStakeType("2"));


        HashMap<String, String> userRouteIds = db.getUserRouteIds();


        try {
            routeCodesArray = new JSONObject(userRouteIds.get("route_ids")).getJSONArray("routeArray");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String> stringArray = new ArrayList<String>();
        final HashMap<Integer, String> map = new HashMap<>();
        final HashMap<Integer, String> idMap = new HashMap<>();
        stringArray.add("All Routes");
        map.put(0, "All Routes");
        for (int i = 1, count = routeCodesArray.length(); i <= count; i++) {

            try {
                idsArray.add(routeCodesArray.get(i - 1).toString());
                idMap.put(i - 1, routeCodesArray.get(i - 1).toString());
                routesDataList = db.getRouteDataByRouteId(routeCodesArray.get(i - 1).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (routesDataList.size() > 0) {
                //System.out.println("routesDataList.get(1).toString() :: " + routesDataList.get(1).toString());
                stringArray.add(routesDataList.get(1).toString());
                map.put(i, routesDataList.get(1).toString());
            }
        }



        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        stringArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        //paymentTypeSpinner.setPrompt("Select routecode");
        moreinfo_menubarSpinner.setAdapter(spinnerArrayAdapter);

        multispinner.setAdapter(spinnerArrayAdapter);

        moreinfo_menubarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {

                } else {
                    selected_val = idsArray.get(i - 1).toString();
                    //selectedroute=routesDataList.get(i - 1).toString();

                    mmSharedPreferences.putString("routename", selected_val);
                    System.out.println("ROUTE JSON OBJ 22:: " + selected_val.toString());
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        for (int i = 0; i < dates.length; i++) {
            Nextdayindent_moreinfoBeen MoreInfobeen = new Nextdayindent_moreinfoBeen(dates[i], milk[i], curd[i],other[i]);
            nextdayIndentMoreInfo.add(MoreInfobeen);
        }

        moreinfo_listView = (ListView) findViewById(R.id.nextday_indent_moreinfoList);
        Nextdayindent_MoreinfoAdapter adapter = new Nextdayindent_MoreinfoAdapter(this, nextdayIndentMoreInfo);
        moreinfo_listView.setAdapter(adapter);
        //moreinfo_listView.setOnItemClickListener(this);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


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

        menu.findItem(R.id.date).setVisible(false);
        menu.findItem(R.id.notifications).setVisible(false);
        menu.findItem(R.id.settings).setVisible(false);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);

        menu.findItem( R.id.autorenew).setVisible(true);
        menu.findItem(R.id.sort).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(NextIndent_Moreinfo.this, DashboardActivity.class);
        startActivity(i);
        finish();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {

        } else {
            selected_val = idsArray.get(position - 1).toString();
            //selectedroute=routesDataList.get(i - 1).toString();

            mmSharedPreferences.putString("routename", selected_val);
            System.out.println("ROUTE JSON OBJ 22:: " + selected_val.toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

