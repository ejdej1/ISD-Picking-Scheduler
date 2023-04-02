import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {

    public static void main(String[] args) {
        String pathStore = args[0];
        String pathOrder = args[1];
        Scheduler ocado = new Scheduler(pathStore, pathOrder);
        ocado.createSchedule();



    }
}
