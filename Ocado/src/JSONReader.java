import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.time.*;
import java.util.Collections;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONReader {

    ArrayList<Order> Array_orders = new ArrayList<Order>();
    ArrayList<Picker> Array_pickers = new ArrayList<>();
    static int pickersNumber;
    static LocalTime startTime;
    static LocalTime endTime;
    public void readOrders () {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("C:\\Users\\stryc\\Documents\\Ocado\\src\\orders.json")) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray orders = (JSONArray) obj;

            //Filling the Array with orders
            for (int i=0; i<orders.size(); i++){
                JSONObject tmp = (JSONObject) orders.get(i);

                String pickingTime_tmp = (String) tmp.get("pickingTime");
                pickingTime_tmp = pickingTime_tmp.replaceAll("[^\\d.]", "");

                Array_orders.add(new Order());
                Array_orders.get(i).orderId = (String) tmp.get("orderId");
                Array_orders.get(i).orderValue = Double.valueOf((String) tmp.get("orderValue"));
                Array_orders.get(i).pickingTime = Integer.valueOf(pickingTime_tmp);
                Array_orders.get(i).completeBy = LocalTime.parse((String) tmp.get("completeBy"));
            }

            Collections.sort(Array_orders, new OrderSortingComparator());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void readPickers () {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("C:\\Users\\stryc\\Documents\\Ocado\\src\\store.json")) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONObject jsonObj = (JSONObject)obj;
            JSONArray pickers = (JSONArray)jsonObj.get("pickers");
            pickersNumber = pickers.size();
            for (int i=0; i<pickersNumber; i++){

                Array_pickers.add(new Picker());
                Array_pickers.get(i).pickerId = (String) pickers.get(i);
                System.out.println(Array_pickers.get(0).readyToWork);
            }
            startTime = LocalTime.parse((String) jsonObj.get("pickingStartTime"));
            endTime = LocalTime.parse((String) jsonObj.get("pickingEndTime"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    public void printPickers () {
        System.out.println(pickersNumber);
    }
    public void printTime () {
        System.out.println("Start of the work: " + startTime);
        System.out.println("End of the work: " + endTime);
    }

}
