package com.example.accessingdatamysql.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.accessingdatamysql.repository.AccountRepository;
import org.springframework.stereotype.Service;
import com.example.accessingdatamysql.model.Account;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
    	if(account.getBalance()>100 && account.getCustId().length()>0) {
            return accountRepository.save(account);
    	}else {
    		throw new RuntimeException("Make sure balance is minimum 100 & Customer ID is valid");
    	}
    }

    public Optional<Account> getAccount(String id) {
        return accountRepository.findById(id);
    }
    
    public List<Account> getAllAccounts() {
    	return accountRepository.findAll();
    }

    public Account deposit(String id, double amount) {
        Account account = getAccount(id).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance() + amount);
        return accountRepository.save(account);
    }

    public Account withdraw(String id, double amount) {
        Account account = getAccount(id).orElseThrow(() -> new RuntimeException("Account not found"));
        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds");
        }
        account.setBalance(account.getBalance() - amount);
        return accountRepository.save(account);
    }
    
    
}