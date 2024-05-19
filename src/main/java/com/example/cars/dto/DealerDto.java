package com.example.cars.dto;

import com.example.cars.entity.Dealer;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing information about a dealer.
 * Used to transfer simplified dealer data between layers of the application.
 */
public class DealerDto {

  private Long id;
  private String name;
  private String address;
  private List<CarDto> carsList = new ArrayList<>();

  /**
     * Converts a Dealer entity to a DealerDto model.
     *
     * @param entity The Dealer entity to convert.
     * @return DealerDto model with simplified dealer information.
     */
  public static DealerDto toDealer(Dealer entity) {
    DealerDto dealerModel = new DealerDto();
    dealerModel.setName(entity.getName());
    dealerModel.setAddress(entity.getAddress());
    if (entity.getCarList() != null) {
      dealerModel.setCarsList(entity.getCarList().stream().map(CarDto::toCar).toList());
    }
    return dealerModel;
  }

  public DealerDto() {
        //Default constructor that doesn't require any specific actions when creating an object.
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getAddress() {
    return address;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public List<CarDto> getCarsList() {
    return carsList;
  }

  public void setCarsList(List<CarDto> carsList) {
    this.carsList = carsList;
  }
}