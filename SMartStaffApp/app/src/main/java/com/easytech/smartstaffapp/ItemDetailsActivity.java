package com.easytech.smartstaffapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easytech.smartstaffapp.pojo.Employee;
import com.easytech.smartstaffapp.pojo.Item;
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

    private LocationTask mAuthTask = null;

    private ImageView imageView;
    private TextView upcView;
    private TextView descView;
    private TextView priceView;
    private TextView catView;
    private TextView nameView;
    private TextView locationView;

    private Employee employee;

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

        SharedPreferences sharedpreferences = getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE);
        String customerJson = sharedpreferences.getString(Constants.Customer, null);
        if(customerJson == null) {
            goToLoginActivity();
        } else {
            Gson gson = new Gson();
            employee = gson.fromJson(customerJson, Employee.class);

            System.out.println("Employee : " + employee.toString());
        }


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

        if(employee.getWarehouseId() != null) {
            mAuthTask = new LocationTask(this);
            mAuthTask.execute((Void) null);
        } else {
            locationView.setText("-");
        }

        upcView.setText(item.getItemUpc());
        descView.setText(item.getItemDesc());
        catView.setText(item.getCategory() + " - " + item.getSubCategory());
        nameView.setText(item.getItemName());
        priceView.setText(String.format("$%.2f", item.getItemPrice()));
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public class LocationTask extends AsyncTask<Void, Void, Boolean> {

        private Context context;
        private String output;

        LocationTask(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(Constants.urlString + "RetrieveWarehouseStorageLocation?itemUpc=" + item.getItemUpc() + "&warehouseId=" + employee.getWarehouseId());

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                output = EntityUtils.toString(httpEntity);

                //System.out.println(output);

                if (output == null) return false;
                else return true;
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
                locationView.setText(output);
            } else {
                Toast.makeText(context, "Location not available for this warehouse.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
