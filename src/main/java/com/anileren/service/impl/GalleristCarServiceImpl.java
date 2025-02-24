package com.anileren.service.impl;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anileren.dto.DtoAddress;
import com.anileren.dto.DtoCar;
import com.anileren.dto.DtoGallerist;
import com.anileren.dto.DtoGalleristCar;
import com.anileren.dto.DtoGalleristCarIU;
import com.anileren.exception.BaseException;
import com.anileren.exception.ErrorMessage;
import com.anileren.exception.MessageType;
import com.anileren.model.Car;
import com.anileren.model.Gallerist;
import com.anileren.model.GalleristCar;
import com.anileren.repository.AddressRepository;
import com.anileren.repository.CarRepository;
import com.anileren.repository.GalleristCarRepository;
import com.anileren.repository.GalleristRepository;
import com.anileren.service.IGalleristCarService;

@Service
public class GalleristCarServiceImpl implements IGalleristCarService{

    @Autowired
    GalleristCarRepository galleristCarRepository;

    @Autowired
    GalleristRepository galleristRepository;

    @Autowired
    CarRepository carRepository;

    @Autowired
    AddressRepository addressRepository;
    
    
    private GalleristCar createGalleristCar(DtoGalleristCarIU input){

        Gallerist gallerist = galleristRepository.findById(input.getGalleristId())
        .orElseThrow(()-> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, input.getGalleristId().toString())));


        Car car = carRepository.findById(input.getCarId()).orElseThrow(()-> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST , input.getCarId().toString())));

        GalleristCar galleristCar = new GalleristCar();
        galleristCar.setCreateTime(new Date());

        galleristCar.setGallerist(gallerist);
        galleristCar.setCar(car);

        return galleristCar;
    }


    @Override
    public DtoGalleristCar saveGalleristCar(DtoGalleristCarIU input) {
        DtoGalleristCar dtoGalleristCar = new DtoGalleristCar();
        DtoGallerist dtoGallerist = new DtoGallerist();
        DtoCar dtoCar = new DtoCar();
        DtoAddress dtoAddress = new DtoAddress();
        GalleristCar savedGalleristCar = galleristCarRepository.save(createGalleristCar(input));

        BeanUtils.copyProperties(savedGalleristCar, dtoGalleristCar);
        BeanUtils.copyProperties(savedGalleristCar.getGallerist(), dtoGallerist);
        BeanUtils.copyProperties(savedGalleristCar.getGallerist().getAddress(), dtoAddress);
        BeanUtils.copyProperties(savedGalleristCar.getCar(), dtoCar);

        dtoGallerist.setAddress(dtoAddress);
        dtoGalleristCar.setCar(dtoCar);
        dtoGalleristCar.setGallerist(dtoGallerist);
        
        return dtoGalleristCar;
    }
    
}
