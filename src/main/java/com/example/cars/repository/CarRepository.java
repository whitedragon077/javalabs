package com.example.cars.repository;

import com.example.cars.entity.Car;
import org.springframework.data.repository.CrudRepository;

public interface CarRepository extends CrudRepository<Car, Long> {
    Car findByMake(String make);
}
