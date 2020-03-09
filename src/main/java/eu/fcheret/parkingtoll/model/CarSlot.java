package eu.fcheret.parkingtoll.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;


@Validated @Data
public class CarSlot   {

  @JsonProperty("slot")
  private Long slot = null;

  @JsonProperty("parking_lot_id")
  private Long parkingLotId = null;

  @JsonProperty("arrival_time")
  private Instant arrivalTime = null;

  @JsonProperty("departure_time")
  private Instant departureTime = null;

  @JsonProperty("type")
  @NotNull
  private String type = null;

  @JsonProperty("price")
  private BigDecimal price = null;

}

