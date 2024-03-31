package com.example.cars.repository;

import com.example.cars.entity.CarEntity;
import org.springframework.data.repository.CrudRepository;

public interface CarRepo extends CrudRepository<CarEntity, Long> {
    CarEntity findByMake(String make);
}
