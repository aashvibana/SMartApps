package com.easytech.smartstaffapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easytech.smartstaffapp.pojo.DeliveryOrder;
import com.easytech.smartstaffapp.pojo.PickingList;

import java.util.List;

/**
 * Created by Bana on 13/4/16.
 */
public class DeliveryOrderAdapter extends BaseAdapter {

    private List<DeliveryOrder> mDeliveryOrder;
    private LayoutInflater mInflater;

    public DeliveryOrderAdapter(List<DeliveryOrder> list, LayoutInflater inflater) {
        mDeliveryOrder = list;
        mInflater = inflater;
    }

    @Override
    public int getCount() {
        return mDeliveryOrder.size();
    }

    @Override
    public Object getItem(int position) {
        return mDeliveryOrder.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewDeliveryOrder deliveryOrder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_do, null);
            deliveryOrder = new ViewDeliveryOrder();

            deliveryOrder.DeliveryOrderId = (TextView) convertView.findViewById(R.id.do_id);
            deliveryOrder.DeliveryOrderDateTime = (TextView) convertView.findViewById(R.id.do_date);
            deliveryOrder.DeliveryOrderPath = (TextView) convertView.findViewById(R.id.do_path);
            deliveryOrder.DeliveryOrderType = (TextView) convertView.findViewById(R.id.do_type);
            convertView.setTag(deliveryOrder);
        } else {
            deliveryOrder = (ViewDeliveryOrder) convertView.getTag();
        }

        final DeliveryOrder curDO = mDeliveryOrder.get(position);

        deliveryOrder.DeliveryOrderId.setText(String.valueOf(curDO.getId()));



        if(curDO.getDeliveryTimeSlot().getTimeSlotNum() != 0)
            deliveryOrder.DeliveryOrderDateTime.setText(String.valueOf(curDO.getDeliveryDate() +", " + curDO.getDeliveryTimeSlot().getTimeSlotNum()));
        else
            deliveryOrder.DeliveryOrderDateTime.setText(String.valueOf(curDO.getDeliveryDate()));
        deliveryOrder.DeliveryOrderPath.setText(curDO.getOrigin() + " - " + curDO.getDestination());
        deliveryOrder.DeliveryOrderType.setText(curDO.getType());

        return convertView;
    }

    private class ViewDeliveryOrder {
        TextView DeliveryOrderId;
        TextView DeliveryOrderPath; //origin - destination
        TextView DeliveryOrderDateTime;
        TextView DeliveryOrderType;
    }
}
