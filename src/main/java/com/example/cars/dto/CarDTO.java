package com.example.cars.dto;

import com.example.cars.entity.Car;

public class CarDTO {
    private Long year;
    private String make;
    private String model;
    private String vin;

    public static CarDTO toCar(Car entity)
    {
        CarDTO carModel = new CarDTO();
        carModel.setModel(entity.getModel());
        carModel.setMake(entity.getMake());
        carModel.setYear(entity.getYear());
        carModel.setVin(entity.getVin());
        return carModel;
    }

    public CarDTO() {
    }

    public Long getYear() {
        return year;
    }

    public String getMake() {
        return make;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getVin() {
        return vin;
    }

    public String getModel() {
        return model;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
