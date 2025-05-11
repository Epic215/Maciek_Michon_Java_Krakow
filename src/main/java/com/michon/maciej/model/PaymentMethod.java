package com.michon.maciej.model;


/*
    PaymentMethod class to store data from paymentmethods.json
*/
public class PaymentMethod {
    public String id;
    public int discount;
    public double limit;

    public PaymentMethod(String id, int discount, double limit) {
        this.id = id;
        this.discount = discount;
        this.limit = limit;
    }


}
