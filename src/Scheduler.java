import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

public class Scheduler {
    LocalTime pickingStartTime;
    LocalTime pickingEndTime;
    ArrayList<Order> Array_orders;
    ArrayList<Picker> Array_Pickers;
    ArrayList<Order> Final_orders = new ArrayList<>();
    LocalTime clock;
    int iterator = 1;
    int iterator2 = 1;

    Scheduler(String pathStore, String pathOrders){

        //Creating Reader object
        //Getting information from JSONReader and setting up important variables
        JSONReader reader_json = new JSONReader(pathStore, pathOrders);
        reader_json.readOrders();
        reader_json.readPickers();

        Array_orders = reader_json.Array_orders;
        pickingStartTime = JSONReader.startTime;
        pickingEndTime = JSONReader.endTime;
        clock = pickingStartTime;

       Array_Pickers = reader_json.Array_pickers;
       for (Picker el: Array_Pickers){
           el.readyToWork = pickingStartTime;
       }
    }
    public void createSchedule () {

        while (clock.isBefore(pickingEndTime) && Array_orders.size() != 0){

            //Delete orders that cannot be completed in time
            deleteOrders();

            //Checking if there is any picker who can start picking
            //Picker starts picking if he is available
            assignPicker();

            clock = clock.plusMinutes(1);
        }
        printSchedule();
    }

    //Checking the availability of Picker
    public boolean checkAvailability (Picker picker, Order order) {
        return !(picker.readyToWork.isAfter(order.completeBy));
    }
    //Finding the picker that will be at the earliest
    public LocalTime bestPicker (ArrayList<Picker> pickers) {
        LocalTime time;
        Picker bestPicker = pickers.get(0);
        for (Picker el: pickers){
            if (el.readyToWork.isBefore(bestPicker.readyToWork)){
                bestPicker = el;
            }
        }

        time = bestPicker.readyToWork;
        return time;
    }

    //Function is checking if an order can be managed, if not it will be sent to function potentialSwap
    //If there is no possibility to swap the order the function will delete it.
    public void deleteOrders(){
        if (Array_orders.get(0).completeBy.isBefore(bestPicker(Array_Pickers).plusMinutes(Array_orders.get(0).pickingTime))) {
            if(!(potentialSwap(Array_orders.get(0)))){
                Array_orders.remove(0);
            }

        }
    }


    public void assignPicker(){
        for(Picker picker: Array_Pickers){

            if (Array_orders.size() == 0) {break;}
            if (checkAvailability(picker, Array_orders.get(0)) && !clock.isBefore(picker.readyToWork)) {

                picker.readyToWork = picker.readyToWork.plusMinutes(Array_orders.get(0).pickingTime);
                Array_orders.get(0).time = clock;
                Array_orders.get(0).pickerId = picker.pickerId;
                Final_orders.add(Array_orders.get(0));
                Array_orders.remove(0);
            }
        }
    }

    //Algorithm that improves the score based on OrderValue
    public boolean potentialSwap (Order notFittingOrder) {
        Collections.sort(Final_orders, new OrderSortingComparatorValue());

        for (int i=0; i< Final_orders.size(); i++){
            //If orderValue of the Order in Array is bigger than notFittingOrder than all next orders will have bigger value
            if ((notFittingOrder.orderValue.compareTo(Final_orders.get(i).orderValue)) < 0) {break;}

            //If time of picking of notFittingOrder is smaller than the order than we can preform swap
            if (notFittingOrder.pickingTime <= Final_orders.get(i).pickingTime){
                Final_orders.remove(i);
                Final_orders.add(notFittingOrder);
                break;

            } else {
                Collections.sort(Final_orders, new OrderSortingComparator());

                //Variables that will help in deciding when the swap is not profitable
                double valueOrders = Final_orders.get(i).orderValue;
                int timeOrder = Final_orders.get(i).pickingTime;

                while (notFittingOrder.orderValue.compareTo(valueOrders) > 0){

                    //If there are still orders to the right the algorithm will try to calculate if
                    //adding them together with current order will be profitable
                    if (Final_orders.size() > i){

                        timeOrder += Final_orders.get(i+iterator).pickingTime;
                        valueOrders += Final_orders.get(i+iterator).orderValue;

                        if (notFittingOrder.pickingTime <= timeOrder){
                            for(int j = i; j<=i+iterator; j++){
                                Final_orders.remove(0);
                            }
                            resetTimeAndPickers();
                            return true;
                        }

                        //If there are no orders which are bigger than starting point
                        //Algorithm will try to find orders that are smaller than starting point
                    } else if (i>0){

                        timeOrder += Final_orders.get(i-iterator2).pickingTime;
                        valueOrders += Final_orders.get(i-iterator2).orderValue;
                        if (notFittingOrder.pickingTime <= timeOrder){
                            for(int j = Final_orders.size()-1; j>i-iterator; j--){
                                Final_orders.remove(0);

                            }
                            resetTimeAndPickers();
                            return true;
                        }

                        //If there are no more orders and notFittingOrder has more value
                        //Algorithm will remove all orders and replace it with notFittingOrder
                    } else {
                        for (int j = 0; j<Final_orders.size(); j++){
                            Final_orders.remove(0);

                        }
                        resetTimeAndPickers();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void printSchedule () {
        Collections.sort(Final_orders, new OrderSortingComparator());
        for (Order el: Final_orders){
            System.out.println(el.pickerId + " " + el.orderId + " " + el.time);
        }
    }

    public void resetTimeAndPickers(){
        clock = pickingStartTime;
        for(Picker picker: Array_Pickers){
            picker.readyToWork = pickingStartTime;
        }
    }

}
