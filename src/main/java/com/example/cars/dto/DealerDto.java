package com.example.cars.dto;

import com.example.cars.entity.Dealer;

import java.util.ArrayList;
import java.util.List;

public class DealerDto {

    private String name;
    private String address;
    private List<CarDto> carsList = new ArrayList<>();

    public static DealerDto toDealer(Dealer entity) {
            DealerDto dealerModel = new DealerDto();
            dealerModel.setName(entity.getName());
            dealerModel.setAddress(entity.getAddress());
            if(dealerModel.getCarsList() != null)
            dealerModel.setCarsList(entity.getCarList().stream().map(CarDto::toCar).toList());
            return dealerModel;
    }

    public DealerDto() {
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