package com.anileren.service;

import com.anileren.dto.DtoSaledCar;
import com.anileren.dto.DtoSaledCarIU;

public interface ISaledCarService {
    public DtoSaledCar buyCar(DtoSaledCarIU input);
    
}
