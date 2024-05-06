package com.example.cars.service;

import com.example.cars.dto.DealerDto;
import com.example.cars.entity.Dealer;
import com.example.cars.exceptions.DealerAlreadyExistException;
import com.example.cars.exceptions.DealerNotFoundException;
import com.example.cars.repository.DealerRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for managing dealers.
 */
@Service
public class DealerService {
  private final DealerRepository dealerRepository;
  private static final String DEALER_NOT_FOUND_STRING = "Дилер не найден";
  private static final String DEALER_ALREADY_EXIST = "Такой дилер уже существует";

  @Autowired

  public DealerService(DealerRepository dealerRepository) {
    this.dealerRepository = dealerRepository;
  }

  /**
     * Adds a new dealer.
     *
     * @param dealer the dealer to add
     * @throws DealerAlreadyExistException    if a dealer with the same name already exists
     */
  public void addDealer(Dealer dealer) throws DealerAlreadyExistException {
    if (dealerRepository.findByName(dealer.getName()) != null) {
      throw new DealerAlreadyExistException(DEALER_ALREADY_EXIST);
    }
    dealerRepository.save(dealer);
  }

  /**
     * Updates an existing dealer.
     *
     * @param name   the name of the dealer to update
     * @param updatedDealer the updated city data
     * @throws DealerNotFoundException if the city with the specified ID is not found
     */
  public void updateDealer(String name, Dealer updatedDealer) throws DealerNotFoundException {
    Dealer dealerEntity = dealerRepository.findByName(name);
    if (dealerEntity != null) {
      dealerEntity.setName(updatedDealer.getName());
      dealerEntity.setAddress(updatedDealer.getAddress());
      dealerEntity.setCarList(updatedDealer.getCarList());
      dealerRepository.save(dealerEntity);
    } else {
      throw new DealerNotFoundException(DEALER_NOT_FOUND_STRING);
    }
  }

  /**
     * Retrieves a dealer by its ID.
     *
     * @param id the ID of the dealer to retrieve
     * @return the DTO representation of the dealer
     * @throws DealerNotFoundException if the dealer with the specified ID is not found
     */
  public DealerDto getDealerById(Long id) throws DealerNotFoundException {
    Optional<Dealer> optionalDealer = dealerRepository.findById(id);
    if (optionalDealer.isPresent()) {
      Dealer dealer = optionalDealer.get();

      return DealerDto.toDealer(dealer);
    } else {
      throw new DealerNotFoundException(DEALER_NOT_FOUND_STRING);
    }
  }

  /**
     * Retrieves a dealer by its name.
     *
     * @param name the name of the dealer to retrieve
     * @return the DTO representation of the dealer
     * @throws DealerNotFoundException if the dealer with the specified ID is not found
     */
  public DealerDto getDealerByName(String name) throws DealerNotFoundException {
    Dealer dealer = dealerRepository.findByName(name);
    if (dealer == null) {
      throw new DealerNotFoundException(DEALER_NOT_FOUND_STRING);
    }
    return DealerDto.toDealer(dealer);
  }

  /**
     * Deletes a dealer by its ID.
     *
     * @param id the ID of the dealer to delete
     * @throws DealerNotFoundException if the dealer with the specified ID is not found
     */
  public void deleteDealer(Long id) throws DealerNotFoundException {
    if (!dealerRepository.existsById(id)) {
      throw new DealerNotFoundException(DEALER_NOT_FOUND_STRING);
    }
    dealerRepository.deleteById(id);
  }
}
