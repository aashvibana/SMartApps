package com.easytech.smartstaffapp;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.easytech.smartstaffapp.pojo.Employee;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static int count = 0;
    private TextView mEmployeeId;
    private TextView mEmployeeName;
    private Employee employee;

    private String fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigationView);

        mEmployeeId = (TextView) navigationView.findViewById(R.id.employee_id);
        mEmployeeName = (TextView) navigationView.findViewById(R.id.employee_name);

        SharedPreferences sharedpreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
        String customerJson = sharedpreferences.getString(Constants.Customer, null);
        if(customerJson == null) {
            goToLoginActivity();
        } else {
            Gson gson = new Gson();
            employee = gson.fromJson(customerJson, Employee.class);

            System.out.println("Employee : " + employee.toString());

            fullName = employee.getFname() + " " + employee.getLname();

            if (fullName != null) mEmployeeName.setText(fullName);
            if(employee.getNric() != null) mEmployeeId.setText(employee.getNric().toString());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_logout) {
            SharedPreferences sharedpreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.putBoolean(Constants.loggedIn, false);
            editor.commit();
            goToLoginActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_purchaseorder) {
            goToPurchaseOrderActivity();
        } else if (id == R.id.nav_picklist) {
            goToPickListActivity();
        } else if (id == R.id.nav_search) {
            goToSearchItemActivity();
        } else if (id == R.id.nav_transport) {
            goToTransportSchedule();
        } else if (id == R.id.nav_delivery) {
            goToDeliveryOrderActivity();
        } else if (id == R.id.nav_shelf) {
            // get prompt.xml view
            LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.prompt, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView.findViewById(R.id.empty_shelf_input);
            final EditText userRemarks = (EditText) promptsView.findViewById(R.id.empty_shelf_remarks);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    String shelfId = userInput.getText().toString();

                                    if (shelfId == null || shelfId.isEmpty()) {
                                        userInput.setError(getString(R.string.error_field_required));
                                        userInput.requestFocus();
                                    } else {
                                        NotificationCompat.Builder mBuilder =
                                                new NotificationCompat.Builder(MainActivity.this)
                                                        .setSmallIcon(R.drawable.ic_notification_alert)
                                                        .setContentTitle("Empty Shelf Notification")
                                                        .setContentText("Shelf " + shelfId + "is empty.\nStaff remarks : " + userRemarks.getText().toString());

                                        Intent resultIntent = new Intent(MainActivity.this, MainActivity.class);
                                        // Because clicking the notification opens a new ("special") activity, there's
                                        // no need to create an artificial back stack.
                                        PendingIntent resultPendingIntent = PendingIntent.getActivity(MainActivity.this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        mBuilder.setContentIntent(resultPendingIntent);
                                        // Sets an ID for the notification
                                        int mNotificationId = ++count;
                                        // Gets an instance of the NotificationManager service
                                        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                        // Builds the notification and issues it.
                                        mNotifyMgr.notify(mNotificationId, mBuilder.build());
                                    }
                                    dialog.dismiss();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        } else if (id == R.id.nav_reporting) {
            goToReportingActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToTransportSchedule() {
        Intent intent = new Intent(this, TransportScheduleActivity.class);
        startActivity(intent);
    }

    private void goToReportingActivity() {
        Intent intent = new Intent(this, ReportingActivity.class);
        startActivity(intent);
    }
    private void goToPurchaseOrderActivity() {
        Intent intent = new Intent(this, ListPurchaseOrderActivity.class);
        startActivity(intent);
    }

    private void goToPickListActivity() {
        Intent intent = new Intent(this, PickListActivity.class);
        startActivity(intent);
    }

    private void goToDeliveryOrderActivity() {
        Intent intent = new Intent(this, ListDeliveryOrderActivity.class);
        startActivity(intent);
    }

    private void goToSearchItemActivity() {
        Intent intent = new Intent(this, SearchItemActivity.class);
        startActivity(intent);
    }

}
