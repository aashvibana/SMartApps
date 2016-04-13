package com.easytech.smartcustomerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.easytech.smartcustomerapp.pojo.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ItemListActivity extends AppCompatActivity {

    private ListView listview;
    private String jsonString;
    private List<Item> mItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listview = (ListView) findViewById(R.id.itemlistview);

        jsonString = getIntent().getStringExtra(Constants.itemList);

        System.out.println(jsonString);

        Gson gson = new Gson();
        mItemList = gson.fromJson(jsonString, new TypeToken<List<Item>>() {
        }.getType());

        if(mItemList.isEmpty()) Toast.makeText(this, "No Items match your search word", Toast.LENGTH_LONG).show();

        else if (mItemList.size() == 1) goToItemDetailsActivity();

        else listview.setAdapter(new ItemAdapter(mItemList, getLayoutInflater()));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent productDetailsIntent = new Intent(getBaseContext(), ItemDetailsActivity.class);
                productDetailsIntent.putExtra(Constants.item, mItemList.get(position));
                startActivity(productDetailsIntent);
            }
        });

    }

    private void goToItemDetailsActivity() {
        Intent intent = new Intent(this, ItemDetailsActivity.class);
        intent.putExtra(Constants.item, mItemList.get(0));
        startActivity(intent);
    }
}
