package com.example.cars.dto;

import com.example.cars.entity.Car;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CarDtoTest {

    @Test
    void testToCar() {
        Car car = new Car();
        car.setYear(2022L);
        car.setMake("TestMake");
        car.setModel("TestModel");
        car.setVin("TestVin");

        CarDto carDto = CarDto.toCar(car);

        assertNotNull(carDto);
        assertEquals(2022L, carDto.getYear());
        assertEquals("TestMake", carDto.getMake());
        assertEquals("TestModel", carDto.getModel());
        assertEquals("TestVin", carDto.getVin());
    }

    @Test
    void testGetYear() {
        CarDto carDto = new CarDto();
        carDto.setYear(2022L);

        Long year = carDto.getYear();

        assertEquals(2022L, year);
    }

    @Test
    void testSetYear() {
        CarDto carDto = new CarDto();

        carDto.setYear(2022L);

        assertEquals(2022L, carDto.getYear());
    }

    @Test
    void testGetMake() {
        CarDto carDto = new CarDto();
        carDto.setMake("TestMake");

        String make = carDto.getMake();

        assertEquals("TestMake", make);
    }

    @Test
    void testSetMake() {
        CarDto carDto = new CarDto();

        carDto.setMake("TestMake");

        assertEquals("TestMake", carDto.getMake());
    }

    @Test
    void testGetModel() {
        CarDto carDto = new CarDto();
        carDto.setModel("TestModel");

        String model = carDto.getModel();

        assertEquals("TestModel", model);
    }

    @Test
    void testSetModel() {
        CarDto carDto = new CarDto();

        carDto.setModel("TestModel");

        assertEquals("TestModel", carDto.getModel());
    }

    @Test
    void testGetVin() {
        CarDto carDto = new CarDto();
        carDto.setVin("TestVin");

        String vin = carDto.getVin();

        assertEquals("TestVin", vin);
    }

    @Test
    void testSetVin() {
        CarDto carDto = new CarDto();

        carDto.setVin("TestVin");

        assertEquals("TestVin", carDto.getVin());
    }
}
