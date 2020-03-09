package eu.fcheret.parkingtoll.service;

import eu.fcheret.parkingtoll.exceptions.NoSuchCarInParkingException;
import eu.fcheret.parkingtoll.exceptions.ParkingIsFullException;
import eu.fcheret.parkingtoll.exceptions.ParkingNotFoundException;
import eu.fcheret.parkingtoll.exceptions.ParkingTypeDoesNotExistException;
import eu.fcheret.parkingtoll.model.CarSlot;
import eu.fcheret.parkingtoll.model.ParkingLot;
import eu.fcheret.parkingtoll.model.ParkingLotTest;
import eu.fcheret.parkingtoll.model.PricingPolicy;
import eu.fcheret.parkingtoll.persistence.InMemoryPersistenceManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ParkingLotServiceTest {


    @Mock @Autowired
    private InMemoryPersistenceManager persistenceManager;

    @InjectMocks
    private ParkingLotService service;

    private List<ParkingLot> parkingLots;

    @BeforeEach
    void init(){
        MockitoAnnotations.initMocks(this);

        //create data for our tests
        parkingLots = new ArrayList<>();
        parkingLots.add(ParkingLotTest.generateParking("Test parking 1",1L));
        parkingLots.add(ParkingLotTest.generateParking("Test parking 2",2L));
        parkingLots.add(ParkingLotTest.generateParking("Test parking 3",3L));
        parkingLots.add(ParkingLotTest.generateParking("Test parking 3 second part",4L));
    }


    @Test
    void createParkingLot() {
        ParkingLot storedParkingLot = ParkingLotTest.generateParking("created parking", 55L);
        when(persistenceManager.save(any())).thenReturn(storedParkingLot);

        ParkingLot parkingLot = service.createParkingLot(storedParkingLot);
        assertEquals(storedParkingLot, parkingLot);
    }

    @Test
    void deleteParkingLotById() {
        when(persistenceManager.findById(5L)).thenReturn(null);
        assertThrows(ParkingNotFoundException.class, () -> service.deleteParkingLotById(5L));

        when(persistenceManager.findById(2L)).thenReturn(parkingLots.get(0));
        ParkingLot parkingLot = service.deleteParkingLotById(2L);
        assertEquals(parkingLots.get(0), parkingLot);
    }

    @Test
    void getParkingById() {
        when(persistenceManager.findById(2L)).thenReturn(parkingLots.get(0));
        ParkingLot parkingLot = service.getParkingById(2L);
        assertEquals(parkingLots.get(0), parkingLot);
        when(persistenceManager.findById(5L)).thenReturn(null);
        assertThrows(ParkingNotFoundException.class, () -> service.getParkingById(5L));
    }

    @Test
    void leaveParkingLotWithFlatFee() {
        when(persistenceManager.findById(parkingLots.get(0).getId())).thenReturn(parkingLots.get(0));

        CarSlot carSlot = new CarSlot();
        carSlot.setType(parkingLots.get(0).getSlotTypes().get(0));
        carSlot = service.parkAtParkingLot(parkingLots.get(0).getId(),carSlot);

        ParkingLot parkingLot = parkingLots.get(0);
        PricingPolicy policy = new PricingPolicy();
        policy.setFlatFee(BigDecimal.valueOf(25.2));
        parkingLot.setFareProcessor(policy);
        carSlot = service.leaveParkingLot(parkingLots.get(0).getId(), carSlot);

        assertTrue(BigDecimal.valueOf(25.2).compareTo(carSlot.getPrice()) == 0);

        assertNotNull(carSlot.getDepartureTime());
        assertNotNull(carSlot.getArrivalTime());
    }

    @Test
    void leaveParkingLotWithNoFee() {
        when(persistenceManager.findById(parkingLots.get(0).getId())).thenReturn(parkingLots.get(0));

        CarSlot carSlot = new CarSlot();
        carSlot.setType(parkingLots.get(0).getSlotTypes().get(0));
        carSlot = service.parkAtParkingLot(parkingLots.get(0).getId(),carSlot);

        ParkingLot parkingLot = parkingLots.get(0);
        PricingPolicy policy = new PricingPolicy();
        policy.setFlatFee(BigDecimal.valueOf(0));
        parkingLot.setFareProcessor(policy);
        carSlot = service.leaveParkingLot(parkingLots.get(0).getId(), carSlot);

        assertTrue(BigDecimal.valueOf(0).compareTo(carSlot.getPrice()) == 0);

        assertNotNull(carSlot.getDepartureTime());
        assertNotNull(carSlot.getArrivalTime());
    }


    @Test
    void leaveParkingLotWhereWeAreNotParkedTest() {
        when(persistenceManager.findById(parkingLots.get(0).getId())).thenReturn(parkingLots.get(0));
        when(persistenceManager.findById(parkingLots.get(1).getId())).thenReturn(parkingLots.get(1));

        CarSlot carSlot = new CarSlot();
        carSlot.setType(parkingLots.get(0).getSlotTypes().get(0));
        CarSlot finalCarSlot = service.parkAtParkingLot(parkingLots.get(0).getId(),carSlot);

        ParkingLot parkingLot = parkingLots.get(0);
        PricingPolicy policy = new PricingPolicy();
        policy.setFlatFee(BigDecimal.valueOf(25.2));
        parkingLot.setFareProcessor(policy);
        assertThrows(NoSuchCarInParkingException.class, () -> service.leaveParkingLot(parkingLots.get(1).getId(), finalCarSlot));
    }

    @Test
    void leaveParkingLotWithFlatFeeAndPerHourTest() {
        when(persistenceManager.findById(parkingLots.get(0).getId())).thenReturn(parkingLots.get(0));

        CarSlot carSlot = new CarSlot();
        carSlot.setType(parkingLots.get(0).getSlotTypes().get(0));
        carSlot = service.parkAtParkingLot(parkingLots.get(0).getId(),carSlot);

        ParkingLot parkingLot = parkingLots.get(0);
        PricingPolicy policy = new PricingPolicy();
        policy.setFlatFee(BigDecimal.valueOf(25.2));
        policy.setPerHourFare(BigDecimal.valueOf(35.3));
        parkingLot.setFareProcessor(policy);
        carSlot = service.leaveParkingLot(parkingLots.get(0).getId(), carSlot);

        assertTrue(BigDecimal.valueOf(25.2).compareTo(carSlot.getPrice()) == 0);

        assertNotNull(carSlot.getDepartureTime());
        assertNotNull(carSlot.getArrivalTime());
    }

    @Test
    void parkAtParkingLot() {
        when(persistenceManager.findById(parkingLots.get(0).getId())).thenReturn(parkingLots.get(0));
        CarSlot carSlot = new CarSlot();
        carSlot.setType(parkingLots.get(0).getSlotTypes().get(0));
        carSlot = service.parkAtParkingLot(parkingLots.get(0).getId(),carSlot);
        assertNotNull(carSlot.getSlot());
        assertEquals(parkingLots.get(0).getId(), carSlot.getParkingLotId());

        CarSlot carSlot2 = new CarSlot();
        carSlot2.setType(parkingLots.get(0).getSlotTypes().get(0));

        parkingLots.get(0).getLayoutList().forEach(layout -> layout.setAvailable(0));
        //parking is now full
        assertThrows(ParkingIsFullException.class, () -> service.parkAtParkingLot(parkingLots.get(0).getId(),carSlot2));
    }

    @Test
    void parkAtParkingLotThatDoesNotExistTest() {
        when(persistenceManager.findById(parkingLots.get(0).getId())).thenReturn(parkingLots.get(0));
        CarSlot carSlot = new CarSlot();
        carSlot.setType("type of slot that does not exist");
        assertThrows(ParkingTypeDoesNotExistException.class, () -> service.parkAtParkingLot(parkingLots.get(0).getId(),carSlot));
    }


    @Test
    void updateParkingLot() {
        //new parking
        when(persistenceManager.findById(any())).thenReturn(null);
        when(persistenceManager.save(any())).thenReturn(parkingLots.get(1));
        ParkingLot result = service.updateParkingLot(55L, parkingLots.get(0));
        assertEquals(parkingLots.get(1), result);

        //existing parking lot, should return null
        when(persistenceManager.findById(any())).thenReturn(parkingLots.get(1));
        result = service.updateParkingLot(55L, parkingLots.get(0));

        assertNull(result);
    }

    @Test
    void searchParkingLot() {
        when(persistenceManager.getAll()).thenReturn(parkingLots);
        Collection<ParkingLot> result = service.searchParkingLot("test parking 1");
        assertEquals(1, result.size());
        assertEquals(parkingLots.get(0), result.toArray()[0]);

        result = service.searchParkingLot(" test parking 3     ");
        assertEquals(2, result.size());

        result = service.searchParkingLot("   ");
        assertEquals(4, result.size());

        result = service.searchParkingLot(" string that is not in the list  ");
        assertEquals(0, result.size());


    }
}