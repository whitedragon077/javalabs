package com.example.cars.exceptions;

public class CarNotFoundException extends  Exception{
    public CarNotFoundException(String message)
    {
        super(message);
    }
}
