package eu.fcheret.parkingtoll.persistence;

import eu.fcheret.parkingtoll.exceptions.ParkingNotFoundException;
import eu.fcheret.parkingtoll.model.Layout;
import eu.fcheret.parkingtoll.model.ParkingLot;
import eu.fcheret.parkingtoll.model.ParkingLotTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryPersistenceManagerTest {
    private final Dao<ParkingLot> manager = new InMemoryPersistenceManager();
    private final SecureRandom random = new SecureRandom();

    @BeforeEach
    void clearDatabase() {
        manager.clearDatabase();
    }

    @Test
    void testClearDatabase() {
        manager.clearDatabase();
        assertEquals(0, manager.getAll().size());
        assertThrows(ParkingNotFoundException.class, () -> manager.findById(0L));
    }

    @Test
    void getParkingLotByIdTest(){
        assertThrows(ParkingNotFoundException.class, () -> manager.findById(5L));
        ParkingLot parkingLot = manager.save(ParkingLotTest.generateParking("test", 5L));
        assertNotNull(parkingLot);
        assertNotNull(parkingLot.getId());
    }

    @Test
    void saveTest() {
        ParkingLot parkingLot = getNewParkingLot();
        //call the service
        ParkingLot returnedParkingLot = manager.save(parkingLot);
        assertEquals(1, manager.getAll().size());

        assertNotNull(returnedParkingLot);
        assertNotNull(returnedParkingLot.getId());

        //call again
        parkingLot = getNewParkingLot();
        returnedParkingLot = manager.save(parkingLot);
        assertNotNull(returnedParkingLot);
        assertNotNull(returnedParkingLot.getId());
        assertEquals(2, manager.getAll().size());

    }

    private ParkingLot getNewParkingLot(){
        ParkingLot parkingLot = new ParkingLot();
        List<Layout> layouts = new ArrayList<>();
        Layout layout = new Layout();
        layout.setAvailable(random.nextInt(1000));
        layout.setName("Test Parking Lot " + random.nextLong());
        layouts.add(layout);
        layout = new Layout();
        layout.setAvailable(random.nextInt(1000));
        layout.setName("Test Parking Lot " + random.nextLong());
        layouts.add(layout);
        parkingLot.setLayoutList(layouts);
        return parkingLot;
    }


    @Test
    void getAllTest() {

        List<ParkingLot> parkingLots = new ArrayList<>();
        ParkingLot parkingLot;
        for (int i = 0; i < 10; i++) {
            parkingLot = getNewParkingLot();
            parkingLots.add(parkingLot);
            manager.save(parkingLot);
        }

        ArrayList<ParkingLot> lots = new ArrayList<>(manager.getAll());
        assertEquals(10, lots.size());

        for (int i = 0; i < parkingLots.size(); i++) {
            assertEquals(parkingLots.get(i), lots.get(i));
        }
    }

    @Test
    void findByIdTest() {
        List<ParkingLot> parkingLots = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            parkingLots.add(getNewParkingLot());
        }
        for (ParkingLot parkingLot : parkingLots) {
            manager.save(parkingLot);
        }

        //Call service and check
        for (ParkingLot parkingLot : parkingLots) {
            assertEquals(parkingLot, manager.findById(parkingLot.getId()));
        }
    }

    @Test
    void updateTest() {
        //first scenario, try to update an object that doesn't exist yet in database
        ParkingLot parkingLot = getNewParkingLot();
        assertEquals(0, manager.getAll().size());
        assertNull(parkingLot.getId());
        manager.update(5L, parkingLot);
        assertEquals(1, manager.getAll().size());
        assertNotNull(parkingLot.getId());

        ParkingLot parkingLot2 = getNewParkingLot();
        //set the id of parking 1
        parkingLot2.setId(parkingLot.getId());
        manager.update(parkingLot2.getId(), parkingLot2);
        assertEquals(1, manager.getAll().size());
    }

    @Test
    void deleteTest() {
        ParkingLot parkingLot = getNewParkingLot();
        assertEquals(0, manager.getAll().size());
        manager.save(parkingLot);
        assertEquals(1, manager.getAll().size());

        //call the service
        manager.delete(parkingLot.getId());
        assertEquals(0, manager.getAll().size());

        //nothing thrown
        manager.delete(parkingLot.getId());

        assertEquals(0, manager.getAll().size());

        manager.save(parkingLot);
        assertEquals(1, manager.getAll().size());
        //now try to delete something else
        ParkingLot parkingLot1 = new ParkingLot();
        parkingLot1.setId(parkingLot.getId() + 1);
        //nothing thrown
        manager.delete(parkingLot1.getId());
        assertEquals(1, manager.getAll().size());
    }

}