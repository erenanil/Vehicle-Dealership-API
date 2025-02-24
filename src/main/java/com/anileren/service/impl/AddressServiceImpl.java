package com.anileren.service.impl;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anileren.dto.DtoAddress;
import com.anileren.dto.DtoAddressIU;
import com.anileren.exception.BaseException;
import com.anileren.exception.ErrorMessage;
import com.anileren.exception.MessageType;
import com.anileren.model.Address;
import com.anileren.repository.AddressRepository;
import com.anileren.service.IAddressService;

import jakarta.transaction.Transactional;

@Service
public class AddressServiceImpl implements IAddressService {
    
    @Autowired
    AddressRepository addressRepository;

    private Address createAddress(DtoAddressIU input){
        
        Address address = new Address();
        address.setCreateTime(new Date());

        BeanUtils.copyProperties(input, address);
        
        return address;
    }

    @Override
    @Transactional
    public DtoAddress saveAddress(DtoAddressIU input) {
        DtoAddress dtoAddress = new DtoAddress();
        Address savedAddress = addressRepository.save(createAddress(input));

        BeanUtils.copyProperties(savedAddress, dtoAddress);

        return dtoAddress;

    }

    @Override
    public DtoAddress getAddressById(Long id) {
        DtoAddress dtoAddress = new DtoAddress();

        Address dbAddress = addressRepository.findById(id)
        .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.ADDRESS_NOT_FOUND, ".")));
        
        

        BeanUtils.copyProperties(dbAddress, dtoAddress);

        return dtoAddress;

    }

    @Override
    @Transactional
    public void deleteAddress(Long id) {
        addressRepository.findById(id)
        .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.ADDRESS_NOT_FOUND, ".")));

        addressRepository.deleteById(id);

    }

    @Override
    @Transactional
    public DtoAddress updateAddress(Long id, DtoAddressIU input) {
        DtoAddress dtoAddress = new DtoAddress();

        Address dbAddress = addressRepository.findById(id)
        .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.ADDRESS_NOT_FOUND, ".")));
        
        BeanUtils.copyProperties(input, dbAddress);
        addressRepository.save(dbAddress);

        BeanUtils.copyProperties(dbAddress, dtoAddress);

        return dtoAddress;
    
    }

    

    





}
