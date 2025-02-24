package com.anileren.controller;

import com.anileren.dto.DtoCustomer;
import com.anileren.dto.DtoCustomerIU;

public interface IRestCustomerController {
    public RootEntity<DtoCustomer> saveCustomer(DtoCustomerIU input);
    public RootEntity<DtoCustomer> updateCustomer(Long id, DtoCustomerIU input);
    public RootEntity<DtoCustomer> getCustomerById(Long id);
    public void deleteCustomer(Long id);
}
