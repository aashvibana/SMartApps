package com.easytech.smartstaffapp.pojo;

/**
 * Created by Bana on 13/4/16.
 */
public class TimeSlot {

    private Long Id;
    private String deliveryScheduleDate;
    private int timeSlotNum;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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
                "Id=" + Id +
                ", deliveryScheduleDate='" + deliveryScheduleDate + '\'' +
                ", timeSlotNum=" + timeSlotNum +
                '}';
    }
}
