package com.anileren.service.impl;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anileren.dto.DtoAccount;
import com.anileren.dto.DtoAccountIU;
import com.anileren.exception.BaseException;
import com.anileren.exception.ErrorMessage;
import com.anileren.exception.MessageType;
import com.anileren.model.Account;
import com.anileren.repository.AccountRepository;
import com.anileren.service.IAccountService;

import jakarta.transaction.Transactional;

@Service

public class AccountServiceImpl implements IAccountService {

    @Autowired
    AccountRepository accountRepository;

    private Account createAccount(DtoAccountIU input){
        Account account = new Account();
        account.setCreateTime(new Date());
        
        BeanUtils.copyProperties(input, account);

        return account;
    }
    
    @Override
    @Transactional
    public DtoAccount saveAccount(DtoAccountIU input) {
        DtoAccount dtoAccount = new DtoAccount();
        Account savedAccount = accountRepository.save(createAccount(input));

        BeanUtils.copyProperties(savedAccount, dtoAccount);

         return dtoAccount;
    }

    @Override
    @Transactional
    public DtoAccount updateAccount(Long id, DtoAccountIU input) {

        DtoAccount dtoAccount = new DtoAccount();
        Account account = accountRepository.findById(id)
        .orElseThrow(()->new BaseException(new ErrorMessage(MessageType.ACCOUNT_NOT_FOUND, ".")));

        BeanUtils.copyProperties(input, account);

        Account savedAccount = accountRepository.save(account);

        BeanUtils.copyProperties(savedAccount, dtoAccount);

        return dtoAccount;
    }

    @Override
    @Transactional
    public void deleteAccount(Long id) {

        Account account = accountRepository.findById(id)
        .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.ACCOUNT_NOT_FOUND, ".")));

        accountRepository.delete(account);
    }

    @Override
    public DtoAccount getAccountById(Long id) {
        DtoAccount dtoAccount = new DtoAccount();

        Account account = accountRepository.findById(id)
        .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.ACCOUNT_NOT_FOUND, ".")));

        BeanUtils.copyProperties(account, dtoAccount);

        return dtoAccount;
    }
    
}
