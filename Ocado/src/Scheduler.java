import java.time.LocalTime;
import java.util.ArrayList;

public class Scheduler {
    LocalTime pickingStartTime;
    LocalTime pickingEndTime;
    ArrayList<Order> Array_orders;
    ArrayList<Picker> Array_Pickers;
    LocalTime clock;
    int iterator = 0;

    Scheduler(String pathStore, String pathOrders){
        //Creating Reader object
        //Getting information from JSONReader
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

    public void deleteOrders(){
        if (Array_orders.get(0).completeBy.isBefore(bestPicker(Array_Pickers).plusMinutes(Array_orders.get(0).pickingTime))) {
            System.out.println(Array_orders.get(0).completeBy + " |||||| " +bestPicker(Array_Pickers).plusMinutes(Array_orders.get(0).pickingTime));
            Array_orders.remove(0);
            ;}
    }

    public void assignPicker(){
        for(Picker picker: Array_Pickers){

            if (Array_orders.size() == 0) {break;}
            if (checkAvailability(picker, Array_orders.get(0)) && !clock.isBefore(picker.readyToWork)) {

                picker.readyToWork = picker.readyToWork.plusMinutes(Array_orders.get(0).pickingTime);
                System.out.println(picker.pickerId + " " + Array_orders.get(0).orderId + " " + clock);
                Array_orders.remove(0);
            }
        }
    }

}
