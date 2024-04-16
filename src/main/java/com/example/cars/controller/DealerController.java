package com.example.cars.controller;

import com.example.cars.dto.DealerDto;
import com.example.cars.entity.Dealer;
import com.example.cars.exceptions.DealerAlreadyExistException;
import com.example.cars.exceptions.DealerNotFoundException;
import com.example.cars.service.DealerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dealers")
public class DealerController {
    private static final String ERROR_MESSAGE = "Ошибка";
    private final DealerService dealerService;

    @Autowired

    public DealerController (DealerService dealerService)
    {
        this.dealerService = dealerService;
    }

    @PostMapping
    public ResponseEntity<String> addDealer(@RequestBody Dealer dealer)
    {
        try{
            dealerService.addDealer(dealer);
            return ResponseEntity.ok("saved");
        }
        catch (DealerAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @GetMapping
    public ResponseEntity <Object> getDealer(@RequestParam Long id)
    {
        try{
            DealerDto dealerDto = dealerService.getDealerById(id);
            return ResponseEntity.ok(dealerDto);
        }
        catch (DealerNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @GetMapping("/name")
    public ResponseEntity<Object> getDealerByName(@RequestParam String name) {
        try {
            DealerDto dealerDto = dealerService.getDealerByName(name);
            return ResponseEntity.ok(dealerDto);
        } catch (DealerNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @PutMapping("/name")

    public ResponseEntity<String> updateDealer(@RequestParam String name, @RequestBody Dealer updatedDealer)
    {
        try {
            dealerService.updateDealer(name, updatedDealer);
            return ResponseEntity.ok("updated");
        }
        catch(DealerNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @DeleteMapping

    public ResponseEntity<String> deleteDealer(@RequestParam Long id){
        try{
            dealerService.deleteDealer(id);
            return ResponseEntity.ok("deleted");
        } catch (DealerNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }

    }

}
