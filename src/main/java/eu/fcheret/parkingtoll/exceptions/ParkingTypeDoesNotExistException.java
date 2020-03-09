package eu.fcheret.parkingtoll.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
    public class ParkingTypeDoesNotExistException extends RuntimeException {
    public ParkingTypeDoesNotExistException(String message){
        super(message);
    }
}
