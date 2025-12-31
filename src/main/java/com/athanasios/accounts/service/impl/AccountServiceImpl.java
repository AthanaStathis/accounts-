package com.athanasios.accounts.service.impl;

import com.athanasios.accounts.constants.AccountConstants;
import com.athanasios.accounts.dto.AccountsDto;
import com.athanasios.accounts.dto.CustomerDto;
import com.athanasios.accounts.entity.Accounts;
import com.athanasios.accounts.entity.Customer;
import com.athanasios.accounts.exception.CustomerAlreadyExistsException;
import com.athanasios.accounts.exception.ResourceNotFoundException;
import com.athanasios.accounts.mapper.AccountsMapper;
import com.athanasios.accounts.mapper.CustomerMapper;
import com.athanasios.accounts.repository.AccountsRepository;
import com.athanasios.accounts.repository.CustomerRepository;
import com.athanasios.accounts.service.AccountsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountsService {
    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    public AccountServiceImpl(AccountsRepository accountsRepository, CustomerRepository customerRepository) {
        this.accountsRepository = accountsRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customer.getMobileNumber());
        if (optionalCustomer.isPresent()){
            throw new CustomerAlreadyExistsException("Customer already exists with given mobile number "
                                                        + customer.getMobileNumber());
        }

        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anonymous");
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                                                .orElseThrow(
                                                        () -> new ResourceNotFoundException(
                                                                "Customer",
                                                                "mobileNumber",
                                                                mobileNumber));

        Accounts accounts = (Accounts) accountsRepository.findByCustomerId(customer.getCustomerId())
                                                .orElseThrow(
                                                        () -> new ResourceNotFoundException(
                                                                "Account",
                                                                "customerId",
                                                                customer.getCustomerId().toString()));

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isAccountUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if (accountsDto != null) {
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber())
                    .orElseThrow(
                            () -> new ResourceNotFoundException(
                                    "Account",
                                    "accountNumber",
                                    accountsDto.getAccountNumber().toString()));

            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(
                            () -> new ResourceNotFoundException(
                                    "Customer",
                                    "customerId",
                                    customerId.toString()));

            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);
            isAccountUpdated = true;
        }
        return isAccountUpdated;
    }

    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccountNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setCreatedBy("Anonymous");
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setAccountNumber(randomAccountNumber);
        newAccount.setAccountType(AccountConstants.SAVINGS);
        newAccount.setBranchAddress(AccountConstants.ADDRESS);
        return newAccount;
    }

}
