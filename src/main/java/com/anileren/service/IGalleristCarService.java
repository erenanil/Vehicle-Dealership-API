package com.anileren.service;

import com.anileren.dto.DtoGalleristCar;
import com.anileren.dto.DtoGalleristCarIU;

public interface IGalleristCarService {
    public DtoGalleristCar saveGalleristCar(DtoGalleristCarIU input);
}
