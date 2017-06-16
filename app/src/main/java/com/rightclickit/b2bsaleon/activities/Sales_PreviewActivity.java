package com.rightclickit.b2bsaleon.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rightclickit.b2bsaleon.R;

public class Sales_PreviewActivity extends AppCompatActivity {

    FloatingActionButton fab;

    TextView Previewprint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_sales__preview);

        this.getSupportActionBar().setTitle("RETAILERS/CONSUMER");
        this.getSupportActionBar().setSubtitle(null);
        //this.getSupportActionBar().setLogo(R.drawable.sales_white);
        // this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        this.getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);


        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator( R.drawable.ic_arrow_back_black_24dp);



        fab = (FloatingActionButton) findViewById(R.id.customerfab);
        fab.setVisibility(View.VISIBLE);
        fab.setImageDrawable( ContextCompat.getDrawable(getApplicationContext(), R.drawable.customer60));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Clicked Consumer Add",Toast.LENGTH_SHORT).show();
                showInputDialog();

            }
        });




        Previewprint=(TextView) findViewById( R.id.tv_preview_print );


        Previewprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Sales_PreviewActivity.this,Sales_Preview_PrintActivity.class);
                startActivity(i);
                finish();
            }
        });






    }




    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(Sales_PreviewActivity.this);
        View promptView = layoutInflater.inflate(R.layout.consumer_adddialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Sales_PreviewActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText name = (EditText) promptView.findViewById(R.id.name);
        final EditText mob = (EditText) promptView.findViewById(R.id.mobileno);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //resultText.setText("Hello, " + editText.getText());
                    }
                });


        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }










    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_dashboard, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Add) {
           // Intent i =new Intent(Sales_PreviewActivity.this,Sales_Retailors_AddActivity.class);
           // startActivity(i);
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


        menu.findItem( R.id.notifications).setVisible(false);
        menu.findItem( R.id.settings).setVisible(false);
        menu.findItem( R.id.logout).setVisible(false);
        menu.findItem( R.id.action_search).setVisible(true);
        menu.findItem( R.id.Add).setVisible(false);



        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, SalesActivity.class);
        startActivity(intent);
        finish();
    }
}
