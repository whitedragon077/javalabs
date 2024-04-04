package com.example.cars.service;

import com.example.cars.dto.CarDto;
import com.example.cars.entity.Car;
import com.example.cars.exceptions.CarAlreadyExistException;
import com.example.cars.exceptions.CarNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.cars.repository.CarRepository;

@Service
public class CarService {

    private final CarRepository carRepository;

     @Autowired

    public CarService (CarRepository carRepository)
     {
         this.carRepository = carRepository;
     }

    public Car addCar(Car car) throws CarAlreadyExistException
    {
        if(carRepository.findByMake(car.getMake()) != null)
            throw new CarAlreadyExistException("Такой автомобиль уже существует");
        return carRepository.save(car);
    }

    public CarDto getCarById (Long id) throws CarNotFoundException
    {
        Car car = carRepository.findById(id).get();
        if (car == null)
        {
            throw new CarNotFoundException("Автомобиль не найден");
        }
        return CarDto.toCar(car);
    }

    public CarDto getCarByMake(String make) throws CarNotFoundException {
        Car car = carRepository.findByMake(make);
        if (car == null) {
            throw new CarNotFoundException("Автомобиль с маркой " + make + " не найден");
        }
        return CarDto.toCar(car);
    }
}
