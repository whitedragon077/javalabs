package com.example.cars.service;

import com.example.cars.dto.CarDto;
import com.example.cars.entity.Car;
import com.example.cars.entity.Dealer;
import com.example.cars.exceptions.CarAlreadyExistException;
import com.example.cars.exceptions.CarNotFoundException;
import com.example.cars.exceptions.DealerNotFoundException;
import com.example.cars.repository.DealerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.cars.repository.CarRepository;
import java.util.Optional;
@Service
public class CarService {

    private final CarRepository carRepository;
    private final DealerRepository dealerRepository;
    private static final String CAR_NOT_FOUND_STRING = "Автомобиль не найден";
    private static final String CAR_ALREADY_EXIST = "Такой автомобиль уже существует";

     @Autowired

    public CarService (CarRepository carRepository, DealerRepository dealerRepository)
     {
         this.carRepository = carRepository;
         this.dealerRepository = dealerRepository;
     }

    public void addCar(Long id, Car car) throws CarAlreadyExistException, DealerNotFoundException {
        Dealer dealer = dealerRepository.findById(id).orElse(null);
        if (dealer != null) {
            car.setDealer(dealer);
            if (carRepository.findByMake(car.getMake()) != null) {
                throw new CarAlreadyExistException(CAR_ALREADY_EXIST);
            }
            carRepository.save(car);
        } else {
            throw new DealerNotFoundException("Не удалось добавить машину. Дилер не найден");
        }
    }

    public void updateCar (String make, Car updatedCar) throws CarNotFoundException {
      Car carEntity = carRepository.findByMake(make);
      if (carEntity != null) {
          carEntity.setMake(updatedCar.getMake());
          carEntity.setModel(updatedCar.getModel());
          carEntity.setYear(updatedCar.getYear());
          carEntity.setVin(updatedCar.getVin());
          carRepository.save(carEntity);
      }
      else {
          throw new CarNotFoundException(CAR_NOT_FOUND_STRING);
      }
    }
    public CarDto getCarById (Long id) throws CarNotFoundException
    {
        Optional<Car> optionalCar = carRepository.findById(id);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
            return CarDto.toCar(car);
        } else {
            throw new CarNotFoundException(CAR_NOT_FOUND_STRING);
        }
    }

    public CarDto getCarByMake(String make) throws CarNotFoundException {
        Car car = carRepository.findByMake(make);
        if (car == null) {
            throw new CarNotFoundException(CAR_NOT_FOUND_STRING);
        }
        return CarDto.toCar(car);
    }

    public void deleteCar (Long id) throws CarNotFoundException {
        if(!carRepository.existsById(id))
        {
            throw new CarNotFoundException(CAR_NOT_FOUND_STRING);
        }
        carRepository.deleteById(id);
    }
}
