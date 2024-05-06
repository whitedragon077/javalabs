package com.example.cars.service;

import static org.mockito.Mockito.when;

import com.example.cars.dto.DealerDto;
import com.example.cars.entity.Dealer;
import com.example.cars.exceptions.DealerAlreadyExistException;
import com.example.cars.exceptions.DealerNotFoundException;
import com.example.cars.repository.DealerRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class DealerServiceTest {

  @Mock
  private DealerRepository dealerRepository;

  private DealerService dealerService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    dealerService = new DealerService(dealerRepository);
  }

  @Test
  void testAddDealer_WithNonExistingDealer_ShouldAddDealer() throws DealerAlreadyExistException {

    Dealer dealer = new Dealer();
    dealer.setName("DealerName");

    when(dealerRepository.findByName(Mockito.anyString())).thenReturn(null);

    dealerService.addDealer(dealer);

    Mockito.verify(dealerRepository, Mockito.times(1)).save(dealer);
  }

  @Test
  void testAddDealer_WithExistingDealer_ShouldThrowException() {

    Dealer dealer = new Dealer();
    dealer.setName("DealerName");

    when(dealerRepository.findByName(Mockito.anyString())).thenReturn(dealer);

    Assertions.assertThrows(DealerAlreadyExistException.class,
            () -> dealerService.addDealer(dealer));
  }

  @Test
  void testUpdateDealer_WithExistingDealer_ShouldUpdateDealer() throws DealerNotFoundException {

    String dealerName = "DealerName";
    Dealer existingDealer = new Dealer();
    existingDealer.setName(dealerName);

    Dealer updatedDealer = new Dealer();
    updatedDealer.setName("UpdatedDealerName");

    when(dealerRepository.findByName(dealerName)).thenReturn(existingDealer);

    dealerService.updateDealer(dealerName, updatedDealer);

    Mockito.verify(dealerRepository, Mockito.times(1)).save(existingDealer);
  }

  @Test
  void testUpdateDealer_WithNonExistingDealer_ShouldThrowException() {

    String dealerName = "DealerName";
    Dealer updatedDealer = new Dealer();
    updatedDealer.setName("UpdatedDealerName");

    when(dealerRepository.findByName(dealerName)).thenReturn(null);

    Assertions.assertThrows(DealerNotFoundException.class,
            () -> dealerService.updateDealer(dealerName, updatedDealer));
  }

  @Test
  void testGetDealerById_WithNonExistingDealer_ShouldThrowException() {

    Long dealerId = 1L;

    when(dealerRepository.findById(dealerId)).thenReturn(Optional.empty());

    Assertions.assertThrows(DealerNotFoundException.class,
            () -> dealerService.getDealerById(dealerId));
  }

  @Test
  void testGetDealerByName_WithExistingDealer_ShouldReturnDto() throws DealerNotFoundException {

    String dealerName = "DealerName";
    Dealer dealer = new Dealer();
    dealer.setName(dealerName);

    when(dealerRepository.findByName(dealerName)).thenReturn(dealer);

    DealerDto dealerDto = dealerService.getDealerByName(dealerName);

    Assertions.assertEquals(dealer.getName(), dealerDto.getName());
  }

  @Test
  void testGetDealerByName_WithNonExistingDealer_ShouldThrowException() {

    String dealerName = "DealerName";

    when(dealerRepository.findByName(dealerName)).thenReturn(null);

    Assertions.assertThrows(DealerNotFoundException.class,
            () -> dealerService.getDealerByName(dealerName));
  }

  @Test
  void testDeleteDealer_WithExistingDealer_ShouldDeleteDealer() throws DealerNotFoundException {

    Long dealerId = 1L;

    when(dealerRepository.existsById(dealerId)).thenReturn(true);

    dealerService.deleteDealer(dealerId);

    Mockito.verify(dealerRepository, Mockito.times(1)).deleteById(dealerId);
  }

  @Test
  void testDeleteDealer_WithNonExistingDealer_ShouldThrowException() {

    Long dealerId = 1L;

    when(dealerRepository.existsById(dealerId)).thenReturn(false);

    Assertions.assertThrows(DealerNotFoundException.class,
            () -> dealerService.deleteDealer(dealerId));
  }
}