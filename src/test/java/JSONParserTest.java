import com.michon.maciej.model.Order;
import com.michon.maciej.model.PaymentMethod;
import com.michon.maciej.util.JSONParser;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JSONParserTest {

    @Test
    void testLoadOrders() throws Exception {
        String path = "src/test/resources/ordersTest.json";
        List<Order> orders = JSONParser.loadOrders(path);

        assertEquals(2, orders.size());
        assertEquals("ORDERTEST1", orders.getFirst().id);
        assertEquals(70.0, orders.getFirst().value);
        assertEquals(List.of("test", "test2"), orders.getFirst().promotions);

        assertEquals("ORDERTEST2", orders.get(1).id);
        assertNull(orders.get(1).promotions);
    }

    @Test
    void testParsePaymentMethods() throws Exception {
        String path = "src/test/resources/paymentmethodsTest.json";
        List<PaymentMethod> methods = JSONParser.parsePaymentMethods(path);

        assertEquals(2, methods.size());
        assertEquals("PUNKTY", methods.getFirst().id);
        assertEquals(20, methods.getFirst().discount);
        assertEquals(120.0, methods.getFirst().limit);

        assertEquals("test", methods.get(1).id);
        assertEquals(1000.0, methods.get(1).limit);
    }


}