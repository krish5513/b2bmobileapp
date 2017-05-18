package com.rightclickit.b2bsaleon.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
//       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
  //      setSupportActionBar(toolbar);


        this.getSupportActionBar().setLogo(R.drawable.dashboard_icon_white_24);
        this.getSupportActionBar().setTitle("DASHBOARD");
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.notifications) {
            Toast.makeText(this, "Clicked on Notifications...", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.settings) {

            loadSettings();
            Toast.makeText(this, "Clicked on Settings...", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadSettings() {
        Intent settingsIntent = new Intent(DashboardActivity.this, SettingsActivity.class);
        // mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // if you keep these flags white screen is coming on Intent navigation
        startActivity(settingsIntent);
        finish();
    }
}
