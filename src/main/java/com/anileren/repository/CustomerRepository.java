package com.anileren.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anileren.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long>{
}
