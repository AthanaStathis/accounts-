package com.athanasios.accounts.controller;


import com.athanasios.accounts.constants.AccountConstants;
import com.athanasios.accounts.dto.CustomerDto;
import com.athanasios.accounts.dto.ResponseDto;
import com.athanasios.accounts.service.AccountsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/accounts/",
                produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class AccountsController {

    private AccountsService accountsService;

    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @GetMapping("say-hello")
    String sayHello() {
        return "Hello World";
    }

    @PostMapping()
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
        accountsService.createAccount(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDto(AccountConstants.STATUS_201, AccountConstants.MESSAGE_201));
    }

    @GetMapping()
    public ResponseEntity<CustomerDto> fetchAccountDetails(@Valid @RequestParam String mobileNumber) {
        CustomerDto customerDto = accountsService.fetchAccount(mobileNumber);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                            .body(customerDto);
    }

    @PutMapping()
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto) {
        boolean isAccountUpdated = accountsService.updateAccount(customerDto);
        if(isAccountUpdated) {
            return ResponseEntity.status(HttpStatus.OK)
                                .body(new ResponseDto(AccountConstants.STATUS_200,
                                                        AccountConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                                .body(new ResponseDto(AccountConstants.STATUS_417,
                                                       AccountConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("delete")
    public ResponseEntity<ResponseDto> deleteAccount(@Valid @RequestParam("mobileNumber") String mobileNumber) {
        boolean isAccountDeleted = accountsService.deleteAccount(mobileNumber);
        if (isAccountDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountConstants.STATUS_200, AccountConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountConstants.MESSAGE_417_DELETE,AccountConstants.MESSAGE_417_DELETE));
        }

    }
}
