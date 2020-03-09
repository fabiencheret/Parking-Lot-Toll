package eu.fcheret.parkingtoll.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigDecimal;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PricingPolicy.class, name="simple")
})
public interface FareProcessor {

    BigDecimal computeFare(CarSlot carSlot);

}
