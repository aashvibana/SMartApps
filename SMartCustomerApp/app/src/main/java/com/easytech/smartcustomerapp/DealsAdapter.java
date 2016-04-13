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

import com.easytech.smartcustomerapp.pojo.Deal;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Bana on 11/4/16.
 */
public class DealsAdapter extends BaseAdapter {

    private List<Deal> deals;
    private LayoutInflater mInflater;

    public DealsAdapter(List<Deal> list, LayoutInflater inflater) {
        deals = list;
        mInflater = inflater;
    }

    @Override
    public int getCount() {
        return deals.size();
    }

    @Override
    public Object getItem(int position) {
        return deals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewDeal deal;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_deal, null);
            deal = new ViewDeal();

            deal.DealImage = (ImageView) convertView.findViewById(R.id.deal_img);
            deal.DealName = (TextView) convertView.findViewById(R.id.deal_name);
            deal.DealDesc = (TextView) convertView.findViewById(R.id.deal_description);
            deal.DealTime = (TextView) convertView.findViewById(R.id.deal_date);
            convertView.setTag(deal);
        } else {
            deal = (ViewDeal) convertView.getTag();
        }

        final Deal curDeal = deals.get(position);

        new AsyncTask<Void, Void, Void>() {

            private Bitmap bmp;

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = new URL(curDeal.getImgLocation()).openStream();
                    bmp = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (bmp != null)
                    deal.DealImage.setImageBitmap(bmp);
            }

        }.execute();
        deal.DealName.setText(curDeal.getName());
        deal.DealDesc.setText(curDeal.getDesc());
        deal.DealTime.setText(curDeal.getStartDate() + " - " + curDeal.getEndDate());

        return convertView;
    }

    private class ViewDeal {
        ImageView DealImage;
        TextView DealName;
        TextView DealDesc;
        TextView DealTime;
    }

}
