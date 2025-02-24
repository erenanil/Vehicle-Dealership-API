package com.anileren.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anileren.controller.IRestGalleristController;
import com.anileren.controller.RestBaseController;
import com.anileren.controller.RootEntity;
import com.anileren.dto.DtoGallerist;
import com.anileren.dto.DtoGalleristIU;
import com.anileren.service.IGalleristService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rest/api/gallerist")
public class RestGalleristController extends RestBaseController implements IRestGalleristController{

    @Autowired
    IGalleristService galleristService;

    @PostMapping("/save")
    @Override
    public RootEntity<DtoGallerist> saveGallerist(@Valid @RequestBody DtoGalleristIU input) {
        return ok(galleristService.saveGallerist(input));
    }
    
}
