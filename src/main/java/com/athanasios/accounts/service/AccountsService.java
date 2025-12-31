package com.athanasios.accounts.service;


import com.athanasios.accounts.dto.AccountsDto;
import com.athanasios.accounts.dto.CustomerDto;

public interface AccountsService {

    void createAccount(CustomerDto customerDto);
}