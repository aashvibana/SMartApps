package com.easytech.smartstaffapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.easytech.smartstaffapp.pojo.Item;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Bana on 14/4/16.
 */
public class poItemsAdapter extends BaseAdapter {
    private List<Item> mItemList;
    private LayoutInflater mInflater;

    public poItemsAdapter(List<Item> list, LayoutInflater inflater) {
        mItemList = list;
        mInflater = inflater;
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewItem item;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_item, null);
            item = new ViewItem();

            item.ItemUpc = (TextView) convertView.findViewById(R.id.item_upc);
            item.ItemName = (TextView) convertView.findViewById(R.id.item_name);
            item.ItemQuantity = (TextView) convertView.findViewById(R.id.item_quantity);
            item.ItemPrice = (TextView) convertView.findViewById(R.id.item_price);
            convertView.setTag(item);
        } else {
            item = (ViewItem) convertView.getTag();
        }

        final Item curItem = mItemList.get(position);

        item.ItemUpc.setText(curItem.getItemUpc());
        item.ItemName.setText(curItem.getItemName());
        item.ItemQuantity.setText(curItem.getQuantity());
        item.ItemPrice.setText(String.format("$%.2f", curItem.getItemPrice()));

        return convertView;
    }

    private class ViewItem {
        TextView ItemUpc;
        TextView ItemName;
        TextView ItemQuantity;
        TextView ItemPrice;

    }
}
