package com.easytech.smartstaffapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.easytech.smartstaffapp.pojo.PickingList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ListPickListActivity extends AppCompatActivity {

    private ListView listview;
    private String jsonString;
    private List<PickingList> mPickList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pick_list);

        listview = (ListView) findViewById(R.id.picklistview);

        jsonString = getIntent().getStringExtra(Constants.itemList);

        //System.out.println(jsonString);

        Gson gson = new Gson();
        mPickList = gson.fromJson(jsonString, new TypeToken<List<PickingList>>() {
        }.getType());

        if(mPickList.isEmpty()) Toast.makeText(this, "No Items match your search word", Toast.LENGTH_LONG).show();

        else if (mPickList.size() == 1) goToPickListDetailsActivity();

        else {
            listview.setAdapter(new PickListAdapter(mPickList, getLayoutInflater()));
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent productDetailsIntent = new Intent(getBaseContext(), PickListDetailsActivity.class);
                    productDetailsIntent.putExtra(Constants.pick, mPickList.get(position));
                    startActivity(productDetailsIntent);
                }
            });
        }
    }

    private void goToPickListDetailsActivity() {
        Intent intent = new Intent(this, PickListDetailsActivity.class);
        intent.putExtra(Constants.item, mPickList.get(0));
        startActivity(intent);
    }
}
