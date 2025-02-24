package com.anileren.service;

import com.anileren.dto.DtoAccount;
import com.anileren.dto.DtoAccountIU;

public interface IAccountService {
    public DtoAccount saveAccount(DtoAccountIU input);
    public DtoAccount updateAccount(Long id, DtoAccountIU input);
    public void deleteAccount(Long id);
    public DtoAccount getAccountById(Long id);
}
