package com.michon.maciej;

import com.michon.maciej.model.Order;
import com.michon.maciej.model.PaymentMethod;
import com.michon.maciej.solution.OptimizeDiscountsGreedy;
import com.michon.maciej.solution.OptimizeSolutionRecursion;
import com.michon.maciej.util.JSONParser;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Main <orders.json> <paymentmethods.json>");
            return;
        }

        List<Order> orders;
        List<PaymentMethod> methods;

        try {
            orders = JSONParser.loadOrders(args[0]);
            methods = JSONParser.parsePaymentMethods(args[1]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, Double> results = null;
        OptimizeSolutionRecursion optimize = new OptimizeSolutionRecursion(orders, methods);
        try {
            results = optimize.getOptimizedResultRecursion(orders,methods);
        } catch (Exception ignored) {

        }
        if (results == null){
            try {
                OptimizeDiscountsGreedy optimize2 = new OptimizeDiscountsGreedy(orders, methods);
                results = optimize2.getOptimizedResultWithOrder(orders,methods);
            } catch (Exception e) {
                System.out.println("Results could not be optimized, result not found");
            }
        }

        if (results != null){
            results.forEach((id, amount) -> System.out.printf("%s %.2f%n", id, amount));
        }

    }


}