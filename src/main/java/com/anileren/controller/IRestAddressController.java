package com.anileren.controller;

import com.anileren.dto.DtoAddress;
import com.anileren.dto.DtoAddressIU;

public interface IRestAddressController {
    
    RootEntity<DtoAddress> saveAddress(DtoAddressIU input);

    RootEntity<DtoAddress> getAddressById(Long id);
}
