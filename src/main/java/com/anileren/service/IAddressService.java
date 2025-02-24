package com.anileren.service;

import com.anileren.dto.DtoAddress;
import com.anileren.dto.DtoAddressIU;

public interface IAddressService {
    DtoAddress saveAddress(DtoAddressIU input);

    DtoAddress getAddressById(Long id);

    void deleteAddress(Long id);

    DtoAddress updateAddress(Long id , DtoAddressIU input);
    
}
