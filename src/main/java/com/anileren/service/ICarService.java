package com.anileren.service;

import com.anileren.dto.DtoCar;
import com.anileren.dto.DtoCarIU;

public interface ICarService {
    public DtoCar saveCar(DtoCarIU input);
    
}
