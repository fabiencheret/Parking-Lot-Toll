package eu.fcheret.parkingtoll.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchCarInParkingException extends RuntimeException {

    public NoSuchCarInParkingException(String message){
        super(message);
    }

}
