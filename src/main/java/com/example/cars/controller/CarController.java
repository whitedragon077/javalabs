package com.example.cars.controller;

import com.example.cars.entity.CarEntity;
import com.example.cars.exceptions.CarAlreadyExist;
import com.example.cars.exceptions.CarNotFoundException;
import com.example.cars.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @PostMapping
    public ResponseEntity AddOneCar (@RequestBody CarEntity car)
    {
        try{
            carService.addCar(car);
            return ResponseEntity.ok("saved");
        }
        catch (CarAlreadyExist e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка");
        }
    }

    @GetMapping
    public ResponseEntity getCar(@RequestParam Long id)
    {
        try{
            return ResponseEntity.ok(carService.getCarById(id));
        }
        catch (CarNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка");
        }
    }

    @GetMapping("/make")
    public ResponseEntity getCarByMake(@RequestParam String make) {
        try {
            return ResponseEntity.ok(carService.getCarByMake(make));
        } catch (CarNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка");
        }
    }
}
