package eu.fcheret.parkingtoll.model;

import eu.fcheret.parkingtoll.exceptions.DepartureIsBeforeArrivalException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PricingPolicyTest {

    @Test
    void computeFareWithOnlyFlatFee() {
        PricingPolicy policy = new PricingPolicy();

        BigDecimal flatFee = BigDecimal.valueOf(12342.55);
        policy.setFlatFee(flatFee);

        Instant now = Instant.now();
        Instant oneHour = now.plus(Duration.ofHours(1));
        CarSlot carSlot = new CarSlot();
        carSlot.setArrivalTime(now);
        carSlot.setDepartureTime(oneHour);

        BigDecimal fare = policy.computeFare(carSlot);
        assertEquals(flatFee, fare);
        carSlot.setArrivalTime(now);
        carSlot.setDepartureTime(now);

        fare = policy.computeFare(carSlot);
        assertEquals(flatFee, fare);

        flatFee = BigDecimal.valueOf(- 12_355.55);
        policy.setFlatFee(flatFee);
        carSlot.setArrivalTime(now);
        carSlot.setDepartureTime(oneHour);
        fare = policy.computeFare(carSlot);
        assertEquals(flatFee, fare);

        Instant longAfter = now.plus(Duration.ofDays(500));
        carSlot.setArrivalTime(now);
        carSlot.setDepartureTime(longAfter);

        fare = policy.computeFare(carSlot);
        assertEquals(flatFee, fare);
    }

    @Test
    void computeFareWithOnlyHourRate() {
        PricingPolicy policy = new PricingPolicy();
        BigDecimal rate = BigDecimal.valueOf(12342.55);
        BigDecimal flatFee = BigDecimal.valueOf(0);
        policy.setPerHourFare(rate);
        policy.setFlatFee(flatFee);
        CarSlot carSlot = new CarSlot();
        Instant now = Instant.now();
        carSlot.setDepartureTime(now);
        carSlot.setArrivalTime(now);

        BigDecimal fare = policy.computeFare(carSlot);
        assertEquals(0, BigDecimal.ZERO.compareTo(fare));

        Instant oneHour = now.plus(Duration.ofHours(1));
        carSlot.setArrivalTime(now);
        carSlot.setDepartureTime(oneHour);
        fare = policy.computeFare(carSlot);
        assertEquals(rate, fare);

        Instant twoHour = now.plus(Duration.ofHours(2));
        carSlot.setArrivalTime(now);
        carSlot.setDepartureTime(twoHour);
        fare = policy.computeFare(carSlot);
        assertEquals(0, rate.multiply(BigDecimal.valueOf(2)).compareTo(fare));

        Instant twoHourAndSomeMinutes = now.plus(Duration.ofHours(2)).plus(Duration.ofMinutes(15));
        carSlot.setArrivalTime(now);
        carSlot.setDepartureTime(twoHourAndSomeMinutes);
        fare = policy.computeFare(carSlot);
        assertEquals(0, rate.multiply(BigDecimal.valueOf(3)).compareTo(fare));

        //departure is before arrival - should not happen
        carSlot.setArrivalTime(twoHourAndSomeMinutes);
        carSlot.setDepartureTime(now);
        assertThrows(DepartureIsBeforeArrivalException.class,() -> policy.computeFare(carSlot));
    }


    @Test
    void testMixOfFares(){
        PricingPolicy policy = new PricingPolicy();
        BigDecimal rate = BigDecimal.valueOf(12342.55);
        BigDecimal flatFee = BigDecimal.valueOf(54321);
        policy.setPerHourFare(rate);
        policy.setFlatFee(flatFee);

        //0s fare
        CarSlot carSlot = new CarSlot();
        Instant now = Instant.now();
        carSlot.setArrivalTime(now);
        carSlot.setDepartureTime(now);
        BigDecimal fare = policy.computeFare(carSlot);
        assertEquals(flatFee, fare);

        //1h fare
        carSlot.setArrivalTime(now);
        carSlot.setDepartureTime(now.plus(Duration.ofHours(1)));
        fare = policy.computeFare(carSlot);
        assertEquals(0, flatFee.add(rate).compareTo(fare));

        //1h20m fare
        carSlot.setDepartureTime(now.plus(Duration.ofHours(1)).plus(Duration.ofMinutes(20)));
        fare = policy.computeFare(carSlot);
        assertEquals(0, flatFee.add(rate.multiply(BigDecimal.valueOf(2))).compareTo(fare));
    }

}