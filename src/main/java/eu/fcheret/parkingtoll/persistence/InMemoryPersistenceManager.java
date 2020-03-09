package eu.fcheret.parkingtoll.persistence;

import eu.fcheret.parkingtoll.exceptions.ParkingNotFoundException;
import eu.fcheret.parkingtoll.model.ParkingLot;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryPersistenceManager implements Dao<ParkingLot> {

    private static final Map<Long, ParkingLot> parkingLotMap = new HashMap<>();
    private static AtomicLong idCounter = new AtomicLong();

    @Override
    public Collection<ParkingLot> getAll() {
        //prevent user from deleting items from the collection
        return Collections.unmodifiableCollection(parkingLotMap.values());
    }

    @Override
    public ParkingLot save(ParkingLot parkingLot) {
        parkingLot.setId(createID());
        parkingLotMap.put(parkingLot.getId(), parkingLot);
        return parkingLot;
    }

    @Override
    public ParkingLot findById(Long id) {
        ParkingLot result = parkingLotMap.get(id);
        if(result == null){
            throw new ParkingNotFoundException("The given ID is not associated with a parking lot.");
        }
        return result;
    }

    @Override
    public void update(Long id, ParkingLot parkingLot) {
        //secure case where user puts wrong ID
        parkingLot.setId(id);
        parkingLotMap.put(parkingLot.getId(), parkingLot);
    }

    @Override
    public void delete(Long id) {
        parkingLotMap.remove(id);
    }

    @Override
    public void clearDatabase() {
        parkingLotMap.clear();
        idCounter.set(0);
    }

    private static Long createID(){
        return idCounter.getAndIncrement();
    }


}
