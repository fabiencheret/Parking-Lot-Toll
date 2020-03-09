package eu.fcheret.parkingtoll.controller;

import eu.fcheret.parkingtoll.api.ParkingLotApiController;
import eu.fcheret.parkingtoll.model.CarSlot;
import eu.fcheret.parkingtoll.model.ParkingLot;
import eu.fcheret.parkingtoll.model.ParkingLotTest;
import eu.fcheret.parkingtoll.model.PricingPolicy;
import eu.fcheret.parkingtoll.service.ParkingLotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ParkingLotApiControllerTest {

    @Mock @Autowired
    private ParkingLotService parkingLotService;
    @InjectMocks
    private ParkingLotApiController parkingLotApiController;

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
    void searchEmptyParkingLotTest() {
        when(parkingLotService.searchParkingLot(any())).thenReturn(Collections.emptyList());

        ResponseEntity<Collection<ParkingLot>> result = parkingLotApiController.searchParkingLot("");
        assertNotNull(result.getBody());
        assertEquals(0, result.getBody().size());
    }

    @Test
    void searchParkingLotTestWithWrongName() {
        when(parkingLotService.searchParkingLot("")).thenReturn(parkingLots);
        when(parkingLotService.searchParkingLot("String that is not in the names of the parking lots"))
                .thenReturn(Collections.emptyList());

        ResponseEntity<Collection<ParkingLot>> result = parkingLotApiController.searchParkingLot("");
        assertNotNull(result.getBody());
        assertEquals(parkingLots.size(), result.getBody().size());

        result = parkingLotApiController.searchParkingLot("String that is not in the names of the parking lots");
        assertNotNull(result.getBody());
        assertEquals(0, result.getBody().size());
    }

    @Test
    void searchParkingLotTestWithCorrectName() {
        when(parkingLotService.searchParkingLot("")).thenReturn(parkingLots);
        when(parkingLotService.searchParkingLot("Test parking 1"))
                .thenReturn(Collections.singletonList(parkingLots.get(0)));

        when(parkingLotService.searchParkingLot("Test parking 3"))
                .thenReturn(parkingLots.subList(2,4));

        ResponseEntity<Collection<ParkingLot>> result = parkingLotApiController.searchParkingLot("");
        assertNotNull(result.getBody());
        assertEquals(parkingLots.size(), result.getBody().size());

        //entire name
        result = parkingLotApiController.searchParkingLot("Test parking 1");
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());

        result = parkingLotApiController.searchParkingLot("Test parking 3");
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());
    }

    @Test
    void parkingLotDeleteByIdTest(){
        //non existing id
        ResponseEntity<ParkingLot> result = parkingLotApiController.deleteParkingLotById(-5L);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        when(parkingLotService.deleteParkingLotById(1L)).thenReturn(parkingLots.get(0));
        result = parkingLotApiController.deleteParkingLotById(1L);
        assertEquals(parkingLots.get(0), result.getBody());
        assertEquals(result.getStatusCode(), HttpStatus.OK);

        verify(parkingLotService, times(2)).deleteParkingLotById(any());
    }

    @Test
    void addParkingLotTest(){
        ParkingLot storedParkingLot = ParkingLotTest.generateParking("created parking", 55L);

        when(parkingLotService.createParkingLot(any())).thenReturn(storedParkingLot);

        ResponseEntity<ParkingLot> response = parkingLotApiController.createParkingLot(parkingLots.get(0));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(URI.create("/parking_lot/55") , response.getHeaders().getLocation());
        assertNotNull(response.getBody());
        assertEquals(55L, response.getBody().getId());
    }

    @Test
    void parkingLotGetByIdTest(){
        ParkingLot storedParkingLot = ParkingLotTest.generateParking("created parking", 5L);
        when(parkingLotService.getParkingById(5L)).thenReturn(storedParkingLot);

        ResponseEntity<ParkingLot> result = parkingLotApiController.getParkingById(5L);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void leaveParkingLotTest(){
        ParkingLot storedParkingLot = ParkingLotTest.generateParking("created parking", 5L);
        PricingPolicy policy = new PricingPolicy();
        policy.setFlatFee(BigDecimal.valueOf(1));
        storedParkingLot.setFareProcessor(policy);

        when(parkingLotService.getParkingById(storedParkingLot.getId())).thenReturn(storedParkingLot);

        CarSlot carSlot = new CarSlot();
        carSlot.setArrivalTime(Instant.now().minus(Duration.ofHours(1)));
        carSlot.setType(storedParkingLot.getSlotTypes().get(0));
        //storedParkingLot.parkCar(carSlot);

        ResponseEntity<CarSlot> result = parkingLotApiController.leaveParkingLot(storedParkingLot.getId(), carSlot);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void parkParkingLotTest(){
        ParkingLot storedParkingLot = ParkingLotTest.generateParking("created parking", 5L);
        CarSlot carSlot = new CarSlot();
        carSlot.setType(storedParkingLot.getSlotTypes().get(0));
        when(parkingLotService.getParkingById(storedParkingLot.getId())).thenReturn(storedParkingLot);

        ResponseEntity<CarSlot> result = parkingLotApiController.parkAtParkingLot(storedParkingLot.getId(), carSlot);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void updateParkingLotTest(){
        //no existing parking lots
        ParkingLot storedParkingLot = ParkingLotTest.generateParking("created parking", 5L);
        when(parkingLotService.updateParkingLot(anyLong(),any())).thenReturn(null);

        ResponseEntity<ParkingLot> result = parkingLotApiController.updateParkingLot(5L, storedParkingLot);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(URI.create("/parking_lot/" + storedParkingLot.getId()), result.getHeaders().getLocation());

        when(parkingLotService.updateParkingLot(anyLong(), any())).thenReturn(storedParkingLot);
        result = parkingLotApiController.updateParkingLot(5L, storedParkingLot);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }
    

}
