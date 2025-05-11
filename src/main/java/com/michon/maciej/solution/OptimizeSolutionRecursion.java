package com.michon.maciej.solution;

import com.michon.maciej.model.Order;
import com.michon.maciej.model.PaymentMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptimizeSolutionRecursion {

    private final List<Order> orders;
    private final List<PaymentMethod> methods;
    private Map<String, Double> result = new HashMap<>();
    private final Map<String, PaymentMethod> mappedMethods = new HashMap<>();

    public OptimizeSolutionRecursion(List<Order> orders, List<PaymentMethod> methods) {
        this.orders = orders;
        this.methods = methods;
        for (PaymentMethod method : methods) {
            mappedMethods.put(method.id, method);
        }
    }

    /*
        getOptimizedResultRecursion is a main method used for optimization.
        This method uses recursion to explore all possible options and combinations of
        payment methods and points usage in order to achieve the highest
        discount while spending the least amount of money using the traditional payment method.
    */
    public Map<String, Double> getOptimizedResultRecursion(List<Order> orders, List<PaymentMethod> methods) throws Exception {
        result.clear();
        for (PaymentMethod method : methods) {
            result.put(method.id, 0.0);
        }
        // These two variables help track the current best solution
        result.put("TOTAL", 0.0);
        result.put("EXTRA_POSSIBLE_POINTS", 0.0);

        result = optimizeRecursion(orders,methods,0,result);
        if (result != null){
            result.remove("TOTAL");
            result.remove("EXTRA_POSSIBLE_POINTS");
        }

        return result;


    }

    private Map<String, Double> optimizeRecursion(List<Order> orders, List<PaymentMethod> methods, int depth, Map<String, Double> currResult) throws Exception {

        if (depth >= orders.size()) {
            return new HashMap<>(currResult);
        }
        PaymentMethod points = mappedMethods.get("PUNKTY");

        Order order = orders.get(depth);

        double orderValue  = order.value; // current order
        double discount; // variable to save how much money we get from discount

        // two variables to check mixed method of payment (money + points)
        double price10Percent = order.value * 0.1;
        double price90Percent = order.value - price10Percent;

        Map<String, Double> bestResult = null;
        Map<String, Double> result = null;


        // Check every payment with points
        if (points != null) {

            // Using only points
            discount = points.discount* orderValue * 0.01;
            if ((orderValue-discount) < (points.limit - currResult.get(points.id))) {

                currResult.put(points.id, currResult.get(points.id) + (orderValue-discount));

                result = optimizeRecursion(orders,methods,depth+1,currResult);

                currResult.put(points.id, currResult.get(points.id) - (orderValue-discount));
                if (result != null) {
                    bestResult = result;
                }
            }

            // Using 10 percent of points (money + points)
            if (price10Percent < points.limit -currResult.get(points.id)){
                for (PaymentMethod method : methods) {
                    if (!method.id.equals(points.id) ) {
                        discount = 0.1 * orderValue;
                        if ((price90Percent-discount) < method.limit -currResult.get(method.id)) {

                            currResult.put(points.id, currResult.get(points.id) + price10Percent);
                            currResult.put(method.id, currResult.get(method.id) + (price90Percent-discount));
                            currResult.put("TOTAL", currResult.get("TOTAL") + (price90Percent-discount));
                            currResult.put("EXTRA_POSSIBLE_POINTS", currResult.get("EXTRA_POSSIBLE_POINTS") + price90Percent);

                            result = optimizeRecursion(orders,methods,depth+1,currResult);

                            currResult.put(points.id, currResult.get(points.id) - price10Percent);
                            currResult.put(method.id, currResult.get(method.id) - (price90Percent-discount));
                            currResult.put("TOTAL", currResult.get("TOTAL") - (price90Percent-discount));
                            currResult.put("EXTRA_POSSIBLE_POINTS", currResult.get("EXTRA_POSSIBLE_POINTS") - price90Percent);
                            if (result != null) {
                                if (result.get("EXTRA_POSSIBLE_POINTS") > 0) {
                                    manageExtraPoints(method, points, result);
                                }
                                if (bestResult == null || bestResult.get("TOTAL") > result.get("TOTAL")) {
                                    bestResult = result;
                                }
                            }
                        }
                    }
                }

                // Using all money from one method and add rest of the points to it.
                for (PaymentMethod method : methods) {
                    if (!method.id.equals(points.id) ) {

                        discount = 0.1 * orderValue;
                        double used = (method.limit -currResult.get(method.id));
                        double neededPoints = (price90Percent-discount) - used + price10Percent;

                        if (neededPoints <= 0 || price10Percent > neededPoints) continue;


                        if (neededPoints<points.limit -currResult.get(points.id)) {

                            currResult.put(points.id, currResult.get(points.id) + neededPoints);
                            currResult.put(method.id, currResult.get(method.id) + used);
                            currResult.put("TOTAL", currResult.get("TOTAL") + used);

                            result = optimizeRecursion(orders,methods,depth+1,currResult);

                            currResult.put(points.id, currResult.get(points.id) - neededPoints);
                            currResult.put(method.id, currResult.get(method.id) - used);
                            currResult.put("TOTAL", currResult.get("TOTAL") - used);

                            if (result != null) {
                                if (bestResult == null || bestResult.get("TOTAL") > result.get("TOTAL")) {
                                    bestResult = result;
                                }
                            }
                        }
                    }
                }


            }
        }

        // Check every payment with only money
        if (order.promotions != null) {
            for (String methodId : order.promotions) {
                PaymentMethod method = mappedMethods.get(methodId);
                discount = method.discount * orderValue * 0.01;
                if ((orderValue-discount) < method.limit -currResult.get(methodId)) {

                    currResult.put(method.id, currResult.get(method.id) + (orderValue-discount));
                    currResult.put("TOTAL", currResult.get("TOTAL") + (orderValue-discount));
                    result = optimizeRecursion(orders,methods,depth+1,currResult);
                    currResult.put("TOTAL", currResult.get("TOTAL") - (orderValue-discount));
                    currResult.put(method.id, currResult.get(method.id) - (orderValue-discount));

                    if (result != null) {
                        if (bestResult == null || bestResult.get("TOTAL") > result.get("TOTAL")) {
                            bestResult = result;
                        }
                    }

                }
            }
        }

        return bestResult;
    }

    // This is used only to check if extra points can be added to the final solution and limit the use of money
    private static void manageExtraPoints(PaymentMethod method, PaymentMethod points, Map<String, Double> result) {
        double addToTotal = points.limit - result.get("PUNKTY");
        if (result.get("EXTRA_POSSIBLE_POINTS")-addToTotal >=0){
            result.put("EXTRA_POSSIBLE_POINTS", result.get("EXTRA_POSSIBLE_POINTS") - addToTotal);
            result.put("TOTAL", result.get("TOTAL") - addToTotal);
            result.put("PUNKTY", result.get("PUNKTY") + addToTotal);
            result.put(method.id, result.get(method.id) - addToTotal);
        }
        else{
            result.put("TOTAL", result.get("TOTAL") - (result.get("EXTRA_POSSIBLE_POINTS")-addToTotal));
            result.put("PUNKTY", result.get("PUNKTY") + (result.get("EXTRA_POSSIBLE_POINTS")-addToTotal));
            result.put(method.id, result.get(method.id) - (result.get("EXTRA_POSSIBLE_POINTS")-addToTotal));
            result.put("EXTRA_POSSIBLE_POINTS", 0.0);
        }
    }

}
