package com.example.cars.dto;

import com.example.cars.entity.Car;

import java.util.Objects;

/**
 * Data Transfer Object (DTO) representing information about a car.
 * Used to transfer simplified car data between layers of the application.
 */
public class CarDto {
  private Long id;
  private Long year;
  private String make;
  private String model;
  private String vin;

  /**
     * Converts a Car entity to a CarDto model.
     *
     * @param entity The Car entity to convert.
     * @return CarDto model with simplified car information.
     */
  public static CarDto toCar(Car entity) {
    CarDto carModel = new CarDto();
    carModel.setId(entity.getId());
    carModel.setModel(entity.getModel());
    carModel.setMake(entity.getMake());
    carModel.setYear(entity.getYear());
    carModel.setVin(entity.getVin());
    return carModel;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    CarDto other = (CarDto) obj;
    return Objects.equals(this.make, other.make) &&
            Objects.equals(this.model, other.model) &&
            Objects.equals(this.year, other.year) &&
            Objects.equals(this.vin, other.vin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(make, model, year, vin);
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public CarDto() {
        //the default constructor is provided to ensure Java Bean compatibility.
  }

  public CarDto(Long id, String vin) {
    this.id = id;
    this.vin = vin;
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
