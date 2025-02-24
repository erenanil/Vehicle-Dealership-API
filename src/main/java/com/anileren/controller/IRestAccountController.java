package com.anileren.controller;

import com.anileren.dto.DtoAccount;
import com.anileren.dto.DtoAccountIU;

public interface IRestAccountController {
    RootEntity<DtoAccount> saveAccount(DtoAccountIU input);
}
