import java.util.Comparator;

public class OrderSortingComparatorValue implements Comparator<Order> {
    @Override
    public int compare(Order order1, Order order2){
        return order1.orderValue.compareTo(order2.orderValue);
    }
}
