package com.anileren.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anileren.controller.IRestAddressController;
import com.anileren.controller.RestBaseController;
import com.anileren.controller.RootEntity;
import com.anileren.dto.DtoAddress;
import com.anileren.dto.DtoAddressIU;
import com.anileren.service.IAddressService;

import jakarta.validation.Valid;

@RequestMapping("/rest/api/address")
@RestController
public class RestAddressControllerImpl extends RestBaseController implements IRestAddressController {

    @Autowired
    IAddressService addressService;

    @PostMapping("/save")
    @Override
    public RootEntity<DtoAddress> saveAddress(@Valid @RequestBody DtoAddressIU input) {
        
        return ok(addressService.saveAddress(input));

    }

    @GetMapping("/get/{id}")
    @Override
    public RootEntity<DtoAddress> getAddressById(@PathVariable Long id) {
        return ok(addressService.getAddressById(id));
    }
    
}
