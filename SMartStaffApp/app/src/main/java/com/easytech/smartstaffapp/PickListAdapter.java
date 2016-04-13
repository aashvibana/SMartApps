package com.easytech.smartstaffapp;

/**
 * Created by Bana on 11/4/16.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.easytech.smartstaffapp.pojo.PickingList;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Bana on 8/4/16.
 */

public class PickListAdapter extends BaseAdapter {

    private List<PickingList> mPickList;
    private LayoutInflater mInflater;

    public PickListAdapter(List<PickingList> list, LayoutInflater inflater) {
        mPickList = list;
        mInflater = inflater;
    }

    @Override
    public int getCount() {
        return mPickList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPickList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewPickList pickList;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_pick_list, null);
            pickList = new ViewPickList();

            pickList.PickListId = (TextView) convertView.findViewById(R.id.picklist_id);
            pickList.PickListTime = (TextView) convertView.findViewById(R.id.picklist_time);
            convertView.setTag(pickList);
        } else {
            pickList = (ViewPickList) convertView.getTag();
        }

        final PickingList curPick = mPickList.get(position);

        pickList.PickListId.setText(String.valueOf(curPick.getId()));
        pickList.PickListTime.setText(String.valueOf(curPick.getPickingTime()));

        return convertView;
    }

    private class ViewPickList {
        TextView PickListId;
        TextView PickListTime;

    }

}