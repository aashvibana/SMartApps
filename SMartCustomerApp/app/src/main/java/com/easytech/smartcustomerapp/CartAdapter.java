package com.easytech.smartcustomerapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.easytech.smartcustomerapp.pojo.Item;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Bana on 13/4/16.
 */
public class CartAdapter extends BaseAdapter {
    private List<Item> mItemList;
    private LayoutInflater mInflater;
    private ShoppingCart shoppingCart = new ShoppingCart();

    public CartAdapter(List<Item> list, LayoutInflater inflater) {
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

            item.ItemImage = (ImageView) convertView.findViewById(R.id.item_img);
            item.ItemName = (TextView) convertView.findViewById(R.id.item_name);
            item.ItemDesc = (TextView) convertView.findViewById(R.id.item_description);
            item.ItemPrice = (TextView) convertView.findViewById(R.id.item_price);
            convertView.setTag(item);
        } else {
            item = (ViewItem) convertView.getTag();
        }

        final Item curItem = mItemList.get(position);

        new AsyncTask<Void, Void, Void>() {

            private Bitmap bmp;

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = new URL(curItem.getItemImage()).openStream();
                    bmp = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (bmp != null)
                    item.ItemImage.setImageBitmap(bmp);
            }

        }.execute();

        item.ItemName.setText(curItem.getItemName());
        item.ItemDesc.setText(curItem.getItemDesc());
        item.ItemPrice.setText(String.format("$%.2f", curItem.getItemPrice() * (double)shoppingCart.getItemQty(curItem)));

        return convertView;
    }

    private class ViewItem {
        ImageView ItemImage;
        TextView ItemName;
        TextView ItemDesc;
        TextView ItemPrice;

    }
}
