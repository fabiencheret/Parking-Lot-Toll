package eu.fcheret.parkingtoll.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import eu.fcheret.parkingtoll.exceptions.ParkingIsFullException;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Layout
 */
@Validated
@Data
@JsonTypeName("layout")
public class Layout {
  @JsonProperty("name")
  @NotNull
  private String name;

  @JsonProperty("available")
  @NotNull
  private AtomicInteger available = new AtomicInteger();
  @JsonIgnore
  Deque<Long> ids;
  @JsonIgnore
  Map<Long, CarSlot> carSlots;

  public Layout() {
    this.carSlots = new HashMap<>();
  }


  public void setAvailable(Integer available) {
    this.available.set(available);
    this.ids = LongStream.range(0, available).boxed().collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
  }

  public Long decrementAndGetID(){
    Long id;
    try {
      id = ids.pop();
    } catch (NoSuchElementException e){
      throw new ParkingIsFullException("Parking is full for this type of car");
    }
    this.available.decrementAndGet();
    return id;
  }

  public void incrementAndFree(Long id){
    ids.add(id);
    this.available.incrementAndGet();
  }

}

