package com.engwork.pgndb.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class PgnDbExceptionHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = PgnDbException.class)
  public PgnDbExceptionModelImpl handleBaseException(PgnDbException pgnDbException) {
    return new
        PgnDbExceptionModelImpl(
        pgnDbException.getHeaderMessage(),
        pgnDbException.getMainMessage(),
        pgnDbException.getFailureType()
    );
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(value = Exception.class)
  public String handleException(Exception exception) {
    return exception.getMessage();
  }

}
