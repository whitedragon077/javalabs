package com.example.cars.dto;

import com.example.cars.entity.Car;
import com.example.cars.entity.Dealer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DealerDtoTest {

    @Test
    void testToModel() {
        Dealer dealer = new Dealer();
        dealer.setName("TestDealer");
        dealer.setAddress("TestAddress");

        Car car1 = new Car();
        car1.setMake("TestMake1");
        car1.setModel("TestModel1");
        car1.setVin("TestVin1");

        Car car2 = new Car();
        car2.setMake("TestMake2");
        car2.setModel("TestModel2");
        car2.setVin("TestVin2");

        dealer.getCarList().add(car1);
        dealer.getCarList().add(car2);

        DealerDto dealerDto = DealerDto.toDealer(dealer);

        assertNotNull(dealerDto);
        assertEquals("TestDealer", dealerDto.getName());
        assertEquals("TestAddress", dealerDto.getAddress());
        assertEquals(2, dealerDto.getCarsList().size());
        assertEquals("TestMake1", dealerDto.getCarsList().get(0).getMake());
        assertEquals("TestModel1", dealerDto.getCarsList().get(0).getModel());
        assertEquals("TestVin1", dealerDto.getCarsList().get(0).getVin());
        assertEquals("TestMake2", dealerDto.getCarsList().get(1).getMake());
        assertEquals("TestModel2", dealerDto.getCarsList().get(1).getModel());
        assertEquals("TestVin2", dealerDto.getCarsList().get(1).getVin());
    }

    @Test
    void testGetName() {
        DealerDto dealerDto = new DealerDto();
        dealerDto.setName("TestDealer");

        String name = dealerDto.getName();

        assertEquals("TestDealer", name);
    }

    @Test
    void testSetName() {
        DealerDto dealerDto = new DealerDto();

        dealerDto.setName("TestDealer");

        assertEquals("TestDealer", dealerDto.getName());
    }

    @Test
    void testGetAddress() {
        DealerDto dealerDto = new DealerDto();
        dealerDto.setAddress("TestAddress");

        String address = dealerDto.getAddress();

        assertEquals("TestAddress", address);
    }

    @Test
    void testSetAddress() {
        DealerDto dealerDto = new DealerDto();

        dealerDto.setAddress("TestAddress");

        assertEquals("TestAddress", dealerDto.getAddress());
    }

    @Test
    void testGetCarsList() {
        DealerDto dealerDto = new DealerDto();
        CarDto carDto1 = new CarDto();
        carDto1.setMake("TestMake1");
        carDto1.setModel("TestModel1");
        carDto1.setVin("TestVin1");

        CarDto carDto2 = new CarDto();
        carDto2.setMake("TestMake2");
        carDto2.setModel("TestModel2");
        carDto2.setVin("TestVin2");

        dealerDto.getCarsList().add(carDto1);
        dealerDto.getCarsList().add(carDto2);

        List<CarDto> carsList = dealerDto.getCarsList();

        assertEquals(2, carsList.size());
        assertEquals("TestMake1", carsList.get(0).getMake());
        assertEquals("TestModel1", carsList.get(0).getModel());
        assertEquals("TestVin1", carsList.get(0).getVin());
        assertEquals("TestMake2", carsList.get(1).getMake());
        assertEquals("TestModel2", carsList.get(1).getModel());
        assertEquals("TestVin2", carsList.get(1).getVin());
    }

    @Test
    void testSetCarsList() {
        DealerDto dealerDto = new DealerDto();
        CarDto carDto1 = new CarDto();
        carDto1.setMake("TestMake1");
        carDto1.setModel("TestModel1");
        carDto1.setVin("TestVin1");

        CarDto carDto2 = new CarDto();
        carDto2.setMake("TestMake2");
        carDto2.setModel("TestModel2");
        carDto2.setVin("TestVin2");

        List<CarDto> carsList = new ArrayList<>();
        carsList.add(carDto1);
        carsList.add(carDto2);

        dealerDto.setCarsList(carsList);

        List<CarDto> resultCarsList = dealerDto.getCarsList();

        assertEquals(2, resultCarsList.size());
        assertEquals("TestMake1", resultCarsList.get(0).getMake());
        assertEquals("TestModel1", resultCarsList.get(0).getModel());
        assertEquals("TestVin1", resultCarsList.get(0).getVin());
        assertEquals("TestMake2", resultCarsList.get(1).getMake());
        assertEquals("TestModel2", resultCarsList.get(1).getModel());
        assertEquals("TestVin2", resultCarsList.get(1).getVin());
    }
}
