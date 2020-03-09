package eu.fcheret.parkingtoll.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ParkingLot
 */
@Validated
@Data
public class ParkingLot   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("name")
  @NotNull @Size(min = 1)
  private String name = null;

  @JsonProperty("layout")
  @Valid @NotNull
  private List<Layout> layoutList = new ArrayList<>();

  @JsonProperty("pricing_policy")
  @Valid @NotNull
  private FareProcessor fareProcessor;

  @JsonIgnore
  public List<String> getSlotTypes(){
    return layoutList.stream().map(Layout::getName).collect(Collectors.toList());
  }

}

