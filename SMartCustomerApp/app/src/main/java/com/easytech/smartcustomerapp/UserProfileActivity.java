package com.easytech.smartcustomerapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.easytech.smartcustomerapp.pojo.Customer;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static Toolbar toolbar;
    private static CollapsingToolbarLayout collapsingToolbar;

    int mutedColor = R.attr.colorPrimary;
    private Customer customer;

    private TextView mEmailView;
    private TextView mNameView;
    private TextView mGenderView;
    private TextView mRaceView;
    private TextView mHomeAddressView;
    private TextView mContactView;
    private TextView mDobView;
    private TextView mMembershipView;
    private TextView mPointsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        mNameView = (TextView) findViewById(R.id.user_name);
        mEmailView = (TextView) findViewById(R.id.user_email);
        mGenderView = (TextView) findViewById(R.id.user_gender);
        mRaceView = (TextView) findViewById(R.id.user_race);
        mHomeAddressView = (TextView) findViewById(R.id.user_address);
        mContactView = (TextView) findViewById(R.id.user_contact);
        mDobView = (TextView) findViewById(R.id.user_dob);
        mMembershipView = (TextView) findViewById(R.id.user_membership);
        mPointsView = (TextView) findViewById(R.id.user_points);

        SharedPreferences sharedpreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
        String customerJson = sharedpreferences.getString(Constants.Customer, null);

//        System.out.println("Userprofile: " + customerJson);

        if (customerJson == null) {
            goToLoginActivity();
        } else {
            Gson gson = new Gson();
            customer = gson.fromJson(customerJson, Customer.class);

            if (customer.getFname() != null && customer.getLname() != null) {
                collapsingToolbar.setTitle(customer.getFname() + " " + customer.getLname());
                mNameView.setText(customer.getFname() + " " + customer.getLname());
            }
            if (customer.getEmail() != null)
                mEmailView.setText(customer.getEmail());
            if (customer.getGender() != null)
                mGenderView.setText(customer.getGender());
            if (customer.getRace() != null)
                mRaceView.setText(customer.getRace());
            if (customer.getAddress() != null && customer.getPostalCode() != null)
                mHomeAddressView.setText(customer.getAddress() + ", " + customer.getPostalCode());
            if (customer.getContact() != null)
                mContactView.setText(customer.getContact());
            if (customer.getDob() != null) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(Constants.dateFormat);
                    mDobView.setText(sdf.format(customer.getDob().toString()));
                } catch (Exception e) {
                    mDobView.setText(customer.getDob().toString());
                }
            }
            if (customer.getMembershipId() != 0)
                mMembershipView.setText(customer.getMembershipId().toString());
            mPointsView.setText(customer.getLoyaltyPoints().toString());
        }

        ImageView header = (ImageView) findViewById(R.id.header);

        collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbar.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimaryDark));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToEditActivity();
            }
        });
    }

    private void goToEditActivity() {
        Intent intent = new Intent(this, EditUserActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        goToMainActivity();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
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
