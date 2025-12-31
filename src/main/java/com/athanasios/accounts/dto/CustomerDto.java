package com.athanasios.accounts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class CustomerDto {

    @NotEmpty(message = "Name cannot be empty or null.")
    private String name;

    @Email(message = "Email should have a xxxx.xxxx@xxx.xxx format." )
    @NotEmpty(message = "Email cannot be empty or null")
    private String email;

    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be a 10-digit number.")
    private String mobileNumber;
    private AccountsDto accountsDto;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public AccountsDto getAccountsDto() {
        return accountsDto;
    }

    public void setAccountsDto(AccountsDto accountsDto) {
        this.accountsDto = accountsDto;
    }
}
