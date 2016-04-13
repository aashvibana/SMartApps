package com.easytech.smartstaffapp.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Bana on 11/4/16.
 */
public class PickingList implements Serializable {

    private Long id;
    private Long warehouseId;
    private Long deliveryOrderId;
    private ArrayList<Item> items = new ArrayList();
    private String loadingBay;
    private Long pickingTime;
    private Long dateCreated;
    private Long dateTimePicked;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Long getDeliveryOrderId() {
        return deliveryOrderId;
    }

    public void setDeliveryOrderId(Long deliveryOrderId) {
        this.deliveryOrderId = deliveryOrderId;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public String getLoadingBay() {
        return loadingBay;
    }

    public void setLoadingBay(String loadingBay) {
        this.loadingBay = loadingBay;
    }

    public Long getPickingTime() {
        return pickingTime;
    }

    public void setPickingTime(Long pickingTime) {
        this.pickingTime = pickingTime;
    }

    public Long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Long getDateTimePicked() {
        return dateTimePicked;
    }

    public void setDateTimePicked(Long dateTimePicked) {
        this.dateTimePicked = dateTimePicked;
    }
}
