package com.example.cars.controller;

import com.example.cars.dto.CarDto;
import com.example.cars.entity.Car;
import com.example.cars.exceptions.CarAdditionException;
import com.example.cars.exceptions.CarAlreadyExistException;
import com.example.cars.exceptions.CarNotFoundException;
import com.example.cars.exceptions.DealerNotFoundException;
import com.example.cars.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class CarControllerTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addCar_ValidCar_ReturnsSuccessResponse() throws CarAlreadyExistException, DealerNotFoundException {
        Car car = new Car();
        doNothing().when(carService).addCar(anyLong(), any(Car.class));

        ResponseEntity<?> response = carController.addCar(1L, car);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Car saved", response.getBody());
    }

    @Test
    void addCar_CarAlreadyExists_ThrowsBadRequestException()
            throws CarAlreadyExistException, DealerNotFoundException {
        Car car = new Car();
        doThrow(new CarAlreadyExistException("Car already exists")).when(carService).addCar(anyLong(), any(Car.class));

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> carController.addCar(1L, car));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void addCarsBulk_ValidCars_ReturnsSuccessResponse()
            throws CarAlreadyExistException, DealerNotFoundException, CarAdditionException {
        List<Car> cars = Collections.singletonList(new Car());
        doNothing().when(carService).addCarsBulk(anyLong(), any());

        ResponseEntity<?> response = carController.addCarsBulk(1L, cars);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Автомобили были успешно сохранены!", response.getBody());
    }

    // Similar tests for other methods

    @Test
    void getCar_ValidId_ReturnsCar() throws CarNotFoundException {
        CarDto carDto = new CarDto();
        when(carService.getCarById(anyLong())).thenReturn(carDto);

        ResponseEntity<?> response = carController.getCar(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carDto, response.getBody());
    }

    @Test
    void getCar_CarNotFound_ThrowsBadRequestException() throws CarNotFoundException {
        when(carService.getCarById(anyLong())).thenThrow(new CarNotFoundException("Car not found"));

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> carController.getCar(1L));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void deleteCar_ValidId_ReturnsSuccessResponse() throws CarNotFoundException {
        doNothing().when(carService).deleteCar(anyLong());

        ResponseEntity<?> response = carController.deleteCar(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Car deleted", response.getBody());
    }

    @Test
    void updateCar_ValidCar_ReturnsSuccessResponse() throws CarNotFoundException {
        Car updatedCar = new Car();
        doNothing().when(carService).updateCar(anyLong(), any(Car.class));

        ResponseEntity<String> response = carController.updateCar(1L, updatedCar);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Car updated", response.getBody());
    }

    @Test
    void updateCar_CarNotFound_ThrowsBadRequestException() throws CarNotFoundException {
        Car updatedCar = new Car();
        doThrow(new CarNotFoundException("Car not found")).when(carService).updateCar(anyLong(), any(Car.class));

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> carController.updateCar(1L, updatedCar));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void transferCarToDealer_ValidCarAndDealer_ReturnsSuccessResponse()
            throws CarNotFoundException, DealerNotFoundException {
        doNothing().when(carService).transferCarToDealer(anyLong(), anyLong());

        ResponseEntity<String> response = carController.transferCarToDealer(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Car transferred to another dealer", response.getBody());
    }

    @Test
    void transferCarToDealer_CarOrDealerNotFound_ThrowsBadRequestException()
            throws CarNotFoundException, DealerNotFoundException {
        doThrow(new CarNotFoundException("Car not found")).when(carService).transferCarToDealer(anyLong(), anyLong());

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> carController.transferCarToDealer(1L, 2L));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void getCarByMake_ValidMake_ReturnsCar() throws CarNotFoundException {
        CarDto carDto = new CarDto();
        when(carService.getCarByMake(anyString())).thenReturn(carDto);

        ResponseEntity<?> response = carController.getCarByMake("Toyota");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carDto, response.getBody());
    }

    @Test
    void getCarByMake_CarNotFound_ThrowsBadRequestException() throws CarNotFoundException {
        when(carService.getCarByMake(anyString())).thenThrow(new CarNotFoundException("Car not found"));

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> carController.getCarByMake("Toyota"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void getAllCarsByDealerIdAndYear_ValidDealerIdAndYear_ReturnsCarList() throws CarNotFoundException {
        List<CarDto> carDtoList = Collections.singletonList(new CarDto());
        when(carService.getCarsByDealerIdAndYear(anyLong(), anyLong())).thenReturn(carDtoList);

        ResponseEntity<?> response = carController.getAllCarsByDealerIdAndYear(1L, 2022L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carDtoList, response.getBody());
    }

    @Test
    void getAllCarsByDealerIdAndYear_CarNotFound_ThrowsBadRequestException() throws CarNotFoundException {
        when(carService.getCarsByDealerIdAndYear(anyLong(), anyLong())).thenThrow(new CarNotFoundException("Car not found"));

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> carController.getAllCarsByDealerIdAndYear(1L, 2022L));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void testAddCar() throws CarAlreadyExistException, DealerNotFoundException {
        // Create a sample car
        Car car = new Car();
        car.setId(1L);
        car.setMake("Toyota");
        car.setModel("Camry");

        // Mock the car service
        doNothing().when(carService).addCar(anyLong(), any(Car.class));

        // Assert that no exceptions are thrown
        assertDoesNotThrow(() -> carController.addCar(1L, car));

        // Perform the request
        carController.addCar(1L, car);

        // Assert the response
        ResponseEntity<Object> response = carController.addCar(1L, car);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Car saved", response.getBody());
    }

    @Test
    void testAddCarsBulk() throws CarAlreadyExistException, DealerNotFoundException, CarAdditionException {
        // Create a list of sample cars
        List<Car> cars = new ArrayList<>();
        Car car1 = new Car();
        car1.setId(1L);
        car1.setMake("Toyota");
        car1.setModel("Camry");
        cars.add(car1);
        Car car2 = new Car();
        car2.setId(2L);
        car2.setMake("Honda");
        car2.setModel("Accord");
        cars.add(car2);

        // Mock the car service
        doNothing().when(carService).addCarsBulk(anyLong(), anyList());

        // Perform the request
        ResponseEntity<?> response = carController.addCarsBulk(1L, cars);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Автомобили были успешно сохранены!", response.getBody());
    }

    @Test
    void testGetCar() throws CarNotFoundException {
        // Create a sample car
        CarDto carDto = new CarDto();
        carDto.setId(1L);
        carDto.setMake("Toyota");
        carDto.setModel("Camry");

        // Mock the car service
        when(carService.getCarById(anyLong())).thenReturn(carDto);

        // Perform the request
        ResponseEntity<Object> response = carController.getCar(1L);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carDto, response.getBody());
    }

    @Test
    void testGetCarByMake()throws CarNotFoundException {
        // Create a sample car
        CarDto carDto = new CarDto();
        carDto.setId(1L);
        carDto.setMake("Toyota");
        carDto.setModel("Camry");

        // Mock the car service
        when(carService.getCarByMake(anyString())).thenReturn(carDto);

        // Perform the request
        ResponseEntity<Object> response = carController.getCarByMake("Toyota");

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(carDto, response.getBody());
    }

    @Test
    void testUpdateCar() throws CarNotFoundException {
        // Create a sample updated car
        Car updatedCar = new Car();
        updatedCar.setId(1L);
        updatedCar.setMake("Toyota");
        updatedCar.setModel("Corolla");

        // Mock the car service
        doNothing().when(carService).updateCar(anyLong(), any(Car.class));

        // Perform the request
        ResponseEntity<String> response = carController.updateCar(1L, updatedCar);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Car updated", response.getBody());
    }

    @Test
    void testTransferCarToDealer() throws CarNotFoundException, DealerNotFoundException {
        // Mock the car service
        doNothing().when(carService).transferCarToDealer(anyLong(), anyLong());

        // Perform the request
        ResponseEntity<String> response = carController.transferCarToDealer(1L, 2L);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Car transferred to another dealer", response.getBody());
    }

    @Test
    void testDeleteCar() throws CarNotFoundException {
        // Mock the car service
        doNothing().when(carService).deleteCar(anyLong());

        // Perform the request
        ResponseEntity<String> response = carController.deleteCar(1L);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Car deleted", response.getBody());
    }

    @Test
    void testGetAllCarsByDealerIdAndYear() throws CarNotFoundException {
        // Create a list of sample cars
        List<CarDto> cars = new ArrayList<>();
        CarDto car1 = new CarDto();
        car1.setId(1L);
        car1.setMake("Toyota");
        car1.setModel("Camry");
        cars.add(car1);
        CarDto car2 = new CarDto();
        car2.setId(2L);
        car2.setMake("Honda");
        car2.setModel("Accord");
        cars.add(car2);

        // Set the dealer ID and year
        Long dealerId = 1L;
        Long year = 2024L;

        // Mock the car service
        when(carService.getCarsByDealerIdAndYear(dealerId, year)).thenReturn(cars);

        // Perform the request
        ResponseEntity<Object> response = carController.getAllCarsByDealerIdAndYear(dealerId, 2024L);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cars, response.getBody());
    }
}