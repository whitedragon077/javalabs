package com.example.cars.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Represents a car entity with associated dealer information.
 */
@Entity
public class Car {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long year;
  private String make;
  private String model;
  private String vin;

  @ManyToOne
  @JoinColumn(name = "dealer_id")
  private Dealer dealer;

  public Car(Long id, String vin) {
    this.id = id;
    this.vin = vin;
  }

  public Car() {
        //the default constructor is provided to ensure Java Bean compatibility.
  }

  public void setId(Long id) {
    this.id = id;
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

  public void setVin(String vin) {
    this.vin = vin;
  }

  public Long getId() {
    return id;
  }

  public Long getYear() {
    return year;
  }

  public String getMake() {
    return make;
  }

  public String getModel() {
    return model;
  }

  public String getVin() {
    return vin;
  }

  public Dealer getDealer() {
    return dealer;
  }

  public void setDealer(Dealer dealer) {
    this.dealer = dealer;
  }

}
