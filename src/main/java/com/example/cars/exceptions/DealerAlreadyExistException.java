package com.example.cars.exceptions;

/**
 * Exception thrown when attempting to create a dealer that already exists.
 */
public class DealerAlreadyExistException extends Exception {
  public DealerAlreadyExistException(String message) {
    super(message);
  }
}
