package com.easytech.smartstaffapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.easytech.smartstaffapp.pojo.PurchaseOrder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Bana on 14/4/16.
 */
public class PurchaseOrderAdapter extends BaseAdapter {

    private List<PurchaseOrder> mPurchaseOrder;
    private LayoutInflater mInflater;

    public PurchaseOrderAdapter(List<PurchaseOrder> list, LayoutInflater inflater) {
        mPurchaseOrder = list;
        mInflater = inflater;
    }

    @Override
    public int getCount() {
        return mPurchaseOrder.size();
    }

    @Override
    public Object getItem(int position) {
        return mPurchaseOrder.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewPurchaseOrder purchaseOrder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_po, null);
            purchaseOrder = new ViewPurchaseOrder();

            purchaseOrder.PurchaseOrderId = (TextView) convertView.findViewById(R.id.po_id);
            purchaseOrder.PurchaseOrderTitle = (TextView) convertView.findViewById(R.id.po_title);
            purchaseOrder.PurchaseOrderApprovalDate = (TextView) convertView.findViewById(R.id.po_date);
            purchaseOrder.PurchaseOrderType = (TextView) convertView.findViewById(R.id.po_type);
            convertView.setTag(purchaseOrder);
        } else {
            purchaseOrder = (ViewPurchaseOrder) convertView.getTag();
        }

        final PurchaseOrder curPO = mPurchaseOrder.get(position);

        purchaseOrder.PurchaseOrderId.setText(String.valueOf(curPO.getId()));
        purchaseOrder.PurchaseOrderTitle.setText(curPO.getTitle());
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.dateFormat);
        purchaseOrder.PurchaseOrderApprovalDate.setText(sdf.format(curPO.getApprovalDate()));
        purchaseOrder.PurchaseOrderType.setText(curPO.getType());

        return convertView;
    }

    private class ViewPurchaseOrder {
        TextView PurchaseOrderId;
        TextView PurchaseOrderTitle; //origin - destination
        TextView PurchaseOrderApprovalDate;
        TextView PurchaseOrderType;
    }
}
