package com.example.cars.repository;

import com.example.cars.entity.Car;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for managing Car entities.
 * Extends CrudRepository for basic CRUD operations on Car entities.
 */
public interface CarRepository extends CrudRepository<Car, Long> {
  Car findByMake(String make);

  Car findByVin(String vin);

  @Query(value = "SELECT c FROM Car c WHERE c.dealer.id = :dealerId AND c.year = :year")
  List<Car> findAllByDealerIdAndYear(Long dealerId, Long year);
}
 