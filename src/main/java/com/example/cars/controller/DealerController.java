package com.example.cars.controller;

import com.example.cars.dto.DealerDto;
import com.example.cars.entity.Dealer;
import com.example.cars.exceptions.DealerAlreadyExistException;
import com.example.cars.exceptions.DealerNotFoundException;
import com.example.cars.exceptions.ExceptionHandler;
import com.example.cars.service.DealerService;
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
* Controller class for managing Dealer-related API endpoints.
*/
@RestController
@RequestMapping("/dealers")
public class DealerController {
  private final DealerService dealerService;
  private static final Logger log = LoggerFactory.getLogger(DealerController.class);

  final ExceptionHandler exceptionHandler;

  @Autowired
    public DealerController(DealerService dealerService, ExceptionHandler exceptionHandler) {
    this.dealerService = dealerService;
    this.exceptionHandler = exceptionHandler;
  }

  /**
  * REST endpoint to create a new dealer.
  *
  * @param dealer The Dealer object containing the information to be saved.
  * @return ResponseEntity with a success message if the dealer was saved successfully.
  */
  @PostMapping
    public ResponseEntity<Object> addDealer(@RequestBody Dealer dealer) {
    log.info("post endpoint. Adding new dealer: {}", dealer);
    try {
      dealerService.addDealer(dealer);
      log.info("Dealer was added successfully");
      return ResponseEntity.ok("Dealer saved");
    } catch (DealerAlreadyExistException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  /**
  * Endpoint to retrieve a dealer by its ID.
  *
  * @param id the ID of the dealer to retrieve
  * @return ResponseEntity with the retrieved dealer if found, or error message if the dealer is not
  *     found or any exception occurs
  */
  @GetMapping
    public ResponseEntity<Object> getDealer(@RequestParam Long id) {
    log.info("get endpoint. Getting dealer with ID: {}", id);
    try {
      DealerDto dealerDto = dealerService.getDealerById(id);
      return ResponseEntity.ok(dealerDto);
    } catch (DealerNotFoundException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  /**
  * Retrieves information about a dealer based on its name.
  *
  * @param name The name of the dealer to retrieve.
  * @return ResponseEntity with the retrieved dealer information or an error response.
  */
  @GetMapping("/name")
    public ResponseEntity<Object> getDealerByName(@RequestParam String name) {
    log.info("get endpoint. Getting dealer by name");
    try {
      DealerDto dealerDto = dealerService.getDealerByName(name);
      return ResponseEntity.ok(dealerDto);
    } catch (DealerNotFoundException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  /**
  * Updates information about a dealer based on its name.
  *
  * @param name   The name of the dealer to update.
  * @param updatedDealer The updated dealer information.
  * @return ResponseEntity with a success message or an error response.
  */
  @PutMapping("/name")
    public ResponseEntity<String> updateDealer(@RequestParam String name,
                                               @RequestBody Dealer updatedDealer) {
    log.info("put endpoint. Updating dealer with name");
    try {
      dealerService.updateDealer(name, updatedDealer);
      log.info("Dealer updated successfully");
      return ResponseEntity.ok("Dealer updated");
    } catch (DealerNotFoundException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  /**
  * Deletes a city based on its ID.
  *
  * @param id The ID of the city to delete.
  * @return ResponseEntity with a success message or an error response.
  */
  @DeleteMapping
   public ResponseEntity<String> deleteDealer(@RequestParam Long id) {
    log.info("delete endpoint. Deleting dealer with ID: {}", id);
    try {
      dealerService.deleteDealer(id);
      log.info("Dealer deleted successfully");
      return ResponseEntity.ok("Dealer deleted");
    } catch (DealerNotFoundException e) {
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getMessage());
    } catch (Exception e) {
      throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }
}