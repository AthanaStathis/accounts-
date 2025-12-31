package com.athanasios.accounts.service.impl;

import com.athanasios.accounts.constants.AccountConstants;
import com.athanasios.accounts.dto.AccountsDto;
import com.athanasios.accounts.dto.CustomerDto;
import com.athanasios.accounts.entity.Accounts;
import com.athanasios.accounts.entity.Customer;
import com.athanasios.accounts.exception.CustomerAlreadyExistsException;
import com.athanasios.accounts.mapper.CustommerMapper;
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
        Customer customer = CustommerMapper.mapToCustomer(customerDto, new Customer());
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

    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomer_id(customer.getCustomerId());
        long randomAccountNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setCreatedBy("Anonymous");
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setAccountNumber(randomAccountNumber);
        newAccount.setAccountType(AccountConstants.SAVINGS);
        newAccount.setBranchAddress(AccountConstants.ADDRESS);
        return newAccount;
    }

}
