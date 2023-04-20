package com.dxvalley.paymentgateway.otherservices.model;

public class Fuel {
    String type;
    float price;

    public Fuel(String type, float price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
