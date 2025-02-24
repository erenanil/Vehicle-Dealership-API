package com.anileren.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anileren.model.Address;

public interface AddressRepository extends JpaRepository<Address,Long>{
    
}
