package com.example.cars.service;

import com.example.cars.component.Cache;
import com.example.cars.dto.CarDto;
import com.example.cars.entity.Car;
import com.example.cars.entity.Dealer;
import com.example.cars.exceptions.CarAdditionException;
import com.example.cars.exceptions.CarAlreadyExistException;
import com.example.cars.exceptions.CarNotFoundException;
import com.example.cars.exceptions.DealerNotFoundException;
import com.example.cars.repository.CarRepository;
import com.example.cars.repository.DealerRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing Car entities.
 * Provides methods for handling CRUD operations on Car entities.
 */
@Service
public class CarService {

  private final Cache<Object, CarDto> carCache;
  private final CarRepository carRepository;

  private static final Logger log = LoggerFactory.getLogger(CarService.class);
  private final DealerRepository dealerRepository;
  private static final String CAR_NOT_FOUND_STRING = "Автомобиль не найден";
  private static final String CAR_ALREADY_EXIST = "Такой автомобиль уже существует";

  /**
     * Service class for managing Car entities.
     */
  @Autowired

  public CarService(CarRepository carRepository, DealerRepository dealerRepository,
                     Cache<Object, CarDto> carCache) {
    this.carRepository = carRepository;
    this.dealerRepository = dealerRepository;
    this.carCache = carCache;
  }

  /**
     * Adds a new car.
     *
     * @param id   the ID of the car to which the dealer belongs
     * @throws DealerNotFoundException     if the dealer with the specified ID is not found
     * @throws CarAlreadyExistException    if a car with the same name already exists.
     */
  public void addCar(Long id, Car car) throws CarAlreadyExistException, DealerNotFoundException {
    Dealer dealer = dealerRepository.findById(id).orElse(null);
    if (dealer != null) {
      car.setDealer(dealer);
      if (carRepository.findByVin(car.getVin()) != null) {
        throw new CarAlreadyExistException(CAR_ALREADY_EXIST);
      }
      carRepository.save(car);
      carCache.clear();

    } else {
      throw new DealerNotFoundException("Не удалось добавить машину. Дилер не найден");
    }
  }

  /**

   * Adds a list of cars to the database, ignoring those that already exist.
   *
   * @param cars List of cars to add
   * @throws CarAlreadyExistException If some cars already exist in the database
   */

  public void addCarsBulk(Long dealerId, List<Car> cars) throws CarAlreadyExistException, DealerNotFoundException, CarAdditionException {
    Set<String> existingCarMakes = new HashSet<>();
    for (Car car : carRepository.findAll()) {
      existingCarMakes.add(car.getVin());
    }

    List<Car> newCars = new ArrayList<>(cars.stream()
            .filter(car -> !existingCarMakes.contains(car.getVin()))
            .collect(Collectors.toMap(Car::getVin, car -> car,
                    (car1, car2) -> car1))
            .values());

    Dealer dealer = dealerRepository.findById(dealerId).orElseThrow(() -> new DealerNotFoundException("Дилер не найден"));

    for (Car car : newCars) {
      car.setDealer(dealer);
    }

    List<Car> savedCars = new ArrayList<>();
    carRepository.saveAll(newCars).forEach(savedCars::add);
    int countSavedCars = savedCars.size();

    if (countSavedCars == 0) {
      throw new CarAlreadyExistException("Все автомобили из списка уже есть в базе.");
    } else if (newCars.size() == countSavedCars) {
      log.info("Успешно добавлено {} новых автомобилей.", countSavedCars);
    } else {
      log.error("Ошибка при добавлении новых автомобилей."
              + " Добавлено {} из {} автомобилей.", countSavedCars, newCars.size());
      throw new CarAdditionException("Ошибка при добавлении новых автомобилей.");
    }
  }

  /**
     * Updates an existing car.
     *
     * @param id   the ID of the car to update
     * @param updatedCar the updated city data
     * @throws CarNotFoundException if the city with the specified ID is not found
     */
  public void updateCar(Long id, Car updatedCar) throws CarNotFoundException {
    Car carEntity = carRepository.findById(id).orElse(null);
    if (carEntity != null) {
      carEntity.setMake(updatedCar.getMake());
      carEntity.setModel(updatedCar.getModel());
      carEntity.setYear(updatedCar.getYear());
      carEntity.setVin(updatedCar.getVin());
      carRepository.save(carEntity);

      carCache.clear();
    } else {
      throw new CarNotFoundException(CAR_NOT_FOUND_STRING);
    }
  }

  /**
     * Retrieves a car by its ID.
     *
     * @param id the ID of the car to retrieve
     * @return the DTO representation of the car
     * @throws CarNotFoundException if the car with the specified ID is not found
     */
  public CarDto getCarById(Long id) throws CarNotFoundException {
    if (carCache.containsKey(id)) {
      return carCache.get(id);
    } else {
      Car car = carRepository.findById(id).orElse(null);
      if (car == null) {
        throw new CarNotFoundException(CAR_NOT_FOUND_STRING);
      }
      CarDto carDto = CarDto.toCar(car);
      carCache.put(id, carDto);
      return carDto;
    }
  }

  /**
     * Retrieves a car by its make.
     *
     * @param make the make of the car to retrieve
     * @return the DTO representation of the car
     * @throws CarNotFoundException if the car with the specified ID is not found
     */
  public CarDto getCarByMake(String make) throws CarNotFoundException {
    if (carCache.containsKey(make)) {
      return carCache.get(make);
    } else {
      Car car = carRepository.findByMake(make);
      if (car == null) {
        throw new CarNotFoundException(CAR_NOT_FOUND_STRING);
      }
      CarDto carDto = CarDto.toCar(car);
      carCache.put(make, carDto);
      return carDto;
    }
  }

  /**
     * Deletes a car by its ID.
     *
     * @param id the ID of the car to delete
     * @throws CarNotFoundException if the city with the specified ID is not found
     */
  public void deleteCar(Long id) throws CarNotFoundException {
    if (!carRepository.existsById(id)) {
      throw new CarNotFoundException(CAR_NOT_FOUND_STRING);
    }
    carRepository.deleteById(id);
    carCache.clear();
  }

  /**
     * Updates an existing car.
     *
     * @param carId   the ID of the car to update
     * @param dealerId the updated dealer data
     * @throws CarNotFoundException if the city with the specified ID is not found
     * @throws DealerNotFoundException if the dealer with the specified ID is not found
     */
  public void transferCarToDealer(Long carId, Long dealerId)
            throws CarNotFoundException, DealerNotFoundException {
    Car car = carRepository.findById(carId).orElse(null);
    if (car == null) {
      throw new CarNotFoundException(CAR_NOT_FOUND_STRING);
    }
    Dealer dealer = dealerRepository.findById(dealerId).orElse(null);
    if (dealer == null) {
      throw new DealerNotFoundException("Дилер не найден");
    }
    car.setDealer(dealer);
    carRepository.save(car);
  }

  /**
     * Retrieves a car by its dealerId and year.
     *
     * @param dealerId the dealer ID of the car to retrieve
     * @param year the year of the car to retrieve
     * @return the DTO representation of the car
     * @throws CarNotFoundException if the car with the specified ID is not found
     */
  public List<CarDto> getCarsByDealerIdAndYear(Long dealerId, Long year)
          throws CarNotFoundException {
    String cacheKey = "dealer_" + dealerId + "_year_" + year;
    if (carCache.containsKey(cacheKey)) {
      return carCache.getList(cacheKey);
    } else {
      List<Car> cars = carRepository.findAllByDealerIdAndYear(dealerId, year);
      if (cars.isEmpty()) {
        throw new CarNotFoundException(CAR_NOT_FOUND_STRING);
      }
      List<CarDto> carDtos = cars.stream().map(CarDto::toCar).toList();
      carCache.putList(cacheKey, carDtos);
      return carDtos;
    }
  }


}


