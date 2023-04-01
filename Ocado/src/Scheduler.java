import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.ArrayList;

public class Scheduler {
    LocalTime pickingStartTime;
    LocalTime pickingEndTime;
    ArrayList<Order> Array_orders;
    ArrayList<Picker> Array_Pickers = new ArrayList<Picker>();
    ArrayList<String> schedule = new ArrayList<>();
    LocalTime clock;
    int iterator = 0;

    Scheduler(){
        //Creating Reader object
        //Getting information from JSONReader
        JSONReader reader_json = new JSONReader();
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

        while (clock.isBefore(pickingEndTime)){

            if (Array_orders.get(0).completeBy.isAfter(clock.plusMinutes(Array_orders.get(0).pickingTime))) {Array_orders.remove(0);}

            //Checking if there is any picker who can start picking
            //Picker starts picking if he is available
            for(Picker picker: Array_Pickers){
                if (checkAviability(picker, Array_orders.get(0))) {
                    picker.readyToWork.plusMinutes(Array_orders.get(0).pickingTime);
                    System.out.println(picker.pickerId + " " + Array_orders.get(0).orderId + " " + clock);
                    Array_orders.remove(0);
                }
            }
            clock = clock.plusMinutes(1);
        }
    }

    public void printOrders(){
        for(int i=0; i<Array_orders.size(); i++){
            System.out.println("Order nr:" + i);
            System.out.println("Order id: " + Array_orders.get(i).orderId);
            System.out.println("Order Value: " + Array_orders.get(i).orderValue);
            System.out.println("Picking Time: "+Array_orders.get(i).pickingTime);
            System.out.println("CompleteBy: " + Array_orders.get(i).completeBy);
        }
    }

    //Checking the aviability of Picker
    private boolean checkAviability (Picker picker, Order order) {
        if (picker.readyToWork.compareTo(order.completeBy) > 0 ) { return false;}
        else{return true;}
    }

}
