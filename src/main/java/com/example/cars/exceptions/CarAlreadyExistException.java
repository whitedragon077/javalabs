package com.example.cars.exceptions;

/**
 * Exception thrown when attempting to create a car that already exists.
 */
public class CarAlreadyExistException extends Exception {
  public CarAlreadyExistException(String message) {
    super(message);
  }
}
