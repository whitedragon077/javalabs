package com.example.cars.repository;

import com.example.cars.entity.Car;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CarRepository extends CrudRepository<Car, Long> {
    Car findByMake(String make);
    @Query(value = "SELECT c FROM Car c WHERE c.dealer.id = :dealerId AND c.year = :year")
    List<Car> findAllByDealerIdAndYear(Long dealerId, Long year);
}
 