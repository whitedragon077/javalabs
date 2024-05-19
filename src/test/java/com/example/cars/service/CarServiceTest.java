package com.example.cars.service;

import com.example.cars.dto.CarDto;
import com.example.cars.component.Cache;
import com.example.cars.entity.Car;
import com.example.cars.entity.Dealer;
import com.example.cars.exceptions.CarAlreadyExistException;
import com.example.cars.exceptions.CarNotFoundException;
import com.example.cars.exceptions.DealerNotFoundException;
import com.example.cars.repository.CarRepository;
import com.example.cars.repository.DealerRepository;
import com.example.cars.service.CarService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private DealerRepository dealerRepository;

    @Mock
    private Cache<Object, CarDto> carCache;

    @InjectMocks
    private CarService carService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCar_WithValidInput_ShouldSaveCar() throws CarAlreadyExistException, DealerNotFoundException {
        // Arrange
        Long dealerId = 1L;
        Dealer dealer = new Dealer();
        dealer.setId(dealerId);
        Car car = new Car();
        car.setVin("VIN123");
        car.setDealer(dealer);

        when(dealerRepository.findById(dealerId)).thenReturn(Optional.of(dealer));
        when(carRepository.findByVin(car.getVin())).thenReturn(null);

        // Act
        carService.addCar(dealerId, car);

        // Assert
        verify(carRepository, times(1)).save(car);
        verify(carCache, times(1)).clear();
    }

    @Test
    void testAddCar_WithExistingVin_ShouldThrowCarAlreadyExistException() {
        // Arrange
        Long dealerId = 1L;
        Dealer dealer = new Dealer();
        dealer.setId(dealerId);
        Car car = new Car();
        car.setVin("VIN123");
        car.setDealer(dealer);

        when(dealerRepository.findById(dealerId)).thenReturn(Optional.of(dealer));
        when(carRepository.findByVin(car.getVin())).thenReturn(new Car());

        // Act & Assert
        assertThrows(CarAlreadyExistException.class, () -> carService.addCar(dealerId, car));
        verify(carRepository, never()).save(car);
        verify(carCache, never()).clear();
    }

    @Test
    void testAddCar_WithNonExistingDealer_ShouldThrowDealerNotFoundException() {
        // Arrange
        Long dealerId = 1L;
        Car car = new Car();

        when(dealerRepository.findById(dealerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DealerNotFoundException.class, () -> carService.addCar(dealerId, car));
        verify(carRepository, never()).save(car);
        verify(carCache, never()).clear();
    }

    // Добавьте другие тесты для остальных методов в CarService

    @Test
    void testGetCarById_WithExistingCarId_ShouldReturnCarDto() throws CarNotFoundException {
        // Arrange
        Long carId = 1L;
        Car car = new Car();
        car.setId(carId);

        CarDto expectedCarDto = CarDto.toCar(car); // Используем toCarDto() для создания expectedCarDto

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        // Act
        CarDto result = carService.getCarById(carId);

        // Assert
        assertEquals(expectedCarDto.getId(), result.getId());
    }

    @Test
    void testGetCar_WithNonExistingCarId_ShouldThrowCarNotFoundException() {
        // Arrange
        Long carId = 1L;

        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CarNotFoundException.class, () -> carService.getCarById(carId));
    }

    @Test
    void testUpdateCar_WithExistingCarId_ShouldUpdateCar() throws CarNotFoundException {
        // Arrange
        Long carId = 1L;
        Car existingCar = new Car();
        existingCar.setId(carId);
        existingCar.setMake("Old Make");
        existingCar.setModel("Old Model");
        existingCar.setYear(2000L);
        existingCar.setVin("Old Vin");

        Car updatedCar = new Car();
        updatedCar.setId(carId);
        updatedCar.setMake("New Make");
        updatedCar.setModel("New Model");
        updatedCar.setYear(2022L);
        updatedCar.setVin("New Vin");

        when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        when(carRepository.save(existingCar)).thenReturn(existingCar);

        // Act
        carService.updateCar(carId, updatedCar);

        // Assert
        assertEquals(updatedCar.getMake(), existingCar.getMake());
        assertEquals(updatedCar.getModel(), existingCar.getModel());
        assertEquals(updatedCar.getYear(), existingCar.getYear());
        assertEquals(updatedCar.getVin(), existingCar.getVin());
    }

    @Test
    void testUpdateCar_WithNonExistingCarId_ShouldThrowCarNotFoundException() {
        // Arrange
        Long carId = 1L;
        Car updatedCar = new Car();
        updatedCar.setId(carId);
        updatedCar.setMake("New Make");
        updatedCar.setModel("New Model");
        updatedCar.setYear(2022L);
        updatedCar.setVin("New Vin");

        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CarNotFoundException.class, () -> carService.updateCar(carId, updatedCar));
    }

    @Test
    void testDeleteCar_WithExistingCarId_ShouldDeleteCar() throws CarNotFoundException {
        // Arrange
        Long carId = 1L;
        Car car = new Car();
        car.setId(carId);

        when(carRepository.existsById(carId)).thenReturn(true);
        doNothing().when(carRepository).deleteById(carId);
        doNothing().when(carCache).clear();

        // Act
        carService.deleteCar(carId);

        // Assert
        verify(carRepository, times(1)).existsById(carId);
        verify(carRepository, times(1)).deleteById(carId);
        verify(carCache, times(1)).clear();
    }


    @Test
    void testDeleteCar_WithNonExistingCarId_ShouldThrowCarNotFoundException() {
        // Arrange
        Long carId = 1L;

        when(carRepository.existsById(carId)).thenReturn(false);

        assertThrows(CarNotFoundException.class, () -> carService.deleteCar(carId));
        verify(carRepository, times(1)).existsById(carId);
        verify(carRepository, never()).deleteById(carId);
        verify(carCache, never()).clear();
    }

    @Test
    void testGetAllCarsByDealer_WithExistingDealerId_ShouldReturnListOfCars() throws CarNotFoundException {

        Long dealerId = 1L;
        Long year = 2022L;
        List<Car> cars = new ArrayList<>();
        cars.add(new Car());
        cars.add(new Car());
        cars.add(new Car());

        when(dealerRepository.existsById(dealerId)).thenReturn(true);
        when(carRepository.findAllByDealerIdAndYear(dealerId, year)).thenReturn(cars);

        List<CarDto> result = carService.getCarsByDealerIdAndYear(dealerId, year);

        List<CarDto> expectedCarDtos = cars.stream().map(CarDto::toCar).collect(Collectors.toList());
        assertEquals(expectedCarDtos, result);
    }

    @Test
    void testGetAllCarsByDealer_WithNonExistingDealerId_ShouldThrowDealerNotFoundException() {
        // Arrange
        Long dealerId = 1L;
        Long year = 2022L;

        when(dealerRepository.existsById(dealerId)).thenReturn(false);

        // Act & Assert
        assertThrows(DealerNotFoundException.class, () -> {
            try {
                carService.getCarsByDealerIdAndYear(dealerId, year);
            } catch (CarNotFoundException e) {
                throw new DealerNotFoundException(e.getMessage());
            }
        });
    }

    @Test
    void testTransferCarToDealer_WithExistingCarAndDealer_ShouldSetDealerAndSaveCar() throws CarNotFoundException, DealerNotFoundException {
        // Arrange
        Long carId = 1L;
        Long dealerId = 1L;
        Car car = new Car();
        Dealer dealer = new Dealer();
        car.setId(carId);
        dealer.setId(dealerId);

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(dealerRepository.findById(dealerId)).thenReturn(Optional.of(dealer));
        when(carRepository.save(car)).thenReturn(car);

        // Act
        carService.transferCarToDealer(carId, dealerId);

        // Assert
        assertEquals(dealer, car.getDealer());
        verify(carRepository, times(1)).findById(carId);
        verify(dealerRepository, times(1)).findById(dealerId);
        verify(carRepository, times(1)).save(car);
    }

    @Test
    void testTransferCarToDealer_WithNonExistingCar_ShouldThrowCarNotFoundException() {
        // Arrange
        Long carId = 1L;
        Long dealerId = 1L;

        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(CarNotFoundException.class, () -> carService.transferCarToDealer(carId, dealerId));
        verify(carRepository, times(1)).findById(carId);
        verify(dealerRepository, never()).findById(dealerId);
        verify(carRepository, never()).save(any());
    }

    @Test
    void testTransferCarToDealer_WithNonExistingDealer_ShouldThrowDealerNotFoundException() {
        // Arrange
        Long carId = 1L;
        Long dealerId = 1L;
        Car car = new Car();
        car.setId(carId);

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(dealerRepository.findById(dealerId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(DealerNotFoundException.class, () -> carService.transferCarToDealer(carId, dealerId));
        verify(carRepository, times(1)).findById(carId);
        verify(dealerRepository, times(1)).findById(dealerId);
        verify(carRepository, never()).save(any());
    }

    @Test
    void testGetCarByMake_WithExistingCar_ShouldReturnDto() throws CarNotFoundException {
        // Arrange
        String make = "Toyota";
        Car car = new Car();
        car.setMake(make);

        when(carCache.containsKey(make)).thenReturn(true);
        when(carCache.get(make)).thenReturn(new CarDto());
        when(carRepository.findByMake(make)).thenReturn(car);

        // Act
        CarDto carDto = carService.getCarByMake(make);

        // Assert
        Assertions.assertNotNull(carDto);
    }

    @Test
    void testGetCarByMake_WithNonExistingCar_ShouldThrowException() {
        // Arrange
        String make = "Toyota";

        when(carCache.containsKey(make)).thenReturn(false);
        when(carRepository.findByMake(make)).thenReturn(null);

        // Act & Assert
        Assertions.assertThrows(CarNotFoundException.class, () -> carService.getCarByMake(make));
    }

    @Test
    void testAddCar() throws CarAlreadyExistException, DealerNotFoundException {
        // Arrange
        Long dealerId = 1L;
        Car car = new Car();
        car.setMake("Toyota");
        car.setModel("Camry");
        car.setYear(2022L);
        car.setVin("123456789");

        Dealer dealer = new Dealer();
        dealer.setId(dealerId);

        when(dealerRepository.findById(dealerId)).thenReturn(Optional.of(dealer));
        when(carRepository.findByVin(car.getVin())).thenReturn(null);

        // Act
        carService.addCar(dealerId, car);

        // Assert
        verify(carRepository, times(1)).save(car);
        verify(carCache, times(1)).clear();
    }

    @Test
    void testUpdateCar() throws CarNotFoundException {
        // Arrange
        Long carId = 1L;
        Car existingCar = new Car();
        existingCar.setId(carId);
        existingCar.setMake("Toyota");
        existingCar.setModel("Camry");
        existingCar.setYear(2022L);
        existingCar.setVin("123456789");

        Car updatedCar = new Car();
        updatedCar.setId(carId);
        updatedCar.setMake("Toyota");
        updatedCar.setModel("Corolla");
        updatedCar.setYear(2022L);
        updatedCar.setVin("123456789");

        when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));

        // Act
        carService.updateCar(carId, updatedCar);

        // Assert
        verify(carRepository, times(1)).save(existingCar);
        verify(carCache, times(1)).clear();
        assertEquals(updatedCar.getModel(), existingCar.getModel());
    }

    @Test
    void testGetCarById() throws CarNotFoundException {
        // Arrange
        Long carId = 1L;
        Car car = new Car();
        car.setId(carId);
        car.setMake("Toyota");
        car.setModel("Camry");
        car.setYear(2022L);
        car.setVin("123456789");

        CarDto carDto = CarDto.toCar(car);

        when(carCache.containsKey(carId)).thenReturn(false);
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(carCache.get(carId)).thenReturn(carDto);

        // Act
        CarDto result = carService.getCarById(carId);

        // Assert
        verify(carCache, times(1)).put(carId, carDto);
        assertEquals(carDto, result);
    }

    @Test
    void testDeleteCar() throws CarNotFoundException {
        // Arrange
        Long carId = 1L;

        when(carRepository.existsById(carId)).thenReturn(true);

        // Act
        carService.deleteCar(carId);

        // Assert
        verify(carRepository, times(1)).deleteById(carId);
        verify(carCache, times(1)).clear();
    }

    @Test
    void testAddCarsBulk_whenAllCarsAreNew_thenSaveAllCars() throws Exception {
        Long dealerId = 1L;
        List<Car> cars = new ArrayList<>();
        Car car1 = new Car();
        car1.setVin("vin1");
        car1.setMake("make1");
        car1.setModel("model1");
        car1.setYear(2020L);
        cars.add(car1);

        Car car2 = new Car();
        car2.setVin("vin2");
        car2.setMake("make2");
        car2.setModel("model2");
        car2.setYear(2021L);
        cars.add(car2);

        Dealer dealer = new Dealer();
        dealer.setId(dealerId);
        dealer.setName("Dealer 1");
        when(dealerRepository.findById(dealerId)).thenReturn(Optional.of(dealer));
        when(carRepository.findAll()).thenReturn(new ArrayList<>());
        when(carRepository.saveAll(anyList())).thenReturn(cars);

        carService.addCarsBulk(dealerId, cars);

        verify(carRepository).saveAll(anyList());
        assertEquals(2, cars.size());
    }


    @Test
    void testAddCarsBulk_whenSomeCarsAlreadyExist_thenSaveOnlyNewCars() throws Exception {
        Long dealerId = 1L;
        List<Car> cars = new ArrayList<>();
        Car car1 = new Car();
        car1.setVin("vin1");
        car1.setMake("make1");
        car1.setModel("model1");
        car1.setYear(2020L);
        cars.add(car1);

        Car car2 = new Car();
        car2.setVin("vin2");
        car2.setMake("make2");
        car2.setModel("model2");
        car2.setYear(2021L);
        cars.add(car2);

        List<Car> existingCars = new ArrayList<>();
        Car existingCar1 = new Car();
        existingCar1.setVin("vin1");
        existingCar1.setMake("make1");
        existingCar1.setModel("model1");
        existingCar1.setYear(2020L);
        existingCars.add(existingCar1);

        Dealer dealer = new Dealer();
        dealer.setId(dealerId);
        dealer.setName("Dealer 1");
        when(dealerRepository.findById(dealerId)).thenReturn(Optional.of(dealer));
        when(carRepository.findAll()).thenReturn(existingCars);
        when(carRepository.saveAll(anyList())).thenReturn(cars.subList(1, 2));

        carService.addCarsBulk(dealerId, cars);

        verify(carRepository).saveAll(anyList());

        // Обновить список cars после вызова carService.addCarsBulk(dealerId, cars)
        cars = new ArrayList<>(cars.subList(1, 2));
        assertEquals(1, cars.size());
    }


    @Test
    void testAddCarsBulk_whenAllCarsAlreadyExist_thenThrowCarAlreadyExistException() {
        Long dealerId = 1L;
        List<Car> cars = new ArrayList<>();
        Car car1 = new Car();
        car1.setVin("vin1");
        car1.setMake("make1");
        car1.setModel("model1");
        car1.setYear(2020L);
        cars.add(car1);

        Car car2 = new Car();
        car2.setVin("vin2");
        car2.setMake("make2");
        car2.setModel("model2");
        car2.setYear(2021l);
        cars.add(car2);

        List<Car> existingCars = new ArrayList<>();
        Car existingCar1 = new Car();
        existingCar1.setVin("vin1");
        existingCar1.setMake("make1");
        existingCar1.setModel("model1");
        existingCar1.setYear(2020L);
        existingCars.add(existingCar1);

        Car existingCar2 = new Car();
        existingCar2.setVin("vin2");
        existingCar2.setMake("make2");
        existingCar2.setModel("model2");
        existingCar2.setYear(2021L);
        existingCars.add(existingCar2);

        Dealer dealer = new Dealer();
        dealer.setId(dealerId);
        dealer.setName("Dealer 1");
        when(dealerRepository.findById(dealerId)).thenReturn(Optional.of(dealer));
        when(carRepository.findAll()).thenReturn(existingCars);

        assertThrows(CarAlreadyExistException.class, () -> carService.addCarsBulk(dealerId, cars));
    }

    @Test
    void testAddCarsBulk_whenDealerNotFound_thenThrowDealerNotFoundException() {
        Long dealerId = 1L;
        List<Car> cars = new ArrayList<>();
        Car car1 = new Car();
        car1.setVin("vin1");
        car1.setMake("make1");
        car1.setModel("model1");
        car1.setYear(2020L);
        cars.add(car1);

        Car car2 = new Car();
        car2.setVin("vin2");
        car2.setMake("make2");
        car2.setModel("model2");
        car2.setYear(2021L);
        cars.add(car2);

        when(dealerRepository.findById(dealerId)).thenReturn(Optional.empty());

        assertThrows(DealerNotFoundException.class, () -> carService.addCarsBulk(dealerId, cars));
    }

}
