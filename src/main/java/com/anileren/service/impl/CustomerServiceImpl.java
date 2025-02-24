package com.anileren.service.impl;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anileren.dto.DtoAccount;
import com.anileren.dto.DtoAddress;
import com.anileren.dto.DtoCustomer;
import com.anileren.dto.DtoCustomerIU;
import com.anileren.exception.BaseException;
import com.anileren.exception.ErrorMessage;
import com.anileren.exception.MessageType;
import com.anileren.model.Account;
import com.anileren.model.Address;
import com.anileren.model.Customer;
import com.anileren.repository.AccountRepository;
import com.anileren.repository.AddressRepository;
import com.anileren.repository.CustomerRepository;
import com.anileren.service.ICustomerService;

import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImpl implements ICustomerService{

    @Autowired
    CustomerRepository customerRepository;
    
    @Autowired
    AddressRepository addressRepository;
   
    @Autowired
    AccountRepository accountRepository;
   
   
    private Customer createCustomer(DtoCustomerIU input){

        Address address = addressRepository.findById(input.getAddressId())
        .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.ADDRESS_NOT_FOUND, input.getAddressId().toString())));
       

        Account account = accountRepository.findById(input.getAccountId())
        .orElseThrow(()-> new BaseException(new ErrorMessage(MessageType.ACCOUNT_NOT_FOUND, input.getAccountId().toString())));
        

        Customer customer = new Customer();
        customer.setCreateTime(new Date());

        BeanUtils.copyProperties(input, customer);

        customer.setAccount(account);
        customer.setAddress(address);

        return customer;
    }
    
    
    @Override
    @Transactional
    public DtoCustomer saveCustomer(DtoCustomerIU input) {
        DtoCustomer dtoCustomer = new DtoCustomer();
        DtoAccount dtoAccount = new DtoAccount();
        DtoAddress dtoAddress = new DtoAddress();

        Customer savedCustomer = customerRepository.save(createCustomer(input));
    
        BeanUtils.copyProperties(savedCustomer, dtoCustomer);
        BeanUtils.copyProperties(savedCustomer.getAddress(), dtoAddress);
        BeanUtils.copyProperties(savedCustomer.getAccount(), dtoAccount);

        dtoCustomer.setAccount(dtoAccount);
        dtoCustomer.setAddress(dtoAddress);
        
        return dtoCustomer;
    }


    @Override
    @Transactional
    public DtoCustomer updateCustomer(Long id, DtoCustomerIU input) {
        Customer customer = customerRepository.findById(id)
        .orElseThrow( () -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, id.toString()))); 

        
        customer.setBirthOfDate(input.getBirthOfDate());
        customer.setFirstName(input.getFirstName());
        customer.setLastName(input.getLastName());
        customer.setTckn(input.getTckn());
        
        Address address = addressRepository.findById(input.getAddressId())
        .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.ADDRESS_NOT_FOUND, input.getAddressId().toString())));
        customer.setAddress(address);

        Account account = accountRepository.findById(input.getAccountId())
        .orElseThrow(() ->  new BaseException(new ErrorMessage(MessageType.ACCOUNT_NOT_FOUND, input.getAccountId().toString())));
        customer.setAccount(account);

        customerRepository.save(customer);

        DtoCustomer dtoCustomer = new DtoCustomer();
        dtoCustomer.setFirstName(customer.getFirstName());
        dtoCustomer.setLastName(customer.getLastName());
        dtoCustomer.setBirthOfDate(customer.getBirthOfDate());
        dtoCustomer.setTckn(customer.getTckn());

        DtoAddress dtoAddress = new DtoAddress();
        dtoAddress.setCity(address.getCity());
        dtoAddress.setDistrict(address.getDistrict());
        dtoAddress.setNeighborhood(address.getNeighborhood());
        dtoAddress.setStreet(address.getStreet());

        DtoAccount dtoAccount = new DtoAccount();
        dtoAccount.setAccountNo(account.getAccountNo());
        dtoAccount.setAmount(account.getAmount());
        dtoAccount.setCurrencyType(account.getCurrencyType());
        dtoAccount.setIban(account.getIban());

        dtoCustomer.setAccount(dtoAccount);
        dtoCustomer.setAddress(dtoAddress);

        
        return dtoCustomer;
    }


    @Override
    public DtoCustomer getCustomerById(Long id) {

        DtoCustomer dtoCustomer = new DtoCustomer();
        DtoAddress dtoAddress = new DtoAddress();
        DtoAccount dtoAccount = new DtoAccount();
        
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, id.toString())));
        
        BeanUtils.copyProperties(customer, dtoCustomer);
        
        
        if (customer.getAccount() != null) {
            BeanUtils.copyProperties(customer.getAccount(), dtoAccount);
            dtoCustomer.setAccount(dtoAccount);
        }

        if (customer.getAddress() != null) {
            BeanUtils.copyProperties(customer.getAddress(), dtoAddress);
            dtoCustomer.setAddress(dtoAddress);
        }
        
        return dtoCustomer;
    }


    @Transactional
    //Silme işleminin atomik olmasını sağlar. Yani, işlem sırasında bir hata olursa, veritabanındaki tüm değişiklikler geri alınır (rollback).
    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(()-> new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST, id.toString())));

        customerRepository.delete(customer);
    }
    
}
