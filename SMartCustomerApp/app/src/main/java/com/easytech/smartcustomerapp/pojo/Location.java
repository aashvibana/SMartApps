package com.easytech.smartcustomerapp.pojo;

/**
 * Created by Bana on 13/4/16.
 */
public class Location {

    private String shelfId;
    private String shelfName;

    public String getShelfId() {
        return shelfId;
    }

    public void setShelfId(String shelfId) {
        this.shelfId = shelfId;
    }

    public String getShelfName() {
        return shelfName;
    }

    public void setShelfName(String shelfName) {
        this.shelfName = shelfName;
    }

    @Override
    public String toString() {
        return "Location{" +
                "shelfId='" + shelfId + '\'' +
                ", shelfName='" + shelfName + '\'' +
                '}';
    }
}
