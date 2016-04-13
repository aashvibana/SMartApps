package com.easytech.smartcustomerapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easytech.smartcustomerapp.pojo.Customer;
import com.easytech.smartcustomerapp.pojo.Item;
import com.easytech.smartcustomerapp.pojo.Location;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;

public class ItemDetailsActivity extends AppCompatActivity {

    private Item item;
    private List<Location> location;

    private LocationTask mAuthTask = null;

    private ImageView imageView;
    private TextView upcView;
    private TextView descView;
    private TextView priceView;
    private TextView catView;
    private TextView nameView;
    private TextView locationView;

    private Button directionButton;

    private ShoppingCart shoppingCart = new ShoppingCart();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        item = (Item) getIntent().getSerializableExtra(Constants.item);

        imageView = (ImageView) findViewById(R.id.item_img);
        upcView = (TextView) findViewById(R.id.item_upc);
        descView = (TextView) findViewById(R.id.item_desc);
        catView = (TextView) findViewById(R.id.item_cat);
        nameView = (TextView) findViewById(R.id.item_name);
        priceView = (TextView) findViewById(R.id.item_price);
        locationView = (TextView) findViewById(R.id.item_location);
        directionButton = (Button) findViewById(R.id.item_direction);

        new AsyncTask<Void, Void, Void>() {

            private Bitmap bmp;

            @Override
            protected Void doInBackground(Void... params) {
                if (item.getItemImage() != null) {
                    try {
                        InputStream in = new URL(item.getItemImage()).openStream();
                        bmp = BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (bmp != null)
                    imageView.setImageBitmap(bmp);
                else
                    imageView.setImageResource(R.drawable.headerimg);
            }

        }.execute();

        upcView.setText(item.getItemUpc());
        descView.setText(item.getItemDesc());
        catView.setText(item.getCategory() + " - " + item.getSubCategory());
        nameView.setText(item.getItemName());
        priceView.setText(String.format("$%.2f", item.getItemPrice()));

        mAuthTask = new LocationTask(this);
        mAuthTask.execute((Void) null);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Item : " + item.toString());
                System.out.println("Qty : " + shoppingCart.getItemQty(item) + 1);
                shoppingCart.updateItem(item, shoppingCart.getItemQty(item) + 1);
                Toast.makeText(ItemDetailsActivity.this, "Item added to Cart!", Toast.LENGTH_SHORT).show();
            }
        });

        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView touch = (ImageView) findViewById(R.id.layout_image);
                touch.setImageDrawable(getDrawable(getImageByCategory(item.getCategory())));
                findViewById(R.id.scrollView).setVisibility(View.GONE);

                findViewById(R.id.layout_image).setVisibility(View.VISIBLE);
            }
        });

    }

    public class LocationTask extends AsyncTask<Void, Void, Boolean> {

        private Context context;

        LocationTask(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String output;
            try {

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(Constants.urlString + "MobileFindItemLoc?itemUpc=" + item.getItemUpc() + "&postalCode=" + 150115);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                output = EntityUtils.toString(httpEntity);

                //System.out.println(output);

                if (output == null) return false;
                else {
                    System.out.println("Location" + output);
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    location = gson.fromJson(output, new TypeToken<List<Location>>() {
                    }.getType());
                    return true;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return false;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                String print = "";
                for(Location loc : location) print += loc.getShelfName() + " ";
                locationView.setText(print);
            } else {
               Toast.makeText(context, "Location not available.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    private int getImageByCategory(String category) {
        if (category.equalsIgnoreCase("Baby Products"))
            return R.drawable.baby;
        else if (category.equalsIgnoreCase("Beverages"))
            return R.drawable.beverages;
        else if (category.equalsIgnoreCase("Breakfast & Dairy"))
            return R.drawable.dairy;
        else if (category.equalsIgnoreCase("Canned Food"))
            return R.drawable.canned;
        else if (category.equalsIgnoreCase("Cartons Deals"))
            return R.drawable.carton;
        else if (category.equalsIgnoreCase("Chilled & Convenience"))
            return R.drawable.chilled;
        else if (category.equalsIgnoreCase("Condiments"))
            return R.drawable.condiments;
        else if (category.equalsIgnoreCase("Desserts"))
            return R.drawable.desserts;
        else if (category.equalsIgnoreCase("Dried Food & Baking"))
            return R.drawable.baking;
        else if (category.equalsIgnoreCase("Fruits"))
            return R.drawable.produce;
        else if (category.equalsIgnoreCase("Health & Wellness"))
            return R.drawable.health;
        else if (category.equalsIgnoreCase("Home Appliances & Stationery"))
            return R.drawable.home;
        else if (category.equalsIgnoreCase("Household Items"))
            return R.drawable.home;
        else if (category.equalsIgnoreCase("Meat, Poultry & Seafood"))
            return R.drawable.meat;
        else if (category.equalsIgnoreCase("Personal Care"))
            return R.drawable.personal;
        else if (category.equalsIgnoreCase("Rice, Noodles & Oil"))
            return R.drawable.essential;
        else if (category.equalsIgnoreCase("Snacks & Confectioneries"))
            return R.drawable.snacks;
        else if (category.equalsIgnoreCase("Vegetables"))
            return R.drawable.produce;
        else return R.drawable.blank;
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
        } else if (id == R.id.action_cart) {
            goToShoppingCartActivity();
        }

        return super.onOptionsItemSelected(item);
    }


    private void goToShoppingCartActivity() {
        Intent intent = new Intent(this, ShoppingCartActivity.class);
        startActivity(intent);
    }
}
