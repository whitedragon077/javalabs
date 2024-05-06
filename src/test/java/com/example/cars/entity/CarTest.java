package com.example.cars.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CarTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        Long id = 1L;
        Long year = 2022L;
        String make = "Toyota";
        String model = "Camry";
        String vin = "ABC123";
        Dealer dealer = new Dealer();

        // Act
        Car car = new Car();
        car.setId(id);
        car.setYear(year);
        car.setMake(make);
        car.setModel(model);
        car.setVin(vin);
        car.setDealer(dealer);

        // Assert
        Assertions.assertEquals(id, car.getId());
        Assertions.assertEquals(year, car.getYear());
        Assertions.assertEquals(make, car.getMake());
        Assertions.assertEquals(model, car.getModel());
        Assertions.assertEquals(vin, car.getVin());
        Assertions.assertEquals(dealer, car.getDealer());
    }

    @Test
    void testConstructorWithIdAndVin() {
        // Arrange
        Long id = 1L;
        String vin = "ABC123";

        // Act
        Car car = new Car(id, vin);

        // Assert
        Assertions.assertEquals(id, car.getId());
        Assertions.assertEquals(vin, car.getVin());
    }
}