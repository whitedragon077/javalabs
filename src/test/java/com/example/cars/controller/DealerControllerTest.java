package com.example.cars.controller;

import com.example.cars.dto.DealerDto;
import com.example.cars.entity.Dealer;
import com.example.cars.exceptions.DealerAlreadyExistException;
import com.example.cars.exceptions.DealerNotFoundException;
import com.example.cars.service.DealerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class DealerControllerTest {

    @Mock
    private DealerService dealerService;

    @InjectMocks
    private DealerController dealerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addDealer_ValidDealer_ReturnsSuccessResponse() throws DealerAlreadyExistException {
        Dealer dealer = new Dealer();
        doNothing().when(dealerService).addDealer(any(Dealer.class));

        ResponseEntity<?> response = dealerController.addDealer(dealer);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Dealer saved", response.getBody());
    }

    @Test
    void addDealer_DealerAlreadyExists_ThrowsBadRequestException() throws DealerAlreadyExistException {
        Dealer dealer = new Dealer();
        doThrow(new DealerAlreadyExistException("Dealer already exists")).when(dealerService).addDealer(any(Dealer.class));

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> dealerController.addDealer(dealer));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void getDealer_ValidId_ReturnsDealer() throws DealerNotFoundException {
        DealerDto dealerDto = new DealerDto();
        when(dealerService.getDealerById(anyLong())).thenReturn(dealerDto);

        ResponseEntity<?> response = dealerController.getDealer(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dealerDto, response.getBody());
    }

    @Test
    void getDealer_DealerNotFound_ThrowsBadRequestException() throws DealerNotFoundException {
        when(dealerService.getDealerById(anyLong())).thenThrow(new DealerNotFoundException("Dealer not found"));

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> dealerController.getDealer(1L));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void getDealerByName_ValidName_ReturnsDealer() throws DealerNotFoundException {
        DealerDto dealerDto = new DealerDto();
        when(dealerService.getDealerByName(anyString())).thenReturn(dealerDto);

        ResponseEntity<?> response = dealerController.getDealerByName("John's Dealership");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dealerDto, response.getBody());
    }

    @Test
    void getDealerByName_DealerNotFound_ThrowsBadRequestException() throws DealerNotFoundException {
        when(dealerService.getDealerByName(anyString())).thenThrow(new DealerNotFoundException("Dealer not found"));

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> dealerController.getDealerByName("John's Dealership"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void updateDealer_ValidNameAndUpdatedDealer_ReturnsSuccessResponse() throws DealerNotFoundException {
        Dealer updatedDealer = new Dealer();
        doNothing().when(dealerService).updateDealer(anyString(), any(Dealer.class));

        ResponseEntity<String> response = dealerController.updateDealer("John's Dealership", updatedDealer);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Dealer updated", response.getBody());
    }

    @Test
    void updateDealer_DealerNotFound_ThrowsBadRequestException() throws DealerNotFoundException {
        Dealer updatedDealer = new Dealer();
        doThrow(new DealerNotFoundException("Dealer not found")).when(dealerService).updateDealer(anyString(), any(Dealer.class));

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> dealerController.updateDealer("John's Dealership", updatedDealer));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void deleteDealer_ValidId_ReturnsSuccessResponse() throws DealerNotFoundException {
        doNothing().when(dealerService).deleteDealer(anyLong());

        ResponseEntity<String> response = dealerController.deleteDealer(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Dealer deleted", response.getBody());
    }

    @Test
    void deleteDealer_DealerNotFound_ThrowsBadRequestException() throws DealerNotFoundException {
        doThrow(new DealerNotFoundException("Dealer not found")).when(dealerService).deleteDealer(anyLong());

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> dealerController.deleteDealer(1L));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }
}