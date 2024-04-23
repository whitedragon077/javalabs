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

import java.util.List;


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

    @PutMapping("/transfer")
    public ResponseEntity<String> transferCarToDealer(@RequestParam Long carId, @RequestParam Long dealerId) {
        try {
            carService.transferCarToDealer(carId, dealerId);
            return ResponseEntity.ok("Машина передана другому дилеру");
        } catch (CarNotFoundException | DealerNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при передаче машины");
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

    @GetMapping("/byYear")

    public ResponseEntity<Object> getAllCarsByDealerIdAndYear (@RequestParam Long dealerId, @RequestParam Long year)
    {
        try {
            List<Car> cars = carService.findAllByDealerIdAndYear(dealerId, year);
            return ResponseEntity.ok(cars);
        }   catch (Exception e) {
            return ResponseEntity.badRequest().body(ERROR_MESSAGE);
        }
    }
}
