package com.example.accessingdatamysql.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.accessingdatamysql.model.Account;
import com.example.accessingdatamysql.model.Customer;
import com.example.accessingdatamysql.service.AccountService;
import com.example.accessingdatamysql.service.CustomerService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	@Autowired
    private AccountService accountService;

	@Autowired
	private CustomerService customerService;
	
	 
	private Account getAccountDetail(String accId) {
		return accountService.getAccount(accId).orElseThrow(() -> new RuntimeException("Account not found")); 
	}
	private boolean checkLoggedIn(String custId) {
		Customer c =  customerService.getCustomer(custId).orElseThrow(()->new RuntimeException("Customer Not found"));
		return c.getIsLoggedIn();
	}
    
    @PostMapping("/create")
    public Account createAccount(@RequestBody Map<String, Object>  account) {
    	if(checkLoggedIn((String)account.get("customerId"))) {
    		Account a =  new Account();
	    	a.setCustId((String)account.get("customerId"));
	    	a.setAccountId((String)account.get("accountId"));
	    	a.setAccountHolderName((String)account.get("accountHolderName"));
	    	a.setBalance((double)account.get("balance"));
    		return accountService.createAccount(a);
    	}else {
    		throw new RuntimeException("Customer Not logged in"); 
    	}

    }

    @GetMapping("/{accountId}")
    public Account getAccount(@PathVariable String accountId) {
    	Account a = getAccountDetail(accountId);
        if(checkLoggedIn(a.getCustId())) {
        	return a;
    	}else {
    		throw new RuntimeException("Customer Not logged in"); 
    	}
    }

    @PostMapping("/{customerId}/{accountId}/deposit")
    public Account deposit(@PathVariable String customerId,@PathVariable String accountId, @RequestBody Map<String, Double> request) {
    	Double amount = request.get("amount");
        if(checkLoggedIn(customerId) && amount>100 ) {
            return accountService.deposit(accountId, amount);
    	}else {
    		throw new RuntimeException("Customer Not logged in Or Amount is less than 100"); 
    	}
    }

    @PostMapping("/{customerId}/{accountId}/withdraw")
    public Account withdraw(@PathVariable String customerId,@PathVariable String accountId, @RequestBody Map<String, Double> request) {
        if(checkLoggedIn(customerId)) {
        	Double amount = request.get("amount");
            return accountService.withdraw(accountId, amount);
    	}else {
    		throw new RuntimeException("Customer Not logged in"); 
    	}
    }
    
    
    @PostMapping("/transfer")
    public void transferAmount(@RequestBody Map<String, Object> request) {
    	if(accountService.getAccount((String)request.get("fromAccId")).get().getBalance() >= (Double)request.get("amount")) {
    		accountService.withdraw((String)request.get("fromAccId"),(Double)request.get("amount"));
    		accountService.deposit((String)request.get("toAccId"),(Double)request.get("amount"));
    	}
    }
    
    @GetMapping("/getAllAccounts")
    public List<Account> getAllAccounts() {
    	return accountService.getAllAccounts();
    }
    
}