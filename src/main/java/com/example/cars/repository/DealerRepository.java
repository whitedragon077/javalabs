package com.example.cars.repository;

import com.example.cars.entity.Dealer;
import org.springframework.data.repository.CrudRepository;

public interface DealerRepository extends CrudRepository<Dealer, Long> {
    Dealer findByName(String name);
}
