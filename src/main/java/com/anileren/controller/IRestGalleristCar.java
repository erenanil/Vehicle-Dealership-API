package com.anileren.controller;

import com.anileren.dto.DtoGalleristCar;
import com.anileren.dto.DtoGalleristCarIU;

public interface IRestGalleristCar {
    public RootEntity<DtoGalleristCar> saveGalleristCar(DtoGalleristCarIU input);
}
