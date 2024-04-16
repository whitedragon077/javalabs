package com.example.cars.controller;

import com.example.cars.dto.CarDto;
import com.example.cars.entity.Car;
import com.example.cars.exceptions.CarAlreadyExistException;
import com.example.cars.exceptions.CarNotFoundException;
import com.example.cars.exceptions.DealerNotFoundException;
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
    public  ResponseEntity<Object> addCar(@RequestParam Long dealerId, @RequestBody Car car)
    {
        try{
            carService.addCar(dealerId, car);
            return ResponseEntity.ok("saved");
        }
        catch (DealerNotFoundException | CarAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @GetMapping
    public ResponseEntity <Object> getCar(@RequestParam Long id)
    {
        try{
            CarDto carDto = carService.getCarById(id);
            return ResponseEntity.ok(carDto);
        }
        catch (CarNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @GetMapping("/make")
    public ResponseEntity<Object> getCarByMake(@RequestParam String make) {
        try {
            CarDto carDto = carService.getCarByMake(make);
            return ResponseEntity.ok(carDto);
        } catch (CarNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @PutMapping("/make")

    public ResponseEntity<String> updateCar(@RequestParam String make, @RequestBody Car updatedCar)
    {
        try {
            carService.updateCar(make, updatedCar);
            return ResponseEntity.ok("updated");
        }
        catch(CarNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }

    @DeleteMapping

    public ResponseEntity<String> deleteCar(@RequestParam Long id){
        try{
            carService.deleteCar(id);
            return ResponseEntity.ok("deleted");
        } catch (CarNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }

    }
}
