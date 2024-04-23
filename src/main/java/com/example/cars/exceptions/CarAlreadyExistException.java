package com.example.cars.exceptions;

public class CarAlreadyExistException extends Exception{
    public CarAlreadyExistException(String message) {
        super(message);
    }
}
