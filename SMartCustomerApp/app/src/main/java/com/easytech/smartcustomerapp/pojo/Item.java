package com.easytech.smartcustomerapp.pojo;

import java.io.Serializable;

/**
 * Created by Bana on 8/4/16.
 */
public class Item implements Serializable {

    private String itemUpc;
    private String category;
    private String subCategory;
    private String itemName;
    private String itemDesc;
    private double itemPrice;
    private String itemImage;

    public Item(String itemUpc, String category, String subCategory, String itemName, String itemDesc, double itemPrice, String itemImage) {
        this.itemUpc = itemUpc;
        this.category = category;
        this.subCategory = subCategory;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.itemPrice = itemPrice;
        this.itemImage = itemImage;
    }

    public String getItemUpc() {
        return itemUpc;
    }

    public void setItemUpc(String itemUpc) {
        this.itemUpc = itemUpc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemUpc='" + itemUpc + '\'' +
                ", category='" + category + '\'' +
                ", subCategory='" + subCategory + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemDesc='" + itemDesc + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemImage='" + itemImage + '\'' +
                '}';
    }
}

