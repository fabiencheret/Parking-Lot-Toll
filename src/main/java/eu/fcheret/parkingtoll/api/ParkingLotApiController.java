package eu.fcheret.parkingtoll.api;

import eu.fcheret.parkingtoll.model.CarSlot;
import eu.fcheret.parkingtoll.model.ParkingLot;
import eu.fcheret.parkingtoll.service.ParkingLotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

@RestController
public class ParkingLotApiController {

    private static final Logger log = LoggerFactory.getLogger(ParkingLotApiController.class);

    private ParkingLotService service;

    @Autowired
    public ParkingLotApiController(ParkingLotService service) {
        this.service = service;
    }

    @PostMapping(value = "/parking_lot",
            produces = { "application/json" },
            consumes = { "application/json" })
    public ResponseEntity<ParkingLot> createParkingLot(@Valid @RequestBody ParkingLot parkingLotItem) {

        parkingLotItem = service.createParkingLot(parkingLotItem);
        return ResponseEntity.created(URI.create("/parking_lot/" + parkingLotItem.getId())).body(parkingLotItem);
    }

    @DeleteMapping(value = "/parking_lot/{parkingLotId}",
            produces = { "application/json" },
            consumes = { "application/json" })
    public ResponseEntity<ParkingLot> deleteParkingLotById(@PathVariable("parkingLotId") Long parkingLotId) {
        ParkingLot parkingLot = service.deleteParkingLotById(parkingLotId);
        return ResponseEntity.ok(parkingLot);
    }

    @GetMapping(value = "/parking_lot/{parkingLotId}",
            produces = { "application/json" })
    public ResponseEntity<ParkingLot> getParkingById(@PathVariable("parkingLotId") Long parkingLotId) {
        ParkingLot parkingLot = service.getParkingById(parkingLotId);
        return ResponseEntity.ok(parkingLot);
    }

    @DeleteMapping(value = "/parking_lot/{parkingLotId}/park",
            produces = { "application/json" })
    public ResponseEntity<CarSlot> leaveParkingLot(@PathVariable("parkingLotId") Long parkingLotId, @Valid @RequestBody CarSlot carSlotItem) {

        CarSlot carSlot = service.leaveParkingLot(parkingLotId, carSlotItem);
        return ResponseEntity.ok(carSlot);
    }

    @PostMapping(value = "/parking_lot/{parkingLotId}/park",
            produces = { "application/json" },
            consumes = { "application/json" })
    public ResponseEntity<CarSlot> parkAtParkingLot(@PathVariable("parkingLotId") Long parkingLotId, @Valid @RequestBody CarSlot carSlotItem) {
        //check if the asked parking exists
        CarSlot slot = service.parkAtParkingLot(parkingLotId, carSlotItem);
        return ResponseEntity.ok(slot);
    }

    @PutMapping(value = "/parking_lot/{parkingLotId}",
            produces = { "application/json" },
            consumes = { "application/json" })
    public ResponseEntity<ParkingLot> updateParkingLot(@PathVariable("parkingLotId") Long parkingLotId, @Valid @RequestBody ParkingLot parkingLotItem) {
        ParkingLot parkingLot = service.updateParkingLot(parkingLotId, parkingLotItem);
        if(parkingLot == null){
            return ResponseEntity.created(URI.create("/parking_lot/" + parkingLotItem.getId())).body(parkingLotItem);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/parking_lot",
            produces = { "application/json" })
    public ResponseEntity<Collection<ParkingLot>> searchParkingLot(@Valid @RequestParam(value = "searchString", required = false) String searchString) {
        Collection<ParkingLot> parkingLots = service.searchParkingLot(searchString);
        return ResponseEntity.ok(parkingLots);
    }

}
