package com.easytech.smartstaffapp.pojo;

import java.util.List;

/**
 * Created by Bana on 13/4/16.
 */
public class TimeSlot {

    private Long id;
    private String deliveryScheduleDate;
    private int timeSlotNum;

    private List<DeliveryOrder> deliveryOrders;

    public List<DeliveryOrder> getDeliveryOrders() {
        return deliveryOrders;
    }

    public void setDeliveryOrders(List<DeliveryOrder> deliveryOrders) {
        this.deliveryOrders = deliveryOrders;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeliveryScheduleDate() {
        return deliveryScheduleDate;
    }

    public void setDeliveryScheduleDate(String deliveryScheduleDate) {
        this.deliveryScheduleDate = deliveryScheduleDate;
    }

    public int getTimeSlotNum() {
        return timeSlotNum;
    }

    public void setTimeSlotNum(int timeSlotNum) {
        this.timeSlotNum = timeSlotNum;
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "id=" + id +
                ", deliveryScheduleDate='" + deliveryScheduleDate + '\'' +
                ", timeSlotNum=" + timeSlotNum +
                '}';
    }
}
