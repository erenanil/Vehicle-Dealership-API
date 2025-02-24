package com.anileren.controller;

import com.anileren.dto.DtoSaledCar;
import com.anileren.dto.DtoSaledCarIU;

public interface IRestSaledCarController {
    public RootEntity<DtoSaledCar> buyCar(DtoSaledCarIU input);
}
