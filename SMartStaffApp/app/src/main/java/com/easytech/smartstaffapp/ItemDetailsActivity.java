package com.easytech.smartstaffapp;

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

import com.easytech.smartstaffapp.pojo.Item;

import java.io.InputStream;
import java.net.URL;

public class ItemDetailsActivity extends AppCompatActivity {

    private Item item;

    private ImageView imageView;
    private TextView upcView;
    private TextView descView;
    private TextView priceView;
    private TextView catView;
    private TextView nameView;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
