package com.easytech.smartcustomerapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.TextView;

import com.easytech.smartcustomerapp.pojo.Customer;
import com.easytech.smartcustomerapp.pojo.Deal;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Customer customer;
    private TextView mCustomerName;
    private TextView mCustomerEmail;

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

        mCustomerEmail = (TextView) navigationView.findViewById(R.id.customer_email);
        mCustomerName = (TextView) navigationView.findViewById(R.id.customer_name);

        SharedPreferences sharedpreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
        String customerJson = sharedpreferences.getString(Constants.Customer, null);
        if(customerJson == null) {
            goToLoginActivity();
        } else {
            Gson gson = new Gson();
            customer = gson.fromJson(customerJson, Customer.class);

            //System.out.println("Customer : " + customer.toString());

            fullName = customer.getFname() + " " + customer.getLname();

            if (fullName != null) mCustomerName.setText(fullName);
            if(customer.getEmail() != null) mCustomerEmail.setText(customer.getEmail());
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
        }  else if (id == R.id.action_cart) {
            goToShoppingCartActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            goToMobileScanGoActivity();
        } else if (id == R.id.nav_deal) {
            goToDealsListActivity();
        } else if (id == R.id.nav_item) {
            goToSearchItemActivity();
        } else if (id == R.id.nav_location) {

        } else if (id == R.id.nav_user) {
            goToUserProfileActivity();

        } else if (id == R.id.nav_logout) {
            SharedPreferences sharedpreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.putBoolean(Constants.loggedIn, false);
            editor.commit();
            goToLoginActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToDealsListActivity() {
        Intent intent = new Intent(this, DealsListActivity.class);
        startActivity(intent);
    }

    private void goToShoppingCartActivity() {
        Intent intent = new Intent(this, ShoppingCartActivity.class);
        startActivity(intent);
    }

    private void goToSearchItemActivity() {
        Intent intent = new Intent(this, SearchItemActivity.class);
        startActivity(intent);
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void goToMobileScanGoActivity() {
        Intent intent = new Intent(this, MobileScanGoActivity.class);
        startActivity(intent);
    }

    private void goToUserProfileActivity() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }
}
