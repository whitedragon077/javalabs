package com.example.cars.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Global exception handler for the Car application.
 */
@ControllerAdvice
public class ExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

  /**
     * Handles exceptions to type InternalServerErrorException.
     *
     * @param ex The InternalServerErrorException instance.
     * @return ResponseEntity with a status of INTERNAL_SERVER_ERROR and the exception message.
     */
  @org.springframework.web.bind.annotation.ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
    log.error("500 Internal Server Error - {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("500: Internal Server Error");
  }

  /**
     * Handles exceptions of type BadRequestException.
     *
     * @param ex The BadRequestException instance.
     * @return ResponseEntity with a status of BAD_REQUEST and the exception message.
     */
  @org.springframework.web.bind.annotation.ExceptionHandler({HttpClientErrorException.class})
  public ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex,
                                                                 WebRequest request) {
    log.warn("400 Bad Request - {}",  ex.getStatusText());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("400: Bad Request");
  }

  @org.springframework.web.bind.annotation.ExceptionHandler({NoResourceFoundException.class})
  public ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex,
                                                                 WebRequest request) {
    log.warn("404 Not Found - {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("404: Not Found");
  }
}
