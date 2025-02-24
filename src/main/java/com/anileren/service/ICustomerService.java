package com.anileren.service;

import com.anileren.dto.DtoCustomer;
import com.anileren.dto.DtoCustomerIU;

public interface ICustomerService {
    public DtoCustomer saveCustomer(DtoCustomerIU input);
    public DtoCustomer updateCustomer(Long id, DtoCustomerIU input);
    public DtoCustomer getCustomerById(Long id);
    public void deleteCustomer(Long id);
}
