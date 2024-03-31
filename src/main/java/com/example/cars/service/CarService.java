package com.example.cars.service;

import com.example.cars.dto.CarDTO;
import com.example.cars.entity.CarEntity;
import com.example.cars.exceptions.CarAlreadyExist;
import com.example.cars.exceptions.CarNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.cars.repository.CarRepo;

@Service
public class CarService {

    @Autowired
    private CarRepo carRepo;

    public CarEntity addCar(CarEntity car) throws CarAlreadyExist
    {
        if(carRepo.findByMake(car.getMake()) != null)
            throw new CarAlreadyExist("Такой автомобиль уже существует");
        return carRepo.save(car);
    }

    public CarDTO getCarById (Long id) throws CarNotFoundException
    {
        CarEntity car = carRepo.findById(id).get();
        if (car == null)
        {
            throw new CarNotFoundException("Автомобиль не найден");
        }
        return CarDTO.toCar(car);
    }

    public CarDTO getCarByMake(String make) throws CarNotFoundException {
        CarEntity car = carRepo.findByMake(make);
        if (car == null) {
            throw new CarNotFoundException("Автомобиль с маркой " + make + " не найден");
        }
        return CarDTO.toCar(car);
    }
}
