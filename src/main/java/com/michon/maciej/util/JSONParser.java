package com.michon.maciej.util;

import com.michon.maciej.model.Order;
import com.michon.maciej.model.PaymentMethod;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
    JSONParser contains two methods:
        - loadOrders: reads data from orders.json and parses it into a List<Order>
        - parsePaymentMethods: reads data from paymentmethods.json and parses it into a List<PaymentMethod>
*/
public class JSONParser {
    public static List<Order> loadOrders(String path) throws IOException {
        String content = Files.readString(Paths.get(path));
        JSONArray arr = new JSONArray(content);
        List<Order> orders = new ArrayList<>();

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            String id = obj.getString("id");
            double value = obj.getDouble("value");

            List<String> promotions = null;
            if (obj.has("promotions")) {
                JSONArray promos = obj.getJSONArray("promotions");
                promotions = new ArrayList<>();
                for (int j = 0; j < promos.length(); j++) {
                    promotions.add(promos.getString(j));
                }
            }

            orders.add(new Order(id, value, promotions));
        }

        return orders;
    }
    public static List<PaymentMethod> parsePaymentMethods(String path) throws IOException {
        String content = Files.readString(Paths.get(path));
        JSONArray arr = new JSONArray(content);
        List<PaymentMethod> paymentMethods = new ArrayList<>();

        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);

            String id = obj.getString("id");
            int discount = obj.getInt("discount");
            double limit = obj.getDouble("limit");

            paymentMethods.add(new PaymentMethod(id, discount, limit));
        }

        return paymentMethods;
    }
}
