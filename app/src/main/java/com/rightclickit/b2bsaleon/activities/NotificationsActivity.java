package com.rightclickit.b2bsaleon.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.NotificationAdapter;
import com.rightclickit.b2bsaleon.beanclass.NotificationBean;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {
    public static final String[] name= new String[] { "Sales"};

    public static final String[] description = new String[] {
            "Be the first to know about discounts and offers at B2BSaleON ! Click here to subscribe"};

    public static final String[] date = new String[] {"12/2/2017"};

    ListView listView;
    List<NotificationBean> rowItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);


        this.getSupportActionBar().setTitle("NOTIFICATIONS");
        this.getSupportActionBar().setSubtitle(null);


        this.getSupportActionBar().setLogo(R.drawable.ic_notifications_black_24dp);

        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);


        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);


        rowItems = new ArrayList<NotificationBean>();
        for (int i = 0; i < name.length; i++) {
            NotificationBean item = new NotificationBean(name[i], description[i], date[i]);
            rowItems.add(item);
        }

        listView = (ListView) findViewById(R.id.list);
        NotificationAdapter adapter = new NotificationAdapter(this,
                R.layout.notigicationadapter, rowItems);
        listView.setAdapter(adapter);
      //  listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
   if (id == R.id.action_search) {

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
        menu.findItem( R.id.Add).setVisible(false);

        menu.findItem( R.id.autorenew).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }


    }

