package com.example.cars.exceptions;

/**
 * Exception indicating that a car was not found.
 * This exception is thrown when attempting to access or manipulate a car that does not exist.
 */
public class CarNotFoundException extends  Exception {
  public CarNotFoundException(String message) {
    super(message);
  }
}
