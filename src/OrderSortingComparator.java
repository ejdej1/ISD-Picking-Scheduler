import java.util.Comparator;

public class OrderSortingComparator implements Comparator<Order> {
    @Override
    public int compare(Order order1, Order order2){
        return order1.completeBy.compareTo(order2.completeBy);
    }
}
