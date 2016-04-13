package com.easytech.smartstaffapp.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Bana on 12/4/16.
 */
public class DeliveryOrder implements Serializable{
    private Long id;
    private Long warehouseId;
    private String origin;
    private String destination;
    private Long deliveryDate;

    private TimeSlot deliveryTimeSlot;

    private Long dateCancelled;

    private String type;
    private Long totalCages;
    private Long totalPallets;

    private List<Request> requests;
    private List<Request> items;

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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Long getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Long deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public TimeSlot getDeliveryTimeSlot() {
        return deliveryTimeSlot;
    }

    public void setDeliveryTimeSlot(TimeSlot deliveryTimeSlot) {
        this.deliveryTimeSlot = deliveryTimeSlot;
    }

    public Long getDateCancelled() {
        return dateCancelled;
    }

    public void setDateCancelled(Long dateCancelled) {
        this.dateCancelled = dateCancelled;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTotalCages() {
        return totalCages;
    }

    public void setTotalCages(Long totalCages) {
        this.totalCages = totalCages;
    }

    public Long getTotalPallets() {
        return totalPallets;
    }

    public void setTotalPallets(Long totalPallets) {
        this.totalPallets = totalPallets;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public List<Request> getItems() {
        return items;
    }

    public void setItems(List<Request> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "DeliveryOrder{" +
                "id=" + id +
                ", warehouseId=" + warehouseId +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", deliveryDate=" + deliveryDate +
                ", deliveryTimeSlot=" + deliveryTimeSlot.toString() +
                ", dateCancelled=" + dateCancelled +
                ", type='" + type + '\'' +
                ", totalCages=" + totalCages +
                ", totalPallets=" + totalPallets +
                ", requests=" + requests +
                ", items=" + items +
                '}';
    }
}
