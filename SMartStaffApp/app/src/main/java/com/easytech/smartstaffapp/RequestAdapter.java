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

import com.easytech.smartstaffapp.pojo.DeliveryOrder;
import com.easytech.smartstaffapp.pojo.Request;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Bana on 14/4/16.
 */
public class RequestAdapter extends BaseAdapter {

    private List<Request> mRequestList;
    private LayoutInflater mInflater;

    public RequestAdapter(List<Request> list, LayoutInflater inflater) {
        mRequestList = list;
        mInflater = inflater;
    }

    @Override
    public int getCount() {
        return mRequestList.size();
    }

    @Override
    public Object getItem(int position) {
        return mRequestList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewRequest request;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_request, null);
            request = new ViewRequest();

            request.RequestId = (TextView) convertView.findViewById(R.id.request_id);
            request.RequestName = (TextView) convertView.findViewById(R.id.request_name);
            request.RequestImg = (ImageView) convertView.findViewById(R.id.request_img);
            request.RequestStore = (TextView) convertView.findViewById(R.id.request_store);
            request.RequestQuantity = (TextView) convertView.findViewById(R.id.request_quantity);
            convertView.setTag(request);
        } else {
            request = (ViewRequest) convertView.getTag();
        }

        final Request curReq = mRequestList.get(position);

        new AsyncTask<Void, Void, Void>() {

            private Bitmap bmp;

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = new URL(curReq.getImg()).openStream();
                    bmp = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (bmp != null)
                    request.RequestImg.setImageBitmap(bmp);
            }

        }.execute();

        request.RequestId.setText(String.valueOf(curReq.getId()));
        request.RequestQuantity.setText(String.valueOf(curReq.getQuantity()));

        if(curReq.getStore() == null)
            request.RequestStore.setVisibility(View.GONE);
        else
            request.RequestStore.setText(curReq.getStore());

        return convertView;
    }

    private class ViewRequest {
        TextView RequestId;
        TextView RequestName;
        ImageView RequestImg;
        TextView RequestStore;
        TextView RequestQuantity;
    }
}
