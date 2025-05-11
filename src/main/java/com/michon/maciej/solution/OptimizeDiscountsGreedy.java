package com.michon.maciej.solution;

import com.michon.maciej.model.Order;
import com.michon.maciej.model.PaymentMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptimizeDiscountsGreedy {
    private final List<Order> orders;
    private final List<PaymentMethod> methods;
    private Map<String, Double> result = new HashMap<>();
    private final Map<String, PaymentMethod> mappedMethods = new HashMap<>();

    public OptimizeDiscountsGreedy(List<Order> orders, List<PaymentMethod> methods) {
        this.orders = orders;
        this.methods = methods;
        for (PaymentMethod method : methods) {
            mappedMethods.put(method.id, method);
        }
    }

    /*
        getOptimizedResultWithOrder is an alternative method used if the OptimizeSolutionRecursion class doesn't find an answer.
    */
    public Map<String, Double> getOptimizedResultWithOrder(List<Order> orders, List<PaymentMethod> methods) throws Exception {
        result.clear();
        for (PaymentMethod method : methods) {
            result.put(method.id, 0.0);
        }
        PaymentMethod points = mappedMethods.get("PUNKTY");
        Map<Order, PaymentMethod> mixedResults = new HashMap<>();

        for (Order order : orders) {
            double bestDiscount = -1;
            PaymentMethod bestMethod = null;
            double orderValue  = order.value;
            double discount;
            double price10Percent = order.value * 0.1;
            double price90Percent = order.value - price10Percent;

            double extraPoints = -1;

            // We check all possibilities using points
            if (points != null) {

                // Using only points
                discount = points.discount* orderValue * 0.01;
                if (discount > bestDiscount && (orderValue-discount) < (points.limit - result.get(points.id))) {
                    bestMethod = points;
                    bestDiscount = discount;
                }

                // Using 10 percent of points (money + points)
                if (price10Percent < points.limit -result.get(points.id)){
                    for (PaymentMethod method : methods) {
                        if (!method.id.equals(points.id) ) {
                            discount = method.discount * orderValue * 0.01;
                            if (discount > bestDiscount && (price90Percent-discount) < method.limit -result.get(method.id)) {
                                extraPoints = price90Percent;
                                bestMethod = method;
                                bestDiscount = discount;
                                break;
                            }
                        }
                    }
                }
            }

            // Using only money
            if (order.promotions != null) {
                for (String methodId : order.promotions) {
                    PaymentMethod method = mappedMethods.get(methodId);
                    discount = 0.1 * orderValue ;
                    if (discount > bestDiscount &&  (orderValue-discount) < method.limit -result.get(methodId)) {
                        extraPoints = -1;
                        bestMethod = method;
                        bestDiscount = discount;
                    }
                }
            }


            if (bestMethod == null) {
                throw new Exception("Can't find solution for " + order.id);
            }

            // Checking if strategy uses mixed payment (money + points )
            if (extraPoints == -1) {

                result.put(bestMethod.id, result.get(bestMethod.id) + orderValue-bestDiscount);
            }
            else{

                result.put(points.id, result.get(points.id) + price10Percent);
                mixedResults.put(order, bestMethod);
                result.put(bestMethod.id, result.get(bestMethod.id) +price90Percent-bestDiscount);

            }

            // Checks if we can distribute rest of unused points
            if (!mixedResults.isEmpty()){
                double restPoints = points.limit - result.get(points.id);
                for (Map.Entry<Order, PaymentMethod> entry : mixedResults.entrySet()) {
                    Order orderMixed = entry.getKey();
                    PaymentMethod method = entry.getValue();
                    double extraCash = orderMixed.value * 0.9;
                    if (extraCash <= restPoints) {
                        restPoints -= extraCash;
                        mixedResults.put(orderMixed, null);
                        result.put(method.id, result.get(method.id) - extraCash*0.9);
                        result.put(points.id, points.limit - restPoints);
                    }
                }
                for (Map.Entry<Order, PaymentMethod> entry : mixedResults.entrySet()) {
                    Order orderMixed = entry.getKey();
                    PaymentMethod method = entry.getValue();
                    double extraCash = orderMixed.value * 0.9;
                    if (extraCash > restPoints && method != null) {
                        result.put(method.id, result.get(method.id) - restPoints);
                        result.put(points.id, points.limit);
                        break;
                    }
                }
            }



        }
        return result;
    }
}
