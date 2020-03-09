package eu.fcheret.parkingtoll.model;

import eu.fcheret.parkingtoll.exceptions.ParkingIsFullException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LayoutTest {

    @Test
    void decrementAndGetID() {
        Layout layout = new Layout();
        layout.setAvailable(2);
        Long id1 = layout.decrementAndGetID();
        assertEquals(1, layout.getAvailable().get());
        Long id2 = layout.decrementAndGetID();
        assertEquals(0, layout.getAvailable().get());
        assertThrows(ParkingIsFullException.class, layout::decrementAndGetID);
        assertEquals(0, layout.getAvailable().get());
        assertNotEquals(id2,id1);
    }

    @Test
    void incrementAndFree() {
        Layout layout = new Layout();
        layout.setAvailable(2);
        Long id1 = layout.decrementAndGetID();
        assertEquals(1, layout.getAvailable().get());
        layout.incrementAndFree(id1);
        assertEquals(2, layout.getAvailable().get());
        Long id2 = layout.decrementAndGetID();
        Long id3 = layout.decrementAndGetID();
        //id1 should be re-used
        assertTrue(id1.equals(id3) || id2.equals(id3));
    }

}