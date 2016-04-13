package com.easytech.smartstaffapp.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bana on 14/4/16.
 */
public class DeliverySchedule {

    private Long id;
    private Long deliveryDate;

    private List<TimeSlot> timeSlots;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Long deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    @Override
    public String toString() {
        return "DeliverySchedule{" +
                "id=" + id +
                ", deliveryDate=" + deliveryDate +
                ", timeSlots=" + timeSlots +
                '}';
    }
}
