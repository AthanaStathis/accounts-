package com.athanasios.accounts.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class AccountsDto {

    @NotEmpty(message = "AccountNumber cannot be empty or null.")
    @Pattern(regexp="(^$|[0-9]{10})",message = "AccountNumber must be 10-digit number")
    private Long accountNumber;

    @NotEmpty(message = "Account type cannot be empty or null.")
    private String accountType;

    @NotEmpty(message = "Branch address cannot be empty or null.")
    private String branchAddress;

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }
}
