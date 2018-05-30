package com.gsy.ml.model.message;

/**
 * Created by Administrator on 2017/9/6.
 */

public class EditPriceModel {
    private double price;
    private double total_price;

    public EditPriceModel(double price, double total_price) {
        this.price = price;
        this.total_price = total_price;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
