

import com.michon.maciej.model.Order;
import com.michon.maciej.model.PaymentMethod;
import com.michon.maciej.solution.OptimizeDiscountsGreedy;
import com.michon.maciej.solution.OptimizeSolutionRecursion;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OptimizeSolutionRecursionTest {
    @Test
    void testSolutionRecursion() throws Exception {
        Order order = new Order("Test1", 120, List.of("Payment1"));
        Order order2 = new Order("Test2", 230, List.of("Payment1", "Payment2"));
        Order order3 = new Order("Test3", 70, null);

        PaymentMethod method = new PaymentMethod("Payment1", 20, 300);
        PaymentMethod method2 = new PaymentMethod("Payment2", 15, 200);
        PaymentMethod method3 = new PaymentMethod("PUNKTY", 30, 100);

        List<Order> orders = List.of(order, order2, order3);
        List<PaymentMethod> methods = List.of(method, method2, method3);

        OptimizeSolutionRecursion optimizeSolutionRecursions = new OptimizeSolutionRecursion(orders, methods);
        Map<String, Double> results = optimizeSolutionRecursions.getOptimizedResultRecursion(orders,methods);

        assertEquals(231.0, results.get("Payment1"), 0.001);
        assertEquals(0.0, results.get("Payment2"), 0.001);
        assertEquals(100.0, results.get("PUNKTY"), 0.001);

    }
    @Test
    void testDiscountsGreedy() throws Exception {
        Order order = new Order("Test1", 120, List.of("Payment1"));
        Order order2 = new Order("Test2", 230, List.of("Payment1", "Payment2"));
        Order order3 = new Order("Test3", 70, null);

        PaymentMethod method = new PaymentMethod("Payment1", 20, 300);
        PaymentMethod method2 = new PaymentMethod("Payment2", 15, 200);
        PaymentMethod method3 = new PaymentMethod("PUNKTY", 30, 100);

        List<Order> orders = List.of(order, order2, order3);
        List<PaymentMethod> methods = List.of(method, method2, method3);

        OptimizeDiscountsGreedy optimizeDiscountsGreedy = new OptimizeDiscountsGreedy(orders, methods);
        Map<String, Double> results = optimizeDiscountsGreedy.getOptimizedResultWithOrder(orders,methods);

        assertEquals(247.0, results.get("Payment1"), 0.001);
        assertEquals(0.0, results.get("Payment2"), 0.001);
        assertEquals(100.0, results.get("PUNKTY"), 0.001);



    }

}