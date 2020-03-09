package eu.fcheret.parkingtoll.service;

import eu.fcheret.parkingtoll.exceptions.NoSuchCarInParkingException;
import eu.fcheret.parkingtoll.exceptions.ParkingIsFullException;
import eu.fcheret.parkingtoll.exceptions.ParkingNotFoundException;
import eu.fcheret.parkingtoll.exceptions.ParkingTypeDoesNotExistException;
import eu.fcheret.parkingtoll.model.CarSlot;
import eu.fcheret.parkingtoll.model.Layout;
import eu.fcheret.parkingtoll.model.ParkingLot;
import eu.fcheret.parkingtoll.persistence.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class ParkingLotService {


    private Dao<ParkingLot> persistenceManager;

    @Autowired
    public ParkingLotService(Dao<ParkingLot> persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    public ParkingLot createParkingLot(ParkingLot parkingLotItem) {
        return persistenceManager.save(parkingLotItem);
    }

    public ParkingLot deleteParkingLotById(Long parkingLotId) {
        //check if object exists
        ParkingLot parkingLot = findParkingLotByIdOrThrow(parkingLotId);
        persistenceManager.delete(parkingLotId);
        return parkingLot;
    }

    public ParkingLot getParkingById(Long parkingLotId) {
        ParkingLot parkingLot = persistenceManager.findById(parkingLotId);
        if(parkingLot == null){
            throw new ParkingNotFoundException("The given ID is not associated with a parking lot.");
        }
        return parkingLot;
    }

    public CarSlot leaveParkingLot(Long parkingLotId, CarSlot carSlotItem) {
        ParkingLot parkingLot = findParkingLotByIdOrThrow(parkingLotId);
        Layout layout = getLayoutByNameOrThrow(carSlotItem.getType(),parkingLot);
        CarSlot carSlot;
        synchronized (this) {
            carSlot = findCarSlotByIdOrThrow(carSlotItem.getSlot(), layout);
            carSlot.setDepartureTime(Instant.now());
            layout.incrementAndFree(carSlot.getSlot());
        }
        BigDecimal fare = parkingLot.getFareProcessor().computeFare(carSlot);
        carSlot.setPrice(fare);
        return carSlot;
    }

    public CarSlot parkAtParkingLot(Long parkingLotId, CarSlot slot) {
        //check if the asked parking exists
        ParkingLot parkingLot = findParkingLotByIdOrThrow(parkingLotId);

        //check if there is available slots
        Layout layout = getLayoutByNameOrThrow(slot.getType(), parkingLot);
        isAvailableOrThrow(layout);
        //found & available spot
        synchronized (this){
            //double check locking
            parkingLot = findParkingLotByIdOrThrow(parkingLotId);
            layout = getLayoutByNameOrThrow(slot.getType(), parkingLot);
            isAvailableOrThrow(layout);
            Long slotId = layout.decrementAndGetID();
            slot.setSlot(slotId);
            slot.setParkingLotId(parkingLotId);
            slot.setArrivalTime(Instant.now());
            layout.getCarSlots().put(slotId, slot);
            persistenceManager.save(parkingLot);
        }
        return slot;
    }

    private void isAvailableOrThrow(Layout layout){
        if(layout.getAvailable().get() == 0){
            throw new ParkingIsFullException("Parking lot is full for this type of car");
        }
    }

    public ParkingLot updateParkingLot(Long parkingLotId, ParkingLot parkingLotItem) {
        //parking lot exists ?
        ParkingLot parkingLot = persistenceManager.findById(parkingLotId);
        ParkingLot newParkingLot = null;
        if(parkingLot == null){
            //new one, save it
            newParkingLot = persistenceManager.save(parkingLotItem);
        } else {
            persistenceManager.update(parkingLotId, parkingLotItem);
        }
        return newParkingLot;
    }

    public Collection<ParkingLot> searchParkingLot(String searchString) {
        if(searchString == null || searchString.trim().isEmpty()){
            return persistenceManager.getAll();
        }
        return persistenceManager.getAll().stream().
                filter(parkingLot -> parkingLot.getName().trim().toUpperCase().contains(searchString.trim().toUpperCase()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Layout getLayoutByNameOrThrow(String slotName, ParkingLot parkingLot){
        return parkingLot.getLayoutList().stream()
                .filter(lay -> lay.getName().equals(slotName)).findFirst()
                .orElseThrow(() -> new ParkingTypeDoesNotExistException("This type of slot does not exist in this parking lot"));
    }

    private ParkingLot findParkingLotByIdOrThrow(Long id){
        ParkingLot parkingLot = persistenceManager.findById(id);
        if(parkingLot == null){
            throw new ParkingNotFoundException("The given ID is not associated with a parking lot.");
        }
        return parkingLot;
    }

    private CarSlot findCarSlotByIdOrThrow(Long id, Layout layout){
        CarSlot carSlot = layout.getCarSlots().get(id);
        if(carSlot == null){
            throw new NoSuchCarInParkingException("This car is not at the specified location");
        }
        return carSlot;
    }

}
