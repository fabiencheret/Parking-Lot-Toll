package eu.fcheret.parkingtoll.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.fcheret.parkingtoll.exceptions.DepartureIsBeforeArrivalException;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

/**
 * PricingPolicy
 */
@Validated
@Data
public class PricingPolicy implements FareProcessor {

  @JsonProperty("flat_fee")
  private BigDecimal flatFee = null;

  @JsonProperty("per_hour_fare")
  private BigDecimal perHourFare = null;

  public BigDecimal computeFare(CarSlot carSlot){
    Instant arrival = carSlot.getArrivalTime();
    Instant departure = carSlot.getDepartureTime();
    if(arrival.isAfter(departure)){
      throw new DepartureIsBeforeArrivalException("Departure is before arrival, should not happen");
    }
    BigDecimal result = BigDecimal.ZERO;
    if(flatFee != null && flatFee.compareTo(BigDecimal.ZERO) != 0){
      result = result.add(flatFee);
    }
    if(perHourFare != null && perHourFare.compareTo(BigDecimal.ZERO) != 0){
      int multiplier = 0;
      Duration duration = Duration.between(arrival,departure);
      multiplier += duration.toHours();
      if(duration.toMinutesPart() != 0){
        multiplier++;
      }
      result = result.add(perHourFare.multiply(BigDecimal.valueOf(multiplier)));
    }
    return result.stripTrailingZeros();
  }

}

