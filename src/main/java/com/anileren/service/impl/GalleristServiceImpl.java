package com.anileren.service.impl;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anileren.dto.DtoAddress;
import com.anileren.dto.DtoGallerist;
import com.anileren.dto.DtoGalleristIU;
import com.anileren.exception.BaseException;
import com.anileren.exception.ErrorMessage;
import com.anileren.exception.MessageType;
import com.anileren.model.Address;
import com.anileren.model.Gallerist;
import com.anileren.repository.AddressRepository;
import com.anileren.repository.GalleristRepository;
import com.anileren.service.IGalleristService;

import jakarta.transaction.Transactional;

@Service
public class GalleristServiceImpl implements IGalleristService{

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    GalleristRepository galleristRepository;


    private Gallerist createGallerist (DtoGalleristIU input){
        Address address = addressRepository.findById(input.getAddresId())
        .orElseThrow(()-> new BaseException(new ErrorMessage(MessageType.ADDRESS_NOT_FOUND, input.getAddresId().toString())));

        Gallerist gallerist = new Gallerist();
        gallerist.setCreateTime(new Date());
        gallerist.setFirstName(input.getFirstName());
        gallerist.setLastName(input.getLastName());

        gallerist.setAddress(address);
        
        return gallerist;
    }

    @Override
    @Transactional
    public DtoGallerist saveGallerist(DtoGalleristIU input) {
        DtoGallerist dtoGallerist = new DtoGallerist();
        DtoAddress dtoAddress = new DtoAddress();

        Gallerist gallerist = galleristRepository.save(createGallerist(input));
        
        BeanUtils.copyProperties(gallerist, dtoGallerist);
        BeanUtils.copyProperties(gallerist.getAddress(), dtoAddress);
        dtoGallerist.setAddress(dtoAddress);

        return dtoGallerist;

    }
    
}
