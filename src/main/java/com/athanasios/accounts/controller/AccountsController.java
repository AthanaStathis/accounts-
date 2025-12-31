package com.athanasios.accounts.controller;


import com.athanasios.accounts.constants.AccountConstants;
import com.athanasios.accounts.dto.AccountsDto;
import com.athanasios.accounts.dto.CustomerDto;
import com.athanasios.accounts.dto.ResponseDto;
import com.athanasios.accounts.service.AccountsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/accounts/",
                produces = {MediaType.APPLICATION_JSON_VALUE})
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
    public ResponseEntity<ResponseDto> createAccount(@RequestBody CustomerDto customerDto) {
        accountsService.createAccount(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDto(AccountConstants.STATUS_201, AccountConstants.MESSAGE_201));
    }

    @GetMapping()
    public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam String mobileNumber) {
        CustomerDto customerDto = accountsService.fetchAccount(mobileNumber);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(customerDto);
    }
}
