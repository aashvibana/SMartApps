package com.easytech.smartcustomerapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.easytech.smartcustomerapp.pojo.Deal;

import java.util.ArrayList;
import java.util.List;

public class DealsListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private List<Deal> dealList;

    private ListView listView;

    private static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals_list);

        initializeDealList();

        listView = (ListView) findViewById(R.id.deals_list);
        if(dealList.isEmpty()) Toast.makeText(this, "No offers available", Toast.LENGTH_LONG).show();

        else listView.setAdapter(new DealsAdapter(dealList, getLayoutInflater()));

    }

    private void initializeDealList() {
        if(dealList == null) {
            dealList = new ArrayList<>();
            Deal deal = new Deal();
            deal.setId(Long.parseLong("1"));
            deal.setDealType("Discount");
            deal.setName("April Bonanza");
            deal.setDesc("Get 10% off all purchases for the month of April");
            deal.setImgLocation("http://d30y9cdsu7xlg0.cloudfront.net/png/10992-200.png");
            deal.setPercent(10);
            deal.setStartDate("01/04/2016");
            deal.setEndDate("30/04/2016");

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_offer)
                            .setContentTitle(deal.getName())
                            .setContentText(deal.getDesc());

            Intent resultIntent = new Intent(this, DealsListActivity.class);
            // Because clicking the notification opens a new ("special") activity, there's
            // no need to create an artificial back stack.
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            // Sets an ID for the notification
            int mNotificationId = ++count;
            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());

            dealList.add(deal);
            Deal deal1 = new Deal();
            deal1.setId(Long.parseLong("1"));
            deal1.setDealType("Package");
            deal1.setName("Buy 1 get 1 free");
            deal1.setDesc("Get a free Head & Shoulders Shampoo when you purchase Head & Shoulders Shampoo!");
            deal1.setImgLocation("http://www.jabongsale.com/wp-content/uploads/2013/06/buy-1-300x265.png");
            deal1.setPercent(50);
            deal1.setStartDate("05/04/2016");
            deal1.setEndDate("05/05/2016");
            dealList.add(deal1);
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

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
