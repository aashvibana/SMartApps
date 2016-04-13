package com.easytech.smartcustomerapp;

import com.easytech.smartcustomerapp.pojo.Item;

import java.util.ArrayList;
import java.util.Hashtable;

public class ShoppingCart {

    private static ArrayList<Item> items;
    private static Hashtable itemHt;

    private static double total = 0;
    private static double shippingFee = 0;

    public static double getShippingFee() {
        return shippingFee;
    }

    public static void setShippingFee(double shippingFee) {
        ShoppingCart.shippingFee = shippingFee;
    }

    public double calculateTotal() {
        total = 0;
        if(items == null) items = new ArrayList<>();
        if(itemHt == null) itemHt = new Hashtable();

        for(Item item : items) {
            total += item.getItemPrice() * getItemQty(item);
        }
        return total + shippingFee;
    }

    /**
     * @return the items
     */
    public ArrayList<Item> getItems() {
        if(items == null) items = new ArrayList<>();
        if(itemHt == null) itemHt = new Hashtable();
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(ArrayList<Item> items) {
        if(items == null) items = new ArrayList<>();
        if(itemHt == null) itemHt = new Hashtable();
        this.items = items;
    }

    public void updateItem(Item item, int qty) {

        if(items == null) items = new ArrayList<>();
        if(itemHt == null) itemHt = new Hashtable();

        int itemIndex = getItemIndex(item);

        if (qty == 0) {
            if (itemIndex != -1) {
                items.remove(itemIndex);
                itemHt.remove(item.getItemUpc());
            }
        } else {
            if (itemIndex != -1) {
                itemHt.put(item.getItemUpc(), qty);
            } else {
                items.add(item);
                itemHt.put(item.getItemUpc(), qty);
            }
        }
    }

    public int getItemQty(Item item) {

        if(items == null) items = new ArrayList<>();
        if(itemHt == null) itemHt = new Hashtable();

        int itemIndex = getItemIndex(item);
        if (itemIndex == -1) {
            return 0;
        } else {
            return (int) itemHt.get(item.getItemUpc());
        }
    }

    public int getItemIndex(Item item) {

        if(items == null) items = new ArrayList<>();
        if(itemHt == null) itemHt = new Hashtable();

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItemUpc().equals(item.getItemUpc())) {
                return i;
            }
        }
        return -1;
    }
}
