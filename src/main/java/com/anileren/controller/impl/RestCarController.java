package com.anileren.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anileren.controller.IRestCarController;
import com.anileren.controller.RestBaseController;
import com.anileren.controller.RootEntity;
import com.anileren.dto.DtoCar;
import com.anileren.dto.DtoCarIU;
import com.anileren.service.ICarService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rest/api/car")
public class RestCarController extends RestBaseController implements IRestCarController {

    @Autowired
    ICarService carService;


    @PostMapping("/save")
    @Override
    public RootEntity<DtoCar> saveCar(@Valid @RequestBody DtoCarIU input) {
        return ok(carService.saveCar(input));
    }
    
}
