package com.rightclickit.b2bsaleon.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.adapters.NotificationAdapter;
import com.rightclickit.b2bsaleon.beanclass.NotificationItem;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {
    public static final String[] name= new String[] { "Sales"};

    public static final String[] description = new String[] {
            "Be the first to know about discounts and offers at B2BSaleON ! Click here to subscribe"};

    public static final String[] date = new String[] {"12/2/2017"};

    ListView listView;
    List<NotificationItem> rowItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        rowItems = new ArrayList<NotificationItem>();
        for (int i = 0; i < name.length; i++) {
            NotificationItem item = new NotificationItem(name[i], description[i], date[i]);
            rowItems.add(item);
        }

        listView = (ListView) findViewById(R.id.list);
        NotificationAdapter adapter = new NotificationAdapter(this,
                R.layout.notigicationadapter, rowItems);
        listView.setAdapter(adapter);
      //  listView.setOnItemClickListener(this);
    }


    }

