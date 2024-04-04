package com.example.cars.controller;

import com.example.cars.entity.Car;
import com.example.cars.exceptions.CarAlreadyExistException;
import com.example.cars.exceptions.CarNotFoundException;
import com.example.cars.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cars")
public class CarController {

    private static final String ERROR_MESSAGE = "Ошибка";
    private final CarService carService;

    @Autowired

    public CarController (CarService carService)
    {
        this.carService = carService;
    }

    @PostMapping
    public  ResponseEntity<?> addCar(@RequestBody Car car)
    {
        try{
            carService.addCar(car);
            return ResponseEntity.ok("saved");
        }
        catch (CarAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @GetMapping
    public ResponseEntity<?> getCar(@RequestParam Long id)
    {
        try{
            return ResponseEntity.ok(carService.getCarById(id));
        }
        catch (CarNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @GetMapping("/make")
    public ResponseEntity<?> getCarByMake(@RequestParam String make) {
        try {
            return ResponseEntity.ok(carService.getCarByMake(make));
        } catch (CarNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }
}
