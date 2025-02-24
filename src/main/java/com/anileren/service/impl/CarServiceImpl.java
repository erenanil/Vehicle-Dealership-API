package com.anileren.service.impl;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anileren.dto.DtoCar;
import com.anileren.dto.DtoCarIU;
import com.anileren.model.Car;
import com.anileren.repository.CarRepository;
import com.anileren.service.ICarService;

import jakarta.transaction.Transactional;

@Service
public class CarServiceImpl implements ICarService{

    @Autowired
    CarRepository carRepository;

    private Car createCar(DtoCarIU input){
        Car car = new Car();
        car.setCreateTime(new Date());

        BeanUtils.copyProperties(input, car);
        return car;
    }

    @Override
    @Transactional
    public DtoCar saveCar(DtoCarIU input) {
        DtoCar dtoCar = new DtoCar();

        Car savedCar = carRepository.save(createCar(input));

        BeanUtils.copyProperties(savedCar, dtoCar);
        return dtoCar;
    }
    
}
