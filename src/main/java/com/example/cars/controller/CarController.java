package com.example.cars.controller;

import com.example.cars.dto.CarDto;
import com.example.cars.entity.Car;
import com.example.cars.exceptions.CarAlreadyExistException;
import com.example.cars.exceptions.CarNotFoundException;
import com.example.cars.exceptions.DealerNotFoundException;
import com.example.cars.exceptions.ExceptionHandler;
import com.example.cars.service.CarService;
import java.util.List;

import com.example.cars.service.RequestCounterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Controller class for managing Car-related API endpoints.
 */
@RestController
@RequestMapping("/cars")
public class CarController {

  private final CarService carService;

  private final RequestCounterService requestCounterService;
  private static final Logger log = LoggerFactory.getLogger(CarController.class);
  final ExceptionHandler exceptionHandler;

  @Autowired
  public CarController(CarService carService, ExceptionHandler exceptionHandler, RequestCounterService requestCounterService) {
    this.carService = carService;
    this.exceptionHandler = exceptionHandler;
    this.requestCounterService = requestCounterService;
  }

  /**
     * Endpoint to add a new car.
     *
     * @param dealerId the ID of the country to which the city belongs
     * @param car      the city object to be added
     * @return ResponseEntity with success message if the car
     *     is added successfully, or error message
     *     if any exception occurs
     */
  @PostMapping
  public ResponseEntity<Object> addCar(@RequestParam Long dealerId, @RequestBody Car car) {
    log.info("post endpoint. Adding new car for dealer with ID: {}", dealerId);
    requestCounterService.increment();
    try {
      carService.addCar(dealerId, car);
      log.info("Car was added successfully");
      return ResponseEntity.ok("Car saved");
    } catch (DealerNotFoundException | CarAlreadyExistException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  /**
   * Handles a POST request for adding cars in bulk.
   *
   * @param cars List of cars to be added
   * @return ResponseEntity with information about the operation status
   */
  @PostMapping("/bulk")
  public ResponseEntity<Object> addCarsBulk(@RequestParam Long dealerId, @RequestBody List<Car> cars) {
    log.info("post endpoint was called");
    requestCounterService.increment();
    try {
      carService.addCarsBulk(dealerId, cars);
      log.info("Cars was successfully saved");
      return ResponseEntity.ok("Автомобили были успешно сохранены!");
    } catch (CarAlreadyExistException e) {
      log.info("One or many cars already exist");
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  /**
     * Endpoint to retrieve a car by its ID.
     *
     * @param id the ID of the car to retrieve
     * @return ResponseEntity with the retrieved city if found, or error message if the car is not
     *     found or any exception occurs
     */
  @GetMapping
  public ResponseEntity<Object> getCar(@RequestParam Long id) {
    log.info("get endpoint. Getting car");
    requestCounterService.increment();
    try {
      CarDto carDto = carService.getCarById(id);
      return ResponseEntity.ok(carDto);
    } catch (CarNotFoundException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  /**
   * Endpoint for retrieving car information by make.
   *
   * @param make The make of the car.
   * @return ResponseEntity containing car information or an error message.
   */

  @GetMapping("/make")
  public ResponseEntity<Object> getCarByMake(@RequestParam String make) {
    log.info("get endpoint. Getting car by make");
    requestCounterService.increment();
    try {
      CarDto carDto = carService.getCarByMake(make);
      return ResponseEntity.ok(carDto);
    } catch (CarNotFoundException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  /**
     * Updates information about a car based on its ID.
     *
     * @param id   The ID of the car to update.
     * @param updatedCar The updated car information.
     * @return ResponseEntity with a success message or an error response.
     */
  @PutMapping("/id")
  public ResponseEntity<String> updateCar(@RequestParam Long id, @RequestBody Car updatedCar) {
    log.info("put endpoint. Updating car with ID: {}", id);
    requestCounterService.increment();
    try {
      carService.updateCar(id, updatedCar);
      log.info("Car updated successfully");
      return ResponseEntity.ok("Car updated");
    } catch (CarNotFoundException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  /**
     * Endpoint for updating user information by ID.
     *
     * @param carId   The ID of the car to be updated.
     * @param dealerId The dealerID of the car.
     * @return ResponseEntity indicating the success or failure of the operation.
     */
  @PutMapping("/transfer")
  public ResponseEntity<String> transferCarToDealer(@RequestParam Long carId,
                                                    @RequestParam Long dealerId) {
    log.info("put endpoint. Transferring car with ID {} to dealer with ID {}", carId, dealerId);
    requestCounterService.increment();
    try {
      carService.transferCarToDealer(carId, dealerId);
      log.info("Car transferred successfully");
      return ResponseEntity.ok("Car transferred to another dealer");
    } catch (CarNotFoundException | DealerNotFoundException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  /**
     * Endpoint to delete an existing car by its ID.
     *
     * @param id the ID of the car to delete
     * @return ResponseEntity with success message if the car is deleted successfully, or error
     *     message if the car is not found or any exception occurs
     */
  @DeleteMapping
  public ResponseEntity<String> deleteCar(@RequestParam Long id) {
    log.info("delete endpoint. Deleting car with ID: {}", id);
    requestCounterService.increment();
    try {
      carService.deleteCar(id);
      log.info("Car deleted successfully");
      return ResponseEntity.ok("Car deleted");
    } catch (CarNotFoundException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  /**
     * Endpoint for retrieving car information by dealerID and year.
     *
     * @param dealerId The ID of the car.
     * @param year The year of the car.
     * @return ResponseEntity containing car information or an error message.
     */
  @GetMapping("/byYear")
    public ResponseEntity<Object> getAllCarsByDealerIdAndYear(@RequestParam Long dealerId,
                                                              @RequestParam Long year) {
    log.info("get endpoint. Getting all cars for dealer with ID: {} and year: {}",
                dealerId, year);
    requestCounterService.increment();
    try {
      List<CarDto> cars = carService.getCarsByDealerIdAndYear(dealerId, year);
      return ResponseEntity.ok(cars);
    } catch (CarNotFoundException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  @GetMapping("/counter")
  public ResponseEntity<Integer> getCounter()
  {
    int count = requestCounterService.getCount();
    return ResponseEntity.ok(count);
  }
}
