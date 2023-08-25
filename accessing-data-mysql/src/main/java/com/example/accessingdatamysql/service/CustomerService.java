package com.example.accessingdatamysql.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.example.accessingdatamysql.model.Account;
import com.example.accessingdatamysql.model.Customer;
import com.example.accessingdatamysql.repository.AccountRepository;
import com.example.accessingdatamysql.repository.CustomerRepository;

@Service
public class CustomerService {

	 	@Autowired
	    private CustomerRepository customerRepository;

	    public Customer createCustomer(Customer c) {
	        return customerRepository.save(c);
	    }

	    public Optional<Customer> getCustomer(String id) {
	        return customerRepository.findById(id);
	    }

	    public void deleteCustomer(String id) {
	        customerRepository.deleteById(id);
	    }
	    
	    
	    public void updateCustomer(String id, Customer newCust) {
	    	Optional<Customer> c = customerRepository.findById(id);
	    	
	    	c = Optional.ofNullable(newCust);
	    	
	    	customerRepository.save(c.get());
	    }
}
