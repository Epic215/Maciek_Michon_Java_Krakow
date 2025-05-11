package com.michon.maciej.model;

import java.util.List;

/*
    Order class to store data from orders.json
*/
public class Order {

    public String id;
    public double value;
    public List<String> promotions;

    public Order(String id, double value, List<String> promotions) {
        this.id = id;
        this.value = value;
        this.promotions = promotions;
    }

}
