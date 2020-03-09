package eu.fcheret.parkingtoll.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class DepartureIsBeforeArrivalException extends RuntimeException {
    public DepartureIsBeforeArrivalException(String message){
        super(message);
    }
}
