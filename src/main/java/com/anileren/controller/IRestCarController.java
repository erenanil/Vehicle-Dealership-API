package com.anileren.controller;

import com.anileren.dto.DtoCar;
import com.anileren.dto.DtoCarIU;

public interface IRestCarController {
    public RootEntity<DtoCar> saveCar(DtoCarIU input);
}
