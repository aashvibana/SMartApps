package com.easytech.smartcustomerapp.pojo;

import java.util.List;

/**
 * Created by Bana on 11/4/16.
 */
public class Deal {

    private Long id;
    private String name;
    private String desc;
    private String dealType; //discount or package
    private String imgLocation;
    private String startDate;
    private String endDate;

    private List<Item> itemList;
    private double priceOff;

    private int percent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public String getImgLocation() {
        return imgLocation;
    }

    public void setImgLocation(String imgLocation) {
        this.imgLocation = imgLocation;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public double getPriceOff() {
        return priceOff;
    }

    public void setPriceOff(double priceOff) {
        this.priceOff = priceOff;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }


    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Deal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", dealType='" + dealType + '\'' +
                ", itemList=" + itemList +
                ", priceOff=" + priceOff +
                ", percent=" + percent +
                '}';
    }
}
