package com.dxvalley.paymentgateway.models;

import androidx.room.*;

@Entity(tableName = "item_db")
public class Item {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "image")
    int image;
    @ColumnInfo(name = "name")
    String name;
    @ColumnInfo(name = "price")
    String price;

    public Item( int image, String name, String price) {
        this.image = image;
        this.name = name;
        this.price = price;
    }

    @Ignore
    public Item(int id, int image, String name, String price) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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
