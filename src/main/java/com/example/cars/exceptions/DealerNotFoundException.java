package com.example.cars.exceptions;

/**
 * Exception indicating that a dealer was not found.
 * This exception is thrown when attempting to access or manipulate a dealer that does not exist.
 */
public class DealerNotFoundException extends  Exception {
  public DealerNotFoundException(String message) {
    super(message);
  }
}
