import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.time.LocalTime;
class JSONReaderTest {
    String pathOrders = "C:\\Users\\stryc\\Desktop\\self-test-data\\advanced-allocation\\orders.json";
    String pathStore = "C:\\Users\\stryc\\Desktop\\self-test-data\\advanced-allocation\\store.json" ;
    @Test
    void readOrdersTest() {
        JSONReader reader = new JSONReader(pathStore, pathOrders);
        reader.readOrders();
        ArrayList<Order> orders = reader.Array_orders;

        assertEquals(7,orders.size());
        assertEquals("order-1", orders.get(0).orderId);
        assertEquals(0.00, orders.get(0).orderValue);
        assertEquals(15, orders.get(0).pickingTime);
        assertEquals(LocalTime.of(9, 15), orders.get(0).completeBy);

    }

    @Test
    void readPickersTest() {
        JSONReader reader = new JSONReader(pathStore, pathOrders);
        reader.readPickers();
        ArrayList<Picker> pickers = reader.Array_pickers;

        assertEquals(2, JSONReader.pickersNumber);
        assertEquals("P1", pickers.get(0).pickerId);
        assertEquals("P2", pickers.get(1).pickerId);


        assertEquals(LocalTime.of(9, 0), JSONReader.startTime);
        assertEquals(LocalTime.of(11, 0), JSONReader.endTime);
    }

}