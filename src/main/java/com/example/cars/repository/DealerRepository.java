package com.example.cars.repository;

import com.example.cars.entity.Dealer;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for managing Dealer entities.
 * Extends CrudRepository for basic CRUD operations on Dealer entities.
 */
public interface DealerRepository extends CrudRepository<Dealer, Long> {
  Dealer findByName(String name);
}
