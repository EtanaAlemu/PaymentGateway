package com.dxvalley.paymentgateway.retailservices.models;

import androidx.room.*;

@Entity(tableName = "item_db")
public class Item {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "image")
    String imageName;
    @ColumnInfo(name = "name")
    String name;
    @ColumnInfo(name = "price")
    String price;


    public Item(String imageName, String name, String price) {
        this.imageName = imageName;
        this.name = name;
        this.price = price;
    }

    @Ignore
    public Item() {

    }

    @Ignore
    public Item(int id, String imageName, String name, String price) {
        this.id = id;
        this.imageName = imageName;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
