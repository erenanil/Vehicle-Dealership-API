package com.anileren.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anileren.controller.IRestGalleristCar;
import com.anileren.controller.RestBaseController;
import com.anileren.controller.RootEntity;
import com.anileren.dto.DtoGalleristCar;
import com.anileren.dto.DtoGalleristCarIU;
import com.anileren.service.IGalleristCarService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rest/api/gallerist-car")
public class RestGalleristCarController extends RestBaseController implements IRestGalleristCar{

    @Autowired
    IGalleristCarService galleristCarService;


    @PostMapping("/save")
    @Override
    public RootEntity<DtoGalleristCar> saveGalleristCar(@Valid @RequestBody DtoGalleristCarIU input) {
        return ok(galleristCarService.saveGalleristCar(input));
    }
    
}
