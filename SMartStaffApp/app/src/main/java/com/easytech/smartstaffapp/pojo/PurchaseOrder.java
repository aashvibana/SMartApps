package com.easytech.smartstaffapp.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Bana on 9/4/16.
 */
public class PurchaseOrder implements Serializable {

    private Long id;
    private String title;
    private String type;
    private String approvalDate;

    private String supplierName;
    private String approver;
    private String desc;
    private double amount;

    private String nextRunDate;
    private String deliveryDate;

    private List<Item> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getNextRunDate() {
        return nextRunDate;
    }

    public void setNextRunDate(String nextRunDate) {
        this.nextRunDate = nextRunDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "PurchaseOrder{" +
                "Id=" + id +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", type='" + type + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", approver='" + approver + '\'' +
                ", amount=" + amount +
                ", nextRunDate=" + nextRunDate +
                ", deliveryDate=" + deliveryDate +
                ", items=" + items.toString() +
                '}';
    }
}
