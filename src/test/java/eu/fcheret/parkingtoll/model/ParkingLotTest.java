package eu.fcheret.parkingtoll.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingLotTest {

    private static ParkingLot parkingLot;
    private static final String[] TYPES = {"STANDARD",  "25kW", "50kW"};

    @BeforeEach
    void initParkingLot(){
        parkingLot = generateParking("Test parking 1", 42L);
    }

     public static ParkingLot generateParking(String name, Long id){
        parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setId(id); //usually DAO assigns it an id
        List<Layout> layouts = new ArrayList<>(TYPES.length);
        Layout layout;
        for (String type : TYPES) {
            layout = new Layout();
            layout.setAvailable(2);
            layout.setName(type);
            layouts.add(layout);
        }
        parkingLot.setLayoutList(layouts);
        return parkingLot;
    }


    @Test
    void getSlotTypes() {

        List<String> types = parkingLot.getSlotTypes();
        assertNotNull(types);
        assertEquals(TYPES.length,types.size());
        assertTrue(types.contains(TYPES[0]));
        assertTrue(types.contains(TYPES[1]));
        assertTrue(types.contains(TYPES[2]));

    }
}