import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import java.util.ArrayList;

class SchedulerTest {
    String pathOrders = "C:\\Users\\stryc\\Desktop\\self-test-data\\advanced-allocation\\orders.json";
    String pathStore = "C:\\Users\\stryc\\Desktop\\self-test-data\\advanced-allocation\\store.json";
    private Scheduler scheduler;

    @Test
    void createScheduleTest() {
        scheduler = new Scheduler(pathStore, pathOrders);
        scheduler.createSchedule();
    }

    @Test
    void checkAvailabilityTest() {
        scheduler = new Scheduler(pathStore, pathOrders);
        Picker picker = new Picker();
        picker.readyToWork = LocalTime.of(10, 0);
        Order order = new Order();

        order.completeBy = LocalTime.of(9, 0);
        assertFalse(scheduler.checkAvailability(picker, order));

        order.completeBy = LocalTime.of(11, 0);
        assertTrue(scheduler.checkAvailability(picker, order));
    }

    @Test
    void bestPickerTest() {
        scheduler = new Scheduler(pathStore, pathOrders);
        ArrayList<Picker> pickers = new ArrayList<>();
        pickers.add(new Picker());
        pickers.add(new Picker());
        pickers.add(new Picker());

        pickers.get(0).readyToWork = LocalTime.of(10, 0);
        pickers.get(1).readyToWork = LocalTime.of(11, 0);
        pickers.get(2).readyToWork = LocalTime.of(12, 0);

        assertEquals(LocalTime.of(10, 0), scheduler.bestPicker(pickers));
    }

}