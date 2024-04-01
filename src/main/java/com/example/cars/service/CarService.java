package com.example.cars.service;

import com.example.cars.dto.CarDTO;
import com.example.cars.entity.Car;
import com.example.cars.exceptions.CarAlreadyExistException;
import com.example.cars.exceptions.CarNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.cars.repository.CarRepository;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public Car addCar(Car car) throws CarAlreadyExistException
    {
        if(carRepository.findByMake(car.getMake()) != null)
            throw new CarAlreadyExistException("Такой автомобиль уже существует");
        return carRepository.save(car);
    }

    public CarDTO getCarById (Long id) throws CarNotFoundException
    {
        Car car = carRepository.findById(id).get();
        if (car == null)
        {
            throw new CarNotFoundException("Автомобиль не найден");
        }
        return CarDTO.toCar(car);
    }

    public CarDTO getCarByMake(String make) throws CarNotFoundException {
        Car car = carRepository.findByMake(make);
        if (car == null) {
            throw new CarNotFoundException("Автомобиль с маркой " + make + " не найден");
        }
        return CarDTO.toCar(car);
    }
}
